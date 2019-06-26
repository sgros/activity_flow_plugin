package menion.android.whereyougo.geo.location;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.geo.location.Point2D.Int;
import menion.android.whereyougo.gui.activity.SatelliteActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Logger;

public class LocationState {
    private static final int GPS_OFF = 1;
    private static final int GPS_ON = 0;
    private static final String KEY_B_GPS_ENABLE_ASK_ON_ENABLE = "KEY_B_GPS_ENABLE_ASK_ON_ENABLE";
    public static final String SIMULATE_LOCATION = "SIMULATE_LOCATION";
    private static final String TAG = "LocationState";
    private static GpsConnection gpsConn;
    private static int lastSource;
    private static Location location;
    private static long mLastGpsFixTime = 0;
    private static ArrayList<ILocationEventListener> mListeners;
    private static final Int mSatsCount = new Int();
    private static int mSource = 1;
    private static boolean speedCorrection = false;

    /* renamed from: menion.android.whereyougo.geo.location.LocationState$1 */
    static class C02601 implements Comparator<ILocationEventListener> {
        C02601() {
        }

        public int compare(ILocationEventListener object1, ILocationEventListener object2) {
            return object1.getPriority() - object2.getPriority();
        }
    }

    public static synchronized void addLocationChangeListener(ILocationEventListener listener) {
        synchronized (LocationState.class) {
            if (listener != null) {
                if (mListeners != null) {
                    if (!mListeners.contains(listener)) {
                        mListeners.add(listener);
                        if (mListeners.size() > 0) {
                            Collections.sort(mListeners, new C02601());
                        }
                        onScreenOn(true);
                    }
                }
            }
        }
    }

    public static void destroy(Context context) {
        setState(context, 1, false);
        mListeners.clear();
        gpsConn = null;
        location = null;
    }

    public static long getLastFixTime() {
        return mLastGpsFixTime;
    }

    public static Location getLastKnownLocation(Activity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService("location");
        Location gpsLocation = null;
        try {
            gpsLocation = new Location(lm.getLastKnownLocation("gps"));
        } catch (SecurityException e) {
            Logger.m26w(TAG, "Failed to retrieve location: access appears to be disabled.");
        } catch (IllegalArgumentException e2) {
            Logger.m26w(TAG, "Failed to retrieve location: device has no GPS provider.");
        }
        Location networkLocation = null;
        try {
            networkLocation = new Location(lm.getLastKnownLocation("network"));
        } catch (SecurityException e3) {
            Logger.m26w(TAG, "Failed to retrieve location: access appears to be disabled.");
        } catch (IllegalArgumentException e4) {
            Logger.m26w(TAG, "Failed to retrieve location: device has no network provider.");
        }
        if (gpsLocation == null || networkLocation == null) {
            if (gpsLocation != null) {
                return gpsLocation;
            }
            if (networkLocation != null) {
                return networkLocation;
            }
            return null;
        } else if (gpsLocation.getTime() > networkLocation.getTime()) {
            return gpsLocation;
        } else {
            return networkLocation;
        }
    }

    public static Location getLocation() {
        if (location == null) {
            return new Location(TAG);
        }
        return new Location(location);
    }

    public static Int getSatCount() {
        return mSatsCount;
    }

    public static void init(Context c) {
        if (location == null) {
            location = PreferenceValues.getLastKnownLocation();
            mListeners = new ArrayList();
            lastSource = -1;
        }
    }

    public static boolean isActualLocationHardwareGps() {
        return mSource == 0 && location.getProvider().equals("gps");
    }

    public static boolean isActualLocationHardwareNetwork() {
        return mSource == 0 && location.getProvider().equals("network");
    }

    public static boolean isActuallyHardwareGpsOn() {
        return mSource == 0;
    }

    public static boolean isGpsRequired() {
        if (mListeners == null) {
            return false;
        }
        for (int i = 0; i < mListeners.size(); i++) {
            if (((ILocationEventListener) mListeners.get(i)).isRequired()) {
                return true;
            }
        }
        return false;
    }

    public static void onActivityPauseInstant(Context context) {
        boolean disableWhenHide = true;
        try {
            boolean screenOff = C0322A.getApp() != null && ((MainApplication) C0322A.getApp()).isScreenOff();
            if (context == null || !Preferences.GPS_DISABLE_WHEN_HIDE) {
                disableWhenHide = false;
            }
            if (!PreferenceValues.existCurrentActivity() || screenOff) {
                PreferenceValues.disableWakeLock();
            }
            if (!disableWhenHide) {
                return;
            }
            if (mListeners.size() != 0 || PreferenceValues.existCurrentActivity()) {
                if (!screenOff) {
                    if (PreferenceValues.existCurrentActivity()) {
                        lastSource = -1;
                        return;
                    }
                }
                if (isGpsRequired()) {
                    lastSource = -1;
                    return;
                }
                lastSource = mSource;
                setState(context, 1, true);
                return;
            }
            lastSource = mSource;
            setState(context, 1, true);
        } catch (Exception e) {
            Logger.m22e(TAG, "onActivityPauseInstant()", e);
        }
    }

    protected static void onGpsStatusChanged(Hashtable<Integer, SatellitePosition> sats) {
        ArrayList<SatellitePosition> pos = null;
        if (sats != null) {
            pos = new ArrayList();
            Enumeration<SatellitePosition> enuPos = sats.elements();
            mSatsCount.f52x = 0;
            mSatsCount.f53y = 0;
            while (enuPos.hasMoreElements()) {
                Int intR;
                SatellitePosition sat = (SatellitePosition) enuPos.nextElement();
                pos.add(sat);
                if (sat.fixed) {
                    intR = mSatsCount;
                    intR.f52x++;
                }
                intR = mSatsCount;
                intR.f53y++;
            }
        }
        postGpsSatelliteChange(pos);
    }

    static void onGpsStatusChanged(int event, GpsStatus gpsStatus) {
        if (mListeners != null && mListeners.size() != 0) {
            if (event == 1 || event == 2) {
                for (int i = 0; i < mListeners.size(); i++) {
                    int i2;
                    ILocationEventListener iLocationEventListener = (ILocationEventListener) mListeners.get(i);
                    String str = "gps";
                    if (event == 1) {
                        i2 = 2;
                    } else {
                        i2 = 1;
                    }
                    iLocationEventListener.onStatusChanged(str, i2, null);
                }
            } else if (event == 4) {
                ArrayList<SatellitePosition> pos = null;
                if (gpsStatus != null) {
                    pos = new ArrayList();
                    mSatsCount.f52x = 0;
                    mSatsCount.f53y = 0;
                    for (GpsSatellite sat : gpsStatus.getSatellites()) {
                        Int intR;
                        SatellitePosition satPos = new SatellitePosition();
                        satPos.azimuth = sat.getAzimuth();
                        satPos.elevation = sat.getElevation();
                        satPos.prn = Integer.valueOf(sat.getPrn());
                        satPos.snr = (int) sat.getSnr();
                        satPos.fixed = sat.usedInFix();
                        if (satPos.fixed) {
                            intR = mSatsCount;
                            intR.f52x++;
                        }
                        intR = mSatsCount;
                        intR.f53y++;
                        pos.add(satPos);
                    }
                }
                postGpsSatelliteChange(pos);
            }
        }
    }

    static void onLocationChanged(Location location) {
        if (location != null) {
            try {
                if (location != null) {
                    if (!location.getProvider().equals("network") || !location.getProvider().equals("gps") || location.getAccuracy() * 3.0f >= location.getAccuracy()) {
                        if (speedCorrection || location.getTime() - location.getTime() >= 5000 || location.getSpeed() <= 100.0f || location.getSpeed() / location.getSpeed() <= 2.0f) {
                            speedCorrection = false;
                        } else {
                            location.setSpeed(location.getSpeed());
                            speedCorrection = true;
                        }
                        if (location.getProvider().equals("gps")) {
                            mLastGpsFixTime = System.currentTimeMillis();
                        }
                        if (location.getSpeed() < 0.5f && ((double) Math.abs(location.getBearing() - location.getBearing())) > 25.0d) {
                            location.setBearing(location.getBearing());
                        }
                    } else {
                        return;
                    }
                }
                if (location.getProvider().equals("gps")) {
                    location.setAltitude(location.getAltitude() + Preferences.GPS_ALTITUDE_CORRECTION);
                }
                location = location;
                for (int i = 0; i < mListeners.size(); i++) {
                    ((ILocationEventListener) mListeners.get(i)).onLocationChanged(location);
                }
            } catch (Exception e) {
                Logger.m22e(TAG, "onLocationChanged(" + location + ")", e);
            }
        }
    }

    static void onProviderDisabled(String provider) {
    }

    static void onProviderEnabled(String provider) {
        Logger.m26w(TAG, "onProviderEnabled(" + provider + ")");
    }

    public static void onScreenOn(boolean force) {
        if (lastSource != -1 && mListeners != null && mListeners.size() > 0) {
            if (PreferenceValues.existCurrentActivity() || force) {
                setState(PreferenceValues.getCurrentActivity(), lastSource, true);
                lastSource = -1;
            }
        }
    }

    static void onStatusChanged(String provider, int status, Bundle extras) {
        Logger.m26w(TAG, "onStatusChanged(" + provider + ", " + status + ", " + extras + ")");
        for (int i = 0; i < mListeners.size(); i++) {
            ((ILocationEventListener) mListeners.get(i)).onStatusChanged(provider, status, extras);
        }
        if (provider.equals("gps") && status == 1 && location != null) {
            location.setProvider("network");
            onLocationChanged(location);
        }
    }

    private static void postGpsSatelliteChange(final ArrayList<SatellitePosition> pos) {
        if (PreferenceValues.getCurrentActivity() != null) {
            PreferenceValues.getCurrentActivity().runOnUiThread(new Runnable() {
                public void run() {
                    for (int i = 0; i < LocationState.mListeners.size(); i++) {
                        ((ILocationEventListener) LocationState.mListeners.get(i)).onGpsStatusChanged(4, pos);
                    }
                }
            });
        }
    }

    public static synchronized void removeLocationChangeListener(ILocationEventListener listener) {
        synchronized (LocationState.class) {
            if (!(mListeners.size() == 0 || listener == null)) {
                if (mListeners.contains(listener)) {
                    mListeners.remove(listener);
                    Logger.m24i(TAG, "removeLocationChangeListener(" + listener + "), actualSize:" + mListeners.size());
                }
            }
        }
    }

    public static void setGpsOff(Context context) {
        setState(context, 1, true);
    }

    public static void setGpsOn(Context context) {
        if (mSource == 0) {
            setGpsOff(context);
        }
        setState(context, 0, true);
    }

    public static void setLocationDirectly(Location location) {
        if (!(Const.STATE_RELEASE || isActuallyHardwareGpsOn())) {
            location.setSpeed(20.0f);
            if (location != null) {
                location.setBearing(location.bearingTo(location));
            }
        }
        onLocationChanged(location);
    }

    private static void setState(final Context context, int source, boolean writeToSettings) {
        if (mSource != source) {
            if (writeToSettings && context != null) {
                Preferences.GPS = source == 0;
                Preferences.setPreference((int) C0254R.string.pref_KEY_B_GPS, Preferences.GPS);
            }
            if (source != 0 || context == null) {
                mSource = 1;
                onGpsStatusChanged(2, null);
                if (gpsConn != null) {
                    gpsConn.destroy();
                    gpsConn = null;
                }
            } else {
                mSource = 0;
                if (gpsConn != null) {
                    gpsConn.destroy();
                    gpsConn = null;
                }
                boolean gpsNotEnabled = false;
                String provider = Secure.getString(context.getContentResolver(), "location_providers_allowed");
                if (provider == null || !(provider.contains("network") || provider.contains("gps"))) {
                    UtilsGUI.showDialogQuestion(PreferenceValues.getCurrentActivity(), (int) C0254R.string.gps_not_enabled_show_system_settings, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                        }
                    }, null);
                    gpsNotEnabled = true;
                } else {
                    gpsConn = new GpsConnection(context);
                }
                if (gpsNotEnabled) {
                    if (context instanceof SatelliteActivity) {
                        ((SatelliteActivity) context).notifyGpsDisable();
                    }
                    setState(context, 1, true);
                }
            }
            onLocationChanged(location);
        }
    }
}
