package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public class MenuPopupWindow extends ListPopupWindow implements MenuItemHoverListener {
   private static Method sSetTouchModalMethod;
   private MenuItemHoverListener mHoverListener;

   static {
      try {
         sSetTouchModalMethod = PopupWindow.class.getDeclaredMethod("setTouchModal", Boolean.TYPE);
      } catch (NoSuchMethodException var1) {
         Log.i("MenuPopupWindow", "Could not find method setTouchModal() on PopupWindow. Oh well.");
      }

   }

   public MenuPopupWindow(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   DropDownListView createDropDownListView(Context var1, boolean var2) {
      MenuPopupWindow.MenuDropDownListView var3 = new MenuPopupWindow.MenuDropDownListView(var1, var2);
      var3.setHoverListener(this);
      return var3;
   }

   public void onItemHoverEnter(MenuBuilder var1, MenuItem var2) {
      if (this.mHoverListener != null) {
         this.mHoverListener.onItemHoverEnter(var1, var2);
      }

   }

   public void onItemHoverExit(MenuBuilder var1, MenuItem var2) {
      if (this.mHoverListener != null) {
         this.mHoverListener.onItemHoverExit(var1, var2);
      }

   }

   public void setEnterTransition(Object var1) {
      if (VERSION.SDK_INT >= 23) {
         this.mPopup.setEnterTransition((Transition)var1);
      }

   }

   public void setExitTransition(Object var1) {
      if (VERSION.SDK_INT >= 23) {
         this.mPopup.setExitTransition((Transition)var1);
      }

   }

   public void setHoverListener(MenuItemHoverListener var1) {
      this.mHoverListener = var1;
   }

   public void setTouchModal(boolean var1) {
      if (sSetTouchModalMethod != null) {
         try {
            sSetTouchModalMethod.invoke(this.mPopup, var1);
         } catch (Exception var3) {
            Log.i("MenuPopupWindow", "Could not invoke setTouchModal() on PopupWindow. Oh well.");
         }
      }

   }

   public static class MenuDropDownListView extends DropDownListView {
      final int mAdvanceKey;
      private MenuItemHoverListener mHoverListener;
      private MenuItem mHoveredMenuItem;
      final int mRetreatKey;

      public MenuDropDownListView(Context var1, boolean var2) {
         super(var1, var2);
         Configuration var3 = var1.getResources().getConfiguration();
         if (VERSION.SDK_INT >= 17 && 1 == var3.getLayoutDirection()) {
            this.mAdvanceKey = 21;
            this.mRetreatKey = 22;
         } else {
            this.mAdvanceKey = 22;
            this.mRetreatKey = 21;
         }

      }

      public boolean onHoverEvent(MotionEvent var1) {
         if (this.mHoverListener != null) {
            ListAdapter var2 = this.getAdapter();
            int var3;
            MenuAdapter var4;
            if (var2 instanceof HeaderViewListAdapter) {
               HeaderViewListAdapter var7 = (HeaderViewListAdapter)var2;
               var3 = var7.getHeadersCount();
               var4 = (MenuAdapter)var7.getWrappedAdapter();
            } else {
               var3 = 0;
               var4 = (MenuAdapter)var2;
            }

            MenuItem var5 = null;
            MenuItemImpl var8 = var5;
            if (var1.getAction() != 10) {
               int var6 = this.pointToPosition((int)var1.getX(), (int)var1.getY());
               var8 = var5;
               if (var6 != -1) {
                  var3 = var6 - var3;
                  var8 = var5;
                  if (var3 >= 0) {
                     var8 = var5;
                     if (var3 < var4.getCount()) {
                        var8 = var4.getItem(var3);
                     }
                  }
               }
            }

            var5 = this.mHoveredMenuItem;
            if (var5 != var8) {
               MenuBuilder var9 = var4.getAdapterMenu();
               if (var5 != null) {
                  this.mHoverListener.onItemHoverExit(var9, var5);
               }

               this.mHoveredMenuItem = var8;
               if (var8 != null) {
                  this.mHoverListener.onItemHoverEnter(var9, var8);
               }
            }
         }

         return super.onHoverEvent(var1);
      }

      public boolean onKeyDown(int var1, KeyEvent var2) {
         ListMenuItemView var3 = (ListMenuItemView)this.getSelectedView();
         if (var3 != null && var1 == this.mAdvanceKey) {
            if (var3.isEnabled() && var3.getItemData().hasSubMenu()) {
               this.performItemClick(var3, this.getSelectedItemPosition(), this.getSelectedItemId());
            }

            return true;
         } else if (var3 != null && var1 == this.mRetreatKey) {
            this.setSelection(-1);
            ((MenuAdapter)this.getAdapter()).getAdapterMenu().close(false);
            return true;
         } else {
            return super.onKeyDown(var1, var2);
         }
      }

      public void setHoverListener(MenuItemHoverListener var1) {
         this.mHoverListener = var1;
      }
   }
}
