package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.os.Parcelable;
import androidx.versionedparcelable.VersionedParcel;

public class IconCompatParcelizer {
   public static IconCompat read(VersionedParcel var0) {
      IconCompat var1 = new IconCompat();
      var1.mType = var0.readInt(var1.mType, 1);
      var1.mData = var0.readByteArray(var1.mData, 2);
      var1.mParcelable = var0.readParcelable(var1.mParcelable, 3);
      var1.mInt1 = var0.readInt(var1.mInt1, 4);
      var1.mInt2 = var0.readInt(var1.mInt2, 5);
      var1.mTintList = (ColorStateList)var0.readParcelable(var1.mTintList, 6);
      var1.mTintModeStr = var0.readString(var1.mTintModeStr, 7);
      var1.onPostParceling();
      return var1;
   }

   public static void write(IconCompat var0, VersionedParcel var1) {
      var1.setSerializationFlags(true, true);
      var0.onPreParceling(var1.isStream());
      int var2 = var0.mType;
      if (-1 != var2) {
         var1.writeInt(var2, 1);
      }

      byte[] var3 = var0.mData;
      if (var3 != null) {
         var1.writeByteArray(var3, 2);
      }

      Parcelable var5 = var0.mParcelable;
      if (var5 != null) {
         var1.writeParcelable(var5, 3);
      }

      var2 = var0.mInt1;
      if (var2 != 0) {
         var1.writeInt(var2, 4);
      }

      var2 = var0.mInt2;
      if (var2 != 0) {
         var1.writeInt(var2, 5);
      }

      ColorStateList var6 = var0.mTintList;
      if (var6 != null) {
         var1.writeParcelable(var6, 6);
      }

      String var4 = var0.mTintModeStr;
      if (var4 != null) {
         var1.writeString(var4, 7);
      }

   }
}
