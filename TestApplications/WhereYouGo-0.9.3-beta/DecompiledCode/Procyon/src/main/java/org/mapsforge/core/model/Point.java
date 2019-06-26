// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.model;

import java.io.Serializable;

public class Point implements Comparable<Point>, Serializable
{
    private static final long serialVersionUID = 1L;
    public final double x;
    public final double y;
    
    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int compareTo(final Point point) {
        int n = 1;
        if (this.x <= point.x) {
            if (this.x < point.x) {
                n = -1;
            }
            else if (this.y <= point.y) {
                if (this.y < point.y) {
                    n = -1;
                }
                else {
                    n = 0;
                }
            }
        }
        return n;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof Point)) {
                b = false;
            }
            else {
                final Point point = (Point)o;
                if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(point.x)) {
                    b = false;
                }
                else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(point.y)) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.x);
        final int n = (int)(doubleToLongBits >>> 32 ^ doubleToLongBits);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.y);
        return (n + 31) * 31 + (int)(doubleToLongBits2 >>> 32 ^ doubleToLongBits2);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("x=");
        sb.append(this.x);
        sb.append(", y=");
        sb.append(this.y);
        return sb.toString();
    }
}
