// 
// Decompiled by Procyon v0.5.34
// 

package androidx.recyclerview.widget;

import android.view.animation.Interpolator;
import android.view.View;
import android.content.Context;
import android.graphics.PointF;
import android.view.animation.LinearInterpolator;
import android.util.DisplayMetrics;
import android.view.animation.DecelerateInterpolator;

public class LinearSmoothScroller extends SmoothScroller
{
    protected final DecelerateInterpolator mDecelerateInterpolator;
    private final DisplayMetrics mDisplayMetrics;
    private boolean mHasCalculatedMillisPerPixel;
    protected int mInterimTargetDx;
    protected int mInterimTargetDy;
    protected final LinearInterpolator mLinearInterpolator;
    private float mMillisPerPixel;
    protected PointF mTargetVector;
    
    public LinearSmoothScroller(final Context context) {
        this.mLinearInterpolator = new LinearInterpolator();
        this.mDecelerateInterpolator = new DecelerateInterpolator();
        this.mHasCalculatedMillisPerPixel = false;
        this.mInterimTargetDx = 0;
        this.mInterimTargetDy = 0;
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
    }
    
    private int clampApplyScroll(final int n, int n2) {
        n2 = n - n2;
        if (n * n2 <= 0) {
            return 0;
        }
        return n2;
    }
    
    private float getSpeedPerPixel() {
        if (!this.mHasCalculatedMillisPerPixel) {
            this.mMillisPerPixel = this.calculateSpeedPerPixel(this.mDisplayMetrics);
            this.mHasCalculatedMillisPerPixel = true;
        }
        return this.mMillisPerPixel;
    }
    
    public int calculateDtToFit(int n, final int n2, final int n3, final int n4, final int n5) {
        if (n5 == -1) {
            return n3 - n;
        }
        if (n5 != 0) {
            if (n5 == 1) {
                return n4 - n2;
            }
            throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
        }
        else {
            n = n3 - n;
            if (n > 0) {
                return n;
            }
            n = n4 - n2;
            if (n < 0) {
                return n;
            }
            return 0;
        }
    }
    
    public int calculateDxToMakeVisible(final View view, final int n) {
        final LayoutManager layoutManager = ((RecyclerView.SmoothScroller)this).getLayoutManager();
        if (layoutManager != null && layoutManager.canScrollHorizontally()) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            return this.calculateDtToFit(layoutManager.getDecoratedLeft(view) - layoutParams.leftMargin, layoutManager.getDecoratedRight(view) + layoutParams.rightMargin, layoutManager.getPaddingLeft(), layoutManager.getWidth() - layoutManager.getPaddingRight(), n);
        }
        return 0;
    }
    
    public int calculateDyToMakeVisible(final View view, final int n) {
        final LayoutManager layoutManager = ((RecyclerView.SmoothScroller)this).getLayoutManager();
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            return this.calculateDtToFit(layoutManager.getDecoratedTop(view) - layoutParams.topMargin, layoutManager.getDecoratedBottom(view) + layoutParams.bottomMargin, layoutManager.getPaddingTop(), layoutManager.getHeight() - layoutManager.getPaddingBottom(), n);
        }
        return 0;
    }
    
    protected float calculateSpeedPerPixel(final DisplayMetrics displayMetrics) {
        return 25.0f / displayMetrics.densityDpi;
    }
    
    protected int calculateTimeForDeceleration(final int n) {
        final double v = this.calculateTimeForScrolling(n);
        Double.isNaN(v);
        return (int)Math.ceil(v / 0.3356);
    }
    
    protected int calculateTimeForScrolling(final int a) {
        return (int)Math.ceil(Math.abs(a) * this.getSpeedPerPixel());
    }
    
    protected int getHorizontalSnapPreference() {
        final PointF mTargetVector = this.mTargetVector;
        if (mTargetVector != null) {
            final float x = mTargetVector.x;
            if (x != 0.0f) {
                if (x > 0.0f) {
                    return 1;
                }
                return -1;
            }
        }
        return 0;
    }
    
    protected int getVerticalSnapPreference() {
        final PointF mTargetVector = this.mTargetVector;
        if (mTargetVector != null) {
            final float y = mTargetVector.y;
            if (y != 0.0f) {
                if (y > 0.0f) {
                    return 1;
                }
                return -1;
            }
        }
        return 0;
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
        final int calculateDxToMakeVisible = this.calculateDxToMakeVisible(view, this.getHorizontalSnapPreference());
        final int calculateDyToMakeVisible = this.calculateDyToMakeVisible(view, this.getVerticalSnapPreference());
        final int calculateTimeForDeceleration = this.calculateTimeForDeceleration((int)Math.sqrt(calculateDxToMakeVisible * calculateDxToMakeVisible + calculateDyToMakeVisible * calculateDyToMakeVisible));
        if (calculateTimeForDeceleration > 0) {
            action.update(-calculateDxToMakeVisible, -calculateDyToMakeVisible, calculateTimeForDeceleration, (Interpolator)this.mDecelerateInterpolator);
        }
    }
    
    protected void updateActionForInterimTarget(final Action action) {
        final PointF computeScrollVectorForPosition = ((RecyclerView.SmoothScroller)this).computeScrollVectorForPosition(((RecyclerView.SmoothScroller)this).getTargetPosition());
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
