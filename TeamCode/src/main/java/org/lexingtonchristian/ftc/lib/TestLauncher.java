package org.lexingtonchristian.ftc.lib;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestLauncher extends LinearOpMode {
    private DcMotor launcherLeft;
    private DcMotor launcherRight;
    private Servo launcherServo;

    @Override
    public void runOpMode() {

        this.launcherLeft = hardwareMap.get(DcMotor.class, "launcherLeft");
        this.launcherRight = hardwareMap.get(DcMotor.class, "launcherRight");
        this.launcherServo = hardwareMap.get(Servo.class, "launcherServo");

        this.launcherLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        launcherLeft.setPower(0.5);
        launcherRight.setPower(0.5);

        Servo.Direction launch = this.gamepad1.x ? Servo.Direction.FORWARD : null;

        launcherServo.setDirection(launch);
    }
}
