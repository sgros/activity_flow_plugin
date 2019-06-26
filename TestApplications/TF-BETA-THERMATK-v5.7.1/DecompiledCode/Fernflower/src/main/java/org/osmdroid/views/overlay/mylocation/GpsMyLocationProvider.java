package org.osmdroid.views.overlay.mylocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.osmdroid.util.NetworkLocationIgnorer;

public class GpsMyLocationProvider implements IMyLocationProvider, LocationListener {
   private final Set locationSources = new HashSet();
   private NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
   private Location mLocation;
   private LocationManager mLocationManager;
   private float mLocationUpdateMinDistance = 0.0F;
   private long mLocationUpdateMinTime = 0L;
   private IMyLocationConsumer mMyLocationConsumer;

   public GpsMyLocationProvider(Context var1) {
      this.mLocationManager = (LocationManager)var1.getSystemService("location");
      this.locationSources.add("gps");
      this.locationSources.add("network");
   }

   public void addLocationSource(String var1) {
      this.locationSources.add(var1);
   }

   public void destroy() {
      this.stopLocationProvider();
      this.mLocation = null;
      this.mLocationManager = null;
      this.mMyLocationConsumer = null;
      this.mIgnorer = null;
   }

   public Location getLastKnownLocation() {
      return this.mLocation;
   }

   public void onLocationChanged(Location var1) {
      if (this.mIgnorer == null) {
         Log.w("OsmDroid", "GpsMyLocation provider, mIgnore is null, unexpected. Location update will be ignored");
      } else {
         if (var1 != null && var1.getProvider() != null) {
            if (this.mIgnorer.shouldIgnore(var1.getProvider(), System.currentTimeMillis())) {
               return;
            }

            this.mLocation = var1;
            IMyLocationConsumer var3 = this.mMyLocationConsumer;
            if (var3 != null) {
               Location var2 = this.mLocation;
               if (var2 != null) {
                  var3.onLocationChanged(var2, this);
               }
            }
         }

      }
   }

   public void onProviderDisabled(String var1) {
   }

   public void onProviderEnabled(String var1) {
   }

   public void onStatusChanged(String var1, int var2, Bundle var3) {
   }

   public void setLocationUpdateMinDistance(float var1) {
      this.mLocationUpdateMinDistance = var1;
   }

   public void setLocationUpdateMinTime(long var1) {
      this.mLocationUpdateMinTime = var1;
   }

   @SuppressLint({"MissingPermission"})
   public boolean startLocationProvider(IMyLocationConsumer var1) {
      this.mMyLocationConsumer = var1;
      Iterator var2 = this.mLocationManager.getProviders(true).iterator();
      boolean var3 = false;

      while(true) {
         String var4;
         do {
            if (!var2.hasNext()) {
               return var3;
            }

            var4 = (String)var2.next();
         } while(!this.locationSources.contains(var4));

         try {
            this.mLocationManager.requestLocationUpdates(var4, this.mLocationUpdateMinTime, this.mLocationUpdateMinDistance, this);
         } catch (Throwable var6) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Unable to attach listener for location provider ");
            var7.append(var4);
            var7.append(" check permissions?");
            Log.e("OsmDroid", var7.toString(), var6);
            continue;
         }

         var3 = true;
      }
   }

   @SuppressLint({"MissingPermission"})
   public void stopLocationProvider() {
      this.mMyLocationConsumer = null;
      LocationManager var1 = this.mLocationManager;
      if (var1 != null) {
         try {
            var1.removeUpdates(this);
         } catch (Throwable var2) {
            Log.w("OsmDroid", "Unable to deattach location listener", var2);
         }
      }

   }
}
