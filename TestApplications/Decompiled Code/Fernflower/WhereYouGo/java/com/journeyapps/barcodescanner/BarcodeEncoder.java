package com.journeyapps.barcodescanner;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public class BarcodeEncoder {
   private static final int BLACK = -16777216;
   private static final int WHITE = -1;

   public Bitmap createBitmap(BitMatrix var1) {
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      int[] var4 = new int[var2 * var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         for(int var6 = 0; var6 < var2; ++var6) {
            int var7;
            if (var1.get(var6, var5)) {
               var7 = -16777216;
            } else {
               var7 = -1;
            }

            var4[var5 * var2 + var6] = var7;
         }
      }

      Bitmap var8 = Bitmap.createBitmap(var2, var3, Config.ARGB_8888);
      var8.setPixels(var4, 0, var2, 0, 0, var2, var3);
      return var8;
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      try {
         MultiFormatWriter var5 = new MultiFormatWriter();
         BitMatrix var8 = var5.encode(var1, var2, var3, var4);
         return var8;
      } catch (WriterException var6) {
         throw var6;
      } catch (Exception var7) {
         throw new WriterException(var7);
      }
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      try {
         MultiFormatWriter var6 = new MultiFormatWriter();
         BitMatrix var9 = var6.encode(var1, var2, var3, var4, var5);
         return var9;
      } catch (WriterException var7) {
         throw var7;
      } catch (Exception var8) {
         throw new WriterException(var8);
      }
   }

   public Bitmap encodeBitmap(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.createBitmap(this.encode(var1, var2, var3, var4));
   }

   public Bitmap encodeBitmap(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      return this.createBitmap(this.encode(var1, var2, var3, var4, var5));
   }
}
