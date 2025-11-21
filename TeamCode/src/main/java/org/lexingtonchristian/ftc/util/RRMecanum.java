package org.lexingtonchristian.ftc.util;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static org.lexingtonchristian.ftc.lib.drive.DriveConstants.encoderTicksToInches;
import static org.lexingtonchristian.ftc.util.Constants.*;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class RRMecanum extends MecanumDrive {

    private static final DecimalFormat TWO_DECIMALS = new DecimalFormat("#.00");

    private final DcMotorEx backLeft, backRight, frontLeft, frontRight;

    private List<Integer> lastEncoderPositions = new ArrayList<>();
    private List<Integer> lastEncoderVelocities = new ArrayList<>();
    private List<DcMotorEx> motors;

    public RRMecanum(double kV, double kA, double kStatic, double trackWidth, double wheelBase, double lateralMultiplier, HardwareMap hardwareMap) {

        super(kV, kA, kStatic, trackWidth, wheelBase, lateralMultiplier);

        this.backLeft = hardwareMap.get(DcMotorEx.class, BACK_LEFT);
        this.backRight = hardwareMap.get(DcMotorEx.class, BACK_RIGHT);
        this.frontLeft = hardwareMap.get(DcMotorEx.class, FRONT_LEFT);
        this.frontRight = hardwareMap.get(DcMotorEx.class, FRONT_RIGHT);

        this.setRunMode();
        this.reverseMotors();

        motors = Arrays.asList(this.backLeft, this.backRight, this.frontLeft, this.frontRight);

    }

    @Override
    public void setMotorPowers(double backLeftPower, double backRightPower, double frontLeftPower, double frontRightPower) {

        this.backLeft.setPower(backLeftPower);
        this.backRight.setPower(backRightPower);
        this.frontLeft.setPower(frontLeftPower);
        this.frontRight.setPower(frontRightPower);

    }

    public void setMotorTicks(double backLeftTicks, double backRightTicks, double frontLeftTicks, double frontRightTicks) {

        this.backLeft.setVelocity(backLeftTicks);
        this.backRight.setVelocity(backRightTicks);
        this.frontLeft.setVelocity(frontLeftTicks);
        this.frontRight.setVelocity(frontRightTicks);

    }

    public void center(double tolerance, Supplier<Double> bearing) {
        while (tolerance < bearing.get() || bearing.get() < -tolerance) {
            centerRotate(Double.parseDouble(TWO_DECIMALS.format(bearing.get() * -0.02)));
        }
        zero();
    }

    public void zero() {

        this.setMotorPowers(0, 0, 0, 0);

    }

    public void runForDistance(double powerX, double powerY, double headingDegrees, double endDistanceInches) {

        List<Boolean> completedMotors = new ArrayList<>(Arrays.asList(false, false, false, false));
        List<Double> startPositions = this.getWheelPositions();
        boolean driveComplete = false;

        this.setDrivePower(new Pose2d(powerX, powerY, Math.toRadians(headingDegrees)));

        while (!driveComplete) {

            List<Double> currentPositions = this.getWheelPositions();

            for (int i = 0; i < 4; i++) {

                double endPosition = startPositions.get(i) + endDistanceInches;
                if (currentPositions.get(i) < endPosition) continue;
                completedMotors.set(i, true);

            }

            driveComplete = completedMotors.stream().allMatch(b -> b);

        }

        this.zero();

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

    private void centerRotate(double velocityTicks) {
        this.backLeft.setVelocity(velocityTicks);
        this.backRight.setVelocity(velocityTicks * -1);
        this.frontLeft.setVelocity(velocityTicks);
        this.frontRight.setVelocity(velocityTicks * -1);
    }

    private void reverseMotors() {

        this.backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        this.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    private void setRunMode() {

        this.backLeft.setMode(RUN_USING_ENCODER);
        this.backRight.setMode(RUN_USING_ENCODER);
        this.frontLeft.setMode(RUN_USING_ENCODER);
        this.frontRight.setMode(RUN_USING_ENCODER);

    }

}
