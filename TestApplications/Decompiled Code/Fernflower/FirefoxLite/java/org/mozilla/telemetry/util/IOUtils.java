package org.mozilla.telemetry.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
   public static void safeClose(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var1) {
         }
      }

   }
}
