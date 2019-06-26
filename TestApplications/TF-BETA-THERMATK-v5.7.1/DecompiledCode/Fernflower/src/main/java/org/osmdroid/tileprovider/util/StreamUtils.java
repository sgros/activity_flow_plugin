package org.osmdroid.tileprovider.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
   public static void closeStream(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var1) {
            var1.printStackTrace();
         }
      }

   }

   public static long copy(InputStream var0, OutputStream var1) throws IOException {
      byte[] var2 = new byte[8192];
      long var3 = 0L;

      while(true) {
         int var5 = var0.read(var2);
         if (var5 == -1) {
            return var3;
         }

         var1.write(var2, 0, var5);
         var3 += (long)var5;
      }
   }
}
