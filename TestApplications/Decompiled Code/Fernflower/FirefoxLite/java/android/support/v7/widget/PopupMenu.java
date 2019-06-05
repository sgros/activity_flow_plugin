package android.support.v7.widget;

import android.content.Context;
import android.support.v7.appcompat.R;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class PopupMenu {
   private final View mAnchor;
   private final Context mContext;
   private final MenuBuilder mMenu;
   PopupMenu.OnMenuItemClickListener mMenuItemClickListener;
   PopupMenu.OnDismissListener mOnDismissListener;
   final MenuPopupHelper mPopup;

   public PopupMenu(Context var1, View var2) {
      this(var1, var2, 0);
   }

   public PopupMenu(Context var1, View var2, int var3) {
      this(var1, var2, var3, R.attr.popupMenuStyle, 0);
   }

   public PopupMenu(Context var1, View var2, int var3, int var4, int var5) {
      this.mContext = var1;
      this.mAnchor = var2;
      this.mMenu = new MenuBuilder(var1);
      this.mMenu.setCallback(new MenuBuilder.Callback() {
         public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
            return PopupMenu.this.mMenuItemClickListener != null ? PopupMenu.this.mMenuItemClickListener.onMenuItemClick(var2) : false;
         }

         public void onMenuModeChange(MenuBuilder var1) {
         }
      });
      this.mPopup = new MenuPopupHelper(var1, this.mMenu, var2, false, var4, var5);
      this.mPopup.setGravity(var3);
      this.mPopup.setOnDismissListener(new android.widget.PopupWindow.OnDismissListener() {
         public void onDismiss() {
            if (PopupMenu.this.mOnDismissListener != null) {
               PopupMenu.this.mOnDismissListener.onDismiss(PopupMenu.this);
            }

         }
      });
   }

   public Menu getMenu() {
      return this.mMenu;
   }

   public MenuInflater getMenuInflater() {
      return new SupportMenuInflater(this.mContext);
   }

   public void inflate(int var1) {
      this.getMenuInflater().inflate(var1, this.mMenu);
   }

   public void setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener var1) {
      this.mMenuItemClickListener = var1;
   }

   public void show() {
      this.mPopup.show();
   }

   public interface OnDismissListener {
      void onDismiss(PopupMenu var1);
   }

   public interface OnMenuItemClickListener {
      boolean onMenuItemClick(MenuItem var1);
   }
}
