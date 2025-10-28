package org.lexingtonchristian.ftc;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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

        initialize();

        while (this.opModeIsActive()) {

            double power = MathHelper.clamp(this.gamepad1.right_trigger, 0.0, 0.4);

            this.enhanced(this.launcherLeft).setVelocity(power * 2550);
            this.enhanced(this.launcherRight).setVelocity(power * 2550);

        }

    }

    private void initialize() {
        this.enhanced(this.launcherLeft).setVelocityPIDFCoefficients(4, 0.5, 0, 11.7);
        this.enhanced(this.launcherRight).setVelocityPIDFCoefficients(4, 0.5, 0, 11.7);
        this.launcherLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.launcherRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private DcMotorEx enhanced(DcMotor motor) {
        return (DcMotorEx) motor;
    }

}
