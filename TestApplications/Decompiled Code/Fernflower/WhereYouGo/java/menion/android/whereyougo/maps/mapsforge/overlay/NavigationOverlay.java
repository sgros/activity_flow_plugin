package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.location.Location;
import java.util.ArrayList;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class NavigationOverlay implements Overlay {
   private Polyline line;
   final MyLocationOverlay myLocationOverlay;
   GeoPoint target;

   public NavigationOverlay(MyLocationOverlay var1) {
      this.myLocationOverlay = var1;
      Paint var2 = new Paint(1);
      var2.setStyle(Style.STROKE);
      var2.setColor(-65536);
      var2.setStrokeWidth(2.0F);
      this.line = new Polyline((PolygonalChain)null, var2);
   }

   private static Point getPoint(GeoPoint var0, Point var1, byte var2) {
      int var3 = (int)(MercatorProjection.longitudeToPixelX(var0.longitude, var2) - var1.x);
      int var4 = (int)(MercatorProjection.latitudeToPixelY(var0.latitude, var2) - var1.y);
      return new Point((double)var3, (double)var4);
   }

   public boolean checkItemHit(GeoPoint var1, MapView var2) {
      synchronized(this){}
      return false;
   }

   public int compareTo(Overlay var1) {
      return 0;
   }

   public void draw(BoundingBox var1, byte var2, Canvas var3) {
      synchronized(this){}

      Throwable var10000;
      label96: {
         Location var4;
         boolean var10001;
         try {
            if (this.target == null || !this.myLocationOverlay.isMyLocationEnabled()) {
               return;
            }

            var4 = this.myLocationOverlay.getLastLocation();
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label96;
         }

         if (var4 == null) {
            return;
         }

         label87:
         try {
            double var5 = MercatorProjection.longitudeToPixelX(var1.minLongitude, var2);
            double var7 = MercatorProjection.latitudeToPixelY(var1.maxLatitude, var2);
            Point var20 = new Point(var5, var7);
            Location var9 = this.myLocationOverlay.getLastLocation();
            GeoPoint var10 = new GeoPoint(var9.getLatitude(), var9.getLongitude());
            ArrayList var21 = new ArrayList();
            var21.add(var10);
            var21.add(this.target);
            Polyline var11 = this.line;
            PolygonalChain var18 = new PolygonalChain(var21);
            var11.setPolygonalChain(var18);
            this.line.draw(var1, var2, var3, var20);
            return;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label87;
         }
      }

      Throwable var19 = var10000;
      throw var19;
   }

   public GeoPoint getTarget() {
      synchronized(this){}

      GeoPoint var1;
      try {
         var1 = this.target;
      } finally {
         ;
      }

      return var1;
   }

   public void onTap(GeoPoint var1) {
   }

   public void setTarget(GeoPoint var1) {
      synchronized(this){}

      try {
         this.target = var1;
      } finally {
         ;
      }

   }
}
