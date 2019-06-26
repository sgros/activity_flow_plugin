// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator.blank;

import org.mapsforge.core.model.GeoPoint;
import android.graphics.Bitmap;
import org.mapsforge.android.maps.mapgenerator.MapGeneratorJob;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;

public class Blank implements MapGenerator
{
    @Override
    public boolean executeJob(final MapGeneratorJob mapGeneratorJob, final Bitmap bitmap) {
        return false;
    }
    
    @Override
    public GeoPoint getStartPoint() {
        return null;
    }
    
    @Override
    public Byte getStartZoomLevel() {
        return null;
    }
    
    @Override
    public byte getZoomLevelMax() {
        return 127;
    }
    
    @Override
    public boolean requiresInternetConnection() {
        return false;
    }
}
