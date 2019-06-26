// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay;

import org.osmdroid.tileprovider.BitmapPool;
import android.util.TypedValue;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import android.view.MotionEvent;
import android.graphics.drawable.BitmapDrawable;
import org.osmdroid.util.RectL;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.Projection;
import android.graphics.Canvas;
import android.content.Context;
import org.osmdroid.views.MapView;
import android.content.res.Resources;
import android.graphics.Point;
import org.osmdroid.util.GeoPoint;
import android.graphics.Paint;
import android.graphics.Rect;
import org.osmdroid.views.MapViewRepository;
import android.graphics.drawable.Drawable;

public class Marker extends OverlayWithIW
{
    protected float mAlpha;
    protected float mAnchorU;
    protected float mAnchorV;
    protected float mBearing;
    private boolean mDisplayed;
    protected float mDragOffsetY;
    protected boolean mDraggable;
    protected boolean mFlat;
    protected float mIWAnchorU;
    protected float mIWAnchorV;
    protected Drawable mIcon;
    protected Drawable mImage;
    protected boolean mIsDragged;
    private MapViewRepository mMapViewRepository;
    protected OnMarkerClickListener mOnMarkerClickListener;
    protected OnMarkerDragListener mOnMarkerDragListener;
    private final Rect mOrientedMarkerRect;
    private Paint mPaint;
    protected boolean mPanToView;
    protected GeoPoint mPosition;
    protected Point mPositionPixels;
    private final Rect mRect;
    protected Resources mResources;
    protected int mTextLabelBackgroundColor;
    protected int mTextLabelFontSize;
    protected int mTextLabelForegroundColor;
    
    public Marker(final MapView mapView) {
        this(mapView, mapView.getContext());
    }
    
    public Marker(final MapView mapView, final Context context) {
        this.mTextLabelBackgroundColor = -1;
        this.mTextLabelForegroundColor = -16777216;
        this.mTextLabelFontSize = 24;
        this.mRect = new Rect();
        this.mOrientedMarkerRect = new Rect();
        this.mMapViewRepository = mapView.getRepository();
        this.mResources = mapView.getContext().getResources();
        this.mBearing = 0.0f;
        this.mAlpha = 1.0f;
        this.mPosition = new GeoPoint(0.0, 0.0);
        this.mAnchorU = 0.5f;
        this.mAnchorV = 0.5f;
        this.mIWAnchorU = 0.5f;
        this.mIWAnchorV = 0.0f;
        this.mDraggable = false;
        this.mIsDragged = false;
        this.mPositionPixels = new Point();
        this.mPanToView = true;
        this.mDragOffsetY = 0.0f;
        this.mFlat = false;
        this.mOnMarkerClickListener = null;
        this.mOnMarkerDragListener = null;
        this.setDefaultIcon();
        this.setInfoWindow(this.mMapViewRepository.getDefaultMarkerInfoWindow());
    }
    
    @Override
    public void draw(final Canvas canvas, final Projection projection) {
        if (this.mIcon == null) {
            return;
        }
        projection.toPixels(this.mPosition, this.mPositionPixels);
        float n;
        if (this.mFlat) {
            n = -this.mBearing;
        }
        else {
            n = -projection.getOrientation() - this.mBearing;
        }
        final Point mPositionPixels = this.mPositionPixels;
        this.drawAt(canvas, mPositionPixels.x, mPositionPixels.y, n);
        if (this.isInfoWindowShown()) {
            super.mInfoWindow.draw();
        }
    }
    
    protected void drawAt(final Canvas canvas, final int n, final int n2, final float n3) {
        final int intrinsicWidth = this.mIcon.getIntrinsicWidth();
        final int intrinsicHeight = this.mIcon.getIntrinsicHeight();
        final int n4 = n - Math.round(intrinsicWidth * this.mAnchorU);
        final int n5 = n2 - Math.round(intrinsicHeight * this.mAnchorV);
        this.mRect.set(n4, n5, intrinsicWidth + n4, intrinsicHeight + n5);
        RectL.getBounds(this.mRect, n, n2, n3, this.mOrientedMarkerRect);
        if (!(this.mDisplayed = Rect.intersects(this.mOrientedMarkerRect, canvas.getClipBounds()))) {
            return;
        }
        if (this.mAlpha == 0.0f) {
            return;
        }
        if (n3 != 0.0f) {
            canvas.save();
            canvas.rotate(n3, (float)n, (float)n2);
        }
        final Drawable mIcon = this.mIcon;
        if (mIcon instanceof BitmapDrawable) {
            Paint mPaint;
            if (this.mAlpha == 1.0f) {
                mPaint = null;
            }
            else {
                if (this.mPaint == null) {
                    this.mPaint = new Paint();
                }
                this.mPaint.setAlpha((int)(this.mAlpha * 255.0f));
                mPaint = this.mPaint;
            }
            canvas.drawBitmap(((BitmapDrawable)this.mIcon).getBitmap(), (float)n4, (float)n5, mPaint);
        }
        else {
            mIcon.setAlpha((int)(this.mAlpha * 255.0f));
            this.mIcon.setBounds(this.mRect);
            this.mIcon.draw(canvas);
        }
        if (n3 != 0.0f) {
            canvas.restore();
        }
    }
    
    public Drawable getImage() {
        return this.mImage;
    }
    
    public GeoPoint getPosition() {
        return this.mPosition;
    }
    
    public boolean hitTest(final MotionEvent motionEvent, final MapView mapView) {
        return this.mIcon != null && this.mDisplayed && this.mOrientedMarkerRect.contains((int)motionEvent.getX(), (int)motionEvent.getY());
    }
    
    public boolean isInfoWindowShown() {
        final InfoWindow mInfoWindow = super.mInfoWindow;
        if (mInfoWindow instanceof MarkerInfoWindow) {
            final MarkerInfoWindow markerInfoWindow = (MarkerInfoWindow)mInfoWindow;
            return markerInfoWindow != null && markerInfoWindow.isOpen() && markerInfoWindow.getMarkerReference() == this;
        }
        return super.isInfoWindowOpen();
    }
    
    public void moveToEventPosition(final MotionEvent motionEvent, final MapView mapView) {
        this.mPosition = (GeoPoint)mapView.getProjection().fromPixels((int)motionEvent.getX(), (int)(motionEvent.getY() - TypedValue.applyDimension(5, this.mDragOffsetY, mapView.getContext().getResources().getDisplayMetrics())));
        mapView.invalidate();
    }
    
    @Override
    public void onDetach(final MapView mapView) {
        BitmapPool.getInstance().asyncRecycle(this.mIcon);
        this.mIcon = null;
        BitmapPool.getInstance().asyncRecycle(this.mImage);
        this.mOnMarkerClickListener = null;
        this.mOnMarkerDragListener = null;
        this.mResources = null;
        this.setRelatedObject(null);
        if (this.isInfoWindowShown()) {
            this.closeInfoWindow();
        }
        this.mMapViewRepository = null;
        this.setInfoWindow(null);
        this.onDestroy();
        super.onDetach(mapView);
    }
    
    @Override
    public boolean onLongPress(final MotionEvent motionEvent, final MapView mapView) {
        final boolean hitTest = this.hitTest(motionEvent, mapView);
        if (hitTest && this.mDraggable) {
            this.mIsDragged = true;
            this.closeInfoWindow();
            final OnMarkerDragListener mOnMarkerDragListener = this.mOnMarkerDragListener;
            if (mOnMarkerDragListener != null) {
                mOnMarkerDragListener.onMarkerDragStart(this);
            }
            this.moveToEventPosition(motionEvent, mapView);
        }
        return hitTest;
    }
    
    protected boolean onMarkerClickDefault(final Marker marker, final MapView mapView) {
        marker.showInfoWindow();
        if (marker.mPanToView) {
            mapView.getController().animateTo(marker.getPosition());
        }
        return true;
    }
    
    @Override
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent, final MapView mapView) {
        boolean b;
        if (b = this.hitTest(motionEvent, mapView)) {
            final OnMarkerClickListener mOnMarkerClickListener = this.mOnMarkerClickListener;
            if (mOnMarkerClickListener == null) {
                return this.onMarkerClickDefault(this, mapView);
            }
            b = mOnMarkerClickListener.onMarkerClick(this, mapView);
        }
        return b;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent, final MapView mapView) {
        if (this.mDraggable && this.mIsDragged) {
            if (motionEvent.getAction() == 1) {
                this.mIsDragged = false;
                final OnMarkerDragListener mOnMarkerDragListener = this.mOnMarkerDragListener;
                if (mOnMarkerDragListener != null) {
                    mOnMarkerDragListener.onMarkerDragEnd(this);
                }
                return true;
            }
            if (motionEvent.getAction() == 2) {
                this.moveToEventPosition(motionEvent, mapView);
                final OnMarkerDragListener mOnMarkerDragListener2 = this.mOnMarkerDragListener;
                if (mOnMarkerDragListener2 != null) {
                    mOnMarkerDragListener2.onMarkerDrag(this);
                }
                return true;
            }
        }
        return false;
    }
    
    public void setAnchor(final float mAnchorU, final float mAnchorV) {
        this.mAnchorU = mAnchorU;
        this.mAnchorV = mAnchorV;
    }
    
    public void setDefaultIcon() {
        this.mIcon = this.mMapViewRepository.getDefaultMarkerIcon();
        this.setAnchor(0.5f, 1.0f);
    }
    
    public void setIcon(final Drawable mIcon) {
        if (mIcon != null) {
            this.mIcon = mIcon;
        }
        else {
            this.setDefaultIcon();
        }
    }
    
    public void setInfoWindow(final MarkerInfoWindow mInfoWindow) {
        super.mInfoWindow = mInfoWindow;
    }
    
    public void setOnMarkerClickListener(final OnMarkerClickListener mOnMarkerClickListener) {
        this.mOnMarkerClickListener = mOnMarkerClickListener;
    }
    
    public void setPosition(final GeoPoint geoPoint) {
        this.mPosition = geoPoint.clone();
        if (this.isInfoWindowShown()) {
            this.closeInfoWindow();
            this.showInfoWindow();
        }
    }
    
    public void showInfoWindow() {
        if (super.mInfoWindow == null) {
            return;
        }
        final int intrinsicWidth = this.mIcon.getIntrinsicWidth();
        final int intrinsicHeight = this.mIcon.getIntrinsicHeight();
        final int n = (int)(intrinsicWidth * (this.mIWAnchorU - this.mAnchorU));
        final int n2 = (int)(intrinsicHeight * (this.mIWAnchorV - this.mAnchorV));
        final float mBearing = this.mBearing;
        if (mBearing == 0.0f) {
            super.mInfoWindow.open(this, this.mPosition, n, n2);
            return;
        }
        final double v = -mBearing;
        Double.isNaN(v);
        final double n3 = v * 3.141592653589793 / 180.0;
        final double cos = Math.cos(n3);
        final double sin = Math.sin(n3);
        final long n4 = n;
        final long n5 = n2;
        super.mInfoWindow.open(this, this.mPosition, (int)RectL.getRotatedX(n4, n5, 0L, 0L, cos, sin), (int)RectL.getRotatedY(n4, n5, 0L, 0L, cos, sin));
    }
    
    public interface OnMarkerClickListener
    {
        boolean onMarkerClick(final Marker p0, final MapView p1);
    }
    
    public interface OnMarkerDragListener
    {
        void onMarkerDrag(final Marker p0);
        
        void onMarkerDragEnd(final Marker p0);
        
        void onMarkerDragStart(final Marker p0);
    }
}
