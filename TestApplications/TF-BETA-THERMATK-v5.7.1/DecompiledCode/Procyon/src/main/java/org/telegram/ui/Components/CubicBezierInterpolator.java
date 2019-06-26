// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.PointF;
import android.view.animation.Interpolator;

public class CubicBezierInterpolator implements Interpolator
{
    public static final CubicBezierInterpolator DEFAULT;
    public static final CubicBezierInterpolator EASE_BOTH;
    public static final CubicBezierInterpolator EASE_IN;
    public static final CubicBezierInterpolator EASE_OUT;
    public static final CubicBezierInterpolator EASE_OUT_QUINT;
    protected PointF a;
    protected PointF b;
    protected PointF c;
    protected PointF end;
    protected PointF start;
    
    static {
        DEFAULT = new CubicBezierInterpolator(0.25, 0.1, 0.25, 1.0);
        EASE_OUT = new CubicBezierInterpolator(0.0, 0.0, 0.58, 1.0);
        EASE_OUT_QUINT = new CubicBezierInterpolator(0.23, 1.0, 0.32, 1.0);
        EASE_IN = new CubicBezierInterpolator(0.42, 0.0, 1.0, 1.0);
        EASE_BOTH = new CubicBezierInterpolator(0.42, 0.0, 0.58, 1.0);
    }
    
    public CubicBezierInterpolator(final double n, final double n2, final double n3, final double n4) {
        this((float)n, (float)n2, (float)n3, (float)n4);
    }
    
    public CubicBezierInterpolator(final float n, final float n2, final float n3, final float n4) {
        this(new PointF(n, n2), new PointF(n3, n4));
    }
    
    public CubicBezierInterpolator(final PointF start, final PointF end) throws IllegalArgumentException {
        this.a = new PointF();
        this.b = new PointF();
        this.c = new PointF();
        final float x = start.x;
        if (x < 0.0f || x > 1.0f) {
            throw new IllegalArgumentException("startX value must be in the range [0, 1]");
        }
        final float x2 = end.x;
        if (x2 >= 0.0f && x2 <= 1.0f) {
            this.start = start;
            this.end = end;
            return;
        }
        throw new IllegalArgumentException("endX value must be in the range [0, 1]");
    }
    
    private float getBezierCoordinateX(final float n) {
        final PointF c = this.c;
        final PointF start = this.start;
        c.x = start.x * 3.0f;
        final PointF b = this.b;
        b.x = (this.end.x - start.x) * 3.0f - c.x;
        final PointF a = this.a;
        a.x = 1.0f - c.x - b.x;
        return n * (c.x + (b.x + a.x * n) * n);
    }
    
    private float getXDerivate(final float n) {
        return this.c.x + n * (this.b.x * 2.0f + this.a.x * 3.0f * n);
    }
    
    protected float getBezierCoordinateY(final float n) {
        final PointF c = this.c;
        final PointF start = this.start;
        c.y = start.y * 3.0f;
        final PointF b = this.b;
        b.y = (this.end.y - start.y) * 3.0f - c.y;
        final PointF a = this.a;
        a.y = 1.0f - c.y - b.y;
        return n * (c.y + (b.y + a.y * n) * n);
    }
    
    public float getInterpolation(final float n) {
        return this.getBezierCoordinateY(this.getXForTime(n));
    }
    
    protected float getXForTime(final float n) {
        int i = 1;
        float n2 = n;
        while (i < 14) {
            final float a = this.getBezierCoordinateX(n2) - n;
            if (Math.abs(a) < 0.001) {
                break;
            }
            n2 -= a / this.getXDerivate(n2);
            ++i;
        }
        return n2;
    }
}
