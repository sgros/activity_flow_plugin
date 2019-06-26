package menion.android.whereyougo.gui.extension;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import menion.android.whereyougo.geo.location.Location;

public class DataInfo implements Comparable<DataInfo> {
    private static final String TAG = "DataInfo";
    public Object addData01;
    public Object addData02;
    private double azimuth;
    private String description;
    private double distance;
    public boolean enabled;
    /* renamed from: id */
    private int f54id;
    private int image;
    private Bitmap imageB;
    private Drawable imageD;
    private Bitmap imageRight;
    private String name;
    public double value01;
    public double value02;

    public DataInfo(DataInfo con) {
        this.enabled = true;
        this.distance = -1.0d;
        this.azimuth = -1.0d;
        this.f54id = con.f54id;
        this.name = con.name;
        this.description = con.description;
        this.image = con.image;
        this.imageD = con.imageD;
        this.imageB = con.imageB;
        this.imageRight = con.imageRight;
        this.value01 = con.value01;
        this.value02 = con.value02;
        this.distance = con.distance;
        this.addData01 = con.addData01;
    }

    public DataInfo(int id, String name) {
        this(id, name, "", -1);
    }

    public DataInfo(int id, String name, Bitmap image) {
        this(id, name, "", image);
    }

    public DataInfo(int id, String name, String desc) {
        this(id, name, desc, -1);
    }

    public DataInfo(int id, String name, String description, Bitmap imageB) {
        this.enabled = true;
        this.distance = -1.0d;
        this.azimuth = -1.0d;
        setBasics(id, name, description);
        this.imageB = imageB;
    }

    public DataInfo(int id, String name, String description, Drawable imageD) {
        this.enabled = true;
        this.distance = -1.0d;
        this.azimuth = -1.0d;
        setBasics(id, name, description);
        this.imageD = imageD;
    }

    public DataInfo(int id, String name, String description, int image) {
        this.enabled = true;
        this.distance = -1.0d;
        this.azimuth = -1.0d;
        setBasics(id, name, description);
        this.image = image;
    }

    public DataInfo(String name) {
        this(-1, name, "", -1);
    }

    public DataInfo(String name, String description) {
        this(-1, name, description, -1);
    }

    public DataInfo(String name, String description, Bitmap image) {
        this(-1, name, description, image);
    }

    public DataInfo(String name, String description, Drawable image) {
        this(-1, name, description, image);
    }

    public DataInfo(String name, String description, int image) {
        this(-1, name, description, image);
    }

    public DataInfo(String name, String description, Object addData01) {
        this(-1, name, description, -1);
        this.addData01 = addData01;
    }

    public void addDescription(String desc) {
        if (this.description == null || this.description.length() == 0) {
            this.description = desc;
        } else {
            this.description += ", " + desc;
        }
    }

    public void clearDistAzi() {
        this.distance = -1.0d;
    }

    public int compareTo(DataInfo another) {
        return this.name.compareTo(another.getName());
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return this.f54id;
    }

    public void setId(int id) {
        this.f54id = id;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setImage(Bitmap imageB) {
        this.imageB = imageB;
    }

    public Bitmap getImageB() {
        return this.imageB;
    }

    public Drawable getImageD() {
        return this.imageD;
    }

    public Bitmap getImageRight() {
        return this.imageRight;
    }

    public DataInfo setImageRight(Bitmap image) {
        this.imageRight = image;
        return this;
    }

    public Location getLocation() {
        Location loc = new Location(TAG);
        loc.setLatitude(this.value01);
        loc.setLongitude(this.value02);
        return loc;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDistAziSet() {
        return this.distance != -1.0d;
    }

    public DataInfo setAddData01(Object data) {
        this.addData01 = data;
        return this;
    }

    private void setBasics(int id, String name, String description) {
        this.f54id = id;
        this.name = name;
        this.description = description;
        this.image = -1;
        this.imageD = null;
        this.imageB = null;
        this.imageRight = null;
    }

    public void setCoordinates(double lat, double lon) {
        this.value01 = lat;
        this.value02 = lon;
    }

    public void setDistAzi(float dist, float azi) {
        this.distance = (double) dist;
        this.azimuth = (double) azi;
    }

    public void setDistAzi(Location refLocation) {
        Location loc = getLocation();
        this.distance = (double) refLocation.distanceTo(loc);
        this.azimuth = (double) refLocation.bearingTo(loc);
    }

    public String toString() {
        return getName();
    }
}
