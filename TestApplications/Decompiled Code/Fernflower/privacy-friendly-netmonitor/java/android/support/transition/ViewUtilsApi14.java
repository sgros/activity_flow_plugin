package android.support.transition;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewParent;

@RequiresApi(14)
class ViewUtilsApi14 implements ViewUtilsImpl {
   private float[] mMatrixValues;

   public void clearNonTransitionAlpha(@NonNull View var1) {
      if (var1.getVisibility() == 0) {
         var1.setTag(R.id.save_non_transition_alpha, (Object)null);
      }

   }

   public ViewOverlayImpl getOverlay(@NonNull View var1) {
      return ViewOverlayApi14.createFrom(var1);
   }

   public float getTransitionAlpha(@NonNull View var1) {
      Float var2 = (Float)var1.getTag(R.id.save_non_transition_alpha);
      return var2 != null ? var1.getAlpha() / var2 : var1.getAlpha();
   }

   public WindowIdImpl getWindowId(@NonNull View var1) {
      return new WindowIdApi14(var1.getWindowToken());
   }

   public void saveNonTransitionAlpha(@NonNull View var1) {
      if (var1.getTag(R.id.save_non_transition_alpha) == null) {
         var1.setTag(R.id.save_non_transition_alpha, var1.getAlpha());
      }

   }

   public void setAnimationMatrix(@NonNull View var1, Matrix var2) {
      if (var2 != null && !var2.isIdentity()) {
         float[] var3 = this.mMatrixValues;
         float[] var4 = var3;
         if (var3 == null) {
            var4 = new float[9];
            this.mMatrixValues = var4;
         }

         var2.getValues(var4);
         float var5 = var4[3];
         float var6 = (float)Math.sqrt((double)(1.0F - var5 * var5));
         byte var7;
         if (var4[0] < 0.0F) {
            var7 = -1;
         } else {
            var7 = 1;
         }

         float var8 = var6 * (float)var7;
         var6 = (float)Math.toDegrees(Math.atan2((double)var5, (double)var8));
         var5 = var4[0] / var8;
         float var9 = var4[4] / var8;
         var8 = var4[2];
         float var10 = var4[5];
         var1.setPivotX(0.0F);
         var1.setPivotY(0.0F);
         var1.setTranslationX(var8);
         var1.setTranslationY(var10);
         var1.setRotation(var6);
         var1.setScaleX(var5);
         var1.setScaleY(var9);
      } else {
         var1.setPivotX((float)(var1.getWidth() / 2));
         var1.setPivotY((float)(var1.getHeight() / 2));
         var1.setTranslationX(0.0F);
         var1.setTranslationY(0.0F);
         var1.setScaleX(1.0F);
         var1.setScaleY(1.0F);
         var1.setRotation(0.0F);
      }

   }

   public void setLeftTopRightBottom(View var1, int var2, int var3, int var4, int var5) {
      var1.setLeft(var2);
      var1.setTop(var3);
      var1.setRight(var4);
      var1.setBottom(var5);
   }

   public void setTransitionAlpha(@NonNull View var1, float var2) {
      Float var3 = (Float)var1.getTag(R.id.save_non_transition_alpha);
      if (var3 != null) {
         var1.setAlpha(var3 * var2);
      } else {
         var1.setAlpha(var2);
      }

   }

   public void transformMatrixToGlobal(@NonNull View var1, @NonNull Matrix var2) {
      ViewParent var3 = var1.getParent();
      if (var3 instanceof View) {
         View var5 = (View)var3;
         this.transformMatrixToGlobal(var5, var2);
         var2.preTranslate((float)(-var5.getScrollX()), (float)(-var5.getScrollY()));
      }

      var2.preTranslate((float)var1.getLeft(), (float)var1.getTop());
      Matrix var4 = var1.getMatrix();
      if (!var4.isIdentity()) {
         var2.preConcat(var4);
      }

   }

   public void transformMatrixToLocal(@NonNull View var1, @NonNull Matrix var2) {
      ViewParent var3 = var1.getParent();
      if (var3 instanceof View) {
         View var5 = (View)var3;
         this.transformMatrixToLocal(var5, var2);
         var2.postTranslate((float)var5.getScrollX(), (float)var5.getScrollY());
      }

      var2.postTranslate((float)var1.getLeft(), (float)var1.getTop());
      Matrix var6 = var1.getMatrix();
      if (!var6.isIdentity()) {
         Matrix var4 = new Matrix();
         if (var6.invert(var4)) {
            var2.postConcat(var4);
         }
      }

   }
}
