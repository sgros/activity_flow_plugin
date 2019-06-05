package org.mapsforge.core.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class IOUtils {
   private static final Logger LOGGER = Logger.getLogger(IOUtils.class.getName());

   private IOUtils() {
      throw new IllegalStateException();
   }

   public static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var1) {
            LOGGER.log(Level.FINE, var1.getMessage(), var1);
         }
      }

   }
}
