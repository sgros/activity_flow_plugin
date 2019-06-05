package android.support.v4.widget;

import android.os.Build.VERSION;

public interface AutoSizeableTextView {
   boolean PLATFORM_SUPPORTS_AUTOSIZE;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 27) {
         var0 = true;
      } else {
         var0 = false;
      }

      PLATFORM_SUPPORTS_AUTOSIZE = var0;
   }
}
