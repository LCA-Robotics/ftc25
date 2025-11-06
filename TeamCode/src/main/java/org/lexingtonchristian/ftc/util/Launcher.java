package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.lexingtonchristian.ftc.PrimaryAuto;

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

    public void servo(boolean on) {
        this.servo.setPower(on ? 1.0 : 0.0);
    }

    public void launch(double power) {
        this.spin(power);
        this.sleep(1500);
        this.servo(true);
        this.zero();
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
        sleep(500); // 500 + 750 = 1250ms wait for first iteration
            for (int i = shots; i > 0; i--) {
                sleep(750);
                this.servo.setPower(1.0);
                sleep(500);
                this.servo.setPower(0.0);
                numBalls--;
                if (i != 1) continue;
                this.zero();
            }
    }

}
