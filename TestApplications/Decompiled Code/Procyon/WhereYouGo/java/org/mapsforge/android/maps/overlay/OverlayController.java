// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import android.graphics.Paint;
import java.util.Iterator;
import java.util.List;
import org.mapsforge.core.model.BoundingBox;
import android.graphics.Bitmap$Config;
import org.mapsforge.android.maps.Projection;
import android.graphics.Point;
import org.mapsforge.core.model.MapPosition;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import android.graphics.Canvas;
import android.graphics.Matrix;
import org.mapsforge.android.maps.MapView;
import android.graphics.Bitmap;
import org.mapsforge.android.maps.PausableThread;

public class OverlayController extends PausableThread
{
    private static final String THREAD_NAME;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private boolean changeSizeNeeded;
    private int height;
    private final MapView mapView;
    private final Matrix matrix;
    private Canvas overlayCanvas;
    private boolean redrawNeeded;
    private final ReentrantReadWriteLock sizeChange;
    private int width;
    
    static {
        THREAD_NAME = OverlayController.class.getSimpleName();
    }
    
    public OverlayController(final MapView mapView) {
        this.sizeChange = new ReentrantReadWriteLock();
        this.mapView = mapView;
        this.matrix = new Matrix();
        this.changeSizeNeeded = true;
    }
    
    private void adjustMatrix(final MapPosition mapPosition, final MapPosition mapPosition2) {
        final Projection projection = this.mapView.getProjection();
        final Point point = projection.toPoint(mapPosition.geoPoint, null, mapPosition.zoomLevel);
        final Point point2 = projection.toPoint(mapPosition2.geoPoint, null, mapPosition.zoomLevel);
        final float n = (float)Math.pow(2.0, mapPosition2.zoomLevel - mapPosition.zoomLevel);
        final int n2 = this.overlayCanvas.getWidth() / 2;
        final int n3 = this.overlayCanvas.getHeight() / 2;
        this.matrix.reset();
        this.matrix.postTranslate((float)(point.x - point2.x), (float)(point.y - point2.y));
        this.matrix.postScale(n, n, (float)n2, (float)n3);
    }
    
    private boolean changeSize() {
        final boolean b = false;
        final int width = this.mapView.getWidth();
        final int height = this.mapView.getHeight();
        boolean b2 = b;
        if (width > 0) {
            b2 = b;
            if (height > 0) {
                if (this.width == width && this.height == height) {
                    this.changeSizeNeeded = false;
                    this.redrawNeeded = false;
                    b2 = b;
                }
                else {
                    this.recycleBitmaps();
                    this.width = width;
                    this.height = height;
                    this.bitmap1 = Bitmap.createBitmap(width, height, Bitmap$Config.ARGB_8888);
                    this.bitmap2 = Bitmap.createBitmap(width, height, Bitmap$Config.ARGB_8888);
                    this.changeSizeNeeded = false;
                    this.redrawNeeded = true;
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    private void checkRedraw() {
        this.sizeChange.readLock().lock();
        try {
            if (this.redrawNeeded) {
                this.redrawNeeded = false;
                this.redraw();
            }
        }
        finally {
            this.sizeChange.readLock().unlock();
        }
    }
    
    private boolean checkSize() {
        this.sizeChange.writeLock().lock();
        try {
            boolean changeSize;
            if (this.changeSizeNeeded) {
                changeSize = this.changeSize();
            }
            else {
                changeSize = true;
                this.sizeChange.writeLock().unlock();
            }
            return changeSize;
        }
        finally {
            this.sizeChange.writeLock().unlock();
        }
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
        if (this.overlayCanvas == null) {
            this.overlayCanvas = new Canvas();
        }
        this.bitmap2.eraseColor(0);
        this.overlayCanvas.setBitmap(this.bitmap2);
        final MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
        final BoundingBox boundingBox = this.mapView.getMapViewPosition().getBoundingBox();
        final List<Overlay> overlays = this.mapView.getOverlays();
        synchronized (overlays) {
            final Iterator<Overlay> iterator = overlays.iterator();
            while (iterator.hasNext()) {
                iterator.next().draw(boundingBox, mapPosition.zoomLevel, this.overlayCanvas);
            }
        }
        // monitorexit(overlays)
        final MapPosition mapPosition2 = this.mapView.getMapViewPosition().getMapPosition();
        synchronized (this.matrix) {
            final MapPosition mapPosition3;
            this.adjustMatrix(mapPosition3, mapPosition2);
            this.swapBitmaps();
            // monitorexit(this.matrix)
            this.mapView.postInvalidate();
        }
    }
    
    private void swapBitmaps() {
        final Bitmap bitmap1 = this.bitmap1;
        this.bitmap1 = this.bitmap2;
        this.bitmap2 = bitmap1;
    }
    
    private void wakeUpThread() {
        synchronized (this) {
            this.notify();
        }
    }
    
    @Override
    protected void afterRun() {
        this.recycleBitmaps();
    }
    
    @Override
    protected void doWork() {
        if (this.checkSize()) {
            this.checkRedraw();
        }
    }
    
    public void draw(final Canvas canvas) {
        if (this.bitmap1 == null) {
            return;
        }
        synchronized (this.matrix) {
            canvas.drawBitmap(this.bitmap1, this.matrix, (Paint)null);
        }
    }
    
    @Override
    protected String getThreadName() {
        return OverlayController.THREAD_NAME;
    }
    
    @Override
    protected ThreadPriority getThreadPriority() {
        return ThreadPriority.BELOW_NORMAL;
    }
    
    @Override
    protected boolean hasWork() {
        return this.changeSizeNeeded || this.redrawNeeded;
    }
    
    public void onSizeChanged() {
        this.changeSizeNeeded = true;
        this.wakeUpThread();
    }
    
    public void postScale(final float n, final float n2, final float n3, final float n4) {
        synchronized (this.matrix) {
            this.matrix.postScale(n, n2, n3, n4);
        }
    }
    
    public void postTranslate(final float n, final float n2) {
        synchronized (this.matrix) {
            this.matrix.postTranslate(n, n2);
        }
    }
    
    public void redrawOverlays() {
        this.redrawNeeded = true;
        this.wakeUpThread();
    }
}
