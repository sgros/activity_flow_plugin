package org.osmdroid.tileprovider.util;

import android.os.Environment;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StorageUtils {
   private static Set getAllStorageLocationsRevised() {
      // $FF: Couldn't be decompiled
   }

   public static File getStorage() {
      List var0 = getStorageList();
      int var1 = 0;

      StorageUtils.StorageInfo var2;
      StorageUtils.StorageInfo var4;
      for(var2 = null; var1 < var0.size(); var2 = var4) {
         StorageUtils.StorageInfo var3 = (StorageUtils.StorageInfo)var0.get(var1);
         var4 = var2;
         if (!var3.readonly) {
            var4 = var2;
            if (isWritable(new File(var3.path))) {
               label38: {
                  if (var2 != null) {
                     var4 = var2;
                     if (var2.freeSpace >= var3.freeSpace) {
                        break label38;
                     }
                  }

                  var4 = var3;
               }
            }
         }

         ++var1;
      }

      if (var2 != null) {
         return new File(var2.path);
      } else {
         try {
            File var6 = Environment.getExternalStorageDirectory();
            return var6;
         } catch (Exception var5) {
            return null;
         }
      }
   }

   public static List getStorageList() {
      // $FF: Couldn't be decompiled
   }

   public static boolean isWritable(File var0) {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append(var0.getAbsolutePath());
         var2.append(File.separator);
         var2.append("osm.tmp");
         File var4 = new File(var2.toString());
         FileOutputStream var5 = new FileOutputStream(var4);
         var5.write("hi".getBytes());
         var5.close();
         var2 = new StringBuilder();
         var2.append(var0.getAbsolutePath());
         var2.append(" is writable");
         Log.i("StorageUtils", var2.toString());
         var4.delete();
         return true;
      } catch (Throwable var3) {
         StringBuilder var1 = new StringBuilder();
         var1.append(var0.getAbsolutePath());
         var1.append(" is NOT writable");
         Log.i("StorageUtils", var1.toString());
         return false;
      }
   }

   public static class StorageInfo {
      String displayName = "";
      public final int display_number;
      public long freeSpace = 0L;
      public final boolean internal;
      public final String path;
      public boolean readonly;

      public StorageInfo(String var1, boolean var2, boolean var3, int var4) {
         this.path = var1;
         this.internal = var2;
         this.display_number = var4;
         if (VERSION.SDK_INT >= 9) {
            this.freeSpace = (new File(var1)).getFreeSpace();
         }

         StringBuilder var5;
         if (!var3) {
            var5 = new StringBuilder();
            var5.append(var1);
            var5.append(File.separator);
            var5.append(UUID.randomUUID().toString());
            File var7 = new File(var5.toString());

            try {
               var7.createNewFile();
               var7.delete();
               this.readonly = false;
            } catch (Throwable var6) {
               this.readonly = true;
            }
         } else {
            this.readonly = var3;
         }

         var5 = new StringBuilder();
         if (var2) {
            var5.append("Internal SD card");
         } else if (var4 > 1) {
            StringBuilder var8 = new StringBuilder();
            var8.append("SD card ");
            var8.append(var4);
            var5.append(var8.toString());
         } else {
            var5.append("SD card");
         }

         if (var3) {
            var5.append(" (Read only)");
         }

         this.displayName = var5.toString();
      }
   }
}
