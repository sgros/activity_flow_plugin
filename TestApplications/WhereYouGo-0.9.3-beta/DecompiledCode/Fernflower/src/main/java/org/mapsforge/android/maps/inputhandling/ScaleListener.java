package org.mapsforge.android.maps.inputhandling;

import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import org.mapsforge.android.maps.MapView;

class ScaleListener implements OnScaleGestureListener {
   private final MapView mapView;
   private float pivotX;
   private float pivotY;
   private float scaleFactorApplied;

   ScaleListener(MapView var1) {
      this.mapView = var1;
   }

   public boolean onScale(ScaleGestureDetector var1) {
      float var2 = var1.getScaleFactor();
      this.scaleFactorApplied *= var2;
      this.mapView.getFrameBuffer().matrixPostScale(var2, var2, this.pivotX, this.pivotY);
      this.mapView.invalidateOnUiThread();
      return true;
   }

   public boolean onScaleBegin(ScaleGestureDetector var1) {
      this.scaleFactorApplied = 1.0F;
      this.pivotX = (float)(this.mapView.getWidth() >> 1);
      this.pivotY = (float)(this.mapView.getHeight() >> 1);
      return true;
   }

   public void onScaleEnd(ScaleGestureDetector var1) {
      byte var2 = (byte)((int)Math.round(Math.log((double)this.scaleFactorApplied) / Math.log(2.0D)));
      this.mapView.getMapViewPosition().zoom(var2, this.scaleFactorApplied);
   }
}
