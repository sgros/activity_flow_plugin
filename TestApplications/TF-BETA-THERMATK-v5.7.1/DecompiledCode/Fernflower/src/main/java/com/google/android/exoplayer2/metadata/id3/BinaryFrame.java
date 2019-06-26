package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class BinaryFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public BinaryFrame createFromParcel(Parcel var1) {
         return new BinaryFrame(var1);
      }

      public BinaryFrame[] newArray(int var1) {
         return new BinaryFrame[var1];
      }
   };
   public final byte[] data;

   BinaryFrame(Parcel var1) {
      String var2 = var1.readString();
      Util.castNonNull(var2);
      super((String)var2);
      byte[] var3 = var1.createByteArray();
      Util.castNonNull(var3);
      this.data = (byte[])var3;
   }

   public BinaryFrame(String var1, byte[] var2) {
      super(var1);
      this.data = var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && BinaryFrame.class == var1.getClass()) {
         BinaryFrame var3 = (BinaryFrame)var1;
         if (!super.id.equals(var3.id) || !Arrays.equals(this.data, var3.data)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (527 + super.id.hashCode()) * 31 + Arrays.hashCode(this.data);
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(super.id);
      var1.writeByteArray(this.data);
   }
}
