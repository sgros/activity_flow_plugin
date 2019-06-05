package com.airbnb.lottie.model.content;

import android.graphics.Path.FillType;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.GradientFillContent;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.layer.BaseLayer;

public class GradientFill implements ContentModel {
   private final AnimatablePointValue endPoint;
   private final FillType fillType;
   private final AnimatableGradientColorValue gradientColor;
   private final GradientType gradientType;
   private final AnimatableFloatValue highlightAngle;
   private final AnimatableFloatValue highlightLength;
   private final String name;
   private final AnimatableIntegerValue opacity;
   private final AnimatablePointValue startPoint;

   public GradientFill(String var1, GradientType var2, FillType var3, AnimatableGradientColorValue var4, AnimatableIntegerValue var5, AnimatablePointValue var6, AnimatablePointValue var7, AnimatableFloatValue var8, AnimatableFloatValue var9) {
      this.gradientType = var2;
      this.fillType = var3;
      this.gradientColor = var4;
      this.opacity = var5;
      this.startPoint = var6;
      this.endPoint = var7;
      this.name = var1;
      this.highlightLength = var8;
      this.highlightAngle = var9;
   }

   public AnimatablePointValue getEndPoint() {
      return this.endPoint;
   }

   public FillType getFillType() {
      return this.fillType;
   }

   public AnimatableGradientColorValue getGradientColor() {
      return this.gradientColor;
   }

   public GradientType getGradientType() {
      return this.gradientType;
   }

   public String getName() {
      return this.name;
   }

   public AnimatableIntegerValue getOpacity() {
      return this.opacity;
   }

   public AnimatablePointValue getStartPoint() {
      return this.startPoint;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new GradientFillContent(var1, var2, this);
   }
}
