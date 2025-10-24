package org.lexingtonchristian.ftc;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.lexingtonchristian.ftc.lib.util.Encoder;
import org.lexingtonchristian.ftc.util.MathHelper;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * {@code backRight}  - {@code Port 0}, {@code Control Hub} <br/>
 * {@code backLeft}   - {@code Port 1}, {@code Control Hub} <br/>
 * {@code frontRight} - {@code Port 2}, {@code Control Hub} <br/>
 * {@code frontLeft}  - {@code Port 3}, {@code Control Hub}
 *
 */

@TeleOp
public class PrimaryTeleOp extends LinearOpMode {

    private DcMotor launcherLeft;
    private DcMotor launcherRight;
    private CRServo launcherServo;

    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor frontRight;

    private AprilTagProcessor tagProcessor;
    private WebcamName webcam;
    private VisionPortal portal;

    @Override
    public void runOpMode() throws InterruptedException {

        hardwareInit();

        waitForStart();

        while (this.opModeIsActive()) {

            double power = MathHelper.clamp(this.gamepad1.right_trigger, 0.0, 0.4);
            double mult = this.gamepad1.right_bumper ? 0.2 : 0.7;

            double lX = this.gamepad1.left_stick_x;
            double lY = this.gamepad1.left_stick_y;
            double rX = this.gamepad1.right_stick_x;

            this.backLeft.setPower(left(lY + lX, rX) * mult);
            this.frontLeft.setPower(left(lY - lX, rX) * mult);

            this.backRight.setPower(right(lY - lX, rX) * mult);
            this.frontRight.setPower(right(lY + lX, rX) * mult);

            this.launcherLeft.setPower(power);
            this.launcherRight.setPower(power);

            boolean canSeeRedGoal = this
                    .getAprilTags()
                    .stream()
                    .anyMatch(tag -> tag.id == 24);
            if (this.gamepad1.x && canSeeRedGoal) {
                AprilTagDetection redGoal = this.getAprilTags()
                        .stream()
                        .filter(tag -> tag.id == 24)
                        .findFirst().get();
                double bearing = redGoal.ftcPose.bearing;
                while (!(-0.01 < bearing && bearing < 0.01)) {
                    if (bearing < 0) {
                        rotate(0.3, Direction.RIGHT);
                    } else if (bearing > 0) {
                        rotate(0.3, Direction.LEFT);
                    }
                    bearing = redGoal.ftcPose.bearing;
                }
            }

            if (this.gamepad1.b) {
                this.launcherServo.setPower(0.7);
            } else {
                this.launcherServo.setPower(0.0);
            }

        }

    }

    private void rotate(double power, Direction direction) {
        switch (direction) {
            case LEFT:
                this.backLeft.setPower(-power);
                this.frontLeft.setPower(-power);
                this.backRight.setPower(power);
                this.frontRight.setPower(power);
            case RIGHT:
                this.backLeft.setPower(power);
                this.frontLeft.setPower(power);
                this.backRight.setPower(-power);
                this.frontRight.setPower(-power);
        }
    }

    private double left(double move, double rX) {
        return MathHelper.clamp(move - rX, -1.0, 1.0);
    }

    private double right(double move, double rX) {
        return MathHelper.clamp(move + rX, -1.0, 1.0);
    }

    private void hardwareInit() {

        this.launcherLeft = hardwareMap.get(DcMotor.class, "leftLauncher");
        this.launcherRight = hardwareMap.get(DcMotor.class, "rightLauncher");
        this.launcherServo = hardwareMap.get(CRServo.class, "launcherServo");

        this.backRight = hardwareMap.get(DcMotor.class, "backRight");
        this.backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        this.frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        this.frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");

        this.launcherLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        this.launcherServo.setDirection(DcMotorSimple.Direction.REVERSE);

        this.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        this.tagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        this.webcam = hardwareMap.get(WebcamName.class, "webcam");
        this.portal = new VisionPortal.Builder()
                .setCamera(webcam)
                .addProcessor(tagProcessor)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(640, 360))
                .build();

    }

    private List<AprilTagDetection> getAprilTags() {
        List<AprilTagDetection> detections = this.tagProcessor.getDetections();
        return detections
                .stream()
                .filter(tag -> 20 <= tag.id && tag.id <= 24)
                .collect(Collectors.toList());
    }

    private enum Direction {
        LEFT,
        RIGHT
    }

}
