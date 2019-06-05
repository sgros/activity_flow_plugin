package android.support.design.button;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;

@TargetApi(21)
class MaterialButtonBackgroundDrawable extends RippleDrawable {
   MaterialButtonBackgroundDrawable(ColorStateList var1, InsetDrawable var2, Drawable var3) {
      super(var1, var2, var3);
   }

   public void setColorFilter(ColorFilter var1) {
      if (this.getDrawable(0) != null) {
         ((GradientDrawable)((LayerDrawable)((InsetDrawable)this.getDrawable(0)).getDrawable()).getDrawable(0)).setColorFilter(var1);
      }

   }
}
