package org.mapsforge.map.reader;

import java.util.List;
import org.mapsforge.core.model.GeoPoint;

public class PointOfInterest {
   public final byte layer;
   public final GeoPoint position;
   public final List tags;

   PointOfInterest(byte var1, List var2, GeoPoint var3) {
      this.layer = (byte)var1;
      this.tags = var2;
      this.position = var3;
   }
}
