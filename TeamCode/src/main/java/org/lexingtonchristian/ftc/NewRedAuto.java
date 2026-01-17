package org.lexingtonchristian.ftc;

import static org.lexingtonchristian.ftc.util.Constants.CYCLE_TIME;
import static org.lexingtonchristian.ftc.util.Constants.RED_GOAL;
import static org.lexingtonchristian.ftc.util.Constants.initDrivetrain;
import static org.lexingtonchristian.ftc.util.Constants.initIntake;
import static org.lexingtonchristian.ftc.util.Constants.initLauncher;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.lexingtonchristian.ftc.components.Intake;
import org.lexingtonchristian.ftc.components.Launcher;
import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Drivetrain;

import java.util.Optional;

@Autonomous(name = "New Red Auto", group = "Competition")
public class NewRedAuto extends LinearOpMode {

    private Drivetrain drivetrain;
    private Launcher launcher;
    private Intake intake;
    private TagDetector tagDetector;

    public void runOpMode() {

        initHardware();

        waitForStart();

        drivetrain.drive(-60.0); // Reverse for 60 inches

        drivetrain.center(2.0, () -> { // Center on the goal, 2 degrees tolerance
            Optional<AprilTagDetection> tag = this.tagDetector.getPossibleTag(RED_GOAL);
            return tag.map(aprilTagDetection ->
                    aprilTagDetection.ftcPose.bearing).orElse(0.0);
        });
        launcher.servo(0.9); // Rotate the launcher servo continuously
        intake.run(0.7); // Feed balls to the launcher
        launcher.spin(1200);

        sleep(CYCLE_TIME * 3 + 1000); // Wait to cycle 3 balls, +1 second error.
        this.zeroAll();

        drivetrain.rotate(45.0);

        intake.run(0.7);
        launcher.servo(0.25);
        drivetrain.drive(42.0);

        intake.zero();
        launcher.servo(0.0);

        drivetrain.drive(-42.0);
        drivetrain.rotate(-45.0);

        intake.run(0.7);
        launcher.servo(0.9);
        launcher.spin(1200);

        sleep(CYCLE_TIME * 3 + 1000);
        this.zeroAll();

    }

    private void zeroAll() { // Stops all motors in drivetrain, launcher, and intake
        drivetrain.zero();
        launcher.zero();
        intake.zero();
        launcher.servo(0.0);
    }

    private void initHardware() {
        this.drivetrain = initDrivetrain(hardwareMap);
        this.launcher = initLauncher(hardwareMap);
        this.intake = initIntake(hardwareMap);
        this.tagDetector = new TagDetector(hardwareMap.get(WebcamName.class, "webcam"));
    }

}
