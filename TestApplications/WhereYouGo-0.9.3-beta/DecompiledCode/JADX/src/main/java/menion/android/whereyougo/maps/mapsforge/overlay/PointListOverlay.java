package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import java.util.HashMap;
import menion.android.whereyougo.maps.mapsforge.TapEventListener;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.model.GeoPoint;

public class PointListOverlay extends ListOverlay {
    HashMap<GeoPoint, PointOverlay> hitMap = new HashMap();
    TapEventListener onTapListener;

    public synchronized void clear() {
        getOverlayItems().clear();
        this.hitMap.clear();
    }

    public synchronized boolean checkItemHit(GeoPoint geoPoint, MapView mapView) {
        boolean z;
        Log.e("litezee", "check hit " + geoPoint.latitude + " " + geoPoint.longitude);
        Projection projection = mapView.getProjection();
        Point eventPosition = projection.toPixels(geoPoint, null);
        if (eventPosition == null) {
            z = false;
        } else {
            Point checkItemPoint = new Point();
            for (int i = getOverlayItems().size() - 1; i >= 0; i--) {
                OverlayItem item = (OverlayItem) getOverlayItems().get(i);
                if (item instanceof PointOverlay) {
                    PointOverlay checkOverlayItem = (PointOverlay) item;
                    if (checkOverlayItem.getGeoPoint() != null) {
                        checkItemPoint = projection.toPixels(checkOverlayItem.getGeoPoint(), checkItemPoint);
                        if (checkItemPoint != null) {
                            Rect checkMarkerBounds = checkOverlayItem.getDrawable().getBounds();
                            if (!(checkMarkerBounds.left == checkMarkerBounds.right || checkMarkerBounds.top == checkMarkerBounds.bottom)) {
                                int checkLeft = checkItemPoint.x + checkMarkerBounds.left;
                                int checkTop = checkItemPoint.y + checkMarkerBounds.top;
                                int checkBottom = checkItemPoint.y + checkMarkerBounds.bottom;
                                if (checkItemPoint.x + checkMarkerBounds.right >= eventPosition.x && checkLeft <= eventPosition.x && checkBottom >= eventPosition.y && checkTop <= eventPosition.y && onTap(checkOverlayItem)) {
                                    this.hitMap.put(geoPoint, checkOverlayItem);
                                    z = true;
                                    break;
                                }
                            }
                        }
                        continue;
                    } else {
                        continue;
                    }
                }
            }
            z = false;
        }
        return z;
    }

    public synchronized void onTap(GeoPoint p) {
        Log.d("MAP", "tapped " + ((PointOverlay) this.hitMap.remove(p)).getId());
    }

    public synchronized boolean onTap(PointOverlay pointOverlay) {
        Log.d("MAP", "tapped bool " + pointOverlay.getId());
        if (this.onTapListener != null) {
            this.onTapListener.onTap(pointOverlay);
        }
        return true;
    }

    public void registerOnTapEvent(TapEventListener onTapListener) {
        this.onTapListener = onTapListener;
    }

    public void unregisterOnTapEvent() {
        this.onTapListener = null;
    }
}
