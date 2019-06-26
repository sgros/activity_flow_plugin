package com.google.android.exoplayer2.metadata;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.List;

public final class Metadata implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public Metadata createFromParcel(Parcel var1) {
         return new Metadata(var1);
      }

      public Metadata[] newArray(int var1) {
         return new Metadata[0];
      }
   };
   private final Metadata.Entry[] entries;

   Metadata(Parcel var1) {
      this.entries = new Metadata.Entry[var1.readInt()];
      int var2 = 0;

      while(true) {
         Metadata.Entry[] var3 = this.entries;
         if (var2 >= var3.length) {
            return;
         }

         var3[var2] = (Metadata.Entry)var1.readParcelable(Metadata.Entry.class.getClassLoader());
         ++var2;
      }
   }

   public Metadata(List var1) {
      if (var1 != null) {
         this.entries = new Metadata.Entry[var1.size()];
         var1.toArray(this.entries);
      } else {
         this.entries = new Metadata.Entry[0];
      }

   }

   public Metadata(Metadata.Entry... var1) {
      Metadata.Entry[] var2 = var1;
      if (var1 == null) {
         var2 = new Metadata.Entry[0];
      }

      this.entries = var2;
   }

   public Metadata copyWithAppendedEntries(Metadata.Entry... var1) {
      Metadata.Entry[] var2 = this.entries;
      var2 = (Metadata.Entry[])Arrays.copyOf(var2, var2.length + var1.length);
      System.arraycopy(var1, 0, var2, this.entries.length, var1.length);
      Util.castNonNullTypeArray(var2);
      return new Metadata((Metadata.Entry[])var2);
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && Metadata.class == var1.getClass()) {
         Metadata var2 = (Metadata)var1;
         return Arrays.equals(this.entries, var2.entries);
      } else {
         return false;
      }
   }

   public Metadata.Entry get(int var1) {
      return this.entries[var1];
   }

   public int hashCode() {
      return Arrays.hashCode(this.entries);
   }

   public int length() {
      return this.entries.length;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.entries.length);
      Metadata.Entry[] var3 = this.entries;
      int var4 = var3.length;

      for(var2 = 0; var2 < var4; ++var2) {
         var1.writeParcelable(var3[var2], 0);
      }

   }

   public interface Entry extends Parcelable {
   }
}
