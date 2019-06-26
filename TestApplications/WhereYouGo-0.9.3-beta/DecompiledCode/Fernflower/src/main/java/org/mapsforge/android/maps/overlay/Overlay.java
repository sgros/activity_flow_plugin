package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;

public interface Overlay extends Comparable {
   void draw(BoundingBox var1, byte var2, Canvas var3);
}
