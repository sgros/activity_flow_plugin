package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.drawable.Drawable;
import menion.android.whereyougo.maps.container.MapPoint;
import org.mapsforge.core.model.GeoPoint;

public class PointOverlay extends LabelMarker {
    /* renamed from: id */
    int f99id;
    MapPoint point;

    public PointOverlay(GeoPoint geoPoint, Drawable drawable, MapPoint point) {
        this(geoPoint, drawable, point, -1);
    }

    public PointOverlay(GeoPoint geoPoint, Drawable drawable, MapPoint point, int id) {
        super(geoPoint, drawable, point.getName(), point.getDescription() == null ? "" : point.getDescription());
        this.f99id = id;
        this.point = point;
    }

    public PointOverlay(int id, GeoPoint geoPoint, Drawable drawable) {
        super(geoPoint, drawable);
        this.f99id = id;
    }

    public int getId() {
        return this.f99id;
    }

    public MapPoint getPoint() {
        return this.point;
    }
}
