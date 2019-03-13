package com.kakaovx.posemachine;

import android.graphics.Bitmap;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Utils {
    public static double distance2D(double x1, double x2, double y1, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    public static double distance2D(float[] pos1, float[] pos2) {
        if (pos1 == null || pos1.length < 2 || pos2 == null || pos2.length < 2)
            return -1;

        return Math.hypot(pos2[0] - pos1[0], pos2[1] - pos1[1]);
    }

    public static Pair<Float, Float> midPoint2D(float[] pos1, float[] pos2) {
        float mid_x = (pos1[0] + pos2[0]) / 2;
        float mid_y = (pos1[1] + pos2[1]) / 2;

        return Pair.create(mid_x, mid_y);
    }

    public static float[][] minMaxFilter(float[][] input, int filterSize, boolean isMaximum, boolean peakOnly) {
        if (input == null || input.length <= 0 || filterSize <= 1 || filterSize >= input[0].length)
            return null;

        float[][] output = new float[input.length][input[0].length];

        int halfSize = filterSize / 2;

        int startX, endX, startY, endY;
        float minMaxValue;

        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input[0].length; y++) {
                startX = Math.max(0, x - halfSize);
                endX = Math.min(input.length - 1, x + halfSize);

                startY = Math.max(0, y - halfSize);
                endY = Math.min(input[x].length - 1, halfSize);

                minMaxValue = input[x][y];
                for (int filterX = startX; filterX <= endX; filterX++) {
                    for (int filterY = startY; filterY <= endY; filterY++) {
                        if (isMaximum)
                            minMaxValue = Math.max(minMaxValue, input[filterX][filterY]);
                        else
                            minMaxValue = Math.min(minMaxValue, input[filterX][filterY]);
                    }
                }

                if (peakOnly == false || minMaxValue == input[x][y])
                    output[x][y] = minMaxValue;
            }
        }

        return output;
    }

    public static float[] minMaxFilter(float[] input, float[] output, int width, int height, int channels, int filterSize, boolean isMaximum, boolean peakOnly) {
        if (input == null || input.length <= 0 || width <= 0 || height <= 0 || channels <= 0 || filterSize <= 1 || filterSize >= width || filterSize >= height)
            return null;

        //float[] output = new float[input.length];

        int halfSize = filterSize / 2;

        int startX, endX, startY, endY;
        float minMaxValue;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                startX = Math.max(0, x - halfSize);
                endX = Math.min(width - 1, x + halfSize);

                startY = Math.max(0, y - halfSize);
                endY = Math.min(height - 1, y + halfSize);

                for (int c = 0; c < channels; c++) {
                    minMaxValue = input[y * width * channels + x * channels + c];

                    for (int filterX = startX; filterX <= endX; filterX++) {
                        for (int filterY = startY; filterY <= endY; filterY++) {
                            if (isMaximum)
                                minMaxValue = Math.max(minMaxValue, input[filterY * width * channels + filterX * channels + c]);
                            else
                                minMaxValue = Math.min(minMaxValue, input[filterY * width * channels + filterX * channels + c]);
                        }
                    }

                    if (peakOnly == false || minMaxValue == input[y * width * channels + x * channels + c])
                        output[y * width * channels + x * channels + c] = minMaxValue;
                    else
                        output[y * width * channels + x * channels + c] = 0;
                }
            }
        }

        return output;
    }

    public static float[][][] minMaxFilter3D(float[][][] input, float[][][] output, int width, int height, int channels, int filterSize, boolean isMaximum, boolean peakOnly) {
        if (input == null || input.length <= 0 || width <= 0 || height <= 0 || channels <= 0 || filterSize <= 1 || filterSize >= width || filterSize >= height)
            return null;

        //float[] output = new float[input.length];

        int halfSize = filterSize / 2;

        int startX, endX, startY, endY;
        float minMaxValue;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                startX = Math.max(0, x - halfSize);
                endX = Math.min(width - 1, x + halfSize);

                startY = Math.max(0, y - halfSize);
                endY = Math.min(height - 1, y + halfSize);

                for (int c = 0; c < channels; c++) {
                    minMaxValue = input[y][x][c];

                    for (int filterX = startX; filterX <= endX; filterX++) {
                        for (int filterY = startY; filterY <= endY; filterY++) {
                            if (isMaximum)
                                minMaxValue = Math.max(minMaxValue, input[filterY][filterX][c]);
                            else
                                minMaxValue = Math.min(minMaxValue, input[filterY][filterX][c]);
                        }
                    }

                    if (peakOnly == false || minMaxValue == input[y][x][c])
                        output[y][x][c] = minMaxValue;
                    else
                        output[y][x][c] = 0;
                }
            }
        }

        return output;
    }

    public static double getAvg(ArrayList<Double> datas) {
        if (datas == null)
            return 0;

        double sum = 0;
        for (Double value : datas)
            sum += value;

        return sum / (double)datas.size();
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String path, String filename) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();

        File fileCacheItem = new File(path + filename);
        OutputStream out = null;

        boolean isSuccess = false;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                isSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }

    public static float[][] divideChannels(float[] data, int[] size, int channelIndex) {
        if (data == null || size == null || size.length != 3)
            return null;

        float[][] channelDatas = new float[size[0]][size[1]];

        for (int y = 0; y < size[0]; y++) {
            for (int x = 0; x < size[1]; x++) {
                channelDatas[y][x] = data[y * size[1] * size[2] + x * size[2] + channelIndex];
            }
        }

        return channelDatas;
    }
}
