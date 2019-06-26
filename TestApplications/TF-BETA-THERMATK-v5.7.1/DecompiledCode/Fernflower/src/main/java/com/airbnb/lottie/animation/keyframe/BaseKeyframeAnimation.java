package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseKeyframeAnimation {
   private float cachedEndProgress = -1.0F;
   private Object cachedGetValue = null;
   private Keyframe cachedGetValueKeyframe;
   private float cachedGetValueProgress = -1.0F;
   private Keyframe cachedKeyframe;
   private float cachedStartDelayProgress = -1.0F;
   private boolean isDiscrete = false;
   private final List keyframes;
   final List listeners = new ArrayList(1);
   private float progress = 0.0F;
   protected LottieValueCallback valueCallback;

   BaseKeyframeAnimation(List var1) {
      this.keyframes = var1;
   }

   private float getStartDelayProgress() {
      if (this.cachedStartDelayProgress == -1.0F) {
         float var1;
         if (this.keyframes.isEmpty()) {
            var1 = 0.0F;
         } else {
            var1 = ((Keyframe)this.keyframes.get(0)).getStartProgress();
         }

         this.cachedStartDelayProgress = var1;
      }

      return this.cachedStartDelayProgress;
   }

   public void addUpdateListener(BaseKeyframeAnimation.AnimationListener var1) {
      this.listeners.add(var1);
   }

   protected Keyframe getCurrentKeyframe() {
      Keyframe var1 = this.cachedKeyframe;
      if (var1 != null && var1.containsProgress(this.progress)) {
         return this.cachedKeyframe;
      } else {
         List var4 = this.keyframes;
         Keyframe var2 = (Keyframe)var4.get(var4.size() - 1);
         var1 = var2;
         if (this.progress < var2.getStartProgress()) {
            int var3 = this.keyframes.size() - 1;

            for(var1 = var2; var3 >= 0; --var3) {
               var1 = (Keyframe)this.keyframes.get(var3);
               if (var1.containsProgress(this.progress)) {
                  break;
               }
            }
         }

         this.cachedKeyframe = var1;
         return var1;
      }
   }

   float getEndProgress() {
      if (this.cachedEndProgress == -1.0F) {
         float var1;
         if (this.keyframes.isEmpty()) {
            var1 = 1.0F;
         } else {
            List var2 = this.keyframes;
            var1 = ((Keyframe)var2.get(var2.size() - 1)).getEndProgress();
         }

         this.cachedEndProgress = var1;
      }

      return this.cachedEndProgress;
   }

   protected float getInterpolatedCurrentKeyframeProgress() {
      Keyframe var1 = this.getCurrentKeyframe();
      return var1.isStatic() ? 0.0F : var1.interpolator.getInterpolation(this.getLinearCurrentKeyframeProgress());
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
      Keyframe var1 = this.getCurrentKeyframe();
      float var2 = this.getInterpolatedCurrentKeyframeProgress();
      if (this.valueCallback == null && var1 == this.cachedGetValueKeyframe && this.cachedGetValueProgress == var2) {
         return this.cachedGetValue;
      } else {
         this.cachedGetValueKeyframe = var1;
         this.cachedGetValueProgress = var2;
         Object var3 = this.getValue(var1, var2);
         this.cachedGetValue = var3;
         return var3;
      }
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
      if (!this.keyframes.isEmpty()) {
         Keyframe var2 = this.getCurrentKeyframe();
         float var3;
         if (var1 < this.getStartDelayProgress()) {
            var3 = this.getStartDelayProgress();
         } else {
            var3 = var1;
            if (var1 > this.getEndProgress()) {
               var3 = this.getEndProgress();
            }
         }

         if (var3 != this.progress) {
            this.progress = var3;
            Keyframe var4 = this.getCurrentKeyframe();
            if (var2 != var4 || !var4.isStatic()) {
               this.notifyListeners();
            }

         }
      }
   }

   public void setValueCallback(LottieValueCallback var1) {
      LottieValueCallback var2 = this.valueCallback;
      if (var2 != null) {
         var2.setAnimation((BaseKeyframeAnimation)null);
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
