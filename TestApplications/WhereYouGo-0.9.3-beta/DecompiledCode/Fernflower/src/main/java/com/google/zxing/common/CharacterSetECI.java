package com.google.zxing.common;

import com.google.zxing.FormatException;
import java.util.HashMap;
import java.util.Map;

public enum CharacterSetECI {
   ASCII(new int[]{27, 170}, new String[]{"US-ASCII"}),
   Big5(28),
   Cp1250(21, new String[]{"windows-1250"}),
   Cp1251(22, new String[]{"windows-1251"}),
   Cp1252(23, new String[]{"windows-1252"}),
   Cp1256(24, new String[]{"windows-1256"}),
   Cp437(new int[]{0, 2}, new String[0]),
   EUC_KR(30, new String[]{"EUC-KR"}),
   GB18030(29, new String[]{"GB2312", "EUC_CN", "GBK"}),
   ISO8859_1(new int[]{1, 3}, new String[]{"ISO-8859-1"}),
   ISO8859_10(12, new String[]{"ISO-8859-10"}),
   ISO8859_11(13, new String[]{"ISO-8859-11"}),
   ISO8859_13(15, new String[]{"ISO-8859-13"}),
   ISO8859_14(16, new String[]{"ISO-8859-14"}),
   ISO8859_15(17, new String[]{"ISO-8859-15"}),
   ISO8859_16(18, new String[]{"ISO-8859-16"}),
   ISO8859_2(4, new String[]{"ISO-8859-2"}),
   ISO8859_3(5, new String[]{"ISO-8859-3"}),
   ISO8859_4(6, new String[]{"ISO-8859-4"}),
   ISO8859_5(7, new String[]{"ISO-8859-5"}),
   ISO8859_6(8, new String[]{"ISO-8859-6"}),
   ISO8859_7(9, new String[]{"ISO-8859-7"}),
   ISO8859_8(10, new String[]{"ISO-8859-8"}),
   ISO8859_9(11, new String[]{"ISO-8859-9"});

   private static final Map NAME_TO_ECI = new HashMap();
   SJIS(20, new String[]{"Shift_JIS"}),
   UTF8(26, new String[]{"UTF-8"}),
   UnicodeBigUnmarked(25, new String[]{"UTF-16BE", "UnicodeBig"});

   private static final Map VALUE_TO_ECI = new HashMap();
   private final String[] otherEncodingNames;
   private final int[] values;

   static {
      CharacterSetECI[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         CharacterSetECI var3 = var0[var2];
         int[] var4 = var3.values;
         int var5 = var4.length;

         int var6;
         for(var6 = 0; var6 < var5; ++var6) {
            int var7 = var4[var6];
            VALUE_TO_ECI.put(var7, var3);
         }

         NAME_TO_ECI.put(var3.name(), var3);
         String[] var8 = var3.otherEncodingNames;
         var5 = var8.length;

         for(var6 = 0; var6 < var5; ++var6) {
            String var9 = var8[var6];
            NAME_TO_ECI.put(var9, var3);
         }
      }

   }

   private CharacterSetECI(int var3) {
      this(new int[]{var3});
   }

   private CharacterSetECI(int var3, String... var4) {
      this.values = new int[]{var3};
      this.otherEncodingNames = var4;
   }

   private CharacterSetECI(int[] var3, String... var4) {
      this.values = var3;
      this.otherEncodingNames = var4;
   }

   public static CharacterSetECI getCharacterSetECIByName(String var0) {
      return (CharacterSetECI)NAME_TO_ECI.get(var0);
   }

   public static CharacterSetECI getCharacterSetECIByValue(int var0) throws FormatException {
      if (var0 >= 0 && var0 < 900) {
         return (CharacterSetECI)VALUE_TO_ECI.get(var0);
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   public int getValue() {
      return this.values[0];
   }
}
