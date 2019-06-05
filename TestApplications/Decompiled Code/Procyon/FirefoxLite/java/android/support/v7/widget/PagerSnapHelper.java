// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import android.util.DisplayMetrics;
import android.content.Context;
import android.view.View;

public class PagerSnapHelper extends SnapHelper
{
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;
    
    private int distanceToCenter(final LayoutManager layoutManager, final View view, final OrientationHelper orientationHelper) {
        final int decoratedStart = orientationHelper.getDecoratedStart(view);
        final int n = orientationHelper.getDecoratedMeasurement(view) / 2;
        int n2;
        if (layoutManager.getClipToPadding()) {
            n2 = orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2;
        }
        else {
            n2 = orientationHelper.getEnd() / 2;
        }
        return decoratedStart + n - n2;
    }
    
    private View findCenterView(final LayoutManager layoutManager, final OrientationHelper orientationHelper) {
        final int childCount = layoutManager.getChildCount();
        View view = null;
        if (childCount == 0) {
            return null;
        }
        int n;
        if (layoutManager.getClipToPadding()) {
            n = orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2;
        }
        else {
            n = orientationHelper.getEnd() / 2;
        }
        int n2 = Integer.MAX_VALUE;
        int n3;
        for (int i = 0; i < childCount; ++i, n2 = n3) {
            final View child = layoutManager.getChildAt(i);
            final int abs = Math.abs(orientationHelper.getDecoratedStart(child) + orientationHelper.getDecoratedMeasurement(child) / 2 - n);
            if (abs < (n3 = n2)) {
                view = child;
                n3 = abs;
            }
        }
        return view;
    }
    
    private View findStartView(final LayoutManager layoutManager, final OrientationHelper orientationHelper) {
        final int childCount = layoutManager.getChildCount();
        View view = null;
        if (childCount == 0) {
            return null;
        }
        int n = Integer.MAX_VALUE;
        int n2;
        for (int i = 0; i < childCount; ++i, n = n2) {
            final View child = layoutManager.getChildAt(i);
            final int decoratedStart = orientationHelper.getDecoratedStart(child);
            if (decoratedStart < (n2 = n)) {
                view = child;
                n2 = decoratedStart;
            }
        }
        return view;
    }
    
    private OrientationHelper getHorizontalHelper(final LayoutManager layoutManager) {
        if (this.mHorizontalHelper == null || this.mHorizontalHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }
    
    private OrientationHelper getVerticalHelper(final LayoutManager layoutManager) {
        if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }
    
    @Override
    public int[] calculateDistanceToFinalSnap(final LayoutManager layoutManager, final View view) {
        final int[] array = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            array[0] = this.distanceToCenter(layoutManager, view, this.getHorizontalHelper(layoutManager));
        }
        else {
            array[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            array[1] = this.distanceToCenter(layoutManager, view, this.getVerticalHelper(layoutManager));
        }
        else {
            array[1] = 0;
        }
        return array;
    }
    
    @Override
    protected LinearSmoothScroller createSnapScroller(final LayoutManager layoutManager) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(this.mRecyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(final DisplayMetrics displayMetrics) {
                return 100.0f / displayMetrics.densityDpi;
            }
            
            @Override
            protected int calculateTimeForScrolling(final int n) {
                return Math.min(100, super.calculateTimeForScrolling(n));
            }
            
            @Override
            protected void onTargetFound(final View view, final State state, final Action action) {
                final int[] calculateDistanceToFinalSnap = PagerSnapHelper.this.calculateDistanceToFinalSnap(PagerSnapHelper.this.mRecyclerView.getLayoutManager(), view);
                final int a = calculateDistanceToFinalSnap[0];
                final int a2 = calculateDistanceToFinalSnap[1];
                final int calculateTimeForDeceleration = this.calculateTimeForDeceleration(Math.max(Math.abs(a), Math.abs(a2)));
                if (calculateTimeForDeceleration > 0) {
                    action.update(a, a2, calculateTimeForDeceleration, (Interpolator)this.mDecelerateInterpolator);
                }
            }
        };
    }
    
    @Override
    public View findSnapView(final LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.findCenterView(layoutManager, this.getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return this.findCenterView(layoutManager, this.getHorizontalHelper(layoutManager));
        }
        return null;
    }
    
    @Override
    public int findTargetSnapPosition(final LayoutManager layoutManager, int n, int n2) {
        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        }
        View view = null;
        if (layoutManager.canScrollVertically()) {
            view = this.findStartView(layoutManager, this.getVerticalHelper(layoutManager));
        }
        else if (layoutManager.canScrollHorizontally()) {
            view = this.findStartView(layoutManager, this.getHorizontalHelper(layoutManager));
        }
        if (view == null) {
            return -1;
        }
        final int position = layoutManager.getPosition(view);
        if (position == -1) {
            return -1;
        }
        final boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
        final int n3 = 0;
        if (canScrollHorizontally ? (n > 0) : (n2 > 0)) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        n = n3;
        Label_0169: {
            if (layoutManager instanceof ScrollVectorProvider) {
                final PointF computeScrollVectorForPosition = ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(itemCount - 1);
                n = n3;
                if (computeScrollVectorForPosition != null) {
                    if (computeScrollVectorForPosition.x >= 0.0f) {
                        n = n3;
                        if (computeScrollVectorForPosition.y >= 0.0f) {
                            break Label_0169;
                        }
                    }
                    n = 1;
                }
            }
        }
        if (n != 0) {
            n = position;
            if (n2 != 0) {
                n = position - 1;
            }
        }
        else {
            n = position;
            if (n2 != 0) {
                n = position + 1;
            }
        }
        return n;
    }
}
