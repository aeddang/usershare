package com.kakaovx.posemachine;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.kakaovx.posemachine.Utils.getAvg;

public class PoseMachine {
    private MaceWrapper maceWrapper;
    private PoseEstimator poseEstimator;
    private PosePlot posePlot;
    private PoseData poseData;

    private float[] heatmaps = null;
    private float[] pafmaps = null;
    private float[] shortOffsets = null;
    private float[] midOffsets = null;

    private long buildPoseTimeMs;

    private ArrayList<Float> fpsList = new ArrayList<>();
    private int fpsBufferCount = 5;
    private long lastRenderTimeMs = 0;

    private ArrayList<Double> similarityBuffers = new ArrayList<>();

    private Application application;

    public interface DataProcessCallback {
        public void onBitmapPrepared();
    }

    public PoseMachine(Application application, MaceWrapper.MODEL_TYPE model_type, MaceWrapper.RUNTIME_TYPE runtime_type) {
        this.application = application;

        maceWrapper = new MaceWrapper(runtime_type);
        poseEstimator = new PoseEstimator(PoseDef.input_height, PoseDef.input_width, PoseDef.output_stride, Keypoints.NUM_KEYPOINTS, 1, (maceWrapper.getModelType() == MaceWrapper.MODEL_TYPE.PersonLab));

        posePlot = new PosePlot();
        poseData = new PoseData();

        heatmaps = new float[maceWrapper.getHeatmapsLength()];
        pafmaps = new float[maceWrapper.getPafmapsLength()];
        shortOffsets = new float[maceWrapper.getShortOffsetsLength()];
        midOffsets = new float[maceWrapper.getMidOffsetsLength()];
    }

    public void Destory() {
        if (maceWrapper != null)
            maceWrapper.release();

        maceWrapper = null;
        poseEstimator = null;
        posePlot = null;
        poseData = null;
    }

    public int getInputWidth() {
        return PoseDef.input_width;
    }
    public int getInputHeight() {
        return PoseDef.input_height;
    }

    public ArrayList<float[][]> poseEstimate(final Bitmap bitmap, final DataProcessCallback callback) {
        if (maceWrapper.getModelType() == MaceWrapper.MODEL_TYPE.PersonLab)
            return poseEstimatePersonLab(bitmap, callback);
        //else if (maceWrapper.getModelType() == SnpeWrapper.MODEL_TYPE.OpenPose)
        //    return poseEstimateOpenPose(bitmap, callback);

        throw new RuntimeException("invalid model type");
    }

    private ArrayList<float[][]> poseEstimatePersonLab(final Bitmap bitmap, final DataProcessCallback callback) {
        if (maceWrapper == null || poseEstimator == null)
            return null;

        if (bitmap == null)
            return null;

        boolean result = maceWrapper.inference(bitmap, heatmaps, shortOffsets, midOffsets, new MaceWrapper.DataProcessCallback() {
            @Override
            public void onBitmapPrepared() {
                if (callback != null)
                    callback.onBitmapPrepared();
            }
        });
        if (result == false)
            return null;

        if (heatmaps == null || heatmaps.length <= 0 ||
                shortOffsets == null || shortOffsets.length <= 0 ||
                midOffsets == null || midOffsets.length <= 0)
            return null;

        ArrayList<float[]> outputs = new ArrayList<>();
        outputs.add(heatmaps);
        outputs.add(shortOffsets);
        outputs.add(midOffsets);

        final long startPoseEstimateTimeMs = SystemClock.uptimeMillis();
        ArrayList<float[][]> poses = poseEstimator.decodePoses(outputs, PoseDef.maxPoseDetections, PoseDef.scoreThreshold, PoseDef.nmsRadius, PoseDef.kpRadius, PoseDef.minKeypoints);
        buildPoseTimeMs = SystemClock.uptimeMillis() - startPoseEstimateTimeMs;

        return poses;
    }

    public Map<String, Long> getDebugTimeInfos() {
        Map<String, Long> infos = maceWrapper.getDebugTimes();
        infos.put("buildPose", buildPoseTimeMs);

        return infos;
    }

    public Vector<String> getDebugInfo(int previewWidth, int previewHeight) {
        final Vector<String> lines = new Vector<String>();

        Map<String, Long> debugTimes = getDebugTimeInfos();

        lines.add("Frame: " + previewWidth + "x" + previewHeight);
        lines.add("Crop: " + PoseDef.input_width + "x" + PoseDef.input_height);
        //lines.add("View: " + canvas.getWidth() + "x" + canvas.getHeight());
        //lines.add("Rotation: " + sensorOrientation);
        lines.add("PreProcess time: " + debugTimes.get("prepareInput") + "ms");
        lines.add("Inference time: " + debugTimes.get("inference") + "ms");
        lines.add("PostProcess time: " + (debugTimes.get("readOutput") + debugTimes.get("buildPose")) + "ms");
        lines.add("Total time: " + (debugTimes.get("total") + debugTimes.get("buildPose")) + "ms");

        if (lastRenderTimeMs != 0) {
            long elapsed = SystemClock.uptimeMillis() - lastRenderTimeMs;
            float fps = 1000 / (float) elapsed;

            fpsList.add(fps);

            float total = 0;
            for (int i = 0; i < fpsList.size(); ++i) total += fpsList.get(i);

            lines.add("FPS: " + String.format("%.1f", (total / fpsList.size())));

            while (fpsList.size() > fpsBufferCount)
                fpsList.remove(0);
        }

        lastRenderTimeMs = SystemClock.uptimeMillis();

        return lines;
    }

    public void drawPose(Canvas canvas, ArrayList<float[][]> poses, boolean drawDetectRect) {
        posePlot.drawPoses(canvas, PoseDef.input_width, PoseDef.input_height, poses, drawDetectRect);
    }

    public boolean setCPPoseDatasFromAsset(Activity activity, String videoJsonFile) {
        if (activity == null || videoJsonFile == null)
            return false;

        if (poseData == null)
            return false;

        String jsonPoseData = poseData.loadJSONFromAsset(activity, videoJsonFile);
        if (jsonPoseData == null || jsonPoseData.isEmpty())
            return false;

        return poseData.parseOutputJson(jsonPoseData);
    }

    public boolean setCPPoseDatas(String jsonData) {
        if (poseData == null || jsonData == null)
            return false;

        return poseData.parseOutputJson(jsonData);
    }

    public double calcPoseSimilarity(ArrayList<float[][]> poses, double timestamp) {
        if (poses == null || poses.isEmpty())
            return 0;

        ArrayList<float[][]> centerPose = poseEstimator.selectCenterPose(poses, PoseDef.input_width, PoseDef.input_height);
        if (centerPose == null)
            return 0;

        List<PoseData.FrameData> frameDatas = poseData.getFrameDatas(timestamp, 14, 5);
        if (frameDatas != null && frameDatas.size() > 0) {
            // cosine similarity
            //double similarity = poseEstimator.getSimilarity(frameDatas, centerPose.get(0), true, true);

            // vertical similarity
            double similarity = poseEstimator.getVerticalSimilarity(frameDatas, centerPose.get(0), true);
            similarity = Math.max(0, similarity);

            similarityBuffers.add(similarity);
            double similarityAvg = getAvg(similarityBuffers);
            while (similarityBuffers.size() > 3)
                similarityBuffers.remove(0);

            return similarityAvg;
        }

        return 0;
    }
}
