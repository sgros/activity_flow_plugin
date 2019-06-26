package org.osmdroid.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;

public class BoundingBox implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public BoundingBox createFromParcel(Parcel var1) {
         return BoundingBox.readFromParcel(var1);
      }

      public BoundingBox[] newArray(int var1) {
         return new BoundingBox[var1];
      }
   };
   private double mLatNorth;
   private double mLatSouth;
   private double mLonEast;
   private double mLonWest;

   public BoundingBox() {
   }

   public BoundingBox(double var1, double var3, double var5, double var7) {
      this.set(var1, var3, var5, var7);
   }

   public static BoundingBox fromGeoPoints(List var0) {
      Iterator var14 = var0.iterator();
      double var1 = -1.7976931348623157E308D;
      double var3 = var1;
      double var5 = Double.MAX_VALUE;

      double var7;
      double var12;
      for(var7 = var5; var14.hasNext(); var3 = Math.max(var3, var12)) {
         IGeoPoint var9 = (IGeoPoint)var14.next();
         double var10 = var9.getLatitude();
         var12 = var9.getLongitude();
         var5 = Math.min(var5, var10);
         var7 = Math.min(var7, var12);
         var1 = Math.max(var1, var10);
      }

      return new BoundingBox(var1, var3, var5, var7);
   }

   public static double getCenterLongitude(double var0, double var2) {
      double var4 = (var2 + var0) / 2.0D;
      double var6 = var4;
      if (var2 < var0) {
         var6 = var4 + 180.0D;
      }

      return MapView.getTileSystem().cleanLongitude(var6);
   }

   private static BoundingBox readFromParcel(Parcel var0) {
      return new BoundingBox(var0.readDouble(), var0.readDouble(), var0.readDouble(), var0.readDouble());
   }

   public BoundingBox clone() {
      return new BoundingBox(this.mLatNorth, this.mLonEast, this.mLatSouth, this.mLonWest);
   }

   public int describeContents() {
      return 0;
   }

   public double getActualNorth() {
      return Math.max(this.mLatNorth, this.mLatSouth);
   }

   public double getActualSouth() {
      return Math.min(this.mLatNorth, this.mLatSouth);
   }

   public double getCenterLatitude() {
      return (this.mLatNorth + this.mLatSouth) / 2.0D;
   }

   public double getCenterLongitude() {
      return getCenterLongitude(this.mLonWest, this.mLonEast);
   }

   public GeoPoint getCenterWithDateLine() {
      return new GeoPoint(this.getCenterLatitude(), this.getCenterLongitude());
   }

   public double getLatNorth() {
      return this.mLatNorth;
   }

   public double getLatSouth() {
      return this.mLatSouth;
   }

   public double getLatitudeSpan() {
      return Math.abs(this.mLatNorth - this.mLatSouth);
   }

   public double getLonEast() {
      return this.mLonEast;
   }

   public double getLonWest() {
      return this.mLonWest;
   }

   @Deprecated
   public double getLongitudeSpan() {
      return Math.abs(this.mLonEast - this.mLonWest);
   }

   public void set(double var1, double var3, double var5, double var7) {
      this.mLatNorth = var1;
      this.mLonEast = var3;
      this.mLatSouth = var5;
      this.mLonWest = var7;
      TileSystem var9 = MapView.getTileSystem();
      StringBuilder var10;
      if (var9.isValidLatitude(var1)) {
         if (var9.isValidLatitude(var5)) {
            if (var9.isValidLongitude(var7)) {
               if (!var9.isValidLongitude(var3)) {
                  var10 = new StringBuilder();
                  var10.append("east must be in ");
                  var10.append(var9.toStringLongitudeSpan());
                  throw new IllegalArgumentException(var10.toString());
               }
            } else {
               var10 = new StringBuilder();
               var10.append("west must be in ");
               var10.append(var9.toStringLongitudeSpan());
               throw new IllegalArgumentException(var10.toString());
            }
         } else {
            var10 = new StringBuilder();
            var10.append("south must be in ");
            var10.append(var9.toStringLatitudeSpan());
            throw new IllegalArgumentException(var10.toString());
         }
      } else {
         var10 = new StringBuilder();
         var10.append("north must be in ");
         var10.append(var9.toStringLatitudeSpan());
         throw new IllegalArgumentException(var10.toString());
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("N:");
      var1.append(this.mLatNorth);
      var1.append("; E:");
      var1.append(this.mLonEast);
      var1.append("; S:");
      var1.append(this.mLatSouth);
      var1.append("; W:");
      var1.append(this.mLonWest);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeDouble(this.mLatNorth);
      var1.writeDouble(this.mLonEast);
      var1.writeDouble(this.mLatSouth);
      var1.writeDouble(this.mLonWest);
   }
}
