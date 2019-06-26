package org.mapsforge.android.maps;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import java.io.OutputStream;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;

public class FrameBuffer {
    static final int MAP_VIEW_BACKGROUND = Color.rgb(238, 238, 238);
    private int height;
    private final MapView mapView;
    private Bitmap mapViewBitmap1;
    private Bitmap mapViewBitmap2;
    private Canvas mapViewCanvas = new Canvas();
    private final Matrix matrix = new Matrix();
    private int width;

    FrameBuffer(MapView mapView) {
        this.mapView = mapView;
    }

    public boolean drawBitmap(Tile tile, Bitmap bitmap) {
        boolean z;
        MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
        synchronized (this) {
            if (bitmap == null) {
                z = false;
            } else if (tile.zoomLevel != mapPosition.zoomLevel) {
                z = false;
            } else if (this.mapView.isZoomAnimatorRunning()) {
                z = false;
            } else {
                GeoPoint geoPoint = mapPosition.geoPoint;
                double pixelLeft = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
                pixelLeft -= (double) (this.width >> 1);
                double pixelTop = MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel) - ((double) (this.height >> 1));
                if (pixelLeft - ((double) tile.getPixelX()) > 256.0d || ((double) this.width) + pixelLeft < ((double) tile.getPixelX())) {
                    z = false;
                } else if (pixelTop - ((double) tile.getPixelY()) > 256.0d || ((double) this.height) + pixelTop < ((double) tile.getPixelY())) {
                    z = false;
                } else {
                    if (!this.matrix.isIdentity()) {
                        this.mapViewBitmap2.eraseColor(MAP_VIEW_BACKGROUND);
                        this.mapViewCanvas.setBitmap(this.mapViewBitmap2);
                        this.mapViewCanvas.drawBitmap(this.mapViewBitmap1, this.matrix, null);
                        this.matrix.reset();
                        Bitmap mapViewBitmapSwap = this.mapViewBitmap1;
                        this.mapViewBitmap1 = this.mapViewBitmap2;
                        this.mapViewBitmap2 = mapViewBitmapSwap;
                    }
                    this.mapViewCanvas.drawBitmap(bitmap, (float) (((double) tile.getPixelX()) - pixelLeft), (float) (((double) tile.getPixelY()) - pixelTop), null);
                    z = true;
                }
            }
        }
        return z;
    }

    public synchronized void matrixPostScale(float scaleX, float scaleY, float pivotX, float pivotY) {
        this.matrix.postScale(scaleX, scaleY, pivotX, pivotY);
        this.mapView.getOverlayController().postScale(scaleX, scaleY, pivotX, pivotY);
    }

    public synchronized void matrixPostTranslate(float translateX, float translateY) {
        this.matrix.postTranslate(translateX, translateY);
        this.mapView.getOverlayController().postTranslate(translateX, translateY);
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void clear() {
        if (this.mapViewBitmap1 != null) {
            this.mapViewBitmap1.eraseColor(MAP_VIEW_BACKGROUND);
        }
        if (this.mapViewBitmap2 != null) {
            this.mapViewBitmap2.eraseColor(MAP_VIEW_BACKGROUND);
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized boolean compress(CompressFormat compressFormat, int quality, OutputStream outputStream) {
        boolean z;
        if (this.mapViewBitmap1 == null) {
            z = false;
        } else {
            z = this.mapViewBitmap1.compress(compressFormat, quality, outputStream);
        }
        return z;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void destroy() {
        if (this.mapViewBitmap1 != null) {
            this.mapViewBitmap1.recycle();
        }
        if (this.mapViewBitmap2 != null) {
            this.mapViewBitmap2.recycle();
        }
        this.mapViewCanvas = null;
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void draw(Canvas canvas) {
        if (this.mapViewBitmap1 != null) {
            canvas.drawBitmap(this.mapViewBitmap1, this.matrix, null);
        }
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void onSizeChanged() {
        destroy();
        this.mapViewCanvas = new Canvas();
        this.width = this.mapView.getWidth();
        this.height = this.mapView.getHeight();
        this.mapViewBitmap1 = Bitmap.createBitmap(this.width, this.height, Config.RGB_565);
        this.mapViewBitmap2 = Bitmap.createBitmap(this.width, this.height, Config.RGB_565);
        clear();
        this.mapViewCanvas.setBitmap(this.mapViewBitmap1);
    }
}
