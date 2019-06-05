// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

import android.os.Message;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.LinearLayout$LayoutParams;
import android.view.View$OnClickListener;
import android.content.Context;
import android.view.ViewConfiguration;
import android.os.Handler;
import android.widget.ZoomControls;

public class MapZoomControls
{
    private static final int DEFAULT_ZOOM_CONTROLS_GRAVITY = 85;
    private static final byte DEFAULT_ZOOM_LEVEL_MAX = 22;
    private static final byte DEFAULT_ZOOM_LEVEL_MIN = 0;
    private static final int MSG_ZOOM_CONTROLS_HIDE = 0;
    private static final int ZOOM_CONTROLS_HORIZONTAL_PADDING = 5;
    private static final long ZOOM_CONTROLS_TIMEOUT;
    private static final int ZOOM_CONTROLS_VERTICAL_PADDING = 15;
    private boolean gravityChanged;
    private boolean showMapZoomControls;
    private final ZoomControls zoomControls;
    private int zoomControlsGravity;
    private final Handler zoomControlsHideHandler;
    private byte zoomLevelMax;
    private byte zoomLevelMin;
    
    static {
        ZOOM_CONTROLS_TIMEOUT = ViewConfiguration.getZoomControlsTimeout();
    }
    
    MapZoomControls(final Context context, final MapView mapView) {
        this.zoomControls = new ZoomControls(context);
        this.showMapZoomControls = true;
        this.zoomLevelMax = 22;
        this.zoomLevelMin = 0;
        this.zoomControls.setVisibility(8);
        this.zoomControlsGravity = 85;
        final MapViewPosition mapViewPosition = mapView.getMapViewPosition();
        this.zoomControls.setOnZoomInClickListener((View$OnClickListener)new ZoomInClickListener(mapViewPosition));
        this.zoomControls.setOnZoomOutClickListener((View$OnClickListener)new ZoomOutClickListener(mapViewPosition));
        this.zoomControlsHideHandler = new ZoomControlsHideHandler(this.zoomControls);
        mapView.addView((View)this.zoomControls, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-2, -2));
    }
    
    private int calculatePositionLeft(int n, final int n2, final int n3) {
        final int i = this.zoomControlsGravity & 0x7;
        switch (i) {
            default: {
                throw new IllegalArgumentException("unknown horizontal gravity: " + i);
            }
            case 3: {
                n = 5;
                break;
            }
            case 1: {
                n = (n2 - n - n3) / 2;
                break;
            }
            case 5: {
                n = n2 - n - n3 - 5;
                break;
            }
        }
        return n;
    }
    
    private int calculatePositionTop(int n, final int n2, final int n3) {
        final int i = this.zoomControlsGravity & 0x70;
        switch (i) {
            default: {
                throw new IllegalArgumentException("unknown vertical gravity: " + i);
            }
            case 48: {
                n = 15;
                break;
            }
            case 16: {
                n = (n2 - n - n3) / 2;
                break;
            }
            case 80: {
                n = n2 - n - n3 - 15;
                break;
            }
        }
        return n;
    }
    
    private void showZoomControls() {
        this.zoomControlsHideHandler.removeMessages(0);
        if (this.zoomControls.getVisibility() != 0) {
            this.zoomControls.show();
        }
    }
    
    private void showZoomControlsWithTimeout() {
        this.showZoomControls();
        this.zoomControlsHideHandler.sendEmptyMessageDelayed(0, MapZoomControls.ZOOM_CONTROLS_TIMEOUT);
    }
    
    int getMeasuredHeight() {
        return this.zoomControls.getMeasuredHeight();
    }
    
    int getMeasuredWidth() {
        return this.zoomControls.getMeasuredWidth();
    }
    
    public int getZoomControlsGravity() {
        return this.zoomControlsGravity;
    }
    
    public byte getZoomLevelMax() {
        return this.zoomLevelMax;
    }
    
    public byte getZoomLevelMin() {
        return this.zoomLevelMin;
    }
    
    public boolean isShowMapZoomControls() {
        return this.showMapZoomControls;
    }
    
    void measure(final int n, final int n2) {
        this.zoomControls.measure(n, n2);
    }
    
    void onLayout(final boolean b, int calculatePositionLeft, int calculatePositionTop, final int n, final int n2) {
        if (b || this.gravityChanged) {
            final int measuredWidth = this.zoomControls.getMeasuredWidth();
            final int measuredHeight = this.zoomControls.getMeasuredHeight();
            calculatePositionLeft = this.calculatePositionLeft(calculatePositionLeft, n, measuredWidth);
            calculatePositionTop = this.calculatePositionTop(calculatePositionTop, n2, measuredHeight);
            this.zoomControls.layout(calculatePositionLeft, calculatePositionTop, calculatePositionLeft + measuredWidth, calculatePositionTop + measuredHeight);
            this.gravityChanged = false;
        }
    }
    
    void onMapViewTouchEvent(final int n) {
        if (this.showMapZoomControls) {
            switch (n) {
                case 0: {
                    this.showZoomControls();
                    break;
                }
                case 3: {
                    this.showZoomControlsWithTimeout();
                    break;
                }
                case 1: {
                    this.showZoomControlsWithTimeout();
                    break;
                }
            }
        }
    }
    
    void onZoomLevelChange(final int n) {
        final boolean isZoomInEnabled = n < this.zoomLevelMax;
        final boolean isZoomOutEnabled = n > this.zoomLevelMin;
        this.zoomControls.setIsZoomInEnabled(isZoomInEnabled);
        this.zoomControls.setIsZoomOutEnabled(isZoomOutEnabled);
    }
    
    public void setShowMapZoomControls(final boolean showMapZoomControls) {
        this.showMapZoomControls = showMapZoomControls;
    }
    
    public void setZoomControlsGravity(final int zoomControlsGravity) {
        if (this.zoomControlsGravity != zoomControlsGravity) {
            this.zoomControlsGravity = zoomControlsGravity;
            this.gravityChanged = true;
        }
    }
    
    public void setZoomLevelMax(final byte b) {
        if (b < this.zoomLevelMin) {
            throw new IllegalArgumentException();
        }
        this.zoomLevelMax = b;
    }
    
    public void setZoomLevelMin(final byte b) {
        if (b > this.zoomLevelMax) {
            throw new IllegalArgumentException();
        }
        this.zoomLevelMin = b;
    }
    
    private static class ZoomControlsHideHandler extends Handler
    {
        private final ZoomControls zoomControls;
        
        ZoomControlsHideHandler(final ZoomControls zoomControls) {
            this.zoomControls = zoomControls;
        }
        
        public void handleMessage(final Message message) {
            this.zoomControls.hide();
        }
    }
    
    private static class ZoomInClickListener implements View$OnClickListener
    {
        private final MapViewPosition mapViewPosition;
        
        ZoomInClickListener(final MapViewPosition mapViewPosition) {
            this.mapViewPosition = mapViewPosition;
        }
        
        public void onClick(final View view) {
            this.mapViewPosition.zoomIn();
        }
    }
    
    private static class ZoomOutClickListener implements View$OnClickListener
    {
        private final MapViewPosition mapViewPosition;
        
        ZoomOutClickListener(final MapViewPosition mapViewPosition) {
            this.mapViewPosition = mapViewPosition;
        }
        
        public void onClick(final View view) {
            this.mapViewPosition.zoomOut();
        }
    }
}
