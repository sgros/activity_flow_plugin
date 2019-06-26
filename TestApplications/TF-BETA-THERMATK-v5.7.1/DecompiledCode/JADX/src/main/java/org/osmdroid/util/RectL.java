package org.osmdroid.util;

import android.graphics.Rect;

public class RectL {
    public long bottom;
    public long left;
    public long right;
    public long top;

    public RectL(long j, long j2, long j3, long j4) {
        set(j, j2, j3, j4);
    }

    public void set(long j, long j2, long j3, long j4) {
        this.left = j;
        this.top = j2;
        this.right = j3;
        this.bottom = j4;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RectL(");
        stringBuilder.append(this.left);
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(this.top);
        stringBuilder.append(" - ");
        stringBuilder.append(this.right);
        stringBuilder.append(str);
        stringBuilder.append(this.bottom);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || RectL.class != obj.getClass()) {
            return false;
        }
        RectL rectL = (RectL) obj;
        if (!(this.left == rectL.left && this.top == rectL.top && this.right == rectL.right && this.bottom == rectL.bottom)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (int) (((((((this.left * 31) + this.top) * 31) + this.right) * 31) + this.bottom) % 2147483647L);
    }

    public static Rect getBounds(Rect rect, int i, int i2, double d, Rect rect2) {
        Rect rect3 = rect;
        Rect rect4 = rect2 != null ? rect2 : new Rect();
        if (d == 0.0d) {
            rect4.top = rect3.top;
            rect4.left = rect3.left;
            rect4.bottom = rect3.bottom;
            rect4.right = rect3.right;
            return rect4;
        }
        double d2 = (3.141592653589793d * d) / 180.0d;
        double cos = Math.cos(d2);
        d2 = Math.sin(d2);
        long j = (long) rect3.left;
        long j2 = (long) rect3.top;
        long j3 = (long) i;
        long j4 = j2;
        long j5 = (long) i2;
        long j6 = j3;
        long j7 = j3;
        j3 = j5;
        long j8 = j2;
        double d3 = cos;
        long j9 = j;
        int rotatedX = (int) getRotatedX(j, j4, j6, j3, d3, d2);
        j6 = j7;
        int i3 = rotatedX;
        int rotatedY = (int) getRotatedY(j9, j8, j6, j3, d3, d2);
        rect4.bottom = rotatedY;
        rect4.top = rotatedY;
        rect4.right = i3;
        rect4.left = i3;
        rect3 = rect;
        j = (long) rect3.right;
        j2 = (long) rect3.top;
        j4 = j2;
        j8 = j2;
        d3 = cos;
        i3 = (int) getRotatedX(j, j4, j6, j3, d3, d2);
        rotatedY = (int) getRotatedY(j, j8, j6, j3, d3, d2);
        if (rect4.top > rotatedY) {
            rect4.top = rotatedY;
        }
        if (rect4.bottom < rotatedY) {
            rect4.bottom = rotatedY;
        }
        if (rect4.left > i3) {
            rect4.left = i3;
        }
        if (rect4.right < i3) {
            rect4.right = i3;
        }
        rect3 = rect;
        j = (long) rect3.right;
        j2 = (long) rect3.bottom;
        j4 = j2;
        j6 = j7;
        j3 = j5;
        j8 = j2;
        d3 = cos;
        i3 = (int) getRotatedX(j, j4, j6, j3, d3, d2);
        rotatedY = (int) getRotatedY(j, j8, j6, j3, d3, d2);
        if (rect4.top > rotatedY) {
            rect4.top = rotatedY;
        }
        if (rect4.bottom < rotatedY) {
            rect4.bottom = rotatedY;
        }
        if (rect4.left > i3) {
            rect4.left = i3;
        }
        if (rect4.right < i3) {
            rect4.right = i3;
        }
        rect3 = rect;
        j = (long) rect3.left;
        j2 = (long) rect3.bottom;
        long j10 = j;
        j4 = j2;
        j6 = j7;
        j3 = j5;
        j8 = j2;
        d3 = cos;
        j9 = j;
        double d4 = d2;
        i3 = (int) getRotatedX(j10, j4, j6, j3, d3, d4);
        int rotatedY2 = (int) getRotatedY(j9, j8, j6, j3, d3, d4);
        if (rect4.top > rotatedY2) {
            rect4.top = rotatedY2;
        }
        if (rect4.bottom < rotatedY2) {
            rect4.bottom = rotatedY2;
        }
        if (rect4.left > i3) {
            rect4.left = i3;
        }
        if (rect4.right < i3) {
            rect4.right = i3;
        }
        return rect4;
    }

    public static long getRotatedX(long j, long j2, long j3, long j4, double d, double d2) {
        double d3 = (double) (j - j3);
        Double.isNaN(d3);
        d3 *= d;
        double d4 = (double) (j2 - j4);
        Double.isNaN(d4);
        return j3 + Math.round(d3 - (d4 * d2));
    }

    public static long getRotatedY(long j, long j2, long j3, long j4, double d, double d2) {
        double d3 = (double) (j - j3);
        Double.isNaN(d3);
        d3 *= d2;
        double d4 = (double) (j2 - j4);
        Double.isNaN(d4);
        return j4 + Math.round(d3 + (d4 * d));
    }
}
