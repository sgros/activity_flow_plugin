package org.telegram.p004ui.Components.Paint;

import android.graphics.PointF;

/* renamed from: org.telegram.ui.Components.Paint.Point */
public class Point {
    public boolean edge;
    /* renamed from: x */
    public double f591x;
    /* renamed from: y */
    public double f592y;
    /* renamed from: z */
    public double f593z;

    public Point(double d, double d2, double d3) {
        this.f591x = d;
        this.f592y = d2;
        this.f593z = d3;
    }

    public Point(Point point) {
        this.f591x = point.f591x;
        this.f592y = point.f592y;
        this.f593z = point.f593z;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point point = (Point) obj;
        if (this.f591x == point.f591x && this.f592y == point.f592y && this.f593z == point.f593z) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public Point multiplySum(Point point, double d) {
        return new Point((this.f591x + point.f591x) * d, (this.f592y + point.f592y) * d, (this.f593z + point.f593z) * d);
    }

    /* Access modifiers changed, original: 0000 */
    public Point multiplyAndAdd(double d, Point point) {
        return new Point((this.f591x * d) + point.f591x, (this.f592y * d) + point.f592y, (this.f593z * d) + point.f593z);
    }

    /* Access modifiers changed, original: 0000 */
    public void alteringAddMultiplication(Point point, double d) {
        this.f591x += point.f591x * d;
        this.f592y += point.f592y * d;
        this.f593z += point.f593z * d;
    }

    /* Access modifiers changed, original: 0000 */
    public Point add(Point point) {
        return new Point(this.f591x + point.f591x, this.f592y + point.f592y, this.f593z + point.f593z);
    }

    /* Access modifiers changed, original: 0000 */
    public Point substract(Point point) {
        return new Point(this.f591x - point.f591x, this.f592y - point.f592y, this.f593z - point.f593z);
    }

    /* Access modifiers changed, original: 0000 */
    public Point multiplyByScalar(double d) {
        return new Point(this.f591x * d, this.f592y * d, this.f593z * d);
    }

    /* Access modifiers changed, original: 0000 */
    public Point getNormalized() {
        return multiplyByScalar(1.0d / getMagnitude());
    }

    private double getMagnitude() {
        double d = this.f591x;
        d *= d;
        double d2 = this.f592y;
        d += d2 * d2;
        d2 = this.f593z;
        return Math.sqrt(d + (d2 * d2));
    }

    /* Access modifiers changed, original: 0000 */
    public float getDistanceTo(Point point) {
        return (float) Math.sqrt((Math.pow(this.f591x - point.f591x, 2.0d) + Math.pow(this.f592y - point.f592y, 2.0d)) + Math.pow(this.f593z - point.f593z, 2.0d));
    }

    /* Access modifiers changed, original: 0000 */
    public PointF toPointF() {
        return new PointF((float) this.f591x, (float) this.f592y);
    }
}
