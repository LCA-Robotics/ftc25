package org.lexingtonchristian.ftc;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import static org.lexingtonchristian.ftc.util.Drivetrain.XYDirection.X_DIRECTION;
import static org.lexingtonchristian.ftc.util.Drivetrain.XYDirection.Y_DIRECTION;

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
import org.lexingtonchristian.ftc.util.Tags;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Autonomous
public class PrimaryAuto extends LinearOpMode {

    private static final int GPP = 21;
    private static final int PGP = 22;
    private static final int PPG = 23;

    private static final Map<Integer, Integer> DISTANCES = Map.of(
            GPP, 1000,
            PGP, 2000,
            PPG, 3000
    );

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
    private int numBalls = 3;

    @Override
    public void runOpMode() {
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

        drive.accelerate(0, 55, Y_DIRECTION);

        while (this.opModeIsActive()) {

            for (AprilTagDetection detection : tagDetector.getAprilTags()) {
                telemetry.addLine(String.format(Locale.ENGLISH, "Range: %2f",
                        detection.ftcPose.range));
            }

            if (numBalls > 0 && tagDetector.hasTag(Tags.CURRENT)) {
                AprilTagDetection redGoal = tagDetector.getTag(Tags.CURRENT);
                if (redGoal == null) continue;
                if (redGoal.ftcPose.range < 36) continue;
                drive.decelerate(55, 0, Y_DIRECTION);
                sleep(90); // decelerate already sleeps 10

                drive.center(5.0, () -> {
                    Optional<AprilTagDetection> goal = tagDetector.getPossibleTag(Tags.CURRENT);
                    return goal.map(aprilTagDetection ->
                            aprilTagDetection.ftcPose.bearing).orElse(0.0);
                });

                sleep(250);
                launch(0.60, 3);
                sleep(500);

                drive.rotate(0.3);
                sleep(1000);
                drive.zero();
            }

        }
    }

    private void launch(double power, int shots) {
        launcherLeft.setPower(power);
        launcherRight.setPower(power);
        sleep(1500);
        for (int i = shots; i > 0; i--) {
            launcherServo.setPower(1.0);
            sleep(500);
            launcherServo.setPower(0.0);
            sleep(1250);
            if (i == 1) {
                launcherLeft.setPower(0.0);
                launcherRight.setPower(0.0);
            }
            numBalls -= 1;
        }
    }

}