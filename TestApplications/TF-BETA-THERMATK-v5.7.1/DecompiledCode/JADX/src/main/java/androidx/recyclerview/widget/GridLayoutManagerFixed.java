package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import java.util.ArrayList;

public class GridLayoutManagerFixed extends GridLayoutManager {
    private ArrayList<View> additionalViews = new ArrayList(4);
    private boolean canScrollVertically = true;

    /* Access modifiers changed, original: protected */
    public boolean hasSiblingChild(int i) {
        throw null;
    }

    public boolean shouldLayoutChildFromOpositeSide(View view) {
        throw null;
    }

    public GridLayoutManagerFixed(Context context, int i) {
        super(context, i);
    }

    public GridLayoutManagerFixed(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
    }

    public void setCanScrollVertically(boolean z) {
        this.canScrollVertically = z;
    }

    public boolean canScrollVertically() {
        return this.canScrollVertically;
    }

    /* Access modifiers changed, original: protected */
    public void recycleViewsFromStart(Recycler recycler, int i, int i2) {
        if (i >= 0) {
            i2 = getChildCount();
            if (this.mShouldReverseLayout) {
                i2--;
                for (int i3 = i2; i3 >= 0; i3--) {
                    View childAt = getChildAt(i3);
                    if (childAt.getBottom() + ((LayoutParams) childAt.getLayoutParams()).bottomMargin > i || childAt.getTop() + childAt.getHeight() > i) {
                        recycleChildren(recycler, i2, i3);
                        return;
                    }
                }
            } else {
                for (int i4 = 0; i4 < i2; i4++) {
                    View childAt2 = getChildAt(i4);
                    if (this.mOrientationHelper.getDecoratedEnd(childAt2) > i || this.mOrientationHelper.getTransformedEndWithDecoration(childAt2) > i) {
                        recycleChildren(recycler, 0, i4);
                        break;
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public int[] calculateItemBorders(int[] iArr, int i, int i2) {
        int i3 = 1;
        if (!(iArr != null && iArr.length == i + 1 && iArr[iArr.length - 1] == i2)) {
            iArr = new int[(i + 1)];
        }
        iArr[0] = 0;
        while (i3 <= i) {
            iArr[i3] = (int) Math.ceil((double) ((((float) i3) / ((float) i)) * ((float) i2)));
            i3++;
        }
        return iArr;
    }

    /* Access modifiers changed, original: protected */
    public void measureChild(View view, int i, boolean z) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i2 = ((rect.left + rect.right) + layoutParams.leftMargin) + layoutParams.rightMargin;
        measureChildWithDecorationsAndMargin(view, LayoutManager.getChildMeasureSpec(this.mCachedBorders[layoutParams.mSpanSize], i, i2, layoutParams.width, false), LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), ((rect.top + rect.bottom) + layoutParams.topMargin) + layoutParams.bottomMargin, layoutParams.height, true), z);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:72:0x0187, code skipped:
            if (r9.mLayoutDirection != -1) goto L_0x018b;
     */
    public void layoutChunk(androidx.recyclerview.widget.RecyclerView.Recycler r25, androidx.recyclerview.widget.RecyclerView.State r26, androidx.recyclerview.widget.LinearLayoutManager.LayoutState r27, androidx.recyclerview.widget.LinearLayoutManager.LayoutChunkResult r28) {
        /*
        r24 = this;
        r6 = r24;
        r7 = r25;
        r8 = r26;
        r9 = r27;
        r10 = r28;
        r0 = r6.mOrientationHelper;
        r11 = r0.getModeInOther();
        r0 = r9.mItemDirection;
        r12 = 0;
        r13 = 1;
        if (r0 != r13) goto L_0x0018;
    L_0x0016:
        r14 = 1;
        goto L_0x0019;
    L_0x0018:
        r14 = 0;
    L_0x0019:
        r10.mConsumed = r12;
        r0 = r9.mCurrentPosition;
        r1 = r9.mLayoutDirection;
        r15 = -1;
        if (r1 == r15) goto L_0x0074;
    L_0x0022:
        r1 = r6.hasSiblingChild(r0);
        if (r1 == 0) goto L_0x0074;
    L_0x0028:
        r1 = r9.mCurrentPosition;
        r1 = r1 + r13;
        r1 = r6.findViewByPosition(r1);
        if (r1 != 0) goto L_0x0074;
    L_0x0031:
        r1 = r9.mCurrentPosition;
        r1 = r1 + r13;
        r1 = r6.hasSiblingChild(r1);
        if (r1 == 0) goto L_0x0041;
    L_0x003a:
        r1 = r9.mCurrentPosition;
        r1 = r1 + 3;
        r9.mCurrentPosition = r1;
        goto L_0x0047;
    L_0x0041:
        r1 = r9.mCurrentPosition;
        r1 = r1 + 2;
        r9.mCurrentPosition = r1;
    L_0x0047:
        r1 = r9.mCurrentPosition;
        r2 = r1;
    L_0x004a:
        if (r2 <= r0) goto L_0x0072;
    L_0x004c:
        r3 = r9.next(r7);
        r4 = r6.additionalViews;
        r4.add(r3);
        if (r2 == r1) goto L_0x006f;
    L_0x0057:
        r4 = r6.mDecorInsets;
        r6.calculateItemDecorationsForChild(r3, r4);
        r6.measureChild(r3, r11, r12);
        r4 = r6.mOrientationHelper;
        r3 = r4.getDecoratedMeasurement(r3);
        r4 = r9.mOffset;
        r4 = r4 - r3;
        r9.mOffset = r4;
        r4 = r9.mAvailable;
        r4 = r4 + r3;
        r9.mAvailable = r4;
    L_0x006f:
        r2 = r2 + -1;
        goto L_0x004a;
    L_0x0072:
        r9.mCurrentPosition = r1;
    L_0x0074:
        r0 = 1;
    L_0x0075:
        if (r0 == 0) goto L_0x029b;
    L_0x0077:
        r0 = r6.mSpanCount;
        r1 = r6.additionalViews;
        r1 = r1.isEmpty();
        r1 = r1 ^ r13;
        r2 = r9.mCurrentPosition;
        r16 = r1;
        r5 = 0;
    L_0x0085:
        r1 = r6.mSpanCount;
        if (r5 >= r1) goto L_0x00d2;
    L_0x0089:
        r1 = r9.hasMore(r8);
        if (r1 == 0) goto L_0x00d2;
    L_0x008f:
        if (r0 <= 0) goto L_0x00d2;
    L_0x0091:
        r1 = r9.mCurrentPosition;
        r2 = r6.getSpanSize(r7, r8, r1);
        r0 = r0 - r2;
        if (r0 >= 0) goto L_0x009b;
    L_0x009a:
        goto L_0x00d2;
    L_0x009b:
        r2 = r6.additionalViews;
        r2 = r2.isEmpty();
        if (r2 != 0) goto L_0x00b6;
    L_0x00a3:
        r2 = r6.additionalViews;
        r2 = r2.get(r12);
        r2 = (android.view.View) r2;
        r3 = r6.additionalViews;
        r3.remove(r12);
        r3 = r9.mCurrentPosition;
        r3 = r3 - r13;
        r9.mCurrentPosition = r3;
        goto L_0x00ba;
    L_0x00b6:
        r2 = r9.next(r7);
    L_0x00ba:
        if (r2 != 0) goto L_0x00bd;
    L_0x00bc:
        goto L_0x00d2;
    L_0x00bd:
        r3 = r6.mSet;
        r3[r5] = r2;
        r5 = r5 + 1;
        r2 = r9.mLayoutDirection;
        if (r2 != r15) goto L_0x0085;
    L_0x00c7:
        if (r0 > 0) goto L_0x0085;
    L_0x00c9:
        r1 = r6.hasSiblingChild(r1);
        if (r1 == 0) goto L_0x0085;
    L_0x00cf:
        r16 = 1;
        goto L_0x0085;
    L_0x00d2:
        if (r5 != 0) goto L_0x00d7;
    L_0x00d4:
        r10.mFinished = r13;
        return;
    L_0x00d7:
        r0 = 0;
        r6.assignSpans(r7, r8, r5, r14);
        r0 = 0;
        r1 = 0;
        r4 = 0;
    L_0x00de:
        if (r0 >= r5) goto L_0x012a;
    L_0x00e0:
        r2 = r6.mSet;
        r2 = r2[r0];
        r3 = r9.mScrapList;
        if (r3 != 0) goto L_0x00f2;
    L_0x00e8:
        if (r14 == 0) goto L_0x00ee;
    L_0x00ea:
        r6.addView(r2);
        goto L_0x00fb;
    L_0x00ee:
        r6.addView(r2, r12);
        goto L_0x00fb;
    L_0x00f2:
        if (r14 == 0) goto L_0x00f8;
    L_0x00f4:
        r6.addDisappearingView(r2);
        goto L_0x00fb;
    L_0x00f8:
        r6.addDisappearingView(r2, r12);
    L_0x00fb:
        r3 = r6.mDecorInsets;
        r6.calculateItemDecorationsForChild(r2, r3);
        r6.measureChild(r2, r11, r12);
        r3 = r6.mOrientationHelper;
        r3 = r3.getDecoratedMeasurement(r2);
        if (r3 <= r4) goto L_0x010c;
    L_0x010b:
        r4 = r3;
    L_0x010c:
        r3 = r2.getLayoutParams();
        r3 = (androidx.recyclerview.widget.GridLayoutManager.LayoutParams) r3;
        r17 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r15 = r6.mOrientationHelper;
        r2 = r15.getDecoratedMeasurementInOther(r2);
        r2 = (float) r2;
        r2 = r2 * r17;
        r3 = r3.mSpanSize;
        r3 = (float) r3;
        r2 = r2 / r3;
        r3 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1));
        if (r3 <= 0) goto L_0x0126;
    L_0x0125:
        r1 = r2;
    L_0x0126:
        r0 = r0 + 1;
        r15 = -1;
        goto L_0x00de;
    L_0x012a:
        r0 = 0;
    L_0x012b:
        if (r0 >= r5) goto L_0x0179;
    L_0x012d:
        r1 = r6.mSet;
        r1 = r1[r0];
        r2 = r6.mOrientationHelper;
        r2 = r2.getDecoratedMeasurement(r1);
        if (r2 == r4) goto L_0x0171;
    L_0x0139:
        r2 = r1.getLayoutParams();
        r2 = (androidx.recyclerview.widget.GridLayoutManager.LayoutParams) r2;
        r3 = r2.mDecorInsets;
        r15 = r3.top;
        r13 = r3.bottom;
        r15 = r15 + r13;
        r13 = r2.topMargin;
        r15 = r15 + r13;
        r13 = r2.bottomMargin;
        r15 = r15 + r13;
        r13 = r3.left;
        r3 = r3.right;
        r13 = r13 + r3;
        r3 = r2.leftMargin;
        r13 = r13 + r3;
        r3 = r2.rightMargin;
        r13 = r13 + r3;
        r3 = r6.mCachedBorders;
        r12 = r2.mSpanSize;
        r3 = r3[r12];
        r2 = r2.width;
        r12 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = 0;
        r2 = androidx.recyclerview.widget.RecyclerView.LayoutManager.getChildMeasureSpec(r3, r12, r13, r2, r7);
        r3 = r4 - r15;
        r3 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r12);
        r12 = 1;
        r6.measureChildWithDecorationsAndMargin(r1, r2, r3, r12);
        goto L_0x0172;
    L_0x0171:
        r7 = 0;
    L_0x0172:
        r0 = r0 + 1;
        r7 = r25;
        r12 = 0;
        r13 = 1;
        goto L_0x012b;
    L_0x0179:
        r7 = 0;
        r0 = r6.mSet;
        r0 = r0[r7];
        r0 = r6.shouldLayoutChildFromOpositeSide(r0);
        if (r0 == 0) goto L_0x018a;
    L_0x0184:
        r1 = r9.mLayoutDirection;
        r2 = -1;
        if (r1 == r2) goto L_0x0192;
    L_0x0189:
        goto L_0x018b;
    L_0x018a:
        r2 = -1;
    L_0x018b:
        if (r0 != 0) goto L_0x020b;
    L_0x018d:
        r0 = r9.mLayoutDirection;
        r1 = 1;
        if (r0 != r1) goto L_0x020b;
    L_0x0192:
        r0 = r9.mLayoutDirection;
        if (r0 != r2) goto L_0x01a1;
    L_0x0196:
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 - r1;
        r1 = r0 - r4;
        r15 = r0;
        r13 = r1;
        r12 = 0;
        goto L_0x01ae;
    L_0x01a1:
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 + r1;
        r1 = r0 + r4;
        r12 = r24.getWidth();
        r13 = r0;
        r15 = r1;
    L_0x01ae:
        r5 = r5 + -1;
        r0 = r12;
        r12 = r5;
    L_0x01b2:
        if (r12 < 0) goto L_0x0207;
    L_0x01b4:
        r1 = r6.mSet;
        r5 = r1[r12];
        r1 = r5.getLayoutParams();
        r18 = r1;
        r18 = (androidx.recyclerview.widget.GridLayoutManager.LayoutParams) r18;
        r1 = r6.mOrientationHelper;
        r1 = r1.getDecoratedMeasurementInOther(r5);
        r2 = r9.mLayoutDirection;
        r3 = 1;
        if (r2 != r3) goto L_0x01cc;
    L_0x01cb:
        r0 = r0 - r1;
    L_0x01cc:
        r19 = r0;
        r20 = r19 + r1;
        r0 = r24;
        r1 = r5;
        r2 = r19;
        r3 = r13;
        r21 = r4;
        r4 = r20;
        r22 = r5;
        r5 = r15;
        r0.layoutDecoratedWithMargins(r1, r2, r3, r4, r5);
        r0 = r9.mLayoutDirection;
        r1 = -1;
        if (r0 != r1) goto L_0x01e8;
    L_0x01e5:
        r0 = r20;
        goto L_0x01ea;
    L_0x01e8:
        r0 = r19;
    L_0x01ea:
        r1 = r18.isItemRemoved();
        if (r1 != 0) goto L_0x01f6;
    L_0x01f0:
        r1 = r18.isItemChanged();
        if (r1 == 0) goto L_0x01f9;
    L_0x01f6:
        r1 = 1;
        r10.mIgnoreConsumed = r1;
    L_0x01f9:
        r1 = r10.mFocusable;
        r2 = r22.hasFocusable();
        r1 = r1 | r2;
        r10.mFocusable = r1;
        r12 = r12 + -1;
        r4 = r21;
        goto L_0x01b2;
    L_0x0207:
        r21 = r4;
        goto L_0x0284;
    L_0x020b:
        r21 = r4;
        r0 = r9.mLayoutDirection;
        r1 = -1;
        if (r0 != r1) goto L_0x0220;
    L_0x0212:
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 - r1;
        r1 = r0 - r21;
        r12 = r24.getWidth();
        r15 = r0;
        r13 = r1;
        goto L_0x022a;
    L_0x0220:
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 + r1;
        r4 = r0 + r21;
        r13 = r0;
        r15 = r4;
        r12 = 0;
    L_0x022a:
        r0 = r12;
        r12 = 0;
    L_0x022c:
        if (r12 >= r5) goto L_0x0284;
    L_0x022e:
        r1 = r6.mSet;
        r4 = r1[r12];
        r1 = r4.getLayoutParams();
        r18 = r1;
        r18 = (androidx.recyclerview.widget.GridLayoutManager.LayoutParams) r18;
        r1 = r6.mOrientationHelper;
        r1 = r1.getDecoratedMeasurementInOther(r4);
        r2 = r9.mLayoutDirection;
        r3 = -1;
        if (r2 != r3) goto L_0x0246;
    L_0x0245:
        r0 = r0 - r1;
    L_0x0246:
        r19 = r0;
        r20 = r19 + r1;
        r0 = r24;
        r1 = r4;
        r2 = r19;
        r3 = r13;
        r22 = r4;
        r4 = r20;
        r23 = r5;
        r5 = r15;
        r0.layoutDecoratedWithMargins(r1, r2, r3, r4, r5);
        r0 = r9.mLayoutDirection;
        r1 = -1;
        if (r0 == r1) goto L_0x0262;
    L_0x025f:
        r0 = r20;
        goto L_0x0264;
    L_0x0262:
        r0 = r19;
    L_0x0264:
        r2 = r18.isItemRemoved();
        if (r2 != 0) goto L_0x0273;
    L_0x026a:
        r2 = r18.isItemChanged();
        if (r2 == 0) goto L_0x0271;
    L_0x0270:
        goto L_0x0273;
    L_0x0271:
        r2 = 1;
        goto L_0x0276;
    L_0x0273:
        r2 = 1;
        r10.mIgnoreConsumed = r2;
    L_0x0276:
        r3 = r10.mFocusable;
        r4 = r22.hasFocusable();
        r3 = r3 | r4;
        r10.mFocusable = r3;
        r12 = r12 + 1;
        r5 = r23;
        goto L_0x022c;
    L_0x0284:
        r1 = -1;
        r2 = 1;
        r0 = r10.mConsumed;
        r0 = r0 + r21;
        r10.mConsumed = r0;
        r0 = r6.mSet;
        r3 = 0;
        java.util.Arrays.fill(r0, r3);
        r7 = r25;
        r0 = r16;
        r12 = 0;
        r13 = 1;
        r15 = -1;
        goto L_0x0075;
    L_0x029b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.GridLayoutManagerFixed.layoutChunk(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State, androidx.recyclerview.widget.LinearLayoutManager$LayoutState, androidx.recyclerview.widget.LinearLayoutManager$LayoutChunkResult):void");
    }
}
