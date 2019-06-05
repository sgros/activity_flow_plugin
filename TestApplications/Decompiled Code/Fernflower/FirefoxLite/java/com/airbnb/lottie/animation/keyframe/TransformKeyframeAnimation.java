package com.airbnb.lottie.animation.keyframe;

import android.graphics.Matrix;
import android.graphics.PointF;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;

public class TransformKeyframeAnimation {
   private final BaseKeyframeAnimation anchorPoint;
   private final BaseKeyframeAnimation endOpacity;
   private final Matrix matrix = new Matrix();
   private final BaseKeyframeAnimation opacity;
   private final BaseKeyframeAnimation position;
   private final BaseKeyframeAnimation rotation;
   private final BaseKeyframeAnimation scale;
   private final BaseKeyframeAnimation startOpacity;

   public TransformKeyframeAnimation(AnimatableTransform var1) {
      this.anchorPoint = var1.getAnchorPoint().createAnimation();
      this.position = var1.getPosition().createAnimation();
      this.scale = var1.getScale().createAnimation();
      this.rotation = var1.getRotation().createAnimation();
      this.opacity = var1.getOpacity().createAnimation();
      if (var1.getStartOpacity() != null) {
         this.startOpacity = var1.getStartOpacity().createAnimation();
      } else {
         this.startOpacity = null;
      }

      if (var1.getEndOpacity() != null) {
         this.endOpacity = var1.getEndOpacity().createAnimation();
      } else {
         this.endOpacity = null;
      }

   }

   public void addAnimationsToLayer(BaseLayer var1) {
      var1.addAnimation(this.anchorPoint);
      var1.addAnimation(this.position);
      var1.addAnimation(this.scale);
      var1.addAnimation(this.rotation);
      var1.addAnimation(this.opacity);
      if (this.startOpacity != null) {
         var1.addAnimation(this.startOpacity);
      }

      if (this.endOpacity != null) {
         var1.addAnimation(this.endOpacity);
      }

   }

   public void addListener(BaseKeyframeAnimation.AnimationListener var1) {
      this.anchorPoint.addUpdateListener(var1);
      this.position.addUpdateListener(var1);
      this.scale.addUpdateListener(var1);
      this.rotation.addUpdateListener(var1);
      this.opacity.addUpdateListener(var1);
      if (this.startOpacity != null) {
         this.startOpacity.addUpdateListener(var1);
      }

      if (this.endOpacity != null) {
         this.endOpacity.addUpdateListener(var1);
      }

   }

   public boolean applyValueCallback(Object var1, LottieValueCallback var2) {
      if (var1 == LottieProperty.TRANSFORM_ANCHOR_POINT) {
         this.anchorPoint.setValueCallback(var2);
      } else if (var1 == LottieProperty.TRANSFORM_POSITION) {
         this.position.setValueCallback(var2);
      } else if (var1 == LottieProperty.TRANSFORM_SCALE) {
         this.scale.setValueCallback(var2);
      } else if (var1 == LottieProperty.TRANSFORM_ROTATION) {
         this.rotation.setValueCallback(var2);
      } else if (var1 == LottieProperty.TRANSFORM_OPACITY) {
         this.opacity.setValueCallback(var2);
      } else if (var1 == LottieProperty.TRANSFORM_START_OPACITY && this.startOpacity != null) {
         this.startOpacity.setValueCallback(var2);
      } else {
         if (var1 != LottieProperty.TRANSFORM_END_OPACITY || this.endOpacity == null) {
            return false;
         }

         this.endOpacity.setValueCallback(var2);
      }

      return true;
   }

   public BaseKeyframeAnimation getEndOpacity() {
      return this.endOpacity;
   }

   public Matrix getMatrix() {
      this.matrix.reset();
      PointF var1 = (PointF)this.position.getValue();
      if (var1.x != 0.0F || var1.y != 0.0F) {
         this.matrix.preTranslate(var1.x, var1.y);
      }

      float var2 = (Float)this.rotation.getValue();
      if (var2 != 0.0F) {
         this.matrix.preRotate(var2);
      }

      ScaleXY var3 = (ScaleXY)this.scale.getValue();
      if (var3.getScaleX() != 1.0F || var3.getScaleY() != 1.0F) {
         this.matrix.preScale(var3.getScaleX(), var3.getScaleY());
      }

      var1 = (PointF)this.anchorPoint.getValue();
      if (var1.x != 0.0F || var1.y != 0.0F) {
         this.matrix.preTranslate(-var1.x, -var1.y);
      }

      return this.matrix;
   }

   public Matrix getMatrixForRepeater(float var1) {
      PointF var2 = (PointF)this.position.getValue();
      PointF var3 = (PointF)this.anchorPoint.getValue();
      ScaleXY var4 = (ScaleXY)this.scale.getValue();
      float var5 = (Float)this.rotation.getValue();
      this.matrix.reset();
      this.matrix.preTranslate(var2.x * var1, var2.y * var1);
      Matrix var10 = this.matrix;
      double var6 = (double)var4.getScaleX();
      double var8 = (double)var1;
      var10.preScale((float)Math.pow(var6, var8), (float)Math.pow((double)var4.getScaleY(), var8));
      this.matrix.preRotate(var5 * var1, var3.x, var3.y);
      return this.matrix;
   }

   public BaseKeyframeAnimation getOpacity() {
      return this.opacity;
   }

   public BaseKeyframeAnimation getStartOpacity() {
      return this.startOpacity;
   }

   public void setProgress(float var1) {
      this.anchorPoint.setProgress(var1);
      this.position.setProgress(var1);
      this.scale.setProgress(var1);
      this.rotation.setProgress(var1);
      this.opacity.setProgress(var1);
      if (this.startOpacity != null) {
         this.startOpacity.setProgress(var1);
      }

      if (this.endOpacity != null) {
         this.endOpacity.setProgress(var1);
      }

   }
}
