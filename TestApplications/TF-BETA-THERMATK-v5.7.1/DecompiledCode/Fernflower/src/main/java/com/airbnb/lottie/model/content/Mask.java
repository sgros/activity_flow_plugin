package com.airbnb.lottie.model.content;

import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;

public class Mask {
   private final boolean inverted;
   private final Mask.MaskMode maskMode;
   private final AnimatableShapeValue maskPath;
   private final AnimatableIntegerValue opacity;

   public Mask(Mask.MaskMode var1, AnimatableShapeValue var2, AnimatableIntegerValue var3, boolean var4) {
      this.maskMode = var1;
      this.maskPath = var2;
      this.opacity = var3;
      this.inverted = var4;
   }

   public Mask.MaskMode getMaskMode() {
      return this.maskMode;
   }

   public AnimatableShapeValue getMaskPath() {
      return this.maskPath;
   }

   public AnimatableIntegerValue getOpacity() {
      return this.opacity;
   }

   public boolean isInverted() {
      return this.inverted;
   }

   public static enum MaskMode {
      MASK_MODE_ADD,
      MASK_MODE_INTERSECT,
      MASK_MODE_SUBTRACT;
   }
}
