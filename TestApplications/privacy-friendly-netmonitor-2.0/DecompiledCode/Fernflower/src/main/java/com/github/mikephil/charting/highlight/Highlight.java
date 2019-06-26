package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.components.YAxis;

public class Highlight {
   private YAxis.AxisDependency axis;
   private int mDataIndex;
   private int mDataSetIndex;
   private float mDrawX;
   private float mDrawY;
   private int mStackIndex;
   private float mX;
   private float mXPx;
   private float mY;
   private float mYPx;

   public Highlight(float var1, float var2, float var3, float var4, int var5, int var6, YAxis.AxisDependency var7) {
      this(var1, var2, var3, var4, var5, var7);
      this.mStackIndex = var6;
   }

   public Highlight(float var1, float var2, float var3, float var4, int var5, YAxis.AxisDependency var6) {
      this.mX = Float.NaN;
      this.mY = Float.NaN;
      this.mDataIndex = -1;
      this.mStackIndex = -1;
      this.mX = var1;
      this.mY = var2;
      this.mXPx = var3;
      this.mYPx = var4;
      this.mDataSetIndex = var5;
      this.axis = var6;
   }

   public Highlight(float var1, float var2, int var3) {
      this.mX = Float.NaN;
      this.mY = Float.NaN;
      this.mDataIndex = -1;
      this.mStackIndex = -1;
      this.mX = var1;
      this.mY = var2;
      this.mDataSetIndex = var3;
   }

   public Highlight(float var1, int var2, int var3) {
      this(var1, Float.NaN, var2);
      this.mStackIndex = var3;
   }

   public boolean equalTo(Highlight var1) {
      if (var1 == null) {
         return false;
      } else {
         return this.mDataSetIndex == var1.mDataSetIndex && this.mX == var1.mX && this.mStackIndex == var1.mStackIndex && this.mDataIndex == var1.mDataIndex;
      }
   }

   public YAxis.AxisDependency getAxis() {
      return this.axis;
   }

   public int getDataIndex() {
      return this.mDataIndex;
   }

   public int getDataSetIndex() {
      return this.mDataSetIndex;
   }

   public float getDrawX() {
      return this.mDrawX;
   }

   public float getDrawY() {
      return this.mDrawY;
   }

   public int getStackIndex() {
      return this.mStackIndex;
   }

   public float getX() {
      return this.mX;
   }

   public float getXPx() {
      return this.mXPx;
   }

   public float getY() {
      return this.mY;
   }

   public float getYPx() {
      return this.mYPx;
   }

   public boolean isStacked() {
      boolean var1;
      if (this.mStackIndex >= 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setDataIndex(int var1) {
      this.mDataIndex = var1;
   }

   public void setDraw(float var1, float var2) {
      this.mDrawX = var1;
      this.mDrawY = var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Highlight, x: ");
      var1.append(this.mX);
      var1.append(", y: ");
      var1.append(this.mY);
      var1.append(", dataSetIndex: ");
      var1.append(this.mDataSetIndex);
      var1.append(", stackIndex (only stacked barentry): ");
      var1.append(this.mStackIndex);
      return var1.toString();
   }
}
