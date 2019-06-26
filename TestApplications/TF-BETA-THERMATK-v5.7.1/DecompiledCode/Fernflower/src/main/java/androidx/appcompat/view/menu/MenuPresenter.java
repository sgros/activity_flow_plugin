package androidx.appcompat.view.menu;

import android.content.Context;

public interface MenuPresenter {
   boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2);

   boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2);

   boolean flagActionItems();

   void initForMenu(Context var1, MenuBuilder var2);

   void onCloseMenu(MenuBuilder var1, boolean var2);

   boolean onSubMenuSelected(SubMenuBuilder var1);

   void setCallback(MenuPresenter.Callback var1);

   void updateMenuView(boolean var1);

   public interface Callback {
      void onCloseMenu(MenuBuilder var1, boolean var2);

      boolean onOpenSubMenu(MenuBuilder var1);
   }
}
