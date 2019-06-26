package org.osmdroid.views.overlay.mylocation;

import android.location.Location;

public interface IMyLocationConsumer {
   void onLocationChanged(Location var1, IMyLocationProvider var2);
}
