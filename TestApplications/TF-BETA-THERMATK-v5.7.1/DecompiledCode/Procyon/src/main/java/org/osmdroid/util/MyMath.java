// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import org.osmdroid.views.util.constants.MathConstants;

public class MyMath implements MathConstants
{
    public static double cleanPositiveAngle(double n) {
        double n2;
        while (true) {
            n2 = n;
            if (n >= 0.0) {
                break;
            }
            n += 360.0;
        }
        while (n2 >= 360.0) {
            n2 -= 360.0;
        }
        return n2;
    }
    
    public static int floorToInt(final double n) {
        final int n2 = (int)n;
        if (n2 <= n) {
            return n2;
        }
        return n2 - 1;
    }
    
    public static long floorToLong(final double n) {
        final long n2 = (long)n;
        if (n2 <= n) {
            return n2;
        }
        return n2 - 1L;
    }
    
    public static double getAngleDifference(double cleanPositiveAngle, final double n, final Boolean b) {
        cleanPositiveAngle = cleanPositiveAngle(n - cleanPositiveAngle);
        if (b != null) {
            if (b) {
                return cleanPositiveAngle;
            }
            return cleanPositiveAngle - 360.0;
        }
        else {
            if (cleanPositiveAngle < 180.0) {
                return cleanPositiveAngle;
            }
            return cleanPositiveAngle - 360.0;
        }
    }
    
    public static int getNextSquareNumberAbove(final float n) {
        int n2 = 1;
        int n3 = 1;
        int n4 = 0;
        while (n2 <= n) {
            n2 *= 2;
            n4 = n3;
            ++n3;
        }
        return n4;
    }
    
    public static int mod(final int n, final int n2) {
        int i = n;
        if (n > 0) {
            return n % n2;
        }
        while (i < 0) {
            i += n2;
        }
        return i;
    }
}
