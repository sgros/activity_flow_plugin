// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay;

import org.osmdroid.api.IMapView;
import android.graphics.Point;
import android.view.KeyEvent;
import android.graphics.Canvas;
import android.view.MotionEvent;
import org.osmdroid.views.MapView;
import java.util.List;

public interface OverlayManager extends List<Overlay>
{
    void onDetach(final MapView p0);
    
    boolean onDoubleTap(final MotionEvent p0, final MapView p1);
    
    boolean onDoubleTapEvent(final MotionEvent p0, final MapView p1);
    
    boolean onDown(final MotionEvent p0, final MapView p1);
    
    void onDraw(final Canvas p0, final MapView p1);
    
    boolean onFling(final MotionEvent p0, final MotionEvent p1, final float p2, final float p3, final MapView p4);
    
    boolean onKeyDown(final int p0, final KeyEvent p1, final MapView p2);
    
    boolean onKeyUp(final int p0, final KeyEvent p1, final MapView p2);
    
    boolean onLongPress(final MotionEvent p0, final MapView p1);
    
    void onPause();
    
    void onResume();
    
    boolean onScroll(final MotionEvent p0, final MotionEvent p1, final float p2, final float p3, final MapView p4);
    
    void onShowPress(final MotionEvent p0, final MapView p1);
    
    boolean onSingleTapConfirmed(final MotionEvent p0, final MapView p1);
    
    boolean onSingleTapUp(final MotionEvent p0, final MapView p1);
    
    boolean onSnapToItem(final int p0, final int p1, final Point p2, final IMapView p3);
    
    boolean onTouchEvent(final MotionEvent p0, final MapView p1);
    
    boolean onTrackballEvent(final MotionEvent p0, final MapView p1);
    
    List<Overlay> overlays();
    
    void setTilesOverlay(final TilesOverlay p0);
}
