package menion.android.whereyougo.maps.container;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MapPoint implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public MapPoint createFromParcel(Parcel var1) {
         return new MapPoint(var1);
      }

      public MapPoint[] newArray(int var1) {
         return new MapPoint[var1];
      }
   };
   private String data;
   private String description;
   private double latitude;
   private double longitude;
   private String name;
   private boolean target;

   public MapPoint() {
   }

   public MapPoint(Parcel var1) {
      this.name = var1.readString();
      this.description = var1.readString();
      this.latitude = var1.readDouble();
      this.longitude = var1.readDouble();
      boolean var2;
      if (var1.readByte() > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.target = var2;
      this.data = var1.readString();
   }

   public MapPoint(String var1, double var2, double var4) {
      this(var1, (String)null, var2, var4, false);
   }

   public MapPoint(String var1, double var2, double var4, boolean var6) {
      this(var1, (String)null, var2, var4, var6);
   }

   public MapPoint(String var1, String var2, double var3, double var5) {
      this(var1, var2, var3, var5, false);
   }

   public MapPoint(String var1, String var2, double var3, double var5, boolean var7) {
      this.name = var1;
      this.description = var2;
      this.latitude = var3;
      this.longitude = var5;
      this.target = var7;
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

   public void setData(String var1) {
      this.data = var1;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public void setLatitude(double var1) {
      this.latitude = var1;
   }

   public void setLongitude(double var1) {
      this.longitude = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setTarget(boolean var1) {
      this.target = var1;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.name);
      var1.writeString(this.description);
      var1.writeDouble(this.latitude);
      var1.writeDouble(this.longitude);
      byte var3;
      if (this.target) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      var1.writeByte((byte)var3);
      var1.writeString(this.data);
   }
}
