package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.RectangleContent;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class RectangleShape implements ContentModel {
   private final AnimatableFloatValue cornerRadius;
   private final String name;
   private final AnimatableValue position;
   private final AnimatablePointValue size;

   public RectangleShape(String var1, AnimatableValue var2, AnimatablePointValue var3, AnimatableFloatValue var4) {
      this.name = var1;
      this.position = var2;
      this.size = var3;
      this.cornerRadius = var4;
   }

   public AnimatableFloatValue getCornerRadius() {
      return this.cornerRadius;
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

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new RectangleContent(var1, var2, this);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("RectangleShape{position=");
      var1.append(this.position);
      var1.append(", size=");
      var1.append(this.size);
      var1.append('}');
      return var1.toString();
   }
}
