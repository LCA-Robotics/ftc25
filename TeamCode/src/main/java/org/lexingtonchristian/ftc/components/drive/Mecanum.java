package org.lexingtonchristian.ftc.components.drive;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.motor.Motor;
import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.util.MathHelper;

import java.util.HashMap;
import java.util.Map;

public class Mecanum {

    private final Motor backLeft;
    private final Motor frontLeft;
    private final Motor backRight;
    private final Motor frontRight;
    private final Map<MotorType, Motor> motors;

    private TagDetector detector;

    public Mecanum(HardwareMap map) {

        this.motors = new HashMap<>();

        this.backLeft = registerMotor(MotorType.BACK_LEFT, map);
        this.frontLeft = registerMotor(MotorType.FRONT_LEFT, map);
        this.backRight = registerMotor(MotorType.BACK_RIGHT, map);
        this.frontRight = registerMotor(MotorType.FRONT_RIGHT, map);

        this.detector = new TagDetector(map.get(WebcamName.class, Constants.WEBCAM_NAME));

    }

    public void align(int tag, double range) {
        double tolerance = 3.0;
        while (
                tolerance < detector.getBearing(tag) || detector.getBearing(tag) < -tolerance ||
                range + tolerance < detector.getRange(tag) || detector.getRange(tag) < range - tolerance ||
                tolerance < detector.getOffset(tag) || detector.getOffset(tag) < -tolerance
        ) {
            move(
                    MathHelper.round((detector.getRange(tag) - range) * 0.02, 2),
                    MathHelper.round(detector.getOffset(tag) * -0.02, 2),
                    MathHelper.round(detector.getBearing(tag) * -0.02, 2),
                    0.65
            );
        }
        zero();
    }

    public void move(double x, double y, double yaw, double limit) {

        double pBackLeft   = x + y + yaw;
        double pFrontLeft  = x - y + yaw;
        double pBackRight  = x - y - yaw;
        double pFrontRight = x + y - yaw;

        double max = MathHelper.max(pBackLeft, pFrontLeft, pBackRight, pFrontRight);
        if (max > 1.0) {
            pBackLeft   /= max;
            pFrontLeft  /= max;
            pBackRight  /= max;
            pFrontRight /= max;
        }

        this.backLeft.setPower(pBackLeft * limit);
        this.frontLeft.setPower(pFrontLeft * limit);
        this.backRight.setPower(pBackRight * limit);
        this.frontRight.setPower(pFrontRight * limit);

    }

    //<editor-fold desc="Encoder movement">

    public void drive(double distance) {
        this.motors.forEach((type, motor) -> motor.toPosition(
                Constants.inchesToTicks(distance * -1.0),
                0.75
        ));
    }

    public void strafe(double distance) {
        int pos = Constants.inchesToTicks(distance);
        this.backLeft.toPosition(-pos, 0.75);
        this.frontLeft.toPosition(pos, 0.75);
        this.backRight.toPosition(pos, 0.75);
        this.frontRight.toPosition(-pos, 0.75);
    }

    public void turn(double degrees) {
        int pos = Constants.inchesToTicks((degrees / 360.0) * Constants.CIRCUMFERENCE);
        this.backLeft.toPosition(pos, 0.75);
        this.frontLeft.toPosition(pos, 0.75);
        this.backRight.toPosition(-pos, 0.75);
        this.frontRight.toPosition(-pos, 0.75);
    }

    //</editor-fold>

    public void zero() {
        this.motors.forEach((type, motor) -> motor.zero());
    }

    public void zero(MotorType motor) {
        this.motors.get(motor).zero();
    }

    private Motor registerMotor(MotorType type, HardwareMap map) {
        return this.motors.put(
                type,
                new Motor(map.get(DcMotorEx.class, type.name))
        );
    }

    public enum MotorType {

        BACK_LEFT("backLeft"),
        FRONT_LEFT("frontLeft"),
        BACK_RIGHT("backRight"),
        FRONT_RIGHT("frontRight");

        public final String name;

        MotorType(String name) {
            this.name = name;
        }

    }

}
