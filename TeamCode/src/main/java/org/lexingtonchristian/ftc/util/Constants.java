package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.lexingtonchristian.ftc.components.Intake;
import org.lexingtonchristian.ftc.components.drive.Drivetrain;
import org.lexingtonchristian.ftc.components.Launcher;

/**
 * Lists constants for use across several classes in order to simplify code and reduce use of magic
 * constants.
 */
public class Constants {

    public static final double kV = 0.0;
    public static final double kA = 0.0;
    public static final double kStatic = 0.0;

    public static final double WHEEL_RADIUS = 2.0;
    public static final double TRACK_WIDTH = 11.375;
    public static final double WHEELBASE = 4.730;

    public static final double GEAR_RATIO = 1;

    public static final double TICKS_PER_REV = 537.6;
    public static final double MAX_RPM = 312;

    public static double MAX_VELOCITY = 52.041349386287315;
    public static double MAX_ACCELERATION = 52.041349386287315;
    public static double MAX_ANGULAR_VELOCITY = Math.toRadians(186.359355);
    public static double MAX_ANGULAR_ACCELERATION = Math.toRadians(186.359355);

    public static PIDFCoefficients MOTOR_VELOCITY_PID = new PIDFCoefficients(
            0.0, 0.0, 0.0, 32767 / ((MAX_RPM / 60) * TICKS_PER_REV)
    );

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

    public static final double LAUNCHER_OPEN   = 0.7;
    public static final double LAUNCHER_CLOSED = -2.0;

    public static final long   CYCLE_TIME      = 500; // milliseconds TODO: Find the time it takes to cycle 1 ball

    public static final String BACK_RIGHT      = "backRight";
    public static final String BACK_LEFT       = "backLeft";
    public static final String FRONT_RIGHT     = "frontRight";
    public static final String FRONT_LEFT      = "frontLeft";

    public static final String LAUNCHER_LEFT   = "launcherLeft";
    public static final String LAUNCHER_RIGHT  = "launcherRight";
    public static final String LAUNCHER_SERVO  = "launcherServo";

    public static final String INTAKE_SERVO    = "intakeServo";

    public static final double P               = 4.00;
    public static final double I               = 0.50;
    public static final double D               = 4.00;
    public static final double F               = 11.7;

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
        return new Intake(map.get(DcMotor.class, INTAKE_SERVO));
    }

    public static double ticksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public static int inchesToTicks(double inches) {
        return (int) ((inches / (WHEEL_RADIUS * 2 * Math.PI)) * TICKS_PER_REV);
    }

}
