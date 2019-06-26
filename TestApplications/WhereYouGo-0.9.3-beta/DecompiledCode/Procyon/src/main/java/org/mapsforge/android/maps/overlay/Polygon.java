// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import android.graphics.Path$FillType;
import android.graphics.Path;
import org.mapsforge.core.model.Point;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.graphics.Paint;

public class Polygon implements OverlayItem
{
    private Paint paintFill;
    private Paint paintStroke;
    private final List<PolygonalChain> polygonalChains;
    
    public Polygon(final Collection<PolygonalChain> c, final Paint paintFill, final Paint paintStroke) {
        if (c == null) {
            this.polygonalChains = Collections.synchronizedList(new ArrayList<PolygonalChain>());
        }
        else {
            this.polygonalChains = Collections.synchronizedList(new ArrayList<PolygonalChain>(c));
        }
        this.paintFill = paintFill;
        this.paintStroke = paintStroke;
    }
    
    @Override
    public boolean draw(final BoundingBox boundingBox, final byte b, final Canvas canvas, final Point point) {
        boolean b2 = true;
        synchronized (this) {
            synchronized (this.polygonalChains) {
                if (this.polygonalChains.isEmpty() || (this.paintStroke == null && this.paintFill == null)) {
                    b2 = false;
                }
                else {
                    final Path path = new Path();
                    path.setFillType(Path$FillType.EVEN_ODD);
                    for (int i = 0; i < this.polygonalChains.size(); ++i) {
                        final Path draw = this.polygonalChains.get(i).draw(b, point, true);
                        if (draw != null) {
                            path.addPath(draw);
                        }
                    }
                    if (this.paintStroke != null) {
                        canvas.drawPath(path, this.paintStroke);
                    }
                    if (this.paintFill != null) {
                        canvas.drawPath(path, this.paintFill);
                    }
                }
                return b2;
            }
        }
    }
    
    public Paint getPaintFill() {
        synchronized (this) {
            return this.paintFill;
        }
    }
    
    public Paint getPaintStroke() {
        synchronized (this) {
            return this.paintStroke;
        }
    }
    
    public List<PolygonalChain> getPolygonalChains() {
        synchronized (this.polygonalChains) {
            return this.polygonalChains;
        }
    }
    
    public void setPaintFill(final Paint paintFill) {
        synchronized (this) {
            this.paintFill = paintFill;
        }
    }
    
    public void setPaintStroke(final Paint paintStroke) {
        synchronized (this) {
            this.paintStroke = paintStroke;
        }
    }
}
