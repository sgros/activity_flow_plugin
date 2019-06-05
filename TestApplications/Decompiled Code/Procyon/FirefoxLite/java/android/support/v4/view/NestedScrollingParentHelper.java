// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper
{
    private int mNestedScrollAxes;
    private final ViewGroup mViewGroup;
    
    public NestedScrollingParentHelper(final ViewGroup mViewGroup) {
        this.mViewGroup = mViewGroup;
    }
    
    public int getNestedScrollAxes() {
        return this.mNestedScrollAxes;
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n) {
        this.onNestedScrollAccepted(view, view2, n, 0);
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int mNestedScrollAxes, final int n) {
        this.mNestedScrollAxes = mNestedScrollAxes;
    }
    
    public void onStopNestedScroll(final View view, final int n) {
        this.mNestedScrollAxes = 0;
    }
}
