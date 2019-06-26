// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.Tile;

final class TileScheduler
{
    private static final int ZOOM_LEVEL_PENALTY = 5;
    
    private TileScheduler() {
        throw new IllegalStateException();
    }
    
    static double getPriority(final Tile tile, final MapView mapView) {
        final byte zoomLevel = tile.zoomLevel;
        final long pixelX = tile.getPixelX();
        final long pixelY = tile.getPixelY();
        final double pixelXToLongitude = MercatorProjection.pixelXToLongitude((double)(pixelX + 128L), zoomLevel);
        final double pixelYToLatitude = MercatorProjection.pixelYToLatitude((double)(pixelY + 128L), zoomLevel);
        final MapPosition mapPosition = mapView.getMapViewPosition().getMapPosition();
        final GeoPoint geoPoint = mapPosition.geoPoint;
        final double n = geoPoint.longitude - pixelXToLongitude;
        final double n2 = geoPoint.latitude - pixelYToLatitude;
        double sqrt = Math.sqrt(n * n + n2 * n2);
        if (mapPosition.zoomLevel != zoomLevel) {
            final int abs = Math.abs(mapPosition.zoomLevel - zoomLevel);
            final double pow = Math.pow(2.0, abs);
            double n3;
            if (mapPosition.zoomLevel < zoomLevel) {
                n3 = sqrt * pow;
            }
            else {
                n3 = sqrt / pow;
            }
            sqrt = n3 * (abs * 5);
        }
        return sqrt;
    }
}
