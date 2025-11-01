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
import org.lexingtonchristian.ftc.util.Drivetrain;
import org.lexingtonchristian.ftc.util.Launcher;
import org.lexingtonchristian.ftc.util.MathHelper;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.lexingtonchristian.ftc.util.TagDetector;
import org.lexingtonchristian.ftc.util.Tags;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <table border="1">
 *     <tr>
 *         <td><b>Port</b></td>
 *         <td><b>Motor</b></td>
 *     </tr>
 *     <tr>
 *         <td>0</td>
 *         <td>backRight</td>
 *     </tr>
 *     <tr>
 *         <td>1</td>
 *         <td>backLeft</td>
 *     </tr>
 *     <tr>
 *         <td>2</td>
 *         <td>frontRight</td>
 *     </tr>
 *     <tr>
 *         <td>3</td>
 *         <td>frontLeft</td>
 *     </tr>
 * </table>
 *
 */

@TeleOp
public class PrimaryTeleOp extends LinearOpMode {

    private Launcher launcher;
    private Drivetrain drivetrain;
    private TagDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {

        // Run all hardware setup & initialization
        hardwareInit();

        waitForStart();

        while (this.opModeIsActive()) {

            // If slowed, run at 20% speed; else, run at 70%
            double speedLimit = this.gamepad1.right_bumper ? 0.15 : 0.4;

            double leftX = this.gamepad1.left_stick_x;  // left stick X
            double leftY = this.gamepad1.left_stick_y;  // left stick Y
            double rightX = this.gamepad1.right_stick_x; // right stick X

            this.drivetrain.move(
                    leftX,
                    leftY,
                    rightX,
                    speedLimit
            );

            if (this.gamepad1.right_trigger > 0.0) {
                this.launcher.spin(0.43);
            } else {
                this.launcher.zero();
            }

            if (this.gamepad1.b) {
                this.launcher.load();
            } else {
                this.launcher.zero();
            }

            if (this.gamepad1.x && this.detector.hasTag(Tags.CURRENT)) {
                this.drivetrain.center(5.0, () -> {
                    Optional<AprilTagDetection> goal = this.detector.getPossibleTag(Tags.CURRENT);
                    return goal.map(aprilTagDetection -> aprilTagDetection.ftcPose.bearing).orElse(0.0);
                });
            }

        }

    }

    private void hardwareInit() {

        this.launcher = new Launcher(
                hardwareMap.get(DcMotor.class, "launcherLeft"),
                hardwareMap.get(DcMotor.class, "launcherRight"),
                hardwareMap.get(CRServo.class, "launcherServo")
        );

        this.drivetrain = new Drivetrain(
                hardwareMap.get(DcMotor.class, "backRight"),
                hardwareMap.get(DcMotor.class, "backLeft"),
                hardwareMap.get(DcMotor.class, "frontRight"),
                hardwareMap.get(DcMotor.class, "frontLeft")
        );

        this.detector = new TagDetector(hardwareMap.get(WebcamName.class, "webcam"));

    }

}
