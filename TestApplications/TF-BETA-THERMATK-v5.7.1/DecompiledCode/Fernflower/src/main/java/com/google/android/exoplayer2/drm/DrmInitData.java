package com.google.android.exoplayer2.drm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class DrmInitData implements Comparator, Parcelable {
   public static final Creator CREATOR = new Creator() {
      public DrmInitData createFromParcel(Parcel var1) {
         return new DrmInitData(var1);
      }

      public DrmInitData[] newArray(int var1) {
         return new DrmInitData[var1];
      }
   };
   private int hashCode;
   public final int schemeDataCount;
   private final DrmInitData.SchemeData[] schemeDatas;
   public final String schemeType;

   DrmInitData(Parcel var1) {
      this.schemeType = var1.readString();
      this.schemeDatas = (DrmInitData.SchemeData[])var1.createTypedArray(DrmInitData.SchemeData.CREATOR);
      this.schemeDataCount = this.schemeDatas.length;
   }

   public DrmInitData(String var1, List var2) {
      this(var1, false, (DrmInitData.SchemeData[])var2.toArray(new DrmInitData.SchemeData[0]));
   }

   private DrmInitData(String var1, boolean var2, DrmInitData.SchemeData... var3) {
      this.schemeType = var1;
      DrmInitData.SchemeData[] var4 = var3;
      if (var2) {
         var4 = (DrmInitData.SchemeData[])var3.clone();
      }

      this.schemeDatas = var4;
      this.schemeDataCount = var4.length;
      Arrays.sort(this.schemeDatas, this);
   }

   public DrmInitData(String var1, DrmInitData.SchemeData... var2) {
      this(var1, true, var2);
   }

   public DrmInitData(List var1) {
      this((String)null, false, (DrmInitData.SchemeData[])var1.toArray(new DrmInitData.SchemeData[0]));
   }

   public DrmInitData(DrmInitData.SchemeData... var1) {
      this((String)null, (DrmInitData.SchemeData[])var1);
   }

   private static boolean containsSchemeDataWithUuid(ArrayList var0, int var1, UUID var2) {
      for(int var3 = 0; var3 < var1; ++var3) {
         if (((DrmInitData.SchemeData)var0.get(var3)).uuid.equals(var2)) {
            return true;
         }
      }

      return false;
   }

   public static DrmInitData createSessionCreationData(DrmInitData var0, DrmInitData var1) {
      ArrayList var2 = new ArrayList();
      byte var3 = 0;
      Object var4 = null;
      String var5;
      int var7;
      int var8;
      String var10;
      if (var0 != null) {
         var5 = var0.schemeType;
         DrmInitData.SchemeData[] var6 = var0.schemeDatas;
         var7 = var6.length;
         var8 = 0;

         while(true) {
            var10 = var5;
            if (var8 >= var7) {
               break;
            }

            DrmInitData.SchemeData var11 = var6[var8];
            if (var11.hasData()) {
               var2.add(var11);
            }

            ++var8;
         }
      } else {
         var10 = null;
      }

      String var14 = var10;
      if (var1 != null) {
         var5 = var10;
         if (var10 == null) {
            var5 = var1.schemeType;
         }

         int var9 = var2.size();
         DrmInitData.SchemeData[] var13 = var1.schemeDatas;
         var7 = var13.length;
         var8 = var3;

         while(true) {
            var14 = var5;
            if (var8 >= var7) {
               break;
            }

            DrmInitData.SchemeData var12 = var13[var8];
            if (var12.hasData() && !containsSchemeDataWithUuid(var2, var9, var12.uuid)) {
               var2.add(var12);
            }

            ++var8;
         }
      }

      if (var2.isEmpty()) {
         var0 = (DrmInitData)var4;
      } else {
         var0 = new DrmInitData(var14, var2);
      }

      return var0;
   }

   public int compare(DrmInitData.SchemeData var1, DrmInitData.SchemeData var2) {
      int var3;
      if (C.UUID_NIL.equals(var1.uuid)) {
         if (C.UUID_NIL.equals(var2.uuid)) {
            var3 = 0;
         } else {
            var3 = 1;
         }
      } else {
         var3 = var1.uuid.compareTo(var2.uuid);
      }

      return var3;
   }

   public DrmInitData copyWithSchemeType(String var1) {
      return Util.areEqual(this.schemeType, var1) ? this : new DrmInitData(var1, false, this.schemeDatas);
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && DrmInitData.class == var1.getClass()) {
         DrmInitData var3 = (DrmInitData)var1;
         if (!Util.areEqual(this.schemeType, var3.schemeType) || !Arrays.equals(this.schemeDatas, var3.schemeDatas)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public DrmInitData.SchemeData get(int var1) {
      return this.schemeDatas[var1];
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         String var1 = this.schemeType;
         int var2;
         if (var1 == null) {
            var2 = 0;
         } else {
            var2 = var1.hashCode();
         }

         this.hashCode = var2 * 31 + Arrays.hashCode(this.schemeDatas);
      }

      return this.hashCode;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.schemeType);
      var1.writeTypedArray(this.schemeDatas, 0);
   }

   public static final class SchemeData implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public DrmInitData.SchemeData createFromParcel(Parcel var1) {
            return new DrmInitData.SchemeData(var1);
         }

         public DrmInitData.SchemeData[] newArray(int var1) {
            return new DrmInitData.SchemeData[var1];
         }
      };
      public final byte[] data;
      private int hashCode;
      public final String licenseServerUrl;
      public final String mimeType;
      public final boolean requiresSecureDecryption;
      private final UUID uuid;

      SchemeData(Parcel var1) {
         this.uuid = new UUID(var1.readLong(), var1.readLong());
         this.licenseServerUrl = var1.readString();
         String var2 = var1.readString();
         Util.castNonNull(var2);
         this.mimeType = (String)var2;
         this.data = var1.createByteArray();
         boolean var3;
         if (var1.readByte() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.requiresSecureDecryption = var3;
      }

      public SchemeData(UUID var1, String var2, String var3, byte[] var4, boolean var5) {
         Assertions.checkNotNull(var1);
         this.uuid = (UUID)var1;
         this.licenseServerUrl = var2;
         Assertions.checkNotNull(var3);
         this.mimeType = (String)var3;
         this.data = var4;
         this.requiresSecureDecryption = var5;
      }

      public SchemeData(UUID var1, String var2, byte[] var3) {
         this(var1, var2, var3, false);
      }

      public SchemeData(UUID var1, String var2, byte[] var3, boolean var4) {
         this(var1, (String)null, var2, var3, var4);
      }

      public boolean canReplace(DrmInitData.SchemeData var1) {
         boolean var2;
         if (this.hasData() && !var1.hasData() && this.matches(var1.uuid)) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public DrmInitData.SchemeData copyWithData(byte[] var1) {
         return new DrmInitData.SchemeData(this.uuid, this.licenseServerUrl, this.mimeType, var1, this.requiresSecureDecryption);
      }

      public int describeContents() {
         return 0;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof DrmInitData.SchemeData)) {
            return false;
         } else {
            boolean var2 = true;
            if (var1 == this) {
               return true;
            } else {
               DrmInitData.SchemeData var3 = (DrmInitData.SchemeData)var1;
               if (!Util.areEqual(this.licenseServerUrl, var3.licenseServerUrl) || !Util.areEqual(this.mimeType, var3.mimeType) || !Util.areEqual(this.uuid, var3.uuid) || !Arrays.equals(this.data, var3.data)) {
                  var2 = false;
               }

               return var2;
            }
         }
      }

      public boolean hasData() {
         boolean var1;
         if (this.data != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public int hashCode() {
         if (this.hashCode == 0) {
            int var1 = this.uuid.hashCode();
            String var2 = this.licenseServerUrl;
            int var3;
            if (var2 == null) {
               var3 = 0;
            } else {
               var3 = var2.hashCode();
            }

            this.hashCode = ((var1 * 31 + var3) * 31 + this.mimeType.hashCode()) * 31 + Arrays.hashCode(this.data);
         }

         return this.hashCode;
      }

      public boolean matches(UUID var1) {
         boolean var2;
         if (!C.UUID_NIL.equals(this.uuid) && !var1.equals(this.uuid)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeLong(this.uuid.getMostSignificantBits());
         var1.writeLong(this.uuid.getLeastSignificantBits());
         var1.writeString(this.licenseServerUrl);
         var1.writeString(this.mimeType);
         var1.writeByteArray(this.data);
         var1.writeByte((byte)this.requiresSecureDecryption);
      }
   }
}
