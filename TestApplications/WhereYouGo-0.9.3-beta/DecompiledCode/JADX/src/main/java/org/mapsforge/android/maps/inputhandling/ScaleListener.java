package org.mapsforge.android.maps.inputhandling;

import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import org.mapsforge.android.maps.MapView;

class ScaleListener implements OnScaleGestureListener {
    private final MapView mapView;
    private float pivotX;
    private float pivotY;
    private float scaleFactorApplied;

    ScaleListener(MapView mapView) {
        this.mapView = mapView;
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float scaleFactor = scaleGestureDetector.getScaleFactor();
        this.scaleFactorApplied *= scaleFactor;
        this.mapView.getFrameBuffer().matrixPostScale(scaleFactor, scaleFactor, this.pivotX, this.pivotY);
        this.mapView.invalidateOnUiThread();
        return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        this.scaleFactorApplied = 1.0f;
        this.pivotX = (float) (this.mapView.getWidth() >> 1);
        this.pivotY = (float) (this.mapView.getHeight() >> 1);
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        this.mapView.getMapViewPosition().zoom((byte) ((int) Math.round(Math.log((double) this.scaleFactorApplied) / Math.log(2.0d))), this.scaleFactorApplied);
    }
}
