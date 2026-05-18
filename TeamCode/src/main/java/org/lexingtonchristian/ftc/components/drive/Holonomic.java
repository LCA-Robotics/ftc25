package org.lexingtonchristian.ftc.components.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.lexingtonchristian.ftc.util.MathHelper;
import org.lexingtonchristian.ftc.util.Motor;

import java.util.HashMap;
import java.util.Map;

public class Holonomic {

    private final DcMotor front;
    private final DcMotor left;
    private final DcMotor back;
    private final DcMotor right;

    public Holonomic(HardwareMap map) {

        this.front = map.get(DcMotor.class, "front");
        this.left = map.get(DcMotor.class, "left");
        this.back = map.get(DcMotor.class, "back");
        this.right = map.get(DcMotor.class, "right");

        this.left.setDirection(DcMotorSimple.Direction.REVERSE);
        this.back.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void move(double x, double y, double r, double scalar) {

        double pFront =  x + r;
        double pLeft  =  y + r;
        double pBack  = -x - r;
        double pRight = -y - r;

        double max = MathHelper.max(
                Math.abs(pFront),
                Math.abs(pLeft),
                Math.abs(pBack),
                Math.abs(pRight)
        );
        if (max > 1.0) {
            pFront /= max;
            pLeft  /= max;
            pBack  /= max;
            pRight /= max;
        }

        this.front.setPower(pFront * scalar);
        this.left.setPower(pLeft * scalar);
        this.back.setPower(pBack * scalar);
        this.right.setPower(pRight * scalar);

    }

}
