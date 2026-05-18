package org.lexingtonchristian.ftc.op;

import static org.lexingtonchristian.ftc.util.Constants.initDetector;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.TagDetector;

@Disabled
public abstract class CoreOpMode extends LinearOpMode {

    protected TagDetector detector;

    protected void zeroAll() {

    }

    protected void initHardware() {
        this.detector = initDetector(hardwareMap);
    }

}
