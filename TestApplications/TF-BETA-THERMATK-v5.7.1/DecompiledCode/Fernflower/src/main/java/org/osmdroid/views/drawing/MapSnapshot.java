package org.osmdroid.views.drawing;

import android.os.Looper;

public class MapSnapshot implements Runnable {
   public static boolean isUIThread() {
      boolean var0;
      if (Looper.myLooper() == Looper.getMainLooper()) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }
}
