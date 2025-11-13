package org.lexingtonchristian.ftc.util;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.encoderTicksToInches;
import static org.lexingtonchristian.ftc.util.Constants.*;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class RRMecanum extends MecanumDrive {

    private final DcMotorEx backLeft, backRight, frontLeft, frontRight;

    private List<Integer> lastEncoderPositions = new ArrayList<>();
    private List<Integer> lastEncoderVelocities = new ArrayList<>();
    private List<DcMotorEx> motors;

    public RRMecanum(double kV, double kA, double kStatic, double trackWidth, double wheelBase, double lateralMultiplier) {

        super(kV, kA, kStatic, trackWidth, wheelBase, lateralMultiplier);

        this.backLeft = hardwareMap.get(DcMotorEx.class, BACK_LEFT);
        this.backRight = hardwareMap.get(DcMotorEx.class, BACK_RIGHT);
        this.frontLeft = hardwareMap.get(DcMotorEx.class, FRONT_LEFT);
        this.frontRight = hardwareMap.get(DcMotorEx.class, FRONT_RIGHT);

        motors = Arrays.asList(this.backLeft, this.backRight, this.frontLeft, this.frontRight);

        reverseMotors();

    }

    @Override
    public void setMotorPowers(double backLeftPower, double backRightPower, double frontLeftPower, double frontRightPower) {

        this.backLeft.setPower(backLeftPower);
        this.backRight.setPower(backRightPower);
        this.frontLeft.setPower(frontLeftPower);
        this.frontRight.setPower(frontRightPower);

    }

    public void zero() {

        this.setMotorPowers(0, 0, 0, 0);

    }

    public void runForDistance(double powerX, double powerY, double heading, double endDistanceInches) {

        List<Boolean> completedMotors = new ArrayList<>(Arrays.asList(false, false, false, false));
        this.setDrivePower(new Pose2d(powerX, powerY, heading));
        List<Double> startPositions = this.getWheelPositions();
        boolean driveComplete = false;

        while (!driveComplete) {

            List<Double> currentPositions = this.getWheelPositions();

            for (int i = 0; i < 4; i++) {

                double endPosition = startPositions.get(i) + endDistanceInches;
                if (currentPositions.get(i) < endPosition) continue;
                completedMotors.set(i, true);

            }

            driveComplete = completedMotors.stream().allMatch(b -> b);

        }

    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {

        lastEncoderPositions.clear();

        List<Double> wheelPositions = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            int position = motor.getCurrentPosition();
            lastEncoderPositions.add(position);
            wheelPositions.add(encoderTicksToInches(position));
        }

        return wheelPositions;

    }

    @Override
    public List<Double> getWheelVelocities() {

        lastEncoderVelocities.clear();

        List<Double> wheelVelocities = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            int velocity = (int) motor.getVelocity();
            lastEncoderVelocities.add(velocity);
            wheelVelocities.add(encoderTicksToInches(velocity));
        }

        return wheelVelocities;

    }

    private void reverseMotors() {

        this.backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

}
