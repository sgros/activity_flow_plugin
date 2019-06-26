package androidx.core.graphics;

import android.graphics.Color;
import com.google.android.exoplayer2.util.NalUnitUtil;

public final class ColorUtils {
    private static final ThreadLocal<double[]> TEMP_ARRAY = new ThreadLocal();

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:19:0x0054 in {5, 13, 14, 15, 16, 18} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public static int calculateMinimumAlpha(int r8, int r9, float r10) {
        /*
        r0 = android.graphics.Color.alpha(r9);
        r1 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r0 != r1) goto L_0x0039;
        r0 = setAlphaComponent(r8, r1);
        r2 = calculateContrast(r0, r9);
        r4 = (double) r10;
        r10 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r10 >= 0) goto L_0x0017;
        r8 = -1;
        return r8;
        r10 = 0;
        r0 = 0;
        r2 = 10;
        if (r10 > r2) goto L_0x0038;
        r2 = r1 - r0;
        r3 = 1;
        if (r2 <= r3) goto L_0x0038;
        r2 = r0 + r1;
        r2 = r2 / 2;
        r3 = setAlphaComponent(r8, r2);
        r6 = calculateContrast(r3, r9);
        r3 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r3 >= 0) goto L_0x0034;
        r0 = r2;
        goto L_0x0035;
        r1 = r2;
        r10 = r10 + 1;
        goto L_0x0019;
        return r1;
        r8 = new java.lang.IllegalArgumentException;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r0 = "background can not be translucent: #";
        r10.append(r0);
        r9 = java.lang.Integer.toHexString(r9);
        r10.append(r9);
        r9 = r10.toString();
        r8.<init>(r9);
        throw r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.ColorUtils.calculateMinimumAlpha(int, int, float):int");
    }

    private static float constrain(float f, float f2, float f3) {
        return f < f2 ? f2 : f > f3 ? f3 : f;
    }

    public static int compositeColors(int i, int i2) {
        int alpha = Color.alpha(i2);
        int alpha2 = Color.alpha(i);
        int compositeAlpha = compositeAlpha(alpha2, alpha);
        return Color.argb(compositeAlpha, compositeComponent(Color.red(i), alpha2, Color.red(i2), alpha, compositeAlpha), compositeComponent(Color.green(i), alpha2, Color.green(i2), alpha, compositeAlpha), compositeComponent(Color.blue(i), alpha2, Color.blue(i2), alpha, compositeAlpha));
    }

    private static int compositeAlpha(int i, int i2) {
        return 255 - (((255 - i2) * (255 - i)) / NalUnitUtil.EXTENDED_SAR);
    }

    private static int compositeComponent(int i, int i2, int i3, int i4, int i5) {
        return i5 == 0 ? 0 : (((i * NalUnitUtil.EXTENDED_SAR) * i2) + ((i3 * i4) * (255 - i2))) / (i5 * NalUnitUtil.EXTENDED_SAR);
    }

    public static double calculateLuminance(int i) {
        double[] tempDouble3Array = getTempDouble3Array();
        colorToXYZ(i, tempDouble3Array);
        return tempDouble3Array[1] / 100.0d;
    }

    public static double calculateContrast(int i, int i2) {
        if (Color.alpha(i2) == NalUnitUtil.EXTENDED_SAR) {
            if (Color.alpha(i) < NalUnitUtil.EXTENDED_SAR) {
                i = compositeColors(i, i2);
            }
            double calculateLuminance = calculateLuminance(i) + 0.05d;
            double calculateLuminance2 = calculateLuminance(i2) + 0.05d;
            return Math.max(calculateLuminance, calculateLuminance2) / Math.min(calculateLuminance, calculateLuminance2);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("background can not be translucent: #");
        stringBuilder.append(Integer.toHexString(i2));
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static void RGBToHSL(int i, int i2, int i3, float[] fArr) {
        float f = ((float) i) / 255.0f;
        float f2 = ((float) i2) / 255.0f;
        float f3 = ((float) i3) / 255.0f;
        float max = Math.max(f, Math.max(f2, f3));
        float min = Math.min(f, Math.min(f2, f3));
        float f4 = max - min;
        float f5 = (max + min) / 2.0f;
        if (max == min) {
            f = 0.0f;
            f2 = 0.0f;
        } else {
            f = max == f ? ((f2 - f3) / f4) % 6.0f : max == f2 ? ((f3 - f) / f4) + 2.0f : ((f - f2) / f4) + 4.0f;
            f2 = f4 / (1.0f - Math.abs((2.0f * f5) - 1.0f));
        }
        f = (f * 60.0f) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        fArr[0] = constrain(f, 0.0f, 360.0f);
        fArr[1] = constrain(f2, 0.0f, 1.0f);
        fArr[2] = constrain(f5, 0.0f, 1.0f);
    }

    public static void colorToHSL(int i, float[] fArr) {
        RGBToHSL(Color.red(i), Color.green(i), Color.blue(i), fArr);
    }

    public static int setAlphaComponent(int i, int i2) {
        if (i2 >= 0 && i2 <= NalUnitUtil.EXTENDED_SAR) {
            return (i & 16777215) | (i2 << 24);
        }
        throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }

    public static void colorToXYZ(int i, double[] dArr) {
        RGBToXYZ(Color.red(i), Color.green(i), Color.blue(i), dArr);
    }

    public static void RGBToXYZ(int i, int i2, int i3, double[] dArr) {
        double[] dArr2 = dArr;
        if (dArr2.length == 3) {
            double d = (double) i;
            Double.isNaN(d);
            d /= 255.0d;
            if (d < 0.04045d) {
                d /= 12.92d;
            } else {
                d = Math.pow((d + 0.055d) / 1.055d, 2.4d);
            }
            double d2 = d;
            d = (double) i2;
            Double.isNaN(d);
            d /= 255.0d;
            if (d < 0.04045d) {
                d /= 12.92d;
            } else {
                d = Math.pow((d + 0.055d) / 1.055d, 2.4d);
            }
            double d3 = d;
            d = (double) i3;
            Double.isNaN(d);
            d /= 255.0d;
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
