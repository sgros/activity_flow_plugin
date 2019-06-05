package org.mapsforge.android.maps;

import android.graphics.Point;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;

public class MapViewPosition {
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private final MapView mapView;
    private byte zoomLevel = (byte) 0;

    MapViewPosition(MapView mapView) {
        this.mapView = mapView;
    }

    public synchronized BoundingBox getBoundingBox() {
        double pixelY;
        int halfCanvasHeight;
        long mapSize;
        double pixelXMin;
        double pixelYMin;
        double pixelXMax;
        double pixelX = MercatorProjection.longitudeToPixelX(this.longitude, this.zoomLevel);
        pixelY = MercatorProjection.latitudeToPixelY(this.latitude, this.zoomLevel);
        int halfCanvasWidth = this.mapView.getWidth() / 2;
        halfCanvasHeight = this.mapView.getHeight() / 2;
        mapSize = MercatorProjection.getMapSize(this.zoomLevel);
        pixelXMin = Math.max(0.0d, pixelX - ((double) halfCanvasWidth));
        pixelYMin = Math.max(0.0d, pixelY - ((double) halfCanvasHeight));
        pixelXMax = Math.min((double) mapSize, ((double) halfCanvasWidth) + pixelX);
        return new BoundingBox(MercatorProjection.pixelYToLatitude(Math.min((double) mapSize, ((double) halfCanvasHeight) + pixelY), this.zoomLevel), MercatorProjection.pixelXToLongitude(pixelXMin, this.zoomLevel), MercatorProjection.pixelYToLatitude(pixelYMin, this.zoomLevel), MercatorProjection.pixelXToLongitude(pixelXMax, this.zoomLevel));
    }

    public synchronized GeoPoint getCenter() {
        return new GeoPoint(this.latitude, this.longitude);
    }

    public synchronized MapPosition getMapPosition() {
        return new MapPosition(getCenter(), this.zoomLevel);
    }

    public synchronized byte getZoomLevel() {
        return this.zoomLevel;
    }

    public void moveCenter(float moveHorizontal, float moveVertical) {
        synchronized (this) {
            double pixelX = MercatorProjection.longitudeToPixelX(this.longitude, this.zoomLevel) - ((double) moveHorizontal);
            double pixelY = MercatorProjection.latitudeToPixelY(this.latitude, this.zoomLevel) - ((double) moveVertical);
            long mapSize = MercatorProjection.getMapSize(this.zoomLevel);
            setCenterInternal(new GeoPoint(MercatorProjection.pixelYToLatitude(Math.min(Math.max(0.0d, pixelY), (double) mapSize), this.zoomLevel), MercatorProjection.pixelXToLongitude(Math.min(Math.max(0.0d, pixelX), (double) mapSize), this.zoomLevel)));
        }
        this.mapView.redraw();
    }

    public void setCenter(GeoPoint geoPoint) {
        setCenterInternal(geoPoint);
        this.mapView.redraw();
    }

    public void setMapPosition(MapPosition mapPosition) {
        synchronized (this) {
            setCenterInternal(mapPosition.geoPoint);
            setZoomLevelInternal(mapPosition.zoomLevel);
        }
        this.mapView.redraw();
    }

    public void setZoomLevel(byte zoomLevel) {
        setZoomLevelInternal(zoomLevel);
        this.mapView.redraw();
    }

    public void zoom(byte zoomLevelDiff, float scaleFactorStart) {
        this.mapView.getZoomAnimator().startAnimation(scaleFactorStart, setZoomLevelDiff(zoomLevelDiff), (float) (this.mapView.getWidth() / 2), (float) (this.mapView.getHeight() / 2));
    }

    public void zoomIn() {
        zoom((byte) 1, 1.0f);
    }

    public void zoomOut() {
        zoom((byte) -1, 1.0f);
    }

    private byte limitZoomLevel(byte newZoomLevel) {
        return (byte) Math.max(Math.min(newZoomLevel, this.mapView.getZoomLevelMax()), this.mapView.getMapZoomControls().getZoomLevelMin());
    }

    private void setCenterInternal(GeoPoint geoPoint) {
        MapPosition mapPositionBefore = getMapPosition();
        synchronized (this) {
            this.latitude = geoPoint.latitude;
            this.longitude = geoPoint.longitude;
        }
        Projection projection = this.mapView.getProjection();
        Point pointBefore = projection.toPoint(mapPositionBefore.geoPoint, null, mapPositionBefore.zoomLevel);
        Point pointAfter = projection.toPoint(getCenter(), null, mapPositionBefore.zoomLevel);
        this.mapView.getFrameBuffer().matrixPostTranslate((float) (pointBefore.x - pointAfter.x), (float) (pointBefore.y - pointAfter.y));
    }

    private synchronized float setZoomLevelDiff(byte zoomLevelDiff) {
        return setZoomLevelNew((byte) (this.zoomLevel + zoomLevelDiff));
    }

    private void setZoomLevelInternal(byte zoomLevelNew) {
        float scaleFactor = setZoomLevelNew(zoomLevelNew);
        this.mapView.getFrameBuffer().matrixPostScale(scaleFactor, scaleFactor, (float) (this.mapView.getWidth() / 2), (float) (this.mapView.getHeight() / 2));
    }

    private synchronized float setZoomLevelNew(byte zoomLevelUnlimited) {
        float f;
        byte zoomLevelOld = this.zoomLevel;
        byte zoomLevelNew = limitZoomLevel(zoomLevelUnlimited);
        if (zoomLevelNew == zoomLevelOld) {
            f = 1.0f;
        } else {
            this.zoomLevel = zoomLevelNew;
            this.mapView.getMapZoomControls().onZoomLevelChange(zoomLevelNew);
            f = (float) Math.pow(2.0d, (double) (zoomLevelNew - zoomLevelOld));
        }
        return f;
    }
}
