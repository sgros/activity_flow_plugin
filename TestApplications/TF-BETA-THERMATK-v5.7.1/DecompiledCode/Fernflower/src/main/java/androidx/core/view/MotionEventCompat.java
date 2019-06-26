package androidx.core.view;

import android.view.MotionEvent;

public final class MotionEventCompat {
   public static boolean isFromSource(MotionEvent var0, int var1) {
      boolean var2;
      if ((var0.getSource() & var1) == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
