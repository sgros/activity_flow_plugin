package androidx.appcompat.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.appcompat.R$layout;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;

public class ActionBarContextView extends AbsActionBarView {
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:66:0x012b in {6, 7, 10, 15, 24, 25, 27, 30, 31, 32, 33, 38, 39, 42, 45, 46, 49, 50, 57, 58, 59, 60, 61, 63, 65} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    protected void onMeasure(int r11, int r12) {
        /*
        r10 = this;
        r0 = android.view.View.MeasureSpec.getMode(r11);
        r1 = " can only be used ";
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r0 != r2) goto L_0x010b;
        r0 = android.view.View.MeasureSpec.getMode(r12);
        if (r0 == 0) goto L_0x00eb;
        r11 = android.view.View.MeasureSpec.getSize(r11);
        r0 = r10.mContentHeight;
        if (r0 <= 0) goto L_0x0019;
        goto L_0x001d;
        r0 = android.view.View.MeasureSpec.getSize(r12);
        r12 = r10.getPaddingTop();
        r1 = r10.getPaddingBottom();
        r12 = r12 + r1;
        r1 = r10.getPaddingLeft();
        r1 = r11 - r1;
        r3 = r10.getPaddingRight();
        r1 = r1 - r3;
        r3 = r0 - r12;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r5 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r4);
        r6 = r10.mClose;
        r7 = 0;
        if (r6 == 0) goto L_0x0050;
        r1 = r10.measureChildView(r6, r1, r5, r7);
        r6 = r10.mClose;
        r6 = r6.getLayoutParams();
        r6 = (android.view.ViewGroup.MarginLayoutParams) r6;
        r8 = r6.leftMargin;
        r6 = r6.rightMargin;
        r8 = r8 + r6;
        r1 = r1 - r8;
        r6 = r10.mMenuView;
        if (r6 == 0) goto L_0x0060;
        r6 = r6.getParent();
        if (r6 != r10) goto L_0x0060;
        r6 = r10.mMenuView;
        r1 = r10.measureChildView(r6, r1, r5, r7);
        r6 = r10.mTitleLayout;
        if (r6 == 0) goto L_0x0093;
        r8 = r10.mCustomView;
        if (r8 != 0) goto L_0x0093;
        r8 = r10.mTitleOptional;
        if (r8 == 0) goto L_0x008f;
        r6 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r7);
        r8 = r10.mTitleLayout;
        r8.measure(r6, r5);
        r5 = r10.mTitleLayout;
        r5 = r5.getMeasuredWidth();
        if (r5 > r1) goto L_0x007f;
        r6 = 1;
        goto L_0x0080;
        r6 = 0;
        if (r6 == 0) goto L_0x0083;
        r1 = r1 - r5;
        r5 = r10.mTitleLayout;
        if (r6 == 0) goto L_0x0089;
        r6 = 0;
        goto L_0x008b;
        r6 = 8;
        r5.setVisibility(r6);
        goto L_0x0093;
        r1 = r10.measureChildView(r6, r1, r5, r7);
        r5 = r10.mCustomView;
        if (r5 == 0) goto L_0x00c9;
        r5 = r5.getLayoutParams();
        r6 = r5.width;
        r8 = -2;
        if (r6 == r8) goto L_0x00a3;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x00a5;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r9 = r5.width;
        if (r9 < 0) goto L_0x00ad;
        r1 = java.lang.Math.min(r9, r1);
        r9 = r5.height;
        if (r9 == r8) goto L_0x00b2;
        goto L_0x00b4;
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4 = r5.height;
        if (r4 < 0) goto L_0x00bc;
        r3 = java.lang.Math.min(r4, r3);
        r4 = r10.mCustomView;
        r1 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r6);
        r2 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r2);
        r4.measure(r1, r2);
        r1 = r10.mContentHeight;
        if (r1 > 0) goto L_0x00e7;
        r0 = r10.getChildCount();
        r1 = 0;
        if (r7 >= r0) goto L_0x00e3;
        r2 = r10.getChildAt(r7);
        r2 = r2.getMeasuredHeight();
        r2 = r2 + r12;
        if (r2 <= r1) goto L_0x00e0;
        r1 = r2;
        r7 = r7 + 1;
        goto L_0x00d2;
        r10.setMeasuredDimension(r11, r1);
        goto L_0x00ea;
        r10.setMeasuredDimension(r11, r0);
        return;
        r11 = new java.lang.IllegalStateException;
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r0 = androidx.appcompat.widget.ActionBarContextView.class;
        r0 = r0.getSimpleName();
        r12.append(r0);
        r12.append(r1);
        r0 = "with android:layout_height=\"wrap_content\"";
        r12.append(r0);
        r12 = r12.toString();
        r11.<init>(r12);
        throw r11;
        r11 = new java.lang.IllegalStateException;
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r0 = androidx.appcompat.widget.ActionBarContextView.class;
        r0 = r0.getSimpleName();
        r12.append(r0);
        r12.append(r1);
        r0 = "with android:layout_width=\"match_parent\" (or fill_parent)";
        r12.append(r0);
        r12 = r12.toString();
        r11.<init>(r12);
        throw r11;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ActionBarContextView.onMeasure(int, int):void");
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    public /* bridge */ /* synthetic */ boolean onHoverEvent(MotionEvent motionEvent) {
        return super.onHoverEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.actionModeStyle);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.ActionMode, i, 0);
        ViewCompat.setBackground(this, obtainStyledAttributes.getDrawable(R$styleable.ActionMode_background));
        this.mTitleStyleRes = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = obtainStyledAttributes.getLayoutDimension(R$styleable.ActionMode_height, 0);
        this.mCloseItemLayout = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_closeItemLayout, R$layout.abc_action_mode_close_item_material);
        obtainStyledAttributes.recycle();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    public void setContentHeight(int i) {
        this.mContentHeight = i;
    }

    public void setCustomView(View view) {
        View view2 = this.mCustomView;
        if (view2 != null) {
            removeView(view2);
        }
        this.mCustomView = view;
        if (view != null) {
            LinearLayout linearLayout = this.mTitleLayout;
            if (linearLayout != null) {
                removeView(linearLayout);
                this.mTitleLayout = null;
            }
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        initTitle();
    }

    public void setSubtitle(CharSequence charSequence) {
        this.mSubtitle = charSequence;
        initTitle();
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    private void initTitle() {
        if (this.mTitleLayout == null) {
            LayoutInflater.from(getContext()).inflate(R$layout.abc_action_bar_title_item, this);
            this.mTitleLayout = (LinearLayout) getChildAt(getChildCount() - 1);
            this.mTitleView = (TextView) this.mTitleLayout.findViewById(R$id.action_bar_title);
            this.mSubtitleView = (TextView) this.mTitleLayout.findViewById(R$id.action_bar_subtitle);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(getContext(), this.mTitleStyleRes);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(getContext(), this.mSubtitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mSubtitleView.setText(this.mSubtitle);
        int isEmpty = TextUtils.isEmpty(this.mTitle) ^ 1;
        int isEmpty2 = TextUtils.isEmpty(this.mSubtitle) ^ 1;
        int i = 0;
        this.mSubtitleView.setVisibility(isEmpty2 != 0 ? 0 : 8);
        LinearLayout linearLayout = this.mTitleLayout;
        if (isEmpty == 0 && isEmpty2 == 0) {
            i = 8;
        }
        linearLayout.setVisibility(i);
        if (this.mTitleLayout.getParent() == null) {
            addView(this.mTitleLayout);
        }
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new MarginLayoutParams(getContext(), attributeSet);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        z = ViewUtils.isLayoutRtl(this);
        int paddingRight = z ? (i3 - i) - getPaddingRight() : getPaddingLeft();
        int paddingTop = getPaddingTop();
        i2 = ((i4 - i2) - getPaddingTop()) - getPaddingBottom();
        View view = this.mClose;
        if (view == null || view.getVisibility() == 8) {
            i4 = paddingRight;
        } else {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mClose.getLayoutParams();
            int i5 = z ? marginLayoutParams.rightMargin : marginLayoutParams.leftMargin;
            i4 = z ? marginLayoutParams.leftMargin : marginLayoutParams.rightMargin;
            int next = AbsActionBarView.next(paddingRight, i5, z);
            i4 = AbsActionBarView.next(next + positionChild(this.mClose, next, paddingTop, i2, z), i4, z);
        }
        LinearLayout linearLayout = this.mTitleLayout;
        if (!(linearLayout == null || this.mCustomView != null || linearLayout.getVisibility() == 8)) {
            i4 += positionChild(this.mTitleLayout, i4, paddingTop, i2, z);
        }
        int i6 = i4;
        View view2 = this.mCustomView;
        if (view2 != null) {
            positionChild(view2, i6, paddingTop, i2, z);
        }
        int paddingLeft = z ? getPaddingLeft() : (i3 - i) - getPaddingRight();
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            positionChild(actionMenuView, paddingLeft, paddingTop, i2, z ^ 1);
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 32) {
            accessibilityEvent.setSource(this);
            accessibilityEvent.setClassName(ActionBarContextView.class.getName());
            accessibilityEvent.setPackageName(getContext().getPackageName());
            accessibilityEvent.setContentDescription(this.mTitle);
            return;
        }
        super.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    public void setTitleOptional(boolean z) {
        if (z != this.mTitleOptional) {
            requestLayout();
        }
        this.mTitleOptional = z;
    }
}
