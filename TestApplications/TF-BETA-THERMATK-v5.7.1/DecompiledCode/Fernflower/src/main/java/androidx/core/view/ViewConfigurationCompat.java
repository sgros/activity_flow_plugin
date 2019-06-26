package androidx.core.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import java.lang.reflect.Method;

public final class ViewConfigurationCompat {
   private static Method sGetScaledScrollFactorMethod;

   static {
      if (VERSION.SDK_INT == 25) {
         try {
            sGetScaledScrollFactorMethod = ViewConfiguration.class.getDeclaredMethod("getScaledScrollFactor");
         } catch (Exception var1) {
            Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
         }
      }

   }

   private static float getLegacyScrollFactor(ViewConfiguration var0, Context var1) {
      if (VERSION.SDK_INT >= 25) {
         Method var2 = sGetScaledScrollFactorMethod;
         if (var2 != null) {
            label18: {
               int var3;
               try {
                  var3 = (Integer)var2.invoke(var0);
               } catch (Exception var4) {
                  Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
                  break label18;
               }

               return (float)var3;
            }
         }
      }

      TypedValue var5 = new TypedValue();
      return var1.getTheme().resolveAttribute(16842829, var5, true) ? var5.getDimension(var1.getResources().getDisplayMetrics()) : 0.0F;
   }

   public static float getScaledHorizontalScrollFactor(ViewConfiguration var0, Context var1) {
      return VERSION.SDK_INT >= 26 ? var0.getScaledHorizontalScrollFactor() : getLegacyScrollFactor(var0, var1);
   }

   public static int getScaledHoverSlop(ViewConfiguration var0) {
      return VERSION.SDK_INT >= 28 ? var0.getScaledHoverSlop() : var0.getScaledTouchSlop() / 2;
   }

   public static float getScaledVerticalScrollFactor(ViewConfiguration var0, Context var1) {
      return VERSION.SDK_INT >= 26 ? var0.getScaledVerticalScrollFactor() : getLegacyScrollFactor(var0, var1);
   }

   public static boolean shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration var0, Context var1) {
      if (VERSION.SDK_INT >= 28) {
         return var0.shouldShowMenuShortcutsWhenKeyboardPresent();
      } else {
         Resources var4 = var1.getResources();
         int var2 = var4.getIdentifier("config_showMenuShortcutsWhenKeyboardPresent", "bool", "android");
         boolean var3;
         if (var2 != 0 && var4.getBoolean(var2)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
