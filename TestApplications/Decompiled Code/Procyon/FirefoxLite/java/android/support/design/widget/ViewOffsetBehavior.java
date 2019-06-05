// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

class ViewOffsetBehavior<V extends View> extends Behavior<V>
{
    private int tempLeftRightOffset;
    private int tempTopBottomOffset;
    private ViewOffsetHelper viewOffsetHelper;
    
    public ViewOffsetBehavior() {
        this.tempTopBottomOffset = 0;
        this.tempLeftRightOffset = 0;
    }
    
    public ViewOffsetBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.tempTopBottomOffset = 0;
        this.tempLeftRightOffset = 0;
    }
    
    public int getTopAndBottomOffset() {
        int topAndBottomOffset;
        if (this.viewOffsetHelper != null) {
            topAndBottomOffset = this.viewOffsetHelper.getTopAndBottomOffset();
        }
        else {
            topAndBottomOffset = 0;
        }
        return topAndBottomOffset;
    }
    
    protected void layoutChild(final CoordinatorLayout coordinatorLayout, final V v, final int n) {
        coordinatorLayout.onLayoutChild(v, n);
    }
    
    @Override
    public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final V v, final int n) {
        this.layoutChild(coordinatorLayout, v, n);
        if (this.viewOffsetHelper == null) {
            this.viewOffsetHelper = new ViewOffsetHelper(v);
        }
        this.viewOffsetHelper.onViewLayout();
        if (this.tempTopBottomOffset != 0) {
            this.viewOffsetHelper.setTopAndBottomOffset(this.tempTopBottomOffset);
            this.tempTopBottomOffset = 0;
        }
        if (this.tempLeftRightOffset != 0) {
            this.viewOffsetHelper.setLeftAndRightOffset(this.tempLeftRightOffset);
            this.tempLeftRightOffset = 0;
        }
        return true;
    }
    
    public boolean setTopAndBottomOffset(final int n) {
        if (this.viewOffsetHelper != null) {
            return this.viewOffsetHelper.setTopAndBottomOffset(n);
        }
        this.tempTopBottomOffset = n;
        return false;
    }
}
