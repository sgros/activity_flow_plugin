// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.overlay;

import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.graphics.Paint;
import org.mapsforge.android.maps.overlay.Marker;
import android.content.Context;
import android.view.WindowManager;
import android.hardware.SensorManager;
import org.mapsforge.android.maps.MapView;
import android.hardware.SensorEventListener;

public class SensorMyLocationOverlay extends MyLocationOverlay implements SensorEventListener
{
    private static final float UPDATE_AZIMUTH = 5.0f;
    private static final int UPDATE_INTERVAL = 100;
    float currentCompassAzimuth;
    float filter;
    float lastCompassAzimuth;
    long lastCompassTimestamp;
    float lastGPSAzimuth;
    long lastGPSTimestamp;
    final MapView mapView;
    final RotationMarker marker;
    final SensorManager sensorManager;
    final WindowManager windowManager;
    
    public SensorMyLocationOverlay(final Context context, final MapView mapView, final RotationMarker marker) {
        super(context, mapView, marker);
        this.filter = 0.15f;
        this.windowManager = (WindowManager)context.getSystemService("window");
        this.sensorManager = (SensorManager)context.getSystemService("sensor");
        this.marker = marker;
        this.mapView = mapView;
    }
    
    public SensorMyLocationOverlay(final Context context, final MapView mapView, final RotationMarker marker, final Paint paint, final Paint paint2) {
        super(context, mapView, marker, paint, paint2);
        this.filter = 0.15f;
        this.windowManager = (WindowManager)context.getSystemService("window");
        this.sensorManager = (SensorManager)context.getSystemService("sensor");
        this.marker = marker;
        this.mapView = mapView;
    }
    
    private float filterValue(final float n, final float n2) {
        float n3;
        if (n < n2 - 180.0f) {
            n3 = n2 - 360.0f;
        }
        else {
            n3 = n2;
            if (n > n2 + 180.0f) {
                n3 = n2 + 360.0f;
            }
        }
        return this.filter * n + (1.0f - this.filter) * n3;
    }
    
    private void setSensor(final boolean b) {
        if (b) {
            this.setSensor(false);
            this.sensorManager.registerListener((SensorEventListener)this, this.sensorManager.getDefaultSensor(3), 2);
        }
        else {
            this.sensorManager.unregisterListener((SensorEventListener)this);
        }
    }
    
    @Override
    public void disableMyLocation() {
        synchronized (this) {
            if (this.isMyLocationEnabled()) {
                this.setSensor(false);
            }
            super.disableMyLocation();
        }
    }
    
    @Override
    public boolean enableMyLocation(final boolean b) {
        final boolean b2 = true;
        synchronized (this) {
            boolean b3;
            if (super.enableMyLocation(b)) {
                this.setSensor(true);
                b3 = b2;
            }
            else {
                b3 = false;
            }
            return b3;
        }
    }
    
    protected int getRotationOffset() {
        int n = 0;
        switch (this.windowManager.getDefaultDisplay().getRotation()) {
            default: {
                n = 0;
                break;
            }
            case 1: {
                n = 90;
                break;
            }
            case 2: {
                n = 180;
                break;
            }
            case 3: {
                n = 270;
                break;
            }
        }
        return n;
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    @Override
    public void onProviderDisabled(final String s) {
        super.onProviderDisabled(s);
        if (!super.isMyLocationEnabled()) {
            this.setSensor(false);
        }
    }
    
    @Override
    public void onProviderEnabled(final String s) {
        super.onProviderEnabled(s);
        if (!super.isMyLocationEnabled()) {
            this.setSensor(false);
        }
    }
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != 3) {
            return;
        }
        final boolean b = false;
        synchronized (this) {
            final long lastCompassTimestamp = sensorEvent.timestamp / 1000000L;
            final float filterValue = this.filterValue((sensorEvent.values[0] + this.getRotationOffset() + 360.0f) % 360.0f, this.currentCompassAzimuth);
            this.currentCompassAzimuth = filterValue;
            this.marker.setRotation(filterValue);
            int n = b ? 1 : 0;
            if (Math.abs(lastCompassTimestamp - this.lastCompassTimestamp) >= 100L) {
                n = (b ? 1 : 0);
                if (Math.abs(filterValue - this.lastCompassAzimuth) >= 5.0f) {
                    this.lastCompassTimestamp = lastCompassTimestamp;
                    this.lastCompassAzimuth = filterValue;
                    n = 1;
                }
            }
            // monitorexit(this)
            if (n != 0) {
                this.mapView.getOverlayController().redrawOverlays();
            }
        }
    }
}
