// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.inputhandling;

import android.view.ScaleGestureDetector;
import org.mapsforge.android.maps.MapView;
import android.view.ScaleGestureDetector$OnScaleGestureListener;

class ScaleListener implements ScaleGestureDetector$OnScaleGestureListener
{
    private final MapView mapView;
    private float pivotX;
    private float pivotY;
    private float scaleFactorApplied;
    
    ScaleListener(final MapView mapView) {
        this.mapView = mapView;
    }
    
    public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
        final float scaleFactor = scaleGestureDetector.getScaleFactor();
        this.scaleFactorApplied *= scaleFactor;
        this.mapView.getFrameBuffer().matrixPostScale(scaleFactor, scaleFactor, this.pivotX, this.pivotY);
        this.mapView.invalidateOnUiThread();
        return true;
    }
    
    public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
        this.scaleFactorApplied = 1.0f;
        this.pivotX = (float)(this.mapView.getWidth() >> 1);
        this.pivotY = (float)(this.mapView.getHeight() >> 1);
        return true;
    }
    
    public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
        this.mapView.getMapViewPosition().zoom((byte)Math.round(Math.log(this.scaleFactorApplied) / Math.log(2.0)), this.scaleFactorApplied);
    }
}
