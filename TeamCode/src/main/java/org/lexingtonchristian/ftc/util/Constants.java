package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.lexingtonchristian.ftc.components.Intake;
import org.lexingtonchristian.ftc.components.Launcher;
import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Drivetrain;

/**
 * Lists constants for use across several classes in order to simplify code and reduce use of magic
 * constants.
 */
public class Constants {

    public static final double WHEEL_RADIUS = 2.0;
    public static final double TRACK_WIDTH = 13.25;
    public static final double CIRCUMFERENCE = TRACK_WIDTH * Math.PI;
    public static final double WHEELBASE = 4.730;

    public static final double GEAR_RATIO = 1;

    public static final double TICKS_PER_REV = 537.6;
    public static final double MAX_RPM = 312;

    /**
     * AprilTag id for the blue goal
     */
    public static final int    BLUE_GOAL       = 20;
    /**
     * AprilTag id for the red goal
     */
    public static final int    RED_GOAL        = 24;

    /**
     * Motif AprilTag (green purple purple)
     */
    public static final int    GPP             = 21;
    /**
     * Motif AprilTag (purple green purple)
     */
    public static final int    PGP             = 22;
    /**
     * Motif AprilTag (purple purple green)
     */
    public static final int    PPG             = 23;

    /**
     * The current alliance goal AprilTag id
     */
    public static final int    CURRENT         = BLUE_GOAL;

    public static final long   CYCLE_TIME      = 4250; // milliseconds TODO: Find the time it takes to cycle 1 ball

    public static final String BACK_RIGHT      = "backRight";
    public static final String BACK_LEFT       = "backLeft";
    public static final String FRONT_RIGHT     = "frontRight";
    public static final String FRONT_LEFT      = "frontLeft";

    public static final String LAUNCHER_LEFT   = "launcherLeft";
    public static final String LAUNCHER_RIGHT  = "launcherRight";
    public static final String LAUNCHER_SERVO  = "launcherServo";

    public static final String INTAKE_MOTOR    = "intake";

    public static final String WEBCAM_NAME     = "webcam";

    public static final double DRIVETRAIN_P    = 1.50;
    public static final double DRIVETRAIN_I    = 0.00;
    public static final double DRIVETRAIN_D    = 0.00;
    public static final double DRIVETRAIN_F    = 19.5;

    /**
     * @param map the {@link HardwareMap} used to reference the motors
     * @return a constructed {@link Drivetrain} with the four motors
     */
    public static Drivetrain initDrivetrain(HardwareMap map) {
        return new Drivetrain(
                map.get(DcMotor.class, BACK_RIGHT),
                map.get(DcMotor.class, BACK_LEFT),
                map.get(DcMotor.class, FRONT_RIGHT),
                map.get(DcMotor.class, FRONT_LEFT)
        );
    }

    /**
     * @param map the {@link HardwareMap} used to reference the motors and servo
     * @return a constructed {@link Launcher} with the motors and servo
     */
    public static Launcher initLauncher(HardwareMap map) {
        return new Launcher(
                map.get(DcMotor.class, LAUNCHER_LEFT),
                map.get(DcMotor.class, LAUNCHER_RIGHT),
                map.get(CRServo.class, LAUNCHER_SERVO)
        );
    }

    public static Intake initIntake(HardwareMap map) {
        return new Intake(map.get(DcMotor.class, INTAKE_MOTOR));
    }

    public static TagDetector initDetector(HardwareMap map) {
        return new TagDetector(map.get(WebcamName.class, "webcam"));
    }

    public static double ticksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public static int inchesToTicks(double inches) {
        return (int) ((inches / (WHEEL_RADIUS * 2 * Math.PI)) * TICKS_PER_REV);
    }

}
