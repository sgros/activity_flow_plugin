package org.telegram.messenger.voip;

import android.text.TextUtils;
import java.io.PrintWriter;
import java.io.StringWriter;

class VLog {
   public static native void d(String var0);

   public static native void e(String var0);

   public static void e(String var0, Throwable var1) {
      StringWriter var2 = new StringWriter();
      if (!TextUtils.isEmpty(var0)) {
         var2.append(var0);
         var2.append(": ");
      }

      var1.printStackTrace(new PrintWriter(var2));
      String[] var5 = var2.toString().split("\n");
      int var3 = var5.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         e(var5[var4]);
      }

   }

   public static void e(Throwable var0) {
      e((String)null, var0);
   }

   public static native void i(String var0);

   public static native void v(String var0);

   public static native void w(String var0);
}
