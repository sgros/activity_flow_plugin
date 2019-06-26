// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import android.graphics.Bitmap$Config;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.model.Tile;
import android.graphics.Paint;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.Bitmap;

public class FrameBuffer
{
    static final int MAP_VIEW_BACKGROUND;
    private int height;
    private final MapView mapView;
    private Bitmap mapViewBitmap1;
    private Bitmap mapViewBitmap2;
    private Canvas mapViewCanvas;
    private final Matrix matrix;
    private int width;
    
    static {
        MAP_VIEW_BACKGROUND = Color.rgb(238, 238, 238);
    }
    
    FrameBuffer(final MapView mapView) {
        this.mapView = mapView;
        this.mapViewCanvas = new Canvas();
        this.matrix = new Matrix();
    }
    
    void clear() {
        synchronized (this) {
            if (this.mapViewBitmap1 != null) {
                this.mapViewBitmap1.eraseColor(FrameBuffer.MAP_VIEW_BACKGROUND);
            }
            if (this.mapViewBitmap2 != null) {
                this.mapViewBitmap2.eraseColor(FrameBuffer.MAP_VIEW_BACKGROUND);
            }
        }
    }
    
    boolean compress(final Bitmap$CompressFormat bitmap$CompressFormat, final int n, final OutputStream outputStream) {
        synchronized (this) {
            return this.mapViewBitmap1 != null && this.mapViewBitmap1.compress(bitmap$CompressFormat, n, outputStream);
        }
    }
    
    void destroy() {
        synchronized (this) {
            if (this.mapViewBitmap1 != null) {
                this.mapViewBitmap1.recycle();
            }
            if (this.mapViewBitmap2 != null) {
                this.mapViewBitmap2.recycle();
            }
            this.mapViewCanvas = null;
        }
    }
    
    void draw(final Canvas canvas) {
        synchronized (this) {
            if (this.mapViewBitmap1 != null) {
                canvas.drawBitmap(this.mapViewBitmap1, this.matrix, (Paint)null);
            }
        }
    }
    
    public boolean drawBitmap(final Tile tile, final Bitmap bitmap) {
        final MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
        // monitorenter(this)
        Label_0025: {
            if (bitmap != null) {
                break Label_0025;
            }
            boolean b = false;
            while (true) {
                try {
                    return b;
                    // iftrue(Label_0049:, tile.zoomLevel == mapPosition.zoomLevel)
                    b = false;
                    // monitorexit(this)
                    continue;
                }
                finally {
                }
                // monitorexit(this)
                Label_0049: {
                    if (this.mapView.isZoomAnimatorRunning()) {
                        b = false;
                    }
                    // monitorexit(this)
                    else {
                        final GeoPoint geoPoint = mapPosition.geoPoint;
                        final double longitudeToPixelX = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
                        final double latitudeToPixelY = MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel);
                        final double n = longitudeToPixelX - (this.width >> 1);
                        final double n2 = latitudeToPixelY - (this.height >> 1);
                        final Tile tile2;
                        if (n - tile2.getPixelX() > 256.0 || this.width + n < tile2.getPixelX()) {
                            b = false;
                        }
                        // monitorexit(this)
                        else if (n2 - tile2.getPixelY() > 256.0 || this.height + n2 < tile2.getPixelY()) {
                            b = false;
                        }
                        // monitorexit(this)
                        else {
                            if (!this.matrix.isIdentity()) {
                                this.mapViewBitmap2.eraseColor(FrameBuffer.MAP_VIEW_BACKGROUND);
                                this.mapViewCanvas.setBitmap(this.mapViewBitmap2);
                                this.mapViewCanvas.drawBitmap(this.mapViewBitmap1, this.matrix, (Paint)null);
                                this.matrix.reset();
                                final Bitmap mapViewBitmap1 = this.mapViewBitmap1;
                                this.mapViewBitmap1 = this.mapViewBitmap2;
                                this.mapViewBitmap2 = mapViewBitmap1;
                            }
                            this.mapViewCanvas.drawBitmap(bitmap, (float)(tile2.getPixelX() - n), (float)(tile2.getPixelY() - n2), (Paint)null);
                            b = true;
                        }
                        // monitorexit(this)
                    }
                }
            }
        }
    }
    
    public void matrixPostScale(final float n, final float n2, final float n3, final float n4) {
        synchronized (this) {
            this.matrix.postScale(n, n2, n3, n4);
            this.mapView.getOverlayController().postScale(n, n2, n3, n4);
        }
    }
    
    public void matrixPostTranslate(final float n, final float n2) {
        synchronized (this) {
            this.matrix.postTranslate(n, n2);
            this.mapView.getOverlayController().postTranslate(n, n2);
        }
    }
    
    void onSizeChanged() {
        synchronized (this) {
            this.destroy();
            this.mapViewCanvas = new Canvas();
            this.width = this.mapView.getWidth();
            this.height = this.mapView.getHeight();
            this.mapViewBitmap1 = Bitmap.createBitmap(this.width, this.height, Bitmap$Config.RGB_565);
            this.mapViewBitmap2 = Bitmap.createBitmap(this.width, this.height, Bitmap$Config.RGB_565);
            this.clear();
            this.mapViewCanvas.setBitmap(this.mapViewBitmap1);
        }
    }
}
