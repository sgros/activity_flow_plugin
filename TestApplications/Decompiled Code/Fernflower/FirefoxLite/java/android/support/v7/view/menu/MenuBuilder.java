package android.support.v7.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyCharacterMap.KeyData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder implements SupportMenu {
   private static final int[] sCategoryToOrder = new int[]{1, 4, 5, 3, 2, 0};
   private ArrayList mActionItems;
   private MenuBuilder.Callback mCallback;
   private final Context mContext;
   private ContextMenuInfo mCurrentMenuInfo;
   private int mDefaultShowAsAction = 0;
   private MenuItemImpl mExpandedItem;
   private boolean mGroupDividerEnabled = false;
   Drawable mHeaderIcon;
   CharSequence mHeaderTitle;
   View mHeaderView;
   private boolean mIsActionItemsStale;
   private boolean mIsClosing = false;
   private boolean mIsVisibleItemsStale;
   private ArrayList mItems;
   private boolean mItemsChangedWhileDispatchPrevented = false;
   private ArrayList mNonActionItems;
   private boolean mOptionalIconsVisible = false;
   private boolean mOverrideVisibleItems;
   private CopyOnWriteArrayList mPresenters = new CopyOnWriteArrayList();
   private boolean mPreventDispatchingItemsChanged = false;
   private boolean mQwertyMode;
   private final Resources mResources;
   private boolean mShortcutsVisible;
   private boolean mStructureChangedWhileDispatchPrevented = false;
   private ArrayList mTempShortcutItemList = new ArrayList();
   private ArrayList mVisibleItems;

   public MenuBuilder(Context var1) {
      this.mContext = var1;
      this.mResources = var1.getResources();
      this.mItems = new ArrayList();
      this.mVisibleItems = new ArrayList();
      this.mIsVisibleItemsStale = true;
      this.mActionItems = new ArrayList();
      this.mNonActionItems = new ArrayList();
      this.mIsActionItemsStale = true;
      this.setShortcutsVisibleInner(true);
   }

   private MenuItemImpl createNewMenuItem(int var1, int var2, int var3, int var4, CharSequence var5, int var6) {
      return new MenuItemImpl(this, var1, var2, var3, var4, var5, var6);
   }

   private void dispatchPresenterUpdate(boolean var1) {
      if (!this.mPresenters.isEmpty()) {
         this.stopDispatchingItemsChanged();
         Iterator var2 = this.mPresenters.iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            MenuPresenter var4 = (MenuPresenter)var3.get();
            if (var4 == null) {
               this.mPresenters.remove(var3);
            } else {
               var4.updateMenuView(var1);
            }
         }

         this.startDispatchingItemsChanged();
      }
   }

   private void dispatchRestoreInstanceState(Bundle var1) {
      SparseArray var2 = var1.getSparseParcelableArray("android:menu:presenters");
      if (var2 != null && !this.mPresenters.isEmpty()) {
         Iterator var3 = this.mPresenters.iterator();

         while(var3.hasNext()) {
            WeakReference var4 = (WeakReference)var3.next();
            MenuPresenter var6 = (MenuPresenter)var4.get();
            if (var6 == null) {
               this.mPresenters.remove(var4);
            } else {
               int var5 = var6.getId();
               if (var5 > 0) {
                  Parcelable var7 = (Parcelable)var2.get(var5);
                  if (var7 != null) {
                     var6.onRestoreInstanceState(var7);
                  }
               }
            }
         }

      }
   }

   private void dispatchSaveInstanceState(Bundle var1) {
      if (!this.mPresenters.isEmpty()) {
         SparseArray var2 = new SparseArray();
         Iterator var3 = this.mPresenters.iterator();

         while(var3.hasNext()) {
            WeakReference var4 = (WeakReference)var3.next();
            MenuPresenter var5 = (MenuPresenter)var4.get();
            if (var5 == null) {
               this.mPresenters.remove(var4);
            } else {
               int var6 = var5.getId();
               if (var6 > 0) {
                  Parcelable var7 = var5.onSaveInstanceState();
                  if (var7 != null) {
                     var2.put(var6, var7);
                  }
               }
            }
         }

         var1.putSparseParcelableArray("android:menu:presenters", var2);
      }
   }

   private boolean dispatchSubMenuSelected(SubMenuBuilder var1, MenuPresenter var2) {
      boolean var3 = this.mPresenters.isEmpty();
      boolean var4 = false;
      if (var3) {
         return false;
      } else {
         if (var2 != null) {
            var4 = var2.onSubMenuSelected(var1);
         }

         Iterator var5 = this.mPresenters.iterator();

         while(var5.hasNext()) {
            WeakReference var7 = (WeakReference)var5.next();
            MenuPresenter var6 = (MenuPresenter)var7.get();
            if (var6 == null) {
               this.mPresenters.remove(var7);
            } else if (!var4) {
               var4 = var6.onSubMenuSelected(var1);
            }
         }

         return var4;
      }
   }

   private static int findInsertIndex(ArrayList var0, int var1) {
      for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
         if (((MenuItemImpl)var0.get(var2)).getOrdering() <= var1) {
            return var2 + 1;
         }
      }

      return 0;
   }

   private static int getOrdering(int var0) {
      int var1 = (-65536 & var0) >> 16;
      if (var1 >= 0 && var1 < sCategoryToOrder.length) {
         return var0 & '\uffff' | sCategoryToOrder[var1] << 16;
      } else {
         throw new IllegalArgumentException("order does not contain a valid category.");
      }
   }

   private void removeItemAtInt(int var1, boolean var2) {
      if (var1 >= 0 && var1 < this.mItems.size()) {
         this.mItems.remove(var1);
         if (var2) {
            this.onItemsChanged(true);
         }

      }
   }

   private void setHeaderInternal(int var1, CharSequence var2, int var3, Drawable var4, View var5) {
      Resources var6 = this.getResources();
      if (var5 != null) {
         this.mHeaderView = var5;
         this.mHeaderTitle = null;
         this.mHeaderIcon = null;
      } else {
         if (var1 > 0) {
            this.mHeaderTitle = var6.getText(var1);
         } else if (var2 != null) {
            this.mHeaderTitle = var2;
         }

         if (var3 > 0) {
            this.mHeaderIcon = ContextCompat.getDrawable(this.getContext(), var3);
         } else if (var4 != null) {
            this.mHeaderIcon = var4;
         }

         this.mHeaderView = null;
      }

      this.onItemsChanged(false);
   }

   private void setShortcutsVisibleInner(boolean var1) {
      boolean var2 = true;
      if (var1 && this.mResources.getConfiguration().keyboard != 1 && ViewConfigurationCompat.shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration.get(this.mContext), this.mContext)) {
         var1 = var2;
      } else {
         var1 = false;
      }

      this.mShortcutsVisible = var1;
   }

   public MenuItem add(int var1) {
      return this.addInternal(0, 0, 0, this.mResources.getString(var1));
   }

   public MenuItem add(int var1, int var2, int var3, int var4) {
      return this.addInternal(var1, var2, var3, this.mResources.getString(var4));
   }

   public MenuItem add(int var1, int var2, int var3, CharSequence var4) {
      return this.addInternal(var1, var2, var3, var4);
   }

   public MenuItem add(CharSequence var1) {
      return this.addInternal(0, 0, 0, var1);
   }

   public int addIntentOptions(int var1, int var2, int var3, ComponentName var4, Intent[] var5, Intent var6, int var7, MenuItem[] var8) {
      PackageManager var9 = this.mContext.getPackageManager();
      byte var10 = 0;
      List var11 = var9.queryIntentActivityOptions(var4, var5, var6, 0);
      int var12;
      if (var11 != null) {
         var12 = var11.size();
      } else {
         var12 = 0;
      }

      int var13 = var10;
      if ((var7 & 1) == 0) {
         this.removeGroup(var1);
         var13 = var10;
      }

      for(; var13 < var12; ++var13) {
         ResolveInfo var14 = (ResolveInfo)var11.get(var13);
         Intent var15;
         if (var14.specificIndex < 0) {
            var15 = var6;
         } else {
            var15 = var5[var14.specificIndex];
         }

         var15 = new Intent(var15);
         var15.setComponent(new ComponentName(var14.activityInfo.applicationInfo.packageName, var14.activityInfo.name));
         MenuItem var16 = this.add(var1, var2, var3, var14.loadLabel(var9)).setIcon(var14.loadIcon(var9)).setIntent(var15);
         if (var8 != null && var14.specificIndex >= 0) {
            var8[var14.specificIndex] = var16;
         }
      }

      return var12;
   }

   protected MenuItem addInternal(int var1, int var2, int var3, CharSequence var4) {
      int var5 = getOrdering(var3);
      MenuItemImpl var6 = this.createNewMenuItem(var1, var2, var3, var5, var4, this.mDefaultShowAsAction);
      if (this.mCurrentMenuInfo != null) {
         var6.setMenuInfo(this.mCurrentMenuInfo);
      }

      this.mItems.add(findInsertIndex(this.mItems, var5), var6);
      this.onItemsChanged(true);
      return var6;
   }

   public void addMenuPresenter(MenuPresenter var1) {
      this.addMenuPresenter(var1, this.mContext);
   }

   public void addMenuPresenter(MenuPresenter var1, Context var2) {
      this.mPresenters.add(new WeakReference(var1));
      var1.initForMenu(var2, this);
      this.mIsActionItemsStale = true;
   }

   public SubMenu addSubMenu(int var1) {
      return this.addSubMenu(0, 0, 0, this.mResources.getString(var1));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, int var4) {
      return this.addSubMenu(var1, var2, var3, this.mResources.getString(var4));
   }

   public SubMenu addSubMenu(int var1, int var2, int var3, CharSequence var4) {
      MenuItemImpl var5 = (MenuItemImpl)this.addInternal(var1, var2, var3, var4);
      SubMenuBuilder var6 = new SubMenuBuilder(this.mContext, this, var5);
      var5.setSubMenu(var6);
      return var6;
   }

   public SubMenu addSubMenu(CharSequence var1) {
      return this.addSubMenu(0, 0, 0, var1);
   }

   public void changeMenuMode() {
      if (this.mCallback != null) {
         this.mCallback.onMenuModeChange(this);
      }

   }

   public void clear() {
      if (this.mExpandedItem != null) {
         this.collapseItemActionView(this.mExpandedItem);
      }

      this.mItems.clear();
      this.onItemsChanged(true);
   }

   public void clearHeader() {
      this.mHeaderIcon = null;
      this.mHeaderTitle = null;
      this.mHeaderView = null;
      this.onItemsChanged(false);
   }

   public void close() {
      this.close(true);
   }

   public final void close(boolean var1) {
      if (!this.mIsClosing) {
         this.mIsClosing = true;
         Iterator var2 = this.mPresenters.iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            MenuPresenter var4 = (MenuPresenter)var3.get();
            if (var4 == null) {
               this.mPresenters.remove(var3);
            } else {
               var4.onCloseMenu(this, var1);
            }
         }

         this.mIsClosing = false;
      }
   }

   public boolean collapseItemActionView(MenuItemImpl var1) {
      boolean var2 = this.mPresenters.isEmpty();
      boolean var3 = false;
      if (!var2 && this.mExpandedItem == var1) {
         this.stopDispatchingItemsChanged();
         Iterator var4 = this.mPresenters.iterator();

         while(true) {
            var2 = var3;
            if (!var4.hasNext()) {
               break;
            }

            WeakReference var5 = (WeakReference)var4.next();
            MenuPresenter var6 = (MenuPresenter)var5.get();
            if (var6 == null) {
               this.mPresenters.remove(var5);
            } else {
               var2 = var6.collapseItemActionView(this, var1);
               var3 = var2;
               if (var2) {
                  break;
               }
            }
         }

         this.startDispatchingItemsChanged();
         if (var2) {
            this.mExpandedItem = null;
         }

         return var2;
      } else {
         return false;
      }
   }

   boolean dispatchMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      boolean var3;
      if (this.mCallback != null && this.mCallback.onMenuItemSelected(var1, var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean expandItemActionView(MenuItemImpl var1) {
      boolean var2 = this.mPresenters.isEmpty();
      boolean var3 = false;
      if (var2) {
         return false;
      } else {
         this.stopDispatchingItemsChanged();
         Iterator var4 = this.mPresenters.iterator();

         while(true) {
            var2 = var3;
            if (!var4.hasNext()) {
               break;
            }

            WeakReference var5 = (WeakReference)var4.next();
            MenuPresenter var6 = (MenuPresenter)var5.get();
            if (var6 == null) {
               this.mPresenters.remove(var5);
            } else {
               var2 = var6.expandItemActionView(this, var1);
               var3 = var2;
               if (var2) {
                  break;
               }
            }
         }

         this.startDispatchingItemsChanged();
         if (var2) {
            this.mExpandedItem = var1;
         }

         return var2;
      }
   }

   public int findGroupIndex(int var1) {
      return this.findGroupIndex(var1, 0);
   }

   public int findGroupIndex(int var1, int var2) {
      int var3 = this.size();
      int var4 = var2;
      if (var2 < 0) {
         var4 = 0;
      }

      while(var4 < var3) {
         if (((MenuItemImpl)this.mItems.get(var4)).getGroupId() == var1) {
            return var4;
         }

         ++var4;
      }

      return -1;
   }

   public MenuItem findItem(int var1) {
      int var2 = this.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         MenuItemImpl var4 = (MenuItemImpl)this.mItems.get(var3);
         if (var4.getItemId() == var1) {
            return var4;
         }

         if (var4.hasSubMenu()) {
            MenuItem var5 = var4.getSubMenu().findItem(var1);
            if (var5 != null) {
               return var5;
            }
         }
      }

      return null;
   }

   public int findItemIndex(int var1) {
      int var2 = this.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (((MenuItemImpl)this.mItems.get(var3)).getItemId() == var1) {
            return var3;
         }
      }

      return -1;
   }

   MenuItemImpl findItemWithShortcutForKey(int var1, KeyEvent var2) {
      ArrayList var3 = this.mTempShortcutItemList;
      var3.clear();
      this.findItemsWithShortcutForKey(var3, var1, var2);
      if (var3.isEmpty()) {
         return null;
      } else {
         int var4 = var2.getMetaState();
         KeyData var5 = new KeyData();
         var2.getKeyData(var5);
         int var6 = var3.size();
         if (var6 == 1) {
            return (MenuItemImpl)var3.get(0);
         } else {
            boolean var7 = this.isQwertyMode();

            for(int var8 = 0; var8 < var6; ++var8) {
               MenuItemImpl var10 = (MenuItemImpl)var3.get(var8);
               char var9;
               if (var7) {
                  var9 = var10.getAlphabeticShortcut();
               } else {
                  var9 = var10.getNumericShortcut();
               }

               if (var9 == var5.meta[0] && (var4 & 2) == 0 || var9 == var5.meta[2] && (var4 & 2) != 0 || var7 && var9 == '\b' && var1 == 67) {
                  return var10;
               }
            }

            return null;
         }
      }
   }

   void findItemsWithShortcutForKey(List var1, int var2, KeyEvent var3) {
      boolean var4 = this.isQwertyMode();
      int var5 = var3.getModifiers();
      KeyData var6 = new KeyData();
      if (var3.getKeyData(var6) || var2 == 67) {
         int var7 = this.mItems.size();

         for(int var8 = 0; var8 < var7; ++var8) {
            MenuItemImpl var9 = (MenuItemImpl)this.mItems.get(var8);
            if (var9.hasSubMenu()) {
               ((MenuBuilder)var9.getSubMenu()).findItemsWithShortcutForKey(var1, var2, var3);
            }

            char var10;
            if (var4) {
               var10 = var9.getAlphabeticShortcut();
            } else {
               var10 = var9.getNumericShortcut();
            }

            int var11;
            if (var4) {
               var11 = var9.getAlphabeticModifiers();
            } else {
               var11 = var9.getNumericModifiers();
            }

            boolean var12;
            if ((var5 & 69647) == (var11 & 69647)) {
               var12 = true;
            } else {
               var12 = false;
            }

            if (var12 && var10 != 0 && (var10 == var6.meta[0] || var10 == var6.meta[2] || var4 && var10 == '\b' && var2 == 67) && var9.isEnabled()) {
               var1.add(var9);
            }
         }

      }
   }

   public void flagActionItems() {
      ArrayList var1 = this.getVisibleItems();
      if (this.mIsActionItemsStale) {
         Iterator var2 = this.mPresenters.iterator();
         boolean var3 = false;

         while(var2.hasNext()) {
            WeakReference var4 = (WeakReference)var2.next();
            MenuPresenter var5 = (MenuPresenter)var4.get();
            if (var5 == null) {
               this.mPresenters.remove(var4);
            } else {
               var3 |= var5.flagActionItems();
            }
         }

         if (var3) {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            int var6 = var1.size();

            for(int var7 = 0; var7 < var6; ++var7) {
               MenuItemImpl var8 = (MenuItemImpl)var1.get(var7);
               if (var8.isActionButton()) {
                  this.mActionItems.add(var8);
               } else {
                  this.mNonActionItems.add(var8);
               }
            }
         } else {
            this.mActionItems.clear();
            this.mNonActionItems.clear();
            this.mNonActionItems.addAll(this.getVisibleItems());
         }

         this.mIsActionItemsStale = false;
      }
   }

   public ArrayList getActionItems() {
      this.flagActionItems();
      return this.mActionItems;
   }

   protected String getActionViewStatesKey() {
      return "android:menu:actionviewstates";
   }

   public Context getContext() {
      return this.mContext;
   }

   public MenuItemImpl getExpandedItem() {
      return this.mExpandedItem;
   }

   public Drawable getHeaderIcon() {
      return this.mHeaderIcon;
   }

   public CharSequence getHeaderTitle() {
      return this.mHeaderTitle;
   }

   public View getHeaderView() {
      return this.mHeaderView;
   }

   public MenuItem getItem(int var1) {
      return (MenuItem)this.mItems.get(var1);
   }

   public ArrayList getNonActionItems() {
      this.flagActionItems();
      return this.mNonActionItems;
   }

   boolean getOptionalIconsVisible() {
      return this.mOptionalIconsVisible;
   }

   Resources getResources() {
      return this.mResources;
   }

   public MenuBuilder getRootMenu() {
      return this;
   }

   public ArrayList getVisibleItems() {
      if (!this.mIsVisibleItemsStale) {
         return this.mVisibleItems;
      } else {
         this.mVisibleItems.clear();
         int var1 = this.mItems.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            MenuItemImpl var3 = (MenuItemImpl)this.mItems.get(var2);
            if (var3.isVisible()) {
               this.mVisibleItems.add(var3);
            }
         }

         this.mIsVisibleItemsStale = false;
         this.mIsActionItemsStale = true;
         return this.mVisibleItems;
      }
   }

   public boolean hasVisibleItems() {
      if (this.mOverrideVisibleItems) {
         return true;
      } else {
         int var1 = this.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            if (((MenuItemImpl)this.mItems.get(var2)).isVisible()) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isGroupDividerEnabled() {
      return this.mGroupDividerEnabled;
   }

   boolean isQwertyMode() {
      return this.mQwertyMode;
   }

   public boolean isShortcutKey(int var1, KeyEvent var2) {
      boolean var3;
      if (this.findItemWithShortcutForKey(var1, var2) != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isShortcutsVisible() {
      return this.mShortcutsVisible;
   }

   void onItemActionRequestChanged(MenuItemImpl var1) {
      this.mIsActionItemsStale = true;
      this.onItemsChanged(true);
   }

   void onItemVisibleChanged(MenuItemImpl var1) {
      this.mIsVisibleItemsStale = true;
      this.onItemsChanged(true);
   }

   public void onItemsChanged(boolean var1) {
      if (!this.mPreventDispatchingItemsChanged) {
         if (var1) {
            this.mIsVisibleItemsStale = true;
            this.mIsActionItemsStale = true;
         }

         this.dispatchPresenterUpdate(var1);
      } else {
         this.mItemsChangedWhileDispatchPrevented = true;
         if (var1) {
            this.mStructureChangedWhileDispatchPrevented = true;
         }
      }

   }

   public boolean performIdentifierAction(int var1, int var2) {
      return this.performItemAction(this.findItem(var1), var2);
   }

   public boolean performItemAction(MenuItem var1, int var2) {
      return this.performItemAction(var1, (MenuPresenter)null, var2);
   }

   public boolean performItemAction(MenuItem var1, MenuPresenter var2, int var3) {
      MenuItemImpl var4 = (MenuItemImpl)var1;
      if (var4 != null && var4.isEnabled()) {
         boolean var5 = var4.invoke();
         ActionProvider var8 = var4.getSupportActionProvider();
         boolean var6;
         if (var8 != null && var8.hasSubMenu()) {
            var6 = true;
         } else {
            var6 = false;
         }

         boolean var7;
         if (var4.hasCollapsibleActionView()) {
            var5 |= var4.expandActionView();
            var7 = var5;
            if (var5) {
               this.close(true);
               var7 = var5;
            }
         } else if (!var4.hasSubMenu() && !var6) {
            var7 = var5;
            if ((var3 & 1) == 0) {
               this.close(true);
               var7 = var5;
            }
         } else {
            if ((var3 & 4) == 0) {
               this.close(false);
            }

            if (!var4.hasSubMenu()) {
               var4.setSubMenu(new SubMenuBuilder(this.getContext(), this, var4));
            }

            SubMenuBuilder var9 = (SubMenuBuilder)var4.getSubMenu();
            if (var6) {
               var8.onPrepareSubMenu(var9);
            }

            var5 |= this.dispatchSubMenuSelected(var9, var2);
            var7 = var5;
            if (!var5) {
               this.close(true);
               var7 = var5;
            }
         }

         return var7;
      } else {
         return false;
      }
   }

   public boolean performShortcut(int var1, KeyEvent var2, int var3) {
      MenuItemImpl var5 = this.findItemWithShortcutForKey(var1, var2);
      boolean var4;
      if (var5 != null) {
         var4 = this.performItemAction(var5, var3);
      } else {
         var4 = false;
      }

      if ((var3 & 2) != 0) {
         this.close(true);
      }

      return var4;
   }

   public void removeGroup(int var1) {
      int var2 = this.findGroupIndex(var1);
      if (var2 >= 0) {
         int var3 = this.mItems.size();

         for(int var4 = 0; var4 < var3 - var2 && ((MenuItemImpl)this.mItems.get(var2)).getGroupId() == var1; ++var4) {
            this.removeItemAtInt(var2, false);
         }

         this.onItemsChanged(true);
      }

   }

   public void removeItem(int var1) {
      this.removeItemAtInt(this.findItemIndex(var1), true);
   }

   public void removeMenuPresenter(MenuPresenter var1) {
      Iterator var2 = this.mPresenters.iterator();

      while(true) {
         WeakReference var3;
         MenuPresenter var4;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (WeakReference)var2.next();
            var4 = (MenuPresenter)var3.get();
         } while(var4 != null && var4 != var1);

         this.mPresenters.remove(var3);
      }
   }

   public void restoreActionViewStates(Bundle var1) {
      if (var1 != null) {
         SparseArray var2 = var1.getSparseParcelableArray(this.getActionViewStatesKey());
         int var3 = this.size();

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            MenuItem var5 = this.getItem(var4);
            View var6 = var5.getActionView();
            if (var6 != null && var6.getId() != -1) {
               var6.restoreHierarchyState(var2);
            }

            if (var5.hasSubMenu()) {
               ((SubMenuBuilder)var5.getSubMenu()).restoreActionViewStates(var1);
            }
         }

         var4 = var1.getInt("android:menu:expandedactionview");
         if (var4 > 0) {
            MenuItem var7 = this.findItem(var4);
            if (var7 != null) {
               var7.expandActionView();
            }
         }

      }
   }

   public void restorePresenterStates(Bundle var1) {
      this.dispatchRestoreInstanceState(var1);
   }

   public void saveActionViewStates(Bundle var1) {
      int var2 = this.size();
      SparseArray var3 = null;

      SparseArray var7;
      for(int var4 = 0; var4 < var2; var3 = var7) {
         MenuItem var5 = this.getItem(var4);
         View var6 = var5.getActionView();
         var7 = var3;
         if (var6 != null) {
            var7 = var3;
            if (var6.getId() != -1) {
               SparseArray var8 = var3;
               if (var3 == null) {
                  var8 = new SparseArray();
               }

               var6.saveHierarchyState(var8);
               var7 = var8;
               if (var5.isActionViewExpanded()) {
                  var1.putInt("android:menu:expandedactionview", var5.getItemId());
                  var7 = var8;
               }
            }
         }

         if (var5.hasSubMenu()) {
            ((SubMenuBuilder)var5.getSubMenu()).saveActionViewStates(var1);
         }

         ++var4;
      }

      if (var3 != null) {
         var1.putSparseParcelableArray(this.getActionViewStatesKey(), var3);
      }

   }

   public void savePresenterStates(Bundle var1) {
      this.dispatchSaveInstanceState(var1);
   }

   public void setCallback(MenuBuilder.Callback var1) {
      this.mCallback = var1;
   }

   public MenuBuilder setDefaultShowAsAction(int var1) {
      this.mDefaultShowAsAction = var1;
      return this;
   }

   void setExclusiveItemChecked(MenuItem var1) {
      int var2 = var1.getGroupId();
      int var3 = this.mItems.size();
      this.stopDispatchingItemsChanged();

      for(int var4 = 0; var4 < var3; ++var4) {
         MenuItemImpl var5 = (MenuItemImpl)this.mItems.get(var4);
         if (var5.getGroupId() == var2 && var5.isExclusiveCheckable() && var5.isCheckable()) {
            boolean var6;
            if (var5 == var1) {
               var6 = true;
            } else {
               var6 = false;
            }

            var5.setCheckedInt(var6);
         }
      }

      this.startDispatchingItemsChanged();
   }

   public void setGroupCheckable(int var1, boolean var2, boolean var3) {
      int var4 = this.mItems.size();

      for(int var5 = 0; var5 < var4; ++var5) {
         MenuItemImpl var6 = (MenuItemImpl)this.mItems.get(var5);
         if (var6.getGroupId() == var1) {
            var6.setExclusiveCheckable(var3);
            var6.setCheckable(var2);
         }
      }

   }

   public void setGroupDividerEnabled(boolean var1) {
      this.mGroupDividerEnabled = var1;
   }

   public void setGroupEnabled(int var1, boolean var2) {
      int var3 = this.mItems.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         MenuItemImpl var5 = (MenuItemImpl)this.mItems.get(var4);
         if (var5.getGroupId() == var1) {
            var5.setEnabled(var2);
         }
      }

   }

   public void setGroupVisible(int var1, boolean var2) {
      int var3 = this.mItems.size();
      int var4 = 0;

      boolean var5;
      boolean var7;
      for(var5 = false; var4 < var3; var5 = var7) {
         MenuItemImpl var6 = (MenuItemImpl)this.mItems.get(var4);
         var7 = var5;
         if (var6.getGroupId() == var1) {
            var7 = var5;
            if (var6.setVisibleInt(var2)) {
               var7 = true;
            }
         }

         ++var4;
      }

      if (var5) {
         this.onItemsChanged(true);
      }

   }

   protected MenuBuilder setHeaderIconInt(int var1) {
      this.setHeaderInternal(0, (CharSequence)null, var1, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderIconInt(Drawable var1) {
      this.setHeaderInternal(0, (CharSequence)null, 0, var1, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderTitleInt(int var1) {
      this.setHeaderInternal(var1, (CharSequence)null, 0, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderTitleInt(CharSequence var1) {
      this.setHeaderInternal(0, var1, 0, (Drawable)null, (View)null);
      return this;
   }

   protected MenuBuilder setHeaderViewInt(View var1) {
      this.setHeaderInternal(0, (CharSequence)null, 0, (Drawable)null, var1);
      return this;
   }

   public void setOverrideVisibleItems(boolean var1) {
      this.mOverrideVisibleItems = var1;
   }

   public void setQwertyMode(boolean var1) {
      this.mQwertyMode = var1;
      this.onItemsChanged(false);
   }

   public int size() {
      return this.mItems.size();
   }

   public void startDispatchingItemsChanged() {
      this.mPreventDispatchingItemsChanged = false;
      if (this.mItemsChangedWhileDispatchPrevented) {
         this.mItemsChangedWhileDispatchPrevented = false;
         this.onItemsChanged(this.mStructureChangedWhileDispatchPrevented);
      }

   }

   public void stopDispatchingItemsChanged() {
      if (!this.mPreventDispatchingItemsChanged) {
         this.mPreventDispatchingItemsChanged = true;
         this.mItemsChangedWhileDispatchPrevented = false;
         this.mStructureChangedWhileDispatchPrevented = false;
      }

   }

   public interface Callback {
      boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2);

      void onMenuModeChange(MenuBuilder var1);
   }

   public interface ItemInvoker {
      boolean invokeItem(MenuItemImpl var1);
   }
}
