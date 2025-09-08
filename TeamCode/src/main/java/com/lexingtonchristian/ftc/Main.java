package com.lexingtonchristian.ftc;

import com.lexingtonchristian.ftc.motor.DriveMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp
public class Main extends LinearOpMode {

    private DriveMotor leftMotor;
    private DriveMotor rightMotor;

    private IMU imu;

    @Override
    public void runOpMode() {

        leftMotor = new DriveMotor(hardwareMap.get(DcMotor.class, "left_motor"), 3.0);
        rightMotor = new DriveMotor(hardwareMap.get(DcMotor.class, "right_motor"), 3.0);

        imu = hardwareMap.get(IMU.class, "imu");

        waitForStart();

        leftMotor.rotate(90.0);
        rightMotor.rotate(90.0);

    }

}
