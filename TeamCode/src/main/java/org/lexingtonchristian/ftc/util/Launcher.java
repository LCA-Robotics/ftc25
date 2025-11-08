package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.lexingtonchristian.ftc.motor.Motor;

public class Launcher {

    private final DcMotorEx left;
    private final DcMotorEx right;

    private final Servo servo;

    public Launcher(DcMotor left, DcMotor right, Servo servo) {

        this.left = (DcMotorEx) left;
        this.right = (DcMotorEx) right;
        this.servo = servo;

        this.left.setDirection(DcMotorSimple.Direction.REVERSE);

        this.left.setVelocityPIDFCoefficients(4, 0.5, 0, 11.7);
        this.right.setVelocityPIDFCoefficients(4, 0.5, 0, 11.7);

    }

    public int numBalls = 3;

    public void spin(double velocity) {
        this.left.setVelocity(velocity);
        this.right.setVelocity(velocity);
    }

    public void zero() {
        this.left.setPower(0.0);
        this.right.setPower(0.0);
    }

    public void servo(boolean closed) {
        this.servo.setPosition(closed ? Constants.LAUNCHER_CLOSED : Constants.LAUNCHER_OPEN);
    }

    public void launch(double power) {
        this.spin(power);
        this.sleep(1500);
        this.servo(true);
        this.sleep(500);
        this.zero();
        this.servo(false);
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void launch(double ticks, int shots) {
        this.spin(ticks);
        sleep(750); // 750 + 750 = 1500ms wait for first iteration
        for (int i = 0; i < shots; i++) {
            this.sleep(750);
            this.servo(true);
            this.sleep(500);
            this.servo(false);
            numBalls--;
            if (i != shots - 1) continue;
            this.zero();
        }
    }

}
