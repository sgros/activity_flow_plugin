package org.osmdroid.util;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.constants.GeoConstants;
import org.osmdroid.views.util.constants.MathConstants;

public class GeoPoint implements IGeoPoint, MathConstants, GeoConstants, Parcelable, Serializable, Cloneable {
    public static final Creator<GeoPoint> CREATOR = new C02641();
    private double mAltitude;
    private double mLatitude;
    private double mLongitude;

    /* renamed from: org.osmdroid.util.GeoPoint$1 */
    static class C02641 implements Creator<GeoPoint> {
        C02641() {
        }

        public GeoPoint createFromParcel(Parcel parcel) {
            return new GeoPoint(parcel, null);
        }

        public GeoPoint[] newArray(int i) {
            return new GeoPoint[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    @Deprecated
    public GeoPoint(int i, int i2) {
        double d = (double) i;
        Double.isNaN(d);
        this.mLatitude = d / 1000000.0d;
        double d2 = (double) i2;
        Double.isNaN(d2);
        this.mLongitude = d2 / 1000000.0d;
    }

    public GeoPoint(double d, double d2) {
        this.mLatitude = d;
        this.mLongitude = d2;
    }

    public GeoPoint(double d, double d2, double d3) {
        this.mLatitude = d;
        this.mLongitude = d2;
        this.mAltitude = d3;
    }

    public GeoPoint(Location location) {
        this(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    public GeoPoint(GeoPoint geoPoint) {
        this.mLatitude = geoPoint.mLatitude;
        this.mLongitude = geoPoint.mLongitude;
        this.mAltitude = geoPoint.mAltitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public void setLatitude(double d) {
        this.mLatitude = d;
    }

    public void setLongitude(double d) {
        this.mLongitude = d;
    }

    public void setCoords(double d, double d2) {
        this.mLatitude = d;
        this.mLongitude = d2;
    }

    public GeoPoint clone() {
        return new GeoPoint(this.mLatitude, this.mLongitude, this.mAltitude);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mLatitude);
        String str = ",";
        stringBuilder.append(str);
        stringBuilder.append(this.mLongitude);
        stringBuilder.append(str);
        stringBuilder.append(this.mAltitude);
        return stringBuilder.toString();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != GeoPoint.class) {
            return false;
        }
        GeoPoint geoPoint = (GeoPoint) obj;
        if (geoPoint.mLatitude == this.mLatitude && geoPoint.mLongitude == this.mLongitude && geoPoint.mAltitude == this.mAltitude) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (((((int) (this.mLatitude * 1.0E-6d)) * 17) + ((int) (this.mLongitude * 1.0E-6d))) * 37) + ((int) this.mAltitude);
    }

    private GeoPoint(Parcel parcel) {
        this.mLatitude = parcel.readDouble();
        this.mLongitude = parcel.readDouble();
        this.mAltitude = parcel.readDouble();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeDouble(this.mAltitude);
    }
}
