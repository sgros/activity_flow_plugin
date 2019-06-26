package androidx.appcompat.widget;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Method;

public class ViewUtils {
   private static Method sComputeFitSystemWindowsMethod;

   static {
      if (VERSION.SDK_INT >= 18) {
         try {
            sComputeFitSystemWindowsMethod = View.class.getDeclaredMethod("computeFitSystemWindows", Rect.class, Rect.class);
            if (!sComputeFitSystemWindowsMethod.isAccessible()) {
               sComputeFitSystemWindowsMethod.setAccessible(true);
            }
         } catch (NoSuchMethodException var1) {
            Log.d("ViewUtils", "Could not find method computeFitSystemWindows. Oh well.");
         }
      }

   }

   public static void computeFitSystemWindows(View var0, Rect var1, Rect var2) {
      Method var3 = sComputeFitSystemWindowsMethod;
      if (var3 != null) {
         try {
            var3.invoke(var0, var1, var2);
         } catch (Exception var4) {
            Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", var4);
         }
      }

   }

   public static boolean isLayoutRtl(View var0) {
      int var1 = ViewCompat.getLayoutDirection(var0);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }
}
