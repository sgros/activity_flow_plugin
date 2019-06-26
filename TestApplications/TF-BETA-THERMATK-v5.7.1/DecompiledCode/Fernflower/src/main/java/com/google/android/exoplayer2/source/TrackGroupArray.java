package com.google.android.exoplayer2.source;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class TrackGroupArray implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public TrackGroupArray createFromParcel(Parcel var1) {
         return new TrackGroupArray(var1);
      }

      public TrackGroupArray[] newArray(int var1) {
         return new TrackGroupArray[var1];
      }
   };
   public static final TrackGroupArray EMPTY = new TrackGroupArray(new TrackGroup[0]);
   private int hashCode;
   public final int length;
   private final TrackGroup[] trackGroups;

   TrackGroupArray(Parcel var1) {
      this.length = var1.readInt();
      this.trackGroups = new TrackGroup[this.length];

      for(int var2 = 0; var2 < this.length; ++var2) {
         this.trackGroups[var2] = (TrackGroup)var1.readParcelable(TrackGroup.class.getClassLoader());
      }

   }

   public TrackGroupArray(TrackGroup... var1) {
      this.trackGroups = var1;
      this.length = var1.length;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && TrackGroupArray.class == var1.getClass()) {
         TrackGroupArray var3 = (TrackGroupArray)var1;
         if (this.length != var3.length || !Arrays.equals(this.trackGroups, var3.trackGroups)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public TrackGroup get(int var1) {
      return this.trackGroups[var1];
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = Arrays.hashCode(this.trackGroups);
      }

      return this.hashCode;
   }

   public int indexOf(TrackGroup var1) {
      for(int var2 = 0; var2 < this.length; ++var2) {
         if (this.trackGroups[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.length);

      for(var2 = 0; var2 < this.length; ++var2) {
         var1.writeParcelable(this.trackGroups[var2], 0);
      }

   }
}
