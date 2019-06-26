package androidx.appcompat.view.menu;

public interface MenuView {
   public interface ItemView {
      MenuItemImpl getItemData();

      void initialize(MenuItemImpl var1, int var2);

      boolean prefersCondensedTitle();
   }
}
