package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import java.util.Map;

public final class ByQuadrantReader implements Reader {
   private final Reader delegate;

   public ByQuadrantReader(Reader var1) {
      this.delegate = var1;
   }

   private static void makeAbsolute(ResultPoint[] var0, int var1, int var2) {
      if (var0 != null) {
         for(int var3 = 0; var3 < var0.length; ++var3) {
            ResultPoint var4 = var0[var3];
            var0[var3] = new ResultPoint(var4.getX() + (float)var1, var4.getY() + (float)var2);
         }
      }

   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, ChecksumException, FormatException {
      return this.decode(var1, (Map)null);
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, ChecksumException, FormatException {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      var3 /= 2;
      var4 /= 2;

      Result var5;
      Result var12;
      try {
         var5 = this.delegate.decode(var1.crop(0, 0, var3, var4), var2);
      } catch (NotFoundException var11) {
         try {
            var5 = this.delegate.decode(var1.crop(var3, 0, var3, var4), var2);
            makeAbsolute(var5.getResultPoints(), var3, 0);
         } catch (NotFoundException var10) {
            try {
               var5 = this.delegate.decode(var1.crop(0, var4, var3, var4), var2);
               makeAbsolute(var5.getResultPoints(), 0, var4);
            } catch (NotFoundException var9) {
               try {
                  var5 = this.delegate.decode(var1.crop(var3, var4, var3, var4), var2);
                  makeAbsolute(var5.getResultPoints(), var3, var4);
               } catch (NotFoundException var8) {
                  int var6 = var3 / 2;
                  int var7 = var4 / 2;
                  var1 = var1.crop(var6, var7, var3, var4);
                  var12 = this.delegate.decode(var1, var2);
                  makeAbsolute(var12.getResultPoints(), var6, var7);
                  return var12;
               }

               var12 = var5;
               return var12;
            }

            var12 = var5;
            return var12;
         }

         var12 = var5;
         return var12;
      }

      var12 = var5;
      return var12;
   }

   public void reset() {
      this.delegate.reset();
   }
}
