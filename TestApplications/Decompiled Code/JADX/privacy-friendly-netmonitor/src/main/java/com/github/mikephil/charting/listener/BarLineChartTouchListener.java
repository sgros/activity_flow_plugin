package com.github.mikephil.charting.listener;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class BarLineChartTouchListener extends ChartTouchListener<BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>>> {
    private IDataSet mClosestDataSetToTouch;
    private MPPointF mDecelerationCurrentPoint = MPPointF.getInstance(0.0f, 0.0f);
    private long mDecelerationLastTime = 0;
    private MPPointF mDecelerationVelocity = MPPointF.getInstance(0.0f, 0.0f);
    private float mDragTriggerDist;
    private Matrix mMatrix = new Matrix();
    private float mMinScalePointerDistance;
    private float mSavedDist = 1.0f;
    private Matrix mSavedMatrix = new Matrix();
    private float mSavedXDist = 1.0f;
    private float mSavedYDist = 1.0f;
    private MPPointF mTouchPointCenter = MPPointF.getInstance(0.0f, 0.0f);
    private MPPointF mTouchStartPoint = MPPointF.getInstance(0.0f, 0.0f);
    private VelocityTracker mVelocityTracker;

    public BarLineChartTouchListener(BarLineChartBase<? extends BarLineScatterCandleBubbleData<? extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>> barLineChartBase, Matrix matrix, float f) {
        super(barLineChartBase);
        this.mMatrix = matrix;
        this.mDragTriggerDist = Utils.convertDpToPixel(f);
        this.mMinScalePointerDistance = Utils.convertDpToPixel(3.5f);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int i = 3;
        if (motionEvent.getActionMasked() == 3 && this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
        if (this.mTouchMode == 0) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        if (!((BarLineChartBase) this.mChart).isDragEnabled() && !((BarLineChartBase) this.mChart).isScaleXEnabled() && !((BarLineChartBase) this.mChart).isScaleYEnabled()) {
            return true;
        }
        int i2 = 0;
        float xVelocity;
        switch (motionEvent.getAction() & 255) {
            case 0:
                startAction(motionEvent);
                stopDeceleration();
                saveTouchStart(motionEvent);
                break;
            case 1:
                VelocityTracker velocityTracker = this.mVelocityTracker;
                int pointerId = motionEvent.getPointerId(0);
                velocityTracker.computeCurrentVelocity(1000, (float) Utils.getMaximumFlingVelocity());
                float yVelocity = velocityTracker.getYVelocity(pointerId);
                xVelocity = velocityTracker.getXVelocity(pointerId);
                if ((Math.abs(xVelocity) > ((float) Utils.getMinimumFlingVelocity()) || Math.abs(yVelocity) > ((float) Utils.getMinimumFlingVelocity())) && this.mTouchMode == 1 && ((BarLineChartBase) this.mChart).isDragDecelerationEnabled()) {
                    stopDeceleration();
                    this.mDecelerationLastTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDecelerationCurrentPoint.f488x = motionEvent.getX();
                    this.mDecelerationCurrentPoint.f489y = motionEvent.getY();
                    this.mDecelerationVelocity.f488x = xVelocity;
                    this.mDecelerationVelocity.f489y = yVelocity;
                    Utils.postInvalidateOnAnimation(this.mChart);
                }
                if (this.mTouchMode == 2 || this.mTouchMode == 3 || this.mTouchMode == 4 || this.mTouchMode == 5) {
                    ((BarLineChartBase) this.mChart).calculateOffsets();
                    ((BarLineChartBase) this.mChart).postInvalidate();
                }
                this.mTouchMode = 0;
                ((BarLineChartBase) this.mChart).enableScroll();
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                endAction(motionEvent);
                break;
            case 2:
                if (this.mTouchMode != 1) {
                    if (this.mTouchMode != 2 && this.mTouchMode != 3 && this.mTouchMode != 4) {
                        if (this.mTouchMode == 0 && Math.abs(ChartTouchListener.distance(motionEvent.getX(), this.mTouchStartPoint.f488x, motionEvent.getY(), this.mTouchStartPoint.f489y)) > this.mDragTriggerDist && ((BarLineChartBase) this.mChart).isDragEnabled()) {
                            if (!(((BarLineChartBase) this.mChart).isFullyZoomedOut() && ((BarLineChartBase) this.mChart).hasNoDragOffset())) {
                                i2 = 1;
                            }
                            if (i2 == 0) {
                                if (((BarLineChartBase) this.mChart).isHighlightPerDragEnabled()) {
                                    this.mLastGesture = ChartGesture.DRAG;
                                    if (((BarLineChartBase) this.mChart).isHighlightPerDragEnabled()) {
                                        performHighlightDrag(motionEvent);
                                        break;
                                    }
                                }
                            }
                            xVelocity = Math.abs(motionEvent.getX() - this.mTouchStartPoint.f488x);
                            float abs = Math.abs(motionEvent.getY() - this.mTouchStartPoint.f489y);
                            if ((((BarLineChartBase) this.mChart).isDragXEnabled() || abs >= xVelocity) && (((BarLineChartBase) this.mChart).isDragYEnabled() || abs <= xVelocity)) {
                                this.mLastGesture = ChartGesture.DRAG;
                                this.mTouchMode = 1;
                                break;
                            }
                        }
                    }
                    ((BarLineChartBase) this.mChart).disableScroll();
                    if (((BarLineChartBase) this.mChart).isScaleXEnabled() || ((BarLineChartBase) this.mChart).isScaleYEnabled()) {
                        performZoom(motionEvent);
                        break;
                    }
                }
                ((BarLineChartBase) this.mChart).disableScroll();
                float f = 0.0f;
                xVelocity = ((BarLineChartBase) this.mChart).isDragXEnabled() ? motionEvent.getX() - this.mTouchStartPoint.f488x : 0.0f;
                if (((BarLineChartBase) this.mChart).isDragYEnabled()) {
                    f = motionEvent.getY() - this.mTouchStartPoint.f489y;
                }
                performDrag(motionEvent, xVelocity, f);
                break;
                break;
            case 3:
                this.mTouchMode = 0;
                endAction(motionEvent);
                break;
            case 5:
                if (motionEvent.getPointerCount() >= 2) {
                    ((BarLineChartBase) this.mChart).disableScroll();
                    saveTouchStart(motionEvent);
                    this.mSavedXDist = getXDist(motionEvent);
                    this.mSavedYDist = getYDist(motionEvent);
                    this.mSavedDist = spacing(motionEvent);
                    if (this.mSavedDist > 10.0f) {
                        if (((BarLineChartBase) this.mChart).isPinchZoomEnabled()) {
                            this.mTouchMode = 4;
                        } else if (((BarLineChartBase) this.mChart).isScaleXEnabled() != ((BarLineChartBase) this.mChart).isScaleYEnabled()) {
                            if (((BarLineChartBase) this.mChart).isScaleXEnabled()) {
                                i = 2;
                            }
                            this.mTouchMode = i;
                        } else {
                            if (this.mSavedXDist > this.mSavedYDist) {
                                i = 2;
                            }
                            this.mTouchMode = i;
                        }
                    }
                    midPoint(this.mTouchPointCenter, motionEvent);
                    break;
                }
                break;
            case 6:
                Utils.velocityTrackerPointerUpCleanUpIfNecessary(motionEvent, this.mVelocityTracker);
                this.mTouchMode = 5;
                break;
        }
        this.mMatrix = ((BarLineChartBase) this.mChart).getViewPortHandler().refresh(this.mMatrix, this.mChart, true);
        return true;
    }

    private void saveTouchStart(MotionEvent motionEvent) {
        this.mSavedMatrix.set(this.mMatrix);
        this.mTouchStartPoint.f488x = motionEvent.getX();
        this.mTouchStartPoint.f489y = motionEvent.getY();
        this.mClosestDataSetToTouch = ((BarLineChartBase) this.mChart).getDataSetByTouchPoint(motionEvent.getX(), motionEvent.getY());
    }

    private void performDrag(MotionEvent motionEvent, float f, float f2) {
        this.mLastGesture = ChartGesture.DRAG;
        this.mMatrix.set(this.mSavedMatrix);
        OnChartGestureListener onChartGestureListener = ((BarLineChartBase) this.mChart).getOnChartGestureListener();
        if (inverted()) {
            if (this.mChart instanceof HorizontalBarChart) {
                f = -f;
            } else {
                f2 = -f2;
            }
        }
        this.mMatrix.postTranslate(f, f2);
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartTranslate(motionEvent, f, f2);
        }
    }

    private void performZoom(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() >= 2) {
            OnChartGestureListener onChartGestureListener = ((BarLineChartBase) this.mChart).getOnChartGestureListener();
            float spacing = spacing(motionEvent);
            if (spacing > this.mMinScalePointerDistance) {
                MPPointF trans = getTrans(this.mTouchPointCenter.f488x, this.mTouchPointCenter.f489y);
                ViewPortHandler viewPortHandler = ((BarLineChartBase) this.mChart).getViewPortHandler();
                Object obj = null;
                float f;
                boolean canZoomOutMoreX;
                if (this.mTouchMode == 4) {
                    boolean canZoomOutMoreY;
                    this.mLastGesture = ChartGesture.PINCH_ZOOM;
                    f = spacing / this.mSavedDist;
                    if (f < 1.0f) {
                        obj = 1;
                    }
                    if (obj != null) {
                        canZoomOutMoreX = viewPortHandler.canZoomOutMoreX();
                    } else {
                        canZoomOutMoreX = viewPortHandler.canZoomInMoreX();
                    }
                    if (obj != null) {
                        canZoomOutMoreY = viewPortHandler.canZoomOutMoreY();
                    } else {
                        canZoomOutMoreY = viewPortHandler.canZoomInMoreY();
                    }
                    float f2 = ((BarLineChartBase) this.mChart).isScaleXEnabled() ? f : 1.0f;
                    if (!((BarLineChartBase) this.mChart).isScaleYEnabled()) {
                        f = 1.0f;
                    }
                    if (canZoomOutMoreY || canZoomOutMoreX) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(f2, f, trans.f488x, trans.f489y);
                        if (onChartGestureListener != null) {
                            onChartGestureListener.onChartScale(motionEvent, f2, f);
                        }
                    }
                } else if (this.mTouchMode == 2 && ((BarLineChartBase) this.mChart).isScaleXEnabled()) {
                    this.mLastGesture = ChartGesture.X_ZOOM;
                    f = getXDist(motionEvent) / this.mSavedXDist;
                    if (f < 1.0f) {
                        obj = 1;
                    }
                    if (obj != null) {
                        canZoomOutMoreX = viewPortHandler.canZoomOutMoreX();
                    } else {
                        canZoomOutMoreX = viewPortHandler.canZoomInMoreX();
                    }
                    if (canZoomOutMoreX) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(f, 1.0f, trans.f488x, trans.f489y);
                        if (onChartGestureListener != null) {
                            onChartGestureListener.onChartScale(motionEvent, f, 1.0f);
                        }
                    }
                } else if (this.mTouchMode == 3 && ((BarLineChartBase) this.mChart).isScaleYEnabled()) {
                    this.mLastGesture = ChartGesture.Y_ZOOM;
                    f = getYDist(motionEvent) / this.mSavedYDist;
                    if (f < 1.0f) {
                        obj = 1;
                    }
                    if (obj != null) {
                        canZoomOutMoreX = viewPortHandler.canZoomOutMoreY();
                    } else {
                        canZoomOutMoreX = viewPortHandler.canZoomInMoreY();
                    }
                    if (canZoomOutMoreX) {
                        this.mMatrix.set(this.mSavedMatrix);
                        this.mMatrix.postScale(1.0f, f, trans.f488x, trans.f489y);
                        if (onChartGestureListener != null) {
                            onChartGestureListener.onChartScale(motionEvent, 1.0f, f);
                        }
                    }
                }
                MPPointF.recycleInstance(trans);
            }
        }
    }

    private void performHighlightDrag(MotionEvent motionEvent) {
        Highlight highlightByTouchPoint = ((BarLineChartBase) this.mChart).getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY());
        if (highlightByTouchPoint != null && !highlightByTouchPoint.equalTo(this.mLastHighlighted)) {
            this.mLastHighlighted = highlightByTouchPoint;
            ((BarLineChartBase) this.mChart).highlightValue(highlightByTouchPoint, true);
        }
    }

    private static void midPoint(MPPointF mPPointF, MotionEvent motionEvent) {
        float y = motionEvent.getY(0) + motionEvent.getY(1);
        mPPointF.f488x = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
        mPPointF.f489y = y / 2.0f;
    }

    private static float spacing(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private static float getXDist(MotionEvent motionEvent) {
        return Math.abs(motionEvent.getX(0) - motionEvent.getX(1));
    }

    private static float getYDist(MotionEvent motionEvent) {
        return Math.abs(motionEvent.getY(0) - motionEvent.getY(1));
    }

    public MPPointF getTrans(float f, float f2) {
        ViewPortHandler viewPortHandler = ((BarLineChartBase) this.mChart).getViewPortHandler();
        f -= viewPortHandler.offsetLeft();
        if (inverted()) {
            f2 = -(f2 - viewPortHandler.offsetTop());
        } else {
            f2 = -((((float) ((BarLineChartBase) this.mChart).getMeasuredHeight()) - f2) - viewPortHandler.offsetBottom());
        }
        return MPPointF.getInstance(f, f2);
    }

    private boolean inverted() {
        return (this.mClosestDataSetToTouch == null && ((BarLineChartBase) this.mChart).isAnyAxisInverted()) || (this.mClosestDataSetToTouch != null && ((BarLineChartBase) this.mChart).isInverted(this.mClosestDataSetToTouch.getAxisDependency()));
    }

    public Matrix getMatrix() {
        return this.mMatrix;
    }

    public void setDragTriggerDist(float f) {
        this.mDragTriggerDist = Utils.convertDpToPixel(f);
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.DOUBLE_TAP;
        OnChartGestureListener onChartGestureListener = ((BarLineChartBase) this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartDoubleTapped(motionEvent);
        }
        if (((BarLineChartBase) this.mChart).isDoubleTapToZoomEnabled() && ((BarLineScatterCandleBubbleData) ((BarLineChartBase) this.mChart).getData()).getEntryCount() > 0) {
            MPPointF trans = getTrans(motionEvent.getX(), motionEvent.getY());
            BarLineChartBase barLineChartBase = (BarLineChartBase) this.mChart;
            float f = 1.0f;
            float f2 = ((BarLineChartBase) this.mChart).isScaleXEnabled() ? 1.4f : 1.0f;
            if (((BarLineChartBase) this.mChart).isScaleYEnabled()) {
                f = 1.4f;
            }
            barLineChartBase.zoom(f2, f, trans.f488x, trans.f489y);
            if (((BarLineChartBase) this.mChart).isLogEnabled()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Double-Tap, Zooming In, x: ");
                stringBuilder.append(trans.f488x);
                stringBuilder.append(", y: ");
                stringBuilder.append(trans.f489y);
                Log.i("BarlineChartTouch", stringBuilder.toString());
            }
            MPPointF.recycleInstance(trans);
        }
        return super.onDoubleTap(motionEvent);
    }

    public void onLongPress(MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.LONG_PRESS;
        OnChartGestureListener onChartGestureListener = ((BarLineChartBase) this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartLongPressed(motionEvent);
        }
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        this.mLastGesture = ChartGesture.SINGLE_TAP;
        OnChartGestureListener onChartGestureListener = ((BarLineChartBase) this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartSingleTapped(motionEvent);
        }
        if (!((BarLineChartBase) this.mChart).isHighlightPerTapEnabled()) {
            return false;
        }
        performHighlight(((BarLineChartBase) this.mChart).getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY()), motionEvent);
        return super.onSingleTapUp(motionEvent);
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.mLastGesture = ChartGesture.FLING;
        OnChartGestureListener onChartGestureListener = ((BarLineChartBase) this.mChart).getOnChartGestureListener();
        if (onChartGestureListener != null) {
            onChartGestureListener.onChartFling(motionEvent, motionEvent2, f, f2);
        }
        return super.onFling(motionEvent, motionEvent2, f, f2);
    }

    public void stopDeceleration() {
        this.mDecelerationVelocity.f488x = 0.0f;
        this.mDecelerationVelocity.f489y = 0.0f;
    }

    public void computeScroll() {
        float f = 0.0f;
        if (this.mDecelerationVelocity.f488x != 0.0f || this.mDecelerationVelocity.f489y != 0.0f) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            MPPointF mPPointF = this.mDecelerationVelocity;
            mPPointF.f488x *= ((BarLineChartBase) this.mChart).getDragDecelerationFrictionCoef();
            mPPointF = this.mDecelerationVelocity;
            mPPointF.f489y *= ((BarLineChartBase) this.mChart).getDragDecelerationFrictionCoef();
            float f2 = ((float) (currentAnimationTimeMillis - this.mDecelerationLastTime)) / 1000.0f;
            float f3 = this.mDecelerationVelocity.f488x * f2;
            float f4 = this.mDecelerationVelocity.f489y * f2;
            mPPointF = this.mDecelerationCurrentPoint;
            mPPointF.f488x += f3;
            mPPointF = this.mDecelerationCurrentPoint;
            mPPointF.f489y += f4;
            MotionEvent obtain = MotionEvent.obtain(currentAnimationTimeMillis, currentAnimationTimeMillis, 2, this.mDecelerationCurrentPoint.f488x, this.mDecelerationCurrentPoint.f489y, 0);
            f3 = ((BarLineChartBase) this.mChart).isDragXEnabled() ? this.mDecelerationCurrentPoint.f488x - this.mTouchStartPoint.f488x : 0.0f;
            if (((BarLineChartBase) this.mChart).isDragYEnabled()) {
                f = this.mDecelerationCurrentPoint.f489y - this.mTouchStartPoint.f489y;
            }
            performDrag(obtain, f3, f);
            obtain.recycle();
            this.mMatrix = ((BarLineChartBase) this.mChart).getViewPortHandler().refresh(this.mMatrix, this.mChart, false);
            this.mDecelerationLastTime = currentAnimationTimeMillis;
            if (((double) Math.abs(this.mDecelerationVelocity.f488x)) >= 0.01d || ((double) Math.abs(this.mDecelerationVelocity.f489y)) >= 0.01d) {
                Utils.postInvalidateOnAnimation(this.mChart);
            } else {
                ((BarLineChartBase) this.mChart).calculateOffsets();
                ((BarLineChartBase) this.mChart).postInvalidate();
                stopDeceleration();
            }
        }
    }
}
