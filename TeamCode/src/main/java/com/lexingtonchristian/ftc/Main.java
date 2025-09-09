package com.lexingtonchristian.ftc;

import com.lexingtonchristian.ftc.motor.Drive;
import com.lexingtonchristian.ftc.motor.DriveMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

@TeleOp
public class Main extends LinearOpMode {

    private DriveMotor backLeft;
    private DriveMotor frontLeft;
    private DriveMotor backRight;
    private DriveMotor frontRight;

    private Drive.Side left;
    private Drive.Side right;
    private Drive drive;

    private IMU imu;

    @Override
    public void runOpMode() {

        double wheelDiameter = 3.0;

        this.backLeft = new DriveMotor(hardwareMap.get(DcMotor.class, "back_left"), wheelDiameter);
        this.frontLeft = new DriveMotor(hardwareMap.get(DcMotor.class, "front_left"), wheelDiameter);
        this.backRight = new DriveMotor(hardwareMap.get(DcMotor.class, "back_right"), wheelDiameter);
        this.frontRight = new DriveMotor(hardwareMap.get(DcMotor.class, "front_right"), wheelDiameter);

        this.left = new Drive.Side(this.backLeft, this.frontLeft);
        this.right = new Drive.Side(this.backRight, this.frontRight);

        this.drive = new Drive(this.left, this.left, 100.0);

        imu = hardwareMap.get(IMU.class, "imu");

        waitForStart();

        this.drive.turnStationary(90.0);

    }

}
