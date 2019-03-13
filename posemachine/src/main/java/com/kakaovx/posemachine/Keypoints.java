package com.kakaovx.posemachine;

public class Keypoints {
    /*
    public static String[] partNames = {
            "nose", "leftEye", "rightEye", "leftEar", "rightEar", "leftShoulder",
            "rightShoulder", "leftElbow", "rightElbow", "leftWrist", "rightWrist",
            "leftHip", "rightHip", "leftKnee", "rightKnee", "leftAnkle", "rightAnkle" };
            */

    public static String[] partNames = {
            "nose", "rightShoulder",  "rightElbow", "rightWrist", "leftShoulder", "leftElbow", "leftWrist",
            "rightHip", "rightKnee", "rightAnkle", "leftHip", "leftKnee", "leftAnkle",
            "rightEye", "leftEye", "rightEar", "leftEar" };

    public static class KeypointPart {
        public static int Nose = 0;
        public static int RightShoulder = 1;
        public static int RightElbow = 2;
        public static int RightWrist = 3;
        public static int LeftShoulder = 4;
        public static int LeftElbow = 5;
        public static int LeftWrist = 6;
        public static int RightHip = 7;
        public static int RightKnee = 8;
        public static int RightAnkle = 9;
        public static int LeftHip = 10;
        public static int LeftKnee = 11;
        public static int LeftAnkle = 12;
        public static int RightEye = 13;
        public static int LeftEye = 14;
        public static int RightEar = 15;
        public static int LeftEar = 16;
    }

    /*
    public enum KeypointPart {
        Nose(0),
        RightShoulder(1),
        RightElbow(2),
        RightWrist(3),
        LeftShoulder(4),
        LeftElbow(5),
        LeftWrist(6),
        RightHip(7),
        RightKnee(8),
        RightAnkle(9),
        LeftHip(10),
        LeftKnee(11),
        LeftAnkle(12),
        RightEye(13),
        LeftEye(14),
        RightEar(15),
        LeftEar(16);

        private final int value;
        private KeypointPart(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    */

    public static boolean isFaceKeypoint(int keypoint) {
        if (keypoint == 0 || keypoint == 13 || keypoint == 14 || keypoint == 15 || keypoint == 16)
            return true;
        return false;
    }

    public static int[] flipKeypoints = {
            0, 4, 5, 6, 1, 2, 3, 10, 11, 12, 7, 8, 9, 14, 13, 16, 15
    };

    public static int NUM_KEYPOINTS = partNames.length;

    public static String[][] connectedPartNames = {
            {"leftHip", "leftShoulder"}, {"leftElbow", "leftShoulder"},
            {"leftElbow", "leftWrist"}, {"leftHip", "leftKnee"},
            {"leftKnee", "leftAnkle"}, {"rightHip", "rightShoulder"},
            {"rightElbow", "rightShoulder"}, {"rightElbow", "rightWrist"},
            {"rightHip", "rightKnee"}, {"rightKnee", "rightAnkle"},
            {"leftShoulder", "rightShoulder"}, {"leftHip", "rightHip"}
    };

    public static int[][] connectedPartIds = {
            { 11, 5 }, { 7, 5 },
            { 7, 9 }, { 11, 13 },
            { 13, 15 }, { 12, 6 },
            { 8, 6 }, { 8, 9 },
            { 12, 14 }, { 14, 16 },
            { 5, 6 }, { 11, 12 }
    };

    public static String[][] poseChain = {
            {"nose", "leftEye"}, {"leftEye", "leftEar"}, {"nose", "rightEye"},
            {"rightEye", "rightEar"}, {"nose", "leftShoulder"},
            {"leftShoulder", "leftElbow"}, {"leftElbow", "leftWrist"},
            {"leftShoulder", "leftHip"}, {"leftHip", "leftKnee"},
            {"leftKnee", "leftAnkle"}, {"nose", "rightShoulder"},
            {"rightShoulder", "rightElbow"}, {"rightElbow", "rightWrist"},
            {"rightShoulder", "rightHip"}, {"rightHip", "rightKnee"},
            {"rightKnee", "rightAnkle"}
    };

    public static int[][] poseChainIds = {
            { 0, 1 }, { 1, 3 }, { 0, 2 },
            { 2, 4 }, { 0, 5 },
            { 5, 7 }, { 7, 9 },
            { 5, 11 }, { 11, 13 },
            { 13, 15 }, { 0, 6 },
            { 6, 8 }, { 8, 10 },
            { 6, 12 }, { 12, 14 },
            { 14, 16 }
    };

    public static int[][] edges = {
            { 0, 14 },
            { 0, 13 },
            { 0, 4 },
            { 0, 1 },
            { 14, 16 },
            { 13, 15 },
            { 4, 10 },
            { 4, 1 },
            { 1, 7 },
            { 10, 11 },
            { 10, 7 },
            { 7, 8 },
            { 11, 12 },
            { 8, 9 },
            { 4, 5 },
            { 1, 2 },
            { 5, 6 },
            { 2, 3 }
    };

    public static int[][] edges_plot = {
            { 0, 14 },
            { 0, 13 },
            { 14, 16 },
            { 13, 15 },
            { 4, 1 },
            { 4, 10 },
            { 1, 7 },
            { 10, 7 },
            { 10, 11 },
            { 7, 8 },
            { 11, 12 },
            { 8, 9 },
            { 4, 5 },
            { 1, 2 },
            { 5, 6 },
            { 2, 3 },
            { -1, -1 },
            { -1, -1 }
    };

    public static int NUM_EDGES = edges.length;

    /*
    public static int[][] colors = {{85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0},
            {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0},
            {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}};

    public static int[][] colors = {{207, 44, 193}, {169, 95, 223}, {209, 25, 85}, {187, 29, 117}, {86, 199, 29}, {8, 120, 121}, {3, 116, 4},
            {16, 140, 67}, {75, 151, 205}, {10, 117, 46}, {18, 117, 186}, {15, 115, 81}, {87, 198, 33}, {210, 127, 23},
            {22, 206, 5}, {152, 192, 39}, {255, 0, 170}, {255, 0, 85}};
    */

    public static int[][] colors = {{152, 192, 39}, {152, 192, 39}, {152, 192, 39}, {152, 192, 39}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0},
            {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0},
            {85, 255, 0}, {85, 255, 0}, {85, 255, 0}, {85, 255, 0}};
}
