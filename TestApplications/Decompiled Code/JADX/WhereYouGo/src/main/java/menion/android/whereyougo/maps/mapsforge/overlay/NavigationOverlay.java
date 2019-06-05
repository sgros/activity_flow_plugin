package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.location.Location;
import android.support.p000v4.internal.view.SupportMenu;
import java.util.ArrayList;
import java.util.List;
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

    public NavigationOverlay(MyLocationOverlay myLocationOverlay) {
        this.myLocationOverlay = myLocationOverlay;
        Paint paintStroke = new Paint(1);
        paintStroke.setStyle(Style.STROKE);
        paintStroke.setColor(SupportMenu.CATEGORY_MASK);
        paintStroke.setStrokeWidth(2.0f);
        this.line = new Polyline(null, paintStroke);
    }

    private static Point getPoint(GeoPoint geoPoint, Point canvasPosition, byte zoomLevel) {
        return new Point((double) ((int) (MercatorProjection.longitudeToPixelX(geoPoint.longitude, zoomLevel) - canvasPosition.f68x)), (double) ((int) (MercatorProjection.latitudeToPixelY(geoPoint.latitude, zoomLevel) - canvasPosition.f69y)));
    }

    public int compareTo(Overlay arg0) {
        return 0;
    }

    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas) {
        if (!(this.target == null || !this.myLocationOverlay.isMyLocationEnabled() || this.myLocationOverlay.getLastLocation() == null)) {
            Point canvasPosition = new Point(MercatorProjection.longitudeToPixelX(boundingBox.minLongitude, zoomLevel), MercatorProjection.latitudeToPixelY(boundingBox.maxLatitude, zoomLevel));
            Location startLocation = this.myLocationOverlay.getLastLocation();
            GeoPoint start = new GeoPoint(startLocation.getLatitude(), startLocation.getLongitude());
            List<GeoPoint> geoPoints = new ArrayList();
            geoPoints.add(start);
            geoPoints.add(this.target);
            this.line.setPolygonalChain(new PolygonalChain(geoPoints));
            this.line.draw(boundingBox, zoomLevel, canvas, canvasPosition);
        }
    }

    public synchronized GeoPoint getTarget() {
        return this.target;
    }

    public synchronized void setTarget(GeoPoint target) {
        this.target = target;
    }

    public synchronized boolean checkItemHit(GeoPoint geoPoint, MapView mapView) {
        return false;
    }

    public void onTap(GeoPoint p) {
    }
}
