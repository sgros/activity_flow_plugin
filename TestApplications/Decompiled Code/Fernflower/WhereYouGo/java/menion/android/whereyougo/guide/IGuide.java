package menion.android.whereyougo.guide;

import menion.android.whereyougo.geo.location.Location;

public interface IGuide {
   void actualizeState(Location var1);

   float getAzimuthToTaget();

   float getDistanceToTarget();

   Location getTargetLocation();

   String getTargetName();

   long getTimeToTarget();

   void manageDistanceSoundsBeeping(double var1);
}
