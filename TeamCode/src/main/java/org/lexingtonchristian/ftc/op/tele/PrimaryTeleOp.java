package org.lexingtonchristian.ftc.op.tele;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.lexingtonchristian.ftc.util.MathHelper;

import static java.lang.Math.*;

@TeleOp(name = "Primary", group = "Competition")
public class PrimaryTeleOp extends OpMode {

    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor frontRight;

    private IMU imu;

    private Gamepad currentGamepad = new Gamepad();
    private Gamepad previousGamepad = new Gamepad();

    private boolean fieldCentric = false;

    @Override
    public void init() {

        this.backLeft = this.hardwareMap.get(DcMotor.class, "backLeft");
        this.frontLeft = this.hardwareMap.get(DcMotor.class, "frontLeft");
        this.backRight = this.hardwareMap.get(DcMotor.class, "backRight");
        this.frontRight = this.hardwareMap.get(DcMotor.class, "frontRight");

        this.backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = this.hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        )));

        currentGamepad.copy(this.gamepad1);

    }

    @Override
    public void loop() {

        previousGamepad.copy(this.currentGamepad);
        currentGamepad.copy(this.gamepad1);

        if (currentGamepad.options && !previousGamepad.options) imu.resetYaw();
        if (currentGamepad.back && !previousGamepad.back) fieldCentric = !fieldCentric;

        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double x0 = currentGamepad.left_stick_x * -1;
        double y0 = currentGamepad.left_stick_y * -1;
        double r0 = currentGamepad.right_stick_x;

        double x1 = x0;
        double y1 = y0;
        if (fieldCentric) {
            x1 = ( x0 * cos(heading) ) - ( y0 * sin(heading) );
            y1 = ( x0 * sin(heading) ) + ( y0 * cos(heading) );
        }

        double pBackLeft   = x1 - y1 - r0;
        double pFrontLeft  = x1 + y1 - r0;
        double pBackRight  = x1 + y1 + r0;
        double pFrontRight = x1 - y1 + r0;

        double max = MathHelper.max(pBackLeft, pFrontLeft, pBackRight, pFrontRight);
        if (max > 1.0) {
            pBackLeft   /= max;
            pFrontLeft  /= max;
            pBackRight  /= max;
            pFrontRight /= max;
        }

        backLeft.setPower(pBackLeft);
        frontLeft.setPower(pFrontLeft);
        backRight.setPower(pBackRight);
        frontRight.setPower(pFrontRight);

    }

}
