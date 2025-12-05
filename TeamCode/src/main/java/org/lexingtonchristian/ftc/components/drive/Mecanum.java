package org.lexingtonchristian.ftc.components.drive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.MAX_ANG_ACCEL;
import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.MAX_ANG_VEL;
import static org.lexingtonchristian.ftc.util.Constants.*;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.lexingtonchristian.ftc.lib.trajectorysequence.TrajectorySequenceBuilder;
import org.lexingtonchristian.ftc.lib.trajectorysequence.TrajectorySequenceRunner;
import org.lexingtonchristian.ftc.lib.util.LynxModuleUtil;
import org.lexingtonchristian.ftc.util.Constants;

public class Mecanum extends MecanumDrive {

    public static final PIDCoefficients TRANSLATIONAL_PID =
            new PIDCoefficients(0.0, 0.0, 0.0);
    public static final PIDCoefficients HEADING_PID =
            new PIDCoefficients(0.0, 0.0, 0.0);

    public static final double LATERAL_MULTIPLIER = 1;

    public static final double VX_WEIGHT = 1;
    public static final double VY_WEIGHT = 1;
    public static final double OMEGA_WEIGHT = 1;

    private static final TrajectoryVelocityConstraint VELOCITY_CONSTRAINT =
            getVelocityConstraint(MAX_VELOCITY, MAX_ANGULAR_VELOCITY, TRACK_WIDTH);
    private static final TrajectoryAccelerationConstraint ACCELERATION_CONSTRAINT =
            getAccelerationConstraint(MAX_ACCELERATION);

    private TrajectorySequenceRunner runner;
    private TrajectoryFollower follower;

    private DcMotorEx backRight, backLeft, frontRight, frontLeft;
    private List<DcMotorEx> motors;

    private IMU imu;
    private VoltageSensor voltageSensor;

    private List<Integer> lastEncoderPositions;
    private List<Integer> lastEncoderVelocities;

    public Mecanum(HardwareMap map) {

        super(kV, kA, kStatic, TRACK_WIDTH, WHEELBASE, LATERAL_MULTIPLIER);

        this.backRight = map.get(DcMotorEx.class, BACK_RIGHT);
        this.backLeft = map.get(DcMotorEx.class, BACK_LEFT);
        this.frontRight = map.get(DcMotorEx.class, FRONT_RIGHT);
        this.frontLeft = map.get(DcMotorEx.class, FRONT_LEFT);
        this.motors = List.of(this.backRight, this.backLeft, this.frontRight, this.frontLeft);

        this.imu = map.get(IMU.class, "imu");
        this.imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.DOWN
        )));

        LynxModuleUtil.ensureMinimumFirmwareVersion(map);

        for (LynxModule module : map.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }


        this.voltageSensor = map.voltageSensor.iterator().next();

        for(DcMotorEx motor : motors) {
            MotorConfigurationType configType = motor.getMotorType().clone();
            configType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(configType);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        this.lastEncoderPositions = new ArrayList<>();
        this.lastEncoderVelocities = new ArrayList<>();

        this.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        this.follower = new HolonomicPIDVAFollower(
                TRANSLATIONAL_PID,
                TRANSLATIONAL_PID,
                HEADING_PID,
                new Pose2d(0.5, 0.5, Math.toRadians(5.0)),
                0.5
        );

        List<Integer> lastTrackingEncoderPositions = new ArrayList<>();
        List<Integer> lastTrackingEncoderVelocities = new ArrayList<>();

        this.runner = new TrajectorySequenceRunner(
                follower, HEADING_PID, voltageSensor, lastEncoderPositions, lastEncoderVelocities,
                lastTrackingEncoderPositions, lastTrackingEncoderVelocities
        );

    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose) {
        return new TrajectoryBuilder(
                startPose,
                VELOCITY_CONSTRAINT,
                ACCELERATION_CONSTRAINT
        );
    }

    public TrajectorySequenceBuilder trajectorySequenceBuilder(Pose2d startPose) {
        return new TrajectorySequenceBuilder(
                startPose,
                VELOCITY_CONSTRAINT, ACCELERATION_CONSTRAINT,
                MAX_ANG_VEL, MAX_ANG_ACCEL
        );
    }

    public void followTrajectoryAsync(Trajectory trajectory) {
        this.runner.followTrajectorySequenceAsync(
                trajectorySequenceBuilder(trajectory.start())
                        .addTrajectory(trajectory)
                        .build()
        );
    }

    public void followTrajectory(Trajectory trajectory) {
        this.followTrajectoryAsync(trajectory);
        waitForIdle();
    }

    public void update() {
        this.updatePoseEstimate();
        DriveSignal signal = this.runner.update(getPoseEstimate(), getPoseVelocity());
        if (signal != null) setDriveSignal(signal);
    }

    public boolean isBusy() {
        return this.runner.isBusy();
    }

    public void waitForIdle() {
        while (!Thread.currentThread().isInterrupted() && this.isBusy()) {
            this.update();
        }
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {

        this.lastEncoderPositions.clear();

        List<Double> wheelPositions = new ArrayList<>();
        this.motors.forEach(motor -> {
            int pos = motor.getCurrentPosition();
            this.lastEncoderPositions.add(pos);
            wheelPositions.add(Constants.ticksToInches(pos));
        });

        return wheelPositions;

    }

    @Override
    public List<Double> getWheelVelocities() {

        this.lastEncoderVelocities.clear();

        List<Double> wheelVelocities = new ArrayList<>();
        this.motors.forEach(motor -> {
            int vel = (int) motor.getVelocity();
            this.lastEncoderVelocities.add(vel);
            wheelVelocities.add(Constants.ticksToInches(vel));
        });

        return wheelVelocities;

    }

    @Override
    public void setDrivePower(@NonNull Pose2d drivePower) {
        super.setDrivePower(drivePower.unaryMinus());
    }

    @Override
    public void setMotorPowers(double frontLeft, double backLeft, double backRight, double frontRight) {
        this.backRight.setPower(backRight);
        this.backLeft.setPower(backLeft);
        this.frontRight.setPower(frontRight);
        this.frontLeft.setPower(frontLeft);
    }

    @Override
    protected double getRawExternalHeading() {
        return this.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    @Override
    public Double getExternalHeadingVelocity() {
        return (double) this.imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
    }

    public static TrajectoryVelocityConstraint getVelocityConstraint(
            double maxVelocity,
            double maxAngularVelocity,
            double trackWidth
    ) {
        return new MinVelocityConstraint(List.of(
                new AngularVelocityConstraint(maxAngularVelocity),
                new MecanumVelocityConstraint(maxVelocity, trackWidth)
        ));
    }

    public static TrajectoryAccelerationConstraint getAccelerationConstraint(double maxAcceleration) {
        return new ProfileAccelerationConstraint(maxAcceleration);
    }

}
