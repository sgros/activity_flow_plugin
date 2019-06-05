// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.openwig;

import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.Logger;
import cz.matejcik.openwig.platform.LocationService;

public class WLocationService implements LocationService
{
    private static final String TAG = "WLocationService";
    
    @Override
    public void connect() {
        Logger.w("WLocationService", "connect()");
    }
    
    @Override
    public void disconnect() {
        Logger.w("WLocationService", "disconnect()");
    }
    
    @Override
    public double getAltitude() {
        return LocationState.getLocation().getAltitude();
    }
    
    @Override
    public double getHeading() {
        return LocationState.getLocation().getBearing();
    }
    
    @Override
    public double getLatitude() {
        return LocationState.getLocation().getLatitude();
    }
    
    @Override
    public double getLongitude() {
        return LocationState.getLocation().getLongitude();
    }
    
    @Override
    public double getPrecision() {
        return LocationState.getLocation().getAccuracy();
    }
    
    @Override
    public int getState() {
        int n;
        if (LocationState.isActuallyHardwareGpsOn()) {
            n = 3;
        }
        else {
            n = 0;
        }
        return n;
    }
}
