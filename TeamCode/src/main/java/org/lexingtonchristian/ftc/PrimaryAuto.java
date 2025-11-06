package org.lexingtonchristian.ftc;

import static org.lexingtonchristian.ftc.util.Constants.GPP;
import static org.lexingtonchristian.ftc.util.Constants.PGP;
import static org.lexingtonchristian.ftc.util.Constants.PPG;
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
import org.lexingtonchristian.ftc.util.Launcher;
import org.lexingtonchristian.ftc.util.TagDetector;
import org.lexingtonchristian.ftc.util.Tags;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Autonomous
public class PrimaryAuto extends LinearOpMode {

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

        // Add webcam
        this.webcam = hardwareMap.get(WebcamName.class, "webcam");

        // Add VisionPortal
        this.portal = new VisionPortal.Builder()
                .setCamera(this.webcam)
                .addProcessor(this.tagProcessor)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(640, 360))
                .build();

        // Create Drivetrain
        Drivetrain drive = new Drivetrain(backRight, backLeft, frontRight, frontLeft);

        // Create TagDetector
        TagDetector tagDetector = new TagDetector(this.tagProcessor, this.portal);

        // Create Launcher
        Launcher launcher = new Launcher(this.launcherLeft, this.launcherRight, this.launcherServo);

        waitForStart();

        drive.accelerate(0, 55, Y_DIRECTION);

        while (this.opModeIsActive()) {

            for (AprilTagDetection detection : tagDetector.getAprilTags()) {
                telemetry.addLine(String.format(Locale.ENGLISH, "Range: %2f",
                        detection.ftcPose.range));
            }

            if (launcher.numBalls > 0 && tagDetector.hasTag(Tags.CURRENT)) {
                AprilTagDetection redGoal = tagDetector.getTag(Tags.CURRENT);
                if (redGoal == null) continue;
                if (redGoal.ftcPose.range < 36) continue;
                drive.decelerate(55, 0, Y_DIRECTION);
                sleep(90); // decelerate already sleeps 10

                drive.center(1.5, () -> {
                    Optional<AprilTagDetection> goal = tagDetector.getPossibleTag(Tags.CURRENT);
                    return goal.map(aprilTagDetection ->
                            aprilTagDetection.ftcPose.bearing).orElse(0.0);
                });

                sleep(250);
                launcher.launch(1000, 1);
                sleep(500);

                drive.move(0.4, 0.0, 0.0);
                sleep(1000);
                drive.zero();
                sleep(20000);
            }
        }
    }

}