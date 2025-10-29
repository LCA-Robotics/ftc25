package org.lexingtonchristian.ftc.util;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.Optional;

public class TagDetector {

    private AprilTagProcessor processor;
    private VisionPortal portal;

    public TagDetector(AprilTagProcessor processor, VisionPortal portal) {
        this.processor = processor;
        this.portal = portal;
    }

    public List<AprilTagDetection> getAprilTags() {
        return this.processor.getDetections();
    }

    public Optional<AprilTagDetection> getTag(int id) {
        return this.getAprilTags()
                .stream()
                .filter(tag -> tag.id == id)
                .findFirst();
    }

    public boolean hasTag(int id) {
        return this.getAprilTags()
                .stream()
                .anyMatch(tag -> tag.id == id);
    }

}
