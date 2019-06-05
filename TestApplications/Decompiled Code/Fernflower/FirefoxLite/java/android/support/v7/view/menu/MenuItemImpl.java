package android.support.v7.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.LinearLayout;

public final class MenuItemImpl implements SupportMenuItem {
   private ActionProvider mActionProvider;
   private View mActionView;
   private final int mCategoryOrder;
   private OnMenuItemClickListener mClickListener;
   private CharSequence mContentDescription;
   private int mFlags = 16;
   private final int mGroup;
   private boolean mHasIconTint = false;
   private boolean mHasIconTintMode = false;
   private Drawable mIconDrawable;
   private int mIconResId = 0;
   private ColorStateList mIconTintList = null;
   private Mode mIconTintMode = null;
   private final int mId;
   private Intent mIntent;
   private boolean mIsActionViewExpanded = false;
   private Runnable mItemCallback;
   MenuBuilder mMenu;
   private ContextMenuInfo mMenuInfo;
   private boolean mNeedToApplyIconTint = false;
   private OnActionExpandListener mOnActionExpandListener;
   private final int mOrdering;
   private char mShortcutAlphabeticChar;
   private int mShortcutAlphabeticModifiers = 4096;
   private char mShortcutNumericChar;
   private int mShortcutNumericModifiers = 4096;
   private int mShowAsAction = 0;
   private SubMenuBuilder mSubMenu;
   private CharSequence mTitle;
   private CharSequence mTitleCondensed;
   private CharSequence mTooltipText;

   MenuItemImpl(MenuBuilder var1, int var2, int var3, int var4, int var5, CharSequence var6, int var7) {
      this.mMenu = var1;
      this.mId = var3;
      this.mGroup = var2;
      this.mCategoryOrder = var4;
      this.mOrdering = var5;
      this.mTitle = var6;
      this.mShowAsAction = var7;
   }

   private static void appendModifier(StringBuilder var0, int var1, int var2, String var3) {
      if ((var1 & var2) == var2) {
         var0.append(var3);
      }

   }

   private Drawable applyIconTintIfNecessary(Drawable var1) {
      Drawable var2 = var1;
      if (var1 != null) {
         var2 = var1;
         if (this.mNeedToApplyIconTint) {
            if (!this.mHasIconTint) {
               var2 = var1;
               if (!this.mHasIconTintMode) {
                  return var2;
               }
            }

            var2 = DrawableCompat.wrap(var1).mutate();
            if (this.mHasIconTint) {
               DrawableCompat.setTintList(var2, this.mIconTintList);
            }

            if (this.mHasIconTintMode) {
               DrawableCompat.setTintMode(var2, this.mIconTintMode);
            }

            this.mNeedToApplyIconTint = false;
         }
      }

      return var2;
   }

   public void actionFormatChanged() {
      this.mMenu.onItemActionRequestChanged(this);
   }

   public boolean collapseActionView() {
      if ((this.mShowAsAction & 8) == 0) {
         return false;
      } else if (this.mActionView == null) {
         return true;
      } else {
         return this.mOnActionExpandListener != null && !this.mOnActionExpandListener.onMenuItemActionCollapse(this) ? false : this.mMenu.collapseItemActionView(this);
      }
   }

   public boolean expandActionView() {
      if (!this.hasCollapsibleActionView()) {
         return false;
      } else {
         return this.mOnActionExpandListener != null && !this.mOnActionExpandListener.onMenuItemActionExpand(this) ? false : this.mMenu.expandItemActionView(this);
      }
   }

   public android.view.ActionProvider getActionProvider() {
      throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
   }

   public View getActionView() {
      if (this.mActionView != null) {
         return this.mActionView;
      } else if (this.mActionProvider != null) {
         this.mActionView = this.mActionProvider.onCreateActionView(this);
         return this.mActionView;
      } else {
         return null;
      }
   }

   public int getAlphabeticModifiers() {
      return this.mShortcutAlphabeticModifiers;
   }

   public char getAlphabeticShortcut() {
      return this.mShortcutAlphabeticChar;
   }

   public CharSequence getContentDescription() {
      return this.mContentDescription;
   }

   public int getGroupId() {
      return this.mGroup;
   }

   public Drawable getIcon() {
      if (this.mIconDrawable != null) {
         return this.applyIconTintIfNecessary(this.mIconDrawable);
      } else if (this.mIconResId != 0) {
         Drawable var1 = AppCompatResources.getDrawable(this.mMenu.getContext(), this.mIconResId);
         this.mIconResId = 0;
         this.mIconDrawable = var1;
         return this.applyIconTintIfNecessary(var1);
      } else {
         return null;
      }
   }

   public ColorStateList getIconTintList() {
      return this.mIconTintList;
   }

   public Mode getIconTintMode() {
      return this.mIconTintMode;
   }

   public Intent getIntent() {
      return this.mIntent;
   }

   @CapturedViewProperty
   public int getItemId() {
      return this.mId;
   }

   public ContextMenuInfo getMenuInfo() {
      return this.mMenuInfo;
   }

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
      char var1;
      char var2;
      if (this.mMenu.isQwertyMode()) {
         var1 = this.mShortcutAlphabeticChar;
         var2 = var1;
      } else {
         var1 = this.mShortcutNumericChar;
         var2 = var1;
      }

      return var2;
   }

   String getShortcutLabel() {
      char var1 = this.getShortcut();
      if (var1 == 0) {
         return "";
      } else {
         Resources var2 = this.mMenu.getContext().getResources();
         StringBuilder var3 = new StringBuilder();
         if (ViewConfiguration.get(this.mMenu.getContext()).hasPermanentMenuKey()) {
            var3.append(var2.getString(R.string.abc_prepend_shortcut_label));
         }

         int var4;
         if (this.mMenu.isQwertyMode()) {
            var4 = this.mShortcutAlphabeticModifiers;
         } else {
            var4 = this.mShortcutNumericModifiers;
         }

         appendModifier(var3, var4, 65536, var2.getString(R.string.abc_menu_meta_shortcut_label));
         appendModifier(var3, var4, 4096, var2.getString(R.string.abc_menu_ctrl_shortcut_label));
         appendModifier(var3, var4, 2, var2.getString(R.string.abc_menu_alt_shortcut_label));
         appendModifier(var3, var4, 1, var2.getString(R.string.abc_menu_shift_shortcut_label));
         appendModifier(var3, var4, 4, var2.getString(R.string.abc_menu_sym_shortcut_label));
         appendModifier(var3, var4, 8, var2.getString(R.string.abc_menu_function_shortcut_label));
         if (var1 != '\b') {
            if (var1 != '\n') {
               if (var1 != ' ') {
                  var3.append(var1);
               } else {
                  var3.append(var2.getString(R.string.abc_menu_space_shortcut_label));
               }
            } else {
               var3.append(var2.getString(R.string.abc_menu_enter_shortcut_label));
            }
         } else {
            var3.append(var2.getString(R.string.abc_menu_delete_shortcut_label));
         }

         return var3.toString();
      }
   }

   public SubMenu getSubMenu() {
      return this.mSubMenu;
   }

   public ActionProvider getSupportActionProvider() {
      return this.mActionProvider;
   }

   @CapturedViewProperty
   public CharSequence getTitle() {
      return this.mTitle;
   }

   public CharSequence getTitleCondensed() {
      CharSequence var1;
      if (this.mTitleCondensed != null) {
         var1 = this.mTitleCondensed;
      } else {
         var1 = this.mTitle;
      }

      return (CharSequence)(VERSION.SDK_INT < 18 && var1 != null && !(var1 instanceof String) ? var1.toString() : var1);
   }

   CharSequence getTitleForItemView(MenuView.ItemView var1) {
      CharSequence var2;
      if (var1 != null && var1.prefersCondensedTitle()) {
         var2 = this.getTitleCondensed();
      } else {
         var2 = this.getTitle();
      }

      return var2;
   }

   public CharSequence getTooltipText() {
      return this.mTooltipText;
   }

   public boolean hasCollapsibleActionView() {
      int var1 = this.mShowAsAction;
      boolean var2 = false;
      if ((var1 & 8) != 0) {
         if (this.mActionView == null && this.mActionProvider != null) {
            this.mActionView = this.mActionProvider.onCreateActionView(this);
         }

         if (this.mActionView != null) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   public boolean hasSubMenu() {
      boolean var1;
      if (this.mSubMenu != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean invoke() {
      if (this.mClickListener != null && this.mClickListener.onMenuItemClick(this)) {
         return true;
      } else if (this.mMenu.dispatchMenuItemSelected(this.mMenu, this)) {
         return true;
      } else if (this.mItemCallback != null) {
         this.mItemCallback.run();
         return true;
      } else {
         if (this.mIntent != null) {
            try {
               this.mMenu.getContext().startActivity(this.mIntent);
               return true;
            } catch (ActivityNotFoundException var2) {
               Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", var2);
            }
         }

         return this.mActionProvider != null && this.mActionProvider.onPerformDefaultAction();
      }
   }

   public boolean isActionButton() {
      boolean var1;
      if ((this.mFlags & 32) == 32) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isActionViewExpanded() {
      return this.mIsActionViewExpanded;
   }

   public boolean isCheckable() {
      int var1 = this.mFlags;
      boolean var2 = true;
      if ((var1 & 1) != 1) {
         var2 = false;
      }

      return var2;
   }

   public boolean isChecked() {
      boolean var1;
      if ((this.mFlags & 2) == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isEnabled() {
      boolean var1;
      if ((this.mFlags & 16) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isExclusiveCheckable() {
      boolean var1;
      if ((this.mFlags & 4) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isVisible() {
      ActionProvider var1 = this.mActionProvider;
      boolean var2 = false;
      boolean var3 = false;
      if (var1 != null && this.mActionProvider.overridesItemVisibility()) {
         var2 = var3;
         if ((this.mFlags & 8) == 0) {
            var2 = var3;
            if (this.mActionProvider.isVisible()) {
               var2 = true;
            }
         }

         return var2;
      } else {
         if ((this.mFlags & 8) == 0) {
            var2 = true;
         }

         return var2;
      }
   }

   public boolean requestsActionButton() {
      int var1 = this.mShowAsAction;
      boolean var2 = true;
      if ((var1 & 1) != 1) {
         var2 = false;
      }

      return var2;
   }

   public boolean requiresActionButton() {
      boolean var1;
      if ((this.mShowAsAction & 2) == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public MenuItem setActionProvider(android.view.ActionProvider var1) {
      throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
   }

   public SupportMenuItem setActionView(int var1) {
      Context var2 = this.mMenu.getContext();
      this.setActionView(LayoutInflater.from(var2).inflate(var1, new LinearLayout(var2), false));
      return this;
   }

   public SupportMenuItem setActionView(View var1) {
      this.mActionView = var1;
      this.mActionProvider = null;
      if (var1 != null && var1.getId() == -1 && this.mId > 0) {
         var1.setId(this.mId);
      }

      this.mMenu.onItemActionRequestChanged(this);
      return this;
   }

   public void setActionViewExpanded(boolean var1) {
      this.mIsActionViewExpanded = var1;
      this.mMenu.onItemsChanged(false);
   }

   public MenuItem setAlphabeticShortcut(char var1) {
      if (this.mShortcutAlphabeticChar == var1) {
         return this;
      } else {
         this.mShortcutAlphabeticChar = Character.toLowerCase(var1);
         this.mMenu.onItemsChanged(false);
         return this;
      }
   }

   public MenuItem setAlphabeticShortcut(char var1, int var2) {
      if (this.mShortcutAlphabeticChar == var1 && this.mShortcutAlphabeticModifiers == var2) {
         return this;
      } else {
         this.mShortcutAlphabeticChar = Character.toLowerCase(var1);
         this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(var2);
         this.mMenu.onItemsChanged(false);
         return this;
      }
   }

   public MenuItem setCheckable(boolean var1) {
      int var2 = this.mFlags;
      this.mFlags = var1 | this.mFlags & -2;
      if (var2 != this.mFlags) {
         this.mMenu.onItemsChanged(false);
      }

      return this;
   }

   public MenuItem setChecked(boolean var1) {
      if ((this.mFlags & 4) != 0) {
         this.mMenu.setExclusiveItemChecked(this);
      } else {
         this.setCheckedInt(var1);
      }

      return this;
   }

   void setCheckedInt(boolean var1) {
      int var2 = this.mFlags;
      int var3 = this.mFlags;
      byte var4;
      if (var1) {
         var4 = 2;
      } else {
         var4 = 0;
      }

      this.mFlags = var4 | var3 & -3;
      if (var2 != this.mFlags) {
         this.mMenu.onItemsChanged(false);
      }

   }

   public SupportMenuItem setContentDescription(CharSequence var1) {
      this.mContentDescription = var1;
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public MenuItem setEnabled(boolean var1) {
      if (var1) {
         this.mFlags |= 16;
      } else {
         this.mFlags &= -17;
      }

      this.mMenu.onItemsChanged(false);
      return this;
   }

   public void setExclusiveCheckable(boolean var1) {
      int var2 = this.mFlags;
      byte var3;
      if (var1) {
         var3 = 4;
      } else {
         var3 = 0;
      }

      this.mFlags = var3 | var2 & -5;
   }

   public MenuItem setIcon(int var1) {
      this.mIconDrawable = null;
      this.mIconResId = var1;
      this.mNeedToApplyIconTint = true;
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public MenuItem setIcon(Drawable var1) {
      this.mIconResId = 0;
      this.mIconDrawable = var1;
      this.mNeedToApplyIconTint = true;
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public MenuItem setIconTintList(ColorStateList var1) {
      this.mIconTintList = var1;
      this.mHasIconTint = true;
      this.mNeedToApplyIconTint = true;
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public MenuItem setIconTintMode(Mode var1) {
      this.mIconTintMode = var1;
      this.mHasIconTintMode = true;
      this.mNeedToApplyIconTint = true;
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public MenuItem setIntent(Intent var1) {
      this.mIntent = var1;
      return this;
   }

   public void setIsActionButton(boolean var1) {
      if (var1) {
         this.mFlags |= 32;
      } else {
         this.mFlags &= -33;
      }

   }

   void setMenuInfo(ContextMenuInfo var1) {
      this.mMenuInfo = var1;
   }

   public MenuItem setNumericShortcut(char var1) {
      if (this.mShortcutNumericChar == var1) {
         return this;
      } else {
         this.mShortcutNumericChar = (char)var1;
         this.mMenu.onItemsChanged(false);
         return this;
      }
   }

   public MenuItem setNumericShortcut(char var1, int var2) {
      if (this.mShortcutNumericChar == var1 && this.mShortcutNumericModifiers == var2) {
         return this;
      } else {
         this.mShortcutNumericChar = (char)var1;
         this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(var2);
         this.mMenu.onItemsChanged(false);
         return this;
      }
   }

   public MenuItem setOnActionExpandListener(OnActionExpandListener var1) {
      this.mOnActionExpandListener = var1;
      return this;
   }

   public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener var1) {
      this.mClickListener = var1;
      return this;
   }

   public MenuItem setShortcut(char var1, char var2) {
      this.mShortcutNumericChar = (char)var1;
      this.mShortcutAlphabeticChar = Character.toLowerCase(var2);
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public MenuItem setShortcut(char var1, char var2, int var3, int var4) {
      this.mShortcutNumericChar = (char)var1;
      this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(var3);
      this.mShortcutAlphabeticChar = Character.toLowerCase(var2);
      this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(var4);
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public void setShowAsAction(int var1) {
      switch(var1 & 3) {
      case 0:
      case 1:
      case 2:
         this.mShowAsAction = var1;
         this.mMenu.onItemActionRequestChanged(this);
         return;
      default:
         throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
      }
   }

   public SupportMenuItem setShowAsActionFlags(int var1) {
      this.setShowAsAction(var1);
      return this;
   }

   public void setSubMenu(SubMenuBuilder var1) {
      this.mSubMenu = var1;
      var1.setHeaderTitle(this.getTitle());
   }

   public SupportMenuItem setSupportActionProvider(ActionProvider var1) {
      if (this.mActionProvider != null) {
         this.mActionProvider.reset();
      }

      this.mActionView = null;
      this.mActionProvider = var1;
      this.mMenu.onItemsChanged(true);
      if (this.mActionProvider != null) {
         this.mActionProvider.setVisibilityListener(new ActionProvider.VisibilityListener() {
            public void onActionProviderVisibilityChanged(boolean var1) {
               MenuItemImpl.this.mMenu.onItemVisibleChanged(MenuItemImpl.this);
            }
         });
      }

      return this;
   }

   public MenuItem setTitle(int var1) {
      return this.setTitle(this.mMenu.getContext().getString(var1));
   }

   public MenuItem setTitle(CharSequence var1) {
      this.mTitle = var1;
      this.mMenu.onItemsChanged(false);
      if (this.mSubMenu != null) {
         this.mSubMenu.setHeaderTitle(var1);
      }

      return this;
   }

   public MenuItem setTitleCondensed(CharSequence var1) {
      this.mTitleCondensed = var1;
      if (var1 == null) {
         var1 = this.mTitle;
      }

      this.mMenu.onItemsChanged(false);
      return this;
   }

   public SupportMenuItem setTooltipText(CharSequence var1) {
      this.mTooltipText = var1;
      this.mMenu.onItemsChanged(false);
      return this;
   }

   public MenuItem setVisible(boolean var1) {
      if (this.setVisibleInt(var1)) {
         this.mMenu.onItemVisibleChanged(this);
      }

      return this;
   }

   boolean setVisibleInt(boolean var1) {
      int var2 = this.mFlags;
      int var3 = this.mFlags;
      boolean var4 = false;
      byte var5;
      if (var1) {
         var5 = 0;
      } else {
         var5 = 8;
      }

      this.mFlags = var5 | var3 & -9;
      var1 = var4;
      if (var2 != this.mFlags) {
         var1 = true;
      }

      return var1;
   }

   public boolean shouldShowIcon() {
      return this.mMenu.getOptionalIconsVisible();
   }

   boolean shouldShowShortcut() {
      boolean var1;
      if (this.mMenu.isShortcutsVisible() && this.getShortcut() != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean showsTextAsAction() {
      boolean var1;
      if ((this.mShowAsAction & 4) == 4) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public String toString() {
      String var1;
      if (this.mTitle != null) {
         var1 = this.mTitle.toString();
      } else {
         var1 = null;
      }

      return var1;
   }
}
