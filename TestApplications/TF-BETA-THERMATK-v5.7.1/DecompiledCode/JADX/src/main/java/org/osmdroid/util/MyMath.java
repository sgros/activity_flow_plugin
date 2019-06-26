package org.osmdroid.util;

import org.osmdroid.views.util.constants.MathConstants;

public class MyMath implements MathConstants {
    public static double cleanPositiveAngle(double d) {
        while (d < 0.0d) {
            d += 360.0d;
        }
        while (d >= 360.0d) {
            d -= 360.0d;
        }
        return d;
    }

    public static int floorToInt(double d) {
        int i = (int) d;
        return ((double) i) <= d ? i : i - 1;
    }

    public static long floorToLong(double d) {
        long j = (long) d;
        return ((double) j) <= d ? j : j - 1;
    }

    public static int getNextSquareNumberAbove(float f) {
        int i = 1;
        int i2 = 1;
        int i3 = 0;
        while (((float) i) <= f) {
            i *= 2;
            i3 = i2;
            i2++;
        }
        return i3;
    }

    public static int mod(int i, int i2) {
        if (i > 0) {
            return i % i2;
        }
        while (i < 0) {
            i += i2;
        }
        return i;
    }

    public static double getAngleDifference(double d, double d2, Boolean bool) {
        d = cleanPositiveAngle(d2 - d);
        return bool != null ? bool.booleanValue() ? d : d - 360.0d : d < 180.0d ? d : d - 360.0d;
    }
}
