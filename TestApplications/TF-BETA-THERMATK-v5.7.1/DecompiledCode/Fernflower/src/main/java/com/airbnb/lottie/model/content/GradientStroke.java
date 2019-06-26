package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.GradientStrokeContent;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.List;

public class GradientStroke implements ContentModel {
   private final ShapeStroke.LineCapType capType;
   private final AnimatableFloatValue dashOffset;
   private final AnimatablePointValue endPoint;
   private final AnimatableGradientColorValue gradientColor;
   private final GradientType gradientType;
   private final boolean hidden;
   private final ShapeStroke.LineJoinType joinType;
   private final List lineDashPattern;
   private final float miterLimit;
   private final String name;
   private final AnimatableIntegerValue opacity;
   private final AnimatablePointValue startPoint;
   private final AnimatableFloatValue width;

   public GradientStroke(String var1, GradientType var2, AnimatableGradientColorValue var3, AnimatableIntegerValue var4, AnimatablePointValue var5, AnimatablePointValue var6, AnimatableFloatValue var7, ShapeStroke.LineCapType var8, ShapeStroke.LineJoinType var9, float var10, List var11, AnimatableFloatValue var12, boolean var13) {
      this.name = var1;
      this.gradientType = var2;
      this.gradientColor = var3;
      this.opacity = var4;
      this.startPoint = var5;
      this.endPoint = var6;
      this.width = var7;
      this.capType = var8;
      this.joinType = var9;
      this.miterLimit = var10;
      this.lineDashPattern = var11;
      this.dashOffset = var12;
      this.hidden = var13;
   }

   public ShapeStroke.LineCapType getCapType() {
      return this.capType;
   }

   public AnimatableFloatValue getDashOffset() {
      return this.dashOffset;
   }

   public AnimatablePointValue getEndPoint() {
      return this.endPoint;
   }

   public AnimatableGradientColorValue getGradientColor() {
      return this.gradientColor;
   }

   public GradientType getGradientType() {
      return this.gradientType;
   }

   public ShapeStroke.LineJoinType getJoinType() {
      return this.joinType;
   }

   public List getLineDashPattern() {
      return this.lineDashPattern;
   }

   public float getMiterLimit() {
      return this.miterLimit;
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

   public AnimatableFloatValue getWidth() {
      return this.width;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return new GradientStrokeContent(var1, var2, this);
   }
}
