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

        while (this.opModeIsActive()) {

            double mult = this.gamepad1.right_bumper ? 0.2 : 1.0;

            double stickY = this.gamepad1.left_stick_y;
            double stickX = this.gamepad1.left_stick_x;

            double leftPower = clamp(stickX - stickY, -1.0, 1.0);
            double rightPower = clamp(stickX + stickY, -1.0, 1.0);

            this.backRight.setPower(rightPower * mult);
            this.backLeft.setPower(leftPower * mult);
        }
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, (Math.min(val, max)));
    }

}
