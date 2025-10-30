package org.lexingtonchristian.ftc.util;

import android.util.Size;

import androidx.annotation.Nullable;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
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

    public TagDetector(WebcamName webcam) {
        this.processor = AprilTagProcessor.easyCreateWithDefaults();
        this.portal = new VisionPortal.Builder()
                .setCamera(webcam)
                .addProcessor(this.processor)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(640, 360))
                .build();
    }

    public List<AprilTagDetection> getAprilTags() {
        return this.processor.getDetections();
    }

    public Optional<AprilTagDetection> getPossibleTag(int id) {
        return this.getAprilTags()
                .stream()
                .filter(tag -> tag.id == id)
                .findFirst();
    }

    public @Nullable AprilTagDetection getTag(int id) {
        return this.getPossibleTag(id).orElse(null);
    }

    public boolean hasTag(int id) {
        return this.getAprilTags()
                .stream()
                .anyMatch(tag -> tag.id == id);
    }

    public Optional<AprilTagDetection> getObelisk() {
        return this.getAprilTags()
                .stream()
                .filter(tag -> 20 < tag.id && tag.id < 24 )
                .findFirst();
    }

}
