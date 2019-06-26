// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import org.mapsforge.core.util.MercatorProjection;
import android.graphics.Path;
import org.mapsforge.core.model.Point;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import org.mapsforge.core.model.GeoPoint;
import java.util.List;

public class PolygonalChain
{
    private final List<GeoPoint> geoPoints;
    
    public PolygonalChain(final Collection<GeoPoint> c) {
        if (c == null) {
            this.geoPoints = Collections.synchronizedList(new ArrayList<GeoPoint>());
        }
        else {
            this.geoPoints = Collections.synchronizedList(new ArrayList<GeoPoint>(c));
        }
    }
    
    protected Path draw(final byte b, final Point point, final boolean b2) {
        while (true) {
            final Path path2;
            synchronized (this.geoPoints) {
                final int size = this.geoPoints.size();
                if (size < 2) {
                    return null;
                }
                path2 = new Path();
                for (int i = 0; i < size; ++i) {
                    final GeoPoint geoPoint = this.geoPoints.get(i);
                    final double latitude = geoPoint.latitude;
                    final float n = (float)(MercatorProjection.longitudeToPixelX(geoPoint.longitude, b) - point.x);
                    final float n2 = (float)(MercatorProjection.latitudeToPixelY(latitude, b) - point.y);
                    if (i == 0) {
                        path2.moveTo(n, n2);
                    }
                    else {
                        path2.lineTo(n, n2);
                    }
                }
            }
            if (b2 && !this.isClosed()) {
                path2.close();
            }
            // monitorexit(list)
            return path2;
        }
    }
    
    public List<GeoPoint> getGeoPoints() {
        synchronized (this.geoPoints) {
            return this.geoPoints;
        }
    }
    
    public boolean isClosed() {
        boolean equals = false;
        synchronized (this.geoPoints) {
            final int size = this.geoPoints.size();
            if (size >= 2) {
                equals = this.geoPoints.get(0).equals(this.geoPoints.get(size - 1));
            }
            // monitorexit(this.geoPoints)
            return equals;
        }
    }
}
