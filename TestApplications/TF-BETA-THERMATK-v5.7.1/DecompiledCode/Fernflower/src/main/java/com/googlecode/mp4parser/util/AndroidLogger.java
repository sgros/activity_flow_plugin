package com.googlecode.mp4parser.util;

import android.util.Log;

public class AndroidLogger extends Logger {
   String name;

   public AndroidLogger(String var1) {
      this.name = var1;
   }

   public void logDebug(String var1) {
      StringBuilder var2 = new StringBuilder(String.valueOf(this.name));
      var2.append(":");
      var2.append(var1);
      Log.d("isoparser", var2.toString());
   }
}
