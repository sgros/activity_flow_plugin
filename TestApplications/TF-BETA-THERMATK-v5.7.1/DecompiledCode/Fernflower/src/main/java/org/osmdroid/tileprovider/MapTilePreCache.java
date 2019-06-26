package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GarbageCollector;
import org.osmdroid.util.MapTileArea;
import org.osmdroid.util.MapTileAreaList;

public class MapTilePreCache {
   private final MapTileCache mCache;
   private final GarbageCollector mGC = new GarbageCollector(new Runnable() {
      public void run() {
         while(true) {
            long var1 = MapTilePreCache.this.next();
            if (var1 == -1L) {
               return;
            }

            MapTilePreCache.this.search(var1);
         }
      }
   });
   private final List mProviders = new ArrayList();
   private final MapTileAreaList mTileAreas = new MapTileAreaList();
   private Iterator mTileIndices;

   public MapTilePreCache(MapTileCache var1) {
      this.mCache = var1;
   }

   private long next() {
      while(true) {
         MapTileAreaList var1 = this.mTileAreas;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label153: {
            try {
               if (!this.mTileIndices.hasNext()) {
                  return -1L;
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label153;
            }

            long var2;
            try {
               var2 = (Long)this.mTileIndices.next();
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label153;
            }

            if (this.mCache.getMapTile(var2) != null) {
               continue;
            }

            return var2;
         }

         while(true) {
            Throwable var4 = var10000;

            try {
               throw var4;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               continue;
            }
         }
      }
   }

   private void refresh() {
      MapTileAreaList var1 = this.mTileAreas;
      synchronized(var1){}
      int var2 = 0;

      Throwable var10000;
      boolean var10001;
      label551: {
         Iterator var3;
         try {
            var3 = this.mCache.getAdditionalMapTileList().getList().iterator();
         } catch (Throwable var59) {
            var10000 = var59;
            var10001 = false;
            break label551;
         }

         while(true) {
            MapTileArea var4;
            MapTileArea var5;
            label542: {
               try {
                  if (!var3.hasNext()) {
                     break;
                  }

                  var4 = (MapTileArea)var3.next();
                  if (var2 < this.mTileAreas.getList().size()) {
                     var5 = (MapTileArea)this.mTileAreas.getList().get(var2);
                     break label542;
                  }
               } catch (Throwable var61) {
                  var10000 = var61;
                  var10001 = false;
                  break label551;
               }

               try {
                  var5 = new MapTileArea();
                  this.mTileAreas.getList().add(var5);
               } catch (Throwable var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label551;
               }
            }

            try {
               var5.set(var4);
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               break label551;
            }

            ++var2;
         }

         while(true) {
            try {
               if (var2 >= this.mTileAreas.getList().size()) {
                  break;
               }

               this.mTileAreas.getList().remove(this.mTileAreas.getList().size() - 1);
            } catch (Throwable var60) {
               var10000 = var60;
               var10001 = false;
               break label551;
            }
         }

         label519:
         try {
            this.mTileIndices = this.mTileAreas.iterator();
            return;
         } catch (Throwable var56) {
            var10000 = var56;
            var10001 = false;
            break label519;
         }
      }

      while(true) {
         Throwable var62 = var10000;

         try {
            throw var62;
         } catch (Throwable var55) {
            var10000 = var55;
            var10001 = false;
            continue;
         }
      }
   }

   private void search(long var1) {
      Iterator var3 = this.mProviders.iterator();

      while(var3.hasNext()) {
         MapTileModuleProviderBase var4 = (MapTileModuleProviderBase)var3.next();

         boolean var10001;
         try {
            if (var4 instanceof MapTileDownloader) {
               ITileSource var5 = ((MapTileDownloader)var4).getTileSource();
               if (var5 instanceof OnlineTileSourceBase && !((OnlineTileSourceBase)var5).getTileSourcePolicy().acceptsPreventive()) {
                  continue;
               }
            }
         } catch (CantContinueException var8) {
            var10001 = false;
            continue;
         }

         Drawable var9;
         try {
            var9 = var4.getTileLoader().loadTile(var1);
         } catch (CantContinueException var7) {
            var10001 = false;
            continue;
         }

         if (var9 != null) {
            try {
               this.mCache.putTile(var1, var9);
               return;
            } catch (CantContinueException var6) {
               var10001 = false;
            }
         }
      }

   }

   public void addProvider(MapTileModuleProviderBase var1) {
      this.mProviders.add(var1);
   }

   public void fill() {
      if (!this.mGC.isRunning()) {
         this.refresh();
         this.mGC.gc();
      }
   }
}
