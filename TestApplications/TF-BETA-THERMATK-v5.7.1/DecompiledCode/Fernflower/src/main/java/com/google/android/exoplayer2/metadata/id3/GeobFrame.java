package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class GeobFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public GeobFrame createFromParcel(Parcel var1) {
         return new GeobFrame(var1);
      }

      public GeobFrame[] newArray(int var1) {
         return new GeobFrame[var1];
      }
   };
   public final byte[] data;
   public final String description;
   public final String filename;
   public final String mimeType;

   GeobFrame(Parcel var1) {
      super("GEOB");
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.mimeType = (String)var2;
      var2 = var1.readString();
      Util.castNonNull(var2);
      this.filename = (String)var2;
      var2 = var1.readString();
      Util.castNonNull(var2);
      this.description = (String)var2;
      byte[] var3 = var1.createByteArray();
      Util.castNonNull(var3);
      this.data = (byte[])var3;
   }

   public GeobFrame(String var1, String var2, String var3, byte[] var4) {
      super("GEOB");
      this.mimeType = var1;
      this.filename = var2;
      this.description = var3;
      this.data = var4;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && GeobFrame.class == var1.getClass()) {
         GeobFrame var3 = (GeobFrame)var1;
         if (!Util.areEqual(this.mimeType, var3.mimeType) || !Util.areEqual(this.filename, var3.filename) || !Util.areEqual(this.description, var3.description) || !Arrays.equals(this.data, var3.data)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.mimeType;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.filename;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.description;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return (((527 + var3) * 31 + var4) * 31 + var2) * 31 + Arrays.hashCode(this.data);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.id);
      var1.append(": mimeType=");
      var1.append(this.mimeType);
      var1.append(", filename=");
      var1.append(this.filename);
      var1.append(", description=");
      var1.append(this.description);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.mimeType);
      var1.writeString(this.filename);
      var1.writeString(this.description);
      var1.writeByteArray(this.data);
   }
}
