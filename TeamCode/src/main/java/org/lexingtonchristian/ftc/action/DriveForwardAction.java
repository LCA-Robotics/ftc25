package org.lexingtonchristian.ftc.action;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.util.Drivetrain;

public class DriveForwardAction extends Action {

    @JsonSerialize
    private double power;
    private long time;

    @Override
    public void run(HardwareMap map) {
        Drivetrain drive = Constants.initDrivetrain(map);
        drive.move(0.0, -power, 0.0);
        this.sleep(time);
        drive.zero();
    }

}
