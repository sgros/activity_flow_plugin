// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.overlay;

import android.location.Location;
import java.util.Collection;
import java.util.ArrayList;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.model.Point;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.android.maps.overlay.Overlay;

public class NavigationOverlay implements Overlay
{
    private Polyline line;
    final MyLocationOverlay myLocationOverlay;
    GeoPoint target;
    
    public NavigationOverlay(final MyLocationOverlay myLocationOverlay) {
        this.myLocationOverlay = myLocationOverlay;
        final Paint paint = new Paint(1);
        paint.setStyle(Paint$Style.STROKE);
        paint.setColor(-65536);
        paint.setStrokeWidth(2.0f);
        this.line = new Polyline(null, paint);
    }
    
    private static Point getPoint(final GeoPoint geoPoint, final Point point, final byte b) {
        return new Point((int)(MercatorProjection.longitudeToPixelX(geoPoint.longitude, b) - point.x), (int)(MercatorProjection.latitudeToPixelY(geoPoint.latitude, b) - point.y));
    }
    
    public boolean checkItemHit(final GeoPoint geoPoint, final MapView mapView) {
        // monitorenter(this)
        // monitorexit(this)
        return false;
    }
    
    @Override
    public int compareTo(final Overlay overlay) {
        return 0;
    }
    
    @Override
    public void draw(final BoundingBox boundingBox, final byte b, final Canvas canvas) {
        synchronized (this) {
            if (this.target != null && this.myLocationOverlay.isMyLocationEnabled() && this.myLocationOverlay.getLastLocation() != null) {
                final Point point = new Point(MercatorProjection.longitudeToPixelX(boundingBox.minLongitude, b), MercatorProjection.latitudeToPixelY(boundingBox.maxLatitude, b));
                final Location lastLocation = this.myLocationOverlay.getLastLocation();
                final GeoPoint geoPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
                final ArrayList<GeoPoint> list = new ArrayList<GeoPoint>();
                list.add(geoPoint);
                list.add(this.target);
                this.line.setPolygonalChain(new PolygonalChain(list));
                this.line.draw(boundingBox, b, canvas, point);
            }
        }
    }
    
    public GeoPoint getTarget() {
        synchronized (this) {
            return this.target;
        }
    }
    
    public void onTap(final GeoPoint geoPoint) {
    }
    
    public void setTarget(final GeoPoint target) {
        synchronized (this) {
            this.target = target;
        }
    }
}
