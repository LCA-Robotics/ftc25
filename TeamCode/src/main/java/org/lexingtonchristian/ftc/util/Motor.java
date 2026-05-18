package org.lexingtonchristian.ftc.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public enum Motor {

    BACK_LEFT("backLeft"),
    FRONT_LEFT("frontLeft"),
    BACK_RIGHT("backRight"),
    FRONT_RIGHT("frontRight");

    public final String name;

    Motor(String name) {
        this.name = name;
    }

    public DcMotor get(HardwareMap map) {
        return map.get(DcMotor.class, name);
    }

}
