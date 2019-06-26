// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.view.menu;

import android.view.KeyEvent;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.content.ActivityNotFoundException;
import android.util.Log;
import android.os.Build$VERSION;
import android.view.SubMenu;
import android.content.res.Resources;
import androidx.appcompat.R$string;
import android.view.ViewConfiguration;
import android.view.ViewDebug$CapturedViewProperty;
import androidx.appcompat.content.res.AppCompatResources;
import android.view.MenuItem;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.MenuItem$OnActionExpandListener;
import android.view.ContextMenu$ContextMenuInfo;
import android.content.Intent;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.MenuItem$OnMenuItemClickListener;
import android.view.View;
import androidx.core.view.ActionProvider;
import androidx.core.internal.view.SupportMenuItem;

public final class MenuItemImpl implements SupportMenuItem
{
    private ActionProvider mActionProvider;
    private View mActionView;
    private final int mCategoryOrder;
    private MenuItem$OnMenuItemClickListener mClickListener;
    private CharSequence mContentDescription;
    private int mFlags;
    private final int mGroup;
    private boolean mHasIconTint;
    private boolean mHasIconTintMode;
    private Drawable mIconDrawable;
    private int mIconResId;
    private ColorStateList mIconTintList;
    private PorterDuff$Mode mIconTintMode;
    private final int mId;
    private Intent mIntent;
    private boolean mIsActionViewExpanded;
    private Runnable mItemCallback;
    MenuBuilder mMenu;
    private ContextMenu$ContextMenuInfo mMenuInfo;
    private boolean mNeedToApplyIconTint;
    private MenuItem$OnActionExpandListener mOnActionExpandListener;
    private final int mOrdering;
    private char mShortcutAlphabeticChar;
    private int mShortcutAlphabeticModifiers;
    private char mShortcutNumericChar;
    private int mShortcutNumericModifiers;
    private int mShowAsAction;
    private SubMenuBuilder mSubMenu;
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
    private CharSequence mTooltipText;
    
    MenuItemImpl(final MenuBuilder mMenu, final int mGroup, final int mId, final int mCategoryOrder, final int mOrdering, final CharSequence mTitle, final int mShowAsAction) {
        this.mShortcutNumericModifiers = 4096;
        this.mShortcutAlphabeticModifiers = 4096;
        this.mIconResId = 0;
        this.mIconTintList = null;
        this.mIconTintMode = null;
        this.mHasIconTint = false;
        this.mHasIconTintMode = false;
        this.mNeedToApplyIconTint = false;
        this.mFlags = 16;
        this.mShowAsAction = 0;
        this.mIsActionViewExpanded = false;
        this.mMenu = mMenu;
        this.mId = mId;
        this.mGroup = mGroup;
        this.mCategoryOrder = mCategoryOrder;
        this.mOrdering = mOrdering;
        this.mTitle = mTitle;
        this.mShowAsAction = mShowAsAction;
    }
    
    private static void appendModifier(final StringBuilder sb, final int n, final int n2, final String str) {
        if ((n & n2) == n2) {
            sb.append(str);
        }
    }
    
    private Drawable applyIconTintIfNecessary(final Drawable drawable) {
        Drawable mutate = drawable;
        if (drawable != null) {
            mutate = drawable;
            if (this.mNeedToApplyIconTint) {
                if (!this.mHasIconTint) {
                    mutate = drawable;
                    if (!this.mHasIconTintMode) {
                        return mutate;
                    }
                }
                mutate = DrawableCompat.wrap(drawable).mutate();
                if (this.mHasIconTint) {
                    DrawableCompat.setTintList(mutate, this.mIconTintList);
                }
                if (this.mHasIconTintMode) {
                    DrawableCompat.setTintMode(mutate, this.mIconTintMode);
                }
                this.mNeedToApplyIconTint = false;
            }
        }
        return mutate;
    }
    
    public void actionFormatChanged() {
        this.mMenu.onItemActionRequestChanged(this);
    }
    
    @Override
    public boolean collapseActionView() {
        if ((this.mShowAsAction & 0x8) == 0x0) {
            return false;
        }
        if (this.mActionView == null) {
            return true;
        }
        final MenuItem$OnActionExpandListener mOnActionExpandListener = this.mOnActionExpandListener;
        return (mOnActionExpandListener == null || mOnActionExpandListener.onMenuItemActionCollapse((MenuItem)this)) && this.mMenu.collapseItemActionView(this);
    }
    
    @Override
    public boolean expandActionView() {
        if (!this.hasCollapsibleActionView()) {
            return false;
        }
        final MenuItem$OnActionExpandListener mOnActionExpandListener = this.mOnActionExpandListener;
        return (mOnActionExpandListener == null || mOnActionExpandListener.onMenuItemActionExpand((MenuItem)this)) && this.mMenu.expandItemActionView(this);
    }
    
    public android.view.ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
    }
    
    @Override
    public View getActionView() {
        final View mActionView = this.mActionView;
        if (mActionView != null) {
            return mActionView;
        }
        final ActionProvider mActionProvider = this.mActionProvider;
        if (mActionProvider != null) {
            return this.mActionView = mActionProvider.onCreateActionView((MenuItem)this);
        }
        return null;
    }
    
    @Override
    public int getAlphabeticModifiers() {
        return this.mShortcutAlphabeticModifiers;
    }
    
    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }
    
    @Override
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }
    
    public int getGroupId() {
        return this.mGroup;
    }
    
    public Drawable getIcon() {
        final Drawable mIconDrawable = this.mIconDrawable;
        if (mIconDrawable != null) {
            return this.applyIconTintIfNecessary(mIconDrawable);
        }
        if (this.mIconResId != 0) {
            final Drawable drawable = AppCompatResources.getDrawable(this.mMenu.getContext(), this.mIconResId);
            this.mIconResId = 0;
            this.mIconDrawable = drawable;
            return this.applyIconTintIfNecessary(drawable);
        }
        return null;
    }
    
    @Override
    public ColorStateList getIconTintList() {
        return this.mIconTintList;
    }
    
    @Override
    public PorterDuff$Mode getIconTintMode() {
        return this.mIconTintMode;
    }
    
    public Intent getIntent() {
        return this.mIntent;
    }
    
    @ViewDebug$CapturedViewProperty
    public int getItemId() {
        return this.mId;
    }
    
    public ContextMenu$ContextMenuInfo getMenuInfo() {
        return this.mMenuInfo;
    }
    
    @Override
    public int getNumericModifiers() {
        return this.mShortcutNumericModifiers;
    }
    
    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }
    
    public int getOrder() {
        return this.mCategoryOrder;
    }
    
    public int getOrdering() {
        return this.mOrdering;
    }
    
    char getShortcut() {
        char c;
        if (this.mMenu.isQwertyMode()) {
            c = this.mShortcutAlphabeticChar;
        }
        else {
            c = this.mShortcutNumericChar;
        }
        return c;
    }
    
    String getShortcutLabel() {
        final char shortcut = this.getShortcut();
        if (shortcut == '\0') {
            return "";
        }
        final Resources resources = this.mMenu.getContext().getResources();
        final StringBuilder sb = new StringBuilder();
        if (ViewConfiguration.get(this.mMenu.getContext()).hasPermanentMenuKey()) {
            sb.append(resources.getString(R$string.abc_prepend_shortcut_label));
        }
        int n;
        if (this.mMenu.isQwertyMode()) {
            n = this.mShortcutAlphabeticModifiers;
        }
        else {
            n = this.mShortcutNumericModifiers;
        }
        appendModifier(sb, n, 65536, resources.getString(R$string.abc_menu_meta_shortcut_label));
        appendModifier(sb, n, 4096, resources.getString(R$string.abc_menu_ctrl_shortcut_label));
        appendModifier(sb, n, 2, resources.getString(R$string.abc_menu_alt_shortcut_label));
        appendModifier(sb, n, 1, resources.getString(R$string.abc_menu_shift_shortcut_label));
        appendModifier(sb, n, 4, resources.getString(R$string.abc_menu_sym_shortcut_label));
        appendModifier(sb, n, 8, resources.getString(R$string.abc_menu_function_shortcut_label));
        if (shortcut != '\b') {
            if (shortcut != '\n') {
                if (shortcut != ' ') {
                    sb.append(shortcut);
                }
                else {
                    sb.append(resources.getString(R$string.abc_menu_space_shortcut_label));
                }
            }
            else {
                sb.append(resources.getString(R$string.abc_menu_enter_shortcut_label));
            }
        }
        else {
            sb.append(resources.getString(R$string.abc_menu_delete_shortcut_label));
        }
        return sb.toString();
    }
    
    public SubMenu getSubMenu() {
        return (SubMenu)this.mSubMenu;
    }
    
    @Override
    public ActionProvider getSupportActionProvider() {
        return this.mActionProvider;
    }
    
    @ViewDebug$CapturedViewProperty
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.mTitleCondensed;
        if (charSequence == null) {
            charSequence = this.mTitle;
        }
        CharSequence string = charSequence;
        if (Build$VERSION.SDK_INT < 18 && (string = charSequence) != null) {
            string = charSequence;
            if (!(charSequence instanceof String)) {
                string = charSequence.toString();
            }
        }
        return string;
    }
    
    CharSequence getTitleForItemView(final MenuView.ItemView itemView) {
        CharSequence charSequence;
        if (itemView != null && itemView.prefersCondensedTitle()) {
            charSequence = this.getTitleCondensed();
        }
        else {
            charSequence = this.getTitle();
        }
        return charSequence;
    }
    
    @Override
    public CharSequence getTooltipText() {
        return this.mTooltipText;
    }
    
    public boolean hasCollapsibleActionView() {
        final int mShowAsAction = this.mShowAsAction;
        boolean b = false;
        if ((mShowAsAction & 0x8) != 0x0) {
            if (this.mActionView == null) {
                final ActionProvider mActionProvider = this.mActionProvider;
                if (mActionProvider != null) {
                    this.mActionView = mActionProvider.onCreateActionView((MenuItem)this);
                }
            }
            b = b;
            if (this.mActionView != null) {
                b = true;
            }
        }
        return b;
    }
    
    public boolean hasSubMenu() {
        return this.mSubMenu != null;
    }
    
    public boolean invoke() {
        final MenuItem$OnMenuItemClickListener mClickListener = this.mClickListener;
        if (mClickListener != null && mClickListener.onMenuItemClick((MenuItem)this)) {
            return true;
        }
        final MenuBuilder mMenu = this.mMenu;
        if (mMenu.dispatchMenuItemSelected(mMenu, (MenuItem)this)) {
            return true;
        }
        final Runnable mItemCallback = this.mItemCallback;
        if (mItemCallback != null) {
            mItemCallback.run();
            return true;
        }
        if (this.mIntent != null) {
            try {
                this.mMenu.getContext().startActivity(this.mIntent);
                return true;
            }
            catch (ActivityNotFoundException ex) {
                Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", (Throwable)ex);
            }
        }
        final ActionProvider mActionProvider = this.mActionProvider;
        return mActionProvider != null && mActionProvider.onPerformDefaultAction();
    }
    
    public boolean isActionButton() {
        return (this.mFlags & 0x20) == 0x20;
    }
    
    @Override
    public boolean isActionViewExpanded() {
        return this.mIsActionViewExpanded;
    }
    
    public boolean isCheckable() {
        final int mFlags = this.mFlags;
        boolean b = true;
        if ((mFlags & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public boolean isChecked() {
        return (this.mFlags & 0x2) == 0x2;
    }
    
    public boolean isEnabled() {
        return (this.mFlags & 0x10) != 0x0;
    }
    
    public boolean isExclusiveCheckable() {
        return (this.mFlags & 0x4) != 0x0;
    }
    
    public boolean isVisible() {
        final ActionProvider mActionProvider = this.mActionProvider;
        final boolean b = true;
        boolean b2 = true;
        if (mActionProvider != null && mActionProvider.overridesItemVisibility()) {
            if ((this.mFlags & 0x8) != 0x0 || !this.mActionProvider.isVisible()) {
                b2 = false;
            }
            return b2;
        }
        return (this.mFlags & 0x8) == 0x0 && b;
    }
    
    public boolean requestsActionButton() {
        final int mShowAsAction = this.mShowAsAction;
        boolean b = true;
        if ((mShowAsAction & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public boolean requiresActionButton() {
        return (this.mShowAsAction & 0x2) == 0x2;
    }
    
    public MenuItem setActionProvider(final android.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
    }
    
    public SupportMenuItem setActionView(final int n) {
        final Context context = this.mMenu.getContext();
        this.setActionView(LayoutInflater.from(context).inflate(n, (ViewGroup)new LinearLayout(context), false));
        return this;
    }
    
    public SupportMenuItem setActionView(final View mActionView) {
        this.mActionView = mActionView;
        this.mActionProvider = null;
        if (mActionView != null && mActionView.getId() == -1) {
            final int mId = this.mId;
            if (mId > 0) {
                mActionView.setId(mId);
            }
        }
        this.mMenu.onItemActionRequestChanged(this);
        return this;
    }
    
    public void setActionViewExpanded(final boolean mIsActionViewExpanded) {
        this.mIsActionViewExpanded = mIsActionViewExpanded;
        this.mMenu.onItemsChanged(false);
    }
    
    public MenuItem setAlphabeticShortcut(final char ch) {
        if (this.mShortcutAlphabeticChar == ch) {
            return (MenuItem)this;
        }
        this.mShortcutAlphabeticChar = Character.toLowerCase(ch);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setAlphabeticShortcut(final char ch, final int n) {
        if (this.mShortcutAlphabeticChar == ch && this.mShortcutAlphabeticModifiers == n) {
            return (MenuItem)this;
        }
        this.mShortcutAlphabeticChar = Character.toLowerCase(ch);
        this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(n);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setCheckable(final boolean b) {
        final int mFlags = this.mFlags;
        this.mFlags = ((b ? 1 : 0) | (mFlags & 0xFFFFFFFE));
        if (mFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setChecked(final boolean checkedInt) {
        if ((this.mFlags & 0x4) != 0x0) {
            this.mMenu.setExclusiveItemChecked((MenuItem)this);
        }
        else {
            this.setCheckedInt(checkedInt);
        }
        return (MenuItem)this;
    }
    
    void setCheckedInt(final boolean b) {
        final int mFlags = this.mFlags;
        int n;
        if (b) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.mFlags = (n | (mFlags & 0xFFFFFFFD));
        if (mFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
    }
    
    @Override
    public SupportMenuItem setContentDescription(final CharSequence mContentDescription) {
        this.mContentDescription = mContentDescription;
        this.mMenu.onItemsChanged(false);
        return this;
    }
    
    public MenuItem setEnabled(final boolean b) {
        if (b) {
            this.mFlags |= 0x10;
        }
        else {
            this.mFlags &= 0xFFFFFFEF;
        }
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public void setExclusiveCheckable(final boolean b) {
        final int mFlags = this.mFlags;
        int n;
        if (b) {
            n = 4;
        }
        else {
            n = 0;
        }
        this.mFlags = (n | (mFlags & 0xFFFFFFFB));
    }
    
    public MenuItem setIcon(final int mIconResId) {
        this.mIconDrawable = null;
        this.mIconResId = mIconResId;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setIcon(final Drawable mIconDrawable) {
        this.mIconResId = 0;
        this.mIconDrawable = mIconDrawable;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setIconTintList(final ColorStateList mIconTintList) {
        this.mIconTintList = mIconTintList;
        this.mHasIconTint = true;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setIconTintMode(final PorterDuff$Mode mIconTintMode) {
        this.mIconTintMode = mIconTintMode;
        this.mHasIconTintMode = true;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setIntent(final Intent mIntent) {
        this.mIntent = mIntent;
        return (MenuItem)this;
    }
    
    public void setIsActionButton(final boolean b) {
        if (b) {
            this.mFlags |= 0x20;
        }
        else {
            this.mFlags &= 0xFFFFFFDF;
        }
    }
    
    void setMenuInfo(final ContextMenu$ContextMenuInfo mMenuInfo) {
        this.mMenuInfo = mMenuInfo;
    }
    
    public MenuItem setNumericShortcut(final char c) {
        if (this.mShortcutNumericChar == c) {
            return (MenuItem)this;
        }
        this.mShortcutNumericChar = c;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setNumericShortcut(final char c, final int n) {
        if (this.mShortcutNumericChar == c && this.mShortcutNumericModifiers == n) {
            return (MenuItem)this;
        }
        this.mShortcutNumericChar = c;
        this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(n);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setOnActionExpandListener(final MenuItem$OnActionExpandListener mOnActionExpandListener) {
        this.mOnActionExpandListener = mOnActionExpandListener;
        return (MenuItem)this;
    }
    
    public MenuItem setOnMenuItemClickListener(final MenuItem$OnMenuItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
        return (MenuItem)this;
    }
    
    public MenuItem setShortcut(final char c, final char ch) {
        this.mShortcutNumericChar = c;
        this.mShortcutAlphabeticChar = Character.toLowerCase(ch);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setShortcut(final char c, final char ch, final int n, final int n2) {
        this.mShortcutNumericChar = c;
        this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(n);
        this.mShortcutAlphabeticChar = Character.toLowerCase(ch);
        this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(n2);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public void setShowAsAction(final int mShowAsAction) {
        final int n = mShowAsAction & 0x3;
        if (n != 0 && n != 1 && n != 2) {
            throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
        }
        this.mShowAsAction = mShowAsAction;
        this.mMenu.onItemActionRequestChanged(this);
    }
    
    public SupportMenuItem setShowAsActionFlags(final int showAsAction) {
        this.setShowAsAction(showAsAction);
        return this;
    }
    
    public void setSubMenu(final SubMenuBuilder mSubMenu) {
        (this.mSubMenu = mSubMenu).setHeaderTitle(this.getTitle());
    }
    
    @Override
    public SupportMenuItem setSupportActionProvider(ActionProvider mActionProvider) {
        final ActionProvider mActionProvider2 = this.mActionProvider;
        if (mActionProvider2 != null) {
            mActionProvider2.reset();
        }
        this.mActionView = null;
        this.mActionProvider = mActionProvider;
        this.mMenu.onItemsChanged(true);
        mActionProvider = this.mActionProvider;
        if (mActionProvider != null) {
            mActionProvider.setVisibilityListener((ActionProvider.VisibilityListener)new ActionProvider.VisibilityListener() {
                @Override
                public void onActionProviderVisibilityChanged(final boolean b) {
                    final MenuItemImpl this$0 = MenuItemImpl.this;
                    this$0.mMenu.onItemVisibleChanged(this$0);
                }
            });
        }
        return this;
    }
    
    public MenuItem setTitle(final int n) {
        this.setTitle(this.mMenu.getContext().getString(n));
        return (MenuItem)this;
    }
    
    public MenuItem setTitle(final CharSequence charSequence) {
        this.mTitle = charSequence;
        this.mMenu.onItemsChanged(false);
        final SubMenuBuilder mSubMenu = this.mSubMenu;
        if (mSubMenu != null) {
            mSubMenu.setHeaderTitle(charSequence);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setTitleCondensed(CharSequence mTitle) {
        this.mTitleCondensed = mTitle;
        if (mTitle == null) {
            mTitle = this.mTitle;
        }
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public SupportMenuItem setTooltipText(final CharSequence mTooltipText) {
        this.mTooltipText = mTooltipText;
        this.mMenu.onItemsChanged(false);
        return this;
    }
    
    public MenuItem setVisible(final boolean visibleInt) {
        if (this.setVisibleInt(visibleInt)) {
            this.mMenu.onItemVisibleChanged(this);
        }
        return (MenuItem)this;
    }
    
    boolean setVisibleInt(final boolean b) {
        final int mFlags = this.mFlags;
        final boolean b2 = false;
        int n;
        if (b) {
            n = 0;
        }
        else {
            n = 8;
        }
        this.mFlags = (n | (mFlags & 0xFFFFFFF7));
        boolean b3 = b2;
        if (mFlags != this.mFlags) {
            b3 = true;
        }
        return b3;
    }
    
    public boolean shouldShowIcon() {
        return this.mMenu.getOptionalIconsVisible();
    }
    
    boolean shouldShowShortcut() {
        return this.mMenu.isShortcutsVisible() && this.getShortcut() != '\0';
    }
    
    public boolean showsTextAsAction() {
        return (this.mShowAsAction & 0x4) == 0x4;
    }
    
    @Override
    public String toString() {
        final CharSequence mTitle = this.mTitle;
        String string;
        if (mTitle != null) {
            string = mTitle.toString();
        }
        else {
            string = null;
        }
        return string;
    }
}
