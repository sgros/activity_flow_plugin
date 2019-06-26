// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

import android.widget.FrameLayout;
import android.view.MenuItem$OnMenuItemClickListener;
import android.view.MenuItem$OnActionExpandListener;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.SubMenu;
import android.view.ContextMenu$ContextMenuInfo;
import android.content.Intent;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ActionProvider;
import android.content.Context;
import java.lang.reflect.Method;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenuItem;

public class MenuItemWrapperICS extends BaseMenuWrapper<SupportMenuItem> implements MenuItem
{
    private Method mSetExclusiveCheckableMethod;
    
    MenuItemWrapperICS(final Context context, final SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }
    
    public boolean collapseActionView() {
        return ((SupportMenuItem)super.mWrappedObject).collapseActionView();
    }
    
    ActionProviderWrapper createActionProviderWrapper(final ActionProvider actionProvider) {
        return new ActionProviderWrapper(super.mContext, actionProvider);
    }
    
    public boolean expandActionView() {
        return ((SupportMenuItem)super.mWrappedObject).expandActionView();
    }
    
    public ActionProvider getActionProvider() {
        final androidx.core.view.ActionProvider supportActionProvider = ((SupportMenuItem)super.mWrappedObject).getSupportActionProvider();
        if (supportActionProvider instanceof ActionProviderWrapper) {
            return ((ActionProviderWrapper)supportActionProvider).mInner;
        }
        return null;
    }
    
    public View getActionView() {
        View view2;
        final View view = view2 = ((SupportMenuItem)super.mWrappedObject).getActionView();
        if (view instanceof CollapsibleActionViewWrapper) {
            view2 = ((CollapsibleActionViewWrapper)view).getWrappedView();
        }
        return view2;
    }
    
    public int getAlphabeticModifiers() {
        return ((SupportMenuItem)super.mWrappedObject).getAlphabeticModifiers();
    }
    
    public char getAlphabeticShortcut() {
        return ((MenuItem)super.mWrappedObject).getAlphabeticShortcut();
    }
    
    public CharSequence getContentDescription() {
        return ((SupportMenuItem)super.mWrappedObject).getContentDescription();
    }
    
    public int getGroupId() {
        return ((MenuItem)super.mWrappedObject).getGroupId();
    }
    
    public Drawable getIcon() {
        return ((MenuItem)super.mWrappedObject).getIcon();
    }
    
    public ColorStateList getIconTintList() {
        return ((SupportMenuItem)super.mWrappedObject).getIconTintList();
    }
    
    public PorterDuff$Mode getIconTintMode() {
        return ((SupportMenuItem)super.mWrappedObject).getIconTintMode();
    }
    
    public Intent getIntent() {
        return ((MenuItem)super.mWrappedObject).getIntent();
    }
    
    public int getItemId() {
        return ((MenuItem)super.mWrappedObject).getItemId();
    }
    
    public ContextMenu$ContextMenuInfo getMenuInfo() {
        return ((MenuItem)super.mWrappedObject).getMenuInfo();
    }
    
    public int getNumericModifiers() {
        return ((SupportMenuItem)super.mWrappedObject).getNumericModifiers();
    }
    
    public char getNumericShortcut() {
        return ((MenuItem)super.mWrappedObject).getNumericShortcut();
    }
    
    public int getOrder() {
        return ((MenuItem)super.mWrappedObject).getOrder();
    }
    
    public SubMenu getSubMenu() {
        return this.getSubMenuWrapper(((MenuItem)super.mWrappedObject).getSubMenu());
    }
    
    public CharSequence getTitle() {
        return ((MenuItem)super.mWrappedObject).getTitle();
    }
    
    public CharSequence getTitleCondensed() {
        return ((MenuItem)super.mWrappedObject).getTitleCondensed();
    }
    
    public CharSequence getTooltipText() {
        return ((SupportMenuItem)super.mWrappedObject).getTooltipText();
    }
    
    public boolean hasSubMenu() {
        return ((MenuItem)super.mWrappedObject).hasSubMenu();
    }
    
    public boolean isActionViewExpanded() {
        return ((SupportMenuItem)super.mWrappedObject).isActionViewExpanded();
    }
    
    public boolean isCheckable() {
        return ((MenuItem)super.mWrappedObject).isCheckable();
    }
    
    public boolean isChecked() {
        return ((MenuItem)super.mWrappedObject).isChecked();
    }
    
    public boolean isEnabled() {
        return ((MenuItem)super.mWrappedObject).isEnabled();
    }
    
    public boolean isVisible() {
        return ((MenuItem)super.mWrappedObject).isVisible();
    }
    
    public MenuItem setActionProvider(final ActionProvider actionProvider) {
        final SupportMenuItem supportMenuItem = (SupportMenuItem)super.mWrappedObject;
        ActionProviderWrapper actionProviderWrapper;
        if (actionProvider != null) {
            actionProviderWrapper = this.createActionProviderWrapper(actionProvider);
        }
        else {
            actionProviderWrapper = null;
        }
        supportMenuItem.setSupportActionProvider(actionProviderWrapper);
        return (MenuItem)this;
    }
    
    public MenuItem setActionView(final int actionView) {
        ((SupportMenuItem)super.mWrappedObject).setActionView(actionView);
        final View actionView2 = ((SupportMenuItem)super.mWrappedObject).getActionView();
        if (actionView2 instanceof CollapsibleActionView) {
            ((SupportMenuItem)super.mWrappedObject).setActionView((View)new CollapsibleActionViewWrapper(actionView2));
        }
        return (MenuItem)this;
    }
    
    public MenuItem setActionView(final View view) {
        Object actionView = view;
        if (view instanceof CollapsibleActionView) {
            actionView = new CollapsibleActionViewWrapper(view);
        }
        ((SupportMenuItem)super.mWrappedObject).setActionView((View)actionView);
        return (MenuItem)this;
    }
    
    public MenuItem setAlphabeticShortcut(final char alphabeticShortcut) {
        ((MenuItem)super.mWrappedObject).setAlphabeticShortcut(alphabeticShortcut);
        return (MenuItem)this;
    }
    
    public MenuItem setAlphabeticShortcut(final char c, final int n) {
        ((SupportMenuItem)super.mWrappedObject).setAlphabeticShortcut(c, n);
        return (MenuItem)this;
    }
    
    public MenuItem setCheckable(final boolean checkable) {
        ((MenuItem)super.mWrappedObject).setCheckable(checkable);
        return (MenuItem)this;
    }
    
    public MenuItem setChecked(final boolean checked) {
        ((MenuItem)super.mWrappedObject).setChecked(checked);
        return (MenuItem)this;
    }
    
    public MenuItem setContentDescription(final CharSequence contentDescription) {
        ((SupportMenuItem)super.mWrappedObject).setContentDescription(contentDescription);
        return (MenuItem)this;
    }
    
    public MenuItem setEnabled(final boolean enabled) {
        ((MenuItem)super.mWrappedObject).setEnabled(enabled);
        return (MenuItem)this;
    }
    
    public void setExclusiveCheckable(final boolean b) {
        try {
            if (this.mSetExclusiveCheckableMethod == null) {
                this.mSetExclusiveCheckableMethod = ((SupportMenuItem)super.mWrappedObject).getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
            }
            this.mSetExclusiveCheckableMethod.invoke(super.mWrappedObject, b);
        }
        catch (Exception ex) {
            Log.w("MenuItemWrapper", "Error while calling setExclusiveCheckable", (Throwable)ex);
        }
    }
    
    public MenuItem setIcon(final int icon) {
        ((MenuItem)super.mWrappedObject).setIcon(icon);
        return (MenuItem)this;
    }
    
    public MenuItem setIcon(final Drawable icon) {
        ((MenuItem)super.mWrappedObject).setIcon(icon);
        return (MenuItem)this;
    }
    
    public MenuItem setIconTintList(final ColorStateList iconTintList) {
        ((SupportMenuItem)super.mWrappedObject).setIconTintList(iconTintList);
        return (MenuItem)this;
    }
    
    public MenuItem setIconTintMode(final PorterDuff$Mode iconTintMode) {
        ((SupportMenuItem)super.mWrappedObject).setIconTintMode(iconTintMode);
        return (MenuItem)this;
    }
    
    public MenuItem setIntent(final Intent intent) {
        ((MenuItem)super.mWrappedObject).setIntent(intent);
        return (MenuItem)this;
    }
    
    public MenuItem setNumericShortcut(final char numericShortcut) {
        ((MenuItem)super.mWrappedObject).setNumericShortcut(numericShortcut);
        return (MenuItem)this;
    }
    
    public MenuItem setNumericShortcut(final char c, final int n) {
        ((SupportMenuItem)super.mWrappedObject).setNumericShortcut(c, n);
        return (MenuItem)this;
    }
    
    public MenuItem setOnActionExpandListener(final MenuItem$OnActionExpandListener menuItem$OnActionExpandListener) {
        final SupportMenuItem supportMenuItem = (SupportMenuItem)super.mWrappedObject;
        Object onActionExpandListener;
        if (menuItem$OnActionExpandListener != null) {
            onActionExpandListener = new OnActionExpandListenerWrapper(menuItem$OnActionExpandListener);
        }
        else {
            onActionExpandListener = null;
        }
        ((MenuItem)supportMenuItem).setOnActionExpandListener((MenuItem$OnActionExpandListener)onActionExpandListener);
        return (MenuItem)this;
    }
    
    public MenuItem setOnMenuItemClickListener(final MenuItem$OnMenuItemClickListener menuItem$OnMenuItemClickListener) {
        final SupportMenuItem supportMenuItem = (SupportMenuItem)super.mWrappedObject;
        Object onMenuItemClickListener;
        if (menuItem$OnMenuItemClickListener != null) {
            onMenuItemClickListener = new OnMenuItemClickListenerWrapper(menuItem$OnMenuItemClickListener);
        }
        else {
            onMenuItemClickListener = null;
        }
        ((MenuItem)supportMenuItem).setOnMenuItemClickListener((MenuItem$OnMenuItemClickListener)onMenuItemClickListener);
        return (MenuItem)this;
    }
    
    public MenuItem setShortcut(final char c, final char c2) {
        ((MenuItem)super.mWrappedObject).setShortcut(c, c2);
        return (MenuItem)this;
    }
    
    public MenuItem setShortcut(final char c, final char c2, final int n, final int n2) {
        ((SupportMenuItem)super.mWrappedObject).setShortcut(c, c2, n, n2);
        return (MenuItem)this;
    }
    
    public void setShowAsAction(final int showAsAction) {
        ((SupportMenuItem)super.mWrappedObject).setShowAsAction(showAsAction);
    }
    
    public MenuItem setShowAsActionFlags(final int showAsActionFlags) {
        ((SupportMenuItem)super.mWrappedObject).setShowAsActionFlags(showAsActionFlags);
        return (MenuItem)this;
    }
    
    public MenuItem setTitle(final int title) {
        ((MenuItem)super.mWrappedObject).setTitle(title);
        return (MenuItem)this;
    }
    
    public MenuItem setTitle(final CharSequence title) {
        ((MenuItem)super.mWrappedObject).setTitle(title);
        return (MenuItem)this;
    }
    
    public MenuItem setTitleCondensed(final CharSequence titleCondensed) {
        ((MenuItem)super.mWrappedObject).setTitleCondensed(titleCondensed);
        return (MenuItem)this;
    }
    
    public MenuItem setTooltipText(final CharSequence tooltipText) {
        ((SupportMenuItem)super.mWrappedObject).setTooltipText(tooltipText);
        return (MenuItem)this;
    }
    
    public MenuItem setVisible(final boolean visible) {
        return ((MenuItem)super.mWrappedObject).setVisible(visible);
    }
    
    class ActionProviderWrapper extends ActionProvider
    {
        final android.view.ActionProvider mInner;
        
        public ActionProviderWrapper(final Context context, final android.view.ActionProvider mInner) {
            super(context);
            this.mInner = mInner;
        }
        
        @Override
        public boolean hasSubMenu() {
            return this.mInner.hasSubMenu();
        }
        
        @Override
        public View onCreateActionView() {
            return this.mInner.onCreateActionView();
        }
        
        @Override
        public boolean onPerformDefaultAction() {
            return this.mInner.onPerformDefaultAction();
        }
        
        @Override
        public void onPrepareSubMenu(final SubMenu subMenu) {
            this.mInner.onPrepareSubMenu(MenuItemWrapperICS.this.getSubMenuWrapper(subMenu));
        }
    }
    
    static class CollapsibleActionViewWrapper extends FrameLayout implements CollapsibleActionView
    {
        final android.view.CollapsibleActionView mWrappedView;
        
        CollapsibleActionViewWrapper(final View view) {
            super(view.getContext());
            this.mWrappedView = (android.view.CollapsibleActionView)view;
            this.addView(view);
        }
        
        View getWrappedView() {
            return (View)this.mWrappedView;
        }
        
        public void onActionViewCollapsed() {
            this.mWrappedView.onActionViewCollapsed();
        }
        
        public void onActionViewExpanded() {
            this.mWrappedView.onActionViewExpanded();
        }
    }
    
    private class OnActionExpandListenerWrapper extends BaseWrapper<MenuItem$OnActionExpandListener> implements MenuItem$OnActionExpandListener
    {
        OnActionExpandListenerWrapper(final MenuItem$OnActionExpandListener menuItem$OnActionExpandListener) {
            super(menuItem$OnActionExpandListener);
        }
        
        public boolean onMenuItemActionCollapse(final MenuItem menuItem) {
            return ((MenuItem$OnActionExpandListener)super.mWrappedObject).onMenuItemActionCollapse(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
        
        public boolean onMenuItemActionExpand(final MenuItem menuItem) {
            return ((MenuItem$OnActionExpandListener)super.mWrappedObject).onMenuItemActionExpand(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }
    
    private class OnMenuItemClickListenerWrapper extends BaseWrapper<MenuItem$OnMenuItemClickListener> implements MenuItem$OnMenuItemClickListener
    {
        OnMenuItemClickListenerWrapper(final MenuItem$OnMenuItemClickListener menuItem$OnMenuItemClickListener) {
            super(menuItem$OnMenuItemClickListener);
        }
        
        public boolean onMenuItemClick(final MenuItem menuItem) {
            return ((MenuItem$OnMenuItemClickListener)super.mWrappedObject).onMenuItemClick(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }
}
