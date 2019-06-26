// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import android.content.SharedPreferences$Editor;
import java.io.FileNotFoundException;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.GeoPoint;
import java.io.File;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

public abstract class MapActivity extends Activity
{
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_MAP_FILE = "mapFile";
    private static final String KEY_RENDER_THEME_FILE = "renderThemeFile";
    private static final String KEY_ZOOM_LEVEL = "zoomLevel";
    private static final String PREFERENCES_FILE = "MapActivity";
    private static final String PREFERENCES_VERSION_KEY = "version";
    private static final int PREFERENCES_VERSION_NUMBER = 2;
    private int lastMapViewId;
    private final List<MapView> mapViews;
    
    public MapActivity() {
        this.mapViews = new ArrayList<MapView>(2);
    }
    
    private static boolean containsMapViewPosition(final SharedPreferences sharedPreferences) {
        return sharedPreferences.contains("latitude") && sharedPreferences.contains("longitude") && sharedPreferences.contains("zoomLevel");
    }
    
    private void destroyMapViews() {
        while (!this.mapViews.isEmpty()) {
            this.mapViews.remove(0).destroy();
        }
    }
    
    private static boolean isCompatible(final SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt("version", -1) == 2;
    }
    
    private void restoreMapView(final MapView mapView) {
        Object sharedPreferences = this.getSharedPreferences("MapActivity", 0);
        if (!isCompatible((SharedPreferences)sharedPreferences) || !containsMapViewPosition((SharedPreferences)sharedPreferences)) {
            return;
        }
        if (((SharedPreferences)sharedPreferences).contains("mapFile")) {
            mapView.setMapFile(new File(((SharedPreferences)sharedPreferences).getString("mapFile", (String)null)));
        }
        while (true) {
            if (!((SharedPreferences)sharedPreferences).contains("renderThemeFile")) {
                break Label_0087;
            }
            try {
                mapView.setRenderTheme(new File(((SharedPreferences)sharedPreferences).getString("renderThemeFile", (String)null)));
                sharedPreferences = new MapPosition(new GeoPoint(((SharedPreferences)sharedPreferences).getFloat("latitude", 0.0f), ((SharedPreferences)sharedPreferences).getFloat("longitude", 0.0f)), (byte)((SharedPreferences)sharedPreferences).getInt("zoomLevel", -1));
                mapView.getMapViewPosition().setMapPosition((MapPosition)sharedPreferences);
            }
            catch (FileNotFoundException ex) {
                continue;
            }
            break;
        }
    }
    
    final int getMapViewId() {
        return ++this.lastMapViewId;
    }
    
    protected void onDestroy() {
        super.onDestroy();
        this.destroyMapViews();
    }
    
    protected void onPause() {
        super.onPause();
        if (!this.mapViews.isEmpty()) {
            for (int i = 0; i < this.mapViews.size(); ++i) {
                this.mapViews.get(i).onPause();
            }
            final SharedPreferences$Editor edit = this.getSharedPreferences("MapActivity", 0).edit();
            edit.clear();
            edit.putInt("version", 2);
            final MapView mapView = this.mapViews.get(0);
            final MapPosition mapPosition = mapView.getMapViewPosition().getMapPosition();
            final GeoPoint geoPoint = mapPosition.geoPoint;
            edit.putFloat("latitude", (float)geoPoint.latitude);
            edit.putFloat("longitude", (float)geoPoint.longitude);
            edit.putInt("zoomLevel", (int)mapPosition.zoomLevel);
            if (mapView.getMapFile() != null) {
                edit.putString("mapFile", mapView.getMapFile().getAbsolutePath());
            }
            if (mapView.getRenderThemeFile() != null) {
                edit.putString("renderThemeFile", mapView.getRenderThemeFile().getAbsolutePath());
            }
            edit.commit();
        }
    }
    
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < this.mapViews.size(); ++i) {
            this.mapViews.get(i).onResume();
        }
    }
    
    final void registerMapView(final MapView mapView) {
        this.mapViews.add(mapView);
        this.restoreMapView(mapView);
    }
}
