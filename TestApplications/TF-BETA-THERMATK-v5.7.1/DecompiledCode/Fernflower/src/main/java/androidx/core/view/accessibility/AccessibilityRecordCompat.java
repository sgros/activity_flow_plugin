package androidx.core.view.accessibility;

import android.os.Build.VERSION;
import android.view.View;
import android.view.accessibility.AccessibilityRecord;

public class AccessibilityRecordCompat {
   public static void setMaxScrollX(AccessibilityRecord var0, int var1) {
      if (VERSION.SDK_INT >= 15) {
         var0.setMaxScrollX(var1);
      }

   }

   public static void setMaxScrollY(AccessibilityRecord var0, int var1) {
      if (VERSION.SDK_INT >= 15) {
         var0.setMaxScrollY(var1);
      }

   }

   public static void setSource(AccessibilityRecord var0, View var1, int var2) {
      if (VERSION.SDK_INT >= 16) {
         var0.setSource(var1, var2);
      }

   }
}
