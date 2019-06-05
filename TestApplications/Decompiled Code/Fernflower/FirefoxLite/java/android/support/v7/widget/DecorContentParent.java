package android.support.v7.widget;

import android.support.v7.view.menu.MenuPresenter;
import android.view.Menu;
import android.view.Window.Callback;

public interface DecorContentParent {
   boolean canShowOverflowMenu();

   void dismissPopups();

   boolean hideOverflowMenu();

   void initFeature(int var1);

   boolean isOverflowMenuShowPending();

   boolean isOverflowMenuShowing();

   void setMenu(Menu var1, MenuPresenter.Callback var2);

   void setMenuPrepared();

   void setWindowCallback(Callback var1);

   void setWindowTitle(CharSequence var1);

   boolean showOverflowMenu();
}
