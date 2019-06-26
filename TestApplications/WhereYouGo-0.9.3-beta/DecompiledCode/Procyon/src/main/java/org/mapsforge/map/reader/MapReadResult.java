// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.reader;

import java.util.List;

public class MapReadResult
{
    public final boolean isWater;
    public final List<PointOfInterest> pointOfInterests;
    public final List<Way> ways;
    
    MapReadResult(final MapReadResultBuilder mapReadResultBuilder) {
        this.pointOfInterests = mapReadResultBuilder.pointOfInterests;
        this.ways = mapReadResultBuilder.ways;
        this.isWater = mapReadResultBuilder.isWater;
    }
}
