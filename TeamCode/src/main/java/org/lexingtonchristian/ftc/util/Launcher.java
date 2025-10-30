package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Launcher {

    private final DcMotorEx left;
    private final DcMotorEx right;

    private final CRServo servo;

    public Launcher(DcMotor left, DcMotor right, CRServo servo) {

        this.left = (DcMotorEx) left;
        this.right = (DcMotorEx) right;
        this.servo = servo;

        this.left.setDirection(DcMotorSimple.Direction.REVERSE);
        this.servo.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void spin(double power) {
        this.left.setPower(power);
        this.right.setPower(power);
    }

    public void zero() {
        this.left.setPower(0.0);
        this.right.setPower(0.0);
    }

    public void load() {
        this.servo.setPower(1.0);
        this.sleep(500);
        this.servo.setPower(0.0);
    }

    public void launch(double power) {
        this.spin(power);
        this.sleep(1500);
        this.load();
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
