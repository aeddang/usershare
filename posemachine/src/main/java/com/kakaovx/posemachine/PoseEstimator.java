package com.kakaovx.posemachine;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import static com.kakaovx.posemachine.Keypoints.NUM_EDGES;

public class PoseEstimator {
    private int height = 304;
    private int width = 304;
    private int outputStride = 8;

    private int height_map = 15;
    private int width_map = 15;

    private float height_pixel = 15;
    private float width_pixel = 15;

    private int numKeyPoints = 17;
    private int kLocalMaximumRadius = 1;
    private int kp_radius = 32;

    private boolean isPersonLab = true;

    private PoseTypes types = new PoseTypes();

    private ArrayList<Pair<Integer, Integer>> dirEdges = new ArrayList<>();
    private ArrayList<Pair<Integer, Integer>> dirReverseEdges = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> skeletonGraph = new ArrayList<>();
    private HashMap<Integer, ArrayList<Pair<Integer, Integer>>> iterativePath = new HashMap<>();

    private ArrayList<float[]> tempHeatmaps = new ArrayList<>();

    private PoseNorm poseNorm = null;

    public PoseEstimator(int height, int width, int outputStride, int numKeyPoints, int kLocalMaximumRadius, boolean isPersonLab) {
        this.height = height;
        this.width = width;
        this.outputStride = outputStride;

        this.height_map = (height - 1) / outputStride + 1;
        this.width_map = (width - 1) / outputStride + 1;

        this.height_pixel = height / (float)height_map;
        this.width_pixel = width / (float)width_map;

        this.numKeyPoints = numKeyPoints;
        this.kLocalMaximumRadius = kLocalMaximumRadius;

        this.poseNorm = new PoseNorm();

        this.isPersonLab = isPersonLab;

        makeDirEdges();
        makeDirReverseEdges();
        makeSkeletonGraph();
        makeIterativePath();
    }

    private float[] popTempHeatmaps() {
        if (tempHeatmaps.isEmpty()) {
            return new float[height_map * width_map * numKeyPoints];
        }

        float[] heatmaps = tempHeatmaps.get(0);
        tempHeatmaps.remove(0);
        return heatmaps;
    }

    private void pushTempHeatmaps(float[] maps) {
        tempHeatmaps.add(maps);
    }

    private void makeDirEdges() {
        dirEdges.clear();

        for (int i = 0; i < NUM_EDGES; i++) {
            dirEdges.add(Pair.create(Keypoints.edges[i][0], Keypoints.edges[i][1]));
        }

        if (isPersonLab) {
            for (int i = 0; i < NUM_EDGES; i++) {
                dirEdges.add(Pair.create(Keypoints.edges[i][1], Keypoints.edges[i][0]));
            }
        }
    }

    private void makeDirReverseEdges() {
        dirReverseEdges.clear();

        for (int i = 0; i < NUM_EDGES; i++) {
            dirReverseEdges.add(Pair.create(Keypoints.edges[i][1], Keypoints.edges[i][0]));
        }
    }

    private float getHeatMapPoint(float[] heatmaps, int y, int x, int kp) {
        return heatmaps[y * (width_map * numKeyPoints) + x * numKeyPoints + kp];
    }
    private float getPafMapPoint(float[] pafmaps, int y, int x, int edge) {
        return pafmaps[y * (width_map * NUM_EDGES * 2) + x * NUM_EDGES * 2 + edge];
    }

    private void makeSkeletonGraph() {
        skeletonGraph.clear();
        for (int i = 0; i < Keypoints.NUM_KEYPOINTS; i++) {
            skeletonGraph.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < Keypoints.NUM_KEYPOINTS; i++) {
            for (int j = 0; j < Keypoints.NUM_KEYPOINTS; j++) {
                for (int k = 0; k < NUM_EDGES; k++) {
                    if (Keypoints.edges[k][0] == i && Keypoints.edges[k][1] == j || Keypoints.edges[k][1] == i && Keypoints.edges[k][0] == j) {
                        skeletonGraph.get(i).add(j);
                        skeletonGraph.get(j).add(i);
                    }
                }
            }
        }
    }

    private boolean scoreIsMaximumInLocalWindow(int keypointId, float score, int heatmapY, int heatmapX, int localMaximumRadius, float[] heatmaps) {
        boolean localMaximum = true;

        int yStart = Math.max(heatmapY - localMaximumRadius, 0);
        int yEnd = Math.min(heatmapY + localMaximumRadius + 1, height_map);
        for (int yCurrent = yStart; yCurrent < yEnd; ++yCurrent) {
            int xStart = Math.max(heatmapX - localMaximumRadius, 0);
            int xEnd = Math.min(heatmapX + localMaximumRadius + 1, width_map);
            for (int xCurrent = xStart; xCurrent < xEnd; ++xCurrent) {
                if (getHeatMapPoint(heatmaps, yCurrent, xCurrent, keypointId) > score) {
                    localMaximum = false;
                    break;
                }
            }

            if (localMaximum == false)
                break;
        }

        return localMaximum;
    }

    private PriorityQueue<PoseTypes.PartWithScore> buildPartWithScoreQueue(float scoreThreshold, int localMaximumRadius, float[] heatmaps, float[] shortOffsets) {
        PriorityQueue<PoseTypes.PartWithScore> queue = new PriorityQueue<>(15 * numKeyPoints, new Comparator<PoseTypes.PartWithScore>() {
            @Override
            public int compare(PoseTypes.PartWithScore o1, PoseTypes.PartWithScore o2) {
                if (o1.score > o2.score)
                    return -1;
                else if (o1.score < o2.score)
                    return 1;
                return 0;
            }
        });

        for (int heatmapY = 0; heatmapY < height_map; ++heatmapY) {
            for (int heatmapX = 0; heatmapX < width_map; ++heatmapX) {
                for (int keypointId = 0; keypointId < numKeyPoints; ++keypointId) {
                    float score = getHeatMapPoint(heatmaps, heatmapY, heatmapX, keypointId);

                    // Only consider parts with score greater or equal to threshold as root candidates.
                    if (score < scoreThreshold)
                        continue;

                    // Only consider keypoints whose score is maximum in a local window.
                    if (scoreIsMaximumInLocalWindow(keypointId, score, heatmapY, heatmapX, localMaximumRadius, heatmaps)) {
                        PoseTypes.PartWithScore partWithScore = types.new PartWithScore(score, types.new Part(heatmapX, heatmapY, keypointId), types.new Vector2D(0, 0));
                        partWithScore.imagePos = getImageCoords(partWithScore.part, outputStride, shortOffsets);
                        queue.add(partWithScore);
                    }
                }
            }
        }

        return queue;
    }

    private PriorityQueue<PoseTypes.PartWithScore> buildPartWithScoreQueue2(float scoreThreshold, int localMaximumRadius, float[] heatmaps, float[] shortOffsets) {
        PriorityQueue<PoseTypes.PartWithScore> queue = new PriorityQueue<>(5 * numKeyPoints, new Comparator<PoseTypes.PartWithScore>() {
            @Override
            public int compare(PoseTypes.PartWithScore o1, PoseTypes.PartWithScore o2) {
                if (o1.score > o2.score)
                    return -1;
                else if (o1.score < o2.score)
                    return 1;
                return 0;
            }
        });

        for (int heatmapY = 0; heatmapY < height_map; ++heatmapY) {
            for (int heatmapX = 0; heatmapX < width_map; ++heatmapX) {
                for (int keypointId = 0; keypointId < numKeyPoints; ++keypointId) {
                    float score = heatmaps[heatmapY * (width_map * numKeyPoints) + heatmapX * numKeyPoints + keypointId];

                    // Only consider parts with score greater or equal to threshold as root candidates.
                    if (score < scoreThreshold)
                        continue;

                    PoseTypes.PartWithScore partWithScore = types.new PartWithScore(score, types.new Part(heatmapX, heatmapY, keypointId), types.new Vector2D(0, 0));
                    partWithScore.imagePos = getImageCoords(partWithScore.part, outputStride, shortOffsets);
                    queue.add(partWithScore);
                }
            }
        }

        return queue;
    }

    private PoseTypes.Vector2DF getPafmapsPointMax(int midIndex, float[] pafmaps) {
        float max_x = 0;
        float max_y = 0;

        for (int y = 0; y < height_map; y++) {
            for (int x = 0; x < width_map; x++) {
                float _x = getPafMapPoint(pafmaps, y, x, midIndex * 2);
                float _y = getPafMapPoint(pafmaps, y, x, midIndex * 2 + 1);

                if (max_x < _x)
                    max_x = _x;
                if (max_y < _y)
                    max_y = _y;
            }
        }

        return types.new Vector2DF(max_y, max_x);
    }

    private PoseTypes.Vector2DF getPafmapsPoint(int y, int x, int midIndex, float[] pafmaps) {
        float paf_x = getPafMapPoint(pafmaps, y, x, midIndex * 2);
        float paf_y = getPafMapPoint(pafmaps, y, x, midIndex * 2 + 1);

        return types.new Vector2DF(paf_y, paf_x);
    }

    private PoseTypes.Vector2DF getShortOffsetPoint(int y, int x, int keypoint, float[] offsets) {
        int arrayIndex = y * (width_map * numKeyPoints * 2) + x * numKeyPoints * 2 + keypoint * 2;
        return types.new Vector2DF(offsets[arrayIndex + 1], offsets[arrayIndex]);
    }

    private PoseTypes.Vector2DF getMidOffsetPoint(int y, int x, int midIndex, float[] offsets) {
        int arrayIndex = y * (width_map * NUM_EDGES * 4) + x * NUM_EDGES * 4 + midIndex * 2;
        return types.new Vector2DF(offsets[arrayIndex + 1], offsets[arrayIndex]);
    }

    private PoseTypes.Vector2D getImageCoords(PoseTypes.Part part, int outputStride, float[] shortOffsets) {
        PoseTypes.Vector2DF offsetPoint = getShortOffsetPoint(part.heatmapY, part.heatmapX, part.id, shortOffsets);

        return types.new Vector2D((int)(part.heatmapY * height_pixel + height_pixel / 2 + offsetPoint.y), (int)(part.heatmapX * width_pixel + width_pixel / 2 + offsetPoint.x));
    }

    private float squaredDistance(float y1, float x1, float y2, float x2) {
        float dy = y2 - y1;
        float dx = x2 - x1;
        return dy * dy + dx * dx;
    }

    private boolean withinNmsRadiusOfCorrespondingPoint(ArrayList<float[][]> poses, int nmsRadius, PoseTypes.Vector2D rootImageCoords, int keypointId) {
        if (poses == null)
            return false;

        for (int i = 0; i < poses.size(); i++) {
            float[][] poseData = poses.get(i);
            if (poseData[keypointId] == null)
                continue;

            if (Utils.distance2D(poseData[keypointId][1], rootImageCoords.y, poseData[keypointId][0], rootImageCoords.x) <= nmsRadius)
                return true;
        }

        return false;
    }

    private float getInstanceScore(ArrayList<PoseTypes.Pose> existingPoses, int squaredNmsRadius, PoseTypes.Keypoint[] instanceKeypoints) {
        return 1.0f;
    }

    private ArrayList<Pair<Integer, Integer>> iterativeBfs(int keypointId) {
        ArrayList<Pair<Integer, Integer>> q = new ArrayList<>();
        q.add(Pair.create(-1, keypointId));

        ArrayList<Integer> visited = new ArrayList<>();

        ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();

        while (q.isEmpty() == false) {
            Pair<Integer, Integer> v = q.remove(0);
            if (visited.contains(v.second) == false) {
                visited.add(v.second);
                path.add(v);

                for (int i = 0; i < skeletonGraph.get(v.second).size(); ++i) {
                    q.add(Pair.create(v.second, skeletonGraph.get(v.second).get(i)));
                }
            }
        }

        return path;
    }

    private void makeIterativePath() {
        iterativePath.clear();

        for (int i = 0; i < Keypoints.NUM_KEYPOINTS; i++) {
            ArrayList<Pair<Integer, Integer>> path = iterativeBfs(i);
            path.remove(0);

            iterativePath.put(i, path);
        }
    }

    private float getCellCenterPosX(float x) {
        return ((int) (x / width_pixel) * width_pixel + width_pixel / 2);
    }

    private float getCellCenterPosY(float y) {
        return ((int) (y / height_pixel) * height_pixel + height_pixel / 2);
    }

    private int limit(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private float[] computeHeatmaps(float[] heatmaps, float[] shortOffsets, float scoreThreshold, int votingRadius) {
        float[] output = popTempHeatmaps();
        Arrays.fill(output, 0);

        for (int heatmapY = 0; heatmapY < height_map; ++heatmapY) {
            for (int heatmapX = 0; heatmapX < width_map; ++heatmapX) {
                for (int keypointId = 0; keypointId < numKeyPoints; ++keypointId) {
                    float score = heatmaps[heatmapY * (width_map * numKeyPoints) + heatmapX * numKeyPoints + keypointId];

                    // Only consider parts with score greater or equal to threshold as root candidates.
                    if (score < scoreThreshold)
                        continue;

                    // voting short range offsets
                    float vote = 0;

                    int yStart = Math.max(heatmapY - votingRadius, 0);
                    int yEnd = Math.min(heatmapY + votingRadius + 1, height_map);
                    int xStart = Math.max(heatmapX - votingRadius, 0);
                    int xEnd = Math.min(heatmapX + votingRadius + 1, width_map);

                    for (int yCurrent = yStart; yCurrent < yEnd; ++yCurrent) {
                        for (int xCurrent = xStart; xCurrent < xEnd; ++xCurrent) {
                            PoseTypes.Vector2DF shortOffset = getShortOffsetPoint(yCurrent, xCurrent, keypointId, shortOffsets);
                            shortOffset.y += (height_pixel * yCurrent) + height_pixel / 2;
                            shortOffset.x += (width_pixel * xCurrent) + width_pixel / 2;

                            int[] cellIdx = {limit((int) (shortOffset.y / height_pixel), 0, height_map - 1), limit((int) (shortOffset.x / width_pixel), 0, width_map - 1)};
                            if (cellIdx[0] == heatmapY && cellIdx[1] == heatmapX)
                                vote += heatmaps[yCurrent * (width_map * numKeyPoints) + xCurrent * numKeyPoints + keypointId];
                        }
                    }

                    output[heatmapY * (width_map * numKeyPoints) + heatmapX * numKeyPoints + keypointId] = vote / (float) ((votingRadius + 2) * (votingRadius + 2)) * score;
                }
            }
        }

        return output;
    }

    private float[] houghVote(float[] heatmaps, float[] shortOffsets) {
        float[] output = popTempHeatmaps();
        Arrays.fill(output, 0);

        int cell_half_size_y = (int)(height_pixel / 2);
        int cell_half_size_x = (int)(width_pixel / 2);

        double kp_maps_r2 = Math.pow(kp_radius / height_pixel, 2);

        for (int heatmapY = 0; heatmapY < height_map; ++heatmapY) {
            for (int heatmapX = 0; heatmapX < width_map; ++heatmapX) {
                for (int keypointId = 0; keypointId < numKeyPoints; ++keypointId) {
                    int shortArrayIndex = heatmapY * (width_map * numKeyPoints * 2) + heatmapX * numKeyPoints * 2 + keypointId * 2;

                    int cell_center_x = (int)(width_pixel * heatmapX + cell_half_size_x + shortOffsets[shortArrayIndex]);
                    int cell_center_y = (int)(height_pixel * heatmapY + cell_half_size_y + shortOffsets[shortArrayIndex + 1]);

                    int[] cellIdx = {limit((int) (cell_center_y / height_pixel), 0, height_map - 1), limit((int) (cell_center_x / width_pixel), 0, width_map - 1)};

                    float score = heatmaps[heatmapY * (width_map * numKeyPoints) + heatmapX * numKeyPoints + keypointId];
                    output[cellIdx[0] * (width_map * numKeyPoints) + cellIdx[1] * numKeyPoints + keypointId] += (float)(score / kp_maps_r2);
                }
            }
        }

        return output;
    }

    private void deleteLackingPoses(ArrayList<float[][]> poses, int minKeyPoints) {
        for (int i = 0; i < poses.size(); i++) {

            int numSkeleton = 0;
            float[][] skeletons = poses.get(i);
            for (int j = 0; j < skeletons.length; j++) {
                if (skeletons[j][2] > 0)
                    numSkeleton++;
            }

            if (numSkeleton >= minKeyPoints)
                continue;

            poses.remove(i);
            --i;
        }
    }

    private float pafScore(float[] _pafmaps, int mid_idx, int[] from_kp, int[] to_kp, long step_count, float threshold_score, long valid_threshold_count) {
        if (_pafmaps == null || mid_idx < 0 || mid_idx >= NUM_EDGES || from_kp == null || to_kp == null || step_count <= 0 || valid_threshold_count <= 0)
            return -1f;

        float[] vec = {
                to_kp[0] - from_kp[0],
                to_kp[1] - from_kp[1]
        };
        float norm = (float)Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1]);
        if (norm < 0.00000000001f)
            return -1f;

        float step_x = vec[0] / step_count;
        float step_y = vec[1] / step_count;

        vec[0] /= norm;
        vec[1] /= norm;

        float scores = 0;
        long valid_count = 0;

        for (int i = 0; i < step_count; i++) {
            int x = Math.round(from_kp[0] + i * step_x);
            int y = Math.round(from_kp[1] + i * step_y);

            int[] cellIdx = { limit((int)(y / height_pixel), 0, height_map - 1), limit((int)(x / width_pixel), 0, width_map - 1) };

            PoseTypes.Vector2DF paf = getPafmapsPoint(cellIdx[0], cellIdx[1], mid_idx, _pafmaps);

            float score = vec[0] * paf.x + vec[1] * paf.y;
            scores += score;

            if (score > threshold_score)
                valid_count++;
        }

        float criterion = scores / step_count;

        if (valid_count < valid_threshold_count || criterion <= threshold_score)
            return -1f;

        return criterion;
    }

    private ArrayList<float[][]> decodeOpenPose(float[] _heatmaps, float[] _pafmaps, float[] shortOffsets, int maxPoseDetections, float scoreThreshold, int nmsRadius, int kpRadius, int minKeyPoints) {
        ArrayList<float[][]> poses = new ArrayList<>();

        //float[] heatmaps = computeHeatmaps(_heatmaps, shortOffsets, scoreThreshold, kLocalMaximumRadius);
        //float[] heatmaps = Utils.minMaxFilter(_heatmaps, popTempHeatmaps(), width_map, height_map, numKeyPoints, 3, true, true);
        //float[] heatmaps = Utils.minMaxFilter(houghVote(_heatmaps, shortOffsets), popTempHeatmaps(), width_map, height_map, numKeyPoints, 3, true, true);

        PriorityQueue<PoseTypes.PartWithScore> queue = buildPartWithScoreQueue(scoreThreshold, kLocalMaximumRadius, _heatmaps, shortOffsets);
        if (queue == null) {
            //pushTempHeatmaps(heatmaps);
            return poses;
        }

        //int squaredNmsRadius = nmsRadius * nmsRadius;
        int squaredNmsRadius = nmsRadius;
        int[] from_kp = new int[2];
        int[] to_kp = new int[2];

        while (poses.size() < maxPoseDetections && queue.size() > 0) {
            PoseTypes.PartWithScore root = queue.poll();

            PoseTypes.Vector2D rootImageCoords = root.imagePos;
            if (withinNmsRadiusOfCorrespondingPoint(poses, squaredNmsRadius, rootImageCoords, root.part.id))
                continue;

            float[][] thisSkeleton = new float[Keypoints.NUM_KEYPOINTS][3];
            thisSkeleton[root.part.id][0] = rootImageCoords.x;
            thisSkeleton[root.part.id][1] = rootImageCoords.y;
            thisSkeleton[root.part.id][2] = root.score;

            ArrayList<Pair<Integer, Integer>> path = iterativePath.get(root.part.id);
            for (Pair<Integer, Integer> edge : path) {
                if (thisSkeleton[edge.first][2] == 0)
                    continue;

                boolean inversed = false;

                int mid_idx = dirEdges.indexOf(edge);
                if (mid_idx < 0) {
                    inversed = true;
                    mid_idx = dirReverseEdges.indexOf(edge);
                }
                if (mid_idx < 0)
                    continue;

                float max_score = -10000;

                from_kp[0] = Math.round(thisSkeleton[edge.first][0]);
                from_kp[1] = Math.round(thisSkeleton[edge.first][1]);

                PoseTypes.PartWithScore maxScorePart = null;
                for (PoseTypes.PartWithScore queued : queue) {
                    if (queued.part.id != edge.second)
                        continue;

                    to_kp[0] = queued.imagePos.x;
                    to_kp[1] = queued.imagePos.y;

                    float score = pafScore(_pafmaps, mid_idx,
                            (inversed == false) ? from_kp : to_kp,
                            (inversed == false) ? to_kp : from_kp,
                            10, 0.3f, 8);
                    if (score <= 0)
                        continue;

                    if (max_score < score) {
                        max_score = score;
                        maxScorePart = queued;
                    }
                }

                if (maxScorePart != null) {
                    queue.remove(maxScorePart);

                    thisSkeleton[edge.second][0] = Math.round(maxScorePart.imagePos.x);
                    thisSkeleton[edge.second][1] = Math.round(maxScorePart.imagePos.y);
                    thisSkeleton[edge.second][2] = maxScorePart.score;
                }
            }

            poses.add(thisSkeleton);
        }

        //pushTempHeatmaps(heatmaps);

        deleteLackingPoses(poses, minKeyPoints);

        return poses;
    }

    private ArrayList<float[][]> decodePersonLab(float[] _heatmaps, float[] shortOffsets, float[] midOffsets, int maxPoseDetections, float scoreThreshold, int nmsRadius, int kpRadius, int minKeyPoints) {
        ArrayList<float[][]> poses = new ArrayList<>();

        //float[] heatmaps = computeHeatmaps(_heatmaps, shortOffsets, scoreThreshold, kLocalMaximumRadius);
        //float[] heatmaps = Utils.minMaxFilter(_heatmaps, popTempHeatmaps(), width_map, height_map, numKeyPoints, 3, true, true);
        //float[] heatmaps = Utils.minMaxFilter(houghVote(_heatmaps, shortOffsets), popTempHeatmaps(), width_map, height_map, numKeyPoints, 3, true, true);

        PriorityQueue<PoseTypes.PartWithScore> queue = buildPartWithScoreQueue(scoreThreshold, kLocalMaximumRadius, _heatmaps, shortOffsets);
        //PriorityQueue<PoseTypes.PartWithScore> queue = buildPartWithScoreQueue(scoreThreshold, kLocalMaximumRadius, heatmaps, shortOffsets);
        if (queue == null) {
            //pushTempHeatmaps(heatmaps);
            return poses;
        }

        while (poses.size() < maxPoseDetections && queue.size() > 0) {
            PoseTypes.PartWithScore root = queue.poll();

            PoseTypes.Vector2D rootImageCoords = root.imagePos;
            if (withinNmsRadiusOfCorrespondingPoint(poses, nmsRadius, rootImageCoords, root.part.id))
                continue;

            float[][] thisSkeleton = new float[Keypoints.NUM_KEYPOINTS][3];
            thisSkeleton[root.part.id][0] = rootImageCoords.x;
            thisSkeleton[root.part.id][1] = rootImageCoords.y;
            thisSkeleton[root.part.id][2] = root.score;

            ArrayList<Pair<Integer, Integer>> path = iterativePath.get(root.part.id);
            for (Pair<Integer, Integer> edge : path) {
                if (thisSkeleton[edge.first][2] == 0)
                    continue;

                int mid_idx = dirEdges.indexOf(edge);
                if (mid_idx < 0)
                    continue;

                int to_keypoint = edge.second;
                int[] from_keypoint = new int[] { Math.round(thisSkeleton[edge.first][0]), Math.round(thisSkeleton[edge.first][1]) };

                int[] outputIdx = { limit((int)(from_keypoint[1] / height_pixel), 0, height_map - 1), limit((int)(from_keypoint[0] / width_pixel), 0, width_map - 1) };

                PoseTypes.Vector2DF midOffset = getMidOffsetPoint(outputIdx[0], outputIdx[1], mid_idx, midOffsets);
                PoseTypes.Vector2DF proposal = midOffset;
                proposal.x += getCellCenterPosX(thisSkeleton[edge.first][0]);
                proposal.y += getCellCenterPosY(thisSkeleton[edge.first][1]);

                for (int i = 0; i < 1; i++) {
                    int[] proposalIdx = {limit((int) (proposal.y / height_pixel), 0, height_map - 1), limit((int) (proposal.x / width_pixel), 0, width_map - 1)};

                    PoseTypes.Vector2DF shortOffset = getShortOffsetPoint(proposalIdx[0], proposalIdx[1], to_keypoint, shortOffsets);
                    proposal.x = getCellCenterPosX(proposal.x) + shortOffset.x;
                    proposal.y = getCellCenterPosY(proposal.y) + shortOffset.y;
                }

                /*
                float maxConf = 0;
                PoseTypes.PartWithScore maxConfPart = null;
                for (PoseTypes.PartWithScore queued : queue) {
                    if (queued.part.id != edge.second)
                        continue;

                    float dist = (float)Utils.distance2D(queued.imagePos.x, proposal.x, queued.imagePos.y, proposal.y);
                    if (dist > kpRadius ||  maxConf > queued.score)
                        continue;

                    maxConf = queued.score;
                    maxConfPart = queued;
                }

                if (maxConfPart != null) {
                    queue.remove(maxConfPart);

                    thisSkeleton[edge.second][0] = Math.round(maxConfPart.imagePos.x);
                    thisSkeleton[edge.second][1] = Math.round(maxConfPart.imagePos.y);
                    thisSkeleton[edge.second][2] = maxConfPart.score;
                }
                */

                float minSquaredDistance = 3000;
                PoseTypes.PartWithScore minDistancePart = null;
                for (PoseTypes.PartWithScore queued : queue) {
                    if (queued.part.id != edge.second)
                        continue;

                    float dist = (float)Utils.distance2D(queued.imagePos.x, proposal.x, queued.imagePos.y, proposal.y);
                    if (dist > kpRadius ||  minSquaredDistance <= dist)
                        continue;

                    minSquaredDistance = dist;
                    minDistancePart = queued;
                }

                if (minDistancePart != null) {
                    queue.remove(minDistancePart);

                    thisSkeleton[edge.second][0] = Math.round(minDistancePart.imagePos.x);
                    thisSkeleton[edge.second][1] = Math.round(minDistancePart.imagePos.y);
                    thisSkeleton[edge.second][2] = minDistancePart.score;
                }
            }

            poses.add(thisSkeleton);
        }

        //pushTempHeatmaps(heatmaps);

        deleteLackingPoses(poses, minKeyPoints);

        return poses;
    }

    public ArrayList<float[][]> decodePoses(ArrayList<float[]> outputs, int maxPoseDetections, float scoreThreshold, int nmsRadius, int kpRadius, int minKeyPoints) {
        if (isPersonLab) {
            return decodePersonLab(outputs.get(0), outputs.get(1), outputs.get(2), maxPoseDetections, scoreThreshold, nmsRadius, kpRadius, minKeyPoints);
        }
        else {
            return decodeOpenPose(outputs.get(0), outputs.get(1), outputs.get(2), maxPoseDetections, scoreThreshold, nmsRadius, kpRadius, minKeyPoints);
        }
    }

    /*
    public PoseTypes.Vector2DF getMidOffset(float[] midOffsets, int srcKeypoint, float srcX, float srcY, int destKeypoint) {
        int mid_idx = dirEdges.indexOf(Pair.create(srcKeypoint, destKeypoint));
        if (mid_idx < 0)
            return null;

        int[] outputIdx = { limit((int)(srcY / height_pixel), 0, height_map - 1), limit((int)(srcX / width_pixel), 0, width_map - 1) };

        return getMidOffsetPoint(outputIdx[0], outputIdx[1], mid_idx, midOffsets);
    }
    */

    public PoseTypes.Vector2DF getShortOffset(float[] shortOffsets, int keypoint, float posX, float posY) {
        int[] outputIdx = { limit((int)(posY / height_pixel), 0, height_map - 1), limit((int)(posX / width_pixel), 0, width_map - 1) };

        float offsetX = getCellCenterPosX(posX) - posX;
        float offsetY = getCellCenterPosY(posY) - posY;

        PoseTypes.Vector2DF offsetPos = getShortOffsetPoint(outputIdx[0], outputIdx[1], keypoint, shortOffsets);
        offsetPos.x += offsetX;
        offsetPos.y += offsetY;

        return offsetPos;
    }

    public ArrayList<float[][]> selectCenterPose(ArrayList<float[][]> poses, int imageWidth, int imageHeight) {
        if (poses.size() <= 0)
            return null;

        float minDistance = 100000;
        int minIndex = -1;

        for (int index = 0; index < poses.size(); index++) {
            float[][] pose = poses.get(index);

            // get center, width, height
            float left = -1000, right = -1000, top = -1000, bottom = -1000;
            for (int i = 0; i < Keypoints.NUM_KEYPOINTS; i++) {
                if (pose[i].length != 3)
                    break;

                if (pose[i][2] <= 0)
                    continue;

                if (left < 0 || pose[i][0] < left)
                    left = pose[i][0];
                if (right < 0 || pose[i][0] > right)
                    right = pose[i][0];
                if (top < 0 || pose[i][1] < top)
                    top = pose[i][1];
                if (bottom < 0 || pose[i][1] > bottom)
                    bottom = pose[i][1];
            }

            float width = right - left;
            float height = bottom - top;

            float[] center = new float[2];
            center[0] = left + width / 2;
            center[1] = top + height / 2;

            // box move and resize
            float box_size = Math.max(width, height);

            float distance = Math.abs(center[0] - imageWidth / 2);
            if (distance >= minDistance)
                continue;

            minDistance = distance;
            minIndex = index;
        }

        ArrayList<float[][]> centerPoses = new ArrayList<float[][]>();
        centerPoses.add(poses.get(minIndex));
        return centerPoses;
    }

    public double getSimilarity(float[][] sourcePose, float[][] targetPose, boolean flip, boolean ignoreFace) {
        return poseNorm.cosineSimilarity(sourcePose, targetPose, flip, ignoreFace);
    }

    public double getSimilarity(List<PoseData.FrameData> frameDatas, float[][] targetPose, boolean flip, boolean ignoreFace) {
        return poseNorm.cosineSimilarity(frameDatas, targetPose, flip, ignoreFace);
    }

    public double getVerticalSimilarity(List<PoseData.FrameData> frameDatas, float[][] targetPose, boolean flip) {
        return poseNorm.verticalSimilarity2(frameDatas, targetPose, flip);
    }
}
