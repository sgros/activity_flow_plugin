package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.highlight.Range;

@SuppressLint({"ParcelCreator"})
public class BarEntry extends Entry {
   private float mNegativeSum;
   private float mPositiveSum;
   private Range[] mRanges;
   private float[] mYVals;

   public BarEntry(float var1, float var2) {
      super(var1, var2);
   }

   public BarEntry(float var1, float var2, Drawable var3) {
      super(var1, var2, var3);
   }

   public BarEntry(float var1, float var2, Drawable var3, Object var4) {
      super(var1, var2, var3, var4);
   }

   public BarEntry(float var1, float var2, Object var3) {
      super(var1, var2, var3);
   }

   public BarEntry(float var1, float[] var2) {
      super(var1, calcSum(var2));
      this.mYVals = var2;
      this.calcPosNegSum();
      this.calcRanges();
   }

   public BarEntry(float var1, float[] var2, Drawable var3) {
      super(var1, calcSum(var2), var3);
      this.mYVals = var2;
      this.calcPosNegSum();
      this.calcRanges();
   }

   public BarEntry(float var1, float[] var2, Drawable var3, Object var4) {
      super(var1, calcSum(var2), var3, var4);
      this.mYVals = var2;
      this.calcPosNegSum();
      this.calcRanges();
   }

   public BarEntry(float var1, float[] var2, Object var3) {
      super(var1, calcSum(var2), var3);
      this.mYVals = var2;
      this.calcPosNegSum();
      this.calcRanges();
   }

   private void calcPosNegSum() {
      if (this.mYVals == null) {
         this.mNegativeSum = 0.0F;
         this.mPositiveSum = 0.0F;
      } else {
         float[] var1 = this.mYVals;
         int var2 = var1.length;
         int var3 = 0;
         float var4 = 0.0F;

         float var5;
         for(var5 = var4; var3 < var2; ++var3) {
            float var6 = var1[var3];
            if (var6 <= 0.0F) {
               var4 += Math.abs(var6);
            } else {
               var5 += var6;
            }
         }

         this.mNegativeSum = var4;
         this.mPositiveSum = var5;
      }
   }

   private static float calcSum(float[] var0) {
      float var1 = 0.0F;
      if (var0 == null) {
         return 0.0F;
      } else {
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            var1 += var0[var3];
         }

         return var1;
      }
   }

   protected void calcRanges() {
      float[] var1 = this.getYVals();
      if (var1 != null && var1.length != 0) {
         this.mRanges = new Range[var1.length];
         float var2 = -this.getNegativeSum();
         int var3 = 0;

         for(float var4 = 0.0F; var3 < this.mRanges.length; ++var3) {
            float var5 = var1[var3];
            Range[] var6;
            if (var5 < 0.0F) {
               var6 = this.mRanges;
               var5 = var2 - var5;
               var6[var3] = new Range(var2, var5);
               var2 = var5;
            } else {
               var6 = this.mRanges;
               var5 += var4;
               var6[var3] = new Range(var4, var5);
               var4 = var5;
            }
         }

      }
   }

   public BarEntry copy() {
      BarEntry var1 = new BarEntry(this.getX(), this.getY(), this.getData());
      var1.setVals(this.mYVals);
      return var1;
   }

   @Deprecated
   public float getBelowSum(int var1) {
      return this.getSumBelow(var1);
   }

   public float getNegativeSum() {
      return this.mNegativeSum;
   }

   public float getPositiveSum() {
      return this.mPositiveSum;
   }

   public Range[] getRanges() {
      return this.mRanges;
   }

   public float getSumBelow(int var1) {
      float[] var2 = this.mYVals;
      float var3 = 0.0F;
      if (var2 == null) {
         return 0.0F;
      } else {
         for(int var4 = this.mYVals.length - 1; var4 > var1 && var4 >= 0; --var4) {
            var3 += this.mYVals[var4];
         }

         return var3;
      }
   }

   public float getY() {
      return super.getY();
   }

   public float[] getYVals() {
      return this.mYVals;
   }

   public boolean isStacked() {
      boolean var1;
      if (this.mYVals != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setVals(float[] var1) {
      this.setY(calcSum(var1));
      this.mYVals = var1;
      this.calcPosNegSum();
      this.calcRanges();
   }
}
