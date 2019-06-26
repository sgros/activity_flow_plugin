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

    public ZoomAnimator(MapView mapView) {
        this.mapView = mapView;
    }

    public boolean isExecuting() {
        return this.executeAnimation;
    }

    public void startAnimation(float scaleFactorStart, float scaleFactorEnd, float focusX, float focusY) {
        this.zoomStart = scaleFactorStart;
        this.pivotX = focusX;
        this.pivotY = focusY;
        this.zoomDifference = scaleFactorEnd - scaleFactorStart;
        this.scaleFactorApplied = this.zoomStart;
        this.executeAnimation = true;
        this.timeStart = SystemClock.uptimeMillis();
        synchronized (this) {
            notify();
        }
    }

    /* Access modifiers changed, original: protected */
    public void doWork() throws InterruptedException {
        long timeElapsed = SystemClock.uptimeMillis() - this.timeStart;
        float scaleFactor = (this.zoomStart + (this.zoomDifference * Math.min(1.0f, ((float) timeElapsed) / 250.0f))) / this.scaleFactorApplied;
        this.scaleFactorApplied *= scaleFactor;
        this.mapView.getFrameBuffer().matrixPostScale(scaleFactor, scaleFactor, this.pivotX, this.pivotY);
        if (timeElapsed >= 250) {
            this.executeAnimation = false;
            this.mapView.redraw();
            return;
        }
        this.mapView.postInvalidate();
        sleep(15);
    }

    /* Access modifiers changed, original: protected */
    public String getThreadName() {
        return THREAD_NAME;
    }

    /* Access modifiers changed, original: protected */
    public ThreadPriority getThreadPriority() {
        return ThreadPriority.ABOVE_NORMAL;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasWork() {
        return this.executeAnimation;
    }
}
