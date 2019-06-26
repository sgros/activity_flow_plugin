// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.drawable.Drawable;
import org.mapsforge.core.model.GeoPoint;
import menion.android.whereyougo.maps.container.MapPoint;

public class PointOverlay extends LabelMarker
{
    int id;
    MapPoint point;
    
    public PointOverlay(final int id, final GeoPoint geoPoint, final Drawable drawable) {
        super(geoPoint, drawable);
        this.id = id;
    }
    
    public PointOverlay(final GeoPoint geoPoint, final Drawable drawable, final MapPoint mapPoint) {
        this(geoPoint, drawable, mapPoint, -1);
    }
    
    public PointOverlay(final GeoPoint geoPoint, final Drawable drawable, final MapPoint point, final int id) {
        final String name = point.getName();
        String description;
        if (point.getDescription() == null) {
            description = "";
        }
        else {
            description = point.getDescription();
        }
        super(geoPoint, drawable, name, description);
        this.id = id;
        this.point = point;
    }
    
    public int getId() {
        return this.id;
    }
    
    public MapPoint getPoint() {
        return this.point;
    }
}
