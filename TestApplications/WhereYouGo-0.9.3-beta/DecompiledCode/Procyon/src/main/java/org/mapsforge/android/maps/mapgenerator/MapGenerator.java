// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import org.mapsforge.core.model.GeoPoint;
import android.graphics.Bitmap;

public interface MapGenerator
{
    boolean executeJob(final MapGeneratorJob p0, final Bitmap p1);
    
    GeoPoint getStartPoint();
    
    Byte getStartZoomLevel();
    
    byte getZoomLevelMax();
    
    boolean requiresInternetConnection();
}
