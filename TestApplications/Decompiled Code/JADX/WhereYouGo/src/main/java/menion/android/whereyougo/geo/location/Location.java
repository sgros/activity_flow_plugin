package menion.android.whereyougo.geo.location;

public class Location extends locus.api.objects.extra.Location {
    public Location(android.location.Location loc) {
        this(loc.getProvider(), loc.getLatitude(), loc.getLongitude());
        setTime(loc.getTime());
        if (loc.hasAccuracy()) {
            setAccuracy(loc.getAccuracy());
        }
        if (loc.hasAltitude()) {
            setAltitude(loc.getAltitude());
        }
        if (loc.hasBearing()) {
            setBearing(loc.getBearing());
        }
        if (loc.hasSpeed()) {
            setSpeed(loc.getSpeed());
        }
    }

    public Location(Location loc) {
        super((locus.api.objects.extra.Location) loc);
    }

    public Location(String provider) {
        super(provider);
    }

    public Location(String provider, double lat, double lon) {
        super(provider, lat, lon);
    }
}
