package menion.android.whereyougo.maps.mapsforge.model;

import org.mapsforge.core.model.GeoPoint;

public class MyGeoPoint extends GeoPoint {
    /* renamed from: id */
    int f83id;

    public MyGeoPoint(double latitude, double longitude, int id) {
        super(latitude, longitude);
        this.f83id = id;
    }

    public int getId() {
        return this.f83id;
    }
}
