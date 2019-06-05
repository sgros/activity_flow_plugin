package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class ExecCom extends Thread {
   public static void closeSilently(Object... var0) {
      int var1 = 0;

      for(int var2 = var0.length; var1 < var2; ++var1) {
         Object var3 = var0[var1];
         if (var3 != null) {
            try {
               if (!(var3 instanceof Closeable)) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("cannot close: ");
                  var4.append(var3);
                  Log.d("NetMonitor", var4.toString());
                  var4 = new StringBuilder();
                  var4.append("cannot close ");
                  var4.append(var3);
                  RuntimeException var5 = new RuntimeException(var4.toString());
                  throw var5;
               }

               ((Closeable)var3).close();
            } catch (Throwable var6) {
               var6.printStackTrace();
            }
         }
      }

   }

   public static String readFully(InputStream var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      byte[] var2 = new byte[1024];

      while(true) {
         int var3 = var0.read(var2);
         if (var3 == -1) {
            return var1.toString("UTF-8");
         }

         var1.write(var2, 0, var3);
      }
   }

   static void user(String var0) {
      try {
         Process var3 = Runtime.getRuntime().exec(var0);

         try {
            var3.waitFor();
         } catch (InterruptedException var1) {
            var1.printStackTrace();
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static String userForResult(String param0) {
      // $FF: Couldn't be decompiled
   }
}
