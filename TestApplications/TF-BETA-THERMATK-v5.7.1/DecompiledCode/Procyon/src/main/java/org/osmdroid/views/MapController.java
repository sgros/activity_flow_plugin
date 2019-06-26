// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views;

import java.util.LinkedList;
import org.osmdroid.util.TileSystem;
import android.annotation.TargetApi;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.MyMath;
import java.util.Iterator;
import android.view.animation.Animation;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.events.MapListener;
import android.view.View;
import android.graphics.Point;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.Animator$AnimatorListener;
import android.animation.ValueAnimator;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.api.IGeoPoint;
import android.view.animation.Animation$AnimationListener;
import org.osmdroid.config.Configuration;
import android.os.Build$VERSION;
import android.view.animation.ScaleAnimation;
import android.animation.Animator;
import org.osmdroid.api.IMapController;

public class MapController implements IMapController, OnFirstLayoutListener
{
    private Animator mCurrentAnimator;
    protected final MapView mMapView;
    private ReplayController mReplayController;
    private double mTargetZoomLevel;
    private ScaleAnimation mZoomInAnimationOld;
    private ScaleAnimation mZoomOutAnimationOld;
    
    public MapController(final MapView mMapView) {
        this.mTargetZoomLevel = 0.0;
        this.mMapView = mMapView;
        this.mReplayController = new ReplayController();
        if (!this.mMapView.isLayoutOccurred()) {
            this.mMapView.addOnFirstLayoutListener((MapView.OnFirstLayoutListener)this);
        }
        if (Build$VERSION.SDK_INT < 11) {
            final ZoomAnimationListener zoomAnimationListener = new ZoomAnimationListener(this);
            this.mZoomInAnimationOld = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 1, 0.5f, 1, 0.5f);
            this.mZoomOutAnimationOld = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 1, 0.5f, 1, 0.5f);
            this.mZoomInAnimationOld.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
            this.mZoomOutAnimationOld.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
            this.mZoomInAnimationOld.setAnimationListener((Animation$AnimationListener)zoomAnimationListener);
            this.mZoomOutAnimationOld.setAnimationListener((Animation$AnimationListener)zoomAnimationListener);
        }
    }
    
    public void animateTo(int n, int n2) {
        if (!this.mMapView.isLayoutOccurred()) {
            this.mReplayController.animateTo(n, n2);
            return;
        }
        if (!this.mMapView.isAnimating()) {
            final MapView mMapView = this.mMapView;
            mMapView.mIsFlinging = false;
            final int n3 = (int)mMapView.getMapScrollX();
            final int n4 = (int)this.mMapView.getMapScrollY();
            n -= this.mMapView.getWidth() / 2;
            n2 -= this.mMapView.getHeight() / 2;
            if (n != n3 || n2 != n4) {
                this.mMapView.getScroller().startScroll(n3, n4, n, n2, Configuration.getInstance().getAnimationSpeedDefault());
                this.mMapView.postInvalidate();
            }
        }
    }
    
    @Override
    public void animateTo(final IGeoPoint geoPoint) {
        this.animateTo(geoPoint, null, null);
    }
    
    @Override
    public void animateTo(final IGeoPoint geoPoint, final Double n, final Long n2) {
        this.animateTo(geoPoint, n, n2, null);
    }
    
    public void animateTo(final IGeoPoint geoPoint, final Double n, final Long n2, final Float n3) {
        this.animateTo(geoPoint, n, n2, n3, null);
    }
    
    public void animateTo(final IGeoPoint geoPoint, final Double n, final Long n2, final Float n3, final Boolean b) {
        if (!this.mMapView.isLayoutOccurred()) {
            this.mReplayController.animateTo(geoPoint, n, n2, n3, b);
            return;
        }
        if (Build$VERSION.SDK_INT >= 11) {
            final MapAnimatorListener mapAnimatorListener = new MapAnimatorListener(this, this.mMapView.getZoomLevelDouble(), n, new GeoPoint(this.mMapView.getProjection().getCurrentCenter()), geoPoint, this.mMapView.getMapOrientation(), n3, b);
            final ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
            ofFloat.addListener((Animator$AnimatorListener)mapAnimatorListener);
            ofFloat.addUpdateListener((ValueAnimator$AnimatorUpdateListener)mapAnimatorListener);
            if (n2 == null) {
                ofFloat.setDuration((long)Configuration.getInstance().getAnimationSpeedDefault());
            }
            else {
                ofFloat.setDuration((long)n2);
            }
            final Animator mCurrentAnimator = this.mCurrentAnimator;
            if (mCurrentAnimator != null) {
                mCurrentAnimator.end();
            }
            ((ValueAnimator)(this.mCurrentAnimator = (Animator)ofFloat)).start();
            return;
        }
        final Point pixels = this.mMapView.getProjection().toPixels(geoPoint, null);
        this.animateTo(pixels.x, pixels.y);
    }
    
    protected void onAnimationEnd() {
        this.mMapView.mIsAnimating.set(false);
        this.mMapView.resetMultiTouchScale();
        if (Build$VERSION.SDK_INT >= 11) {
            this.mCurrentAnimator = null;
        }
        else {
            this.mMapView.clearAnimation();
            this.mZoomInAnimationOld.reset();
            this.mZoomOutAnimationOld.reset();
            this.setZoom(this.mTargetZoomLevel);
        }
        this.mMapView.invalidate();
    }
    
    protected void onAnimationStart() {
        this.mMapView.mIsAnimating.set(true);
    }
    
    @Override
    public void onFirstLayout(final View view, final int n, final int n2, final int n3, final int n4) {
        this.mReplayController.replayCalls();
    }
    
    @Override
    public void setCenter(final IGeoPoint geoPoint) {
        if (!this.mMapView.isLayoutOccurred()) {
            this.mReplayController.setCenter(geoPoint);
            return;
        }
        this.mMapView.setExpectedCenter(geoPoint);
    }
    
    @Override
    public double setZoom(final double zoomLevel) {
        return this.mMapView.setZoomLevel(zoomLevel);
    }
    
    @Override
    public void stopAnimation(final boolean b) {
        if (!this.mMapView.getScroller().isFinished()) {
            if (b) {
                final MapView mMapView = this.mMapView;
                mMapView.mIsFlinging = false;
                mMapView.getScroller().abortAnimation();
            }
            else {
                this.stopPanning();
            }
        }
        if (Build$VERSION.SDK_INT >= 11) {
            final Animator mCurrentAnimator = this.mCurrentAnimator;
            if (this.mMapView.mIsAnimating.get()) {
                if (b) {
                    mCurrentAnimator.end();
                }
                else {
                    mCurrentAnimator.cancel();
                }
            }
        }
        else if (this.mMapView.mIsAnimating.get()) {
            this.mMapView.clearAnimation();
        }
    }
    
    public void stopPanning() {
        final MapView mMapView = this.mMapView;
        mMapView.mIsFlinging = false;
        mMapView.getScroller().forceFinished(true);
    }
    
    @Override
    public boolean zoomIn() {
        return this.zoomIn(null);
    }
    
    public boolean zoomIn(final Long n) {
        return this.zoomTo(this.mMapView.getZoomLevelDouble() + 1.0, n);
    }
    
    @Override
    public boolean zoomInFixing(final int n, final int n2) {
        return this.zoomInFixing(n, n2, null);
    }
    
    public boolean zoomInFixing(final int n, final int n2, final Long n3) {
        return this.zoomToFixing(this.mMapView.getZoomLevelDouble() + 1.0, n, n2, n3);
    }
    
    @Override
    public boolean zoomOut() {
        return this.zoomOut(null);
    }
    
    public boolean zoomOut(final Long n) {
        return this.zoomTo(this.mMapView.getZoomLevelDouble() - 1.0, n);
    }
    
    @Deprecated
    @Override
    public boolean zoomOutFixing(final int n, final int n2) {
        return this.zoomToFixing(this.mMapView.getZoomLevelDouble() - 1.0, n, n2, null);
    }
    
    public boolean zoomTo(final double n, final Long n2) {
        return this.zoomToFixing(n, this.mMapView.getWidth() / 2, this.mMapView.getHeight() / 2, n2);
    }
    
    public boolean zoomToFixing(double d, final int n, final int n2, final Long n3) {
        if (d > this.mMapView.getMaxZoomLevel()) {
            d = this.mMapView.getMaxZoomLevel();
        }
        double minZoomLevel = d;
        if (d < this.mMapView.getMinZoomLevel()) {
            minZoomLevel = this.mMapView.getMinZoomLevel();
        }
        d = this.mMapView.getZoomLevelDouble();
        if ((minZoomLevel >= d || !this.mMapView.canZoomOut()) && (minZoomLevel <= d || !this.mMapView.canZoomIn())) {
            return false;
        }
        if (this.mMapView.mIsAnimating.getAndSet(true)) {
            return false;
        }
        ZoomEvent zoomEvent = null;
        for (final MapListener mapListener : this.mMapView.mListners) {
            if (zoomEvent == null) {
                zoomEvent = new ZoomEvent(this.mMapView, minZoomLevel);
            }
            mapListener.onZoom(zoomEvent);
        }
        this.mMapView.setMultiTouchScaleInitPoint((float)n, (float)n2);
        this.mMapView.startAnimation();
        final float n4 = (float)Math.pow(2.0, minZoomLevel - d);
        if (Build$VERSION.SDK_INT >= 11) {
            final MapAnimatorListener mapAnimatorListener = new MapAnimatorListener(this, d, minZoomLevel, null, null, null, null, null);
            final ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
            ofFloat.addListener((Animator$AnimatorListener)mapAnimatorListener);
            ofFloat.addUpdateListener((ValueAnimator$AnimatorUpdateListener)mapAnimatorListener);
            if (n3 == null) {
                ofFloat.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
            }
            else {
                ofFloat.setDuration((long)n3);
            }
            ((ValueAnimator)(this.mCurrentAnimator = (Animator)ofFloat)).start();
            return true;
        }
        this.mTargetZoomLevel = minZoomLevel;
        if (minZoomLevel > d) {
            this.mMapView.startAnimation((Animation)this.mZoomInAnimationOld);
        }
        else {
            this.mMapView.startAnimation((Animation)this.mZoomOutAnimationOld);
        }
        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, n4, 1.0f, n4, 1, 0.5f, 1, 0.5f);
        if (n3 == null) {
            scaleAnimation.setDuration((long)Configuration.getInstance().getAnimationSpeedShort());
        }
        else {
            scaleAnimation.setDuration((long)n3);
        }
        scaleAnimation.setAnimationListener((Animation$AnimationListener)new ZoomAnimationListener(this));
        return true;
    }
    
    public void zoomToSpan(double max, final double n) {
        if (max > 0.0) {
            if (n > 0.0) {
                if (!this.mMapView.isLayoutOccurred()) {
                    this.mReplayController.zoomToSpan(max, n);
                    return;
                }
                final BoundingBox boundingBox = this.mMapView.getProjection().getBoundingBox();
                final double zoomLevel = this.mMapView.getProjection().getZoomLevel();
                max = Math.max(max / boundingBox.getLatitudeSpan(), n / boundingBox.getLongitudeSpan());
                if (max > 1.0) {
                    final MapView mMapView = this.mMapView;
                    max = MyMath.getNextSquareNumberAbove((float)max);
                    Double.isNaN(max);
                    mMapView.setZoomLevel(zoomLevel - max);
                }
                else if (max < 0.5) {
                    final MapView mMapView2 = this.mMapView;
                    max = MyMath.getNextSquareNumberAbove(1.0f / (float)max);
                    Double.isNaN(max);
                    mMapView2.setZoomLevel(zoomLevel + max - 1.0);
                }
            }
        }
    }
    
    public void zoomToSpan(final int n, final int n2) {
        final double v = n;
        Double.isNaN(v);
        final double v2 = n2;
        Double.isNaN(v2);
        this.zoomToSpan(v * 1.0E-6, v2 * 1.0E-6);
    }
    
    @TargetApi(11)
    private static class MapAnimatorListener implements Animator$AnimatorListener, ValueAnimator$AnimatorUpdateListener
    {
        private final GeoPoint mCenter;
        private final IGeoPoint mCenterEnd;
        private final IGeoPoint mCenterStart;
        private final MapController mMapController;
        private final Float mOrientationSpan;
        private final Float mOrientationStart;
        private final Double mZoomEnd;
        private final Double mZoomStart;
        
        public MapAnimatorListener(final MapController mMapController, final Double mZoomStart, final Double mZoomEnd, final IGeoPoint mCenterStart, final IGeoPoint mCenterEnd, final Float mOrientationStart, final Float n, final Boolean b) {
            this.mCenter = new GeoPoint(0.0, 0.0);
            this.mMapController = mMapController;
            this.mZoomStart = mZoomStart;
            this.mZoomEnd = mZoomEnd;
            this.mCenterStart = mCenterStart;
            this.mCenterEnd = mCenterEnd;
            if (n == null) {
                this.mOrientationStart = null;
                this.mOrientationSpan = null;
            }
            else {
                this.mOrientationStart = mOrientationStart;
                this.mOrientationSpan = (float)MyMath.getAngleDifference(this.mOrientationStart, n, b);
            }
        }
        
        public void onAnimationCancel(final Animator animator) {
            this.mMapController.onAnimationEnd();
        }
        
        public void onAnimationEnd(final Animator animator) {
            this.mMapController.onAnimationEnd();
        }
        
        public void onAnimationRepeat(final Animator animator) {
        }
        
        public void onAnimationStart(final Animator animator) {
            this.mMapController.onAnimationStart();
        }
        
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            final float floatValue = (float)valueAnimator.getAnimatedValue();
            if (this.mZoomEnd != null) {
                final double doubleValue = this.mZoomStart;
                final double doubleValue2 = this.mZoomEnd;
                final double doubleValue3 = this.mZoomStart;
                final double v = floatValue;
                Double.isNaN(v);
                this.mMapController.mMapView.setZoomLevel(doubleValue + (doubleValue2 - doubleValue3) * v);
            }
            if (this.mOrientationSpan != null) {
                this.mMapController.mMapView.setMapOrientation(this.mOrientationStart + this.mOrientationSpan * floatValue);
            }
            if (this.mCenterEnd != null) {
                final MapView mMapView = this.mMapController.mMapView;
                final TileSystem tileSystem = MapView.getTileSystem();
                final double cleanLongitude = tileSystem.cleanLongitude(this.mCenterStart.getLongitude());
                final double cleanLongitude2 = tileSystem.cleanLongitude(this.mCenterEnd.getLongitude());
                final double n = floatValue;
                Double.isNaN(n);
                final double cleanLongitude3 = tileSystem.cleanLongitude(cleanLongitude + (cleanLongitude2 - cleanLongitude) * n);
                final double cleanLatitude = tileSystem.cleanLatitude(this.mCenterStart.getLatitude());
                final double cleanLatitude2 = tileSystem.cleanLatitude(this.mCenterEnd.getLatitude());
                Double.isNaN(n);
                this.mCenter.setCoords(tileSystem.cleanLatitude(cleanLatitude + (cleanLatitude2 - cleanLatitude) * n), cleanLongitude3);
                this.mMapController.mMapView.setExpectedCenter(this.mCenter);
            }
            this.mMapController.mMapView.invalidate();
        }
    }
    
    private class ReplayController
    {
        private LinkedList<ReplayClass> mReplayList;
        
        private ReplayController() {
            this.mReplayList = new LinkedList<ReplayClass>();
        }
        
        public void animateTo(final int n, final int n2) {
            this.mReplayList.add(new ReplayClass(ReplayType.AnimateToPoint, new Point(n, n2), null));
        }
        
        public void animateTo(final IGeoPoint geoPoint, final Double n, final Long n2, final Float n3, final Boolean b) {
            this.mReplayList.add(new ReplayClass(ReplayType.AnimateToGeoPoint, null, geoPoint, n, n2, n3, b));
        }
        
        public void replayCalls() {
            for (final ReplayClass replayClass : this.mReplayList) {
                final int n = MapController$1.$SwitchMap$org$osmdroid$views$MapController$ReplayType[replayClass.mReplayType.ordinal()];
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                continue;
                            }
                            if (replayClass.mPoint == null) {
                                continue;
                            }
                            MapController.this.zoomToSpan(replayClass.mPoint.x, replayClass.mPoint.y);
                        }
                        else {
                            if (replayClass.mGeoPoint == null) {
                                continue;
                            }
                            MapController.this.setCenter(replayClass.mGeoPoint);
                        }
                    }
                    else {
                        if (replayClass.mPoint == null) {
                            continue;
                        }
                        MapController.this.animateTo(replayClass.mPoint.x, replayClass.mPoint.y);
                    }
                }
                else {
                    if (replayClass.mGeoPoint == null) {
                        continue;
                    }
                    MapController.this.animateTo(replayClass.mGeoPoint, replayClass.mZoom, replayClass.mSpeed, replayClass.mOrientation, replayClass.mClockwise);
                }
            }
            this.mReplayList.clear();
        }
        
        public void setCenter(final IGeoPoint geoPoint) {
            this.mReplayList.add(new ReplayClass(ReplayType.SetCenterPoint, null, geoPoint));
        }
        
        public void zoomToSpan(final double n, final double n2) {
            this.mReplayList.add(new ReplayClass(ReplayType.ZoomToSpanPoint, new Point((int)(n * 1000000.0), (int)(n2 * 1000000.0)), null));
        }
        
        private class ReplayClass
        {
            private final Boolean mClockwise;
            private IGeoPoint mGeoPoint;
            private final Float mOrientation;
            private Point mPoint;
            private ReplayType mReplayType;
            private final Long mSpeed;
            private final Double mZoom;
            
            public ReplayClass(final ReplayController replayController, final ReplayType replayType, final Point point, final IGeoPoint geoPoint) {
                this(replayController, replayType, point, geoPoint, null, null, null, null);
            }
            
            public ReplayClass(final ReplayType mReplayType, final Point mPoint, final IGeoPoint mGeoPoint, final Double mZoom, final Long mSpeed, final Float mOrientation, final Boolean mClockwise) {
                this.mReplayType = mReplayType;
                this.mPoint = mPoint;
                this.mGeoPoint = mGeoPoint;
                this.mSpeed = mSpeed;
                this.mZoom = mZoom;
                this.mOrientation = mOrientation;
                this.mClockwise = mClockwise;
            }
        }
    }
    
    private enum ReplayType
    {
        AnimateToGeoPoint, 
        AnimateToPoint, 
        SetCenterPoint, 
        ZoomToSpanPoint;
    }
    
    protected static class ZoomAnimationListener implements Animation$AnimationListener
    {
        private MapController mMapController;
        
        public ZoomAnimationListener(final MapController mMapController) {
            this.mMapController = mMapController;
        }
        
        public void onAnimationEnd(final Animation animation) {
            this.mMapController.onAnimationEnd();
        }
        
        public void onAnimationRepeat(final Animation animation) {
        }
        
        public void onAnimationStart(final Animation animation) {
            this.mMapController.onAnimationStart();
        }
    }
}
