// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay;

import org.osmdroid.api.IMapView;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import org.osmdroid.views.Projection;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.content.Context;
import org.osmdroid.views.MapView;
import org.osmdroid.util.TileSystem;
import org.osmdroid.util.BoundingBox;
import java.util.concurrent.atomic.AtomicInteger;
import android.graphics.Rect;
import org.osmdroid.views.util.constants.OverlayConstants;

public abstract class Overlay implements OverlayConstants
{
    protected static final float SHADOW_X_SKEW = -0.9f;
    protected static final float SHADOW_Y_SCALE = 0.5f;
    private static final Rect mRect;
    private static AtomicInteger sOrdinal;
    protected BoundingBox mBounds;
    private boolean mEnabled;
    private final TileSystem tileSystem;
    
    static {
        Overlay.sOrdinal = new AtomicInteger();
        mRect = new Rect();
    }
    
    public Overlay() {
        this.mEnabled = true;
        this.tileSystem = MapView.getTileSystem();
        this.mBounds = new BoundingBox(this.tileSystem.getMaxLatitude(), this.tileSystem.getMaxLongitude(), this.tileSystem.getMinLatitude(), this.tileSystem.getMinLongitude());
    }
    
    @Deprecated
    public Overlay(final Context context) {
        this.mEnabled = true;
        this.tileSystem = MapView.getTileSystem();
        this.mBounds = new BoundingBox(this.tileSystem.getMaxLatitude(), this.tileSystem.getMaxLongitude(), this.tileSystem.getMinLatitude(), this.tileSystem.getMinLongitude());
    }
    
    protected static void drawAt(final Canvas canvas, final Drawable drawable, final int n, final int n2, final boolean b, final float n3) {
        synchronized (Overlay.class) {
            canvas.save();
            canvas.rotate(-n3, (float)n, (float)n2);
            drawable.copyBounds(Overlay.mRect);
            drawable.setBounds(Overlay.mRect.left + n, Overlay.mRect.top + n2, Overlay.mRect.right + n, Overlay.mRect.bottom + n2);
            drawable.draw(canvas);
            drawable.setBounds(Overlay.mRect);
            canvas.restore();
        }
    }
    
    protected static final int getSafeMenuId() {
        return Overlay.sOrdinal.getAndIncrement();
    }
    
    protected static final int getSafeMenuIdSequence(final int delta) {
        return Overlay.sOrdinal.getAndAdd(delta);
    }
    
    public void draw(final Canvas canvas, final MapView mapView, final boolean b) {
        if (b) {
            return;
        }
        this.draw(canvas, mapView.getProjection());
    }
    
    public void draw(final Canvas canvas, final Projection projection) {
    }
    
    public BoundingBox getBounds() {
        return this.mBounds;
    }
    
    public boolean isEnabled() {
        return this.mEnabled;
    }
    
    public void onDetach(final MapView mapView) {
    }
    
    public boolean onDoubleTap(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onDoubleTapEvent(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onDown(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2, final MapView mapView) {
        return false;
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onLongPress(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public void onPause() {
    }
    
    public void onResume() {
    }
    
    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2, final MapView mapView) {
        return false;
    }
    
    public void onShowPress(final MotionEvent motionEvent, final MapView mapView) {
    }
    
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public boolean onTrackballEvent(final MotionEvent motionEvent, final MapView mapView) {
        return false;
    }
    
    public void setEnabled(final boolean mEnabled) {
        this.mEnabled = mEnabled;
    }
    
    public interface Snappable
    {
        boolean onSnapToItem(final int p0, final int p1, final Point p2, final IMapView p3);
    }
}
