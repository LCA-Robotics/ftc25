package org.lexingtonchristian.ftc.op.tele;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "Primary", group = "Competition")
public class PrimaryTeleOp extends OpMode {

    private Gamepad currentGamepad;
    private Gamepad previousGamepad;

    @Override
    public void init() {

        this.currentGamepad.copy(this.gamepad1);

    }

    @Override
    public void loop() {

        this.previousGamepad.copy(this.currentGamepad);
        this.currentGamepad.copy(this.gamepad1);

        handleState();




    }

    public void handleState() {



    }

}
