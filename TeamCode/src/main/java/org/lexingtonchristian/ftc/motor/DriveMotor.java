package org.lexingtonchristian.ftc.motor;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * A wrapper class for a {@link com.qualcomm.robotcore.hardware.DcMotor} that provides multiple
 * extra utilities to the end user, such as rotating a certain number of degrees or driving
 * a particular distance based on the diameter of the wheel attached to the motor.
 */
public class DriveMotor {

    /**
     * Private reference to the bare motor
     */
    private final DcMotor motor;

    /**
     * Wheel diameter in centimeters
     */
    private final double diameter;

    /**
     * Number of motor ticks per revolution
     */
    private final double ticksPerRev;

    /**
     * @param motor the bare motor
     * @param diameter diameter of the wheel in centimeters
     */
    public DriveMotor(DcMotor motor, double diameter) {
        this.motor = motor;
        this.diameter = diameter;
        this.ticksPerRev = this.motor.getMotorType().getTicksPerRev();
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
     * @param degrees angle to turn in degrees
     */
    public void rotate(double degrees) {
        int current = this.motor.getCurrentPosition();
        int clicks = (int) ((degrees / 360) * ticksPerRev);
        this.motor.setTargetPosition(current + clicks);
    }

}
