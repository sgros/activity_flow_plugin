package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public final class Code128Writer extends OneDimensionalCodeWriter {
   private static final int CODE_CODE_B = 100;
   private static final int CODE_CODE_C = 99;
   private static final int CODE_FNC_1 = 102;
   private static final int CODE_FNC_2 = 97;
   private static final int CODE_FNC_3 = 96;
   private static final int CODE_FNC_4_B = 100;
   private static final int CODE_START_B = 104;
   private static final int CODE_START_C = 105;
   private static final int CODE_STOP = 106;
   private static final char ESCAPE_FNC_1 = 'ñ';
   private static final char ESCAPE_FNC_2 = 'ò';
   private static final char ESCAPE_FNC_3 = 'ó';
   private static final char ESCAPE_FNC_4 = 'ô';

   private static int chooseCode(CharSequence var0, int var1, int var2) {
      Code128Writer.CType var3 = findCType(var0, var1);
      int var4;
      if (var3 != Code128Writer.CType.UNCODABLE && var3 != Code128Writer.CType.ONE_DIGIT) {
         var4 = var2;
         if (var2 != 99) {
            Code128Writer.CType var5;
            if (var2 == 100) {
               var4 = var2;
               if (var3 != Code128Writer.CType.FNC_1) {
                  var5 = findCType(var0, var1 + 2);
                  var4 = var2;
                  if (var5 != Code128Writer.CType.UNCODABLE) {
                     var4 = var2;
                     if (var5 != Code128Writer.CType.ONE_DIGIT) {
                        if (var5 == Code128Writer.CType.FNC_1) {
                           if (findCType(var0, var1 + 3) == Code128Writer.CType.TWO_DIGITS) {
                              var4 = 99;
                           } else {
                              var4 = 100;
                           }
                        } else {
                           var1 += 4;

                           while(true) {
                              var5 = findCType(var0, var1);
                              if (var5 != Code128Writer.CType.TWO_DIGITS) {
                                 if (var5 == Code128Writer.CType.ONE_DIGIT) {
                                    var4 = 100;
                                 } else {
                                    var4 = 99;
                                 }
                                 break;
                              }

                              var1 += 2;
                           }
                        }
                     }
                  }
               }
            } else {
               var5 = var3;
               if (var3 == Code128Writer.CType.FNC_1) {
                  var5 = findCType(var0, var1 + 1);
               }

               if (var5 == Code128Writer.CType.TWO_DIGITS) {
                  var4 = 99;
               } else {
                  var4 = 100;
               }
            }
         }
      } else {
         var4 = 100;
      }

      return var4;
   }

   private static Code128Writer.CType findCType(CharSequence var0, int var1) {
      int var2 = var0.length();
      Code128Writer.CType var4;
      if (var1 >= var2) {
         var4 = Code128Writer.CType.UNCODABLE;
      } else {
         char var3 = var0.charAt(var1);
         if (var3 == 241) {
            var4 = Code128Writer.CType.FNC_1;
         } else if (var3 >= '0' && var3 <= '9') {
            if (var1 + 1 >= var2) {
               var4 = Code128Writer.CType.ONE_DIGIT;
            } else {
               char var5 = var0.charAt(var1 + 1);
               if (var5 >= '0' && var5 <= '9') {
                  var4 = Code128Writer.CType.TWO_DIGITS;
               } else {
                  var4 = Code128Writer.CType.ONE_DIGIT;
               }
            }
         } else {
            var4 = Code128Writer.CType.UNCODABLE;
         }
      }

      return var4;
   }

   public BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map var5) throws WriterException {
      if (var2 != BarcodeFormat.CODE_128) {
         throw new IllegalArgumentException("Can only encode CODE_128, but got " + var2);
      } else {
         return super.encode(var1, var2, var3, var4, var5);
      }
   }

   public boolean[] encode(String var1) {
      int var2 = var1.length();
      if (var2 > 0 && var2 <= 80) {
         int var3;
         for(var3 = 0; var3 < var2; ++var3) {
            char var4 = var1.charAt(var3);
            if (var4 < ' ' || var4 > '~') {
               switch(var4) {
               case 'ñ':
               case 'ò':
               case 'ó':
               case 'ô':
                  break;
               default:
                  throw new IllegalArgumentException("Bad character in input: " + var4);
               }
            }
         }

         ArrayList var5 = new ArrayList();
         int var6 = 0;
         int var7 = 1;
         int var8 = 0;
         int var9 = 0;

         while(var9 < var2) {
            int var10 = chooseCode(var1, var9, var8);
            int var11;
            if (var10 == var8) {
               switch(var1.charAt(var9)) {
               case 'ñ':
                  var3 = 102;
                  break;
               case 'ò':
                  var3 = 97;
                  break;
               case 'ó':
                  var3 = 96;
                  break;
               case 'ô':
                  var3 = 100;
                  break;
               default:
                  if (var8 == 100) {
                     var3 = var1.charAt(var9) - 32;
                  } else {
                     var3 = Integer.parseInt(var1.substring(var9, var9 + 2));
                     ++var9;
                  }
               }

               var10 = var9 + 1;
               var11 = var8;
            } else {
               if (var8 == 0) {
                  if (var10 == 100) {
                     var3 = 104;
                  } else {
                     var3 = 105;
                  }
               } else {
                  var3 = var10;
               }

               var11 = var10;
               var10 = var9;
            }

            var5.add(Code128Reader.CODE_PATTERNS[var3]);
            var3 = var6 + var3 * var7;
            var6 = var3;
            var8 = var11;
            var9 = var10;
            if (var10 != 0) {
               ++var7;
               var6 = var3;
               var8 = var11;
               var9 = var10;
            }
         }

         var5.add(Code128Reader.CODE_PATTERNS[var6 % 103]);
         var5.add(Code128Reader.CODE_PATTERNS[106]);
         var7 = 0;
         Iterator var12 = var5.iterator();

         while(var12.hasNext()) {
            int[] var13 = (int[])var12.next();
            var6 = var13.length;
            var9 = 0;
            var3 = var7;

            while(true) {
               var7 = var3;
               if (var9 >= var6) {
                  break;
               }

               var3 += var13[var9];
               ++var9;
            }
         }

         boolean[] var14 = new boolean[var7];
         var3 = 0;

         for(Iterator var15 = var5.iterator(); var15.hasNext(); var3 += appendPattern(var14, var3, (int[])var15.next(), true)) {
         }

         return var14;
      } else {
         throw new IllegalArgumentException("Contents length should be between 1 and 80 characters, but got " + var2);
      }
   }

   private static enum CType {
      FNC_1,
      ONE_DIGIT,
      TWO_DIGITS,
      UNCODABLE;
   }
}
