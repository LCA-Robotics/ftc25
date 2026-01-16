package org.lexingtonchristian.ftc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {

    public static double clamp(double val, double min, double max) {
        return Math.max(min, (Math.min(val, max)));
    }

    public static double max(double... values) {
        double max = values[0];
        double current;
        for (int i = 1; i < values.length; i++) {
            current = values[i];
            max = Math.max(current, max);
        }
        return max;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        return new BigDecimal(Double.toString(value))
                .setScale(places, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static boolean roughEqual(double a, double b, double tolerance) {
        return (a - tolerance) < b && b < (a + tolerance);
    }

}
