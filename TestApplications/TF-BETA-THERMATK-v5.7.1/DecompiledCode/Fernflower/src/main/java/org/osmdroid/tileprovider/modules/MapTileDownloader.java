package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.TileSystem;
import org.osmdroid.util.UrlBackoff;

public class MapTileDownloader extends MapTileModuleProviderBase {
   private final IFilesystemCache mFilesystemCache;
   private final INetworkAvailablityCheck mNetworkAvailablityCheck;
   private TileDownloader mTileDownloader;
   private final MapTileDownloader.TileLoader mTileLoader;
   private final AtomicReference mTileSource;
   private final UrlBackoff mUrlBackoff;

   public MapTileDownloader(ITileSource var1, IFilesystemCache var2, INetworkAvailablityCheck var3) {
      this(var1, var2, var3, Configuration.getInstance().getTileDownloadThreads(), Configuration.getInstance().getTileDownloadMaxQueueSize());
   }

   public MapTileDownloader(ITileSource var1, IFilesystemCache var2, INetworkAvailablityCheck var3, int var4, int var5) {
      super(var4, var5);
      this.mTileSource = new AtomicReference();
      this.mTileLoader = new MapTileDownloader.TileLoader();
      this.mUrlBackoff = new UrlBackoff();
      this.mTileDownloader = new TileDownloader();
      this.mFilesystemCache = var2;
      this.mNetworkAvailablityCheck = var3;
      this.setTileSource(var1);
   }

   public void detach() {
      super.detach();
      IFilesystemCache var1 = this.mFilesystemCache;
      if (var1 != null) {
         var1.onDetach();
      }

   }

   public int getMaximumZoomLevel() {
      OnlineTileSourceBase var1 = (OnlineTileSourceBase)this.mTileSource.get();
      int var2;
      if (var1 != null) {
         var2 = var1.getMaximumZoomLevel();
      } else {
         var2 = TileSystem.getMaximumZoomLevel();
      }

      return var2;
   }

   public int getMinimumZoomLevel() {
      OnlineTileSourceBase var1 = (OnlineTileSourceBase)this.mTileSource.get();
      int var2;
      if (var1 != null) {
         var2 = var1.getMinimumZoomLevel();
      } else {
         var2 = 0;
      }

      return var2;
   }

   protected String getName() {
      return "Online Tile Download Provider";
   }

   protected String getThreadGroupName() {
      return "downloader";
   }

   public MapTileDownloader.TileLoader getTileLoader() {
      return this.mTileLoader;
   }

   public ITileSource getTileSource() {
      return (ITileSource)this.mTileSource.get();
   }

   public boolean getUsesDataConnection() {
      return true;
   }

   public void setTileSource(ITileSource var1) {
      if (var1 instanceof OnlineTileSourceBase) {
         this.mTileSource.set((OnlineTileSourceBase)var1);
      } else {
         this.mTileSource.set((Object)null);
      }

   }

   protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
      protected TileLoader() {
         super();
      }

      protected Drawable downloadTile(long var1, int var3, String var4) throws CantContinueException {
         OnlineTileSourceBase var5 = (OnlineTileSourceBase)MapTileDownloader.this.mTileSource.get();
         if (var5 == null) {
            return null;
         } else {
            try {
               var5.acquire();
            } catch (InterruptedException var9) {
               return null;
            }

            Drawable var10;
            try {
               var10 = MapTileDownloader.this.mTileDownloader.downloadTile(var1, var3, var4, MapTileDownloader.this.mFilesystemCache, var5);
            } finally {
               var5.release();
            }

            return var10;
         }
      }

      public Drawable loadTile(long var1) throws CantContinueException {
         OnlineTileSourceBase var3 = (OnlineTileSourceBase)MapTileDownloader.this.mTileSource.get();
         if (var3 == null) {
            return null;
         } else if (MapTileDownloader.this.mNetworkAvailablityCheck != null && !MapTileDownloader.this.mNetworkAvailablityCheck.getNetworkAvailable()) {
            if (Configuration.getInstance().isDebugMode()) {
               StringBuilder var6 = new StringBuilder();
               var6.append("Skipping ");
               var6.append(MapTileDownloader.this.getName());
               var6.append(" due to NetworkAvailabliltyCheck.");
               Log.d("OsmDroid", var6.toString());
            }

            return null;
         } else {
            String var4 = var3.getTileURLString(var1);
            if (TextUtils.isEmpty(var4)) {
               return null;
            } else if (MapTileDownloader.this.mUrlBackoff.shouldWait(var4)) {
               return null;
            } else {
               Drawable var5 = this.downloadTile(var1, 0, var4);
               if (var5 == null) {
                  MapTileDownloader.this.mUrlBackoff.next(var4);
               } else {
                  MapTileDownloader.this.mUrlBackoff.remove(var4);
               }

               return var5;
            }
         }
      }

      protected void tileLoaded(MapTileRequestState var1, Drawable var2) {
         MapTileDownloader.this.removeTileFromQueues(var1.getMapTile());
         var1.getCallback().mapTileRequestCompleted(var1, (Drawable)null);
         BitmapPool.getInstance().asyncRecycle(var2);
      }
   }
}
