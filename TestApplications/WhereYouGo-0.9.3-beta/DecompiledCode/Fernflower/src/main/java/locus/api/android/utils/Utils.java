package locus.api.android.utils;

import android.database.Cursor;
import locus.api.utils.Logger;

public class Utils extends locus.api.utils.Utils {
   private static final String TAG = Utils.class.getSimpleName();

   public static void closeQuietly(Cursor var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Exception var2) {
            Logger.logE(TAG, "closeQuietly(" + var0 + "), e:" + var2);
            var2.printStackTrace();
         }
      }

   }
}
