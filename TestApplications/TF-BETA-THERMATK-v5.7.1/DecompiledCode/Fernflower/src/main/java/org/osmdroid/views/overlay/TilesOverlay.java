package org.osmdroid.views.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.tileprovider.TileStates;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.RectL;
import org.osmdroid.util.TileLooper;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;

public class TilesOverlay extends Overlay implements IOverlayMenuProvider {
   public static final ColorFilter INVERT_COLORS;
   public static final int MENU_MAP_MODE = Overlay.getSafeMenuId();
   public static final int MENU_OFFLINE = Overlay.getSafeMenuId();
   public static final int MENU_SNAPSHOT = Overlay.getSafeMenuId();
   public static final int MENU_STATES = Overlay.getSafeMenuId();
   public static final int MENU_TILE_SOURCE_STARTING_ID = Overlay.getSafeMenuIdSequence(TileSourceFactory.getTileSources().size());
   static final float[] negate = new float[]{-1.0F, 0.0F, 0.0F, 0.0F, 255.0F, 0.0F, -1.0F, 0.0F, 0.0F, 255.0F, 0.0F, 0.0F, -1.0F, 0.0F, 255.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
   private Context ctx;
   private ColorFilter currentColorFilter = null;
   private boolean horizontalWrapEnabled = true;
   private Rect mCanvasRect;
   protected final Paint mDebugPaint = new Paint();
   private final Rect mIntersectionRect = new Rect();
   private int mLoadingBackgroundColor = Color.rgb(216, 208, 208);
   private int mLoadingLineColor = Color.rgb(200, 192, 192);
   private BitmapDrawable mLoadingTile = null;
   private boolean mOptionsMenuEnabled = true;
   protected Projection mProjection;
   private final Rect mProtectedTiles = new Rect();
   private final TilesOverlay.OverlayTileLooper mTileLooper = new TilesOverlay.OverlayTileLooper();
   protected final MapTileProviderBase mTileProvider;
   private final Rect mTileRect = new Rect();
   private final TileStates mTileStates = new TileStates();
   protected final RectL mViewPort = new RectL();
   protected Drawable userSelectedLoadingDrawable = null;
   private boolean verticalWrapEnabled = true;

   static {
      INVERT_COLORS = new ColorMatrixColorFilter(negate);
   }

   public TilesOverlay(MapTileProviderBase var1, Context var2, boolean var3, boolean var4) {
      this.ctx = var2;
      if (var1 != null) {
         this.mTileProvider = var1;
         this.setHorizontalWrapEnabled(var3);
         this.setVerticalWrapEnabled(var4);
      } else {
         throw new IllegalArgumentException("You must pass a valid tile provider to the tiles overlay.");
      }
   }

   private void clearLoadingTile() {
      BitmapDrawable var1 = this.mLoadingTile;
      this.mLoadingTile = null;
      BitmapPool.getInstance().asyncRecycle(var1);
   }

   private Drawable getLoadingTile() {
      Drawable var1 = this.userSelectedLoadingDrawable;
      if (var1 != null) {
         return var1;
      } else {
         if (this.mLoadingTile == null && this.mLoadingBackgroundColor != 0) {
            label85: {
               label77: {
                  boolean var10001;
                  int var2;
                  label65: {
                     try {
                        if (this.mTileProvider.getTileSource() != null) {
                           var2 = this.mTileProvider.getTileSource().getTileSizePixels();
                           break label65;
                        }
                     } catch (OutOfMemoryError var15) {
                        var10001 = false;
                        break label85;
                     } catch (NullPointerException var16) {
                        var10001 = false;
                        break label77;
                     }

                     var2 = 256;
                  }

                  Bitmap var17;
                  Canvas var3;
                  Paint var4;
                  int var5;
                  try {
                     var17 = Bitmap.createBitmap(var2, var2, Config.ARGB_8888);
                     var3 = new Canvas(var17);
                     var4 = new Paint();
                     var3.drawColor(this.mLoadingBackgroundColor);
                     var4.setColor(this.mLoadingLineColor);
                     var4.setStrokeWidth(0.0F);
                     var5 = var2 / 16;
                  } catch (OutOfMemoryError var13) {
                     var10001 = false;
                     break label85;
                  } catch (NullPointerException var14) {
                     var10001 = false;
                     break label77;
                  }

                  for(int var6 = 0; var6 < var2; var6 += var5) {
                     float var7 = (float)var6;
                     float var8 = (float)var2;

                     try {
                        var3.drawLine(0.0F, var7, var8, var7, var4);
                        var3.drawLine(var7, 0.0F, var7, var8, var4);
                     } catch (OutOfMemoryError var11) {
                        var10001 = false;
                        break label85;
                     } catch (NullPointerException var12) {
                        var10001 = false;
                        break label77;
                     }
                  }

                  try {
                     BitmapDrawable var18 = new BitmapDrawable(var17);
                     this.mLoadingTile = var18;
                     return this.mLoadingTile;
                  } catch (OutOfMemoryError var9) {
                     var10001 = false;
                     break label85;
                  } catch (NullPointerException var10) {
                     var10001 = false;
                  }
               }

               Log.e("OsmDroid", "NullPointerException getting loading tile");
               System.gc();
               return this.mLoadingTile;
            }

            Log.e("OsmDroid", "OutOfMemoryError getting loading tile");
            System.gc();
         }

         return this.mLoadingTile;
      }
   }

   public void draw(Canvas var1, Projection var2) {
      if (Configuration.getInstance().isDebugTileProviders()) {
         Log.d("OsmDroid", "onDraw");
      }

      if (this.setViewPort(var1, var2)) {
         this.drawTiles(var1, this.getProjection(), this.getProjection().getZoomLevel(), this.mViewPort);
      }
   }

   public void drawTiles(Canvas var1, Projection var2, double var3, RectL var5) {
      this.mProjection = var2;
      this.mTileLooper.loop(var3, var5, var1);
   }

   protected Rect getCanvasRect() {
      return this.mCanvasRect;
   }

   public int getMaximumZoomLevel() {
      return this.mTileProvider.getMaximumZoomLevel();
   }

   public int getMinimumZoomLevel() {
      return this.mTileProvider.getMinimumZoomLevel();
   }

   protected Projection getProjection() {
      return this.mProjection;
   }

   public void onDetach(MapView var1) {
      this.mTileProvider.detach();
      this.ctx = null;
      BitmapPool.getInstance().asyncRecycle(this.mLoadingTile);
      this.mLoadingTile = null;
      BitmapPool.getInstance().asyncRecycle(this.userSelectedLoadingDrawable);
      this.userSelectedLoadingDrawable = null;
   }

   protected void onTileReadyToDraw(Canvas var1, Drawable var2, Rect var3) {
      var2.setColorFilter(this.currentColorFilter);
      var2.setBounds(var3.left, var3.top, var3.right, var3.bottom);
      var3 = this.getCanvasRect();
      if (var3 == null) {
         var2.draw(var1);
      } else if (this.mIntersectionRect.setIntersect(var1.getClipBounds(), var3)) {
         var1.save();
         var1.clipRect(this.mIntersectionRect);
         var2.draw(var1);
         var1.restore();
      }
   }

   public void protectDisplayedTilesForCache(Canvas var1, Projection var2) {
      if (this.setViewPort(var1, var2)) {
         TileSystem.getTileFromMercator(this.mViewPort, TileSystem.getTileSize(this.mProjection.getZoomLevel()), this.mProtectedTiles);
         int var3 = TileSystem.getInputTileZoomLevel(this.mProjection.getZoomLevel());
         this.mTileProvider.getTileCache().getMapTileArea().set(var3, this.mProtectedTiles);
         this.mTileProvider.getTileCache().maintenance();
      }
   }

   public void setHorizontalWrapEnabled(boolean var1) {
      this.horizontalWrapEnabled = var1;
      this.mTileLooper.setHorizontalWrapEnabled(var1);
   }

   public void setLoadingBackgroundColor(int var1) {
      if (this.mLoadingBackgroundColor != var1) {
         this.mLoadingBackgroundColor = var1;
         this.clearLoadingTile();
      }

   }

   protected void setProjection(Projection var1) {
      this.mProjection = var1;
   }

   public void setUseDataConnection(boolean var1) {
      this.mTileProvider.setUseDataConnection(var1);
   }

   public void setVerticalWrapEnabled(boolean var1) {
      this.verticalWrapEnabled = var1;
      this.mTileLooper.setVerticalWrapEnabled(var1);
   }

   protected boolean setViewPort(Canvas var1, Projection var2) {
      this.setProjection(var2);
      this.getProjection().getMercatorViewPort(this.mViewPort);
      return true;
   }

   public boolean useDataConnection() {
      return this.mTileProvider.useDataConnection();
   }

   protected class OverlayTileLooper extends TileLooper {
      private Canvas mCanvas;

      public OverlayTileLooper() {
      }

      public void finaliseLoop() {
         TilesOverlay.this.mTileStates.finaliseLoop();
      }

      public void handleTile(long var1, int var3, int var4) {
         Drawable var5 = TilesOverlay.this.mTileProvider.getMapTile(var1);
         TilesOverlay.this.mTileStates.handleTile(var5);
         if (this.mCanvas != null) {
            boolean var6 = var5 instanceof ReusableBitmapDrawable;
            ReusableBitmapDrawable var7;
            if (var6) {
               var7 = (ReusableBitmapDrawable)var5;
            } else {
               var7 = null;
            }

            Drawable var8 = var5;
            if (var5 == null) {
               var8 = TilesOverlay.this.getLoadingTile();
            }

            if (var8 != null) {
               TilesOverlay var23 = TilesOverlay.this;
               var23.mProjection.getPixelFromTile(var3, var4, var23.mTileRect);
               if (var6) {
                  var7.beginUsingDrawable();
               }

               boolean var9;
               label290: {
                  boolean var10;
                  Throwable var10000;
                  label297: {
                     var5 = var8;
                     var9 = var6;
                     boolean var10001;
                     if (var6) {
                        label295: {
                           var5 = var8;
                           var9 = var6;
                           var10 = var6;

                           try {
                              if (var7.isBitmapValid()) {
                                 break label295;
                              }
                           } catch (Throwable var22) {
                              var10000 = var22;
                              var10001 = false;
                              break label297;
                           }

                           var10 = var6;

                           try {
                              var5 = TilesOverlay.this.getLoadingTile();
                           } catch (Throwable var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label297;
                           }

                           var9 = false;
                        }
                     }

                     var10 = var9;

                     label267:
                     try {
                        TilesOverlay.this.onTileReadyToDraw(this.mCanvas, var5, TilesOverlay.this.mTileRect);
                        break label290;
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label267;
                     }
                  }

                  Throwable var24 = var10000;
                  if (var10) {
                     var7.finishUsingDrawable();
                  }

                  throw var24;
               }

               if (var9) {
                  var7.finishUsingDrawable();
               }
            }

            if (Configuration.getInstance().isDebugTileProviders()) {
               TilesOverlay var25 = TilesOverlay.this;
               var25.mProjection.getPixelFromTile(var3, var4, var25.mTileRect);
               this.mCanvas.drawText(MapTileIndex.toString(var1), (float)(TilesOverlay.this.mTileRect.left + 1), (float)TilesOverlay.this.mTileRect.top + TilesOverlay.this.mDebugPaint.getTextSize(), TilesOverlay.this.mDebugPaint);
               this.mCanvas.drawLine((float)TilesOverlay.this.mTileRect.left, (float)TilesOverlay.this.mTileRect.top, (float)TilesOverlay.this.mTileRect.right, (float)TilesOverlay.this.mTileRect.top, TilesOverlay.this.mDebugPaint);
               this.mCanvas.drawLine((float)TilesOverlay.this.mTileRect.left, (float)TilesOverlay.this.mTileRect.top, (float)TilesOverlay.this.mTileRect.left, (float)TilesOverlay.this.mTileRect.bottom, TilesOverlay.this.mDebugPaint);
            }

         }
      }

      public void initialiseLoop() {
         Rect var1 = super.mTiles;
         int var2 = var1.right;
         int var3 = var1.left;
         int var4 = var1.bottom;
         int var5 = var1.top;
         TilesOverlay.this.mTileProvider.ensureCapacity((var4 - var5 + 1) * (var2 - var3 + 1) + Configuration.getInstance().getCacheMapTileOvershoot());
         TilesOverlay.this.mTileStates.initialiseLoop();
         super.initialiseLoop();
      }

      public void loop(double var1, RectL var3, Canvas var4) {
         this.mCanvas = var4;
         this.loop(var1, var3);
      }
   }
}
