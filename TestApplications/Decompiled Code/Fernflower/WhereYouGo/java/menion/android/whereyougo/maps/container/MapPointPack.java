package menion.android.whereyougo.maps.container;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class MapPointPack implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public MapPointPack createFromParcel(Parcel var1) {
         return new MapPointPack(var1);
      }

      public MapPointPack[] newArray(int var1) {
         return new MapPointPack[var1];
      }
   };
   private Bitmap icon;
   private boolean isPolygon;
   private ArrayList points;
   private int resource;

   public MapPointPack() {
      this.icon = null;
      this.points = new ArrayList();
   }

   public MapPointPack(Parcel var1) {
      boolean var2 = true;
      super();
      this.icon = null;
      if (var1.readInt() != 1) {
         var2 = false;
      }

      this.isPolygon = var2;
      this.resource = var1.readInt();
      this.points = var1.readArrayList(this.getClass().getClassLoader());
   }

   public MapPointPack(ArrayList var1, boolean var2) {
      this.icon = null;
      this.points = var1;
      this.isPolygon = var2;
   }

   public MapPointPack(ArrayList var1, boolean var2, int var3) {
      this(var1, var2);
      this.resource = var3;
   }

   public MapPointPack(ArrayList var1, boolean var2, Bitmap var3) {
      this(var1, var2);
      this.icon = var3;
   }

   public MapPointPack(boolean var1) {
      this(new ArrayList(), var1);
   }

   public MapPointPack(boolean var1, int var2) {
      this(new ArrayList(), var1, var2);
   }

   public MapPointPack(boolean var1, Bitmap var2) {
      this(new ArrayList(), var1, var2);
   }

   public int describeContents() {
      return 0;
   }

   public Bitmap getIcon() {
      return this.icon;
   }

   public ArrayList getPoints() {
      return this.points;
   }

   public int getResource() {
      return this.resource;
   }

   public boolean isPolygon() {
      return this.isPolygon;
   }

   public void setIcon(Bitmap var1) {
      this.icon = var1;
   }

   public void setPoints(ArrayList var1) {
      this.points = var1;
   }

   public void setPolygon(boolean var1) {
      this.isPolygon = var1;
   }

   public void setResource(int var1) {
      this.resource = var1;
   }

   public void writeToParcel(Parcel var1, int var2) {
      byte var3;
      if (this.isPolygon) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      var1.writeInt(var3);
      var1.writeInt(this.resource);
      var1.writeList(this.points);
   }
}
