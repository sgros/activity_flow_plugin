package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class ApicFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public ApicFrame createFromParcel(Parcel var1) {
         return new ApicFrame(var1);
      }

      public ApicFrame[] newArray(int var1) {
         return new ApicFrame[var1];
      }
   };
   public final String description;
   public final String mimeType;
   public final byte[] pictureData;
   public final int pictureType;

   ApicFrame(Parcel var1) {
      super("APIC");
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.mimeType = (String)var2;
      var2 = var1.readString();
      Util.castNonNull(var2);
      this.description = (String)var2;
      this.pictureType = var1.readInt();
      byte[] var3 = var1.createByteArray();
      Util.castNonNull(var3);
      this.pictureData = (byte[])var3;
   }

   public ApicFrame(String var1, String var2, int var3, byte[] var4) {
      super("APIC");
      this.mimeType = var1;
      this.description = var2;
      this.pictureType = var3;
      this.pictureData = var4;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && ApicFrame.class == var1.getClass()) {
         ApicFrame var3 = (ApicFrame)var1;
         if (this.pictureType != var3.pictureType || !Util.areEqual(this.mimeType, var3.mimeType) || !Util.areEqual(this.description, var3.description) || !Arrays.equals(this.pictureData, var3.pictureData)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = this.pictureType;
      String var2 = this.mimeType;
      int var3 = 0;
      int var4;
      if (var2 != null) {
         var4 = var2.hashCode();
      } else {
         var4 = 0;
      }

      var2 = this.description;
      if (var2 != null) {
         var3 = var2.hashCode();
      }

      return (((527 + var1) * 31 + var4) * 31 + var3) * 31 + Arrays.hashCode(this.pictureData);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.id);
      var1.append(": mimeType=");
      var1.append(this.mimeType);
      var1.append(", description=");
      var1.append(this.description);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.mimeType);
      var1.writeString(this.description);
      var1.writeInt(this.pictureType);
      var1.writeByteArray(this.pictureData);
   }
}
