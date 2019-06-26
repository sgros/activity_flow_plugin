package androidx.core.view;

import android.os.Build.VERSION;
import android.view.ViewGroup.MarginLayoutParams;

public final class MarginLayoutParamsCompat {
   public static int getMarginEnd(MarginLayoutParams var0) {
      return VERSION.SDK_INT >= 17 ? var0.getMarginEnd() : var0.rightMargin;
   }

   public static int getMarginStart(MarginLayoutParams var0) {
      return VERSION.SDK_INT >= 17 ? var0.getMarginStart() : var0.leftMargin;
   }
}
