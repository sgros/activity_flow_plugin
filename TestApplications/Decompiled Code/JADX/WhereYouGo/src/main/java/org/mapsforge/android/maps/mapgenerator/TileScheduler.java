package org.mapsforge.android.maps.mapgenerator;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;

final class TileScheduler {
    private static final int ZOOM_LEVEL_PENALTY = 5;

    static double getPriority(Tile tile, MapView mapView) {
        byte tileZoomLevel = tile.zoomLevel;
        long tileCenterPixelY = tile.getPixelY() + 128;
        double tileCenterLongitude = MercatorProjection.pixelXToLongitude((double) (tile.getPixelX() + 128), tileZoomLevel);
        double tileCenterLatitude = MercatorProjection.pixelYToLatitude((double) tileCenterPixelY, tileZoomLevel);
        MapPosition mapPosition = mapView.getMapViewPosition().getMapPosition();
        GeoPoint geoPoint = mapPosition.geoPoint;
        double longitudeDiff = geoPoint.longitude - tileCenterLongitude;
        double latitudeDiff = geoPoint.latitude - tileCenterLatitude;
        double euclidianDistance = Math.sqrt((longitudeDiff * longitudeDiff) + (latitudeDiff * latitudeDiff));
        if (mapPosition.zoomLevel == tileZoomLevel) {
            return euclidianDistance;
        }
        double scaledEuclidianDistance;
        int zoomLevelDiff = Math.abs(mapPosition.zoomLevel - tileZoomLevel);
        double scaleFactor = Math.pow(2.0d, (double) zoomLevelDiff);
        if (mapPosition.zoomLevel < tileZoomLevel) {
            scaledEuclidianDistance = euclidianDistance * scaleFactor;
        } else {
            scaledEuclidianDistance = euclidianDistance / scaleFactor;
        }
        return scaledEuclidianDistance * ((double) (zoomLevelDiff * 5));
    }

    private TileScheduler() {
        throw new IllegalStateException();
    }
}
