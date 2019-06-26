package org.mapsforge.map.reader;

import java.util.List;
import org.mapsforge.core.model.GeoPoint;

public class Way {
   public final GeoPoint[][] geoPoints;
   public final GeoPoint labelPosition;
   public final byte layer;
   public final List tags;

   Way(byte var1, List var2, GeoPoint[][] var3, GeoPoint var4) {
      this.layer = (byte)var1;
      this.tags = var2;
      this.geoPoints = var3;
      this.labelPosition = var4;
   }
}
