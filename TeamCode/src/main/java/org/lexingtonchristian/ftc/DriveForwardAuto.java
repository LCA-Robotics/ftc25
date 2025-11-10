package org.lexingtonchristian.ftc;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.util.Drivetrain;

@Autonomous
public class DriveForwardAuto extends LinearOpMode {

    private Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();

        waitForStart();

        this.drivetrain.move(0.0, -0.4, 0.0);
        this.sleep(500);
        this.drivetrain.zero();

    }

    private void initHardware() {
        this.drivetrain = Constants.initDrivetrain(this.hardwareMap);
    }

}
