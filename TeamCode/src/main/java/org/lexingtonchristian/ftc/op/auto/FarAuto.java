package org.lexingtonchristian.ftc.op.auto;

import static org.lexingtonchristian.ftc.util.Constants.CYCLE_TIME;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.lexingtonchristian.ftc.components.Intake;
import org.lexingtonchristian.ftc.components.Launcher;
import org.lexingtonchristian.ftc.components.drive.Drivetrain;
import org.lexingtonchristian.ftc.util.Constants;

@Autonomous(name = "Far Side Autonomous", group = "Competition")
public class FarAuto extends LinearOpMode {

    private Intake intake;
    private Launcher launcher;
    private Drivetrain drivetrain;

    @Override
    public void runOpMode() {

        initHardware();

        waitForStart();

        this.launcher.spin(2100);

        sleep(2000);

        this.intake.run(1.0);
        this.launcher.servo(1.0);

        sleep(CYCLE_TIME * 3 + 1000);

        this.intake.zero();
        this.launcher.servo(0.0);

        this.drivetrain.drive(24);

    }

    private void initHardware() {

        this.intake = new Intake(this.hardwareMap.get(DcMotor.class, "intake"));

        this.launcher = Constants.initLauncher(hardwareMap);
        this.drivetrain = Constants.initDrivetrain(hardwareMap);

    }

}
