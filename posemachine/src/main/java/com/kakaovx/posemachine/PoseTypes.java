package com.kakaovx.posemachine;

public class PoseTypes {
    class Vector2D {
        int y;
        int x;

        public Vector2D(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    class Vector2DF {
        float y;
        float x;

        public Vector2DF(float y, float x) {
            this.y = y;
            this.x = x;
        }
    }

    class Part {
        int heatmapX;
        int heatmapY;
        int id;

        public Part(int heatmapX, int heatmapY, int id) {
            this.heatmapX = heatmapX;
            this.heatmapY = heatmapY;
            this.id = id;
        }
    }

    class PartWithScore {
        float score;
        Part part;
        Vector2D imagePos;

        public PartWithScore(float score, Part part, Vector2D imagePos) {
            this.score = score;
            this.part = part;
            this.imagePos = imagePos;
        }
    }

    class Keypoint {
        float score;
        Vector2D position;
        String part;
    }

    class Pose {
        Keypoint[] keypoints;
        float score;
    }
}
