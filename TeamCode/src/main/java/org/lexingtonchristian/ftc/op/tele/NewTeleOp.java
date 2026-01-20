package org.lexingtonchristian.ftc.op.tele;

import org.lexingtonchristian.ftc.op.CoreOpMode;
import org.lexingtonchristian.ftc.util.Constants;

public class NewTeleOp extends CoreOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();

        waitForStart();

        if (this.gamepad1.x) this.drivetrain.align(Constants.RED_GOAL, 3.0);



    }


}
