package org.mapsforge.android.maps;

import android.graphics.Point;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;

class MapViewProjection implements Projection {
    private static final String INVALID_MAP_VIEW_DIMENSIONS = "invalid MapView dimensions";
    private final MapView mapView;

    MapViewProjection(MapView mapView) {
        this.mapView = mapView;
    }

    public GeoPoint fromPixels(int x, int y) {
        if (this.mapView.getWidth() <= 0 || this.mapView.getHeight() <= 0) {
            return null;
        }
        MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
        GeoPoint geoPoint = mapPosition.geoPoint;
        double pixelX = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
        return new GeoPoint(MercatorProjection.pixelYToLatitude(((double) y) + (MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel) - ((double) (this.mapView.getHeight() >> 1))), mapPosition.zoomLevel), MercatorProjection.pixelXToLongitude(((double) x) + (pixelX - ((double) (this.mapView.getWidth() >> 1))), mapPosition.zoomLevel));
    }

    public double getLatitudeSpan() {
        if (this.mapView.getWidth() <= 0 || this.mapView.getHeight() <= 0) {
            throw new IllegalStateException(INVALID_MAP_VIEW_DIMENSIONS);
        }
        return Math.abs(fromPixels(0, 0).latitude - fromPixels(0, this.mapView.getHeight()).latitude);
    }

    public double getLongitudeSpan() {
        if (this.mapView.getWidth() <= 0 || this.mapView.getHeight() <= 0) {
            throw new IllegalStateException(INVALID_MAP_VIEW_DIMENSIONS);
        }
        return Math.abs(fromPixels(0, 0).longitude - fromPixels(this.mapView.getWidth(), 0).longitude);
    }

    public float metersToPixels(float meters, byte zoom) {
        return (float) (((double) meters) * (1.0d / MercatorProjection.calculateGroundResolution(this.mapView.getMapViewPosition().getCenter().latitude, zoom)));
    }

    public Point toPixels(GeoPoint in, Point out) {
        if (this.mapView.getWidth() <= 0 || this.mapView.getHeight() <= 0) {
            return null;
        }
        MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
        GeoPoint geoPoint = mapPosition.geoPoint;
        double pixelX = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
        pixelX -= (double) (this.mapView.getWidth() >> 1);
        double pixelY = MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel) - ((double) (this.mapView.getHeight() >> 1));
        if (out == null) {
            return new Point((int) (MercatorProjection.longitudeToPixelX(in.longitude, mapPosition.zoomLevel) - pixelX), (int) (MercatorProjection.latitudeToPixelY(in.latitude, mapPosition.zoomLevel) - pixelY));
        }
        out.x = (int) (MercatorProjection.longitudeToPixelX(in.longitude, mapPosition.zoomLevel) - pixelX);
        out.y = (int) (MercatorProjection.latitudeToPixelY(in.latitude, mapPosition.zoomLevel) - pixelY);
        return out;
    }

    public Point toPoint(GeoPoint in, Point out, byte zoom) {
        if (out == null) {
            return new Point((int) MercatorProjection.longitudeToPixelX(in.longitude, zoom), (int) MercatorProjection.latitudeToPixelY(in.latitude, zoom));
        }
        out.x = (int) MercatorProjection.longitudeToPixelX(in.longitude, zoom);
        out.y = (int) MercatorProjection.latitudeToPixelY(in.latitude, zoom);
        return out;
    }
}
