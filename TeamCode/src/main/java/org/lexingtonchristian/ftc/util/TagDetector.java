package org.lexingtonchristian.ftc.util;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class TagDetector {

    private AprilTagProcessor processor;
    private VisionPortal portal;

    public TagDetector(AprilTagProcessor processor, VisionPortal portal) {
        this.processor = processor;
        this.portal = portal;
    }



}
