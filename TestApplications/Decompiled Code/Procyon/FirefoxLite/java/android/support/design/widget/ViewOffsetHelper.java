// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.support.v4.view.ViewCompat;
import android.view.View;

class ViewOffsetHelper
{
    private int layoutLeft;
    private int layoutTop;
    private int offsetLeft;
    private int offsetTop;
    private final View view;
    
    public ViewOffsetHelper(final View view) {
        this.view = view;
    }
    
    private void updateOffsets() {
        ViewCompat.offsetTopAndBottom(this.view, this.offsetTop - (this.view.getTop() - this.layoutTop));
        ViewCompat.offsetLeftAndRight(this.view, this.offsetLeft - (this.view.getLeft() - this.layoutLeft));
    }
    
    public int getTopAndBottomOffset() {
        return this.offsetTop;
    }
    
    public void onViewLayout() {
        this.layoutTop = this.view.getTop();
        this.layoutLeft = this.view.getLeft();
        this.updateOffsets();
    }
    
    public boolean setLeftAndRightOffset(final int offsetLeft) {
        if (this.offsetLeft != offsetLeft) {
            this.offsetLeft = offsetLeft;
            this.updateOffsets();
            return true;
        }
        return false;
    }
    
    public boolean setTopAndBottomOffset(final int offsetTop) {
        if (this.offsetTop != offsetTop) {
            this.offsetTop = offsetTop;
            this.updateOffsets();
            return true;
        }
        return false;
    }
}
