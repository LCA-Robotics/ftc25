package org.lexingtonchristian.ftc;

import static org.lexingtonchristian.ftc.util.Constants.CYCLE_TIME;
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
import org.lexingtonchristian.ftc.util.Constants;

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

        launcher.spin(1050);

        drivetrain.drive(-40.0); // Reverse for 40 inches

        drivetrain.center(2.0, () -> { // Center on the goal, 2 degrees tolerance
            Optional<AprilTagDetection> tag = this.tagDetector.getPossibleTag(Constants.RED_GOAL);
            return tag.map(aprilTagDetection ->
                    aprilTagDetection.ftcPose.bearing).orElse(0.0);
        });

        launcher.servo(1.0); // Rotate the launcher servo continuously

        intake.run(1.0); // Feed balls to the launcher

        sleep(CYCLE_TIME * 3 + 500); // Wait to cycle 3 balls, +500ms error.

        this.zeroAll();

        drivetrain.rotate(1000); // TODO: Rotation for degrees
        sleep(1000);


    }

    private void zeroAll() { // Stops all movement in drivetrain, launcher, and intake
        drivetrain.zero();
        launcher.zero();
        intake.zero();
    }

    private void initHardware() {
        this.drivetrain = initDrivetrain(hardwareMap);
        this.launcher = initLauncher(hardwareMap);
        this.intake = initIntake(hardwareMap);
        this.tagDetector = new TagDetector(hardwareMap.get(WebcamName.class, "webcam"));
    }

}
