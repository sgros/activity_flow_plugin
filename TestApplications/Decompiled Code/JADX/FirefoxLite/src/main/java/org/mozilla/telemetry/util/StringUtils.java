package org.mozilla.telemetry.util;

public class StringUtils {
    public static String safeSubstring(String str, int i, int i2) {
        return str.substring(Math.max(0, i), Math.min(i2, str.length()));
    }
}
