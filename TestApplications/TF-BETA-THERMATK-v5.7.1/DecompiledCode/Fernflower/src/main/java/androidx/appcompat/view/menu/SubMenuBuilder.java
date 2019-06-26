package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class SubMenuBuilder extends MenuBuilder implements SubMenu {
   private MenuItemImpl mItem;
   private MenuBuilder mParentMenu;

   public SubMenuBuilder(Context var1, MenuBuilder var2, MenuItemImpl var3) {
      super(var1);
      this.mParentMenu = var2;
      this.mItem = var3;
   }

   public boolean collapseItemActionView(MenuItemImpl var1) {
      return this.mParentMenu.collapseItemActionView(var1);
   }

   boolean dispatchMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      boolean var3;
      if (!super.dispatchMenuItemSelected(var1, var2) && !this.mParentMenu.dispatchMenuItemSelected(var1, var2)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   public boolean expandItemActionView(MenuItemImpl var1) {
      return this.mParentMenu.expandItemActionView(var1);
   }

   public MenuItem getItem() {
      return this.mItem;
   }

   public Menu getParentMenu() {
      return this.mParentMenu;
   }

   public MenuBuilder getRootMenu() {
      return this.mParentMenu.getRootMenu();
   }

   public boolean isGroupDividerEnabled() {
      return this.mParentMenu.isGroupDividerEnabled();
   }

   public boolean isQwertyMode() {
      return this.mParentMenu.isQwertyMode();
   }

   public boolean isShortcutsVisible() {
      return this.mParentMenu.isShortcutsVisible();
   }

   public void setCallback(MenuBuilder.Callback var1) {
      this.mParentMenu.setCallback(var1);
   }

   public void setGroupDividerEnabled(boolean var1) {
      this.mParentMenu.setGroupDividerEnabled(var1);
   }

   public SubMenu setHeaderIcon(int var1) {
      super.setHeaderIconInt(var1);
      return (SubMenu)this;
   }

   public SubMenu setHeaderIcon(Drawable var1) {
      super.setHeaderIconInt(var1);
      return (SubMenu)this;
   }

   public SubMenu setHeaderTitle(int var1) {
      super.setHeaderTitleInt(var1);
      return (SubMenu)this;
   }

   public SubMenu setHeaderTitle(CharSequence var1) {
      super.setHeaderTitleInt(var1);
      return (SubMenu)this;
   }

   public SubMenu setHeaderView(View var1) {
      super.setHeaderViewInt(var1);
      return (SubMenu)this;
   }

   public SubMenu setIcon(int var1) {
      this.mItem.setIcon(var1);
      return this;
   }

   public SubMenu setIcon(Drawable var1) {
      this.mItem.setIcon(var1);
      return this;
   }

   public void setQwertyMode(boolean var1) {
      this.mParentMenu.setQwertyMode(var1);
   }
}
