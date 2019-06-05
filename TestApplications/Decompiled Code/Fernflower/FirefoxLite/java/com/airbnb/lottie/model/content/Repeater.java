package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.RepeaterContent;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.BaseLayer;

public class Repeater implements ContentModel {
   private final AnimatableFloatValue copies;
   private final String name;
   private final AnimatableFloatValue offset;
   private final AnimatableTransform transform;

   public Repeater(String var1, AnimatableFloatValue var2, AnimatableFloatValue var3, AnimatableTransform var4) {
      this.name = var1;
      this.copies = var2;
      this.offset = var3;
      this.transform = var4;
   }

   public AnimatableFloatValue getCopies() {
      return this.copies;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableFloatValue getOffset() {
      return this.offset;
   }

   public AnimatableTransform getTransform() {
      return this.transform;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new RepeaterContent(var1, var2, this);
   }
}
