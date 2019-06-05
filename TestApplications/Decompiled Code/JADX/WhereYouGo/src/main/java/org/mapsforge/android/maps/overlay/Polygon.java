package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

public class Polygon implements OverlayItem {
    private Paint paintFill;
    private Paint paintStroke;
    private final List<PolygonalChain> polygonalChains;

    public Polygon(Collection<PolygonalChain> polygonalChains, Paint paintFill, Paint paintStroke) {
        if (polygonalChains == null) {
            this.polygonalChains = Collections.synchronizedList(new ArrayList());
        } else {
            this.polygonalChains = Collections.synchronizedList(new ArrayList(polygonalChains));
        }
        this.paintFill = paintFill;
        this.paintStroke = paintStroke;
    }

    public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
        boolean z = true;
        synchronized (this) {
            synchronized (this.polygonalChains) {
                if (this.polygonalChains.isEmpty() || (this.paintStroke == null && this.paintFill == null)) {
                    z = false;
                } else {
                    Path path = new Path();
                    path.setFillType(FillType.EVEN_ODD);
                    for (int i = 0; i < this.polygonalChains.size(); i++) {
                        Path closedPath = ((PolygonalChain) this.polygonalChains.get(i)).draw(zoomLevel, canvasPosition, true);
                        if (closedPath != null) {
                            path.addPath(closedPath);
                        }
                    }
                    if (this.paintStroke != null) {
                        canvas.drawPath(path, this.paintStroke);
                    }
                    if (this.paintFill != null) {
                        canvas.drawPath(path, this.paintFill);
                    }
                }
            }
        }
        return z;
    }

    public synchronized Paint getPaintFill() {
        return this.paintFill;
    }

    public synchronized Paint getPaintStroke() {
        return this.paintStroke;
    }

    public List<PolygonalChain> getPolygonalChains() {
        List list;
        synchronized (this.polygonalChains) {
            list = this.polygonalChains;
        }
        return list;
    }

    public synchronized void setPaintFill(Paint paintFill) {
        this.paintFill = paintFill;
    }

    public synchronized void setPaintStroke(Paint paintStroke) {
        this.paintStroke = paintStroke;
    }
}
