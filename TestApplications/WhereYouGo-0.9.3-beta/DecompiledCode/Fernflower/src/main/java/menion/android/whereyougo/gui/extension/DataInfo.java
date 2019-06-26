package menion.android.whereyougo.gui.extension;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import menion.android.whereyougo.geo.location.Location;

public class DataInfo implements Comparable {
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

   public DataInfo(int var1, String var2) {
      this(var1, var2, "", -1);
   }

   public DataInfo(int var1, String var2, Bitmap var3) {
      this(var1, var2, "", var3);
   }

   public DataInfo(int var1, String var2, String var3) {
      this(var1, var2, var3, -1);
   }

   public DataInfo(int var1, String var2, String var3, int var4) {
      this.enabled = true;
      this.distance = -1.0D;
      this.azimuth = -1.0D;
      this.setBasics(var1, var2, var3);
      this.image = var4;
   }

   public DataInfo(int var1, String var2, String var3, Bitmap var4) {
      this.enabled = true;
      this.distance = -1.0D;
      this.azimuth = -1.0D;
      this.setBasics(var1, var2, var3);
      this.imageB = var4;
   }

   public DataInfo(int var1, String var2, String var3, Drawable var4) {
      this.enabled = true;
      this.distance = -1.0D;
      this.azimuth = -1.0D;
      this.setBasics(var1, var2, var3);
      this.imageD = var4;
   }

   public DataInfo(String var1) {
      this(-1, var1, "", -1);
   }

   public DataInfo(String var1, String var2) {
      this(-1, var1, var2, -1);
   }

   public DataInfo(String var1, String var2, int var3) {
      this(-1, var1, var2, var3);
   }

   public DataInfo(String var1, String var2, Bitmap var3) {
      this(-1, var1, var2, (Bitmap)var3);
   }

   public DataInfo(String var1, String var2, Drawable var3) {
      this(-1, var1, var2, (Drawable)var3);
   }

   public DataInfo(String var1, String var2, Object var3) {
      this(-1, var1, var2, -1);
      this.addData01 = var3;
   }

   public DataInfo(DataInfo var1) {
      this.enabled = true;
      this.distance = -1.0D;
      this.azimuth = -1.0D;
      this.id = var1.id;
      this.name = var1.name;
      this.description = var1.description;
      this.image = var1.image;
      this.imageD = var1.imageD;
      this.imageB = var1.imageB;
      this.imageRight = var1.imageRight;
      this.value01 = var1.value01;
      this.value02 = var1.value02;
      this.distance = var1.distance;
      this.addData01 = var1.addData01;
   }

   private void setBasics(int var1, String var2, String var3) {
      this.id = var1;
      this.name = var2;
      this.description = var3;
      this.image = -1;
      this.imageD = null;
      this.imageB = null;
      this.imageRight = null;
   }

   public void addDescription(String var1) {
      if (this.description != null && this.description.length() != 0) {
         this.description = this.description + ", " + var1;
      } else {
         this.description = var1;
      }

   }

   public void clearDistAzi() {
      this.distance = -1.0D;
   }

   public int compareTo(DataInfo var1) {
      return this.name.compareTo(var1.getName());
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
      Location var1 = new Location("DataInfo");
      var1.setLatitude(this.value01);
      var1.setLongitude(this.value02);
      return var1;
   }

   public String getName() {
      return this.name;
   }

   public boolean isDistAziSet() {
      boolean var1;
      if (this.distance != -1.0D) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public DataInfo setAddData01(Object var1) {
      this.addData01 = var1;
      return this;
   }

   public void setCoordinates(double var1, double var3) {
      this.value01 = var1;
      this.value02 = var3;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public void setDistAzi(float var1, float var2) {
      this.distance = (double)var1;
      this.azimuth = (double)var2;
   }

   public void setDistAzi(Location var1) {
      Location var2 = this.getLocation();
      this.distance = (double)var1.distanceTo(var2);
      this.azimuth = (double)var1.bearingTo(var2);
   }

   public void setId(int var1) {
      this.id = var1;
   }

   public void setImage(int var1) {
      this.image = var1;
   }

   public void setImage(Bitmap var1) {
      this.imageB = var1;
   }

   public DataInfo setImageRight(Bitmap var1) {
      this.imageRight = var1;
      return this;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String toString() {
      return this.getName();
   }
}
