package android.support.v4.text;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Locale;

public final class TextUtilsCompat {
   private static final String ARAB_SCRIPT_SUBTAG = "Arab";
   private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
   @Deprecated
   public static final Locale ROOT = new Locale("", "");

   private TextUtilsCompat() {
   }

   private static int getLayoutDirectionFromFirstChar(@NonNull Locale var0) {
      switch(Character.getDirectionality(var0.getDisplayName(var0).charAt(0))) {
      case 1:
      case 2:
         return 1;
      default:
         return 0;
      }
   }

   public static int getLayoutDirectionFromLocale(@Nullable Locale var0) {
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

   @NonNull
   public static String htmlEncode(@NonNull String var0) {
      if (VERSION.SDK_INT >= 17) {
         return TextUtils.htmlEncode(var0);
      } else {
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var3 = var0.charAt(var2);
            if (var3 != '"') {
               if (var3 != '<') {
                  if (var3 != '>') {
                     switch(var3) {
                     case '&':
                        var1.append("&amp;");
                        break;
                     case '\'':
                        var1.append("&#39;");
                        break;
                     default:
                        var1.append(var3);
                     }
                  } else {
                     var1.append("&gt;");
                  }
               } else {
                  var1.append("&lt;");
               }
            } else {
               var1.append("&quot;");
            }
         }

         return var1.toString();
      }
   }
}
