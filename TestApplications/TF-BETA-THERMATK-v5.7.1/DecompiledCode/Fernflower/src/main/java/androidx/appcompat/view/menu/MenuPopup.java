package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

abstract class MenuPopup implements ShowableListMenu, MenuPresenter, OnItemClickListener {
   private Rect mEpicenterBounds;

   protected static int measureIndividualMenuWidth(ListAdapter var0, ViewGroup var1, Context var2, int var3) {
      int var4 = 0;
      int var5 = MeasureSpec.makeMeasureSpec(0, 0);
      int var6 = MeasureSpec.makeMeasureSpec(0, 0);
      int var7 = var0.getCount();
      Object var8 = var1;
      View var14 = null;
      int var9 = 0;

      int var11;
      for(int var10 = 0; var4 < var7; var9 = var11) {
         var11 = var0.getItemViewType(var4);
         int var12 = var10;
         View var13 = var14;
         if (var11 != var10) {
            var13 = null;
            var12 = var11;
         }

         Object var15 = var8;
         if (var8 == null) {
            var15 = new FrameLayout(var2);
         }

         var13 = var0.getView(var4, var13, (ViewGroup)var15);
         var13.measure(var5, var6);
         var10 = var13.getMeasuredWidth();
         if (var10 >= var3) {
            return var3;
         }

         var11 = var9;
         if (var10 > var9) {
            var11 = var10;
         }

         ++var4;
         var10 = var12;
         var8 = var15;
         var14 = var13;
      }

      return var9;
   }

   protected static boolean shouldPreserveIconSpacing(MenuBuilder var0) {
      int var1 = var0.size();
      boolean var2 = false;
      int var3 = 0;

      boolean var4;
      while(true) {
         var4 = var2;
         if (var3 >= var1) {
            break;
         }

         MenuItem var5 = var0.getItem(var3);
         if (var5.isVisible() && var5.getIcon() != null) {
            var4 = true;
            break;
         }

         ++var3;
      }

      return var4;
   }

   protected static MenuAdapter toMenuAdapter(ListAdapter var0) {
      return var0 instanceof HeaderViewListAdapter ? (MenuAdapter)((HeaderViewListAdapter)var0).getWrappedAdapter() : (MenuAdapter)var0;
   }

   public abstract void addMenu(MenuBuilder var1);

   protected boolean closeMenuOnSubMenuOpened() {
      return true;
   }

   public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2) {
      return false;
   }

   public Rect getEpicenterBounds() {
      return this.mEpicenterBounds;
   }

   public void initForMenu(Context var1, MenuBuilder var2) {
   }

   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      ListAdapter var7 = (ListAdapter)var1.getAdapter();
      MenuBuilder var6 = toMenuAdapter(var7).mAdapterMenu;
      MenuItem var8 = (MenuItem)var7.getItem(var3);
      byte var9;
      if (this.closeMenuOnSubMenuOpened()) {
         var9 = 0;
      } else {
         var9 = 4;
      }

      var6.performItemAction(var8, this, var9);
   }

   public abstract void setAnchorView(View var1);

   public void setEpicenterBounds(Rect var1) {
      this.mEpicenterBounds = var1;
   }

   public abstract void setForceShowIcon(boolean var1);

   public abstract void setGravity(int var1);

   public abstract void setHorizontalOffset(int var1);

   public abstract void setOnDismissListener(OnDismissListener var1);

   public abstract void setShowTitle(boolean var1);

   public abstract void setVerticalOffset(int var1);
}
