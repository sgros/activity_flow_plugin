package menion.android.whereyougo.geo.location;

import android.content.Context;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.audio.UtilsAudio;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;

public class GpsConnection {
    private static final String TAG = "GpsConnection";
    private final MyGpsListener gpsListener = new MyGpsListener(this, null);
    private boolean gpsProviderEnabled;
    private GpsStatus gpsStatus;
    private boolean isFixed = false;
    private final MyLocationListener llGPS = new MyLocationListener();
    private final MyLocationListener llNetwork = new MyLocationListener();
    private LocationManager locationManager;
    private Timer mGpsTimer;
    private boolean networkProviderEnabled;

    /* renamed from: menion.android.whereyougo.geo.location.GpsConnection$1 */
    class C02591 extends TimerTask {
        C02591() {
        }

        public void run() {
            if (Preferences.GPS_BEEP_ON_GPS_FIX) {
                UtilsAudio.playBeep(2);
            }
            GpsConnection.this.mGpsTimer = null;
            GpsConnection.this.isFixed = false;
        }
    }

    private class MyGpsListener implements Listener {
        private MyGpsListener() {
        }

        /* synthetic */ MyGpsListener(GpsConnection x0, C02591 x1) {
            this();
        }

        public void onGpsStatusChanged(int event) {
            try {
                if (GpsConnection.this.locationManager != null && event != 3) {
                    if (event == 4) {
                        if (GpsConnection.this.gpsStatus == null) {
                            GpsConnection.this.gpsStatus = GpsConnection.this.locationManager.getGpsStatus(null);
                        } else {
                            GpsConnection.this.gpsStatus = GpsConnection.this.locationManager.getGpsStatus(GpsConnection.this.gpsStatus);
                        }
                        LocationState.onGpsStatusChanged(event, GpsConnection.this.gpsStatus);
                    } else if (event == 1) {
                        LocationState.onGpsStatusChanged(event, null);
                    } else if (event == 2) {
                        LocationState.onGpsStatusChanged(event, null);
                    }
                }
            } catch (Exception e) {
                Logger.m22e(GpsConnection.TAG, "onGpsStatusChanged(" + event + ")", e);
            }
        }
    }

    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            GpsConnection.this.handleOnLocationChanged(new Location(location));
        }

        public void onProviderDisabled(String provider) {
            LocationState.onProviderDisabled(provider);
            if (GpsConnection.this.locationManager != null && !GpsConnection.this.locationManager.isProviderEnabled("gps") && !GpsConnection.this.locationManager.isProviderEnabled("network")) {
                LocationState.setGpsOff(PreferenceValues.getCurrentActivity());
                GpsConnection.this.destroy();
            } else if (provider.equals("gps")) {
                GpsConnection.this.enableNetwork();
            }
        }

        public void onProviderEnabled(String provider) {
            LocationState.onProviderEnabled(provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationState.onStatusChanged(provider, status, extras);
        }
    }

    public GpsConnection(Context context) {
        Logger.m26w(TAG, "onCreate()");
        this.locationManager = (LocationManager) context.getSystemService("location");
        List<String> providers = this.locationManager.getAllProviders();
        try {
            this.locationManager.removeUpdates(this.llGPS);
        } catch (Exception e) {
            Logger.m26w(TAG, "problem removing listeners llGPS, e:" + e);
        }
        try {
            this.locationManager.removeUpdates(this.llNetwork);
        } catch (Exception e2) {
            Logger.m26w(TAG, "problem removing listeners llNetwork, e:" + e2);
        }
        if (providers.contains("network")) {
            try {
                this.locationManager.requestLocationUpdates("network", (long) (Preferences.GPS_MIN_TIME * 1000), 0.0f, this.llNetwork);
                this.networkProviderEnabled = true;
            } catch (Exception e22) {
                Logger.m26w(TAG, "problem adding 'network' provider, e:" + e22);
                this.networkProviderEnabled = false;
            }
        }
        if (providers.contains("gps")) {
            try {
                this.locationManager.requestLocationUpdates("gps", (long) (Preferences.GPS_MIN_TIME * 1000), 0.0f, this.llGPS);
                this.gpsProviderEnabled = true;
            } catch (Exception e222) {
                Logger.m26w(TAG, "problem adding 'GPS' provider, e:" + e222);
                this.gpsProviderEnabled = false;
            }
        }
        try {
            this.locationManager.addGpsStatusListener(this.gpsListener);
        } catch (Exception e2222) {
            Logger.m26w(TAG, "problem adding 'GPS status' listener, e:" + e2222);
        }
        if (this.networkProviderEnabled || this.gpsProviderEnabled) {
            ManagerNotify.toastShortMessage(context, context.getString(C0254R.string.gps_enabled));
            return;
        }
        if (PreferenceValues.getCurrentActivity() != null) {
            UtilsGUI.showDialogInfo(PreferenceValues.getCurrentActivity(), (int) C0254R.string.no_location_providers_available);
        }
        LocationState.setGpsOff(context);
        destroy();
    }

    public void destroy() {
        if (this.locationManager != null) {
            disableNetwork();
            this.locationManager.removeUpdates(this.llGPS);
            this.locationManager.removeGpsStatusListener(this.gpsListener);
            this.locationManager = null;
            ManagerNotify.toastShortMessage((int) C0254R.string.gps_disabled);
        }
    }

    private void disableNetwork() {
        if (this.networkProviderEnabled) {
            this.locationManager.removeUpdates(this.llNetwork);
            this.networkProviderEnabled = false;
        }
    }

    private void enableNetwork() {
        if (!this.networkProviderEnabled) {
            try {
                this.locationManager.requestLocationUpdates("network", (long) (Preferences.GPS_MIN_TIME * 1000), 0.0f, this.llNetwork);
                this.networkProviderEnabled = true;
            } catch (Exception e) {
            }
        }
    }

    private synchronized void handleOnLocationChanged(Location location) {
        if (!this.isFixed) {
            if (location.getProvider().equals("gps")) {
                if (Preferences.GPS_BEEP_ON_GPS_FIX) {
                    UtilsAudio.playBeep(1);
                }
                disableNetwork();
                this.isFixed = true;
            }
            LocationState.onLocationChanged(location);
        } else if (location.getProvider().equals("gps")) {
            LocationState.onLocationChanged(location);
            setNewTimer();
        }
    }

    public boolean isProviderEnabled(String provider) {
        return this.locationManager != null && this.locationManager.isProviderEnabled(provider);
    }

    private void setNewTimer() {
        if (this.mGpsTimer != null) {
            this.mGpsTimer.cancel();
        }
        this.mGpsTimer = new Timer();
        this.mGpsTimer.schedule(new C02591(), 60000);
    }
}
