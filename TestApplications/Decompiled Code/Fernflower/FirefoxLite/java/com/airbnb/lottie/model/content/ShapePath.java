package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.ShapeContent;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class ShapePath implements ContentModel {
   private final int index;
   private final String name;
   private final AnimatableShapeValue shapePath;

   public ShapePath(String var1, int var2, AnimatableShapeValue var3) {
      this.name = var1;
      this.index = var2;
      this.shapePath = var3;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableShapeValue getShapePath() {
      return this.shapePath;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new ShapeContent(var1, var2, this);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ShapePath{name=");
      var1.append(this.name);
      var1.append(", index=");
      var1.append(this.index);
      var1.append('}');
      return var1.toString();
   }
}
