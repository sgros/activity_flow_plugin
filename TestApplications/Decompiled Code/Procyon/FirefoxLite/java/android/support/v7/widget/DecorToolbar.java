// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.Window$Callback;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Menu;
import android.content.Context;

public interface DecorToolbar
{
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
    
    void setCollapsible(final boolean p0);
    
    void setDisplayOptions(final int p0);
    
    void setEmbeddedTabView(final ScrollingTabContainerView p0);
    
    void setHomeButtonEnabled(final boolean p0);
    
    void setIcon(final int p0);
    
    void setIcon(final Drawable p0);
    
    void setLogo(final int p0);
    
    void setMenu(final Menu p0, final MenuPresenter.Callback p1);
    
    void setMenuCallbacks(final MenuPresenter.Callback p0, final MenuBuilder.Callback p1);
    
    void setMenuPrepared();
    
    void setNavigationIcon(final Drawable p0);
    
    void setVisibility(final int p0);
    
    void setWindowCallback(final Window$Callback p0);
    
    void setWindowTitle(final CharSequence p0);
    
    ViewPropertyAnimatorCompat setupAnimatorToVisibility(final int p0, final long p1);
    
    boolean showOverflowMenu();
}
