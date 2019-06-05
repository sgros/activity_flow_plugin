// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.geo.orientation;

import android.os.Bundle;
import android.hardware.SensorEvent;
import menion.android.whereyougo.geo.location.SatellitePosition;
import java.util.ArrayList;
import android.hardware.Sensor;
import menion.android.whereyougo.utils.A;
import java.util.Iterator;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.geo.location.LocationState;
import android.hardware.SensorManager;
import java.util.Vector;
import android.hardware.GeomagneticField;
import menion.android.whereyougo.geo.location.ILocationEventListener;
import android.hardware.SensorEventListener;

public class Orientation implements SensorEventListener, ILocationEventListener
{
    private static final String TAG = "Orientation";
    private static float aboveOrBelow;
    private static GeomagneticField gmf;
    private static long lastCompute;
    private static float orient;
    private static float pitch;
    private static float roll;
    private final Vector<IOrientationEventListener> listeners;
    private float mLastAziGps;
    private float mLastAziSensor;
    private float mLastPitch;
    private float mLastRoll;
    private SensorManager sensorManager;
    
    static {
        Orientation.aboveOrBelow = 0.0f;
    }
    
    public Orientation() {
        this.listeners = new Vector<IOrientationEventListener>();
    }
    
    private float filterValue(final float n, float filter) {
        float n2;
        if (n < filter - 180.0f) {
            n2 = filter - 360.0f;
        }
        else {
            n2 = filter;
            if (n > filter + 180.0f) {
                n2 = filter + 360.0f;
            }
        }
        filter = this.getFilter();
        return (float)(n * filter + n2 * (1.0 - filter));
    }
    
    public static float getDeclination() {
        final long currentTimeMillis = System.currentTimeMillis();
        if (Orientation.gmf == null || currentTimeMillis - Orientation.lastCompute > 300000L) {
            final Location location = LocationState.getLocation();
            Orientation.gmf = new GeomagneticField((float)location.getLatitude(), (float)location.getLongitude(), (float)location.getAltitude(), currentTimeMillis);
            Orientation.lastCompute = currentTimeMillis;
            Logger.w("Orientation", "getDeclination() - dec:" + Orientation.gmf.getDeclination());
        }
        return Orientation.gmf.getDeclination();
    }
    
    private float getFilter() {
        float n = 0.0f;
        switch (Preferences.SENSOR_ORIENT_FILTER) {
            default: {
                n = 1.0f;
                break;
            }
            case 1: {
                n = 0.2f;
                break;
            }
            case 2: {
                n = 0.06f;
                break;
            }
            case 3: {
                n = 0.03f;
                break;
            }
        }
        return n;
    }
    
    private void sendOrientation(final float mLastPitch, final float mLastRoll) {
        float n;
        if (!Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE || LocationState.getLocation().getSpeed() < Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE_VALUE) {
            if (!Preferences.SENSOR_HARDWARE_COMPASS) {
                n = this.mLastAziGps;
            }
            else {
                n = this.mLastAziSensor;
            }
        }
        else {
            n = this.mLastAziGps;
        }
        this.mLastPitch = mLastPitch;
        this.mLastRoll = mLastRoll;
        final Iterator<IOrientationEventListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onOrientationChanged(n, this.mLastPitch, this.mLastRoll);
        }
    }
    
    public void addListener(final IOrientationEventListener obj) {
        if (!this.listeners.contains(obj)) {
            this.listeners.add(obj);
            Logger.i("Orientation", "addListener(" + obj + "), listeners.size():" + this.listeners.size());
            this.manageSensors();
        }
    }
    
    public String getName() {
        return "Orientation";
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
            this.sendOrientation(this.mLastAziSensor = 0.0f, 0.0f);
            this.sensorManager.unregisterListener((SensorEventListener)this);
            this.sensorManager = null;
            LocationState.removeLocationChangeListener(this);
        }
        if (this.listeners.size() > 0) {
            if (this.sensorManager == null) {
                (this.sensorManager = (SensorManager)A.getMain().getSystemService("sensor")).registerListener((SensorEventListener)this, this.sensorManager.getDefaultSensor(3), 1);
                this.sensorManager.registerListener((SensorEventListener)this, this.sensorManager.getDefaultSensor(1), 1);
            }
            if (!Preferences.SENSOR_HARDWARE_COMPASS || Preferences.SENSOR_HARDWARE_COMPASS_AUTO_CHANGE) {
                LocationState.addLocationChangeListener(this);
                this.mLastAziGps = 0.0f;
            }
        }
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public void onGpsStatusChanged(final int n, final ArrayList<SatellitePosition> list) {
    }
    
    public void onLocationChanged(final Location location) {
        if (location.getBearing() != 0.0f) {
            this.mLastAziGps = location.getBearing();
            this.sendOrientation(this.mLastPitch, this.mLastRoll);
        }
    }
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case 1: {
                final float filter = this.getFilter();
                Orientation.aboveOrBelow = (float)(sensorEvent.values[2] * filter + Orientation.aboveOrBelow * (1.0 - filter));
                break;
            }
            case 3: {
                float n = sensorEvent.values[0];
                if (Preferences.SENSOR_BEARING_TRUE) {
                    n += getDeclination();
                }
                Orientation.orient = this.filterValue(n, Orientation.orient);
                Orientation.pitch = this.filterValue(sensorEvent.values[1], Orientation.pitch);
                Orientation.roll = this.filterValue(sensorEvent.values[2], Orientation.roll);
                float roll;
                if (Orientation.aboveOrBelow < 0.0f) {
                    if (Orientation.roll < 0.0f) {
                        roll = -180.0f - Orientation.roll;
                    }
                    else {
                        roll = 180.0f - Orientation.roll;
                    }
                }
                else {
                    roll = Orientation.roll;
                }
                this.mLastAziSensor = Orientation.orient;
                switch (A.getMain().getWindowManager().getDefaultDisplay().getRotation()) {
                    case 1: {
                        this.mLastAziSensor += 90.0f;
                        break;
                    }
                    case 2: {
                        this.mLastAziSensor -= 180.0f;
                        break;
                    }
                    case 3: {
                        this.mLastAziSensor -= 90.0f;
                        break;
                    }
                }
                this.sendOrientation(Orientation.pitch, roll);
                break;
            }
        }
    }
    
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
    
    public void removeAllListeners() {
        this.listeners.clear();
        this.manageSensors();
    }
    
    public void removeListener(final IOrientationEventListener obj) {
        if (this.listeners.contains(obj)) {
            this.listeners.remove(obj);
            Logger.i("Orientation", "removeListener(" + obj + "), listeners.size():" + this.listeners.size());
            this.manageSensors();
        }
    }
}
