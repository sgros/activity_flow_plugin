// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import android.graphics.Path;
import org.mapsforge.core.model.Point;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import android.graphics.Paint;

public class Polyline implements OverlayItem
{
    private Paint paintStroke;
    private PolygonalChain polygonalChain;
    
    public Polyline(final PolygonalChain polygonalChain, final Paint paintStroke) {
        this.polygonalChain = polygonalChain;
        this.paintStroke = paintStroke;
    }
    
    @Override
    public boolean draw(final BoundingBox boundingBox, final byte b, final Canvas canvas, final Point point) {
        final boolean b2 = false;
        // monitorenter(this)
        boolean b3 = b2;
        try {
            if (this.polygonalChain != null) {
                if (this.paintStroke == null) {
                    b3 = b2;
                }
                else {
                    final Path draw = this.polygonalChain.draw(b, point, false);
                    b3 = b2;
                    if (draw != null) {
                        canvas.drawPath(draw, this.paintStroke);
                        b3 = true;
                    }
                }
            }
            return b3;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public Paint getPaintStroke() {
        synchronized (this) {
            return this.paintStroke;
        }
    }
    
    public PolygonalChain getPolygonalChain() {
        synchronized (this) {
            return this.polygonalChain;
        }
    }
    
    public void setPaintStroke(final Paint paintStroke) {
        synchronized (this) {
            this.paintStroke = paintStroke;
        }
    }
    
    public void setPolygonalChain(final PolygonalChain polygonalChain) {
        synchronized (this) {
            this.polygonalChain = polygonalChain;
        }
    }
}
