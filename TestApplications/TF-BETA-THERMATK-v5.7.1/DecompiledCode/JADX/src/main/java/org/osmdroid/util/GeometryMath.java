package org.osmdroid.util;

import android.graphics.Rect;

public class GeometryMath {
    public static final Rect getBoundingBoxForRotatatedRectangle(Rect rect, int i, int i2, float f, Rect rect2) {
        Rect rect3;
        float f2;
        Rect rect4 = rect;
        int i3 = i;
        int i4 = i2;
        if (rect2 == null) {
            rect3 = new Rect();
            f2 = f;
        } else {
            f2 = f;
            rect3 = rect2;
        }
        double d = (double) f2;
        Double.isNaN(d);
        d *= 0.017453292519943295d;
        double sin = Math.sin(d);
        d = Math.cos(d);
        int i5 = rect4.left;
        double d2 = (double) (i5 - i3);
        int i6 = rect4.top;
        double d3 = (double) (i6 - i4);
        double d4 = (double) i3;
        Double.isNaN(d2);
        double d5 = d2 * d;
        Double.isNaN(d4);
        d5 = d4 - d5;
        Double.isNaN(d3);
        d5 += d3 * sin;
        int i7 = i5;
        Rect rect5 = rect3;
        double d6 = (double) i4;
        Double.isNaN(d2);
        d2 *= sin;
        Double.isNaN(d6);
        d2 = d6 - d2;
        Double.isNaN(d3);
        d2 -= d3 * d;
        int i8 = rect4.right;
        double d7 = d2;
        d2 = (double) (i8 - i3);
        int i9 = i8;
        double d8 = (double) (i6 - i4);
        Double.isNaN(d2);
        double d9 = d2 * d;
        Double.isNaN(d4);
        d9 = d4 - d9;
        Double.isNaN(d8);
        double d10 = d9 + (d8 * sin);
        Double.isNaN(d2);
        d2 *= sin;
        Double.isNaN(d6);
        d2 = d6 - d2;
        Double.isNaN(d8);
        d2 -= d8 * d;
        d8 = (double) (i7 - i3);
        int i10 = rect4.bottom;
        double d11 = d2;
        d2 = (double) (i10 - i4);
        Double.isNaN(d8);
        double d12 = d8 * d;
        Double.isNaN(d4);
        d12 = d4 - d12;
        Double.isNaN(d2);
        double d13 = d12 + (d2 * sin);
        Double.isNaN(d8);
        d8 *= sin;
        Double.isNaN(d6);
        d8 = d6 - d8;
        Double.isNaN(d2);
        d2 = d8 - (d2 * d);
        d8 = (double) (i9 - i3);
        double d14 = (double) (i10 - i4);
        Double.isNaN(d8);
        d12 = d8 * d;
        Double.isNaN(d4);
        d4 -= d12;
        Double.isNaN(d14);
        double d15 = d4 + (d14 * sin);
        Double.isNaN(d8);
        d8 *= sin;
        Double.isNaN(d6);
        d6 -= d8;
        Double.isNaN(d14);
        d14 = d6 - (d14 * d);
        Rect rect6 = rect5;
        rect6.left = MyMath.floorToInt(Min4(d5, d10, d13, d15));
        rect6.top = MyMath.floorToInt(Min4(d7, d11, d2, d14));
        rect6.right = MyMath.floorToInt(Max4(d5, d10, d13, d15));
        rect6.bottom = MyMath.floorToInt(Max4(d7, d11, d2, d14));
        return rect6;
    }

    private static double Min4(double d, double d2, double d3, double d4) {
        return Math.floor(Math.min(Math.min(d, d2), Math.min(d3, d4)));
    }

    private static double Max4(double d, double d2, double d3, double d4) {
        return Math.ceil(Math.max(Math.max(d, d2), Math.max(d3, d4)));
    }
}
