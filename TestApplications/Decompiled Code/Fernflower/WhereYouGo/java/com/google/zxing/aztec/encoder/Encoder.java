package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;

public final class Encoder {
   public static final int DEFAULT_AZTEC_LAYERS = 0;
   public static final int DEFAULT_EC_PERCENT = 33;
   private static final int MAX_NB_BITS = 32;
   private static final int MAX_NB_BITS_COMPACT = 4;
   private static final int[] WORD_SIZE = new int[]{4, 6, 6, 8, 8, 8, 8, 8, 8, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};

   private Encoder() {
   }

   private static int[] bitsToWords(BitArray var0, int var1, int var2) {
      int[] var3 = new int[var2];
      var2 = 0;

      for(int var4 = var0.getSize() / var1; var2 < var4; ++var2) {
         int var5 = 0;

         for(int var6 = 0; var6 < var1; ++var6) {
            int var7;
            if (var0.get(var2 * var1 + var6)) {
               var7 = 1 << var1 - var6 - 1;
            } else {
               var7 = 0;
            }

            var5 |= var7;
         }

         var3[var2] = var5;
      }

      return var3;
   }

   private static void drawBullsEye(BitMatrix var0, int var1, int var2) {
      for(int var3 = 0; var3 < var2; var3 += 2) {
         for(int var4 = var1 - var3; var4 <= var1 + var3; ++var4) {
            var0.set(var4, var1 - var3);
            var0.set(var4, var1 + var3);
            var0.set(var1 - var3, var4);
            var0.set(var1 + var3, var4);
         }
      }

      var0.set(var1 - var2, var1 - var2);
      var0.set(var1 - var2 + 1, var1 - var2);
      var0.set(var1 - var2, var1 - var2 + 1);
      var0.set(var1 + var2, var1 - var2);
      var0.set(var1 + var2, var1 - var2 + 1);
      var0.set(var1 + var2, var1 + var2 - 1);
   }

   private static void drawModeMessage(BitMatrix var0, boolean var1, int var2, BitArray var3) {
      int var4 = var2 / 2;
      int var5;
      if (var1) {
         for(var2 = 0; var2 < 7; ++var2) {
            var5 = var4 - 3 + var2;
            if (var3.get(var2)) {
               var0.set(var5, var4 - 5);
            }

            if (var3.get(var2 + 7)) {
               var0.set(var4 + 5, var5);
            }

            if (var3.get(20 - var2)) {
               var0.set(var5, var4 + 5);
            }

            if (var3.get(27 - var2)) {
               var0.set(var4 - 5, var5);
            }
         }
      } else {
         for(var2 = 0; var2 < 10; ++var2) {
            var5 = var4 - 5 + var2 + var2 / 5;
            if (var3.get(var2)) {
               var0.set(var5, var4 - 7);
            }

            if (var3.get(var2 + 10)) {
               var0.set(var4 + 7, var5);
            }

            if (var3.get(29 - var2)) {
               var0.set(var5, var4 + 7);
            }

            if (var3.get(39 - var2)) {
               var0.set(var4 - 7, var5);
            }
         }
      }

   }

   public static AztecCode encode(byte[] var0) {
      return encode(var0, 33, 0);
   }

   public static AztecCode encode(byte[] var0, int var1, int var2) {
      BitArray var3 = (new HighLevelEncoder(var0)).encode();
      int var4 = var3.getSize() * var1 / 100 + 11;
      int var5 = var3.getSize();
      boolean var6;
      int var7;
      boolean var8;
      int var9;
      BitArray var10;
      int var11;
      int var12;
      BitArray var16;
      byte var18;
      if (var2 != 0) {
         if (var2 < 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         var7 = Math.abs(var2);
         if (var6) {
            var18 = 4;
         } else {
            var18 = 32;
         }

         if (var7 > var18) {
            throw new IllegalArgumentException(String.format("Illegal value %s for layers", var2));
         }

         var2 = totalBitsInLayer(var7, var6);
         var1 = WORD_SIZE[var7];
         var16 = stuffBits(var3, var1);
         if (var16.getSize() + var4 > var2 - var2 % var1) {
            throw new IllegalArgumentException("Data to large for user specified layer");
         }

         var8 = var6;
         var9 = var7;
         var10 = var16;
         var11 = var2;
         var12 = var1;
         if (var6) {
            var8 = var6;
            var9 = var7;
            var10 = var16;
            var11 = var2;
            var12 = var1;
            if (var16.getSize() > var1 << 6) {
               throw new IllegalArgumentException("Data to large for user specified layer");
            }
         }
      } else {
         var11 = 0;
         var16 = null;
         var2 = 0;

         while(true) {
            if (var2 > 32) {
               throw new IllegalArgumentException("Data too large for an Aztec code");
            }

            if (var2 <= 3) {
               var6 = true;
            } else {
               var6 = false;
            }

            if (var6) {
               var9 = var2 + 1;
            } else {
               var9 = var2;
            }

            var7 = totalBitsInLayer(var9, var6);
            var10 = var16;
            var12 = var11;
            if (var5 + var4 <= var7) {
               label166: {
                  var1 = var11;
                  if (var11 != WORD_SIZE[var9]) {
                     var1 = WORD_SIZE[var9];
                     var16 = stuffBits(var3, var1);
                  }

                  if (var6) {
                     var10 = var16;
                     var12 = var1;
                     if (var16.getSize() > var1 << 6) {
                        break label166;
                     }
                  }

                  var8 = var6;
                  var10 = var16;
                  var11 = var7;
                  var12 = var1;
                  if (var16.getSize() + var4 <= var7 - var7 % var1) {
                     break;
                  }

                  var12 = var1;
                  var10 = var16;
               }
            }

            ++var2;
            var16 = var10;
            var11 = var12;
         }
      }

      var3 = generateCheckWords(var10, var11, var12);
      var5 = var10.getSize() / var12;
      var10 = generateModeMessage(var8, var9, var5);
      if (var8) {
         var18 = 11;
      } else {
         var18 = 14;
      }

      var4 = var18 + (var9 << 2);
      int[] var13 = new int[var4];
      if (var8) {
         var2 = var4;
         var11 = 0;

         while(true) {
            var1 = var2;
            if (var11 >= var13.length) {
               break;
            }

            var13[var11] = var11++;
         }
      } else {
         var11 = var4 + 1 + (var4 / 2 - 1) / 15 * 2;
         var7 = var4 / 2;
         var12 = var11 / 2;
         var2 = 0;

         while(true) {
            var1 = var11;
            if (var2 >= var7) {
               break;
            }

            var1 = var2 + var2 / 15;
            var13[var7 - var2 - 1] = var12 - var1 - 1;
            var13[var7 + var2] = var12 + var1 + 1;
            ++var2;
         }
      }

      BitMatrix var17 = new BitMatrix(var1);
      var11 = 0;

      for(var2 = 0; var11 < var9; ++var11) {
         byte var20;
         if (var8) {
            var20 = 9;
         } else {
            var20 = 12;
         }

         int var14 = (var9 - var11 << 2) + var20;

         for(var12 = 0; var12 < var14; ++var12) {
            int var15 = var12 << 1;

            for(var7 = 0; var7 < 2; ++var7) {
               if (var3.get(var2 + var15 + var7)) {
                  var17.set(var13[(var11 << 1) + var7], var13[(var11 << 1) + var12]);
               }

               if (var3.get((var14 << 1) + var2 + var15 + var7)) {
                  var17.set(var13[(var11 << 1) + var12], var13[var4 - 1 - (var11 << 1) - var7]);
               }

               if (var3.get((var14 << 2) + var2 + var15 + var7)) {
                  var17.set(var13[var4 - 1 - (var11 << 1) - var7], var13[var4 - 1 - (var11 << 1) - var12]);
               }

               if (var3.get(var14 * 6 + var2 + var15 + var7)) {
                  var17.set(var13[var4 - 1 - (var11 << 1) - var12], var13[(var11 << 1) + var7]);
               }
            }
         }

         var2 += var14 << 3;
      }

      drawModeMessage(var17, var8, var1, var10);
      if (var8) {
         drawBullsEye(var17, var1 / 2, 5);
      } else {
         drawBullsEye(var17, var1 / 2, 7);
         var11 = 0;

         for(var2 = 0; var11 < var4 / 2 - 1; var2 += 16) {
            for(var12 = var1 / 2 & 1; var12 < var1; var12 += 2) {
               var17.set(var1 / 2 - var2, var12);
               var17.set(var1 / 2 + var2, var12);
               var17.set(var12, var1 / 2 - var2);
               var17.set(var12, var1 / 2 + var2);
            }

            var11 += 15;
         }
      }

      AztecCode var19 = new AztecCode();
      var19.setCompact(var8);
      var19.setSize(var1);
      var19.setLayers(var9);
      var19.setCodeWords(var5);
      var19.setMatrix(var17);
      return var19;
   }

   private static BitArray generateCheckWords(BitArray var0, int var1, int var2) {
      byte var3 = 0;
      int var4 = var0.getSize() / var2;
      ReedSolomonEncoder var5 = new ReedSolomonEncoder(getGF(var2));
      int var6 = var1 / var2;
      int[] var7 = bitsToWords(var0, var2, var6);
      var5.encode(var7, var6 - var4);
      BitArray var8 = new BitArray();
      var8.appendBits(0, var1 % var2);
      var4 = var7.length;

      for(var1 = var3; var1 < var4; ++var1) {
         var8.appendBits(var7[var1], var2);
      }

      return var8;
   }

   static BitArray generateModeMessage(boolean var0, int var1, int var2) {
      BitArray var3 = new BitArray();
      if (var0) {
         var3.appendBits(var1 - 1, 2);
         var3.appendBits(var2 - 1, 6);
         var3 = generateCheckWords(var3, 28, 4);
      } else {
         var3.appendBits(var1 - 1, 5);
         var3.appendBits(var2 - 1, 11);
         var3 = generateCheckWords(var3, 40, 4);
      }

      return var3;
   }

   private static GenericGF getGF(int var0) {
      GenericGF var1;
      switch(var0) {
      case 4:
         var1 = GenericGF.AZTEC_PARAM;
         break;
      case 5:
      case 7:
      case 9:
      case 11:
      default:
         throw new IllegalArgumentException("Unsupported word size " + var0);
      case 6:
         var1 = GenericGF.AZTEC_DATA_6;
         break;
      case 8:
         var1 = GenericGF.AZTEC_DATA_8;
         break;
      case 10:
         var1 = GenericGF.AZTEC_DATA_10;
         break;
      case 12:
         var1 = GenericGF.AZTEC_DATA_12;
      }

      return var1;
   }

   static BitArray stuffBits(BitArray var0, int var1) {
      BitArray var2 = new BitArray();
      int var3 = var0.getSize();
      int var4 = (1 << var1) - 2;

      for(int var5 = 0; var5 < var3; var5 += var1) {
         int var6 = 0;

         int var8;
         for(int var7 = 0; var7 < var1; var6 = var8) {
            label29: {
               if (var5 + var7 < var3) {
                  var8 = var6;
                  if (!var0.get(var5 + var7)) {
                     break label29;
                  }
               }

               var8 = var6 | 1 << var1 - 1 - var7;
            }

            ++var7;
         }

         if ((var6 & var4) == var4) {
            var2.appendBits(var6 & var4, var1);
            --var5;
         } else if ((var6 & var4) == 0) {
            var2.appendBits(var6 | 1, var1);
            --var5;
         } else {
            var2.appendBits(var6, var1);
         }
      }

      return var2;
   }

   private static int totalBitsInLayer(int var0, boolean var1) {
      byte var2;
      if (var1) {
         var2 = 88;
      } else {
         var2 = 112;
      }

      return (var2 + (var0 << 4)) * var0;
   }
}
