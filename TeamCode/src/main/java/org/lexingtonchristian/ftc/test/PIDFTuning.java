package org.lexingtonchristian.ftc.test;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Drivetrain;
import org.lexingtonchristian.ftc.util.Constants;

import java.util.HashMap;
import java.util.Map;

@TeleOp(name = "PIDF Tuner", group = "Utility")
public class PIDFTuning extends LinearOpMode {

    private Drivetrain drivetrain;

    public static final String[] NAMES = new String[]{
            "Proportional",
            "Integral",
            "Derivative",
            "Feedforward"
    };
    public static final double[] COEFFICIENTS = new double[]{20.0, 0.0, 5.0, 15.0};

    @Override
    public void runOpMode() throws InterruptedException {

        this.drivetrain = Constants.initDrivetrain(hardwareMap);

        waitForStart();

        int selected = 0;
        while(this.opModeIsActive()) {

            if (this.gamepad1.left_bumper) this.drivetrain.zero();

            if (this.gamepad1.y) selected = Math.abs((selected + 1) % 4);

            if (this.gamepad1.x) COEFFICIENTS[selected] -= 0.1;
            if (this.gamepad1.b) COEFFICIENTS[selected] += 0.1;

            this.drivetrain.setPIDFCoefficients(
                    COEFFICIENTS[0],
                    COEFFICIENTS[1],
                    COEFFICIENTS[2],
                    COEFFICIENTS[3]
            );

            this.telemetry.addData("Selected", NAMES[selected]);
            this.telemetry.addData("P", COEFFICIENTS[0]);
            this.telemetry.addData("I", COEFFICIENTS[1]);
            this.telemetry.addData("D", COEFFICIENTS[2]);
            this.telemetry.addData("F", COEFFICIENTS[3]);
            this.updateTelemetry(this.telemetry);

            if (this.gamepad1.a) this.drivetrain.drive(12.0);

            sleep(100);

        }

    }

}
