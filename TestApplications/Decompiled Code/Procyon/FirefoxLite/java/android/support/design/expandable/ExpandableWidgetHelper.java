// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.expandable;

import android.os.Bundle;
import android.view.ViewParent;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

public final class ExpandableWidgetHelper
{
    private boolean expanded;
    private int expandedComponentIdHint;
    private final View widget;
    
    private void dispatchExpandedStateChanged() {
        final ViewParent parent = this.widget.getParent();
        if (parent instanceof CoordinatorLayout) {
            ((CoordinatorLayout)parent).dispatchDependentViewsChanged(this.widget);
        }
    }
    
    public int getExpandedComponentIdHint() {
        return this.expandedComponentIdHint;
    }
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public void onRestoreInstanceState(final Bundle bundle) {
        this.expanded = bundle.getBoolean("expanded", false);
        this.expandedComponentIdHint = bundle.getInt("expandedComponentIdHint", 0);
        if (this.expanded) {
            this.dispatchExpandedStateChanged();
        }
    }
    
    public Bundle onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean("expanded", this.expanded);
        bundle.putInt("expandedComponentIdHint", this.expandedComponentIdHint);
        return bundle;
    }
    
    public void setExpandedComponentIdHint(final int expandedComponentIdHint) {
        this.expandedComponentIdHint = expandedComponentIdHint;
    }
}
