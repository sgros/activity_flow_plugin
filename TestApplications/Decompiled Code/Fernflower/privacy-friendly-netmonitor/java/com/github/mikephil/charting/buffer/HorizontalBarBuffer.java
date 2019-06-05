package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class HorizontalBarBuffer extends BarBuffer {
   public HorizontalBarBuffer(int var1, int var2, boolean var3) {
      super(var1, var2, var3);
   }

   public void feed(IBarDataSet var1) {
      float var2 = (float)var1.getEntryCount();
      float var3 = this.phaseX;
      float var4 = this.mBarWidth / 2.0F;

      for(int var5 = 0; (float)var5 < var2 * var3; ++var5) {
         BarEntry var6 = (BarEntry)var1.getEntryForIndex(var5);
         if (var6 != null) {
            float var7 = var6.getX();
            float var8 = var6.getY();
            float[] var9 = var6.getYVals();
            float var10;
            float var12;
            if (this.mContainsStacks && var9 != null) {
               var8 = -var6.getNegativeSum();
               var10 = 0.0F;

               for(int var11 = 0; var11 < var9.length; var8 = var12) {
                  var12 = var9[var11];
                  float var13;
                  float var14;
                  if (var12 >= 0.0F) {
                     var12 += var10;
                     var13 = var8;
                     var14 = var12;
                     var8 = var12;
                     var12 = var13;
                  } else {
                     var13 = Math.abs(var12);
                     var12 = Math.abs(var12);
                     var14 = var10;
                     var10 = var8;
                     var12 += var8;
                     var8 += var13;
                  }

                  float var15;
                  float var16;
                  if (this.mInverted) {
                     if (var10 >= var8) {
                        var15 = var10;
                     } else {
                        var15 = var8;
                     }

                     var16 = var15;
                     var13 = var8;
                     if (var10 <= var8) {
                        var16 = var15;
                        var13 = var10;
                     }
                  } else {
                     if (var10 >= var8) {
                        var15 = var10;
                     } else {
                        var15 = var8;
                     }

                     var13 = var8;
                     if (var10 <= var8) {
                        var13 = var10;
                     }

                     var16 = var13;
                     var13 = var15;
                  }

                  var8 = this.phaseY;
                  this.addBar(var16 * this.phaseY, var7 + var4, var13 * var8, var7 - var4);
                  ++var11;
                  var10 = var14;
               }
            } else {
               if (this.mInverted) {
                  if (var8 >= 0.0F) {
                     var10 = var8;
                  } else {
                     var10 = 0.0F;
                  }

                  if (var8 > 0.0F) {
                     var8 = 0.0F;
                  }

                  var12 = var10;
                  var10 = var8;
                  var8 = var12;
               } else {
                  if (var8 >= 0.0F) {
                     var10 = var8;
                  } else {
                     var10 = 0.0F;
                  }

                  if (var8 > 0.0F) {
                     var8 = 0.0F;
                  }
               }

               if (var10 > 0.0F) {
                  var10 *= this.phaseY;
               } else {
                  var8 *= this.phaseY;
               }

               this.addBar(var8, var7 + var4, var10, var7 - var4);
            }
         }
      }

      this.reset();
   }
}
