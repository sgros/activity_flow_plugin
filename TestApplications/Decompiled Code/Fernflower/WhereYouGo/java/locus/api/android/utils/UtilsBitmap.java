package locus.api.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import java.io.IOException;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class UtilsBitmap {
   private static final String TAG = "UtilsBitmap";

   public static Bitmap getBitmap(byte[] var0) {
      Bitmap var1;
      Bitmap var3;
      try {
         var1 = BitmapFactory.decodeByteArray(var0, 0, var0.length);
      } catch (Exception var2) {
         Logger.logE("UtilsBitmap", "getBitmap(" + var0 + ")", var2);
         var3 = null;
         return var3;
      }

      var3 = var1;
      return var3;
   }

   public static byte[] getBitmap(Bitmap param0, CompressFormat param1) {
      // $FF: Couldn't be decompiled
   }

   public static Bitmap readBitmap(DataReaderBigEndian var0) {
      int var1 = var0.readInt();
      Bitmap var2;
      if (var1 > 0) {
         var2 = getBitmap(var0.readBytes(var1));
      } else {
         var2 = null;
      }

      return var2;
   }

   public static void writeBitmap(DataWriterBigEndian var0, Bitmap var1, CompressFormat var2) throws IOException {
      if (var1 == null) {
         var0.writeInt(0);
      } else {
         byte[] var3 = getBitmap(var1, var2);
         if (var3 != null && var3.length != 0) {
            var0.writeInt(var3.length);
            var0.write(var3);
         } else {
            Logger.logW("UtilsBitmap", "writeBitmap(), unknown problem");
            var0.writeInt(0);
         }
      }

   }
}
