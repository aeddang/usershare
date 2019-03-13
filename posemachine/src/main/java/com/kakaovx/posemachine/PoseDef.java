package com.kakaovx.posemachine;

public class PoseDef {
    public static final int input_width = 304;
    public static final int input_height = 304;
    public static final int input_channel = 3;
    public static final int output_stride = 16;

    public static final int maxPoseDetections = 10;
    public static final float scoreThreshold = 0.5f;
    public static final int nmsRadius = 6;
    public static final int kpRadius = 16;
    public static final int minKeypoints = 4;
}
