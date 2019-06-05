package org.mapsforge.android.maps.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class MyLocationOverlay implements LocationListener, Overlay {
    private static final int UPDATE_DISTANCE = 0;
    private static final int UPDATE_INTERVAL = 1000;
    private boolean centerAtNextFix;
    private final Circle circle;
    private Location lastLocation;
    private final LocationManager locationManager;
    private final MapView mapView;
    private final Marker marker;
    private boolean myLocationEnabled;
    private boolean snapToLocationEnabled;

    public static GeoPoint locationToGeoPoint(Location location) {
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }

    private static Paint getDefaultCircleFill() {
        return getPaint(Style.FILL, -16776961, 48);
    }

    private static Paint getDefaultCircleStroke() {
        Paint paint = getPaint(Style.STROKE, -16776961, 128);
        paint.setStrokeWidth(2.0f);
        return paint;
    }

    private static Paint getPaint(Style style, int color, int alpha) {
        Paint paint = new Paint(1);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAlpha(alpha);
        return paint;
    }

    public MyLocationOverlay(Context context, MapView mapView, Drawable drawable) {
        this(context, mapView, drawable, getDefaultCircleFill(), getDefaultCircleStroke());
    }

    public MyLocationOverlay(Context context, MapView mapView, Drawable drawable, Paint circleFill, Paint circleStroke) {
        this.mapView = mapView;
        this.locationManager = (LocationManager) context.getSystemService("location");
        this.marker = new Marker(null, drawable);
        this.circle = new Circle(null, 0.0f, circleFill, circleStroke);
    }

    public synchronized void disableMyLocation() {
        if (this.myLocationEnabled) {
            this.myLocationEnabled = false;
            this.locationManager.removeUpdates(this);
            this.mapView.getOverlayController().redrawOverlays();
        }
    }

    public synchronized void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas) {
        if (this.myLocationEnabled) {
            Point canvasPosition = new Point(MercatorProjection.longitudeToPixelX(boundingBox.minLongitude, zoomLevel), MercatorProjection.latitudeToPixelY(boundingBox.maxLatitude, zoomLevel));
            this.circle.draw(boundingBox, zoomLevel, canvas, canvasPosition);
            this.marker.draw(boundingBox, zoomLevel, canvas, canvasPosition);
        }
    }

    public synchronized boolean enableMyLocation(boolean centerAtFirstFix) {
        boolean z;
        if (enableBestAvailableProvider()) {
            this.centerAtNextFix = centerAtFirstFix;
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public synchronized Location getLastLocation() {
        return this.lastLocation;
    }

    public synchronized boolean isCenterAtNextFix() {
        return this.centerAtNextFix;
    }

    public synchronized boolean isMyLocationEnabled() {
        return this.myLocationEnabled;
    }

    public synchronized boolean isSnapToLocationEnabled() {
        return this.snapToLocationEnabled;
    }

    public void onLocationChanged(Location location) {
        synchronized (this) {
            this.lastLocation = location;
            GeoPoint geoPoint = locationToGeoPoint(location);
            this.marker.setGeoPoint(geoPoint);
            this.circle.setGeoPoint(geoPoint);
            this.circle.setRadius(location.getAccuracy());
            if (this.centerAtNextFix || this.snapToLocationEnabled) {
                this.centerAtNextFix = false;
                this.mapView.getMapViewPosition().setCenter(geoPoint);
            }
        }
        this.mapView.getOverlayController().redrawOverlays();
    }

    public void onProviderDisabled(String provider) {
        enableBestAvailableProvider();
    }

    public void onProviderEnabled(String provider) {
        enableBestAvailableProvider();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public synchronized void setSnapToLocationEnabled(boolean snapToLocationEnabled) {
        this.snapToLocationEnabled = snapToLocationEnabled;
    }

    private synchronized boolean enableBestAvailableProvider() {
        boolean z;
        disableMyLocation();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        String bestAvailableProvider = this.locationManager.getBestProvider(criteria, true);
        if (bestAvailableProvider == null) {
            z = false;
        } else {
            this.locationManager.requestLocationUpdates(bestAvailableProvider, 1000, 0.0f, this);
            this.myLocationEnabled = true;
            z = true;
        }
        return z;
    }

    public int compareTo(Overlay o) {
        return 0;
    }
}
