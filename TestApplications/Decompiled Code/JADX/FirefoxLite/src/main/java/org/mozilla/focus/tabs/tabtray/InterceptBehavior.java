package org.mozilla.focus.tabs.tabtray;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class InterceptBehavior<V extends View> extends BottomSheetBehavior<V> {
    private boolean intercept = true;

    public InterceptBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        return this.intercept || super.onInterceptTouchEvent(coordinatorLayout, v, motionEvent);
    }

    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        return this.intercept || super.onTouchEvent(coordinatorLayout, v, motionEvent);
    }

    /* Access modifiers changed, original: 0000 */
    public void setIntercept(boolean z) {
        this.intercept = z;
    }
}
