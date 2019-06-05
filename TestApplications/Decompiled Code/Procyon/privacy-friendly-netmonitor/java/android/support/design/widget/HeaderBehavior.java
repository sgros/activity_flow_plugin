// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.v4.math.MathUtils;
import android.view.ViewConfiguration;
import android.view.MotionEvent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.widget.OverScroller;
import android.view.View;

abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V>
{
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId;
    private Runnable mFlingRunnable;
    private boolean mIsBeingDragged;
    private int mLastMotionY;
    OverScroller mScroller;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    
    public HeaderBehavior() {
        this.mActivePointerId = -1;
        this.mTouchSlop = -1;
    }
    
    public HeaderBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.mActivePointerId = -1;
        this.mTouchSlop = -1;
    }
    
    private void ensureVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }
    
    boolean canDragView(final V v) {
        return false;
    }
    
    final boolean fling(final CoordinatorLayout coordinatorLayout, final V v, final int n, final int n2, final float a) {
        if (this.mFlingRunnable != null) {
            v.removeCallbacks(this.mFlingRunnable);
            this.mFlingRunnable = null;
        }
        if (this.mScroller == null) {
            this.mScroller = new OverScroller(v.getContext());
        }
        this.mScroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(a), 0, 0, n, n2);
        if (this.mScroller.computeScrollOffset()) {
            ViewCompat.postOnAnimation(v, this.mFlingRunnable = new FlingRunnable(coordinatorLayout, v));
            return true;
        }
        this.onFlingFinished(coordinatorLayout, v);
        return false;
    }
    
    int getMaxDragOffset(final V v) {
        return -v.getHeight();
    }
    
    int getScrollRangeForDragFling(final V v) {
        return v.getHeight();
    }
    
    int getTopBottomOffsetForScrollingSibling() {
        return this.getTopAndBottomOffset();
    }
    
    void onFlingFinished(final CoordinatorLayout coordinatorLayout, final V v) {
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (this.mTouchSlop < 0) {
            this.mTouchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getAction() == 2 && this.mIsBeingDragged) {
            return true;
        }
        switch (motionEvent.getActionMasked()) {
            case 2: {
                final int mActivePointerId = this.mActivePointerId;
                if (mActivePointerId == -1) {
                    break;
                }
                final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                final int mLastMotionY = (int)motionEvent.getY(pointerIndex);
                if (Math.abs(mLastMotionY - this.mLastMotionY) > this.mTouchSlop) {
                    this.mIsBeingDragged = true;
                    this.mLastMotionY = mLastMotionY;
                    break;
                }
                break;
            }
            case 1:
            case 3: {
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            }
            case 0: {
                this.mIsBeingDragged = false;
                final int n = (int)motionEvent.getX();
                final int mLastMotionY2 = (int)motionEvent.getY();
                if (this.canDragView(v) && coordinatorLayout.isPointInChildBounds(v, n, mLastMotionY2)) {
                    this.mLastMotionY = mLastMotionY2;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.ensureVelocityTracker();
                    break;
                }
                break;
            }
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(motionEvent);
        }
        return this.mIsBeingDragged;
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (this.mTouchSlop < 0) {
            this.mTouchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        switch (motionEvent.getActionMasked()) {
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (pointerIndex == -1) {
                    return false;
                }
                final int mLastMotionY = (int)motionEvent.getY(pointerIndex);
                int n;
                final int a = n = this.mLastMotionY - mLastMotionY;
                if (!this.mIsBeingDragged) {
                    n = a;
                    if (Math.abs(a) > this.mTouchSlop) {
                        this.mIsBeingDragged = true;
                        if (a > 0) {
                            n = a - this.mTouchSlop;
                        }
                        else {
                            n = a + this.mTouchSlop;
                        }
                    }
                }
                if (this.mIsBeingDragged) {
                    this.mLastMotionY = mLastMotionY;
                    this.scroll(coordinatorLayout, v, n, this.getMaxDragOffset(v), 0);
                    break;
                }
                break;
            }
            case 1: {
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    this.fling(coordinatorLayout, v, -this.getScrollRangeForDragFling(v), 0, this.mVelocityTracker.getYVelocity(this.mActivePointerId));
                }
            }
            case 3: {
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            }
            case 0: {
                final int n2 = (int)motionEvent.getX();
                final int mLastMotionY2 = (int)motionEvent.getY();
                if (coordinatorLayout.isPointInChildBounds(v, n2, mLastMotionY2) && this.canDragView(v)) {
                    this.mLastMotionY = mLastMotionY2;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.ensureVelocityTracker();
                    break;
                }
                return false;
            }
        }
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.addMovement(motionEvent);
        }
        return true;
    }
    
    final int scroll(final CoordinatorLayout coordinatorLayout, final V v, final int n, final int n2, final int n3) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, this.getTopBottomOffsetForScrollingSibling() - n, n2, n3);
    }
    
    int setHeaderTopBottomOffset(final CoordinatorLayout coordinatorLayout, final V v, final int n) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    int setHeaderTopBottomOffset(final CoordinatorLayout coordinatorLayout, final V v, int clamp, final int n, final int n2) {
        final int topAndBottomOffset = this.getTopAndBottomOffset();
        if (n != 0 && topAndBottomOffset >= n && topAndBottomOffset <= n2) {
            clamp = MathUtils.clamp(clamp, n, n2);
            if (topAndBottomOffset != clamp) {
                this.setTopAndBottomOffset(clamp);
                clamp = topAndBottomOffset - clamp;
                return clamp;
            }
        }
        clamp = 0;
        return clamp;
    }
    
    private class FlingRunnable implements Runnable
    {
        private final V mLayout;
        private final CoordinatorLayout mParent;
        
        FlingRunnable(final CoordinatorLayout mParent, final V mLayout) {
            this.mParent = mParent;
            this.mLayout = mLayout;
        }
        
        @Override
        public void run() {
            if (this.mLayout != null && HeaderBehavior.this.mScroller != null) {
                if (HeaderBehavior.this.mScroller.computeScrollOffset()) {
                    HeaderBehavior.this.setHeaderTopBottomOffset(this.mParent, this.mLayout, HeaderBehavior.this.mScroller.getCurrY());
                    ViewCompat.postOnAnimation(this.mLayout, this);
                }
                else {
                    HeaderBehavior.this.onFlingFinished(this.mParent, this.mLayout);
                }
            }
        }
    }
}
