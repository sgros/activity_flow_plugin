package org.mozilla.focus.utils;

import android.database.Cursor;

public class CursorUtils {
   public static void closeCursorSafely(Cursor var0) {
      if (var0 != null && !var0.isClosed()) {
         var0.close();
      }

   }
}
