// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.core.model;

import java.io.Serializable;

public class BoundingBox implements Serializable
{
    private static final long serialVersionUID = 1L;
    public final double maxLatitude;
    public final double maxLongitude;
    public final double minLatitude;
    public final double minLongitude;
    
    public BoundingBox(final double n, final double n2, final double n3, final double n4) {
        CoordinatesUtil.validateLatitude(n);
        CoordinatesUtil.validateLongitude(n2);
        CoordinatesUtil.validateLatitude(n3);
        CoordinatesUtil.validateLongitude(n4);
        if (n > n3) {
            throw new IllegalArgumentException("invalid latitude range: " + n + ' ' + n3);
        }
        if (n2 > n4) {
            throw new IllegalArgumentException("invalid longitude range: " + n2 + ' ' + n4);
        }
        this.minLatitude = n;
        this.minLongitude = n2;
        this.maxLatitude = n3;
        this.maxLongitude = n4;
    }
    
    public static BoundingBox fromString(final String s) {
        final double[] coordinateString = CoordinatesUtil.parseCoordinateString(s, 4);
        return new BoundingBox(coordinateString[0], coordinateString[1], coordinateString[2], coordinateString[3]);
    }
    
    public boolean contains(final GeoPoint geoPoint) {
        return this.minLatitude <= geoPoint.latitude && this.maxLatitude >= geoPoint.latitude && this.minLongitude <= geoPoint.longitude && this.maxLongitude >= geoPoint.longitude;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this != o) {
            if (!(o instanceof BoundingBox)) {
                b = false;
            }
            else {
                final BoundingBox boundingBox = (BoundingBox)o;
                if (Double.doubleToLongBits(this.maxLatitude) != Double.doubleToLongBits(boundingBox.maxLatitude)) {
                    b = false;
                }
                else if (Double.doubleToLongBits(this.maxLongitude) != Double.doubleToLongBits(boundingBox.maxLongitude)) {
                    b = false;
                }
                else if (Double.doubleToLongBits(this.minLatitude) != Double.doubleToLongBits(boundingBox.minLatitude)) {
                    b = false;
                }
                else if (Double.doubleToLongBits(this.minLongitude) != Double.doubleToLongBits(boundingBox.minLongitude)) {
                    b = false;
                }
            }
        }
        return b;
    }
    
    public GeoPoint getCenterPoint() {
        return new GeoPoint(this.minLatitude + (this.maxLatitude - this.minLatitude) / 2.0, this.minLongitude + (this.maxLongitude - this.minLongitude) / 2.0);
    }
    
    public double getLatitudeSpan() {
        return this.maxLatitude - this.minLatitude;
    }
    
    public double getLongitudeSpan() {
        return this.maxLongitude - this.minLongitude;
    }
    
    @Override
    public int hashCode() {
        final long doubleToLongBits = Double.doubleToLongBits(this.maxLatitude);
        final int n = (int)(doubleToLongBits >>> 32 ^ doubleToLongBits);
        final long doubleToLongBits2 = Double.doubleToLongBits(this.maxLongitude);
        final int n2 = (int)(doubleToLongBits2 >>> 32 ^ doubleToLongBits2);
        final long doubleToLongBits3 = Double.doubleToLongBits(this.minLatitude);
        final int n3 = (int)(doubleToLongBits3 >>> 32 ^ doubleToLongBits3);
        final long doubleToLongBits4 = Double.doubleToLongBits(this.minLongitude);
        return (((n + 31) * 31 + n2) * 31 + n3) * 31 + (int)(doubleToLongBits4 >>> 32 ^ doubleToLongBits4);
    }
    
    public boolean intersects(final BoundingBox boundingBox) {
        boolean b = true;
        if (this != boundingBox && (this.maxLatitude < boundingBox.minLatitude || this.maxLongitude < boundingBox.minLongitude || this.minLatitude > boundingBox.maxLatitude || this.minLongitude > boundingBox.maxLongitude)) {
            b = false;
        }
        return b;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("minLatitude=");
        sb.append(this.minLatitude);
        sb.append(", minLongitude=");
        sb.append(this.minLongitude);
        sb.append(", maxLatitude=");
        sb.append(this.maxLatitude);
        sb.append(", maxLongitude=");
        sb.append(this.maxLongitude);
        return sb.toString();
    }
}
