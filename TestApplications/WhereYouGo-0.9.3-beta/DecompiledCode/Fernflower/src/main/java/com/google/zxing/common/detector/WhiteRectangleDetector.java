package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

public final class WhiteRectangleDetector {
   private static final int CORR = 1;
   private static final int INIT_SIZE = 10;
   private final int downInit;
   private final int height;
   private final BitMatrix image;
   private final int leftInit;
   private final int rightInit;
   private final int upInit;
   private final int width;

   public WhiteRectangleDetector(BitMatrix var1) throws NotFoundException {
      this(var1, 10, var1.getWidth() / 2, var1.getHeight() / 2);
   }

   public WhiteRectangleDetector(BitMatrix var1, int var2, int var3, int var4) throws NotFoundException {
      this.image = var1;
      this.height = var1.getHeight();
      this.width = var1.getWidth();
      var2 /= 2;
      this.leftInit = var3 - var2;
      this.rightInit = var3 + var2;
      this.upInit = var4 - var2;
      this.downInit = var4 + var2;
      if (this.upInit < 0 || this.leftInit < 0 || this.downInit >= this.height || this.rightInit >= this.width) {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private ResultPoint[] centerEdges(ResultPoint var1, ResultPoint var2, ResultPoint var3, ResultPoint var4) {
      float var5 = var1.getX();
      float var6 = var1.getY();
      float var7 = var2.getX();
      float var8 = var2.getY();
      float var9 = var3.getX();
      float var10 = var3.getY();
      float var11 = var4.getX();
      float var12 = var4.getY();
      ResultPoint[] var13;
      if (var5 < (float)this.width / 2.0F) {
         var13 = new ResultPoint[]{new ResultPoint(var11 - 1.0F, 1.0F + var12), new ResultPoint(1.0F + var7, 1.0F + var8), new ResultPoint(var9 - 1.0F, var10 - 1.0F), new ResultPoint(1.0F + var5, var6 - 1.0F)};
      } else {
         var13 = new ResultPoint[]{new ResultPoint(1.0F + var11, 1.0F + var12), new ResultPoint(1.0F + var7, var8 - 1.0F), new ResultPoint(var9 - 1.0F, 1.0F + var10), new ResultPoint(var5 - 1.0F, var6 - 1.0F)};
      }

      return var13;
   }

   private boolean containsBlackPoint(int var1, int var2, int var3, boolean var4) {
      boolean var5 = true;
      if (var4) {
         while(var1 <= var2) {
            if (this.image.get(var1, var3)) {
               var4 = var5;
               return var4;
            }

            ++var1;
         }
      } else {
         while(var1 <= var2) {
            var4 = var5;
            if (this.image.get(var3, var1)) {
               return var4;
            }

            ++var1;
         }
      }

      var4 = false;
      return var4;
   }

   private ResultPoint getBlackPointOnSegment(float var1, float var2, float var3, float var4) {
      int var5 = MathUtils.round(MathUtils.distance(var1, var2, var3, var4));
      var3 = (var3 - var1) / (float)var5;
      var4 = (var4 - var2) / (float)var5;
      int var6 = 0;

      ResultPoint var9;
      while(true) {
         if (var6 >= var5) {
            var9 = null;
            break;
         }

         int var7 = MathUtils.round((float)var6 * var3 + var1);
         int var8 = MathUtils.round((float)var6 * var4 + var2);
         if (this.image.get(var7, var8)) {
            var9 = new ResultPoint((float)var7, (float)var8);
            break;
         }

         ++var6;
      }

      return var9;
   }

   public ResultPoint[] detect() throws NotFoundException {
      int var1 = this.leftInit;
      int var2 = this.rightInit;
      int var3 = this.upInit;
      int var4 = this.downInit;
      boolean var5 = false;
      boolean var6 = true;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      boolean var11 = false;

      int var12;
      int var13;
      int var14;
      boolean var15;
      int var16;
      int var30;
      while(true) {
         var12 = var4;
         var13 = var1;
         var14 = var2;
         var15 = var5;
         var16 = var3;
         if (!var6) {
            break;
         }

         boolean var29 = false;
         boolean var17 = true;
         var14 = var2;
         boolean var18 = var8;

         boolean var19;
         while((var17 || !var18) && var14 < this.width) {
            var19 = this.containsBlackPoint(var3, var4, var14, false);
            if (var19) {
               ++var14;
               var29 = true;
               var18 = true;
               var17 = var19;
            } else {
               var17 = var19;
               if (!var18) {
                  ++var14;
                  var17 = var19;
               }
            }
         }

         if (var14 >= this.width) {
            var15 = true;
            var16 = var3;
            var13 = var1;
            var12 = var4;
            break;
         }

         var17 = true;
         var30 = var4;
         boolean var21 = var9;
         boolean var31 = var29;

         while((var17 || !var21) && var30 < this.height) {
            var19 = this.containsBlackPoint(var1, var14, var30, true);
            if (var19) {
               ++var30;
               var31 = true;
               var21 = true;
               var17 = var19;
            } else {
               var17 = var19;
               if (!var21) {
                  ++var30;
                  var17 = var19;
               }
            }
         }

         if (var30 >= this.height) {
            var31 = true;
            var12 = var30;
            var13 = var1;
            var15 = var31;
            var16 = var3;
            break;
         }

         var17 = true;
         var13 = var1;
         boolean var22 = var10;
         boolean var28 = var31;

         while((var17 || !var22) && var13 >= 0) {
            var19 = this.containsBlackPoint(var3, var30, var13, false);
            if (var19) {
               --var13;
               var28 = true;
               var22 = true;
               var17 = var19;
            } else {
               var17 = var19;
               if (!var22) {
                  --var13;
                  var17 = var19;
               }
            }
         }

         if (var13 < 0) {
            var31 = true;
            var12 = var30;
            var15 = var31;
            var16 = var3;
            break;
         }

         var17 = true;
         var16 = var3;
         boolean var23 = var11;

         while((var17 || !var23) && var16 >= 0) {
            var19 = this.containsBlackPoint(var13, var14, var16, true);
            if (var19) {
               --var16;
               var28 = true;
               var23 = true;
               var17 = var19;
            } else {
               var17 = var19;
               if (!var23) {
                  --var16;
                  var17 = var19;
               }
            }
         }

         if (var16 < 0) {
            boolean var27 = true;
            var12 = var30;
            var15 = var27;
            break;
         }

         var6 = var28;
         var9 = var21;
         var10 = var22;
         var8 = var18;
         var11 = var23;
         var4 = var30;
         var1 = var13;
         var2 = var14;
         var3 = var16;
         if (var28) {
            var7 = true;
            var6 = var28;
            var9 = var21;
            var10 = var22;
            var8 = var18;
            var11 = var23;
            var4 = var30;
            var1 = var13;
            var2 = var14;
            var3 = var16;
         }
      }

      if (!var15 && var7) {
         var3 = var14 - var13;
         ResultPoint var20 = null;

         for(var30 = 1; var20 == null && var30 < var3; ++var30) {
            var20 = this.getBlackPointOnSegment((float)var13, (float)(var12 - var30), (float)(var13 + var30), (float)var12);
         }

         if (var20 == null) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            ResultPoint var24 = null;

            for(var30 = 1; var24 == null && var30 < var3; ++var30) {
               var24 = this.getBlackPointOnSegment((float)var13, (float)(var16 + var30), (float)(var13 + var30), (float)var16);
            }

            if (var24 == null) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               ResultPoint var25 = null;

               for(var30 = 1; var25 == null && var30 < var3; ++var30) {
                  var25 = this.getBlackPointOnSegment((float)var14, (float)(var16 + var30), (float)(var14 - var30), (float)var16);
               }

               if (var25 == null) {
                  throw NotFoundException.getNotFoundInstance();
               } else {
                  ResultPoint var26 = null;

                  for(var30 = 1; var26 == null && var30 < var3; ++var30) {
                     var26 = this.getBlackPointOnSegment((float)var14, (float)(var12 - var30), (float)(var14 - var30), (float)var12);
                  }

                  if (var26 == null) {
                     throw NotFoundException.getNotFoundInstance();
                  } else {
                     return this.centerEdges(var26, var20, var25, var24);
                  }
               }
            }
         }
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }
}
