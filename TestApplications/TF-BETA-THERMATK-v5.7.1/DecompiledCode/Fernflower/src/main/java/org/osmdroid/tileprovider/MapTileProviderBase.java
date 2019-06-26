package org.osmdroid.tileprovider;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.modules.MapTileApproximater;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.PointL;
import org.osmdroid.util.RectL;
import org.osmdroid.util.TileLooper;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.Projection;

public abstract class MapTileProviderBase implements IMapTileProviderCallback {
   protected final MapTileCache mTileCache;
   protected Drawable mTileNotFoundImage;
   private final Collection mTileRequestCompleteHandlers;
   private ITileSource mTileSource;
   protected boolean mUseDataConnection;

   public MapTileProviderBase(ITileSource var1) {
      this(var1, (Handler)null);
   }

   public MapTileProviderBase(ITileSource var1, Handler var2) {
      this.mTileRequestCompleteHandlers = new LinkedHashSet();
      this.mUseDataConnection = true;
      this.mTileNotFoundImage = null;
      this.mTileCache = this.createTileCache();
      this.mTileRequestCompleteHandlers.add(var2);
      this.mTileSource = var1;
   }

   public void clearTileCache() {
      this.mTileCache.clear();
   }

   public MapTileCache createTileCache() {
      return new MapTileCache();
   }

   public void detach() {
      BitmapPool.getInstance().asyncRecycle(this.mTileNotFoundImage);
      this.mTileNotFoundImage = null;
      this.clearTileCache();
   }

   public void ensureCapacity(int var1) {
      this.mTileCache.ensureCapacity(var1);
   }

   public abstract Drawable getMapTile(long var1);

   public abstract int getMaximumZoomLevel();

   public abstract int getMinimumZoomLevel();

   public MapTileCache getTileCache() {
      return this.mTileCache;
   }

   public Collection getTileRequestCompleteHandlers() {
      return this.mTileRequestCompleteHandlers;
   }

   public ITileSource getTileSource() {
      return this.mTileSource;
   }

   public void mapTileRequestCompleted(MapTileRequestState var1, Drawable var2) {
      this.putTileIntoCache(var1.getMapTile(), var2, -1);
      Iterator var4 = this.mTileRequestCompleteHandlers.iterator();

      while(var4.hasNext()) {
         Handler var3 = (Handler)var4.next();
         if (var3 != null) {
            var3.sendEmptyMessage(0);
         }
      }

      if (Configuration.getInstance().isDebugTileProviders()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("MapTileProviderBase.mapTileRequestCompleted(): ");
         var5.append(MapTileIndex.toString(var1.getMapTile()));
         Log.d("OsmDroid", var5.toString());
      }

   }

   public void mapTileRequestExpiredTile(MapTileRequestState var1, Drawable var2) {
      this.putTileIntoCache(var1.getMapTile(), var2, ExpirableBitmapDrawable.getState(var2));
      Iterator var4 = this.mTileRequestCompleteHandlers.iterator();

      while(var4.hasNext()) {
         Handler var3 = (Handler)var4.next();
         if (var3 != null) {
            var3.sendEmptyMessage(0);
         }
      }

      if (Configuration.getInstance().isDebugTileProviders()) {
         StringBuilder var5 = new StringBuilder();
         var5.append("MapTileProviderBase.mapTileRequestExpiredTile(): ");
         var5.append(MapTileIndex.toString(var1.getMapTile()));
         Log.d("OsmDroid", var5.toString());
      }

   }

   public void mapTileRequestFailed(MapTileRequestState var1) {
      Iterator var2;
      Handler var3;
      if (this.mTileNotFoundImage != null) {
         this.putTileIntoCache(var1.getMapTile(), this.mTileNotFoundImage, -4);
         var2 = this.mTileRequestCompleteHandlers.iterator();

         while(var2.hasNext()) {
            var3 = (Handler)var2.next();
            if (var3 != null) {
               var3.sendEmptyMessage(0);
            }
         }
      } else {
         var2 = this.mTileRequestCompleteHandlers.iterator();

         while(var2.hasNext()) {
            var3 = (Handler)var2.next();
            if (var3 != null) {
               var3.sendEmptyMessage(1);
            }
         }
      }

      if (Configuration.getInstance().isDebugTileProviders()) {
         StringBuilder var4 = new StringBuilder();
         var4.append("MapTileProviderBase.mapTileRequestFailed(): ");
         var4.append(MapTileIndex.toString(var1.getMapTile()));
         Log.d("OsmDroid", var4.toString());
      }

   }

   protected void putTileIntoCache(long var1, Drawable var3, int var4) {
      if (var3 != null) {
         Drawable var5 = this.mTileCache.getMapTile(var1);
         if (var5 == null || ExpirableBitmapDrawable.getState(var5) <= var4) {
            ExpirableBitmapDrawable.setState(var3, var4);
            this.mTileCache.putTile(var1, var3);
         }
      }
   }

   public void rescaleCache(Projection var1, double var2, double var4, Rect var6) {
      if (TileSystem.getInputTileZoomLevel(var2) != TileSystem.getInputTileZoomLevel(var4)) {
         long var7 = System.currentTimeMillis();
         if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder var9 = new StringBuilder();
            var9.append("rescale tile cache from ");
            var9.append(var4);
            var9.append(" to ");
            var9.append(var2);
            Log.i("OsmDroid", var9.toString());
         }

         PointL var16 = var1.toMercatorPixels(var6.left, var6.top, (PointL)null);
         PointL var12 = var1.toMercatorPixels(var6.right, var6.bottom, (PointL)null);
         RectL var15 = new RectL(var16.x, var16.y, var12.x, var12.y);
         Object var13;
         if (var2 > var4) {
            var13 = new MapTileProviderBase.ZoomInTileLooper();
         } else {
            var13 = new MapTileProviderBase.ZoomOutTileLooper();
         }

         ((MapTileProviderBase.ScaleTileLooper)var13).loop(var2, var15, var4, this.getTileSource().getTileSizePixels());
         long var10 = System.currentTimeMillis();
         if (Configuration.getInstance().isDebugTileProviders()) {
            StringBuilder var14 = new StringBuilder();
            var14.append("Finished rescale in ");
            var14.append(var10 - var7);
            var14.append("ms");
            Log.i("OsmDroid", var14.toString());
         }

      }
   }

   public void setTileSource(ITileSource var1) {
      this.mTileSource = var1;
      this.clearTileCache();
   }

   public void setUseDataConnection(boolean var1) {
      this.mUseDataConnection = var1;
   }

   public boolean useDataConnection() {
      return this.mUseDataConnection;
   }

   private abstract class ScaleTileLooper extends TileLooper {
      private boolean isWorth;
      protected Paint mDebugPaint;
      protected Rect mDestRect;
      protected int mDiff;
      protected final HashMap mNewTiles;
      protected int mOldTileZoomLevel;
      protected Rect mSrcRect;
      protected int mTileSize;
      protected int mTileSize_2;

      private ScaleTileLooper() {
         this.mNewTiles = new HashMap();
      }

      // $FF: synthetic method
      ScaleTileLooper(Object var2) {
         this();
      }

      protected abstract void computeTile(long var1, int var3, int var4);

      public void finaliseLoop() {
         while(!this.mNewTiles.isEmpty()) {
            long var1 = (Long)this.mNewTiles.keySet().iterator().next();
            this.putScaledTileIntoCache(var1, (Bitmap)this.mNewTiles.remove(var1));
         }

      }

      public void handleTile(long var1, int var3, int var4) {
         if (this.isWorth) {
            if (MapTileProviderBase.this.getMapTile(var1) == null) {
               try {
                  this.computeTile(var1, var3, var4);
               } catch (OutOfMemoryError var6) {
                  Log.e("OsmDroid", "OutOfMemoryError rescaling cache");
               }
            }

         }
      }

      public void initialiseLoop() {
         super.initialiseLoop();
         this.mDiff = Math.abs(super.mTileZoomLevel - this.mOldTileZoomLevel);
         int var1 = this.mTileSize;
         int var2 = this.mDiff;
         this.mTileSize_2 = var1 >> var2;
         boolean var3;
         if (var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isWorth = var3;
      }

      public void loop(double var1, RectL var3, double var4, int var6) {
         this.mSrcRect = new Rect();
         this.mDestRect = new Rect();
         this.mDebugPaint = new Paint();
         this.mOldTileZoomLevel = TileSystem.getInputTileZoomLevel(var4);
         this.mTileSize = var6;
         this.loop(var1, var3);
      }

      protected void putScaledTileIntoCache(long var1, Bitmap var3) {
         ReusableBitmapDrawable var4 = new ReusableBitmapDrawable(var3);
         MapTileProviderBase.this.putTileIntoCache(var1, var4, -3);
         if (Configuration.getInstance().isDebugMode()) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Created scaled tile: ");
            var5.append(MapTileIndex.toString(var1));
            Log.d("OsmDroid", var5.toString());
            this.mDebugPaint.setTextSize(40.0F);
            (new Canvas(var3)).drawText("scaled", 50.0F, 50.0F, this.mDebugPaint);
         }

      }
   }

   private class ZoomInTileLooper extends MapTileProviderBase.ScaleTileLooper {
      private ZoomInTileLooper() {
         super(null);
      }

      // $FF: synthetic method
      ZoomInTileLooper(Object var2) {
         this();
      }

      public void computeTile(long var1, int var3, int var4) {
         long var5 = MapTileIndex.getTileIndex(super.mOldTileZoomLevel, MapTileIndex.getX(var1) >> super.mDiff, MapTileIndex.getY(var1) >> super.mDiff);
         Drawable var7 = MapTileProviderBase.this.mTileCache.getMapTile(var5);
         if (var7 instanceof BitmapDrawable) {
            Bitmap var8 = MapTileApproximater.approximateTileFromLowerZoom((BitmapDrawable)var7, var1, super.mDiff);
            if (var8 != null) {
               super.mNewTiles.put(var1, var8);
            }
         }

      }
   }

   private class ZoomOutTileLooper extends MapTileProviderBase.ScaleTileLooper {
      private ZoomOutTileLooper() {
         super(null);
      }

      // $FF: synthetic method
      ZoomOutTileLooper(Object var2) {
         this();
      }

      protected void computeTile(long var1, int var3, int var4) {
         if (super.mDiff < 4) {
            int var5 = MapTileIndex.getX(var1);
            int var6 = super.mDiff;
            int var7 = MapTileIndex.getY(var1);
            int var8 = super.mDiff;
            int var9 = 1 << var8;
            Bitmap var10 = null;
            Object var11 = var10;

            Object var18;
            for(var3 = 0; var3 < var9; var11 = var18) {
               Bitmap var19 = var10;
               var4 = 0;

               Object var16;
               for(var18 = var11; var4 < var9; var18 = var16) {
                  long var13 = MapTileIndex.getTileIndex(super.mOldTileZoomLevel, (var5 << var6) + var3, (var7 << var8) + var4);
                  Drawable var15 = MapTileProviderBase.this.mTileCache.getMapTile(var13);
                  Bitmap var21 = var19;
                  var16 = var18;
                  if (var15 instanceof BitmapDrawable) {
                     Bitmap var22 = ((BitmapDrawable)var15).getBitmap();
                     var21 = var19;
                     var16 = var18;
                     if (var22 != null) {
                        var21 = var19;
                        if (var19 == null) {
                           var21 = MapTileApproximater.getTileBitmap(super.mTileSize);
                           var18 = new Canvas(var21);
                           ((Canvas)var18).drawColor(-3355444);
                        }

                        Rect var20 = super.mDestRect;
                        int var17 = super.mTileSize_2;
                        var20.set(var3 * var17, var4 * var17, (var3 + 1) * var17, var17 * (var4 + 1));
                        ((Canvas)var18).drawBitmap(var22, (Rect)null, super.mDestRect, (Paint)null);
                        var16 = var18;
                     }
                  }

                  ++var4;
                  var19 = var21;
               }

               ++var3;
               var10 = var19;
            }

            if (var10 != null) {
               super.mNewTiles.put(var1, var10);
            }

         }
      }
   }
}
