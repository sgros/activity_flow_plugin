// 
// Decompiled by Procyon v0.5.34
// 

package androidx.recyclerview.widget;

import android.graphics.Rect;
import java.util.Arrays;
import android.view.View$MeasureSpec;
import android.content.Context;
import android.view.View;
import java.util.ArrayList;

public class GridLayoutManagerFixed extends GridLayoutManager
{
    private ArrayList<View> additionalViews;
    private boolean canScrollVertically;
    
    public GridLayoutManagerFixed(final Context context, final int n) {
        super(context, n);
        this.additionalViews = new ArrayList<View>(4);
        this.canScrollVertically = true;
    }
    
    public GridLayoutManagerFixed(final Context context, final int n, final int n2, final boolean b) {
        super(context, n, n2, b);
        this.additionalViews = new ArrayList<View>(4);
        this.canScrollVertically = true;
    }
    
    @Override
    protected int[] calculateItemBorders(final int[] array, final int n, final int n2) {
        int i = 1;
        int[] array2 = null;
        Label_0035: {
            if (array != null && array.length == n + 1) {
                array2 = array;
                if (array[array.length - 1] == n2) {
                    break Label_0035;
                }
            }
            array2 = new int[n + 1];
        }
        array2[0] = 0;
        while (i <= n) {
            array2[i] = (int)Math.ceil(i / (float)n * n2);
            ++i;
        }
        return array2;
    }
    
    @Override
    public boolean canScrollVertically() {
        return this.canScrollVertically;
    }
    
    protected boolean hasSiblingChild(final int n) {
        throw null;
    }
    
    @Override
    void layoutChunk(final Recycler recycler, final State state, final LayoutState layoutState, final LayoutChunkResult layoutChunkResult) {
        final int modeInOther = super.mOrientationHelper.getModeInOther();
        final boolean b = layoutState.mItemDirection == 1;
        layoutChunkResult.mConsumed = 0;
        final int mCurrentPosition = layoutState.mCurrentPosition;
        if (layoutState.mLayoutDirection != -1 && this.hasSiblingChild(mCurrentPosition) && this.findViewByPosition(layoutState.mCurrentPosition + 1) == null) {
            if (this.hasSiblingChild(layoutState.mCurrentPosition + 1)) {
                layoutState.mCurrentPosition += 3;
            }
            else {
                layoutState.mCurrentPosition += 2;
            }
            int i;
            int mCurrentPosition2;
            for (mCurrentPosition2 = (i = layoutState.mCurrentPosition); i > mCurrentPosition; --i) {
                final View next = layoutState.next(recycler);
                this.additionalViews.add(next);
                if (i != mCurrentPosition2) {
                    ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(next, super.mDecorInsets);
                    this.measureChild(next, modeInOther, false);
                    final int decoratedMeasurement = super.mOrientationHelper.getDecoratedMeasurement(next);
                    layoutState.mOffset -= decoratedMeasurement;
                    layoutState.mAvailable += decoratedMeasurement;
                }
            }
            layoutState.mCurrentPosition = mCurrentPosition2;
        }
        int n;
        for (int j = 1; j != 0; j = n) {
            int mSpanCount = super.mSpanCount;
            final boolean empty = this.additionalViews.isEmpty();
            final int mCurrentPosition3 = layoutState.mCurrentPosition;
            n = ((empty ^ true) ? 1 : 0);
            int n2 = 0;
            while (n2 < super.mSpanCount && layoutState.hasMore(state) && mSpanCount > 0) {
                final int mCurrentPosition4 = layoutState.mCurrentPosition;
                final int n3 = mSpanCount - this.getSpanSize(recycler, state, mCurrentPosition4);
                if (n3 < 0) {
                    break;
                }
                View next2;
                if (!this.additionalViews.isEmpty()) {
                    next2 = this.additionalViews.get(0);
                    this.additionalViews.remove(0);
                    --layoutState.mCurrentPosition;
                }
                else {
                    next2 = layoutState.next(recycler);
                }
                if (next2 == null) {
                    break;
                }
                super.mSet[n2] = next2;
                final int n4 = n2 + 1;
                mSpanCount = n3;
                n2 = n4;
                if (layoutState.mLayoutDirection != -1) {
                    continue;
                }
                mSpanCount = n3;
                n2 = n4;
                if (n3 > 0) {
                    continue;
                }
                mSpanCount = n3;
                n2 = n4;
                if (!this.hasSiblingChild(mCurrentPosition4)) {
                    continue;
                }
                n = 1;
                mSpanCount = n3;
                n2 = n4;
            }
            if (n2 == 0) {
                layoutChunkResult.mFinished = true;
                return;
            }
            this.assignSpans(recycler, state, n2, b);
            int k = 0;
            float n5 = 0.0f;
            int n6 = 0;
            while (k < n2) {
                final View view = super.mSet[k];
                if (layoutState.mScrapList == null) {
                    if (b) {
                        ((RecyclerView.LayoutManager)this).addView(view);
                    }
                    else {
                        ((RecyclerView.LayoutManager)this).addView(view, 0);
                    }
                }
                else if (b) {
                    ((RecyclerView.LayoutManager)this).addDisappearingView(view);
                }
                else {
                    ((RecyclerView.LayoutManager)this).addDisappearingView(view, 0);
                }
                ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, super.mDecorInsets);
                this.measureChild(view, modeInOther, false);
                final int decoratedMeasurement2 = super.mOrientationHelper.getDecoratedMeasurement(view);
                int n7 = n6;
                if (decoratedMeasurement2 > n6) {
                    n7 = decoratedMeasurement2;
                }
                final float n8 = super.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0f / ((LayoutParams)view.getLayoutParams()).mSpanSize;
                float n9 = n5;
                if (n8 > n5) {
                    n9 = n8;
                }
                ++k;
                n5 = n9;
                n6 = n7;
            }
            for (int l = 0; l < n2; ++l) {
                final View view2 = super.mSet[l];
                if (super.mOrientationHelper.getDecoratedMeasurement(view2) != n6) {
                    final LayoutParams layoutParams = (LayoutParams)view2.getLayoutParams();
                    final Rect mDecorInsets = layoutParams.mDecorInsets;
                    this.measureChildWithDecorationsAndMargin(view2, RecyclerView.LayoutManager.getChildMeasureSpec(super.mCachedBorders[layoutParams.mSpanSize], 1073741824, mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width, false), View$MeasureSpec.makeMeasureSpec(n6 - (mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin), 1073741824), true);
                }
            }
            final boolean shouldLayoutChildFromOpositeSide = this.shouldLayoutChildFromOpositeSide(super.mSet[0]);
            int n16;
            if ((shouldLayoutChildFromOpositeSide && layoutState.mLayoutDirection == -1) || (!shouldLayoutChildFromOpositeSide && layoutState.mLayoutDirection == 1)) {
                int n11;
                int n10;
                int width;
                if (layoutState.mLayoutDirection == -1) {
                    n10 = (n11 = layoutState.mOffset - layoutChunkResult.mConsumed) - n6;
                    width = 0;
                }
                else {
                    final int n12 = layoutState.mOffset + layoutChunkResult.mConsumed;
                    width = ((RecyclerView.LayoutManager)this).getWidth();
                    n10 = n12;
                    n11 = n12 + n6;
                }
                int n13 = n2 - 1;
                int n14 = width;
                while (n13 >= 0) {
                    final View view3 = super.mSet[n13];
                    final LayoutParams layoutParams2 = (LayoutParams)view3.getLayoutParams();
                    final int decoratedMeasurementInOther = super.mOrientationHelper.getDecoratedMeasurementInOther(view3);
                    int n15 = n14;
                    if (layoutState.mLayoutDirection == 1) {
                        n15 = n14 - decoratedMeasurementInOther;
                    }
                    n14 = n15 + decoratedMeasurementInOther;
                    ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(view3, n15, n10, n14, n11);
                    if (layoutState.mLayoutDirection != -1) {
                        n14 = n15;
                    }
                    if (((RecyclerView.LayoutParams)layoutParams2).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams2).isItemChanged()) {
                        layoutChunkResult.mIgnoreConsumed = true;
                    }
                    layoutChunkResult.mFocusable |= view3.hasFocusable();
                    --n13;
                }
                n16 = n6;
            }
            else {
                final int n17 = n6;
                int width2;
                int n19;
                int n20;
                if (layoutState.mLayoutDirection == -1) {
                    final int n18 = layoutState.mOffset - layoutChunkResult.mConsumed;
                    width2 = ((RecyclerView.LayoutManager)this).getWidth();
                    n19 = n18;
                    n20 = n18 - n17;
                }
                else {
                    n19 = (n20 = layoutState.mOffset + layoutChunkResult.mConsumed) + n17;
                    width2 = 0;
                }
                int n21 = 0;
                final int n22 = n2;
                while (true) {
                    n16 = n17;
                    if (n21 >= n22) {
                        break;
                    }
                    final View view4 = super.mSet[n21];
                    final LayoutParams layoutParams3 = (LayoutParams)view4.getLayoutParams();
                    final int decoratedMeasurementInOther2 = super.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    int n23 = width2;
                    if (layoutState.mLayoutDirection == -1) {
                        n23 = width2 - decoratedMeasurementInOther2;
                    }
                    width2 = n23 + decoratedMeasurementInOther2;
                    ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(view4, n23, n20, width2, n19);
                    if (layoutState.mLayoutDirection == -1) {
                        width2 = n23;
                    }
                    if (((RecyclerView.LayoutParams)layoutParams3).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams3).isItemChanged()) {
                        layoutChunkResult.mIgnoreConsumed = true;
                    }
                    layoutChunkResult.mFocusable |= view4.hasFocusable();
                    ++n21;
                }
            }
            layoutChunkResult.mConsumed += n16;
            Arrays.fill(super.mSet, null);
        }
    }
    
    @Override
    protected void measureChild(final View view, final int n, final boolean b) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        this.measureChildWithDecorationsAndMargin(view, RecyclerView.LayoutManager.getChildMeasureSpec(super.mCachedBorders[layoutParams.mSpanSize], n, mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width, false), RecyclerView.LayoutManager.getChildMeasureSpec(super.mOrientationHelper.getTotalSpace(), ((RecyclerView.LayoutManager)this).getHeightMode(), mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height, true), b);
    }
    
    @Override
    protected void recycleViewsFromStart(final Recycler recycler, final int n, int i) {
        if (n < 0) {
            return;
        }
        int childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        if (super.mShouldReverseLayout) {
            View child;
            for (i = --childCount; i >= 0; --i) {
                child = ((RecyclerView.LayoutManager)this).getChildAt(i);
                if (child.getBottom() + ((RecyclerView.LayoutParams)child.getLayoutParams()).bottomMargin > n || child.getTop() + child.getHeight() > n) {
                    this.recycleChildren(recycler, childCount, i);
                    return;
                }
            }
        }
        else {
            View child2;
            for (i = 0; i < childCount; ++i) {
                child2 = ((RecyclerView.LayoutManager)this).getChildAt(i);
                if (super.mOrientationHelper.getDecoratedEnd(child2) > n || super.mOrientationHelper.getTransformedEndWithDecoration(child2) > n) {
                    this.recycleChildren(recycler, 0, i);
                    break;
                }
            }
        }
    }
    
    public void setCanScrollVertically(final boolean canScrollVertically) {
        this.canScrollVertically = canScrollVertically;
    }
    
    public boolean shouldLayoutChildFromOpositeSide(final View view) {
        throw null;
    }
}
