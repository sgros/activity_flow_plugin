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
    private final List<MapView> mapViews = new ArrayList(2);

    private static boolean containsMapViewPosition(SharedPreferences sharedPreferences) {
        return sharedPreferences.contains(KEY_LATITUDE) && sharedPreferences.contains(KEY_LONGITUDE) && sharedPreferences.contains(KEY_ZOOM_LEVEL);
    }

    private static boolean isCompatible(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(PREFERENCES_VERSION_KEY, -1) == 2;
    }

    private void destroyMapViews() {
        while (!this.mapViews.isEmpty()) {
            ((MapView) this.mapViews.remove(0)).destroy();
        }
    }

    private void restoreMapView(MapView mapView) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, 0);
        if (isCompatible(sharedPreferences) && containsMapViewPosition(sharedPreferences)) {
            if (sharedPreferences.contains(KEY_MAP_FILE)) {
                mapView.setMapFile(new File(sharedPreferences.getString(KEY_MAP_FILE, null)));
            }
            if (sharedPreferences.contains(KEY_RENDER_THEME_FILE)) {
                try {
                    mapView.setRenderTheme(new File(sharedPreferences.getString(KEY_RENDER_THEME_FILE, null)));
                } catch (FileNotFoundException e) {
                }
            }
            mapView.getMapViewPosition().setMapPosition(new MapPosition(new GeoPoint((double) sharedPreferences.getFloat(KEY_LATITUDE, 0.0f), (double) sharedPreferences.getFloat(KEY_LONGITUDE, 0.0f)), (byte) sharedPreferences.getInt(KEY_ZOOM_LEVEL, -1)));
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        destroyMapViews();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        if (!this.mapViews.isEmpty()) {
            int n = this.mapViews.size();
            for (int i = 0; i < n; i++) {
                ((MapView) this.mapViews.get(i)).onPause();
            }
            Editor editor = getSharedPreferences(PREFERENCES_FILE, 0).edit();
            editor.clear();
            editor.putInt(PREFERENCES_VERSION_KEY, 2);
            MapView mapView = (MapView) this.mapViews.get(0);
            MapPosition mapPosition = mapView.getMapViewPosition().getMapPosition();
            GeoPoint geoPoint = mapPosition.geoPoint;
            editor.putFloat(KEY_LATITUDE, (float) geoPoint.latitude);
            editor.putFloat(KEY_LONGITUDE, (float) geoPoint.longitude);
            editor.putInt(KEY_ZOOM_LEVEL, mapPosition.zoomLevel);
            if (mapView.getMapFile() != null) {
                editor.putString(KEY_MAP_FILE, mapView.getMapFile().getAbsolutePath());
            }
            if (mapView.getRenderThemeFile() != null) {
                editor.putString(KEY_RENDER_THEME_FILE, mapView.getRenderThemeFile().getAbsolutePath());
            }
            editor.commit();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        int n = this.mapViews.size();
        for (int i = 0; i < n; i++) {
            ((MapView) this.mapViews.get(i)).onResume();
        }
    }

    /* Access modifiers changed, original: final */
    public final int getMapViewId() {
        int i = this.lastMapViewId + 1;
        this.lastMapViewId = i;
        return i;
    }

    /* Access modifiers changed, original: final */
    public final void registerMapView(MapView mapView) {
        this.mapViews.add(mapView);
        restoreMapView(mapView);
    }
}
