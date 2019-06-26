// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay;

import org.osmdroid.views.overlay.infowindow.InfoWindow;

public abstract class OverlayWithIW extends Overlay
{
    protected InfoWindow mInfoWindow;
    protected Object mRelatedObject;
    protected String mSnippet;
    protected String mSubDescription;
    protected String mTitle;
    
    public void closeInfoWindow() {
        final InfoWindow mInfoWindow = this.mInfoWindow;
        if (mInfoWindow != null) {
            mInfoWindow.close();
        }
    }
    
    public String getSnippet() {
        return this.mSnippet;
    }
    
    public String getSubDescription() {
        return this.mSubDescription;
    }
    
    public String getTitle() {
        return this.mTitle;
    }
    
    public boolean isInfoWindowOpen() {
        final InfoWindow mInfoWindow = this.mInfoWindow;
        return mInfoWindow != null && mInfoWindow.isOpen();
    }
    
    public void onDestroy() {
        final InfoWindow mInfoWindow = this.mInfoWindow;
        if (mInfoWindow != null) {
            mInfoWindow.close();
            this.mInfoWindow.onDetach();
            this.mInfoWindow = null;
            this.mRelatedObject = null;
        }
    }
    
    public void setRelatedObject(final Object mRelatedObject) {
        this.mRelatedObject = mRelatedObject;
    }
}
