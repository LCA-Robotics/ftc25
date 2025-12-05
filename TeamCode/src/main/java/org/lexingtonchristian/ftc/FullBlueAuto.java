package org.lexingtonchristian.ftc;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.util.Direction;
import org.lexingtonchristian.ftc.util.Drivetrain;
import org.lexingtonchristian.ftc.util.Launcher;
import org.lexingtonchristian.ftc.util.TagDetector;

import java.util.Locale;
import java.util.Optional;

@Autonomous(name = "Blue Alliance Auto", group = "Competition")
public class FullBlueAuto extends LinearOpMode {

    private Drivetrain drive;
    private Launcher launcher;
    private TagDetector detector;

    @Override
    public void runOpMode() {

        initHardware();

        waitForStart();

        this.launcher.servo(-1.0);
        sleep(600);
        this.launcher.servo(0.0);

        this.sleep(10000);

        this.drive.accelerate(0, 55, Direction.Y);

        while (this.opModeIsActive()) {
            for (AprilTagDetection detection : this.detector.getAprilTags()) {
                telemetry.addLine(String.format(Locale.ENGLISH, "Range: %2f",
                        detection.ftcPose.range));
            }

            if (launcher.numBalls > 0 && detector.hasTag(Constants.BLUE_GOAL)) {
                AprilTagDetection goal = detector.getTag(Constants.BLUE_GOAL);
                if (goal == null) continue;
                if (goal.ftcPose.range < 38) continue;
                this.drive.decelerate(55, 0, Direction.Y);
                sleep(90); // decelerate already sleeps 10

                this.drive.center(3.0, () -> {
                    Optional<AprilTagDetection> tag = detector.getPossibleTag(Constants.BLUE_GOAL);
                    return tag.map(aprilTagDetection ->
                            aprilTagDetection.ftcPose.bearing).orElse(0.0);
                });

                sleep(250);
                this.launcher.launch(1070, 3);
                sleep(500);

                this.drive.move(0.4, 0.0, 0.0);
                sleep(2000);
                this.drive.zero();

                sleep(25000); // remain stopped for rest of autonomous
            }
        }
    }

    private void initHardware() {
        this.launcher = Constants.initLauncher(hardwareMap);
        this.drive = Constants.initDrivetrain(hardwareMap);
        this.detector = new TagDetector(hardwareMap.get(WebcamName.class, "webcam"));
    }

}