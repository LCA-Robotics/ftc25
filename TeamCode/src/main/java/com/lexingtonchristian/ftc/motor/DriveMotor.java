package com.lexingtonchristian.ftc.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveMotor {

    private static final int CLICKS_PER_ROTATION = 28;

    /**
     * Private reference to the bare motor
     */
    private final DcMotor motor;

    /**
     * Wheel diameter in centimeters
     */
    private final double diameter;

    /**
     * @param motor the bare motor
     * @param diameter diameter of the wheel in centimeters
     */
    public DriveMotor(DcMotor motor, double diameter) {
        this.motor = motor;
        this.diameter = diameter;
        this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Moves the robot a certain distance.
     *
     * @param distance distance in centimeters for the motor to drive
     */
    public void driveDistance(double distance) {
        double rotations = distance / (Math.PI * diameter);
        double degrees = 360.0 * rotations;
        rotate(degrees);
    }

    /**
     * Turns the motor a number of degrees.
     *
     * @param degrees number of degrees to turn the motor
     */
    public void rotate(double degrees) {
        int current = this.motor.getCurrentPosition();
        int clicks = (int) (degrees / 360) * CLICKS_PER_ROTATION;
        this.motor.setTargetPosition(current + clicks);
    }

}
