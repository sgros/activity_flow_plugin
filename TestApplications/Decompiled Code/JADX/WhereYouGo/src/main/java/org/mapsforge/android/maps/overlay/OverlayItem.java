package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

public interface OverlayItem {
    boolean draw(BoundingBox boundingBox, byte b, Canvas canvas, Point point);
}
