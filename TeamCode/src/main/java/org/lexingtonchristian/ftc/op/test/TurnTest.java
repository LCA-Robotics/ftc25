package org.lexingtonchristian.ftc.op.test;

import static org.lexingtonchristian.ftc.util.Constants.initDrivetrain;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.drive.Drivetrain;

@Autonomous
public class TurnTest extends LinearOpMode {

    private Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        initHardware();

        waitForStart();

        this.drivetrain.rotate(45);

        sleep(200);

        this.drivetrain.rotate(-90);

        sleep(200);

        this.drivetrain.rotate(45);

    }

    private void initHardware() {
        this.drivetrain = initDrivetrain(hardwareMap);
    }

}
