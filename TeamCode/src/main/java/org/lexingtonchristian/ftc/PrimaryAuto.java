package org.lexingtonchristian.ftc;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.lexingtonchristian.ftc.lib.drive.SampleMecanumDrive;

import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

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
    private int numBalls = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        // Assign launcher motors + servo to hardwaremap
        this.launcherLeft = hardwareMap.get(DcMotor.class, "leftLauncher");
        this.launcherRight = hardwareMap.get(DcMotor.class, "rightLauncher");
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

        MecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        waitForStart();

        drive.setMotorPowers(.5, .5, .5, .5);

        while (this.opModeIsActive()) {

            boolean canSeeRedGoal = this
                    .getAprilTags()
                    .stream()
                    .anyMatch(tag -> tag.id == 24);
            if (numBalls > 0 && canSeeRedGoal) {
                AprilTagDetection redGoal = this.getAprilTags()
                        .stream()
                        .filter(tag -> tag.id == 24)
                        .findFirst().orElse(null);
                if (redGoal == null) continue;
                if (redGoal.ftcPose.range < 32) continue;
                drive.setMotorPowers(0, 0, 0, 0);
                launch(0.45);

            }
        }
    }

    private List<AprilTagDetection> getAprilTags() {
        List<AprilTagDetection> detections = this.tagProcessor.getDetections();
        return detections
                .stream()
                .filter(tag -> 20 <= tag.id && tag.id <= 24)
                .collect(Collectors.toList());
    }

    private void launch(double power) {
        launcherLeft.setPower(power);
        launcherRight.setPower(power);
        sleep(1500);
        launcherServo.setPower(1.0);
        sleep(500);
        launcherServo.setPower(0.0);
        launcherLeft.setPower(0.0);
        launcherRight.setPower(0.0);
        numBalls -= 1;
    }

    private void initialize() {
        this.enhanced(launcherLeft).setVelocityPIDFCoefficients(4, 0.5, 0, 11.7);
        this.enhanced(launcherRight).setVelocityPIDFCoefficients(4, 0.5, 0, 11.7);
        this.launcherLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.launcherRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private DcMotorEx enhanced(DcMotor motor) {
        return (DcMotorEx) motor;
    }

}