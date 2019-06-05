// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.tabs.tabtray;

import android.view.MotionEvent;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

public class InterceptBehavior<V extends View> extends BottomSheetBehavior<V>
{
    private boolean intercept;
    
    public InterceptBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.intercept = true;
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        return this.intercept || super.onInterceptTouchEvent(coordinatorLayout, v, motionEvent);
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        return this.intercept || super.onTouchEvent(coordinatorLayout, v, motionEvent);
    }
    
    void setIntercept(final boolean intercept) {
        this.intercept = intercept;
    }
}
