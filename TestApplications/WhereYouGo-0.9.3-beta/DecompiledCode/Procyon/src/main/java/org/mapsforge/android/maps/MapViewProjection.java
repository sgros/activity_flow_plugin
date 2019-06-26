// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import android.graphics.Point;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.model.GeoPoint;

class MapViewProjection implements Projection
{
    private static final String INVALID_MAP_VIEW_DIMENSIONS = "invalid MapView dimensions";
    private final MapView mapView;
    
    MapViewProjection(final MapView mapView) {
        this.mapView = mapView;
    }
    
    @Override
    public GeoPoint fromPixels(final int n, final int n2) {
        GeoPoint geoPoint;
        if (this.mapView.getWidth() <= 0 || this.mapView.getHeight() <= 0) {
            geoPoint = null;
        }
        else {
            final MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
            final GeoPoint geoPoint2 = mapPosition.geoPoint;
            geoPoint = new GeoPoint(MercatorProjection.pixelYToLatitude(n2 + (MercatorProjection.latitudeToPixelY(geoPoint2.latitude, mapPosition.zoomLevel) - (this.mapView.getHeight() >> 1)), mapPosition.zoomLevel), MercatorProjection.pixelXToLongitude(n + (MercatorProjection.longitudeToPixelX(geoPoint2.longitude, mapPosition.zoomLevel) - (this.mapView.getWidth() >> 1)), mapPosition.zoomLevel));
        }
        return geoPoint;
    }
    
    @Override
    public double getLatitudeSpan() {
        if (this.mapView.getWidth() > 0 && this.mapView.getHeight() > 0) {
            return Math.abs(this.fromPixels(0, 0).latitude - this.fromPixels(0, this.mapView.getHeight()).latitude);
        }
        throw new IllegalStateException("invalid MapView dimensions");
    }
    
    @Override
    public double getLongitudeSpan() {
        if (this.mapView.getWidth() > 0 && this.mapView.getHeight() > 0) {
            return Math.abs(this.fromPixels(0, 0).longitude - this.fromPixels(this.mapView.getWidth(), 0).longitude);
        }
        throw new IllegalStateException("invalid MapView dimensions");
    }
    
    @Override
    public float metersToPixels(final float n, final byte b) {
        return (float)(n * (1.0 / MercatorProjection.calculateGroundResolution(this.mapView.getMapViewPosition().getCenter().latitude, b)));
    }
    
    @Override
    public Point toPixels(final GeoPoint geoPoint, Point point) {
        if (this.mapView.getWidth() <= 0 || this.mapView.getHeight() <= 0) {
            point = null;
        }
        else {
            final MapPosition mapPosition = this.mapView.getMapViewPosition().getMapPosition();
            final GeoPoint geoPoint2 = mapPosition.geoPoint;
            final double longitudeToPixelX = MercatorProjection.longitudeToPixelX(geoPoint2.longitude, mapPosition.zoomLevel);
            final double latitudeToPixelY = MercatorProjection.latitudeToPixelY(geoPoint2.latitude, mapPosition.zoomLevel);
            final double n = longitudeToPixelX - (this.mapView.getWidth() >> 1);
            final double n2 = latitudeToPixelY - (this.mapView.getHeight() >> 1);
            if (point == null) {
                point = new Point((int)(MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel) - n), (int)(MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel) - n2));
            }
            else {
                point.x = (int)(MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel) - n);
                point.y = (int)(MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel) - n2);
            }
        }
        return point;
    }
    
    @Override
    public Point toPoint(final GeoPoint geoPoint, Point point, final byte b) {
        if (point == null) {
            point = new Point((int)MercatorProjection.longitudeToPixelX(geoPoint.longitude, b), (int)MercatorProjection.latitudeToPixelY(geoPoint.latitude, b));
        }
        else {
            point.x = (int)MercatorProjection.longitudeToPixelX(geoPoint.longitude, b);
            point.y = (int)MercatorProjection.latitudeToPixelY(geoPoint.latitude, b);
        }
        return point;
    }
}
