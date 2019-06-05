// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import android.view.MotionEvent;
import com.github.mikephil.charting.utils.Utils;
import android.view.VelocityTracker;
import android.graphics.Matrix;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.charts.BarLineChartBase;

public class BarLineChartTouchListener extends ChartTouchListener<BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>>>
{
    private IDataSet mClosestDataSetToTouch;
    private MPPointF mDecelerationCurrentPoint;
    private long mDecelerationLastTime;
    private MPPointF mDecelerationVelocity;
    private float mDragTriggerDist;
    private Matrix mMatrix;
    private float mMinScalePointerDistance;
    private float mSavedDist;
    private Matrix mSavedMatrix;
    private float mSavedXDist;
    private float mSavedYDist;
    private MPPointF mTouchPointCenter;
    private MPPointF mTouchStartPoint;
    private VelocityTracker mVelocityTracker;
    
    public BarLineChartTouchListener(final BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> barLineChartBase, final Matrix mMatrix, final float n) {
        super(barLineChartBase);
        this.mMatrix = new Matrix();
        this.mSavedMatrix = new Matrix();
        this.mTouchStartPoint = MPPointF.getInstance(0.0f, 0.0f);
        this.mTouchPointCenter = MPPointF.getInstance(0.0f, 0.0f);
        this.mSavedXDist = 1.0f;
        this.mSavedYDist = 1.0f;
        this.mSavedDist = 1.0f;
        this.mDecelerationLastTime = 0L;
        this.mDecelerationCurrentPoint = MPPointF.getInstance(0.0f, 0.0f);
        this.mDecelerationVelocity = MPPointF.getInstance(0.0f, 0.0f);
        this.mMatrix = mMatrix;
        this.mDragTriggerDist = Utils.convertDpToPixel(n);
        this.mMinScalePointerDistance = Utils.convertDpToPixel(3.5f);
    }
    
    private static float getXDist(final MotionEvent motionEvent) {
        return Math.abs(motionEvent.getX(0) - motionEvent.getX(1));
    }
    
    private static float getYDist(final MotionEvent motionEvent) {
        return Math.abs(motionEvent.getY(0) - motionEvent.getY(1));
    }
    
    private boolean inverted() {
        return (this.mClosestDataSetToTouch == null && ((BarLineChartBase)this.mChart).isAnyAxisInverted()) || (this.mClosestDataSetToTouch != null && ((BarLineChartBase)this.mChart).isInverted(this.mClosestDataSetToTouch.getAxisDependency()));
    }
    
    private static void midPoint(final MPPointF mpPointF, final MotionEvent motionEvent) {
        final float x = motionEvent.getX(0);
        final float x2 = motionEvent.getX(1);
        final float y = motionEvent.getY(0);
        final float y2 = motionEvent.getY(1);
        mpPointF.x = (x + x2) / 2.0f;
        mpPointF.y = (y + y2) / 2.0f;
    }
    
    private void performDrag(final MotionEvent motionEvent, final float n, final float n2) {
        this.mLastGesture = ChartGesture.DRAG;
        this.mMatrix.set(this.mSavedMatrix);
        final OnChartGestureListener onChartGestureListener = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        float n3 = n;
        float n4 = n2;
        if (this.inverted()) {
            if (this.mChart instanceof HorizontalBarChart) {
                n3 = -n;
                n4 = n2;
            }
            else {
                n4 = -n2;
                n3 = n;
            }
        }
        this.mMatrix.postTranslate(n3, n4);
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartTranslate(motionEvent, n3, n4);
        }
    }
    
    private void performHighlightDrag(final MotionEvent motionEvent) {
        final Highlight highlightByTouchPoint = ((BarLineChartBase)this.mChart).getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY());
        if (highlightByTouchPoint != null && !highlightByTouchPoint.equalTo(this.mLastHighlighted)) {
            this.mLastHighlighted = highlightByTouchPoint;
            ((BarLineChartBase)this.mChart).highlightValue(highlightByTouchPoint, true);
        }
    }
    
    private void performZoom(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() >= 2) {
            final OnChartGestureListener onChartGestureListener = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
            final float spacing = spacing(motionEvent);
            if (spacing > this.mMinScalePointerDistance) {
                final MPPointF trans = this.getTrans(this.mTouchPointCenter.x, this.mTouchPointCenter.y);
                final ViewPortHandler viewPortHandler = ((BarLineChartBase)this.mChart).getViewPortHandler();
                final int mTouchMode = this.mTouchMode;
                final int n = 0;
                final int n2 = 0;
                boolean b = false;
                if (mTouchMode == 4) {
                    this.mLastGesture = ChartGesture.PINCH_ZOOM;
                    float n3 = spacing / this.mSavedDist;
                    if (n3 < 1.0f) {
                        b = true;
                    }
                    boolean b2;
                    if (b) {
                        b2 = viewPortHandler.canZoomOutMoreX();
                    }
                    else {
                        b2 = viewPortHandler.canZoomInMoreX();
                    }
                    boolean b3;
                    if (b) {
                        b3 = viewPortHandler.canZoomOutMoreY();
                    }
                    else {
                        b3 = viewPortHandler.canZoomInMoreY();
                    }
                    float n4;
                    if (((BarLineChartBase)this.mChart).isScaleXEnabled()) {
                        n4 = n3;
                    }
                    else {
                        n4 = 1.0f;
                    }
                    if (!((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                        n3 = 1.0f;
                    }
                    if (b3 || b2) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(n4, n3, trans.x, trans.y);
                        if (onChartGestureListener != null) {
                            onChartGestureListener.onChartScale(motionEvent, n4, n3);
                        }
                    }
                }
                else if (this.mTouchMode == 2 && ((BarLineChartBase)this.mChart).isScaleXEnabled()) {
                    this.mLastGesture = ChartGesture.X_ZOOM;
                    final float n5 = getXDist(motionEvent) / this.mSavedXDist;
                    int n6 = n;
                    if (n5 < 1.0f) {
                        n6 = 1;
                    }
                    boolean b4;
                    if (n6 != 0) {
                        b4 = viewPortHandler.canZoomOutMoreX();
                    }
                    else {
                        b4 = viewPortHandler.canZoomInMoreX();
                    }
                    if (b4) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(n5, 1.0f, trans.x, trans.y);
                        if (onChartGestureListener != null) {
                            onChartGestureListener.onChartScale(motionEvent, n5, 1.0f);
                        }
                    }
                }
                else if (this.mTouchMode == 3 && ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                    this.mLastGesture = ChartGesture.Y_ZOOM;
                    final float n7 = getYDist(motionEvent) / this.mSavedYDist;
                    int n8 = n2;
                    if (n7 < 1.0f) {
                        n8 = 1;
                    }
                    boolean b5;
                    if (n8 != 0) {
                        b5 = viewPortHandler.canZoomOutMoreY();
                    }
                    else {
                        b5 = viewPortHandler.canZoomInMoreY();
                    }
                    if (b5) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(1.0f, n7, trans.x, trans.y);
                        if (onChartGestureListener != null) {
                            onChartGestureListener.onChartScale(motionEvent, 1.0f, n7);
                        }
                    }
                }
                MPPointF.recycleInstance(trans);
            }
        }
    }
    
    private void saveTouchStart(final MotionEvent motionEvent) {
        this.mSavedMatrix.set(this.mMatrix);
        this.mTouchStartPoint.x = motionEvent.getX();
        this.mTouchStartPoint.y = motionEvent.getY();
        this.mClosestDataSetToTouch = ((BarLineChartBase)this.mChart).getDataSetByTouchPoint(motionEvent.getX(), motionEvent.getY());
    }
    
    private static float spacing(final MotionEvent motionEvent) {
        final float n = motionEvent.getX(0) - motionEvent.getX(1);
        final float n2 = motionEvent.getY(0) - motionEvent.getY(1);
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    public void computeScroll() {
        final float x = this.mDecelerationVelocity.x;
        float n = 0.0f;
        if (x == 0.0f && this.mDecelerationVelocity.y == 0.0f) {
            return;
        }
        final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        final MPPointF mDecelerationVelocity = this.mDecelerationVelocity;
        mDecelerationVelocity.x *= ((BarLineChartBase)this.mChart).getDragDecelerationFrictionCoef();
        final MPPointF mDecelerationVelocity2 = this.mDecelerationVelocity;
        mDecelerationVelocity2.y *= ((BarLineChartBase)this.mChart).getDragDecelerationFrictionCoef();
        final float n2 = (currentAnimationTimeMillis - this.mDecelerationLastTime) / 1000.0f;
        final float x2 = this.mDecelerationVelocity.x;
        final float y = this.mDecelerationVelocity.y;
        final MPPointF mDecelerationCurrentPoint = this.mDecelerationCurrentPoint;
        mDecelerationCurrentPoint.x += x2 * n2;
        final MPPointF mDecelerationCurrentPoint2 = this.mDecelerationCurrentPoint;
        mDecelerationCurrentPoint2.y += y * n2;
        final MotionEvent obtain = MotionEvent.obtain(currentAnimationTimeMillis, currentAnimationTimeMillis, 2, this.mDecelerationCurrentPoint.x, this.mDecelerationCurrentPoint.y, 0);
        float n3;
        if (((BarLineChartBase)this.mChart).isDragXEnabled()) {
            n3 = this.mDecelerationCurrentPoint.x - this.mTouchStartPoint.x;
        }
        else {
            n3 = 0.0f;
        }
        if (((BarLineChartBase)this.mChart).isDragYEnabled()) {
            n = this.mDecelerationCurrentPoint.y - this.mTouchStartPoint.y;
        }
        this.performDrag(obtain, n3, n);
        obtain.recycle();
        this.mMatrix = ((BarLineChartBase)this.mChart).getViewPortHandler().refresh(this.mMatrix, (View)this.mChart, false);
        this.mDecelerationLastTime = currentAnimationTimeMillis;
        if (Math.abs(this.mDecelerationVelocity.x) < 0.01 && Math.abs(this.mDecelerationVelocity.y) < 0.01) {
            ((BarLineChartBase)this.mChart).calculateOffsets();
            ((BarLineChartBase)this.mChart).postInvalidate();
            this.stopDeceleration();
        }
        else {
            Utils.postInvalidateOnAnimation((View)this.mChart);
        }
    }
    
    public Matrix getMatrix() {
        return this.mMatrix;
    }
    
    public MPPointF getTrans(final float n, float n2) {
        final ViewPortHandler viewPortHandler = ((BarLineChartBase)this.mChart).getViewPortHandler();
        final float offsetLeft = viewPortHandler.offsetLeft();
        if (this.inverted()) {
            n2 = -(n2 - viewPortHandler.offsetTop());
        }
        else {
            n2 = -(((BarLineChartBase)this.mChart).getMeasuredHeight() - n2 - viewPortHandler.offsetBottom());
        }
        return MPPointF.getInstance(n - offsetLeft, n2);
    }
    
    public boolean onDoubleTap(final MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.DOUBLE_TAP;
        final OnChartGestureListener onChartGestureListener = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartDoubleTapped(motionEvent);
        }
        if (((BarLineChartBase)this.mChart).isDoubleTapToZoomEnabled() && ((BarLineScatterCandleBubbleData)((BarLineChartBase)this.mChart).getData()).getEntryCount() > 0) {
            final MPPointF trans = this.getTrans(motionEvent.getX(), motionEvent.getY());
            final BarLineChartBase barLineChartBase = (BarLineChartBase)this.mChart;
            final boolean scaleXEnabled = ((BarLineChartBase)this.mChart).isScaleXEnabled();
            float n = 1.0f;
            float n2;
            if (scaleXEnabled) {
                n2 = 1.4f;
            }
            else {
                n2 = 1.0f;
            }
            if (((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                n = 1.4f;
            }
            barLineChartBase.zoom(n2, n, trans.x, trans.y);
            if (((BarLineChartBase)this.mChart).isLogEnabled()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Double-Tap, Zooming In, x: ");
                sb.append(trans.x);
                sb.append(", y: ");
                sb.append(trans.y);
                Log.i("BarlineChartTouch", sb.toString());
            }
            MPPointF.recycleInstance(trans);
        }
        return super.onDoubleTap(motionEvent);
    }
    
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        this.mLastGesture = ChartGesture.FLING;
        final OnChartGestureListener onChartGestureListener = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartFling(motionEvent, motionEvent2, n, n2);
        }
        return super.onFling(motionEvent, motionEvent2, n, n2);
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.LONG_PRESS;
        final OnChartGestureListener onChartGestureListener = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartLongPressed(motionEvent);
        }
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.SINGLE_TAP;
        final OnChartGestureListener onChartGestureListener = ((BarLineChartBase)this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartSingleTapped(motionEvent);
        }
        if (!((BarLineChartBase)this.mChart).isHighlightPerTapEnabled()) {
            return false;
        }
        this.performHighlight(((BarLineChartBase)this.mChart).getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY()), motionEvent);
        return super.onSingleTapUp(motionEvent);
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        int n = 3;
        if (actionMasked == 3 && this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        if (this.mTouchMode == 0) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        if (!((BarLineChartBase)this.mChart).isDragEnabled() && !((BarLineChartBase)this.mChart).isScaleXEnabled() && !((BarLineChartBase)this.mChart).isScaleYEnabled()) {
            return true;
        }
        final int action = motionEvent.getAction();
        final int n2 = 0;
        switch (action & 0xFF) {
            case 6: {
                Utils.velocityTrackerPointerUpCleanUpIfNecessary(motionEvent, this.mVelocityTracker);
                this.mTouchMode = 5;
                break;
            }
            case 5: {
                if (motionEvent.getPointerCount() >= 2) {
                    ((BarLineChartBase)this.mChart).disableScroll();
                    this.saveTouchStart(motionEvent);
                    this.mSavedXDist = getXDist(motionEvent);
                    this.mSavedYDist = getYDist(motionEvent);
                    this.mSavedDist = spacing(motionEvent);
                    if (this.mSavedDist > 10.0f) {
                        if (((BarLineChartBase)this.mChart).isPinchZoomEnabled()) {
                            this.mTouchMode = 4;
                        }
                        else if (((BarLineChartBase)this.mChart).isScaleXEnabled() != ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                            if (((BarLineChartBase)this.mChart).isScaleXEnabled()) {
                                n = 2;
                            }
                            this.mTouchMode = n;
                        }
                        else {
                            if (this.mSavedXDist > this.mSavedYDist) {
                                n = 2;
                            }
                            this.mTouchMode = n;
                        }
                    }
                    midPoint(this.mTouchPointCenter, motionEvent);
                    break;
                }
                break;
            }
            case 3: {
                this.mTouchMode = 0;
                this.endAction(motionEvent);
                break;
            }
            case 2: {
                if (this.mTouchMode == 1) {
                    ((BarLineChartBase)this.mChart).disableScroll();
                    final boolean dragXEnabled = ((BarLineChartBase)this.mChart).isDragXEnabled();
                    float n3 = 0.0f;
                    float n4;
                    if (dragXEnabled) {
                        n4 = motionEvent.getX() - this.mTouchStartPoint.x;
                    }
                    else {
                        n4 = 0.0f;
                    }
                    if (((BarLineChartBase)this.mChart).isDragYEnabled()) {
                        n3 = motionEvent.getY() - this.mTouchStartPoint.y;
                    }
                    this.performDrag(motionEvent, n4, n3);
                    break;
                }
                if (this.mTouchMode != 2 && this.mTouchMode != 3 && this.mTouchMode != 4) {
                    if (this.mTouchMode != 0 || Math.abs(ChartTouchListener.distance(motionEvent.getX(), this.mTouchStartPoint.x, motionEvent.getY(), this.mTouchStartPoint.y)) <= this.mDragTriggerDist || !((BarLineChartBase)this.mChart).isDragEnabled()) {
                        break;
                    }
                    int n5 = 0;
                    Label_0571: {
                        if (((BarLineChartBase)this.mChart).isFullyZoomedOut()) {
                            n5 = n2;
                            if (((BarLineChartBase)this.mChart).hasNoDragOffset()) {
                                break Label_0571;
                            }
                        }
                        n5 = 1;
                    }
                    if (n5 != 0) {
                        final float abs = Math.abs(motionEvent.getX() - this.mTouchStartPoint.x);
                        final float abs2 = Math.abs(motionEvent.getY() - this.mTouchStartPoint.y);
                        if ((((BarLineChartBase)this.mChart).isDragXEnabled() || abs2 >= abs) && (((BarLineChartBase)this.mChart).isDragYEnabled() || abs2 <= abs)) {
                            this.mLastGesture = ChartGesture.DRAG;
                            this.mTouchMode = 1;
                            break;
                        }
                        break;
                    }
                    else {
                        if (!((BarLineChartBase)this.mChart).isHighlightPerDragEnabled()) {
                            break;
                        }
                        this.mLastGesture = ChartGesture.DRAG;
                        if (((BarLineChartBase)this.mChart).isHighlightPerDragEnabled()) {
                            this.performHighlightDrag(motionEvent);
                            break;
                        }
                        break;
                    }
                }
                else {
                    ((BarLineChartBase)this.mChart).disableScroll();
                    if (((BarLineChartBase)this.mChart).isScaleXEnabled() || ((BarLineChartBase)this.mChart).isScaleYEnabled()) {
                        this.performZoom(motionEvent);
                        break;
                    }
                    break;
                }
                break;
            }
            case 1: {
                final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                final int pointerId = motionEvent.getPointerId(0);
                mVelocityTracker.computeCurrentVelocity(1000, (float)Utils.getMaximumFlingVelocity());
                final float yVelocity = mVelocityTracker.getYVelocity(pointerId);
                final float xVelocity = mVelocityTracker.getXVelocity(pointerId);
                if ((Math.abs(xVelocity) > Utils.getMinimumFlingVelocity() || Math.abs(yVelocity) > Utils.getMinimumFlingVelocity()) && this.mTouchMode == 1 && ((BarLineChartBase)this.mChart).isDragDecelerationEnabled()) {
                    this.stopDeceleration();
                    this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDecelerationCurrentPoint.x = motionEvent.getX();
                    this.mDecelerationCurrentPoint.y = motionEvent.getY();
                    this.mDecelerationVelocity.x = xVelocity;
                    this.mDecelerationVelocity.y = yVelocity;
                    Utils.postInvalidateOnAnimation((View)this.mChart);
                }
                if (this.mTouchMode == 2 || this.mTouchMode == 3 || this.mTouchMode == 4 || this.mTouchMode == 5) {
                    ((BarLineChartBase)this.mChart).calculateOffsets();
                    ((BarLineChartBase)this.mChart).postInvalidate();
                }
                this.mTouchMode = 0;
                ((BarLineChartBase)this.mChart).enableScroll();
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                this.endAction(motionEvent);
                break;
            }
            case 0: {
                this.startAction(motionEvent);
                this.stopDeceleration();
                this.saveTouchStart(motionEvent);
                break;
            }
        }
        this.mMatrix = ((BarLineChartBase)this.mChart).getViewPortHandler().refresh(this.mMatrix, (View)this.mChart, true);
        return true;
    }
    
    public void setDragTriggerDist(final float n) {
        this.mDragTriggerDist = Utils.convertDpToPixel(n);
    }
    
    public void stopDeceleration() {
        this.mDecelerationVelocity.x = 0.0f;
        this.mDecelerationVelocity.y = 0.0f;
    }
}
