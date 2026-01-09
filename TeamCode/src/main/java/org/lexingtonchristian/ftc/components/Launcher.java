package org.lexingtonchristian.ftc.components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/** <p>
 *  Provides utilities for easily controlling a two-flywheel launcher, including:
 *      <ul>
 *          <li>{@link Launcher#spin(double)}</li>
 *          <li>{@link Launcher#zero()}</li>
 *          <li>{@link Launcher#servo(double)}</li>
 *          <li>{@link Launcher#launch(double, int)}</li>
 *      </ul>
 *  </p>
 */
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

    /**
     * <p>
     *     Allows the flywheels to spin continuously.
     * </p>
     * <p>
     *     If {@code velocity} is positive, the flywheels should be rotating outwardly.
     * </p>
     */
    public void spin(double velocity) {
        this.left.setVelocity(velocity);
        this.right.setVelocity(velocity);
    }

    /**
     * <p> Turns off the flywheel motors </p>
     */
    public void zero() {
        this.left.setPower(0.0);
        this.right.setPower(0.0);
    }

    /**
     * <p> Rotates the launcher servo continuously at {@code power} power. </p>
     * <p> If {@code power} is positive, the servo should rotate in a counterclockwise direction. </p>
     */
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

    /**
     * <p> Spins flywheels at {@code ticks} velocity to shoot {@code shots} balls. </p>
     */
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
            Intake.numBalls--;
            if (i != shots - 1) continue;
            this.zero();
        }
    }

}
