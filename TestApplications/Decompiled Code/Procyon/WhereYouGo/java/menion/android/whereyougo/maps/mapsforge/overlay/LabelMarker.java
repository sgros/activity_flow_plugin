// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Rect;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.core.model.Point;
import android.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import android.graphics.drawable.Drawable;
import org.mapsforge.core.model.GeoPoint;
import android.graphics.Color;
import android.graphics.Paint$Style;
import android.graphics.Paint;
import org.mapsforge.android.maps.overlay.Marker;

public class LabelMarker extends Marker
{
    static Paint labelBgPaint;
    static Paint labelPaint;
    protected String description;
    protected String label;
    protected boolean labelVisible;
    protected boolean markerVisible;
    
    static {
        (LabelMarker.labelPaint = new Paint()).setStyle(Paint$Style.STROKE);
        (LabelMarker.labelBgPaint = new Paint()).setColor(Color.argb(192, 255, 255, 255));
        LabelMarker.labelBgPaint.setStyle(Paint$Style.FILL);
    }
    
    public LabelMarker(final GeoPoint geoPoint, final Drawable drawable) {
        this(geoPoint, drawable, null, null);
    }
    
    public LabelMarker(final GeoPoint geoPoint, final Drawable drawable, final String s) {
        this(geoPoint, drawable, s, null);
    }
    
    public LabelMarker(final GeoPoint geoPoint, final Drawable drawable, final String label, final String description) {
        super(geoPoint, drawable);
        this.markerVisible = true;
        this.labelVisible = true;
        this.label = label;
        this.description = description;
    }
    
    @Override
    public boolean draw(final BoundingBox boundingBox, final byte b, final Canvas canvas, final Point point) {
        synchronized (this) {
            boolean b2;
            if (this.markerVisible && !super.draw(boundingBox, b, canvas, point)) {
                b2 = false;
            }
            else if (this.labelVisible && this.label == null) {
                b2 = false;
            }
            else if (!this.labelVisible) {
                b2 = true;
            }
            else {
                final GeoPoint geoPoint = this.getGeoPoint();
                final Drawable drawable = this.getDrawable();
                final double latitude = geoPoint.latitude;
                final int n = (int)(MercatorProjection.longitudeToPixelX(geoPoint.longitude, b) - point.x);
                final int n2 = (int)(MercatorProjection.latitudeToPixelY(latitude, b) - point.y);
                final Rect copyBounds = drawable.copyBounds();
                final int left = copyBounds.left;
                final int top = copyBounds.top;
                final int right = copyBounds.right;
                final int n3 = n2 + copyBounds.bottom;
                final Rect rect = new Rect();
                LabelMarker.labelPaint.getTextBounds(this.label, 0, this.label.length(), rect);
                final int n4 = (n + left + (n + right)) / 2 - rect.width() / 2;
                final Rect rect2 = new Rect(n4 - 2, n3 - 2, rect.width() + n4 + 2, rect.height() + n3 + 2);
                canvas.drawRect(rect2, LabelMarker.labelBgPaint);
                canvas.drawRect(rect2, LabelMarker.labelPaint);
                canvas.drawText(this.label, (float)n4, (float)(rect.height() + n3), LabelMarker.labelPaint);
                b2 = true;
            }
            return b2;
        }
    }
    
    public String getDescription() {
        synchronized (this) {
            return this.description;
        }
    }
    
    public String getLabel() {
        synchronized (this) {
            return this.label;
        }
    }
    
    public boolean isLabelVisible() {
        return this.labelVisible;
    }
    
    public boolean isMarkerVisible() {
        return this.markerVisible;
    }
    
    public void setDescription(final String description) {
        synchronized (this) {
            this.description = description;
        }
    }
    
    public void setLabel(final String label) {
        synchronized (this) {
            this.label = label;
        }
    }
    
    public void setLabelVisible(final boolean labelVisible) {
        this.labelVisible = labelVisible;
    }
    
    public void setMarkerVisible(final boolean markerVisible) {
        this.markerVisible = markerVisible;
    }
}
