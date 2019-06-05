package org.mozilla.focus.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import org.mozilla.focus.utils.OnSwipeListener;
import org.mozilla.focus.utils.SwipeMotionDetector;

public class SwipeMotionLayout extends ConstraintLayout {
    private SwipeMotionDetector swipeMotionDetector;

    public SwipeMotionLayout(Context context) {
        this(context, null);
    }

    public SwipeMotionLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwipeMotionLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        if (onInterceptTouchEvent && this.swipeMotionDetector != null) {
            this.swipeMotionDetector.onTouch(this, motionEvent);
        }
        return onInterceptTouchEvent;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        if (onSwipeListener != null) {
            this.swipeMotionDetector = new SwipeMotionDetector(getContext(), onSwipeListener);
            setOnTouchListener(this.swipeMotionDetector);
            return;
        }
        this.swipeMotionDetector = null;
        setOnTouchListener(null);
    }
}
