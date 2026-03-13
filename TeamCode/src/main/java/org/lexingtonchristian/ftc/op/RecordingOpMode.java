package org.lexingtonchristian.ftc.op;

import static org.lexingtonchristian.ftc.util.Constants.*;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Mecanum;
import org.lexingtonchristian.ftc.snapshot.DeviceSnapshot;
import org.lexingtonchristian.ftc.util.AutoWriter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@TeleOp(name = "Record New Autonomous")
public class RecordingOpMode extends LinearOpMode {

    protected Mecanum drivetrain;
    protected TagDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {

        this.drivetrain = new Mecanum(hardwareMap, null);

        waitForStart();

        try {

            AutoWriter writer = new AutoWriter("test.txt");

            long start = System.currentTimeMillis();
            long current;
            long last = start;
            while (opModeIsActive()) {

                double speedLimit = this.gamepad1.right_bumper ? 0.30 : 0.85;

                double leftX = this.gamepad1.left_stick_x;  // left stick X
                double leftY = this.gamepad1.left_stick_y;  // left stick Y
                double rightX = this.gamepad1.right_stick_x * 0.6; // right stick X (rotational, slow by 60%)

                this.drivetrain.move(
                        leftY,
                        -leftX,
                        -rightX,
                        speedLimit
                );

                current = System.currentTimeMillis();
                if (current < last + 50) continue;
                last = current;

                List<DeviceSnapshot> snapshots = new ArrayList<>();
                this.drivetrain.forEach((type, motor) ->
                        snapshots.add(snapshot(type.name, motor.getPower())));

                writer.writeSnapshot(snapshots.toArray(new DeviceSnapshot[]{}));

            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private DeviceSnapshot snapshot(String name, double value) {
        return new DeviceSnapshot(
                name, value
        );
    }

}
