package org.lexingtonchristian.ftc.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.Intake;
import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.components.Launcher;

@Autonomous(name = "Test Launch", group = "Test")
public class TestLaunch extends LinearOpMode {

    private Launcher launcher;
    private Intake intake;

    @Override
    public void runOpMode() {

        initHardware();

        waitForStart();

        while (this.opModeIsActive()) {
            intake.run(0.7);
            launcher.servo(1.0);
            launcher.spin(1000);
        }
    }


    private void initHardware() {
        this.launcher = Constants.initLauncher(hardwareMap);
        this.intake = Constants.initIntake(hardwareMap);
    }

}