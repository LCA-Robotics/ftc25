package org.lexingtonchristian.ftc.motor;

import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_D;
import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_F;
import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_I;
import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_P;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Motor {

    private final DcMotorEx raw;
    private final boolean usePIDF;

    public Motor(DcMotor motor, boolean usePIDF) {
        this.raw = (DcMotorEx) motor;
        this.usePIDF = usePIDF;
        if (usePIDF) this.raw.setVelocityPIDFCoefficients(
                DRIVETRAIN_P,
                DRIVETRAIN_I,
                DRIVETRAIN_D,
                DRIVETRAIN_F
        );
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
