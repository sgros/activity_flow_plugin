// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.overlay;

import android.graphics.Rect;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.BoundingBox;
import android.graphics.Canvas;
import org.mapsforge.core.model.GeoPoint;
import android.graphics.drawable.Drawable;

public class Marker implements OverlayItem
{
    private Drawable drawable;
    private GeoPoint geoPoint;
    
    public Marker(final GeoPoint geoPoint, final Drawable drawable) {
        this.geoPoint = geoPoint;
        this.drawable = drawable;
    }
    
    public static Drawable boundCenter(final Drawable drawable) {
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();
        drawable.setBounds(intrinsicWidth / -2, intrinsicHeight / -2, intrinsicWidth / 2, intrinsicHeight / 2);
        return drawable;
    }
    
    public static Drawable boundCenterBottom(final Drawable drawable) {
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        drawable.setBounds(intrinsicWidth / -2, -drawable.getIntrinsicHeight(), intrinsicWidth / 2, 0);
        return drawable;
    }
    
    private static boolean intersect(final Canvas canvas, final float n, final float n2, final float n3, final float n4) {
        return n3 >= 0.0f && n <= canvas.getWidth() && n4 >= 0.0f && n2 <= canvas.getHeight();
    }
    
    @Override
    public boolean draw(final BoundingBox boundingBox, final byte b, final Canvas canvas, final Point point) {
        synchronized (this) {
            boolean b2;
            if (this.geoPoint == null || this.drawable == null) {
                b2 = false;
            }
            else {
                final double latitude = this.geoPoint.latitude;
                final int n = (int)(MercatorProjection.longitudeToPixelX(this.geoPoint.longitude, b) - point.x);
                final int n2 = (int)(MercatorProjection.latitudeToPixelY(latitude, b) - point.y);
                final Rect copyBounds = this.drawable.copyBounds();
                final int n3 = n + copyBounds.left;
                final int n4 = n2 + copyBounds.top;
                final int n5 = n + copyBounds.right;
                final int n6 = n2 + copyBounds.bottom;
                if (!intersect(canvas, (float)n3, (float)n4, (float)n5, (float)n6)) {
                    b2 = false;
                }
                else {
                    this.drawable.setBounds(n3, n4, n5, n6);
                    this.drawable.draw(canvas);
                    this.drawable.setBounds(copyBounds);
                    b2 = true;
                }
            }
            return b2;
        }
    }
    
    public Drawable getDrawable() {
        synchronized (this) {
            return this.drawable;
        }
    }
    
    public GeoPoint getGeoPoint() {
        synchronized (this) {
            return this.geoPoint;
        }
    }
    
    public void setDrawable(final Drawable drawable) {
        synchronized (this) {
            this.drawable = drawable;
        }
    }
    
    public void setGeoPoint(final GeoPoint geoPoint) {
        synchronized (this) {
            this.geoPoint = geoPoint;
        }
    }
}
