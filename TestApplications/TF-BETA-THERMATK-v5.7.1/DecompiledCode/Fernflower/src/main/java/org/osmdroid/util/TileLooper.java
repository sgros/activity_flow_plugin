package org.osmdroid.util;

import android.graphics.Rect;

public abstract class TileLooper {
   private boolean horizontalWrapEnabled;
   protected int mTileZoomLevel;
   protected final Rect mTiles;
   private boolean verticalWrapEnabled;

   public TileLooper() {
      this(false, false);
   }

   public TileLooper(boolean var1, boolean var2) {
      this.mTiles = new Rect();
      this.horizontalWrapEnabled = true;
      this.verticalWrapEnabled = true;
      this.horizontalWrapEnabled = var1;
      this.verticalWrapEnabled = var2;
   }

   public abstract void finaliseLoop();

   public abstract void handleTile(long var1, int var3, int var4);

   public void initialiseLoop() {
   }

   protected void loop(double var1, RectL var3) {
      TileSystem.getTileFromMercator(var3, TileSystem.getTileSize(var1), this.mTiles);
      this.mTileZoomLevel = TileSystem.getInputTileZoomLevel(var1);
      this.initialiseLoop();
      int var4 = 1 << this.mTileZoomLevel;
      int var5 = this.mTiles.left;

      while(true) {
         Rect var9 = this.mTiles;
         if (var5 > var9.right) {
            this.finaliseLoop();
            return;
         }

         for(int var6 = var9.top; var6 <= this.mTiles.bottom; ++var6) {
            if ((this.horizontalWrapEnabled || var5 >= 0 && var5 < var4) && (this.verticalWrapEnabled || var6 >= 0 && var6 < var4)) {
               int var7 = MyMath.mod(var5, var4);
               int var8 = MyMath.mod(var6, var4);
               this.handleTile(MapTileIndex.getTileIndex(this.mTileZoomLevel, var7, var8), var5, var6);
            }
         }

         ++var5;
      }
   }

   public void setHorizontalWrapEnabled(boolean var1) {
      this.horizontalWrapEnabled = var1;
   }

   public void setVerticalWrapEnabled(boolean var1) {
      this.verticalWrapEnabled = var1;
   }
}
