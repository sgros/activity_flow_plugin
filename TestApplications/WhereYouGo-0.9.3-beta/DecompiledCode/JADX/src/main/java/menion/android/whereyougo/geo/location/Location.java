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

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Location)) {
            return false;
        }
        Location other = (Location) obj;
        if (getLatitude() == other.getLatitude() && getLongitude() == other.getLongitude()) {
            return true;
        }
        return false;
    }
}
