package com.google.zxing.multi;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class GenericMultipleBarcodeReader implements MultipleBarcodeReader {
   private static final int MAX_DEPTH = 4;
   private static final int MIN_DIMENSION_TO_RECUR = 100;
   private final Reader delegate;

   public GenericMultipleBarcodeReader(Reader var1) {
      this.delegate = var1;
   }

   private void doDecodeMultiple(BinaryBitmap var1, Map var2, List var3, int var4, int var5, int var6) {
      if (var6 <= 4) {
         Result var7;
         try {
            var7 = this.delegate.decode(var1, var2);
         } catch (ReaderException var23) {
            return;
         }

         boolean var8 = false;
         Iterator var9 = var3.iterator();

         boolean var10;
         while(true) {
            var10 = var8;
            if (!var9.hasNext()) {
               break;
            }

            if (((Result)var9.next()).getText().equals(var7.getText())) {
               var10 = true;
               break;
            }
         }

         if (!var10) {
            var3.add(translateResultPoints(var7, var4, var5));
         }

         ResultPoint[] var24 = var7.getResultPoints();
         if (var24 != null && var24.length != 0) {
            int var11 = var1.getWidth();
            int var12 = var1.getHeight();
            float var13 = (float)var11;
            float var14 = (float)var12;
            float var15 = 0.0F;
            float var16 = 0.0F;
            int var25 = var24.length;

            float var20;
            for(int var27 = 0; var27 < var25; var14 = var20) {
               ResultPoint var26 = var24[var27];
               float var17 = var15;
               float var18 = var16;
               float var19 = var13;
               var20 = var14;
               if (var26 != null) {
                  var19 = var26.getX();
                  float var21 = var26.getY();
                  float var22 = var13;
                  if (var19 < var13) {
                     var22 = var19;
                  }

                  var13 = var14;
                  if (var21 < var14) {
                     var13 = var21;
                  }

                  var14 = var15;
                  if (var19 > var15) {
                     var14 = var19;
                  }

                  var17 = var14;
                  var18 = var16;
                  var19 = var22;
                  var20 = var13;
                  if (var21 > var16) {
                     var20 = var13;
                     var19 = var22;
                     var18 = var21;
                     var17 = var14;
                  }
               }

               ++var27;
               var15 = var17;
               var16 = var18;
               var13 = var19;
            }

            if (var13 > 100.0F) {
               this.doDecodeMultiple(var1.crop(0, 0, (int)var13, var12), var2, var3, var4, var5, var6 + 1);
            }

            if (var14 > 100.0F) {
               this.doDecodeMultiple(var1.crop(0, 0, var11, (int)var14), var2, var3, var4, var5, var6 + 1);
            }

            if (var15 < (float)(var11 - 100)) {
               this.doDecodeMultiple(var1.crop((int)var15, 0, var11 - (int)var15, var12), var2, var3, var4 + (int)var15, var5, var6 + 1);
            }

            if (var16 < (float)(var12 - 100)) {
               this.doDecodeMultiple(var1.crop(0, (int)var16, var11, var12 - (int)var16), var2, var3, var4, var5 + (int)var16, var6 + 1);
            }
         }
      }

   }

   private static Result translateResultPoints(Result var0, int var1, int var2) {
      ResultPoint[] var3 = var0.getResultPoints();
      if (var3 != null) {
         ResultPoint[] var4 = new ResultPoint[var3.length];

         for(int var5 = 0; var5 < var3.length; ++var5) {
            ResultPoint var6 = var3[var5];
            if (var6 != null) {
               var4[var5] = new ResultPoint(var6.getX() + (float)var1, var6.getY() + (float)var2);
            }
         }

         Result var7 = new Result(var0.getText(), var0.getRawBytes(), var0.getNumBits(), var4, var0.getBarcodeFormat(), var0.getTimestamp());
         var7.putAllMetadata(var0.getResultMetadata());
         var0 = var7;
      }

      return var0;
   }

   public Result[] decodeMultiple(BinaryBitmap var1) throws NotFoundException {
      return this.decodeMultiple(var1, (Map)null);
   }

   public Result[] decodeMultiple(BinaryBitmap var1, Map var2) throws NotFoundException {
      ArrayList var3 = new ArrayList();
      this.doDecodeMultiple(var1, var2, var3, 0, 0, 0);
      if (var3.isEmpty()) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return (Result[])var3.toArray(new Result[var3.size()]);
      }
   }
}
