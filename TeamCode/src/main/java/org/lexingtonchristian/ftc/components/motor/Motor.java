package org.lexingtonchristian.ftc.components.motor;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_D;
import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_F;
import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_I;
import static org.lexingtonchristian.ftc.util.Constants.DRIVETRAIN_P;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.lexingtonchristian.ftc.util.MathHelper;
import org.lexingtonchristian.ftc.util.Util;

public class Motor {

    private final DcMotorEx raw;

    public Motor(DcMotor motor) {
        this.raw = (DcMotorEx) motor;
        setMode(STOP_AND_RESET_ENCODER);
        this.raw.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(
                        DRIVETRAIN_P,
                        DRIVETRAIN_I,
                        DRIVETRAIN_D,
                        DRIVETRAIN_F
                )
        );
    }

    public void toPosition(int position, double power) {

        this.raw.setTargetPosition(this.raw.getCurrentPosition() + position);
        setPower(power);
        setMode(RUN_TO_POSITION);

        waitUntilPosition(10);

        setPower(0.0);

    }

    public void zero() {
        this.raw.setPower(0.0);
        setMode(STOP_AND_RESET_ENCODER);
    }

    public void setPower(double power) {
        this.raw.setPower(power);
    }

    public void setVelocity(double velocity) {
        this.raw.setVelocity(velocity);
    }

    public void setMode(DcMotor.RunMode mode) {
        this.raw.setMode(mode);
    }

    public void waitUntilPosition(int interval) {
        Util.waitUntil(interval, () -> MathHelper.roughEqual(
                this.raw.getCurrentPosition(),
                this.raw.getTargetPosition(),
                this.raw.getTargetPositionTolerance()
        ));
    }

}
