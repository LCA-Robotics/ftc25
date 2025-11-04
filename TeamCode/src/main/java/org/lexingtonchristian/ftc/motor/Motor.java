package org.lexingtonchristian.ftc.motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Motor {

    private final DcMotorEx raw;

    public Motor(DcMotor motor) {
        this.raw = (DcMotorEx) motor;
    }

    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
        this.raw.setVelocityPIDFCoefficients(p, i, d, f);
    }

    public void setVelocity(double velocity) {
        this.raw.setVelocity(velocity);
    }

}
