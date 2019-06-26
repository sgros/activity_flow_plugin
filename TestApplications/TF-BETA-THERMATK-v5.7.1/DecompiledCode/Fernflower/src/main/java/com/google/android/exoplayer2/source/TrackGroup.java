package com.google.android.exoplayer2.source;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

public final class TrackGroup implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public TrackGroup createFromParcel(Parcel var1) {
         return new TrackGroup(var1);
      }

      public TrackGroup[] newArray(int var1) {
         return new TrackGroup[var1];
      }
   };
   private final Format[] formats;
   private int hashCode;
   public final int length;

   TrackGroup(Parcel var1) {
      this.length = var1.readInt();
      this.formats = new Format[this.length];

      for(int var2 = 0; var2 < this.length; ++var2) {
         this.formats[var2] = (Format)var1.readParcelable(Format.class.getClassLoader());
      }

   }

   public TrackGroup(Format... var1) {
      boolean var2;
      if (var1.length > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.formats = var1;
      this.length = var1.length;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && TrackGroup.class == var1.getClass()) {
         TrackGroup var3 = (TrackGroup)var1;
         if (this.length != var3.length || !Arrays.equals(this.formats, var3.formats)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public Format getFormat(int var1) {
      return this.formats[var1];
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = 527 + Arrays.hashCode(this.formats);
      }

      return this.hashCode;
   }

   public int indexOf(Format var1) {
      int var2 = 0;

      while(true) {
         Format[] var3 = this.formats;
         if (var2 >= var3.length) {
            return -1;
         }

         if (var1 == var3[var2]) {
            return var2;
         }

         ++var2;
      }
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.length);

      for(var2 = 0; var2 < this.length; ++var2) {
         var1.writeParcelable(this.formats[var2], 0);
      }

   }
}
