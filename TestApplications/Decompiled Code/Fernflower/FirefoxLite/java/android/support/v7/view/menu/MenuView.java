package android.support.v7.view.menu;

public interface MenuView {
   void initialize(MenuBuilder var1);

   public interface ItemView {
      MenuItemImpl getItemData();

      void initialize(MenuItemImpl var1, int var2);

      boolean prefersCondensedTitle();
   }
}
