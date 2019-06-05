package android.support.p004v7.widget;

import android.support.p004v7.view.menu.MenuPresenter.Callback;
import android.view.Menu;
import android.view.Window;

/* renamed from: android.support.v7.widget.DecorContentParent */
public interface DecorContentParent {
    boolean canShowOverflowMenu();

    void dismissPopups();

    boolean hideOverflowMenu();

    void initFeature(int i);

    boolean isOverflowMenuShowPending();

    boolean isOverflowMenuShowing();

    void setMenu(Menu menu, Callback callback);

    void setMenuPrepared();

    void setWindowCallback(Window.Callback callback);

    void setWindowTitle(CharSequence charSequence);

    boolean showOverflowMenu();
}
