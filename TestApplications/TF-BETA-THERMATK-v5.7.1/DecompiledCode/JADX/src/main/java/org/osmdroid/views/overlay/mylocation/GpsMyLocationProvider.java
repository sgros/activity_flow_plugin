package org.osmdroid.views.overlay.mylocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.HashSet;
import java.util.Set;
import org.osmdroid.util.NetworkLocationIgnorer;

public class GpsMyLocationProvider implements IMyLocationProvider, LocationListener {
    private final Set<String> locationSources = new HashSet();
    private NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
    private Location mLocation;
    private LocationManager mLocationManager;
    private float mLocationUpdateMinDistance = 0.0f;
    private long mLocationUpdateMinTime = 0;
    private IMyLocationConsumer mMyLocationConsumer;

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public GpsMyLocationProvider(Context context) {
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        this.locationSources.add("gps");
        this.locationSources.add("network");
    }

    public void addLocationSource(String str) {
        this.locationSources.add(str);
    }

    public void setLocationUpdateMinTime(long j) {
        this.mLocationUpdateMinTime = j;
    }

    public void setLocationUpdateMinDistance(float f) {
        this.mLocationUpdateMinDistance = f;
    }

    @SuppressLint({"MissingPermission"})
    public boolean startLocationProvider(IMyLocationConsumer iMyLocationConsumer) {
        this.mMyLocationConsumer = iMyLocationConsumer;
        boolean z = false;
        for (String str : this.mLocationManager.getProviders(true)) {
            if (this.locationSources.contains(str)) {
                try {
                    this.mLocationManager.requestLocationUpdates(str, this.mLocationUpdateMinTime, this.mLocationUpdateMinDistance, this);
                    z = true;
                } catch (Throwable th) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to attach listener for location provider ");
                    stringBuilder.append(str);
                    stringBuilder.append(" check permissions?");
                    Log.e("OsmDroid", stringBuilder.toString(), th);
                }
            }
        }
        return z;
    }

    @SuppressLint({"MissingPermission"})
    public void stopLocationProvider() {
        this.mMyLocationConsumer = null;
        LocationManager locationManager = this.mLocationManager;
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(this);
            } catch (Throwable th) {
                Log.w("OsmDroid", "Unable to deattach location listener", th);
            }
        }
    }

    public Location getLastKnownLocation() {
        return this.mLocation;
    }

    public void destroy() {
        stopLocationProvider();
        this.mLocation = null;
        this.mLocationManager = null;
        this.mMyLocationConsumer = null;
        this.mIgnorer = null;
    }

    public void onLocationChanged(Location location) {
        if (this.mIgnorer == null) {
            Log.w("OsmDroid", "GpsMyLocation provider, mIgnore is null, unexpected. Location update will be ignored");
        } else if (location != null && location.getProvider() != null && !this.mIgnorer.shouldIgnore(location.getProvider(), System.currentTimeMillis())) {
            this.mLocation = location;
            IMyLocationConsumer iMyLocationConsumer = this.mMyLocationConsumer;
            if (iMyLocationConsumer != null) {
                Location location2 = this.mLocation;
                if (location2 != null) {
                    iMyLocationConsumer.onLocationChanged(location2, this);
                }
            }
        }
    }
}
