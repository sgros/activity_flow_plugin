package org.osmdroid.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

public class BoundingBox implements Parcelable, Serializable {
    public static final Creator<BoundingBox> CREATOR = new C02611();
    private double mLatNorth;
    private double mLatSouth;
    private double mLonEast;
    private double mLonWest;

    /* renamed from: org.osmdroid.util.BoundingBox$1 */
    static class C02611 implements Creator<BoundingBox> {
        C02611() {
        }

        public BoundingBox createFromParcel(Parcel parcel) {
            return BoundingBox.readFromParcel(parcel);
        }

        public BoundingBox[] newArray(int i) {
            return new BoundingBox[i];
        }
    }

    public int describeContents() {
        return 0;
    }

    public BoundingBox(double d, double d2, double d3, double d4) {
        set(d, d2, d3, d4);
    }

    public void set(double d, double d2, double d3, double d4) {
        this.mLatNorth = d;
        this.mLonEast = d2;
        this.mLatSouth = d3;
        this.mLonWest = d4;
        TileSystem tileSystem = MapView.getTileSystem();
        StringBuilder stringBuilder;
        if (!tileSystem.isValidLatitude(d)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("north must be in ");
            stringBuilder.append(tileSystem.toStringLatitudeSpan());
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (!tileSystem.isValidLatitude(d3)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("south must be in ");
            stringBuilder.append(tileSystem.toStringLatitudeSpan());
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (!tileSystem.isValidLongitude(d4)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("west must be in ");
            stringBuilder.append(tileSystem.toStringLongitudeSpan());
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (!tileSystem.isValidLongitude(d2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("east must be in ");
            stringBuilder.append(tileSystem.toStringLongitudeSpan());
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public BoundingBox clone() {
        return new BoundingBox(this.mLatNorth, this.mLonEast, this.mLatSouth, this.mLonWest);
    }

    public GeoPoint getCenterWithDateLine() {
        return new GeoPoint(getCenterLatitude(), getCenterLongitude());
    }

    public double getLatNorth() {
        return this.mLatNorth;
    }

    public double getLatSouth() {
        return this.mLatSouth;
    }

    public double getCenterLatitude() {
        return (this.mLatNorth + this.mLatSouth) / 2.0d;
    }

    public double getCenterLongitude() {
        return getCenterLongitude(this.mLonWest, this.mLonEast);
    }

    public static double getCenterLongitude(double d, double d2) {
        double d3 = (d2 + d) / 2.0d;
        if (d2 < d) {
            d3 += 180.0d;
        }
        return MapView.getTileSystem().cleanLongitude(d3);
    }

    public double getActualNorth() {
        return Math.max(this.mLatNorth, this.mLatSouth);
    }

    public double getActualSouth() {
        return Math.min(this.mLatNorth, this.mLatSouth);
    }

    public double getLonEast() {
        return this.mLonEast;
    }

    public double getLonWest() {
        return this.mLonWest;
    }

    public double getLatitudeSpan() {
        return Math.abs(this.mLatNorth - this.mLatSouth);
    }

    @Deprecated
    public double getLongitudeSpan() {
        return Math.abs(this.mLonEast - this.mLonWest);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("N:");
        stringBuffer.append(this.mLatNorth);
        stringBuffer.append("; E:");
        stringBuffer.append(this.mLonEast);
        stringBuffer.append("; S:");
        stringBuffer.append(this.mLatSouth);
        stringBuffer.append("; W:");
        stringBuffer.append(this.mLonWest);
        return stringBuffer.toString();
    }

    public static BoundingBox fromGeoPoints(List<? extends IGeoPoint> list) {
        double d = -1.7976931348623157E308d;
        double d2 = d;
        double d3 = Double.MAX_VALUE;
        double d4 = d3;
        for (IGeoPoint iGeoPoint : list) {
            double latitude = iGeoPoint.getLatitude();
            double longitude = iGeoPoint.getLongitude();
            d3 = Math.min(d3, latitude);
            d4 = Math.min(d4, longitude);
            d = Math.max(d, latitude);
            d2 = Math.max(d2, longitude);
        }
        return new BoundingBox(d, d2, d3, d4);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.mLatNorth);
        parcel.writeDouble(this.mLonEast);
        parcel.writeDouble(this.mLatSouth);
        parcel.writeDouble(this.mLonWest);
    }

    private static BoundingBox readFromParcel(Parcel parcel) {
        return new BoundingBox(parcel.readDouble(), parcel.readDouble(), parcel.readDouble(), parcel.readDouble());
    }
}
