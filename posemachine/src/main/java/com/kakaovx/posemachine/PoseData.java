package com.kakaovx.posemachine;

import android.app.Activity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class PoseData {
    public class FrameData {
        double timestamp;
        float[][] keypoints;
        boolean isDetected;

        public FrameData() {
            timestamp = 0;
            keypoints = new float[Keypoints.NUM_KEYPOINTS][3];
            isDetected = false;
        }
    }

    private ArrayList<FrameData> frameDatas = new ArrayList<>();
    private TreeMap<Double, Integer> treeMap = new TreeMap<>();

    public FrameData getFrameData(double timestamp) {
        if (treeMap == null)
            return null;

        try {
            Double floorkey = treeMap.floorKey(timestamp);
            Double ceilingkey = treeMap.ceilingKey(timestamp);

            Integer index = 0;

            if (floorkey == null) {
                index = treeMap.get(ceilingkey);
            } else if (ceilingkey == null) {
                index = treeMap.get(floorkey);
            } else {
                Double key = (timestamp - floorkey < ceilingkey - timestamp || ceilingkey - timestamp < 0) && timestamp - floorkey > 0 ? floorkey : ceilingkey;
                index = treeMap.get(key);
            }

            return frameDatas.get(index);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<FrameData> getFrameDatas(double timestamp, int frontCount, int backCount) {
        if (treeMap == null)
            return null;

        try {
            Double floorkey = treeMap.floorKey(timestamp);
            Double ceilingkey = treeMap.ceilingKey(timestamp);

            Integer index = 0;

            if (floorkey == null) {
                index = treeMap.get(ceilingkey);
            } else if (ceilingkey == null) {
                index = treeMap.get(floorkey);
            } else {
                Double key = (timestamp - floorkey < ceilingkey - timestamp || ceilingkey - timestamp < 0) && timestamp - floorkey > 0 ? floorkey : ceilingkey;
                index = treeMap.get(key);
            }

            int startIndex = index - frontCount;
            int endIndex = index + backCount + 1;

            if (startIndex < 0) startIndex = 0;
            if (endIndex > frameDatas.size()) endIndex = frameDatas.size();

            return frameDatas.subList(startIndex, endIndex);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String loadJSONFromAsset(Activity activity, String fileName) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public boolean parseOutputJson(String jsonFile) {
        try {
            JSONObject jsonObject = new JSONObject(jsonFile);
            JSONArray jsonArray = jsonObject.getJSONArray("keypoints");

            ArrayList<HashMap<String, String>> keypoints = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapKeypoint;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject keypointData = jsonArray.getJSONObject(i);

                FrameData frameData = new FrameData();
                frameData.timestamp = keypointData.getDouble("timestamp");

                JSONArray posevector = keypointData.getJSONArray("posevector");

                int loopcount = posevector.length() / 3;
                for (int j = 0; j < loopcount; j++) {
                    frameData.keypoints[j][0] = (float) posevector.getDouble(j * 3);
                    frameData.keypoints[j][1] = (float) posevector.getDouble(j * 3 + 1);
                    frameData.keypoints[j][2] = (float) posevector.getDouble(j * 3 + 2);
                }

                frameDatas.add(frameData);
                treeMap.put(frameData.timestamp, frameDatas.size() - 1);
            }

            return (frameDatas.size() > 0);
        } catch (JSONException e) {
            e.printStackTrace();

            return false;
        }
    }
}
