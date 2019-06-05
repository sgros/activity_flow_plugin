package com.bumptech.glide.disklrucache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

final class Util {
   static final Charset US_ASCII = Charset.forName("US-ASCII");
   static final Charset UTF_8 = Charset.forName("UTF-8");

   static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }

   }

   static void deleteContents(File var0) throws IOException {
      File[] var1 = var0.listFiles();
      StringBuilder var4;
      if (var1 != null) {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            var0 = var1[var3];
            if (var0.isDirectory()) {
               deleteContents(var0);
            }

            if (!var0.delete()) {
               var4 = new StringBuilder();
               var4.append("failed to delete file: ");
               var4.append(var0);
               throw new IOException(var4.toString());
            }
         }

      } else {
         var4 = new StringBuilder();
         var4.append("not a readable directory: ");
         var4.append(var0);
         throw new IOException(var4.toString());
      }
   }
}
