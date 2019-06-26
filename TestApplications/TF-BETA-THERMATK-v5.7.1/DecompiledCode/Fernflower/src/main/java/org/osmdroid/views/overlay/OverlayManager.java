package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.List;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;

public interface OverlayManager extends List {
   void onDetach(MapView var1);

   boolean onDoubleTap(MotionEvent var1, MapView var2);

   boolean onDoubleTapEvent(MotionEvent var1, MapView var2);

   boolean onDown(MotionEvent var1, MapView var2);

   void onDraw(Canvas var1, MapView var2);

   boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4, MapView var5);

   boolean onKeyDown(int var1, KeyEvent var2, MapView var3);

   boolean onKeyUp(int var1, KeyEvent var2, MapView var3);

   boolean onLongPress(MotionEvent var1, MapView var2);

   void onPause();

   void onResume();

   boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4, MapView var5);

   void onShowPress(MotionEvent var1, MapView var2);

   boolean onSingleTapConfirmed(MotionEvent var1, MapView var2);

   boolean onSingleTapUp(MotionEvent var1, MapView var2);

   boolean onSnapToItem(int var1, int var2, Point var3, IMapView var4);

   boolean onTouchEvent(MotionEvent var1, MapView var2);

   boolean onTrackballEvent(MotionEvent var1, MapView var2);

   List overlays();

   void setTilesOverlay(TilesOverlay var1);
}
