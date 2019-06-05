package android.support.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class Explode extends Visibility {
   private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
   private static final TimeInterpolator sAccelerate = new AccelerateInterpolator();
   private static final TimeInterpolator sDecelerate = new DecelerateInterpolator();
   private int[] mTempLoc = new int[2];

   public Explode() {
      this.setPropagation(new CircularPropagation());
   }

   public Explode(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.setPropagation(new CircularPropagation());
   }

   private static float calculateDistance(float var0, float var1) {
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   private static float calculateMaxDistance(View var0, int var1, int var2) {
      var1 = Math.max(var1, var0.getWidth() - var1);
      var2 = Math.max(var2, var0.getHeight() - var2);
      return calculateDistance((float)var1, (float)var2);
   }

   private void calculateOut(View var1, Rect var2, int[] var3) {
      var1.getLocationOnScreen(this.mTempLoc);
      int var4 = this.mTempLoc[0];
      int var5 = this.mTempLoc[1];
      Rect var6 = this.getEpicenter();
      int var7;
      int var8;
      if (var6 == null) {
         var7 = var1.getWidth() / 2 + var4 + Math.round(var1.getTranslationX());
         var8 = var1.getHeight() / 2 + var5 + Math.round(var1.getTranslationY());
      } else {
         var7 = var6.centerX();
         var8 = var6.centerY();
      }

      int var9 = var2.centerX();
      int var10 = var2.centerY();
      float var11 = (float)(var9 - var7);
      float var12 = (float)(var10 - var8);
      float var13 = var11;
      float var14 = var12;
      if (var11 == 0.0F) {
         var13 = var11;
         var14 = var12;
         if (var12 == 0.0F) {
            var13 = (float)(Math.random() * 2.0D) - 1.0F;
            var14 = (float)(Math.random() * 2.0D) - 1.0F;
         }
      }

      var11 = calculateDistance(var13, var14);
      var13 /= var11;
      var11 = var14 / var11;
      var14 = calculateMaxDistance(var1, var7 - var4, var8 - var5);
      var3[0] = Math.round(var13 * var14);
      var3[1] = Math.round(var14 * var11);
   }

   private void captureValues(TransitionValues var1) {
      View var2 = var1.view;
      var2.getLocationOnScreen(this.mTempLoc);
      int var3 = this.mTempLoc[0];
      int var4 = this.mTempLoc[1];
      int var5 = var2.getWidth();
      int var6 = var2.getHeight();
      var1.values.put("android:explode:screenBounds", new Rect(var3, var4, var5 + var3, var6 + var4));
   }

   public void captureEndValues(@NonNull TransitionValues var1) {
      super.captureEndValues(var1);
      this.captureValues(var1);
   }

   public void captureStartValues(@NonNull TransitionValues var1) {
      super.captureStartValues(var1);
      this.captureValues(var1);
   }

   public Animator onAppear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      if (var4 == null) {
         return null;
      } else {
         Rect var9 = (Rect)var4.values.get("android:explode:screenBounds");
         float var5 = var2.getTranslationX();
         float var6 = var2.getTranslationY();
         this.calculateOut(var1, var9, this.mTempLoc);
         float var7 = (float)this.mTempLoc[0];
         float var8 = (float)this.mTempLoc[1];
         return TranslationAnimationCreator.createAnimation(var2, var4, var9.left, var9.top, var5 + var7, var6 + var8, var5, var6, sDecelerate);
      }
   }

   public Animator onDisappear(ViewGroup var1, View var2, TransitionValues var3, TransitionValues var4) {
      if (var3 == null) {
         return null;
      } else {
         Rect var12 = (Rect)var3.values.get("android:explode:screenBounds");
         int var5 = var12.left;
         int var6 = var12.top;
         float var7 = var2.getTranslationX();
         float var8 = var2.getTranslationY();
         int[] var9 = (int[])var3.view.getTag(R.id.transition_position);
         float var10;
         float var11;
         if (var9 != null) {
            var10 = (float)(var9[0] - var12.left) + var7;
            var11 = (float)(var9[1] - var12.top) + var8;
            var12.offsetTo(var9[0], var9[1]);
         } else {
            var10 = var7;
            var11 = var8;
         }

         this.calculateOut(var1, var12, this.mTempLoc);
         return TranslationAnimationCreator.createAnimation(var2, var3, var5, var6, var7, var8, var10 + (float)this.mTempLoc[0], var11 + (float)this.mTempLoc[1], sAccelerate);
      }
   }
}
