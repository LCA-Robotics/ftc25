package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.text.DecimalFormat;
import java.util.function.Supplier;

public class Drivetrain {

    private static final DecimalFormat TWO_DECIMALS = new DecimalFormat("#.00");

    private final DcMotorEx backRight;
    private final DcMotorEx backLeft;

    private final DcMotorEx frontRight;
    private final DcMotorEx frontLeft;

    public Drivetrain(DcMotor backRight, DcMotor backLeft, DcMotor frontRight, DcMotor frontLeft) {

        this.backRight  = (DcMotorEx) backRight;
        this.backLeft   = (DcMotorEx) backLeft;
        this.frontRight = (DcMotorEx) frontRight;
        this.frontLeft  = (DcMotorEx) frontLeft;

        this.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.backRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void center(double tolerance, Supplier<Double> bearing) {
        while (tolerance < bearing.get() || bearing.get() < -tolerance) {
            rotate(Double.parseDouble(TWO_DECIMALS.format(bearing.get() * -0.02)));
        }
        zero();
    }

    public void distance(double distance, Supplier<Double> range) {
        double error = range.get() - distance;
        while (Math.abs(error) > 1.0) {
            double speed = MathHelper.clamp(error * 0.01, 0.0, 0.55);
            move(speed, 0.0, 0.0);
            error = range.get() - distance;
        }
        zero();
    }

    public void rotate(double yaw) {
        move(0.0, 0.0, yaw);
    }

    public void move(double x, double y, double yaw) {
        move(x, y, yaw, 1.0);
    }

    public void move(double x, double y, double yaw, double limit) {

        double pBackRight   = y - x + yaw;
        double pBackLeft    = y + x - yaw;
        double pFrontRight  = y + x + yaw;
        double pFrontLeft   = y - x - yaw;

        // Normalize speeds
        double max = MathHelper.max(pBackRight, pBackLeft, pFrontRight, pFrontLeft);
        if (max > 1.0) {
            pBackRight  /= max;
            pBackLeft   /= max;
            pFrontRight /= max;
            pFrontLeft  /= max;
        }

        this.backRight.setPower(pBackRight * limit);
        this.backLeft.setPower(pBackLeft * limit);
        this.frontRight.setPower(pFrontRight * limit);
        this.frontLeft.setPower(pFrontLeft * limit);

    }

    public void zero() {
        this.backRight.setPower(0.0);
        this.backLeft.setPower(0.0);
        this.frontRight.setPower(0.0);
        this.frontLeft.setPower(0.0);
    }

    public void accelerate(int startPercent, int endPercent, XYDirection direction) {
        for (int i = startPercent; i <= endPercent; i++) {
            double drivePower = i * 0.01;
            switch (direction) {
                case X_DIRECTION:
                    move(drivePower, 0, 0);
                    break;
                case Y_DIRECTION:
                    move(0, drivePower, 0);
                    break;
            }
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void decelerate(int startPercent, int endPercent, XYDirection direction) {
        for (int i = startPercent; i >= endPercent; i--) {
            double drivePower = i * 0.01;
            switch (direction) {
                case X_DIRECTION:
                    move(drivePower, 0, 0);
                case Y_DIRECTION:
                    move(0, drivePower, 0);
            }
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public enum XYDirection {
        X_DIRECTION,
        Y_DIRECTION
    }

}
