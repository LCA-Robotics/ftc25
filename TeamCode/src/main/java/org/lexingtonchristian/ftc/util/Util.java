package org.lexingtonchristian.ftc.util;

import java.util.function.Supplier;

public class Util {

    public static void waitUntil(int interval, Supplier<Boolean> condition) {
        while (!condition.get()) {
            try {
                Thread.sleep(interval);
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

}
