package org.lexingtonchristian.ftc.op.record;

import static org.lexingtonchristian.ftc.util.Constants.initDetector;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.lexingtonchristian.ftc.components.TagDetector;
import org.lexingtonchristian.ftc.components.drive.Mecanum;
import org.lexingtonchristian.ftc.snapshot.Choreographer;

@TeleOp(name = "Record New Autonomous")
public class RecordingOpMode extends LinearOpMode {

    protected Mecanum drivetrain;
    protected TagDetector detector;

    @Override
    public void runOpMode() throws InterruptedException {

        this.drivetrain = new Mecanum(hardwareMap);

        Choreographer.Builder builder = new Choreographer.Builder("test.txt");
        this.drivetrain.forEach((type, motor) -> builder.addDevice(motor));
        Choreographer choreographer = builder.build();

        waitForStart();

        long time;
        while (opModeIsActive()) {

            double limit = this.gamepad1.right_bumper ? 0.30 : 0.85;

            double leftX = this.gamepad1.left_stick_x;  // left stick X
            double leftY = this.gamepad1.left_stick_y;  // left stick Y
            double rightX = this.gamepad1.right_stick_x * 0.6; // right stick X (rotational, slow by 60%)

            this.drivetrain.move(
                    leftY,
                    -leftX,
                    -rightX,
                    limit
            );

            time = System.currentTimeMillis();
            if (time % 5 != 0) continue;

            choreographer.writePicture();

        }

        choreographer.close();

    }

}
