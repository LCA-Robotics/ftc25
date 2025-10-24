package org.lexingtonchristian.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.lexingtonchristian.ftc.util.MathHelper;

@TeleOp
public class SimpleDrive extends LinearOpMode {

    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor frontRight;

    @Override
    public void runOpMode() {

        this.backLeft = hardwareMap.get(DcMotor.class, "leftRear");
        this.frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        this.backRight = hardwareMap.get(DcMotor.class, "rightRear");
        this.frontRight = hardwareMap.get(DcMotor.class, "rightFront");

        waitForStart();

        while (this.opModeIsActive()) {

            double mult = this.gamepad1.right_bumper ? 0.2 : 0.5;

            double leftStickY = this.gamepad1.left_stick_y;
            double leftStickX = this.gamepad1.left_stick_x;
            double rightStickX = this.gamepad1.right_stick_x;

            double backLeftPower = MathHelper.clamp((leftStickX + leftStickY) - rightStickX, -1.0, 1.0);
            double backRightPower = MathHelper.clamp((leftStickX - leftStickY) + rightStickX, -1.0, 1.0);
            double frontLeftPower = MathHelper.clamp((leftStickX - leftStickY) - rightStickX, -1.0, 1.0);
            double frontRightPower = MathHelper.clamp((leftStickX + leftStickY) + rightStickX, -1.0, 1.0);

            this.backLeft.setPower(backLeftPower * mult);
            this.backRight.setPower(backRightPower * mult * -1);
            this.frontLeft.setPower(frontLeftPower * mult);
            this.frontRight.setPower(frontRightPower * mult * -1);

        }

    }



}
