package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

@Deprecated
public final class MonochromeRectangleDetector {
   private static final int MAX_MODULES = 32;
   private final BitMatrix image;

   public MonochromeRectangleDetector(BitMatrix var1) {
      this.image = var1;
   }

   private int[] blackWhiteRange(int var1, int var2, int var3, int var4, boolean var5) {
      int var6 = (var3 + var4) / 2;
      int var7 = var6;

      int var8;
      int var9;
      while(true) {
         var8 = var7;
         var7 = var7;
         if (var8 < var3) {
            break;
         }

         label86: {
            if (var5) {
               if (!this.image.get(var8, var1)) {
                  break label86;
               }
            } else if (!this.image.get(var1, var8)) {
               break label86;
            }

            var7 = var8 - 1;
            continue;
         }

         var7 = var8;

         while(true) {
            var9 = var7 - 1;
            if (var9 < var3) {
               break;
            }

            if (var5) {
               var7 = var9;
               if (this.image.get(var9, var1)) {
                  break;
               }
            } else {
               var7 = var9;
               if (this.image.get(var1, var9)) {
                  break;
               }
            }
         }

         if (var9 >= var3) {
            var7 = var9;
            if (var8 - var9 <= var2) {
               continue;
            }
         }

         var7 = var8;
         break;
      }

      var9 = var7 + 1;
      var8 = var6;

      while(true) {
         var3 = var8;
         var8 = var8;
         if (var3 >= var4) {
            break;
         }

         label96: {
            if (var5) {
               if (this.image.get(var3, var1)) {
                  break label96;
               }
            } else if (this.image.get(var1, var3)) {
               break label96;
            }

            var8 = var3;

            while(true) {
               var7 = var8 + 1;
               if (var7 >= var4) {
                  break;
               }

               if (var5) {
                  var8 = var7;
                  if (this.image.get(var7, var1)) {
                     break;
                  }
               } else {
                  var8 = var7;
                  if (this.image.get(var1, var7)) {
                     break;
                  }
               }
            }

            if (var7 < var4) {
               var8 = var7;
               if (var7 - var3 <= var2) {
                  continue;
               }
            }

            var8 = var3;
            break;
         }

         var8 = var3 + 1;
      }

      var1 = var8 - 1;
      int[] var10;
      if (var1 > var9) {
         var10 = new int[]{var9, var1};
      } else {
         var10 = null;
      }

      return var10;
   }

   private ResultPoint findCornerFromCenter(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) throws NotFoundException {
      int[] var10 = null;
      int var11 = var5;

      int[] var13;
      for(int var12 = var1; var11 < var8 && var11 >= var7 && var12 < var4 && var12 >= var3; var10 = var13) {
         if (var2 == 0) {
            var13 = this.blackWhiteRange(var11, var9, var3, var4, true);
         } else {
            var13 = this.blackWhiteRange(var12, var9, var7, var8, false);
         }

         if (var13 == null) {
            if (var10 == null) {
               throw NotFoundException.getNotFoundInstance();
            }

            byte var15;
            ResultPoint var16;
            if (var2 == 0) {
               var2 = var11 - var6;
               if (var10[0] < var1) {
                  if (var10[1] > var1) {
                     if (var6 > 0) {
                        var15 = 0;
                     } else {
                        var15 = 1;
                     }

                     var16 = new ResultPoint((float)var10[var15], (float)var2);
                  } else {
                     var16 = new ResultPoint((float)var10[0], (float)var2);
                  }
               } else {
                  var16 = new ResultPoint((float)var10[1], (float)var2);
               }
            } else {
               var1 = var12 - var2;
               if (var10[0] < var5) {
                  if (var10[1] > var5) {
                     float var14 = (float)var1;
                     if (var2 < 0) {
                        var15 = 0;
                     } else {
                        var15 = 1;
                     }

                     var16 = new ResultPoint(var14, (float)var10[var15]);
                  } else {
                     var16 = new ResultPoint((float)var1, (float)var10[0]);
                  }
               } else {
                  var16 = new ResultPoint((float)var1, (float)var10[1]);
               }
            }

            return var16;
         }

         var11 += var6;
         var12 += var2;
      }

      throw NotFoundException.getNotFoundInstance();
   }

   public ResultPoint[] detect() throws NotFoundException {
      int var1 = this.image.getHeight();
      int var2 = this.image.getWidth();
      int var3 = var1 / 2;
      int var4 = var2 / 2;
      int var5 = Math.max(1, var1 / 256);
      int var6 = Math.max(1, var2 / 256);
      int var7 = (int)this.findCornerFromCenter(var4, 0, 0, var2, var3, -var5, 0, var1, var4 / 2).getY() - 1;
      ResultPoint var8 = this.findCornerFromCenter(var4, -var6, 0, var2, var3, 0, var7, var1, var3 / 2);
      int var9 = (int)var8.getX() - 1;
      ResultPoint var10 = this.findCornerFromCenter(var4, var6, var9, var2, var3, 0, var7, var1, var3 / 2);
      var2 = (int)var10.getX() + 1;
      ResultPoint var11 = this.findCornerFromCenter(var4, 0, var9, var2, var3, var5, var7, var1, var4 / 2);
      var1 = (int)var11.getY();
      return new ResultPoint[]{this.findCornerFromCenter(var4, 0, var9, var2, var3, -var5, var7, var1 + 1, var4 / 4), var8, var10, var11};
   }
}
