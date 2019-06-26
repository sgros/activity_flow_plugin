package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
   private static Method sAddLikelySubtagsMethod;
   private static Method sGetScriptMethod;

   static {
      int var0 = VERSION.SDK_INT;
      if (var0 < 21) {
         label31: {
            Exception var10000;
            label34: {
               Class var1;
               boolean var10001;
               try {
                  var1 = Class.forName("libcore.icu.ICU");
               } catch (Exception var4) {
                  var10000 = var4;
                  var10001 = false;
                  break label34;
               }

               if (var1 == null) {
                  break label31;
               }

               try {
                  sGetScriptMethod = var1.getMethod("getScript", String.class);
                  sAddLikelySubtagsMethod = var1.getMethod("addLikelySubtags", String.class);
                  break label31;
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
               }
            }

            Exception var5 = var10000;
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w("ICUCompat", var5);
         }
      } else if (var0 < 24) {
         try {
            sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
         } catch (Exception var2) {
            throw new IllegalStateException(var2);
         }
      }

   }

   private static String addLikelySubtags(Locale var0) {
      String var4 = var0.toString();

      try {
         if (sAddLikelySubtagsMethod != null) {
            String var1 = (String)sAddLikelySubtagsMethod.invoke((Object)null, var4);
            return var1;
         }
      } catch (IllegalAccessException var2) {
         Log.w("ICUCompat", var2);
      } catch (InvocationTargetException var3) {
         Log.w("ICUCompat", var3);
      }

      return var4;
   }

   private static String getScript(String var0) {
      try {
         if (sGetScriptMethod != null) {
            var0 = (String)sGetScriptMethod.invoke((Object)null, var0);
            return var0;
         }
      } catch (IllegalAccessException var1) {
         Log.w("ICUCompat", var1);
      } catch (InvocationTargetException var2) {
         Log.w("ICUCompat", var2);
      }

      return null;
   }

   public static String maximizeAndGetScript(Locale var0) {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 24) {
         return ULocale.addLikelySubtags(ULocale.forLocale(var0)).getScript();
      } else if (var1 >= 21) {
         try {
            String var2 = ((Locale)sAddLikelySubtagsMethod.invoke((Object)null, var0)).getScript();
            return var2;
         } catch (InvocationTargetException var3) {
            Log.w("ICUCompat", var3);
         } catch (IllegalAccessException var4) {
            Log.w("ICUCompat", var4);
         }

         return var0.getScript();
      } else {
         String var5 = addLikelySubtags(var0);
         return var5 != null ? getScript(var5) : null;
      }
   }
}
