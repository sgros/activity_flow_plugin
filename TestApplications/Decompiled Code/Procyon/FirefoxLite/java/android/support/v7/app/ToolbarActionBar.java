// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.app;

import android.support.v7.view.WindowCallbackWrapper;
import android.graphics.drawable.Drawable;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.content.res.Configuration;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.view.Menu;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.MenuItem;
import android.view.Window$Callback;
import java.util.ArrayList;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.DecorToolbar;

class ToolbarActionBar extends ActionBar
{
    DecorToolbar mDecorToolbar;
    private boolean mLastMenuVisibility;
    private boolean mMenuCallbackSet;
    private final Toolbar.OnMenuItemClickListener mMenuClicker;
    private final Runnable mMenuInvalidator;
    private ArrayList<OnMenuVisibilityListener> mMenuVisibilityListeners;
    boolean mToolbarMenuPrepared;
    Window$Callback mWindowCallback;
    
    ToolbarActionBar(final Toolbar toolbar, final CharSequence windowTitle, final Window$Callback window$Callback) {
        this.mMenuVisibilityListeners = new ArrayList<OnMenuVisibilityListener>();
        this.mMenuInvalidator = new Runnable() {
            @Override
            public void run() {
                ToolbarActionBar.this.populateOptionsMenu();
            }
        };
        this.mMenuClicker = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                return ToolbarActionBar.this.mWindowCallback.onMenuItemSelected(0, menuItem);
            }
        };
        this.mDecorToolbar = new ToolbarWidgetWrapper(toolbar, false);
        this.mWindowCallback = (Window$Callback)new ToolbarCallbackWrapper(window$Callback);
        this.mDecorToolbar.setWindowCallback(this.mWindowCallback);
        toolbar.setOnMenuItemClickListener(this.mMenuClicker);
        this.mDecorToolbar.setWindowTitle(windowTitle);
    }
    
    private Menu getMenu() {
        if (!this.mMenuCallbackSet) {
            this.mDecorToolbar.setMenuCallbacks(new ActionMenuPresenterCallback(), new MenuBuilderCallback());
            this.mMenuCallbackSet = true;
        }
        return this.mDecorToolbar.getMenu();
    }
    
    @Override
    public boolean closeOptionsMenu() {
        return this.mDecorToolbar.hideOverflowMenu();
    }
    
    @Override
    public boolean collapseActionView() {
        if (this.mDecorToolbar.hasExpandedActionView()) {
            this.mDecorToolbar.collapseActionView();
            return true;
        }
        return false;
    }
    
    @Override
    public void dispatchMenuVisibilityChanged(final boolean mLastMenuVisibility) {
        if (mLastMenuVisibility == this.mLastMenuVisibility) {
            return;
        }
        this.mLastMenuVisibility = mLastMenuVisibility;
        for (int size = this.mMenuVisibilityListeners.size(), i = 0; i < size; ++i) {
            this.mMenuVisibilityListeners.get(i).onMenuVisibilityChanged(mLastMenuVisibility);
        }
    }
    
    @Override
    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }
    
    @Override
    public Context getThemedContext() {
        return this.mDecorToolbar.getContext();
    }
    
    public Window$Callback getWrappedWindowCallback() {
        return this.mWindowCallback;
    }
    
    @Override
    public boolean invalidateOptionsMenu() {
        this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
        ViewCompat.postOnAnimation((View)this.mDecorToolbar.getViewGroup(), this.mMenuInvalidator);
        return true;
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }
    
    @Override
    void onDestroy() {
        this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
    }
    
    @Override
    public boolean onKeyShortcut(final int n, final KeyEvent keyEvent) {
        final Menu menu = this.getMenu();
        if (menu != null) {
            int deviceId;
            if (keyEvent != null) {
                deviceId = keyEvent.getDeviceId();
            }
            else {
                deviceId = -1;
            }
            final int keyboardType = KeyCharacterMap.load(deviceId).getKeyboardType();
            boolean qwertyMode = true;
            if (keyboardType == 1) {
                qwertyMode = false;
            }
            menu.setQwertyMode(qwertyMode);
            return menu.performShortcut(n, keyEvent, 0);
        }
        return false;
    }
    
    @Override
    public boolean onMenuKeyEvent(final KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1) {
            this.openOptionsMenu();
        }
        return true;
    }
    
    @Override
    public boolean openOptionsMenu() {
        return this.mDecorToolbar.showOverflowMenu();
    }
    
    void populateOptionsMenu() {
        final Menu menu = this.getMenu();
        MenuBuilder menuBuilder;
        if (menu instanceof MenuBuilder) {
            menuBuilder = (MenuBuilder)menu;
        }
        else {
            menuBuilder = null;
        }
        if (menuBuilder != null) {
            menuBuilder.stopDispatchingItemsChanged();
        }
        try {
            menu.clear();
            if (!this.mWindowCallback.onCreatePanelMenu(0, menu) || !this.mWindowCallback.onPreparePanel(0, (View)null, menu)) {
                menu.clear();
            }
        }
        finally {
            if (menuBuilder != null) {
                menuBuilder.startDispatchingItemsChanged();
            }
        }
    }
    
    @Override
    public void setDefaultDisplayHomeAsUpEnabled(final boolean b) {
    }
    
    @Override
    public void setDisplayHomeAsUpEnabled(final boolean b) {
        int n;
        if (b) {
            n = 4;
        }
        else {
            n = 0;
        }
        this.setDisplayOptions(n, 4);
    }
    
    public void setDisplayOptions(final int n, final int n2) {
        this.mDecorToolbar.setDisplayOptions((n & n2) | (n2 & this.mDecorToolbar.getDisplayOptions()));
    }
    
    @Override
    public void setDisplayShowHomeEnabled(final boolean b) {
        int n;
        if (b) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.setDisplayOptions(n, 2);
    }
    
    @Override
    public void setElevation(final float n) {
        ViewCompat.setElevation((View)this.mDecorToolbar.getViewGroup(), n);
    }
    
    @Override
    public void setHomeAsUpIndicator(final Drawable navigationIcon) {
        this.mDecorToolbar.setNavigationIcon(navigationIcon);
    }
    
    @Override
    public void setHomeButtonEnabled(final boolean b) {
    }
    
    @Override
    public void setShowHideAnimationEnabled(final boolean b) {
    }
    
    @Override
    public void setWindowTitle(final CharSequence windowTitle) {
        this.mDecorToolbar.setWindowTitle(windowTitle);
    }
    
    private final class ActionMenuPresenterCallback implements Callback
    {
        private boolean mClosingActionMenu;
        
        ActionMenuPresenterCallback() {
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
            if (this.mClosingActionMenu) {
                return;
            }
            this.mClosingActionMenu = true;
            ToolbarActionBar.this.mDecorToolbar.dismissPopupMenus();
            if (ToolbarActionBar.this.mWindowCallback != null) {
                ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, (Menu)menuBuilder);
            }
            this.mClosingActionMenu = false;
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            if (ToolbarActionBar.this.mWindowCallback != null) {
                ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, (Menu)menuBuilder);
                return true;
            }
            return false;
        }
    }
    
    private final class MenuBuilderCallback implements Callback
    {
        MenuBuilderCallback() {
        }
        
        @Override
        public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
            return false;
        }
        
        @Override
        public void onMenuModeChange(final MenuBuilder menuBuilder) {
            if (ToolbarActionBar.this.mWindowCallback != null) {
                if (ToolbarActionBar.this.mDecorToolbar.isOverflowMenuShowing()) {
                    ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, (Menu)menuBuilder);
                }
                else if (ToolbarActionBar.this.mWindowCallback.onPreparePanel(0, (View)null, (Menu)menuBuilder)) {
                    ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, (Menu)menuBuilder);
                }
            }
        }
    }
    
    private class ToolbarCallbackWrapper extends WindowCallbackWrapper
    {
        public ToolbarCallbackWrapper(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public View onCreatePanelView(final int n) {
            if (n == 0) {
                return new View(ToolbarActionBar.this.mDecorToolbar.getContext());
            }
            return super.onCreatePanelView(n);
        }
        
        @Override
        public boolean onPreparePanel(final int n, final View view, final Menu menu) {
            final boolean onPreparePanel = super.onPreparePanel(n, view, menu);
            if (onPreparePanel && !ToolbarActionBar.this.mToolbarMenuPrepared) {
                ToolbarActionBar.this.mDecorToolbar.setMenuPrepared();
                ToolbarActionBar.this.mToolbarMenuPrepared = true;
            }
            return onPreparePanel;
        }
    }
}
