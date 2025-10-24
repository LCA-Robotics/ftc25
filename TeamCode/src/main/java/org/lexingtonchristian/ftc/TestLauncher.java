package org.lexingtonchristian.ftc;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.lexingtonchristian.ftc.util.MathHelper;

@TeleOp
public class TestLauncher extends LinearOpMode {

    private DcMotor launcherLeft;
    private DcMotor launcherRight;
    private CRServo launcherServo;

    @Override
    public void runOpMode() {

        this.launcherLeft = hardwareMap.get(DcMotor.class, "leftLauncher");
        this.launcherRight = hardwareMap.get(DcMotor.class, "rightLauncher");
        this.launcherServo = hardwareMap.get(CRServo.class, "launcherServo");

        this.launcherLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (this.opModeIsActive()) {

            double power = MathHelper.clamp(this.gamepad1.right_trigger, 0.0, 0.4);

            this.launcherLeft.setPower(power);
            this.launcherRight.setPower(power);



        }

    }

}
