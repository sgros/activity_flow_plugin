// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.transformation;

import android.view.ViewTreeObserver$OnPreDrawListener;
import android.support.v4.view.ViewCompat;
import java.util.List;
import android.support.design.expandable.ExpandableWidget;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.support.design.widget.CoordinatorLayout;

public abstract class ExpandableBehavior extends Behavior<View>
{
    private int currentState;
    
    public ExpandableBehavior() {
        this.currentState = 0;
    }
    
    public ExpandableBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.currentState = 0;
    }
    
    private boolean didStateChange(final boolean b) {
        final boolean b2 = false;
        final boolean b3 = false;
        if (b) {
            if (this.currentState != 0) {
                final boolean b4 = b3;
                if (this.currentState != 2) {
                    return b4;
                }
            }
            return true;
        }
        boolean b5 = b2;
        if (this.currentState == 1) {
            b5 = true;
        }
        return b5;
    }
    
    protected ExpandableWidget findExpandableWidget(final CoordinatorLayout coordinatorLayout, final View view) {
        final List<View> dependencies = coordinatorLayout.getDependencies(view);
        for (int size = dependencies.size(), i = 0; i < size; ++i) {
            final View view2 = dependencies.get(i);
            if (this.layoutDependsOn(coordinatorLayout, view, view2)) {
                return (ExpandableWidget)view2;
            }
        }
        return null;
    }
    
    @Override
    public abstract boolean layoutDependsOn(final CoordinatorLayout p0, final View p1, final View p2);
    
    @Override
    public boolean onDependentViewChanged(final CoordinatorLayout coordinatorLayout, final View view, final View view2) {
        final ExpandableWidget expandableWidget = (ExpandableWidget)view2;
        if (this.didStateChange(expandableWidget.isExpanded())) {
            int currentState;
            if (expandableWidget.isExpanded()) {
                currentState = 1;
            }
            else {
                currentState = 2;
            }
            this.currentState = currentState;
            return this.onExpandedStateChange((View)expandableWidget, view, expandableWidget.isExpanded(), true);
        }
        return false;
    }
    
    protected abstract boolean onExpandedStateChange(final View p0, final View p1, final boolean p2, final boolean p3);
    
    @Override
    public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final View view, int currentState) {
        if (!ViewCompat.isLaidOut(view)) {
            final ExpandableWidget expandableWidget = this.findExpandableWidget(coordinatorLayout, view);
            if (expandableWidget != null && this.didStateChange(expandableWidget.isExpanded())) {
                if (expandableWidget.isExpanded()) {
                    currentState = 1;
                }
                else {
                    currentState = 2;
                }
                this.currentState = currentState;
                currentState = this.currentState;
                view.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                    public boolean onPreDraw() {
                        view.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                        if (ExpandableBehavior.this.currentState == currentState) {
                            ExpandableBehavior.this.onExpandedStateChange((View)expandableWidget, view, expandableWidget.isExpanded(), false);
                        }
                        return false;
                    }
                });
            }
        }
        return false;
    }
}
