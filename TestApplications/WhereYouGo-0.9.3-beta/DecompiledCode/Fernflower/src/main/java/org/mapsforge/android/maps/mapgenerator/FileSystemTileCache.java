package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.android.AndroidUtils;

public class FileSystemTileCache implements TileCache {
   private static final String CACHE_DIRECTORY = "/Android/data/org.mapsforge.android.maps/cache/";
   private static final String IMAGE_FILE_NAME_EXTENSION = ".tile";
   private static final float LOAD_FACTOR = 0.6F;
   private static final Logger LOGGER = Logger.getLogger(FileSystemTileCache.class.getName());
   private static final String SERIALIZATION_FILE_NAME = "cache.ser";
   private static final int TILE_SIZE_IN_BYTES = 131072;
   private final Bitmap bitmapGet;
   private final ByteBuffer byteBuffer;
   private File cacheDirectory;
   private long cacheId;
   private int capacity;
   private Map map;
   private int mapViewId;
   private boolean persistent;

   public FileSystemTileCache(int var1, int var2) {
      this.mapViewId = var2;
      this.capacity = this.checkCapacity(var1);
      if (this.capacity > 0 && this.cacheDirectory != null) {
         Map var3 = deserializeMap(this.cacheDirectory);
         if (var3 == null) {
            this.map = createMap(this.capacity);
         } else {
            this.map = var3;
         }

         this.byteBuffer = ByteBuffer.allocate(131072);
         this.bitmapGet = Bitmap.createBitmap(256, 256, Config.RGB_565);
      } else {
         this.byteBuffer = null;
         this.bitmapGet = null;
         this.map = createMap(0);
      }

   }

   private int checkCapacity(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("capacity must not be negative: " + var1);
      } else {
         if (AndroidUtils.applicationRunsOnAndroidEmulator()) {
            var1 = 0;
         } else if (!"mounted".equals(Environment.getExternalStorageState())) {
            var1 = 0;
         } else {
            this.cacheDirectory = this.createCacheDirectory();
            if (this.cacheDirectory == null) {
               var1 = 0;
            }
         }

         return var1;
      }
   }

   private File createCacheDirectory() {
      String var1 = Environment.getExternalStorageDirectory().getAbsolutePath();
      File var2 = new File(var1 + "/Android/data/org.mapsforge.android.maps/cache/" + this.mapViewId);
      File var3;
      if (!var2.exists() && !var2.mkdirs()) {
         LOGGER.log(Level.SEVERE, "could not create directory: ", var2);
         var3 = null;
      } else if (!var2.isDirectory()) {
         LOGGER.log(Level.SEVERE, "not a directory", var2);
         var3 = null;
      } else if (!var2.canRead()) {
         LOGGER.log(Level.SEVERE, "cannot read directory", var2);
         var3 = null;
      } else {
         var3 = var2;
         if (!var2.canWrite()) {
            LOGGER.log(Level.SEVERE, "cannot write directory", var2);
            var3 = null;
         }
      }

      return var3;
   }

   private static Map createMap(final int var0) {
      return new LinkedHashMap((int)((float)var0 / 0.6F) + 2, 0.6F, true) {
         private static final long serialVersionUID = 1L;

         protected boolean removeEldestEntry(Entry var1) {
            if (this.size() > var0) {
               this.remove(var1.getKey());
               if (!((File)var1.getValue()).delete()) {
                  ((File)var1.getValue()).deleteOnExit();
               }
            }

            return false;
         }
      };
   }

   private static Map deserializeMap(File param0) {
      // $FF: Couldn't be decompiled
   }

   private static boolean serializeMap(File param0, Map param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean containsKey(MapGeneratorJob var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.map.containsKey(var1);
      } finally {
         ;
      }

      return var2;
   }

   public void destroy() {
      synchronized(this){}

      Throwable var10000;
      label1018: {
         boolean var10001;
         try {
            if (this.bitmapGet != null) {
               this.bitmapGet.recycle();
            }
         } catch (Throwable var94) {
            var10000 = var94;
            var10001 = false;
            break label1018;
         }

         int var1;
         try {
            var1 = this.capacity;
         } catch (Throwable var93) {
            var10000 = var93;
            var10001 = false;
            break label1018;
         }

         if (var1 == 0) {
            return;
         }

         try {
            if (this.persistent && serializeMap(this.cacheDirectory, this.map)) {
               return;
            }
         } catch (Throwable var92) {
            var10000 = var92;
            var10001 = false;
            break label1018;
         }

         Iterator var2;
         try {
            var2 = this.map.values().iterator();
         } catch (Throwable var90) {
            var10000 = var90;
            var10001 = false;
            break label1018;
         }

         File var3;
         while(true) {
            try {
               if (!var2.hasNext()) {
                  break;
               }

               var3 = (File)var2.next();
               if (!var3.delete()) {
                  var3.deleteOnExit();
               }
            } catch (Throwable var91) {
               var10000 = var91;
               var10001 = false;
               break label1018;
            }
         }

         File[] var95;
         try {
            this.map.clear();
            if (this.cacheDirectory == null) {
               return;
            }

            var95 = this.cacheDirectory.listFiles(FileSystemTileCache.ImageFileNameFilter.INSTANCE);
         } catch (Throwable var89) {
            var10000 = var89;
            var10001 = false;
            break label1018;
         }

         if (var95 != null) {
            int var4;
            try {
               var4 = var95.length;
            } catch (Throwable var87) {
               var10000 = var87;
               var10001 = false;
               break label1018;
            }

            for(var1 = 0; var1 < var4; ++var1) {
               var3 = var95[var1];

               try {
                  if (!var3.delete()) {
                     var3.deleteOnExit();
                  }
               } catch (Throwable var88) {
                  var10000 = var88;
                  var10001 = false;
                  break label1018;
               }
            }
         }

         label972:
         try {
            if (!this.cacheDirectory.delete()) {
               this.cacheDirectory.deleteOnExit();
            }

            return;
         } catch (Throwable var86) {
            var10000 = var86;
            var10001 = false;
            break label972;
         }
      }

      Throwable var96 = var10000;
      throw var96;
   }

   public Bitmap get(MapGeneratorJob param1) {
      // $FF: Couldn't be decompiled
   }

   public int getCapacity() {
      synchronized(this){}

      int var1;
      try {
         var1 = this.capacity;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isPersistent() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.persistent;
      } finally {
         ;
      }

      return var1;
   }

   public void put(MapGeneratorJob param1, Bitmap param2) {
      // $FF: Couldn't be decompiled
   }

   public void setCapacity(int var1) {
      synchronized(this){}

      Throwable var10000;
      label152: {
         boolean var10001;
         int var2;
         try {
            var2 = this.capacity;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label152;
         }

         if (var2 == var1) {
            return;
         }

         Map var3;
         try {
            this.capacity = this.checkCapacity(var1);
            if (this.capacity == 0) {
               return;
            }

            var3 = createMap(this.capacity);
            if (this.map != null) {
               var3.putAll(this.map);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label152;
         }

         label140:
         try {
            this.map = var3;
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label140;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   public void setPersistent(boolean var1) {
      synchronized(this){}

      try {
         this.persistent = var1;
      } finally {
         ;
      }

   }

   private static final class ImageFileNameFilter implements FilenameFilter {
      static final FilenameFilter INSTANCE = new FileSystemTileCache.ImageFileNameFilter();

      public boolean accept(File var1, String var2) {
         return var2.endsWith(".tile");
      }
   }
}
