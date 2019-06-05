package menion.android.whereyougo.openwig;

import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.Logger;
import p005cz.matejcik.openwig.platform.LocationService;

public class WLocationService implements LocationService {
    private static final String TAG = "WLocationService";

    public void connect() {
        Logger.m26w(TAG, "connect()");
    }

    public void disconnect() {
        Logger.m26w(TAG, "disconnect()");
    }

    public double getAltitude() {
        return LocationState.getLocation().getAltitude();
    }

    public double getHeading() {
        return (double) LocationState.getLocation().getBearing();
    }

    public double getLatitude() {
        return LocationState.getLocation().getLatitude();
    }

    public double getLongitude() {
        return LocationState.getLocation().getLongitude();
    }

    public double getPrecision() {
        return (double) LocationState.getLocation().getAccuracy();
    }

    public int getState() {
        if (LocationState.isActuallyHardwareGpsOn()) {
            return 3;
        }
        return 0;
    }
}
