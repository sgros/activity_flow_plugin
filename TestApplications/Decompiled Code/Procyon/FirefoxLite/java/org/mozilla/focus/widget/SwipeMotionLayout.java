// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.view.View$OnTouchListener;
import org.mozilla.focus.utils.OnSwipeListener;
import android.view.View;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import org.mozilla.focus.utils.SwipeMotionDetector;
import android.support.constraint.ConstraintLayout;

public class SwipeMotionLayout extends ConstraintLayout
{
    private SwipeMotionDetector swipeMotionDetector;
    
    public SwipeMotionLayout(final Context context) {
        this(context, null);
    }
    
    public SwipeMotionLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SwipeMotionLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        if (onInterceptTouchEvent && this.swipeMotionDetector != null) {
            this.swipeMotionDetector.onTouch((View)this, motionEvent);
        }
        return onInterceptTouchEvent;
    }
    
    public void setOnSwipeListener(final OnSwipeListener onSwipeListener) {
        if (onSwipeListener != null) {
            this.setOnTouchListener((View$OnTouchListener)(this.swipeMotionDetector = new SwipeMotionDetector(this.getContext(), onSwipeListener)));
        }
        else {
            this.swipeMotionDetector = null;
            this.setOnTouchListener((View$OnTouchListener)null);
        }
    }
}
