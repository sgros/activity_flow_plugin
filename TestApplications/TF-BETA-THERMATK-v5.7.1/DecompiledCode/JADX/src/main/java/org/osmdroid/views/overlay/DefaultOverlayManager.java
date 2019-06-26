package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay.Snappable;

public class DefaultOverlayManager extends AbstractList<Overlay> implements OverlayManager {
    private final CopyOnWriteArrayList<Overlay> mOverlayList = new CopyOnWriteArrayList();
    private TilesOverlay mTilesOverlay;

    /* renamed from: org.osmdroid.views.overlay.DefaultOverlayManager$1 */
    class C02751 implements Iterable<Overlay> {
        C02751() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A:{LOOP_START, SYNTHETIC, Splitter:B:0:0x0000, LOOP:0: B:0:0x0000->B:1:?} */
        /* JADX WARNING: Missing exception handler attribute for start block: B:0:0x0000 */
        /* JADX WARNING: Can't wrap try/catch for region: R(4:0|1|3|2) */
        private java.util.ListIterator<org.osmdroid.views.overlay.Overlay> bulletProofReverseListIterator() {
            /*
            r2 = this;
        L_0x0000:
            r0 = org.osmdroid.views.overlay.DefaultOverlayManager.this;	 Catch:{ IndexOutOfBoundsException -> 0x0000 }
            r0 = r0.mOverlayList;	 Catch:{ IndexOutOfBoundsException -> 0x0000 }
            r1 = org.osmdroid.views.overlay.DefaultOverlayManager.this;	 Catch:{ IndexOutOfBoundsException -> 0x0000 }
            r1 = r1.mOverlayList;	 Catch:{ IndexOutOfBoundsException -> 0x0000 }
            r1 = r1.size();	 Catch:{ IndexOutOfBoundsException -> 0x0000 }
            r0 = r0.listIterator(r1);	 Catch:{ IndexOutOfBoundsException -> 0x0000 }
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.overlay.DefaultOverlayManager$C02751.bulletProofReverseListIterator():java.util.ListIterator");
        }

        public Iterator<Overlay> iterator() {
            final ListIterator bulletProofReverseListIterator = bulletProofReverseListIterator();
            return new Iterator<Overlay>() {
                public boolean hasNext() {
                    return bulletProofReverseListIterator.hasPrevious();
                }

                public Overlay next() {
                    return (Overlay) bulletProofReverseListIterator.previous();
                }

                public void remove() {
                    bulletProofReverseListIterator.remove();
                }
            };
        }
    }

    public DefaultOverlayManager(TilesOverlay tilesOverlay) {
        setTilesOverlay(tilesOverlay);
    }

    public Overlay get(int i) {
        return (Overlay) this.mOverlayList.get(i);
    }

    public int size() {
        return this.mOverlayList.size();
    }

    public void add(int i, Overlay overlay) {
        if (overlay == null) {
            Log.e("OsmDroid", "Attempt to add a null overlay to the collection. This is probably a bug and should be reported!", new Exception());
            return;
        }
        this.mOverlayList.add(i, overlay);
    }

    public Overlay remove(int i) {
        return (Overlay) this.mOverlayList.remove(i);
    }

    public Overlay set(int i, Overlay overlay) {
        if (overlay != null) {
            return (Overlay) this.mOverlayList.set(i, overlay);
        }
        Log.e("OsmDroid", "Attempt to set a null overlay to the collection. This is probably a bug and should be reported!", new Exception());
        return null;
    }

    public void setTilesOverlay(TilesOverlay tilesOverlay) {
        this.mTilesOverlay = tilesOverlay;
    }

    public Iterable<Overlay> overlaysReversed() {
        return new C02751();
    }

    public List<Overlay> overlays() {
        return this.mOverlayList;
    }

    public void onDraw(Canvas canvas, MapView mapView) {
        onDrawHelper(canvas, mapView, mapView.getProjection());
    }

    private void onDrawHelper(Canvas canvas, MapView mapView, Projection projection) {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.protectDisplayedTilesForCache(canvas, projection);
        }
        Iterator it = this.mOverlayList.iterator();
        while (it.hasNext()) {
            Overlay overlay = (Overlay) it.next();
            if (overlay != null && overlay.isEnabled() && (overlay instanceof TilesOverlay)) {
                ((TilesOverlay) overlay).protectDisplayedTilesForCache(canvas, projection);
            }
        }
        tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null && tilesOverlay.isEnabled()) {
            if (mapView != null) {
                this.mTilesOverlay.draw(canvas, mapView, false);
            } else {
                this.mTilesOverlay.draw(canvas, projection);
            }
        }
        it = this.mOverlayList.iterator();
        while (it.hasNext()) {
            Overlay overlay2 = (Overlay) it.next();
            if (overlay2 != null && overlay2.isEnabled()) {
                if (mapView != null) {
                    overlay2.draw(canvas, mapView, false);
                } else {
                    overlay2.draw(canvas, projection);
                }
            }
        }
    }

    public void onDetach(MapView mapView) {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.onDetach(mapView);
        }
        for (Overlay onDetach : overlaysReversed()) {
            onDetach.onDetach(mapView);
        }
        clear();
    }

    public void onPause() {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.onPause();
        }
        for (Overlay onPause : overlaysReversed()) {
            onPause.onPause();
        }
    }

    public void onResume() {
        TilesOverlay tilesOverlay = this.mTilesOverlay;
        if (tilesOverlay != null) {
            tilesOverlay.onResume();
        }
        for (Overlay onResume : overlaysReversed()) {
            onResume.onResume();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent, MapView mapView) {
        for (Overlay onKeyDown : overlaysReversed()) {
            if (onKeyDown.onKeyDown(i, keyEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        for (Overlay onKeyUp : overlaysReversed()) {
            if (onKeyUp.onKeyUp(i, keyEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onTouchEvent : overlaysReversed()) {
            if (onTouchEvent.onTouchEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onTrackballEvent : overlaysReversed()) {
            if (onTrackballEvent.onTrackballEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onSnapToItem(int i, int i2, Point point, IMapView iMapView) {
        for (Overlay overlay : overlaysReversed()) {
            if ((overlay instanceof Snappable) && ((Snappable) overlay).onSnapToItem(i, i2, point, iMapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDoubleTap(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onDoubleTap : overlaysReversed()) {
            if (onDoubleTap.onDoubleTap(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onDoubleTapEvent : overlaysReversed()) {
            if (onDoubleTapEvent.onDoubleTapEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onSingleTapConfirmed : overlaysReversed()) {
            if (onSingleTapConfirmed.onSingleTapConfirmed(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDown(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onDown : overlaysReversed()) {
            if (onDown.onDown(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView) {
        for (Overlay onFling : overlaysReversed()) {
            if (onFling.onFling(motionEvent, motionEvent2, f, f2, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onLongPress(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onLongPress : overlaysReversed()) {
            if (onLongPress.onLongPress(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2, MapView mapView) {
        for (Overlay onScroll : overlaysReversed()) {
            if (onScroll.onScroll(motionEvent, motionEvent2, f, f2, mapView)) {
                return true;
            }
        }
        return false;
    }

    public void onShowPress(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onShowPress : overlaysReversed()) {
            onShowPress.onShowPress(motionEvent, mapView);
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent, MapView mapView) {
        for (Overlay onSingleTapUp : overlaysReversed()) {
            if (onSingleTapUp.onSingleTapUp(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
}
