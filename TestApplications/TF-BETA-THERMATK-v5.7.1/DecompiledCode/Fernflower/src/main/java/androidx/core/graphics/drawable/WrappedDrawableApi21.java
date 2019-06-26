package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.Method;

class WrappedDrawableApi21 extends WrappedDrawableApi14 {
   private static Method sIsProjectedDrawableMethod;

   WrappedDrawableApi21(Drawable var1) {
      super(var1);
      this.findAndCacheIsProjectedDrawableMethod();
   }

   WrappedDrawableApi21(WrappedDrawableState var1, Resources var2) {
      super(var1, var2);
      this.findAndCacheIsProjectedDrawableMethod();
   }

   private void findAndCacheIsProjectedDrawableMethod() {
      if (sIsProjectedDrawableMethod == null) {
         try {
            sIsProjectedDrawableMethod = Drawable.class.getDeclaredMethod("isProjected");
         } catch (Exception var2) {
            Log.w("WrappedDrawableApi21", "Failed to retrieve Drawable#isProjected() method", var2);
         }
      }

   }

   public Rect getDirtyBounds() {
      return super.mDrawable.getDirtyBounds();
   }

   public void getOutline(Outline var1) {
      super.mDrawable.getOutline(var1);
   }

   protected boolean isCompatTintEnabled() {
      int var1 = VERSION.SDK_INT;
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 == 21) {
         Drawable var4 = super.mDrawable;
         if (!(var4 instanceof GradientDrawable) && !(var4 instanceof DrawableContainer) && !(var4 instanceof InsetDrawable)) {
            var3 = var2;
            if (!(var4 instanceof RippleDrawable)) {
               return var3;
            }
         }

         var3 = true;
      }

      return var3;
   }

   public void setHotspot(float var1, float var2) {
      super.mDrawable.setHotspot(var1, var2);
   }

   public void setHotspotBounds(int var1, int var2, int var3, int var4) {
      super.mDrawable.setHotspotBounds(var1, var2, var3, var4);
   }

   public boolean setState(int[] var1) {
      if (super.setState(var1)) {
         this.invalidateSelf();
         return true;
      } else {
         return false;
      }
   }

   public void setTint(int var1) {
      if (this.isCompatTintEnabled()) {
         super.setTint(var1);
      } else {
         super.mDrawable.setTint(var1);
      }

   }

   public void setTintList(ColorStateList var1) {
      if (this.isCompatTintEnabled()) {
         super.setTintList(var1);
      } else {
         super.mDrawable.setTintList(var1);
      }

   }

   public void setTintMode(Mode var1) {
      if (this.isCompatTintEnabled()) {
         super.setTintMode(var1);
      } else {
         super.mDrawable.setTintMode(var1);
      }

   }
}
