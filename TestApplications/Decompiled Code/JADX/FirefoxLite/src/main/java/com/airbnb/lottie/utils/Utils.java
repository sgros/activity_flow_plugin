package com.airbnb.lottie.utils;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.C0352L;
import com.airbnb.lottie.animation.content.TrimPathContent;
import java.io.Closeable;

public final class Utils {
    private static final float SQRT_2 = ((float) Math.sqrt(2.0d));
    private static float dpScale = -1.0f;
    private static final PathMeasure pathMeasure = new PathMeasure();
    private static final float[] points = new float[4];
    private static final Path tempPath = new Path();
    private static final Path tempPath2 = new Path();

    public static int hashFor(float f, float f2, float f3, float f4) {
        int i = f != 0.0f ? (int) (((float) 527) * f) : 17;
        if (f2 != 0.0f) {
            i = (int) (((float) (i * 31)) * f2);
        }
        if (f3 != 0.0f) {
            i = (int) (((float) (i * 31)) * f3);
        }
        return f4 != 0.0f ? (int) (((float) (i * 31)) * f4) : i;
    }

    public static boolean isAtLeastVersion(int i, int i2, int i3, int i4, int i5, int i6) {
        boolean z = false;
        if (i < i4) {
            return false;
        }
        if (i > i4) {
            return true;
        }
        if (i2 < i5) {
            return false;
        }
        if (i2 > i5) {
            return true;
        }
        if (i3 >= i6) {
            z = true;
        }
        return z;
    }

    public static Path createPath(PointF pointF, PointF pointF2, PointF pointF3, PointF pointF4) {
        Path path = new Path();
        path.moveTo(pointF.x, pointF.y);
        if (pointF3 == null || pointF4 == null || (pointF3.length() == 0.0f && pointF4.length() == 0.0f)) {
            path.lineTo(pointF2.x, pointF2.y);
        } else {
            Path path2 = path;
            path2.cubicTo(pointF3.x + pointF.x, pointF.y + pointF3.y, pointF2.x + pointF4.x, pointF2.y + pointF4.y, pointF2.x, pointF2.y);
        }
        return path;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    public static float getScale(Matrix matrix) {
        points[0] = 0.0f;
        points[1] = 0.0f;
        points[2] = SQRT_2;
        points[3] = SQRT_2;
        matrix.mapPoints(points);
        return ((float) Math.hypot((double) (points[2] - points[0]), (double) (points[3] - points[1]))) / 2.0f;
    }

    public static void applyTrimPathIfNeeded(Path path, TrimPathContent trimPathContent) {
        if (trimPathContent != null) {
            applyTrimPathIfNeeded(path, ((Float) trimPathContent.getStart().getValue()).floatValue() / 100.0f, ((Float) trimPathContent.getEnd().getValue()).floatValue() / 100.0f, ((Float) trimPathContent.getOffset().getValue()).floatValue() / 360.0f);
        }
    }

    public static void applyTrimPathIfNeeded(Path path, float f, float f2, float f3) {
        C0352L.beginSection("applyTrimPathIfNeeded");
        pathMeasure.setPath(path, false);
        float length = pathMeasure.getLength();
        if (f == 1.0f && f2 == 0.0f) {
            C0352L.endSection("applyTrimPathIfNeeded");
        } else if (length < 1.0f || ((double) Math.abs((f2 - f) - 1.0f)) < 0.01d) {
            C0352L.endSection("applyTrimPathIfNeeded");
        } else {
            f *= length;
            f2 *= length;
            f3 *= length;
            float min = Math.min(f, f2) + f3;
            f = Math.max(f, f2) + f3;
            if (min >= length && f >= length) {
                min = (float) MiscUtils.floorMod(min, length);
                f = (float) MiscUtils.floorMod(f, length);
            }
            if (min < 0.0f) {
                min = (float) MiscUtils.floorMod(min, length);
            }
            if (f < 0.0f) {
                f = (float) MiscUtils.floorMod(f, length);
            }
            int i = (min > f ? 1 : (min == f ? 0 : -1));
            if (i == 0) {
                path.reset();
                C0352L.endSection("applyTrimPathIfNeeded");
                return;
            }
            if (i >= 0) {
                min -= length;
            }
            tempPath.reset();
            pathMeasure.getSegment(min, f, tempPath, true);
            if (f > length) {
                tempPath2.reset();
                pathMeasure.getSegment(0.0f, f % length, tempPath2, true);
                tempPath.addPath(tempPath2);
            } else if (min < 0.0f) {
                tempPath2.reset();
                pathMeasure.getSegment(min + length, length, tempPath2, true);
                tempPath.addPath(tempPath2);
            }
            path.set(tempPath);
            C0352L.endSection("applyTrimPathIfNeeded");
        }
    }

    public static float dpScale() {
        if (dpScale == -1.0f) {
            dpScale = Resources.getSystem().getDisplayMetrics().density;
        }
        return dpScale;
    }
}
