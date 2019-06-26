package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileContainer;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileProviderArray extends MapTileProviderBase implements MapTileContainer {
   private IRegisterReceiver mRegisterReceiver;
   protected final List mTileProviderList;
   private final Map mWorking;

   protected MapTileProviderArray(ITileSource var1, IRegisterReceiver var2) {
      this(var1, var2, new MapTileModuleProviderBase[0]);
   }

   public MapTileProviderArray(ITileSource var1, IRegisterReceiver var2, MapTileModuleProviderBase[] var3) {
      super(var1);
      this.mWorking = new HashMap();
      this.mRegisterReceiver = null;
      this.mRegisterReceiver = var2;
      this.mTileProviderList = new ArrayList();
      Collections.addAll(this.mTileProviderList, var3);
   }

   private void remove(long param1) {
      // $FF: Couldn't be decompiled
   }

   private void runAsyncNextProvider(MapTileRequestState param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean contains(long param1) {
      // $FF: Couldn't be decompiled
   }

   public void detach() {
      // $FF: Couldn't be decompiled
   }

   protected MapTileModuleProviderBase findNextAppropriateProvider(MapTileRequestState var1) {
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;

      MapTileModuleProviderBase var5;
      while(true) {
         var5 = var1.getNextProvider();
         boolean var6 = var2;
         boolean var7 = var3;
         boolean var8 = var4;
         if (var5 != null) {
            boolean var9 = this.getProviderExists(var5);
            var3 = true;
            var6 = var9 ^ true;
            if (!this.useDataConnection() && var5.getUsesDataConnection()) {
               var8 = true;
            } else {
               var8 = false;
            }

            int var10 = MapTileIndex.getZoom(var1.getMapTile());
            var4 = var3;
            if (var10 <= var5.getMaximumZoomLevel()) {
               if (var10 < var5.getMinimumZoomLevel()) {
                  var4 = var3;
               } else {
                  var4 = false;
               }
            }

            var7 = var8;
            var8 = var4;
         }

         if (var5 == null) {
            break;
         }

         var2 = var6;
         var3 = var7;
         var4 = var8;
         if (!var6) {
            var2 = var6;
            var3 = var7;
            var4 = var8;
            if (!var7) {
               var2 = var6;
               var3 = var7;
               var4 = var8;
               if (!var8) {
                  break;
               }
            }
         }
      }

      return var5;
   }

   public Drawable getMapTile(long var1) {
      Drawable var3 = super.mTileCache.getMapTile(var1);
      if (var3 != null) {
         if (ExpirableBitmapDrawable.getState(var3) == -1) {
            return var3;
         }

         if (this.isDowngradedMode(var1)) {
            return var3;
         }
      }

      Map var4 = this.mWorking;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label177: {
         try {
            if (this.mWorking.containsKey(var1)) {
               return var3;
            }
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label177;
         }

         try {
            this.mWorking.put(var1, 0);
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label177;
         }

         this.runAsyncNextProvider(new MapTileRequestState(var1, this.mTileProviderList, this));
         return var3;
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

   public int getMaximumZoomLevel() {
      List var1 = this.mTileProviderList;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label230: {
         Iterator var2;
         try {
            var2 = this.mTileProviderList.iterator();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label230;
         }

         int var3 = 0;

         while(true) {
            try {
               if (!var2.hasNext()) {
                  break;
               }

               MapTileModuleProviderBase var4 = (MapTileModuleProviderBase)var2.next();
               if (var4.getMaximumZoomLevel() > var3) {
                  var3 = var4.getMaximumZoomLevel();
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label230;
            }
         }

         label212:
         try {
            return var3;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label212;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   public int getMinimumZoomLevel() {
      int var1 = TileSystem.getMaximumZoomLevel();
      List var2 = this.mTileProviderList;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label221: {
         Iterator var3;
         try {
            var3 = this.mTileProviderList.iterator();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label221;
         }

         while(true) {
            try {
               while(var3.hasNext()) {
                  MapTileModuleProviderBase var4 = (MapTileModuleProviderBase)var3.next();
                  if (var4.getMinimumZoomLevel() < var1) {
                     var1 = var4.getMinimumZoomLevel();
                  }
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            try {
               return var1;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean getProviderExists(MapTileModuleProviderBase var1) {
      return this.mTileProviderList.contains(var1);
   }

   protected boolean isDowngradedMode(long var1) {
      return false;
   }

   public void mapTileRequestCompleted(MapTileRequestState var1, Drawable var2) {
      super.mapTileRequestCompleted(var1, var2);
      this.remove(var1.getMapTile());
   }

   public void mapTileRequestExpiredTile(MapTileRequestState param1, Drawable param2) {
      // $FF: Couldn't be decompiled
   }

   public void mapTileRequestFailed(MapTileRequestState var1) {
      this.runAsyncNextProvider(var1);
   }

   public void mapTileRequestFailedExceedsMaxQueueSize(MapTileRequestState var1) {
      super.mapTileRequestFailed(var1);
      this.remove(var1.getMapTile());
   }

   public void setTileSource(ITileSource var1) {
      super.setTileSource(var1);
      List var2 = this.mTileProviderList;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var3;
         try {
            var3 = this.mTileProviderList.iterator();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var3.hasNext()) {
                  ((MapTileModuleProviderBase)var3.next()).setTileSource(var1);
                  this.clearTileCache();
                  continue;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }
}
