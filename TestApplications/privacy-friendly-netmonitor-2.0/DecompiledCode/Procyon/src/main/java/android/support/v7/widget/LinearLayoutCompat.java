// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.annotation.RestrictTo;
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
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
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
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
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
    
    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }
    
    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
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
    
    void layoutVertical(int n, int i, int nextLocationOffset, int n2) {
        final int paddingLeft = this.getPaddingLeft();
        final int n3 = nextLocationOffset - n;
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
                n = this.getPaddingTop() + n2 - i - this.mTotalLength;
            }
        }
        else {
            n = this.getPaddingTop();
            n += (n2 - i - this.mTotalLength) / 2;
        }
        View virtualChild;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        for (i = 0; i < virtualChildCount; ++i) {
            virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                nextLocationOffset = n + this.measureNullChild(i);
            }
            else {
                nextLocationOffset = n;
                if (virtualChild.getVisibility() != 8) {
                    measuredWidth = virtualChild.getMeasuredWidth();
                    measuredHeight = virtualChild.getMeasuredHeight();
                    layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                    n2 = layoutParams.gravity;
                    if ((nextLocationOffset = n2) < 0) {
                        nextLocationOffset = (mGravity & 0x800007);
                    }
                    nextLocationOffset = (GravityCompat.getAbsoluteGravity(nextLocationOffset, ViewCompat.getLayoutDirection((View)this)) & 0x7);
                    if (nextLocationOffset != 1) {
                        if (nextLocationOffset != 5) {
                            nextLocationOffset = layoutParams.leftMargin + paddingLeft;
                        }
                        else {
                            nextLocationOffset = n3 - paddingRight - measuredWidth - layoutParams.rightMargin;
                        }
                    }
                    else {
                        nextLocationOffset = (n3 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                    }
                    n2 = n;
                    if (this.hasDividerBeforeChildAt(i)) {
                        n2 = n + this.mDividerHeight;
                    }
                    n = n2 + layoutParams.topMargin;
                    this.setChildFrame(virtualChild, nextLocationOffset, n + this.getLocationOffset(virtualChild), measuredWidth, measuredHeight);
                    n2 = layoutParams.bottomMargin;
                    nextLocationOffset = this.getNextLocationOffset(virtualChild);
                    i += this.getChildrenSkipCount(virtualChild, i);
                    n += measuredHeight + n2 + nextLocationOffset;
                    continue;
                }
            }
            n = nextLocationOffset;
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
        int n3 = 1073741824;
        final boolean b = mode == 1073741824;
        int i = 0;
        int n4;
        int max = n4 = i;
        int n5;
        int max2 = n5 = n4;
        int n7;
        int n6 = n7 = n5;
        int n8 = 1;
        float mWeightSum = 0.0f;
        int b2 = Integer.MIN_VALUE;
        while (i < virtualChildCount) {
            final View virtualChild = this.getVirtualChildAt(i);
            int n15 = 0;
            int n16 = 0;
            Label_0882: {
                if (virtualChild == null) {
                    this.mTotalLength += this.measureNullChild(i);
                }
                else {
                    if (virtualChild.getVisibility() != 8) {
                        if (this.hasDividerBeforeChildAt(i)) {
                            this.mTotalLength += this.mDividerWidth;
                        }
                        final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                        mWeightSum += layoutParams.weight;
                        Label_0602: {
                            int max3;
                            if (mode == n3 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                                if (b) {
                                    this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                                }
                                else {
                                    final int mTotalLength = this.mTotalLength;
                                    this.mTotalLength = Math.max(mTotalLength, layoutParams.leftMargin + mTotalLength + layoutParams.rightMargin);
                                }
                                if (!mBaselineAligned) {
                                    n4 = 1;
                                    break Label_0602;
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
                                final LayoutParams layoutParams2 = layoutParams;
                                final View view = virtualChild;
                                this.measureChildBeforeLayout(virtualChild, i, n, mTotalLength2, n2, 0);
                                if (width != Integer.MIN_VALUE) {
                                    layoutParams2.width = width;
                                }
                                final int measuredWidth = view.getMeasuredWidth();
                                if (b) {
                                    this.mTotalLength += layoutParams2.leftMargin + measuredWidth + layoutParams2.rightMargin + this.getNextLocationOffset(view);
                                }
                                else {
                                    final int mTotalLength3 = this.mTotalLength;
                                    this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredWidth + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset(view));
                                }
                                max3 = b2;
                                if (mUseLargestChild) {
                                    max3 = Math.max(measuredWidth, b2);
                                }
                            }
                            b2 = max3;
                        }
                        final int n9 = i;
                        final int n10 = 1073741824;
                        int n11;
                        if (mode2 != 1073741824 && layoutParams.height == -1) {
                            n11 = (n7 = 1);
                        }
                        else {
                            n11 = 0;
                        }
                        int b3 = layoutParams.topMargin + layoutParams.bottomMargin;
                        int n12 = virtualChild.getMeasuredHeight() + b3;
                        final int combineMeasuredStates = View.combineMeasuredStates(n6, virtualChild.getMeasuredState());
                        if (mBaselineAligned) {
                            final int baseline = virtualChild.getBaseline();
                            if (baseline != -1) {
                                int n13;
                                if (layoutParams.gravity < 0) {
                                    n13 = this.mGravity;
                                }
                                else {
                                    n13 = layoutParams.gravity;
                                }
                                final int n14 = ((n13 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                                mMaxAscent[n14] = Math.max(mMaxAscent[n14], baseline);
                                mMaxDescent[n14] = Math.max(mMaxDescent[n14], n12 - baseline);
                            }
                        }
                        max = Math.max(max, n12);
                        if (n8 != 0 && layoutParams.height == -1) {
                            n8 = 1;
                        }
                        else {
                            n8 = 0;
                        }
                        int max4;
                        if (layoutParams.weight > 0.0f) {
                            if (n11 == 0) {
                                b3 = n12;
                            }
                            max4 = Math.max(n5, b3);
                        }
                        else {
                            if (n11 != 0) {
                                n12 = b3;
                            }
                            max2 = Math.max(max2, n12);
                            max4 = n5;
                        }
                        n15 = this.getChildrenSkipCount(virtualChild, n9) + n9;
                        n6 = combineMeasuredStates;
                        n5 = max4;
                        n16 = n10;
                        break Label_0882;
                    }
                    i += this.getChildrenSkipCount(virtualChild, i);
                }
                final int n17 = i;
                n16 = n3;
                n15 = n17;
            }
            final int n18 = n16;
            i = n15 + 1;
            n3 = n18;
        }
        final int a = max;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        int max5 = 0;
        Label_1025: {
            if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1) {
                max5 = a;
                if (mMaxAscent[3] == -1) {
                    break Label_1025;
                }
            }
            max5 = Math.max(a, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
        }
        if (mUseLargestChild) {
            final int n19 = mode;
            if (n19 == Integer.MIN_VALUE || n19 == 0) {
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
                        final LayoutParams layoutParams3 = (LayoutParams)virtualChild2.getLayoutParams();
                        if (b) {
                            this.mTotalLength += layoutParams3.leftMargin + b2 + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild2);
                        }
                        else {
                            final int mTotalLength4 = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + b2 + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild2));
                        }
                    }
                }
            }
        }
        final int n20 = mode;
        this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
        final int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), n, 0);
        final int n21 = (0xFFFFFF & resolveSizeAndState) - this.mTotalLength;
        int a2;
        int n22;
        int max7;
        int n23;
        if (n4 == 0 && (n21 == 0 || mWeightSum <= 0.0f)) {
            final int max6 = Math.max(max2, n5);
            if (mUseLargestChild && n20 != 1073741824) {
                for (int k = 0; k < virtualChildCount; ++k) {
                    final View virtualChild3 = this.getVirtualChildAt(k);
                    if (virtualChild3 != null) {
                        if (virtualChild3.getVisibility() != 8) {
                            if (((LayoutParams)virtualChild3.getLayoutParams()).weight > 0.0f) {
                                virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(b2, 1073741824), View$MeasureSpec.makeMeasureSpec(virtualChild3.getMeasuredHeight(), 1073741824));
                            }
                        }
                    }
                }
            }
            a2 = max6;
            n22 = virtualChildCount;
            max7 = max5;
            n23 = n8;
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
            a2 = max2;
            int l = 0;
            int n24 = -1;
            n23 = n8;
            final int n25 = virtualChildCount;
            int combineMeasuredStates2 = n6;
            int n26 = n21;
            while (l < n25) {
                final View virtualChild4 = this.getVirtualChildAt(l);
                if (virtualChild4 != null) {
                    if (virtualChild4.getVisibility() != 8) {
                        final LayoutParams layoutParams4 = (LayoutParams)virtualChild4.getLayoutParams();
                        final float weight = layoutParams4.weight;
                        if (weight > 0.0f) {
                            final int n27 = (int)(n26 * weight / mWeightSum);
                            mWeightSum -= weight;
                            final int paddingTop = this.getPaddingTop();
                            final int paddingBottom = this.getPaddingBottom();
                            final int n28 = n26 - n27;
                            final int childMeasureSpec = getChildMeasureSpec(n2, paddingTop + paddingBottom + layoutParams4.topMargin + layoutParams4.bottomMargin, layoutParams4.height);
                            if (layoutParams4.width == 0 && n20 == 1073741824) {
                                int n29;
                                if (n27 > 0) {
                                    n29 = n27;
                                }
                                else {
                                    n29 = 0;
                                }
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(n29, 1073741824), childMeasureSpec);
                            }
                            else {
                                int n30;
                                if ((n30 = virtualChild4.getMeasuredWidth() + n27) < 0) {
                                    n30 = 0;
                                }
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(n30, 1073741824), childMeasureSpec);
                            }
                            combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates2, virtualChild4.getMeasuredState() & 0xFF000000);
                            n26 = n28;
                        }
                        if (b) {
                            this.mTotalLength += virtualChild4.getMeasuredWidth() + layoutParams4.leftMargin + layoutParams4.rightMargin + this.getNextLocationOffset(virtualChild4);
                        }
                        else {
                            final int mTotalLength5 = this.mTotalLength;
                            this.mTotalLength = Math.max(mTotalLength5, virtualChild4.getMeasuredWidth() + mTotalLength5 + layoutParams4.leftMargin + layoutParams4.rightMargin + this.getNextLocationOffset(virtualChild4));
                        }
                        final boolean b4 = mode2 != 1073741824 && layoutParams4.height == -1;
                        final int n31 = layoutParams4.topMargin + layoutParams4.bottomMargin;
                        final int b5 = virtualChild4.getMeasuredHeight() + n31;
                        final int max8 = Math.max(n24, b5);
                        int b6;
                        if (b4) {
                            b6 = n31;
                        }
                        else {
                            b6 = b5;
                        }
                        final int max9 = Math.max(a2, b6);
                        if (n23 != 0 && layoutParams4.height == -1) {
                            n23 = 1;
                        }
                        else {
                            n23 = 0;
                        }
                        if (mBaselineAligned) {
                            final int baseline2 = virtualChild4.getBaseline();
                            if (baseline2 != -1) {
                                int n32;
                                if (layoutParams4.gravity < 0) {
                                    n32 = this.mGravity;
                                }
                                else {
                                    n32 = layoutParams4.gravity;
                                }
                                final int n33 = ((n32 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                                mMaxAscent[n33] = Math.max(mMaxAscent[n33], baseline2);
                                mMaxDescent[n33] = Math.max(mMaxDescent[n33], b5 - baseline2);
                            }
                        }
                        a2 = max9;
                        n24 = max8;
                    }
                }
                ++l;
            }
            n22 = n25;
            this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
            if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1 && mMaxAscent[3] == -1) {
                max7 = n24;
                n6 = combineMeasuredStates2;
            }
            else {
                max7 = Math.max(n24, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
                n6 = combineMeasuredStates2;
            }
        }
        if (n23 != 0 || mode2 == 1073741824) {
            a2 = max7;
        }
        this.setMeasuredDimension(resolveSizeAndState | (0xFF000000 & n6), View.resolveSizeAndState(Math.max(a2 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), n2, n6 << 16));
        if (n7 != 0) {
            this.forceUniformHeight(n22, n);
        }
    }
    
    int measureNullChild(final int n) {
        return 0;
    }
    
    void measureVertical(final int n, final int n2) {
        int n3 = 0;
        this.mTotalLength = 0;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        final int mBaselineAlignedChildIndex = this.mBaselineAlignedChildIndex;
        final boolean mUseLargestChild = this.mUseLargestChild;
        int n4;
        int a = n4 = 0;
        int n5;
        int i = n5 = n4;
        int a2;
        int n6 = a2 = n5;
        float mWeightSum = 0.0f;
        int n7 = 1;
        int max = Integer.MIN_VALUE;
        while (i < virtualChildCount) {
            final View virtualChild = this.getVirtualChildAt(i);
            int n12 = 0;
            int n15 = 0;
            int n16 = 0;
            int n17 = 0;
            Label_0670: {
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
                        n5 = 1;
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
                        final View view = virtualChild;
                        final LayoutParams layoutParams2 = layoutParams;
                        this.measureChildBeforeLayout(virtualChild, i, n, 0, n2, mTotalLength2);
                        if (height != Integer.MIN_VALUE) {
                            layoutParams2.height = height;
                        }
                        final int measuredHeight = view.getMeasuredHeight();
                        final int mTotalLength3 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredHeight + layoutParams2.topMargin + layoutParams2.bottomMargin + this.getNextLocationOffset(view));
                        if (mUseLargestChild) {
                            max = Math.max(measuredHeight, max);
                        }
                    }
                    final int n8 = i;
                    if (mBaselineAlignedChildIndex >= 0 && mBaselineAlignedChildIndex == n8 + 1) {
                        this.mBaselineChildTop = this.mTotalLength;
                    }
                    if (n8 < mBaselineAlignedChildIndex && layoutParams.weight > 0.0f) {
                        throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                    }
                    int n9;
                    if (mode != 1073741824 && layoutParams.width == -1) {
                        n9 = (n6 = 1);
                    }
                    else {
                        n9 = 0;
                    }
                    int b = layoutParams.leftMargin + layoutParams.rightMargin;
                    int n10 = virtualChild.getMeasuredWidth() + b;
                    final int max2 = Math.max(a, n10);
                    final int combineMeasuredStates = View.combineMeasuredStates(n3, virtualChild.getMeasuredState());
                    int n11;
                    if (n7 != 0 && layoutParams.width == -1) {
                        n11 = 1;
                    }
                    else {
                        n11 = 0;
                    }
                    int max3;
                    int max4;
                    if (layoutParams.weight > 0.0f) {
                        if (n9 == 0) {
                            b = n10;
                        }
                        max3 = Math.max(n4, b);
                        max4 = a2;
                    }
                    else {
                        if (n9 != 0) {
                            n10 = b;
                        }
                        max4 = Math.max(a2, n10);
                        max3 = n4;
                    }
                    final int childrenSkipCount = this.getChildrenSkipCount(virtualChild, n8);
                    n12 = n11;
                    a = max2;
                    final int n13 = combineMeasuredStates;
                    final int n14 = max;
                    n15 = childrenSkipCount + n8;
                    a2 = max4;
                    n16 = n13;
                    n4 = max3;
                    n17 = n14;
                    break Label_0670;
                }
                final int n18 = n3;
                n17 = max;
                n16 = n18;
                n15 = i;
                n12 = n7;
            }
            i = n15 + 1;
            final int n19 = n16;
            max = n17;
            n3 = n19;
            n7 = n12;
        }
        final int a3 = a2;
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerHeight;
        }
        if (mUseLargestChild) {
            final int n20 = mode2;
            if (n20 == Integer.MIN_VALUE || n20 == 0) {
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
                        final LayoutParams layoutParams3 = (LayoutParams)virtualChild2.getLayoutParams();
                        final int mTotalLength4 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + max + layoutParams3.topMargin + layoutParams3.bottomMargin + this.getNextLocationOffset(virtualChild2));
                    }
                }
            }
        }
        final int n21 = mode2;
        this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        final int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), n2, 0);
        final int n22 = (0xFFFFFF & resolveSizeAndState) - this.mTotalLength;
        int n23;
        if (n5 == 0 && (n22 == 0 || mWeightSum <= 0.0f)) {
            final int max5 = Math.max(a3, n4);
            if (mUseLargestChild && n21 != 1073741824) {
                for (int k = 0; k < virtualChildCount; ++k) {
                    final View virtualChild3 = this.getVirtualChildAt(k);
                    if (virtualChild3 != null) {
                        if (virtualChild3.getVisibility() != 8) {
                            if (((LayoutParams)virtualChild3.getLayoutParams()).weight > 0.0f) {
                                virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(virtualChild3.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(max, 1073741824));
                            }
                        }
                    }
                }
            }
            n23 = max5;
        }
        else {
            if (this.mWeightSum > 0.0f) {
                mWeightSum = this.mWeightSum;
            }
            this.mTotalLength = 0;
            int l = 0;
            final int n24 = n3;
            int n25 = n22;
            int a4 = a;
            int combineMeasuredStates2 = n24;
            int max6 = a3;
            while (l < virtualChildCount) {
                final View virtualChild4 = this.getVirtualChildAt(l);
                if (virtualChild4.getVisibility() != 8) {
                    final LayoutParams layoutParams4 = (LayoutParams)virtualChild4.getLayoutParams();
                    final float weight = layoutParams4.weight;
                    if (weight > 0.0f) {
                        final int n26 = (int)(n25 * weight / mWeightSum);
                        final int paddingLeft = this.getPaddingLeft();
                        final int paddingRight = this.getPaddingRight();
                        final int n27 = n25 - n26;
                        final int leftMargin = layoutParams4.leftMargin;
                        final int rightMargin = layoutParams4.rightMargin;
                        final int width = layoutParams4.width;
                        mWeightSum -= weight;
                        final int childMeasureSpec = getChildMeasureSpec(n, paddingLeft + paddingRight + leftMargin + rightMargin, width);
                        if (layoutParams4.height == 0 && n21 == 1073741824) {
                            int n28;
                            if (n26 > 0) {
                                n28 = n26;
                            }
                            else {
                                n28 = 0;
                            }
                            virtualChild4.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n28, 1073741824));
                        }
                        else {
                            int n29;
                            if ((n29 = virtualChild4.getMeasuredHeight() + n26) < 0) {
                                n29 = 0;
                            }
                            virtualChild4.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n29, 1073741824));
                        }
                        combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates2, virtualChild4.getMeasuredState() & 0xFFFFFF00);
                        n25 = n27;
                    }
                    final int n30 = layoutParams4.leftMargin + layoutParams4.rightMargin;
                    int n31 = virtualChild4.getMeasuredWidth() + n30;
                    final int max7 = Math.max(a4, n31);
                    if (mode != 1073741824 && layoutParams4.width == -1) {
                        n31 = n30;
                    }
                    max6 = Math.max(max6, n31);
                    int n32;
                    if (n7 != 0 && layoutParams4.width == -1) {
                        n32 = 1;
                    }
                    else {
                        n32 = 0;
                    }
                    final int mTotalLength5 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength5, mTotalLength5 + virtualChild4.getMeasuredHeight() + layoutParams4.topMargin + layoutParams4.bottomMargin + this.getNextLocationOffset(virtualChild4));
                    n7 = n32;
                    a4 = max7;
                }
                ++l;
            }
            this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
            n3 = combineMeasuredStates2;
            a = a4;
            n23 = max6;
        }
        int n33 = a;
        if (n7 == 0) {
            n33 = a;
            if (mode != 1073741824) {
                n33 = n23;
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(n33 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), n, n3), resolveSizeAndState);
        if (n6 != 0) {
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
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
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
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface DividerMode {
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
        
        public LayoutParams(final int n, final int n2, final float weight) {
            super(n, n2);
            this.gravity = -1;
            this.weight = weight;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = -1;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = -1;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.gravity = -1;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface OrientationMode {
    }
}
