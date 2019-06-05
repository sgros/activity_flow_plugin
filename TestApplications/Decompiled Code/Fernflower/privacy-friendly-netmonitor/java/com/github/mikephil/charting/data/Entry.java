package com.github.mikephil.charting.data;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.github.mikephil.charting.utils.Utils;

public class Entry extends BaseEntry implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public Entry createFromParcel(Parcel var1) {
         return new Entry(var1);
      }

      public Entry[] newArray(int var1) {
         return new Entry[var1];
      }
   };
   private float x = 0.0F;

   public Entry() {
   }

   public Entry(float var1, float var2) {
      super(var2);
      this.x = var1;
   }

   public Entry(float var1, float var2, Drawable var3) {
      super(var2, var3);
      this.x = var1;
   }

   public Entry(float var1, float var2, Drawable var3, Object var4) {
      super(var2, var3, var4);
      this.x = var1;
   }

   public Entry(float var1, float var2, Object var3) {
      super(var2, var3);
      this.x = var1;
   }

   protected Entry(Parcel var1) {
      this.x = var1.readFloat();
      this.setY(var1.readFloat());
      if (var1.readInt() == 1) {
         this.setData(var1.readParcelable(Object.class.getClassLoader()));
      }

   }

   public Entry copy() {
      return new Entry(this.x, this.getY(), this.getData());
   }

   public int describeContents() {
      return 0;
   }

   public boolean equalTo(Entry var1) {
      if (var1 == null) {
         return false;
      } else if (var1.getData() != this.getData()) {
         return false;
      } else if (Math.abs(var1.x - this.x) > Utils.FLOAT_EPSILON) {
         return false;
      } else {
         return Math.abs(var1.getY() - this.getY()) <= Utils.FLOAT_EPSILON;
      }
   }

   public float getX() {
      return this.x;
   }

   public void setX(float var1) {
      this.x = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Entry, x: ");
      var1.append(this.x);
      var1.append(" y: ");
      var1.append(this.getY());
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeFloat(this.x);
      var1.writeFloat(this.getY());
      if (this.getData() != null) {
         if (!(this.getData() instanceof Parcelable)) {
            throw new ParcelFormatException("Cannot parcel an Entry with non-parcelable data");
         }

         var1.writeInt(1);
         var1.writeParcelable((Parcelable)this.getData(), var2);
      } else {
         var1.writeInt(0);
      }

   }
}
