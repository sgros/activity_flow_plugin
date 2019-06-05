package menion.android.whereyougo.maps.container;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MapPoint implements Parcelable {
    public static final Creator<MapPoint> CREATOR = new C03041();
    private String data;
    private String description;
    private double latitude;
    private double longitude;
    private String name;
    private boolean target;

    /* renamed from: menion.android.whereyougo.maps.container.MapPoint$1 */
    static class C03041 implements Creator<MapPoint> {
        C03041() {
        }

        public MapPoint createFromParcel(Parcel p) {
            return new MapPoint(p);
        }

        public MapPoint[] newArray(int size) {
            return new MapPoint[size];
        }
    }

    public MapPoint(Parcel p) {
        this.name = p.readString();
        this.description = p.readString();
        this.latitude = p.readDouble();
        this.longitude = p.readDouble();
        this.target = p.readByte() > (byte) 0;
        this.data = p.readString();
    }

    public MapPoint(String name, double latitude, double longitude) {
        this(name, null, latitude, longitude, false);
    }

    public MapPoint(String name, double latitude, double longitude, boolean target) {
        this(name, null, latitude, longitude, target);
    }

    public MapPoint(String name, String description, double latitude, double longitude) {
        this(name, description, latitude, longitude, false);
    }

    public MapPoint(String name, String description, double latitude, double longitude, boolean target) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.target = target;
    }

    public int describeContents() {
        return 0;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isTarget() {
        return this.target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

    public void writeToParcel(Parcel p, int arg1) {
        p.writeString(this.name);
        p.writeString(this.description);
        p.writeDouble(this.latitude);
        p.writeDouble(this.longitude);
        p.writeByte((byte) (this.target ? 1 : 0));
        p.writeString(this.data);
    }
}
