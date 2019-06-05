package org.mapsforge.core.model;

import java.io.Serializable;

public class Point implements Comparable<Point>, Serializable {
    private static final long serialVersionUID = 1;
    /* renamed from: x */
    public final double f68x;
    /* renamed from: y */
    public final double f69y;

    public Point(double x, double y) {
        this.f68x = x;
        this.f69y = y;
    }

    public int compareTo(Point point) {
        if (this.f68x > point.f68x) {
            return 1;
        }
        if (this.f68x < point.f68x) {
            return -1;
        }
        if (this.f69y > point.f69y) {
            return 1;
        }
        if (this.f69y < point.f69y) {
            return -1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point other = (Point) obj;
        if (Double.doubleToLongBits(this.f68x) != Double.doubleToLongBits(other.f68x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.f69y) != Double.doubleToLongBits(other.f69y)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.f68x);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits(this.f69y);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("x=");
        stringBuilder.append(this.f68x);
        stringBuilder.append(", y=");
        stringBuilder.append(this.f69y);
        return stringBuilder.toString();
    }
}
