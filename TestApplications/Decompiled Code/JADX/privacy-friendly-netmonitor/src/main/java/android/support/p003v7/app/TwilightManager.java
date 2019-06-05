package android.support.p003v7.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.p000v4.content.PermissionChecker;
import android.util.Log;
import java.util.Calendar;

/* renamed from: android.support.v7.app.TwilightManager */
class TwilightManager {
    private static final int SUNRISE = 6;
    private static final int SUNSET = 22;
    private static final String TAG = "TwilightManager";
    private static TwilightManager sInstance;
    private final Context mContext;
    private final LocationManager mLocationManager;
    private final TwilightState mTwilightState = new TwilightState();

    /* renamed from: android.support.v7.app.TwilightManager$TwilightState */
    private static class TwilightState {
        boolean isNight;
        long nextUpdate;
        long todaySunrise;
        long todaySunset;
        long tomorrowSunrise;
        long yesterdaySunset;

        TwilightState() {
        }
    }

    static TwilightManager getInstance(@NonNull Context context) {
        if (sInstance == null) {
            context = context.getApplicationContext();
            sInstance = new TwilightManager(context, (LocationManager) context.getSystemService("location"));
        }
        return sInstance;
    }

    @VisibleForTesting
    static void setInstance(TwilightManager twilightManager) {
        sInstance = twilightManager;
    }

    @VisibleForTesting
    TwilightManager(@NonNull Context context, @NonNull LocationManager locationManager) {
        this.mContext = context;
        this.mLocationManager = locationManager;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isNight() {
        TwilightState twilightState = this.mTwilightState;
        if (isStateValid()) {
            return twilightState.isNight;
        }
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            updateState(lastKnownLocation);
            return twilightState.isNight;
        }
        Log.i(TAG, "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
        int i = Calendar.getInstance().get(11);
        boolean z = i < 6 || i >= 22;
        return z;
    }

    private Location getLastKnownLocation() {
        Location location = null;
        Location lastKnownLocationForProvider = PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") == 0 ? getLastKnownLocationForProvider("network") : null;
        if (PermissionChecker.checkSelfPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            location = getLastKnownLocationForProvider("gps");
        }
        if (location == null || lastKnownLocationForProvider == null) {
            if (location != null) {
                lastKnownLocationForProvider = location;
            }
            return lastKnownLocationForProvider;
        }
        if (location.getTime() > lastKnownLocationForProvider.getTime()) {
            lastKnownLocationForProvider = location;
        }
        return lastKnownLocationForProvider;
    }

    private Location getLastKnownLocationForProvider(String str) {
        if (this.mLocationManager != null) {
            try {
                if (this.mLocationManager.isProviderEnabled(str)) {
                    return this.mLocationManager.getLastKnownLocation(str);
                }
            } catch (Exception e) {
                Log.d(TAG, "Failed to get last known location", e);
            }
        }
        return null;
    }

    private boolean isStateValid() {
        return this.mTwilightState != null && this.mTwilightState.nextUpdate > System.currentTimeMillis();
    }

    private void updateState(@NonNull Location location) {
        long j;
        TwilightState twilightState = this.mTwilightState;
        long currentTimeMillis = System.currentTimeMillis();
        TwilightCalculator instance = TwilightCalculator.getInstance();
        TwilightCalculator twilightCalculator = instance;
        twilightCalculator.calculateTwilight(currentTimeMillis - 86400000, location.getLatitude(), location.getLongitude());
        long j2 = instance.sunset;
        twilightCalculator.calculateTwilight(currentTimeMillis, location.getLatitude(), location.getLongitude());
        boolean z = true;
        if (instance.state != 1) {
            z = false;
        }
        boolean z2 = z;
        long j3 = instance.sunrise;
        long j4 = j2;
        j2 = instance.sunset;
        TwilightState twilightState2 = twilightState;
        long j5 = j3;
        boolean z3 = z2;
        instance.calculateTwilight(currentTimeMillis + 86400000, location.getLatitude(), location.getLongitude());
        long j6 = instance.sunrise;
        if (j5 == -1 || j2 == -1) {
            j = currentTimeMillis + 43200000;
        } else {
            j = currentTimeMillis > j2 ? 0 + j6 : currentTimeMillis > j5 ? 0 + j2 : 0 + j5;
            j += 60000;
        }
        TwilightState twilightState3 = twilightState2;
        twilightState3.isNight = z3;
        twilightState3.yesterdaySunset = j4;
        twilightState3.todaySunrise = j5;
        twilightState3.todaySunset = j2;
        twilightState3.tomorrowSunrise = j6;
        twilightState3.nextUpdate = j;
    }
}
