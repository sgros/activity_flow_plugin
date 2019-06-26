// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.widget.AbsListView;
import android.view.View$MeasureSpec;
import android.util.Log;
import android.widget.ListView;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.support.v4.view.ViewCompat;
import android.view.ViewConfiguration;
import android.view.animation.Transformation;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation$AnimationListener;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.NestedScrollingChildHelper;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.view.ViewGroup;

public class SwipeRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild
{
    private static final int ALPHA_ANIMATION_DURATION = 300;
    private static final int ANIMATE_TO_START_DURATION = 200;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int CIRCLE_BG_LIGHT = -328966;
    @VisibleForTesting
    static final int CIRCLE_DIAMETER = 40;
    @VisibleForTesting
    static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    public static final int DEFAULT = 1;
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private static final float DRAG_RATE = 0.5f;
    private static final int INVALID_POINTER = -1;
    public static final int LARGE = 0;
    private static final int[] LAYOUT_ATTRS;
    private static final String LOG_TAG = "SwipeRefreshLayout";
    private static final int MAX_ALPHA = 255;
    private static final float MAX_PROGRESS_ANGLE = 0.8f;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int STARTING_PROGRESS_ALPHA = 76;
    private int mActivePointerId;
    private Animation mAlphaMaxAnimation;
    private Animation mAlphaStartAnimation;
    private final Animation mAnimateToCorrectPosition;
    private final Animation mAnimateToStartPosition;
    private OnChildScrollUpCallback mChildScrollUpCallback;
    private int mCircleDiameter;
    CircleImageView mCircleView;
    private int mCircleViewIndex;
    int mCurrentTargetOffsetTop;
    private final DecelerateInterpolator mDecelerateInterpolator;
    protected int mFrom;
    private float mInitialDownY;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private boolean mNestedScrollInProgress;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    boolean mNotify;
    protected int mOriginalOffsetTop;
    private final int[] mParentOffsetInWindow;
    private final int[] mParentScrollConsumed;
    CircularProgressDrawable mProgress;
    private Animation$AnimationListener mRefreshListener;
    boolean mRefreshing;
    private boolean mReturningToStart;
    boolean mScale;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mScaleDownToStartAnimation;
    int mSpinnerOffsetEnd;
    float mStartingScale;
    private View mTarget;
    private float mTotalDragDistance;
    private float mTotalUnconsumed;
    private int mTouchSlop;
    boolean mUsingCustomStart;
    
    static {
        LAYOUT_ATTRS = new int[] { 16842766 };
    }
    
    public SwipeRefreshLayout(final Context context) {
        this(context, null);
    }
    
    public SwipeRefreshLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mRefreshing = false;
        this.mTotalDragDistance = -1.0f;
        this.mParentScrollConsumed = new int[2];
        this.mParentOffsetInWindow = new int[2];
        this.mActivePointerId = -1;
        this.mCircleViewIndex = -1;
        this.mRefreshListener = (Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                if (SwipeRefreshLayout.this.mRefreshing) {
                    SwipeRefreshLayout.this.mProgress.setAlpha(255);
                    SwipeRefreshLayout.this.mProgress.start();
                    if (SwipeRefreshLayout.this.mNotify && SwipeRefreshLayout.this.mListener != null) {
                        SwipeRefreshLayout.this.mListener.onRefresh();
                    }
                    SwipeRefreshLayout.this.mCurrentTargetOffsetTop = SwipeRefreshLayout.this.mCircleView.getTop();
                }
                else {
                    SwipeRefreshLayout.this.reset();
                }
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
            }
        };
        this.mAnimateToCorrectPosition = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                int mSpinnerOffsetEnd;
                if (!SwipeRefreshLayout.this.mUsingCustomStart) {
                    mSpinnerOffsetEnd = SwipeRefreshLayout.this.mSpinnerOffsetEnd - Math.abs(SwipeRefreshLayout.this.mOriginalOffsetTop);
                }
                else {
                    mSpinnerOffsetEnd = SwipeRefreshLayout.this.mSpinnerOffsetEnd;
                }
                SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(SwipeRefreshLayout.this.mFrom + (int)((mSpinnerOffsetEnd - SwipeRefreshLayout.this.mFrom) * n) - SwipeRefreshLayout.this.mCircleView.getTop());
                SwipeRefreshLayout.this.mProgress.setArrowScale(1.0f - n);
            }
        };
        this.mAnimateToStartPosition = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                SwipeRefreshLayout.this.moveToStart(n);
            }
        };
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mMediumAnimationDuration = this.getResources().getInteger(17694721);
        this.setWillNotDraw(false);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        this.mCircleDiameter = (int)(40.0f * displayMetrics.density);
        this.createProgressView();
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        this.mSpinnerOffsetEnd = (int)(64.0f * displayMetrics.density);
        this.mTotalDragDistance = (float)this.mSpinnerOffsetEnd;
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        this.setNestedScrollingEnabled(true);
        final int n = -this.mCircleDiameter;
        this.mCurrentTargetOffsetTop = n;
        this.mOriginalOffsetTop = n;
        this.moveToStart(1.0f);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, SwipeRefreshLayout.LAYOUT_ATTRS);
        this.setEnabled(obtainStyledAttributes.getBoolean(0, true));
        obtainStyledAttributes.recycle();
    }
    
    private void animateOffsetToCorrectPosition(final int mFrom, final Animation$AnimationListener animationListener) {
        this.mFrom = mFrom;
        this.mAnimateToCorrectPosition.reset();
        this.mAnimateToCorrectPosition.setDuration(200L);
        this.mAnimateToCorrectPosition.setInterpolator((Interpolator)this.mDecelerateInterpolator);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
    }
    
    private void animateOffsetToStartPosition(final int mFrom, final Animation$AnimationListener animationListener) {
        if (this.mScale) {
            this.startScaleDownReturnToStartAnimation(mFrom, animationListener);
        }
        else {
            this.mFrom = mFrom;
            this.mAnimateToStartPosition.reset();
            this.mAnimateToStartPosition.setDuration(200L);
            this.mAnimateToStartPosition.setInterpolator((Interpolator)this.mDecelerateInterpolator);
            if (animationListener != null) {
                this.mCircleView.setAnimationListener(animationListener);
            }
            this.mCircleView.clearAnimation();
            this.mCircleView.startAnimation(this.mAnimateToStartPosition);
        }
    }
    
    private void createProgressView() {
        this.mCircleView = new CircleImageView(this.getContext(), -328966);
        (this.mProgress = new CircularProgressDrawable(this.getContext())).setStyle(1);
        this.mCircleView.setImageDrawable((Drawable)this.mProgress);
        this.mCircleView.setVisibility(8);
        this.addView((View)this.mCircleView);
    }
    
    private void ensureTarget() {
        if (this.mTarget == null) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                final View child = this.getChildAt(i);
                if (!child.equals(this.mCircleView)) {
                    this.mTarget = child;
                    break;
                }
            }
        }
    }
    
    private void finishSpinner(final float n) {
        if (n > this.mTotalDragDistance) {
            this.setRefreshing(true, true);
        }
        else {
            this.mRefreshing = false;
            this.mProgress.setStartEndTrim(0.0f, 0.0f);
            Object o = null;
            if (!this.mScale) {
                o = new Animation$AnimationListener() {
                    public void onAnimationEnd(final Animation animation) {
                        if (!SwipeRefreshLayout.this.mScale) {
                            SwipeRefreshLayout.this.startScaleDownAnimation(null);
                        }
                    }
                    
                    public void onAnimationRepeat(final Animation animation) {
                    }
                    
                    public void onAnimationStart(final Animation animation) {
                    }
                };
            }
            this.animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, (Animation$AnimationListener)o);
            this.mProgress.setArrowEnabled(false);
        }
    }
    
    private boolean isAnimationRunning(final Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }
    
    private void moveSpinner(final float a) {
        this.mProgress.setArrowEnabled(true);
        final float min = Math.min(1.0f, Math.abs(a / this.mTotalDragDistance));
        final float b = (float)Math.max(min - 0.4, 0.0) * 5.0f / 3.0f;
        final float abs = Math.abs(a);
        final float mTotalDragDistance = this.mTotalDragDistance;
        int mSpinnerOffsetEnd;
        if (this.mUsingCustomStart) {
            mSpinnerOffsetEnd = this.mSpinnerOffsetEnd - this.mOriginalOffsetTop;
        }
        else {
            mSpinnerOffsetEnd = this.mSpinnerOffsetEnd;
        }
        final float n = (float)mSpinnerOffsetEnd;
        final double a2 = Math.max(0.0f, Math.min(abs - mTotalDragDistance, n * 2.0f) / n) / 4.0f;
        final float n2 = (float)(a2 - Math.pow(a2, 2.0)) * 2.0f;
        final int mOriginalOffsetTop = this.mOriginalOffsetTop;
        final int n3 = (int)(n * min + n * n2 * 2.0f);
        if (this.mCircleView.getVisibility() != 0) {
            this.mCircleView.setVisibility(0);
        }
        if (!this.mScale) {
            this.mCircleView.setScaleX(1.0f);
            this.mCircleView.setScaleY(1.0f);
        }
        if (this.mScale) {
            this.setAnimationProgress(Math.min(1.0f, a / this.mTotalDragDistance));
        }
        if (a < this.mTotalDragDistance) {
            if (this.mProgress.getAlpha() > 76 && !this.isAnimationRunning(this.mAlphaStartAnimation)) {
                this.startProgressAlphaStartAnimation();
            }
        }
        else if (this.mProgress.getAlpha() < 255 && !this.isAnimationRunning(this.mAlphaMaxAnimation)) {
            this.startProgressAlphaMaxAnimation();
        }
        this.mProgress.setStartEndTrim(0.0f, Math.min(0.8f, b * 0.8f));
        this.mProgress.setArrowScale(Math.min(1.0f, b));
        this.mProgress.setProgressRotation((-0.25f + 0.4f * b + n2 * 2.0f) * 0.5f);
        this.setTargetOffsetTopAndBottom(mOriginalOffsetTop + n3 - this.mCurrentTargetOffsetTop);
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mActivePointerId = motionEvent.getPointerId(n);
        }
    }
    
    private void setColorViewAlpha(final int n) {
        this.mCircleView.getBackground().setAlpha(n);
        this.mProgress.setAlpha(n);
    }
    
    private void setRefreshing(final boolean mRefreshing, final boolean mNotify) {
        if (this.mRefreshing != mRefreshing) {
            this.mNotify = mNotify;
            this.ensureTarget();
            this.mRefreshing = mRefreshing;
            if (this.mRefreshing) {
                this.animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
            }
            else {
                this.startScaleDownAnimation(this.mRefreshListener);
            }
        }
    }
    
    private Animation startAlphaAnimation(final int n, final int n2) {
        final Animation animation = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                SwipeRefreshLayout.this.mProgress.setAlpha((int)(n + (n2 - n) * n));
            }
        };
        animation.setDuration(300L);
        this.mCircleView.setAnimationListener(null);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation((Animation)animation);
        return animation;
    }
    
    private void startDragging(final float n) {
        if (n - this.mInitialDownY > this.mTouchSlop && !this.mIsBeingDragged) {
            this.mInitialMotionY = this.mInitialDownY + this.mTouchSlop;
            this.mIsBeingDragged = true;
            this.mProgress.setAlpha(76);
        }
    }
    
    private void startProgressAlphaMaxAnimation() {
        this.mAlphaMaxAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 255);
    }
    
    private void startProgressAlphaStartAnimation() {
        this.mAlphaStartAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 76);
    }
    
    private void startScaleDownReturnToStartAnimation(final int mFrom, final Animation$AnimationListener animationListener) {
        this.mFrom = mFrom;
        this.mStartingScale = this.mCircleView.getScaleX();
        (this.mScaleDownToStartAnimation = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(SwipeRefreshLayout.this.mStartingScale + -SwipeRefreshLayout.this.mStartingScale * n);
                SwipeRefreshLayout.this.moveToStart(n);
            }
        }).setDuration(150L);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownToStartAnimation);
    }
    
    private void startScaleUpAnimation(final Animation$AnimationListener animationListener) {
        this.mCircleView.setVisibility(0);
        if (Build$VERSION.SDK_INT >= 11) {
            this.mProgress.setAlpha(255);
        }
        (this.mScaleAnimation = new Animation() {
            public void applyTransformation(final float animationProgress, final Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(animationProgress);
            }
        }).setDuration((long)this.mMediumAnimationDuration);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleAnimation);
    }
    
    public boolean canChildScrollUp() {
        if (this.mChildScrollUpCallback != null) {
            return this.mChildScrollUpCallback.canChildScrollUp(this, this.mTarget);
        }
        if (this.mTarget instanceof ListView) {
            return ListViewCompat.canScrollList((ListView)this.mTarget, -1);
        }
        return this.mTarget.canScrollVertically(-1);
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    protected int getChildDrawingOrder(final int n, final int n2) {
        if (this.mCircleViewIndex < 0) {
            return n2;
        }
        if (n2 == n - 1) {
            return this.mCircleViewIndex;
        }
        if (n2 >= this.mCircleViewIndex) {
            return n2 + 1;
        }
        return n2;
    }
    
    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }
    
    public int getProgressCircleDiameter() {
        return this.mCircleDiameter;
    }
    
    public int getProgressViewEndOffset() {
        return this.mSpinnerOffsetEnd;
    }
    
    public int getProgressViewStartOffset() {
        return this.mOriginalOffsetTop;
    }
    
    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }
    
    public boolean isRefreshing() {
        return this.mRefreshing;
    }
    
    void moveToStart(final float n) {
        this.setTargetOffsetTopAndBottom(this.mFrom + (int)((this.mOriginalOffsetTop - this.mFrom) * n) - this.mCircleView.getTop());
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.reset();
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        this.ensureTarget();
        final int actionMasked = motionEvent.getActionMasked();
        if (this.mReturningToStart && actionMasked == 0) {
            this.mReturningToStart = false;
        }
        if (this.isEnabled() && !this.mReturningToStart && !this.canChildScrollUp() && !this.mRefreshing && !this.mNestedScrollInProgress) {
            if (actionMasked != 6) {
                switch (actionMasked) {
                    case 2: {
                        if (this.mActivePointerId == -1) {
                            Log.e(SwipeRefreshLayout.LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                            return false;
                        }
                        final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (pointerIndex < 0) {
                            return false;
                        }
                        this.startDragging(motionEvent.getY(pointerIndex));
                        break;
                    }
                    case 1:
                    case 3: {
                        this.mIsBeingDragged = false;
                        this.mActivePointerId = -1;
                        break;
                    }
                    case 0: {
                        this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCircleView.getTop());
                        this.mActivePointerId = motionEvent.getPointerId(0);
                        this.mIsBeingDragged = false;
                        final int pointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (pointerIndex2 < 0) {
                            return false;
                        }
                        this.mInitialDownY = motionEvent.getY(pointerIndex2);
                        break;
                    }
                }
            }
            else {
                this.onSecondaryPointerUp(motionEvent);
            }
            return this.mIsBeingDragged;
        }
        return false;
    }
    
    protected void onLayout(final boolean b, int measuredWidth, int n, int n2, int measuredHeight) {
        measuredWidth = this.getMeasuredWidth();
        measuredHeight = this.getMeasuredHeight();
        if (this.getChildCount() == 0) {
            return;
        }
        if (this.mTarget == null) {
            this.ensureTarget();
        }
        if (this.mTarget == null) {
            return;
        }
        final View mTarget = this.mTarget;
        n = this.getPaddingLeft();
        n2 = this.getPaddingTop();
        mTarget.layout(n, n2, measuredWidth - this.getPaddingLeft() - this.getPaddingRight() + n, measuredHeight - this.getPaddingTop() - this.getPaddingBottom() + n2);
        n2 = this.mCircleView.getMeasuredWidth();
        n = this.mCircleView.getMeasuredHeight();
        final CircleImageView mCircleView = this.mCircleView;
        measuredWidth /= 2;
        n2 /= 2;
        mCircleView.layout(measuredWidth - n2, this.mCurrentTargetOffsetTop, measuredWidth + n2, this.mCurrentTargetOffsetTop + n);
    }
    
    public void onMeasure(int i, final int n) {
        super.onMeasure(i, n);
        if (this.mTarget == null) {
            this.ensureTarget();
        }
        if (this.mTarget == null) {
            return;
        }
        this.mTarget.measure(View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
        this.mCircleView.measure(View$MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824), View$MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824));
        this.mCircleViewIndex = -1;
        for (i = 0; i < this.getChildCount(); ++i) {
            if (this.getChildAt(i) == this.mCircleView) {
                this.mCircleViewIndex = i;
                break;
            }
        }
    }
    
    public boolean onNestedFling(final View view, final float n, final float n2, final boolean b) {
        return this.dispatchNestedFling(n, n2, b);
    }
    
    public boolean onNestedPreFling(final View view, final float n, final float n2) {
        return this.dispatchNestedPreFling(n, n2);
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array) {
        if (n2 > 0 && this.mTotalUnconsumed > 0.0f) {
            final float n3 = (float)n2;
            if (n3 > this.mTotalUnconsumed) {
                array[1] = n2 - (int)this.mTotalUnconsumed;
                this.mTotalUnconsumed = 0.0f;
            }
            else {
                this.mTotalUnconsumed -= n3;
                array[1] = n2;
            }
            this.moveSpinner(this.mTotalUnconsumed);
        }
        if (this.mUsingCustomStart && n2 > 0 && this.mTotalUnconsumed == 0.0f && Math.abs(n2 - array[1]) > 0) {
            this.mCircleView.setVisibility(8);
        }
        final int[] mParentScrollConsumed = this.mParentScrollConsumed;
        if (this.dispatchNestedPreScroll(n - array[0], n2 - array[1], mParentScrollConsumed, null)) {
            array[0] += mParentScrollConsumed[0];
            array[1] += mParentScrollConsumed[1];
        }
    }
    
    public void onNestedScroll(final View view, int a, final int n, final int n2, final int n3) {
        this.dispatchNestedScroll(a, n, n2, n3, this.mParentOffsetInWindow);
        a = n3 + this.mParentOffsetInWindow[1];
        if (a < 0 && !this.canChildScrollUp()) {
            this.moveSpinner(this.mTotalUnconsumed += Math.abs(a));
        }
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, n);
        this.startNestedScroll(n & 0x2);
        this.mTotalUnconsumed = 0.0f;
        this.mNestedScrollInProgress = true;
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n) {
        return this.isEnabled() && !this.mReturningToStart && !this.mRefreshing && (n & 0x2) != 0x0;
    }
    
    public void onStopNestedScroll(final View view) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view);
        this.mNestedScrollInProgress = false;
        if (this.mTotalUnconsumed > 0.0f) {
            this.finishSpinner(this.mTotalUnconsumed);
            this.mTotalUnconsumed = 0.0f;
        }
        this.stopNestedScroll();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (this.mReturningToStart && actionMasked == 0) {
            this.mReturningToStart = false;
        }
        if (this.isEnabled() && !this.mReturningToStart && !this.canChildScrollUp() && !this.mRefreshing && !this.mNestedScrollInProgress) {
            switch (actionMasked) {
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    break;
                }
                case 5: {
                    final int actionIndex = motionEvent.getActionIndex();
                    if (actionIndex < 0) {
                        Log.e(SwipeRefreshLayout.LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                        return false;
                    }
                    this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                    break;
                }
                case 3: {
                    return false;
                }
                case 2: {
                    final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex < 0) {
                        Log.e(SwipeRefreshLayout.LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    }
                    final float y = motionEvent.getY(pointerIndex);
                    this.startDragging(y);
                    if (!this.mIsBeingDragged) {
                        break;
                    }
                    final float n = (y - this.mInitialMotionY) * 0.5f;
                    if (n > 0.0f) {
                        this.moveSpinner(n);
                        break;
                    }
                    return false;
                }
                case 1: {
                    final int pointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex2 < 0) {
                        Log.e(SwipeRefreshLayout.LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                        return false;
                    }
                    if (this.mIsBeingDragged) {
                        final float y2 = motionEvent.getY(pointerIndex2);
                        final float mInitialMotionY = this.mInitialMotionY;
                        this.mIsBeingDragged = false;
                        this.finishSpinner((y2 - mInitialMotionY) * 0.5f);
                    }
                    this.mActivePointerId = -1;
                    return false;
                }
                case 0: {
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mIsBeingDragged = false;
                    break;
                }
            }
            return true;
        }
        return false;
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        if (Build$VERSION.SDK_INT >= 21 || !(this.mTarget instanceof AbsListView)) {
            if (this.mTarget == null || ViewCompat.isNestedScrollingEnabled(this.mTarget)) {
                super.requestDisallowInterceptTouchEvent(b);
            }
        }
    }
    
    void reset() {
        this.mCircleView.clearAnimation();
        this.mProgress.stop();
        this.mCircleView.setVisibility(8);
        this.setColorViewAlpha(255);
        if (this.mScale) {
            this.setAnimationProgress(0.0f);
        }
        else {
            this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCurrentTargetOffsetTop);
        }
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
    }
    
    void setAnimationProgress(final float n) {
        this.mCircleView.setScaleX(n);
        this.mCircleView.setScaleY(n);
    }
    
    @Deprecated
    public void setColorScheme(@ColorRes final int... colorSchemeResources) {
        this.setColorSchemeResources(colorSchemeResources);
    }
    
    public void setColorSchemeColors(@ColorInt final int... colorSchemeColors) {
        this.ensureTarget();
        this.mProgress.setColorSchemeColors(colorSchemeColors);
    }
    
    public void setColorSchemeResources(@ColorRes final int... array) {
        final Context context = this.getContext();
        int i = 0;
        final int[] colorSchemeColors = new int[array.length];
        while (i < array.length) {
            colorSchemeColors[i] = ContextCompat.getColor(context, array[i]);
            ++i;
        }
        this.setColorSchemeColors(colorSchemeColors);
    }
    
    public void setDistanceToTriggerSync(final int n) {
        this.mTotalDragDistance = (float)n;
    }
    
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            this.reset();
        }
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public void setOnChildScrollUpCallback(@Nullable final OnChildScrollUpCallback mChildScrollUpCallback) {
        this.mChildScrollUpCallback = mChildScrollUpCallback;
    }
    
    public void setOnRefreshListener(final OnRefreshListener mListener) {
        this.mListener = mListener;
    }
    
    @Deprecated
    public void setProgressBackgroundColor(final int progressBackgroundColorSchemeResource) {
        this.setProgressBackgroundColorSchemeResource(progressBackgroundColorSchemeResource);
    }
    
    public void setProgressBackgroundColorSchemeColor(@ColorInt final int backgroundColor) {
        this.mCircleView.setBackgroundColor(backgroundColor);
    }
    
    public void setProgressBackgroundColorSchemeResource(@ColorRes final int n) {
        this.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this.getContext(), n));
    }
    
    public void setProgressViewEndTarget(final boolean mScale, final int mSpinnerOffsetEnd) {
        this.mSpinnerOffsetEnd = mSpinnerOffsetEnd;
        this.mScale = mScale;
        this.mCircleView.invalidate();
    }
    
    public void setProgressViewOffset(final boolean mScale, final int mOriginalOffsetTop, final int mSpinnerOffsetEnd) {
        this.mScale = mScale;
        this.mOriginalOffsetTop = mOriginalOffsetTop;
        this.mSpinnerOffsetEnd = mSpinnerOffsetEnd;
        this.mUsingCustomStart = true;
        this.reset();
        this.mRefreshing = false;
    }
    
    public void setRefreshing(final boolean mRefreshing) {
        if (mRefreshing && this.mRefreshing != mRefreshing) {
            this.mRefreshing = mRefreshing;
            int mSpinnerOffsetEnd;
            if (!this.mUsingCustomStart) {
                mSpinnerOffsetEnd = this.mSpinnerOffsetEnd + this.mOriginalOffsetTop;
            }
            else {
                mSpinnerOffsetEnd = this.mSpinnerOffsetEnd;
            }
            this.setTargetOffsetTopAndBottom(mSpinnerOffsetEnd - this.mCurrentTargetOffsetTop);
            this.mNotify = false;
            this.startScaleUpAnimation(this.mRefreshListener);
        }
        else {
            this.setRefreshing(mRefreshing, false);
        }
    }
    
    public void setSize(final int style) {
        if (style != 0 && style != 1) {
            return;
        }
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        if (style == 0) {
            this.mCircleDiameter = (int)(56.0f * displayMetrics.density);
        }
        else {
            this.mCircleDiameter = (int)(40.0f * displayMetrics.density);
        }
        this.mCircleView.setImageDrawable((Drawable)null);
        this.mProgress.setStyle(style);
        this.mCircleView.setImageDrawable((Drawable)this.mProgress);
    }
    
    void setTargetOffsetTopAndBottom(final int n) {
        this.mCircleView.bringToFront();
        ViewCompat.offsetTopAndBottom((View)this.mCircleView, n);
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
    }
    
    public boolean startNestedScroll(final int n) {
        return this.mNestedScrollingChildHelper.startNestedScroll(n);
    }
    
    void startScaleDownAnimation(final Animation$AnimationListener animationListener) {
        (this.mScaleDownAnimation = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(1.0f - n);
            }
        }).setDuration(150L);
        this.mCircleView.setAnimationListener(animationListener);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownAnimation);
    }
    
    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }
    
    public interface OnChildScrollUpCallback
    {
        boolean canChildScrollUp(final SwipeRefreshLayout p0, @Nullable final View p1);
    }
    
    public interface OnRefreshListener
    {
        void onRefresh();
    }
}
