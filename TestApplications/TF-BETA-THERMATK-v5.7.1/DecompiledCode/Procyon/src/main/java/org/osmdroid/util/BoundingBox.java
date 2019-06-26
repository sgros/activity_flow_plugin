// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import org.osmdroid.views.MapView;
import java.util.Iterator;
import org.osmdroid.api.IGeoPoint;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import java.io.Serializable;
import android.os.Parcelable;

public class BoundingBox implements Parcelable, Serializable
{
    public static final Parcelable$Creator<BoundingBox> CREATOR;
    private double mLatNorth;
    private double mLatSouth;
    private double mLonEast;
    private double mLonWest;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<BoundingBox>() {
            public BoundingBox createFromParcel(final Parcel parcel) {
                return readFromParcel(parcel);
            }
            
            public BoundingBox[] newArray(final int n) {
                return new BoundingBox[n];
            }
        };
    }
    
    public BoundingBox() {
    }
    
    public BoundingBox(final double n, final double n2, final double n3, final double n4) {
        this.set(n, n2, n3, n4);
    }
    
    public static BoundingBox fromGeoPoints(final List<? extends IGeoPoint> list) {
        final Iterator<? extends IGeoPoint> iterator = list.iterator();
        double max2;
        double max = max2 = -1.7976931348623157E308;
        double min2;
        double min = min2 = Double.MAX_VALUE;
        while (iterator.hasNext()) {
            final IGeoPoint geoPoint = (IGeoPoint)iterator.next();
            final double latitude = geoPoint.getLatitude();
            final double longitude = geoPoint.getLongitude();
            min = Math.min(min, latitude);
            min2 = Math.min(min2, longitude);
            max = Math.max(max, latitude);
            max2 = Math.max(max2, longitude);
        }
        return new BoundingBox(max, max2, min, min2);
    }
    
    public static double getCenterLongitude(final double n, final double n2) {
        double n3 = (n2 + n) / 2.0;
        if (n2 < n) {
            n3 += 180.0;
        }
        return MapView.getTileSystem().cleanLongitude(n3);
    }
    
    private static BoundingBox readFromParcel(final Parcel parcel) {
        return new BoundingBox(parcel.readDouble(), parcel.readDouble(), parcel.readDouble(), parcel.readDouble());
    }
    
    public BoundingBox clone() {
        return new BoundingBox(this.mLatNorth, this.mLonEast, this.mLatSouth, this.mLonWest);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public double getActualNorth() {
        return Math.max(this.mLatNorth, this.mLatSouth);
    }
    
    public double getActualSouth() {
        return Math.min(this.mLatNorth, this.mLatSouth);
    }
    
    public double getCenterLatitude() {
        return (this.mLatNorth + this.mLatSouth) / 2.0;
    }
    
    public double getCenterLongitude() {
        return getCenterLongitude(this.mLonWest, this.mLonEast);
    }
    
    public GeoPoint getCenterWithDateLine() {
        return new GeoPoint(this.getCenterLatitude(), this.getCenterLongitude());
    }
    
    public double getLatNorth() {
        return this.mLatNorth;
    }
    
    public double getLatSouth() {
        return this.mLatSouth;
    }
    
    public double getLatitudeSpan() {
        return Math.abs(this.mLatNorth - this.mLatSouth);
    }
    
    public double getLonEast() {
        return this.mLonEast;
    }
    
    public double getLonWest() {
        return this.mLonWest;
    }
    
    @Deprecated
    public double getLongitudeSpan() {
        return Math.abs(this.mLonEast - this.mLonWest);
    }
    
    public void set(final double mLatNorth, final double mLonEast, final double mLatSouth, final double mLonWest) {
        this.mLatNorth = mLatNorth;
        this.mLonEast = mLonEast;
        this.mLatSouth = mLatSouth;
        this.mLonWest = mLonWest;
        final TileSystem tileSystem = MapView.getTileSystem();
        if (!tileSystem.isValidLatitude(mLatNorth)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("north must be in ");
            sb.append(tileSystem.toStringLatitudeSpan());
            throw new IllegalArgumentException(sb.toString());
        }
        if (!tileSystem.isValidLatitude(mLatSouth)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("south must be in ");
            sb2.append(tileSystem.toStringLatitudeSpan());
            throw new IllegalArgumentException(sb2.toString());
        }
        if (!tileSystem.isValidLongitude(mLonWest)) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("west must be in ");
            sb3.append(tileSystem.toStringLongitudeSpan());
            throw new IllegalArgumentException(sb3.toString());
        }
        if (tileSystem.isValidLongitude(mLonEast)) {
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("east must be in ");
        sb4.append(tileSystem.toStringLongitudeSpan());
        throw new IllegalArgumentException(sb4.toString());
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("N:");
        sb.append(this.mLatNorth);
        sb.append("; E:");
        sb.append(this.mLonEast);
        sb.append("; S:");
        sb.append(this.mLatSouth);
        sb.append("; W:");
        sb.append(this.mLonWest);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeDouble(this.mLatNorth);
        parcel.writeDouble(this.mLonEast);
        parcel.writeDouble(this.mLatSouth);
        parcel.writeDouble(this.mLonWest);
    }
}
