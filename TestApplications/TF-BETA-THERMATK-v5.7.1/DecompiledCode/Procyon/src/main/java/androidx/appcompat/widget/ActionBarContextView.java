// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.MotionEvent;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.text.TextUtils;
import androidx.appcompat.R$id;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.appcompat.R$layout;
import androidx.core.view.ViewCompat;
import androidx.appcompat.R$styleable;
import androidx.appcompat.R$attr;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class ActionBarContextView extends AbsActionBarView
{
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
    
    public ActionBarContextView(final Context context) {
        this(context, null);
    }
    
    public ActionBarContextView(final Context context, final AttributeSet set) {
        this(context, set, R$attr.actionModeStyle);
    }
    
    public ActionBarContextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R$styleable.ActionMode, n, 0);
        ViewCompat.setBackground((View)this, obtainStyledAttributes.getDrawable(R$styleable.ActionMode_background));
        this.mTitleStyleRes = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_subtitleTextStyle, 0);
        super.mContentHeight = obtainStyledAttributes.getLayoutDimension(R$styleable.ActionMode_height, 0);
        this.mCloseItemLayout = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_closeItemLayout, R$layout.abc_action_mode_close_item_material);
        obtainStyledAttributes.recycle();
    }
    
    private void initTitle() {
        if (this.mTitleLayout == null) {
            LayoutInflater.from(this.getContext()).inflate(R$layout.abc_action_bar_title_item, (ViewGroup)this);
            this.mTitleLayout = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
            this.mTitleView = (TextView)this.mTitleLayout.findViewById(R$id.action_bar_title);
            this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R$id.action_bar_subtitle);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mSubtitleView.setText(this.mSubtitle);
        final boolean empty = TextUtils.isEmpty(this.mTitle);
        final boolean b = TextUtils.isEmpty(this.mSubtitle) ^ true;
        final TextView mSubtitleView = this.mSubtitleView;
        final int n = 0;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        mSubtitleView.setVisibility(visibility);
        final LinearLayout mTitleLayout = this.mTitleLayout;
        int visibility2 = n;
        if (!(empty ^ true)) {
            if (b) {
                visibility2 = n;
            }
            else {
                visibility2 = 8;
            }
        }
        mTitleLayout.setVisibility(visibility2);
        if (this.mTitleLayout.getParent() == null) {
            this.addView((View)this.mTitleLayout);
        }
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return (ViewGroup$LayoutParams)new ViewGroup$MarginLayoutParams(-1, -2);
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return (ViewGroup$LayoutParams)new ViewGroup$MarginLayoutParams(this.getContext(), set);
    }
    
    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }
    
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final ActionMenuPresenter mActionMenuPresenter = super.mActionMenuPresenter;
        if (mActionMenuPresenter != null) {
            mActionMenuPresenter.hideOverflowMenu();
            super.mActionMenuPresenter.hideSubMenus();
        }
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 32) {
            accessibilityEvent.setSource((View)this);
            accessibilityEvent.setClassName((CharSequence)ActionBarContextView.class.getName());
            accessibilityEvent.setPackageName((CharSequence)this.getContext().getPackageName());
            accessibilityEvent.setContentDescription(this.mTitle);
        }
        else {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
        }
    }
    
    protected void onLayout(final boolean b, int paddingLeft, int n, final int n2, int n3) {
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        int paddingLeft2;
        if (layoutRtl) {
            paddingLeft2 = n2 - paddingLeft - this.getPaddingRight();
        }
        else {
            paddingLeft2 = this.getPaddingLeft();
        }
        final int paddingTop = this.getPaddingTop();
        final int n4 = n3 - n - this.getPaddingTop() - this.getPaddingBottom();
        final View mClose = this.mClose;
        if (mClose != null && mClose.getVisibility() != 8) {
            final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)this.mClose.getLayoutParams();
            if (layoutRtl) {
                n = viewGroup$MarginLayoutParams.rightMargin;
            }
            else {
                n = viewGroup$MarginLayoutParams.leftMargin;
            }
            if (layoutRtl) {
                n3 = viewGroup$MarginLayoutParams.leftMargin;
            }
            else {
                n3 = viewGroup$MarginLayoutParams.rightMargin;
            }
            n = AbsActionBarView.next(paddingLeft2, n, layoutRtl);
            n = AbsActionBarView.next(n + this.positionChild(this.mClose, n, paddingTop, n4, layoutRtl), n3, layoutRtl);
        }
        else {
            n = paddingLeft2;
        }
        final LinearLayout mTitleLayout = this.mTitleLayout;
        n3 = n;
        if (mTitleLayout != null) {
            n3 = n;
            if (this.mCustomView == null) {
                n3 = n;
                if (mTitleLayout.getVisibility() != 8) {
                    n3 = n + this.positionChild((View)this.mTitleLayout, n, paddingTop, n4, layoutRtl);
                }
            }
        }
        final View mCustomView = this.mCustomView;
        if (mCustomView != null) {
            this.positionChild(mCustomView, n3, paddingTop, n4, layoutRtl);
        }
        if (layoutRtl) {
            paddingLeft = this.getPaddingLeft();
        }
        else {
            paddingLeft = n2 - paddingLeft - this.getPaddingRight();
        }
        final ActionMenuView mMenuView = super.mMenuView;
        if (mMenuView != null) {
            this.positionChild((View)mMenuView, paddingLeft, paddingTop, n4, layoutRtl ^ true);
        }
    }
    
    protected void onMeasure(int i, int b) {
        final int mode = View$MeasureSpec.getMode(i);
        final int n = 1073741824;
        if (mode != 1073741824) {
            final StringBuilder sb = new StringBuilder();
            sb.append(ActionBarContextView.class.getSimpleName());
            sb.append(" can only be used ");
            sb.append("with android:layout_width=\"match_parent\" (or fill_parent)");
            throw new IllegalStateException(sb.toString());
        }
        if (View$MeasureSpec.getMode(b) != 0) {
            final int size = View$MeasureSpec.getSize(i);
            int n2 = super.mContentHeight;
            if (n2 <= 0) {
                n2 = View$MeasureSpec.getSize(b);
            }
            final int n3 = this.getPaddingTop() + this.getPaddingBottom();
            i = size - this.getPaddingLeft() - this.getPaddingRight();
            final int b2 = n2 - n3;
            final int measureSpec = View$MeasureSpec.makeMeasureSpec(b2, Integer.MIN_VALUE);
            final View mClose = this.mClose;
            final int n4 = 0;
            b = i;
            if (mClose != null) {
                i = this.measureChildView(mClose, i, measureSpec, 0);
                final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)this.mClose.getLayoutParams();
                b = i - (viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin);
            }
            final ActionMenuView mMenuView = super.mMenuView;
            i = b;
            if (mMenuView != null) {
                i = b;
                if (mMenuView.getParent() == this) {
                    i = this.measureChildView((View)super.mMenuView, b, measureSpec, 0);
                }
            }
            final LinearLayout mTitleLayout = this.mTitleLayout;
            b = i;
            if (mTitleLayout != null) {
                b = i;
                if (this.mCustomView == null) {
                    if (this.mTitleOptional) {
                        b = View$MeasureSpec.makeMeasureSpec(0, 0);
                        this.mTitleLayout.measure(b, measureSpec);
                        final int measuredWidth = this.mTitleLayout.getMeasuredWidth();
                        final boolean b3 = measuredWidth <= i;
                        b = i;
                        if (b3) {
                            b = i - measuredWidth;
                        }
                        final LinearLayout mTitleLayout2 = this.mTitleLayout;
                        if (b3) {
                            i = 0;
                        }
                        else {
                            i = 8;
                        }
                        mTitleLayout2.setVisibility(i);
                    }
                    else {
                        b = this.measureChildView((View)mTitleLayout, i, measureSpec, 0);
                    }
                }
            }
            final View mCustomView = this.mCustomView;
            if (mCustomView != null) {
                final ViewGroup$LayoutParams layoutParams = mCustomView.getLayoutParams();
                if (layoutParams.width != -2) {
                    i = 1073741824;
                }
                else {
                    i = Integer.MIN_VALUE;
                }
                final int width = layoutParams.width;
                int min = b;
                if (width >= 0) {
                    min = Math.min(width, b);
                }
                if (layoutParams.height != -2) {
                    b = n;
                }
                else {
                    b = Integer.MIN_VALUE;
                }
                final int height = layoutParams.height;
                int min2 = b2;
                if (height >= 0) {
                    min2 = Math.min(height, b2);
                }
                this.mCustomView.measure(View$MeasureSpec.makeMeasureSpec(min, i), View$MeasureSpec.makeMeasureSpec(min2, b));
            }
            if (super.mContentHeight <= 0) {
                final int childCount = this.getChildCount();
                b = 0;
                int n5;
                int n6;
                for (i = n4; i < childCount; ++i, b = n6) {
                    n5 = this.getChildAt(i).getMeasuredHeight() + n3;
                    if (n5 > (n6 = b)) {
                        n6 = n5;
                    }
                }
                this.setMeasuredDimension(size, b);
            }
            else {
                this.setMeasuredDimension(size, n2);
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(ActionBarContextView.class.getSimpleName());
        sb2.append(" can only be used ");
        sb2.append("with android:layout_height=\"wrap_content\"");
        throw new IllegalStateException(sb2.toString());
    }
    
    @Override
    public void setContentHeight(final int mContentHeight) {
        super.mContentHeight = mContentHeight;
    }
    
    public void setCustomView(final View mCustomView) {
        final View mCustomView2 = this.mCustomView;
        if (mCustomView2 != null) {
            this.removeView(mCustomView2);
        }
        if ((this.mCustomView = mCustomView) != null) {
            final LinearLayout mTitleLayout = this.mTitleLayout;
            if (mTitleLayout != null) {
                this.removeView((View)mTitleLayout);
                this.mTitleLayout = null;
            }
        }
        if (mCustomView != null) {
            this.addView(mCustomView);
        }
        this.requestLayout();
    }
    
    public void setSubtitle(final CharSequence mSubtitle) {
        this.mSubtitle = mSubtitle;
        this.initTitle();
    }
    
    public void setTitle(final CharSequence mTitle) {
        this.mTitle = mTitle;
        this.initTitle();
    }
    
    public void setTitleOptional(final boolean mTitleOptional) {
        if (mTitleOptional != this.mTitleOptional) {
            this.requestLayout();
        }
        this.mTitleOptional = mTitleOptional;
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
