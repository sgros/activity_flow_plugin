// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import org.mapsforge.core.model.Point;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;

public interface OverlayItem
{
    boolean draw(final BoundingBox p0, final byte p1, final Canvas p2, final Point p3);
}
