package menion.android.whereyougo.preferences;

import menion.android.whereyougo.MainApplication;

public class Locale {
   public static String getString(int var0) {
      String var1;
      if (MainApplication.getContext() != null) {
         var1 = MainApplication.getContext().getString(var0);
      } else {
         var1 = "";
      }

      return var1;
   }

   public static String getString(int var0, Object... var1) {
      String var2;
      if (MainApplication.getContext() != null) {
         var2 = MainApplication.getContext().getString(var0, var1);
      } else {
         var2 = "";
      }

      return var2;
   }
}
