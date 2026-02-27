package org.lexingtonchristian.ftc.components.hardware;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

import org.lexingtonchristian.ftc.snapshot.Device;
import org.lexingtonchristian.ftc.util.Constants.MotorType;
import org.lexingtonchristian.ftc.util.MathHelper;
import org.lexingtonchristian.ftc.util.Util;

public class Motor implements Device<Double> {

    private final DcMotorEx raw;
    private final MotorType type;

    public Motor(DcMotor motor, MotorType type) {
        this.raw = (DcMotorEx) motor;
        this.type = type;
        setMode(STOP_AND_RESET_ENCODER);
    }

    //<editor-fold desc="Getters & setters">

    public void setPower(double power) {
        this.raw.setPower(power);
    }
    public double getPower() {
        return this.raw.getPower();
    }

    public void setVelocity(double velocity) {
        this.raw.setVelocity(velocity);
    }
    public double getVelocity() {
        return this.raw.getVelocity();
    }

    public void setGoal(int position) {
        this.raw.setTargetPosition(position);
    }
    public int getGoal() {
        return this.raw.getTargetPosition();
    }

    public int getPosition() {
        return this.raw.getCurrentPosition();
    }

    public void setMode(RunMode mode) {
        this.raw.setMode(mode);
    }
    public RunMode getMode() {
        return this.raw.getMode();
    }

    public void setDirection(Direction direction) {
        this.raw.setDirection(direction);
    }
    public Direction getDirection() {
        return this.raw.getDirection();
    }

    //</editor-fold>

    //<editor-fold desc="Utilities">

    public void zero() {
        this.raw.setPower(0.0);
        setMode(STOP_AND_RESET_ENCODER);
    }

    public void waitUntilPosition(int interval) {
        Util.waitUntil(interval, () -> MathHelper.roughEqual(
                this.raw.getCurrentPosition(),
                this.raw.getTargetPosition(),
                this.raw.getTargetPositionTolerance()
        ));
    }

    //</editor-fold>

    @Override
    public String getName() {
        return type.name;
    }

    @Override
    public Double getValue() {
        return this.raw.getPower();
    }

}
