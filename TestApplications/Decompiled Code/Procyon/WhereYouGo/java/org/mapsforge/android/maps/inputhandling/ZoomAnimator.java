// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.inputhandling;

import android.os.SystemClock;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.PausableThread;

public class ZoomAnimator extends PausableThread
{
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
    
    public ZoomAnimator(final MapView mapView) {
        this.mapView = mapView;
    }
    
    @Override
    protected void doWork() throws InterruptedException {
        final long n = SystemClock.uptimeMillis() - this.timeStart;
        final float n2 = (this.zoomStart + this.zoomDifference * Math.min(1.0f, n / 250.0f)) / this.scaleFactorApplied;
        this.scaleFactorApplied *= n2;
        this.mapView.getFrameBuffer().matrixPostScale(n2, n2, this.pivotX, this.pivotY);
        if (n >= 250L) {
            this.executeAnimation = false;
            this.mapView.redraw();
        }
        else {
            this.mapView.postInvalidate();
            Thread.sleep(15L);
        }
    }
    
    @Override
    protected String getThreadName() {
        return "ZoomAnimator";
    }
    
    @Override
    protected ThreadPriority getThreadPriority() {
        return ThreadPriority.ABOVE_NORMAL;
    }
    
    @Override
    protected boolean hasWork() {
        return this.executeAnimation;
    }
    
    public boolean isExecuting() {
        return this.executeAnimation;
    }
    
    public void startAnimation(final float zoomStart, final float n, final float pivotX, final float pivotY) {
        this.zoomStart = zoomStart;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.zoomDifference = n - zoomStart;
        this.scaleFactorApplied = this.zoomStart;
        this.executeAnimation = true;
        this.timeStart = SystemClock.uptimeMillis();
        synchronized (this) {
            this.notify();
        }
    }
}
