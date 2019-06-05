package org.mapsforge.android.maps;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;

public abstract class MapActivity extends Activity {
   private static final String KEY_LATITUDE = "latitude";
   private static final String KEY_LONGITUDE = "longitude";
   private static final String KEY_MAP_FILE = "mapFile";
   private static final String KEY_RENDER_THEME_FILE = "renderThemeFile";
   private static final String KEY_ZOOM_LEVEL = "zoomLevel";
   private static final String PREFERENCES_FILE = "MapActivity";
   private static final String PREFERENCES_VERSION_KEY = "version";
   private static final int PREFERENCES_VERSION_NUMBER = 2;
   private int lastMapViewId;
   private final List mapViews = new ArrayList(2);

   private static boolean containsMapViewPosition(SharedPreferences var0) {
      boolean var1;
      if (var0.contains("latitude") && var0.contains("longitude") && var0.contains("zoomLevel")) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void destroyMapViews() {
      while(!this.mapViews.isEmpty()) {
         ((MapView)this.mapViews.remove(0)).destroy();
      }

   }

   private static boolean isCompatible(SharedPreferences var0) {
      boolean var1;
      if (var0.getInt("version", -1) == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private void restoreMapView(MapView var1) {
      SharedPreferences var2 = this.getSharedPreferences("MapActivity", 0);
      if (isCompatible(var2) && containsMapViewPosition(var2)) {
         if (var2.contains("mapFile")) {
            var1.setMapFile(new File(var2.getString("mapFile", (String)null)));
         }

         if (var2.contains("renderThemeFile")) {
            try {
               File var3 = new File(var2.getString("renderThemeFile", (String)null));
               var1.setRenderTheme(var3);
            } catch (FileNotFoundException var7) {
            }
         }

         float var4 = var2.getFloat("latitude", 0.0F);
         float var5 = var2.getFloat("longitude", 0.0F);
         int var6 = var2.getInt("zoomLevel", -1);
         MapPosition var8 = new MapPosition(new GeoPoint((double)var4, (double)var5), (byte)var6);
         var1.getMapViewPosition().setMapPosition(var8);
      }

   }

   final int getMapViewId() {
      int var1 = this.lastMapViewId + 1;
      this.lastMapViewId = var1;
      return var1;
   }

   protected void onDestroy() {
      super.onDestroy();
      this.destroyMapViews();
   }

   protected void onPause() {
      super.onPause();
      if (!this.mapViews.isEmpty()) {
         int var1 = 0;

         for(int var2 = this.mapViews.size(); var1 < var2; ++var1) {
            ((MapView)this.mapViews.get(var1)).onPause();
         }

         Editor var3 = this.getSharedPreferences("MapActivity", 0).edit();
         var3.clear();
         var3.putInt("version", 2);
         MapView var4 = (MapView)this.mapViews.get(0);
         MapPosition var5 = var4.getMapViewPosition().getMapPosition();
         GeoPoint var6 = var5.geoPoint;
         var3.putFloat("latitude", (float)var6.latitude);
         var3.putFloat("longitude", (float)var6.longitude);
         var3.putInt("zoomLevel", var5.zoomLevel);
         if (var4.getMapFile() != null) {
            var3.putString("mapFile", var4.getMapFile().getAbsolutePath());
         }

         if (var4.getRenderThemeFile() != null) {
            var3.putString("renderThemeFile", var4.getRenderThemeFile().getAbsolutePath());
         }

         var3.commit();
      }

   }

   protected void onResume() {
      super.onResume();
      int var1 = 0;

      for(int var2 = this.mapViews.size(); var1 < var2; ++var1) {
         ((MapView)this.mapViews.get(var1)).onResume();
      }

   }

   final void registerMapView(MapView var1) {
      this.mapViews.add(var1);
      this.restoreMapView(var1);
   }
}
