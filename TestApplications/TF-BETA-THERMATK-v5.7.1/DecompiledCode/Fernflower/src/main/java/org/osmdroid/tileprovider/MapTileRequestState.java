package org.osmdroid.tileprovider;

import java.util.List;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;

public class MapTileRequestState {
   private int index;
   private final IMapTileProviderCallback mCallback;
   private MapTileModuleProviderBase mCurrentProvider;
   private final long mMapTileIndex;
   private final List mProviderQueue;

   public MapTileRequestState(long var1, List var3, IMapTileProviderCallback var4) {
      this.mProviderQueue = var3;
      this.mMapTileIndex = var1;
      this.mCallback = var4;
   }

   public IMapTileProviderCallback getCallback() {
      return this.mCallback;
   }

   public long getMapTile() {
      return this.mMapTileIndex;
   }

   public MapTileModuleProviderBase getNextProvider() {
      MapTileModuleProviderBase var1;
      if (this.isEmpty()) {
         var1 = null;
      } else {
         List var3 = this.mProviderQueue;
         int var2 = this.index++;
         var1 = (MapTileModuleProviderBase)var3.get(var2);
      }

      this.mCurrentProvider = var1;
      return this.mCurrentProvider;
   }

   public boolean isEmpty() {
      List var1 = this.mProviderQueue;
      boolean var2;
      if (var1 != null && this.index < var1.size()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }
}
