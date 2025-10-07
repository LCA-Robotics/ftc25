package org.lexingtonchristian.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class Main extends LinearOpMode {

    private DcMotor backLeft;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor frontRight;

    @Override
    public void runOpMode() {

        this.backLeft = hardwareMap.get(DcMotor.class, "back_left");
        this.frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        this.backRight = hardwareMap.get(DcMotor.class, "back_right");
        this.frontRight = hardwareMap.get(DcMotor.class, "front_right");

        waitForStart();


        double mult = this.gamepad1.right_bumper ? 0.2 : 1.0;

        double leftStickY = this.gamepad1.left_stick_y;
        double leftStickX = this.gamepad1.left_stick_x;
        double rightStickX = this.gamepad1.right_stick_x;

        while (this.opModeIsActive()) {
            double backLeftPower = clamp((leftStickY - leftStickX) + rightStickX, -1.0, 1.0);
            double backRightPower = clamp((leftStickY + leftStickX) - rightStickX, -1.0, 1.0);
            double frontLeftPower = clamp((leftStickY + leftStickX) + rightStickX, -1.0, 1.0);
            double frontRightPower = clamp((leftStickY - leftStickX) - rightStickX, -1.0, 1.0);

            this.backLeft.setPower(backLeftPower * mult);
            this.backRight.setPower(backRightPower * mult);
            this.frontLeft.setPower(frontLeftPower * mult);
            this.frontRight.setPower(frontRightPower * mult);


        }
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, (Math.min(val, max)));
    }

}
