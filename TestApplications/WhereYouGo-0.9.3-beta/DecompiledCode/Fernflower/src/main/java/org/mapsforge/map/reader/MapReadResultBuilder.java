package org.mapsforge.map.reader;

import java.util.ArrayList;
import java.util.List;

class MapReadResultBuilder {
   boolean isWater;
   final List pointOfInterests = new ArrayList();
   final List ways = new ArrayList();

   void add(PoiWayBundle var1) {
      this.pointOfInterests.addAll(var1.pois);
      this.ways.addAll(var1.ways);
   }

   MapReadResult build() {
      return new MapReadResult(this);
   }
}
