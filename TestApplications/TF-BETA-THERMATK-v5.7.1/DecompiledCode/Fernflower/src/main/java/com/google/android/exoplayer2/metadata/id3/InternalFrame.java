package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.util.Util;

public final class InternalFrame extends Id3Frame {
   public static final Creator CREATOR = new Creator() {
      public InternalFrame createFromParcel(Parcel var1) {
         return new InternalFrame(var1);
      }

      public InternalFrame[] newArray(int var1) {
         return new InternalFrame[var1];
      }
   };
   public final String description;
   public final String domain;
   public final String text;

   InternalFrame(Parcel var1) {
      super("----");
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.domain = (String)var2;
      var2 = var1.readString();
      Util.castNonNull(var2);
      this.description = (String)var2;
      String var3 = var1.readString();
      Util.castNonNull(var3);
      this.text = (String)var3;
   }

   public InternalFrame(String var1, String var2, String var3) {
      super("----");
      this.domain = var1;
      this.description = var2;
      this.text = var3;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && InternalFrame.class == var1.getClass()) {
         InternalFrame var3 = (InternalFrame)var1;
         if (!Util.areEqual(this.description, var3.description) || !Util.areEqual(this.domain, var3.domain) || !Util.areEqual(this.text, var3.text)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.domain;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.description;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.text;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return ((527 + var3) * 31 + var4) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.id);
      var1.append(": domain=");
      var1.append(this.domain);
      var1.append(", description=");
      var1.append(this.description);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(super.id);
      var1.writeString(this.domain);
      var1.writeString(this.text);
   }
}
