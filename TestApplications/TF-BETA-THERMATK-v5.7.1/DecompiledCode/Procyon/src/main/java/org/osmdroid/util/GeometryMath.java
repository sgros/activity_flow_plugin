// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import android.graphics.Rect;

public class GeometryMath
{
    private static double Max4(final double a, final double b, final double a2, final double b2) {
        return Math.ceil(Math.max(Math.max(a, b), Math.max(a2, b2)));
    }
    
    private static double Min4(final double a, final double b, final double a2, final double b2) {
        return Math.floor(Math.min(Math.min(a, b), Math.min(a2, b2)));
    }
    
    public static final Rect getBoundingBoxForRotatatedRectangle(final Rect rect, final int n, final int n2, final float n3, Rect rect2) {
        if (rect2 == null) {
            rect2 = new Rect();
        }
        final double v = n3;
        Double.isNaN(v);
        final double n4 = v * 0.017453292519943295;
        final double sin = Math.sin(n4);
        final double cos = Math.cos(n4);
        final int left = rect.left;
        final double n5 = left - n;
        final int top = rect.top;
        final double n6 = top - n2;
        final double n7 = n;
        Double.isNaN(n5);
        Double.isNaN(n7);
        Double.isNaN(n6);
        final double n8 = n7 - n5 * cos + n6 * sin;
        final double n9 = n2;
        Double.isNaN(n5);
        Double.isNaN(n9);
        Double.isNaN(n6);
        final double n10 = n9 - n5 * sin - n6 * cos;
        final int right = rect.right;
        final double n11 = right - n;
        final double n12 = top - n2;
        Double.isNaN(n11);
        Double.isNaN(n7);
        Double.isNaN(n12);
        final double n13 = n7 - n11 * cos + n12 * sin;
        Double.isNaN(n11);
        Double.isNaN(n9);
        Double.isNaN(n12);
        final double n14 = n9 - n11 * sin - n12 * cos;
        final double n15 = left - n;
        final int bottom = rect.bottom;
        final double n16 = bottom - n2;
        Double.isNaN(n15);
        Double.isNaN(n7);
        Double.isNaN(n16);
        final double n17 = n7 - n15 * cos + n16 * sin;
        Double.isNaN(n15);
        Double.isNaN(n9);
        Double.isNaN(n16);
        final double n18 = n9 - n15 * sin - n16 * cos;
        final double n19 = right - n;
        final double n20 = bottom - n2;
        Double.isNaN(n19);
        Double.isNaN(n7);
        Double.isNaN(n20);
        final double n21 = n7 - n19 * cos + n20 * sin;
        Double.isNaN(n19);
        Double.isNaN(n9);
        Double.isNaN(n20);
        final double n22 = n9 - n19 * sin - n20 * cos;
        rect2.left = MyMath.floorToInt(Min4(n8, n13, n17, n21));
        rect2.top = MyMath.floorToInt(Min4(n10, n14, n18, n22));
        rect2.right = MyMath.floorToInt(Max4(n8, n13, n17, n21));
        rect2.bottom = MyMath.floorToInt(Max4(n10, n14, n18, n22));
        return rect2;
    }
}
