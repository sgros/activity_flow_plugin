package org.mapsforge.android.maps.inputhandling;

import android.os.SystemClock;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.PausableThread;

public class ZoomAnimator extends PausableThread {
   private static final int DEFAULT_DURATION = 250;
   private static final int FRAME_LENGTH_IN_MS = 15;
   private static final String THREAD_NAME = "ZoomAnimator";
   private boolean executeAnimation;
   private final MapView mapView;
   private float pivotX;
   private float pivotY;
   private float scaleFactorApplied;
   private long timeStart;
   private float zoomDifference;
   private float zoomStart;

   public ZoomAnimator(MapView var1) {
      this.mapView = var1;
   }

   protected void doWork() throws InterruptedException {
      long var1 = SystemClock.uptimeMillis() - this.timeStart;
      float var3 = Math.min(1.0F, (float)var1 / 250.0F);
      var3 = (this.zoomStart + this.zoomDifference * var3) / this.scaleFactorApplied;
      this.scaleFactorApplied *= var3;
      this.mapView.getFrameBuffer().matrixPostScale(var3, var3, this.pivotX, this.pivotY);
      if (var1 >= 250L) {
         this.executeAnimation = false;
         this.mapView.redraw();
      } else {
         this.mapView.postInvalidate();
         sleep(15L);
      }

   }

   protected String getThreadName() {
      return "ZoomAnimator";
   }

   protected PausableThread.ThreadPriority getThreadPriority() {
      return PausableThread.ThreadPriority.ABOVE_NORMAL;
   }

   protected boolean hasWork() {
      return this.executeAnimation;
   }

   public boolean isExecuting() {
      return this.executeAnimation;
   }

   public void startAnimation(float param1, float param2, float param3, float param4) {
      // $FF: Couldn't be decompiled
   }
}
