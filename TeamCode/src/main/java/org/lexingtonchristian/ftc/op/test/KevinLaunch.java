package org.lexingtonchristian.ftc.op.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.lexingtonchristian.ftc.op.CoreOpMode;

@TeleOp
public class KevinLaunch extends CoreOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();

        waitForStart();

        while (opModeIsActive()) {

            this.launcher.spin(this.gamepad1.right_trigger > 0 ? 2550 : 0);
            this.launcher.servo(this.gamepad1.b ? 1.0 : 0.0);

        }

    }

}
