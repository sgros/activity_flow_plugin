// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.graphics.PointF;

public class Point
{
    public boolean edge;
    public double x;
    public double y;
    public double z;
    
    public Point(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Point(final Point point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }
    
    private double getMagnitude() {
        final double x = this.x;
        final double y = this.y;
        final double z = this.z;
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    Point add(final Point point) {
        return new Point(this.x + point.x, this.y + point.y, this.z + point.z);
    }
    
    void alteringAddMultiplication(final Point point, final double n) {
        this.x += point.x * n;
        this.y += point.y * n;
        this.z += point.z * n;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Point)) {
            return false;
        }
        final Point point = (Point)o;
        boolean b2 = b;
        if (this.x == point.x) {
            b2 = b;
            if (this.y == point.y) {
                b2 = b;
                if (this.z == point.z) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    float getDistanceTo(final Point point) {
        return (float)Math.sqrt(Math.pow(this.x - point.x, 2.0) + Math.pow(this.y - point.y, 2.0) + Math.pow(this.z - point.z, 2.0));
    }
    
    Point getNormalized() {
        return this.multiplyByScalar(1.0 / this.getMagnitude());
    }
    
    Point multiplyAndAdd(final double n, final Point point) {
        return new Point(this.x * n + point.x, this.y * n + point.y, this.z * n + point.z);
    }
    
    Point multiplyByScalar(final double n) {
        return new Point(this.x * n, this.y * n, this.z * n);
    }
    
    Point multiplySum(final Point point, final double n) {
        return new Point((this.x + point.x) * n, (this.y + point.y) * n, (this.z + point.z) * n);
    }
    
    Point substract(final Point point) {
        return new Point(this.x - point.x, this.y - point.y, this.z - point.z);
    }
    
    PointF toPointF() {
        return new PointF((float)this.x, (float)this.y);
    }
}
