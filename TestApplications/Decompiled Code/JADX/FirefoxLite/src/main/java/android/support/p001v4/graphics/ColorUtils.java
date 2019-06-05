package android.support.p001v4.graphics;

import android.graphics.Color;

/* renamed from: android.support.v4.graphics.ColorUtils */
public final class ColorUtils {
    private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal();

    public static int compositeColors(int i, int i2) {
        int alpha = Color.alpha(i2);
        int alpha2 = Color.alpha(i);
        int compositeAlpha = ColorUtils.compositeAlpha(alpha2, alpha);
        return Color.argb(compositeAlpha, ColorUtils.compositeComponent(Color.red(i), alpha2, Color.red(i2), alpha, compositeAlpha), ColorUtils.compositeComponent(Color.green(i), alpha2, Color.green(i2), alpha, compositeAlpha), ColorUtils.compositeComponent(Color.blue(i), alpha2, Color.blue(i2), alpha, compositeAlpha));
    }

    private static int compositeAlpha(int i, int i2) {
        return 255 - (((255 - i2) * (255 - i)) / 255);
    }

    private static int compositeComponent(int i, int i2, int i3, int i4, int i5) {
        return i5 == 0 ? 0 : (((i * 255) * i2) + ((i3 * i4) * (255 - i2))) / (i5 * 255);
    }

    public static double calculateLuminance(int i) {
        double[] tempDouble3Array = ColorUtils.getTempDouble3Array();
        ColorUtils.colorToXYZ(i, tempDouble3Array);
        return tempDouble3Array[1] / 100.0d;
    }

    public static double calculateContrast(int i, int i2) {
        if (Color.alpha(i2) == 255) {
            if (Color.alpha(i) < 255) {
                i = ColorUtils.compositeColors(i, i2);
            }
            double calculateLuminance = ColorUtils.calculateLuminance(i) + 0.05d;
            double calculateLuminance2 = ColorUtils.calculateLuminance(i2) + 0.05d;
            return Math.max(calculateLuminance, calculateLuminance2) / Math.min(calculateLuminance, calculateLuminance2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("background can not be translucent: #");
        stringBuilder.append(Integer.toHexString(i2));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static int setAlphaComponent(int i, int i2) {
        if (i2 >= 0 && i2 <= 255) {
            return (i & 16777215) | (i2 << 24);
        }
        throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }

    public static void colorToXYZ(int i, double[] dArr) {
        ColorUtils.RGBToXYZ(Color.red(i), Color.green(i), Color.blue(i), dArr);
    }

    public static void RGBToXYZ(int i, int i2, int i3, double[] dArr) {
        double[] dArr2 = dArr;
        if (dArr2.length == 3) {
            double d = ((double) i) / 255.0d;
            if (d < 0.04045d) {
                d /= 12.92d;
            } else {
                d = Math.pow((d + 0.055d) / 1.055d, 2.4d);
            }
            double d2 = d;
            d = ((double) i2) / 255.0d;
            if (d < 0.04045d) {
                d /= 12.92d;
            } else {
                d = Math.pow((d + 0.055d) / 1.055d, 2.4d);
            }
            double d3 = d;
            d = ((double) i3) / 255.0d;
            if (d < 0.04045d) {
                d /= 12.92d;
            } else {
                d = Math.pow((d + 0.055d) / 1.055d, 2.4d);
            }
            dArr2[0] = (((0.4124d * d2) + (0.3576d * d3)) + (0.1805d * d)) * 100.0d;
            dArr2[1] = (((0.2126d * d2) + (0.7152d * d3)) + (0.0722d * d)) * 100.0d;
            dArr2[2] = (((d2 * 0.0193d) + (d3 * 0.1192d)) + (d * 0.9505d)) * 100.0d;
            return;
        }
        throw new IllegalArgumentException("outXyz must have a length of 3.");
    }

    private static double[] getTempDouble3Array() {
        double[] dArr = (double[]) TEMP_ARRAY.get();
        if (dArr != null) {
            return dArr;
        }
        dArr = new double[3];
        TEMP_ARRAY.set(dArr);
        return dArr;
    }
}
