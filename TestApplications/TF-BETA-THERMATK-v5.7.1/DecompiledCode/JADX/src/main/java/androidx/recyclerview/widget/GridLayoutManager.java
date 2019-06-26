package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutManager.LayoutPrefetchRegistry;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.State;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    protected int[] mCachedBorders;
    final Rect mDecorInsets = new Rect();
    boolean mPendingSpanCountChange = false;
    final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
    final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    View[] mSet;
    int mSpanCount = -1;
    SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
    private boolean mUsingSpansToEstimateScrollBarDimensions;

    public static abstract class SpanSizeLookup {
        private boolean mCacheSpanGroupIndices = false;
        private boolean mCacheSpanIndices = false;
        final SparseIntArray mSpanGroupIndexCache = new SparseIntArray();
        final SparseIntArray mSpanIndexCache = new SparseIntArray();

        public abstract int getSpanSize(int i);

        public void setSpanIndexCacheEnabled(boolean z) {
            if (!z) {
                this.mSpanGroupIndexCache.clear();
            }
            this.mCacheSpanIndices = z;
        }

        public void setSpanGroupIndexCacheEnabled(boolean z) {
            if (!z) {
                this.mSpanGroupIndexCache.clear();
            }
            this.mCacheSpanGroupIndices = z;
        }

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public void invalidateSpanGroupIndexCache() {
            this.mSpanGroupIndexCache.clear();
        }

        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }

        public boolean isSpanGroupIndexCacheEnabled() {
            return this.mCacheSpanGroupIndices;
        }

        /* Access modifiers changed, original: 0000 */
        public int getCachedSpanIndex(int i, int i2) {
            if (!this.mCacheSpanIndices) {
                return getSpanIndex(i, i2);
            }
            int i3 = this.mSpanIndexCache.get(i, -1);
            if (i3 != -1) {
                return i3;
            }
            i2 = getSpanIndex(i, i2);
            this.mSpanIndexCache.put(i, i2);
            return i2;
        }

        /* Access modifiers changed, original: 0000 */
        public int getCachedSpanGroupIndex(int i, int i2) {
            if (!this.mCacheSpanGroupIndices) {
                return getSpanGroupIndex(i, i2);
            }
            int i3 = this.mSpanGroupIndexCache.get(i, -1);
            if (i3 != -1) {
                return i3;
            }
            i2 = getSpanGroupIndex(i, i2);
            this.mSpanGroupIndexCache.put(i, i2);
            return i2;
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0037 A:{RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0036 A:{RETURN} */
        public int getSpanIndex(int r6, int r7) {
            /*
            r5 = this;
            r0 = r5.getSpanSize(r6);
            r1 = 0;
            if (r0 != r7) goto L_0x0008;
        L_0x0007:
            return r1;
        L_0x0008:
            r2 = r5.mCacheSpanIndices;
            if (r2 == 0) goto L_0x0020;
        L_0x000c:
            r2 = r5.mSpanIndexCache;
            r2 = findFirstKeyLessThan(r2, r6);
            if (r2 < 0) goto L_0x0020;
        L_0x0014:
            r3 = r5.mSpanIndexCache;
            r3 = r3.get(r2);
            r4 = r5.getSpanSize(r2);
            r3 = r3 + r4;
            goto L_0x0030;
        L_0x0020:
            r2 = 0;
            r3 = 0;
        L_0x0022:
            if (r2 >= r6) goto L_0x0033;
        L_0x0024:
            r4 = r5.getSpanSize(r2);
            r3 = r3 + r4;
            if (r3 != r7) goto L_0x002d;
        L_0x002b:
            r3 = 0;
            goto L_0x0030;
        L_0x002d:
            if (r3 <= r7) goto L_0x0030;
        L_0x002f:
            r3 = r4;
        L_0x0030:
            r2 = r2 + 1;
            goto L_0x0022;
        L_0x0033:
            r0 = r0 + r3;
            if (r0 > r7) goto L_0x0037;
        L_0x0036:
            return r3;
        L_0x0037:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager$SpanSizeLookup.getSpanIndex(int, int):int");
        }

        static int findFirstKeyLessThan(SparseIntArray sparseIntArray, int i) {
            int size = sparseIntArray.size() - 1;
            int i2 = 0;
            while (i2 <= size) {
                int i3 = (i2 + size) >>> 1;
                if (sparseIntArray.keyAt(i3) < i) {
                    i2 = i3 + 1;
                } else {
                    size = i3 - 1;
                }
            }
            i2--;
            return (i2 < 0 || i2 >= sparseIntArray.size()) ? -1 : sparseIntArray.keyAt(i2);
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x002f  */
        /* JADX WARNING: Removed duplicated region for block: B:24:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0045  */
        public int getSpanGroupIndex(int r7, int r8) {
            /*
            r6 = this;
            r0 = r6.mCacheSpanGroupIndices;
            r1 = 0;
            if (r0 == 0) goto L_0x0026;
        L_0x0005:
            r0 = r6.mSpanGroupIndexCache;
            r0 = findFirstKeyLessThan(r0, r7);
            r2 = -1;
            if (r0 == r2) goto L_0x0026;
        L_0x000e:
            r2 = r6.mSpanGroupIndexCache;
            r2 = r2.get(r0);
            r3 = r0 + 1;
            r4 = r6.getCachedSpanIndex(r0, r8);
            r0 = r6.getSpanSize(r0);
            r0 = r0 + r4;
            if (r0 != r8) goto L_0x0029;
        L_0x0021:
            r0 = r2 + 1;
            r2 = r0;
            r0 = 0;
            goto L_0x0029;
        L_0x0026:
            r0 = 0;
            r2 = 0;
            r3 = 0;
        L_0x0029:
            r4 = r6.getSpanSize(r7);
        L_0x002d:
            if (r3 >= r7) goto L_0x0042;
        L_0x002f:
            r5 = r6.getSpanSize(r3);
            r0 = r0 + r5;
            if (r0 != r8) goto L_0x003a;
        L_0x0036:
            r2 = r2 + 1;
            r0 = 0;
            goto L_0x003f;
        L_0x003a:
            if (r0 <= r8) goto L_0x003f;
        L_0x003c:
            r2 = r2 + 1;
            r0 = r5;
        L_0x003f:
            r3 = r3 + 1;
            goto L_0x002d;
        L_0x0042:
            r0 = r0 + r4;
            if (r0 <= r8) goto L_0x0047;
        L_0x0045:
            r2 = r2 + 1;
        L_0x0047:
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager$SpanSizeLookup.getSpanGroupIndex(int, int):int");
        }
    }

    public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
        public int getSpanSize(int i) {
            return 1;
        }

        public int getSpanIndex(int i, int i2) {
            return i % i2;
        }
    }

    public static class LayoutParams extends androidx.recyclerview.widget.RecyclerView.LayoutParams {
        int mSpanIndex = -1;
        public int mSpanSize = 0;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }

    public GridLayoutManager(Context context, int i) {
        super(context);
        setSpanCount(i);
    }

    public GridLayoutManager(Context context, int i, int i2, boolean z) {
        super(context, i2, z);
        setSpanCount(i);
    }

    public void setStackFromEnd(boolean z) {
        if (z) {
            throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
        }
        super.setStackFromEnd(false);
    }

    public int getRowCountForAccessibility(Recycler recycler, State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    public int getColumnCountForAccessibility(Recycler recycler, State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LayoutParams) {
            LayoutParams layoutParams2 = (LayoutParams) layoutParams;
            int spanGroupIndex = getSpanGroupIndex(recycler, state, layoutParams2.getViewLayoutPosition());
            boolean z;
            if (this.mOrientation == 0) {
                int spanIndex = layoutParams2.getSpanIndex();
                int spanSize = layoutParams2.getSpanSize();
                z = this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount;
                accessibilityNodeInfoCompat.setCollectionItemInfo(CollectionItemInfoCompat.obtain(spanIndex, spanSize, spanGroupIndex, 1, z, false));
            } else {
                int spanIndex2 = layoutParams2.getSpanIndex();
                int spanSize2 = layoutParams2.getSpanSize();
                z = this.mSpanCount > 1 && layoutParams2.getSpanSize() == this.mSpanCount;
                accessibilityNodeInfoCompat.setCollectionItemInfo(CollectionItemInfoCompat.obtain(spanGroupIndex, 1, spanIndex2, spanSize2, z, false));
            }
            return;
        }
        super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
    }

    public void onLayoutChildren(Recycler recycler, State state) {
        if (state.isPreLayout()) {
            cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        clearPreLayoutSpanMappingCache();
    }

    public void onLayoutCompleted(State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }

    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    private void cachePreLayoutSpanMapping() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
            int viewLayoutPosition = layoutParams.getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.getSpanIndex());
        }
    }

    public void onItemsAdded(RecyclerView recyclerView, int i, int i2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsChanged(RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsRemoved(RecyclerView recyclerView, int i, int i2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsUpdated(RecyclerView recyclerView, int i, int i2, Object obj) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsMoved(RecyclerView recyclerView, int i, int i2, int i3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public androidx.recyclerview.widget.RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }

    public androidx.recyclerview.widget.RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    public androidx.recyclerview.widget.RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public boolean checkLayoutParams(androidx.recyclerview.widget.RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }

    private void updateMeasurements() {
        int width;
        int paddingLeft;
        if (getOrientation() == 1) {
            width = getWidth() - getPaddingRight();
            paddingLeft = getPaddingLeft();
        } else {
            width = getHeight() - getPaddingBottom();
            paddingLeft = getPaddingTop();
        }
        calculateItemBorders(width - paddingLeft);
    }

    public void setMeasuredDimension(Rect rect, int i, int i2) {
        int chooseSize;
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(rect, i, i2);
        }
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        if (this.mOrientation == 1) {
            chooseSize = LayoutManager.chooseSize(i2, rect.height() + paddingTop, getMinimumHeight());
            int[] iArr = this.mCachedBorders;
            i = LayoutManager.chooseSize(i, iArr[iArr.length - 1] + paddingLeft, getMinimumWidth());
        } else {
            i = LayoutManager.chooseSize(i, rect.width() + paddingLeft, getMinimumWidth());
            int[] iArr2 = this.mCachedBorders;
            chooseSize = LayoutManager.chooseSize(i2, iArr2[iArr2.length - 1] + paddingTop, getMinimumHeight());
        }
        setMeasuredDimension(i, chooseSize);
    }

    private void calculateItemBorders(int i) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, i);
    }

    /* Access modifiers changed, original: protected */
    public int[] calculateItemBorders(int[] iArr, int i, int i2) {
        int i3 = 1;
        if (!(iArr != null && iArr.length == i + 1 && iArr[iArr.length - 1] == i2)) {
            iArr = new int[(i + 1)];
        }
        int i4 = 0;
        iArr[0] = 0;
        int i5 = i2 / i;
        i2 %= i;
        int i6 = 0;
        while (i3 <= i) {
            int i7;
            i4 += i2;
            if (i4 <= 0 || i - i4 >= i2) {
                i7 = i5;
            } else {
                i7 = i5 + 1;
                i4 -= i;
            }
            i6 += i7;
            iArr[i3] = i6;
            i3++;
        }
        return iArr;
    }

    /* Access modifiers changed, original: 0000 */
    public int getSpaceForSpanRange(int i, int i2) {
        int[] iArr;
        if (this.mOrientation == 1 && isLayoutRTL()) {
            iArr = this.mCachedBorders;
            int i3 = this.mSpanCount;
            return iArr[i3 - i] - iArr[(i3 - i) - i2];
        }
        iArr = this.mCachedBorders;
        return iArr[i2 + i] - iArr[i];
    }

    /* Access modifiers changed, original: 0000 */
    public void onAnchorReady(Recycler recycler, State state, AnchorInfo anchorInfo, int i) {
        super.onAnchorReady(recycler, state, anchorInfo, i);
        updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, i);
        }
        ensureViewSet();
    }

    private void ensureViewSet() {
        View[] viewArr = this.mSet;
        if (viewArr == null || viewArr.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }

    public int scrollHorizontallyBy(int i, Recycler recycler, State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollHorizontallyBy(i, recycler, state);
    }

    public int scrollVerticallyBy(int i, Recycler recycler, State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollVerticallyBy(i, recycler, state);
    }

    private void ensureAnchorIsInCorrectSpan(Recycler recycler, State state, AnchorInfo anchorInfo, int i) {
        Object obj = i == 1 ? 1 : null;
        int spanIndex = getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (obj != null) {
            while (spanIndex > 0) {
                i = anchorInfo.mPosition;
                if (i > 0) {
                    anchorInfo.mPosition = i - 1;
                    spanIndex = getSpanIndex(recycler, state, anchorInfo.mPosition);
                } else {
                    return;
                }
            }
            return;
        }
        i = state.getItemCount() - 1;
        int i2 = anchorInfo.mPosition;
        while (i2 < i) {
            int i3 = i2 + 1;
            int spanIndex2 = getSpanIndex(recycler, state, i3);
            if (spanIndex2 <= spanIndex) {
                break;
            }
            i2 = i3;
            spanIndex = spanIndex2;
        }
        anchorInfo.mPosition = i2;
    }

    /* Access modifiers changed, original: 0000 */
    public View findReferenceChild(Recycler recycler, State state, int i, int i2, int i3) {
        ensureLayoutState();
        int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        int i4 = i2 > i ? 1 : -1;
        View view = null;
        View view2 = null;
        while (i != i2) {
            View childAt = getChildAt(i);
            int position = getPosition(childAt);
            if (position >= 0 && position < i3 && getSpanIndex(recycler, state, position) == 0) {
                if (((androidx.recyclerview.widget.RecyclerView.LayoutParams) childAt.getLayoutParams()).isItemRemoved()) {
                    if (view2 == null) {
                        view2 = childAt;
                    }
                } else if (this.mOrientationHelper.getDecoratedStart(childAt) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(childAt) >= startAfterPadding) {
                    return childAt;
                } else {
                    if (view == null) {
                        view = childAt;
                    }
                }
            }
            i += i4;
        }
        if (view == null) {
            view = view2;
        }
        return view;
    }

    private int getSpanGroupIndex(Recycler recycler, State state, int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(i, this.mSpanCount);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot find span size for pre layout position. ");
        stringBuilder.append(i);
        Log.w(TAG, stringBuilder.toString());
        return 0;
    }

    private int getSpanIndex(Recycler recycler, State state, int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
        }
        int i2 = this.mPreLayoutSpanIndexCache.get(i, -1);
        if (i2 != -1) {
            return i2;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
        stringBuilder.append(i);
        Log.w(TAG, stringBuilder.toString());
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int getSpanSize(Recycler recycler, State state, int i) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(i);
        }
        int i2 = this.mPreLayoutSpanSizeCache.get(i, -1);
        if (i2 != -1) {
            return i2;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(i);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
        stringBuilder.append(i);
        Log.w(TAG, stringBuilder.toString());
        return 1;
    }

    /* Access modifiers changed, original: 0000 */
    public void collectPrefetchPositionsForLayoutState(State state, LayoutState layoutState, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i = this.mSpanCount;
        for (int i2 = 0; i2 < this.mSpanCount && layoutState.hasMore(state) && i > 0; i2++) {
            int i3 = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(i3, Math.max(0, layoutState.mScrollingOffset));
            i -= this.mSpanSizeLookup.getSpanSize(i3);
            layoutState.mCurrentPosition += layoutState.mItemDirection;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void layoutChunk(Recycler recycler, State state, LayoutState layoutState, LayoutChunkResult layoutChunkResult) {
        int i;
        int spanSize;
        Recycler recycler2 = recycler;
        State state2 = state;
        LayoutState layoutState2 = layoutState;
        LayoutChunkResult layoutChunkResult2 = layoutChunkResult;
        int modeInOther = this.mOrientationHelper.getModeInOther();
        int i2 = 0;
        Object obj = modeInOther != 1073741824 ? 1 : null;
        int i3 = getChildCount() > 0 ? this.mCachedBorders[this.mSpanCount] : 0;
        if (obj != null) {
            updateMeasurements();
        }
        boolean z = layoutState2.mItemDirection == 1;
        int i4 = this.mSpanCount;
        if (!z) {
            i4 = getSpanIndex(recycler2, state2, layoutState2.mCurrentPosition) + getSpanSize(recycler2, state2, layoutState2.mCurrentPosition);
        }
        int i5 = i4;
        i4 = 0;
        while (i4 < this.mSpanCount && layoutState2.hasMore(state2) && i5 > 0) {
            i = layoutState2.mCurrentPosition;
            spanSize = getSpanSize(recycler2, state2, i);
            if (spanSize <= this.mSpanCount) {
                i5 -= spanSize;
                if (i5 < 0) {
                    break;
                }
                View next = layoutState2.next(recycler2);
                if (next == null) {
                    break;
                }
                this.mSet[i4] = next;
                i4++;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Item at position ");
                stringBuilder.append(i);
                stringBuilder.append(" requires ");
                stringBuilder.append(spanSize);
                stringBuilder.append(" spans but GridLayoutManager has only ");
                stringBuilder.append(this.mSpanCount);
                stringBuilder.append(" spans.");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        if (i4 == 0) {
            layoutChunkResult2.mFinished = true;
            return;
        }
        View view;
        int i6;
        float f = 0.0f;
        assignSpans(recycler2, state2, i4, z);
        int i7 = 0;
        int i8 = 0;
        while (i7 < i4) {
            View view2 = this.mSet[i7];
            if (layoutState2.mScrapList == null) {
                if (z) {
                    addView(view2);
                } else {
                    addView(view2, i2);
                }
            } else if (z) {
                addDisappearingView(view2);
            } else {
                addDisappearingView(view2, i2);
            }
            calculateItemDecorationsForChild(view2, this.mDecorInsets);
            measureChild(view2, modeInOther, i2);
            i = this.mOrientationHelper.getDecoratedMeasurement(view2);
            if (i > i8) {
                i8 = i;
            }
            float decoratedMeasurementInOther = (((float) this.mOrientationHelper.getDecoratedMeasurementInOther(view2)) * 1.0f) / ((float) ((LayoutParams) view2.getLayoutParams()).mSpanSize);
            if (decoratedMeasurementInOther > f) {
                f = decoratedMeasurementInOther;
            }
            i7++;
            i2 = 0;
        }
        if (obj != null) {
            guessMeasurement(f, i3);
            i8 = 0;
            for (i7 = 0; i7 < i4; i7++) {
                view = this.mSet[i7];
                measureChild(view, 1073741824, true);
                modeInOther = this.mOrientationHelper.getDecoratedMeasurement(view);
                if (modeInOther > i8) {
                    i8 = modeInOther;
                }
            }
        }
        for (i7 = 0; i7 < i4; i7++) {
            view = this.mSet[i7];
            if (this.mOrientationHelper.getDecoratedMeasurement(view) != i8) {
                int childMeasureSpec;
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                Rect rect = layoutParams.mDecorInsets;
                int i9 = ((rect.top + rect.bottom) + layoutParams.topMargin) + layoutParams.bottomMargin;
                i3 = ((rect.left + rect.right) + layoutParams.leftMargin) + layoutParams.rightMargin;
                i2 = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
                if (this.mOrientation == 1) {
                    childMeasureSpec = LayoutManager.getChildMeasureSpec(i2, 1073741824, i3, layoutParams.width, false);
                    i2 = MeasureSpec.makeMeasureSpec(i8 - i9, 1073741824);
                } else {
                    i3 = MeasureSpec.makeMeasureSpec(i8 - i3, 1073741824);
                    i2 = LayoutManager.getChildMeasureSpec(i2, 1073741824, i9, layoutParams.height, false);
                    childMeasureSpec = i3;
                }
                measureChildWithDecorationsAndMargin(view, childMeasureSpec, i2, true);
            }
        }
        i5 = 0;
        layoutChunkResult2.mConsumed = i8;
        if (this.mOrientation == 1) {
            if (layoutState2.mLayoutDirection == -1) {
                i2 = layoutState2.mOffset;
                i8 = i2 - i8;
                i6 = i2;
            } else {
                i2 = layoutState2.mOffset;
                i6 = i2 + i8;
                i8 = i2;
            }
            i7 = 0;
            i2 = 0;
        } else if (layoutState2.mLayoutDirection == -1) {
            i2 = layoutState2.mOffset;
            i7 = i2 - i8;
            i8 = 0;
            i6 = 0;
        } else {
            i2 = layoutState2.mOffset;
            i7 = i2 + i8;
            i8 = 0;
            i6 = 0;
            int i10 = i2;
            i2 = i7;
            i7 = i10;
        }
        while (i5 < i4) {
            int i11;
            int decoratedMeasurementInOther2;
            View view3 = this.mSet[i5];
            LayoutParams layoutParams2 = (LayoutParams) view3.getLayoutParams();
            if (this.mOrientation != 1) {
                i8 = getPaddingTop() + this.mCachedBorders[layoutParams2.mSpanIndex];
                i11 = i7;
                i = i8;
                decoratedMeasurementInOther2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view3) + i8;
                spanSize = i2;
            } else if (isLayoutRTL()) {
                i7 = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams2.mSpanIndex];
                spanSize = i7;
                i = i8;
                decoratedMeasurementInOther2 = i6;
                i11 = i7 - this.mOrientationHelper.getDecoratedMeasurementInOther(view3);
            } else {
                i7 = getPaddingLeft() + this.mCachedBorders[layoutParams2.mSpanIndex];
                i11 = i7;
                i = i8;
                decoratedMeasurementInOther2 = i6;
                spanSize = this.mOrientationHelper.getDecoratedMeasurementInOther(view3) + i7;
            }
            layoutDecoratedWithMargins(view3, i11, i, spanSize, decoratedMeasurementInOther2);
            if (layoutParams2.isItemRemoved() || layoutParams2.isItemChanged()) {
                layoutChunkResult2.mIgnoreConsumed = true;
            }
            layoutChunkResult2.mFocusable |= view3.hasFocusable();
            i5++;
            i7 = i11;
            i8 = i;
            i2 = spanSize;
            i6 = decoratedMeasurementInOther2;
        }
        Arrays.fill(this.mSet, null);
    }

    /* Access modifiers changed, original: protected */
    public void measureChild(View view, int i, boolean z) {
        int childMeasureSpec;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i2 = ((rect.top + rect.bottom) + layoutParams.topMargin) + layoutParams.bottomMargin;
        int i3 = ((rect.left + rect.right) + layoutParams.leftMargin) + layoutParams.rightMargin;
        int spaceForSpanRange = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
            i = LayoutManager.getChildMeasureSpec(spaceForSpanRange, i, i3, layoutParams.width, false);
            childMeasureSpec = LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i2, layoutParams.height, true);
        } else {
            i = LayoutManager.getChildMeasureSpec(spaceForSpanRange, i, i2, layoutParams.height, false);
            int childMeasureSpec2 = LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), i3, layoutParams.width, true);
            childMeasureSpec = i;
            i = childMeasureSpec2;
        }
        measureChildWithDecorationsAndMargin(view, i, childMeasureSpec, z);
    }

    private void guessMeasurement(float f, int i) {
        calculateItemBorders(Math.max(Math.round(f * ((float) this.mSpanCount)), i));
    }

    /* Access modifiers changed, original: protected */
    public void measureChildWithDecorationsAndMargin(View view, int i, int i2, boolean z) {
        androidx.recyclerview.widget.RecyclerView.LayoutParams layoutParams = (androidx.recyclerview.widget.RecyclerView.LayoutParams) view.getLayoutParams();
        if (z) {
            z = shouldReMeasureChild(view, i, i2, layoutParams);
        } else {
            z = shouldMeasureChild(view, i, i2, layoutParams);
        }
        if (z) {
            view.measure(i, i2);
        }
    }

    /* Access modifiers changed, original: protected */
    public void assignSpans(Recycler recycler, State state, int i, boolean z) {
        int i2;
        int i3 = 0;
        int i4 = -1;
        if (z) {
            i4 = i;
            i = 0;
            i2 = 1;
        } else {
            i--;
            i2 = -1;
        }
        while (i != i4) {
            View view = this.mSet[i];
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.mSpanSize = getSpanSize(recycler, state, getPosition(view));
            layoutParams.mSpanIndex = i3;
            i3 += layoutParams.mSpanSize;
            i += i2;
        }
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public void setSpanCount(int i) {
        if (i != this.mSpanCount) {
            this.mPendingSpanCountChange = true;
            if (i >= 1) {
                this.mSpanCount = i;
                this.mSpanSizeLookup.invalidateSpanIndexCache();
                requestLayout();
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Span count should be at least 1. Provided ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:72:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0105  */
    /* JADX WARNING: Missing block: B:55:0x00d7, code skipped:
            if (r13 == (r2 > r8)) goto L_0x00cd;
     */
    /* JADX WARNING: Missing block: B:66:0x00f7, code skipped:
            if (r13 == r11) goto L_0x00b7;
     */
    public android.view.View onFocusSearchFailed(android.view.View r24, int r25, androidx.recyclerview.widget.RecyclerView.Recycler r26, androidx.recyclerview.widget.RecyclerView.State r27) {
        /*
        r23 = this;
        r0 = r23;
        r1 = r26;
        r2 = r27;
        r3 = r23.findContainingItemView(r24);
        r4 = 0;
        if (r3 != 0) goto L_0x000e;
    L_0x000d:
        return r4;
    L_0x000e:
        r5 = r3.getLayoutParams();
        r5 = (androidx.recyclerview.widget.GridLayoutManager.LayoutParams) r5;
        r6 = r5.mSpanIndex;
        r5 = r5.mSpanSize;
        r5 = r5 + r6;
        r7 = super.onFocusSearchFailed(r24, r25, r26, r27);
        if (r7 != 0) goto L_0x0020;
    L_0x001f:
        return r4;
    L_0x0020:
        r7 = r25;
        r7 = r0.convertFocusDirectionToLayoutDirection(r7);
        r9 = 1;
        if (r7 != r9) goto L_0x002b;
    L_0x0029:
        r7 = 1;
        goto L_0x002c;
    L_0x002b:
        r7 = 0;
    L_0x002c:
        r10 = r0.mShouldReverseLayout;
        if (r7 == r10) goto L_0x0032;
    L_0x0030:
        r7 = 1;
        goto L_0x0033;
    L_0x0032:
        r7 = 0;
    L_0x0033:
        r10 = -1;
        if (r7 == 0) goto L_0x003e;
    L_0x0036:
        r7 = r23.getChildCount();
        r7 = r7 - r9;
        r11 = -1;
        r12 = -1;
        goto L_0x0045;
    L_0x003e:
        r7 = r23.getChildCount();
        r11 = r7;
        r7 = 0;
        r12 = 1;
    L_0x0045:
        r13 = r0.mOrientation;
        if (r13 != r9) goto L_0x0051;
    L_0x0049:
        r13 = r23.isLayoutRTL();
        if (r13 == 0) goto L_0x0051;
    L_0x004f:
        r13 = 1;
        goto L_0x0052;
    L_0x0051:
        r13 = 0;
    L_0x0052:
        r14 = r0.getSpanGroupIndex(r1, r2, r7);
        r10 = r4;
        r8 = -1;
        r15 = 0;
        r16 = 0;
        r17 = -1;
    L_0x005d:
        if (r7 == r11) goto L_0x0147;
    L_0x005f:
        r9 = r0.getSpanGroupIndex(r1, r2, r7);
        r1 = r0.getChildAt(r7);
        if (r1 != r3) goto L_0x006b;
    L_0x0069:
        goto L_0x0147;
    L_0x006b:
        r18 = r1.hasFocusable();
        if (r18 == 0) goto L_0x0085;
    L_0x0071:
        if (r9 == r14) goto L_0x0085;
    L_0x0073:
        if (r4 == 0) goto L_0x0077;
    L_0x0075:
        goto L_0x0147;
    L_0x0077:
        r18 = r3;
        r19 = r8;
        r21 = r10;
        r20 = r11;
        r8 = r16;
        r10 = r17;
        goto L_0x0133;
    L_0x0085:
        r9 = r1.getLayoutParams();
        r9 = (androidx.recyclerview.widget.GridLayoutManager.LayoutParams) r9;
        r2 = r9.mSpanIndex;
        r18 = r3;
        r3 = r9.mSpanSize;
        r3 = r3 + r2;
        r19 = r1.hasFocusable();
        if (r19 == 0) goto L_0x009d;
    L_0x0098:
        if (r2 != r6) goto L_0x009d;
    L_0x009a:
        if (r3 != r5) goto L_0x009d;
    L_0x009c:
        return r1;
    L_0x009d:
        r19 = r1.hasFocusable();
        if (r19 == 0) goto L_0x00a5;
    L_0x00a3:
        if (r4 == 0) goto L_0x00ad;
    L_0x00a5:
        r19 = r1.hasFocusable();
        if (r19 != 0) goto L_0x00b9;
    L_0x00ab:
        if (r10 != 0) goto L_0x00b9;
    L_0x00ad:
        r19 = r8;
        r21 = r10;
    L_0x00b1:
        r20 = r11;
        r8 = r16;
        r10 = r17;
    L_0x00b7:
        r11 = 1;
        goto L_0x0103;
    L_0x00b9:
        r19 = java.lang.Math.max(r2, r6);
        r20 = java.lang.Math.min(r3, r5);
        r21 = r10;
        r10 = r20 - r19;
        r19 = r1.hasFocusable();
        if (r19 == 0) goto L_0x00da;
    L_0x00cb:
        if (r10 <= r15) goto L_0x00d0;
    L_0x00cd:
        r19 = r8;
        goto L_0x00b1;
    L_0x00d0:
        if (r10 != r15) goto L_0x00fa;
    L_0x00d2:
        if (r2 <= r8) goto L_0x00d6;
    L_0x00d4:
        r10 = 1;
        goto L_0x00d7;
    L_0x00d6:
        r10 = 0;
    L_0x00d7:
        if (r13 != r10) goto L_0x00fa;
    L_0x00d9:
        goto L_0x00cd;
    L_0x00da:
        if (r4 != 0) goto L_0x00fa;
    L_0x00dc:
        r19 = r8;
        r20 = r11;
        r8 = 0;
        r11 = 1;
        r22 = r0.isViewPartiallyVisible(r1, r8, r11);
        if (r22 == 0) goto L_0x00fe;
    L_0x00e8:
        r8 = r16;
        if (r10 <= r8) goto L_0x00ef;
    L_0x00ec:
        r10 = r17;
        goto L_0x0103;
    L_0x00ef:
        if (r10 != r8) goto L_0x0100;
    L_0x00f1:
        r10 = r17;
        if (r2 <= r10) goto L_0x00f6;
    L_0x00f5:
        goto L_0x00f7;
    L_0x00f6:
        r11 = 0;
    L_0x00f7:
        if (r13 != r11) goto L_0x0102;
    L_0x00f9:
        goto L_0x00b7;
    L_0x00fa:
        r19 = r8;
        r20 = r11;
    L_0x00fe:
        r8 = r16;
    L_0x0100:
        r10 = r17;
    L_0x0102:
        r11 = 0;
    L_0x0103:
        if (r11 == 0) goto L_0x0133;
    L_0x0105:
        r11 = r1.hasFocusable();
        if (r11 == 0) goto L_0x0120;
    L_0x010b:
        r4 = r9.mSpanIndex;
        r3 = java.lang.Math.min(r3, r5);
        r2 = java.lang.Math.max(r2, r6);
        r3 = r3 - r2;
        r15 = r3;
        r16 = r8;
        r17 = r10;
        r10 = r21;
        r8 = r4;
        r4 = r1;
        goto L_0x013b;
    L_0x0120:
        r8 = r9.mSpanIndex;
        r3 = java.lang.Math.min(r3, r5);
        r2 = java.lang.Math.max(r2, r6);
        r3 = r3 - r2;
        r10 = r1;
        r16 = r3;
        r17 = r8;
        r8 = r19;
        goto L_0x013b;
    L_0x0133:
        r16 = r8;
        r17 = r10;
        r8 = r19;
        r10 = r21;
    L_0x013b:
        r7 = r7 + r12;
        r1 = r26;
        r2 = r27;
        r3 = r18;
        r11 = r20;
        r9 = 1;
        goto L_0x005d;
    L_0x0147:
        r21 = r10;
        if (r4 == 0) goto L_0x014c;
    L_0x014b:
        goto L_0x014e;
    L_0x014c:
        r4 = r21;
    L_0x014e:
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManager.onFocusSearchFailed(android.view.View, int, androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State):android.view.View");
    }

    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
    }

    public int computeHorizontalScrollRange(State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollRangeWithSpanInfo(state);
        }
        return super.computeHorizontalScrollRange(state);
    }

    public int computeVerticalScrollRange(State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollRangeWithSpanInfo(state);
        }
        return super.computeVerticalScrollRange(state);
    }

    public int computeHorizontalScrollOffset(State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollOffsetWithSpanInfo(state);
        }
        return super.computeHorizontalScrollOffset(state);
    }

    public int computeVerticalScrollOffset(State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollOffsetWithSpanInfo(state);
        }
        return super.computeVerticalScrollOffset(state);
    }

    public void setUsingSpansToEstimateScrollbarDimensions(boolean z) {
        this.mUsingSpansToEstimateScrollBarDimensions = z;
    }

    public boolean isUsingSpansToEstimateScrollbarDimensions() {
        return this.mUsingSpansToEstimateScrollBarDimensions;
    }

    private int computeScrollRangeWithSpanInfo(State state) {
        if (!(getChildCount() == 0 || state.getItemCount() == 0)) {
            ensureLayoutState();
            View findFirstVisibleChildClosestToStart = findFirstVisibleChildClosestToStart(!isSmoothScrollbarEnabled(), true);
            View findFirstVisibleChildClosestToEnd = findFirstVisibleChildClosestToEnd(!isSmoothScrollbarEnabled(), true);
            if (!(findFirstVisibleChildClosestToStart == null || findFirstVisibleChildClosestToEnd == null)) {
                if (!isSmoothScrollbarEnabled()) {
                    return this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1;
                }
                return (int) ((((float) (this.mOrientationHelper.getDecoratedEnd(findFirstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart))) / ((float) ((this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount) - this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount)) + 1))) * ((float) (this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1)));
            }
        }
        return 0;
    }

    private int computeScrollOffsetWithSpanInfo(State state) {
        if (!(getChildCount() == 0 || state.getItemCount() == 0)) {
            ensureLayoutState();
            boolean isSmoothScrollbarEnabled = isSmoothScrollbarEnabled();
            View findFirstVisibleChildClosestToStart = findFirstVisibleChildClosestToStart(!isSmoothScrollbarEnabled, true);
            View findFirstVisibleChildClosestToEnd = findFirstVisibleChildClosestToEnd(!isSmoothScrollbarEnabled, true);
            if (!(findFirstVisibleChildClosestToStart == null || findFirstVisibleChildClosestToEnd == null)) {
                int cachedSpanGroupIndex = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount);
                int cachedSpanGroupIndex2 = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount);
                int min = Math.min(cachedSpanGroupIndex, cachedSpanGroupIndex2);
                cachedSpanGroupIndex = Math.max(cachedSpanGroupIndex, cachedSpanGroupIndex2);
                int cachedSpanGroupIndex3 = this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1;
                if (this.mShouldReverseLayout) {
                    cachedSpanGroupIndex3 = Math.max(0, (cachedSpanGroupIndex3 - cachedSpanGroupIndex) - 1);
                } else {
                    cachedSpanGroupIndex3 = Math.max(0, min);
                }
                if (!isSmoothScrollbarEnabled) {
                    return cachedSpanGroupIndex3;
                }
                return Math.round((((float) cachedSpanGroupIndex3) * (((float) Math.abs(this.mOrientationHelper.getDecoratedEnd(findFirstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart))) / ((float) ((this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount) - this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount)) + 1)))) + ((float) (this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart))));
            }
        }
        return 0;
    }
}
