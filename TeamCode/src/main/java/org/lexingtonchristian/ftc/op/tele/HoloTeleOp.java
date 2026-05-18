package org.lexingtonchristian.ftc.op.tele;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.drive.Holonomic;

public class HoloTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Holonomic drivetrain = new Holonomic(hardwareMap);

        waitForStart();

        double SCALAR = 0.85;
        while (opModeIsActive()) {

            double x = this.gamepad1.left_stick_x;
            double y = this.gamepad1.left_stick_y;
            double r = this.gamepad1.right_stick_x;

            drivetrain.move(x, y, r, SCALAR);

        }

    }

}
