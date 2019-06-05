package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityEvent;

public final class AccessibilityEventCompat {
   public static int getContentChangeTypes(AccessibilityEvent var0) {
      return VERSION.SDK_INT >= 19 ? var0.getContentChangeTypes() : 0;
   }

   public static void setContentChangeTypes(AccessibilityEvent var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.setContentChangeTypes(var1);
      }

   }
}
