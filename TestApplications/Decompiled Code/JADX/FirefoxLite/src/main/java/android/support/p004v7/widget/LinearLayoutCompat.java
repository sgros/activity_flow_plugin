package android.support.p004v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.p001v4.view.GravityCompat;
import android.support.p001v4.view.ViewCompat;
import android.support.p004v7.appcompat.C0187R;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/* renamed from: android.support.v7.widget.LinearLayoutCompat */
public class LinearLayoutCompat extends ViewGroup {
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

    /* renamed from: android.support.v7.widget.LinearLayoutCompat$LayoutParams */
    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0187R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(C0187R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(C0187R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public int measureNullChild(int i) {
        return 0;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0187R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(C0187R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        i2 = obtainStyledAttributes.getInt(C0187R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i2 >= 0) {
            setGravity(i2);
        }
        boolean z = obtainStyledAttributes.getBoolean(C0187R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(C0187R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(C0187R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(C0187R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(C0187R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(C0187R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(C0187R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawDividersVertical(Canvas canvas) {
        int virtualChildCount = getVirtualChildCount();
        int i = 0;
        while (i < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
            i++;
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                virtualChildCount = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                virtualChildCount = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, virtualChildCount);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawDividersHorizontal(Canvas canvas) {
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i = 0;
        while (i < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                int right;
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    right = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    right = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, right);
            }
            i++;
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    virtualChildCount = (virtualChildAt2.getLeft() - layoutParams2.leftMargin) - this.mDividerWidth;
                } else {
                    virtualChildCount = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                }
            } else if (isLayoutRtl) {
                virtualChildCount = getPaddingLeft();
            } else {
                virtualChildCount = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, virtualChildCount);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* Access modifiers changed, original: 0000 */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        if (getChildCount() > this.mBaselineAlignedChildIndex) {
            View childAt = getChildAt(this.mBaselineAlignedChildIndex);
            int baseline = childAt.getBaseline();
            if (baseline != -1) {
                int i = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int i2 = this.mGravity & 112;
                    if (i2 != 48) {
                        if (i2 == 16) {
                            i += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                        } else if (i2 == 80) {
                            i = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                        }
                    }
                }
                return (i + ((LayoutParams) childAt.getLayoutParams()).topMargin) + baseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("base aligned child index out of range (0, ");
            stringBuilder.append(getChildCount());
            stringBuilder.append(")");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* Access modifiers changed, original: 0000 */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* Access modifiers changed, original: 0000 */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        boolean z = false;
        if (i == 0) {
            if ((this.mShowDividers & 1) != 0) {
                z = true;
            }
            return z;
        } else if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z = true;
            }
            return z;
        } else if ((this.mShowDividers & 2) == 0) {
            return false;
        } else {
            for (i--; i >= 0; i--) {
                if (getChildAt(i).getVisibility() != 8) {
                    z = true;
                    break;
                }
            }
            return z;
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:141:0x0334  */
    public void measureVertical(int r41, int r42) {
        /*
        r40 = this;
        r7 = r40;
        r8 = r41;
        r9 = r42;
        r10 = 0;
        r7.mTotalLength = r10;
        r11 = r40.getVirtualChildCount();
        r12 = android.view.View.MeasureSpec.getMode(r41);
        r13 = android.view.View.MeasureSpec.getMode(r42);
        r14 = r7.mBaselineAlignedChildIndex;
        r15 = r7.mUseLargestChild;
        r16 = 0;
        r17 = 1;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r18 = 0;
        r19 = 1;
        r20 = 0;
    L_0x002a:
        r10 = 8;
        r22 = r4;
        if (r6 >= r11) goto L_0x019d;
    L_0x0030:
        r4 = r7.getVirtualChildAt(r6);
        if (r4 != 0) goto L_0x0047;
    L_0x0036:
        r4 = r7.mTotalLength;
        r10 = r7.measureNullChild(r6);
        r4 = r4 + r10;
        r7.mTotalLength = r4;
        r32 = r11;
        r31 = r13;
        r4 = r22;
        goto L_0x0191;
    L_0x0047:
        r24 = r1;
        r1 = r4.getVisibility();
        if (r1 != r10) goto L_0x005e;
    L_0x004f:
        r1 = r7.getChildrenSkipCount(r4, r6);
        r6 = r6 + r1;
        r32 = r11;
        r31 = r13;
        r4 = r22;
        r1 = r24;
        goto L_0x0191;
    L_0x005e:
        r1 = r7.hasDividerBeforeChildAt(r6);
        if (r1 == 0) goto L_0x006b;
    L_0x0064:
        r1 = r7.mTotalLength;
        r10 = r7.mDividerHeight;
        r1 = r1 + r10;
        r7.mTotalLength = r1;
    L_0x006b:
        r1 = r4.getLayoutParams();
        r10 = r1;
        r10 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r10;
        r1 = r10.weight;
        r25 = r0 + r1;
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 != r1) goto L_0x00a7;
    L_0x007a:
        r0 = r10.height;
        if (r0 != 0) goto L_0x00a7;
    L_0x007e:
        r0 = r10.weight;
        r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r0 <= 0) goto L_0x00a7;
    L_0x0084:
        r0 = r7.mTotalLength;
        r1 = r10.topMargin;
        r1 = r1 + r0;
        r26 = r2;
        r2 = r10.bottomMargin;
        r1 = r1 + r2;
        r0 = java.lang.Math.max(r0, r1);
        r7.mTotalLength = r0;
        r0 = r3;
        r3 = r4;
        r33 = r5;
        r32 = r11;
        r31 = r13;
        r13 = r22;
        r8 = r24;
        r29 = r26;
        r18 = 1;
        r11 = r6;
        goto L_0x0117;
    L_0x00a7:
        r26 = r2;
        r0 = r10.height;
        if (r0 != 0) goto L_0x00b8;
    L_0x00ad:
        r0 = r10.weight;
        r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r0 <= 0) goto L_0x00b8;
    L_0x00b3:
        r0 = -2;
        r10.height = r0;
        r2 = 0;
        goto L_0x00ba;
    L_0x00b8:
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
    L_0x00ba:
        r23 = 0;
        r0 = (r25 > r16 ? 1 : (r25 == r16 ? 0 : -1));
        if (r0 != 0) goto L_0x00c5;
    L_0x00c0:
        r0 = r7.mTotalLength;
        r27 = r0;
        goto L_0x00c7;
    L_0x00c5:
        r27 = 0;
    L_0x00c7:
        r0 = r40;
        r8 = r24;
        r24 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r1 = r4;
        r28 = r2;
        r29 = r26;
        r2 = r6;
        r9 = r3;
        r3 = r41;
        r30 = r4;
        r32 = r11;
        r31 = r13;
        r13 = r22;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = r23;
        r33 = r5;
        r5 = r42;
        r11 = r6;
        r6 = r27;
        r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6);
        r0 = r28;
        r1 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r0 == r1) goto L_0x00f4;
    L_0x00f2:
        r10.height = r0;
    L_0x00f4:
        r0 = r30.getMeasuredHeight();
        r1 = r7.mTotalLength;
        r2 = r1 + r0;
        r3 = r10.topMargin;
        r2 = r2 + r3;
        r3 = r10.bottomMargin;
        r2 = r2 + r3;
        r3 = r30;
        r4 = r7.getNextLocationOffset(r3);
        r2 = r2 + r4;
        r1 = java.lang.Math.max(r1, r2);
        r7.mTotalLength = r1;
        if (r15 == 0) goto L_0x0116;
    L_0x0111:
        r0 = java.lang.Math.max(r0, r9);
        goto L_0x0117;
    L_0x0116:
        r0 = r9;
    L_0x0117:
        if (r14 < 0) goto L_0x0121;
    L_0x0119:
        r6 = r11 + 1;
        if (r14 != r6) goto L_0x0121;
    L_0x011d:
        r1 = r7.mTotalLength;
        r7.mBaselineChildTop = r1;
    L_0x0121:
        if (r11 >= r14) goto L_0x0132;
    L_0x0123:
        r1 = r10.weight;
        r1 = (r1 > r16 ? 1 : (r1 == r16 ? 0 : -1));
        if (r1 > 0) goto L_0x012a;
    L_0x0129:
        goto L_0x0132;
    L_0x012a:
        r0 = new java.lang.RuntimeException;
        r1 = "A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.";
        r0.<init>(r1);
        throw r0;
    L_0x0132:
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 == r1) goto L_0x013f;
    L_0x0136:
        r1 = r10.width;
        r2 = -1;
        if (r1 != r2) goto L_0x013f;
    L_0x013b:
        r1 = 1;
        r20 = 1;
        goto L_0x0140;
    L_0x013f:
        r1 = 0;
    L_0x0140:
        r2 = r10.leftMargin;
        r4 = r10.rightMargin;
        r2 = r2 + r4;
        r4 = r3.getMeasuredWidth();
        r4 = r4 + r2;
        r5 = r29;
        r5 = java.lang.Math.max(r5, r4);
        r6 = r3.getMeasuredState();
        r6 = android.view.View.combineMeasuredStates(r8, r6);
        if (r19 == 0) goto L_0x0161;
    L_0x015a:
        r8 = r10.width;
        r9 = -1;
        if (r8 != r9) goto L_0x0161;
    L_0x015f:
        r8 = 1;
        goto L_0x0162;
    L_0x0161:
        r8 = 0;
    L_0x0162:
        r9 = r10.weight;
        r9 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1));
        if (r9 <= 0) goto L_0x0174;
    L_0x0168:
        if (r1 == 0) goto L_0x016b;
    L_0x016a:
        goto L_0x016c;
    L_0x016b:
        r2 = r4;
    L_0x016c:
        r4 = java.lang.Math.max(r13, r2);
        r13 = r4;
        r1 = r33;
        goto L_0x017f;
    L_0x0174:
        if (r1 == 0) goto L_0x0179;
    L_0x0176:
        r1 = r33;
        goto L_0x017b;
    L_0x0179:
        r2 = r4;
        goto L_0x0176;
    L_0x017b:
        r1 = java.lang.Math.max(r1, r2);
    L_0x017f:
        r2 = r7.getChildrenSkipCount(r3, r11);
        r2 = r2 + r11;
        r3 = r0;
        r19 = r8;
        r4 = r13;
        r0 = r25;
        r39 = r5;
        r5 = r1;
        r1 = r6;
        r6 = r2;
        r2 = r39;
    L_0x0191:
        r6 = r6 + 1;
        r13 = r31;
        r11 = r32;
        r8 = r41;
        r9 = r42;
        goto L_0x002a;
    L_0x019d:
        r8 = r1;
        r9 = r3;
        r1 = r5;
        r32 = r11;
        r31 = r13;
        r13 = r22;
        r5 = r2;
        r2 = r7.mTotalLength;
        if (r2 <= 0) goto L_0x01bb;
    L_0x01ab:
        r2 = r32;
        r3 = r7.hasDividerBeforeChildAt(r2);
        if (r3 == 0) goto L_0x01bd;
    L_0x01b3:
        r3 = r7.mTotalLength;
        r4 = r7.mDividerHeight;
        r3 = r3 + r4;
        r7.mTotalLength = r3;
        goto L_0x01bd;
    L_0x01bb:
        r2 = r32;
    L_0x01bd:
        if (r15 == 0) goto L_0x020c;
    L_0x01bf:
        r3 = r31;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r3 == r4) goto L_0x01c7;
    L_0x01c5:
        if (r3 != 0) goto L_0x020e;
    L_0x01c7:
        r4 = 0;
        r7.mTotalLength = r4;
        r4 = 0;
    L_0x01cb:
        if (r4 >= r2) goto L_0x020e;
    L_0x01cd:
        r6 = r7.getVirtualChildAt(r4);
        if (r6 != 0) goto L_0x01dd;
    L_0x01d3:
        r6 = r7.mTotalLength;
        r11 = r7.measureNullChild(r4);
        r6 = r6 + r11;
        r7.mTotalLength = r6;
        goto L_0x0207;
    L_0x01dd:
        r11 = r6.getVisibility();
        if (r11 != r10) goto L_0x01e9;
    L_0x01e3:
        r6 = r7.getChildrenSkipCount(r6, r4);
        r4 = r4 + r6;
        goto L_0x0207;
    L_0x01e9:
        r11 = r6.getLayoutParams();
        r11 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r11;
        r14 = r7.mTotalLength;
        r21 = r14 + r9;
        r10 = r11.topMargin;
        r21 = r21 + r10;
        r10 = r11.bottomMargin;
        r21 = r21 + r10;
        r6 = r7.getNextLocationOffset(r6);
        r6 = r21 + r6;
        r6 = java.lang.Math.max(r14, r6);
        r7.mTotalLength = r6;
    L_0x0207:
        r4 = r4 + 1;
        r10 = 8;
        goto L_0x01cb;
    L_0x020c:
        r3 = r31;
    L_0x020e:
        r4 = r7.mTotalLength;
        r6 = r40.getPaddingTop();
        r10 = r40.getPaddingBottom();
        r6 = r6 + r10;
        r4 = r4 + r6;
        r7.mTotalLength = r4;
        r4 = r7.mTotalLength;
        r6 = r40.getSuggestedMinimumHeight();
        r4 = java.lang.Math.max(r4, r6);
        r10 = r9;
        r6 = r42;
        r9 = 0;
        r4 = android.view.View.resolveSizeAndState(r4, r6, r9);
        r9 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
        r9 = r9 & r4;
        r11 = r7.mTotalLength;
        r9 = r9 - r11;
        if (r18 != 0) goto L_0x027f;
    L_0x0237:
        if (r9 == 0) goto L_0x023e;
    L_0x0239:
        r11 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r11 <= 0) goto L_0x023e;
    L_0x023d:
        goto L_0x027f;
    L_0x023e:
        r0 = java.lang.Math.max(r1, r13);
        if (r15 == 0) goto L_0x027a;
    L_0x0244:
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r3 == r1) goto L_0x027a;
    L_0x0248:
        r1 = 0;
    L_0x0249:
        if (r1 >= r2) goto L_0x027a;
    L_0x024b:
        r3 = r7.getVirtualChildAt(r1);
        if (r3 == 0) goto L_0x0277;
    L_0x0251:
        r9 = r3.getVisibility();
        r11 = 8;
        if (r9 != r11) goto L_0x025a;
    L_0x0259:
        goto L_0x0277;
    L_0x025a:
        r9 = r3.getLayoutParams();
        r9 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r9;
        r9 = r9.weight;
        r9 = (r9 > r16 ? 1 : (r9 == r16 ? 0 : -1));
        if (r9 <= 0) goto L_0x0277;
    L_0x0266:
        r9 = r3.getMeasuredWidth();
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r9 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r11);
        r13 = android.view.View.MeasureSpec.makeMeasureSpec(r10, r11);
        r3.measure(r9, r13);
    L_0x0277:
        r1 = r1 + 1;
        goto L_0x0249;
    L_0x027a:
        r1 = r8;
        r11 = r41;
        goto L_0x037d;
    L_0x027f:
        r10 = r7.mWeightSum;
        r10 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1));
        if (r10 <= 0) goto L_0x0287;
    L_0x0285:
        r0 = r7.mWeightSum;
    L_0x0287:
        r10 = 0;
        r7.mTotalLength = r10;
        r11 = r0;
        r0 = 0;
        r39 = r8;
        r8 = r1;
        r1 = r39;
    L_0x0291:
        if (r0 >= r2) goto L_0x036c;
    L_0x0293:
        r13 = r7.getVirtualChildAt(r0);
        r14 = r13.getVisibility();
        r15 = 8;
        if (r14 != r15) goto L_0x02a6;
    L_0x029f:
        r38 = r3;
        r10 = r11;
        r11 = r41;
        goto L_0x0364;
    L_0x02a6:
        r14 = r13.getLayoutParams();
        r14 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r14;
        r10 = r14.weight;
        r18 = (r10 > r16 ? 1 : (r10 == r16 ? 0 : -1));
        if (r18 <= 0) goto L_0x030b;
    L_0x02b2:
        r15 = (float) r9;
        r15 = r15 * r10;
        r15 = r15 / r11;
        r15 = (int) r15;
        r11 = r11 - r10;
        r9 = r9 - r15;
        r10 = r40.getPaddingLeft();
        r18 = r40.getPaddingRight();
        r10 = r10 + r18;
        r34 = r9;
        r9 = r14.leftMargin;
        r10 = r10 + r9;
        r9 = r14.rightMargin;
        r10 = r10 + r9;
        r9 = r14.width;
        r35 = r11;
        r11 = r41;
        r9 = android.support.p004v7.widget.LinearLayoutCompat.getChildMeasureSpec(r11, r10, r9);
        r10 = r14.height;
        if (r10 != 0) goto L_0x02ea;
    L_0x02d9:
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r3 == r10) goto L_0x02de;
    L_0x02dd:
        goto L_0x02ec;
    L_0x02de:
        if (r15 <= 0) goto L_0x02e1;
    L_0x02e0:
        goto L_0x02e2;
    L_0x02e1:
        r15 = 0;
    L_0x02e2:
        r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r10);
        r13.measure(r9, r15);
        goto L_0x02fc;
    L_0x02ea:
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
    L_0x02ec:
        r18 = r13.getMeasuredHeight();
        r15 = r18 + r15;
        if (r15 >= 0) goto L_0x02f5;
    L_0x02f4:
        r15 = 0;
    L_0x02f5:
        r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r10);
        r13.measure(r9, r15);
    L_0x02fc:
        r9 = r13.getMeasuredState();
        r9 = r9 & -256;
        r1 = android.view.View.combineMeasuredStates(r1, r9);
        r9 = r34;
        r10 = r35;
        goto L_0x030e;
    L_0x030b:
        r10 = r11;
        r11 = r41;
    L_0x030e:
        r15 = r14.leftMargin;
        r36 = r1;
        r1 = r14.rightMargin;
        r15 = r15 + r1;
        r1 = r13.getMeasuredWidth();
        r1 = r1 + r15;
        r5 = java.lang.Math.max(r5, r1);
        r37 = r1;
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 == r1) goto L_0x032d;
    L_0x0324:
        r1 = r14.width;
        r38 = r3;
        r3 = -1;
        if (r1 != r3) goto L_0x0330;
    L_0x032b:
        r1 = 1;
        goto L_0x0331;
    L_0x032d:
        r38 = r3;
        r3 = -1;
    L_0x0330:
        r1 = 0;
    L_0x0331:
        if (r1 == 0) goto L_0x0334;
    L_0x0333:
        goto L_0x0336;
    L_0x0334:
        r15 = r37;
    L_0x0336:
        r1 = java.lang.Math.max(r8, r15);
        if (r19 == 0) goto L_0x0342;
    L_0x033c:
        r8 = r14.width;
        if (r8 != r3) goto L_0x0342;
    L_0x0340:
        r8 = 1;
        goto L_0x0343;
    L_0x0342:
        r8 = 0;
    L_0x0343:
        r15 = r7.mTotalLength;
        r18 = r13.getMeasuredHeight();
        r18 = r15 + r18;
        r3 = r14.topMargin;
        r18 = r18 + r3;
        r3 = r14.bottomMargin;
        r18 = r18 + r3;
        r3 = r7.getNextLocationOffset(r13);
        r3 = r18 + r3;
        r3 = java.lang.Math.max(r15, r3);
        r7.mTotalLength = r3;
        r19 = r8;
        r8 = r1;
        r1 = r36;
    L_0x0364:
        r0 = r0 + 1;
        r11 = r10;
        r3 = r38;
        r10 = 0;
        goto L_0x0291;
    L_0x036c:
        r11 = r41;
        r0 = r7.mTotalLength;
        r3 = r40.getPaddingTop();
        r9 = r40.getPaddingBottom();
        r3 = r3 + r9;
        r0 = r0 + r3;
        r7.mTotalLength = r0;
        r0 = r8;
    L_0x037d:
        if (r19 != 0) goto L_0x0384;
    L_0x037f:
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 == r3) goto L_0x0384;
    L_0x0383:
        goto L_0x0385;
    L_0x0384:
        r0 = r5;
    L_0x0385:
        r3 = r40.getPaddingLeft();
        r5 = r40.getPaddingRight();
        r3 = r3 + r5;
        r0 = r0 + r3;
        r3 = r40.getSuggestedMinimumWidth();
        r0 = java.lang.Math.max(r0, r3);
        r0 = android.view.View.resolveSizeAndState(r0, r11, r1);
        r7.setMeasuredDimension(r0, r4);
        if (r20 == 0) goto L_0x03a3;
    L_0x03a0:
        r7.forceUniformWidth(r2, r6);
    L_0x03a3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.LinearLayoutCompat.measureVertical(int, int):void");
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0189  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01d9  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01cb  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x044d  */
    public void measureHorizontal(int r45, int r46) {
        /*
        r44 = this;
        r7 = r44;
        r8 = r45;
        r9 = r46;
        r10 = 0;
        r7.mTotalLength = r10;
        r11 = r44.getVirtualChildCount();
        r12 = android.view.View.MeasureSpec.getMode(r45);
        r13 = android.view.View.MeasureSpec.getMode(r46);
        r0 = r7.mMaxAscent;
        r14 = 4;
        if (r0 == 0) goto L_0x001e;
    L_0x001a:
        r0 = r7.mMaxDescent;
        if (r0 != 0) goto L_0x0026;
    L_0x001e:
        r0 = new int[r14];
        r7.mMaxAscent = r0;
        r0 = new int[r14];
        r7.mMaxDescent = r0;
    L_0x0026:
        r15 = r7.mMaxAscent;
        r6 = r7.mMaxDescent;
        r16 = 3;
        r5 = -1;
        r15[r16] = r5;
        r17 = 2;
        r15[r17] = r5;
        r18 = 1;
        r15[r18] = r5;
        r15[r10] = r5;
        r6[r16] = r5;
        r6[r17] = r5;
        r6[r18] = r5;
        r6[r10] = r5;
        r4 = r7.mBaselineAligned;
        r3 = r7.mUseLargestChild;
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 != r2) goto L_0x004c;
    L_0x0049:
        r19 = 1;
        goto L_0x004e;
    L_0x004c:
        r19 = 0;
    L_0x004e:
        r20 = 0;
        r0 = 0;
        r1 = 0;
        r14 = 0;
        r21 = 0;
        r22 = 0;
        r23 = 0;
        r24 = 0;
        r26 = 0;
        r27 = 1;
        r28 = 0;
    L_0x0061:
        r29 = r6;
        r5 = 8;
        if (r1 >= r11) goto L_0x0205;
    L_0x0067:
        r6 = r7.getVirtualChildAt(r1);
        if (r6 != 0) goto L_0x007c;
    L_0x006d:
        r5 = r7.mTotalLength;
        r6 = r7.measureNullChild(r1);
        r5 = r5 + r6;
        r7.mTotalLength = r5;
    L_0x0076:
        r32 = r3;
        r36 = r4;
        goto L_0x01f5;
    L_0x007c:
        r10 = r6.getVisibility();
        if (r10 != r5) goto L_0x0088;
    L_0x0082:
        r5 = r7.getChildrenSkipCount(r6, r1);
        r1 = r1 + r5;
        goto L_0x0076;
    L_0x0088:
        r5 = r7.hasDividerBeforeChildAt(r1);
        if (r5 == 0) goto L_0x0095;
    L_0x008e:
        r5 = r7.mTotalLength;
        r10 = r7.mDividerWidth;
        r5 = r5 + r10;
        r7.mTotalLength = r5;
    L_0x0095:
        r5 = r6.getLayoutParams();
        r10 = r5;
        r10 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r10;
        r5 = r10.weight;
        r31 = r0 + r5;
        if (r12 != r2) goto L_0x00eb;
    L_0x00a2:
        r0 = r10.width;
        if (r0 != 0) goto L_0x00eb;
    L_0x00a6:
        r0 = r10.weight;
        r0 = (r0 > r20 ? 1 : (r0 == r20 ? 0 : -1));
        if (r0 <= 0) goto L_0x00eb;
    L_0x00ac:
        if (r19 == 0) goto L_0x00b9;
    L_0x00ae:
        r0 = r7.mTotalLength;
        r5 = r10.leftMargin;
        r2 = r10.rightMargin;
        r5 = r5 + r2;
        r0 = r0 + r5;
        r7.mTotalLength = r0;
        goto L_0x00c7;
    L_0x00b9:
        r0 = r7.mTotalLength;
        r2 = r10.leftMargin;
        r2 = r2 + r0;
        r5 = r10.rightMargin;
        r2 = r2 + r5;
        r0 = java.lang.Math.max(r0, r2);
        r7.mTotalLength = r0;
    L_0x00c7:
        if (r4 == 0) goto L_0x00dc;
    L_0x00c9:
        r0 = 0;
        r2 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r0);
        r6.measure(r2, r2);
        r34 = r1;
        r32 = r3;
        r36 = r4;
        r3 = r6;
        r30 = -2;
        goto L_0x0165;
    L_0x00dc:
        r34 = r1;
        r32 = r3;
        r36 = r4;
        r3 = r6;
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r22 = 1;
        r30 = -2;
        goto L_0x0167;
    L_0x00eb:
        r0 = r10.width;
        if (r0 != 0) goto L_0x00fa;
    L_0x00ef:
        r0 = r10.weight;
        r0 = (r0 > r20 ? 1 : (r0 == r20 ? 0 : -1));
        if (r0 <= 0) goto L_0x00fa;
    L_0x00f5:
        r5 = -2;
        r10.width = r5;
        r2 = 0;
        goto L_0x00fd;
    L_0x00fa:
        r5 = -2;
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
    L_0x00fd:
        r0 = (r31 > r20 ? 1 : (r31 == r20 ? 0 : -1));
        if (r0 != 0) goto L_0x0106;
    L_0x0101:
        r0 = r7.mTotalLength;
        r30 = r0;
        goto L_0x0108;
    L_0x0106:
        r30 = 0;
    L_0x0108:
        r33 = 0;
        r0 = r44;
        r34 = r1;
        r1 = r6;
        r35 = r2;
        r2 = r34;
        r32 = r3;
        r3 = r45;
        r36 = r4;
        r4 = r30;
        r9 = -1;
        r30 = -2;
        r5 = r46;
        r37 = r6;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r6 = r33;
        r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6);
        r0 = r35;
        if (r0 == r9) goto L_0x012f;
    L_0x012d:
        r10.width = r0;
    L_0x012f:
        r0 = r37.getMeasuredWidth();
        if (r19 == 0) goto L_0x0148;
    L_0x0135:
        r1 = r7.mTotalLength;
        r2 = r10.leftMargin;
        r2 = r2 + r0;
        r3 = r10.rightMargin;
        r2 = r2 + r3;
        r3 = r37;
        r4 = r7.getNextLocationOffset(r3);
        r2 = r2 + r4;
        r1 = r1 + r2;
        r7.mTotalLength = r1;
        goto L_0x015f;
    L_0x0148:
        r3 = r37;
        r1 = r7.mTotalLength;
        r2 = r1 + r0;
        r4 = r10.leftMargin;
        r2 = r2 + r4;
        r4 = r10.rightMargin;
        r2 = r2 + r4;
        r4 = r7.getNextLocationOffset(r3);
        r2 = r2 + r4;
        r1 = java.lang.Math.max(r1, r2);
        r7.mTotalLength = r1;
    L_0x015f:
        if (r32 == 0) goto L_0x0165;
    L_0x0161:
        r14 = java.lang.Math.max(r0, r14);
    L_0x0165:
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
    L_0x0167:
        if (r13 == r1) goto L_0x0172;
    L_0x0169:
        r0 = r10.height;
        r2 = -1;
        if (r0 != r2) goto L_0x0172;
    L_0x016e:
        r0 = 1;
        r28 = 1;
        goto L_0x0173;
    L_0x0172:
        r0 = 0;
    L_0x0173:
        r2 = r10.topMargin;
        r4 = r10.bottomMargin;
        r2 = r2 + r4;
        r4 = r3.getMeasuredHeight();
        r4 = r4 + r2;
        r5 = r3.getMeasuredState();
        r6 = r26;
        r5 = android.view.View.combineMeasuredStates(r6, r5);
        if (r36 == 0) goto L_0x01b5;
    L_0x0189:
        r6 = r3.getBaseline();
        r9 = -1;
        if (r6 == r9) goto L_0x01b5;
    L_0x0190:
        r9 = r10.gravity;
        if (r9 >= 0) goto L_0x0197;
    L_0x0194:
        r9 = r7.mGravity;
        goto L_0x0199;
    L_0x0197:
        r9 = r10.gravity;
    L_0x0199:
        r9 = r9 & 112;
        r25 = 4;
        r9 = r9 >> 4;
        r9 = r9 & -2;
        r9 = r9 >> 1;
        r1 = r15[r9];
        r1 = java.lang.Math.max(r1, r6);
        r15[r9] = r1;
        r1 = r29[r9];
        r6 = r4 - r6;
        r1 = java.lang.Math.max(r1, r6);
        r29[r9] = r1;
    L_0x01b5:
        r1 = r21;
        r1 = java.lang.Math.max(r1, r4);
        if (r27 == 0) goto L_0x01c4;
    L_0x01bd:
        r6 = r10.height;
        r9 = -1;
        if (r6 != r9) goto L_0x01c4;
    L_0x01c2:
        r6 = 1;
        goto L_0x01c5;
    L_0x01c4:
        r6 = 0;
    L_0x01c5:
        r9 = r10.weight;
        r9 = (r9 > r20 ? 1 : (r9 == r20 ? 0 : -1));
        if (r9 <= 0) goto L_0x01d9;
    L_0x01cb:
        if (r0 == 0) goto L_0x01d0;
    L_0x01cd:
        r10 = r24;
        goto L_0x01d2;
    L_0x01d0:
        r2 = r4;
        goto L_0x01cd;
    L_0x01d2:
        r24 = java.lang.Math.max(r10, r2);
    L_0x01d6:
        r10 = r34;
        goto L_0x01e7;
    L_0x01d9:
        r10 = r24;
        if (r0 == 0) goto L_0x01de;
    L_0x01dd:
        r4 = r2;
    L_0x01de:
        r2 = r23;
        r23 = java.lang.Math.max(r2, r4);
        r24 = r10;
        goto L_0x01d6;
    L_0x01e7:
        r0 = r7.getChildrenSkipCount(r3, r10);
        r0 = r0 + r10;
        r21 = r1;
        r26 = r5;
        r27 = r6;
        r1 = r0;
        r0 = r31;
    L_0x01f5:
        r1 = r1 + 1;
        r6 = r29;
        r3 = r32;
        r4 = r36;
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = -1;
        r9 = r46;
        r10 = 0;
        goto L_0x0061;
    L_0x0205:
        r32 = r3;
        r36 = r4;
        r1 = r21;
        r2 = r23;
        r10 = r24;
        r6 = r26;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r30 = -2;
        r3 = r7.mTotalLength;
        if (r3 <= 0) goto L_0x0226;
    L_0x0219:
        r3 = r7.hasDividerBeforeChildAt(r11);
        if (r3 == 0) goto L_0x0226;
    L_0x021f:
        r3 = r7.mTotalLength;
        r4 = r7.mDividerWidth;
        r3 = r3 + r4;
        r7.mTotalLength = r3;
    L_0x0226:
        r3 = r15[r18];
        r4 = -1;
        if (r3 != r4) goto L_0x023c;
    L_0x022b:
        r3 = 0;
        r5 = r15[r3];
        if (r5 != r4) goto L_0x023c;
    L_0x0230:
        r3 = r15[r17];
        if (r3 != r4) goto L_0x023c;
    L_0x0234:
        r3 = r15[r16];
        if (r3 == r4) goto L_0x0239;
    L_0x0238:
        goto L_0x023c;
    L_0x0239:
        r38 = r6;
        goto L_0x026f;
    L_0x023c:
        r3 = r15[r16];
        r4 = 0;
        r5 = r15[r4];
        r9 = r15[r18];
        r4 = r15[r17];
        r4 = java.lang.Math.max(r9, r4);
        r4 = java.lang.Math.max(r5, r4);
        r3 = java.lang.Math.max(r3, r4);
        r4 = r29[r16];
        r5 = 0;
        r9 = r29[r5];
        r5 = r29[r18];
        r38 = r6;
        r6 = r29[r17];
        r5 = java.lang.Math.max(r5, r6);
        r5 = java.lang.Math.max(r9, r5);
        r4 = java.lang.Math.max(r4, r5);
        r3 = r3 + r4;
        r21 = java.lang.Math.max(r1, r3);
        r1 = r21;
    L_0x026f:
        if (r32 == 0) goto L_0x02d2;
    L_0x0271:
        r3 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r12 == r3) goto L_0x0277;
    L_0x0275:
        if (r12 != 0) goto L_0x02d2;
    L_0x0277:
        r3 = 0;
        r7.mTotalLength = r3;
        r3 = 0;
    L_0x027b:
        if (r3 >= r11) goto L_0x02d2;
    L_0x027d:
        r4 = r7.getVirtualChildAt(r3);
        if (r4 != 0) goto L_0x028d;
    L_0x0283:
        r4 = r7.mTotalLength;
        r5 = r7.measureNullChild(r3);
        r4 = r4 + r5;
        r7.mTotalLength = r4;
        goto L_0x029a;
    L_0x028d:
        r5 = r4.getVisibility();
        r6 = 8;
        if (r5 != r6) goto L_0x029d;
    L_0x0295:
        r4 = r7.getChildrenSkipCount(r4, r3);
        r3 = r3 + r4;
    L_0x029a:
        r39 = r1;
        goto L_0x02cd;
    L_0x029d:
        r5 = r4.getLayoutParams();
        r5 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r5;
        if (r19 == 0) goto L_0x02b6;
    L_0x02a5:
        r6 = r7.mTotalLength;
        r9 = r5.leftMargin;
        r9 = r9 + r14;
        r5 = r5.rightMargin;
        r9 = r9 + r5;
        r4 = r7.getNextLocationOffset(r4);
        r9 = r9 + r4;
        r6 = r6 + r9;
        r7.mTotalLength = r6;
        goto L_0x029a;
    L_0x02b6:
        r6 = r7.mTotalLength;
        r9 = r6 + r14;
        r39 = r1;
        r1 = r5.leftMargin;
        r9 = r9 + r1;
        r1 = r5.rightMargin;
        r9 = r9 + r1;
        r1 = r7.getNextLocationOffset(r4);
        r9 = r9 + r1;
        r1 = java.lang.Math.max(r6, r9);
        r7.mTotalLength = r1;
    L_0x02cd:
        r3 = r3 + 1;
        r1 = r39;
        goto L_0x027b;
    L_0x02d2:
        r39 = r1;
        r1 = r7.mTotalLength;
        r3 = r44.getPaddingLeft();
        r4 = r44.getPaddingRight();
        r3 = r3 + r4;
        r1 = r1 + r3;
        r7.mTotalLength = r1;
        r1 = r7.mTotalLength;
        r3 = r44.getSuggestedMinimumWidth();
        r1 = java.lang.Math.max(r1, r3);
        r3 = 0;
        r1 = android.view.View.resolveSizeAndState(r1, r8, r3);
        r3 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
        r3 = r3 & r1;
        r4 = r7.mTotalLength;
        r3 = r3 - r4;
        if (r22 != 0) goto L_0x0343;
    L_0x02fa:
        if (r3 == 0) goto L_0x0301;
    L_0x02fc:
        r5 = (r0 > r20 ? 1 : (r0 == r20 ? 0 : -1));
        if (r5 <= 0) goto L_0x0301;
    L_0x0300:
        goto L_0x0343;
    L_0x0301:
        r0 = java.lang.Math.max(r2, r10);
        if (r32 == 0) goto L_0x033d;
    L_0x0307:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 == r2) goto L_0x033d;
    L_0x030b:
        r2 = 0;
    L_0x030c:
        if (r2 >= r11) goto L_0x033d;
    L_0x030e:
        r3 = r7.getVirtualChildAt(r2);
        if (r3 == 0) goto L_0x033a;
    L_0x0314:
        r5 = r3.getVisibility();
        r6 = 8;
        if (r5 != r6) goto L_0x031d;
    L_0x031c:
        goto L_0x033a;
    L_0x031d:
        r5 = r3.getLayoutParams();
        r5 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r5;
        r5 = r5.weight;
        r5 = (r5 > r20 ? 1 : (r5 == r20 ? 0 : -1));
        if (r5 <= 0) goto L_0x033a;
    L_0x0329:
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = android.view.View.MeasureSpec.makeMeasureSpec(r14, r5);
        r9 = r3.getMeasuredHeight();
        r9 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r5);
        r3.measure(r6, r9);
    L_0x033a:
        r2 = r2 + 1;
        goto L_0x030c;
    L_0x033d:
        r42 = r11;
        r3 = r46;
        goto L_0x04eb;
    L_0x0343:
        r5 = r7.mWeightSum;
        r5 = (r5 > r20 ? 1 : (r5 == r20 ? 0 : -1));
        if (r5 <= 0) goto L_0x034b;
    L_0x0349:
        r0 = r7.mWeightSum;
    L_0x034b:
        r5 = -1;
        r15[r16] = r5;
        r15[r17] = r5;
        r15[r18] = r5;
        r6 = 0;
        r15[r6] = r5;
        r29[r16] = r5;
        r29[r17] = r5;
        r29[r18] = r5;
        r29[r6] = r5;
        r7.mTotalLength = r6;
        r10 = r2;
        r9 = r38;
        r6 = -1;
        r2 = r0;
        r0 = 0;
    L_0x0365:
        if (r0 >= r11) goto L_0x0491;
    L_0x0367:
        r14 = r7.getVirtualChildAt(r0);
        if (r14 == 0) goto L_0x0480;
    L_0x036d:
        r5 = r14.getVisibility();
        r4 = 8;
        if (r5 != r4) goto L_0x0377;
    L_0x0375:
        goto L_0x0480;
    L_0x0377:
        r5 = r14.getLayoutParams();
        r5 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r5;
        r4 = r5.weight;
        r21 = (r4 > r20 ? 1 : (r4 == r20 ? 0 : -1));
        if (r21 <= 0) goto L_0x03e0;
    L_0x0383:
        r8 = (float) r3;
        r8 = r8 * r4;
        r8 = r8 / r2;
        r8 = (int) r8;
        r2 = r2 - r4;
        r3 = r3 - r8;
        r4 = r44.getPaddingTop();
        r21 = r44.getPaddingBottom();
        r4 = r4 + r21;
        r40 = r2;
        r2 = r5.topMargin;
        r4 = r4 + r2;
        r2 = r5.bottomMargin;
        r4 = r4 + r2;
        r2 = r5.height;
        r41 = r3;
        r42 = r11;
        r3 = r46;
        r11 = -1;
        r2 = android.support.p004v7.widget.LinearLayoutCompat.getChildMeasureSpec(r3, r4, r2);
        r4 = r5.width;
        if (r4 != 0) goto L_0x03be;
    L_0x03ad:
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 == r4) goto L_0x03b2;
    L_0x03b1:
        goto L_0x03c0;
    L_0x03b2:
        if (r8 <= 0) goto L_0x03b5;
    L_0x03b4:
        goto L_0x03b6;
    L_0x03b5:
        r8 = 0;
    L_0x03b6:
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r4);
        r14.measure(r8, r2);
        goto L_0x03d0;
    L_0x03be:
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
    L_0x03c0:
        r21 = r14.getMeasuredWidth();
        r8 = r21 + r8;
        if (r8 >= 0) goto L_0x03c9;
    L_0x03c8:
        r8 = 0;
    L_0x03c9:
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r4);
        r14.measure(r8, r2);
    L_0x03d0:
        r2 = r14.getMeasuredState();
        r4 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r2 = r2 & r4;
        r9 = android.view.View.combineMeasuredStates(r9, r2);
        r2 = r40;
        r4 = r41;
        goto L_0x03e6;
    L_0x03e0:
        r4 = r3;
        r42 = r11;
        r3 = r46;
        r11 = -1;
    L_0x03e6:
        if (r19 == 0) goto L_0x0405;
    L_0x03e8:
        r8 = r7.mTotalLength;
        r21 = r14.getMeasuredWidth();
        r11 = r5.leftMargin;
        r21 = r21 + r11;
        r11 = r5.rightMargin;
        r21 = r21 + r11;
        r11 = r7.getNextLocationOffset(r14);
        r21 = r21 + r11;
        r8 = r8 + r21;
        r7.mTotalLength = r8;
        r43 = r2;
    L_0x0402:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x0420;
    L_0x0405:
        r8 = r7.mTotalLength;
        r11 = r14.getMeasuredWidth();
        r11 = r11 + r8;
        r43 = r2;
        r2 = r5.leftMargin;
        r11 = r11 + r2;
        r2 = r5.rightMargin;
        r11 = r11 + r2;
        r2 = r7.getNextLocationOffset(r14);
        r11 = r11 + r2;
        r2 = java.lang.Math.max(r8, r11);
        r7.mTotalLength = r2;
        goto L_0x0402;
    L_0x0420:
        if (r13 == r2) goto L_0x0429;
    L_0x0422:
        r2 = r5.height;
        r8 = -1;
        if (r2 != r8) goto L_0x0429;
    L_0x0427:
        r2 = 1;
        goto L_0x042a;
    L_0x0429:
        r2 = 0;
    L_0x042a:
        r8 = r5.topMargin;
        r11 = r5.bottomMargin;
        r8 = r8 + r11;
        r11 = r14.getMeasuredHeight();
        r11 = r11 + r8;
        r6 = java.lang.Math.max(r6, r11);
        if (r2 == 0) goto L_0x043b;
    L_0x043a:
        goto L_0x043c;
    L_0x043b:
        r8 = r11;
    L_0x043c:
        r2 = java.lang.Math.max(r10, r8);
        if (r27 == 0) goto L_0x0449;
    L_0x0442:
        r8 = r5.height;
        r10 = -1;
        if (r8 != r10) goto L_0x044a;
    L_0x0447:
        r8 = 1;
        goto L_0x044b;
    L_0x0449:
        r10 = -1;
    L_0x044a:
        r8 = 0;
    L_0x044b:
        if (r36 == 0) goto L_0x0478;
    L_0x044d:
        r14 = r14.getBaseline();
        if (r14 == r10) goto L_0x0478;
    L_0x0453:
        r10 = r5.gravity;
        if (r10 >= 0) goto L_0x045a;
    L_0x0457:
        r5 = r7.mGravity;
        goto L_0x045c;
    L_0x045a:
        r5 = r5.gravity;
    L_0x045c:
        r5 = r5 & 112;
        r21 = 4;
        r5 = r5 >> 4;
        r5 = r5 & -2;
        r5 = r5 >> 1;
        r10 = r15[r5];
        r10 = java.lang.Math.max(r10, r14);
        r15[r5] = r10;
        r10 = r29[r5];
        r11 = r11 - r14;
        r10 = java.lang.Math.max(r10, r11);
        r29[r5] = r10;
        goto L_0x047a;
    L_0x0478:
        r21 = 4;
    L_0x047a:
        r10 = r2;
        r27 = r8;
        r2 = r43;
        goto L_0x0487;
    L_0x0480:
        r4 = r3;
        r42 = r11;
        r3 = r46;
        r21 = 4;
    L_0x0487:
        r0 = r0 + 1;
        r3 = r4;
        r11 = r42;
        r5 = -1;
        r8 = r45;
        goto L_0x0365;
    L_0x0491:
        r42 = r11;
        r3 = r46;
        r0 = r7.mTotalLength;
        r2 = r44.getPaddingLeft();
        r4 = r44.getPaddingRight();
        r2 = r2 + r4;
        r0 = r0 + r2;
        r7.mTotalLength = r0;
        r0 = r15[r18];
        r2 = -1;
        if (r0 != r2) goto L_0x04b8;
    L_0x04a8:
        r0 = 0;
        r4 = r15[r0];
        if (r4 != r2) goto L_0x04b8;
    L_0x04ad:
        r0 = r15[r17];
        if (r0 != r2) goto L_0x04b8;
    L_0x04b1:
        r0 = r15[r16];
        if (r0 == r2) goto L_0x04b6;
    L_0x04b5:
        goto L_0x04b8;
    L_0x04b6:
        r0 = r6;
        goto L_0x04e6;
    L_0x04b8:
        r0 = r15[r16];
        r2 = 0;
        r4 = r15[r2];
        r5 = r15[r18];
        r8 = r15[r17];
        r5 = java.lang.Math.max(r5, r8);
        r4 = java.lang.Math.max(r4, r5);
        r0 = java.lang.Math.max(r0, r4);
        r4 = r29[r16];
        r2 = r29[r2];
        r5 = r29[r18];
        r8 = r29[r17];
        r5 = java.lang.Math.max(r5, r8);
        r2 = java.lang.Math.max(r2, r5);
        r2 = java.lang.Math.max(r4, r2);
        r0 = r0 + r2;
        r0 = java.lang.Math.max(r6, r0);
    L_0x04e6:
        r39 = r0;
        r38 = r9;
        r0 = r10;
    L_0x04eb:
        if (r27 != 0) goto L_0x04f3;
    L_0x04ed:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 == r2) goto L_0x04f3;
    L_0x04f1:
        r39 = r0;
    L_0x04f3:
        r0 = r44.getPaddingTop();
        r2 = r44.getPaddingBottom();
        r0 = r0 + r2;
        r0 = r39 + r0;
        r2 = r44.getSuggestedMinimumHeight();
        r0 = java.lang.Math.max(r0, r2);
        r2 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r2 = r38 & r2;
        r1 = r1 | r2;
        r2 = r38 << 16;
        r0 = android.view.View.resolveSizeAndState(r0, r3, r2);
        r7.setMeasuredDimension(r1, r0);
        if (r28 == 0) goto L_0x051d;
    L_0x0516:
        r1 = r42;
        r0 = r45;
        r7.forceUniformHeight(r1, r0);
    L_0x051d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.LinearLayoutCompat.measureHorizontal(int, int):void");
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int paddingTop;
        int paddingLeft = getPaddingLeft();
        int i5 = i3 - i;
        int paddingRight = i5 - getPaddingRight();
        int paddingRight2 = (i5 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        i5 = this.mGravity & 112;
        int i6 = this.mGravity & 8388615;
        if (i5 == 16) {
            paddingTop = (((i4 - i2) - this.mTotalLength) / 2) + getPaddingTop();
        } else if (i5 != 80) {
            paddingTop = getPaddingTop();
        } else {
            paddingTop = ((getPaddingTop() + i4) - i2) - this.mTotalLength;
        }
        int i7 = 0;
        while (i7 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i7);
            if (virtualChildAt == null) {
                paddingTop += measureNullChild(i7);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i8 = layoutParams.gravity;
                if (i8 < 0) {
                    i8 = i6;
                }
                i8 = GravityCompat.getAbsoluteGravity(i8, ViewCompat.getLayoutDirection(this)) & 7;
                if (i8 == 1) {
                    i8 = ((((paddingRight2 - measuredWidth) / 2) + paddingLeft) + layoutParams.leftMargin) - layoutParams.rightMargin;
                } else if (i8 != 5) {
                    i8 = layoutParams.leftMargin + paddingLeft;
                } else {
                    i8 = (paddingRight - measuredWidth) - layoutParams.rightMargin;
                }
                i5 = i8;
                if (hasDividerBeforeChildAt(i7)) {
                    paddingTop += this.mDividerHeight;
                }
                int i9 = paddingTop + layoutParams.topMargin;
                LayoutParams layoutParams2 = layoutParams;
                setChildFrame(virtualChildAt, i5, i9 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i7 += getChildrenSkipCount(virtualChildAt, i7);
                paddingTop = i9 + ((measuredHeight + layoutParams2.bottomMargin) + getNextLocationOffset(virtualChildAt));
            }
            i7++;
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00f6  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x010a  */
    public void layoutHorizontal(int r28, int r29, int r30, int r31) {
        /*
        r27 = this;
        r6 = r27;
        r2 = android.support.p004v7.widget.ViewUtils.isLayoutRtl(r27);
        r7 = r27.getPaddingTop();
        r3 = r31 - r29;
        r4 = r27.getPaddingBottom();
        r8 = r3 - r4;
        r3 = r3 - r7;
        r4 = r27.getPaddingBottom();
        r9 = r3 - r4;
        r10 = r27.getVirtualChildCount();
        r3 = r6.mGravity;
        r4 = 8388615; // 0x800007 float:1.1754953E-38 double:4.1445265E-317;
        r3 = r3 & r4;
        r4 = r6.mGravity;
        r11 = r4 & 112;
        r12 = r6.mBaselineAligned;
        r13 = r6.mMaxAscent;
        r14 = r6.mMaxDescent;
        r4 = android.support.p001v4.view.ViewCompat.getLayoutDirection(r27);
        r3 = android.support.p001v4.view.GravityCompat.getAbsoluteGravity(r3, r4);
        r15 = 2;
        r5 = 1;
        if (r3 == r5) goto L_0x004e;
    L_0x0039:
        r4 = 5;
        if (r3 == r4) goto L_0x0041;
    L_0x003c:
        r0 = r27.getPaddingLeft();
        goto L_0x0059;
    L_0x0041:
        r3 = r27.getPaddingLeft();
        r3 = r3 + r30;
        r3 = r3 - r28;
        r0 = r6.mTotalLength;
        r0 = r3 - r0;
        goto L_0x0059;
    L_0x004e:
        r3 = r27.getPaddingLeft();
        r0 = r30 - r28;
        r1 = r6.mTotalLength;
        r0 = r0 - r1;
        r0 = r0 / r15;
        r0 = r0 + r3;
    L_0x0059:
        r1 = 0;
        if (r2 == 0) goto L_0x0063;
    L_0x005c:
        r2 = r10 + -1;
        r16 = r2;
        r17 = -1;
        goto L_0x0067;
    L_0x0063:
        r16 = 0;
        r17 = 1;
    L_0x0067:
        r3 = 0;
    L_0x0068:
        if (r3 >= r10) goto L_0x014b;
    L_0x006a:
        r1 = r17 * r3;
        r2 = r16 + r1;
        r1 = r6.getVirtualChildAt(r2);
        if (r1 != 0) goto L_0x0085;
    L_0x0074:
        r1 = r6.measureNullChild(r2);
        r0 = r0 + r1;
    L_0x0079:
        r26 = r7;
        r23 = r10;
        r24 = r11;
        r18 = 1;
        r20 = -1;
        goto L_0x013f;
    L_0x0085:
        r5 = r1.getVisibility();
        r15 = 8;
        if (r5 == r15) goto L_0x013b;
    L_0x008d:
        r15 = r1.getMeasuredWidth();
        r5 = r1.getMeasuredHeight();
        r20 = r1.getLayoutParams();
        r4 = r20;
        r4 = (android.support.p004v7.widget.LinearLayoutCompat.LayoutParams) r4;
        if (r12 == 0) goto L_0x00ad;
    L_0x009f:
        r22 = r3;
        r3 = r4.height;
        r23 = r10;
        r10 = -1;
        if (r3 == r10) goto L_0x00b1;
    L_0x00a8:
        r3 = r1.getBaseline();
        goto L_0x00b2;
    L_0x00ad:
        r22 = r3;
        r23 = r10;
    L_0x00b1:
        r3 = -1;
    L_0x00b2:
        r10 = r4.gravity;
        if (r10 >= 0) goto L_0x00b7;
    L_0x00b6:
        r10 = r11;
    L_0x00b7:
        r10 = r10 & 112;
        r24 = r11;
        r11 = 16;
        if (r10 == r11) goto L_0x00f6;
    L_0x00bf:
        r11 = 48;
        if (r10 == r11) goto L_0x00e3;
    L_0x00c3:
        r11 = 80;
        if (r10 == r11) goto L_0x00cc;
    L_0x00c7:
        r3 = r7;
        r11 = -1;
    L_0x00c9:
        r18 = 1;
        goto L_0x0104;
    L_0x00cc:
        r10 = r8 - r5;
        r11 = r4.bottomMargin;
        r10 = r10 - r11;
        r11 = -1;
        if (r3 == r11) goto L_0x00e1;
    L_0x00d4:
        r20 = r1.getMeasuredHeight();
        r20 = r20 - r3;
        r3 = 2;
        r21 = r14[r3];
        r21 = r21 - r20;
        r10 = r10 - r21;
    L_0x00e1:
        r3 = r10;
        goto L_0x00c9;
    L_0x00e3:
        r11 = -1;
        r10 = r4.topMargin;
        r10 = r10 + r7;
        if (r3 == r11) goto L_0x00f2;
    L_0x00e9:
        r18 = 1;
        r20 = r13[r18];
        r20 = r20 - r3;
        r10 = r10 + r20;
        goto L_0x00f4;
    L_0x00f2:
        r18 = 1;
    L_0x00f4:
        r3 = r10;
        goto L_0x0104;
    L_0x00f6:
        r11 = -1;
        r18 = 1;
        r3 = r9 - r5;
        r10 = 2;
        r3 = r3 / r10;
        r3 = r3 + r7;
        r10 = r4.topMargin;
        r3 = r3 + r10;
        r10 = r4.bottomMargin;
        r3 = r3 - r10;
    L_0x0104:
        r10 = r6.hasDividerBeforeChildAt(r2);
        if (r10 == 0) goto L_0x010d;
    L_0x010a:
        r10 = r6.mDividerWidth;
        r0 = r0 + r10;
    L_0x010d:
        r10 = r4.leftMargin;
        r10 = r10 + r0;
        r0 = r6.getLocationOffset(r1);
        r19 = r10 + r0;
        r0 = r27;
        r25 = r1;
        r11 = r2;
        r2 = r19;
        r19 = r22;
        r26 = r7;
        r20 = -1;
        r7 = r4;
        r4 = r15;
        r0.setChildFrame(r1, r2, r3, r4, r5);
        r0 = r7.rightMargin;
        r15 = r15 + r0;
        r0 = r25;
        r1 = r6.getNextLocationOffset(r0);
        r15 = r15 + r1;
        r10 = r10 + r15;
        r0 = r6.getChildrenSkipCount(r0, r11);
        r3 = r19 + r0;
        r0 = r10;
        goto L_0x013f;
    L_0x013b:
        r19 = r3;
        goto L_0x0079;
    L_0x013f:
        r3 = r3 + 1;
        r10 = r23;
        r11 = r24;
        r7 = r26;
        r5 = 1;
        r15 = 2;
        goto L_0x0068;
    L_0x014b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p004v7.widget.LinearLayoutCompat.layoutHorizontal(int, int, int, int):void");
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= 8388611;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int i) {
        i &= 8388615;
        if ((8388615 & this.mGravity) != i) {
            this.mGravity = i | (this.mGravity & -8388616);
            requestLayout();
        }
    }

    public void setVerticalGravity(int i) {
        i &= 112;
        if ((this.mGravity & 112) != i) {
            this.mGravity = i | (this.mGravity & -113);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -2);
        }
        return this.mOrientation == 1 ? new LayoutParams(-1, -2) : null;
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
    }
}
