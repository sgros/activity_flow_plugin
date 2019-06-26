package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class Marker implements OverlayItem {
    private Drawable drawable;
    private GeoPoint geoPoint;

    public static Drawable boundCenter(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        drawable.setBounds(intrinsicWidth / -2, intrinsicHeight / -2, intrinsicWidth / 2, intrinsicHeight / 2);
        return drawable;
    }

    public static Drawable boundCenterBottom(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        drawable.setBounds(intrinsicWidth / -2, -drawable.getIntrinsicHeight(), intrinsicWidth / 2, 0);
        return drawable;
    }

    private static boolean intersect(Canvas canvas, float left, float top, float right, float bottom) {
        return right >= 0.0f && left <= ((float) canvas.getWidth()) && bottom >= 0.0f && top <= ((float) canvas.getHeight());
    }

    public Marker(GeoPoint geoPoint, Drawable drawable) {
        this.geoPoint = geoPoint;
        this.drawable = drawable;
    }

    public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
        boolean z;
        if (this.geoPoint == null || this.drawable == null) {
            z = false;
        } else {
            int pixelX = (int) (MercatorProjection.longitudeToPixelX(this.geoPoint.longitude, zoomLevel) - canvasPosition.f68x);
            int pixelY = (int) (MercatorProjection.latitudeToPixelY(this.geoPoint.latitude, zoomLevel) - canvasPosition.f69y);
            Rect drawableBounds = this.drawable.copyBounds();
            int left = pixelX + drawableBounds.left;
            int top = pixelY + drawableBounds.top;
            int right = pixelX + drawableBounds.right;
            int bottom = pixelY + drawableBounds.bottom;
            if (intersect(canvas, (float) left, (float) top, (float) right, (float) bottom)) {
                this.drawable.setBounds(left, top, right, bottom);
                this.drawable.draw(canvas);
                this.drawable.setBounds(drawableBounds);
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public synchronized Drawable getDrawable() {
        return this.drawable;
    }

    public synchronized GeoPoint getGeoPoint() {
        return this.geoPoint;
    }

    public synchronized void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public synchronized void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
