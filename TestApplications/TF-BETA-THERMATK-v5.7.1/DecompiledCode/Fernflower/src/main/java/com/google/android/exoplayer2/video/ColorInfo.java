package com.google.android.exoplayer2.video;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class ColorInfo implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public ColorInfo createFromParcel(Parcel var1) {
         return new ColorInfo(var1);
      }

      public ColorInfo[] newArray(int var1) {
         return new ColorInfo[0];
      }
   };
   public final int colorRange;
   public final int colorSpace;
   public final int colorTransfer;
   private int hashCode;
   public final byte[] hdrStaticInfo;

   public ColorInfo(int var1, int var2, int var3, byte[] var4) {
      this.colorSpace = var1;
      this.colorRange = var2;
      this.colorTransfer = var3;
      this.hdrStaticInfo = var4;
   }

   ColorInfo(Parcel var1) {
      this.colorSpace = var1.readInt();
      this.colorRange = var1.readInt();
      this.colorTransfer = var1.readInt();
      byte[] var2;
      if (Util.readBoolean(var1)) {
         var2 = var1.createByteArray();
      } else {
         var2 = null;
      }

      this.hdrStaticInfo = var2;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && ColorInfo.class == var1.getClass()) {
         ColorInfo var3 = (ColorInfo)var1;
         if (this.colorSpace != var3.colorSpace || this.colorRange != var3.colorRange || this.colorTransfer != var3.colorTransfer || !Arrays.equals(this.hdrStaticInfo, var3.hdrStaticInfo)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = (((527 + this.colorSpace) * 31 + this.colorRange) * 31 + this.colorTransfer) * 31 + Arrays.hashCode(this.hdrStaticInfo);
      }

      return this.hashCode;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ColorInfo(");
      var1.append(this.colorSpace);
      var1.append(", ");
      var1.append(this.colorRange);
      var1.append(", ");
      var1.append(this.colorTransfer);
      var1.append(", ");
      boolean var2;
      if (this.hdrStaticInfo != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      var1.append(var2);
      var1.append(")");
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.colorSpace);
      var1.writeInt(this.colorRange);
      var1.writeInt(this.colorTransfer);
      boolean var3;
      if (this.hdrStaticInfo != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Util.writeBoolean(var1, var3);
      byte[] var4 = this.hdrStaticInfo;
      if (var4 != null) {
         var1.writeByteArray(var4);
      }

   }
}
