// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Rect;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.model.BoundingBox;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.android.maps.overlay.Marker;

public class RotationMarker extends Marker
{
    float rotation;
    
    public RotationMarker(final GeoPoint geoPoint, final Drawable drawable) {
        super(geoPoint, drawable);
    }
    
    private static boolean intersect(final Canvas canvas, final float n, final float n2, final float n3, final float n4) {
        return n3 >= 0.0f && n <= canvas.getWidth() && n4 >= 0.0f && n2 <= canvas.getHeight();
    }
    
    @Override
    public boolean draw(final BoundingBox boundingBox, final byte b, final Canvas canvas, final Point point) {
        synchronized (this) {
            final GeoPoint geoPoint = this.getGeoPoint();
            final Drawable drawable = this.getDrawable();
            boolean b2;
            if (geoPoint == null || drawable == null) {
                b2 = false;
            }
            else {
                final double latitude = geoPoint.latitude;
                final int n = (int)(MercatorProjection.longitudeToPixelX(geoPoint.longitude, b) - point.x);
                final int n2 = (int)(MercatorProjection.latitudeToPixelY(latitude, b) - point.y);
                final Rect copyBounds = drawable.copyBounds();
                final int n3 = n + copyBounds.left;
                final int n4 = n2 + copyBounds.top;
                final int n5 = n + copyBounds.right;
                final int n6 = n2 + copyBounds.bottom;
                if (!intersect(canvas, (float)n3, (float)n4, (float)n5, (float)n6)) {
                    b2 = false;
                }
                else {
                    final int save = canvas.save();
                    canvas.rotate(this.rotation, (float)n, (float)n2);
                    drawable.setBounds(n3, n4, n5, n6);
                    drawable.draw(canvas);
                    drawable.setBounds(copyBounds);
                    canvas.restoreToCount(save);
                    b2 = true;
                }
            }
            return b2;
        }
    }
    
    public float getRotation() {
        return this.rotation;
    }
    
    public void setRotation(final float rotation) {
        this.rotation = rotation;
    }
}
