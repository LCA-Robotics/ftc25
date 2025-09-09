package com.lexingtonchristian.ftc.motor;

public class Drive {

    private final Side sideL;
    private final Side sideR;

    private final double robotDiameter;

    public Drive(Side left, Side right, double robotDiameter) {
        this.sideL = left;
        this.sideR = right;
        this.robotDiameter = robotDiameter;
    }

    /**
     * Drives the robot a certain distance with both sides
     *
     * @param distance distance to drive in centimeters
     */
    public void drive(double distance) {
        this.sideR.drive(distance);
        this.sideL.drive(distance);
    }

    /**
     * Turn the robot in place by a certain angle
     *
     * @param degrees angle to turn in degrees
     */
    public void turnStationary(double degrees) {
        double distance = (degrees / 360.0) * robotDiameter;
        if (degrees > 0) {
            this.sideR.drive(distance);
            this.sideL.drive(-distance);
        } else {
            this.sideR.drive(-distance);
            this.sideL.drive(distance);
        }
    }

    public static class Side {

        /**
         * References to all motors for this side of the drive configuration
         */
        private final DriveMotor[] motors;

        /**
         * @param motors all motors on this side of the robot
         */
        public Side(DriveMotor... motors) {
            this.motors = motors;
        }

        /**
         * Drives a given distance in centimeters using all motors
         *
         * @param distance distance in centimeters to drive
         */
        public void drive(double distance) {
            for (DriveMotor motor : this.motors) {
                motor.driveDistance(distance);
            }
        }

    }

}
