package menion.android.whereyougo.guide;

import menion.android.whereyougo.geo.location.Location;

public interface IGuide {
    void actualizeState(Location location);

    float getAzimuthToTaget();

    float getDistanceToTarget();

    Location getTargetLocation();

    String getTargetName();

    long getTimeToTarget();

    void manageDistanceSoundsBeeping(double d);
}
