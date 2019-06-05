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
    private int activePointerId;
    private Runnable flingRunnable;
    private boolean isBeingDragged;
    private int lastMotionY;
    OverScroller scroller;
    private int touchSlop;
    private VelocityTracker velocityTracker;
    
    public HeaderBehavior() {
        this.activePointerId = -1;
        this.touchSlop = -1;
    }
    
    public HeaderBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.activePointerId = -1;
        this.touchSlop = -1;
    }
    
    private void ensureVelocityTracker() {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
    }
    
    boolean canDragView(final V v) {
        return false;
    }
    
    final boolean fling(final CoordinatorLayout coordinatorLayout, final V v, final int n, final int n2, final float a) {
        if (this.flingRunnable != null) {
            v.removeCallbacks(this.flingRunnable);
            this.flingRunnable = null;
        }
        if (this.scroller == null) {
            this.scroller = new OverScroller(v.getContext());
        }
        this.scroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(a), 0, 0, n, n2);
        if (this.scroller.computeScrollOffset()) {
            ViewCompat.postOnAnimation(v, this.flingRunnable = new FlingRunnable(coordinatorLayout, v));
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
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getAction() == 2 && this.isBeingDragged) {
            return true;
        }
        switch (motionEvent.getActionMasked()) {
            case 2: {
                final int activePointerId = this.activePointerId;
                if (activePointerId == -1) {
                    break;
                }
                final int pointerIndex = motionEvent.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                final int lastMotionY = (int)motionEvent.getY(pointerIndex);
                if (Math.abs(lastMotionY - this.lastMotionY) > this.touchSlop) {
                    this.isBeingDragged = true;
                    this.lastMotionY = lastMotionY;
                    break;
                }
                break;
            }
            case 1:
            case 3: {
                this.isBeingDragged = false;
                this.activePointerId = -1;
                if (this.velocityTracker != null) {
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                    break;
                }
                break;
            }
            case 0: {
                this.isBeingDragged = false;
                final int n = (int)motionEvent.getX();
                final int lastMotionY2 = (int)motionEvent.getY();
                if (this.canDragView(v) && coordinatorLayout.isPointInChildBounds(v, n, lastMotionY2)) {
                    this.lastMotionY = lastMotionY2;
                    this.activePointerId = motionEvent.getPointerId(0);
                    this.ensureVelocityTracker();
                    break;
                }
                break;
            }
        }
        if (this.velocityTracker != null) {
            this.velocityTracker.addMovement(motionEvent);
        }
        return this.isBeingDragged;
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (this.touchSlop < 0) {
            this.touchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        switch (motionEvent.getActionMasked()) {
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.activePointerId);
                if (pointerIndex == -1) {
                    return false;
                }
                final int lastMotionY = (int)motionEvent.getY(pointerIndex);
                int n;
                final int a = n = this.lastMotionY - lastMotionY;
                if (!this.isBeingDragged) {
                    n = a;
                    if (Math.abs(a) > this.touchSlop) {
                        this.isBeingDragged = true;
                        if (a > 0) {
                            n = a - this.touchSlop;
                        }
                        else {
                            n = a + this.touchSlop;
                        }
                    }
                }
                if (this.isBeingDragged) {
                    this.lastMotionY = lastMotionY;
                    this.scroll(coordinatorLayout, v, n, this.getMaxDragOffset(v), 0);
                    break;
                }
                break;
            }
            case 1: {
                if (this.velocityTracker != null) {
                    this.velocityTracker.addMovement(motionEvent);
                    this.velocityTracker.computeCurrentVelocity(1000);
                    this.fling(coordinatorLayout, v, -this.getScrollRangeForDragFling(v), 0, this.velocityTracker.getYVelocity(this.activePointerId));
                }
            }
            case 3: {
                this.isBeingDragged = false;
                this.activePointerId = -1;
                if (this.velocityTracker != null) {
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                    break;
                }
                break;
            }
            case 0: {
                final int n2 = (int)motionEvent.getX();
                final int lastMotionY2 = (int)motionEvent.getY();
                if (coordinatorLayout.isPointInChildBounds(v, n2, lastMotionY2) && this.canDragView(v)) {
                    this.lastMotionY = lastMotionY2;
                    this.activePointerId = motionEvent.getPointerId(0);
                    this.ensureVelocityTracker();
                    break;
                }
                return false;
            }
        }
        if (this.velocityTracker != null) {
            this.velocityTracker.addMovement(motionEvent);
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
        private final V layout;
        private final CoordinatorLayout parent;
        
        FlingRunnable(final CoordinatorLayout parent, final V layout) {
            this.parent = parent;
            this.layout = layout;
        }
        
        @Override
        public void run() {
            if (this.layout != null && HeaderBehavior.this.scroller != null) {
                if (HeaderBehavior.this.scroller.computeScrollOffset()) {
                    HeaderBehavior.this.setHeaderTopBottomOffset(this.parent, this.layout, HeaderBehavior.this.scroller.getCurrY());
                    ViewCompat.postOnAnimation(this.layout, this);
                }
                else {
                    HeaderBehavior.this.onFlingFinished(this.parent, this.layout);
                }
            }
        }
    }
}
