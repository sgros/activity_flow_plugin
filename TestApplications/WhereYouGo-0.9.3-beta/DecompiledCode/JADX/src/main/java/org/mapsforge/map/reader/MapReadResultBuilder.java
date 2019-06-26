package org.mapsforge.map.reader;

import java.util.ArrayList;
import java.util.List;

class MapReadResultBuilder {
    boolean isWater;
    final List<PointOfInterest> pointOfInterests = new ArrayList();
    final List<Way> ways = new ArrayList();

    MapReadResultBuilder() {
    }

    /* Access modifiers changed, original: 0000 */
    public void add(PoiWayBundle poiWayBundle) {
        this.pointOfInterests.addAll(poiWayBundle.pois);
        this.ways.addAll(poiWayBundle.ways);
    }

    /* Access modifiers changed, original: 0000 */
    public MapReadResult build() {
        return new MapReadResult(this);
    }
}
