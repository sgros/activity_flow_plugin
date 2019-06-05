package com.airbnb.lottie.network;

import android.content.Context;
import android.support.v4.util.Pair;
import com.airbnb.lottie.L;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class NetworkCache {
   private final Context appContext;
   private final String url;

   NetworkCache(Context var1, String var2) {
      this.appContext = var1.getApplicationContext();
      this.url = var2;
   }

   private static String filenameForUrl(String var0, FileExtension var1, boolean var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("lottie_cache_");
      var3.append(var0.replaceAll("\\W+", ""));
      if (var2) {
         var0 = var1.extension;
      } else {
         var0 = var1.tempExtension();
      }

      var3.append(var0);
      return var3.toString();
   }

   private File getCachedFile(String var1) throws FileNotFoundException {
      File var2 = new File(this.appContext.getCacheDir(), filenameForUrl(var1, FileExtension.Json, false));
      if (var2.exists()) {
         return var2;
      } else {
         File var3 = new File(this.appContext.getCacheDir(), filenameForUrl(var1, FileExtension.Zip, false));
         return var3.exists() ? var3 : null;
      }
   }

   Pair fetch() {
      File var1;
      try {
         var1 = this.getCachedFile(this.url);
      } catch (FileNotFoundException var6) {
         return null;
      }

      if (var1 == null) {
         return null;
      } else {
         FileInputStream var2;
         try {
            var2 = new FileInputStream(var1);
         } catch (FileNotFoundException var5) {
            return null;
         }

         FileExtension var3;
         if (var1.getAbsolutePath().endsWith(".zip")) {
            var3 = FileExtension.Zip;
         } else {
            var3 = FileExtension.Json;
         }

         StringBuilder var4 = new StringBuilder();
         var4.append("Cache hit for ");
         var4.append(this.url);
         var4.append(" at ");
         var4.append(var1.getAbsolutePath());
         L.debug(var4.toString());
         return new Pair(var3, var2);
      }
   }

   void renameTempFile(FileExtension var1) {
      String var5 = filenameForUrl(this.url, var1, true);
      File var2 = new File(this.appContext.getCacheDir(), var5);
      File var6 = new File(var2.getAbsolutePath().replace(".temp", ""));
      boolean var3 = var2.renameTo(var6);
      StringBuilder var4 = new StringBuilder();
      var4.append("Copying temp file to real file (");
      var4.append(var6);
      var4.append(")");
      L.debug(var4.toString());
      if (!var3) {
         var4 = new StringBuilder();
         var4.append("Unable to rename cache file ");
         var4.append(var2.getAbsolutePath());
         var4.append(" to ");
         var4.append(var6.getAbsolutePath());
         var4.append(".");
         L.warn(var4.toString());
      }

   }

   File writeTempCacheFile(InputStream var1, FileExtension var2) throws IOException {
      String var62 = filenameForUrl(this.url, var2, true);
      File var3 = new File(this.appContext.getCacheDir(), var62);

      label442: {
         Throwable var10000;
         label446: {
            boolean var10001;
            FileOutputStream var63;
            try {
               var63 = new FileOutputStream(var3);
            } catch (Throwable var61) {
               var10000 = var61;
               var10001 = false;
               break label446;
            }

            label447: {
               label436: {
                  byte[] var4;
                  try {
                     var4 = new byte[1024];
                  } catch (Throwable var60) {
                     var10000 = var60;
                     var10001 = false;
                     break label436;
                  }

                  while(true) {
                     int var5;
                     try {
                        var5 = var1.read(var4);
                     } catch (Throwable var58) {
                        var10000 = var58;
                        var10001 = false;
                        break;
                     }

                     if (var5 == -1) {
                        try {
                           var63.flush();
                           break label447;
                        } catch (Throwable var57) {
                           var10000 = var57;
                           var10001 = false;
                           break;
                        }
                     }

                     try {
                        var63.write(var4, 0, var5);
                     } catch (Throwable var59) {
                        var10000 = var59;
                        var10001 = false;
                        break;
                     }
                  }
               }

               Throwable var65 = var10000;

               try {
                  var63.close();
                  throw var65;
               } catch (Throwable var55) {
                  var10000 = var55;
                  var10001 = false;
                  break label446;
               }
            }

            label422:
            try {
               var63.close();
               break label442;
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label422;
            }
         }

         Throwable var64 = var10000;
         var1.close();
         throw var64;
      }

      var1.close();
      return var3;
   }
}
