package com.airbnb.lottie.model.content;

import android.graphics.Path.FillType;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.FillContent;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class ShapeFill implements ContentModel {
   private final AnimatableColorValue color;
   private final boolean fillEnabled;
   private final FillType fillType;
   private final String name;
   private final AnimatableIntegerValue opacity;

   public ShapeFill(String var1, boolean var2, FillType var3, AnimatableColorValue var4, AnimatableIntegerValue var5) {
      this.name = var1;
      this.fillEnabled = var2;
      this.fillType = var3;
      this.color = var4;
      this.opacity = var5;
   }

   public AnimatableColorValue getColor() {
      return this.color;
   }

   public FillType getFillType() {
      return this.fillType;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableIntegerValue getOpacity() {
      return this.opacity;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new FillContent(var1, var2, this);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ShapeFill{color=, fillEnabled=");
      var1.append(this.fillEnabled);
      var1.append('}');
      return var1.toString();
   }
}
