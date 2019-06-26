package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;

public final class TextInformationFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public TextInformationFrame createFromParcel(Parcel var1) {
         return new TextInformationFrame(var1);
      }

      public TextInformationFrame[] newArray(int var1) {
         return new TextInformationFrame[var1];
      }
   };
   public final String description;
   public final String value;

   TextInformationFrame(Parcel var1) {
      String var2 = var1.readString();
      Util.castNonNull(var2);
      super((String)var2);
      this.description = var1.readString();
      String var3 = var1.readString();
      Util.castNonNull(var3);
      this.value = (String)var3;
   }

   public TextInformationFrame(String var1, String var2, String var3) {
      super(var1);
      this.description = var2;
      this.value = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && TextInformationFrame.class == var1.getClass()) {
         TextInformationFrame var3 = (TextInformationFrame)var1;
         if (!super.id.equals(var3.id) || !Util.areEqual(this.description, var3.description) || !Util.areEqual(this.value, var3.value)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = super.id.hashCode();
      String var2 = this.description;
      int var3 = 0;
      int var4;
      if (var2 != null) {
         var4 = var2.hashCode();
      } else {
         var4 = 0;
      }

      var2 = this.value;
      if (var2 != null) {
         var3 = var2.hashCode();
      }

      return ((527 + var1) * 31 + var4) * 31 + var3;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.id);
      var1.append(": value=");
      var1.append(this.value);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(super.id);
      var1.writeString(this.description);
      var1.writeString(this.value);
   }
}
