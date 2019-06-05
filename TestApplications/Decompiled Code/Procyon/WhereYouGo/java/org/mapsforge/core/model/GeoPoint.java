// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.model;

import java.io.Serializable;

public class GeoPoint implements Comparable<GeoPoint>, Serializable
{
    private static final double EQUATORIAL_RADIUS = 6378137.0;
    private static final long serialVersionUID = 1L;
    public final double latitude;
    public final double longitude;
    
    public GeoPoint(final double latitude, final double longitude) {
        CoordinatesUtil.validateLatitude(latitude);
        CoordinatesUtil.validateLongitude(longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public static GeoPoint fromString(final String s) {
        final double[] coordinateString = CoordinatesUtil.parseCoordinateString(s, 2);
        return new GeoPoint(coordinateString[0], coordinateString[1]);
    }
    
    public static double latitudeDistance(final int n) {
        return n * 360 / 4.007501668557849E7;
    }
    
    public static double longitudeDistance(final int n, final double angdeg) {
        return n * 360 / (4.007501668557849E7 * Math.cos(Math.toRadians(angdeg)));
    }
    
    @Override
    public int compareTo(final GeoPoint geoPoint) {
        int n = 1;
        if (this.longitude <= geoPoint.longitude) {
            if (this.longitude < geoPoint.longitude) {
                n = -1;
            }
            else if (this.latitude <= geoPoint.latitude) {
                if (this.latitude < geoPoint.latitude) {
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
            if (!(o instanceof GeoPoint)) {
                b = false;
            }
            else {
                final GeoPoint geoPoint = (GeoPoint)o;
                if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(geoPoint.latitude)) {
                    b = false;
                }
                else if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(geoPoint.longitude)) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.latitude);
        final int n = (int)(doubleToLongBits >>> 32 ^ doubleToLongBits);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.longitude);
        return (n + 31) * 31 + (int)(doubleToLongBits2 >>> 32 ^ doubleToLongBits2);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("latitude=");
        sb.append(this.latitude);
        sb.append(", longitude=");
        sb.append(this.longitude);
        return sb.toString();
    }
}
