// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

import android.graphics.Matrix;
import android.content.res.Resources;
import android.graphics.PointF;
import java.io.Closeable;
import com.airbnb.lottie.animation.content.TrimPathContent;
import com.airbnb.lottie.L;
import android.graphics.Path;
import android.graphics.PathMeasure;

public final class Utils
{
    private static final float SQRT_2;
    private static float dpScale;
    private static final PathMeasure pathMeasure;
    private static final float[] points;
    private static final Path tempPath;
    private static final Path tempPath2;
    
    static {
        pathMeasure = new PathMeasure();
        tempPath = new Path();
        tempPath2 = new Path();
        points = new float[4];
        SQRT_2 = (float)Math.sqrt(2.0);
        Utils.dpScale = -1.0f;
    }
    
    public static void applyTrimPathIfNeeded(final Path path, float min, float n, float n2) {
        L.beginSection("applyTrimPathIfNeeded");
        Utils.pathMeasure.setPath(path, false);
        final float length = Utils.pathMeasure.getLength();
        if (min == 1.0f && n == 0.0f) {
            L.endSection("applyTrimPathIfNeeded");
            return;
        }
        if (length < 1.0f || Math.abs(n - min - 1.0f) < 0.01) {
            L.endSection("applyTrimPathIfNeeded");
            return;
        }
        final float n3 = min * length;
        n *= length;
        min = Math.min(n3, n);
        final float max = Math.max(n3, n);
        n2 *= length;
        n = min + n2;
        final float n4 = max + n2;
        n2 = n;
        min = n4;
        if (n >= length) {
            n2 = n;
            min = n4;
            if (n4 >= length) {
                n2 = (float)MiscUtils.floorMod(n, length);
                min = (float)MiscUtils.floorMod(n4, length);
            }
        }
        n = n2;
        if (n2 < 0.0f) {
            n = (float)MiscUtils.floorMod(n2, length);
        }
        n2 = min;
        if (min < 0.0f) {
            n2 = (float)MiscUtils.floorMod(min, length);
        }
        final float n5 = fcmpl(n, n2);
        if (n5 == 0) {
            path.reset();
            L.endSection("applyTrimPathIfNeeded");
            return;
        }
        min = n;
        if (n5 >= 0) {
            min = n - length;
        }
        Utils.tempPath.reset();
        Utils.pathMeasure.getSegment(min, n2, Utils.tempPath, true);
        if (n2 > length) {
            Utils.tempPath2.reset();
            Utils.pathMeasure.getSegment(0.0f, n2 % length, Utils.tempPath2, true);
            Utils.tempPath.addPath(Utils.tempPath2);
        }
        else if (min < 0.0f) {
            Utils.tempPath2.reset();
            Utils.pathMeasure.getSegment(min + length, length, Utils.tempPath2, true);
            Utils.tempPath.addPath(Utils.tempPath2);
        }
        path.set(Utils.tempPath);
        L.endSection("applyTrimPathIfNeeded");
    }
    
    public static void applyTrimPathIfNeeded(final Path path, final TrimPathContent trimPathContent) {
        if (trimPathContent == null) {
            return;
        }
        applyTrimPathIfNeeded(path, trimPathContent.getStart().getValue() / 100.0f, trimPathContent.getEnd().getValue() / 100.0f, trimPathContent.getOffset().getValue() / 360.0f);
    }
    
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            goto Label_0016;
        }
        try {
            closeable.close();
            goto Label_0016;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            goto Label_0016;
        }
    }
    
    public static Path createPath(final PointF pointF, final PointF pointF2, final PointF pointF3, final PointF pointF4) {
        final Path path = new Path();
        path.moveTo(pointF.x, pointF.y);
        if (pointF3 != null && pointF4 != null && (pointF3.length() != 0.0f || pointF4.length() != 0.0f)) {
            path.cubicTo(pointF3.x + pointF.x, pointF.y + pointF3.y, pointF2.x + pointF4.x, pointF2.y + pointF4.y, pointF2.x, pointF2.y);
        }
        else {
            path.lineTo(pointF2.x, pointF2.y);
        }
        return path;
    }
    
    public static float dpScale() {
        if (Utils.dpScale == -1.0f) {
            Utils.dpScale = Resources.getSystem().getDisplayMetrics().density;
        }
        return Utils.dpScale;
    }
    
    public static float getScale(final Matrix matrix) {
        Utils.points[0] = 0.0f;
        Utils.points[1] = 0.0f;
        Utils.points[2] = Utils.SQRT_2;
        Utils.points[3] = Utils.SQRT_2;
        matrix.mapPoints(Utils.points);
        return (float)Math.hypot(Utils.points[2] - Utils.points[0], Utils.points[3] - Utils.points[1]) / 2.0f;
    }
    
    public static int hashFor(final float n, final float n2, final float n3, final float n4) {
        int n5;
        if (n != 0.0f) {
            n5 = (int)(527 * n);
        }
        else {
            n5 = 17;
        }
        int n6 = n5;
        if (n2 != 0.0f) {
            n6 = (int)(n5 * 31 * n2);
        }
        int n7 = n6;
        if (n3 != 0.0f) {
            n7 = (int)(n6 * 31 * n3);
        }
        int n8 = n7;
        if (n4 != 0.0f) {
            n8 = (int)(n7 * 31 * n4);
        }
        return n8;
    }
    
    public static boolean isAtLeastVersion(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        boolean b = false;
        if (n < n4) {
            return false;
        }
        if (n > n4) {
            return true;
        }
        if (n2 < n5) {
            return false;
        }
        if (n2 > n5) {
            return true;
        }
        if (n3 >= n6) {
            b = true;
        }
        return b;
    }
}
