package org.telegram.ui.Components;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class Scroller {
   private static float DECELERATION_RATE = (float)(Math.log(0.75D) / Math.log(0.9D));
   private static final int DEFAULT_DURATION = 250;
   private static float END_TENSION;
   private static final int FLING_MODE = 1;
   private static final int NB_SAMPLES = 100;
   private static final int SCROLL_MODE = 0;
   private static final float[] SPLINE;
   private static float START_TENSION = 0.4F;
   private static float sViscousFluidNormalize;
   private static float sViscousFluidScale;
   private int mCurrX;
   private int mCurrY;
   private float mDeceleration;
   private float mDeltaX;
   private float mDeltaY;
   private int mDuration;
   private float mDurationReciprocal;
   private int mFinalX;
   private int mFinalY;
   private boolean mFinished;
   private boolean mFlywheel;
   private Interpolator mInterpolator;
   private int mMaxX;
   private int mMaxY;
   private int mMinX;
   private int mMinY;
   private int mMode;
   private final float mPpi;
   private long mStartTime;
   private int mStartX;
   private int mStartY;
   private float mVelocity;

   static {
      END_TENSION = 1.0F - START_TENSION;
      SPLINE = new float[101];
      float var0 = 0.0F;

      for(int var1 = 0; var1 <= 100; ++var1) {
         float var2 = (float)var1 / 100.0F;
         float var3 = 1.0F;

         while(true) {
            float var4 = (var3 - var0) / 2.0F + var0;
            float var5 = 1.0F - var4;
            float var6 = 3.0F * var4 * var5;
            float var7 = START_TENSION;
            float var8 = END_TENSION;
            float var9 = var4 * var4 * var4;
            var5 = (var5 * var7 + var8 * var4) * var6 + var9;
            if ((double)Math.abs(var5 - var2) < 1.0E-5D) {
               SPLINE[var1] = var6 + var9;
               break;
            }

            if (var5 > var2) {
               var3 = var4;
            } else {
               var0 = var4;
            }
         }
      }

      SPLINE[100] = 1.0F;
      sViscousFluidScale = 8.0F;
      sViscousFluidNormalize = 1.0F;
      sViscousFluidNormalize = 1.0F / viscousFluid(1.0F);
   }

   public Scroller(Context var1) {
      this(var1, (Interpolator)null);
   }

   public Scroller(Context var1, Interpolator var2) {
      this(var1, var2, true);
   }

   public Scroller(Context var1, Interpolator var2, boolean var3) {
      this.mFinished = true;
      this.mInterpolator = var2;
      this.mPpi = var1.getResources().getDisplayMetrics().density * 160.0F;
      this.mDeceleration = this.computeDeceleration(ViewConfiguration.getScrollFriction());
      this.mFlywheel = var3;
   }

   private float computeDeceleration(float var1) {
      return this.mPpi * 386.0878F * var1;
   }

   static float viscousFluid(float var0) {
      var0 *= sViscousFluidScale;
      if (var0 < 1.0F) {
         var0 -= 1.0F - (float)Math.exp((double)(-var0));
      } else {
         var0 = (1.0F - (float)Math.exp((double)(1.0F - var0))) * 0.63212055F + 0.36787945F;
      }

      return var0 * sViscousFluidNormalize;
   }

   public void abortAnimation() {
      this.mCurrX = this.mFinalX;
      this.mCurrY = this.mFinalY;
      this.mFinished = true;
   }

   public boolean computeScrollOffset() {
      if (this.mFinished) {
         return false;
      } else {
         int var1 = (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
         int var2 = this.mDuration;
         if (var1 < var2) {
            int var3 = this.mMode;
            float var9;
            if (var3 != 0) {
               if (var3 == 1) {
                  float var4 = (float)var1 / (float)var2;
                  var1 = (int)(var4 * 100.0F);
                  float var5 = (float)var1 / 100.0F;
                  var2 = var1 + 1;
                  float var6 = (float)var2 / 100.0F;
                  float[] var7 = SPLINE;
                  float var8 = var7[var1];
                  var9 = var7[var2];
                  var9 = var8 + (var4 - var5) / (var6 - var5) * (var9 - var8);
                  var2 = this.mStartX;
                  this.mCurrX = var2 + Math.round((float)(this.mFinalX - var2) * var9);
                  this.mCurrX = Math.min(this.mCurrX, this.mMaxX);
                  this.mCurrX = Math.max(this.mCurrX, this.mMinX);
                  var2 = this.mStartY;
                  this.mCurrY = var2 + Math.round(var9 * (float)(this.mFinalY - var2));
                  this.mCurrY = Math.min(this.mCurrY, this.mMaxY);
                  this.mCurrY = Math.max(this.mCurrY, this.mMinY);
                  if (this.mCurrX == this.mFinalX && this.mCurrY == this.mFinalY) {
                     this.mFinished = true;
                  }
               }
            } else {
               var9 = (float)var1 * this.mDurationReciprocal;
               Interpolator var10 = this.mInterpolator;
               if (var10 == null) {
                  var9 = viscousFluid(var9);
               } else {
                  var9 = var10.getInterpolation(var9);
               }

               this.mCurrX = this.mStartX + Math.round(this.mDeltaX * var9);
               this.mCurrY = this.mStartY + Math.round(var9 * this.mDeltaY);
            }
         } else {
            this.mCurrX = this.mFinalX;
            this.mCurrY = this.mFinalY;
            this.mFinished = true;
         }

         return true;
      }
   }

   public void extendDuration(int var1) {
      this.mDuration = this.timePassed() + var1;
      this.mDurationReciprocal = 1.0F / (float)this.mDuration;
      this.mFinished = false;
   }

   public void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      float var9;
      float var10;
      float var12;
      if (this.mFlywheel && !this.mFinished) {
         var9 = this.getCurrVelocity();
         var10 = (float)(this.mFinalX - this.mStartX);
         float var11 = (float)(this.mFinalY - this.mStartY);
         var12 = (float)Math.sqrt((double)(var10 * var10 + var11 * var11));
         var10 /= var12;
         var11 /= var12;
         var12 = var10 * var9;
         var11 *= var9;
         var10 = (float)var3;
         if (Math.signum(var10) == Math.signum(var12)) {
            var9 = (float)var4;
            var3 = var3;
            if (Math.signum(var9) == Math.signum(var11)) {
               var3 = (int)(var10 + var12);
               var4 = (int)(var9 + var11);
            }
         }
      }

      this.mMode = 1;
      this.mFinished = false;
      var10 = (float)Math.sqrt((double)(var3 * var3 + var4 * var4));
      this.mVelocity = var10;
      double var14 = Math.log((double)(START_TENSION * var10 / 800.0F));
      double var16 = (double)DECELERATION_RATE;
      Double.isNaN(var16);
      this.mDuration = (int)(Math.exp(var14 / (var16 - 1.0D)) * 1000.0D);
      this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
      this.mStartX = var1;
      this.mStartY = var2;
      var12 = 1.0F;
      if (var10 == 0.0F) {
         var9 = 1.0F;
      } else {
         var9 = (float)var3 / var10;
      }

      if (var10 != 0.0F) {
         var12 = (float)var4 / var10;
      }

      var16 = (double)800.0F;
      var10 = DECELERATION_RATE;
      double var18 = (double)var10;
      double var20 = (double)var10;
      Double.isNaN(var20);
      Double.isNaN(var18);
      var14 = Math.exp(var18 / (var20 - 1.0D) * var14);
      Double.isNaN(var16);
      var3 = (int)(var16 * var14);
      this.mMinX = var5;
      this.mMaxX = var6;
      this.mMinY = var7;
      this.mMaxY = var8;
      var10 = (float)var3;
      this.mFinalX = var1 + Math.round(var9 * var10);
      this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
      this.mFinalX = Math.max(this.mFinalX, this.mMinX);
      this.mFinalY = Math.round(var10 * var12) + var2;
      this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
      this.mFinalY = Math.max(this.mFinalY, this.mMinY);
   }

   public final void forceFinished(boolean var1) {
      this.mFinished = var1;
   }

   public float getCurrVelocity() {
      return this.mVelocity - this.mDeceleration * (float)this.timePassed() / 2000.0F;
   }

   public final int getCurrX() {
      return this.mCurrX;
   }

   public final int getCurrY() {
      return this.mCurrY;
   }

   public final int getDuration() {
      return this.mDuration;
   }

   public final int getFinalX() {
      return this.mFinalX;
   }

   public final int getFinalY() {
      return this.mFinalY;
   }

   public final int getStartX() {
      return this.mStartX;
   }

   public final int getStartY() {
      return this.mStartY;
   }

   public final boolean isFinished() {
      return this.mFinished;
   }

   public boolean isScrollingInDirection(float var1, float var2) {
      boolean var3;
      if (!this.mFinished && Math.signum(var1) == Math.signum((float)(this.mFinalX - this.mStartX)) && Math.signum(var2) == Math.signum((float)(this.mFinalY - this.mStartY))) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void setFinalX(int var1) {
      this.mFinalX = var1;
      this.mDeltaX = (float)(this.mFinalX - this.mStartX);
      this.mFinished = false;
   }

   public void setFinalY(int var1) {
      this.mFinalY = var1;
      this.mDeltaY = (float)(this.mFinalY - this.mStartY);
      this.mFinished = false;
   }

   public final void setFriction(float var1) {
      this.mDeceleration = this.computeDeceleration(var1);
   }

   public void startScroll(int var1, int var2, int var3, int var4) {
      this.startScroll(var1, var2, var3, var4, 250);
   }

   public void startScroll(int var1, int var2, int var3, int var4, int var5) {
      this.mMode = 0;
      this.mFinished = false;
      this.mDuration = var5;
      this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
      this.mStartX = var1;
      this.mStartY = var2;
      this.mFinalX = var1 + var3;
      this.mFinalY = var2 + var4;
      this.mDeltaX = (float)var3;
      this.mDeltaY = (float)var4;
      this.mDurationReciprocal = 1.0F / (float)this.mDuration;
   }

   public int timePassed() {
      return (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
   }
}
