package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

public class Polyline implements OverlayItem {
    private Paint paintStroke;
    private PolygonalChain polygonalChain;

    public Polyline(PolygonalChain polygonalChain, Paint paintStroke) {
        this.polygonalChain = polygonalChain;
        this.paintStroke = paintStroke;
    }

    public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
        boolean z = false;
        synchronized (this) {
            if (!(this.polygonalChain == null || this.paintStroke == null)) {
                Path path = this.polygonalChain.draw(zoomLevel, canvasPosition, false);
                if (path != null) {
                    canvas.drawPath(path, this.paintStroke);
                    z = true;
                }
            }
        }
        return z;
    }

    public synchronized Paint getPaintStroke() {
        return this.paintStroke;
    }

    public synchronized PolygonalChain getPolygonalChain() {
        return this.polygonalChain;
    }

    public synchronized void setPaintStroke(Paint paintStroke) {
        this.paintStroke = paintStroke;
    }

    public synchronized void setPolygonalChain(PolygonalChain polygonalChain) {
        this.polygonalChain = polygonalChain;
    }
}
