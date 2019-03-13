package com.kakaovx.posemachine;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.kakaovx.posemachine.Keypoints.NUM_KEYPOINTS;
import static com.kakaovx.posemachine.Keypoints.KeypointPart;

public class PoseNorm {
    private int[][] skeletonLengthPair = {
            { KeypointPart.LeftShoulder, KeypointPart.RightShoulder },
            { KeypointPart.LeftHip, KeypointPart.RightHip },
            { KeypointPart.LeftShoulder, KeypointPart.LeftElbow },
            { KeypointPart.LeftElbow, KeypointPart.LeftWrist },
            { KeypointPart.RightShoulder, KeypointPart.RightElbow },
            { KeypointPart.RightElbow, KeypointPart.RightWrist },
            { KeypointPart.LeftHip, KeypointPart.LeftKnee },
            { KeypointPart.LeftKnee, KeypointPart.LeftAnkle },
            { KeypointPart.RightHip, KeypointPart.RightKnee },
            { KeypointPart.RightKnee, KeypointPart.RightAnkle }
    };
    private double[] skeletonLengthRate = {
            1.43,
            2.5,
            1.89,
            2.5,
            1.89,
            2.5,
            1.43,
            1.43,
            1.43,
            1.43
    };

    public boolean Normalization(float[][] pose) {
        if (pose == null || pose.length != NUM_KEYPOINTS)
            return false;

        // get center, width, height
        float left = -1000, right = -1000, top = -1000, bottom = -1000;
        for (int i = 0; i < NUM_KEYPOINTS; i++) {
            if (pose[i].length != 3)
                return false;

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

        float[] offsets = new float[2];
        offsets[0] = center[0] - box_size / 2;
        offsets[1] = center[1] - box_size / 2;

        // resize (1 x 1) scale
        float scale = 1 / box_size;

        for (int i = 0; i < NUM_KEYPOINTS; i++) {
            if (pose[i][2] <= 0)
                continue;
            pose[i][0] -= offsets[0];
            pose[i][0] *= scale;
            pose[i][1] -= offsets[1];
            pose[i][1] *= scale;
        }

        // l2 normalization
        float sum = 0;
        int count = 0;
        for (int i = 0; i < NUM_KEYPOINTS; i++) {
            if (pose[i][2] <= 0)
                continue;
            sum += Math.exp(pose[i][0]) + Math.exp(pose[i][1]);
            count++;
        }
        float unit = (float)Math.sqrt(sum) / count;

        for (int i = 0; i < NUM_KEYPOINTS; i++) {
            if (pose[i][2] <= 0)
                continue;
            pose[i][0] /= unit;
            pose[i][1] /= unit;
        }

        return true;
    }

    public float getSpineLength(float[][] pose) {
        if (pose == null || pose.length != NUM_KEYPOINTS)
            return 0;

        double spineLength = 0;

        if (pose[KeypointPart.LeftShoulder][2] > 0 && pose[KeypointPart.RightShoulder][2] > 0 &&
                pose[KeypointPart.LeftHip][2] > 0 && pose[KeypointPart.RightHip][2] > 0) {
            Pair<Float, Float> shoulderMid = Utils.midPoint2D(pose[KeypointPart.LeftShoulder], pose[KeypointPart.RightShoulder]);
            Pair<Float, Float> hipMid = Utils.midPoint2D(pose[KeypointPart.LeftHip], pose[KeypointPart.RightHip]);

            double len = Utils.distance2D(shoulderMid.first, hipMid.first, shoulderMid.second, hipMid.second);
            if (len > spineLength)
                spineLength = len;
        }

        for (int i = 0; i < skeletonLengthPair.length; ++i) {
            if (pose[skeletonLengthPair[i][0]][2] <= 0 || pose[skeletonLengthPair[i][1]][2] <= 0)
                continue;

            double len = Utils.distance2D(pose[skeletonLengthPair[i][0]], pose[skeletonLengthPair[i][1]]) * skeletonLengthRate[i];
            if (len > spineLength)
                spineLength = len;
        }

        return (float)spineLength;
    }

    public float[][] encodePoseVertical(float[][] pose) {
        float spineLength = getSpineLength(pose);
        if (spineLength <= 0)
            return null;

        float[][] encoded = new float[skeletonLengthPair.length + 1][2];

        for (int i = 2; i < skeletonLengthPair.length; i++) {
            int part1 = skeletonLengthPair[i][0];
            int part2 = skeletonLengthPair[i][1];

            if (pose[part1][2] <= 0 || pose[part2][2] <= 0)
                continue;

            float avgConf = (pose[part1][2] + pose[part2][2]) / 2.f;
            float partLength = (float)(spineLength / skeletonLengthRate[i]);

            float diff = (pose[part1][1] - pose[part2][1]) / partLength;

            encoded[i][0] = diff;
            encoded[i][1] = avgConf;
        }

        if (pose[KeypointPart.LeftShoulder][2] > 0 && pose[KeypointPart.RightShoulder][2] > 0 &&
                pose[KeypointPart.LeftHip][2] > 0 && pose[KeypointPart.RightHip][2] > 0) {
            Pair<Float, Float> shoulderMid = Utils.midPoint2D(pose[KeypointPart.LeftShoulder], pose[KeypointPart.RightShoulder]);
            Pair<Float, Float> hipMid = Utils.midPoint2D(pose[KeypointPart.LeftHip], pose[KeypointPart.RightHip]);

            float avgConf = (pose[KeypointPart.LeftShoulder][2] +
                             pose[KeypointPart.RightShoulder][2] +
                             pose[KeypointPart.LeftHip][2] +
                             pose[KeypointPart.RightHip][2]) / 4.f;

            float diff = (shoulderMid.second - hipMid.second) / spineLength;

            encoded[skeletonLengthPair.length][0] = diff;
            encoded[skeletonLengthPair.length][1] = avgConf;
        }

        return encoded;
    }

    public float encodedPoseDiff(float[][] encode1, float[][] encode2) {
        if (encode1 == null || encode2 == null || encode1.length <= 0 || encode1.length != encode2.length)
            return -1;

        float sumConf = 0;
        float sumDiff = 0;
        for (int i = 0; i < encode1.length; ++i) {
            if (encode1[i][1] <= 0 || encode2[i][1] <= 0)
                continue;

            float avgConf = (encode1[i][1] + encode2[i][1]) / 2f;
            float diff = (float)Math.pow(Math.max(0, Math.abs(encode1[i][0] - encode2[i][0]) - 0.1), 2) * avgConf;

            sumConf += avgConf;
            sumDiff += diff;
        }

        if (sumDiff == 0 && sumConf == 0)
            return -1;

        return sumDiff / sumConf;
    }

    public float[][] flipPose(float[][] pose, boolean updateOrigin) {
        if (pose == null || pose.length <= 0 || pose[0] == null || pose[0].length <= 0)
            return null;

        float left = -1000, right = -1000, top = -1000, bottom = -1000;
        for (int i = 0; i < pose.length; i++) {
            if (pose[i].length != 3)
                return null;

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
    float centerX = left + width / 2;

    float[][] flippedPose = new float[pose.length][pose[0].length];

        for (int i = 0; i < pose.length; i++) {
        if (pose[i].length != 3)
            return null;

        float x = pose[Keypoints.flipKeypoints[i]][0];
        x -= (x - centerX) * 2;

        flippedPose[i][0] = x;
        flippedPose[i][1] = pose[Keypoints.flipKeypoints[i]][1];
        flippedPose[i][2] = pose[Keypoints.flipKeypoints[i]][2];
    }

        if (updateOrigin) {
        for (int i = 0; i < flippedPose.length; i++) {
            System.arraycopy(flippedPose[i], 0, pose[i], 0, flippedPose[i].length);
        }
    }

        return flippedPose;
    }

    // K(pose1, pose2) = <pose1 dot pose2> / (||pose1||*||pose2||)
    public double cosineSimilarity(float[][] pose1, float[][] pose2, boolean flip, boolean ignoreFace) {
        if (pose1 == null || pose2 == null || pose1.length <= 0 || pose2.length <= 0)
            return 0;

        float[][] flippedPose = null;
        if (flip)
            flippedPose = flipPose(pose2, false);

        Normalization(pose1);
        Normalization(pose2);

        double[] vector1 = new double[NUM_KEYPOINTS * 2];
        double[] vector2 = new double[NUM_KEYPOINTS * 2];

        for (int i = 0; i < NUM_KEYPOINTS; i++) {
            if (pose1[i][2] == 0 || pose2[i][2] == 0 ||
                    ignoreFace && Keypoints.isFaceKeypoint(i)) {
                vector1[i * 2] = 0;
                vector1[i * 2 + 1] = 0;
                vector2[i * 2] = 0;
                vector2[i * 2 + 1] = 0;

                continue;
            }

            vector1[i * 2] = pose1[i][0];
            vector1[i * 2 + 1] = pose1[i][1];
            vector2[i * 2] = pose2[i][0];
            vector2[i * 2 + 1] = pose2[i][1];
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }

        //return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));

        double cosineSimilarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        double distance = 2 * (1 - cosineSimilarity);
        double similarity = 1 - Math.sqrt(distance) * 2.5;
        if (similarity < 0) similarity = 0;

        if (flip) {
            Normalization(flippedPose);
            for (int i = 0; i < NUM_KEYPOINTS; i++) {
                if (pose1[i][2] == 0 || flippedPose[i][2] == 0 ||
                        ignoreFace && Keypoints.isFaceKeypoint(i)) {
                    vector2[i * 2] = 0;
                    vector2[i * 2 + 1] = 0;

                    continue;
                }

                vector2[i * 2] = flippedPose[i][0];
                vector2[i * 2 + 1] = flippedPose[i][1];
            }

            dotProduct = 0.0;
            norm1 = 0.0;
            norm2 = 0.0;

            for (int i = 0; i < vector1.length; i++) {
                dotProduct += vector1[i] * vector2[i];
                norm1 += Math.pow(vector1[i], 2);
                norm2 += Math.pow(vector2[i], 2);
            }

            //return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));

            cosineSimilarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
            distance = 2 * (1 - cosineSimilarity);
            double flipSimilarity = 1 - Math.sqrt(distance) * 2.5;
            if (flipSimilarity < 0) flipSimilarity = 0;
            if (similarity < flipSimilarity)
                similarity = flipSimilarity;
        }

        return similarity;
    }

    public double cosineSimilarity(List<PoseData.FrameData> frameDatas, float[][] pose2, boolean flip, boolean ignoreFace) {
        if (frameDatas == null || pose2 == null || frameDatas.size() <= 0 || pose2.length <= 0)
            return 0;

        float[][] flippedPose = null;
        if (flip) {
            flippedPose = flipPose(pose2, false);
            Normalization(flippedPose);
        }

        double similarity = 100;
        int minIndex = -1;

        for (int index = 0; index < frameDatas.size(); ++index) {
            if (frameDatas.get(index).isDetected)
                continue;

            float[][] pose1 = frameDatas.get(index).keypoints;

            Normalization(pose1);
            Normalization(pose2);

            double[] vector1 = new double[NUM_KEYPOINTS * 2];
            double[] vector2 = new double[NUM_KEYPOINTS * 2];

            for (int i = 0; i < NUM_KEYPOINTS; i++) {
                if (pose1[i][2] == 0 || pose2[i][2] == 0 ||
                        ignoreFace && Keypoints.isFaceKeypoint(i)) {
                    vector1[i * 2] = 0;
                    vector1[i * 2 + 1] = 0;
                    vector2[i * 2] = 0;
                    vector2[i * 2 + 1] = 0;

                    continue;
                }

                vector1[i * 2] = pose1[i][0];
                vector1[i * 2 + 1] = pose1[i][1];
                vector2[i * 2] = pose2[i][0];
                vector2[i * 2 + 1] = pose2[i][1];
            }

            double dotProduct = 0.0;
            double norm1 = 0.0;
            double norm2 = 0.0;

            for (int i = 0; i < vector1.length; i++) {
                dotProduct += vector1[i] * vector2[i];
                norm1 += Math.pow(vector1[i], 2);
                norm2 += Math.pow(vector2[i], 2);
            }

            //return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));

            double cosineSimilarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
            double distance = 2 * (1 - cosineSimilarity);
            double _similarity = 1 - Math.sqrt(distance) * 2.5;
            if (_similarity < 0) _similarity = 0;

            if (flip) {
                for (int i = 0; i < NUM_KEYPOINTS; i++) {
                    if (pose1[i][2] == 0 || flippedPose[i][2] == 0 ||
                            ignoreFace && Keypoints.isFaceKeypoint(i)) {
                        vector2[i * 2] = 0;
                        vector2[i * 2 + 1] = 0;

                        continue;
                    }

                    vector2[i * 2] = flippedPose[i][0];
                    vector2[i * 2 + 1] = flippedPose[i][1];
                }

                dotProduct = 0.0;
                norm1 = 0.0;
                norm2 = 0.0;

                for (int i = 0; i < vector1.length; i++) {
                    dotProduct += vector1[i] * vector2[i];
                    norm1 += Math.pow(vector1[i], 2);
                    norm2 += Math.pow(vector2[i], 2);
                }

                //return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));

                cosineSimilarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
                distance = 2 * (1 - cosineSimilarity);
                double flipSimilarity = 1 - Math.sqrt(distance) * 2.5;
                if (flipSimilarity < 0) flipSimilarity = 0;
                if (_similarity < flipSimilarity)
                    _similarity = flipSimilarity;
            }

            if (similarity > _similarity) {
                similarity = _similarity;
                minIndex = index;
            }
        }

        if (minIndex >= 0) {
            frameDatas.get(minIndex).isDetected = true;
        }

        return similarity;
    }

    private double getBaseUnitLength(float[][] pose) {
        double shoulderLength = Utils.distance2D(pose[KeypointPart.LeftShoulder], pose[KeypointPart.RightShoulder]);

        Pair<Float, Float> shoulderMid = Utils.midPoint2D(pose[KeypointPart.LeftShoulder], pose[KeypointPart.RightShoulder]);
        Pair<Float, Float> hipMid = Utils.midPoint2D(pose[KeypointPart.LeftHip], pose[KeypointPart.RightHip]);

        double spineLength = Utils.distance2D(shoulderMid.first, hipMid.first, shoulderMid.second, hipMid.second);

        return Math.max(shoulderLength * 1.3, spineLength);
    }

    private double[] getVerticalOffsets(float[][] pose) {
        // check shoulders, hips
        if (pose[KeypointPart.LeftShoulder][2] <= 0 ||
                pose[KeypointPart.RightShoulder][2] <= 0 ||
                pose[KeypointPart.LeftHip][2] <= 0 ||
                pose[KeypointPart.RightHip][2] <= 0)
            return null;

        double shoulderLength = Utils.distance2D(pose[KeypointPart.LeftShoulder], pose[KeypointPart.RightShoulder]);

        Pair<Float, Float> shoulderMid = Utils.midPoint2D(pose[KeypointPart.LeftShoulder], pose[KeypointPart.RightShoulder]);
        Pair<Float, Float> hipMid = Utils.midPoint2D(pose[KeypointPart.LeftHip], pose[KeypointPart.RightHip]);

        double spineLength = Utils.distance2D(shoulderMid.first, hipMid.first, shoulderMid.second, hipMid.second);

        double baseUnitLength = Math.max(shoulderLength * 1.3, spineLength);

        double[] offsets = new double[NUM_KEYPOINTS * 2];

        // upper body
        offsets[KeypointPart.LeftShoulder] = (pose[KeypointPart.LeftShoulder][1] - shoulderMid.second) / baseUnitLength;
        offsets[KeypointPart.LeftShoulder + NUM_KEYPOINTS] = pose[KeypointPart.LeftShoulder][2];
        offsets[KeypointPart.RightShoulder] = (pose[KeypointPart.RightShoulder][1] - shoulderMid.second) / baseUnitLength;
        offsets[KeypointPart.RightShoulder + NUM_KEYPOINTS] = pose[KeypointPart.RightShoulder][2];

        offsets[KeypointPart.LeftElbow] = (pose[KeypointPart.LeftElbow][1] - shoulderMid.second) / baseUnitLength;
        offsets[KeypointPart.LeftElbow + NUM_KEYPOINTS] = pose[KeypointPart.LeftElbow][2];
        offsets[KeypointPart.RightElbow] = (pose[KeypointPart.RightElbow][1] - shoulderMid.second) / baseUnitLength;
        offsets[KeypointPart.RightElbow + NUM_KEYPOINTS] = pose[KeypointPart.RightElbow][2];

        offsets[KeypointPart.LeftWrist] = (pose[KeypointPart.LeftWrist][1] - shoulderMid.second) / baseUnitLength;
        offsets[KeypointPart.LeftWrist + NUM_KEYPOINTS] = pose[KeypointPart.LeftWrist][2];
        offsets[KeypointPart.RightWrist] = (pose[KeypointPart.RightWrist][1] - shoulderMid.second) / baseUnitLength;
        offsets[KeypointPart.RightWrist + NUM_KEYPOINTS] = pose[KeypointPart.RightWrist][2];

        // lower body
        offsets[KeypointPart.LeftHip] = (pose[KeypointPart.LeftHip][1] - hipMid.second) / baseUnitLength;
        offsets[KeypointPart.LeftHip + NUM_KEYPOINTS] = pose[KeypointPart.LeftHip][2];
        offsets[KeypointPart.RightHip] = (pose[KeypointPart.RightHip][1] - hipMid.second) / baseUnitLength;
        offsets[KeypointPart.RightHip + NUM_KEYPOINTS] = pose[KeypointPart.RightHip][2];

        offsets[KeypointPart.LeftKnee] = (pose[KeypointPart.LeftKnee][1] - hipMid.second) / baseUnitLength;
        offsets[KeypointPart.LeftKnee + NUM_KEYPOINTS] = pose[KeypointPart.LeftKnee][2];
        offsets[KeypointPart.RightKnee] = (pose[KeypointPart.RightKnee][1] - hipMid.second) / baseUnitLength;
        offsets[KeypointPart.RightKnee + NUM_KEYPOINTS] = pose[KeypointPart.RightKnee][2];

        offsets[KeypointPart.LeftAnkle] = (pose[KeypointPart.LeftAnkle][1] - hipMid.second) / baseUnitLength;
        offsets[KeypointPart.LeftAnkle + NUM_KEYPOINTS] = pose[KeypointPart.LeftAnkle][2];
        offsets[KeypointPart.RightAnkle] = (pose[KeypointPart.RightAnkle][1] - hipMid.second) / baseUnitLength;
        offsets[KeypointPart.RightAnkle + NUM_KEYPOINTS] = pose[KeypointPart.RightAnkle][2];

        // face
        offsets[KeypointPart.Nose] = 0;
        offsets[KeypointPart.LeftEye] = 0;
        offsets[KeypointPart.RightEye] = 0;
        offsets[KeypointPart.LeftEar] = 0;
        offsets[KeypointPart.RightEar] = 0;

        return offsets;
    }

    private double getDiffParts(double[] offset1, double[] offset2, int[] parts) {
        if (offset1 == null || offset2 == null || parts == null)
            return -1;

        double diff = 0;
        int count = 0;

        for (int part : parts) {
            if (offset1[part + NUM_KEYPOINTS] > 0 && offset2[part + NUM_KEYPOINTS] > 0) {
                diff += Math.abs(offset1[part] - offset2[part]);
                count++;
            }
        }

        return (count > 1) ? diff / (double) count : -1;
    }

    private double getOffsetDistance(double[] offset1, double[] offset2) {
        if (offset1 == null || offset2 == null)
            return -1;

        int[] leftArmParts = { KeypointPart.LeftShoulder, KeypointPart.LeftElbow, KeypointPart.LeftWrist };
        double leftArmsDiff = getDiffParts(offset1, offset2, leftArmParts);

        int[] rightArmParts = { KeypointPart.RightShoulder, KeypointPart.RightElbow, KeypointPart.RightWrist };
        double rightArmsDiff = getDiffParts(offset1, offset2, rightArmParts);

        int[] leftLegsParts = { KeypointPart.LeftHip, KeypointPart.LeftKnee, KeypointPart.LeftAnkle };
        double leftLegsDiff = getDiffParts(offset1, offset2, leftLegsParts);

        int[] rightLegsParts = { KeypointPart.RightHip, KeypointPart.RightKnee, KeypointPart.RightAnkle };
        double rightLegsDiff = getDiffParts(offset1, offset2, rightLegsParts);

        return Math.max(Math.max(leftArmsDiff, rightArmsDiff), Math.max(leftLegsDiff, rightLegsDiff));
    }

    public double verticalSimilarity(List<PoseData.FrameData> frameDatas, float[][] pose2, boolean flip) {
        if (frameDatas == null || pose2 == null || frameDatas.size() <= 0 || pose2.length <= 0)
            return -1;

        ArrayList<double[]> pose2VerticalOffsets = new ArrayList<>();
        double[] offsets = getVerticalOffsets(pose2);
        if (offsets == null)
            return -1;

        pose2VerticalOffsets.add(offsets);

        float[][] flippedPose = null;
        if (flip) {
            flippedPose = flipPose(pose2, false);
            if (flippedPose != null) {
                offsets = getVerticalOffsets(flippedPose);
                if (offsets != null)
                    pose2VerticalOffsets.add(offsets);
            }
        }

        double minDistance = 10000;
        int minIndex = -1;

        for (int index = 0; index < frameDatas.size(); ++index) {
            if (frameDatas.get(index).isDetected)
                continue;

            float[][] pose1 = frameDatas.get(index).keypoints;

            double[] verticalOffsetPose1 = getVerticalOffsets(pose1);
            if (verticalOffsetPose1 == null)
                continue;

            double _minDistance = 10000;
            for (double[] _offsets : pose2VerticalOffsets) {
                double _distance = getOffsetDistance(verticalOffsetPose1, _offsets);
                if (_distance <= _minDistance)
                    _minDistance = _distance;
            }

            if (minDistance > _minDistance) {
                minDistance = _minDistance;
                minIndex = index;
            }
        }

        if (minIndex >= 0) {
            frameDatas.get(minIndex).isDetected = true;
        }

        return Math.max(0, Math.min(1.0, 1.0 - minDistance * 1.5));
    }

    public double verticalSimilarity2(List<PoseData.FrameData> frameDatas, float[][] pose2, boolean flip) {
        if (frameDatas == null || pose2 == null || frameDatas.size() <= 0 || pose2.length <= 0)
            return -1;

        ArrayList<float[][]> encodedPose2 = new ArrayList<>();
        float[][] encoded = encodePoseVertical(pose2);
        if (encoded == null)
            return -1;

        encodedPose2.add(encoded);

        float[][] flippedPose = null;
        if (flip) {
            flippedPose = flipPose(pose2, false);
            if (flippedPose != null) {
                encoded = encodePoseVertical(pose2);
                if (encoded != null)
                    encodedPose2.add(encoded);
            }
        }

        double minDistance = 10000;
        int minIndex = -1;

        for (int index = 0; index < frameDatas.size(); ++index) {
            if (frameDatas.get(index).isDetected)
                continue;

            float[][] pose1 = frameDatas.get(index).keypoints;

            encoded = encodePoseVertical(pose1);
            if (encoded == null)
                continue;

            float _minDistance = 10000;
            for (float[][] _encodeData : encodedPose2) {
                float _distance = encodedPoseDiff(_encodeData, encoded);
                if (_distance < 0)
                    continue;

                if (_distance <= _minDistance)
                    _minDistance = _distance;
            }

            if (minDistance > _minDistance) {
                minDistance = _minDistance;
                minIndex = index;
            }
        }

        if (minIndex >= 0) {
            frameDatas.get(minIndex).isDetected = true;
        }

        return Math.max(0, Math.min(1.0, 1 - minDistance * 10));
    }
}
