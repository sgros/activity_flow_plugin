package android.support.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

public class CircularPropagation extends VisibilityPropagation {
   private float mPropagationSpeed = 3.0F;

   private static float distance(float var0, float var1, float var2, float var3) {
      var0 = var2 - var0;
      var1 = var3 - var1;
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   public long getStartDelay(ViewGroup var1, Transition var2, TransitionValues var3, TransitionValues var4) {
      if (var3 == null && var4 == null) {
         return 0L;
      } else {
         byte var5;
         if (var4 != null && this.getViewVisibility(var3) != 0) {
            var5 = 1;
            var3 = var4;
         } else {
            var5 = -1;
         }

         int var6 = this.getViewX(var3);
         int var7 = this.getViewY(var3);
         Rect var15 = var2.getEpicenter();
         int var8;
         int var9;
         if (var15 != null) {
            var8 = var15.centerX();
            var9 = var15.centerY();
         } else {
            int[] var16 = new int[2];
            var1.getLocationOnScreen(var16);
            var8 = Math.round((float)(var16[0] + var1.getWidth() / 2) + var1.getTranslationX());
            var9 = Math.round((float)(var16[1] + var1.getHeight() / 2) + var1.getTranslationY());
         }

         float var10 = distance((float)var6, (float)var7, (float)var8, (float)var9) / distance(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight());
         long var11 = var2.getDuration();
         long var13 = var11;
         if (var11 < 0L) {
            var13 = 300L;
         }

         return (long)Math.round((float)(var13 * (long)var5) / this.mPropagationSpeed * var10);
      }
   }

   public void setPropagationSpeed(float var1) {
      if (var1 == 0.0F) {
         throw new IllegalArgumentException("propagationSpeed may not be 0");
      } else {
         this.mPropagationSpeed = var1;
      }
   }
}
