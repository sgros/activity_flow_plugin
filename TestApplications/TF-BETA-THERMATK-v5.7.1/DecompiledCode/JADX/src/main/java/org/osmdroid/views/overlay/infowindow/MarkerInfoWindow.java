package org.osmdroid.views.overlay.infowindow;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MarkerInfoWindow extends BasicInfoWindow {
    protected Marker mMarkerRef;

    public MarkerInfoWindow(int i, MapView mapView) {
        super(i, mapView);
    }

    public Marker getMarkerReference() {
        return this.mMarkerRef;
    }

    public void onOpen(Object obj) {
        super.onOpen(obj);
        this.mMarkerRef = (Marker) obj;
        View view = this.mView;
        if (view == null) {
            Log.w("OsmDroid", "Error trapped, MarkerInfoWindow.open, mView is null!");
            return;
        }
        ImageView imageView = (ImageView) view.findViewById(BasicInfoWindow.mImageId);
        Drawable image = this.mMarkerRef.getImage();
        if (image != null) {
            imageView.setImageDrawable(image);
            imageView.setScaleType(ScaleType.CENTER_INSIDE);
            imageView.setVisibility(0);
        } else {
            imageView.setVisibility(8);
        }
    }

    public void onClose() {
        super.onClose();
        this.mMarkerRef = null;
    }
}
