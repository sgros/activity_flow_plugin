package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.EnumMap;
import java.util.Map;

final class UPCEANExtension5Support {
   private static final int[] CHECK_DIGIT_ENCODINGS = new int[]{24, 20, 18, 17, 12, 6, 3, 10, 9, 5};
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
      for(int var8 = 0; var8 < 5 && var6 < var5; var6 = var11) {
         int var9 = UPCEANReader.decodeDigit(var1, var4, var6, UPCEANReader.L_AND_G_PATTERNS);
         var3.append((char)(var9 % 10 + 48));
         int var10 = var4.length;

         for(var11 = 0; var11 < var10; ++var11) {
            var6 += var4[var11];
         }

         var10 = var7;
         if (var9 >= 10) {
            var10 = var7 | 1 << 4 - var8;
         }

         var11 = var6;
         if (var8 != 4) {
            var11 = var1.getNextUnset(var1.getNextSet(var6));
         }

         ++var8;
         var7 = var10;
      }

      if (var3.length() != 5) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         var11 = determineCheckDigit(var7);
         if (extensionChecksum(var3.toString()) != var11) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            return var6;
         }
      }
   }

   private static int determineCheckDigit(int var0) throws NotFoundException {
      for(int var1 = 0; var1 < 10; ++var1) {
         if (var0 == CHECK_DIGIT_ENCODINGS[var1]) {
            return var1;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static int extensionChecksum(CharSequence var0) {
      int var1 = var0.length();
      int var2 = 0;

      int var3;
      for(var3 = var1 - 2; var3 >= 0; var3 -= 2) {
         var2 += var0.charAt(var3) - 48;
      }

      var2 *= 3;

      for(var3 = var1 - 1; var3 >= 0; var3 -= 2) {
         var2 += var0.charAt(var3) - 48;
      }

      return var2 * 3 % 10;
   }

   private static String parseExtension5String(String var0) {
      String var1;
      switch(var0.charAt(0)) {
      case '0':
         var1 = "£";
         break;
      case '5':
         var1 = "$";
         break;
      case '9':
         if ("90000".equals(var0)) {
            var0 = null;
            return var0;
         }

         if ("99991".equals(var0)) {
            var0 = "0.00";
            return var0;
         }

         if ("99990".equals(var0)) {
            var0 = "Used";
            return var0;
         }

         var1 = "";
         break;
      default:
         var1 = "";
      }

      int var2 = Integer.parseInt(var0.substring(1));
      int var3 = var2 / 100;
      var2 %= 100;
      if (var2 < 10) {
         var0 = "0" + var2;
      } else {
         var0 = String.valueOf(var2);
      }

      var0 = var1 + String.valueOf(var3) + '.' + var0;
      return var0;
   }

   private static Map parseExtensionString(String var0) {
      Object var1 = null;
      EnumMap var3;
      if (var0.length() != 5) {
         var3 = (EnumMap)var1;
      } else {
         String var2 = parseExtension5String(var0);
         var3 = (EnumMap)var1;
         if (var2 != null) {
            var3 = new EnumMap(ResultMetadataType.class);
            var3.put(ResultMetadataType.SUGGESTED_PRICE, var2);
         }
      }

      return var3;
   }

   Result decodeRow(int var1, BitArray var2, int[] var3) throws NotFoundException {
      StringBuilder var4 = this.decodeRowStringBuffer;
      var4.setLength(0);
      int var5 = this.decodeMiddle(var2, var3, var4);
      String var11 = var4.toString();
      Map var8 = parseExtensionString(var11);
      ResultPoint var6 = new ResultPoint((float)(var3[0] + var3[1]) / 2.0F, (float)var1);
      ResultPoint var7 = new ResultPoint((float)var5, (float)var1);
      BarcodeFormat var9 = BarcodeFormat.UPC_EAN_EXTENSION;
      Result var10 = new Result(var11, (byte[])null, new ResultPoint[]{var6, var7}, var9);
      if (var8 != null) {
         var10.putAllMetadata(var8);
      }

      return var10;
   }
}
