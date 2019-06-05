// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.animation.content.KeyPathElementContent;
import java.util.List;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.CubicCurveData;
import android.graphics.Path;
import com.airbnb.lottie.model.content.ShapeData;
import android.graphics.PointF;

public class MiscUtils
{
    public static PointF addPoints(final PointF pointF, final PointF pointF2) {
        return new PointF(pointF.x + pointF2.x, pointF.y + pointF2.y);
    }
    
    public static float clamp(final float b, final float a, final float a2) {
        return Math.max(a, Math.min(a2, b));
    }
    
    public static int clamp(final int b, final int a, final int a2) {
        return Math.max(a, Math.min(a2, b));
    }
    
    public static boolean contains(final float n, final float n2, final float n3) {
        return n >= n2 && n <= n3;
    }
    
    private static int floorDiv(final int n, final int n2) {
        final int n3 = n / n2;
        final boolean b = (n ^ n2) >= 0;
        int n4 = n3;
        if (!b) {
            n4 = n3;
            if (n % n2 != 0) {
                n4 = n3 - 1;
            }
        }
        return n4;
    }
    
    static int floorMod(final float n, final float n2) {
        return floorMod((int)n, (int)n2);
    }
    
    private static int floorMod(final int n, final int n2) {
        return n - n2 * floorDiv(n, n2);
    }
    
    public static void getPathFromData(final ShapeData shapeData, final Path path) {
        path.reset();
        final PointF initialPoint = shapeData.getInitialPoint();
        path.moveTo(initialPoint.x, initialPoint.y);
        final PointF pointF = new PointF(initialPoint.x, initialPoint.y);
        for (int i = 0; i < shapeData.getCurves().size(); ++i) {
            final CubicCurveData cubicCurveData = shapeData.getCurves().get(i);
            final PointF controlPoint1 = cubicCurveData.getControlPoint1();
            final PointF controlPoint2 = cubicCurveData.getControlPoint2();
            final PointF vertex = cubicCurveData.getVertex();
            if (controlPoint1.equals((Object)pointF) && controlPoint2.equals((Object)vertex)) {
                path.lineTo(vertex.x, vertex.y);
            }
            else {
                path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, vertex.x, vertex.y);
            }
            pointF.set(vertex.x, vertex.y);
        }
        if (shapeData.isClosed()) {
            path.close();
        }
    }
    
    public static double lerp(final double n, final double n2, final double n3) {
        return n + n3 * (n2 - n);
    }
    
    public static float lerp(final float n, final float n2, final float n3) {
        return n + n3 * (n2 - n);
    }
    
    public static int lerp(final int n, final int n2, final float n3) {
        return (int)(n + n3 * (n2 - n));
    }
    
    public static void resolveKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2, final KeyPathElementContent keyPathElementContent) {
        if (keyPath.fullyResolvesTo(keyPathElementContent.getName(), n)) {
            list.add(keyPath2.addKey(keyPathElementContent.getName()).resolve(keyPathElementContent));
        }
    }
}
