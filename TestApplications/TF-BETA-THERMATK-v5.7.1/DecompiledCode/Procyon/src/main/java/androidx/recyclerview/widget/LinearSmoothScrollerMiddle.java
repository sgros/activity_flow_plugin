// 
// Decompiled by Procyon v0.5.34
// 

package androidx.recyclerview.widget;

import android.view.animation.Interpolator;
import android.view.View;
import android.content.Context;
import android.graphics.PointF;
import android.view.animation.LinearInterpolator;
import android.view.animation.DecelerateInterpolator;

public class LinearSmoothScrollerMiddle extends SmoothScroller
{
    private final float MILLISECONDS_PER_PX;
    protected final DecelerateInterpolator mDecelerateInterpolator;
    protected int mInterimTargetDx;
    protected int mInterimTargetDy;
    protected final LinearInterpolator mLinearInterpolator;
    protected PointF mTargetVector;
    
    public LinearSmoothScrollerMiddle(final Context context) {
        this.mLinearInterpolator = new LinearInterpolator();
        this.mDecelerateInterpolator = new DecelerateInterpolator(1.5f);
        this.mInterimTargetDx = 0;
        this.mInterimTargetDy = 0;
        this.MILLISECONDS_PER_PX = 25.0f / context.getResources().getDisplayMetrics().densityDpi;
    }
    
    private int clampApplyScroll(final int n, int n2) {
        n2 = n - n2;
        if (n * n2 <= 0) {
            return 0;
        }
        return n2;
    }
    
    public int calculateDyToMakeVisible(final View view) {
        final LayoutManager layoutManager = ((RecyclerView.SmoothScroller)this).getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.canScrollVertically()) {
                final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                final int n = layoutManager.getDecoratedTop(view) - layoutParams.topMargin;
                final int n2 = layoutManager.getDecoratedBottom(view) + layoutParams.bottomMargin;
                final int n3 = layoutManager.getHeight() - layoutManager.getPaddingBottom() - layoutManager.getPaddingTop();
                final int n4 = n2 - n;
                int n5;
                if (n4 > n3) {
                    n5 = 0;
                }
                else {
                    n5 = (n3 - n4) / 2;
                }
                final int n6 = n5 - n;
                if (n6 > 0) {
                    return n6;
                }
                final int n7 = n4 + n5 - n2;
                if (n7 < 0) {
                    return n7;
                }
            }
        }
        return 0;
    }
    
    protected int calculateTimeForDeceleration(final int n) {
        final double v = this.calculateTimeForScrolling(n);
        Double.isNaN(v);
        return (int)Math.ceil(v / 0.3356);
    }
    
    protected int calculateTimeForScrolling(final int a) {
        return (int)Math.ceil(Math.abs(a) * this.MILLISECONDS_PER_PX);
    }
    
    @Override
    public PointF computeScrollVectorForPosition(final int n) {
        final LayoutManager layoutManager = ((RecyclerView.SmoothScroller)this).getLayoutManager();
        if (layoutManager instanceof ScrollVectorProvider) {
            return ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(n);
        }
        return null;
    }
    
    @Override
    protected void onSeekTargetStep(final int n, final int n2, final State state, final Action action) {
        if (((RecyclerView.SmoothScroller)this).getChildCount() == 0) {
            ((RecyclerView.SmoothScroller)this).stop();
            return;
        }
        this.mInterimTargetDx = this.clampApplyScroll(this.mInterimTargetDx, n);
        this.mInterimTargetDy = this.clampApplyScroll(this.mInterimTargetDy, n2);
        if (this.mInterimTargetDx == 0 && this.mInterimTargetDy == 0) {
            this.updateActionForInterimTarget(action);
        }
    }
    
    @Override
    protected void onStart() {
    }
    
    @Override
    protected void onStop() {
        this.mInterimTargetDy = 0;
        this.mInterimTargetDx = 0;
        this.mTargetVector = null;
    }
    
    @Override
    protected void onTargetFound(final View view, final State state, final Action action) {
        final int calculateDyToMakeVisible = this.calculateDyToMakeVisible(view);
        final int calculateTimeForDeceleration = this.calculateTimeForDeceleration(calculateDyToMakeVisible);
        if (calculateTimeForDeceleration > 0) {
            action.update(0, -calculateDyToMakeVisible, Math.max(400, calculateTimeForDeceleration), (Interpolator)this.mDecelerateInterpolator);
        }
    }
    
    protected void updateActionForInterimTarget(final Action action) {
        final PointF computeScrollVectorForPosition = this.computeScrollVectorForPosition(((RecyclerView.SmoothScroller)this).getTargetPosition());
        if (computeScrollVectorForPosition != null && (computeScrollVectorForPosition.x != 0.0f || computeScrollVectorForPosition.y != 0.0f)) {
            ((RecyclerView.SmoothScroller)this).normalize(computeScrollVectorForPosition);
            this.mTargetVector = computeScrollVectorForPosition;
            this.mInterimTargetDx = (int)(computeScrollVectorForPosition.x * 10000.0f);
            this.mInterimTargetDy = (int)(computeScrollVectorForPosition.y * 10000.0f);
            action.update((int)(this.mInterimTargetDx * 1.2f), (int)(this.mInterimTargetDy * 1.2f), (int)(this.calculateTimeForScrolling(10000) * 1.2f), (Interpolator)this.mLinearInterpolator);
            return;
        }
        action.jumpTo(((RecyclerView.SmoothScroller)this).getTargetPosition());
        ((RecyclerView.SmoothScroller)this).stop();
    }
}
