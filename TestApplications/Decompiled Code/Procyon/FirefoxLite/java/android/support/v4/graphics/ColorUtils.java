// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.graphics;

import android.graphics.Color;

public final class ColorUtils
{
    private static final ThreadLocal<double[]> TEMP_ARRAY;
    
    static {
        TEMP_ARRAY = new ThreadLocal<double[]>();
    }
    
    public static void RGBToXYZ(final int n, final int n2, final int n3, final double[] array) {
        if (array.length == 3) {
            final double n4 = n / 255.0;
            double pow;
            if (n4 < 0.04045) {
                pow = n4 / 12.92;
            }
            else {
                pow = Math.pow((n4 + 0.055) / 1.055, 2.4);
            }
            final double n5 = n2 / 255.0;
            double pow2;
            if (n5 < 0.04045) {
                pow2 = n5 / 12.92;
            }
            else {
                pow2 = Math.pow((n5 + 0.055) / 1.055, 2.4);
            }
            final double n6 = n3 / 255.0;
            double pow3;
            if (n6 < 0.04045) {
                pow3 = n6 / 12.92;
            }
            else {
                pow3 = Math.pow((n6 + 0.055) / 1.055, 2.4);
            }
            array[0] = (0.4124 * pow + 0.3576 * pow2 + 0.1805 * pow3) * 100.0;
            array[1] = (0.2126 * pow + 0.7152 * pow2 + 0.0722 * pow3) * 100.0;
            array[2] = (pow * 0.0193 + pow2 * 0.1192 + pow3 * 0.9505) * 100.0;
            return;
        }
        throw new IllegalArgumentException("outXyz must have a length of 3.");
    }
    
    public static double calculateContrast(final int n, final int i) {
        if (Color.alpha(i) == 255) {
            int compositeColors = n;
            if (Color.alpha(n) < 255) {
                compositeColors = compositeColors(n, i);
            }
            final double n2 = calculateLuminance(compositeColors) + 0.05;
            final double n3 = calculateLuminance(i) + 0.05;
            return Math.max(n2, n3) / Math.min(n2, n3);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("background can not be translucent: #");
        sb.append(Integer.toHexString(i));
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static double calculateLuminance(final int n) {
        final double[] tempDouble3Array = getTempDouble3Array();
        colorToXYZ(n, tempDouble3Array);
        return tempDouble3Array[1] / 100.0;
    }
    
    public static void colorToXYZ(final int n, final double[] array) {
        RGBToXYZ(Color.red(n), Color.green(n), Color.blue(n), array);
    }
    
    private static int compositeAlpha(final int n, final int n2) {
        return 255 - (255 - n2) * (255 - n) / 255;
    }
    
    public static int compositeColors(final int n, final int n2) {
        final int alpha = Color.alpha(n2);
        final int alpha2 = Color.alpha(n);
        final int compositeAlpha = compositeAlpha(alpha2, alpha);
        return Color.argb(compositeAlpha, compositeComponent(Color.red(n), alpha2, Color.red(n2), alpha, compositeAlpha), compositeComponent(Color.green(n), alpha2, Color.green(n2), alpha, compositeAlpha), compositeComponent(Color.blue(n), alpha2, Color.blue(n2), alpha, compositeAlpha));
    }
    
    private static int compositeComponent(final int n, final int n2, final int n3, final int n4, final int n5) {
        if (n5 == 0) {
            return 0;
        }
        return (n * 255 * n2 + n3 * n4 * (255 - n2)) / (n5 * 255);
    }
    
    private static double[] getTempDouble3Array() {
        double[] value;
        if ((value = ColorUtils.TEMP_ARRAY.get()) == null) {
            value = new double[3];
            ColorUtils.TEMP_ARRAY.set(value);
        }
        return value;
    }
    
    public static int setAlphaComponent(final int n, final int n2) {
        if (n2 >= 0 && n2 <= 255) {
            return (n & 0xFFFFFF) | n2 << 24;
        }
        throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }
}
