package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.widget.ImageView;

public class ImageViewCompat {
   public static ColorStateList getImageTintList(ImageView var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getImageTintList();
      } else {
         ColorStateList var1;
         if (var0 instanceof TintableImageSourceView) {
            var1 = ((TintableImageSourceView)var0).getSupportImageTintList();
         } else {
            var1 = null;
         }

         return var1;
      }
   }

   public static Mode getImageTintMode(ImageView var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getImageTintMode();
      } else {
         Mode var1;
         if (var0 instanceof TintableImageSourceView) {
            var1 = ((TintableImageSourceView)var0).getSupportImageTintMode();
         } else {
            var1 = null;
         }

         return var1;
      }
   }

   public static void setImageTintList(ImageView var0, ColorStateList var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setImageTintList(var1);
         if (VERSION.SDK_INT == 21) {
            Drawable var3 = var0.getDrawable();
            boolean var2;
            if (var0.getImageTintList() != null && var0.getImageTintMode() != null) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (var3 != null && var2) {
               if (var3.isStateful()) {
                  var3.setState(var0.getDrawableState());
               }

               var0.setImageDrawable(var3);
            }
         }
      } else if (var0 instanceof TintableImageSourceView) {
         ((TintableImageSourceView)var0).setSupportImageTintList(var1);
      }

   }

   public static void setImageTintMode(ImageView var0, Mode var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setImageTintMode(var1);
         if (VERSION.SDK_INT == 21) {
            Drawable var3 = var0.getDrawable();
            boolean var2;
            if (var0.getImageTintList() != null && var0.getImageTintMode() != null) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (var3 != null && var2) {
               if (var3.isStateful()) {
                  var3.setState(var0.getDrawableState());
               }

               var0.setImageDrawable(var3);
            }
         }
      } else if (var0 instanceof TintableImageSourceView) {
         ((TintableImageSourceView)var0).setSupportImageTintMode(var1);
      }

   }
}
