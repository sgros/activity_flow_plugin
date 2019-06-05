package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window.Callback;

public interface DecorToolbar {
   boolean canShowOverflowMenu();

   void collapseActionView();

   void dismissPopupMenus();

   Context getContext();

   int getDisplayOptions();

   Menu getMenu();

   int getNavigationMode();

   CharSequence getTitle();

   ViewGroup getViewGroup();

   boolean hasExpandedActionView();

   boolean hideOverflowMenu();

   void initIndeterminateProgress();

   void initProgress();

   boolean isOverflowMenuShowPending();

   boolean isOverflowMenuShowing();

   void setCollapsible(boolean var1);

   void setDisplayOptions(int var1);

   void setEmbeddedTabView(ScrollingTabContainerView var1);

   void setHomeButtonEnabled(boolean var1);

   void setIcon(int var1);

   void setIcon(Drawable var1);

   void setLogo(int var1);

   void setMenu(Menu var1, MenuPresenter.Callback var2);

   void setMenuCallbacks(MenuPresenter.Callback var1, MenuBuilder.Callback var2);

   void setMenuPrepared();

   void setNavigationIcon(Drawable var1);

   void setVisibility(int var1);

   void setWindowCallback(Callback var1);

   void setWindowTitle(CharSequence var1);

   ViewPropertyAnimatorCompat setupAnimatorToVisibility(int var1, long var2);

   boolean showOverflowMenu();
}
