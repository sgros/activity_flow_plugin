package com.airbnb.lottie.utils;

import android.util.Log;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieLogger;
import java.util.HashSet;
import java.util.Set;

public class LogcatLogger implements LottieLogger {
   private static final Set loggedMessages = new HashSet();

   public void debug(String var1) {
      this.debug(var1, (Throwable)null);
   }

   public void debug(String var1, Throwable var2) {
      if (L.DBG) {
         Log.d("LOTTIE", var1, var2);
      }

   }

   public void warning(String var1) {
      this.warning(var1, (Throwable)null);
   }

   public void warning(String var1, Throwable var2) {
      if (!loggedMessages.contains(var1)) {
         Log.w("LOTTIE", var1, var2);
         loggedMessages.add(var1);
      }
   }
}
