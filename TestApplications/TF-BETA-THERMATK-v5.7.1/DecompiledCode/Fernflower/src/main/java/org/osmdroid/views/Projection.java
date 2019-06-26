package org.osmdroid.views;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IProjection;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeometryMath;
import org.osmdroid.util.PointL;
import org.osmdroid.util.RectL;
import org.osmdroid.util.TileSystem;

public class Projection implements IProjection {
   private boolean horizontalWrapEnabled;
   private final BoundingBox mBoundingBoxProjection;
   private final GeoPoint mCurrentCenter;
   private final Rect mIntrinsicScreenRectProjection;
   private final double mMercatorMapSize;
   private long mOffsetX;
   private long mOffsetY;
   private final float mOrientation;
   public final double mProjectedMapSize;
   private final Matrix mRotateAndScaleMatrix;
   private final float[] mRotateScalePoints;
   private final Rect mScreenRectProjection;
   private long mScrollX;
   private long mScrollY;
   private final double mTileSize;
   private final TileSystem mTileSystem;
   private final Matrix mUnrotateAndScaleMatrix;
   private final double mZoomLevelProjection;
   private boolean verticalWrapEnabled;

   public Projection(double var1, int var3, int var4, GeoPoint var5, float var6, boolean var7, boolean var8) {
      this(var1, new Rect(0, 0, var3, var4), var5, 0L, 0L, var6, var7, var8, MapView.getTileSystem());
   }

   public Projection(double var1, Rect var3, GeoPoint var4, long var5, long var7, float var9, boolean var10, boolean var11, TileSystem var12) {
      this.mProjectedMapSize = TileSystem.MapSize(30.0D);
      this.mRotateAndScaleMatrix = new Matrix();
      this.mUnrotateAndScaleMatrix = new Matrix();
      this.mRotateScalePoints = new float[2];
      this.mBoundingBoxProjection = new BoundingBox();
      this.mScreenRectProjection = new Rect();
      this.mCurrentCenter = new GeoPoint(0.0D, 0.0D);
      this.mZoomLevelProjection = var1;
      this.horizontalWrapEnabled = var10;
      this.verticalWrapEnabled = var11;
      this.mTileSystem = var12;
      this.mMercatorMapSize = TileSystem.MapSize(this.mZoomLevelProjection);
      this.mTileSize = TileSystem.getTileSize(this.mZoomLevelProjection);
      this.mIntrinsicScreenRectProjection = var3;
      if (var4 == null) {
         var4 = new GeoPoint(0.0D, 0.0D);
      }

      this.mScrollX = var5;
      this.mScrollY = var7;
      this.mOffsetX = (long)this.getScreenCenterX() - this.mScrollX - this.mTileSystem.getMercatorXFromLongitude(var4.getLongitude(), this.mMercatorMapSize, this.horizontalWrapEnabled);
      this.mOffsetY = (long)this.getScreenCenterY() - this.mScrollY - this.mTileSystem.getMercatorYFromLatitude(var4.getLatitude(), this.mMercatorMapSize, this.verticalWrapEnabled);
      this.mOrientation = var9;
      this.mRotateAndScaleMatrix.preRotate(this.mOrientation, (float)this.getScreenCenterX(), (float)this.getScreenCenterY());
      this.mRotateAndScaleMatrix.invert(this.mUnrotateAndScaleMatrix);
      this.refresh();
   }

   Projection(MapView var1) {
      this(var1.getZoomLevelDouble(), var1.getIntrinsicScreenRect((Rect)null), var1.getExpectedCenter(), var1.getMapScrollX(), var1.getMapScrollY(), var1.getMapOrientation(), var1.isHorizontalMapRepetitionEnabled(), var1.isVerticalMapRepetitionEnabled(), MapView.getTileSystem());
   }

   private Point applyMatrixToPoint(int var1, int var2, Point var3, Matrix var4, boolean var5) {
      if (var3 == null) {
         var3 = new Point();
      }

      if (var5) {
         float[] var6 = this.mRotateScalePoints;
         var6[0] = (float)var1;
         var6[1] = (float)var2;
         var4.mapPoints(var6);
         float[] var7 = this.mRotateScalePoints;
         var3.x = (int)var7[0];
         var3.y = (int)var7[1];
      } else {
         var3.x = var1;
         var3.y = var2;
      }

      return var3;
   }

   private long getCloserPixel(long var1, int var3, int var4, double var5) {
      long var7 = (long)((var3 + var4) / 2);
      long var9 = (long)var3;
      long var11 = 0L;
      long var13 = var11;
      long var15 = var1;
      double var17;
      if (var1 < var9) {
         for(var15 = var11; var1 < var9; var1 = var13) {
            var17 = (double)var1;
            Double.isNaN(var17);
            var13 = (long)(var17 + var5);
            var15 = var1;
         }

         if (var1 < (long)var4) {
            return var1;
         } else {
            return Math.abs(var7 - var1) < Math.abs(var7 - var15) ? var1 : var15;
         }
      } else {
         while(var15 >= var9) {
            var17 = (double)var15;
            Double.isNaN(var17);
            var1 = (long)(var17 - var5);
            var13 = var15;
            var15 = var1;
         }

         if (var13 < (long)var4) {
            return var13;
         } else {
            return Math.abs(var7 - var15) < Math.abs(var7 - var13) ? var15 : var13;
         }
      }
   }

   private long getLongPixelFromMercator(long var1, boolean var3, long var4, int var6, int var7) {
      var4 += var1;
      var1 = var4;
      if (var3) {
         var1 = this.getCloserPixel(var4, var6, var7, this.mMercatorMapSize);
      }

      return var1;
   }

   private long getLongPixelXFromMercator(long var1, boolean var3) {
      long var4 = this.mOffsetX;
      Rect var6 = this.mIntrinsicScreenRectProjection;
      return this.getLongPixelFromMercator(var1, var3, var4, var6.left, var6.right);
   }

   private long getLongPixelYFromMercator(long var1, boolean var3) {
      long var4 = this.mOffsetY;
      Rect var6 = this.mIntrinsicScreenRectProjection;
      return this.getLongPixelFromMercator(var1, var3, var4, var6.top, var6.bottom);
   }

   public static long getScrollableOffset(long var0, long var2, double var4, int var6, int var7) {
      while(true) {
         long var8 = var2 - var0;
         if (var8 >= 0L) {
            if (var8 < (long)(var6 - var7 * 2)) {
               long var12 = var8 / 2L;
               var8 = (long)(var6 / 2);
               var0 = var8 - var12 - var0;
               if (var0 > 0L) {
                  return var0;
               }

               var0 = var8 + var12 - var2;
               if (var0 < 0L) {
                  return var0;
               }

               return 0L;
            }

            var0 = (long)var7 - var0;
            if (var0 < 0L) {
               return var0;
            }

            var0 = (long)(var6 - var7) - var2;
            if (var0 > 0L) {
               return var0;
            }

            return 0L;
         }

         double var10 = (double)var2;
         Double.isNaN(var10);
         var2 = (long)(var10 + var4);
      }
   }

   private void refresh() {
      Rect var1 = this.mIntrinsicScreenRectProjection;
      this.fromPixels((var1.left + var1.right) / 2, (var1.top + var1.bottom) / 2, this.mCurrentCenter);
      var1 = this.mIntrinsicScreenRectProjection;
      IGeoPoint var4 = this.fromPixels(var1.right, var1.top, (GeoPoint)null, true);
      Rect var2 = this.mIntrinsicScreenRectProjection;
      IGeoPoint var5 = this.fromPixels(var2.left, var2.bottom, (GeoPoint)null, true);
      this.mBoundingBoxProjection.set(var4.getLatitude(), var4.getLongitude(), var5.getLatitude(), var5.getLongitude());
      float var3 = this.mOrientation;
      if (var3 != 0.0F && var3 != 180.0F) {
         GeometryMath.getBoundingBoxForRotatatedRectangle(this.mIntrinsicScreenRectProjection, this.getScreenCenterX(), this.getScreenCenterY(), this.mOrientation, this.mScreenRectProjection);
      } else {
         var2 = this.mScreenRectProjection;
         var1 = this.mIntrinsicScreenRectProjection;
         var2.left = var1.left;
         var2.top = var1.top;
         var2.right = var1.right;
         var2.bottom = var1.bottom;
      }

   }

   void adjustOffsets(double var1, double var3, boolean var5, int var6) {
      long var7 = 0L;
      long var9;
      if (var5) {
         var7 = getScrollableOffset(this.getLongPixelYFromLatitude(var1), this.getLongPixelYFromLatitude(var3), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.height(), var6);
         var9 = 0L;
      } else {
         var9 = getScrollableOffset(this.getLongPixelXFromLongitude(var1), this.getLongPixelXFromLongitude(var3), this.mMercatorMapSize, this.mIntrinsicScreenRectProjection.width(), var6);
      }

      this.adjustOffsets(var9, var7);
   }

   void adjustOffsets(long var1, long var3) {
      if (var1 != 0L || var3 != 0L) {
         this.mOffsetX += var1;
         this.mOffsetY += var3;
         this.mScrollX -= var1;
         this.mScrollY -= var3;
         this.refresh();
      }
   }

   public void adjustOffsets(IGeoPoint var1, PointF var2) {
      if (var2 != null) {
         Point var4 = this.unrotateAndScalePoint((int)var2.x, (int)var2.y, (Point)null);
         Point var3 = this.toPixels(var1, (Point)null);
         this.adjustOffsets((long)(var4.x - var3.x), (long)(var4.y - var3.y));
      }
   }

   public void detach() {
   }

   public IGeoPoint fromPixels(int var1, int var2) {
      return this.fromPixels(var1, var2, (GeoPoint)null, false);
   }

   public IGeoPoint fromPixels(int var1, int var2, GeoPoint var3) {
      return this.fromPixels(var1, var2, var3, false);
   }

   public IGeoPoint fromPixels(int var1, int var2, GeoPoint var3, boolean var4) {
      TileSystem var5 = this.mTileSystem;
      long var6 = this.getCleanMercator(this.getMercatorXFromPixel(var1), this.horizontalWrapEnabled);
      long var8 = this.getCleanMercator(this.getMercatorYFromPixel(var2), this.verticalWrapEnabled);
      double var10 = this.mMercatorMapSize;
      boolean var12;
      if (!this.horizontalWrapEnabled && !var4) {
         var12 = false;
      } else {
         var12 = true;
      }

      if (!this.verticalWrapEnabled && !var4) {
         var4 = false;
      } else {
         var4 = true;
      }

      return var5.getGeoFromMercator(var6, var8, var10, var3, var12, var4);
   }

   public BoundingBox getBoundingBox() {
      return this.mBoundingBoxProjection;
   }

   public long getCleanMercator(long var1, boolean var3) {
      return this.mTileSystem.getCleanMercator(var1, this.mMercatorMapSize, var3);
   }

   public GeoPoint getCurrentCenter() {
      return this.mCurrentCenter;
   }

   public Matrix getInvertedScaleRotateCanvasMatrix() {
      return this.mUnrotateAndScaleMatrix;
   }

   public long getLongPixelXFromLongitude(double var1) {
      return this.getLongPixelXFromMercator(this.mTileSystem.getMercatorXFromLongitude(var1, this.mMercatorMapSize, false), false);
   }

   public long getLongPixelXFromLongitude(double var1, boolean var3) {
      TileSystem var4 = this.mTileSystem;
      double var5 = this.mMercatorMapSize;
      if (!this.horizontalWrapEnabled && !var3) {
         var3 = false;
      } else {
         var3 = true;
      }

      return this.getLongPixelXFromMercator(var4.getMercatorXFromLongitude(var1, var5, var3), this.horizontalWrapEnabled);
   }

   public long getLongPixelYFromLatitude(double var1) {
      return this.getLongPixelYFromMercator(this.mTileSystem.getMercatorYFromLatitude(var1, this.mMercatorMapSize, false), false);
   }

   public long getLongPixelYFromLatitude(double var1, boolean var3) {
      TileSystem var4 = this.mTileSystem;
      double var5 = this.mMercatorMapSize;
      if (!this.verticalWrapEnabled && !var3) {
         var3 = false;
      } else {
         var3 = true;
      }

      return this.getLongPixelYFromMercator(var4.getMercatorYFromLatitude(var1, var5, var3), this.verticalWrapEnabled);
   }

   public long getMercatorFromTile(int var1) {
      return TileSystem.getMercatorFromTile(var1, this.mTileSize);
   }

   public RectL getMercatorViewPort(RectL var1) {
      if (var1 == null) {
         var1 = new RectL();
      }

      Rect var2 = this.mIntrinsicScreenRectProjection;
      int var3 = var2.left;
      float var4 = (float)var3;
      int var5 = var2.right;
      float var6 = (float)var5;
      int var7 = var2.top;
      float var8 = (float)var7;
      int var9 = var2.bottom;
      float var10 = (float)var9;
      float var11 = var4;
      float var12 = var6;
      float var13 = var8;
      float var14 = var10;
      if (this.mOrientation != 0.0F) {
         float[] var16 = new float[8];
         var11 = (float)var3;
         int var15 = 0;
         var16[0] = var11;
         var16[1] = (float)var7;
         var16[2] = (float)var5;
         var16[3] = (float)var9;
         var16[4] = (float)var3;
         var16[5] = (float)var9;
         var16[6] = (float)var5;
         var16[7] = (float)var7;
         this.mUnrotateAndScaleMatrix.mapPoints(var16);

         while(true) {
            var11 = var4;
            var12 = var6;
            var13 = var8;
            var14 = var10;
            if (var15 >= 8) {
               break;
            }

            var11 = var4;
            if (var4 > var16[var15]) {
               var11 = var16[var15];
            }

            var13 = var6;
            if (var6 < var16[var15]) {
               var13 = var16[var15];
            }

            var5 = var15 + 1;
            var14 = var8;
            if (var8 > var16[var5]) {
               var14 = var16[var5];
            }

            var12 = var10;
            if (var10 < var16[var5]) {
               var12 = var16[var5];
            }

            var15 += 2;
            var4 = var11;
            var6 = var13;
            var8 = var14;
            var10 = var12;
         }
      }

      var1.left = this.getMercatorXFromPixel((int)var11);
      var1.top = this.getMercatorYFromPixel((int)var13);
      var1.right = this.getMercatorXFromPixel((int)var12);
      var1.bottom = this.getMercatorYFromPixel((int)var14);
      return var1;
   }

   public long getMercatorXFromPixel(int var1) {
      return (long)var1 - this.mOffsetX;
   }

   public long getMercatorYFromPixel(int var1) {
      return (long)var1 - this.mOffsetY;
   }

   public float getOrientation() {
      return this.mOrientation;
   }

   public Rect getPixelFromTile(int var1, int var2, Rect var3) {
      if (var3 == null) {
         var3 = new Rect();
      }

      var3.left = TileSystem.truncateToInt(this.getLongPixelXFromMercator(this.getMercatorFromTile(var1), false));
      var3.top = TileSystem.truncateToInt(this.getLongPixelYFromMercator(this.getMercatorFromTile(var2), false));
      var3.right = TileSystem.truncateToInt(this.getLongPixelXFromMercator(this.getMercatorFromTile(var1 + 1), false));
      var3.bottom = TileSystem.truncateToInt(this.getLongPixelYFromMercator(this.getMercatorFromTile(var2 + 1), false));
      return var3;
   }

   public int getScreenCenterX() {
      Rect var1 = this.mIntrinsicScreenRectProjection;
      return (var1.right + var1.left) / 2;
   }

   public int getScreenCenterY() {
      Rect var1 = this.mIntrinsicScreenRectProjection;
      return (var1.bottom + var1.top) / 2;
   }

   public double getZoomLevel() {
      return this.mZoomLevelProjection;
   }

   public void restore(Canvas var1, boolean var2) {
      if (this.mOrientation != 0.0F || var2) {
         var1.restore();
      }

   }

   public Point rotateAndScalePoint(int var1, int var2, Point var3) {
      Matrix var4 = this.mRotateAndScaleMatrix;
      boolean var5;
      if (this.mOrientation != 0.0F) {
         var5 = true;
      } else {
         var5 = false;
      }

      return this.applyMatrixToPoint(var1, var2, var3, var4, var5);
   }

   public void save(Canvas var1, boolean var2, boolean var3) {
      if (this.mOrientation != 0.0F || var3) {
         var1.save();
         Matrix var4;
         if (var2) {
            var4 = this.mRotateAndScaleMatrix;
         } else {
            var4 = this.mUnrotateAndScaleMatrix;
         }

         var1.concat(var4);
      }

   }

   boolean setMapScroll(MapView var1) {
      if (var1.getMapScrollX() == this.mScrollX && var1.getMapScrollY() == this.mScrollY) {
         return false;
      } else {
         var1.setMapScroll(this.mScrollX, this.mScrollY);
         return true;
      }
   }

   public PointL toMercatorPixels(int var1, int var2, PointL var3) {
      if (var3 == null) {
         var3 = new PointL();
      }

      var3.x = this.getCleanMercator(this.getMercatorXFromPixel(var1), this.horizontalWrapEnabled);
      var3.y = this.getCleanMercator(this.getMercatorYFromPixel(var2), this.verticalWrapEnabled);
      return var3;
   }

   public Point toPixels(IGeoPoint var1, Point var2) {
      return this.toPixels(var1, var2, false);
   }

   public Point toPixels(IGeoPoint var1, Point var2, boolean var3) {
      if (var2 == null) {
         var2 = new Point();
      }

      var2.x = TileSystem.truncateToInt(this.getLongPixelXFromLongitude(var1.getLongitude(), var3));
      var2.y = TileSystem.truncateToInt(this.getLongPixelYFromLatitude(var1.getLatitude(), var3));
      return var2;
   }

   public Point unrotateAndScalePoint(int var1, int var2, Point var3) {
      Matrix var4 = this.mUnrotateAndScaleMatrix;
      boolean var5;
      if (this.mOrientation != 0.0F) {
         var5 = true;
      } else {
         var5 = false;
      }

      return this.applyMatrixToPoint(var1, var2, var3, var4, var5);
   }
}
