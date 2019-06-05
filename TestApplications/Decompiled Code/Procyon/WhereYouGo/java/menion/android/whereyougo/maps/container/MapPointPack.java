// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.container;

import java.util.List;
import android.os.Parcel;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class MapPointPack implements Parcelable
{
    public static final Parcelable$Creator<MapPointPack> CREATOR;
    private Bitmap icon;
    private boolean isPolygon;
    private ArrayList<MapPoint> points;
    private int resource;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MapPointPack>() {
            public MapPointPack createFromParcel(final Parcel parcel) {
                return new MapPointPack(parcel);
            }
            
            public MapPointPack[] newArray(final int n) {
                return new MapPointPack[n];
            }
        };
    }
    
    public MapPointPack() {
        this.icon = null;
        this.points = new ArrayList<MapPoint>();
    }
    
    public MapPointPack(final Parcel parcel) {
        boolean isPolygon = true;
        this.icon = null;
        if (parcel.readInt() != 1) {
            isPolygon = false;
        }
        this.isPolygon = isPolygon;
        this.resource = parcel.readInt();
        this.points = (ArrayList<MapPoint>)parcel.readArrayList(this.getClass().getClassLoader());
    }
    
    public MapPointPack(final ArrayList<MapPoint> points, final boolean isPolygon) {
        this.icon = null;
        this.points = points;
        this.isPolygon = isPolygon;
    }
    
    public MapPointPack(final ArrayList<MapPoint> list, final boolean b, final int resource) {
        this(list, b);
        this.resource = resource;
    }
    
    public MapPointPack(final ArrayList<MapPoint> list, final boolean b, final Bitmap icon) {
        this(list, b);
        this.icon = icon;
    }
    
    public MapPointPack(final boolean b) {
        this(new ArrayList<MapPoint>(), b);
    }
    
    public MapPointPack(final boolean b, final int n) {
        this(new ArrayList<MapPoint>(), b, n);
    }
    
    public MapPointPack(final boolean b, final Bitmap bitmap) {
        this(new ArrayList<MapPoint>(), b, bitmap);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Bitmap getIcon() {
        return this.icon;
    }
    
    public ArrayList<MapPoint> getPoints() {
        return this.points;
    }
    
    public int getResource() {
        return this.resource;
    }
    
    public boolean isPolygon() {
        return this.isPolygon;
    }
    
    public void setIcon(final Bitmap icon) {
        this.icon = icon;
    }
    
    public void setPoints(final ArrayList<MapPoint> points) {
        this.points = points;
    }
    
    public void setPolygon(final boolean isPolygon) {
        this.isPolygon = isPolygon;
    }
    
    public void setResource(final int resource) {
        this.resource = resource;
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        if (this.isPolygon) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        parcel.writeInt(this.resource);
        parcel.writeList((List)this.points);
    }
}
