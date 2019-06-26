package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class RotationMarker extends Marker {
    float rotation;

    public RotationMarker(GeoPoint geoPoint, Drawable drawable) {
        super(geoPoint, drawable);
    }

    private static boolean intersect(Canvas canvas, float left, float top, float right, float bottom) {
        return right >= 0.0f && left <= ((float) canvas.getWidth()) && bottom >= 0.0f && top <= ((float) canvas.getHeight());
    }

    public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
        boolean z;
        GeoPoint geoPoint = getGeoPoint();
        Drawable drawable = getDrawable();
        if (geoPoint == null || drawable == null) {
            z = false;
        } else {
            int pixelX = (int) (MercatorProjection.longitudeToPixelX(geoPoint.longitude, zoomLevel) - canvasPosition.f68x);
            int pixelY = (int) (MercatorProjection.latitudeToPixelY(geoPoint.latitude, zoomLevel) - canvasPosition.f69y);
            Rect drawableBounds = drawable.copyBounds();
            int left = pixelX + drawableBounds.left;
            int top = pixelY + drawableBounds.top;
            int right = pixelX + drawableBounds.right;
            int bottom = pixelY + drawableBounds.bottom;
            if (intersect(canvas, (float) left, (float) top, (float) right, (float) bottom)) {
                int saveCount = canvas.save();
                canvas.rotate(this.rotation, (float) pixelX, (float) pixelY);
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(canvas);
                drawable.setBounds(drawableBounds);
                canvas.restoreToCount(saveCount);
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
