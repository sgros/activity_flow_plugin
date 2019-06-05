// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.view.View$MeasureSpec;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.math.MathUtils;
import java.util.List;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;

abstract class HeaderScrollingViewBehavior extends ViewOffsetBehavior<View>
{
    private int overlayTop;
    final Rect tempRect1;
    final Rect tempRect2;
    private int verticalLayoutGap;
    
    public HeaderScrollingViewBehavior() {
        this.tempRect1 = new Rect();
        this.tempRect2 = new Rect();
        this.verticalLayoutGap = 0;
    }
    
    public HeaderScrollingViewBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.tempRect1 = new Rect();
        this.tempRect2 = new Rect();
        this.verticalLayoutGap = 0;
    }
    
    private static int resolveGravity(final int n) {
        int n2 = n;
        if (n == 0) {
            n2 = 8388659;
        }
        return n2;
    }
    
    abstract View findFirstDependency(final List<View> p0);
    
    final int getOverlapPixelsForOffset(final View view) {
        final int overlayTop = this.overlayTop;
        int clamp = 0;
        if (overlayTop != 0) {
            clamp = MathUtils.clamp((int)(this.getOverlapRatioForOffset(view) * this.overlayTop), 0, this.overlayTop);
        }
        return clamp;
    }
    
    float getOverlapRatioForOffset(final View view) {
        return 1.0f;
    }
    
    public final int getOverlayTop() {
        return this.overlayTop;
    }
    
    int getScrollRange(final View view) {
        return view.getMeasuredHeight();
    }
    
    final int getVerticalLayoutGap() {
        return this.verticalLayoutGap;
    }
    
    @Override
    protected void layoutChild(final CoordinatorLayout coordinatorLayout, final View view, int overlapPixelsForOffset) {
        final View firstDependency = this.findFirstDependency(coordinatorLayout.getDependencies(view));
        if (firstDependency != null) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect tempRect1 = this.tempRect1;
            tempRect1.set(coordinatorLayout.getPaddingLeft() + layoutParams.leftMargin, firstDependency.getBottom() + layoutParams.topMargin, coordinatorLayout.getWidth() - coordinatorLayout.getPaddingRight() - layoutParams.rightMargin, coordinatorLayout.getHeight() + firstDependency.getBottom() - coordinatorLayout.getPaddingBottom() - layoutParams.bottomMargin);
            final WindowInsetsCompat lastWindowInsets = coordinatorLayout.getLastWindowInsets();
            if (lastWindowInsets != null && ViewCompat.getFitsSystemWindows((View)coordinatorLayout) && !ViewCompat.getFitsSystemWindows(view)) {
                tempRect1.left += lastWindowInsets.getSystemWindowInsetLeft();
                tempRect1.right -= lastWindowInsets.getSystemWindowInsetRight();
            }
            final Rect tempRect2 = this.tempRect2;
            GravityCompat.apply(resolveGravity(layoutParams.gravity), view.getMeasuredWidth(), view.getMeasuredHeight(), tempRect1, tempRect2, overlapPixelsForOffset);
            overlapPixelsForOffset = this.getOverlapPixelsForOffset(firstDependency);
            view.layout(tempRect2.left, tempRect2.top - overlapPixelsForOffset, tempRect2.right, tempRect2.bottom - overlapPixelsForOffset);
            this.verticalLayoutGap = tempRect2.top - firstDependency.getBottom();
        }
        else {
            super.layoutChild(coordinatorLayout, view, overlapPixelsForOffset);
            this.verticalLayoutGap = 0;
        }
    }
    
    @Override
    public boolean onMeasureChild(final CoordinatorLayout coordinatorLayout, final View view, final int n, final int n2, int n3, final int n4) {
        final int height = view.getLayoutParams().height;
        if (height == -1 || height == -2) {
            final View firstDependency = this.findFirstDependency(coordinatorLayout.getDependencies(view));
            if (firstDependency != null) {
                if (ViewCompat.getFitsSystemWindows(firstDependency) && !ViewCompat.getFitsSystemWindows(view)) {
                    ViewCompat.setFitsSystemWindows(view, true);
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        view.requestLayout();
                        return true;
                    }
                }
                if ((n3 = View$MeasureSpec.getSize(n3)) == 0) {
                    n3 = coordinatorLayout.getHeight();
                }
                final int measuredHeight = firstDependency.getMeasuredHeight();
                final int scrollRange = this.getScrollRange(firstDependency);
                int n5;
                if (height == -1) {
                    n5 = 1073741824;
                }
                else {
                    n5 = Integer.MIN_VALUE;
                }
                coordinatorLayout.onMeasureChild(view, n, n2, View$MeasureSpec.makeMeasureSpec(n3 - measuredHeight + scrollRange, n5), n4);
                return true;
            }
        }
        return false;
    }
    
    public final void setOverlayTop(final int overlayTop) {
        this.overlayTop = overlayTop;
    }
}
