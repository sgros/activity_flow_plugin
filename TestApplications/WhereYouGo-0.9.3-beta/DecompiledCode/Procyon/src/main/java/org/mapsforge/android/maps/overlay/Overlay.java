// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;

public interface Overlay extends Comparable<Overlay>
{
    void draw(final BoundingBox p0, final byte p1, final Canvas p2);
}
