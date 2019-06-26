package org.osmdroid.tileprovider.modules;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.TileSystem;

public class MapTileAssetsProvider extends MapTileFileStorageProviderBase {
   private final AssetManager mAssets;
   private final AtomicReference mTileSource;

   public MapTileAssetsProvider(IRegisterReceiver var1, AssetManager var2, ITileSource var3) {
      this(var1, var2, var3, Configuration.getInstance().getTileDownloadThreads(), Configuration.getInstance().getTileDownloadMaxQueueSize());
   }

   public MapTileAssetsProvider(IRegisterReceiver var1, AssetManager var2, ITileSource var3, int var4, int var5) {
      super(var1, var4, var5);
      this.mTileSource = new AtomicReference();
      this.setTileSource(var3);
      this.mAssets = var2;
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
      return "Assets Cache Provider";
   }

   protected String getThreadGroupName() {
      return "assets";
   }

   public MapTileAssetsProvider.TileLoader getTileLoader() {
      return new MapTileAssetsProvider.TileLoader(this.mAssets);
   }

   public boolean getUsesDataConnection() {
      return false;
   }

   public void setTileSource(ITileSource var1) {
      this.mTileSource.set(var1);
   }

   protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
      private AssetManager mAssets = null;

      public TileLoader(AssetManager var2) {
         super();
         this.mAssets = var2;
      }

      public Drawable loadTile(long var1) throws CantContinueException {
         ITileSource var3 = (ITileSource)MapTileAssetsProvider.this.mTileSource.get();
         if (var3 == null) {
            return null;
         } else {
            try {
               Drawable var6 = var3.getDrawable(this.mAssets.open(var3.getTileRelativeFilenameString(var1)));
               return var6;
            } catch (IOException var4) {
               return null;
            } catch (BitmapTileSourceBase.LowMemoryException var5) {
               throw new CantContinueException(var5);
            }
         }
      }
   }
}
