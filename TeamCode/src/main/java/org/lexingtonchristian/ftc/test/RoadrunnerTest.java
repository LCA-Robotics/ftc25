package org.lexingtonchristian.ftc.test;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.lexingtonchristian.ftc.components.drive.Mecanum;

@Autonomous(name = "Roadrunner Test", group = "Test")
public class RoadrunnerTest extends LinearOpMode {

    private Mecanum drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        this.drivetrain = new Mecanum(this.hardwareMap);

        Trajectory traj = this.drivetrain.trajectoryBuilder(new Pose2d())
                .forward(10.0) // in.
                .build();

        waitForStart();

        this.drivetrain.followTrajectory(traj);


    }

}
