package com.google.zxing.common;

import com.google.zxing.DecodeHintType;
import java.nio.charset.Charset;
import java.util.Map;

public final class StringUtils {
   private static final boolean ASSUME_SHIFT_JIS;
   private static final String EUC_JP = "EUC_JP";
   public static final String GB2312 = "GB2312";
   private static final String ISO88591 = "ISO8859_1";
   private static final String PLATFORM_DEFAULT_ENCODING = Charset.defaultCharset().name();
   public static final String SHIFT_JIS = "SJIS";
   private static final String UTF8 = "UTF8";

   static {
      boolean var0;
      if (!"SJIS".equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING) && !"EUC_JP".equalsIgnoreCase(PLATFORM_DEFAULT_ENCODING)) {
         var0 = false;
      } else {
         var0 = true;
      }

      ASSUME_SHIFT_JIS = var0;
   }

   private StringUtils() {
   }

   public static String guessEncoding(byte[] var0, Map var1) {
      String var29;
      if (var1 != null && var1.containsKey(DecodeHintType.CHARACTER_SET)) {
         var29 = var1.get(DecodeHintType.CHARACTER_SET).toString();
      } else {
         int var2 = var0.length;
         boolean var3 = true;
         boolean var4 = true;
         boolean var5 = true;
         int var6 = 0;
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;
         int var10 = 0;
         int var11 = 0;
         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var15 = 0;
         int var16 = 0;
         boolean var17;
         if (var0.length > 3 && var0[0] == -17 && var0[1] == -69 && var0[2] == -65) {
            var17 = true;
         } else {
            var17 = false;
         }

         int var24;
         for(int var18 = 0; var18 < var2 && (var3 || var4 || var5); var6 = var24) {
            int var19 = var0[var18] & 255;
            boolean var20 = var5;
            int var21 = var7;
            int var22 = var8;
            int var23 = var9;
            var24 = var6;
            if (var5) {
               label207: {
                  if (var6 > 0) {
                     var24 = var6;
                     if ((var19 & 128) != 0) {
                        var24 = var6 - 1;
                        var23 = var9;
                        var22 = var8;
                        var21 = var7;
                        var20 = var5;
                        break label207;
                     }
                  } else {
                     var20 = var5;
                     var21 = var7;
                     var22 = var8;
                     var23 = var9;
                     var24 = var6;
                     if ((var19 & 128) == 0) {
                        break label207;
                     }

                     var24 = var6;
                     if ((var19 & 64) != 0) {
                        var24 = var6 + 1;
                        if ((var19 & 32) == 0) {
                           var21 = var7 + 1;
                           var20 = var5;
                           var22 = var8;
                           var23 = var9;
                           break label207;
                        }

                        ++var24;
                        if ((var19 & 16) == 0) {
                           var22 = var8 + 1;
                           var20 = var5;
                           var21 = var7;
                           var23 = var9;
                           break label207;
                        }

                        var6 = var24 + 1;
                        var24 = var6;
                        if ((var19 & 8) == 0) {
                           var23 = var9 + 1;
                           var20 = var5;
                           var21 = var7;
                           var22 = var8;
                           var24 = var6;
                           break label207;
                        }
                     }
                  }

                  var20 = false;
                  var21 = var7;
                  var22 = var8;
                  var23 = var9;
               }
            }

            boolean var25 = var3;
            var7 = var16;
            if (var3) {
               if (var19 > 127 && var19 < 160) {
                  var25 = false;
                  var7 = var16;
               } else {
                  var25 = var3;
                  var7 = var16;
                  if (var19 > 159) {
                     label209: {
                        if (var19 >= 192 && var19 != 215) {
                           var25 = var3;
                           var7 = var16;
                           if (var19 != 247) {
                              break label209;
                           }
                        }

                        var7 = var16 + 1;
                        var25 = var3;
                     }
                  }
               }
            }

            var5 = var4;
            var8 = var10;
            var6 = var13;
            var9 = var12;
            int var26 = var11;
            int var27 = var15;
            int var28 = var14;
            if (var4) {
               if (var10 > 0) {
                  if (var19 >= 64 && var19 != 127 && var19 <= 252) {
                     var8 = var10 - 1;
                     var5 = var4;
                     var6 = var13;
                     var9 = var12;
                     var26 = var11;
                     var27 = var15;
                     var28 = var14;
                  } else {
                     var5 = false;
                     var28 = var14;
                     var27 = var15;
                     var26 = var11;
                     var9 = var12;
                     var6 = var13;
                     var8 = var10;
                  }
               } else if (var19 != 128 && var19 != 160 && var19 <= 239) {
                  byte var31;
                  if (var19 > 160 && var19 < 224) {
                     var13 = var11 + 1;
                     var31 = 0;
                     var11 = var12 + 1;
                     var5 = var4;
                     var8 = var10;
                     var6 = var31;
                     var9 = var11;
                     var26 = var13;
                     var27 = var15;
                     var28 = var14;
                     if (var11 > var14) {
                        var28 = var11;
                        var5 = var4;
                        var8 = var10;
                        var6 = var31;
                        var9 = var11;
                        var26 = var13;
                        var27 = var15;
                     }
                  } else if (var19 > 127) {
                     var12 = var10 + 1;
                     var31 = 0;
                     var10 = var13 + 1;
                     var5 = var4;
                     var8 = var12;
                     var6 = var10;
                     var9 = var31;
                     var26 = var11;
                     var27 = var15;
                     var28 = var14;
                     if (var10 > var15) {
                        var27 = var10;
                        var5 = var4;
                        var8 = var12;
                        var6 = var10;
                        var9 = var31;
                        var26 = var11;
                        var28 = var14;
                     }
                  } else {
                     var9 = 0;
                     var6 = 0;
                     var5 = var4;
                     var8 = var10;
                     var26 = var11;
                     var27 = var15;
                     var28 = var14;
                  }
               } else {
                  var5 = false;
                  var8 = var10;
                  var6 = var13;
                  var9 = var12;
                  var26 = var11;
                  var27 = var15;
                  var28 = var14;
               }
            }

            ++var18;
            var3 = var25;
            var4 = var5;
            var5 = var20;
            var16 = var7;
            var10 = var8;
            var13 = var6;
            var12 = var9;
            var11 = var26;
            var15 = var27;
            var14 = var28;
            var7 = var21;
            var8 = var22;
            var9 = var23;
         }

         boolean var32 = var5;
         if (var5) {
            var32 = var5;
            if (var6 > 0) {
               var32 = false;
            }
         }

         boolean var30 = var4;
         if (var4) {
            var30 = var4;
            if (var10 > 0) {
               var30 = false;
            }
         }

         if (!var32 || !var17 && var7 + var8 + var9 <= 0) {
            if (!var30 || !ASSUME_SHIFT_JIS && var14 < 3 && var15 < 3) {
               if (var3 && var30) {
                  if ((var14 != 2 || var11 != 2) && var16 * 10 < var2) {
                     var29 = "ISO8859_1";
                  } else {
                     var29 = "SJIS";
                  }
               } else if (var3) {
                  var29 = "ISO8859_1";
               } else if (var30) {
                  var29 = "SJIS";
               } else if (var32) {
                  var29 = "UTF8";
               } else {
                  var29 = PLATFORM_DEFAULT_ENCODING;
               }
            } else {
               var29 = "SJIS";
            }
         } else {
            var29 = "UTF8";
         }
      }

      return var29;
   }
}
