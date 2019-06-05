package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window.Callback;
import java.util.ArrayList;

class ToolbarActionBar extends ActionBar {
   DecorToolbar mDecorToolbar;
   private boolean mLastMenuVisibility;
   private boolean mMenuCallbackSet;
   private final Toolbar.OnMenuItemClickListener mMenuClicker = new Toolbar.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem var1) {
         return ToolbarActionBar.this.mWindowCallback.onMenuItemSelected(0, var1);
      }
   };
   private final Runnable mMenuInvalidator = new Runnable() {
      public void run() {
         ToolbarActionBar.this.populateOptionsMenu();
      }
   };
   private ArrayList mMenuVisibilityListeners = new ArrayList();
   boolean mToolbarMenuPrepared;
   Callback mWindowCallback;

   ToolbarActionBar(Toolbar var1, CharSequence var2, Callback var3) {
      this.mDecorToolbar = new ToolbarWidgetWrapper(var1, false);
      this.mWindowCallback = new ToolbarActionBar.ToolbarCallbackWrapper(var3);
      this.mDecorToolbar.setWindowCallback(this.mWindowCallback);
      var1.setOnMenuItemClickListener(this.mMenuClicker);
      this.mDecorToolbar.setWindowTitle(var2);
   }

   private Menu getMenu() {
      if (!this.mMenuCallbackSet) {
         this.mDecorToolbar.setMenuCallbacks(new ToolbarActionBar.ActionMenuPresenterCallback(), new ToolbarActionBar.MenuBuilderCallback());
         this.mMenuCallbackSet = true;
      }

      return this.mDecorToolbar.getMenu();
   }

   public boolean closeOptionsMenu() {
      return this.mDecorToolbar.hideOverflowMenu();
   }

   public boolean collapseActionView() {
      if (this.mDecorToolbar.hasExpandedActionView()) {
         this.mDecorToolbar.collapseActionView();
         return true;
      } else {
         return false;
      }
   }

   public void dispatchMenuVisibilityChanged(boolean var1) {
      if (var1 != this.mLastMenuVisibility) {
         this.mLastMenuVisibility = var1;
         int var2 = this.mMenuVisibilityListeners.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            ((ActionBar.OnMenuVisibilityListener)this.mMenuVisibilityListeners.get(var3)).onMenuVisibilityChanged(var1);
         }

      }
   }

   public int getDisplayOptions() {
      return this.mDecorToolbar.getDisplayOptions();
   }

   public Context getThemedContext() {
      return this.mDecorToolbar.getContext();
   }

   public Callback getWrappedWindowCallback() {
      return this.mWindowCallback;
   }

   public boolean invalidateOptionsMenu() {
      this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
      ViewCompat.postOnAnimation(this.mDecorToolbar.getViewGroup(), this.mMenuInvalidator);
      return true;
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
   }

   void onDestroy() {
      this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
   }

   public boolean onKeyShortcut(int var1, KeyEvent var2) {
      Menu var3 = this.getMenu();
      if (var3 != null) {
         int var4;
         if (var2 != null) {
            var4 = var2.getDeviceId();
         } else {
            var4 = -1;
         }

         var4 = KeyCharacterMap.load(var4).getKeyboardType();
         boolean var5 = true;
         if (var4 == 1) {
            var5 = false;
         }

         var3.setQwertyMode(var5);
         return var3.performShortcut(var1, var2, 0);
      } else {
         return false;
      }
   }

   public boolean onMenuKeyEvent(KeyEvent var1) {
      if (var1.getAction() == 1) {
         this.openOptionsMenu();
      }

      return true;
   }

   public boolean openOptionsMenu() {
      return this.mDecorToolbar.showOverflowMenu();
   }

   void populateOptionsMenu() {
      Menu var1 = this.getMenu();
      MenuBuilder var2;
      if (var1 instanceof MenuBuilder) {
         var2 = (MenuBuilder)var1;
      } else {
         var2 = null;
      }

      if (var2 != null) {
         var2.stopDispatchingItemsChanged();
      }

      try {
         var1.clear();
         if (!this.mWindowCallback.onCreatePanelMenu(0, var1) || !this.mWindowCallback.onPreparePanel(0, (View)null, var1)) {
            var1.clear();
         }
      } finally {
         if (var2 != null) {
            var2.startDispatchingItemsChanged();
         }

      }

   }

   public void setDefaultDisplayHomeAsUpEnabled(boolean var1) {
   }

   public void setDisplayHomeAsUpEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 4;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 4);
   }

   public void setDisplayOptions(int var1, int var2) {
      int var3 = this.mDecorToolbar.getDisplayOptions();
      this.mDecorToolbar.setDisplayOptions(var1 & var2 | var2 & var3);
   }

   public void setDisplayShowHomeEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 2;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 2);
   }

   public void setElevation(float var1) {
      ViewCompat.setElevation(this.mDecorToolbar.getViewGroup(), var1);
   }

   public void setHomeAsUpIndicator(Drawable var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeButtonEnabled(boolean var1) {
   }

   public void setShowHideAnimationEnabled(boolean var1) {
   }

   public void setWindowTitle(CharSequence var1) {
      this.mDecorToolbar.setWindowTitle(var1);
   }

   private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      private boolean mClosingActionMenu;

      ActionMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         if (!this.mClosingActionMenu) {
            this.mClosingActionMenu = true;
            ToolbarActionBar.this.mDecorToolbar.dismissPopupMenus();
            if (ToolbarActionBar.this.mWindowCallback != null) {
               ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, var1);
            }

            this.mClosingActionMenu = false;
         }
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         if (ToolbarActionBar.this.mWindowCallback != null) {
            ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, var1);
            return true;
         } else {
            return false;
         }
      }
   }

   private final class MenuBuilderCallback implements MenuBuilder.Callback {
      MenuBuilderCallback() {
      }

      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         return false;
      }

      public void onMenuModeChange(MenuBuilder var1) {
         if (ToolbarActionBar.this.mWindowCallback != null) {
            if (ToolbarActionBar.this.mDecorToolbar.isOverflowMenuShowing()) {
               ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, var1);
            } else if (ToolbarActionBar.this.mWindowCallback.onPreparePanel(0, (View)null, var1)) {
               ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, var1);
            }
         }

      }
   }

   private class ToolbarCallbackWrapper extends WindowCallbackWrapper {
      public ToolbarCallbackWrapper(Callback var2) {
         super(var2);
      }

      public View onCreatePanelView(int var1) {
         return var1 == 0 ? new View(ToolbarActionBar.this.mDecorToolbar.getContext()) : super.onCreatePanelView(var1);
      }

      public boolean onPreparePanel(int var1, View var2, Menu var3) {
         boolean var4 = super.onPreparePanel(var1, var2, var3);
         if (var4 && !ToolbarActionBar.this.mToolbarMenuPrepared) {
            ToolbarActionBar.this.mDecorToolbar.setMenuPrepared();
            ToolbarActionBar.this.mToolbarMenuPrepared = true;
         }

         return var4;
      }
   }
}
