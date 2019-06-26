package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class PrivFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public PrivFrame createFromParcel(Parcel var1) {
         return new PrivFrame(var1);
      }

      public PrivFrame[] newArray(int var1) {
         return new PrivFrame[var1];
      }
   };
   public final String owner;
   public final byte[] privateData;

   PrivFrame(Parcel var1) {
      super("PRIV");
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.owner = (String)var2;
      byte[] var3 = var1.createByteArray();
      Util.castNonNull(var3);
      this.privateData = (byte[])var3;
   }

   public PrivFrame(String var1, byte[] var2) {
      super("PRIV");
      this.owner = var1;
      this.privateData = var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && PrivFrame.class == var1.getClass()) {
         PrivFrame var3 = (PrivFrame)var1;
         if (!Util.areEqual(this.owner, var3.owner) || !Arrays.equals(this.privateData, var3.privateData)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.owner;
      int var2;
      if (var1 != null) {
         var2 = var1.hashCode();
      } else {
         var2 = 0;
      }

      return (527 + var2) * 31 + Arrays.hashCode(this.privateData);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.id);
      var1.append(": owner=");
      var1.append(this.owner);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.owner);
      var1.writeByteArray(this.privateData);
   }
}
