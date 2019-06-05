package com.github.mikephil.charting.components;

import android.graphics.Typeface;
import com.github.mikephil.charting.utils.Utils;

public abstract class ComponentBase {
   protected boolean mEnabled = true;
   protected int mTextColor = -16777216;
   protected float mTextSize = Utils.convertDpToPixel(10.0F);
   protected Typeface mTypeface = null;
   protected float mXOffset = 5.0F;
   protected float mYOffset = 5.0F;

   public int getTextColor() {
      return this.mTextColor;
   }

   public float getTextSize() {
      return this.mTextSize;
   }

   public Typeface getTypeface() {
      return this.mTypeface;
   }

   public float getXOffset() {
      return this.mXOffset;
   }

   public float getYOffset() {
      return this.mYOffset;
   }

   public boolean isEnabled() {
      return this.mEnabled;
   }

   public void setEnabled(boolean var1) {
      this.mEnabled = var1;
   }

   public void setTextColor(int var1) {
      this.mTextColor = var1;
   }

   public void setTextSize(float var1) {
      float var2 = var1;
      if (var1 > 24.0F) {
         var2 = 24.0F;
      }

      var1 = var2;
      if (var2 < 6.0F) {
         var1 = 6.0F;
      }

      this.mTextSize = Utils.convertDpToPixel(var1);
   }

   public void setTypeface(Typeface var1) {
      this.mTypeface = var1;
   }

   public void setXOffset(float var1) {
      this.mXOffset = Utils.convertDpToPixel(var1);
   }

   public void setYOffset(float var1) {
      this.mYOffset = Utils.convertDpToPixel(var1);
   }
}
