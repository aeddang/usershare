package com.kakaovx.posemachine;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.xiaomi.mace.JniMaceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MaceWrapper {
    private String storagePath;

    private final int num_kp = Keypoints.NUM_KEYPOINTS;
    private final int num_edges = Keypoints.NUM_EDGES;
    private final int output_size = (PoseDef.input_height - 1) / PoseDef.output_stride + 1;

    private int[] intValues = new int[PoseDef.input_height * PoseDef.input_width];
    private float[] floatValues = new float[PoseDef.input_height * PoseDef.input_width * PoseDef.input_channel];

    private float[] heatmaps = new float[output_size * output_size * num_kp];
    private float[] pafmaps = new float[output_size * output_size * num_edges * 2];
    private float[] short_offsets = new float[output_size * output_size * num_kp * 2];
    private float[] mid_offsets = new float[output_size * output_size * num_edges * 4];

    private long prepareInputTimeMs;
    private long inferenceTimeMs;
    private long readOutputTimeMs;
    private long totalTimeMs;

    public enum MODEL_TYPE {
        PersonLab,
        OpenPose
    };
    public enum RUNTIME_TYPE {
        GPU,
        DSP,
        CPU
    };

    private MaceWrapper.MODEL_TYPE model_type = MaceWrapper.MODEL_TYPE.PersonLab;
    private MaceWrapper.RUNTIME_TYPE runtime_type = MaceWrapper.RUNTIME_TYPE.DSP;

    private boolean initialized = false;

    public interface DataProcessCallback {
        public void onBitmapPrepared();
    }

    public MaceWrapper(RUNTIME_TYPE runtime_type) {
        //System.loadLibrary("OpenCL");

        storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mace";
        File file = new File(storagePath);
        if (!file.exists()) {
            file.mkdir();
        }

        macePersonlabCreateGPUContext();
        macePersonlabCreateEngine(runtime_type);
    }

    public void release() {

    }

    public MaceWrapper.MODEL_TYPE getModelType() {
        return model_type;
    }

    private void macePersonlabCreateGPUContext() {
        int result = JniMaceUtils.macePersonlabCreateGPUContext(storagePath);
        Log.i("APPModel", "macePersonlabCreateGPUContext result = " + result);
    }

    private boolean macePersonlabCreateEngine(RUNTIME_TYPE runtime_type) {
        int result = JniMaceUtils.macePersonlabCreateEngine(
                2, 1,
                3, 3,
                "personlab", runtime_type.toString());
        Log.i("APPModel", "macePersonlabCreateEngine result = " + result);

        if (result == -1) {
            initialized = false;
            return false;
        } else {
            initialized = true;
            return true;
        }
    }

    private boolean macePersonlabEstimate(float[] input, float[] output_kp_maps, float[] output_short_offsets, float[] output_mid_offsets) {
        int result = JniMaceUtils.macePersonlabEstimate(input, output_kp_maps, output_short_offsets, output_mid_offsets);
        return (result >= 0);
    }

    public int getHeatmapsLength() { return heatmaps.length; }
    public int getPafmapsLength() { return pafmaps.length; }
    public int getShortOffsetsLength() { return short_offsets.length; }
    public int getMidOffsetsLength() { return mid_offsets.length; }

    public Map<String, Long> getDebugTimes() {
        Map<String, Long> times = new HashMap<>();

        times.put("prepareInput", prepareInputTimeMs);
        times.put("inference", inferenceTimeMs);
        times.put("readOutput", readOutputTimeMs);
        times.put("total", totalTimeMs);

        return times;
    }

    public boolean inference(Bitmap bitmap, float[] output1, float[] output2, float[] output3, MaceWrapper.DataProcessCallback callback) {
        if (model_type == MaceWrapper.MODEL_TYPE.PersonLab)
            return inferencePersonLab(bitmap, output1, output2, output3, callback);
        else if (model_type == MaceWrapper.MODEL_TYPE.OpenPose)
            return inferenceOpenPose(bitmap, output1, output2, output3, callback);

        throw new RuntimeException("Invalid configuration");
    }

    public boolean inferenceOpenPose(Bitmap bitmap, float[] _heatmaps, float[] _pafmaps, float[] _short_offsets, MaceWrapper.DataProcessCallback callback) {
        if (bitmap == null)
            return false;

        if (initialized == false)
            return false;

        final long startTime = SystemClock.uptimeMillis();

        final long startPrepareInputTime = SystemClock.uptimeMillis();

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        if (callback != null)
            callback.onBitmapPrepared();

        for (int i = 0; i < intValues.length; ++i) {
            floatValues[i * 3 + 0] = ((intValues[i] >> 16) & 0xFF) / 127.5f - 1.f;
            floatValues[i * 3 + 1] = ((intValues[i] >> 8) & 0xFF) / 127.5f - 1.f;
            floatValues[i * 3 + 2] = (intValues[i] & 0xFF) / 127.5f - 1.f;
        }

        prepareInputTimeMs = SystemClock.uptimeMillis() - startPrepareInputTime;

        float[] outputHeatmaps = (_heatmaps != null) ? _heatmaps : heatmaps;
        float[] outputPafmaps = (_pafmaps != null) ? _pafmaps : pafmaps;
        float[] outputShortOffsets = (_short_offsets != null) ? _short_offsets : short_offsets;

        final long startInferenceTime = SystemClock.uptimeMillis();
        final boolean result = macePersonlabEstimate(floatValues, outputHeatmaps, outputPafmaps, outputShortOffsets);
        inferenceTimeMs = SystemClock.uptimeMillis() - startInferenceTime;

        final long startReadOutputTimeMs = SystemClock.uptimeMillis();

        readOutputTimeMs = SystemClock.uptimeMillis() - startReadOutputTimeMs;

        totalTimeMs = SystemClock.uptimeMillis() - startTime;

        return true;
    }

    public boolean inferencePersonLab(Bitmap bitmap, float[] _heatmaps, float[] _short_offsets, float[] _mid_offsets, MaceWrapper.DataProcessCallback callback) {
        if (bitmap == null)
            return false;

        if (initialized == false)
            return false;

        final long startTime = SystemClock.uptimeMillis();

        final long startPrepareInputTime = SystemClock.uptimeMillis();

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        if (callback != null)
            callback.onBitmapPrepared();

        for (int i = 0; i < intValues.length; ++i) {
            floatValues[i * 3 + 0] = ((intValues[i] >> 16) & 0xFF) / 127.5f - 1.f;
            floatValues[i * 3 + 1] = ((intValues[i] >> 8) & 0xFF) / 127.5f - 1.f;
            floatValues[i * 3 + 2] = (intValues[i] & 0xFF) / 127.5f - 1.f;
        }

        prepareInputTimeMs = SystemClock.uptimeMillis() - startPrepareInputTime;

        float[] outputHeatmaps = (_heatmaps != null) ? _heatmaps : heatmaps;
        float[] outputShortOffsets = (_short_offsets != null) ? _short_offsets : short_offsets;
        float[] outputMidOffsets = (_mid_offsets != null) ? _mid_offsets : mid_offsets;

        final long startInferenceTime = SystemClock.uptimeMillis();
        final boolean result = macePersonlabEstimate(floatValues, outputHeatmaps, outputShortOffsets, outputMidOffsets);
        inferenceTimeMs = SystemClock.uptimeMillis() - startInferenceTime;

        final long startReadOutputTimeMs = SystemClock.uptimeMillis();

        /*
        float[] outputHeatmaps = (_heatmaps != null) ? _heatmaps : heatmaps;
        float[] outputShortOffsets = (_short_offsets != null) ? _short_offsets : short_offsets;
        float[] outputMidOffsets = (_mid_offsets != null) ? _mid_offsets : mid_offsets;

        System.arraycopy(outputs, 0, outputHeatmaps, 0, getHeatmapsLength());
        System.arraycopy(outputs, getHeatmapsLength(), outputShortOffsets, 0, getShortOffsetsLength());
        System.arraycopy(outputs, getHeatmapsLength() + getShortOffsetsLength(), outputMidOffsets, 0, getMidOffsetsLength());
        */

        readOutputTimeMs = SystemClock.uptimeMillis() - startReadOutputTimeMs;

        totalTimeMs = SystemClock.uptimeMillis() - startTime;

        return true;
    }
}
