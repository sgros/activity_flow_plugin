package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;

public interface Overlay extends Comparable<Overlay> {
    void draw(BoundingBox boundingBox, byte b, Canvas canvas);
}
