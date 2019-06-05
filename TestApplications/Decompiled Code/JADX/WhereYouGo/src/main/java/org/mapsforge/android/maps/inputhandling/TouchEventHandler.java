package org.mapsforge.android.maps.inputhandling;

import android.content.Context;
import android.support.p000v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.overlay.Overlay;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;

public class TouchEventHandler {
    private static final int INVALID_POINTER_ID = -1;
    private int activePointerId;
    private final float doubleTapDelta;
    private final int doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
    private long doubleTouchStart;
    private final float mapMoveDelta;
    private MapView mapView;
    private final MapViewPosition mapViewPosition;
    private boolean moveThresholdReached;
    private boolean previousEventTap;
    private Point previousPosition;
    private Point previousTapPosition;
    private long previousTapTime;
    private final ScaleGestureDetector scaleGestureDetector;
    protected Timer singleTapActionTimer;

    public static int getAction(MotionEvent motionEvent) {
        return motionEvent.getAction() & 255;
    }

    public TouchEventHandler(Context context, MapView mapView) {
        this.mapView = mapView;
        this.mapViewPosition = mapView.getMapViewPosition();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mapMoveDelta = (float) viewConfiguration.getScaledTouchSlop();
        this.doubleTapDelta = (float) viewConfiguration.getScaledDoubleTapSlop();
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener(mapView));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = getAction(motionEvent);
        if (action != 2 || motionEvent.getPointerCount() > 1) {
            this.scaleGestureDetector.onTouchEvent(motionEvent);
        }
        switch (action) {
            case 0:
                return onActionDown(motionEvent);
            case 1:
                return onActionUp(motionEvent);
            case 2:
                return onActionMove(motionEvent);
            case 3:
                return onActionCancel();
            case 5:
                return onActionPointerDown(motionEvent);
            case 6:
                return onActionPointerUp(motionEvent);
            default:
                return false;
        }
    }

    private boolean onActionCancel() {
        this.activePointerId = -1;
        return true;
    }

    private boolean onActionDown(MotionEvent motionEvent) {
        this.activePointerId = motionEvent.getPointerId(0);
        this.previousPosition = new Point((double) motionEvent.getX(), (double) motionEvent.getY());
        this.moveThresholdReached = false;
        return true;
    }

    private boolean onActionMove(MotionEvent motionEvent) {
        if (!this.scaleGestureDetector.isInProgress()) {
            int pointerIndex = motionEvent.findPointerIndex(this.activePointerId);
            float moveX = (float) (((double) motionEvent.getX(pointerIndex)) - this.previousPosition.f68x);
            float moveY = (float) (((double) motionEvent.getY(pointerIndex)) - this.previousPosition.f69y);
            if (this.moveThresholdReached) {
                this.previousPosition = new Point((double) motionEvent.getX(pointerIndex), (double) motionEvent.getY(pointerIndex));
                this.mapViewPosition.moveCenter(moveX, moveY);
            } else if (Math.abs(moveX) > this.mapMoveDelta || Math.abs(moveY) > this.mapMoveDelta) {
                this.moveThresholdReached = true;
                this.previousPosition = new Point((double) motionEvent.getX(pointerIndex), (double) motionEvent.getY(pointerIndex));
            }
        }
        return true;
    }

    private boolean onActionPointerDown(MotionEvent motionEvent) {
        this.doubleTouchStart = motionEvent.getEventTime();
        return true;
    }

    private boolean onActionPointerUp(MotionEvent motionEvent) {
        int pointerIndex = (motionEvent.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (motionEvent.getPointerId(pointerIndex) == this.activePointerId) {
            if (pointerIndex == 0) {
                pointerIndex = 1;
            } else {
                pointerIndex = 0;
            }
            this.activePointerId = motionEvent.getPointerId(pointerIndex);
            this.previousPosition = new Point((double) motionEvent.getX(pointerIndex), (double) motionEvent.getY(pointerIndex));
        }
        if (motionEvent.getEventTime() - this.doubleTouchStart < ((long) this.doubleTapTimeout)) {
            this.previousEventTap = false;
            this.mapViewPosition.zoomOut();
        }
        return true;
    }

    private boolean onActionUp(MotionEvent motionEvent) {
        int pointerIndex = motionEvent.findPointerIndex(this.activePointerId);
        this.activePointerId = -1;
        if (this.moveThresholdReached) {
            this.previousEventTap = false;
            return true;
        }
        if (this.previousEventTap) {
            double diffX = Math.abs(((double) motionEvent.getX(pointerIndex)) - this.previousTapPosition.f68x);
            double diffY = Math.abs(((double) motionEvent.getY(pointerIndex)) - this.previousTapPosition.f69y);
            long doubleTapTime = motionEvent.getEventTime() - this.previousTapTime;
            if (diffX < ((double) this.doubleTapDelta) && diffY < ((double) this.doubleTapDelta) && doubleTapTime < ((long) this.doubleTapTimeout)) {
                this.previousEventTap = false;
                this.mapViewPosition.zoomIn();
                try {
                    if (this.singleTapActionTimer != null) {
                        this.singleTapActionTimer.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        this.previousEventTap = true;
        this.previousTapPosition = new Point((double) motionEvent.getX(pointerIndex), (double) motionEvent.getY(pointerIndex));
        this.previousTapTime = motionEvent.getEventTime();
        GeoPoint pnt = null;
        try {
            pnt = this.mapView.getProjection().fromPixels((int) motionEvent.getX(pointerIndex), (int) motionEvent.getY(pointerIndex));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (pnt != null) {
            final GeoPoint geoPoint;
            GeoPoint tapPoint = pnt;
            final MapView mapViewFinal = this.mapView;
            boolean tapInvoked = false;
            synchronized (this.mapView.getOverlays()) {
                for (final Overlay ovl : this.mapView.getOverlays()) {
                    try {
                        Method checkItemHit = ovl.getClass().getMethod("checkItemHit", new Class[]{GeoPoint.class, MapView.class});
                        final Method onTap = ovl.getClass().getMethod("onTap", new Class[]{GeoPoint.class});
                        if (((Boolean) checkItemHit.invoke(ovl, new Object[]{tapPoint, this.mapView})).booleanValue()) {
                            tapInvoked = true;
                            this.singleTapActionTimer = new Timer();
                            geoPoint = tapPoint;
                            this.singleTapActionTimer.schedule(new TimerTask() {
                                public void run() {
                                    try {
                                        onTap.invoke(ovl, new Object[]{geoPoint});
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, ((long) this.doubleTapTimeout) + 10);
                            break;
                        }
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
            }
            if (!tapInvoked) {
                this.singleTapActionTimer = new Timer();
                geoPoint = tapPoint;
                this.singleTapActionTimer.schedule(new TimerTask() {
                    public void run() {
                        mapViewFinal.onTapEvent(geoPoint);
                    }
                }, ((long) this.doubleTapTimeout) + 10);
            }
        }
        return true;
    }
}
