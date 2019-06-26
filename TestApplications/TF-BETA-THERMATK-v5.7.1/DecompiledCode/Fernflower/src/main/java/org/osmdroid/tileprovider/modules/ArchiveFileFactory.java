package org.osmdroid.tileprovider.modules;

import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ArchiveFileFactory {
   static Map extensionMap = new HashMap();

   static {
      extensionMap.put("zip", ZipFileArchive.class);
      if (VERSION.SDK_INT >= 10) {
         extensionMap.put("sqlite", DatabaseFileArchive.class);
         extensionMap.put("mbtiles", MBTilesFileArchive.class);
         extensionMap.put("gemf", GEMFFileArchive.class);
      }

   }

   public static IArchiveFile getArchiveFile(File var0) {
      String var1 = var0.getName();
      String var2 = var1;
      if (var1.contains(".")) {
         try {
            var2 = var1.substring(var1.lastIndexOf(".") + 1);
         } catch (Exception var6) {
            var2 = var1;
         }
      }

      Class var7 = (Class)extensionMap.get(var2.toLowerCase());
      if (var7 != null) {
         StringBuilder var8;
         try {
            IArchiveFile var9 = (IArchiveFile)var7.newInstance();
            var9.init(var0);
            return var9;
         } catch (InstantiationException var3) {
            var8 = new StringBuilder();
            var8.append("Error initializing archive file provider ");
            var8.append(var0.getAbsolutePath());
            Log.e("OsmDroid", var8.toString(), var3);
         } catch (IllegalAccessException var4) {
            var8 = new StringBuilder();
            var8.append("Error initializing archive file provider ");
            var8.append(var0.getAbsolutePath());
            Log.e("OsmDroid", var8.toString(), var4);
         } catch (Exception var5) {
            var8 = new StringBuilder();
            var8.append("Error opening archive file ");
            var8.append(var0.getAbsolutePath());
            Log.e("OsmDroid", var8.toString(), var5);
         }
      }

      return null;
   }
}
