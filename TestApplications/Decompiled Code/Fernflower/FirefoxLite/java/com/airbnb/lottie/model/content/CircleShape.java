package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.EllipseContent;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class CircleShape implements ContentModel {
   private final boolean isReversed;
   private final String name;
   private final AnimatableValue position;
   private final AnimatablePointValue size;

   public CircleShape(String var1, AnimatableValue var2, AnimatablePointValue var3, boolean var4) {
      this.name = var1;
      this.position = var2;
      this.size = var3;
      this.isReversed = var4;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableValue getPosition() {
      return this.position;
   }

   public AnimatablePointValue getSize() {
      return this.size;
   }

   public boolean isReversed() {
      return this.isReversed;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new EllipseContent(var1, var2, this);
   }
}
