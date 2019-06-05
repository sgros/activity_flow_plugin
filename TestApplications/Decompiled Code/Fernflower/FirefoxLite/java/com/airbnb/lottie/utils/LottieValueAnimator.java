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
      return this.composition == null ? Float.MAX_VALUE : 1.0E9F / this.composition.getFrameRate() / Math.abs(this.speed);
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
         if (this.frame < this.minFrame || this.frame > this.maxFrame) {
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
         var1 = System.nanoTime();
         long var3 = this.lastFrameTimeNs;
         float var5 = this.getFrameDurationNs();
         float var6 = (float)(var1 - var3) / var5;
         float var7 = this.frame;
         var5 = var6;
         if (this.isReversed()) {
            var5 = -var6;
         }

         this.frame = var7 + var5;
         boolean var8 = MiscUtils.contains(this.frame, this.getMinFrame(), this.getMaxFrame());
         this.frame = MiscUtils.clamp(this.frame, this.getMinFrame(), this.getMaxFrame());
         this.lastFrameTimeNs = var1;
         this.notifyUpdate();
         if (var8 ^ true) {
            if (this.getRepeatCount() != -1 && this.repeatCount >= this.getRepeatCount()) {
               this.frame = this.getMaxFrame();
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
                     var5 = this.getMaxFrame();
                  } else {
                     var5 = this.getMinFrame();
                  }

                  this.frame = var5;
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
         return this.isReversed() ? (this.getMaxFrame() - this.frame) / (this.getMaxFrame() - this.getMinFrame()) : (this.frame - this.getMinFrame()) / (this.getMaxFrame() - this.getMinFrame());
      }
   }

   public Object getAnimatedValue() {
      return this.getAnimatedValueAbsolute();
   }

   public float getAnimatedValueAbsolute() {
      return this.composition == null ? 0.0F : (this.frame - this.composition.getStartFrame()) / (this.composition.getEndFrame() - this.composition.getStartFrame());
   }

   public long getDuration() {
      long var1;
      if (this.composition == null) {
         var1 = 0L;
      } else {
         var1 = (long)this.composition.getDuration();
      }

      return var1;
   }

   public float getFrame() {
      return this.frame;
   }

   public float getMaxFrame() {
      if (this.composition == null) {
         return 0.0F;
      } else {
         float var1;
         if (this.maxFrame == 2.14748365E9F) {
            var1 = this.composition.getEndFrame();
         } else {
            var1 = this.maxFrame;
         }

         return var1;
      }
   }

   public float getMinFrame() {
      if (this.composition == null) {
         return 0.0F;
      } else {
         float var1;
         if (this.minFrame == -2.14748365E9F) {
            var1 = this.composition.getStartFrame();
         } else {
            var1 = this.minFrame;
         }

         return var1;
      }
   }

   public float getSpeed() {
      return this.speed;
   }

   public boolean isRunning() {
      return this.running;
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
      this.lastFrameTimeNs = System.nanoTime();
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
         this.setMinAndMaxFrames((int)Math.max(this.minFrame, var1.getStartFrame()), (int)Math.min(this.maxFrame, var1.getEndFrame()));
      } else {
         this.setMinAndMaxFrames((int)var1.getStartFrame(), (int)var1.getEndFrame());
      }

      this.setFrame((int)this.frame);
      this.lastFrameTimeNs = System.nanoTime();
   }

   public void setFrame(int var1) {
      float var2 = this.frame;
      float var3 = (float)var1;
      if (var2 != var3) {
         this.frame = MiscUtils.clamp(var3, this.getMinFrame(), this.getMaxFrame());
         this.lastFrameTimeNs = System.nanoTime();
         this.notifyUpdate();
      }
   }

   public void setMaxFrame(int var1) {
      this.setMinAndMaxFrames((int)this.minFrame, var1);
   }

   public void setMinAndMaxFrames(int var1, int var2) {
      float var3;
      if (this.composition == null) {
         var3 = -3.4028235E38F;
      } else {
         var3 = this.composition.getStartFrame();
      }

      float var4;
      if (this.composition == null) {
         var4 = Float.MAX_VALUE;
      } else {
         var4 = this.composition.getEndFrame();
      }

      float var5 = (float)var1;
      this.minFrame = MiscUtils.clamp(var5, var3, var4);
      float var6 = (float)var2;
      this.maxFrame = MiscUtils.clamp(var6, var3, var4);
      this.setFrame((int)MiscUtils.clamp(this.frame, var5, var6));
   }

   public void setMinFrame(int var1) {
      this.setMinAndMaxFrames(var1, (int)this.maxFrame);
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
