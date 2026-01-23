package org.lexingtonchristian.ftc.op.tele;

import org.lexingtonchristian.ftc.op.CoreOpMode;
import org.lexingtonchristian.ftc.util.Constants;

public class NewTeleOp extends CoreOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        final double LAUNCHER_VELOCITY = 1050;
        final double INTAKE_SPEED      = 1.0;
        final double SERVO_SPEED       = 1.0;

        initHardware();

        waitForStart();

        while (opModeIsActive()) {

            double leftX = this.gamepad1.left_stick_x;
            double leftY = this.gamepad1.left_stick_y;
            double rightX = this.gamepad1.right_stick_x;

            boolean slow = this.gamepad1.right_bumper;
            this.drivetrain.move(
                    leftX,
                    leftY,
                    rightX,
                    slow ? 0.30 : 0.85
            );

            if (this.gamepad1.b) this.launcher.servo(SERVO_SPEED);
            if (this.gamepad1.x) this.drivetrain.align(Constants.RED_GOAL, 3.0);

            if (this.gamepad1.left_trigger > 0.0) {
                this.intake.run(INTAKE_SPEED);
                this.launcher.servo(SERVO_SPEED);
            } else if (this.gamepad1.left_bumper) {
                this.intake.run(-INTAKE_SPEED);
                this.launcher.servo(-SERVO_SPEED);
            } else {
                this.intake.zero();
                this.launcher.zero();
            }

            if (this.gamepad1.right_trigger > 0.0) {
                this.launcher.spin(LAUNCHER_VELOCITY);
            }


        }

    }


}
