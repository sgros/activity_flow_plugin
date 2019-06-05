package menion.android.whereyougo.guide;

import menion.android.whereyougo.geo.location.Location;
import p005cz.matejcik.openwig.Zone;

public class ZoneGuide extends Guide {
    private boolean mAlreadyEntered = false;
    private final Zone mZone;

    public ZoneGuide(Zone zone) {
        super(zone.name, new Location("Guidance: " + zone.name, zone.bbCenter.latitude, zone.bbCenter.longitude));
        this.mZone = zone;
        this.mAlreadyEntered = false;
    }
}
