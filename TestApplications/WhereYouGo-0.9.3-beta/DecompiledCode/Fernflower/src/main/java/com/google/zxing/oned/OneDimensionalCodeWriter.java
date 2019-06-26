package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Map;

public abstract class OneDimensionalCodeWriter implements Writer {
   protected static int appendPattern(boolean[] var0, int var1, int[] var2, boolean var3) {
      int var4 = 0;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         int var7 = var2[var6];

         for(int var8 = 0; var8 < var7; ++var1) {
            var0[var1] = var3;
            ++var8;
         }

         var4 += var7;
         if (!var3) {
            var3 = true;
         } else {
            var3 = false;
         }
      }

      return var4;
   }

   private static BitMatrix renderResult(boolean[] var0, int var1, int var2, int var3) {
      int var4 = var0.length;
      int var5 = var4 + var3;
      int var6 = Math.max(var1, var5);
      var3 = Math.max(1, var2);
      var5 = var6 / var5;
      var1 = (var6 - var4 * var5) / 2;
      BitMatrix var7 = new BitMatrix(var6, var3);

      for(var2 = 0; var2 < var4; var1 += var5) {
         if (var0[var2]) {
            var7.setRegion(var1, 0, var5, var3);
         }

         ++var2;
      }

      return var7;
   }

   public final BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4) throws WriterException {
      return this.encode(var1, var2, var3, var4, (Map)null);
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var1.isEmpty()) {
         throw new IllegalArgumentException("Found empty contents");
      } else if (var3 >= 0 && var4 >= 0) {
         int var6 = this.getDefaultMargin();
         int var7 = var6;
         if (var5 != null) {
            var7 = var6;
            if (var5.containsKey(EncodeHintType.MARGIN)) {
               var7 = Integer.parseInt(var5.get(EncodeHintType.MARGIN).toString());
            }
         }

         return renderResult(this.encode(var1), var3, var4, var7);
      } else {
         throw new IllegalArgumentException("Negative size is not allowed. Input: " + var3 + 'x' + var4);
      }
   }

   public abstract boolean[] encode(String var1);

   public int getDefaultMargin() {
      return 10;
   }
}
