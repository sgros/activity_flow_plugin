package org.mozilla.rocket.tabs.web;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Download implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public Download createFromParcel(Parcel var1) {
         String var2 = var1.readString();
         String var3 = var1.readString();
         String var4 = var1.readString();
         String var5 = var1.readString();
         String var6 = var1.readString();
         long var7 = var1.readLong();
         boolean var9;
         if (var1.readByte() != 0) {
            var9 = true;
         } else {
            var9 = false;
         }

         return new Download(var2, var3, var4, var5, var6, var7, var9);
      }

      public Download[] newArray(int var1) {
         return new Download[var1];
      }
   };
   private final String contentDisposition;
   private final long contentLength;
   private final String mimeType;
   private final String name;
   private final boolean startFromContextMenu;
   private final String url;
   private final String userAgent;

   public Download(String var1, String var2, String var3, String var4, String var5, long var6, boolean var8) {
      this.url = var1;
      this.name = var2;
      this.userAgent = var3;
      this.contentDisposition = var4;
      this.mimeType = var5;
      this.contentLength = var6;
      this.startFromContextMenu = var8;
   }

   public int describeContents() {
      return 0;
   }

   public String getContentDisposition() {
      return this.contentDisposition;
   }

   public long getContentLength() {
      return this.contentLength;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getName() {
      return this.name;
   }

   public String getUrl() {
      return this.url;
   }

   public String getUserAgent() {
      return this.userAgent;
   }

   public boolean isStartFromContextMenu() {
      return this.startFromContextMenu;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.url);
      var1.writeString(this.name);
      var1.writeString(this.userAgent);
      var1.writeString(this.contentDisposition);
      var1.writeString(this.mimeType);
      var1.writeLong(this.contentLength);
      var1.writeByte((byte)(this.startFromContextMenu ^ 1));
   }
}
