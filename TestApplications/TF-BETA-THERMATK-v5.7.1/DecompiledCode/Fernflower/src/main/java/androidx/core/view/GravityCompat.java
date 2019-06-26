package androidx.core.view;

import android.os.Build.VERSION;
import android.view.Gravity;

public final class GravityCompat {
   public static int getAbsoluteGravity(int var0, int var1) {
      return VERSION.SDK_INT >= 17 ? Gravity.getAbsoluteGravity(var0, var1) : var0 & -8388609;
   }
}
