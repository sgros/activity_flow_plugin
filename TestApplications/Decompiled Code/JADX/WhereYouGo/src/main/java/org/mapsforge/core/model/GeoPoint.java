package org.mapsforge.core.model;

import java.io.Serializable;

public class GeoPoint implements Comparable<GeoPoint>, Serializable {
    private static final double EQUATORIAL_RADIUS = 6378137.0d;
    private static final long serialVersionUID = 1;
    public final double latitude;
    public final double longitude;

    public static GeoPoint fromString(String geoPointString) {
        double[] coordinates = CoordinatesUtil.parseCoordinateString(geoPointString, 2);
        return new GeoPoint(coordinates[0], coordinates[1]);
    }

    public static double latitudeDistance(int meters) {
        return ((double) (meters * 360)) / 4.007501668557849E7d;
    }

    public static double longitudeDistance(int meters, double latitude) {
        return ((double) (meters * 360)) / (4.007501668557849E7d * Math.cos(Math.toRadians(latitude)));
    }

    public GeoPoint(double latitude, double longitude) {
        CoordinatesUtil.validateLatitude(latitude);
        CoordinatesUtil.validateLongitude(longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int compareTo(GeoPoint geoPoint) {
        if (this.longitude > geoPoint.longitude) {
            return 1;
        }
        if (this.longitude < geoPoint.longitude) {
            return -1;
        }
        if (this.latitude > geoPoint.latitude) {
            return 1;
        }
        if (this.latitude < geoPoint.latitude) {
            return -1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeoPoint)) {
            return false;
        }
        GeoPoint other = (GeoPoint) obj;
        if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
            return false;
        }
        if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.latitude);
        int result = ((int) ((temp >>> 32) ^ temp)) + 31;
        temp = Double.doubleToLongBits(this.longitude);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("latitude=");
        stringBuilder.append(this.latitude);
        stringBuilder.append(", longitude=");
        stringBuilder.append(this.longitude);
        return stringBuilder.toString();
    }
}
