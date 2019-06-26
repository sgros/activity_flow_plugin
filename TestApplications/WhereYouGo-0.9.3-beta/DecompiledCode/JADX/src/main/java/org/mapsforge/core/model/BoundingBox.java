package org.mapsforge.core.model;

import java.io.Serializable;

public class BoundingBox implements Serializable {
    private static final long serialVersionUID = 1;
    public final double maxLatitude;
    public final double maxLongitude;
    public final double minLatitude;
    public final double minLongitude;

    public static BoundingBox fromString(String boundingBoxString) {
        double[] coordinates = CoordinatesUtil.parseCoordinateString(boundingBoxString, 4);
        return new BoundingBox(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
    }

    public BoundingBox(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        CoordinatesUtil.validateLatitude(minLatitude);
        CoordinatesUtil.validateLongitude(minLongitude);
        CoordinatesUtil.validateLatitude(maxLatitude);
        CoordinatesUtil.validateLongitude(maxLongitude);
        if (minLatitude > maxLatitude) {
            throw new IllegalArgumentException("invalid latitude range: " + minLatitude + ' ' + maxLatitude);
        } else if (minLongitude > maxLongitude) {
            throw new IllegalArgumentException("invalid longitude range: " + minLongitude + ' ' + maxLongitude);
        } else {
            this.minLatitude = minLatitude;
            this.minLongitude = minLongitude;
            this.maxLatitude = maxLatitude;
            this.maxLongitude = maxLongitude;
        }
    }

    public boolean contains(GeoPoint geoPoint) {
        return this.minLatitude <= geoPoint.latitude && this.maxLatitude >= geoPoint.latitude && this.minLongitude <= geoPoint.longitude && this.maxLongitude >= geoPoint.longitude;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BoundingBox)) {
            return false;
        }
        BoundingBox other = (BoundingBox) obj;
        if (Double.doubleToLongBits(this.maxLatitude) != Double.doubleToLongBits(other.maxLatitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.maxLongitude) != Double.doubleToLongBits(other.maxLongitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.minLatitude) != Double.doubleToLongBits(other.minLatitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.minLongitude) != Double.doubleToLongBits(other.minLongitude)) {
            return false;
        }
        return true;
    }

    public GeoPoint getCenterPoint() {
        return new GeoPoint(this.minLatitude + ((this.maxLatitude - this.minLatitude) / 2.0d), this.minLongitude + ((this.maxLongitude - this.minLongitude) / 2.0d));
    }

    public double getLatitudeSpan() {
        return this.maxLatitude - this.minLatitude;
    }

    public double getLongitudeSpan() {
        return this.maxLongitude - this.minLongitude;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.maxLatitude);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits(this.maxLongitude);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.minLatitude);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.minLongitude);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public boolean intersects(BoundingBox boundingBox) {
        if (this == boundingBox) {
            return true;
        }
        if (this.maxLatitude < boundingBox.minLatitude || this.maxLongitude < boundingBox.minLongitude || this.minLatitude > boundingBox.maxLatitude || this.minLongitude > boundingBox.maxLongitude) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("minLatitude=");
        stringBuilder.append(this.minLatitude);
        stringBuilder.append(", minLongitude=");
        stringBuilder.append(this.minLongitude);
        stringBuilder.append(", maxLatitude=");
        stringBuilder.append(this.maxLatitude);
        stringBuilder.append(", maxLongitude=");
        stringBuilder.append(this.maxLongitude);
        return stringBuilder.toString();
    }
}
