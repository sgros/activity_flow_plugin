package org.mapsforge.android.maps.inputhandling;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.KeyEvent.Callback;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.PausableThread;

public class MapMover extends PausableThread implements Callback {
   private static final int DEFAULT_MOVE_SPEED_FACTOR = 8;
   private static final int FRAME_LENGTH_IN_MS = 15;
   private static final float MOVE_SPEED = 0.2F;
   private static final String THREAD_NAME = "MapMover";
   private static final float TRACKBALL_MOVE_SPEED_FACTOR = 40.0F;
   private final MapView mapView;
   private float moveSpeedFactor;
   private float moveX;
   private float moveY;
   private long timePrevious;

   public MapMover(MapView var1) {
      this.mapView = var1;
      this.moveSpeedFactor = 8.0F;
   }

   private void moveDown() {
      // $FF: Couldn't be decompiled
   }

   private void moveLeft() {
      // $FF: Couldn't be decompiled
   }

   private void moveRight() {
      // $FF: Couldn't be decompiled
   }

   private void moveUp() {
      // $FF: Couldn't be decompiled
   }

   protected void afterPause() {
      this.timePrevious = SystemClock.uptimeMillis();
   }

   protected void doWork() throws InterruptedException {
      long var1 = SystemClock.uptimeMillis();
      long var3 = var1 - this.timePrevious;
      this.timePrevious = var1;
      this.mapView.getMapViewPosition().moveCenter((float)var3 * this.moveX, (float)var3 * this.moveY);
      sleep(15L);
   }

   public float getMoveSpeedFactor() {
      return this.moveSpeedFactor;
   }

   protected String getThreadName() {
      return "MapMover";
   }

   protected PausableThread.ThreadPriority getThreadPriority() {
      return PausableThread.ThreadPriority.NORMAL;
   }

   protected boolean hasWork() {
      boolean var1;
      if (this.moveX == 0.0F && this.moveY == 0.0F) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = false;
      if (this.mapView.isClickable()) {
         if (var1 == 21) {
            this.moveLeft();
            var3 = true;
         } else if (var1 == 22) {
            this.moveRight();
            var3 = true;
         } else if (var1 == 19) {
            this.moveUp();
            var3 = true;
         } else if (var1 == 20) {
            this.moveDown();
            var3 = true;
         }
      }

      return var3;
   }

   public boolean onKeyLongPress(int var1, KeyEvent var2) {
      return false;
   }

   public boolean onKeyMultiple(int var1, int var2, KeyEvent var3) {
      return false;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var3 = false;
      if (this.mapView.isClickable()) {
         if (var1 != 21 && var1 != 22) {
            if (var1 == 19 || var1 == 20) {
               this.moveY = 0.0F;
               var3 = true;
            }
         } else {
            this.moveX = 0.0F;
            var3 = true;
         }
      }

      return var3;
   }

   public boolean onTrackballEvent(MotionEvent var1) {
      boolean var2 = false;
      if (this.mapView.isClickable() && var1.getAction() == 2) {
         float var3 = var1.getX();
         float var4 = this.getMoveSpeedFactor();
         float var5 = var1.getY();
         float var6 = this.getMoveSpeedFactor();
         this.mapView.getMapViewPosition().moveCenter(var3 * 40.0F * var4, var5 * 40.0F * var6);
         var2 = true;
      }

      return var2;
   }

   public void setMoveSpeedFactor(float var1) {
      if (var1 < 0.0F) {
         throw new IllegalArgumentException();
      } else {
         this.moveSpeedFactor = var1;
      }
   }

   public void stopMove() {
      this.moveX = 0.0F;
      this.moveY = 0.0F;
   }
}
