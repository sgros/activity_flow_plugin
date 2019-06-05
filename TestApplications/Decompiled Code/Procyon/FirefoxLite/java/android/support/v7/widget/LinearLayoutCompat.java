// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.View$MeasureSpec;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

public class LinearLayoutCompat extends ViewGroup
{
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;
    
    public LinearLayoutCompat(final Context context) {
        this(context, null);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.LinearLayoutCompat, n, 0);
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (n >= 0) {
            this.setOrientation(n);
        }
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (n >= 0) {
            this.setGravity(n);
        }
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!boolean1) {
            this.setBaselineAligned(boolean1);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        this.setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }
    
    private void forceUniformHeight(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.height == -1) {
                    final int width = layoutParams.width;
                    layoutParams.width = virtualChild.getMeasuredWidth();
                    this.measureChildWithMargins(virtualChild, n2, 0, measureSpec, 0);
                    layoutParams.width = width;
                }
            }
        }
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = virtualChild.getMeasuredHeight();
                    this.measureChildWithMargins(virtualChild, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n3 + n, n4 + n2);
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    void drawDividersHorizontal(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                int n;
                if (layoutRtl) {
                    n = virtualChild.getRight() + layoutParams.rightMargin;
                }
                else {
                    n = virtualChild.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
                }
                this.drawVerticalDivider(canvas, n);
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int paddingLeft;
            if (virtualChild2 == null) {
                if (layoutRtl) {
                    paddingLeft = this.getPaddingLeft();
                }
                else {
                    paddingLeft = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
                }
            }
            else {
                final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                if (layoutRtl) {
                    paddingLeft = virtualChild2.getLeft() - layoutParams2.leftMargin - this.mDividerWidth;
                }
                else {
                    paddingLeft = virtualChild2.getRight() + layoutParams2.rightMargin;
                }
            }
            this.drawVerticalDivider(canvas, paddingLeft);
        }
    }
    
    void drawDividersVertical(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                this.drawHorizontalDivider(canvas, virtualChild.getTop() - ((LayoutParams)virtualChild.getLayoutParams()).topMargin - this.mDividerHeight);
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int n;
            if (virtualChild2 == null) {
                n = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
            }
            else {
                n = virtualChild2.getBottom() + ((LayoutParams)virtualChild2.getLayoutParams()).bottomMargin;
            }
            this.drawHorizontalDivider(canvas, n);
        }
    }
    
    void drawHorizontalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, n, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + n);
        this.mDivider.draw(canvas);
    }
    
    void drawVerticalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(n, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + n, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -2);
        }
        if (this.mOrientation == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        if (this.getChildCount() <= this.mBaselineAlignedChildIndex) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        final View child = this.getChildAt(this.mBaselineAlignedChildIndex);
        final int baseline = child.getBaseline();
        if (baseline != -1) {
            int mBaselineChildTop;
            final int n = mBaselineChildTop = this.mBaselineChildTop;
            if (this.mOrientation == 1) {
                final int n2 = this.mGravity & 0x70;
                mBaselineChildTop = n;
                if (n2 != 48) {
                    if (n2 != 16) {
                        if (n2 != 80) {
                            mBaselineChildTop = n;
                        }
                        else {
                            mBaselineChildTop = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                        }
                    }
                    else {
                        mBaselineChildTop = n + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                    }
                }
            }
            return mBaselineChildTop + ((LayoutParams)child.getLayoutParams()).topMargin + baseline;
        }
        if (this.mBaselineAlignedChildIndex == 0) {
            return -1;
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
    }
    
    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }
    
    int getChildrenSkipCount(final View view, final int n) {
        return 0;
    }
    
    public Drawable getDividerDrawable() {
        return this.mDivider;
    }
    
    public int getDividerPadding() {
        return this.mDividerPadding;
    }
    
    public int getDividerWidth() {
        return this.mDividerWidth;
    }
    
    public int getGravity() {
        return this.mGravity;
    }
    
    int getLocationOffset(final View view) {
        return 0;
    }
    
    int getNextLocationOffset(final View view) {
        return 0;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public int getShowDividers() {
        return this.mShowDividers;
    }
    
    View getVirtualChildAt(final int n) {
        return this.getChildAt(n);
    }
    
    int getVirtualChildCount() {
        return this.getChildCount();
    }
    
    public float getWeightSum() {
        return this.mWeightSum;
    }
    
    protected boolean hasDividerBeforeChildAt(int n) {
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = false;
        if (n == 0) {
            if ((this.mShowDividers & 0x1) != 0x0) {
                b3 = true;
            }
            return b3;
        }
        if (n == this.getChildCount()) {
            boolean b4 = b;
            if ((this.mShowDividers & 0x4) != 0x0) {
                b4 = true;
            }
            return b4;
        }
        if ((this.mShowDividers & 0x2) != 0x0) {
            --n;
            boolean b5;
            while (true) {
                b5 = b2;
                if (n < 0) {
                    break;
                }
                if (this.getChildAt(n).getVisibility() != 8) {
                    b5 = true;
                    break;
                }
                --n;
            }
            return b5;
        }
        return false;
    }
    
    void layoutHorizontal(int paddingLeft, int n, int n2, int i) {
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        final int paddingTop = this.getPaddingTop();
        final int n3 = i - n;
        final int paddingBottom = this.getPaddingBottom();
        final int paddingBottom2 = this.getPaddingBottom();
        final int virtualChildCount = this.getVirtualChildCount();
        i = this.mGravity;
        n = (this.mGravity & 0x70);
        final boolean mBaselineAligned = this.mBaselineAligned;
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        i = GravityCompat.getAbsoluteGravity(i & 0x800007, ViewCompat.getLayoutDirection((View)this));
        if (i != 1) {
            if (i != 5) {
                paddingLeft = this.getPaddingLeft();
            }
            else {
                paddingLeft = this.getPaddingLeft() + n2 - paddingLeft - this.mTotalLength;
            }
        }
        else {
            i = this.getPaddingLeft();
            paddingLeft = (n2 - paddingLeft - this.mTotalLength) / 2 + i;
        }
        int n4;
        int n5;
        if (layoutRtl) {
            n4 = virtualChildCount - 1;
            n5 = -1;
        }
        else {
            n4 = 0;
            n5 = 1;
        }
        i = 0;
        n2 = paddingTop;
        while (i < virtualChildCount) {
            final int n6 = n4 + n5 * i;
            final View virtualChild = this.getVirtualChildAt(n6);
            if (virtualChild == null) {
                paddingLeft += this.measureNullChild(n6);
            }
            else if (virtualChild.getVisibility() != 8) {
                final int measuredWidth = virtualChild.getMeasuredWidth();
                final int measuredHeight = virtualChild.getMeasuredHeight();
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                int baseline;
                if (mBaselineAligned && layoutParams.height != -1) {
                    baseline = virtualChild.getBaseline();
                }
                else {
                    baseline = -1;
                }
                int gravity;
                if ((gravity = layoutParams.gravity) < 0) {
                    gravity = n;
                }
                final int n7 = gravity & 0x70;
                int n8;
                if (n7 != 16) {
                    if (n7 != 48) {
                        if (n7 != 80) {
                            n8 = n2;
                        }
                        else {
                            final int n9 = n8 = n3 - paddingBottom - measuredHeight - layoutParams.bottomMargin;
                            if (baseline != -1) {
                                n8 = n9 - (mMaxDescent[2] - (virtualChild.getMeasuredHeight() - baseline));
                            }
                        }
                    }
                    else {
                        n8 = layoutParams.topMargin + n2;
                        if (baseline != -1) {
                            n8 += mMaxAscent[1] - baseline;
                        }
                    }
                }
                else {
                    n8 = (n3 - paddingTop - paddingBottom2 - measuredHeight) / 2 + n2 + layoutParams.topMargin - layoutParams.bottomMargin;
                }
                int n10 = paddingLeft;
                if (this.hasDividerBeforeChildAt(n6)) {
                    n10 = paddingLeft + this.mDividerWidth;
                }
                paddingLeft = layoutParams.leftMargin + n10;
                this.setChildFrame(virtualChild, paddingLeft + this.getLocationOffset(virtualChild), n8, measuredWidth, measuredHeight);
                final int rightMargin = layoutParams.rightMargin;
                final int nextLocationOffset = this.getNextLocationOffset(virtualChild);
                i += this.getChildrenSkipCount(virtualChild, n6);
                paddingLeft += measuredWidth + rightMargin + nextLocationOffset;
            }
            ++i;
        }
    }
    
    void layoutVertical(int n, int i, int bottomMargin, int gravity) {
        final int paddingLeft = this.getPaddingLeft();
        final int n2 = bottomMargin - n;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        final int virtualChildCount = this.getVirtualChildCount();
        n = (this.mGravity & 0x70);
        final int mGravity = this.mGravity;
        if (n != 16) {
            if (n != 80) {
                n = this.getPaddingTop();
            }
            else {
                n = this.getPaddingTop() + gravity - i - this.mTotalLength;
            }
        }
        else {
            n = this.getPaddingTop();
            n += (gravity - i - this.mTotalLength) / 2;
        }
        View virtualChild;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        int nextLocationOffset;
        for (i = 0; i < virtualChildCount; i = gravity + 1, n = bottomMargin) {
            virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                bottomMargin = n + this.measureNullChild(i);
                gravity = i;
            }
            else {
                bottomMargin = n;
                gravity = i;
                if (virtualChild.getVisibility() != 8) {
                    measuredWidth = virtualChild.getMeasuredWidth();
                    measuredHeight = virtualChild.getMeasuredHeight();
                    layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                    gravity = layoutParams.gravity;
                    if ((bottomMargin = gravity) < 0) {
                        bottomMargin = (mGravity & 0x800007);
                    }
                    bottomMargin = (GravityCompat.getAbsoluteGravity(bottomMargin, ViewCompat.getLayoutDirection((View)this)) & 0x7);
                    if (bottomMargin != 1) {
                        if (bottomMargin != 5) {
                            bottomMargin = layoutParams.leftMargin + paddingLeft;
                        }
                        else {
                            bottomMargin = n2 - paddingRight - measuredWidth - layoutParams.rightMargin;
                        }
                    }
                    else {
                        bottomMargin = (n2 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                    }
                    gravity = n;
                    if (this.hasDividerBeforeChildAt(i)) {
                        gravity = n + this.mDividerHeight;
                    }
                    n = gravity + layoutParams.topMargin;
                    this.setChildFrame(virtualChild, bottomMargin, n + this.getLocationOffset(virtualChild), measuredWidth, measuredHeight);
                    bottomMargin = layoutParams.bottomMargin;
                    nextLocationOffset = this.getNextLocationOffset(virtualChild);
                    gravity = i + this.getChildrenSkipCount(virtualChild, i);
                    bottomMargin = n + (measuredHeight + bottomMargin + nextLocationOffset);
                }
            }
        }
    }
    
    void measureChildBeforeLayout(final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        this.measureChildWithMargins(view, n2, n3, n4, n5);
    }
    
    void measureHorizontal(final int n, final int n2) {
        this.mTotalLength = 0;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        mMaxAscent[2] = (mMaxAscent[3] = -1);
        mMaxAscent[0] = (mMaxAscent[1] = -1);
        mMaxDescent[2] = (mMaxDescent[3] = -1);
        mMaxDescent[0] = (mMaxDescent[1] = -1);
        final boolean mBaselineAligned = this.mBaselineAligned;
        final boolean mUseLargestChild = this.mUseLargestChild;
        final boolean b = mode == 1073741824;
        float mWeightSum = 0.0f;
        int i = 0;
        int b2 = 0;
        int max = 0;
        boolean b3 = false;
        int max2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 1;
        boolean b4 = false;
        while (i < virtualChildCount) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                mWeightSum += layoutParams.weight;
                Label_0570: {
                    int max3;
                    if (mode == 1073741824 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                        if (b) {
                            this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                        }
                        else {
                            final int mTotalLength = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength, layoutParams.leftMargin + mTotalLength + layoutParams.rightMargin);
                        }
                        if (!mBaselineAligned) {
                            b3 = true;
                            break Label_0570;
                        }
                        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChild.measure(measureSpec, measureSpec);
                        max3 = b2;
                    }
                    else {
                        int width;
                        if (layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                            layoutParams.width = -2;
                            width = 0;
                        }
                        else {
                            width = Integer.MIN_VALUE;
                        }
                        int mTotalLength2;
                        if (mWeightSum == 0.0f) {
                            mTotalLength2 = this.mTotalLength;
                        }
                        else {
                            mTotalLength2 = 0;
                        }
                        this.measureChildBeforeLayout(virtualChild, i, n, mTotalLength2, n2, 0);
                        if (width != Integer.MIN_VALUE) {
                            layoutParams.width = width;
                        }
                        final int measuredWidth = virtualChild.getMeasuredWidth();
                        if (b) {
                            this.mTotalLength += layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin + this.getNextLocationOffset(virtualChild);
                        }
                        else {
                            final int mTotalLength3 = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + this.getNextLocationOffset(virtualChild));
                        }
                        max3 = b2;
                        if (mUseLargestChild) {
                            max3 = Math.max(measuredWidth, b2);
                        }
                    }
                    b2 = max3;
                }
                final int n6 = i;
                boolean b5;
                if (mode2 != 1073741824 && layoutParams.height == -1) {
                    b5 = true;
                    b4 = true;
                }
                else {
                    b5 = false;
                }
                int b6 = layoutParams.topMargin + layoutParams.bottomMargin;
                int n7 = virtualChild.getMeasuredHeight() + b6;
                final int combineMeasuredStates = View.combineMeasuredStates(n4, virtualChild.getMeasuredState());
                if (mBaselineAligned) {
                    final int baseline = virtualChild.getBaseline();
                    if (baseline != -1) {
                        int n8;
                        if (layoutParams.gravity < 0) {
                            n8 = this.mGravity;
                        }
                        else {
                            n8 = layoutParams.gravity;
                        }
                        final int n9 = ((n8 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                        mMaxAscent[n9] = Math.max(mMaxAscent[n9], baseline);
                        mMaxDescent[n9] = Math.max(mMaxDescent[n9], n7 - baseline);
                    }
                }
                max = Math.max(max, n7);
                if (n5 != 0 && layoutParams.height == -1) {
                    n5 = 1;
                }
                else {
                    n5 = 0;
                }
                int max4;
                if (layoutParams.weight > 0.0f) {
                    if (!b5) {
                        b6 = n7;
                    }
                    max4 = Math.max(n3, b6);
                }
                else {
                    if (b5) {
                        n7 = b6;
                    }
                    max2 = Math.max(max2, n7);
                    max4 = n3;
                }
                final int childrenSkipCount = this.getChildrenSkipCount(virtualChild, n6);
                n4 = combineMeasuredStates;
                final int n10 = childrenSkipCount + n6;
                n3 = max4;
                i = n10;
            }
            ++i;
        }
        int max5 = max;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (mMaxAscent[1] != -1 || mMaxAscent[0] != -1 || mMaxAscent[2] != -1 || mMaxAscent[3] != -1) {
            max5 = Math.max(max5, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
        }
        int n11 = n4;
        int n12 = max5;
        Label_1181: {
            if (mUseLargestChild) {
                if (mode != Integer.MIN_VALUE) {
                    n12 = max5;
                    if (mode != 0) {
                        break Label_1181;
                    }
                }
                this.mTotalLength = 0;
                int n13 = 0;
                while (true) {
                    n12 = max5;
                    if (n13 >= virtualChildCount) {
                        break;
                    }
                    final View virtualChild2 = this.getVirtualChildAt(n13);
                    if (virtualChild2 == null) {
                        this.mTotalLength += this.measureNullChild(n13);
                    }
                    else if (virtualChild2.getVisibility() == 8) {
                        n13 += this.getChildrenSkipCount(virtualChild2, n13);
                    }
                    else {
                        final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                        if (b) {
                            this.mTotalLength += layoutParams2.leftMargin + b2 + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild2);
                        }
                        else {
                            final int mTotalLength4 = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + b2 + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild2));
                        }
                    }
                    ++n13;
                }
            }
        }
        this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
        final int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), n, 0);
        final int n14 = (0xFFFFFF & resolveSizeAndState) - this.mTotalLength;
        int n15;
        int n16;
        int n17;
        int n18;
        if (!b3 && (n14 == 0 || mWeightSum <= 0.0f)) {
            final int max6 = Math.max(max2, n3);
            if (mUseLargestChild && mode != 1073741824) {
                for (int j = 0; j < virtualChildCount; ++j) {
                    final View virtualChild3 = this.getVirtualChildAt(j);
                    if (virtualChild3 != null) {
                        if (virtualChild3.getVisibility() != 8) {
                            if (((LayoutParams)virtualChild3.getLayoutParams()).weight > 0.0f) {
                                virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(b2, 1073741824), View$MeasureSpec.makeMeasureSpec(virtualChild3.getMeasuredHeight(), 1073741824));
                            }
                        }
                    }
                }
            }
            n15 = virtualChildCount;
            n16 = max6;
            n17 = n5;
            n18 = n12;
        }
        else {
            if (this.mWeightSum > 0.0f) {
                mWeightSum = this.mWeightSum;
            }
            mMaxAscent[2] = (mMaxAscent[3] = -1);
            mMaxAscent[0] = (mMaxAscent[1] = -1);
            mMaxDescent[2] = (mMaxDescent[3] = -1);
            mMaxDescent[0] = (mMaxDescent[1] = -1);
            this.mTotalLength = 0;
            int n19 = -1;
            final int n20 = 0;
            n17 = n5;
            n15 = virtualChildCount;
            int a = max2;
            int combineMeasuredStates2 = n11;
            int n21 = n14;
            for (int k = n20; k < n15; ++k) {
                final View virtualChild4 = this.getVirtualChildAt(k);
                if (virtualChild4 != null) {
                    if (virtualChild4.getVisibility() != 8) {
                        final LayoutParams layoutParams3 = (LayoutParams)virtualChild4.getLayoutParams();
                        final float weight = layoutParams3.weight;
                        if (weight > 0.0f) {
                            final int n22 = (int)(n21 * weight / mWeightSum);
                            final int childMeasureSpec = getChildMeasureSpec(n2, this.getPaddingTop() + this.getPaddingBottom() + layoutParams3.topMargin + layoutParams3.bottomMargin, layoutParams3.height);
                            if (layoutParams3.width == 0 && mode == 1073741824) {
                                int n23;
                                if (n22 > 0) {
                                    n23 = n22;
                                }
                                else {
                                    n23 = 0;
                                }
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(n23, 1073741824), childMeasureSpec);
                            }
                            else {
                                int n24;
                                if ((n24 = virtualChild4.getMeasuredWidth() + n22) < 0) {
                                    n24 = 0;
                                }
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(n24, 1073741824), childMeasureSpec);
                            }
                            combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates2, virtualChild4.getMeasuredState() & 0xFF000000);
                            mWeightSum -= weight;
                            n21 -= n22;
                        }
                        if (b) {
                            this.mTotalLength += virtualChild4.getMeasuredWidth() + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild4);
                        }
                        else {
                            final int mTotalLength5 = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength5, virtualChild4.getMeasuredWidth() + mTotalLength5 + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild4));
                        }
                        final boolean b7 = mode2 != 1073741824 && layoutParams3.height == -1;
                        final int n25 = layoutParams3.topMargin + layoutParams3.bottomMargin;
                        final int b8 = virtualChild4.getMeasuredHeight() + n25;
                        final int max7 = Math.max(n19, b8);
                        int b9;
                        if (b7) {
                            b9 = n25;
                        }
                        else {
                            b9 = b8;
                        }
                        final int max8 = Math.max(a, b9);
                        if (n17 != 0 && layoutParams3.height == -1) {
                            n17 = 1;
                        }
                        else {
                            n17 = 0;
                        }
                        if (mBaselineAligned) {
                            final int baseline2 = virtualChild4.getBaseline();
                            if (baseline2 != -1) {
                                int n26;
                                if (layoutParams3.gravity < 0) {
                                    n26 = this.mGravity;
                                }
                                else {
                                    n26 = layoutParams3.gravity;
                                }
                                final int n27 = ((n26 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                                mMaxAscent[n27] = Math.max(mMaxAscent[n27], baseline2);
                                mMaxDescent[n27] = Math.max(mMaxDescent[n27], b8 - baseline2);
                            }
                        }
                        a = max8;
                        n19 = max7;
                    }
                }
            }
            this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
            int max9;
            if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1 && mMaxAscent[3] == -1) {
                max9 = n19;
            }
            else {
                max9 = Math.max(n19, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
            }
            final int n28 = max9;
            n11 = combineMeasuredStates2;
            n16 = a;
            n18 = n28;
        }
        int n29 = n18;
        if (n17 == 0) {
            n29 = n18;
            if (mode2 != 1073741824) {
                n29 = n16;
            }
        }
        this.setMeasuredDimension(resolveSizeAndState | (n11 & 0xFF000000), View.resolveSizeAndState(Math.max(n29 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), n2, n11 << 16));
        if (b4) {
            this.forceUniformHeight(n15, n);
        }
    }
    
    int measureNullChild(final int n) {
        return 0;
    }
    
    void measureVertical(final int n, final int n2) {
        this.mTotalLength = 0;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        final int mBaselineAlignedChildIndex = this.mBaselineAlignedChildIndex;
        final boolean mUseLargestChild = this.mUseLargestChild;
        float mWeightSum = 0.0f;
        int combineMeasuredStates = 0;
        int max = 0;
        int max2 = 0;
        int n3 = 0;
        int n4 = 0;
        int i = 0;
        boolean b = false;
        int n5 = 1;
        boolean b2 = false;
        while (i < virtualChildCount) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerHeight;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                mWeightSum += layoutParams.weight;
                if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                    final int mTotalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength, layoutParams.topMargin + mTotalLength + layoutParams.bottomMargin);
                    b = true;
                }
                else {
                    int height;
                    if (layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                        layoutParams.height = -2;
                        height = 0;
                    }
                    else {
                        height = Integer.MIN_VALUE;
                    }
                    int mTotalLength2;
                    if (mWeightSum == 0.0f) {
                        mTotalLength2 = this.mTotalLength;
                    }
                    else {
                        mTotalLength2 = 0;
                    }
                    this.measureChildBeforeLayout(virtualChild, i, n, 0, n2, mTotalLength2);
                    if (height != Integer.MIN_VALUE) {
                        layoutParams.height = height;
                    }
                    final int measuredHeight = virtualChild.getMeasuredHeight();
                    final int mTotalLength3 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + this.getNextLocationOffset(virtualChild));
                    if (mUseLargestChild) {
                        max2 = Math.max(measuredHeight, max2);
                    }
                }
                final int n6 = i;
                if (mBaselineAlignedChildIndex >= 0 && mBaselineAlignedChildIndex == n6 + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (n6 < mBaselineAlignedChildIndex && layoutParams.weight > 0.0f) {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
                boolean b3;
                if (mode != 1073741824 && layoutParams.width == -1) {
                    b3 = true;
                    b2 = true;
                }
                else {
                    b3 = false;
                }
                final int n7 = layoutParams.leftMargin + layoutParams.rightMargin;
                int b4 = virtualChild.getMeasuredWidth() + n7;
                final int max3 = Math.max(max, b4);
                final int combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, virtualChild.getMeasuredState());
                int n8;
                if (n5 != 0 && layoutParams.width == -1) {
                    n8 = 1;
                }
                else {
                    n8 = 0;
                }
                int max4;
                int n9;
                if (layoutParams.weight > 0.0f) {
                    if (b3) {
                        b4 = n7;
                    }
                    max4 = Math.max(n3, b4);
                    n9 = n4;
                }
                else {
                    if (b3) {
                        b4 = n7;
                    }
                    final int max5 = Math.max(n4, b4);
                    max4 = n3;
                    n9 = max5;
                }
                final int childrenSkipCount = this.getChildrenSkipCount(virtualChild, n6);
                final int n10 = n8;
                final int n11 = max4;
                combineMeasuredStates = combineMeasuredStates2;
                i = childrenSkipCount + n6;
                final int n12 = max3;
                n5 = n10;
                n4 = n9;
                n3 = n11;
                max = n12;
            }
            ++i;
        }
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerHeight;
        }
        if (mUseLargestChild) {
            final int n13 = mode2;
            if (n13 == Integer.MIN_VALUE || n13 == 0) {
                this.mTotalLength = 0;
                for (int j = 0; j < virtualChildCount; ++j) {
                    final View virtualChild2 = this.getVirtualChildAt(j);
                    if (virtualChild2 == null) {
                        this.mTotalLength += this.measureNullChild(j);
                    }
                    else if (virtualChild2.getVisibility() == 8) {
                        j += this.getChildrenSkipCount(virtualChild2, j);
                    }
                    else {
                        final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                        final int mTotalLength4 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + max2 + layoutParams2.topMargin + layoutParams2.bottomMargin + this.getNextLocationOffset(virtualChild2));
                    }
                }
            }
        }
        final int n14 = mode2;
        this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        final int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), n2, 0);
        int n15 = (0xFFFFFF & resolveSizeAndState) - this.mTotalLength;
        int n16;
        int n17;
        if (!b && (n15 == 0 || mWeightSum <= 0.0f)) {
            final int max6 = Math.max(n4, n3);
            if (mUseLargestChild && n14 != 1073741824) {
                for (int k = 0; k < virtualChildCount; ++k) {
                    final View virtualChild3 = this.getVirtualChildAt(k);
                    if (virtualChild3 != null) {
                        if (virtualChild3.getVisibility() != 8) {
                            if (((LayoutParams)virtualChild3.getLayoutParams()).weight > 0.0f) {
                                virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(virtualChild3.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(max2, 1073741824));
                            }
                        }
                    }
                }
            }
            n16 = combineMeasuredStates;
            n17 = max6;
        }
        else {
            if (this.mWeightSum > 0.0f) {
                mWeightSum = this.mWeightSum;
            }
            this.mTotalLength = 0;
            final int n18 = 0;
            int a = n4;
            final int n19 = n14;
            for (int l = n18; l < virtualChildCount; ++l) {
                final View virtualChild4 = this.getVirtualChildAt(l);
                if (virtualChild4.getVisibility() != 8) {
                    final LayoutParams layoutParams3 = (LayoutParams)virtualChild4.getLayoutParams();
                    final float weight = layoutParams3.weight;
                    if (weight > 0.0f) {
                        final int n20 = (int)(n15 * weight / mWeightSum);
                        final int childMeasureSpec = getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + layoutParams3.leftMargin + layoutParams3.rightMargin, layoutParams3.width);
                        if (layoutParams3.height == 0 && n19 == 1073741824) {
                            int n21;
                            if (n20 > 0) {
                                n21 = n20;
                            }
                            else {
                                n21 = 0;
                            }
                            virtualChild4.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n21, 1073741824));
                        }
                        else {
                            int n22;
                            if ((n22 = virtualChild4.getMeasuredHeight() + n20) < 0) {
                                n22 = 0;
                            }
                            virtualChild4.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n22, 1073741824));
                        }
                        combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, virtualChild4.getMeasuredState() & 0xFFFFFF00);
                        n15 -= n20;
                        mWeightSum -= weight;
                    }
                    final int n23 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                    final int b5 = virtualChild4.getMeasuredWidth() + n23;
                    max = Math.max(max, b5);
                    int b6;
                    if (mode != 1073741824 && layoutParams3.width == -1) {
                        b6 = n23;
                    }
                    else {
                        b6 = b5;
                    }
                    final int max7 = Math.max(a, b6);
                    int n24;
                    if (n5 != 0 && layoutParams3.width == -1) {
                        n24 = 1;
                    }
                    else {
                        n24 = 0;
                    }
                    final int mTotalLength5 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength5, mTotalLength5 + virtualChild4.getMeasuredHeight() + layoutParams3.topMargin + layoutParams3.bottomMargin + this.getNextLocationOffset(virtualChild4));
                    n5 = n24;
                    a = max7;
                }
            }
            this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
            final int n25 = a;
            n16 = combineMeasuredStates;
            n17 = n25;
        }
        if (n5 != 0 || mode == 1073741824) {
            n17 = max;
        }
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(n17 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), n, n16), resolveSizeAndState);
        if (b2) {
            this.forceUniformWidth(virtualChildCount, n2);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            this.drawDividersVertical(canvas);
        }
        else {
            this.drawDividersHorizontal(canvas);
        }
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)LinearLayoutCompat.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)LinearLayoutCompat.class.getName());
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.mOrientation == 1) {
            this.layoutVertical(n, n2, n3, n4);
        }
        else {
            this.layoutHorizontal(n, n2, n3, n4);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mOrientation == 1) {
            this.measureVertical(n, n2);
        }
        else {
            this.measureHorizontal(n, n2);
        }
    }
    
    public void setBaselineAligned(final boolean mBaselineAligned) {
        this.mBaselineAligned = mBaselineAligned;
    }
    
    public void setBaselineAlignedChildIndex(final int mBaselineAlignedChildIndex) {
        if (mBaselineAlignedChildIndex >= 0 && mBaselineAlignedChildIndex < this.getChildCount()) {
            this.mBaselineAlignedChildIndex = mBaselineAlignedChildIndex;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("base aligned child index out of range (0, ");
        sb.append(this.getChildCount());
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setDividerDrawable(final Drawable mDivider) {
        if (mDivider == this.mDivider) {
            return;
        }
        this.mDivider = mDivider;
        boolean willNotDraw = false;
        if (mDivider != null) {
            this.mDividerWidth = mDivider.getIntrinsicWidth();
            this.mDividerHeight = mDivider.getIntrinsicHeight();
        }
        else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        if (mDivider == null) {
            willNotDraw = true;
        }
        this.setWillNotDraw(willNotDraw);
        this.requestLayout();
    }
    
    public void setDividerPadding(final int mDividerPadding) {
        this.mDividerPadding = mDividerPadding;
    }
    
    public void setGravity(int mGravity) {
        if (this.mGravity != mGravity) {
            int n = mGravity;
            if ((0x800007 & mGravity) == 0x0) {
                n = (mGravity | 0x800003);
            }
            mGravity = n;
            if ((n & 0x70) == 0x0) {
                mGravity = (n | 0x30);
            }
            this.mGravity = mGravity;
            this.requestLayout();
        }
    }
    
    public void setHorizontalGravity(int n) {
        n &= 0x800007;
        if ((0x800007 & this.mGravity) != n) {
            this.mGravity = (n | (this.mGravity & 0xFF7FFFF8));
            this.requestLayout();
        }
    }
    
    public void setMeasureWithLargestChildEnabled(final boolean mUseLargestChild) {
        this.mUseLargestChild = mUseLargestChild;
    }
    
    public void setOrientation(final int mOrientation) {
        if (this.mOrientation != mOrientation) {
            this.mOrientation = mOrientation;
            this.requestLayout();
        }
    }
    
    public void setShowDividers(final int mShowDividers) {
        if (mShowDividers != this.mShowDividers) {
            this.requestLayout();
        }
        this.mShowDividers = mShowDividers;
    }
    
    public void setVerticalGravity(int n) {
        n &= 0x70;
        if ((this.mGravity & 0x70) != n) {
            this.mGravity = (n | (this.mGravity & 0xFFFFFF8F));
            this.requestLayout();
        }
    }
    
    public void setWeightSum(final float b) {
        this.mWeightSum = Math.max(0.0f, b);
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int gravity;
        public float weight;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.gravity = -1;
            this.weight = 0.0f;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = -1;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = -1;
        }
    }
}
