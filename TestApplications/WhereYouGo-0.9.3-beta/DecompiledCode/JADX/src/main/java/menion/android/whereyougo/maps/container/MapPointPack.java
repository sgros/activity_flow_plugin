package menion.android.whereyougo.maps.container;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class MapPointPack implements Parcelable {
    public static final Creator<MapPointPack> CREATOR = new C03051();
    private Bitmap icon;
    private boolean isPolygon;
    private ArrayList<MapPoint> points;
    private int resource;

    /* renamed from: menion.android.whereyougo.maps.container.MapPointPack$1 */
    static class C03051 implements Creator<MapPointPack> {
        C03051() {
        }

        public MapPointPack createFromParcel(Parcel p) {
            return new MapPointPack(p);
        }

        public MapPointPack[] newArray(int size) {
            return new MapPointPack[size];
        }
    }

    public MapPointPack() {
        this.icon = null;
        this.points = new ArrayList();
    }

    public MapPointPack(ArrayList<MapPoint> points, boolean isPolygon) {
        this.icon = null;
        this.points = points;
        this.isPolygon = isPolygon;
    }

    public MapPointPack(ArrayList<MapPoint> points, boolean isPolygon, Bitmap icon) {
        this((ArrayList) points, isPolygon);
        this.icon = icon;
    }

    public MapPointPack(ArrayList<MapPoint> points, boolean isPolygon, int resource) {
        this((ArrayList) points, isPolygon);
        this.resource = resource;
    }

    public MapPointPack(boolean isPolygon) {
        this(new ArrayList(), isPolygon);
    }

    public MapPointPack(boolean isPolygon, Bitmap icon) {
        this(new ArrayList(), isPolygon, icon);
    }

    public MapPointPack(boolean isPolygon, int resource) {
        this(new ArrayList(), isPolygon, resource);
    }

    public MapPointPack(Parcel p) {
        boolean z = true;
        this.icon = null;
        if (p.readInt() != 1) {
            z = false;
        }
        this.isPolygon = z;
        this.resource = p.readInt();
        this.points = p.readArrayList(getClass().getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public Bitmap getIcon() {
        return this.icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public ArrayList<MapPoint> getPoints() {
        return this.points;
    }

    public void setPoints(ArrayList<MapPoint> points) {
        this.points = points;
    }

    public int getResource() {
        return this.resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public boolean isPolygon() {
        return this.isPolygon;
    }

    public void setPolygon(boolean isPolygon) {
        this.isPolygon = isPolygon;
    }

    public void writeToParcel(Parcel p, int arg1) {
        p.writeInt(this.isPolygon ? 1 : 0);
        p.writeInt(this.resource);
        p.writeList(this.points);
    }
}
