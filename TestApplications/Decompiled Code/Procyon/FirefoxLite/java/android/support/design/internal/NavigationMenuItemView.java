// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.graphics.drawable.Drawable$ConstantState;
import android.widget.TextView;
import android.support.v4.widget.TextViewCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.TooltipCompat;
import android.view.ViewStub;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.graphics.drawable.StateListDrawable;
import android.view.ViewGroup$LayoutParams;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.support.design.R;
import android.view.LayoutInflater;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.CheckedTextView;
import android.support.v7.view.menu.MenuItemImpl;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v7.view.menu.MenuView;

public class NavigationMenuItemView extends ForegroundLinearLayout implements ItemView
{
    private static final int[] CHECKED_STATE_SET;
    private final AccessibilityDelegateCompat accessibilityDelegate;
    private FrameLayout actionArea;
    boolean checkable;
    private Drawable emptyDrawable;
    private boolean hasIconTintList;
    private final int iconSize;
    private ColorStateList iconTintList;
    private MenuItemImpl itemData;
    private boolean needsEmptyIcon;
    private final CheckedTextView textView;
    
    static {
        CHECKED_STATE_SET = new int[] { 16842912 };
    }
    
    public NavigationMenuItemView(final Context context) {
        this(context, null);
    }
    
    public NavigationMenuItemView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public NavigationMenuItemView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.accessibilityDelegate = new AccessibilityDelegateCompat() {
            @Override
            public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                accessibilityNodeInfoCompat.setCheckable(NavigationMenuItemView.this.checkable);
            }
        };
        this.setOrientation(0);
        LayoutInflater.from(context).inflate(R.layout.design_navigation_menu_item, (ViewGroup)this, true);
        this.iconSize = context.getResources().getDimensionPixelSize(R.dimen.design_navigation_icon_size);
        (this.textView = (CheckedTextView)this.findViewById(R.id.design_menu_item_text)).setDuplicateParentStateEnabled(true);
        ViewCompat.setAccessibilityDelegate((View)this.textView, this.accessibilityDelegate);
    }
    
    private void adjustAppearance() {
        if (this.shouldExpandActionArea()) {
            this.textView.setVisibility(8);
            if (this.actionArea != null) {
                final LayoutParams layoutParams = (LayoutParams)this.actionArea.getLayoutParams();
                layoutParams.width = -1;
                this.actionArea.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            }
        }
        else {
            this.textView.setVisibility(0);
            if (this.actionArea != null) {
                final LayoutParams layoutParams2 = (LayoutParams)this.actionArea.getLayoutParams();
                layoutParams2.width = -2;
                this.actionArea.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
        }
    }
    
    private StateListDrawable createDefaultBackground() {
        final TypedValue typedValue = new TypedValue();
        if (this.getContext().getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.colorControlHighlight, typedValue, true)) {
            final StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(NavigationMenuItemView.CHECKED_STATE_SET, (Drawable)new ColorDrawable(typedValue.data));
            stateListDrawable.addState(NavigationMenuItemView.EMPTY_STATE_SET, (Drawable)new ColorDrawable(0));
            return stateListDrawable;
        }
        return null;
    }
    
    private void setActionView(final View view) {
        if (view != null) {
            if (this.actionArea == null) {
                this.actionArea = (FrameLayout)((ViewStub)this.findViewById(R.id.design_menu_item_action_area_stub)).inflate();
            }
            this.actionArea.removeAllViews();
            this.actionArea.addView(view);
        }
    }
    
    private boolean shouldExpandActionArea() {
        return this.itemData.getTitle() == null && this.itemData.getIcon() == null && this.itemData.getActionView() != null;
    }
    
    @Override
    public MenuItemImpl getItemData() {
        return this.itemData;
    }
    
    @Override
    public void initialize(final MenuItemImpl itemData, int visibility) {
        this.itemData = itemData;
        if (itemData.isVisible()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        this.setVisibility(visibility);
        if (this.getBackground() == null) {
            ViewCompat.setBackground((View)this, (Drawable)this.createDefaultBackground());
        }
        this.setCheckable(itemData.isCheckable());
        this.setChecked(itemData.isChecked());
        this.setEnabled(itemData.isEnabled());
        this.setTitle(itemData.getTitle());
        this.setIcon(itemData.getIcon());
        this.setActionView(itemData.getActionView());
        this.setContentDescription(itemData.getContentDescription());
        TooltipCompat.setTooltipText((View)this, itemData.getTooltipText());
        this.adjustAppearance();
    }
    
    protected int[] onCreateDrawableState(final int n) {
        final int[] onCreateDrawableState = super.onCreateDrawableState(n + 1);
        if (this.itemData != null && this.itemData.isCheckable() && this.itemData.isChecked()) {
            mergeDrawableStates(onCreateDrawableState, NavigationMenuItemView.CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }
    
    @Override
    public boolean prefersCondensedTitle() {
        return false;
    }
    
    public void recycle() {
        if (this.actionArea != null) {
            this.actionArea.removeAllViews();
        }
        this.textView.setCompoundDrawables((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
    }
    
    public void setCheckable(final boolean checkable) {
        this.refreshDrawableState();
        if (this.checkable != checkable) {
            this.checkable = checkable;
            this.accessibilityDelegate.sendAccessibilityEvent((View)this.textView, 2048);
        }
    }
    
    public void setChecked(final boolean checked) {
        this.refreshDrawableState();
        this.textView.setChecked(checked);
    }
    
    public void setHorizontalPadding(final int n) {
        this.setPadding(n, 0, n, 0);
    }
    
    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            Drawable mutate = drawable;
            if (this.hasIconTintList) {
                final Drawable$ConstantState constantState = drawable.getConstantState();
                if (constantState != null) {
                    drawable = constantState.newDrawable();
                }
                mutate = DrawableCompat.wrap(drawable).mutate();
                DrawableCompat.setTintList(mutate, this.iconTintList);
            }
            mutate.setBounds(0, 0, this.iconSize, this.iconSize);
            drawable = mutate;
        }
        else if (this.needsEmptyIcon) {
            if (this.emptyDrawable == null) {
                this.emptyDrawable = ResourcesCompat.getDrawable(this.getResources(), R.drawable.navigation_empty_icon, this.getContext().getTheme());
                if (this.emptyDrawable != null) {
                    this.emptyDrawable.setBounds(0, 0, this.iconSize, this.iconSize);
                }
            }
            drawable = this.emptyDrawable;
        }
        TextViewCompat.setCompoundDrawablesRelative((TextView)this.textView, drawable, null, null, null);
    }
    
    public void setIconPadding(final int compoundDrawablePadding) {
        this.textView.setCompoundDrawablePadding(compoundDrawablePadding);
    }
    
    void setIconTintList(final ColorStateList iconTintList) {
        this.iconTintList = iconTintList;
        this.hasIconTintList = (this.iconTintList != null);
        if (this.itemData != null) {
            this.setIcon(this.itemData.getIcon());
        }
    }
    
    public void setNeedsEmptyIcon(final boolean needsEmptyIcon) {
        this.needsEmptyIcon = needsEmptyIcon;
    }
    
    public void setTextAppearance(final int n) {
        TextViewCompat.setTextAppearance((TextView)this.textView, n);
    }
    
    public void setTextColor(final ColorStateList textColor) {
        this.textView.setTextColor(textColor);
    }
    
    public void setTitle(final CharSequence text) {
        this.textView.setText(text);
    }
}
