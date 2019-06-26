package org.osmdroid.tileprovider.modules;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileApproximater extends MapTileModuleProviderBase {
   private final List mProviders;
   private int minZoomLevel;

   public MapTileApproximater() {
      this(Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
   }

   public MapTileApproximater(int var1, int var2) {
      super(var1, var2);
      this.mProviders = new CopyOnWriteArrayList();
   }

   public static Bitmap approximateTileFromLowerZoom(BitmapDrawable var0, long var1, int var3) {
      if (var3 <= 0) {
         return null;
      } else {
         int var4 = var0.getBitmap().getWidth();
         Bitmap var5 = getTileBitmap(var4);
         Canvas var6 = new Canvas(var5);
         boolean var7 = var0 instanceof ReusableBitmapDrawable;
         ReusableBitmapDrawable var8;
         if (var7) {
            var8 = (ReusableBitmapDrawable)var0;
         } else {
            var8 = null;
         }

         if (var7) {
            var8.beginUsingDrawable();
         }

         boolean var10;
         label257: {
            Throwable var10000;
            label258: {
               boolean var9 = false;
               boolean var10001;
               if (var7) {
                  var10 = var9;

                  try {
                     if (!var8.isBitmapValid()) {
                        break label257;
                     }
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label258;
                  }
               }

               int var28 = var4 >> var3;
               if (var28 == 0) {
                  var10 = var9;
                  break label257;
               }

               int var27;
               try {
                  var27 = MapTileIndex.getX(var1);
               } catch (Throwable var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label258;
               }

               int var11 = 1 << var3;
               var3 = var27 % var11 * var28;

               try {
                  var27 = MapTileIndex.getY(var1) % var11 * var28;
                  Rect var12 = new Rect(var3, var27, var3 + var28, var28 + var27);
                  Rect var13 = new Rect(0, 0, var4, var4);
                  var6.drawBitmap(var0.getBitmap(), var12, var13, (Paint)null);
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label258;
               }

               var10 = true;
               break label257;
            }

            Throwable var26 = var10000;
            if (var7) {
               var8.finishUsingDrawable();
            }

            throw var26;
         }

         if (var7) {
            var8.finishUsingDrawable();
         }

         return !var10 ? null : var5;
      }
   }

   public static Bitmap approximateTileFromLowerZoom(MapTileModuleProviderBase var0, long var1, int var3) {
      if (var3 <= 0) {
         return null;
      } else {
         int var4 = MapTileIndex.getZoom(var1) - var3;
         if (var4 < var0.getMinimumZoomLevel()) {
            return null;
         } else if (var4 > var0.getMaximumZoomLevel()) {
            return null;
         } else {
            long var5 = MapTileIndex.getTileIndex(var4, MapTileIndex.getX(var1) >> var3, MapTileIndex.getY(var1) >> var3);

            try {
               Drawable var8 = var0.getTileLoader().loadTile(var5);
               if (!(var8 instanceof BitmapDrawable)) {
                  return null;
               } else {
                  Bitmap var9 = approximateTileFromLowerZoom((BitmapDrawable)var8, var1, var3);
                  return var9;
               }
            } catch (Exception var7) {
               return null;
            }
         }
      }
   }

   private void computeZoomLevels() {
      this.minZoomLevel = 0;
      Iterator var1 = this.mProviders.iterator();
      boolean var2 = true;

      while(var1.hasNext()) {
         int var3 = ((MapTileModuleProviderBase)var1.next()).getMinimumZoomLevel();
         if (var2) {
            this.minZoomLevel = var3;
            var2 = false;
         } else {
            this.minZoomLevel = Math.min(this.minZoomLevel, var3);
         }
      }

   }

   public static Bitmap getTileBitmap(int var0) {
      Bitmap var1 = BitmapPool.getInstance().obtainSizedBitmapFromPool(var0, var0);
      return var1 != null ? var1 : Bitmap.createBitmap(var0, var0, Config.ARGB_8888);
   }

   public void addProvider(MapTileModuleProviderBase var1) {
      this.mProviders.add(var1);
      this.computeZoomLevels();
   }

   public Bitmap approximateTileFromLowerZoom(long var1) {
      for(int var3 = 1; MapTileIndex.getZoom(var1) - var3 >= 0; ++var3) {
         Bitmap var4 = this.approximateTileFromLowerZoom(var1, var3);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   public Bitmap approximateTileFromLowerZoom(long var1, int var3) {
      Iterator var4 = this.mProviders.iterator();

      Bitmap var5;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         var5 = approximateTileFromLowerZoom((MapTileModuleProviderBase)var4.next(), var1, var3);
      } while(var5 == null);

      return var5;
   }

   public void detach() {
      super.detach();
      this.mProviders.clear();
   }

   public int getMaximumZoomLevel() {
      return TileSystem.getMaximumZoomLevel();
   }

   public int getMinimumZoomLevel() {
      return this.minZoomLevel;
   }

   protected String getName() {
      return "Offline Tile Approximation Provider";
   }

   protected String getThreadGroupName() {
      return "approximater";
   }

   public MapTileApproximater.TileLoader getTileLoader() {
      return new MapTileApproximater.TileLoader();
   }

   public boolean getUsesDataConnection() {
      return false;
   }

   @Deprecated
   public void setTileSource(ITileSource var1) {
   }

   protected class TileLoader extends MapTileModuleProviderBase.TileLoader {
      protected TileLoader() {
         super();
      }

      public Drawable loadTile(long var1) {
         Bitmap var3 = MapTileApproximater.this.approximateTileFromLowerZoom(var1);
         if (var3 != null) {
            BitmapDrawable var4 = new BitmapDrawable(var3);
            ExpirableBitmapDrawable.setState(var4, -3);
            return var4;
         } else {
            return null;
         }
      }
   }
}
