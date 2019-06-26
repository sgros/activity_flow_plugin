// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import android.graphics.Rect;

public class RectL
{
    public long bottom;
    public long left;
    public long right;
    public long top;
    
    public RectL() {
    }
    
    public RectL(final long n, final long n2, final long n3, final long n4) {
        this.set(n, n2, n3, n4);
    }
    
    public static Rect getBounds(final Rect rect, int n, int n2, double cos, Rect rect2) {
        if (rect2 == null) {
            rect2 = new Rect();
        }
        if (cos == 0.0) {
            rect2.top = rect.top;
            rect2.left = rect.left;
            rect2.bottom = rect.bottom;
            rect2.right = rect.right;
            return rect2;
        }
        final double n3 = 3.141592653589793 * cos / 180.0;
        cos = Math.cos(n3);
        final double sin = Math.sin(n3);
        final int left = rect.left;
        final int top = rect.top;
        final long n4 = left;
        final long n5 = top;
        final long n6 = n;
        final long n7 = n2;
        n2 = (int)getRotatedX(n4, n5, n6, n7, cos, sin);
        n = (int)getRotatedY(n4, n5, n6, n7, cos, sin);
        rect2.bottom = n;
        rect2.top = n;
        rect2.right = n2;
        rect2.left = n2;
        n2 = rect.right;
        n = rect.top;
        final long n8 = n2;
        final long n9 = n;
        n = (int)getRotatedX(n8, n9, n6, n7, cos, sin);
        n2 = (int)getRotatedY(n8, n9, n6, n7, cos, sin);
        if (rect2.top > n2) {
            rect2.top = n2;
        }
        if (rect2.bottom < n2) {
            rect2.bottom = n2;
        }
        if (rect2.left > n) {
            rect2.left = n;
        }
        if (rect2.right < n) {
            rect2.right = n;
        }
        n2 = rect.right;
        n = rect.bottom;
        final long n10 = n2;
        final long n11 = n;
        n2 = (int)getRotatedX(n10, n11, n6, n7, cos, sin);
        n = (int)getRotatedY(n10, n11, n6, n7, cos, sin);
        if (rect2.top > n) {
            rect2.top = n;
        }
        if (rect2.bottom < n) {
            rect2.bottom = n;
        }
        if (rect2.left > n2) {
            rect2.left = n2;
        }
        if (rect2.right < n2) {
            rect2.right = n2;
        }
        n2 = rect.left;
        n = rect.bottom;
        final long n12 = n2;
        final long n13 = n;
        n2 = (int)getRotatedX(n12, n13, n6, n7, cos, sin);
        n = (int)getRotatedY(n12, n13, n6, n7, cos, sin);
        if (rect2.top > n) {
            rect2.top = n;
        }
        if (rect2.bottom < n) {
            rect2.bottom = n;
        }
        if (rect2.left > n2) {
            rect2.left = n2;
        }
        if (rect2.right < n2) {
            rect2.right = n2;
        }
        return rect2;
    }
    
    public static long getRotatedX(final long n, final long n2, final long n3, final long n4, final double n5, final double n6) {
        final double v = (double)(n - n3);
        Double.isNaN(v);
        final double v2 = (double)(n2 - n4);
        Double.isNaN(v2);
        return n3 + Math.round(v * n5 - v2 * n6);
    }
    
    public static long getRotatedY(final long n, final long n2, final long n3, final long n4, final double n5, final double n6) {
        final double v = (double)(n - n3);
        Double.isNaN(v);
        final double v2 = (double)(n2 - n4);
        Double.isNaN(v2);
        return n4 + Math.round(v * n6 + v2 * n5);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o != null && RectL.class == o.getClass()) {
            final RectL rectL = (RectL)o;
            if (this.left != rectL.left || this.top != rectL.top || this.right != rectL.right || this.bottom != rectL.bottom) {
                b = false;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)((((this.left * 31L + this.top) * 31L + this.right) * 31L + this.bottom) % 2147483647L);
    }
    
    public void set(final long left, final long top, final long right, final long bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RectL(");
        sb.append(this.left);
        sb.append(", ");
        sb.append(this.top);
        sb.append(" - ");
        sb.append(this.right);
        sb.append(", ");
        sb.append(this.bottom);
        sb.append(")");
        return sb.toString();
    }
}
