package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class Circle implements OverlayItem {
    private GeoPoint geoPoint;
    private Paint paintFill;
    private Paint paintStroke;
    private float radius;

    private static void checkRadius(float radius) {
        if (radius < 0.0f) {
            throw new IllegalArgumentException("radius must not be negative: " + radius);
        }
    }

    private static double metersToPixels(double latitude, float meters, byte zoom) {
        return ((double) meters) / MercatorProjection.calculateGroundResolution(latitude, zoom);
    }

    public Circle(GeoPoint geoPoint, float radius, Paint paintFill, Paint paintStroke) {
        checkRadius(radius);
        this.geoPoint = geoPoint;
        this.radius = radius;
        this.paintFill = paintFill;
        this.paintStroke = paintStroke;
    }

    public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
        boolean z;
        if (this.geoPoint == null || (this.paintStroke == null && this.paintFill == null)) {
            z = false;
        } else {
            double latitude = this.geoPoint.latitude;
            float pixelX = (float) (MercatorProjection.longitudeToPixelX(this.geoPoint.longitude, zoomLevel) - canvasPosition.f68x);
            float pixelY = (float) (MercatorProjection.latitudeToPixelY(latitude, zoomLevel) - canvasPosition.f69y);
            float radiusInPixel = (float) metersToPixels(latitude, this.radius, zoomLevel);
            if (this.paintStroke != null) {
                canvas.drawCircle(pixelX, pixelY, radiusInPixel, this.paintStroke);
            }
            if (this.paintFill != null) {
                canvas.drawCircle(pixelX, pixelY, radiusInPixel, this.paintFill);
            }
            z = true;
        }
        return z;
    }

    public synchronized GeoPoint getGeoPoint() {
        return this.geoPoint;
    }

    public synchronized Paint getPaintFill() {
        return this.paintFill;
    }

    public synchronized Paint getPaintStroke() {
        return this.paintStroke;
    }

    public synchronized float getRadius() {
        return this.radius;
    }

    public synchronized void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public synchronized void setPaintFill(Paint paintFill) {
        this.paintFill = paintFill;
    }

    public synchronized void setPaintStroke(Paint paintStroke) {
        this.paintStroke = paintStroke;
    }

    public synchronized void setRadius(float radius) {
        checkRadius(radius);
        this.radius = radius;
    }
}
