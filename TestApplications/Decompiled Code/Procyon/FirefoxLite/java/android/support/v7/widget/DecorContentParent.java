// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.view.Window$Callback;
import android.support.v7.view.menu.MenuPresenter;
import android.view.Menu;

public interface DecorContentParent
{
    boolean canShowOverflowMenu();
    
    void dismissPopups();
    
    boolean hideOverflowMenu();
    
    void initFeature(final int p0);
    
    boolean isOverflowMenuShowPending();
    
    boolean isOverflowMenuShowing();
    
    void setMenu(final Menu p0, final MenuPresenter.Callback p1);
    
    void setMenuPrepared();
    
    void setWindowCallback(final Window$Callback p0);
    
    void setWindowTitle(final CharSequence p0);
    
    boolean showOverflowMenu();
}
