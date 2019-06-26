package android.support.v4.text;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@TargetApi(23)
@RequiresApi(23)
class ICUCompatApi23 {
   private static final String TAG = "ICUCompatIcs";
   private static Method sAddLikelySubtagsMethod;

   static {
      try {
         sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
      } catch (Exception var1) {
         throw new IllegalStateException(var1);
      }
   }

   public static String maximizeAndGetScript(Locale var0) {
      String var1;
      String var4;
      label23: {
         try {
            var1 = ((Locale)sAddLikelySubtagsMethod.invoke((Object)null, var0)).getScript();
            break label23;
         } catch (InvocationTargetException var2) {
            Log.w("ICUCompatIcs", var2);
         } catch (IllegalAccessException var3) {
            Log.w("ICUCompatIcs", var3);
         }

         var4 = var0.getScript();
         return var4;
      }

      var4 = var1;
      return var4;
   }
}
