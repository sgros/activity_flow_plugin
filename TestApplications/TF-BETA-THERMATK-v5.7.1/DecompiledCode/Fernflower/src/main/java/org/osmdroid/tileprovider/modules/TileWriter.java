package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class TileWriter implements IFilesystemCache {
   static boolean hasInited;
   private static long mUsedCacheSpace;
   Thread initThread = null;
   private long mMaximumCachedFileAge;

   public TileWriter() {
      if (!hasInited) {
         hasInited = true;
         this.initThread = new Thread() {
            public void run() {
               TileWriter.mUsedCacheSpace = 0L;
               TileWriter.this.calculateDirectorySize(Configuration.getInstance().getOsmdroidTileCache());
               if (TileWriter.mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
                  TileWriter.this.cutCurrentCache();
               }

               if (Configuration.getInstance().isDebugMode()) {
                  Log.d("OsmDroid", "Finished init thread");
               }

            }
         };
         this.initThread.setPriority(1);
         this.initThread.start();
      }

   }

   private void calculateDirectorySize(File var1) {
      File[] var2 = var1.listFiles();
      if (var2 != null) {
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File var5 = var2[var4];
            if (var5.isFile()) {
               mUsedCacheSpace += var5.length();
            }

            if (var5.isDirectory() && !this.isSymbolicDirectoryLink(var1, var5)) {
               this.calculateDirectorySize(var5);
            }
         }
      }

   }

   private boolean createFolderAndCheckIfExists(File var1) {
      if (var1.mkdirs()) {
         return true;
      } else {
         StringBuilder var2;
         if (Configuration.getInstance().isDebugMode()) {
            var2 = new StringBuilder();
            var2.append("Failed to create ");
            var2.append(var1);
            var2.append(" - wait and check again");
            Log.d("OsmDroid", var2.toString());
         }

         try {
            Thread.sleep(500L);
         } catch (InterruptedException var3) {
         }

         if (var1.exists()) {
            if (Configuration.getInstance().isDebugMode()) {
               var2 = new StringBuilder();
               var2.append("Seems like another thread created ");
               var2.append(var1);
               Log.d("OsmDroid", var2.toString());
            }

            return true;
         } else {
            if (Configuration.getInstance().isDebugMode()) {
               var2 = new StringBuilder();
               var2.append("File still doesn't exist: ");
               var2.append(var1);
               Log.d("OsmDroid", var2.toString());
            }

            return false;
         }
      }
   }

   private void cutCurrentCache() {
      File var1 = Configuration.getInstance().getOsmdroidTileCache();
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label687: {
         label691: {
            List var81;
            try {
               if (mUsedCacheSpace <= Configuration.getInstance().getTileFileSystemCacheTrimBytes()) {
                  break label691;
               }

               StringBuilder var2 = new StringBuilder();
               var2.append("Trimming tile cache from ");
               var2.append(mUsedCacheSpace);
               var2.append(" to ");
               var2.append(Configuration.getInstance().getTileFileSystemCacheTrimBytes());
               Log.d("OsmDroid", var2.toString());
               var81 = this.getDirectoryFileList(Configuration.getInstance().getOsmdroidTileCache());
            } catch (Throwable var78) {
               var10000 = var78;
               var10001 = false;
               break label687;
            }

            int var3 = 0;

            int var5;
            File[] var82;
            try {
               var82 = (File[])var81.toArray(new File[0]);
               Comparator var4 = new Comparator() {
                  public int compare(File var1, File var2) {
                     return Long.valueOf(var1.lastModified()).compareTo(var2.lastModified());
                  }
               };
               Arrays.sort(var82, var4);
               var5 = var82.length;
            } catch (Throwable var77) {
               var10000 = var77;
               var10001 = false;
               break label687;
            }

            while(true) {
               File var6;
               label683: {
                  if (var3 < var5) {
                     var6 = var82[var3];

                     try {
                        if (mUsedCacheSpace > Configuration.getInstance().getTileFileSystemCacheTrimBytes()) {
                           break label683;
                        }
                     } catch (Throwable var80) {
                        var10000 = var80;
                        var10001 = false;
                        break label687;
                     }
                  }

                  try {
                     Log.d("OsmDroid", "Finished trimming tile cache");
                     break;
                  } catch (Throwable var75) {
                     var10000 = var75;
                     var10001 = false;
                     break label687;
                  }
               }

               label675: {
                  long var7;
                  try {
                     var7 = var6.length();
                     if (!var6.delete()) {
                        break label675;
                     }

                     if (Configuration.getInstance().isDebugTileProviders()) {
                        StringBuilder var84 = new StringBuilder();
                        var84.append("Cache trim deleting ");
                        var84.append(var6.getAbsolutePath());
                        Log.d("OsmDroid", var84.toString());
                     }
                  } catch (Throwable var79) {
                     var10000 = var79;
                     var10001 = false;
                     break label687;
                  }

                  try {
                     mUsedCacheSpace -= var7;
                  } catch (Throwable var76) {
                     var10000 = var76;
                     var10001 = false;
                     break label687;
                  }
               }

               ++var3;
            }
         }

         label659:
         try {
            return;
         } catch (Throwable var74) {
            var10000 = var74;
            var10001 = false;
            break label659;
         }
      }

      while(true) {
         Throwable var83 = var10000;

         try {
            throw var83;
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            continue;
         }
      }
   }

   private List getDirectoryFileList(File var1) {
      ArrayList var2 = new ArrayList();
      File[] var6 = var1.listFiles();
      if (var6 != null) {
         int var3 = var6.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File var5 = var6[var4];
            if (var5.isFile()) {
               var2.add(var5);
            }

            if (var5.isDirectory()) {
               var2.addAll(this.getDirectoryFileList(var5));
            }
         }
      }

      return var2;
   }

   private boolean isSymbolicDirectoryLink(File var1, File var2) {
      boolean var3;
      try {
         var3 = var1.getCanonicalPath().equals(var2.getCanonicalFile().getParent());
      } catch (NoSuchElementException | IOException var4) {
         return true;
      }

      return var3 ^ true;
   }

   public File getFile(ITileSource var1, long var2) {
      File var4 = Configuration.getInstance().getOsmdroidTileCache();
      StringBuilder var5 = new StringBuilder();
      var5.append(var1.getTileRelativeFilenameString(var2));
      var5.append(".tile");
      return new File(var4, var5.toString());
   }

   public Drawable loadTile(ITileSource var1, long var2) throws Exception {
      File var4 = this.getFile(var1, var2);
      if (!var4.exists()) {
         return null;
      } else {
         Drawable var8 = var1.getDrawable(var4.getPath());
         long var5 = System.currentTimeMillis();
         boolean var7;
         if (var4.lastModified() < var5 - this.mMaximumCachedFileAge) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (var7 && var8 != null) {
            if (Configuration.getInstance().isDebugMode()) {
               StringBuilder var9 = new StringBuilder();
               var9.append("Tile expired: ");
               var9.append(MapTileIndex.toString(var2));
               Log.d("OsmDroid", var9.toString());
            }

            ExpirableBitmapDrawable.setState(var8, -2);
         }

         return var8;
      }
   }

   public void onDetach() {
      Thread var1 = this.initThread;
      if (var1 != null) {
         try {
            var1.interrupt();
         } catch (Throwable var2) {
         }
      }

   }

   public boolean saveFile(ITileSource param1, long param2, InputStream param4, Long param5) {
      // $FF: Couldn't be decompiled
   }

   public void setMaximumCachedFileAge(long var1) {
      this.mMaximumCachedFileAge = var1;
   }
}
