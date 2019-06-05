package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

class TransitionUtils {
   private static final boolean HAS_IS_ATTACHED_TO_WINDOW;
   private static final boolean HAS_OVERLAY;
   private static final boolean HAS_PICTURE_BITMAP;

   static {
      int var0 = VERSION.SDK_INT;
      boolean var1 = false;
      boolean var2;
      if (var0 >= 19) {
         var2 = true;
      } else {
         var2 = false;
      }

      HAS_IS_ATTACHED_TO_WINDOW = var2;
      if (VERSION.SDK_INT >= 18) {
         var2 = true;
      } else {
         var2 = false;
      }

      HAS_OVERLAY = var2;
      var2 = var1;
      if (VERSION.SDK_INT >= 28) {
         var2 = true;
      }

      HAS_PICTURE_BITMAP = var2;
   }

   static View copyViewImage(ViewGroup var0, View var1, View var2) {
      Matrix var3 = new Matrix();
      var3.setTranslate((float)(-var2.getScrollX()), (float)(-var2.getScrollY()));
      ViewUtils.transformMatrixToGlobal(var1, var3);
      ViewUtils.transformMatrixToLocal(var0, var3);
      RectF var4 = new RectF(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
      var3.mapRect(var4);
      int var5 = Math.round(var4.left);
      int var6 = Math.round(var4.top);
      int var7 = Math.round(var4.right);
      int var8 = Math.round(var4.bottom);
      ImageView var10 = new ImageView(var1.getContext());
      var10.setScaleType(ScaleType.CENTER_CROP);
      Bitmap var9 = createViewBitmap(var1, var3, var4, var0);
      if (var9 != null) {
         var10.setImageBitmap(var9);
      }

      var10.measure(MeasureSpec.makeMeasureSpec(var7 - var5, 1073741824), MeasureSpec.makeMeasureSpec(var8 - var6, 1073741824));
      var10.layout(var5, var6, var7, var8);
      return var10;
   }

   private static Bitmap createViewBitmap(View var0, Matrix var1, RectF var2, ViewGroup var3) {
      boolean var4;
      boolean var5;
      label42: {
         if (HAS_IS_ATTACHED_TO_WINDOW) {
            var4 = var0.isAttachedToWindow() ^ true;
            if (var3 != null) {
               var5 = var3.isAttachedToWindow();
               break label42;
            }
         } else {
            var4 = false;
         }

         var5 = false;
      }

      boolean var6 = HAS_OVERLAY;
      Object var7 = null;
      ViewGroup var8;
      int var9;
      if (var6 && var4) {
         if (!var5) {
            return null;
         }

         var8 = (ViewGroup)var0.getParent();
         var9 = var8.indexOfChild(var0);
         var3.getOverlay().add(var0);
      } else {
         var8 = null;
         var9 = 0;
      }

      int var10 = Math.round(var2.width());
      int var11 = Math.round(var2.height());
      Bitmap var12 = (Bitmap)var7;
      if (var10 > 0) {
         var12 = (Bitmap)var7;
         if (var11 > 0) {
            float var13 = Math.min(1.0F, 1048576.0F / (float)(var10 * var11));
            var10 = Math.round((float)var10 * var13);
            var11 = Math.round((float)var11 * var13);
            var1.postTranslate(-var2.left, -var2.top);
            var1.postScale(var13, var13);
            Canvas var14;
            if (HAS_PICTURE_BITMAP) {
               Picture var15 = new Picture();
               var14 = var15.beginRecording(var10, var11);
               var14.concat(var1);
               var0.draw(var14);
               var15.endRecording();
               var12 = Bitmap.createBitmap(var15);
            } else {
               var12 = Bitmap.createBitmap(var10, var11, Config.ARGB_8888);
               var14 = new Canvas(var12);
               var14.concat(var1);
               var0.draw(var14);
            }
         }
      }

      if (HAS_OVERLAY && var4) {
         var3.getOverlay().remove(var0);
         var8.addView(var0, var9);
      }

      return var12;
   }

   static Animator mergeAnimators(Animator var0, Animator var1) {
      if (var0 == null) {
         return var1;
      } else if (var1 == null) {
         return var0;
      } else {
         AnimatorSet var2 = new AnimatorSet();
         var2.playTogether(new Animator[]{var0, var1});
         return var2;
      }
   }
}
