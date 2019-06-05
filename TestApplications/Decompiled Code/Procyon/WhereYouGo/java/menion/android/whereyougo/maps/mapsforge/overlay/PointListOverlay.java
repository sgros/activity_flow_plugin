// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Rect;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.overlay.OverlayItem;
import android.graphics.Point;
import android.util.Log;
import org.mapsforge.android.maps.MapView;
import menion.android.whereyougo.maps.mapsforge.TapEventListener;
import org.mapsforge.core.model.GeoPoint;
import java.util.HashMap;
import org.mapsforge.android.maps.overlay.ListOverlay;

public class PointListOverlay extends ListOverlay
{
    HashMap<GeoPoint, PointOverlay> hitMap;
    TapEventListener onTapListener;
    
    public PointListOverlay() {
        this.hitMap = new HashMap<GeoPoint, PointOverlay>();
    }
    
    public boolean checkItemHit(final GeoPoint key, final MapView mapView) {
        synchronized (this) {
            Log.e("litezee", "check hit " + key.latitude + " " + key.longitude);
            final Projection projection = mapView.getProjection();
            final Point pixels = projection.toPixels(key, null);
            boolean b;
            if (pixels == null) {
                b = false;
            }
            else {
                Point point = new Point();
                Point point2;
                for (int i = this.getOverlayItems().size() - 1; i >= 0; --i, point = point2) {
                    final OverlayItem overlayItem = this.getOverlayItems().get(i);
                    if (!(overlayItem instanceof PointOverlay)) {
                        point2 = point;
                    }
                    else {
                        final PointOverlay value = (PointOverlay)overlayItem;
                        point2 = point;
                        if (value.getGeoPoint() != null) {
                            final Point pixels2 = projection.toPixels(value.getGeoPoint(), point);
                            if ((point2 = pixels2) != null) {
                                final Rect bounds = value.getDrawable().getBounds();
                                point2 = pixels2;
                                if (bounds.left != bounds.right) {
                                    point2 = pixels2;
                                    if (bounds.top != bounds.bottom) {
                                        final int x = pixels2.x;
                                        final int left = bounds.left;
                                        final int x2 = pixels2.x;
                                        final int right = bounds.right;
                                        final int y = pixels2.y;
                                        final int top = bounds.top;
                                        final int y2 = pixels2.y;
                                        final int bottom = bounds.bottom;
                                        point2 = pixels2;
                                        if (x2 + right >= pixels.x) {
                                            point2 = pixels2;
                                            if (x + left <= pixels.x) {
                                                point2 = pixels2;
                                                if (y2 + bottom >= pixels.y) {
                                                    point2 = pixels2;
                                                    if (y + top <= pixels.y) {
                                                        point2 = pixels2;
                                                        if (this.onTap(value)) {
                                                            this.hitMap.put(key, value);
                                                            b = true;
                                                            return b;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                b = false;
            }
            return b;
        }
    }
    
    public void clear() {
        synchronized (this) {
            this.getOverlayItems().clear();
            this.hitMap.clear();
        }
    }
    
    public void onTap(final GeoPoint key) {
        synchronized (this) {
            Log.d("MAP", "tapped " + this.hitMap.remove(key).getId());
        }
    }
    
    public boolean onTap(final PointOverlay pointOverlay) {
        synchronized (this) {
            Log.d("MAP", "tapped bool " + pointOverlay.getId());
            if (this.onTapListener != null) {
                this.onTapListener.onTap(pointOverlay);
            }
            return true;
        }
    }
    
    public void registerOnTapEvent(final TapEventListener onTapListener) {
        this.onTapListener = onTapListener;
    }
    
    public void unregisterOnTapEvent() {
        this.onTapListener = null;
    }
}
