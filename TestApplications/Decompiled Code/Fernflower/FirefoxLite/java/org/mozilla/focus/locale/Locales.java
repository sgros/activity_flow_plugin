package org.mozilla.focus.locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.util.Locale;

public class Locales {
   public static String getLanguage(Locale var0) {
      String var1 = var0.getLanguage();
      if (var1.equals("iw")) {
         return "he";
      } else if (var1.equals("in")) {
         return "id";
      } else {
         return var1.equals("ji") ? "yi" : var1;
      }
   }

   public static String getLanguageTag(Locale var0) {
      String var1 = getLanguage(var0);
      String var3 = var0.getCountry();
      if (var3.equals("")) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1);
         var2.append("-");
         var2.append(var3);
         return var2.toString();
      }
   }

   public static Resources getLocalizedResources(Context var0) {
      Resources var1 = var0.getResources();
      Locale var2 = LocaleManager.getInstance().getCurrentLocale(var0);
      Locale var3 = var1.getConfiguration().locale;
      if (var2 != null && var3 != null) {
         if (var2.toLanguageTag().equals(var3.toLanguageTag())) {
            return var1;
         } else {
            Configuration var4 = new Configuration(var1.getConfiguration());
            var4.setLocale(var2);
            return var0.createConfigurationContext(var4).getResources();
         }
      } else {
         return var1;
      }
   }

   public static void initializeLocale(Context var0) {
      LocaleManager var1 = LocaleManager.getInstance();
      ThreadPolicy var2 = StrictMode.allowThreadDiskReads();
      StrictMode.allowThreadDiskWrites();

      try {
         var1.getAndApplyPersistedLocale(var0);
      } finally {
         StrictMode.setThreadPolicy(var2);
      }

   }

   public static Locale parseLocaleCode(String var0) {
      int var1 = var0.indexOf(45);
      int var2 = var1;
      if (var1 == -1) {
         var2 = var0.indexOf(95);
         if (var2 == -1) {
            return new Locale(var0);
         }
      }

      return new Locale(var0.substring(0, var2), var0.substring(var2 + 1));
   }
}
