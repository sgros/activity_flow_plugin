// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views;

import java.util.Iterator;
import org.osmdroid.library.R$layout;
import org.osmdroid.library.R$drawable;
import java.util.HashSet;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import java.util.Set;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import android.graphics.drawable.Drawable;

public class MapViewRepository
{
    private Drawable mDefaultMarkerIcon;
    private MarkerInfoWindow mDefaultMarkerInfoWindow;
    private BasicInfoWindow mDefaultPolygonInfoWindow;
    private BasicInfoWindow mDefaultPolylineInfoWindow;
    private final Set<InfoWindow> mInfoWindowList;
    private MapView mMapView;
    
    public MapViewRepository(final MapView mMapView) {
        this.mInfoWindowList = new HashSet<InfoWindow>();
        this.mMapView = mMapView;
    }
    
    public void add(final InfoWindow infoWindow) {
        this.mInfoWindowList.add(infoWindow);
    }
    
    public Drawable getDefaultMarkerIcon() {
        if (this.mDefaultMarkerIcon == null) {
            this.mDefaultMarkerIcon = this.mMapView.getContext().getResources().getDrawable(R$drawable.marker_default);
        }
        return this.mDefaultMarkerIcon;
    }
    
    public MarkerInfoWindow getDefaultMarkerInfoWindow() {
        if (this.mDefaultMarkerInfoWindow == null) {
            this.mDefaultMarkerInfoWindow = new MarkerInfoWindow(R$layout.bonuspack_bubble, this.mMapView);
        }
        return this.mDefaultMarkerInfoWindow;
    }
    
    public void onDetach() {
        synchronized (this.mInfoWindowList) {
            final Iterator<InfoWindow> iterator = this.mInfoWindowList.iterator();
            while (iterator.hasNext()) {
                iterator.next().onDetach();
            }
            this.mInfoWindowList.clear();
            // monitorexit(this.mInfoWindowList)
            this.mMapView = null;
            this.mDefaultMarkerInfoWindow = null;
            this.mDefaultPolylineInfoWindow = null;
            this.mDefaultPolygonInfoWindow = null;
            this.mDefaultMarkerIcon = null;
        }
    }
}
