package org.mapsforge.android.maps.overlay;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.PausableThread;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.model.BoundingBox;
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

    public OverlayController(MapView mapView) {
        this.mapView = mapView;
        this.matrix = new Matrix();
        this.changeSizeNeeded = true;
    }

    public void draw(Canvas canvas) {
        if (this.bitmap1 != null) {
            synchronized (this.matrix) {
                canvas.drawBitmap(this.bitmap1, this.matrix, null);
            }
        }
    }

    public void onSizeChanged() {
        this.changeSizeNeeded = true;
        wakeUpThread();
    }

    public void postScale(float scaleX, float scaleY, float pivotX, float pivotY) {
        synchronized (this.matrix) {
            this.matrix.postScale(scaleX, scaleY, pivotX, pivotY);
        }
    }

    public void postTranslate(float translateX, float translateY) {
        synchronized (this.matrix) {
            this.matrix.postTranslate(translateX, translateY);
        }
    }

    public void redrawOverlays() {
        this.redrawNeeded = true;
        wakeUpThread();
    }

    private void adjustMatrix(MapPosition mapPositionBefore, MapPosition mapPositionAfter) {
        Projection projection = this.mapView.getProjection();
        Point pointBefore = projection.toPoint(mapPositionBefore.geoPoint, null, mapPositionBefore.zoomLevel);
        Point pointAfter = projection.toPoint(mapPositionAfter.geoPoint, null, mapPositionBefore.zoomLevel);
        float scaleFactor = (float) Math.pow(2.0d, (double) (mapPositionAfter.zoomLevel - mapPositionBefore.zoomLevel));
        int pivotX = this.overlayCanvas.getWidth() / 2;
        int pivotY = this.overlayCanvas.getHeight() / 2;
        this.matrix.reset();
        this.matrix.postTranslate((float) (pointBefore.x - pointAfter.x), (float) (pointBefore.y - pointAfter.y));
        this.matrix.postScale(scaleFactor, scaleFactor, (float) pivotX, (float) pivotY);
    }

    private boolean changeSize() {
        int newWidth = this.mapView.getWidth();
        int newHeight = this.mapView.getHeight();
        if (newWidth <= 0 || newHeight <= 0) {
            return false;
        }
        if (this.width == newWidth && this.height == newHeight) {
            this.changeSizeNeeded = false;
            this.redrawNeeded = false;
            return false;
        }
        recycleBitmaps();
        this.width = newWidth;
        this.height = newHeight;
        this.bitmap1 = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
        this.bitmap2 = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
        this.changeSizeNeeded = false;
        this.redrawNeeded = true;
        return true;
    }

    private void checkRedraw() {
        this.sizeChange.readLock().lock();
        try {
            if (this.redrawNeeded) {
                this.redrawNeeded = false;
                redraw();
            }
            this.sizeChange.readLock().unlock();
        } catch (Throwable th) {
            this.sizeChange.readLock().unlock();
        }
    }

    private boolean checkSize() {
        this.sizeChange.writeLock().lock();
        try {
            if (this.changeSizeNeeded) {
                boolean changeSize = changeSize();
                return changeSize;
            }
            this.sizeChange.writeLock().unlock();
            return true;
        } finally {
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
        MapPosition mapPositionBefore = this.mapView.getMapViewPosition().getMapPosition();
        BoundingBox boundingBox = this.mapView.getMapViewPosition().getBoundingBox();
        List<Overlay> overlays = this.mapView.getOverlays();
        synchronized (overlays) {
            for (Overlay overlay : overlays) {
                overlay.draw(boundingBox, mapPositionBefore.zoomLevel, this.overlayCanvas);
            }
        }
        MapPosition mapPositionAfter = this.mapView.getMapViewPosition().getMapPosition();
        synchronized (this.matrix) {
            adjustMatrix(mapPositionBefore, mapPositionAfter);
            swapBitmaps();
        }
        this.mapView.postInvalidate();
    }

    private void swapBitmaps() {
        Bitmap bitmapTemp = this.bitmap1;
        this.bitmap1 = this.bitmap2;
        this.bitmap2 = bitmapTemp;
    }

    private void wakeUpThread() {
        synchronized (this) {
            notify();
        }
    }

    /* Access modifiers changed, original: protected */
    public void afterRun() {
        recycleBitmaps();
    }

    /* Access modifiers changed, original: protected */
    public void doWork() {
        if (checkSize()) {
            checkRedraw();
        }
    }

    /* Access modifiers changed, original: protected */
    public String getThreadName() {
        return THREAD_NAME;
    }

    /* Access modifiers changed, original: protected */
    public ThreadPriority getThreadPriority() {
        return ThreadPriority.BELOW_NORMAL;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasWork() {
        return this.changeSizeNeeded || this.redrawNeeded;
    }
}
