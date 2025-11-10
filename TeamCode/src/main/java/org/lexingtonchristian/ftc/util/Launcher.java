package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Launcher {

    private final DcMotorEx left;
    private final DcMotorEx right;

    private final CRServo servo;

    public Launcher(DcMotor left, DcMotor right, CRServo servo) {

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

    public void servo(double power) {
        this.servo.setPower(power);
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
        this.sleep(1050); // 750 + 750 = 1500ms wait for first iteration
        for (int i = 0; i < shots; i++) {
            this.sleep(850);
            this.servo(1.0);
            this.sleep(400);
            this.servo(-1.0);
            this.sleep(250);
            this.servo(0.0);
            numBalls--;
            if (i != shots - 1) continue;
            this.zero();
        }
    }

}
