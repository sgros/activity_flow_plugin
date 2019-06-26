// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.utils;

import menion.android.whereyougo.preferences.Preferences;
import cz.matejcik.openwig.Zone;
import menion.android.whereyougo.geo.location.Location;
import cz.matejcik.openwig.EventTable;

public class UtilsWherigo
{
    public static Location extractLocation(final EventTable eventTable) {
        Location location;
        if (eventTable == null || !eventTable.isLocated()) {
            location = null;
        }
        else {
            final Location location2 = new Location();
            if (eventTable instanceof Zone) {
                final Zone zone = (Zone)eventTable;
                if (Preferences.GUIDING_ZONE_NAVIGATION_POINT == 1) {
                    location2.setLatitude(zone.nearestPoint.latitude);
                    location2.setLongitude(zone.nearestPoint.longitude);
                    location = location2;
                }
                else if (eventTable.position != null) {
                    location2.setLatitude(zone.position.latitude);
                    location2.setLongitude(zone.position.longitude);
                    location = location2;
                }
                else {
                    location2.setLatitude(zone.bbCenter.latitude);
                    location2.setLongitude(zone.bbCenter.longitude);
                    location = location2;
                }
            }
            else {
                location2.setLatitude(eventTable.position.latitude);
                location2.setLongitude(eventTable.position.longitude);
                location = location2;
            }
        }
        return location;
    }
}
