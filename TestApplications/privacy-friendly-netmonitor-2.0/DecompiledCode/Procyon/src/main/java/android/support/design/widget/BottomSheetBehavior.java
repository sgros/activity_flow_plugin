// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.annotation.NonNull;
import android.view.ViewParent;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.support.annotation.VisibleForTesting;
import android.view.ViewGroup;
import android.view.ViewGroup$LayoutParams;
import android.util.TypedValue;
import android.content.res.TypedArray;
import android.view.ViewConfiguration;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.math.MathUtils;
import android.view.VelocityTracker;
import java.lang.ref.WeakReference;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;

public class BottomSheetBehavior<V extends View> extends Behavior<V>
{
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_SETTLING = 2;
    int mActivePointerId;
    private BottomSheetCallback mCallback;
    private final ViewDragHelper.Callback mDragCallback;
    boolean mHideable;
    private boolean mIgnoreEvents;
    private int mInitialY;
    private int mLastNestedScrollDy;
    int mMaxOffset;
    private float mMaximumVelocity;
    int mMinOffset;
    private boolean mNestedScrolled;
    WeakReference<View> mNestedScrollingChildRef;
    int mParentHeight;
    private int mPeekHeight;
    private boolean mPeekHeightAuto;
    private int mPeekHeightMin;
    private boolean mSkipCollapsed;
    int mState;
    boolean mTouchingScrollingChild;
    private VelocityTracker mVelocityTracker;
    ViewDragHelper mViewDragHelper;
    WeakReference<V> mViewRef;
    
    public BottomSheetBehavior() {
        this.mState = 4;
        this.mDragCallback = new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
                return view.getLeft();
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, int n2) {
                final int mMinOffset = BottomSheetBehavior.this.mMinOffset;
                if (BottomSheetBehavior.this.mHideable) {
                    n2 = BottomSheetBehavior.this.mParentHeight;
                }
                else {
                    n2 = BottomSheetBehavior.this.mMaxOffset;
                }
                return MathUtils.clamp(n, mMinOffset, n2);
            }
            
            @Override
            public int getViewVerticalDragRange(final View view) {
                if (BottomSheetBehavior.this.mHideable) {
                    return BottomSheetBehavior.this.mParentHeight - BottomSheetBehavior.this.mMinOffset;
                }
                return BottomSheetBehavior.this.mMaxOffset - BottomSheetBehavior.this.mMinOffset;
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (n == 1) {
                    BottomSheetBehavior.this.setStateInternal(1);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                BottomSheetBehavior.this.dispatchOnSlide(n2);
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                int stateInternal = 4;
                int n3 = 0;
                Label_0135: {
                    if (n2 < 0.0f) {
                        n3 = BottomSheetBehavior.this.mMinOffset;
                    }
                    else {
                        if (BottomSheetBehavior.this.mHideable && BottomSheetBehavior.this.shouldHide(view, n2)) {
                            n3 = BottomSheetBehavior.this.mParentHeight;
                            stateInternal = 5;
                            break Label_0135;
                        }
                        if (n2 != 0.0f) {
                            n3 = BottomSheetBehavior.this.mMaxOffset;
                            break Label_0135;
                        }
                        final int top = view.getTop();
                        if (Math.abs(top - BottomSheetBehavior.this.mMinOffset) >= Math.abs(top - BottomSheetBehavior.this.mMaxOffset)) {
                            n3 = BottomSheetBehavior.this.mMaxOffset;
                            break Label_0135;
                        }
                        n3 = BottomSheetBehavior.this.mMinOffset;
                    }
                    stateInternal = 3;
                }
                if (BottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(view.getLeft(), n3)) {
                    BottomSheetBehavior.this.setStateInternal(2);
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, stateInternal));
                }
                else {
                    BottomSheetBehavior.this.setStateInternal(stateInternal);
                }
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                final int mState = BottomSheetBehavior.this.mState;
                boolean b = true;
                if (mState == 1) {
                    return false;
                }
                if (BottomSheetBehavior.this.mTouchingScrollingChild) {
                    return false;
                }
                if (BottomSheetBehavior.this.mState == 3 && BottomSheetBehavior.this.mActivePointerId == n) {
                    final View view2 = BottomSheetBehavior.this.mNestedScrollingChildRef.get();
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                if (BottomSheetBehavior.this.mViewRef == null || BottomSheetBehavior.this.mViewRef.get() != view) {
                    b = false;
                }
                return b;
            }
        };
    }
    
    public BottomSheetBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.mState = 4;
        this.mDragCallback = new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
                return view.getLeft();
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, int n2) {
                final int mMinOffset = BottomSheetBehavior.this.mMinOffset;
                if (BottomSheetBehavior.this.mHideable) {
                    n2 = BottomSheetBehavior.this.mParentHeight;
                }
                else {
                    n2 = BottomSheetBehavior.this.mMaxOffset;
                }
                return MathUtils.clamp(n, mMinOffset, n2);
            }
            
            @Override
            public int getViewVerticalDragRange(final View view) {
                if (BottomSheetBehavior.this.mHideable) {
                    return BottomSheetBehavior.this.mParentHeight - BottomSheetBehavior.this.mMinOffset;
                }
                return BottomSheetBehavior.this.mMaxOffset - BottomSheetBehavior.this.mMinOffset;
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (n == 1) {
                    BottomSheetBehavior.this.setStateInternal(1);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                BottomSheetBehavior.this.dispatchOnSlide(n2);
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                int stateInternal = 4;
                int n3 = 0;
                Label_0135: {
                    if (n2 < 0.0f) {
                        n3 = BottomSheetBehavior.this.mMinOffset;
                    }
                    else {
                        if (BottomSheetBehavior.this.mHideable && BottomSheetBehavior.this.shouldHide(view, n2)) {
                            n3 = BottomSheetBehavior.this.mParentHeight;
                            stateInternal = 5;
                            break Label_0135;
                        }
                        if (n2 != 0.0f) {
                            n3 = BottomSheetBehavior.this.mMaxOffset;
                            break Label_0135;
                        }
                        final int top = view.getTop();
                        if (Math.abs(top - BottomSheetBehavior.this.mMinOffset) >= Math.abs(top - BottomSheetBehavior.this.mMaxOffset)) {
                            n3 = BottomSheetBehavior.this.mMaxOffset;
                            break Label_0135;
                        }
                        n3 = BottomSheetBehavior.this.mMinOffset;
                    }
                    stateInternal = 3;
                }
                if (BottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(view.getLeft(), n3)) {
                    BottomSheetBehavior.this.setStateInternal(2);
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, stateInternal));
                }
                else {
                    BottomSheetBehavior.this.setStateInternal(stateInternal);
                }
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                final int mState = BottomSheetBehavior.this.mState;
                boolean b = true;
                if (mState == 1) {
                    return false;
                }
                if (BottomSheetBehavior.this.mTouchingScrollingChild) {
                    return false;
                }
                if (BottomSheetBehavior.this.mState == 3 && BottomSheetBehavior.this.mActivePointerId == n) {
                    final View view2 = BottomSheetBehavior.this.mNestedScrollingChildRef.get();
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                if (BottomSheetBehavior.this.mViewRef == null || BottomSheetBehavior.this.mViewRef.get() != view) {
                    b = false;
                }
                return b;
            }
        };
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.BottomSheetBehavior_Layout);
        final TypedValue peekValue = obtainStyledAttributes.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (peekValue != null && peekValue.data == -1) {
            this.setPeekHeight(peekValue.data);
        }
        else {
            this.setPeekHeight(obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        }
        this.setHideable(obtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        this.setSkipCollapsed(obtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        obtainStyledAttributes.recycle();
        this.mMaximumVelocity = (float)ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }
    
    public static <V extends View> BottomSheetBehavior<V> from(final V v) {
        final ViewGroup$LayoutParams layoutParams = v.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        final Behavior behavior = ((LayoutParams)layoutParams).getBehavior();
        if (!(behavior instanceof BottomSheetBehavior)) {
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        return (BottomSheetBehavior<V>)behavior;
    }
    
    private float getYVelocity() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        return this.mVelocityTracker.getYVelocity(this.mActivePointerId);
    }
    
    private void reset() {
        this.mActivePointerId = -1;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    void dispatchOnSlide(final int n) {
        final View view = this.mViewRef.get();
        if (view != null && this.mCallback != null) {
            if (n > this.mMaxOffset) {
                this.mCallback.onSlide(view, (this.mMaxOffset - n) / (float)(this.mParentHeight - this.mMaxOffset));
            }
            else {
                this.mCallback.onSlide(view, (this.mMaxOffset - n) / (float)(this.mMaxOffset - this.mMinOffset));
            }
        }
    }
    
    @VisibleForTesting
    View findScrollingChild(View scrollingChild) {
        if (ViewCompat.isNestedScrollingEnabled(scrollingChild)) {
            return scrollingChild;
        }
        if (scrollingChild instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)scrollingChild;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                scrollingChild = this.findScrollingChild(viewGroup.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }
    
    public final int getPeekHeight() {
        int mPeekHeight;
        if (this.mPeekHeightAuto) {
            mPeekHeight = -1;
        }
        else {
            mPeekHeight = this.mPeekHeight;
        }
        return mPeekHeight;
    }
    
    @VisibleForTesting
    int getPeekHeightMin() {
        return this.mPeekHeightMin;
    }
    
    public boolean getSkipCollapsed() {
        return this.mSkipCollapsed;
    }
    
    public final int getState() {
        return this.mState;
    }
    
    public boolean isHideable() {
        return this.mHideable;
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        final boolean shown = v.isShown();
        final boolean b = false;
        if (!shown) {
            this.mIgnoreEvents = true;
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        Label_0233: {
            if (actionMasked != 3) {
                switch (actionMasked) {
                    default: {
                        break Label_0233;
                    }
                    case 0: {
                        final int n = (int)motionEvent.getX();
                        this.mInitialY = (int)motionEvent.getY();
                        View view;
                        if (this.mNestedScrollingChildRef != null) {
                            view = this.mNestedScrollingChildRef.get();
                        }
                        else {
                            view = null;
                        }
                        if (view != null && coordinatorLayout.isPointInChildBounds(view, n, this.mInitialY)) {
                            this.mActivePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                            this.mTouchingScrollingChild = true;
                        }
                        this.mIgnoreEvents = (this.mActivePointerId == -1 && !coordinatorLayout.isPointInChildBounds(v, n, this.mInitialY));
                        break Label_0233;
                    }
                    case 1: {
                        break;
                    }
                }
            }
            this.mTouchingScrollingChild = false;
            this.mActivePointerId = -1;
            if (this.mIgnoreEvents) {
                return this.mIgnoreEvents = false;
            }
        }
        if (!this.mIgnoreEvents && this.mViewDragHelper.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        final View view2 = this.mNestedScrollingChildRef.get();
        boolean b2 = b;
        if (actionMasked == 2) {
            b2 = b;
            if (view2 != null) {
                b2 = b;
                if (!this.mIgnoreEvents) {
                    b2 = b;
                    if (this.mState != 1) {
                        b2 = b;
                        if (!coordinatorLayout.isPointInChildBounds(view2, (int)motionEvent.getX(), (int)motionEvent.getY())) {
                            b2 = b;
                            if (Math.abs(this.mInitialY - motionEvent.getY()) > this.mViewDragHelper.getTouchSlop()) {
                                b2 = true;
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final V referent, int n) {
        if (ViewCompat.getFitsSystemWindows((View)coordinatorLayout) && !ViewCompat.getFitsSystemWindows(referent)) {
            ViewCompat.setFitsSystemWindows(referent, true);
        }
        final int top = referent.getTop();
        coordinatorLayout.onLayoutChild(referent, n);
        this.mParentHeight = coordinatorLayout.getHeight();
        if (this.mPeekHeightAuto) {
            if (this.mPeekHeightMin == 0) {
                this.mPeekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            n = Math.max(this.mPeekHeightMin, this.mParentHeight - coordinatorLayout.getWidth() * 9 / 16);
        }
        else {
            n = this.mPeekHeight;
        }
        this.mMinOffset = Math.max(0, this.mParentHeight - referent.getHeight());
        this.mMaxOffset = Math.max(this.mParentHeight - n, this.mMinOffset);
        if (this.mState == 3) {
            ViewCompat.offsetTopAndBottom(referent, this.mMinOffset);
        }
        else if (this.mHideable && this.mState == 5) {
            ViewCompat.offsetTopAndBottom(referent, this.mParentHeight);
        }
        else if (this.mState == 4) {
            ViewCompat.offsetTopAndBottom(referent, this.mMaxOffset);
        }
        else if (this.mState == 1 || this.mState == 2) {
            ViewCompat.offsetTopAndBottom(referent, top - referent.getTop());
        }
        if (this.mViewDragHelper == null) {
            this.mViewDragHelper = ViewDragHelper.create(coordinatorLayout, this.mDragCallback);
        }
        this.mViewRef = new WeakReference<V>(referent);
        this.mNestedScrollingChildRef = new WeakReference<View>(this.findScrollingChild(referent));
        return true;
    }
    
    @Override
    public boolean onNestedPreFling(final CoordinatorLayout coordinatorLayout, final V v, final View view, final float n, final float n2) {
        return view == this.mNestedScrollingChildRef.get() && (this.mState != 3 || super.onNestedPreFling(coordinatorLayout, v, view, n, n2));
    }
    
    @Override
    public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, int top, final int mLastNestedScrollDy, final int[] array) {
        if (view != this.mNestedScrollingChildRef.get()) {
            return;
        }
        top = v.getTop();
        final int n = top - mLastNestedScrollDy;
        if (mLastNestedScrollDy > 0) {
            if (n < this.mMinOffset) {
                array[1] = top - this.mMinOffset;
                ViewCompat.offsetTopAndBottom(v, -array[1]);
                this.setStateInternal(3);
            }
            else {
                ViewCompat.offsetTopAndBottom(v, -(array[1] = mLastNestedScrollDy));
                this.setStateInternal(1);
            }
        }
        else if (mLastNestedScrollDy < 0 && !view.canScrollVertically(-1)) {
            if (n > this.mMaxOffset && !this.mHideable) {
                array[1] = top - this.mMaxOffset;
                ViewCompat.offsetTopAndBottom(v, -array[1]);
                this.setStateInternal(4);
            }
            else {
                ViewCompat.offsetTopAndBottom(v, -(array[1] = mLastNestedScrollDy));
                this.setStateInternal(1);
            }
        }
        this.dispatchOnSlide(v.getTop());
        this.mLastNestedScrollDy = mLastNestedScrollDy;
        this.mNestedScrolled = true;
    }
    
    @Override
    public void onRestoreInstanceState(final CoordinatorLayout coordinatorLayout, final V v, final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v, savedState.getSuperState());
        if (savedState.state != 1 && savedState.state != 2) {
            this.mState = savedState.state;
        }
        else {
            this.mState = 4;
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState(final CoordinatorLayout coordinatorLayout, final V v) {
        return (Parcelable)new SavedState(super.onSaveInstanceState(coordinatorLayout, v), this.mState);
    }
    
    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n) {
        boolean b = false;
        this.mLastNestedScrollDy = 0;
        this.mNestedScrolled = false;
        if ((n & 0x2) != 0x0) {
            b = true;
        }
        return b;
    }
    
    @Override
    public void onStopNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view) {
        final int top = v.getTop();
        final int mMinOffset = this.mMinOffset;
        int stateInternal = 3;
        if (top == mMinOffset) {
            this.setStateInternal(3);
            return;
        }
        if (this.mNestedScrollingChildRef != null && view == this.mNestedScrollingChildRef.get() && this.mNestedScrolled) {
            int n = 0;
            Label_0166: {
                if (this.mLastNestedScrollDy > 0) {
                    n = this.mMinOffset;
                }
                else if (this.mHideable && this.shouldHide(v, this.getYVelocity())) {
                    n = this.mParentHeight;
                    stateInternal = 5;
                }
                else {
                    if (this.mLastNestedScrollDy == 0) {
                        final int top2 = v.getTop();
                        if (Math.abs(top2 - this.mMinOffset) < Math.abs(top2 - this.mMaxOffset)) {
                            n = this.mMinOffset;
                            break Label_0166;
                        }
                        n = this.mMaxOffset;
                    }
                    else {
                        n = this.mMaxOffset;
                    }
                    stateInternal = 4;
                }
            }
            if (this.mViewDragHelper.smoothSlideViewTo(v, v.getLeft(), n)) {
                this.setStateInternal(2);
                ViewCompat.postOnAnimation(v, new SettleRunnable(v, stateInternal));
            }
            else {
                this.setStateInternal(stateInternal);
            }
            this.mNestedScrolled = false;
        }
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (!v.isShown()) {
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (this.mState == 1 && actionMasked == 0) {
            return true;
        }
        this.mViewDragHelper.processTouchEvent(motionEvent);
        if (actionMasked == 0) {
            this.reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        if (actionMasked == 2 && !this.mIgnoreEvents && Math.abs(this.mInitialY - motionEvent.getY()) > this.mViewDragHelper.getTouchSlop()) {
            this.mViewDragHelper.captureChildView(v, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return this.mIgnoreEvents ^ true;
    }
    
    public void setBottomSheetCallback(final BottomSheetCallback mCallback) {
        this.mCallback = mCallback;
    }
    
    public void setHideable(final boolean mHideable) {
        this.mHideable = mHideable;
    }
    
    public final void setPeekHeight(int b) {
        final int n = 1;
        Label_0073: {
            if (b == -1) {
                if (!this.mPeekHeightAuto) {
                    this.mPeekHeightAuto = true;
                    b = n;
                    break Label_0073;
                }
            }
            else if (this.mPeekHeightAuto || this.mPeekHeight != b) {
                this.mPeekHeightAuto = false;
                this.mPeekHeight = Math.max(0, b);
                this.mMaxOffset = this.mParentHeight - b;
                b = n;
                break Label_0073;
            }
            b = 0;
        }
        if (b != 0 && this.mState == 4 && this.mViewRef != null) {
            final View view = this.mViewRef.get();
            if (view != null) {
                view.requestLayout();
            }
        }
    }
    
    public void setSkipCollapsed(final boolean mSkipCollapsed) {
        this.mSkipCollapsed = mSkipCollapsed;
    }
    
    public final void setState(final int mState) {
        if (mState == this.mState) {
            return;
        }
        if (this.mViewRef == null) {
            if (mState == 4 || mState == 3 || (this.mHideable && mState == 5)) {
                this.mState = mState;
            }
            return;
        }
        final View view = this.mViewRef.get();
        if (view == null) {
            return;
        }
        final ViewParent parent = view.getParent();
        if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
            view.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    BottomSheetBehavior.this.startSettlingAnimation(view, mState);
                }
            });
        }
        else {
            this.startSettlingAnimation(view, mState);
        }
    }
    
    void setStateInternal(final int mState) {
        if (this.mState == mState) {
            return;
        }
        this.mState = mState;
        final View view = this.mViewRef.get();
        if (view != null && this.mCallback != null) {
            this.mCallback.onStateChanged(view, mState);
        }
    }
    
    boolean shouldHide(final View view, final float n) {
        final boolean mSkipCollapsed = this.mSkipCollapsed;
        boolean b = true;
        if (mSkipCollapsed) {
            return true;
        }
        if (view.getTop() < this.mMaxOffset) {
            return false;
        }
        if (Math.abs(view.getTop() + n * 0.1f - this.mMaxOffset) / this.mPeekHeight <= 0.5f) {
            b = false;
        }
        return b;
    }
    
    void startSettlingAnimation(final View view, final int n) {
        int n2;
        if (n == 4) {
            n2 = this.mMaxOffset;
        }
        else if (n == 3) {
            n2 = this.mMinOffset;
        }
        else {
            if (!this.mHideable || n != 5) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Illegal state argument: ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            n2 = this.mParentHeight;
        }
        if (this.mViewDragHelper.smoothSlideViewTo(view, view.getLeft(), n2)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation(view, new SettleRunnable(view, n));
        }
        else {
            this.setStateInternal(n);
        }
    }
    
    public abstract static class BottomSheetCallback
    {
        public abstract void onSlide(@NonNull final View p0, final float p1);
        
        public abstract void onStateChanged(@NonNull final View p0, final int p1);
    }
    
    protected static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        final int state;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel) {
            this(parcel, null);
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
        }
        
        public SavedState(final Parcelable parcelable, final int state) {
            super(parcelable);
            this.state = state;
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.state);
        }
    }
    
    private class SettleRunnable implements Runnable
    {
        private final int mTargetState;
        private final View mView;
        
        SettleRunnable(final View mView, final int mTargetState) {
            this.mView = mView;
            this.mTargetState = mTargetState;
        }
        
        @Override
        public void run() {
            if (BottomSheetBehavior.this.mViewDragHelper != null && BottomSheetBehavior.this.mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.mView, this);
            }
            else {
                BottomSheetBehavior.this.setStateInternal(this.mTargetState);
            }
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface State {
    }
}
