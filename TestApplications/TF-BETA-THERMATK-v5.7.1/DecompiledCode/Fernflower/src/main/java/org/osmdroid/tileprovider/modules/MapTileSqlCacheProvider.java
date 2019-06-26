package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileSqlCacheProvider extends MapTileFileStorageProviderBase {
   private static final String[] columns = new String[]{"tile", "expires"};
   private final AtomicReference mTileSource = new AtomicReference();
   private SqlTileWriter mWriter;

   public MapTileSqlCacheProvider(IRegisterReceiver var1, ITileSource var2) {
      super(var1, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
      this.setTileSource(var2);
      this.mWriter = new SqlTileWriter();
   }

   public void detach() {
      SqlTileWriter var1 = this.mWriter;
      if (var1 != null) {
         var1.onDetach();
      }

      this.mWriter = null;
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
      return "SQL Cache Archive Provider";
   }

   protected String getThreadGroupName() {
      return "sqlcache";
   }

   public MapTileSqlCacheProvider.TileLoader getTileLoader() {
      return new MapTileSqlCacheProvider.TileLoader();
   }

   public boolean getUsesDataConnection() {
      return false;
   }

   protected void onMediaMounted() {
   }

   protected void onMediaUnmounted() {
      SqlTileWriter var1 = this.mWriter;
      if (var1 != null) {
         var1.onDetach();
      }

      this.mWriter = new SqlTileWriter();
   }

   public void setTileSource(ITileSource var1) {
      this.mTileSource.set(var1);
   }

   protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
      protected TileLoader() {
         super();
      }

      public Drawable loadTile(long var1) throws CantContinueException {
         ITileSource var3 = (ITileSource)MapTileSqlCacheProvider.this.mTileSource.get();
         if (var3 == null) {
            return null;
         } else if (MapTileSqlCacheProvider.this.mWriter != null) {
            BitmapTileSourceBase.LowMemoryException var16;
            label44: {
               Throwable var10000;
               label50: {
                  Drawable var13;
                  boolean var10001;
                  try {
                     var13 = MapTileSqlCacheProvider.this.mWriter.loadTile(var3, var1);
                  } catch (BitmapTileSourceBase.LowMemoryException var11) {
                     var16 = var11;
                     var10001 = false;
                     break label44;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label50;
                  }

                  if (var13 == null) {
                     try {
                        ++Counters.fileCacheMiss;
                     } catch (BitmapTileSourceBase.LowMemoryException var9) {
                        var16 = var9;
                        var10001 = false;
                        break label44;
                     } catch (Throwable var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label50;
                     }
                  } else {
                     try {
                        ++Counters.fileCacheHit;
                     } catch (BitmapTileSourceBase.LowMemoryException var7) {
                        var16 = var7;
                        var10001 = false;
                        break label44;
                     } catch (Throwable var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label50;
                     }
                  }

                  try {
                     return var13;
                  } catch (BitmapTileSourceBase.LowMemoryException var5) {
                     var16 = var5;
                     var10001 = false;
                     break label44;
                  } catch (Throwable var6) {
                     var10000 = var6;
                     var10001 = false;
                  }
               }

               Throwable var14 = var10000;
               Log.e("OsmDroid", "Error loading tile", var14);
               return null;
            }

            BitmapTileSourceBase.LowMemoryException var4 = var16;
            StringBuilder var15 = new StringBuilder();
            var15.append("LowMemoryException downloading MapTile: ");
            var15.append(MapTileIndex.toString(var1));
            var15.append(" : ");
            var15.append(var4);
            Log.w("OsmDroid", var15.toString());
            ++Counters.fileCacheOOM;
            throw new CantContinueException(var4);
         } else {
            Log.d("OsmDroid", "TileLoader failed to load tile due to mWriter being null (map shutdown?)");
            return null;
         }
      }
   }
}
