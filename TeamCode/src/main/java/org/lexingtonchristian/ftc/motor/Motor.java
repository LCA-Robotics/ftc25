package org.lexingtonchristian.ftc.motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import static org.lexingtonchristian.ftc.util.Constants.*;

public class Motor {

    private final DcMotorEx raw;
    private final boolean usePIDF;

    public Motor(DcMotor motor, boolean usePIDF) {
        this.raw = (DcMotorEx) motor;
        this.usePIDF = usePIDF;
        if (usePIDF) this.raw.setVelocityPIDFCoefficients(P, I, D, F);
    }

    public void setPower(double power) {
        if (usePIDF) throw new UnsupportedOperationException("Cannot set power on a PIDF motor!");
        this.raw.setPower(power);
    }

    public void setVelocity(double velocity) {
        if (!usePIDF) throw new UnsupportedOperationException("Cannot set velocity on a non-PIDF motor!");
        this.raw.setVelocity(velocity);
    }

}
