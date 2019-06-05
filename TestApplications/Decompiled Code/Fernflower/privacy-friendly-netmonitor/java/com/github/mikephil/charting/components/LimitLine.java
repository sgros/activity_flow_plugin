package com.github.mikephil.charting.components;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint.Style;
import com.github.mikephil.charting.utils.Utils;

public class LimitLine extends ComponentBase {
   private DashPathEffect mDashPathEffect;
   private String mLabel;
   private LimitLine.LimitLabelPosition mLabelPosition;
   private float mLimit = 0.0F;
   private int mLineColor = Color.rgb(237, 91, 91);
   private float mLineWidth = 2.0F;
   private Style mTextStyle;

   public LimitLine(float var1) {
      this.mTextStyle = Style.FILL_AND_STROKE;
      this.mLabel = "";
      this.mDashPathEffect = null;
      this.mLabelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP;
      this.mLimit = var1;
   }

   public LimitLine(float var1, String var2) {
      this.mTextStyle = Style.FILL_AND_STROKE;
      this.mLabel = "";
      this.mDashPathEffect = null;
      this.mLabelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP;
      this.mLimit = var1;
      this.mLabel = var2;
   }

   public void disableDashedLine() {
      this.mDashPathEffect = null;
   }

   public void enableDashedLine(float var1, float var2, float var3) {
      this.mDashPathEffect = new DashPathEffect(new float[]{var1, var2}, var3);
   }

   public DashPathEffect getDashPathEffect() {
      return this.mDashPathEffect;
   }

   public String getLabel() {
      return this.mLabel;
   }

   public LimitLine.LimitLabelPosition getLabelPosition() {
      return this.mLabelPosition;
   }

   public float getLimit() {
      return this.mLimit;
   }

   public int getLineColor() {
      return this.mLineColor;
   }

   public float getLineWidth() {
      return this.mLineWidth;
   }

   public Style getTextStyle() {
      return this.mTextStyle;
   }

   public boolean isDashedLineEnabled() {
      boolean var1;
      if (this.mDashPathEffect == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void setLabel(String var1) {
      this.mLabel = var1;
   }

   public void setLabelPosition(LimitLine.LimitLabelPosition var1) {
      this.mLabelPosition = var1;
   }

   public void setLineColor(int var1) {
      this.mLineColor = var1;
   }

   public void setLineWidth(float var1) {
      float var2 = var1;
      if (var1 < 0.2F) {
         var2 = 0.2F;
      }

      var1 = var2;
      if (var2 > 12.0F) {
         var1 = 12.0F;
      }

      this.mLineWidth = Utils.convertDpToPixel(var1);
   }

   public void setTextStyle(Style var1) {
      this.mTextStyle = var1;
   }

   public static enum LimitLabelPosition {
      LEFT_BOTTOM,
      LEFT_TOP,
      RIGHT_BOTTOM,
      RIGHT_TOP;
   }
}
