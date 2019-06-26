// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views.overlay;

import java.util.ListIterator;
import java.util.List;
import org.osmdroid.api.IMapView;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.util.Log;
import java.util.Iterator;
import org.osmdroid.views.Projection;
import org.osmdroid.views.MapView;
import android.graphics.Canvas;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.AbstractList;

public class DefaultOverlayManager extends AbstractList<Overlay> implements OverlayManager
{
    private final CopyOnWriteArrayList<Overlay> mOverlayList;
    private TilesOverlay mTilesOverlay;
    
    public DefaultOverlayManager(final TilesOverlay tilesOverlay) {
        this.setTilesOverlay(tilesOverlay);
        this.mOverlayList = new CopyOnWriteArrayList<Overlay>();
    }
    
    private void onDrawHelper(final Canvas canvas, final MapView mapView, final Projection projection) {
        final TilesOverlay mTilesOverlay = this.mTilesOverlay;
        if (mTilesOverlay != null) {
            mTilesOverlay.protectDisplayedTilesForCache(canvas, projection);
        }
        for (final Overlay overlay : this.mOverlayList) {
            if (overlay != null && overlay.isEnabled() && overlay instanceof TilesOverlay) {
                ((TilesOverlay)overlay).protectDisplayedTilesForCache(canvas, projection);
            }
        }
        final TilesOverlay mTilesOverlay2 = this.mTilesOverlay;
        if (mTilesOverlay2 != null && mTilesOverlay2.isEnabled()) {
            if (mapView != null) {
                this.mTilesOverlay.draw(canvas, mapView, false);
            }
            else {
                this.mTilesOverlay.draw(canvas, projection);
            }
        }
        for (final Overlay overlay2 : this.mOverlayList) {
            if (overlay2 != null && overlay2.isEnabled()) {
                if (mapView != null) {
                    overlay2.draw(canvas, mapView, false);
                }
                else {
                    overlay2.draw(canvas, projection);
                }
            }
        }
    }
    
    @Override
    public void add(final int index, final Overlay element) {
        if (element == null) {
            Log.e("OsmDroid", "Attempt to add a null overlay to the collection. This is probably a bug and should be reported!", (Throwable)new Exception());
        }
        else {
            this.mOverlayList.add(index, element);
        }
    }
    
    @Override
    public Overlay get(final int index) {
        return this.mOverlayList.get(index);
    }
    
    @Override
    public void onDetach(final MapView mapView) {
        final TilesOverlay mTilesOverlay = this.mTilesOverlay;
        if (mTilesOverlay != null) {
            mTilesOverlay.onDetach(mapView);
        }
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            iterator.next().onDetach(mapView);
        }
        this.clear();
    }
    
    @Override
    public boolean onDoubleTap(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onDoubleTap(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onDoubleTapEvent(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onDoubleTapEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onDown(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onDown(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onDraw(final Canvas canvas, final MapView mapView) {
        this.onDrawHelper(canvas, mapView, mapView.getProjection());
    }
    
    @Override
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onFling(motionEvent, motionEvent2, n, n2, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onKeyDown(final int n, final KeyEvent keyEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onKeyDown(n, keyEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onKeyUp(final int n, final KeyEvent keyEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onKeyUp(n, keyEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onLongPress(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onLongPress(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onPause() {
        final TilesOverlay mTilesOverlay = this.mTilesOverlay;
        if (mTilesOverlay != null) {
            mTilesOverlay.onPause();
        }
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            iterator.next().onPause();
        }
    }
    
    @Override
    public void onResume() {
        final TilesOverlay mTilesOverlay = this.mTilesOverlay;
        if (mTilesOverlay != null) {
            mTilesOverlay.onResume();
        }
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            iterator.next().onResume();
        }
    }
    
    @Override
    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onScroll(motionEvent, motionEvent2, n, n2, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onShowPress(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            iterator.next().onShowPress(motionEvent, mapView);
        }
    }
    
    @Override
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onSingleTapConfirmed(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onSingleTapUp(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onSingleTapUp(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onSnapToItem(final int n, final int n2, final Point point, final IMapView mapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay instanceof Overlay.Snappable && ((Overlay.Snappable)overlay).onSnapToItem(n, n2, point, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onTouchEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onTrackballEvent(final MotionEvent motionEvent, final MapView mapView) {
        final Iterator<Overlay> iterator = this.overlaysReversed().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onTrackballEvent(motionEvent, mapView)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Overlay> overlays() {
        return this.mOverlayList;
    }
    
    public Iterable<Overlay> overlaysReversed() {
        return new Iterable<Overlay>() {
            private ListIterator<Overlay> bulletProofReverseListIterator() {
                try {
                    return DefaultOverlayManager.this.mOverlayList.listIterator(DefaultOverlayManager.this.mOverlayList.size());
                }
                catch (IndexOutOfBoundsException ex) {
                    return DefaultOverlayManager.this.mOverlayList.listIterator(DefaultOverlayManager.this.mOverlayList.size());
                }
            }
            
            @Override
            public Iterator<Overlay> iterator() {
                return new Iterator<Overlay>() {
                    final /* synthetic */ ListIterator val$i = DefaultOverlayManager$1.this.bulletProofReverseListIterator();
                    
                    @Override
                    public boolean hasNext() {
                        return this.val$i.hasPrevious();
                    }
                    
                    @Override
                    public Overlay next() {
                        return this.val$i.previous();
                    }
                    
                    @Override
                    public void remove() {
                        this.val$i.remove();
                    }
                };
            }
        };
    }
    
    @Override
    public Overlay remove(final int index) {
        return this.mOverlayList.remove(index);
    }
    
    @Override
    public Overlay set(final int index, final Overlay element) {
        if (element == null) {
            Log.e("OsmDroid", "Attempt to set a null overlay to the collection. This is probably a bug and should be reported!", (Throwable)new Exception());
            return null;
        }
        return this.mOverlayList.set(index, element);
    }
    
    @Override
    public void setTilesOverlay(final TilesOverlay mTilesOverlay) {
        this.mTilesOverlay = mTilesOverlay;
    }
    
    @Override
    public int size() {
        return this.mOverlayList.size();
    }
}
