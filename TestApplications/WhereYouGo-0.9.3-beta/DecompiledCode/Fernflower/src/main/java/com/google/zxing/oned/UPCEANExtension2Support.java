package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.EnumMap;
import java.util.Map;

final class UPCEANExtension2Support {
   private final int[] decodeMiddleCounters = new int[4];
   private final StringBuilder decodeRowStringBuffer = new StringBuilder();

   private int decodeMiddle(BitArray var1, int[] var2, StringBuilder var3) throws NotFoundException {
      int[] var4 = this.decodeMiddleCounters;
      var4[0] = 0;
      var4[1] = 0;
      var4[2] = 0;
      var4[3] = 0;
      int var5 = var1.getSize();
      int var6 = var2[1];
      int var7 = 0;

      int var11;
      for(int var8 = 0; var8 < 2 && var6 < var5; var6 = var11) {
         int var9 = UPCEANReader.decodeDigit(var1, var4, var6, UPCEANReader.L_AND_G_PATTERNS);
         var3.append((char)(var9 % 10 + 48));
         int var10 = var4.length;

         for(var11 = 0; var11 < var10; ++var11) {
            var6 += var4[var11];
         }

         var10 = var7;
         if (var9 >= 10) {
            var10 = var7 | 1 << 1 - var8;
         }

         var11 = var6;
         if (var8 != 1) {
            var11 = var1.getNextUnset(var1.getNextSet(var6));
         }

         ++var8;
         var7 = var10;
      }

      if (var3.length() != 2) {
         throw NotFoundException.getNotFoundInstance();
      } else if (Integer.parseInt(var3.toString()) % 4 != var7) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         return var6;
      }
   }

   private static Map parseExtensionString(String var0) {
      EnumMap var2;
      if (var0.length() != 2) {
         var2 = null;
      } else {
         EnumMap var1 = new EnumMap(ResultMetadataType.class);
         var1.put(ResultMetadataType.ISSUE_NUMBER, Integer.valueOf(var0));
         var2 = var1;
      }

      return var2;
   }

   Result decodeRow(int var1, BitArray var2, int[] var3) throws NotFoundException {
      StringBuilder var4 = this.decodeRowStringBuffer;
      var4.setLength(0);
      int var5 = this.decodeMiddle(var2, var3, var4);
      String var11 = var4.toString();
      Map var8 = parseExtensionString(var11);
      ResultPoint var6 = new ResultPoint((float)(var3[0] + var3[1]) / 2.0F, (float)var1);
      ResultPoint var9 = new ResultPoint((float)var5, (float)var1);
      BarcodeFormat var7 = BarcodeFormat.UPC_EAN_EXTENSION;
      Result var10 = new Result(var11, (byte[])null, new ResultPoint[]{var6, var9}, var7);
      if (var8 != null) {
         var10.putAllMetadata(var8);
      }

      return var10;
   }
}
