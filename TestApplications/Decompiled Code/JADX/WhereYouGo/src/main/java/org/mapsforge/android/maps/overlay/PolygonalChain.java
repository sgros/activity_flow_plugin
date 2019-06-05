package org.mapsforge.android.maps.overlay;

import android.graphics.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class PolygonalChain {
    private final List<GeoPoint> geoPoints;

    public PolygonalChain(Collection<GeoPoint> geoPoints) {
        if (geoPoints == null) {
            this.geoPoints = Collections.synchronizedList(new ArrayList());
        } else {
            this.geoPoints = Collections.synchronizedList(new ArrayList(geoPoints));
        }
    }

    public List<GeoPoint> getGeoPoints() {
        List list;
        synchronized (this.geoPoints) {
            list = this.geoPoints;
        }
        return list;
    }

    public boolean isClosed() {
        boolean z = false;
        synchronized (this.geoPoints) {
            int numberOfGeoPoints = this.geoPoints.size();
            if (numberOfGeoPoints < 2) {
            } else {
                z = ((GeoPoint) this.geoPoints.get(0)).equals((GeoPoint) this.geoPoints.get(numberOfGeoPoints - 1));
            }
        }
        return z;
    }

    /* Access modifiers changed, original: protected */
    public Path draw(byte zoomLevel, Point canvasPosition, boolean closeAutomatically) {
        Path path;
        synchronized (this.geoPoints) {
            int numberOfGeoPoints = this.geoPoints.size();
            if (numberOfGeoPoints < 2) {
                path = null;
            } else {
                path = new Path();
                for (int i = 0; i < numberOfGeoPoints; i++) {
                    GeoPoint geoPoint = (GeoPoint) this.geoPoints.get(i);
                    float pixelX = (float) (MercatorProjection.longitudeToPixelX(geoPoint.longitude, zoomLevel) - canvasPosition.f68x);
                    float pixelY = (float) (MercatorProjection.latitudeToPixelY(geoPoint.latitude, zoomLevel) - canvasPosition.f69y);
                    if (i == 0) {
                        path.moveTo(pixelX, pixelY);
                    } else {
                        path.lineTo(pixelX, pixelY);
                    }
                }
                if (closeAutomatically) {
                    if (!isClosed()) {
                        path.close();
                    }
                }
            }
        }
        return path;
    }
}
