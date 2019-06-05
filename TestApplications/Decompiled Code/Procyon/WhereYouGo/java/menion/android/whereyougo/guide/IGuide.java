// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.guide;

import menion.android.whereyougo.geo.location.Location;

public interface IGuide
{
    void actualizeState(final Location p0);
    
    float getAzimuthToTaget();
    
    float getDistanceToTarget();
    
    Location getTargetLocation();
    
    String getTargetName();
    
    long getTimeToTarget();
    
    void manageDistanceSoundsBeeping(final double p0);
}
