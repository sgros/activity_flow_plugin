package android.support.design.transformation;

import android.content.Context;
import android.support.design.expandable.ExpandableWidget;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.util.List;

public abstract class ExpandableBehavior extends Behavior<View> {
    private int currentState = 0;

    public abstract boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2);

    public abstract boolean onExpandedStateChange(View view, View view2, boolean z, boolean z2);

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, final View view, int i) {
        if (!ViewCompat.isLaidOut(view)) {
            final ExpandableWidget findExpandableWidget = findExpandableWidget(coordinatorLayout, view);
            if (findExpandableWidget != null && didStateChange(findExpandableWidget.isExpanded())) {
                this.currentState = findExpandableWidget.isExpanded() ? 1 : 2;
                i = this.currentState;
                view.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (ExpandableBehavior.this.currentState == i) {
                            ExpandableBehavior.this.onExpandedStateChange((View) findExpandableWidget, view, findExpandableWidget.isExpanded(), false);
                        }
                        return false;
                    }
                });
            }
        }
        return false;
    }

    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        ExpandableWidget expandableWidget = (ExpandableWidget) view2;
        if (!didStateChange(expandableWidget.isExpanded())) {
            return false;
        }
        this.currentState = expandableWidget.isExpanded() ? 1 : 2;
        return onExpandedStateChange((View) expandableWidget, view, expandableWidget.isExpanded(), true);
    }

    /* Access modifiers changed, original: protected */
    public ExpandableWidget findExpandableWidget(CoordinatorLayout coordinatorLayout, View view) {
        List dependencies = coordinatorLayout.getDependencies(view);
        int size = dependencies.size();
        for (int i = 0; i < size; i++) {
            View view2 = (View) dependencies.get(i);
            if (layoutDependsOn(coordinatorLayout, view, view2)) {
                return (ExpandableWidget) view2;
            }
        }
        return null;
    }

    private boolean didStateChange(boolean z) {
        boolean z2 = false;
        if (z) {
            if (this.currentState == 0 || this.currentState == 2) {
                z2 = true;
            }
            return z2;
        }
        if (this.currentState == 1) {
            z2 = true;
        }
        return z2;
    }
}
