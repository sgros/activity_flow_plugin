// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable$Creator;
import java.io.Serializable;
import android.os.Parcelable;
import org.osmdroid.util.constants.GeoConstants;
import org.osmdroid.views.util.constants.MathConstants;
import org.osmdroid.api.IGeoPoint;

public class GeoPoint implements IGeoPoint, MathConstants, GeoConstants, Parcelable, Serializable, Cloneable
{
    public static final Parcelable$Creator<GeoPoint> CREATOR;
    private double mAltitude;
    private double mLatitude;
    private double mLongitude;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<GeoPoint>() {
            public GeoPoint createFromParcel(final Parcel parcel) {
                return new GeoPoint(parcel, null);
            }
            
            public GeoPoint[] newArray(final int n) {
                return new GeoPoint[n];
            }
        };
    }
    
    public GeoPoint(final double mLatitude, final double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }
    
    public GeoPoint(final double mLatitude, final double mLongitude, final double mAltitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mAltitude = mAltitude;
    }
    
    @Deprecated
    public GeoPoint(final int n, final int n2) {
        final double v = n;
        Double.isNaN(v);
        this.mLatitude = v / 1000000.0;
        final double v2 = n2;
        Double.isNaN(v2);
        this.mLongitude = v2 / 1000000.0;
    }
    
    public GeoPoint(final Location location) {
        this(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }
    
    private GeoPoint(final Parcel parcel) {
        this.mLatitude = parcel.readDouble();
        this.mLongitude = parcel.readDouble();
        this.mAltitude = parcel.readDouble();
    }
    
    public GeoPoint(final GeoPoint geoPoint) {
        this.mLatitude = geoPoint.mLatitude;
        this.mLongitude = geoPoint.mLongitude;
        this.mAltitude = geoPoint.mAltitude;
    }
    
    public GeoPoint clone() {
        return new GeoPoint(this.mLatitude, this.mLongitude, this.mAltitude);
    }
    
    public int describeContents() {
        return 0;
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
        if (o.getClass() != GeoPoint.class) {
            return false;
        }
        final GeoPoint geoPoint = (GeoPoint)o;
        boolean b2 = b;
        if (geoPoint.mLatitude == this.mLatitude) {
            b2 = b;
            if (geoPoint.mLongitude == this.mLongitude) {
                b2 = b;
                if (geoPoint.mAltitude == this.mAltitude) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public double getLatitude() {
        return this.mLatitude;
    }
    
    @Override
    public double getLongitude() {
        return this.mLongitude;
    }
    
    @Override
    public int hashCode() {
        return ((int)(this.mLatitude * 1.0E-6) * 17 + (int)(this.mLongitude * 1.0E-6)) * 37 + (int)this.mAltitude;
    }
    
    public void setCoords(final double mLatitude, final double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }
    
    public void setLatitude(final double mLatitude) {
        this.mLatitude = mLatitude;
    }
    
    public void setLongitude(final double mLongitude) {
        this.mLongitude = mLongitude;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mLatitude);
        sb.append(",");
        sb.append(this.mLongitude);
        sb.append(",");
        sb.append(this.mAltitude);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeDouble(this.mAltitude);
    }
}
