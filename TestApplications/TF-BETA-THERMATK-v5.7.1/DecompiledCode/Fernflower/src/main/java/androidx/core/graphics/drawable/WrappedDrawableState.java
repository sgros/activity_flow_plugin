package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;

final class WrappedDrawableState extends ConstantState {
   int mChangingConfigurations;
   ConstantState mDrawableState;
   ColorStateList mTint = null;
   Mode mTintMode;

   WrappedDrawableState(WrappedDrawableState var1) {
      this.mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;
      if (var1 != null) {
         this.mChangingConfigurations = var1.mChangingConfigurations;
         this.mDrawableState = var1.mDrawableState;
         this.mTint = var1.mTint;
         this.mTintMode = var1.mTintMode;
      }

   }

   boolean canConstantState() {
      boolean var1;
      if (this.mDrawableState != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public int getChangingConfigurations() {
      int var1 = this.mChangingConfigurations;
      ConstantState var2 = this.mDrawableState;
      int var3;
      if (var2 != null) {
         var3 = var2.getChangingConfigurations();
      } else {
         var3 = 0;
      }

      return var1 | var3;
   }

   public Drawable newDrawable() {
      return this.newDrawable((Resources)null);
   }

   public Drawable newDrawable(Resources var1) {
      return (Drawable)(VERSION.SDK_INT >= 21 ? new WrappedDrawableApi21(this, var1) : new WrappedDrawableApi14(this, var1));
   }
}
