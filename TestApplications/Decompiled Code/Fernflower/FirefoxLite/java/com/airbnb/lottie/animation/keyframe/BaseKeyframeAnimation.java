package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseKeyframeAnimation {
   private Keyframe cachedKeyframe;
   private boolean isDiscrete = false;
   private final List keyframes;
   final List listeners = new ArrayList();
   private float progress = 0.0F;
   protected LottieValueCallback valueCallback;

   BaseKeyframeAnimation(List var1) {
      this.keyframes = var1;
   }

   private Keyframe getCurrentKeyframe() {
      if (this.cachedKeyframe != null && this.cachedKeyframe.containsProgress(this.progress)) {
         return this.cachedKeyframe;
      } else {
         Keyframe var1 = (Keyframe)this.keyframes.get(this.keyframes.size() - 1);
         Keyframe var2 = var1;
         if (this.progress < var1.getStartProgress()) {
            int var3 = this.keyframes.size() - 1;

            for(var2 = var1; var3 >= 0; --var3) {
               var2 = (Keyframe)this.keyframes.get(var3);
               if (var2.containsProgress(this.progress)) {
                  break;
               }
            }
         }

         this.cachedKeyframe = var2;
         return var2;
      }
   }

   private float getInterpolatedCurrentKeyframeProgress() {
      Keyframe var1 = this.getCurrentKeyframe();
      return var1.isStatic() ? 0.0F : var1.interpolator.getInterpolation(this.getLinearCurrentKeyframeProgress());
   }

   private float getStartDelayProgress() {
      float var1;
      if (this.keyframes.isEmpty()) {
         var1 = 0.0F;
      } else {
         var1 = ((Keyframe)this.keyframes.get(0)).getStartProgress();
      }

      return var1;
   }

   public void addUpdateListener(BaseKeyframeAnimation.AnimationListener var1) {
      this.listeners.add(var1);
   }

   float getEndProgress() {
      float var1;
      if (this.keyframes.isEmpty()) {
         var1 = 1.0F;
      } else {
         var1 = ((Keyframe)this.keyframes.get(this.keyframes.size() - 1)).getEndProgress();
      }

      return var1;
   }

   float getLinearCurrentKeyframeProgress() {
      if (this.isDiscrete) {
         return 0.0F;
      } else {
         Keyframe var1 = this.getCurrentKeyframe();
         return var1.isStatic() ? 0.0F : (this.progress - var1.getStartProgress()) / (var1.getEndProgress() - var1.getStartProgress());
      }
   }

   public float getProgress() {
      return this.progress;
   }

   public Object getValue() {
      return this.getValue(this.getCurrentKeyframe(), this.getInterpolatedCurrentKeyframeProgress());
   }

   abstract Object getValue(Keyframe var1, float var2);

   public void notifyListeners() {
      for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
         ((BaseKeyframeAnimation.AnimationListener)this.listeners.get(var1)).onValueChanged();
      }

   }

   public void setIsDiscrete() {
      this.isDiscrete = true;
   }

   public void setProgress(float var1) {
      float var2;
      if (var1 < this.getStartDelayProgress()) {
         var2 = this.getStartDelayProgress();
      } else {
         var2 = var1;
         if (var1 > this.getEndProgress()) {
            var2 = this.getEndProgress();
         }
      }

      if (var2 != this.progress) {
         this.progress = var2;
         this.notifyListeners();
      }
   }

   public void setValueCallback(LottieValueCallback var1) {
      if (this.valueCallback != null) {
         this.valueCallback.setAnimation((BaseKeyframeAnimation)null);
      }

      this.valueCallback = var1;
      if (var1 != null) {
         var1.setAnimation(this);
      }

   }

   public interface AnimationListener {
      void onValueChanged();
   }
}
