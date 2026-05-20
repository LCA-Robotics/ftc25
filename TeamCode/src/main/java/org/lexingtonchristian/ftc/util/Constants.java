package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.lexingtonchristian.ftc.components.TagDetector;

/**
 * Lists constants for use across several classes in order to simplify code and reduce use of magic
 * constants.
 */
public class Constants {

    public static final double WHEEL_RADIUS = 2.0;
    public static final double TRACK_WIDTH = 13.25;
    public static final double WHEELBASE = 4.75;
    public static final double CIRCUMFERENCE = Math.hypot(TRACK_WIDTH, WHEELBASE) * Math.PI;

    public static final double GEAR_RATIO = 1;

    public static final double TICKS_PER_REV = 537.6;
    public static final double MAX_RPM = 312;

    public static final double DRIVETRAIN_P    = 1.50;
    public static final double DRIVETRAIN_I    = 0.00;
    public static final double DRIVETRAIN_D    = 0.00;
    public static final double DRIVETRAIN_F    = 19.5;

    public static TagDetector initDetector(HardwareMap map) {
        return new TagDetector(map.get(WebcamName.class, "webcam"));
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

        public DcMotor get(HardwareMap map) {
            return map.get(DcMotor.class, name);
        }

    }

}
