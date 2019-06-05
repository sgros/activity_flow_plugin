package android.support.design.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.R;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class NavigationMenuPresenter implements MenuPresenter {
   NavigationMenuPresenter.NavigationMenuAdapter adapter;
   private MenuPresenter.Callback callback;
   LinearLayout headerLayout;
   ColorStateList iconTintList;
   private int id;
   Drawable itemBackground;
   int itemHorizontalPadding;
   int itemIconPadding;
   LayoutInflater layoutInflater;
   MenuBuilder menu;
   private NavigationMenuView menuView;
   final OnClickListener onClickListener = new OnClickListener() {
      public void onClick(View var1) {
         NavigationMenuItemView var3 = (NavigationMenuItemView)var1;
         NavigationMenuPresenter.this.setUpdateSuspended(true);
         MenuItemImpl var4 = var3.getItemData();
         boolean var2 = NavigationMenuPresenter.this.menu.performItemAction(var4, NavigationMenuPresenter.this, 0);
         if (var4 != null && var4.isCheckable() && var2) {
            NavigationMenuPresenter.this.adapter.setCheckedItem(var4);
         }

         NavigationMenuPresenter.this.setUpdateSuspended(false);
         NavigationMenuPresenter.this.updateMenuView(false);
      }
   };
   int paddingSeparator;
   private int paddingTopDefault;
   int textAppearance;
   boolean textAppearanceSet;
   ColorStateList textColor;

   public void addHeaderView(View var1) {
      this.headerLayout.addView(var1);
      this.menuView.setPadding(0, 0, 0, this.menuView.getPaddingBottom());
   }

   public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public void dispatchApplyWindowInsets(WindowInsetsCompat var1) {
      int var2 = var1.getSystemWindowInsetTop();
      if (this.paddingTopDefault != var2) {
         this.paddingTopDefault = var2;
         if (this.headerLayout.getChildCount() == 0) {
            this.menuView.setPadding(0, this.paddingTopDefault, 0, this.menuView.getPaddingBottom());
         }
      }

      ViewCompat.dispatchApplyWindowInsets(this.headerLayout, var1);
   }

   public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean flagActionItems() {
      return false;
   }

   public MenuItemImpl getCheckedItem() {
      return this.adapter.getCheckedItem();
   }

   public int getHeaderCount() {
      return this.headerLayout.getChildCount();
   }

   public int getId() {
      return this.id;
   }

   public Drawable getItemBackground() {
      return this.itemBackground;
   }

   public int getItemHorizontalPadding() {
      return this.itemHorizontalPadding;
   }

   public int getItemIconPadding() {
      return this.itemIconPadding;
   }

   public ColorStateList getItemTextColor() {
      return this.textColor;
   }

   public ColorStateList getItemTintList() {
      return this.iconTintList;
   }

   public MenuView getMenuView(ViewGroup var1) {
      if (this.menuView == null) {
         this.menuView = (NavigationMenuView)this.layoutInflater.inflate(R.layout.design_navigation_menu, var1, false);
         if (this.adapter == null) {
            this.adapter = new NavigationMenuPresenter.NavigationMenuAdapter();
         }

         this.headerLayout = (LinearLayout)this.layoutInflater.inflate(R.layout.design_navigation_item_header, this.menuView, false);
         this.menuView.setAdapter(this.adapter);
      }

      return this.menuView;
   }

   public View inflateHeaderView(int var1) {
      View var2 = this.layoutInflater.inflate(var1, this.headerLayout, false);
      this.addHeaderView(var2);
      return var2;
   }

   public void initForMenu(Context var1, MenuBuilder var2) {
      this.layoutInflater = LayoutInflater.from(var1);
      this.menu = var2;
      this.paddingSeparator = var1.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
   }

   public void onCloseMenu(MenuBuilder var1, boolean var2) {
      if (this.callback != null) {
         this.callback.onCloseMenu(var1, var2);
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof Bundle) {
         Bundle var3 = (Bundle)var1;
         SparseArray var2 = var3.getSparseParcelableArray("android:menu:list");
         if (var2 != null) {
            this.menuView.restoreHierarchyState(var2);
         }

         Bundle var5 = var3.getBundle("android:menu:adapter");
         if (var5 != null) {
            this.adapter.restoreInstanceState(var5);
         }

         SparseArray var4 = var3.getSparseParcelableArray("android:menu:header");
         if (var4 != null) {
            this.headerLayout.restoreHierarchyState(var4);
         }
      }

   }

   public Parcelable onSaveInstanceState() {
      Bundle var1 = new Bundle();
      SparseArray var2;
      if (this.menuView != null) {
         var2 = new SparseArray();
         this.menuView.saveHierarchyState(var2);
         var1.putSparseParcelableArray("android:menu:list", var2);
      }

      if (this.adapter != null) {
         var1.putBundle("android:menu:adapter", this.adapter.createInstanceState());
      }

      if (this.headerLayout != null) {
         var2 = new SparseArray();
         this.headerLayout.saveHierarchyState(var2);
         var1.putSparseParcelableArray("android:menu:header", var2);
      }

      return var1;
   }

   public boolean onSubMenuSelected(SubMenuBuilder var1) {
      return false;
   }

   public void setCallback(MenuPresenter.Callback var1) {
      this.callback = var1;
   }

   public void setCheckedItem(MenuItemImpl var1) {
      this.adapter.setCheckedItem(var1);
   }

   public void setId(int var1) {
      this.id = var1;
   }

   public void setItemBackground(Drawable var1) {
      this.itemBackground = var1;
      this.updateMenuView(false);
   }

   public void setItemHorizontalPadding(int var1) {
      this.itemHorizontalPadding = var1;
      this.updateMenuView(false);
   }

   public void setItemIconPadding(int var1) {
      this.itemIconPadding = var1;
      this.updateMenuView(false);
   }

   public void setItemIconTintList(ColorStateList var1) {
      this.iconTintList = var1;
      this.updateMenuView(false);
   }

   public void setItemTextAppearance(int var1) {
      this.textAppearance = var1;
      this.textAppearanceSet = true;
      this.updateMenuView(false);
   }

   public void setItemTextColor(ColorStateList var1) {
      this.textColor = var1;
      this.updateMenuView(false);
   }

   public void setUpdateSuspended(boolean var1) {
      if (this.adapter != null) {
         this.adapter.setUpdateSuspended(var1);
      }

   }

   public void updateMenuView(boolean var1) {
      if (this.adapter != null) {
         this.adapter.update();
      }

   }

   private static class HeaderViewHolder extends NavigationMenuPresenter.ViewHolder {
      public HeaderViewHolder(View var1) {
         super(var1);
      }
   }

   private class NavigationMenuAdapter extends RecyclerView.Adapter {
      private MenuItemImpl checkedItem;
      private final ArrayList items = new ArrayList();
      private boolean updateSuspended;

      NavigationMenuAdapter() {
         this.prepareMenuItems();
      }

      private void appendTransparentIconIfMissing(int var1, int var2) {
         while(var1 < var2) {
            ((NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(var1)).needsEmptyIcon = true;
            ++var1;
         }

      }

      private void prepareMenuItems() {
         if (!this.updateSuspended) {
            this.updateSuspended = true;
            this.items.clear();
            this.items.add(new NavigationMenuPresenter.NavigationMenuHeaderItem());
            int var1 = NavigationMenuPresenter.this.menu.getVisibleItems().size();
            int var2 = 0;
            int var3 = -1;
            boolean var4 = false;

            int var10;
            for(int var5 = 0; var2 < var1; var5 = var10) {
               MenuItemImpl var6 = (MenuItemImpl)NavigationMenuPresenter.this.menu.getVisibleItems().get(var2);
               if (var6.isChecked()) {
                  this.setCheckedItem(var6);
               }

               if (var6.isCheckable()) {
                  var6.setExclusiveCheckable(false);
               }

               int var8;
               boolean var9;
               if (!var6.hasSubMenu()) {
                  var8 = var6.getGroupId();
                  int var16;
                  if (var8 != var3) {
                     var5 = this.items.size();
                     if (var6.getIcon() != null) {
                        var9 = true;
                     } else {
                        var9 = false;
                     }

                     var16 = var5;
                     if (var2 != 0) {
                        var16 = var5 + 1;
                        this.items.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, NavigationMenuPresenter.this.paddingSeparator));
                     }
                  } else {
                     var9 = var4;
                     var16 = var5;
                     if (!var4) {
                        var9 = var4;
                        var16 = var5;
                        if (var6.getIcon() != null) {
                           this.appendTransparentIconIfMissing(var5, this.items.size());
                           var9 = true;
                           var16 = var5;
                        }
                     }
                  }

                  NavigationMenuPresenter.NavigationMenuTextItem var17 = new NavigationMenuPresenter.NavigationMenuTextItem(var6);
                  var17.needsEmptyIcon = var9;
                  this.items.add(var17);
                  var10 = var16;
               } else {
                  SubMenu var7 = var6.getSubMenu();
                  var8 = var3;
                  var9 = var4;
                  var10 = var5;
                  if (var7.hasVisibleItems()) {
                     if (var2 != 0) {
                        this.items.add(new NavigationMenuPresenter.NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, 0));
                     }

                     this.items.add(new NavigationMenuPresenter.NavigationMenuTextItem(var6));
                     int var11 = this.items.size();
                     int var12 = var7.size();
                     var8 = 0;

                     boolean var13;
                     boolean var15;
                     for(var13 = false; var8 < var12; var13 = var15) {
                        MenuItemImpl var14 = (MenuItemImpl)var7.getItem(var8);
                        var15 = var13;
                        if (var14.isVisible()) {
                           var15 = var13;
                           if (!var13) {
                              var15 = var13;
                              if (var14.getIcon() != null) {
                                 var15 = true;
                              }
                           }

                           if (var14.isCheckable()) {
                              var14.setExclusiveCheckable(false);
                           }

                           if (var6.isChecked()) {
                              this.setCheckedItem(var6);
                           }

                           this.items.add(new NavigationMenuPresenter.NavigationMenuTextItem(var14));
                        }

                        ++var8;
                     }

                     var8 = var3;
                     var9 = var4;
                     var10 = var5;
                     if (var13) {
                        this.appendTransparentIconIfMissing(var11, this.items.size());
                        var8 = var3;
                        var9 = var4;
                        var10 = var5;
                     }
                  }
               }

               ++var2;
               var3 = var8;
               var4 = var9;
            }

            this.updateSuspended = false;
         }
      }

      public Bundle createInstanceState() {
         Bundle var1 = new Bundle();
         if (this.checkedItem != null) {
            var1.putInt("android:menu:checked", this.checkedItem.getItemId());
         }

         SparseArray var2 = new SparseArray();
         int var3 = 0;

         for(int var4 = this.items.size(); var3 < var4; ++var3) {
            NavigationMenuPresenter.NavigationMenuItem var5 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var3);
            if (var5 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
               MenuItemImpl var6 = ((NavigationMenuPresenter.NavigationMenuTextItem)var5).getMenuItem();
               View var8;
               if (var6 != null) {
                  var8 = var6.getActionView();
               } else {
                  var8 = null;
               }

               if (var8 != null) {
                  ParcelableSparseArray var7 = new ParcelableSparseArray();
                  var8.saveHierarchyState(var7);
                  var2.put(var6.getItemId(), var7);
               }
            }
         }

         var1.putSparseParcelableArray("android:menu:action_views", var2);
         return var1;
      }

      public MenuItemImpl getCheckedItem() {
         return this.checkedItem;
      }

      public int getItemCount() {
         return this.items.size();
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         NavigationMenuPresenter.NavigationMenuItem var2 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var1);
         if (var2 instanceof NavigationMenuPresenter.NavigationMenuSeparatorItem) {
            return 2;
         } else if (var2 instanceof NavigationMenuPresenter.NavigationMenuHeaderItem) {
            return 3;
         } else if (var2 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
            return ((NavigationMenuPresenter.NavigationMenuTextItem)var2).getMenuItem().hasSubMenu() ? 1 : 0;
         } else {
            throw new RuntimeException("Unknown item type.");
         }
      }

      public void onBindViewHolder(NavigationMenuPresenter.ViewHolder var1, int var2) {
         switch(this.getItemViewType(var2)) {
         case 0:
            NavigationMenuItemView var6 = (NavigationMenuItemView)var1.itemView;
            var6.setIconTintList(NavigationMenuPresenter.this.iconTintList);
            if (NavigationMenuPresenter.this.textAppearanceSet) {
               var6.setTextAppearance(NavigationMenuPresenter.this.textAppearance);
            }

            if (NavigationMenuPresenter.this.textColor != null) {
               var6.setTextColor(NavigationMenuPresenter.this.textColor);
            }

            Drawable var4;
            if (NavigationMenuPresenter.this.itemBackground != null) {
               var4 = NavigationMenuPresenter.this.itemBackground.getConstantState().newDrawable();
            } else {
               var4 = null;
            }

            ViewCompat.setBackground(var6, var4);
            NavigationMenuPresenter.NavigationMenuTextItem var5 = (NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(var2);
            var6.setNeedsEmptyIcon(var5.needsEmptyIcon);
            var6.setHorizontalPadding(NavigationMenuPresenter.this.itemHorizontalPadding);
            var6.setIconPadding(NavigationMenuPresenter.this.itemIconPadding);
            var6.initialize(var5.getMenuItem(), 0);
            break;
         case 1:
            ((TextView)var1.itemView).setText(((NavigationMenuPresenter.NavigationMenuTextItem)this.items.get(var2)).getMenuItem().getTitle());
            break;
         case 2:
            NavigationMenuPresenter.NavigationMenuSeparatorItem var3 = (NavigationMenuPresenter.NavigationMenuSeparatorItem)this.items.get(var2);
            var1.itemView.setPadding(0, var3.getPaddingTop(), 0, var3.getPaddingBottom());
         }

      }

      public NavigationMenuPresenter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         switch(var2) {
         case 0:
            return new NavigationMenuPresenter.NormalViewHolder(NavigationMenuPresenter.this.layoutInflater, var1, NavigationMenuPresenter.this.onClickListener);
         case 1:
            return new NavigationMenuPresenter.SubheaderViewHolder(NavigationMenuPresenter.this.layoutInflater, var1);
         case 2:
            return new NavigationMenuPresenter.SeparatorViewHolder(NavigationMenuPresenter.this.layoutInflater, var1);
         case 3:
            return new NavigationMenuPresenter.HeaderViewHolder(NavigationMenuPresenter.this.headerLayout);
         default:
            return null;
         }
      }

      public void onViewRecycled(NavigationMenuPresenter.ViewHolder var1) {
         if (var1 instanceof NavigationMenuPresenter.NormalViewHolder) {
            ((NavigationMenuItemView)var1.itemView).recycle();
         }

      }

      public void restoreInstanceState(Bundle var1) {
         byte var2 = 0;
         int var3 = var1.getInt("android:menu:checked", 0);
         int var5;
         NavigationMenuPresenter.NavigationMenuItem var6;
         if (var3 != 0) {
            this.updateSuspended = true;
            int var4 = this.items.size();

            for(var5 = 0; var5 < var4; ++var5) {
               var6 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var5);
               if (var6 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
                  MenuItemImpl var9 = ((NavigationMenuPresenter.NavigationMenuTextItem)var6).getMenuItem();
                  if (var9 != null && var9.getItemId() == var3) {
                     this.setCheckedItem(var9);
                     break;
                  }
               }
            }

            this.updateSuspended = false;
            this.prepareMenuItems();
         }

         SparseArray var8 = var1.getSparseParcelableArray("android:menu:action_views");
         if (var8 != null) {
            var3 = this.items.size();

            for(var5 = var2; var5 < var3; ++var5) {
               var6 = (NavigationMenuPresenter.NavigationMenuItem)this.items.get(var5);
               if (var6 instanceof NavigationMenuPresenter.NavigationMenuTextItem) {
                  MenuItemImpl var7 = ((NavigationMenuPresenter.NavigationMenuTextItem)var6).getMenuItem();
                  if (var7 != null) {
                     View var10 = var7.getActionView();
                     if (var10 != null) {
                        ParcelableSparseArray var11 = (ParcelableSparseArray)var8.get(var7.getItemId());
                        if (var11 != null) {
                           var10.restoreHierarchyState(var11);
                        }
                     }
                  }
               }
            }
         }

      }

      public void setCheckedItem(MenuItemImpl var1) {
         if (this.checkedItem != var1 && var1.isCheckable()) {
            if (this.checkedItem != null) {
               this.checkedItem.setChecked(false);
            }

            this.checkedItem = var1;
            var1.setChecked(true);
         }
      }

      public void setUpdateSuspended(boolean var1) {
         this.updateSuspended = var1;
      }

      public void update() {
         this.prepareMenuItems();
         this.notifyDataSetChanged();
      }
   }

   private static class NavigationMenuHeaderItem implements NavigationMenuPresenter.NavigationMenuItem {
      NavigationMenuHeaderItem() {
      }
   }

   private interface NavigationMenuItem {
   }

   private static class NavigationMenuSeparatorItem implements NavigationMenuPresenter.NavigationMenuItem {
      private final int paddingBottom;
      private final int paddingTop;

      public NavigationMenuSeparatorItem(int var1, int var2) {
         this.paddingTop = var1;
         this.paddingBottom = var2;
      }

      public int getPaddingBottom() {
         return this.paddingBottom;
      }

      public int getPaddingTop() {
         return this.paddingTop;
      }
   }

   private static class NavigationMenuTextItem implements NavigationMenuPresenter.NavigationMenuItem {
      private final MenuItemImpl menuItem;
      boolean needsEmptyIcon;

      NavigationMenuTextItem(MenuItemImpl var1) {
         this.menuItem = var1;
      }

      public MenuItemImpl getMenuItem() {
         return this.menuItem;
      }
   }

   private static class NormalViewHolder extends NavigationMenuPresenter.ViewHolder {
      public NormalViewHolder(LayoutInflater var1, ViewGroup var2, OnClickListener var3) {
         super(var1.inflate(R.layout.design_navigation_item, var2, false));
         this.itemView.setOnClickListener(var3);
      }
   }

   private static class SeparatorViewHolder extends NavigationMenuPresenter.ViewHolder {
      public SeparatorViewHolder(LayoutInflater var1, ViewGroup var2) {
         super(var1.inflate(R.layout.design_navigation_item_separator, var2, false));
      }
   }

   private static class SubheaderViewHolder extends NavigationMenuPresenter.ViewHolder {
      public SubheaderViewHolder(LayoutInflater var1, ViewGroup var2) {
         super(var1.inflate(R.layout.design_navigation_item_subheader, var2, false));
      }
   }

   private abstract static class ViewHolder extends RecyclerView.ViewHolder {
      public ViewHolder(View var1) {
         super(var1);
      }
   }
}
