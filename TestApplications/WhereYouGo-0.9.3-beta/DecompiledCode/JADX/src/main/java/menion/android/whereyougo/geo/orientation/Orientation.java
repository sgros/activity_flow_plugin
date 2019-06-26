package menion.android.whereyougo.geo.orientation;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.SatellitePosition;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;

public class Orientation implements SensorEventListener, ILocationEventListener {
    private static final String TAG = "Orientation";
    private static float aboveOrBelow = 0.0f;
    private static GeomagneticField gmf;
    private static long lastCompute;
    private static float orient;
    private static float pitch;
    private static float roll;
    private final Vector<IOrientationEventListener> listeners = new Vector();
    private float mLastAziGps;
    private float mLastAziSensor;
    private float mLastPitch;
    private float mLastRoll;
    private SensorManager sensorManager;

    public static float getDeclination() {
        long actualTime = System.currentTimeMillis();
        if (gmf == null || actualTime - lastCompute > 300000) {
            Location loc = LocationState.getLocation();
            gmf = new GeomagneticField((float) loc.getLatitude(), (float) loc.getLongitude(), (float) loc.getAltitude(), actualTime);
            lastCompute = actualTime;
            Logger.m26w(TAG, "getDeclination() - dec:" + gmf.getDeclination());
        }
        return gmf.getDeclination();
    }

    public void addListener(IOrientationEventListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
            Logger.m24i(TAG, "addListener(" + listener + "), listeners.size():" + this.listeners.size());
            manageSensors();
        }
    }

    private float filterValue(float valueActual, float valueLast) {
        if (valueActual < valueLast - 180.0f) {
            valueLast -= 360.0f;
        } else if (valueActual > valueLast + 180.0f) {
            valueLast += 360.0f;
        }
        float filter = getFilter();
        return (float) (((double) (valueActual * filter)) + (((double) valueLast) * (1.0d - ((double) filter))));
    }

    private float getFilter() {
        switch (Preferences.SENSOR_ORIENT_FILTER) {
            case 1:
                return 0.2f;
            case 2:
                return 0.06f;
            case 3:
                return 0.03f;
            default:
                return 1.0f;
        }
    }

    public String getName() {
        return TAG;
    }

    public int getPriority() {
        return 2;
    }

    public boolean isRequired() {
        return false;
    }

    public void manageSensors() {
        if (this.sensorManager != null) {
            this.mLastAziGps = 0.0f;
            this.mLastAziSensor = 0.0f;
            sendOrientation(0.0f, 0.0f);
            this.sensorManager.unregisterListener(this);
            this.sensorManager = null;
            LocationState.removeLocationChangeListener(this);
        }
        if (this.listeners.size() > 0) {
            if (this.sensorManager == null) {
                this.sensorManager = (SensorManager) C0322A.getMain().getSystemService("sensor");
                this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(3), 1);
                this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(1), 1);
            }
            if (!Preferences.SENSOR_HARDWARE_COMPASS || Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE) {
                LocationState.addLocationChangeListener(this);
                this.mLastAziGps = 0.0f;
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onGpsStatusChanged(int event, ArrayList<SatellitePosition> arrayList) {
    }

    public void onLocationChanged(Location location) {
        if (location.getBearing() != 0.0f) {
            this.mLastAziGps = location.getBearing();
            sendOrientation(this.mLastPitch, this.mLastRoll);
        }
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case 1:
                float filter = getFilter();
                aboveOrBelow = (float) (((double) (event.values[2] * filter)) + (((double) aboveOrBelow) * (1.0d - ((double) filter))));
                return;
            case 3:
                float rollDef;
                float valueOr = event.values[0];
                if (Preferences.SENSOR_BEARING_TRUE) {
                    valueOr += getDeclination();
                }
                orient = filterValue(valueOr, orient);
                pitch = filterValue(event.values[1], pitch);
                roll = filterValue(event.values[2], roll);
                if (aboveOrBelow >= 0.0f) {
                    rollDef = roll;
                } else if (roll < 0.0f) {
                    rollDef = -180.0f - roll;
                } else {
                    rollDef = 180.0f - roll;
                }
                this.mLastAziSensor = orient;
                switch (C0322A.getMain().getWindowManager().getDefaultDisplay().getRotation()) {
                    case 1:
                        this.mLastAziSensor += 90.0f;
                        break;
                    case 2:
                        this.mLastAziSensor -= 180.0f;
                        break;
                    case 3:
                        this.mLastAziSensor -= 90.0f;
                        break;
                }
                sendOrientation(pitch, rollDef);
                return;
            default:
                return;
        }
    }

    public void onStatusChanged(String provider, int state, Bundle extras) {
    }

    public void removeAllListeners() {
        this.listeners.clear();
        manageSensors();
    }

    public void removeListener(IOrientationEventListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
            Logger.m24i(TAG, "removeListener(" + listener + "), listeners.size():" + this.listeners.size());
            manageSensors();
        }
    }

    private void sendOrientation(float pitch, float roll) {
        float usedOrient;
        if (Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE && LocationState.getLocation().getSpeed() >= ((float) Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE)) {
            usedOrient = this.mLastAziGps;
        } else if (Preferences.SENSOR_HARDWARE_COMPASS) {
            usedOrient = this.mLastAziSensor;
        } else {
            usedOrient = this.mLastAziGps;
        }
        this.mLastPitch = pitch;
        this.mLastRoll = roll;
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((IOrientationEventListener) it.next()).onOrientationChanged(usedOrient, this.mLastPitch, this.mLastRoll);
        }
    }
}
