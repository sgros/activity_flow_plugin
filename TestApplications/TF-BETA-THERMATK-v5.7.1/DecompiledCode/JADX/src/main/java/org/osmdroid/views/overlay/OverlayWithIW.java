package org.osmdroid.views.overlay;

import org.osmdroid.views.overlay.infowindow.InfoWindow;

public abstract class OverlayWithIW extends Overlay {
    protected InfoWindow mInfoWindow;
    protected Object mRelatedObject;
    protected String mSnippet;
    protected String mSubDescription;
    protected String mTitle;

    public String getTitle() {
        return this.mTitle;
    }

    public String getSnippet() {
        return this.mSnippet;
    }

    public String getSubDescription() {
        return this.mSubDescription;
    }

    public void setRelatedObject(Object obj) {
        this.mRelatedObject = obj;
    }

    public void closeInfoWindow() {
        InfoWindow infoWindow = this.mInfoWindow;
        if (infoWindow != null) {
            infoWindow.close();
        }
    }

    public void onDestroy() {
        InfoWindow infoWindow = this.mInfoWindow;
        if (infoWindow != null) {
            infoWindow.close();
            this.mInfoWindow.onDetach();
            this.mInfoWindow = null;
            this.mRelatedObject = null;
        }
    }

    public boolean isInfoWindowOpen() {
        InfoWindow infoWindow = this.mInfoWindow;
        return infoWindow != null && infoWindow.isOpen();
    }
}
