// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ListOverlay implements Overlay
{
    private final List<OverlayItem> overlayItems;
    
    public ListOverlay() {
        this.overlayItems = Collections.synchronizedList(new ArrayList<OverlayItem>());
    }
    
    @Override
    public int compareTo(final Overlay overlay) {
        return 0;
    }
    
    @Override
    public void draw(final BoundingBox boundingBox, final byte b, final Canvas canvas) {
        synchronized (this) {
            final Point point = new Point(MercatorProjection.longitudeToPixelX(boundingBox.minLongitude, b), MercatorProjection.latitudeToPixelY(boundingBox.maxLatitude, b));
            synchronized (this.overlayItems) {
                for (int size = this.overlayItems.size(), i = 0; i < size; ++i) {
                    this.overlayItems.get(i).draw(boundingBox, b, canvas, point);
                }
            }
        }
    }
    
    public List<OverlayItem> getOverlayItems() {
        synchronized (this.overlayItems) {
            return this.overlayItems;
        }
    }
}
