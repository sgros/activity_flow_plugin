package org.osmdroid.util;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.constants.GeoConstants;
import org.osmdroid.views.util.constants.MathConstants;

public class GeoPoint implements IGeoPoint, MathConstants, GeoConstants, Parcelable, Serializable, Cloneable {
   public static final Creator CREATOR = new Creator() {
      public GeoPoint createFromParcel(Parcel var1) {
         return new GeoPoint(var1);
      }

      public GeoPoint[] newArray(int var1) {
         return new GeoPoint[var1];
      }
   };
   private double mAltitude;
   private double mLatitude;
   private double mLongitude;

   public GeoPoint(double var1, double var3) {
      this.mLatitude = var1;
      this.mLongitude = var3;
   }

   public GeoPoint(double var1, double var3, double var5) {
      this.mLatitude = var1;
      this.mLongitude = var3;
      this.mAltitude = var5;
   }

   @Deprecated
   public GeoPoint(int var1, int var2) {
      double var3 = (double)var1;
      Double.isNaN(var3);
      this.mLatitude = var3 / 1000000.0D;
      var3 = (double)var2;
      Double.isNaN(var3);
      this.mLongitude = var3 / 1000000.0D;
   }

   public GeoPoint(Location var1) {
      this(var1.getLatitude(), var1.getLongitude(), var1.getAltitude());
   }

   private GeoPoint(Parcel var1) {
      this.mLatitude = var1.readDouble();
      this.mLongitude = var1.readDouble();
      this.mAltitude = var1.readDouble();
   }

   // $FF: synthetic method
   GeoPoint(Parcel var1, Object var2) {
      this(var1);
   }

   public GeoPoint(GeoPoint var1) {
      this.mLatitude = var1.mLatitude;
      this.mLongitude = var1.mLongitude;
      this.mAltitude = var1.mAltitude;
   }

   public GeoPoint clone() {
      return new GeoPoint(this.mLatitude, this.mLongitude, this.mAltitude);
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (var1.getClass() != GeoPoint.class) {
         return false;
      } else {
         GeoPoint var4 = (GeoPoint)var1;
         boolean var3 = var2;
         if (var4.mLatitude == this.mLatitude) {
            var3 = var2;
            if (var4.mLongitude == this.mLongitude) {
               var3 = var2;
               if (var4.mAltitude == this.mAltitude) {
                  var3 = true;
               }
            }
         }

         return var3;
      }
   }

   public double getLatitude() {
      return this.mLatitude;
   }

   public double getLongitude() {
      return this.mLongitude;
   }

   public int hashCode() {
      return ((int)(this.mLatitude * 1.0E-6D) * 17 + (int)(this.mLongitude * 1.0E-6D)) * 37 + (int)this.mAltitude;
   }

   public void setCoords(double var1, double var3) {
      this.mLatitude = var1;
      this.mLongitude = var3;
   }

   public void setLatitude(double var1) {
      this.mLatitude = var1;
   }

   public void setLongitude(double var1) {
      this.mLongitude = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.mLatitude);
      var1.append(",");
      var1.append(this.mLongitude);
      var1.append(",");
      var1.append(this.mAltitude);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeDouble(this.mLatitude);
      var1.writeDouble(this.mLongitude);
      var1.writeDouble(this.mAltitude);
   }
}
