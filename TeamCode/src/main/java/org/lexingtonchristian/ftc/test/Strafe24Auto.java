package org.lexingtonchristian.ftc.test;

import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.TRACK_WIDTH;
import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.WHEELBASE;
import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.kA;
import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.kStatic;
import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.kV;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.lexingtonchristian.ftc.util.Constants;
import org.lexingtonchristian.ftc.components.Launcher;
import org.lexingtonchristian.ftc.components.drive.RRMecanum;
import org.lexingtonchristian.ftc.components.TagDetector;

@Autonomous(name = "Strafe 24", group = "Test")
public class Strafe24Auto extends LinearOpMode {

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

        drive.runForDistance(0.55, 0.0, 0.0, 24.0);

    }


    public static Pose2d move(double x, double y, double heading) {
        return new Pose2d(x, y, heading);
    }


}
