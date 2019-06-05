package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.layer.BaseLayer;

public class AnimatableTransform implements ContentModel {
   private final AnimatablePathValue anchorPoint;
   private final AnimatableFloatValue endOpacity;
   private final AnimatableIntegerValue opacity;
   private final AnimatableValue position;
   private final AnimatableFloatValue rotation;
   private final AnimatableScaleValue scale;
   private final AnimatableFloatValue startOpacity;

   public AnimatableTransform() {
      this(new AnimatablePathValue(), new AnimatablePathValue(), new AnimatableScaleValue(), new AnimatableFloatValue(), new AnimatableIntegerValue(), new AnimatableFloatValue(), new AnimatableFloatValue());
   }

   public AnimatableTransform(AnimatablePathValue var1, AnimatableValue var2, AnimatableScaleValue var3, AnimatableFloatValue var4, AnimatableIntegerValue var5, AnimatableFloatValue var6, AnimatableFloatValue var7) {
      this.anchorPoint = var1;
      this.position = var2;
      this.scale = var3;
      this.rotation = var4;
      this.opacity = var5;
      this.startOpacity = var6;
      this.endOpacity = var7;
   }

   public TransformKeyframeAnimation createAnimation() {
      return new TransformKeyframeAnimation(this);
   }

   public AnimatablePathValue getAnchorPoint() {
      return this.anchorPoint;
   }

   public AnimatableFloatValue getEndOpacity() {
      return this.endOpacity;
   }

   public AnimatableIntegerValue getOpacity() {
      return this.opacity;
   }

   public AnimatableValue getPosition() {
      return this.position;
   }

   public AnimatableFloatValue getRotation() {
      return this.rotation;
   }

   public AnimatableScaleValue getScale() {
      return this.scale;
   }

   public AnimatableFloatValue getStartOpacity() {
      return this.startOpacity;
   }

   public Content toContent(LottieDrawable var1, BaseLayer var2) {
      return null;
   }
}
