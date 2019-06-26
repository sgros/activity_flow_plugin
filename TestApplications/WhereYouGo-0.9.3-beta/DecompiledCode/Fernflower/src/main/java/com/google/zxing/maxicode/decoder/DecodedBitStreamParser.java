package com.google.zxing.maxicode.decoder;

import com.google.zxing.common.DecoderResult;
import java.text.DecimalFormat;
import java.util.List;

final class DecodedBitStreamParser {
   private static final char ECI = '\ufffa';
   private static final char FS = '\u001c';
   private static final char GS = '\u001d';
   private static final char LATCHA = '\ufff7';
   private static final char LATCHB = '\ufff8';
   private static final char LOCK = '\ufff9';
   private static final char NS = '\ufffb';
   private static final char PAD = '￼';
   private static final char RS = '\u001e';
   private static final String[] SETS = new String[]{"\nABCDEFGHIJKLMNOPQRSTUVWXYZ\ufffa\u001c\u001d\u001e\ufffb ￼\"#$%&'()*+,-./0123456789:\ufff1\ufff2\ufff3\ufff4\ufff8", "`abcdefghijklmnopqrstuvwxyz\ufffa\u001c\u001d\u001e\ufffb{￼}~\u007f;<=>?[\\]^_ ,./:@!|￼\ufff5\ufff6￼\ufff0\ufff2\ufff3\ufff4\ufff7", "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚ\ufffa\u001c\u001d\u001eÛÜÝÞßª¬±²³µ¹º¼½¾\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\ufff7 \ufff9\ufff3\ufff4\ufff8", "àáâãäåæçèéêëìíîïðñòóôõö÷øùú\ufffa\u001c\u001d\u001e\ufffbûüýþÿ¡¨«¯°´·¸»¿\u008a\u008b\u008c\u008d\u008e\u008f\u0090\u0091\u0092\u0093\u0094\ufff7 \ufff2\ufff9\ufff4\ufff8", "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\ufffa￼￼\u001b\ufffb\u001c\u001d\u001e\u001f\u009f ¢£¤¥¦§©\u00ad®¶\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\ufff7 \ufff2\ufff3\ufff9\ufff8", "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&'()*+,-./0123456789:;<=>?"};
   private static final char SHIFTA = '\ufff0';
   private static final char SHIFTB = '\ufff1';
   private static final char SHIFTC = '\ufff2';
   private static final char SHIFTD = '\ufff3';
   private static final char SHIFTE = '\ufff4';
   private static final char THREESHIFTA = '\ufff6';
   private static final char TWOSHIFTA = '\ufff5';

   private DecodedBitStreamParser() {
   }

   static DecoderResult decode(byte[] var0, int var1) {
      StringBuilder var2 = new StringBuilder(144);
      switch(var1) {
      case 2:
      case 3:
         String var4;
         if (var1 == 2) {
            int var3 = getPostCode2(var0);
            var4 = (new DecimalFormat("0000000000".substring(0, getPostCode2Length(var0)))).format((long)var3);
         } else {
            var4 = getPostCode3(var0);
         }

         DecimalFormat var5 = new DecimalFormat("000");
         String var6 = var5.format((long)getCountry(var0));
         String var7 = var5.format((long)getServiceClass(var0));
         var2.append(getMessage(var0, 10, 84));
         if (var2.toString().startsWith("[)>\u001e01\u001d")) {
            var2.insert(9, var4 + '\u001d' + var6 + '\u001d' + var7 + '\u001d');
         } else {
            var2.insert(0, var4 + '\u001d' + var6 + '\u001d' + var7 + '\u001d');
         }
         break;
      case 4:
         var2.append(getMessage(var0, 1, 93));
         break;
      case 5:
         var2.append(getMessage(var0, 1, 77));
      }

      return new DecoderResult(var0, var2.toString(), (List)null, String.valueOf(var1));
   }

   private static int getBit(int var0, byte[] var1) {
      byte var2 = 1;
      int var3 = var0 - 1;
      byte var4 = var2;
      if ((var1[var3 / 6] & 1 << 5 - var3 % 6) == 0) {
         var4 = 0;
      }

      return var4;
   }

   private static int getCountry(byte[] var0) {
      return getInt(var0, new byte[]{53, 54, 43, 44, 45, 46, 47, 48, 37, 38});
   }

   private static int getInt(byte[] var0, byte[] var1) {
      if (var1.length == 0) {
         throw new IllegalArgumentException();
      } else {
         int var2 = 0;

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2 += getBit(var1[var3], var0) << var1.length - var3 - 1;
         }

         return var2;
      }
   }

   private static String getMessage(byte[] var0, int var1, int var2) {
      StringBuilder var3 = new StringBuilder();
      int var4 = -1;
      int var5 = 0;
      int var6 = 0;

      int var15;
      for(int var7 = var1; var7 < var1 + var2; var4 = var15) {
         char var8 = SETS[var5].charAt(var0[var7]);
         byte var14;
         switch(var8) {
         case '\ufff0':
         case '\ufff1':
         case '\ufff2':
         case '\ufff3':
         case '\ufff4':
            var15 = var8 - '\ufff0';
            var4 = 1;
            var6 = var5;
            var5 = var15;
            break;
         case '\ufff5':
            var14 = 0;
            var4 = 2;
            var6 = var5;
            var5 = var14;
            break;
         case '\ufff6':
            var14 = 0;
            var4 = 3;
            var6 = var5;
            var5 = var14;
            break;
         case '\ufff7':
            var5 = 0;
            var4 = -1;
            break;
         case '\ufff8':
            var5 = 1;
            var4 = -1;
            break;
         case '\ufff9':
            var4 = -1;
            break;
         case '\ufffa':
         default:
            var3.append(var8);
            break;
         case '\ufffb':
            ++var7;
            byte var9 = var0[var7];
            ++var7;
            byte var10 = var0[var7];
            ++var7;
            byte var11 = var0[var7];
            ++var7;
            byte var12 = var0[var7];
            ++var7;
            byte var13 = var0[var7];
            var3.append((new DecimalFormat("000000000")).format((long)((var9 << 24) + (var10 << 18) + (var11 << 12) + (var12 << 6) + var13)));
         }

         var15 = var4 - 1;
         if (var4 == 0) {
            var5 = var6;
         }

         ++var7;
      }

      while(var3.length() > 0 && var3.charAt(var3.length() - 1) == '￼') {
         var3.setLength(var3.length() - 1);
      }

      return var3.toString();
   }

   private static int getPostCode2(byte[] var0) {
      return getInt(var0, new byte[]{33, 34, 35, 36, 25, 26, 27, 28, 29, 30, 19, 20, 21, 22, 23, 24, 13, 14, 15, 16, 17, 18, 7, 8, 9, 10, 11, 12, 1, 2});
   }

   private static int getPostCode2Length(byte[] var0) {
      return getInt(var0, new byte[]{39, 40, 41, 42, 31, 32});
   }

   private static String getPostCode3(byte[] var0) {
      return String.valueOf(new char[]{SETS[0].charAt(getInt(var0, new byte[]{39, 40, 41, 42, 31, 32})), SETS[0].charAt(getInt(var0, new byte[]{33, 34, 35, 36, 25, 26})), SETS[0].charAt(getInt(var0, new byte[]{27, 28, 29, 30, 19, 20})), SETS[0].charAt(getInt(var0, new byte[]{21, 22, 23, 24, 13, 14})), SETS[0].charAt(getInt(var0, new byte[]{15, 16, 17, 18, 7, 8})), SETS[0].charAt(getInt(var0, new byte[]{9, 10, 11, 12, 1, 2}))});
   }

   private static int getServiceClass(byte[] var0) {
      return getInt(var0, new byte[]{55, 56, 57, 58, 59, 60, 49, 50, 51, 52});
   }
}
