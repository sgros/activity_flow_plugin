package com.airbnb.lottie.utils;

import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import com.airbnb.lottie.LottieComposition;

public class LottieValueAnimator extends BaseLottieAnimator implements FrameCallback {
   private LottieComposition composition;
   private float frame = 0.0F;
   private long lastFrameTimeNs = 0L;
   private float maxFrame = 2.14748365E9F;
   private float minFrame = -2.14748365E9F;
   private int repeatCount = 0;
   protected boolean running = false;
   private float speed = 1.0F;
   private boolean speedReversedForRepeatMode = false;

   private float getFrameDurationNs() {
      LottieComposition var1 = this.composition;
      return var1 == null ? Float.MAX_VALUE : 1.0E9F / var1.getFrameRate() / Math.abs(this.speed);
   }

   private boolean isReversed() {
      boolean var1;
      if (this.getSpeed() < 0.0F) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void verifyFrame() {
      if (this.composition != null) {
         float var1 = this.frame;
         if (var1 < this.minFrame || var1 > this.maxFrame) {
            throw new IllegalStateException(String.format("Frame must be [%f,%f]. It is %f", this.minFrame, this.maxFrame, this.frame));
         }
      }
   }

   public void cancel() {
      this.notifyCancel();
      this.removeFrameCallback();
   }

   public void clearComposition() {
      this.composition = null;
      this.minFrame = -2.14748365E9F;
      this.maxFrame = 2.14748365E9F;
   }

   public void doFrame(long var1) {
      this.postFrameCallback();
      if (this.composition != null && this.isRunning()) {
         long var3 = this.lastFrameTimeNs;
         long var5 = 0L;
         if (var3 != 0L) {
            var5 = var1 - var3;
         }

         float var7 = this.getFrameDurationNs();
         float var8 = (float)var5 / var7;
         float var9 = this.frame;
         var7 = var8;
         if (this.isReversed()) {
            var7 = -var8;
         }

         this.frame = var9 + var7;
         boolean var10 = MiscUtils.contains(this.frame, this.getMinFrame(), this.getMaxFrame());
         this.frame = MiscUtils.clamp(this.frame, this.getMinFrame(), this.getMaxFrame());
         this.lastFrameTimeNs = var1;
         this.notifyUpdate();
         if (var10 ^ true) {
            if (this.getRepeatCount() != -1 && this.repeatCount >= this.getRepeatCount()) {
               if (this.speed < 0.0F) {
                  var7 = this.getMinFrame();
               } else {
                  var7 = this.getMaxFrame();
               }

               this.frame = var7;
               this.removeFrameCallback();
               this.notifyEnd(this.isReversed());
            } else {
               this.notifyRepeat();
               ++this.repeatCount;
               if (this.getRepeatMode() == 2) {
                  this.speedReversedForRepeatMode ^= true;
                  this.reverseAnimationSpeed();
               } else {
                  if (this.isReversed()) {
                     var7 = this.getMaxFrame();
                  } else {
                     var7 = this.getMinFrame();
                  }

                  this.frame = var7;
               }

               this.lastFrameTimeNs = var1;
            }
         }

         this.verifyFrame();
      }

   }

   public void endAnimation() {
      this.removeFrameCallback();
      this.notifyEnd(this.isReversed());
   }

   public float getAnimatedFraction() {
      if (this.composition == null) {
         return 0.0F;
      } else {
         float var1;
         float var2;
         float var3;
         if (this.isReversed()) {
            var1 = this.getMaxFrame() - this.frame;
            var2 = this.getMaxFrame();
            var3 = this.getMinFrame();
         } else {
            var1 = this.frame - this.getMinFrame();
            var2 = this.getMaxFrame();
            var3 = this.getMinFrame();
         }

         return var1 / (var2 - var3);
      }
   }

   public Object getAnimatedValue() {
      return this.getAnimatedValueAbsolute();
   }

   public float getAnimatedValueAbsolute() {
      LottieComposition var1 = this.composition;
      return var1 == null ? 0.0F : (this.frame - var1.getStartFrame()) / (this.composition.getEndFrame() - this.composition.getStartFrame());
   }

   public long getDuration() {
      LottieComposition var1 = this.composition;
      long var2;
      if (var1 == null) {
         var2 = 0L;
      } else {
         var2 = (long)var1.getDuration();
      }

      return var2;
   }

   public float getFrame() {
      return this.frame;
   }

   public float getMaxFrame() {
      LottieComposition var1 = this.composition;
      if (var1 == null) {
         return 0.0F;
      } else {
         float var2 = this.maxFrame;
         float var3 = var2;
         if (var2 == 2.14748365E9F) {
            var3 = var1.getEndFrame();
         }

         return var3;
      }
   }

   public float getMinFrame() {
      LottieComposition var1 = this.composition;
      if (var1 == null) {
         return 0.0F;
      } else {
         float var2 = this.minFrame;
         float var3 = var2;
         if (var2 == -2.14748365E9F) {
            var3 = var1.getStartFrame();
         }

         return var3;
      }
   }

   public float getSpeed() {
      return this.speed;
   }

   public boolean isRunning() {
      return this.running;
   }

   public void pauseAnimation() {
      this.removeFrameCallback();
   }

   public void playAnimation() {
      this.running = true;
      this.notifyStart(this.isReversed());
      float var1;
      if (this.isReversed()) {
         var1 = this.getMaxFrame();
      } else {
         var1 = this.getMinFrame();
      }

      this.setFrame((int)var1);
      this.lastFrameTimeNs = 0L;
      this.repeatCount = 0;
      this.postFrameCallback();
   }

   protected void postFrameCallback() {
      if (this.isRunning()) {
         this.removeFrameCallback(false);
         Choreographer.getInstance().postFrameCallback(this);
      }

   }

   protected void removeFrameCallback() {
      this.removeFrameCallback(true);
   }

   protected void removeFrameCallback(boolean var1) {
      Choreographer.getInstance().removeFrameCallback(this);
      if (var1) {
         this.running = false;
      }

   }

   public void resumeAnimation() {
      this.running = true;
      this.postFrameCallback();
      this.lastFrameTimeNs = 0L;
      if (this.isReversed() && this.getFrame() == this.getMinFrame()) {
         this.frame = this.getMaxFrame();
      } else if (!this.isReversed() && this.getFrame() == this.getMaxFrame()) {
         this.frame = this.getMinFrame();
      }

   }

   public void reverseAnimationSpeed() {
      this.setSpeed(-this.getSpeed());
   }

   public void setComposition(LottieComposition var1) {
      boolean var2;
      if (this.composition == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.composition = var1;
      if (var2) {
         this.setMinAndMaxFrames((float)((int)Math.max(this.minFrame, var1.getStartFrame())), (float)((int)Math.min(this.maxFrame, var1.getEndFrame())));
      } else {
         this.setMinAndMaxFrames((float)((int)var1.getStartFrame()), (float)((int)var1.getEndFrame()));
      }

      float var3 = this.frame;
      this.frame = 0.0F;
      this.setFrame((int)var3);
   }

   public void setFrame(int var1) {
      float var2 = this.frame;
      float var3 = (float)var1;
      if (var2 != var3) {
         this.frame = MiscUtils.clamp(var3, this.getMinFrame(), this.getMaxFrame());
         this.lastFrameTimeNs = 0L;
         this.notifyUpdate();
      }
   }

   public void setMaxFrame(float var1) {
      this.setMinAndMaxFrames(this.minFrame, var1);
   }

   public void setMinAndMaxFrames(float var1, float var2) {
      if (var1 <= var2) {
         LottieComposition var3 = this.composition;
         float var4;
         if (var3 == null) {
            var4 = -3.4028235E38F;
         } else {
            var4 = var3.getStartFrame();
         }

         var3 = this.composition;
         float var5;
         if (var3 == null) {
            var5 = Float.MAX_VALUE;
         } else {
            var5 = var3.getEndFrame();
         }

         this.minFrame = MiscUtils.clamp(var1, var4, var5);
         this.maxFrame = MiscUtils.clamp(var2, var4, var5);
         this.setFrame((int)MiscUtils.clamp(this.frame, var1, var2));
      } else {
         throw new IllegalArgumentException(String.format("minFrame (%s) must be <= maxFrame (%s)", var1, var2));
      }
   }

   public void setMinFrame(int var1) {
      this.setMinAndMaxFrames((float)var1, (float)((int)this.maxFrame));
   }

   public void setRepeatMode(int var1) {
      super.setRepeatMode(var1);
      if (var1 != 2 && this.speedReversedForRepeatMode) {
         this.speedReversedForRepeatMode = false;
         this.reverseAnimationSpeed();
      }

   }

   public void setSpeed(float var1) {
      this.speed = var1;
   }
}
