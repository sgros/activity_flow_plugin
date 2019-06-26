package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileFileArchiveProvider extends MapTileFileStorageProviderBase {
   private final boolean ignoreTileSource;
   private final ArrayList mArchiveFiles;
   private final boolean mSpecificArchivesProvided;
   private final AtomicReference mTileSource;

   public MapTileFileArchiveProvider(IRegisterReceiver var1, ITileSource var2) {
      this(var1, var2, (IArchiveFile[])null);
   }

   public MapTileFileArchiveProvider(IRegisterReceiver var1, ITileSource var2, IArchiveFile[] var3) {
      this(var1, var2, var3, false);
   }

   public MapTileFileArchiveProvider(IRegisterReceiver var1, ITileSource var2, IArchiveFile[] var3, boolean var4) {
      super(var1, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
      this.mArchiveFiles = new ArrayList();
      this.mTileSource = new AtomicReference();
      this.ignoreTileSource = var4;
      this.setTileSource(var2);
      if (var3 == null) {
         this.mSpecificArchivesProvided = false;
         this.findArchiveFiles();
      } else {
         this.mSpecificArchivesProvided = true;

         for(int var5 = var3.length - 1; var5 >= 0; --var5) {
            this.mArchiveFiles.add(var3[var5]);
         }
      }

   }

   // $FF: synthetic method
   static AtomicReference access$000(MapTileFileArchiveProvider var0) {
      return var0.mTileSource;
   }

   // $FF: synthetic method
   static InputStream access$100(MapTileFileArchiveProvider var0, long var1, ITileSource var3) {
      return var0.getInputStream(var1, var3);
   }

   private void clearArcives() {
      for(; !this.mArchiveFiles.isEmpty(); this.mArchiveFiles.remove(0)) {
         IArchiveFile var1 = (IArchiveFile)this.mArchiveFiles.get(0);
         if (var1 != null) {
            var1.close();
         }
      }

   }

   private void findArchiveFiles() {
      this.clearArcives();
      File[] var1 = Configuration.getInstance().getOsmdroidBasePath().listFiles();
      if (var1 != null) {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            IArchiveFile var4 = ArchiveFileFactory.getArchiveFile(var1[var3]);
            if (var4 != null) {
               var4.setIgnoreTileSource(this.ignoreTileSource);
               this.mArchiveFiles.add(var4);
            }
         }
      }

   }

   private InputStream getInputStream(long var1, ITileSource var3) {
      synchronized(this){}

      Throwable var10000;
      label225: {
         Iterator var4;
         boolean var10001;
         try {
            var4 = this.mArchiveFiles.iterator();
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label225;
         }

         while(true) {
            IArchiveFile var5;
            try {
               if (!var4.hasNext()) {
                  return null;
               }

               var5 = (IArchiveFile)var4.next();
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break;
            }

            if (var5 != null) {
               InputStream var6;
               try {
                  var6 = var5.getInputStream(var3, var1);
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break;
               }

               if (var6 != null) {
                  try {
                     if (Configuration.getInstance().isDebugMode()) {
                        StringBuilder var28 = new StringBuilder();
                        var28.append("Found tile ");
                        var28.append(MapTileIndex.toString(var1));
                        var28.append(" in ");
                        var28.append(var5);
                        Log.d("OsmDroid", var28.toString());
                     }
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break;
                  }

                  return var6;
               }
            }
         }
      }

      Throwable var27 = var10000;
      throw var27;
   }

   public void detach() {
      this.clearArcives();
      super.detach();
   }

   public int getMaximumZoomLevel() {
      ITileSource var1 = (ITileSource)this.mTileSource.get();
      int var2;
      if (var1 != null) {
         var2 = var1.getMaximumZoomLevel();
      } else {
         var2 = TileSystem.getMaximumZoomLevel();
      }

      return var2;
   }

   public int getMinimumZoomLevel() {
      ITileSource var1 = (ITileSource)this.mTileSource.get();
      int var2;
      if (var1 != null) {
         var2 = var1.getMinimumZoomLevel();
      } else {
         var2 = 0;
      }

      return var2;
   }

   protected String getName() {
      return "File Archive Provider";
   }

   protected String getThreadGroupName() {
      return "filearchive";
   }

   public MapTileFileArchiveProvider.TileLoader getTileLoader() {
      return new MapTileFileArchiveProvider.TileLoader();
   }

   public boolean getUsesDataConnection() {
      return false;
   }

   protected void onMediaMounted() {
      if (!this.mSpecificArchivesProvided) {
         this.findArchiveFiles();
      }

   }

   protected void onMediaUnmounted() {
      if (!this.mSpecificArchivesProvided) {
         this.findArchiveFiles();
      }

   }

   public void setTileSource(ITileSource var1) {
      this.mTileSource.set(var1);
   }

   protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
      protected TileLoader() {
         super();
      }

      public Drawable loadTile(long param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
