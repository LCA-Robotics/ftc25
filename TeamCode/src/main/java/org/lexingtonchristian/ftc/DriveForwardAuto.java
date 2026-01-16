package org.lexingtonchristian.ftc;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.drive.Drivetrain;
import org.lexingtonchristian.ftc.util.Constants;

@Autonomous(name = "Drive Forward", group = "Competition")
public class DriveForwardAuto extends LinearOpMode {

    private Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();

        waitForStart();

        this.drivetrain.drive(24);

    }

    private void initHardware() {
        this.drivetrain = Constants.initDrivetrain(this.hardwareMap);
    }

}
