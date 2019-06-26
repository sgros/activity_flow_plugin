// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.widget.LinearLayout$LayoutParams;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.LinearLayout;

public class ActionBarMenu extends LinearLayout
{
    protected boolean isActionMode;
    protected ActionBar parentActionBar;
    
    public ActionBarMenu(final Context context) {
        super(context);
    }
    
    public ActionBarMenu(final Context context, final ActionBar parentActionBar) {
        super(context);
        this.setOrientation(0);
        this.parentActionBar = parentActionBar;
    }
    
    public ActionBarMenuItem addItem(final int n, final int n2) {
        int n3;
        if (this.isActionMode) {
            n3 = this.parentActionBar.itemsActionModeBackgroundColor;
        }
        else {
            n3 = this.parentActionBar.itemsBackgroundColor;
        }
        return this.addItem(n, n2, n3);
    }
    
    public ActionBarMenuItem addItem(final int n, final int n2, final int n3) {
        return this.addItem(n, n2, n3, null, AndroidUtilities.dp(48.0f), null);
    }
    
    public ActionBarMenuItem addItem(final int i, final int imageResource, final int n, final Drawable imageDrawable, final int n2, final CharSequence contentDescription) {
        final Context context = this.getContext();
        int n3;
        if (this.isActionMode) {
            n3 = this.parentActionBar.itemsActionModeColor;
        }
        else {
            n3 = this.parentActionBar.itemsColor;
        }
        final ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, this, n, n3);
        actionBarMenuItem.setTag((Object)i);
        if (imageDrawable != null) {
            actionBarMenuItem.iconView.setImageDrawable(imageDrawable);
        }
        else if (imageResource != 0) {
            actionBarMenuItem.iconView.setImageResource(imageResource);
        }
        this.addView((View)actionBarMenuItem, (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(n2, -1));
        actionBarMenuItem.setOnClickListener((View$OnClickListener)new _$$Lambda$ActionBarMenu$ppo9UED664gE_YCecAHKNZM7u90(this));
        if (contentDescription != null) {
            actionBarMenuItem.setContentDescription(contentDescription);
        }
        return actionBarMenuItem;
    }
    
    public ActionBarMenuItem addItem(final int n, final Drawable drawable) {
        int n2;
        if (this.isActionMode) {
            n2 = this.parentActionBar.itemsActionModeBackgroundColor;
        }
        else {
            n2 = this.parentActionBar.itemsBackgroundColor;
        }
        return this.addItem(n, 0, n2, drawable, AndroidUtilities.dp(48.0f), null);
    }
    
    public ActionBarMenuItem addItemWithWidth(final int n, final int n2, final int n3) {
        int n4;
        if (this.isActionMode) {
            n4 = this.parentActionBar.itemsActionModeBackgroundColor;
        }
        else {
            n4 = this.parentActionBar.itemsBackgroundColor;
        }
        return this.addItem(n, n2, n4, null, n3, null);
    }
    
    public ActionBarMenuItem addItemWithWidth(final int n, final int n2, final int n3, final CharSequence charSequence) {
        int n4;
        if (this.isActionMode) {
            n4 = this.parentActionBar.itemsActionModeBackgroundColor;
        }
        else {
            n4 = this.parentActionBar.itemsBackgroundColor;
        }
        return this.addItem(n, n2, n4, null, n3, charSequence);
    }
    
    public void clearItems() {
        this.removeAllViews();
    }
    
    public void closeSearchField(final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                final ActionBarMenuItem actionBarMenuItem = (ActionBarMenuItem)child;
                if (actionBarMenuItem.isSearchField()) {
                    this.parentActionBar.onSearchFieldVisibilityChanged(false);
                    actionBarMenuItem.toggleSearch(b);
                    break;
                }
            }
        }
    }
    
    public ActionBarMenuItem getItem(final int i) {
        final View viewWithTag = this.findViewWithTag((Object)i);
        if (viewWithTag instanceof ActionBarMenuItem) {
            return (ActionBarMenuItem)viewWithTag;
        }
        return null;
    }
    
    public void hideAllPopupMenus() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem)child).closeSubMenu();
            }
        }
    }
    
    public void onItemClick(final int n) {
        final ActionBar.ActionBarMenuOnItemClick actionBarMenuOnItemClick = this.parentActionBar.actionBarMenuOnItemClick;
        if (actionBarMenuOnItemClick != null) {
            actionBarMenuOnItemClick.onItemClick(n);
        }
    }
    
    public void onMenuButtonPressed() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                final ActionBarMenuItem actionBarMenuItem = (ActionBarMenuItem)child;
                if (actionBarMenuItem.getVisibility() == 0) {
                    if (actionBarMenuItem.hasSubMenu()) {
                        actionBarMenuItem.toggleSubMenu();
                        break;
                    }
                    if (actionBarMenuItem.overrideMenuClick) {
                        this.onItemClick((int)actionBarMenuItem.getTag());
                        break;
                    }
                }
            }
        }
    }
    
    public void openSearchField(final boolean b, final String s, final boolean b2) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                final ActionBarMenuItem actionBarMenuItem = (ActionBarMenuItem)child;
                if (actionBarMenuItem.isSearchField()) {
                    if (b) {
                        this.parentActionBar.onSearchFieldVisibilityChanged(actionBarMenuItem.toggleSearch(true));
                    }
                    actionBarMenuItem.setSearchFieldText(s, b2);
                    actionBarMenuItem.getSearchField().setSelection(s.length());
                    break;
                }
            }
        }
    }
    
    protected void redrawPopup(final int n) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem)child).redrawPopup(n);
            }
        }
    }
    
    public void setEnabled(final boolean b) {
        super.setEnabled(b);
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            this.getChildAt(i).setEnabled(b);
        }
    }
    
    protected void setPopupItemsColor(final int n, final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem)child).setPopupItemsColor(n, b);
            }
        }
    }
    
    public void setSearchTextColor(final int n, final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                final ActionBarMenuItem actionBarMenuItem = (ActionBarMenuItem)child;
                if (actionBarMenuItem.isSearchField()) {
                    if (b) {
                        actionBarMenuItem.getSearchField().setHintTextColor(n);
                        break;
                    }
                    actionBarMenuItem.getSearchField().setTextColor(n);
                    break;
                }
            }
        }
    }
    
    protected void updateItemsBackgroundColor() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                int n;
                if (this.isActionMode) {
                    n = this.parentActionBar.itemsActionModeBackgroundColor;
                }
                else {
                    n = this.parentActionBar.itemsBackgroundColor;
                }
                child.setBackgroundDrawable(Theme.createSelectorDrawable(n));
            }
        }
    }
    
    protected void updateItemsColor() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child instanceof ActionBarMenuItem) {
                final ActionBarMenuItem actionBarMenuItem = (ActionBarMenuItem)child;
                int iconColor;
                if (this.isActionMode) {
                    iconColor = this.parentActionBar.itemsActionModeColor;
                }
                else {
                    iconColor = this.parentActionBar.itemsColor;
                }
                actionBarMenuItem.setIconColor(iconColor);
            }
        }
    }
}
