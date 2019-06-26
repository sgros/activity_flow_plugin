// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.time;

import java.util.TimeZone;
import java.util.Calendar;

public class SunDate
{
    private static final double DEGRAD = 0.017453292519943295;
    private static final double INV360 = 0.002777777777777778;
    private static final double RADEG = 57.29577951308232;
    
    private static double GMST0(final double n) {
        return revolution(n * 0.985647352 + 818.9874);
    }
    
    private static double acosd(final double a) {
        return Math.acos(a) * 57.29577951308232;
    }
    
    private static double atan2d(final double y, final double x) {
        return Math.atan2(y, x) * 57.29577951308232;
    }
    
    public static int[] calculateSunriseSunset(final double n, final double n2) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        final double[] array = new double[2];
        sunRiseSetForYear(instance.get(1), instance.get(2) + 1, instance.get(5), n2, n, array);
        final int n3 = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000 / 60;
        final int n4 = (int)(array[0] * 60.0) + n3;
        final int n5 = (int)(array[1] * 60.0) + n3;
        int n6;
        if (n4 < 0) {
            n6 = n4 + 1440;
        }
        else if ((n6 = n4) > 1440) {
            n6 = n4 - 1440;
        }
        int n7;
        if (n5 < 0 || (n7 = n5) > 1440) {
            n7 = n5 + 1440;
        }
        return new int[] { n6, n7 };
    }
    
    private static double cosd(final double n) {
        return Math.cos(n * 0.017453292519943295);
    }
    
    private static long days_since_2000_Jan_0(final int n, final int n2, final int n3) {
        return n * 367L - (n + (n2 + 9) / 12) * 7 / 4 + n2 * 275 / 9 + n3 - 730530L;
    }
    
    private static double rev180(final double n) {
        return n - Math.floor(0.002777777777777778 * n + 0.5) * 360.0;
    }
    
    private static double revolution(final double n) {
        return n - Math.floor(0.002777777777777778 * n) * 360.0;
    }
    
    private static double sind(final double n) {
        return Math.sin(n * 0.017453292519943295);
    }
    
    private static int sunRiseSetForYear(final int n, final int n2, final int n3, final double n4, final double n5, final double[] array) {
        return sunRiseSetHelperForYear(n, n2, n3, n4, n5, -0.5833333333333334, 1, array);
    }
    
    private static int sunRiseSetHelperForYear(int n, final int n2, final int n3, double revolution, final double n4, final double n5, final int n6, final double[] array) {
        final double[] array2 = { 0.0 };
        final double[] array3 = { 0.0 };
        final double[] array4 = { 0.0 };
        final double v = (double)days_since_2000_Jan_0(n, n2, n3);
        Double.isNaN(v);
        final double n7 = v + 0.5 - revolution / 360.0;
        revolution = revolution(GMST0(n7) + 180.0 + revolution);
        sun_RA_decAtDay(n7, array2, array3, array4);
        revolution = rev180(revolution - array2[0]) / 15.0;
        final double n8 = 12.0;
        final double n9 = 12.0 - revolution;
        revolution = 0.2666 / array4[0];
        if (n6 != 0) {
            revolution = n5 - revolution;
        }
        else {
            revolution = n5;
        }
        revolution = (sind(revolution) - sind(n4) * sind(array3[0])) / (cosd(n4) * cosd(array3[0]));
        if (revolution >= 1.0) {
            n = -1;
            revolution = 0.0;
        }
        else if (revolution <= -1.0) {
            n = 1;
            revolution = n8;
        }
        else {
            revolution = acosd(revolution) / 15.0;
            n = 0;
        }
        array[0] = n9 - revolution;
        array[1] = n9 + revolution;
        return n;
    }
    
    private static void sun_RA_decAtDay(double n, final double[] array, final double[] array2, final double[] array3) {
        final double[] array4 = { 0.0 };
        sunposAtDay(n, array4, array3);
        final double n2 = array3[0] * cosd(array4[0]);
        final double n3 = array3[0] * sind(array4[0]);
        final double n4 = 23.4393 - n * 3.563E-7;
        n = cosd(n4) * n3;
        final double sind = sind(n4);
        array[0] = atan2d(n, n2);
        array2[0] = atan2d(n3 * sind, Math.sqrt(n2 * n2 + n * n));
    }
    
    private static void sunposAtDay(final double n, final double[] array, final double[] array2) {
        final double revolution = revolution(0.9856002585 * n + 356.047);
        final double n2 = 0.016709 - n * 1.151E-9;
        final double n3 = 57.29577951308232 * n2 * sind(revolution) * (cosd(revolution) * n2 + 1.0) + revolution;
        final double n4 = cosd(n3) - n2;
        final double n5 = Math.sqrt(1.0 - n2 * n2) * sind(n3);
        array2[0] = Math.sqrt(n4 * n4 + n5 * n5);
        array[0] = atan2d(n5, n4) + (4.70935E-5 * n + 282.9404);
        if (array[0] >= 360.0) {
            array[0] -= 360.0;
        }
    }
    
    private static double tand(final double n) {
        return Math.tan(n * 0.017453292519943295);
    }
}
