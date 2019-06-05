package android.support.p000v4.view.animation;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Interpolator;

/* renamed from: android.support.v4.view.animation.PathInterpolatorApi14 */
class PathInterpolatorApi14 implements Interpolator {
    private static final float PRECISION = 0.002f;
    /* renamed from: mX */
    private final float[] f25mX;
    /* renamed from: mY */
    private final float[] f26mY;

    PathInterpolatorApi14(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        int i = ((int) (length / PRECISION)) + 1;
        this.f25mX = new float[i];
        this.f26mY = new float[i];
        float[] fArr = new float[2];
        for (int i2 = 0; i2 < i; i2++) {
            pathMeasure.getPosTan((((float) i2) * length) / ((float) (i - 1)), fArr, null);
            this.f25mX[i2] = fArr[0];
            this.f26mY[i2] = fArr[1];
        }
    }

    PathInterpolatorApi14(float f, float f2) {
        this(PathInterpolatorApi14.createQuad(f, f2));
    }

    PathInterpolatorApi14(float f, float f2, float f3, float f4) {
        this(PathInterpolatorApi14.createCubic(f, f2, f3, f4));
    }

    public float getInterpolation(float f) {
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        int i = 0;
        int length = this.f25mX.length - 1;
        while (length - i > 1) {
            int i2 = (i + length) / 2;
            if (f < this.f25mX[i2]) {
                length = i2;
            } else {
                i = i2;
            }
        }
        float f2 = this.f25mX[length] - this.f25mX[i];
        if (f2 == 0.0f) {
            return this.f26mY[i];
        }
        f = (f - this.f25mX[i]) / f2;
        float f3 = this.f26mY[i];
        return f3 + (f * (this.f26mY[length] - f3));
    }

    private static Path createQuad(float f, float f2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(f, f2, 1.0f, 1.0f);
        return path;
    }

    private static Path createCubic(float f, float f2, float f3, float f4) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(f, f2, f3, f4, 1.0f, 1.0f);
        return path;
    }
}
