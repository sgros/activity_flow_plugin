// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import android.os.Bundle;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import android.graphics.Paint$Style;
import android.location.Criteria;
import org.mapsforge.core.model.GeoPoint;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.content.Context;
import org.mapsforge.android.maps.MapView;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;

public class MyLocationOverlay implements LocationListener, Overlay
{
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
    
    public MyLocationOverlay(final Context context, final MapView mapView, final Drawable drawable) {
        this(context, mapView, drawable, getDefaultCircleFill(), getDefaultCircleStroke());
    }
    
    public MyLocationOverlay(final Context context, final MapView mapView, final Drawable drawable, final Paint paint, final Paint paint2) {
        this.mapView = mapView;
        this.locationManager = (LocationManager)context.getSystemService("location");
        this.marker = new Marker(null, drawable);
        this.circle = new Circle(null, 0.0f, paint, paint2);
    }
    
    private boolean enableBestAvailableProvider() {
        synchronized (this) {
            this.disableMyLocation();
            final Criteria criteria = new Criteria();
            criteria.setAccuracy(1);
            final String bestProvider = this.locationManager.getBestProvider(criteria, true);
            boolean b;
            if (bestProvider == null) {
                b = false;
            }
            else {
                this.locationManager.requestLocationUpdates(bestProvider, 1000L, 0.0f, (LocationListener)this);
                this.myLocationEnabled = true;
                b = true;
            }
            return b;
        }
    }
    
    private static Paint getDefaultCircleFill() {
        return getPaint(Paint$Style.FILL, -16776961, 48);
    }
    
    private static Paint getDefaultCircleStroke() {
        final Paint paint = getPaint(Paint$Style.STROKE, -16776961, 128);
        paint.setStrokeWidth(2.0f);
        return paint;
    }
    
    private static Paint getPaint(final Paint$Style style, final int color, final int alpha) {
        final Paint paint = new Paint(1);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAlpha(alpha);
        return paint;
    }
    
    public static GeoPoint locationToGeoPoint(final Location location) {
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }
    
    public int compareTo(final Overlay overlay) {
        return 0;
    }
    
    public void disableMyLocation() {
        synchronized (this) {
            if (this.myLocationEnabled) {
                this.myLocationEnabled = false;
                this.locationManager.removeUpdates((LocationListener)this);
                this.mapView.getOverlayController().redrawOverlays();
            }
        }
    }
    
    public void draw(final BoundingBox boundingBox, final byte b, final Canvas canvas) {
        synchronized (this) {
            if (this.myLocationEnabled) {
                final Point point = new Point(MercatorProjection.longitudeToPixelX(boundingBox.minLongitude, b), MercatorProjection.latitudeToPixelY(boundingBox.maxLatitude, b));
                this.circle.draw(boundingBox, b, canvas, point);
                this.marker.draw(boundingBox, b, canvas, point);
            }
        }
    }
    
    public boolean enableMyLocation(final boolean centerAtNextFix) {
        synchronized (this) {
            boolean b;
            if (!this.enableBestAvailableProvider()) {
                b = false;
            }
            else {
                this.centerAtNextFix = centerAtNextFix;
                b = true;
            }
            return b;
        }
    }
    
    public Location getLastLocation() {
        synchronized (this) {
            return this.lastLocation;
        }
    }
    
    public boolean isCenterAtNextFix() {
        synchronized (this) {
            return this.centerAtNextFix;
        }
    }
    
    public boolean isMyLocationEnabled() {
        synchronized (this) {
            return this.myLocationEnabled;
        }
    }
    
    public boolean isSnapToLocationEnabled() {
        synchronized (this) {
            return this.snapToLocationEnabled;
        }
    }
    
    public void onLocationChanged(final Location lastLocation) {
        synchronized (this) {
            this.lastLocation = lastLocation;
            final GeoPoint locationToGeoPoint = locationToGeoPoint(lastLocation);
            this.marker.setGeoPoint(locationToGeoPoint);
            this.circle.setGeoPoint(locationToGeoPoint);
            this.circle.setRadius(lastLocation.getAccuracy());
            if (this.centerAtNextFix || this.snapToLocationEnabled) {
                this.centerAtNextFix = false;
                this.mapView.getMapViewPosition().setCenter(locationToGeoPoint);
            }
            // monitorexit(this)
            this.mapView.getOverlayController().redrawOverlays();
        }
    }
    
    public void onProviderDisabled(final String s) {
        this.enableBestAvailableProvider();
    }
    
    public void onProviderEnabled(final String s) {
        this.enableBestAvailableProvider();
    }
    
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
    
    public void setSnapToLocationEnabled(final boolean snapToLocationEnabled) {
        synchronized (this) {
            this.snapToLocationEnabled = snapToLocationEnabled;
        }
    }
}
