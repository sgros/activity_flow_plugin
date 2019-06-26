package com.google.zxing.oned;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public abstract class OneDReader implements Reader {
   private Result doDecode(BinaryBitmap var1, Map var2) throws NotFoundException {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      BitArray var5 = new BitArray(var3);
      boolean var6;
      if (var2 != null && ((Map)var2).containsKey(DecodeHintType.TRY_HARDER)) {
         var6 = true;
      } else {
         var6 = false;
      }

      byte var7;
      if (var6) {
         var7 = 8;
      } else {
         var7 = 5;
      }

      int var8 = Math.max(1, var4 >> var7);
      int var20;
      if (var6) {
         var20 = var4;
      } else {
         var20 = 15;
      }

      int var21 = 0;

      while(true) {
         if (var21 < var20) {
            int var9 = (var21 + 1) / 2;
            boolean var10;
            if ((var21 & 1) == 0) {
               var10 = true;
            } else {
               var10 = false;
            }

            int var22;
            if (var10) {
               var22 = var9;
            } else {
               var22 = -var9;
            }

            var9 = (var4 >> 1) + var8 * var22;
            if (var9 >= 0 && var9 < var4) {
               Object var11;
               BitArray var12;
               label110: {
                  BitArray var23;
                  try {
                     var23 = var1.getBlackRow(var9, var5);
                  } catch (NotFoundException var14) {
                     var11 = var2;
                     var12 = var5;
                     break label110;
                  }

                  var5 = var23;
                  var22 = 0;

                  Result var13;
                  while(true) {
                     var12 = var5;
                     var11 = var2;
                     if (var22 >= 2) {
                        break label110;
                     }

                     var11 = var2;
                     if (var22 == 1) {
                        var5.reverse();
                        var11 = var2;
                        if (var2 != null) {
                           var11 = var2;
                           if (((Map)var2).containsKey(DecodeHintType.NEED_RESULT_POINT_CALLBACK)) {
                              var11 = new EnumMap(DecodeHintType.class);
                              ((Map)var11).putAll((Map)var2);
                              ((Map)var11).remove(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
                           }
                        }
                     }

                     label112: {
                        boolean var10001;
                        try {
                           var13 = this.decodeRow(var9, var5, (Map)var11);
                        } catch (ReaderException var18) {
                           var10001 = false;
                           break label112;
                        }

                        if (var22 != 1) {
                           break;
                        }

                        ResultPoint[] var24;
                        try {
                           var13.putMetadata(ResultMetadataType.ORIENTATION, 180);
                           var24 = var13.getResultPoints();
                        } catch (ReaderException var17) {
                           var10001 = false;
                           break label112;
                        }

                        if (var24 == null) {
                           break;
                        }

                        ResultPoint var19;
                        try {
                           var19 = new ResultPoint((float)var3 - var24[0].getX() - 1.0F, var24[0].getY());
                        } catch (ReaderException var16) {
                           var10001 = false;
                           break label112;
                        }

                        var24[0] = var19;

                        try {
                           var24[1] = new ResultPoint((float)var3 - var24[1].getX() - 1.0F, var24[1].getY());
                           break;
                        } catch (ReaderException var15) {
                           var10001 = false;
                        }
                     }

                     ++var22;
                     var2 = var11;
                  }

                  return var13;
               }

               ++var21;
               var5 = var12;
               var2 = var11;
               continue;
            }
         }

         throw NotFoundException.getNotFoundInstance();
      }
   }

   protected static float patternMatchVariance(int[] var0, int[] var1, float var2) {
      float var3 = Float.POSITIVE_INFINITY;
      int var4 = var0.length;
      int var5 = 0;
      int var6 = 0;

      int var7;
      for(var7 = 0; var7 < var4; ++var7) {
         var5 += var0[var7];
         var6 += var1[var7];
      }

      float var8;
      if (var5 < var6) {
         var8 = var3;
      } else {
         float var9 = (float)var5 / (float)var6;
         float var10 = 0.0F;

         for(var7 = 0; var7 < var4; ++var7) {
            var6 = var0[var7];
            var8 = (float)var1[var7] * var9;
            float var11;
            if ((float)var6 > var8) {
               var11 = (float)var6 - var8;
            } else {
               var11 = var8 - (float)var6;
            }

            var8 = var3;
            if (var11 > var2 * var9) {
               return var8;
            }

            var10 += var11;
         }

         var8 = var10 / (float)var5;
      }

      return var8;
   }

   protected static void recordPattern(BitArray var0, int var1, int[] var2) throws NotFoundException {
      int var3 = var2.length;
      Arrays.fill(var2, 0, var3, 0);
      int var4 = var0.getSize();
      if (var1 >= var4) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         boolean var5;
         if (!var0.get(var1)) {
            var5 = true;
         } else {
            var5 = false;
         }

         byte var6 = 0;
         boolean var7 = var5;
         int var10 = var1;
         var1 = var6;

         int var8;
         while(true) {
            var8 = var1;
            if (var10 >= var4) {
               break;
            }

            int var11;
            if (var0.get(var10) ^ var7) {
               int var10002 = var2[var1]++;
               var11 = var1;
            } else {
               var11 = var1 + 1;
               var8 = var11;
               if (var11 == var3) {
                  break;
               }

               var2[var11] = 1;
               boolean var9;
               if (!var7) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               var7 = var9;
            }

            ++var10;
            var1 = var11;
         }

         if (var8 != var3 && (var8 != var3 - 1 || var10 != var4)) {
            throw NotFoundException.getNotFoundInstance();
         }
      }
   }

   protected static void recordPatternInReverse(BitArray var0, int var1, int[] var2) throws NotFoundException {
      int var3 = var2.length;
      boolean var4 = var0.get(var1);

      while(var1 > 0 && var3 >= 0) {
         int var5 = var1 - 1;
         var1 = var5;
         if (var0.get(var5) != var4) {
            --var3;
            if (!var4) {
               var4 = true;
            } else {
               var4 = false;
            }

            var1 = var5;
         }
      }

      if (var3 >= 0) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         recordPattern(var0, var1 + 1, var2);
      }
   }

   public Result decode(BinaryBitmap var1) throws NotFoundException, FormatException {
      return this.decode(var1, (Map)null);
   }

   public Result decode(BinaryBitmap var1, Map var2) throws NotFoundException, FormatException {
      Result var9;
      Result var11;
      try {
         var11 = this.doDecode(var1, var2);
      } catch (NotFoundException var7) {
         boolean var4;
         if (var2 != null && var2.containsKey(DecodeHintType.TRY_HARDER)) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (var4 && var1.isRotateSupported()) {
            BinaryBitmap var3 = var1.rotateCounterClockwise();
            Result var10 = this.doDecode(var3, var2);
            Map var8 = var10.getResultMetadata();
            short var5 = 270;
            int var12 = var5;
            if (var8 != null) {
               var12 = var5;
               if (var8.containsKey(ResultMetadataType.ORIENTATION)) {
                  var12 = ((Integer)var8.get(ResultMetadataType.ORIENTATION) + 270) % 360;
               }
            }

            var10.putMetadata(ResultMetadataType.ORIENTATION, var12);
            ResultPoint[] var6 = var10.getResultPoints();
            var9 = var10;
            if (var6 != null) {
               int var13 = var3.getHeight();
               var12 = 0;

               while(true) {
                  var9 = var10;
                  if (var12 >= var6.length) {
                     return var9;
                  }

                  var6[var12] = new ResultPoint((float)var13 - var6[var12].getY() - 1.0F, var6[var12].getX());
                  ++var12;
               }
            }

            return var9;
         }

         throw var7;
      }

      var9 = var11;
      return var9;
   }

   public abstract Result decodeRow(int var1, BitArray var2, Map var3) throws NotFoundException, ChecksumException, FormatException;

   public void reset() {
   }
}
