package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.MapTileArea;
import org.osmdroid.util.MapTileAreaComputer;
import org.osmdroid.util.MapTileAreaList;
import org.osmdroid.util.MapTileContainer;
import org.osmdroid.util.MapTileList;

public class MapTileCache {
   private final MapTileAreaList mAdditionalMapTileList;
   private boolean mAutoEnsureCapacity;
   private final HashMap mCachedTiles;
   private int mCapacity;
   private final List mComputers;
   private final MapTileList mGC;
   private final MapTileArea mMapTileArea;
   private final MapTilePreCache mPreCache;
   private final List mProtectors;
   private boolean mStressedMemory;
   private MapTileCache.TileRemovedListener mTileRemovedListener;

   public MapTileCache() {
      this(Configuration.getInstance().getCacheMapTileCount());
   }

   public MapTileCache(int var1) {
      this.mCachedTiles = new HashMap();
      this.mMapTileArea = new MapTileArea();
      this.mAdditionalMapTileList = new MapTileAreaList();
      this.mGC = new MapTileList();
      this.mComputers = new ArrayList();
      this.mProtectors = new ArrayList();
      this.ensureCapacity(var1);
      this.mPreCache = new MapTilePreCache(this);
   }

   private void populateSyncCachedTiles(MapTileList var1) {
      HashMap var2 = this.mCachedTiles;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var3;
         try {
            var1.ensureCapacity(this.mCachedTiles.size());
            var1.clear();
            var3 = this.mCachedTiles.keySet().iterator();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var3.hasNext()) {
                  var1.put((Long)var3.next());
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

   private void refreshAdditionalLists() {
      Iterator var1 = this.mComputers.iterator();

      int var2;
      for(var2 = 0; var1.hasNext(); ++var2) {
         MapTileAreaComputer var3 = (MapTileAreaComputer)var1.next();
         MapTileArea var4;
         if (var2 < this.mAdditionalMapTileList.getList().size()) {
            var4 = (MapTileArea)this.mAdditionalMapTileList.getList().get(var2);
         } else {
            var4 = new MapTileArea();
            this.mAdditionalMapTileList.getList().add(var4);
         }

         var3.computeFromSource(this.mMapTileArea, var4);
      }

      while(var2 < this.mAdditionalMapTileList.getList().size()) {
         this.mAdditionalMapTileList.getList().remove(this.mAdditionalMapTileList.getList().size() - 1);
      }

   }

   private boolean shouldKeepTile(long var1) {
      if (this.mMapTileArea.contains(var1)) {
         return true;
      } else if (this.mAdditionalMapTileList.contains(var1)) {
         return true;
      } else {
         Iterator var3 = this.mProtectors.iterator();

         do {
            if (!var3.hasNext()) {
               return false;
            }
         } while(!((MapTileContainer)var3.next()).contains(var1));

         return true;
      }
   }

   public void clear() {
      MapTileList var1 = new MapTileList();
      this.populateSyncCachedTiles(var1);

      for(int var2 = 0; var2 < var1.getSize(); ++var2) {
         this.remove(var1.get(var2));
      }

      this.mCachedTiles.clear();
   }

   public boolean ensureCapacity(int var1) {
      if (this.mCapacity < var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Tile cache increased from ");
         var2.append(this.mCapacity);
         var2.append(" to ");
         var2.append(var1);
         Log.i("OsmDroid", var2.toString());
         this.mCapacity = var1;
         return true;
      } else {
         return false;
      }
   }

   public void garbageCollection() {
      int var1 = this.mCachedTiles.size();
      int var2;
      int var3;
      if (!this.mStressedMemory) {
         var2 = var1 - this.mCapacity;
         var3 = var2;
         if (var2 <= 0) {
            return;
         }
      } else {
         var3 = Integer.MAX_VALUE;
      }

      this.refreshAdditionalLists();
      var2 = var3;
      if (this.mAutoEnsureCapacity) {
         var2 = var3;
         if (this.ensureCapacity(this.mMapTileArea.size() + this.mAdditionalMapTileList.size())) {
            var2 = var3;
            if (!this.mStressedMemory) {
               var3 = var1 - this.mCapacity;
               var2 = var3;
               if (var3 <= 0) {
                  return;
               }
            }
         }
      }

      this.populateSyncCachedTiles(this.mGC);
      byte var6 = 0;
      var3 = var2;

      for(var2 = var6; var2 < this.mGC.getSize(); ++var2) {
         long var4 = this.mGC.get(var2);
         if (!this.shouldKeepTile(var4)) {
            this.remove(var4);
            var1 = var3 - 1;
            var3 = var1;
            if (var1 == 0) {
               break;
            }
         }
      }

   }

   public MapTileAreaList getAdditionalMapTileList() {
      return this.mAdditionalMapTileList;
   }

   public Drawable getMapTile(long param1) {
      // $FF: Couldn't be decompiled
   }

   public MapTileArea getMapTileArea() {
      return this.mMapTileArea;
   }

   public MapTilePreCache getPreCache() {
      return this.mPreCache;
   }

   public List getProtectedTileComputers() {
      return this.mComputers;
   }

   public List getProtectedTileContainers() {
      return this.mProtectors;
   }

   public MapTileCache.TileRemovedListener getTileRemovedListener() {
      return this.mTileRemovedListener;
   }

   public void maintenance() {
      this.garbageCollection();
      this.mPreCache.fill();
   }

   public void putTile(long param1, Drawable param3) {
      // $FF: Couldn't be decompiled
   }

   protected void remove(long param1) {
      // $FF: Couldn't be decompiled
   }

   public void setAutoEnsureCapacity(boolean var1) {
      this.mAutoEnsureCapacity = var1;
   }

   public void setStressedMemory(boolean var1) {
      this.mStressedMemory = var1;
   }

   public interface TileRemovedListener {
      void onTileRemoved(long var1);
   }
}
