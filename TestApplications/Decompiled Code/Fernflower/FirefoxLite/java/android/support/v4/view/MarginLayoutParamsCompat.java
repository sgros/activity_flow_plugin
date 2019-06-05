package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewGroup.MarginLayoutParams;

public final class MarginLayoutParamsCompat {
   public static int getMarginEnd(MarginLayoutParams var0) {
      return VERSION.SDK_INT >= 17 ? var0.getMarginEnd() : var0.rightMargin;
   }

   public static int getMarginStart(MarginLayoutParams var0) {
      return VERSION.SDK_INT >= 17 ? var0.getMarginStart() : var0.leftMargin;
   }

   public static void setMarginEnd(MarginLayoutParams var0, int var1) {
      if (VERSION.SDK_INT >= 17) {
         var0.setMarginEnd(var1);
      } else {
         var0.rightMargin = var1;
      }

   }
}
