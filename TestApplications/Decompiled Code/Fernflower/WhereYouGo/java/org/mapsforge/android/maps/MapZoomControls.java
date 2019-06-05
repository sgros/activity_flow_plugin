package org.mapsforge.android.maps;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.ZoomControls;
import android.widget.LinearLayout.LayoutParams;

public class MapZoomControls {
   private static final int DEFAULT_ZOOM_CONTROLS_GRAVITY = 85;
   private static final byte DEFAULT_ZOOM_LEVEL_MAX = 22;
   private static final byte DEFAULT_ZOOM_LEVEL_MIN = 0;
   private static final int MSG_ZOOM_CONTROLS_HIDE = 0;
   private static final int ZOOM_CONTROLS_HORIZONTAL_PADDING = 5;
   private static final long ZOOM_CONTROLS_TIMEOUT = ViewConfiguration.getZoomControlsTimeout();
   private static final int ZOOM_CONTROLS_VERTICAL_PADDING = 15;
   private boolean gravityChanged;
   private boolean showMapZoomControls;
   private final ZoomControls zoomControls;
   private int zoomControlsGravity;
   private final Handler zoomControlsHideHandler;
   private byte zoomLevelMax;
   private byte zoomLevelMin;

   MapZoomControls(Context var1, MapView var2) {
      this.zoomControls = new ZoomControls(var1);
      this.showMapZoomControls = true;
      this.zoomLevelMax = (byte)22;
      this.zoomLevelMin = (byte)0;
      this.zoomControls.setVisibility(8);
      this.zoomControlsGravity = 85;
      MapViewPosition var3 = var2.getMapViewPosition();
      this.zoomControls.setOnZoomInClickListener(new MapZoomControls.ZoomInClickListener(var3));
      this.zoomControls.setOnZoomOutClickListener(new MapZoomControls.ZoomOutClickListener(var3));
      this.zoomControlsHideHandler = new MapZoomControls.ZoomControlsHideHandler(this.zoomControls);
      LayoutParams var4 = new LayoutParams(-2, -2);
      var2.addView(this.zoomControls, var4);
   }

   private int calculatePositionLeft(int var1, int var2, int var3) {
      int var4 = this.zoomControlsGravity & 7;
      switch(var4) {
      case 1:
         var1 = (var2 - var1 - var3) / 2;
         break;
      case 2:
      case 4:
      default:
         throw new IllegalArgumentException("unknown horizontal gravity: " + var4);
      case 3:
         var1 = 5;
         break;
      case 5:
         var1 = var2 - var1 - var3 - 5;
      }

      return var1;
   }

   private int calculatePositionTop(int var1, int var2, int var3) {
      int var4 = this.zoomControlsGravity & 112;
      switch(var4) {
      case 16:
         var1 = (var2 - var1 - var3) / 2;
         break;
      case 48:
         var1 = 15;
         break;
      case 80:
         var1 = var2 - var1 - var3 - 15;
         break;
      default:
         throw new IllegalArgumentException("unknown vertical gravity: " + var4);
      }

      return var1;
   }

   private void showZoomControls() {
      this.zoomControlsHideHandler.removeMessages(0);
      if (this.zoomControls.getVisibility() != 0) {
         this.zoomControls.show();
      }

   }

   private void showZoomControlsWithTimeout() {
      this.showZoomControls();
      this.zoomControlsHideHandler.sendEmptyMessageDelayed(0, ZOOM_CONTROLS_TIMEOUT);
   }

   int getMeasuredHeight() {
      return this.zoomControls.getMeasuredHeight();
   }

   int getMeasuredWidth() {
      return this.zoomControls.getMeasuredWidth();
   }

   public int getZoomControlsGravity() {
      return this.zoomControlsGravity;
   }

   public byte getZoomLevelMax() {
      return this.zoomLevelMax;
   }

   public byte getZoomLevelMin() {
      return this.zoomLevelMin;
   }

   public boolean isShowMapZoomControls() {
      return this.showMapZoomControls;
   }

   void measure(int var1, int var2) {
      this.zoomControls.measure(var1, var2);
   }

   void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      if (var1 || this.gravityChanged) {
         int var6 = this.zoomControls.getMeasuredWidth();
         int var7 = this.zoomControls.getMeasuredHeight();
         var2 = this.calculatePositionLeft(var2, var4, var6);
         var3 = this.calculatePositionTop(var3, var5, var7);
         this.zoomControls.layout(var2, var3, var2 + var6, var3 + var7);
         this.gravityChanged = false;
      }

   }

   void onMapViewTouchEvent(int var1) {
      if (this.showMapZoomControls) {
         switch(var1) {
         case 0:
            this.showZoomControls();
            break;
         case 1:
            this.showZoomControlsWithTimeout();
         case 2:
         default:
            break;
         case 3:
            this.showZoomControlsWithTimeout();
         }
      }

   }

   void onZoomLevelChange(int var1) {
      boolean var2;
      if (var1 < this.zoomLevelMax) {
         var2 = true;
      } else {
         var2 = false;
      }

      boolean var3;
      if (var1 > this.zoomLevelMin) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.zoomControls.setIsZoomInEnabled(var2);
      this.zoomControls.setIsZoomOutEnabled(var3);
   }

   public void setShowMapZoomControls(boolean var1) {
      this.showMapZoomControls = var1;
   }

   public void setZoomControlsGravity(int var1) {
      if (this.zoomControlsGravity != var1) {
         this.zoomControlsGravity = var1;
         this.gravityChanged = true;
      }

   }

   public void setZoomLevelMax(byte var1) {
      if (var1 < this.zoomLevelMin) {
         throw new IllegalArgumentException();
      } else {
         this.zoomLevelMax = (byte)var1;
      }
   }

   public void setZoomLevelMin(byte var1) {
      if (var1 > this.zoomLevelMax) {
         throw new IllegalArgumentException();
      } else {
         this.zoomLevelMin = (byte)var1;
      }
   }

   private static class ZoomControlsHideHandler extends Handler {
      private final ZoomControls zoomControls;

      ZoomControlsHideHandler(ZoomControls var1) {
         this.zoomControls = var1;
      }

      public void handleMessage(Message var1) {
         this.zoomControls.hide();
      }
   }

   private static class ZoomInClickListener implements OnClickListener {
      private final MapViewPosition mapViewPosition;

      ZoomInClickListener(MapViewPosition var1) {
         this.mapViewPosition = var1;
      }

      public void onClick(View var1) {
         this.mapViewPosition.zoomIn();
      }
   }

   private static class ZoomOutClickListener implements OnClickListener {
      private final MapViewPosition mapViewPosition;

      ZoomOutClickListener(MapViewPosition var1) {
         this.mapViewPosition = var1;
      }

      public void onClick(View var1) {
         this.mapViewPosition.zoomOut();
      }
   }
}
