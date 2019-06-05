package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

public class BarBuffer extends AbstractBuffer {
   protected float mBarWidth = 1.0F;
   protected boolean mContainsStacks = false;
   protected int mDataSetCount = 1;
   protected int mDataSetIndex = 0;
   protected boolean mInverted = false;

   public BarBuffer(int var1, int var2, boolean var3) {
      super(var1);
      this.mDataSetCount = var2;
      this.mContainsStacks = var3;
   }

   protected void addBar(float var1, float var2, float var3, float var4) {
      float[] var5 = this.buffer;
      int var6 = this.index++;
      var5[var6] = var1;
      var5 = this.buffer;
      var6 = this.index++;
      var5[var6] = var2;
      var5 = this.buffer;
      var6 = this.index++;
      var5[var6] = var3;
      var5 = this.buffer;
      var6 = this.index++;
      var5[var6] = var4;
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

               float var15;
               for(int var11 = 0; var11 < var9.length; var8 = var15) {
                  var12 = var9[var11];
                  float var14;
                  if (var12 == 0.0F && (var10 == 0.0F || var8 == 0.0F)) {
                     var14 = var10;
                     var15 = var8;
                     var8 = var12;
                     var12 = var12;
                  } else if (var12 >= 0.0F) {
                     var12 += var10;
                     var14 = var12;
                     var15 = var8;
                     var8 = var10;
                  } else {
                     var15 = Math.abs(var12) + var8;
                     var12 = Math.abs(var12);
                     float var13 = var12 + var8;
                     var12 = var15;
                     var8 = var8;
                     var15 = var13;
                     var14 = var10;
                  }

                  if (this.mInverted) {
                     if (var8 >= var12) {
                        var10 = var8;
                     } else {
                        var10 = var12;
                     }

                     if (var8 > var12) {
                        var8 = var12;
                     }
                  } else {
                     if (var8 >= var12) {
                        var10 = var8;
                     } else {
                        var10 = var12;
                     }

                     if (var8 > var12) {
                        var8 = var12;
                     }

                     var12 = var10;
                     var10 = var8;
                     var8 = var12;
                  }

                  this.addBar(var7 - var4, var8 * this.phaseY, var7 + var4, var10 * this.phaseY);
                  ++var11;
                  var10 = var14;
               }
            } else {
               if (this.mInverted) {
                  if (var8 >= 0.0F) {
                     var12 = var8;
                  } else {
                     var12 = 0.0F;
                  }

                  if (var8 > 0.0F) {
                     var8 = 0.0F;
                  }

                  var10 = var12;
                  var12 = var8;
                  var8 = var10;
               } else {
                  if (var8 >= 0.0F) {
                     var12 = var8;
                  } else {
                     var12 = 0.0F;
                  }

                  if (var8 > 0.0F) {
                     var8 = 0.0F;
                  }
               }

               if (var12 > 0.0F) {
                  var12 *= this.phaseY;
               } else {
                  var8 *= this.phaseY;
               }

               this.addBar(var7 - var4, var12, var7 + var4, var8);
            }
         }
      }

      this.reset();
   }

   public void setBarWidth(float var1) {
      this.mBarWidth = var1;
   }

   public void setDataSet(int var1) {
      this.mDataSetIndex = var1;
   }

   public void setInverted(boolean var1) {
      this.mInverted = var1;
   }
}
