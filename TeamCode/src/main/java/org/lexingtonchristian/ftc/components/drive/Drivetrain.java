package org.lexingtonchristian.ftc.components.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.util.Direction;
import org.lexingtonchristian.ftc.util.MathHelper;
import org.lexingtonchristian.ftc.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 *     Provides several utilities for working with a Mecanum drivetrain, such as:
 *     <ul>
 *         <li>{@link Drivetrain#move(double, double, double, double)}</li>
 *         <li>{@link Drivetrain#turn(double)}</li>
 *         <li>{@link Drivetrain#center(double, Supplier)}</li>
 *         <li>{@link Drivetrain#distance(double, Supplier)}</li>
 *     </ul>
 * </p>
 */
public class Drivetrain {

    private final DcMotorEx backRight;
    private final DcMotorEx backLeft;

    private final DcMotorEx frontRight;
    private final DcMotorEx frontLeft;

    private final List<DcMotorEx> motors;

    public Drivetrain(DcMotor backRight, DcMotor backLeft, DcMotor frontRight, DcMotor frontLeft) {

        this.backRight  = (DcMotorEx) backRight;
        this.backLeft   = (DcMotorEx) backLeft;
        this.frontRight = (DcMotorEx) frontRight;
        this.frontLeft  = (DcMotorEx) frontLeft;

        this.motors = new ArrayList<>();
        this.motors.add(this.backRight);
        this.motors.add(this.backLeft);
        this.motors.add(this.frontRight);
        this.motors.add(this.frontLeft);

        this.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.backRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void center(double tolerance, Supplier<Double> bearing) {
        while (tolerance < bearing.get() || bearing.get() < -tolerance) {
            this.turn(MathHelper.round(bearing.get() * -0.02, 2));
        }
        this.zero();
    }

    public void align(double tolerance, Supplier<Double> x) {
        while (tolerance < x.get() || x.get() < -tolerance) {
            double speed = MathHelper.clamp(x.get() * 0.01, 0.0, 0.55);
            this.move(0.0, speed, 0.0);
        }
        this.zero();
    }

    public void distance(double distance, Supplier<Double> range) {
        double error = range.get() - distance;
        while (Math.abs(error) > 1.0) {
            double speed = MathHelper.clamp(error * 0.01, 0.0, 0.55);
            this.move(speed, 0.0, 0.0);
            error = range.get() - distance;
        }
        this.zero();
    }

    /**
     *
     * <p>
     *     Allows rotation of the robot without the need to explicitly set the values of {@code x}
     *     and {@code y} to zero.
     * </p>
     * <p>
     *     A positive {@code yaw} value indicates counterclockwise rotation, and vice versa.
     * </p>
     *
     * @param yaw turn power
     * @see Drivetrain#center(double, Supplier)
     * @see Drivetrain#move(double, double, double, double)
     */
    public void turn(double yaw) {
        this.move(0.0, 0.0, yaw);
    }

    /**
     * <p>
     *     Allows rotation of the robot at {@code velocity} ticks per revolution.
     * </p>
     * <p>
     *     A positive {@code velocity} value indicates counterclockwise rotation, and vice versa.
     * </p>
     *
     * @param degrees degrees to turn
     * @see Drivetrain#center(double, Supplier)
     * @see Drivetrain#move(double, double, double, double)
     */
    public void rotate(double degrees) {

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int position = Constants.inchesToTicks((degrees / 360.0) * Constants.CIRCUMFERENCE);
        this.backLeft.setTargetPosition(position);
        this.frontLeft.setTargetPosition(position);
        this.backRight.setTargetPosition(-position);
        this.frontRight.setTargetPosition(-position);

        setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitToPosition(10);

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    public void move(double x, double y, double yaw) {
        this.move(x, y, yaw, 1.0);
    }



    /**
     * <p>
     *     Takes forward, strafe, and rotation, determines the correct power levels for each
     *     motor, and multiplies the value by {@code limit} for slower movement.
     * </p>
     * <p>
     *     A positive {@code x} value indicates forward movement, and vice versa. <br/>
     *     A positive {@code y} value indicates left movement, and vice versa. <br/>
     *     A positive {@code yaw} value indicates counterclockwise rotation, and vice versa.
     * </p>
     * <p>
     *     All motor powers are <b>normalized</b> before application – this means they are all
     *     divided by the value of whichever motor's power would be greatest after calculation – such
     *     that, were the back left motor the one with the highest power, its power would be
     *     normalized to {@code 1.0} and every other motor's power would be adjusted accordingly.
     * </p>
     *
     * @param x drive power
     * @param y strafe power
     * @param yaw turn power
     * @param limit speed multiplier
     * @see Drivetrain#turn(double)
     * @see Drivetrain#move(double, double, double)
     * @see Drivetrain#distance(double, Supplier)
     */
    public void move(double x, double y, double yaw, double limit) {

        double pBackRight   =  x - y - yaw;
        double pBackLeft    =  x + y + yaw;
        double pFrontRight  =  x + y - yaw;
        double pFrontLeft   =  x - y + yaw;

        // Normalize speeds
        double max = MathHelper.max(pBackRight, pBackLeft, pFrontRight, pFrontLeft);
        if (max > 1.0) {
            pBackRight  /= max;
            pBackLeft   /= max;
            pFrontRight /= max;
            pFrontLeft  /= max;
        }

        this.backRight.setPower(pBackRight * limit);
        this.backLeft.setPower(pBackLeft * limit);
        this.frontRight.setPower(pFrontRight * limit);
        this.frontLeft.setPower(pFrontLeft * limit);

    }

    public void drive(double distance) {

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.motors.forEach(motor -> {
            motor.setTargetPosition(Constants.inchesToTicks(distance * -1));
            motor.setPower(0.5);
        });

        setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitToPosition(10);

        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

    /**
     * Zeroes all movement for all motors.
     */
    public void zero() {
        this.backRight.setPower(0.0);
        this.backLeft.setPower(0.0);
        this.frontRight.setPower(0.0);
        this.frontLeft.setPower(0.0);
    }

    public void waitToPosition(int interval) {
        this.motors.forEach(motor ->
                Util.waitUntil(interval, () -> MathHelper.roughEqual(
                        motor.getCurrentPosition(),
                        motor.getTargetPosition(),
                        motor.getTargetPositionTolerance()
                ))
        );
    }

    public void setMode(DcMotor.RunMode mode) {
        this.motors.forEach(motor -> motor.setMode(mode));
    }

    public void setPIDFCoefficients(double p, double i, double d, double f) {
        this.motors.forEach(motor -> motor.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(p, i, d, f)
        ));
    }

}
