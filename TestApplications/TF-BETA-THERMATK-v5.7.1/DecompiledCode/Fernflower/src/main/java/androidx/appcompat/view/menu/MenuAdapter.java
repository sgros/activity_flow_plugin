package androidx.appcompat.view.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
   MenuBuilder mAdapterMenu;
   private int mExpandedIndex = -1;
   private boolean mForceShowIcon;
   private final LayoutInflater mInflater;
   private final int mItemLayoutRes;
   private final boolean mOverflowOnly;

   public MenuAdapter(MenuBuilder var1, LayoutInflater var2, boolean var3, int var4) {
      this.mOverflowOnly = var3;
      this.mInflater = var2;
      this.mAdapterMenu = var1;
      this.mItemLayoutRes = var4;
      this.findExpandedIndex();
   }

   void findExpandedIndex() {
      MenuItemImpl var1 = this.mAdapterMenu.getExpandedItem();
      if (var1 != null) {
         ArrayList var2 = this.mAdapterMenu.getNonActionItems();
         int var3 = var2.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            if ((MenuItemImpl)var2.get(var4) == var1) {
               this.mExpandedIndex = var4;
               return;
            }
         }
      }

      this.mExpandedIndex = -1;
   }

   public MenuBuilder getAdapterMenu() {
      return this.mAdapterMenu;
   }

   public int getCount() {
      ArrayList var1;
      if (this.mOverflowOnly) {
         var1 = this.mAdapterMenu.getNonActionItems();
      } else {
         var1 = this.mAdapterMenu.getVisibleItems();
      }

      return this.mExpandedIndex < 0 ? var1.size() : var1.size() - 1;
   }

   public MenuItemImpl getItem(int var1) {
      ArrayList var2;
      if (this.mOverflowOnly) {
         var2 = this.mAdapterMenu.getNonActionItems();
      } else {
         var2 = this.mAdapterMenu.getVisibleItems();
      }

      int var3 = this.mExpandedIndex;
      int var4 = var1;
      if (var3 >= 0) {
         var4 = var1;
         if (var1 >= var3) {
            var4 = var1 + 1;
         }
      }

      return (MenuItemImpl)var2.get(var4);
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      View var4 = var2;
      if (var2 == null) {
         var4 = this.mInflater.inflate(this.mItemLayoutRes, var3, false);
      }

      int var5 = this.getItem(var1).getGroupId();
      int var6 = var1 - 1;
      if (var6 >= 0) {
         var6 = this.getItem(var6).getGroupId();
      } else {
         var6 = var5;
      }

      ListMenuItemView var9 = (ListMenuItemView)var4;
      boolean var7;
      if (this.mAdapterMenu.isGroupDividerEnabled() && var5 != var6) {
         var7 = true;
      } else {
         var7 = false;
      }

      var9.setGroupDividerEnabled(var7);
      MenuView.ItemView var8 = (MenuView.ItemView)var4;
      if (this.mForceShowIcon) {
         var9.setForceShowIcon(true);
      }

      var8.initialize(this.getItem(var1), 0);
      return var4;
   }

   public void notifyDataSetChanged() {
      this.findExpandedIndex();
      super.notifyDataSetChanged();
   }

   public void setForceShowIcon(boolean var1) {
      this.mForceShowIcon = var1;
   }
}
