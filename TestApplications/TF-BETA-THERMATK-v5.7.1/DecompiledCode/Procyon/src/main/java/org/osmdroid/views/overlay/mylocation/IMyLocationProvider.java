// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay.mylocation;

import android.location.Location;

public interface IMyLocationProvider
{
    void destroy();
    
    Location getLastKnownLocation();
    
    boolean startLocationProvider(final IMyLocationConsumer p0);
    
    void stopLocationProvider();
}
