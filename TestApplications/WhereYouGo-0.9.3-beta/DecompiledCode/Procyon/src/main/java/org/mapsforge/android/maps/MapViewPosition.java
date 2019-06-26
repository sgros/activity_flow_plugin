// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.MapPosition;
import android.graphics.Point;
import org.mapsforge.core.model.GeoPoint;

public class MapViewPosition
{
    private double latitude;
    private double longitude;
    private final MapView mapView;
    private byte zoomLevel;
    
    MapViewPosition(final MapView mapView) {
        this.mapView = mapView;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.zoomLevel = 0;
    }
    
    private byte limitZoomLevel(final byte a) {
        return (byte)Math.max(Math.min(a, this.mapView.getZoomLevelMax()), this.mapView.getMapZoomControls().getZoomLevelMin());
    }
    
    private void setCenterInternal(final GeoPoint geoPoint) {
        final MapPosition mapPosition = this.getMapPosition();
        synchronized (this) {
            this.latitude = geoPoint.latitude;
            this.longitude = geoPoint.longitude;
            // monitorexit(this)
            final Projection projection = this.mapView.getProjection();
            final Point point = projection.toPoint(mapPosition.geoPoint, null, mapPosition.zoomLevel);
            final Point point2 = projection.toPoint(this.getCenter(), null, mapPosition.zoomLevel);
            this.mapView.getFrameBuffer().matrixPostTranslate((float)(point.x - point2.x), (float)(point.y - point2.y));
        }
    }
    
    private float setZoomLevelDiff(final byte b) {
        synchronized (this) {
            return this.setZoomLevelNew((byte)(this.zoomLevel + b));
        }
    }
    
    private void setZoomLevelInternal(final byte zoomLevelNew) {
        final float setZoomLevelNew = this.setZoomLevelNew(zoomLevelNew);
        this.mapView.getFrameBuffer().matrixPostScale(setZoomLevelNew, setZoomLevelNew, (float)(this.mapView.getWidth() / 2), (float)(this.mapView.getHeight() / 2));
    }
    
    private float setZoomLevelNew(final byte b) {
        synchronized (this) {
            final byte zoomLevel = this.zoomLevel;
            final byte limitZoomLevel = this.limitZoomLevel(b);
            float n;
            if (limitZoomLevel == zoomLevel) {
                n = 1.0f;
            }
            else {
                this.zoomLevel = limitZoomLevel;
                this.mapView.getMapZoomControls().onZoomLevelChange(limitZoomLevel);
                n = (float)Math.pow(2.0, limitZoomLevel - zoomLevel);
            }
            return n;
        }
    }
    
    public BoundingBox getBoundingBox() {
        synchronized (this) {
            final double longitudeToPixelX = MercatorProjection.longitudeToPixelX(this.longitude, this.zoomLevel);
            final double latitudeToPixelY = MercatorProjection.latitudeToPixelY(this.latitude, this.zoomLevel);
            final int n = this.mapView.getWidth() / 2;
            final int n2 = this.mapView.getHeight() / 2;
            final long mapSize = MercatorProjection.getMapSize(this.zoomLevel);
            return new BoundingBox(MercatorProjection.pixelYToLatitude(Math.min((double)mapSize, n2 + latitudeToPixelY), this.zoomLevel), MercatorProjection.pixelXToLongitude(Math.max(0.0, longitudeToPixelX - n), this.zoomLevel), MercatorProjection.pixelYToLatitude(Math.max(0.0, latitudeToPixelY - n2), this.zoomLevel), MercatorProjection.pixelXToLongitude(Math.min((double)mapSize, n + longitudeToPixelX), this.zoomLevel));
        }
    }
    
    public GeoPoint getCenter() {
        synchronized (this) {
            return new GeoPoint(this.latitude, this.longitude);
        }
    }
    
    public MapPosition getMapPosition() {
        synchronized (this) {
            return new MapPosition(this.getCenter(), this.zoomLevel);
        }
    }
    
    public byte getZoomLevel() {
        synchronized (this) {
            return this.zoomLevel;
        }
    }
    
    public void moveCenter(final float n, final float n2) {
        synchronized (this) {
            final double longitudeToPixelX = MercatorProjection.longitudeToPixelX(this.longitude, this.zoomLevel);
            final double n3 = n;
            final double latitudeToPixelY = MercatorProjection.latitudeToPixelY(this.latitude, this.zoomLevel);
            final double n4 = n2;
            final long mapSize = MercatorProjection.getMapSize(this.zoomLevel);
            this.setCenterInternal(new GeoPoint(MercatorProjection.pixelYToLatitude(Math.min(Math.max(0.0, latitudeToPixelY - n4), (double)mapSize), this.zoomLevel), MercatorProjection.pixelXToLongitude(Math.min(Math.max(0.0, longitudeToPixelX - n3), (double)mapSize), this.zoomLevel)));
            // monitorexit(this)
            this.mapView.redraw();
        }
    }
    
    public void setCenter(final GeoPoint centerInternal) {
        this.setCenterInternal(centerInternal);
        this.mapView.redraw();
    }
    
    public void setMapPosition(final MapPosition mapPosition) {
        synchronized (this) {
            this.setCenterInternal(mapPosition.geoPoint);
            this.setZoomLevelInternal(mapPosition.zoomLevel);
            // monitorexit(this)
            this.mapView.redraw();
        }
    }
    
    public void setZoomLevel(final byte zoomLevelInternal) {
        this.setZoomLevelInternal(zoomLevelInternal);
        this.mapView.redraw();
    }
    
    public void zoom(final byte zoomLevelDiff, final float n) {
        this.mapView.getZoomAnimator().startAnimation(n, this.setZoomLevelDiff(zoomLevelDiff), (float)(this.mapView.getWidth() / 2), (float)(this.mapView.getHeight() / 2));
    }
    
    public void zoomIn() {
        this.zoom((byte)1, 1.0f);
    }
    
    public void zoomOut() {
        this.zoom((byte)(-1), 1.0f);
    }
}
