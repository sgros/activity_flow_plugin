package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.Mask;
import java.util.ArrayList;
import java.util.List;

public class MaskKeyframeAnimation {
   private final List maskAnimations;
   private final List masks;
   private final List opacityAnimations;

   public MaskKeyframeAnimation(List var1) {
      this.masks = var1;
      this.maskAnimations = new ArrayList(var1.size());
      this.opacityAnimations = new ArrayList(var1.size());

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.maskAnimations.add(((Mask)var1.get(var2)).getMaskPath().createAnimation());
         AnimatableIntegerValue var3 = ((Mask)var1.get(var2)).getOpacity();
         this.opacityAnimations.add(var3.createAnimation());
      }

   }

   public List getMaskAnimations() {
      return this.maskAnimations;
   }

   public List getMasks() {
      return this.masks;
   }

   public List getOpacityAnimations() {
      return this.opacityAnimations;
   }
}
