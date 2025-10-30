package org.lexingtonchristian.ftc;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.lexingtonchristian.ftc.util.Drivetrain;
import org.lexingtonchristian.ftc.util.TagDetector;

import java.util.Optional;

@Autonomous
public class PrimaryAuto extends LinearOpMode {
    // Launcher motors + servo
    private DcMotor launcherLeft;
    private DcMotor launcherRight;
    private CRServo launcherServo;

    // Drive motors
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    // AprilTag Processor
    private AprilTagProcessor tagProcessor = AprilTagProcessor.easyCreateWithDefaults();
    private WebcamName webcam;
    private VisionPortal portal;

    // Default number of balls in index
    private int numBalls = 3;

    @Override
    public void runOpMode() throws InterruptedException {
        // Assign launcher motors + servo to hardwaremap
        this.launcherLeft = hardwareMap.get(DcMotor.class, "launcherLeft");
        this.launcherRight = hardwareMap.get(DcMotor.class, "launcherRight");
        this.launcherServo = hardwareMap.get(CRServo.class, "launcherServo");

        // Assign drive motors to hardwaremap
        this.backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        this.backRight = hardwareMap.get(DcMotor.class, "backRight");
        this.frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        this.frontRight = hardwareMap.get(DcMotor.class, "frontRight");

        // Reverse launcher servo + motor
        this.launcherLeft.setDirection(REVERSE);
        this.launcherServo.setDirection(REVERSE);

        // Reverse drive motors
        this.backRight.setDirection(REVERSE);
        this.frontRight.setDirection(REVERSE);

        // Add webcam
        this.webcam = hardwareMap.get(WebcamName.class, "webcam");

        // Add VisionPortal
        this.portal = new VisionPortal.Builder()
                .setCamera(webcam)
                .addProcessor(tagProcessor)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(640, 360))
                .build();

        // Create Drivetrain
        Drivetrain drive = new Drivetrain(backRight, backLeft, frontRight, frontLeft);

        // Create TagDetector
        TagDetector tagDetector = new TagDetector(tagProcessor, portal);

        waitForStart();

        drive.move(0, 0.55, 0);

        while (this.opModeIsActive()) {
            boolean canSeeRedGoal = tagDetector.hasTag(24);
            if (numBalls > 0 && canSeeRedGoal) {
                AprilTagDetection redGoal = tagDetector
                        .getTag(24)
                        .orElse(null);
                if (redGoal == null) continue;
                if (redGoal.ftcPose.range < 40) continue;
                drive.zero();
                launch(0.37, 3);
            }
        }
    }

    private void launch(double power, int shots) {
        this.launcherLeft.setPower(power);
        this.launcherRight.setPower(power);
        sleep(1500);
        for (int i = shots; i > 0; i--) {
            this.launcherServo.setPower(1.0);
            sleep(500);
            this.launcherServo.setPower(0.0);
            sleep(1000);
            if (i == 1) {
                this.launcherLeft.setPower(0.0);
                this.launcherRight.setPower(0.0);
            }
            numBalls -= 1;
        }
    }

}