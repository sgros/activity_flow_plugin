package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

public final class CompoundButtonCompat {
   private static Field sButtonDrawableField;
   private static boolean sButtonDrawableFieldFetched;

   public static Drawable getButtonDrawable(CompoundButton var0) {
      if (VERSION.SDK_INT >= 23) {
         return var0.getButtonDrawable();
      } else {
         if (!sButtonDrawableFieldFetched) {
            try {
               sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable");
               sButtonDrawableField.setAccessible(true);
            } catch (NoSuchFieldException var2) {
               Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", var2);
            }

            sButtonDrawableFieldFetched = true;
         }

         if (sButtonDrawableField != null) {
            try {
               Drawable var4 = (Drawable)sButtonDrawableField.get(var0);
               return var4;
            } catch (IllegalAccessException var3) {
               Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", var3);
               sButtonDrawableField = null;
            }
         }

         return null;
      }
   }

   public static void setButtonTintList(CompoundButton var0, ColorStateList var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setButtonTintList(var1);
      } else if (var0 instanceof TintableCompoundButton) {
         ((TintableCompoundButton)var0).setSupportButtonTintList(var1);
      }

   }

   public static void setButtonTintMode(CompoundButton var0, Mode var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setButtonTintMode(var1);
      } else if (var0 instanceof TintableCompoundButton) {
         ((TintableCompoundButton)var0).setSupportButtonTintMode(var1);
      }

   }
}
