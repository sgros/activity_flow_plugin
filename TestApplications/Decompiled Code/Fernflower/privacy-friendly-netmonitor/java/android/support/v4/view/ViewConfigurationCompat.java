package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import java.lang.reflect.Method;

@Deprecated
public final class ViewConfigurationCompat {
   private static final String TAG = "ViewConfigCompat";
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

   private ViewConfigurationCompat() {
   }

   private static float getLegacyScrollFactor(ViewConfiguration var0, Context var1) {
      if (VERSION.SDK_INT >= 25 && sGetScaledScrollFactorMethod != null) {
         label18: {
            int var2;
            try {
               var2 = (Integer)sGetScaledScrollFactorMethod.invoke(var0);
            } catch (Exception var3) {
               Log.i("ViewConfigCompat", "Could not find method getScaledScrollFactor() on ViewConfiguration");
               break label18;
            }

            return (float)var2;
         }
      }

      TypedValue var4 = new TypedValue();
      return var1.getTheme().resolveAttribute(16842829, var4, true) ? var4.getDimension(var1.getResources().getDisplayMetrics()) : 0.0F;
   }

   public static float getScaledHorizontalScrollFactor(@NonNull ViewConfiguration var0, @NonNull Context var1) {
      return VERSION.SDK_INT >= 26 ? var0.getScaledHorizontalScrollFactor() : getLegacyScrollFactor(var0, var1);
   }

   @Deprecated
   public static int getScaledPagingTouchSlop(ViewConfiguration var0) {
      return var0.getScaledPagingTouchSlop();
   }

   public static float getScaledVerticalScrollFactor(@NonNull ViewConfiguration var0, @NonNull Context var1) {
      return VERSION.SDK_INT >= 26 ? var0.getScaledVerticalScrollFactor() : getLegacyScrollFactor(var0, var1);
   }

   @Deprecated
   public static boolean hasPermanentMenuKey(ViewConfiguration var0) {
      return var0.hasPermanentMenuKey();
   }
}
