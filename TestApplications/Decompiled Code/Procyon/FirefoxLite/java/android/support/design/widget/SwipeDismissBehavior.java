// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;

public class SwipeDismissBehavior<V extends View> extends Behavior<V>
{
    float alphaEndSwipeDistance;
    float alphaStartSwipeDistance;
    private final ViewDragHelper.Callback dragCallback;
    float dragDismissThreshold;
    private boolean interceptingEvents;
    OnDismissListener listener;
    private float sensitivity;
    private boolean sensitivitySet;
    int swipeDirection;
    ViewDragHelper viewDragHelper;
    
    public SwipeDismissBehavior() {
        this.sensitivity = 0.0f;
        this.swipeDirection = 2;
        this.dragDismissThreshold = 0.5f;
        this.alphaStartSwipeDistance = 0.0f;
        this.alphaEndSwipeDistance = 0.5f;
        this.dragCallback = new ViewDragHelper.Callback() {
            private int activePointerId = -1;
            private int originalCapturedViewLeft;
            
            private boolean shouldDismiss(final View view, final float n) {
                final boolean b = false;
                final boolean b2 = false;
                boolean b3 = false;
                final float n2 = fcmpl(n, 0.0f);
                if (n2 == 0) {
                    final int left = view.getLeft();
                    final int originalCapturedViewLeft = this.originalCapturedViewLeft;
                    final int round = Math.round(view.getWidth() * SwipeDismissBehavior.this.dragDismissThreshold);
                    boolean b4 = b2;
                    if (Math.abs(left - originalCapturedViewLeft) >= round) {
                        b4 = true;
                    }
                    return b4;
                }
                final boolean b5 = ViewCompat.getLayoutDirection(view) == 1;
                if (SwipeDismissBehavior.this.swipeDirection == 2) {
                    return true;
                }
                if (SwipeDismissBehavior.this.swipeDirection == 0) {
                    if (b5) {
                        if (n >= 0.0f) {
                            return b3;
                        }
                    }
                    else if (n2 <= 0) {
                        return b3;
                    }
                    b3 = true;
                    return b3;
                }
                if (SwipeDismissBehavior.this.swipeDirection == 1) {
                    if (b5) {
                        final boolean b6 = b;
                        if (n2 <= 0) {
                            return b6;
                        }
                    }
                    else {
                        final boolean b6 = b;
                        if (n >= 0.0f) {
                            return b6;
                        }
                    }
                    return true;
                }
                return false;
            }
            
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, int n2) {
                if (ViewCompat.getLayoutDirection(view) == 1) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                int n3;
                if (SwipeDismissBehavior.this.swipeDirection == 0) {
                    if (n2 != 0) {
                        n3 = this.originalCapturedViewLeft - view.getWidth();
                        n2 = this.originalCapturedViewLeft;
                    }
                    else {
                        n3 = this.originalCapturedViewLeft;
                        n2 = this.originalCapturedViewLeft;
                        n2 += view.getWidth();
                    }
                }
                else if (SwipeDismissBehavior.this.swipeDirection == 1) {
                    if (n2 != 0) {
                        n3 = this.originalCapturedViewLeft;
                        n2 = this.originalCapturedViewLeft;
                        n2 += view.getWidth();
                    }
                    else {
                        n3 = this.originalCapturedViewLeft - view.getWidth();
                        n2 = this.originalCapturedViewLeft;
                    }
                }
                else {
                    n3 = this.originalCapturedViewLeft - view.getWidth();
                    n2 = this.originalCapturedViewLeft;
                    n2 += view.getWidth();
                }
                return SwipeDismissBehavior.clamp(n3, n, n2);
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, final int n2) {
                return view.getTop();
            }
            
            @Override
            public int getViewHorizontalDragRange(final View view) {
                return view.getWidth();
            }
            
            @Override
            public void onViewCaptured(final View view, final int activePointerId) {
                this.activePointerId = activePointerId;
                this.originalCapturedViewLeft = view.getLeft();
                final ViewParent parent = view.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (SwipeDismissBehavior.this.listener != null) {
                    SwipeDismissBehavior.this.listener.onDragStateChanged(n);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                final float n5 = this.originalCapturedViewLeft + view.getWidth() * SwipeDismissBehavior.this.alphaStartSwipeDistance;
                final float n6 = this.originalCapturedViewLeft + view.getWidth() * SwipeDismissBehavior.this.alphaEndSwipeDistance;
                final float n7 = (float)n;
                if (n7 <= n5) {
                    view.setAlpha(1.0f);
                }
                else if (n7 >= n6) {
                    view.setAlpha(0.0f);
                }
                else {
                    view.setAlpha(SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(n5, n6, n7), 1.0f));
                }
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                this.activePointerId = -1;
                final int width = view.getWidth();
                int originalCapturedViewLeft;
                boolean b;
                if (this.shouldDismiss(view, n)) {
                    if (view.getLeft() < this.originalCapturedViewLeft) {
                        originalCapturedViewLeft = this.originalCapturedViewLeft - width;
                    }
                    else {
                        originalCapturedViewLeft = this.originalCapturedViewLeft + width;
                    }
                    b = true;
                }
                else {
                    originalCapturedViewLeft = this.originalCapturedViewLeft;
                    b = false;
                }
                if (SwipeDismissBehavior.this.viewDragHelper.settleCapturedViewAt(originalCapturedViewLeft, view.getTop())) {
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, b));
                }
                else if (b && SwipeDismissBehavior.this.listener != null) {
                    SwipeDismissBehavior.this.listener.onDismiss(view);
                }
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                return this.activePointerId == -1 && SwipeDismissBehavior.this.canSwipeDismissView(view);
            }
        };
    }
    
    static float clamp(final float a, final float b, final float b2) {
        return Math.min(Math.max(a, b), b2);
    }
    
    static int clamp(final int a, final int b, final int b2) {
        return Math.min(Math.max(a, b), b2);
    }
    
    private void ensureViewDragHelper(final ViewGroup viewGroup) {
        if (this.viewDragHelper == null) {
            ViewDragHelper viewDragHelper;
            if (this.sensitivitySet) {
                viewDragHelper = ViewDragHelper.create(viewGroup, this.sensitivity, this.dragCallback);
            }
            else {
                viewDragHelper = ViewDragHelper.create(viewGroup, this.dragCallback);
            }
            this.viewDragHelper = viewDragHelper;
        }
    }
    
    static float fraction(final float n, final float n2, final float n3) {
        return (n3 - n) / (n2 - n);
    }
    
    public boolean canSwipeDismissView(final View view) {
        return true;
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        boolean b = this.interceptingEvents;
        final int actionMasked = motionEvent.getActionMasked();
        Label_0080: {
            if (actionMasked != 3) {
                switch (actionMasked) {
                    default: {
                        break Label_0080;
                    }
                    case 0: {
                        this.interceptingEvents = coordinatorLayout.isPointInChildBounds(v, (int)motionEvent.getX(), (int)motionEvent.getY());
                        b = this.interceptingEvents;
                        break Label_0080;
                    }
                    case 1: {
                        break;
                    }
                }
            }
            this.interceptingEvents = false;
        }
        if (b) {
            this.ensureViewDragHelper(coordinatorLayout);
            return this.viewDragHelper.shouldInterceptTouchEvent(motionEvent);
        }
        return false;
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (this.viewDragHelper != null) {
            this.viewDragHelper.processTouchEvent(motionEvent);
            return true;
        }
        return false;
    }
    
    public void setEndAlphaSwipeDistance(final float n) {
        this.alphaEndSwipeDistance = clamp(0.0f, n, 1.0f);
    }
    
    public void setListener(final OnDismissListener listener) {
        this.listener = listener;
    }
    
    public void setStartAlphaSwipeDistance(final float n) {
        this.alphaStartSwipeDistance = clamp(0.0f, n, 1.0f);
    }
    
    public void setSwipeDirection(final int swipeDirection) {
        this.swipeDirection = swipeDirection;
    }
    
    public interface OnDismissListener
    {
        void onDismiss(final View p0);
        
        void onDragStateChanged(final int p0);
    }
    
    private class SettleRunnable implements Runnable
    {
        private final boolean dismiss;
        private final View view;
        
        SettleRunnable(final View view, final boolean dismiss) {
            this.view = view;
            this.dismiss = dismiss;
        }
        
        @Override
        public void run() {
            if (SwipeDismissBehavior.this.viewDragHelper != null && SwipeDismissBehavior.this.viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, this);
            }
            else if (this.dismiss && SwipeDismissBehavior.this.listener != null) {
                SwipeDismissBehavior.this.listener.onDismiss(this.view);
            }
        }
    }
}
