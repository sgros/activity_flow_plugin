package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class LabelMarker extends Marker {
    static Paint labelBgPaint = new Paint();
    static Paint labelPaint = new Paint();
    protected String description;
    protected String label;
    protected boolean labelVisible;
    protected boolean markerVisible;

    static {
        labelPaint.setStyle(Style.STROKE);
        labelBgPaint.setColor(Color.argb(192, 255, 255, 255));
        labelBgPaint.setStyle(Style.FILL);
    }

    public LabelMarker(GeoPoint geoPoint, Drawable drawable) {
        this(geoPoint, drawable, null, null);
    }

    public LabelMarker(GeoPoint geoPoint, Drawable drawable, String label) {
        this(geoPoint, drawable, label, null);
    }

    public LabelMarker(GeoPoint geoPoint, Drawable drawable, String label, String description) {
        super(geoPoint, drawable);
        this.markerVisible = true;
        this.labelVisible = true;
        this.label = label;
        this.description = description;
    }

    public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point canvasPosition) {
        boolean z;
        if (this.markerVisible && !super.draw(boundingBox, zoomLevel, canvas, canvasPosition)) {
            z = false;
        } else if (this.labelVisible && this.label == null) {
            z = false;
        } else if (this.labelVisible) {
            GeoPoint geoPoint = getGeoPoint();
            Drawable drawable = getDrawable();
            int pixelX = (int) (MercatorProjection.longitudeToPixelX(geoPoint.longitude, zoomLevel) - canvasPosition.f68x);
            int pixelY = (int) (MercatorProjection.latitudeToPixelY(geoPoint.latitude, zoomLevel) - canvasPosition.f69y);
            Rect drawableBounds = drawable.copyBounds();
            int left = pixelX + drawableBounds.left;
            int top = pixelY + drawableBounds.top;
            int right = pixelX + drawableBounds.right;
            int bottom = pixelY + drawableBounds.bottom;
            Rect text = new Rect();
            labelPaint.getTextBounds(this.label, 0, this.label.length(), text);
            int x = ((left + right) / 2) - (text.width() / 2);
            int y = bottom;
            Rect rect = new Rect(x - 2, y - 2, (text.width() + x) + 2, (text.height() + y) + 2);
            canvas.drawRect(rect, labelBgPaint);
            canvas.drawRect(rect, labelPaint);
            canvas.drawText(this.label, (float) x, (float) (text.height() + y), labelPaint);
            z = true;
        } else {
            z = true;
        }
        return z;
    }

    public synchronized String getDescription() {
        return this.description;
    }

    public synchronized void setDescription(String description) {
        this.description = description;
    }

    public synchronized String getLabel() {
        return this.label;
    }

    public synchronized void setLabel(String label) {
        this.label = label;
    }

    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    public void setLabelVisible(boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    public boolean isMarkerVisible() {
        return this.markerVisible;
    }

    public void setMarkerVisible(boolean markerVisible) {
        this.markerVisible = markerVisible;
    }
}
