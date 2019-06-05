// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.data.filter;

import android.annotation.TargetApi;
import java.util.Arrays;

public class Approximator
{
    float[] concat(final float[]... array) {
        int n;
        for (int length = array.length, i = n = 0; i < length; ++i) {
            n += array[i].length;
        }
        final float[] array2 = new float[n];
        int n2;
        for (int length2 = array.length, j = n2 = 0; j < length2; ++j) {
            final float[] array3 = array[j];
            for (int length3 = array3.length, k = 0; k < length3; ++k) {
                array2[n2] = array3[k];
                ++n2;
            }
        }
        return array2;
    }
    
    @TargetApi(9)
    public float[] reduceWithDouglasPeucker(float[] reduceWithDouglasPeucker, final float n) {
        final float n2 = reduceWithDouglasPeucker[0];
        final float n3 = reduceWithDouglasPeucker[1];
        float n4 = 0.0f;
        final Line line = new Line(n2, n3, reduceWithDouglasPeucker[reduceWithDouglasPeucker.length - 2], reduceWithDouglasPeucker[reduceWithDouglasPeucker.length - 1]);
        int from = 0;
        float n5;
        for (int i = 2; i < reduceWithDouglasPeucker.length - 2; i += 2, n4 = n5) {
            final float distance = line.distance(reduceWithDouglasPeucker[i], reduceWithDouglasPeucker[i + 1]);
            n5 = n4;
            if (distance > n4) {
                from = i;
                n5 = distance;
            }
        }
        if (n4 > n) {
            final float[] reduceWithDouglasPeucker2 = this.reduceWithDouglasPeucker(Arrays.copyOfRange(reduceWithDouglasPeucker, 0, from + 2), n);
            reduceWithDouglasPeucker = this.reduceWithDouglasPeucker(Arrays.copyOfRange(reduceWithDouglasPeucker, from, reduceWithDouglasPeucker.length), n);
            return this.concat(new float[][] { reduceWithDouglasPeucker2, Arrays.copyOfRange(reduceWithDouglasPeucker, 2, reduceWithDouglasPeucker.length) });
        }
        return line.getPoints();
    }
    
    private class Line
    {
        private float dx;
        private float dy;
        private float exsy;
        private float length;
        private float[] points;
        private float sxey;
        
        public Line(final float n, final float n2, final float n3, final float n4) {
            this.dx = n - n3;
            this.dy = n2 - n4;
            this.sxey = n * n4;
            this.exsy = n3 * n2;
            this.length = (float)Math.sqrt(this.dx * this.dx + this.dy * this.dy);
            this.points = new float[] { n, n2, n3, n4 };
        }
        
        public float distance(final float n, final float n2) {
            return Math.abs(this.dy * n - this.dx * n2 + this.sxey - this.exsy) / this.length;
        }
        
        public float[] getPoints() {
            return this.points;
        }
    }
}
