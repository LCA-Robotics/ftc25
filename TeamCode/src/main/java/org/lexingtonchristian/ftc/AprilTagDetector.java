package org.lexingtonchristian.ftc;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.Locale;

@Autonomous
public class AprilTagDetector extends LinearOpMode {

    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor frontRight;

    double vecX = 0.0;
    double vecY = 0.0;
    double vecZ = 0.0;

    double bearing = 0.0;
    double range = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {

        this.backLeft = hardwareMap.get(DcMotor.class, "leftRear");
        this.frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        this.backRight = hardwareMap.get(DcMotor.class, "rightRear");
        this.frontRight = hardwareMap.get(DcMotor.class, "rightFront");

        double leftBound = -0.5;
        double rightBound = 0.5;

        AprilTagProcessor tagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        VisionPortal portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .addProcessor(tagProcessor)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(1920, 1080))
                .build();

        waitForStart();

        while (this.opModeIsActive()) {



            List<AprilTagDetection> detections = tagProcessor.getDetections();
            AprilTagDetection obelisk22 = null;

            for (AprilTagDetection detection : detections) {
                telemetry.addLine(String.format(Locale.ENGLISH, "Name: %s", detection.metadata.name));
                telemetry.addLine(String.format(Locale.ENGLISH,
                        "Pose: (%.2f, %.2f, %.2f)",
                        detection.ftcPose.x,
                        detection.ftcPose.y,
                        detection.ftcPose.z
                ));
                telemetry.addLine(String.format(Locale.ENGLISH, "Bearing: %.2f", detection.ftcPose.bearing));
                telemetry.addLine(String.format(Locale.ENGLISH, "Range: %.2f", detection.ftcPose.range));
                telemetry.update();
                if (detection.id == 22) {
                    obelisk22 = detection;
                    updatePose(obelisk22);
                }
            }

            if (obelisk22 == null) {
                setAll(0.0);
                continue;
            }

            while (bearing < -0.05 || 0.05 < bearing) {
                if (bearing < -0.05) turnLeft(0.2);
                if (0.05 < bearing) turnRight(0.2);
                updatePose(obelisk22);
            }
            setAll(0.0);

        }

    }

    public void setAll(double power) {
        this.backLeft.setPower(power);
        this.frontLeft.setPower(power);
        this.backRight.setPower(power);
        this.frontRight.setPower(power);
    }

    public void turnRight(double power) {
        this.backLeft.setPower(power);
        this.frontLeft.setPower(power);
        this.backRight.setPower(-power);
        this.frontRight.setPower(-power);
    }

    public void turnLeft(double power) {
        this.backLeft.setPower(-power);
        this.frontLeft.setPower(-power);
        this.backRight.setPower(power);
        this.frontRight.setPower(power);
    }

    public void updatePose(AprilTagDetection detection) {
        vecX = detection.ftcPose.x;
        vecY = detection.ftcPose.y;
        vecZ = detection.ftcPose.z;

        bearing = detection.ftcPose.bearing;
        range = detection.ftcPose.range;
    }

}
