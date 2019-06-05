package kotlinx.coroutines.experimental.android;

import android.os.Handler;
import android.os.Looper;

public final class HandlerContextKt {
   private static final HandlerContext UI = new HandlerContext(new Handler(Looper.getMainLooper()), "UI");

   public static final HandlerContext getUI() {
      return UI;
   }
}
