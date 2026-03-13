package org.lexingtonchristian.ftc.op;

import static org.lexingtonchristian.ftc.util.Constants.initDetector;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Mecanum;
import org.lexingtonchristian.ftc.snapshot.DeviceSnapshot;
import org.lexingtonchristian.ftc.util.AutoReader;
import org.lexingtonchristian.ftc.util.AutoWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Autonomous(name = "Recorded Autonomous")
public class RecordedOpMode extends LinearOpMode {

    protected Mecanum drivetrain;
    protected TagDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {

        this.drivetrain = new Mecanum(hardwareMap, null);

        waitForStart();

        try {

            AutoReader reader = new AutoReader("test.txt");

            long start = System.currentTimeMillis();
            long current;
            long last = start;
            while (opModeIsActive()) {

                current = System.currentTimeMillis();
                if (current < last + 50) continue;
                last = current;

                Map<String, Double> snapshot = reader.nextSnapshot();
                if (snapshot == null) break;

                this.drivetrain.forEach((type, motor) -> {
                    if (!snapshot.containsKey(type.name)) return;
                    motor.setPower(snapshot.get(type.name));
                });

            }

            reader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
