package android.support.v4.graphics;

import android.graphics.Paint;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

public final class PaintCompat {
   private PaintCompat() {
   }

   public static boolean hasGlyph(@NonNull Paint var0, @NonNull String var1) {
      boolean var2;
      if (VERSION.SDK_INT >= 23) {
         var2 = PaintCompatApi23.hasGlyph(var0, var1);
      } else {
         var2 = PaintCompatGingerbread.hasGlyph(var0, var1);
      }

      return var2;
   }
}
