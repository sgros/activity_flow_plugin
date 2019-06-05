package menion.android.whereyougo.guide;

import menion.android.whereyougo.geo.location.Location;

public class WaypointGuide extends Guide {
    private static final String TAG = "WaypointGuide";

    public WaypointGuide(String name, Location location) {
        super(name, location);
    }
}
