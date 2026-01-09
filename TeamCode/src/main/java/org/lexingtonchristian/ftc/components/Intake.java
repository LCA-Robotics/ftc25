package org.lexingtonchristian.ftc.components;

import static org.lexingtonchristian.ftc.util.Constants.CYCLE_TIME;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {

    private final DcMotorEx motor;

    public static int numBalls = 3;

    public Intake(DcMotor intakeMotor) {
        this.motor = (DcMotorEx) intakeMotor;
    }

    public void cycle(int cycles) {
        double motorPower;
        while (cycles > 0) {
            if (cycles > 1) {
                motorPower = 1.0;
            } else {
                motorPower = 0.;
            }
            double timeMultiplier = 1 / motorPower;
            this.motor.setPower(motorPower);
            this.sleep(CYCLE_TIME * (long) timeMultiplier); // TODO: Find accurate cycle time for one ball
            cycles--;
        }
        this.zero();
    }

    public void run(double power) {
        this.motor.setPower(power);
    }

    public void zero() {
        this.motor.setPower(0.0);
    }

    public void eject(int amount) {
        this.motor.setPower(-1.0);
        this.sleep(CYCLE_TIME * amount);
        this.zero();
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
