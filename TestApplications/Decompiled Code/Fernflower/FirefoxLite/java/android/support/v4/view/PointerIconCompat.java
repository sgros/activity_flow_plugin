package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.PointerIcon;

public final class PointerIconCompat {
   private Object mPointerIcon;

   private PointerIconCompat(Object var1) {
      this.mPointerIcon = var1;
   }

   public static PointerIconCompat getSystemIcon(Context var0, int var1) {
      return VERSION.SDK_INT >= 24 ? new PointerIconCompat(PointerIcon.getSystemIcon(var0, var1)) : new PointerIconCompat((Object)null);
   }

   public Object getPointerIcon() {
      return this.mPointerIcon;
   }
}
