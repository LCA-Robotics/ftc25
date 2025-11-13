package org.lexingtonchristian.ftc;

import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.util.Launcher;
import org.lexingtonchristian.ftc.util.RRMecanum;
import org.lexingtonchristian.ftc.util.TagDetector;

@Autonomous
public class TestAuto extends LinearOpMode {

    @Override
    public void runOpMode() {

        Launcher launcher = Constants.initLauncher(hardwareMap);
        TagDetector tagDetector = new TagDetector(hardwareMap.get(WebcamName.class, "webcam"));
        RRMecanum drive = new RRMecanum(kV, kA, kStatic, TRACK_WIDTH, WHEELBASE, 1.0, hardwareMap) {
            @Override
            protected double getRawExternalHeading() {
                return 0;
            }
        };

        waitForStart();

        drive.runForDistance(0.0, 0.55, 0.0, 24.0);

        drive.zero();

    }


    public static Pose2d move(double x, double y, double heading) {
        return new Pose2d(x, y, heading);
    }


}
