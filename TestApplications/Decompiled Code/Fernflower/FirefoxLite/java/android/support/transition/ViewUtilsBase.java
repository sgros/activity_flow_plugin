package android.support.transition;

import android.graphics.Matrix;
import android.view.View;
import android.view.ViewParent;

class ViewUtilsBase {
   public void clearNonTransitionAlpha(View var1) {
      if (var1.getVisibility() == 0) {
         var1.setTag(R.id.save_non_transition_alpha, (Object)null);
      }

   }

   public float getTransitionAlpha(View var1) {
      Float var2 = (Float)var1.getTag(R.id.save_non_transition_alpha);
      return var2 != null ? var1.getAlpha() / var2 : var1.getAlpha();
   }

   public void saveNonTransitionAlpha(View var1) {
      if (var1.getTag(R.id.save_non_transition_alpha) == null) {
         var1.setTag(R.id.save_non_transition_alpha, var1.getAlpha());
      }

   }

   public void setLeftTopRightBottom(View var1, int var2, int var3, int var4, int var5) {
      var1.setLeft(var2);
      var1.setTop(var3);
      var1.setRight(var4);
      var1.setBottom(var5);
   }

   public void setTransitionAlpha(View var1, float var2) {
      Float var3 = (Float)var1.getTag(R.id.save_non_transition_alpha);
      if (var3 != null) {
         var1.setAlpha(var3 * var2);
      } else {
         var1.setAlpha(var2);
      }

   }

   public void transformMatrixToGlobal(View var1, Matrix var2) {
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

   public void transformMatrixToLocal(View var1, Matrix var2) {
      ViewParent var3 = var1.getParent();
      if (var3 instanceof View) {
         View var5 = (View)var3;
         this.transformMatrixToLocal(var5, var2);
         var2.postTranslate((float)var5.getScrollX(), (float)var5.getScrollY());
      }

      var2.postTranslate((float)var1.getLeft(), (float)var1.getTop());
      Matrix var4 = var1.getMatrix();
      if (!var4.isIdentity()) {
         Matrix var6 = new Matrix();
         if (var4.invert(var6)) {
            var2.postConcat(var6);
         }
      }

   }
}
