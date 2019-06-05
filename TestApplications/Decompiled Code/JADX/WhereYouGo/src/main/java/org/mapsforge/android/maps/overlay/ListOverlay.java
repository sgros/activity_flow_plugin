package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class ListOverlay implements Overlay {
    private final List<OverlayItem> overlayItems = Collections.synchronizedList(new ArrayList());

    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas) {
        Point canvasPosition = new Point(MercatorProjection.longitudeToPixelX(boundingBox.minLongitude, zoomLevel), MercatorProjection.latitudeToPixelY(boundingBox.maxLatitude, zoomLevel));
        synchronized (this.overlayItems) {
            int numberOfOverlayItems = this.overlayItems.size();
            for (int i = 0; i < numberOfOverlayItems; i++) {
                ((OverlayItem) this.overlayItems.get(i)).draw(boundingBox, zoomLevel, canvas, canvasPosition);
            }
        }
    }

    public List<OverlayItem> getOverlayItems() {
        List list;
        synchronized (this.overlayItems) {
            list = this.overlayItems;
        }
        return list;
    }

    public int compareTo(Overlay o) {
        return 0;
    }
}
