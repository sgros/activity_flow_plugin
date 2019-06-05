package android.support.p004v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.p001v4.view.ViewPropertyAnimatorCompat;
import android.support.p004v7.view.menu.MenuBuilder;
import android.support.p004v7.view.menu.MenuPresenter.Callback;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;

/* renamed from: android.support.v7.widget.DecorToolbar */
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

    void setCollapsible(boolean z);

    void setDisplayOptions(int i);

    void setEmbeddedTabView(ScrollingTabContainerView scrollingTabContainerView);

    void setHomeButtonEnabled(boolean z);

    void setIcon(int i);

    void setIcon(Drawable drawable);

    void setLogo(int i);

    void setMenu(Menu menu, Callback callback);

    void setMenuCallbacks(Callback callback, MenuBuilder.Callback callback2);

    void setMenuPrepared();

    void setNavigationIcon(Drawable drawable);

    void setVisibility(int i);

    void setWindowCallback(Window.Callback callback);

    void setWindowTitle(CharSequence charSequence);

    ViewPropertyAnimatorCompat setupAnimatorToVisibility(int i, long j);

    boolean showOverflowMenu();
}
