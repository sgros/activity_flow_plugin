package org.osmdroid.views.overlay.infowindow;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.LayoutParams;
import org.osmdroid.views.drawing.MapSnapshot;

public abstract class InfoWindow {
    protected boolean mIsVisible = false;
    protected MapView mMapView;
    private int mOffsetX;
    private int mOffsetY;
    private GeoPoint mPosition;
    protected Object mRelatedObject;
    protected View mView;

    public abstract void onClose();

    public abstract void onOpen(Object obj);

    public InfoWindow(int i, MapView mapView) {
        this.mMapView = mapView;
        this.mMapView.getRepository().add(this);
        this.mView = ((LayoutInflater) mapView.getContext().getSystemService("layout_inflater")).inflate(i, (ViewGroup) mapView.getParent(), false);
        this.mView.setTag(this);
    }

    public void open(Object obj, GeoPoint geoPoint, int i, int i2) {
        close();
        this.mRelatedObject = obj;
        this.mPosition = geoPoint;
        this.mOffsetX = i;
        this.mOffsetY = i2;
        onOpen(obj);
        LayoutParams layoutParams = new LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY);
        MapView mapView = this.mMapView;
        if (mapView != null) {
            View view = this.mView;
            if (view != null) {
                mapView.addView(view, layoutParams);
                this.mIsVisible = true;
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error trapped, InfoWindow.open mMapView: ");
        String str = "null";
        String str2 = "ok";
        stringBuilder.append(this.mMapView == null ? str : str2);
        stringBuilder.append(" mView: ");
        if (this.mView != null) {
            str = str2;
        }
        stringBuilder.append(str);
        Log.w("OsmDroid", stringBuilder.toString());
    }

    public void draw() {
        if (this.mIsVisible) {
            try {
                this.mMapView.updateViewLayout(this.mView, new LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY));
            } catch (Exception e) {
                if (MapSnapshot.isUIThread()) {
                    throw e;
                }
            }
        }
    }

    public void close() {
        if (this.mIsVisible) {
            this.mIsVisible = false;
            ((ViewGroup) this.mView.getParent()).removeView(this.mView);
            onClose();
        }
    }

    public void onDetach() {
        close();
        View view = this.mView;
        if (view != null) {
            view.setTag(null);
        }
        this.mView = null;
        this.mMapView = null;
        if (Configuration.getInstance().isDebugMode()) {
            Log.d("OsmDroid", "Marked detached");
        }
    }

    public boolean isOpen() {
        return this.mIsVisible;
    }
}
