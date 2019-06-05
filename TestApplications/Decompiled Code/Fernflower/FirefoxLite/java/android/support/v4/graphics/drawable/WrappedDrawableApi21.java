package android.support.v4.graphics.drawable;

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

   WrappedDrawableApi21(WrappedDrawableApi14.DrawableWrapperState var1, Resources var2) {
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
      return this.mDrawable.getDirtyBounds();
   }

   public void getOutline(Outline var1) {
      this.mDrawable.getOutline(var1);
   }

   protected boolean isCompatTintEnabled() {
      int var1 = VERSION.SDK_INT;
      boolean var2 = false;
      if (var1 != 21) {
         return false;
      } else {
         Drawable var3 = this.mDrawable;
         if (var3 instanceof GradientDrawable || var3 instanceof DrawableContainer || var3 instanceof InsetDrawable || var3 instanceof RippleDrawable) {
            var2 = true;
         }

         return var2;
      }
   }

   WrappedDrawableApi14.DrawableWrapperState mutateConstantState() {
      return new WrappedDrawableApi21.DrawableWrapperStateLollipop(this.mState, (Resources)null);
   }

   public void setHotspot(float var1, float var2) {
      this.mDrawable.setHotspot(var1, var2);
   }

   public void setHotspotBounds(int var1, int var2, int var3, int var4) {
      this.mDrawable.setHotspotBounds(var1, var2, var3, var4);
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
         this.mDrawable.setTint(var1);
      }

   }

   public void setTintList(ColorStateList var1) {
      if (this.isCompatTintEnabled()) {
         super.setTintList(var1);
      } else {
         this.mDrawable.setTintList(var1);
      }

   }

   public void setTintMode(Mode var1) {
      if (this.isCompatTintEnabled()) {
         super.setTintMode(var1);
      } else {
         this.mDrawable.setTintMode(var1);
      }

   }

   private static class DrawableWrapperStateLollipop extends WrappedDrawableApi14.DrawableWrapperState {
      DrawableWrapperStateLollipop(WrappedDrawableApi14.DrawableWrapperState var1, Resources var2) {
         super(var1, var2);
      }

      public Drawable newDrawable(Resources var1) {
         return new WrappedDrawableApi21(this, var1);
      }
   }
}
