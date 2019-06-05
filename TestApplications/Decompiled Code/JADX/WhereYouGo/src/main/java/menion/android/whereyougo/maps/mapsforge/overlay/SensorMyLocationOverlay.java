package menion.android.whereyougo.maps.mapsforge.overlay;

import android.content.Context;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import org.mapsforge.android.maps.MapView;

public class SensorMyLocationOverlay extends MyLocationOverlay implements SensorEventListener {
    private static final float UPDATE_AZIMUTH = 5.0f;
    private static final int UPDATE_INTERVAL = 100;
    float currentCompassAzimuth;
    float filter = 0.15f;
    float lastCompassAzimuth;
    long lastCompassTimestamp;
    float lastGPSAzimuth;
    long lastGPSTimestamp;
    final MapView mapView;
    final RotationMarker marker;
    final SensorManager sensorManager;
    final WindowManager windowManager;

    public SensorMyLocationOverlay(Context context, MapView mapView, RotationMarker marker) {
        super(context, mapView, marker);
        this.windowManager = (WindowManager) context.getSystemService("window");
        this.sensorManager = (SensorManager) context.getSystemService("sensor");
        this.marker = marker;
        this.mapView = mapView;
    }

    public SensorMyLocationOverlay(Context context, MapView mapView, RotationMarker marker, Paint circleFill, Paint circleStroke) {
        super(context, mapView, marker, circleFill, circleStroke);
        this.windowManager = (WindowManager) context.getSystemService("window");
        this.sensorManager = (SensorManager) context.getSystemService("sensor");
        this.marker = marker;
        this.mapView = mapView;
    }

    public synchronized void disableMyLocation() {
        if (isMyLocationEnabled()) {
            setSensor(false);
        }
        super.disableMyLocation();
    }

    public synchronized boolean enableMyLocation(boolean centerAtFirstFix) {
        boolean z = true;
        synchronized (this) {
            if (super.enableMyLocation(centerAtFirstFix)) {
                setSensor(true);
            } else {
                z = false;
            }
        }
        return z;
    }

    private float filterValue(float current, float last) {
        if (current < last - 180.0f) {
            last -= 360.0f;
        } else if (current > last + 180.0f) {
            last += 360.0f;
        }
        return (this.filter * current) + ((1.0f - this.filter) * last);
    }

    /* Access modifiers changed, original: protected */
    public int getRotationOffset() {
        switch (this.windowManager.getDefaultDisplay().getRotation()) {
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
            default:
                return 0;
        }
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    public void onProviderDisabled(String provider) {
        super.onProviderDisabled(provider);
        if (!super.isMyLocationEnabled()) {
            setSensor(false);
        }
    }

    public void onProviderEnabled(String provider) {
        super.onProviderEnabled(provider);
        if (!super.isMyLocationEnabled()) {
            setSensor(false);
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 3) {
            boolean redraw = false;
            synchronized (this) {
                long timestamp = event.timestamp / 1000000;
                float azimuth = filterValue(((event.values[0] + ((float) getRotationOffset())) + 360.0f) % 360.0f, this.currentCompassAzimuth);
                this.currentCompassAzimuth = azimuth;
                this.marker.setRotation(azimuth);
                if (Math.abs(timestamp - this.lastCompassTimestamp) >= 100 && Math.abs(azimuth - this.lastCompassAzimuth) >= UPDATE_AZIMUTH) {
                    this.lastCompassTimestamp = timestamp;
                    this.lastCompassAzimuth = azimuth;
                    redraw = true;
                }
            }
            if (redraw) {
                this.mapView.getOverlayController().redrawOverlays();
            }
        }
    }

    private void setSensor(boolean state) {
        if (state) {
            setSensor(false);
            this.sensorManager.registerListener(this, this.sensorManager.getDefaultSensor(3), 2);
            return;
        }
        this.sensorManager.unregisterListener(this);
    }
}
