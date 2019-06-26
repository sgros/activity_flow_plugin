// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay.infowindow;

import android.util.Log;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.drawing.MapSnapshot;
import android.view.ViewGroup$LayoutParams;
import org.osmdroid.api.IGeoPoint;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public abstract class InfoWindow
{
    protected boolean mIsVisible;
    protected MapView mMapView;
    private int mOffsetX;
    private int mOffsetY;
    private GeoPoint mPosition;
    protected Object mRelatedObject;
    protected View mView;
    
    public InfoWindow(final int n, final MapView mMapView) {
        this.mMapView = mMapView;
        this.mMapView.getRepository().add(this);
        this.mIsVisible = false;
        (this.mView = ((LayoutInflater)mMapView.getContext().getSystemService("layout_inflater")).inflate(n, (ViewGroup)mMapView.getParent(), false)).setTag((Object)this);
    }
    
    public void close() {
        if (this.mIsVisible) {
            this.mIsVisible = false;
            ((ViewGroup)this.mView.getParent()).removeView(this.mView);
            this.onClose();
        }
    }
    
    public void draw() {
        if (!this.mIsVisible) {
            return;
        }
        try {
            this.mMapView.updateViewLayout(this.mView, (ViewGroup$LayoutParams)new MapView.LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY));
        }
        catch (Exception ex) {
            if (MapSnapshot.isUIThread()) {
                throw ex;
            }
        }
    }
    
    public boolean isOpen() {
        return this.mIsVisible;
    }
    
    public abstract void onClose();
    
    public void onDetach() {
        this.close();
        final View mView = this.mView;
        if (mView != null) {
            mView.setTag((Object)null);
        }
        this.mView = null;
        this.mMapView = null;
        if (Configuration.getInstance().isDebugMode()) {
            Log.d("OsmDroid", "Marked detached");
        }
    }
    
    public abstract void onOpen(final Object p0);
    
    public void open(final Object mRelatedObject, final GeoPoint mPosition, final int mOffsetX, final int mOffsetY) {
        this.close();
        this.mRelatedObject = mRelatedObject;
        this.mPosition = mPosition;
        this.mOffsetX = mOffsetX;
        this.mOffsetY = mOffsetY;
        this.onOpen(mRelatedObject);
        final MapView.LayoutParams layoutParams = new MapView.LayoutParams(-2, -2, this.mPosition, 8, this.mOffsetX, this.mOffsetY);
        final MapView mMapView = this.mMapView;
        if (mMapView != null) {
            final View mView = this.mView;
            if (mView != null) {
                mMapView.addView(mView, (ViewGroup$LayoutParams)layoutParams);
                this.mIsVisible = true;
                return;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Error trapped, InfoWindow.open mMapView: ");
        final MapView mMapView2 = this.mMapView;
        final String s = "null";
        String str;
        if (mMapView2 == null) {
            str = "null";
        }
        else {
            str = "ok";
        }
        sb.append(str);
        sb.append(" mView: ");
        String str2;
        if (this.mView == null) {
            str2 = s;
        }
        else {
            str2 = "ok";
        }
        sb.append(str2);
        Log.w("OsmDroid", sb.toString());
    }
}
