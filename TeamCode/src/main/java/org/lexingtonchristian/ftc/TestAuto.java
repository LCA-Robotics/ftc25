package org.lexingtonchristian.ftc;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.lib.drive.SampleTankDrive;

@Autonomous(group = "testing")
public class TestAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        SampleTankDrive drive = new SampleTankDrive(hardwareMap);

        Trajectory traj = drive.trajectoryBuilder(new Pose2d())
                .forward(5 * 12)
                .build();

        waitForStart();

        drive.followTrajectory(traj);
        drive.turn(Math.toRadians(90.0F));
        drive.followTrajectory(traj);
        drive.turn(Math.toRadians(-90.0F));
        drive.followTrajectory(traj);
        drive.turn(Math.toRadians(-90.0F));
        drive.followTrajectory(traj);
        drive.turn(Math.toRadians(-90.0F));
        drive.followTrajectory(traj);
        drive.followTrajectory(traj);

    }

}
