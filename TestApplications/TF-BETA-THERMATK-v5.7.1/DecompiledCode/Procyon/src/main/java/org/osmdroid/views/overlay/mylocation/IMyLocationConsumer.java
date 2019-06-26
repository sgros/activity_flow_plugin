// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay.mylocation;

import android.location.Location;

public interface IMyLocationConsumer
{
    void onLocationChanged(final Location p0, final IMyLocationProvider p1);
}
