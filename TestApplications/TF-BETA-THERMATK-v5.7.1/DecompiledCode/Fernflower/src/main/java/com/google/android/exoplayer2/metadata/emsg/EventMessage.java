package com.google.android.exoplayer2.metadata.emsg;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class EventMessage implements Metadata.Entry {
   public static final Creator CREATOR = new Creator() {
      public EventMessage createFromParcel(Parcel var1) {
         return new EventMessage(var1);
      }

      public EventMessage[] newArray(int var1) {
         return new EventMessage[var1];
      }
   };
   public final long durationMs;
   private int hashCode;
   public final long id;
   public final byte[] messageData;
   public final long presentationTimeUs;
   public final String schemeIdUri;
   public final String value;

   EventMessage(Parcel var1) {
      String var2 = var1.readString();
      Util.castNonNull(var2);
      this.schemeIdUri = (String)var2;
      var2 = var1.readString();
      Util.castNonNull(var2);
      this.value = (String)var2;
      this.presentationTimeUs = var1.readLong();
      this.durationMs = var1.readLong();
      this.id = var1.readLong();
      byte[] var3 = var1.createByteArray();
      Util.castNonNull(var3);
      this.messageData = (byte[])var3;
   }

   public EventMessage(String var1, String var2, long var3, long var5, byte[] var7, long var8) {
      this.schemeIdUri = var1;
      this.value = var2;
      this.durationMs = var3;
      this.id = var5;
      this.messageData = var7;
      this.presentationTimeUs = var8;
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && EventMessage.class == var1.getClass()) {
         EventMessage var3 = (EventMessage)var1;
         if (this.presentationTimeUs != var3.presentationTimeUs || this.durationMs != var3.durationMs || this.id != var3.id || !Util.areEqual(this.schemeIdUri, var3.schemeIdUri) || !Util.areEqual(this.value, var3.value) || !Arrays.equals(this.messageData, var3.messageData)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         String var1 = this.schemeIdUri;
         int var2 = 0;
         int var3;
         if (var1 != null) {
            var3 = var1.hashCode();
         } else {
            var3 = 0;
         }

         var1 = this.value;
         if (var1 != null) {
            var2 = var1.hashCode();
         }

         long var4 = this.presentationTimeUs;
         int var6 = (int)(var4 ^ var4 >>> 32);
         var4 = this.durationMs;
         int var7 = (int)(var4 ^ var4 >>> 32);
         var4 = this.id;
         this.hashCode = (((((527 + var3) * 31 + var2) * 31 + var6) * 31 + var7) * 31 + (int)(var4 ^ var4 >>> 32)) * 31 + Arrays.hashCode(this.messageData);
      }

      return this.hashCode;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("EMSG: scheme=");
      var1.append(this.schemeIdUri);
      var1.append(", id=");
      var1.append(this.id);
      var1.append(", value=");
      var1.append(this.value);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.schemeIdUri);
      var1.writeString(this.value);
      var1.writeLong(this.presentationTimeUs);
      var1.writeLong(this.durationMs);
      var1.writeLong(this.id);
      var1.writeByteArray(this.messageData);
   }
}
