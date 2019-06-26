package androidx.core.text;

import android.os.Build.VERSION;
import android.text.TextUtils;
import java.util.Locale;

public final class TextUtilsCompat {
   private static final Locale ROOT = new Locale("", "");

   private static int getLayoutDirectionFromFirstChar(Locale var0) {
      byte var1 = Character.getDirectionality(var0.getDisplayName(var0).charAt(0));
      return var1 != 1 && var1 != 2 ? 0 : 1;
   }

   public static int getLayoutDirectionFromLocale(Locale var0) {
      if (VERSION.SDK_INT >= 17) {
         return TextUtils.getLayoutDirectionFromLocale(var0);
      } else {
         if (var0 != null && !var0.equals(ROOT)) {
            String var1 = ICUCompat.maximizeAndGetScript(var0);
            if (var1 == null) {
               return getLayoutDirectionFromFirstChar(var0);
            }

            if (var1.equalsIgnoreCase("Arab") || var1.equalsIgnoreCase("Hebr")) {
               return 1;
            }
         }

         return 0;
      }
   }
}
