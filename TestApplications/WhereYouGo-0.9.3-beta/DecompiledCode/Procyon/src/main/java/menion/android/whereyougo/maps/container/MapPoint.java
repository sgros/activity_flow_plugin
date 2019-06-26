// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.container;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class MapPoint implements Parcelable
{
    public static final Parcelable$Creator<MapPoint> CREATOR;
    private String data;
    private String description;
    private double latitude;
    private double longitude;
    private String name;
    private boolean target;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MapPoint>() {
            public MapPoint createFromParcel(final Parcel parcel) {
                return new MapPoint(parcel);
            }
            
            public MapPoint[] newArray(final int n) {
                return new MapPoint[n];
            }
        };
    }
    
    public MapPoint() {
    }
    
    public MapPoint(final Parcel parcel) {
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
        this.target = (parcel.readByte() > 0);
        this.data = parcel.readString();
    }
    
    public MapPoint(final String s, final double n, final double n2) {
        this(s, null, n, n2, false);
    }
    
    public MapPoint(final String s, final double n, final double n2, final boolean b) {
        this(s, null, n, n2, b);
    }
    
    public MapPoint(final String s, final String s2, final double n, final double n2) {
        this(s, s2, n, n2, false);
    }
    
    public MapPoint(final String name, final String description, final double latitude, final double longitude, final boolean target) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.target = target;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getData() {
        return this.data;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public double getLatitude() {
        return this.latitude;
    }
    
    public double getLongitude() {
        return this.longitude;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isTarget() {
        return this.target;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }
    
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTarget(final boolean target) {
        this.target = target;
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        if (this.target) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeByte((byte)n);
        parcel.writeString(this.data);
    }
}
