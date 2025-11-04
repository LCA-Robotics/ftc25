package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {

    public static final int    BLUE_GOAL       = 20;
    public static final int    RED_GOAL        = 24;

    public static final int    GPP             = 21;
    public static final int    PGP             = 22;
    public static final int    PPG             = 23;

    public static final int    CURRENT         = RED_GOAL;

    public static final String BACK_RIGHT      = "backRight";
    public static final String BACK_LEFT       = "backLeft";
    public static final String FRONT_RIGHT     = "frontRight";
    public static final String FRONT_LEFT      = "frontLeft";

    public static final String LAUNCHER_LEFT   = "launcherLeft";
    public static final String LAUNCHER_RIGHT  = "launcherRight";
    public static final String LAUNCHER_SERVO  = "launcherServo";

    public static Drivetrain initDrivetrain(HardwareMap map) {
        return new Drivetrain(
                map.get(DcMotor.class, BACK_RIGHT),
                map.get(DcMotor.class, BACK_LEFT),
                map.get(DcMotor.class, FRONT_RIGHT),
                map.get(DcMotor.class, FRONT_LEFT)
        );
    }

    public static Launcher initLauncher(HardwareMap map) {
        return new Launcher(
                map.get(DcMotor.class, LAUNCHER_LEFT),
                map.get(DcMotor.class, LAUNCHER_RIGHT),
                map.get(CRServo.class, LAUNCHER_SERVO)
        );
    }

}
