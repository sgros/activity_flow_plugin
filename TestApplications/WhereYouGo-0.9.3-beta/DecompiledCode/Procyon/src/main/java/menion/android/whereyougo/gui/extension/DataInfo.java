// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.extension;

import menion.android.whereyougo.geo.location.Location;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;

public class DataInfo implements Comparable<DataInfo>
{
    private static final String TAG = "DataInfo";
    public Object addData01;
    public Object addData02;
    private double azimuth;
    private String description;
    private double distance;
    public boolean enabled;
    private int id;
    private int image;
    private Bitmap imageB;
    private Drawable imageD;
    private Bitmap imageRight;
    private String name;
    public double value01;
    public double value02;
    
    public DataInfo(final int n, final String s) {
        this(n, s, "", -1);
    }
    
    public DataInfo(final int n, final String s, final Bitmap bitmap) {
        this(n, s, "", bitmap);
    }
    
    public DataInfo(final int n, final String s, final String s2) {
        this(n, s, s2, -1);
    }
    
    public DataInfo(final int n, final String s, final String s2, final int image) {
        this.enabled = true;
        this.distance = -1.0;
        this.azimuth = -1.0;
        this.setBasics(n, s, s2);
        this.image = image;
    }
    
    public DataInfo(final int n, final String s, final String s2, final Bitmap imageB) {
        this.enabled = true;
        this.distance = -1.0;
        this.azimuth = -1.0;
        this.setBasics(n, s, s2);
        this.imageB = imageB;
    }
    
    public DataInfo(final int n, final String s, final String s2, final Drawable imageD) {
        this.enabled = true;
        this.distance = -1.0;
        this.azimuth = -1.0;
        this.setBasics(n, s, s2);
        this.imageD = imageD;
    }
    
    public DataInfo(final String s) {
        this(-1, s, "", -1);
    }
    
    public DataInfo(final String s, final String s2) {
        this(-1, s, s2, -1);
    }
    
    public DataInfo(final String s, final String s2, final int n) {
        this(-1, s, s2, n);
    }
    
    public DataInfo(final String s, final String s2, final Bitmap bitmap) {
        this(-1, s, s2, bitmap);
    }
    
    public DataInfo(final String s, final String s2, final Drawable drawable) {
        this(-1, s, s2, drawable);
    }
    
    public DataInfo(final String s, final String s2, final Object addData01) {
        this(-1, s, s2, -1);
        this.addData01 = addData01;
    }
    
    public DataInfo(final DataInfo dataInfo) {
        this.enabled = true;
        this.distance = -1.0;
        this.azimuth = -1.0;
        this.id = dataInfo.id;
        this.name = dataInfo.name;
        this.description = dataInfo.description;
        this.image = dataInfo.image;
        this.imageD = dataInfo.imageD;
        this.imageB = dataInfo.imageB;
        this.imageRight = dataInfo.imageRight;
        this.value01 = dataInfo.value01;
        this.value02 = dataInfo.value02;
        this.distance = dataInfo.distance;
        this.addData01 = dataInfo.addData01;
    }
    
    private void setBasics(final int id, final String name, final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = -1;
        this.imageD = null;
        this.imageB = null;
        this.imageRight = null;
    }
    
    public void addDescription(final String s) {
        if (this.description == null || this.description.length() == 0) {
            this.description = s;
        }
        else {
            this.description = this.description + ", " + s;
        }
    }
    
    public void clearDistAzi() {
        this.distance = -1.0;
    }
    
    @Override
    public int compareTo(final DataInfo dataInfo) {
        return this.name.compareTo(dataInfo.getName());
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getImage() {
        return this.image;
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
    
    public Location getLocation() {
        final Location location = new Location("DataInfo");
        location.setLatitude(this.value01);
        location.setLongitude(this.value02);
        return location;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isDistAziSet() {
        return this.distance != -1.0;
    }
    
    public DataInfo setAddData01(final Object addData01) {
        this.addData01 = addData01;
        return this;
    }
    
    public void setCoordinates(final double value01, final double value2) {
        this.value01 = value01;
        this.value02 = value2;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setDistAzi(final float n, final float n2) {
        this.distance = n;
        this.azimuth = n2;
    }
    
    public void setDistAzi(final Location location) {
        final Location location2 = this.getLocation();
        this.distance = location.distanceTo(location2);
        this.azimuth = location.bearingTo(location2);
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setImage(final int image) {
        this.image = image;
    }
    
    public void setImage(final Bitmap imageB) {
        this.imageB = imageB;
    }
    
    public DataInfo setImageRight(final Bitmap imageRight) {
        this.imageRight = imageRight;
        return this;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
