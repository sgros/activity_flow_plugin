// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import java.util.List;

class PoiWayBundle
{
    final List<PointOfInterest> pois;
    final List<Way> ways;
    
    PoiWayBundle(final List<PointOfInterest> pois, final List<Way> ways) {
        this.pois = pois;
        this.ways = ways;
    }
}
