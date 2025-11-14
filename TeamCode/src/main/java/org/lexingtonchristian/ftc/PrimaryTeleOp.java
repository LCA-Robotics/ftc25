package org.lexingtonchristian.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.util.Drivetrain;
import org.lexingtonchristian.ftc.util.Launcher;
import org.lexingtonchristian.ftc.util.TagDetector;

import java.util.Optional;

@TeleOp
public class PrimaryTeleOp extends LinearOpMode {

    private Launcher launcher;
    private Drivetrain drivetrain;
    private TagDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {

        // Run all hardware setup & initialization
        initHardware();

        waitForStart();

        this.launcher.servo(-1.0);
        this.sleep(600);
        this.launcher.servo(0.0);

        while (this.opModeIsActive()) {

            // If slowed, run at 30% speed; else, run at 85%
            double speedLimit = this.gamepad1.right_bumper ? 0.30 : 0.85;

            double leftX =  this.gamepad1.left_stick_x;  // left stick X
            double leftY =  this.gamepad1.left_stick_y;  // left stick Y
            double rightX = this.gamepad1.right_stick_x * 0.6; // right stick X (rotational, slow by 60%)

            this.drivetrain.move(
                    -leftX,
                    -leftY,
                    -rightX,
                    speedLimit
            );

            if (this.gamepad1.right_trigger > 0.0) {
                this.launcher.spin(1050);
            } else {
                this.launcher.zero();
            }

            if (this.gamepad1.b) {
                this.launcher.servo(1.0);
                this.sleep(1200);
                this.launcher.servo(-1.0);
                this.sleep(600);
                this.launcher.servo(0.0);
            }

            if (this.gamepad1.x && this.detector.hasTag(Constants.CURRENT)) {
                this.drivetrain.center(5.0, () -> {
                    Optional<AprilTagDetection> goal = this.detector.getPossibleTag(Constants.CURRENT);
                    return goal.map(aprilTagDetection -> aprilTagDetection.ftcPose.bearing).orElse(0.0);
                });
            }

        }

    }

    private void initHardware() {

        this.launcher = Constants.initLauncher(hardwareMap);
        this.drivetrain = Constants.initDrivetrain(hardwareMap);

        this.detector = new TagDetector(hardwareMap.get(WebcamName.class, "webcam"));

    }

}
