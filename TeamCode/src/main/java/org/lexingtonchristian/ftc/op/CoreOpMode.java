package org.lexingtonchristian.ftc.op;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.Intake;
import org.lexingtonchristian.ftc.components.Launcher;
import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Drivetrain;
import org.lexingtonchristian.ftc.components.drive.Mecanum;

import static org.lexingtonchristian.ftc.util.Constants.*;

public abstract class CoreOpMode extends LinearOpMode {

    protected Mecanum drivetrain;

    protected Intake intake;
    protected Launcher launcher;

    protected TagDetector detector;

    protected void zeroAll() {
        this.intake.zero();
        this.launcher.zero();
        this.launcher.servo(0.0);
        this.drivetrain.zero();
    }

    protected void initHardware() {

        this.drivetrain = new Mecanum(hardwareMap);

        this.intake = initIntake(hardwareMap);
        this.launcher = initLauncher(hardwareMap);

        this.detector = initDetector(hardwareMap);

    }

}
