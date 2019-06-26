package org.mapsforge.android.maps;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ZoomControls;

public class MapZoomControls {
    private static final int DEFAULT_ZOOM_CONTROLS_GRAVITY = 85;
    private static final byte DEFAULT_ZOOM_LEVEL_MAX = (byte) 22;
    private static final byte DEFAULT_ZOOM_LEVEL_MIN = (byte) 0;
    private static final int MSG_ZOOM_CONTROLS_HIDE = 0;
    private static final int ZOOM_CONTROLS_HORIZONTAL_PADDING = 5;
    private static final long ZOOM_CONTROLS_TIMEOUT = ViewConfiguration.getZoomControlsTimeout();
    private static final int ZOOM_CONTROLS_VERTICAL_PADDING = 15;
    private boolean gravityChanged;
    private boolean showMapZoomControls = true;
    private final ZoomControls zoomControls;
    private int zoomControlsGravity;
    private final Handler zoomControlsHideHandler;
    private byte zoomLevelMax = DEFAULT_ZOOM_LEVEL_MAX;
    private byte zoomLevelMin = DEFAULT_ZOOM_LEVEL_MIN;

    private static class ZoomControlsHideHandler extends Handler {
        private final ZoomControls zoomControls;

        ZoomControlsHideHandler(ZoomControls zoomControls) {
            this.zoomControls = zoomControls;
        }

        public void handleMessage(Message message) {
            this.zoomControls.hide();
        }
    }

    private static class ZoomInClickListener implements OnClickListener {
        private final MapViewPosition mapViewPosition;

        ZoomInClickListener(MapViewPosition mapViewPosition) {
            this.mapViewPosition = mapViewPosition;
        }

        public void onClick(View view) {
            this.mapViewPosition.zoomIn();
        }
    }

    private static class ZoomOutClickListener implements OnClickListener {
        private final MapViewPosition mapViewPosition;

        ZoomOutClickListener(MapViewPosition mapViewPosition) {
            this.mapViewPosition = mapViewPosition;
        }

        public void onClick(View view) {
            this.mapViewPosition.zoomOut();
        }
    }

    MapZoomControls(Context context, MapView mapView) {
        this.zoomControls = new ZoomControls(context);
        this.zoomControls.setVisibility(8);
        this.zoomControlsGravity = DEFAULT_ZOOM_CONTROLS_GRAVITY;
        MapViewPosition mapViewPosition = mapView.getMapViewPosition();
        this.zoomControls.setOnZoomInClickListener(new ZoomInClickListener(mapViewPosition));
        this.zoomControls.setOnZoomOutClickListener(new ZoomOutClickListener(mapViewPosition));
        this.zoomControlsHideHandler = new ZoomControlsHideHandler(this.zoomControls);
        mapView.addView(this.zoomControls, new LayoutParams(-2, -2));
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

    public void setShowMapZoomControls(boolean showMapZoomControls) {
        this.showMapZoomControls = showMapZoomControls;
    }

    public void setZoomControlsGravity(int zoomControlsGravity) {
        if (this.zoomControlsGravity != zoomControlsGravity) {
            this.zoomControlsGravity = zoomControlsGravity;
            this.gravityChanged = true;
        }
    }

    public void setZoomLevelMax(byte zoomLevelMax) {
        if (zoomLevelMax < this.zoomLevelMin) {
            throw new IllegalArgumentException();
        }
        this.zoomLevelMax = zoomLevelMax;
    }

    public void setZoomLevelMin(byte zoomLevelMin) {
        if (zoomLevelMin > this.zoomLevelMax) {
            throw new IllegalArgumentException();
        }
        this.zoomLevelMin = zoomLevelMin;
    }

    private int calculatePositionLeft(int left, int right, int zoomControlsWidth) {
        int gravity = this.zoomControlsGravity & 7;
        switch (gravity) {
            case 1:
                return ((right - left) - zoomControlsWidth) / 2;
            case 3:
                return 5;
            case 5:
                return ((right - left) - zoomControlsWidth) - 5;
            default:
                throw new IllegalArgumentException("unknown horizontal gravity: " + gravity);
        }
    }

    private int calculatePositionTop(int top, int bottom, int zoomControlsHeight) {
        int gravity = this.zoomControlsGravity & 112;
        switch (gravity) {
            case 16:
                return ((bottom - top) - zoomControlsHeight) / 2;
            case 48:
                return 15;
            case 80:
                return ((bottom - top) - zoomControlsHeight) - 15;
            default:
                throw new IllegalArgumentException("unknown vertical gravity: " + gravity);
        }
    }

    private void showZoomControls() {
        this.zoomControlsHideHandler.removeMessages(0);
        if (this.zoomControls.getVisibility() != 0) {
            this.zoomControls.show();
        }
    }

    private void showZoomControlsWithTimeout() {
        showZoomControls();
        this.zoomControlsHideHandler.sendEmptyMessageDelayed(0, ZOOM_CONTROLS_TIMEOUT);
    }

    /* Access modifiers changed, original: 0000 */
    public int getMeasuredHeight() {
        return this.zoomControls.getMeasuredHeight();
    }

    /* Access modifiers changed, original: 0000 */
    public int getMeasuredWidth() {
        return this.zoomControls.getMeasuredWidth();
    }

    /* Access modifiers changed, original: 0000 */
    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
        this.zoomControls.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /* Access modifiers changed, original: 0000 */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed || this.gravityChanged) {
            int zoomControlsWidth = this.zoomControls.getMeasuredWidth();
            int zoomControlsHeight = this.zoomControls.getMeasuredHeight();
            int positionLeft = calculatePositionLeft(left, right, zoomControlsWidth);
            int positionTop = calculatePositionTop(top, bottom, zoomControlsHeight);
            this.zoomControls.layout(positionLeft, positionTop, positionLeft + zoomControlsWidth, positionTop + zoomControlsHeight);
            this.gravityChanged = false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onMapViewTouchEvent(int action) {
        if (this.showMapZoomControls) {
            switch (action) {
                case 0:
                    showZoomControls();
                    return;
                case 1:
                    showZoomControlsWithTimeout();
                    return;
                case 3:
                    showZoomControlsWithTimeout();
                    return;
                default:
                    return;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onZoomLevelChange(int newZoomLevel) {
        boolean zoomInEnabled;
        boolean zoomOutEnabled;
        if (newZoomLevel < this.zoomLevelMax) {
            zoomInEnabled = true;
        } else {
            zoomInEnabled = false;
        }
        if (newZoomLevel > this.zoomLevelMin) {
            zoomOutEnabled = true;
        } else {
            zoomOutEnabled = false;
        }
        this.zoomControls.setIsZoomInEnabled(zoomInEnabled);
        this.zoomControls.setIsZoomOutEnabled(zoomOutEnabled);
    }
}
