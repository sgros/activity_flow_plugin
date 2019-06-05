package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.drawable.Drawable;
import menion.android.whereyougo.maps.container.MapPoint;
import org.mapsforge.core.model.GeoPoint;

public class PointOverlay extends LabelMarker {
   int id;
   MapPoint point;

   public PointOverlay(int var1, GeoPoint var2, Drawable var3) {
      super(var2, var3);
      this.id = var1;
   }

   public PointOverlay(GeoPoint var1, Drawable var2, MapPoint var3) {
      this(var1, var2, var3, -1);
   }

   public PointOverlay(GeoPoint var1, Drawable var2, MapPoint var3, int var4) {
      String var5 = var3.getName();
      String var6;
      if (var3.getDescription() == null) {
         var6 = "";
      } else {
         var6 = var3.getDescription();
      }

      super(var1, var2, var5, var6);
      this.id = var4;
      this.point = var3;
   }

   public int getId() {
      return this.id;
   }

   public MapPoint getPoint() {
      return this.point;
   }
}
