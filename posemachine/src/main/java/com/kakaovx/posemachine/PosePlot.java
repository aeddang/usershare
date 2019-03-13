package com.kakaovx.posemachine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

import com.kakaovx.posemachine.Keypoints;

import java.util.ArrayList;

public class PosePlot {
    private Paint paint = null;
    private Paint inputRectPaint = null;

    public PosePlot() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(8);
    }

    public void drawPoses(Canvas canvas, int input_width, int input_height, ArrayList<float[][]> skeletons, boolean drawDetectRect) {
        if (canvas == null)
            return;

        float scaleWidth = canvas.getWidth() / (float)input_width;
        float scaleHeight = canvas.getHeight() / (float)input_height;

        float scale = Math.min(scaleWidth, scaleHeight);

        Pair<Float, Float> offset = Pair.create((canvas.getWidth() - input_width * scale) / 2, (canvas.getHeight() - input_height * scale) / 2);

        if (drawDetectRect) {
            if (inputRectPaint == null) {
                inputRectPaint = new Paint();
                inputRectPaint.setStyle(Paint.Style.STROKE);
                inputRectPaint.setStrokeWidth(8);
                inputRectPaint.setColor(Color.rgb(255, 0, 0));
            }
            canvas.drawRect(offset.first, offset.second, offset.first + input_width * scale, offset.second + input_height * scale, inputRectPaint);
        }

        drawPoses(canvas, offset, scale, skeletons);
    }

    private void drawPoses(Canvas canvas, Pair<Float, Float> offset, float scaleFactor, ArrayList<float[][]> skeletons) {
        if (canvas == null || skeletons == null || skeletons.isEmpty())
            return;

        for (int i = 0; i < Keypoints.NUM_KEYPOINTS; ++i) {
            paint.setColor(Color.rgb(Keypoints.colors[i][0], Keypoints.colors[i][1], Keypoints.colors[i][2]));
            for (int j = 0; j < skeletons.size(); ++j) {
                if (skeletons.get(j)[i][2] > 0)
                    canvas.drawCircle(offset.first + skeletons.get(j)[i][0] * scaleFactor, offset.second + skeletons.get(j)[i][1] * scaleFactor, 5, paint);
            }
        }

        for (int i = 0; i < Keypoints.NUM_EDGES; ++i) {
            for (int j = 0; j < skeletons.size(); ++j) {
                int[] edge = Keypoints.edges_plot[i];
                if (edge[0] < 0 || edge[1] < 0)
                    continue;
                if (skeletons.get(j)[edge[0]][2] == 0 || skeletons.get(j)[edge[1]][2] == 0)
                    continue;

                paint.setColor(Color.rgb(Keypoints.colors[i][0], Keypoints.colors[i][1], Keypoints.colors[i][2]));

                canvas.drawLine(offset.first + skeletons.get(j)[edge[0]][0] * scaleFactor,
                        offset.second + skeletons.get(j)[edge[0]][1] * scaleFactor,
                        offset.first + skeletons.get(j)[edge[1]][0] * scaleFactor,
                        offset.second + skeletons.get(j)[edge[1]][1] * scaleFactor, paint);
            }
        }
    }
}
