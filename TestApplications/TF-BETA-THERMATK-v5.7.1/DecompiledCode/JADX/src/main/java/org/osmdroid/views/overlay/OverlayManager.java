package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.List;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;

public interface OverlayManager extends List<Overlay> {
    void onDetach(MapView mapView);

    boolean onDoubleTap(MotionEvent motionEvent, MapView mapView);

    boolean onDoubleTapEvent(MotionEvent motionEvent, MapView mapView);

    boolean onDown(MotionEvent motionEvent, MapView mapView);

    void onDraw(Canvas canvas, MapView mapView);

    boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView);

    boolean onKeyDown(int i, KeyEvent keyEvent, MapView mapView);

    boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView);

    boolean onLongPress(MotionEvent motionEvent, MapView mapView);

    void onPause();

    void onResume();

    boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView);

    void onShowPress(MotionEvent motionEvent, MapView mapView);

    boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView);

    boolean onSingleTapUp(MotionEvent motionEvent, MapView mapView);

    boolean onSnapToItem(int i, int i2, Point point, IMapView iMapView);

    boolean onTouchEvent(MotionEvent motionEvent, MapView mapView);

    boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView);

    List<Overlay> overlays();

    void setTilesOverlay(TilesOverlay tilesOverlay);
}
