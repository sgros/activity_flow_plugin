// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

class MapReadResultBuilder
{
    boolean isWater;
    final List<PointOfInterest> pointOfInterests;
    final List<Way> ways;
    
    MapReadResultBuilder() {
        this.pointOfInterests = new ArrayList<PointOfInterest>();
        this.ways = new ArrayList<Way>();
    }
    
    void add(final PoiWayBundle poiWayBundle) {
        this.pointOfInterests.addAll(poiWayBundle.pois);
        this.ways.addAll(poiWayBundle.ways);
    }
    
    MapReadResult build() {
        return new MapReadResult(this);
    }
}
