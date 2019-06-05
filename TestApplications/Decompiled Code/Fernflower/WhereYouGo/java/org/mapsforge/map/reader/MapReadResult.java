package org.mapsforge.map.reader;

import java.util.List;

public class MapReadResult {
   public final boolean isWater;
   public final List pointOfInterests;
   public final List ways;

   MapReadResult(MapReadResultBuilder var1) {
      this.pointOfInterests = var1.pointOfInterests;
      this.ways = var1.ways;
      this.isWater = var1.isWater;
   }
}
