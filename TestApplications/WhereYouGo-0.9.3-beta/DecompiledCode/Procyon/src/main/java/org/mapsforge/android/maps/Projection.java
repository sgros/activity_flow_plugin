// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import android.graphics.Point;
import org.mapsforge.core.model.GeoPoint;

public interface Projection
{
    GeoPoint fromPixels(final int p0, final int p1);
    
    double getLatitudeSpan();
    
    double getLongitudeSpan();
    
    float metersToPixels(final float p0, final byte p1);
    
    Point toPixels(final GeoPoint p0, final Point p1);
    
    Point toPoint(final GeoPoint p0, final Point p1, final byte p2);
}
