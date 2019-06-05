package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

public interface OverlayItem {
   boolean draw(BoundingBox var1, byte var2, Canvas var3, Point var4);
}
