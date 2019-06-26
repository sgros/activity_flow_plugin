package menion.android.whereyougo.gui.utils;

import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.preferences.Preferences;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Zone;

public class UtilsWherigo {
    public static Location extractLocation(EventTable et) {
        if (et == null || !et.isLocated()) {
            return null;
        }
        Location loc = new Location();
        if (et instanceof Zone) {
            Zone z = (Zone) et;
            if (Preferences.GUIDING_ZONE_NAVIGATION_POINT == 1) {
                loc.setLatitude(z.nearestPoint.latitude);
                loc.setLongitude(z.nearestPoint.longitude);
                return loc;
            } else if (et.position != null) {
                loc.setLatitude(z.position.latitude);
                loc.setLongitude(z.position.longitude);
                return loc;
            } else {
                loc.setLatitude(z.bbCenter.latitude);
                loc.setLongitude(z.bbCenter.longitude);
                return loc;
            }
        }
        loc.setLatitude(et.position.latitude);
        loc.setLongitude(et.position.longitude);
        return loc;
    }
}
