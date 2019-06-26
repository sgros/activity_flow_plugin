package com.google.android.exoplayer2.extractor.mp4;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class MdtaMetadataEntry implements Metadata.Entry {
   public static final Creator CREATOR = new Creator() {
      public MdtaMetadataEntry createFromParcel(Parcel var1) {
         return new MdtaMetadataEntry(var1);
      }

      public MdtaMetadataEntry[] newArray(int var1) {
         return new MdtaMetadataEntry[var1];
      }
   };
   public final String key;
   public final int localeIndicator;
   public final int typeIndicator;
   public final byte[] value;

   private MdtaMetadataEntry(Parcel var1) {
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.key = (String)var2;
      this.value = new byte[var1.readInt()];
      var1.readByteArray(this.value);
      this.localeIndicator = var1.readInt();
      this.typeIndicator = var1.readInt();
   }

   // $FF: synthetic method
   MdtaMetadataEntry(Parcel var1, Object var2) {
      this(var1);
   }

   public MdtaMetadataEntry(String var1, byte[] var2, int var3, int var4) {
      this.key = var1;
      this.value = var2;
      this.localeIndicator = var3;
      this.typeIndicator = var4;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && MdtaMetadataEntry.class == var1.getClass()) {
         MdtaMetadataEntry var3 = (MdtaMetadataEntry)var1;
         if (!this.key.equals(var3.key) || !Arrays.equals(this.value, var3.value) || this.localeIndicator != var3.localeIndicator || this.typeIndicator != var3.typeIndicator) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (((527 + this.key.hashCode()) * 31 + Arrays.hashCode(this.value)) * 31 + this.localeIndicator) * 31 + this.typeIndicator;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("mdta: key=");
      var1.append(this.key);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.key);
      var1.writeInt(this.value.length);
      var1.writeByteArray(this.value);
      var1.writeInt(this.localeIndicator);
      var1.writeInt(this.typeIndicator);
   }
}
