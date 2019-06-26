// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay.infowindow;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.util.Log;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MarkerInfoWindow extends BasicInfoWindow
{
    protected Marker mMarkerRef;
    
    public MarkerInfoWindow(final int n, final MapView mapView) {
        super(n, mapView);
    }
    
    public Marker getMarkerReference() {
        return this.mMarkerRef;
    }
    
    @Override
    public void onClose() {
        super.onClose();
        this.mMarkerRef = null;
    }
    
    @Override
    public void onOpen(final Object o) {
        super.onOpen(o);
        this.mMarkerRef = (Marker)o;
        final View mView = super.mView;
        if (mView == null) {
            Log.w("OsmDroid", "Error trapped, MarkerInfoWindow.open, mView is null!");
            return;
        }
        final ImageView imageView = (ImageView)mView.findViewById(BasicInfoWindow.mImageId);
        final Drawable image = this.mMarkerRef.getImage();
        if (image != null) {
            imageView.setImageDrawable(image);
            imageView.setScaleType(ImageView$ScaleType.CENTER_INSIDE);
            imageView.setVisibility(0);
        }
        else {
            imageView.setVisibility(8);
        }
    }
}
