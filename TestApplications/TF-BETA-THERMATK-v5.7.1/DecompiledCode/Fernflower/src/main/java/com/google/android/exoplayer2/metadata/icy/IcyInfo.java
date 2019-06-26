package com.google.android.exoplayer2.metadata.icy;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Util;

public final class IcyInfo implements Metadata.Entry {
   public static final Creator CREATOR = new Creator() {
      public IcyInfo createFromParcel(Parcel var1) {
         return new IcyInfo(var1);
      }

      public IcyInfo[] newArray(int var1) {
         return new IcyInfo[var1];
      }
   };
   public final String title;
   public final String url;

   IcyInfo(Parcel var1) {
      this.title = var1.readString();
      this.url = var1.readString();
   }

   public IcyInfo(String var1, String var2) {
      this.title = var1;
      this.url = var2;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && IcyInfo.class == var1.getClass()) {
         IcyInfo var3 = (IcyInfo)var1;
         if (!Util.areEqual(this.title, var3.title) || !Util.areEqual(this.url, var3.url)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      String var1 = this.title;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.url;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return (527 + var3) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ICY: title=\"");
      var1.append(this.title);
      var1.append("\", url=\"");
      var1.append(this.url);
      var1.append("\"");
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.title);
      var1.writeString(this.url);
   }
}
