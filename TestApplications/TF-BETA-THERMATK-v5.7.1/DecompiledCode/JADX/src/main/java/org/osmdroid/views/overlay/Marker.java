package org.osmdroid.views.overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.RectL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapViewRepository;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.telegram.p004ui.ActionBar.Theme;

public class Marker extends OverlayWithIW {
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

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker, MapView mapView);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public Marker(MapView mapView) {
        this(mapView, mapView.getContext());
    }

    public Marker(MapView mapView, Context context) {
        this.mTextLabelBackgroundColor = -1;
        this.mTextLabelForegroundColor = Theme.ACTION_BAR_VIDEO_EDIT_COLOR;
        this.mTextLabelFontSize = 24;
        this.mRect = new Rect();
        this.mOrientedMarkerRect = new Rect();
        this.mMapViewRepository = mapView.getRepository();
        this.mResources = mapView.getContext().getResources();
        this.mBearing = 0.0f;
        this.mAlpha = 1.0f;
        this.mPosition = new GeoPoint(0.0d, 0.0d);
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
        setDefaultIcon();
        setInfoWindow(this.mMapViewRepository.getDefaultMarkerInfoWindow());
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            this.mIcon = drawable;
        } else {
            setDefaultIcon();
        }
    }

    public void setDefaultIcon() {
        this.mIcon = this.mMapViewRepository.getDefaultMarkerIcon();
        setAnchor(0.5f, 1.0f);
    }

    public GeoPoint getPosition() {
        return this.mPosition;
    }

    public void setPosition(GeoPoint geoPoint) {
        this.mPosition = geoPoint.clone();
        if (isInfoWindowShown()) {
            closeInfoWindow();
            showInfoWindow();
        }
    }

    public void setAnchor(float f, float f2) {
        this.mAnchorU = f;
        this.mAnchorV = f2;
    }

    public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        this.mOnMarkerClickListener = onMarkerClickListener;
    }

    public Drawable getImage() {
        return this.mImage;
    }

    public void setInfoWindow(MarkerInfoWindow markerInfoWindow) {
        this.mInfoWindow = markerInfoWindow;
    }

    public void showInfoWindow() {
        if (this.mInfoWindow != null) {
            int intrinsicWidth = this.mIcon.getIntrinsicWidth();
            intrinsicWidth = (int) (((float) intrinsicWidth) * (this.mIWAnchorU - this.mAnchorU));
            int intrinsicHeight = (int) (((float) this.mIcon.getIntrinsicHeight()) * (this.mIWAnchorV - this.mAnchorV));
            float f = this.mBearing;
            if (f == 0.0f) {
                this.mInfoWindow.open(this, this.mPosition, intrinsicWidth, intrinsicHeight);
                return;
            }
            double d = (double) (-f);
            Double.isNaN(d);
            d = (d * 3.141592653589793d) / 180.0d;
            double cos = Math.cos(d);
            d = Math.sin(d);
            long j = (long) intrinsicWidth;
            long j2 = j;
            long j3 = (long) intrinsicHeight;
            long j4 = j;
            double d2 = cos;
            this.mInfoWindow.open(this, this.mPosition, (int) RectL.getRotatedX(j2, j3, 0, 0, d2, d), (int) RectL.getRotatedY(j4, j3, 0, 0, d2, d));
        }
    }

    public boolean isInfoWindowShown() {
        InfoWindow infoWindow = this.mInfoWindow;
        if (!(infoWindow instanceof MarkerInfoWindow)) {
            return super.isInfoWindowOpen();
        }
        MarkerInfoWindow markerInfoWindow = (MarkerInfoWindow) infoWindow;
        boolean z = markerInfoWindow != null && markerInfoWindow.isOpen() && markerInfoWindow.getMarkerReference() == this;
        return z;
    }

    public void draw(Canvas canvas, Projection projection) {
        if (this.mIcon != null) {
            projection.toPixels(this.mPosition, this.mPositionPixels);
            float f = this.mFlat ? -this.mBearing : (-projection.getOrientation()) - this.mBearing;
            Point point = this.mPositionPixels;
            drawAt(canvas, point.x, point.y, f);
            if (isInfoWindowShown()) {
                this.mInfoWindow.draw();
            }
        }
    }

    public void onDetach(MapView mapView) {
        BitmapPool.getInstance().asyncRecycle(this.mIcon);
        this.mIcon = null;
        BitmapPool.getInstance().asyncRecycle(this.mImage);
        this.mOnMarkerClickListener = null;
        this.mOnMarkerDragListener = null;
        this.mResources = null;
        setRelatedObject(null);
        if (isInfoWindowShown()) {
            closeInfoWindow();
        }
        this.mMapViewRepository = null;
        setInfoWindow(null);
        onDestroy();
        super.onDetach(mapView);
    }

    public boolean hitTest(MotionEvent motionEvent, MapView mapView) {
        return this.mIcon != null && this.mDisplayed && this.mOrientedMarkerRect.contains((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        boolean hitTest = hitTest(motionEvent, mapView);
        if (hitTest) {
            OnMarkerClickListener onMarkerClickListener = this.mOnMarkerClickListener;
            if (onMarkerClickListener == null) {
                return onMarkerClickDefault(this, mapView);
            }
            hitTest = onMarkerClickListener.onMarkerClick(this, mapView);
        }
        return hitTest;
    }

    public void moveToEventPosition(MotionEvent motionEvent, MapView mapView) {
        this.mPosition = (GeoPoint) mapView.getProjection().fromPixels((int) motionEvent.getX(), (int) (motionEvent.getY() - TypedValue.applyDimension(5, this.mDragOffsetY, mapView.getContext().getResources().getDisplayMetrics())));
        mapView.invalidate();
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        boolean hitTest = hitTest(motionEvent, mapView);
        if (hitTest && this.mDraggable) {
            this.mIsDragged = true;
            closeInfoWindow();
            OnMarkerDragListener onMarkerDragListener = this.mOnMarkerDragListener;
            if (onMarkerDragListener != null) {
                onMarkerDragListener.onMarkerDragStart(this);
            }
            moveToEventPosition(motionEvent, mapView);
        }
        return hitTest;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (this.mDraggable && this.mIsDragged) {
            OnMarkerDragListener onMarkerDragListener;
            if (motionEvent.getAction() == 1) {
                this.mIsDragged = false;
                onMarkerDragListener = this.mOnMarkerDragListener;
                if (onMarkerDragListener != null) {
                    onMarkerDragListener.onMarkerDragEnd(this);
                }
                return true;
            } else if (motionEvent.getAction() == 2) {
                moveToEventPosition(motionEvent, mapView);
                onMarkerDragListener = this.mOnMarkerDragListener;
                if (onMarkerDragListener != null) {
                    onMarkerDragListener.onMarkerDrag(this);
                }
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean onMarkerClickDefault(Marker marker, MapView mapView) {
        marker.showInfoWindow();
        if (marker.mPanToView) {
            mapView.getController().animateTo(marker.getPosition());
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void drawAt(Canvas canvas, int i, int i2, float f) {
        int intrinsicWidth = this.mIcon.getIntrinsicWidth();
        int intrinsicHeight = this.mIcon.getIntrinsicHeight();
        int round = i - Math.round(((float) intrinsicWidth) * this.mAnchorU);
        int round2 = i2 - Math.round(((float) intrinsicHeight) * this.mAnchorV);
        this.mRect.set(round, round2, intrinsicWidth + round, intrinsicHeight + round2);
        RectL.getBounds(this.mRect, i, i2, (double) f, this.mOrientedMarkerRect);
        this.mDisplayed = Rect.intersects(this.mOrientedMarkerRect, canvas.getClipBounds());
        if (this.mDisplayed && this.mAlpha != 0.0f) {
            if (f != 0.0f) {
                canvas.save();
                canvas.rotate(f, (float) i, (float) i2);
            }
            Drawable drawable = this.mIcon;
            if (drawable instanceof BitmapDrawable) {
                Paint paint;
                if (this.mAlpha == 1.0f) {
                    paint = null;
                } else {
                    if (this.mPaint == null) {
                        this.mPaint = new Paint();
                    }
                    this.mPaint.setAlpha((int) (this.mAlpha * 255.0f));
                    paint = this.mPaint;
                }
                canvas.drawBitmap(((BitmapDrawable) this.mIcon).getBitmap(), (float) round, (float) round2, paint);
            } else {
                drawable.setAlpha((int) (this.mAlpha * 255.0f));
                this.mIcon.setBounds(this.mRect);
                this.mIcon.draw(canvas);
            }
            if (f != 0.0f) {
                canvas.restore();
            }
        }
    }
}
