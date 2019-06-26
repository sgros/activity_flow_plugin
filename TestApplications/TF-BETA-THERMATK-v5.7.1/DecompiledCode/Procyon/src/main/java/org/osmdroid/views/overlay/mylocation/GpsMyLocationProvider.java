// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay.mylocation;

import android.annotation.SuppressLint;
import java.util.Iterator;
import android.os.Bundle;
import android.util.Log;
import java.util.HashSet;
import android.content.Context;
import android.location.LocationManager;
import android.location.Location;
import org.osmdroid.util.NetworkLocationIgnorer;
import java.util.Set;
import android.location.LocationListener;

public class GpsMyLocationProvider implements IMyLocationProvider, LocationListener
{
    private final Set<String> locationSources;
    private NetworkLocationIgnorer mIgnorer;
    private Location mLocation;
    private LocationManager mLocationManager;
    private float mLocationUpdateMinDistance;
    private long mLocationUpdateMinTime;
    private IMyLocationConsumer mMyLocationConsumer;
    
    public GpsMyLocationProvider(final Context context) {
        this.mLocationUpdateMinTime = 0L;
        this.mLocationUpdateMinDistance = 0.0f;
        this.mIgnorer = new NetworkLocationIgnorer();
        this.locationSources = new HashSet<String>();
        this.mLocationManager = (LocationManager)context.getSystemService("location");
        this.locationSources.add("gps");
        this.locationSources.add("network");
    }
    
    public void addLocationSource(final String s) {
        this.locationSources.add(s);
    }
    
    @Override
    public void destroy() {
        this.stopLocationProvider();
        this.mLocation = null;
        this.mLocationManager = null;
        this.mMyLocationConsumer = null;
        this.mIgnorer = null;
    }
    
    @Override
    public Location getLastKnownLocation() {
        return this.mLocation;
    }
    
    public void onLocationChanged(final Location mLocation) {
        if (this.mIgnorer == null) {
            Log.w("OsmDroid", "GpsMyLocation provider, mIgnore is null, unexpected. Location update will be ignored");
            return;
        }
        if (mLocation != null) {
            if (mLocation.getProvider() != null) {
                if (this.mIgnorer.shouldIgnore(mLocation.getProvider(), System.currentTimeMillis())) {
                    return;
                }
                this.mLocation = mLocation;
                final IMyLocationConsumer mMyLocationConsumer = this.mMyLocationConsumer;
                if (mMyLocationConsumer != null) {
                    final Location mLocation2 = this.mLocation;
                    if (mLocation2 != null) {
                        mMyLocationConsumer.onLocationChanged(mLocation2, this);
                    }
                }
            }
        }
    }
    
    public void onProviderDisabled(final String s) {
    }
    
    public void onProviderEnabled(final String s) {
    }
    
    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
    }
    
    public void setLocationUpdateMinDistance(final float mLocationUpdateMinDistance) {
        this.mLocationUpdateMinDistance = mLocationUpdateMinDistance;
    }
    
    public void setLocationUpdateMinTime(final long mLocationUpdateMinTime) {
        this.mLocationUpdateMinTime = mLocationUpdateMinTime;
    }
    
    @SuppressLint({ "MissingPermission" })
    @Override
    public boolean startLocationProvider(final IMyLocationConsumer mMyLocationConsumer) {
        this.mMyLocationConsumer = mMyLocationConsumer;
        final Iterator<String> iterator = (Iterator<String>)this.mLocationManager.getProviders(true).iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final String str = iterator.next();
            if (this.locationSources.contains(str)) {
                try {
                    this.mLocationManager.requestLocationUpdates(str, this.mLocationUpdateMinTime, this.mLocationUpdateMinDistance, (LocationListener)this);
                    b = true;
                }
                catch (Throwable t) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to attach listener for location provider ");
                    sb.append(str);
                    sb.append(" check permissions?");
                    Log.e("OsmDroid", sb.toString(), t);
                }
            }
        }
        return b;
    }
    
    @SuppressLint({ "MissingPermission" })
    @Override
    public void stopLocationProvider() {
        this.mMyLocationConsumer = null;
        final LocationManager mLocationManager = this.mLocationManager;
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates((LocationListener)this);
            }
            catch (Throwable t) {
                Log.w("OsmDroid", "Unable to deattach location listener", t);
            }
        }
    }
}
