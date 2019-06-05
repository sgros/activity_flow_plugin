package com.github.mikephil.charting.data.filter;

import android.annotation.TargetApi;
import java.util.Arrays;

public class Approximator {

    private class Line {
        /* renamed from: dx */
        private float f55dx;
        /* renamed from: dy */
        private float f56dy;
        private float exsy;
        private float length = ((float) Math.sqrt((double) ((this.f55dx * this.f55dx) + (this.f56dy * this.f56dy))));
        private float[] points;
        private float sxey;

        public Line(float f, float f2, float f3, float f4) {
            this.f55dx = f - f3;
            this.f56dy = f2 - f4;
            this.sxey = f * f4;
            this.exsy = f3 * f2;
            this.points = new float[]{f, f2, f3, f4};
        }

        public float distance(float f, float f2) {
            return Math.abs((((this.f56dy * f) - (this.f55dx * f2)) + this.sxey) - this.exsy) / this.length;
        }

        public float[] getPoints() {
            return this.points;
        }
    }

    @TargetApi(9)
    public float[] reduceWithDouglasPeucker(float[] fArr, float f) {
        float f2 = 0.0f;
        Line line = new Line(fArr[0], fArr[1], fArr[fArr.length - 2], fArr[fArr.length - 1]);
        int i = 0;
        for (int i2 = 2; i2 < fArr.length - 2; i2 += 2) {
            float distance = line.distance(fArr[i2], fArr[i2 + 1]);
            if (distance > f2) {
                i = i2;
                f2 = distance;
            }
        }
        if (f2 <= f) {
            return line.getPoints();
        }
        float[] reduceWithDouglasPeucker = reduceWithDouglasPeucker(Arrays.copyOfRange(fArr, 0, i + 2), f);
        fArr = reduceWithDouglasPeucker(Arrays.copyOfRange(fArr, i, fArr.length), f);
        fArr = Arrays.copyOfRange(fArr, 2, fArr.length);
        return concat(reduceWithDouglasPeucker, fArr);
    }

    /* Access modifiers changed, original: varargs */
    public float[] concat(float[]... fArr) {
        int i = 0;
        int i2 = i;
        while (i < fArr.length) {
            i2 += fArr[i].length;
            i++;
        }
        float[] fArr2 = new float[i2];
        i = fArr.length;
        i2 = 0;
        int i3 = i2;
        while (i2 < i) {
            int i4 = i3;
            for (float f : fArr[i2]) {
                fArr2[i4] = f;
                i4++;
            }
            i2++;
            i3 = i4;
        }
        return fArr2;
    }
}
