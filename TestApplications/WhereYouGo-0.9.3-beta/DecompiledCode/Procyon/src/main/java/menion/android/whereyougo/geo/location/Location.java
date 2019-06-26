// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.geo.location;

public class Location extends locus.api.objects.extra.Location
{
    public Location() {
    }
    
    public Location(final android.location.Location location) {
        this(location.getProvider(), location.getLatitude(), location.getLongitude());
        this.setTime(location.getTime());
        if (location.hasAccuracy()) {
            this.setAccuracy(location.getAccuracy());
        }
        if (location.hasAltitude()) {
            this.setAltitude(location.getAltitude());
        }
        if (location.hasBearing()) {
            this.setBearing(location.getBearing());
        }
        if (location.hasSpeed()) {
            this.setSpeed(location.getSpeed());
        }
    }
    
    public Location(final String s) {
        super(s);
    }
    
    public Location(final String s, final double n, final double n2) {
        super(s, n, n2);
    }
    
    public Location(final Location location) {
        super(location);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (o != this) {
            if (o == null) {
                b = false;
            }
            else if (!(o instanceof Location)) {
                b = false;
            }
            else {
                final Location location = (Location)o;
                if (this.getLatitude() != location.getLatitude() || this.getLongitude() != location.getLongitude()) {
                    b = false;
                }
            }
        }
        return b;
    }
}
