// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import org.mapsforge.core.model.Point;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.util.MercatorProjection;
import android.graphics.Paint;
import org.mapsforge.core.model.GeoPoint;

public class Circle implements OverlayItem
{
    private GeoPoint geoPoint;
    private Paint paintFill;
    private Paint paintStroke;
    private float radius;
    
    public Circle(final GeoPoint geoPoint, final float radius, final Paint paintFill, final Paint paintStroke) {
        checkRadius(radius);
        this.geoPoint = geoPoint;
        this.radius = radius;
        this.paintFill = paintFill;
        this.paintStroke = paintStroke;
    }
    
    private static void checkRadius(final float f) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("radius must not be negative: " + f);
        }
    }
    
    private static double metersToPixels(double calculateGroundResolution, final float n, final byte b) {
        calculateGroundResolution = MercatorProjection.calculateGroundResolution(calculateGroundResolution, b);
        return n / calculateGroundResolution;
    }
    
    @Override
    public boolean draw(final BoundingBox boundingBox, final byte b, final Canvas canvas, final Point point) {
        synchronized (this) {
            boolean b2;
            if (this.geoPoint == null || (this.paintStroke == null && this.paintFill == null)) {
                b2 = false;
            }
            else {
                final double latitude = this.geoPoint.latitude;
                final float n = (float)(MercatorProjection.longitudeToPixelX(this.geoPoint.longitude, b) - point.x);
                final float n2 = (float)(MercatorProjection.latitudeToPixelY(latitude, b) - point.y);
                final float n3 = (float)metersToPixels(latitude, this.radius, b);
                if (this.paintStroke != null) {
                    canvas.drawCircle(n, n2, n3, this.paintStroke);
                }
                if (this.paintFill != null) {
                    canvas.drawCircle(n, n2, n3, this.paintFill);
                }
                b2 = true;
            }
            return b2;
        }
    }
    
    public GeoPoint getGeoPoint() {
        synchronized (this) {
            return this.geoPoint;
        }
    }
    
    public Paint getPaintFill() {
        synchronized (this) {
            return this.paintFill;
        }
    }
    
    public Paint getPaintStroke() {
        synchronized (this) {
            return this.paintStroke;
        }
    }
    
    public float getRadius() {
        synchronized (this) {
            return this.radius;
        }
    }
    
    public void setGeoPoint(final GeoPoint geoPoint) {
        synchronized (this) {
            this.geoPoint = geoPoint;
        }
    }
    
    public void setPaintFill(final Paint paintFill) {
        synchronized (this) {
            this.paintFill = paintFill;
        }
    }
    
    public void setPaintStroke(final Paint paintStroke) {
        synchronized (this) {
            this.paintStroke = paintStroke;
        }
    }
    
    public void setRadius(final float radius) {
        synchronized (this) {
            checkRadius(radius);
            this.radius = radius;
        }
    }
}
