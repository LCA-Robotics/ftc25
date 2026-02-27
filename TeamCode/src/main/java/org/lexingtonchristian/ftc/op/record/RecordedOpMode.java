package org.lexingtonchristian.ftc.op.record;

import static org.lexingtonchristian.ftc.util.Constants.initDetector;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Mecanum;
import org.lexingtonchristian.ftc.snapshot.Choreographer;

import java.util.Map;

@Autonomous(name = "Recorded Autonomous")
public class RecordedOpMode extends LinearOpMode {

    protected Mecanum drivetrain;
    protected TagDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {

        this.detector = initDetector(hardwareMap);
        this.drivetrain = new Mecanum(hardwareMap, detector);

        Choreographer.Builder builder = new Choreographer.Builder("test.txt");
        this.drivetrain.forEach((type, motor) -> builder.addDevice(motor));
        Choreographer choreographer = builder.build();

        waitForStart();

        long time;
        while (opModeIsActive()) {

            time = System.currentTimeMillis();
            if (time % 5 != 0) continue;

            Map<String, ?> snapshots = choreographer.readPicture();
            this.drivetrain.forEach((type, motor) -> {
                if (!snapshots.containsKey(type.name)) return;
                motor.setPower((double) snapshots.get(type.name));
            });

        }

    }

}
