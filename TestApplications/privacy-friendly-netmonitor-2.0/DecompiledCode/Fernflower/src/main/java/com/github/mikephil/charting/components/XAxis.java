package com.github.mikephil.charting.components;

import com.github.mikephil.charting.utils.Utils;

public class XAxis extends AxisBase {
   private boolean mAvoidFirstLastClipping = false;
   public int mLabelHeight = 1;
   public int mLabelRotatedHeight = 1;
   public int mLabelRotatedWidth = 1;
   protected float mLabelRotationAngle = 0.0F;
   public int mLabelWidth = 1;
   private XAxis.XAxisPosition mPosition;

   public XAxis() {
      this.mPosition = XAxis.XAxisPosition.TOP;
      this.mYOffset = Utils.convertDpToPixel(4.0F);
   }

   public float getLabelRotationAngle() {
      return this.mLabelRotationAngle;
   }

   public XAxis.XAxisPosition getPosition() {
      return this.mPosition;
   }

   public boolean isAvoidFirstLastClippingEnabled() {
      return this.mAvoidFirstLastClipping;
   }

   public void setAvoidFirstLastClipping(boolean var1) {
      this.mAvoidFirstLastClipping = var1;
   }

   public void setLabelRotationAngle(float var1) {
      this.mLabelRotationAngle = var1;
   }

   public void setPosition(XAxis.XAxisPosition var1) {
      this.mPosition = var1;
   }

   public static enum XAxisPosition {
      BOTH_SIDED,
      BOTTOM,
      BOTTOM_INSIDE,
      TOP,
      TOP_INSIDE;
   }
}
