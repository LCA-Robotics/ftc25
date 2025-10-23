package org.lexingtonchristian.ftc.lib;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class TestLauncher extends LinearOpMode {
    private DcMotor launcherLeft;
    private DcMotor launcherRight;

    @Override
    public void runOpMode() {

        this.launcherLeft = hardwareMap.get(DcMotor.class, "launcherLeft");
        this.launcherRight = hardwareMap.get(DcMotor.class, "launcherRight");

        this.launcherLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        launcherLeft.setPower(this.gamepad1.right_trigger);
        launcherRight.setPower(this.gamepad1.right_trigger);

    }
}
