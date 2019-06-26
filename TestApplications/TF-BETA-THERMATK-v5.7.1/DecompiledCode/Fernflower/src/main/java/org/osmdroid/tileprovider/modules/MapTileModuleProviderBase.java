package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public abstract class MapTileModuleProviderBase {
   private final ExecutorService mExecutor;
   protected final LinkedHashMap mPending;
   protected final Object mQueueLockObject = new Object();
   protected final HashMap mWorking;

   public MapTileModuleProviderBase(int var1, final int var2) {
      int var3 = var1;
      if (var2 < var1) {
         Log.w("OsmDroid", "The pending queue size is smaller than the thread pool size. Automatically reducing the thread pool size.");
         var3 = var2;
      }

      this.mExecutor = Executors.newFixedThreadPool(var3, new ConfigurablePriorityThreadFactory(5, this.getThreadGroupName()));
      this.mWorking = new HashMap();
      this.mPending = new LinkedHashMap(var2 + 2, 0.1F, true) {
         protected boolean removeEldestEntry(Entry var1) {
            if (this.size() <= var2) {
               return false;
            } else {
               Iterator var5 = MapTileModuleProviderBase.this.mPending.keySet().iterator();

               while(var5.hasNext()) {
                  long var2x = (Long)var5.next();
                  if (!MapTileModuleProviderBase.this.mWorking.containsKey(var2x)) {
                     MapTileRequestState var4 = (MapTileRequestState)MapTileModuleProviderBase.this.mPending.get(var2x);
                     if (var4 != null) {
                        MapTileModuleProviderBase.this.removeTileFromQueues(var2x);
                        var4.getCallback().mapTileRequestFailedExceedsMaxQueueSize(var4);
                        break;
                     }
                  }
               }

               return false;
            }
         }
      };
   }

   private void clearQueue() {
      // $FF: Couldn't be decompiled
   }

   public void detach() {
      this.clearQueue();
      this.mExecutor.shutdown();
   }

   public abstract int getMaximumZoomLevel();

   public abstract int getMinimumZoomLevel();

   protected abstract String getName();

   protected abstract String getThreadGroupName();

   public abstract MapTileModuleProviderBase.TileLoader getTileLoader();

   public abstract boolean getUsesDataConnection();

   public void loadMapTileAsync(MapTileRequestState var1) {
      if (!this.mExecutor.isShutdown()) {
         Object var2 = this.mQueueLockObject;
         synchronized(var2){}

         label269: {
            Throwable var10000;
            boolean var10001;
            label271: {
               label260: {
                  try {
                     if (!Configuration.getInstance().isDebugTileProviders()) {
                        break label260;
                     }

                     StringBuilder var3 = new StringBuilder();
                     var3.append("MapTileModuleProviderBase.loadMaptileAsync() on provider: ");
                     var3.append(this.getName());
                     var3.append(" for tile: ");
                     var3.append(MapTileIndex.toString(var1.getMapTile()));
                     Log.d("OsmDroid", var3.toString());
                     if (this.mPending.containsKey(var1.getMapTile())) {
                        Log.d("OsmDroid", "MapTileModuleProviderBase.loadMaptileAsync() tile already exists in request queue for modular provider. Moving to front of queue.");
                        break label260;
                     }
                  } catch (Throwable var28) {
                     var10000 = var28;
                     var10001 = false;
                     break label271;
                  }

                  try {
                     Log.d("OsmDroid", "MapTileModuleProviderBase.loadMaptileAsync() adding tile to request queue for modular provider.");
                  } catch (Throwable var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label271;
                  }
               }

               label249:
               try {
                  this.mPending.put(var1.getMapTile(), var1);
                  break label269;
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label249;
               }
            }

            while(true) {
               Throwable var29 = var10000;

               try {
                  throw var29;
               } catch (Throwable var25) {
                  var10000 = var25;
                  var10001 = false;
                  continue;
               }
            }
         }

         try {
            this.mExecutor.execute(this.getTileLoader());
         } catch (RejectedExecutionException var24) {
            Log.w("OsmDroid", "RejectedExecutionException", var24);
         }

      }
   }

   protected void removeTileFromQueues(long var1) {
      Object var3 = this.mQueueLockObject;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (Configuration.getInstance().isDebugTileProviders()) {
               StringBuilder var4 = new StringBuilder();
               var4.append("MapTileModuleProviderBase.removeTileFromQueues() on provider: ");
               var4.append(this.getName());
               var4.append(" for tile: ");
               var4.append(MapTileIndex.toString(var1));
               Log.d("OsmDroid", var4.toString());
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            this.mPending.remove(var1);
            this.mWorking.remove(var1);
            return;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var17 = var10000;

         try {
            throw var17;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            continue;
         }
      }
   }

   public abstract void setTileSource(ITileSource var1);

   public abstract class TileLoader implements Runnable {
      public abstract Drawable loadTile(long var1) throws CantContinueException;

      protected MapTileRequestState nextTile() {
         Object var1 = MapTileModuleProviderBase.this.mQueueLockObject;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label576: {
            Iterator var2;
            try {
               var2 = MapTileModuleProviderBase.this.mPending.keySet().iterator();
            } catch (Throwable var61) {
               var10000 = var61;
               var10001 = false;
               break label576;
            }

            MapTileRequestState var3 = null;
            Long var4 = null;

            while(true) {
               Long var5;
               try {
                  if (!var2.hasNext()) {
                     break;
                  }

                  var5 = (Long)var2.next();
                  if (MapTileModuleProviderBase.this.mWorking.containsKey(var5)) {
                     continue;
                  }

                  if (Configuration.getInstance().isDebugTileProviders()) {
                     StringBuilder var62 = new StringBuilder();
                     var62.append("TileLoader.nextTile() on provider: ");
                     var62.append(MapTileModuleProviderBase.this.getName());
                     var62.append(" found tile in working queue: ");
                     var62.append(MapTileIndex.toString(var5));
                     Log.d("OsmDroid", var62.toString());
                  }
               } catch (Throwable var60) {
                  var10000 = var60;
                  var10001 = false;
                  break label576;
               }

               var4 = var5;
            }

            if (var4 != null) {
               try {
                  if (Configuration.getInstance().isDebugTileProviders()) {
                     StringBuilder var64 = new StringBuilder();
                     var64.append("TileLoader.nextTile() on provider: ");
                     var64.append(MapTileModuleProviderBase.this.getName());
                     var64.append(" adding tile to working queue: ");
                     var64.append(var4);
                     Log.d("OsmDroid", var64.toString());
                  }
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label576;
               }

               try {
                  MapTileModuleProviderBase.this.mWorking.put(var4, MapTileModuleProviderBase.this.mPending.get(var4));
               } catch (Throwable var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label576;
               }
            }

            if (var4 != null) {
               try {
                  var3 = (MapTileRequestState)MapTileModuleProviderBase.this.mPending.get(var4);
               } catch (Throwable var57) {
                  var10000 = var57;
                  var10001 = false;
                  break label576;
               }
            }

            label545:
            try {
               return var3;
            } catch (Throwable var56) {
               var10000 = var56;
               var10001 = false;
               break label545;
            }
         }

         while(true) {
            Throwable var63 = var10000;

            try {
               throw var63;
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               continue;
            }
         }
      }

      protected void onTileLoaderInit() {
      }

      protected void onTileLoaderShutdown() {
      }

      public final void run() {
         this.onTileLoaderInit();

         while(true) {
            MapTileRequestState var1 = this.nextTile();
            if (var1 == null) {
               this.onTileLoaderShutdown();
               return;
            }

            if (Configuration.getInstance().isDebugTileProviders()) {
               StringBuilder var2 = new StringBuilder();
               var2.append("TileLoader.run() processing next tile: ");
               var2.append(MapTileIndex.toString(var1.getMapTile()));
               var2.append(", pending:");
               var2.append(MapTileModuleProviderBase.this.mPending.size());
               var2.append(", working:");
               var2.append(MapTileModuleProviderBase.this.mWorking.size());
               Log.d("OsmDroid", var2.toString());
            }

            Drawable var7 = null;

            label36: {
               Drawable var8;
               try {
                  var8 = this.loadTile(var1.getMapTile());
               } catch (CantContinueException var5) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Tile loader can't continue: ");
                  var4.append(MapTileIndex.toString(var1.getMapTile()));
                  Log.i("OsmDroid", var4.toString(), var5);
                  MapTileModuleProviderBase.this.clearQueue();
                  break label36;
               } catch (Throwable var6) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Error downloading tile: ");
                  var3.append(MapTileIndex.toString(var1.getMapTile()));
                  Log.i("OsmDroid", var3.toString(), var6);
                  break label36;
               }

               var7 = var8;
            }

            if (var7 == null) {
               this.tileLoadedFailed(var1);
            } else if (ExpirableBitmapDrawable.getState(var7) == -2) {
               this.tileLoadedExpired(var1, var7);
            } else if (ExpirableBitmapDrawable.getState(var7) == -3) {
               this.tileLoadedScaled(var1, var7);
            } else {
               this.tileLoaded(var1, var7);
            }
         }
      }

      protected void tileLoaded(MapTileRequestState var1, Drawable var2) {
         if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder var3 = new StringBuilder();
            var3.append("TileLoader.tileLoaded() on provider: ");
            var3.append(MapTileModuleProviderBase.this.getName());
            var3.append(" with tile: ");
            var3.append(MapTileIndex.toString(var1.getMapTile()));
            Log.d("OsmDroid", var3.toString());
         }

         MapTileModuleProviderBase.this.removeTileFromQueues(var1.getMapTile());
         ExpirableBitmapDrawable.setState(var2, -1);
         var1.getCallback().mapTileRequestCompleted(var1, var2);
      }

      protected void tileLoadedExpired(MapTileRequestState var1, Drawable var2) {
         if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder var3 = new StringBuilder();
            var3.append("TileLoader.tileLoadedExpired() on provider: ");
            var3.append(MapTileModuleProviderBase.this.getName());
            var3.append(" with tile: ");
            var3.append(MapTileIndex.toString(var1.getMapTile()));
            Log.d("OsmDroid", var3.toString());
         }

         MapTileModuleProviderBase.this.removeTileFromQueues(var1.getMapTile());
         ExpirableBitmapDrawable.setState(var2, -2);
         var1.getCallback().mapTileRequestExpiredTile(var1, var2);
      }

      protected void tileLoadedFailed(MapTileRequestState var1) {
         if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder var2 = new StringBuilder();
            var2.append("TileLoader.tileLoadedFailed() on provider: ");
            var2.append(MapTileModuleProviderBase.this.getName());
            var2.append(" with tile: ");
            var2.append(MapTileIndex.toString(var1.getMapTile()));
            Log.d("OsmDroid", var2.toString());
         }

         MapTileModuleProviderBase.this.removeTileFromQueues(var1.getMapTile());
         var1.getCallback().mapTileRequestFailed(var1);
      }

      protected void tileLoadedScaled(MapTileRequestState var1, Drawable var2) {
         if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder var3 = new StringBuilder();
            var3.append("TileLoader.tileLoadedScaled() on provider: ");
            var3.append(MapTileModuleProviderBase.this.getName());
            var3.append(" with tile: ");
            var3.append(MapTileIndex.toString(var1.getMapTile()));
            Log.d("OsmDroid", var3.toString());
         }

         MapTileModuleProviderBase.this.removeTileFromQueues(var1.getMapTile());
         ExpirableBitmapDrawable.setState(var2, -3);
         var1.getCallback().mapTileRequestExpiredTile(var1, var2);
      }
   }
}
