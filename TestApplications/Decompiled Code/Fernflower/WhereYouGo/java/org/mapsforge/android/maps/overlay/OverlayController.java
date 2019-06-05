package org.mapsforge.android.maps.overlay;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.PausableThread;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.model.MapPosition;

public class OverlayController extends PausableThread {
   private static final String THREAD_NAME = OverlayController.class.getSimpleName();
   private Bitmap bitmap1;
   private Bitmap bitmap2;
   private boolean changeSizeNeeded;
   private int height;
   private final MapView mapView;
   private final Matrix matrix;
   private Canvas overlayCanvas;
   private boolean redrawNeeded;
   private final ReentrantReadWriteLock sizeChange = new ReentrantReadWriteLock();
   private int width;

   public OverlayController(MapView var1) {
      this.mapView = var1;
      this.matrix = new Matrix();
      this.changeSizeNeeded = true;
   }

   private void adjustMatrix(MapPosition var1, MapPosition var2) {
      Projection var3 = this.mapView.getProjection();
      Point var4 = var3.toPoint(var1.geoPoint, (Point)null, var1.zoomLevel);
      Point var8 = var3.toPoint(var2.geoPoint, (Point)null, var1.zoomLevel);
      float var5 = (float)Math.pow(2.0D, (double)(var2.zoomLevel - var1.zoomLevel));
      int var6 = this.overlayCanvas.getWidth() / 2;
      int var7 = this.overlayCanvas.getHeight() / 2;
      this.matrix.reset();
      this.matrix.postTranslate((float)(var4.x - var8.x), (float)(var4.y - var8.y));
      this.matrix.postScale(var5, var5, (float)var6, (float)var7);
   }

   private boolean changeSize() {
      boolean var1 = false;
      int var2 = this.mapView.getWidth();
      int var3 = this.mapView.getHeight();
      boolean var4 = var1;
      if (var2 > 0) {
         var4 = var1;
         if (var3 > 0) {
            if (this.width == var2 && this.height == var3) {
               this.changeSizeNeeded = false;
               this.redrawNeeded = false;
               var4 = var1;
            } else {
               this.recycleBitmaps();
               this.width = var2;
               this.height = var3;
               this.bitmap1 = Bitmap.createBitmap(var2, var3, Config.ARGB_8888);
               this.bitmap2 = Bitmap.createBitmap(var2, var3, Config.ARGB_8888);
               this.changeSizeNeeded = false;
               this.redrawNeeded = true;
               var4 = true;
            }
         }
      }

      return var4;
   }

   private void checkRedraw() {
      this.sizeChange.readLock().lock();

      try {
         if (this.redrawNeeded) {
            this.redrawNeeded = false;
            this.redraw();
         }
      } finally {
         this.sizeChange.readLock().unlock();
      }

   }

   private boolean checkSize() {
      this.sizeChange.writeLock().lock();
      boolean var4 = false;

      boolean var1;
      label50: {
         try {
            var4 = true;
            if (this.changeSizeNeeded) {
               var1 = this.changeSize();
               var4 = false;
               break label50;
            }

            var4 = false;
         } finally {
            if (var4) {
               this.sizeChange.writeLock().unlock();
            }
         }

         var1 = true;
         this.sizeChange.writeLock().unlock();
         return var1;
      }

      this.sizeChange.writeLock().unlock();
      return var1;
   }

   private void recycleBitmaps() {
      if (this.bitmap1 != null) {
         this.bitmap1.recycle();
         this.bitmap1 = null;
      }

      if (this.bitmap2 != null) {
         this.bitmap2.recycle();
         this.bitmap2 = null;
      }

      this.overlayCanvas = null;
   }

   private void redraw() {
      // $FF: Couldn't be decompiled
   }

   private void swapBitmaps() {
      Bitmap var1 = this.bitmap1;
      this.bitmap1 = this.bitmap2;
      this.bitmap2 = var1;
   }

   private void wakeUpThread() {
      // $FF: Couldn't be decompiled
   }

   protected void afterRun() {
      this.recycleBitmaps();
   }

   protected void doWork() {
      if (this.checkSize()) {
         this.checkRedraw();
      }

   }

   public void draw(Canvas param1) {
      // $FF: Couldn't be decompiled
   }

   protected String getThreadName() {
      return THREAD_NAME;
   }

   protected PausableThread.ThreadPriority getThreadPriority() {
      return PausableThread.ThreadPriority.BELOW_NORMAL;
   }

   protected boolean hasWork() {
      boolean var1;
      if (!this.changeSizeNeeded && !this.redrawNeeded) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void onSizeChanged() {
      this.changeSizeNeeded = true;
      this.wakeUpThread();
   }

   public void postScale(float param1, float param2, float param3, float param4) {
      // $FF: Couldn't be decompiled
   }

   public void postTranslate(float param1, float param2) {
      // $FF: Couldn't be decompiled
   }

   public void redrawOverlays() {
      this.redrawNeeded = true;
      this.wakeUpThread();
   }
}
