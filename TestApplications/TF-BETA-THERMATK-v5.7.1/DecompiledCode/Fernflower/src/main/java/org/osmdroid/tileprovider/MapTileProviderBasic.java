package org.osmdroid.tileprovider;

import android.content.Context;
import android.os.Build.VERSION;
import java.util.Iterator;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.modules.MapTileApproximater;
import org.osmdroid.tileprovider.modules.MapTileAssetsProvider;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileFileStorageProviderBase;
import org.osmdroid.tileprovider.modules.MapTileFilesystemProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.modules.MapTileSqlCacheProvider;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.modules.TileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.MapTileAreaBorderComputer;
import org.osmdroid.util.MapTileAreaZoomComputer;
import org.osmdroid.util.MapTileIndex;

public class MapTileProviderBasic extends MapTileProviderArray implements IMapTileProviderCallback {
   private final MapTileApproximater mApproximationProvider;
   private final MapTileDownloader mDownloaderProvider;
   private final INetworkAvailablityCheck mNetworkAvailabilityCheck;
   protected IFilesystemCache tileWriter;

   public MapTileProviderBasic(Context var1, ITileSource var2) {
      this(var1, var2, (IFilesystemCache)null);
   }

   public MapTileProviderBasic(Context var1, ITileSource var2, IFilesystemCache var3) {
      this(new SimpleRegisterReceiver(var1), new NetworkAvailabliltyCheck(var1), var2, var1, var3);
   }

   public MapTileProviderBasic(IRegisterReceiver var1, INetworkAvailablityCheck var2, ITileSource var3, Context var4, IFilesystemCache var5) {
      super(var3, var1);
      this.mNetworkAvailabilityCheck = var2;
      if (var5 != null) {
         this.tileWriter = var5;
      } else if (VERSION.SDK_INT < 10) {
         this.tileWriter = new TileWriter();
      } else {
         this.tileWriter = new SqlTileWriter();
      }

      MapTileAssetsProvider var8 = new MapTileAssetsProvider(var1, var4.getAssets(), var3);
      super.mTileProviderList.add(var8);
      MapTileFileStorageProviderBase var7 = getMapTileFileStorageProviderBase(var1, var3, this.tileWriter);
      super.mTileProviderList.add(var7);
      MapTileFileArchiveProvider var6 = new MapTileFileArchiveProvider(var1, var3);
      super.mTileProviderList.add(var6);
      this.mApproximationProvider = new MapTileApproximater();
      super.mTileProviderList.add(this.mApproximationProvider);
      this.mApproximationProvider.addProvider(var8);
      this.mApproximationProvider.addProvider(var7);
      this.mApproximationProvider.addProvider(var6);
      this.mDownloaderProvider = new MapTileDownloader(var3, this.tileWriter, var2);
      super.mTileProviderList.add(this.mDownloaderProvider);
      this.getTileCache().getProtectedTileComputers().add(new MapTileAreaZoomComputer(-1));
      this.getTileCache().getProtectedTileComputers().add(new MapTileAreaBorderComputer(1));
      this.getTileCache().setAutoEnsureCapacity(false);
      this.getTileCache().setStressedMemory(false);
      this.getTileCache().getPreCache().addProvider(var8);
      this.getTileCache().getPreCache().addProvider(var7);
      this.getTileCache().getPreCache().addProvider(var6);
      this.getTileCache().getPreCache().addProvider(this.mDownloaderProvider);
      this.getTileCache().getProtectedTileContainers().add(this);
      this.setOfflineFirst(true);
   }

   public static MapTileFileStorageProviderBase getMapTileFileStorageProviderBase(IRegisterReceiver var0, ITileSource var1, IFilesystemCache var2) {
      return (MapTileFileStorageProviderBase)(var2 instanceof TileWriter ? new MapTileFilesystemProvider(var0, var1) : new MapTileSqlCacheProvider(var0, var1));
   }

   public void detach() {
      IFilesystemCache var1 = this.tileWriter;
      if (var1 != null) {
         var1.onDetach();
      }

      this.tileWriter = null;
      super.detach();
   }

   protected boolean isDowngradedMode(long var1) {
      INetworkAvailablityCheck var3 = this.mNetworkAvailabilityCheck;
      boolean var4 = true;
      if ((var3 == null || var3.getNetworkAvailable()) && this.useDataConnection()) {
         Iterator var11 = super.mTileProviderList.iterator();
         int var5 = -1;
         int var6 = -1;

         while(true) {
            int var8;
            int var9;
            do {
               MapTileModuleProviderBase var7;
               do {
                  if (!var11.hasNext()) {
                     boolean var10 = var4;
                     if (var5 != -1) {
                        if (var6 == -1) {
                           var10 = var4;
                        } else {
                           var9 = MapTileIndex.getZoom(var1);
                           var10 = var4;
                           if (var9 >= var5) {
                              if (var9 > var6) {
                                 var10 = var4;
                              } else {
                                 var10 = false;
                              }
                           }
                        }
                     }

                     return var10;
                  }

                  var7 = (MapTileModuleProviderBase)var11.next();
               } while(!var7.getUsesDataConnection());

               label38: {
                  var8 = var7.getMinimumZoomLevel();
                  if (var5 != -1) {
                     var9 = var5;
                     if (var5 <= var8) {
                        break label38;
                     }
                  }

                  var9 = var8;
               }

               var8 = var7.getMaximumZoomLevel();
               if (var6 == -1) {
                  break;
               }

               var5 = var9;
            } while(var6 >= var8);

            var6 = var8;
            var5 = var9;
         }
      } else {
         return true;
      }
   }

   public boolean setOfflineFirst(boolean var1) {
      Iterator var2 = super.mTileProviderList.iterator();
      int var3 = -1;
      int var4 = -1;

      int var8;
      for(int var5 = 0; var2.hasNext(); var4 = var8) {
         MapTileModuleProviderBase var6 = (MapTileModuleProviderBase)var2.next();
         int var7 = var3;
         if (var3 == -1) {
            var7 = var3;
            if (var6 == this.mDownloaderProvider) {
               var7 = var5;
            }
         }

         var8 = var4;
         if (var4 == -1) {
            var8 = var4;
            if (var6 == this.mApproximationProvider) {
               var8 = var5;
            }
         }

         ++var5;
         var3 = var7;
      }

      if (var3 != -1 && var4 != -1) {
         if (var4 < var3 && var1) {
            return true;
         } else if (var4 > var3 && !var1) {
            return true;
         } else {
            super.mTileProviderList.set(var3, this.mApproximationProvider);
            super.mTileProviderList.set(var4, this.mDownloaderProvider);
            return true;
         }
      } else {
         return false;
      }
   }
}
