package org.lexingtonchristian.ftc.util;

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

}
