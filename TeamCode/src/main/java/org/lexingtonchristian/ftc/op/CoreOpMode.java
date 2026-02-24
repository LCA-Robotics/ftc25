package org.lexingtonchristian.ftc.op;

import static org.lexingtonchristian.ftc.util.Constants.initDetector;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Mecanum;

@Disabled
public abstract class CoreOpMode extends LinearOpMode {

    protected Mecanum drivetrain;
    protected TagDetector detector;

    protected void zeroAll() {
        this.drivetrain.zero();
    }

    protected void initHardware() {
        this.detector = initDetector(hardwareMap);
        this.drivetrain = new Mecanum(hardwareMap, this.detector);
    }

}
