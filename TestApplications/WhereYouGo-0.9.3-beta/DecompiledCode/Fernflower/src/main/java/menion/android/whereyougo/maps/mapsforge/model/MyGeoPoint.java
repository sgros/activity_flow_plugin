package menion.android.whereyougo.maps.mapsforge.model;

import org.mapsforge.core.model.GeoPoint;

public class MyGeoPoint extends GeoPoint {
   int id;

   public MyGeoPoint(double var1, double var3, int var5) {
      super(var1, var3);
      this.id = var5;
   }

   public int getId() {
      return this.id;
   }
}
