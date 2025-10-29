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
import org.lexingtonchristian.ftc.util.MathHelper;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.lexingtonchristian.ftc.util.TagDetector;

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

    private static final int BLU_GOAL = 20;
    private static final int RED_GOAL = 24;

    private static final int GPP = 21;
    private static final int PGP = 22;
    private static final int PPG = 23;

    private DcMotor launcherLeft;
    private DcMotor launcherRight;
    private CRServo launcherServo;

    private Drivetrain drivetrain;

    private AprilTagProcessor tagProcessor;
    private WebcamName webcam;
    private VisionPortal portal;

    private TagDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {

        // Run all hardware setup & initialization
        hardwareInit();

        waitForStart();

        while (this.opModeIsActive()) {

            // Set launcher power (max of 45%)
            double launchPower = MathHelper.clamp(this.gamepad1.right_trigger, 0.0, 0.45);

            // If slowed, run at 20% speed; else, run at 70%
            double speedLimit = this.gamepad1.right_bumper ? 0.2 : 0.7;

            double leftX = this.gamepad1.left_stick_x;  // left stick X
            double leftY = this.gamepad1.left_stick_y;  // left stick Y
            double rightX = this.gamepad1.right_stick_x; // right stick X

            this.drivetrain.move(
                    -leftX,
                    -leftY,
                    -rightX,
                    speedLimit
            );

            this.launcherLeft.setPower(launchPower);
            this.launcherRight.setPower(launchPower);

            if (this.gamepad1.b) {
                this.launcherServo.setPower(0.7);
            } else {
                this.launcherServo.setPower(0.0);
            }

            if (this.gamepad1.x && this.detector.hasTag(RED_GOAL)) {
                this.drivetrain.center(5.0, () -> {
                    Optional<AprilTagDetection> goal = this.detector.getTag(RED_GOAL);
                    return goal.map(aprilTagDetection -> aprilTagDetection.ftcPose.bearing).orElse(0.0);
                });
            }

        }

    }

    private void hardwareInit() {

        this.launcherLeft = hardwareMap.get(DcMotor.class, "leftLauncher");
        this.launcherRight = hardwareMap.get(DcMotor.class, "rightLauncher");
        this.launcherServo = hardwareMap.get(CRServo.class, "launcherServo");

        this.drivetrain = new Drivetrain(
                hardwareMap.get(DcMotor.class, "backRight"),
                hardwareMap.get(DcMotor.class, "backLeft"),
                hardwareMap.get(DcMotor.class, "frontRight"),
                hardwareMap.get(DcMotor.class, "frontLeft")
        );

        this.launcherLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        this.launcherServo.setDirection(DcMotorSimple.Direction.REVERSE);

        this.tagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        this.webcam = hardwareMap.get(WebcamName.class, "webcam");
        this.portal = new VisionPortal.Builder()
                .setCamera(webcam)
                .addProcessor(tagProcessor)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(640, 360))
                .build();

        this.detector = new TagDetector(this.tagProcessor, this.portal);

    }

}
