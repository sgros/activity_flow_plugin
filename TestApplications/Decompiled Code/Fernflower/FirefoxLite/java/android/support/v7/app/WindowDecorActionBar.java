package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.ActionMode;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class WindowDecorActionBar extends ActionBar implements ActionBarOverlayLayout.ActionBarVisibilityCallback {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   private static final Interpolator sHideInterpolator = new AccelerateInterpolator();
   private static final Interpolator sShowInterpolator = new DecelerateInterpolator();
   WindowDecorActionBar.ActionModeImpl mActionMode;
   private Activity mActivity;
   ActionBarContainer mContainerView;
   boolean mContentAnimations = true;
   View mContentView;
   Context mContext;
   ActionBarContextView mContextView;
   private int mCurWindowVisibility = 0;
   ViewPropertyAnimatorCompatSet mCurrentShowAnim;
   DecorToolbar mDecorToolbar;
   ActionMode mDeferredDestroyActionMode;
   ActionMode.Callback mDeferredModeDestroyCallback;
   private Dialog mDialog;
   private boolean mDisplayHomeAsUpSet;
   private boolean mHasEmbeddedTabs;
   boolean mHiddenByApp;
   boolean mHiddenBySystem;
   final ViewPropertyAnimatorListener mHideListener = new ViewPropertyAnimatorListenerAdapter() {
      public void onAnimationEnd(View var1) {
         if (WindowDecorActionBar.this.mContentAnimations && WindowDecorActionBar.this.mContentView != null) {
            WindowDecorActionBar.this.mContentView.setTranslationY(0.0F);
            WindowDecorActionBar.this.mContainerView.setTranslationY(0.0F);
         }

         WindowDecorActionBar.this.mContainerView.setVisibility(8);
         WindowDecorActionBar.this.mContainerView.setTransitioning(false);
         WindowDecorActionBar.this.mCurrentShowAnim = null;
         WindowDecorActionBar.this.completeDeferredDestroyActionMode();
         if (WindowDecorActionBar.this.mOverlayLayout != null) {
            ViewCompat.requestApplyInsets(WindowDecorActionBar.this.mOverlayLayout);
         }

      }
   };
   boolean mHideOnContentScroll;
   private boolean mLastMenuVisibility;
   private ArrayList mMenuVisibilityListeners = new ArrayList();
   private boolean mNowShowing = true;
   ActionBarOverlayLayout mOverlayLayout;
   private int mSavedTabPosition = -1;
   private boolean mShowHideAnimationEnabled;
   final ViewPropertyAnimatorListener mShowListener = new ViewPropertyAnimatorListenerAdapter() {
      public void onAnimationEnd(View var1) {
         WindowDecorActionBar.this.mCurrentShowAnim = null;
         WindowDecorActionBar.this.mContainerView.requestLayout();
      }
   };
   private boolean mShowingForMode;
   ScrollingTabContainerView mTabScrollView;
   private ArrayList mTabs = new ArrayList();
   private Context mThemedContext;
   final ViewPropertyAnimatorUpdateListener mUpdateListener = new ViewPropertyAnimatorUpdateListener() {
      public void onAnimationUpdate(View var1) {
         ((View)WindowDecorActionBar.this.mContainerView.getParent()).invalidate();
      }
   };

   public WindowDecorActionBar(Activity var1, boolean var2) {
      this.mActivity = var1;
      View var3 = var1.getWindow().getDecorView();
      this.init(var3);
      if (!var2) {
         this.mContentView = var3.findViewById(16908290);
      }

   }

   public WindowDecorActionBar(Dialog var1) {
      this.mDialog = var1;
      this.init(var1.getWindow().getDecorView());
   }

   static boolean checkShowingFlags(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return true;
      } else {
         return !var0 && !var1;
      }
   }

   private DecorToolbar getDecorToolbar(View var1) {
      if (var1 instanceof DecorToolbar) {
         return (DecorToolbar)var1;
      } else if (var1 instanceof Toolbar) {
         return ((Toolbar)var1).getWrapper();
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Can't make a decor toolbar out of ");
         String var3;
         if (var1 != null) {
            var3 = var1.getClass().getSimpleName();
         } else {
            var3 = "null";
         }

         var2.append(var3);
         throw new IllegalStateException(var2.toString());
      }
   }

   private void hideForActionMode() {
      if (this.mShowingForMode) {
         this.mShowingForMode = false;
         if (this.mOverlayLayout != null) {
            this.mOverlayLayout.setShowingForActionMode(false);
         }

         this.updateVisibility(false);
      }

   }

   private void init(View var1) {
      this.mOverlayLayout = (ActionBarOverlayLayout)var1.findViewById(R.id.decor_content_parent);
      if (this.mOverlayLayout != null) {
         this.mOverlayLayout.setActionBarVisibilityCallback(this);
      }

      this.mDecorToolbar = this.getDecorToolbar(var1.findViewById(R.id.action_bar));
      this.mContextView = (ActionBarContextView)var1.findViewById(R.id.action_context_bar);
      this.mContainerView = (ActionBarContainer)var1.findViewById(R.id.action_bar_container);
      if (this.mDecorToolbar != null && this.mContextView != null && this.mContainerView != null) {
         this.mContext = this.mDecorToolbar.getContext();
         boolean var2;
         if ((this.mDecorToolbar.getDisplayOptions() & 4) != 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (var2) {
            this.mDisplayHomeAsUpSet = true;
         }

         ActionBarPolicy var5 = ActionBarPolicy.get(this.mContext);
         boolean var3;
         if (!var5.enableHomeButtonByDefault() && !var2) {
            var3 = false;
         } else {
            var3 = true;
         }

         this.setHomeButtonEnabled(var3);
         this.setHasEmbeddedTabs(var5.hasEmbeddedTabs());
         TypedArray var6 = this.mContext.obtainStyledAttributes((AttributeSet)null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
         if (var6.getBoolean(R.styleable.ActionBar_hideOnContentScroll, false)) {
            this.setHideOnContentScrollEnabled(true);
         }

         int var7 = var6.getDimensionPixelSize(R.styleable.ActionBar_elevation, 0);
         if (var7 != 0) {
            this.setElevation((float)var7);
         }

         var6.recycle();
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append(this.getClass().getSimpleName());
         var4.append(" can only be used ");
         var4.append("with a compatible window decor layout");
         throw new IllegalStateException(var4.toString());
      }
   }

   private void setHasEmbeddedTabs(boolean var1) {
      this.mHasEmbeddedTabs = var1;
      if (!this.mHasEmbeddedTabs) {
         this.mDecorToolbar.setEmbeddedTabView((ScrollingTabContainerView)null);
         this.mContainerView.setTabContainer(this.mTabScrollView);
      } else {
         this.mContainerView.setTabContainer((ScrollingTabContainerView)null);
         this.mDecorToolbar.setEmbeddedTabView(this.mTabScrollView);
      }

      int var2 = this.getNavigationMode();
      boolean var3 = true;
      boolean var5;
      if (var2 == 2) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (this.mTabScrollView != null) {
         if (var5) {
            this.mTabScrollView.setVisibility(0);
            if (this.mOverlayLayout != null) {
               ViewCompat.requestApplyInsets(this.mOverlayLayout);
            }
         } else {
            this.mTabScrollView.setVisibility(8);
         }
      }

      DecorToolbar var4 = this.mDecorToolbar;
      if (!this.mHasEmbeddedTabs && var5) {
         var1 = true;
      } else {
         var1 = false;
      }

      var4.setCollapsible(var1);
      ActionBarOverlayLayout var6 = this.mOverlayLayout;
      if (!this.mHasEmbeddedTabs && var5) {
         var1 = var3;
      } else {
         var1 = false;
      }

      var6.setHasNonEmbeddedTabs(var1);
   }

   private boolean shouldAnimateContextView() {
      return ViewCompat.isLaidOut(this.mContainerView);
   }

   private void showForActionMode() {
      if (!this.mShowingForMode) {
         this.mShowingForMode = true;
         if (this.mOverlayLayout != null) {
            this.mOverlayLayout.setShowingForActionMode(true);
         }

         this.updateVisibility(false);
      }

   }

   private void updateVisibility(boolean var1) {
      if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
         if (!this.mNowShowing) {
            this.mNowShowing = true;
            this.doShow(var1);
         }
      } else if (this.mNowShowing) {
         this.mNowShowing = false;
         this.doHide(var1);
      }

   }

   public void animateToMode(boolean var1) {
      if (var1) {
         this.showForActionMode();
      } else {
         this.hideForActionMode();
      }

      if (this.shouldAnimateContextView()) {
         ViewPropertyAnimatorCompat var2;
         ViewPropertyAnimatorCompat var3;
         if (var1) {
            var2 = this.mDecorToolbar.setupAnimatorToVisibility(4, 100L);
            var3 = this.mContextView.setupAnimatorToVisibility(0, 200L);
         } else {
            var3 = this.mDecorToolbar.setupAnimatorToVisibility(0, 200L);
            var2 = this.mContextView.setupAnimatorToVisibility(8, 100L);
         }

         ViewPropertyAnimatorCompatSet var4 = new ViewPropertyAnimatorCompatSet();
         var4.playSequentially(var2, var3);
         var4.start();
      } else if (var1) {
         this.mDecorToolbar.setVisibility(4);
         this.mContextView.setVisibility(0);
      } else {
         this.mDecorToolbar.setVisibility(0);
         this.mContextView.setVisibility(8);
      }

   }

   public boolean collapseActionView() {
      if (this.mDecorToolbar != null && this.mDecorToolbar.hasExpandedActionView()) {
         this.mDecorToolbar.collapseActionView();
         return true;
      } else {
         return false;
      }
   }

   void completeDeferredDestroyActionMode() {
      if (this.mDeferredModeDestroyCallback != null) {
         this.mDeferredModeDestroyCallback.onDestroyActionMode(this.mDeferredDestroyActionMode);
         this.mDeferredDestroyActionMode = null;
         this.mDeferredModeDestroyCallback = null;
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

   public void doHide(boolean var1) {
      if (this.mCurrentShowAnim != null) {
         this.mCurrentShowAnim.cancel();
      }

      if (this.mCurWindowVisibility != 0 || !this.mShowHideAnimationEnabled && !var1) {
         this.mHideListener.onAnimationEnd((View)null);
      } else {
         this.mContainerView.setAlpha(1.0F);
         this.mContainerView.setTransitioning(true);
         ViewPropertyAnimatorCompatSet var2 = new ViewPropertyAnimatorCompatSet();
         float var3 = (float)(-this.mContainerView.getHeight());
         float var4 = var3;
         if (var1) {
            int[] var5 = new int[]{0, 0};
            this.mContainerView.getLocationInWindow(var5);
            var4 = var3 - (float)var5[1];
         }

         ViewPropertyAnimatorCompat var6 = ViewCompat.animate(this.mContainerView).translationY(var4);
         var6.setUpdateListener(this.mUpdateListener);
         var2.play(var6);
         if (this.mContentAnimations && this.mContentView != null) {
            var2.play(ViewCompat.animate(this.mContentView).translationY(var4));
         }

         var2.setInterpolator(sHideInterpolator);
         var2.setDuration(250L);
         var2.setListener(this.mHideListener);
         this.mCurrentShowAnim = var2;
         var2.start();
      }

   }

   public void doShow(boolean var1) {
      if (this.mCurrentShowAnim != null) {
         this.mCurrentShowAnim.cancel();
      }

      this.mContainerView.setVisibility(0);
      if (this.mCurWindowVisibility == 0 && (this.mShowHideAnimationEnabled || var1)) {
         this.mContainerView.setTranslationY(0.0F);
         float var2 = (float)(-this.mContainerView.getHeight());
         float var3 = var2;
         if (var1) {
            int[] var4 = new int[]{0, 0};
            this.mContainerView.getLocationInWindow(var4);
            var3 = var2 - (float)var4[1];
         }

         this.mContainerView.setTranslationY(var3);
         ViewPropertyAnimatorCompatSet var6 = new ViewPropertyAnimatorCompatSet();
         ViewPropertyAnimatorCompat var5 = ViewCompat.animate(this.mContainerView).translationY(0.0F);
         var5.setUpdateListener(this.mUpdateListener);
         var6.play(var5);
         if (this.mContentAnimations && this.mContentView != null) {
            this.mContentView.setTranslationY(var3);
            var6.play(ViewCompat.animate(this.mContentView).translationY(0.0F));
         }

         var6.setInterpolator(sShowInterpolator);
         var6.setDuration(250L);
         var6.setListener(this.mShowListener);
         this.mCurrentShowAnim = var6;
         var6.start();
      } else {
         this.mContainerView.setAlpha(1.0F);
         this.mContainerView.setTranslationY(0.0F);
         if (this.mContentAnimations && this.mContentView != null) {
            this.mContentView.setTranslationY(0.0F);
         }

         this.mShowListener.onAnimationEnd((View)null);
      }

      if (this.mOverlayLayout != null) {
         ViewCompat.requestApplyInsets(this.mOverlayLayout);
      }

   }

   public void enableContentAnimations(boolean var1) {
      this.mContentAnimations = var1;
   }

   public int getDisplayOptions() {
      return this.mDecorToolbar.getDisplayOptions();
   }

   public int getNavigationMode() {
      return this.mDecorToolbar.getNavigationMode();
   }

   public Context getThemedContext() {
      if (this.mThemedContext == null) {
         TypedValue var1 = new TypedValue();
         this.mContext.getTheme().resolveAttribute(R.attr.actionBarWidgetTheme, var1, true);
         int var2 = var1.resourceId;
         if (var2 != 0) {
            this.mThemedContext = new ContextThemeWrapper(this.mContext, var2);
         } else {
            this.mThemedContext = this.mContext;
         }
      }

      return this.mThemedContext;
   }

   public void hideForSystem() {
      if (!this.mHiddenBySystem) {
         this.mHiddenBySystem = true;
         this.updateVisibility(true);
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      this.setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
   }

   public void onContentScrollStarted() {
      if (this.mCurrentShowAnim != null) {
         this.mCurrentShowAnim.cancel();
         this.mCurrentShowAnim = null;
      }

   }

   public void onContentScrollStopped() {
   }

   public boolean onKeyShortcut(int var1, KeyEvent var2) {
      if (this.mActionMode == null) {
         return false;
      } else {
         Menu var3 = this.mActionMode.getMenu();
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
   }

   public void onWindowVisibilityChanged(int var1) {
      this.mCurWindowVisibility = var1;
   }

   public void setDefaultDisplayHomeAsUpEnabled(boolean var1) {
      if (!this.mDisplayHomeAsUpSet) {
         this.setDisplayHomeAsUpEnabled(var1);
      }

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
      if ((var2 & 4) != 0) {
         this.mDisplayHomeAsUpSet = true;
      }

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
      ViewCompat.setElevation(this.mContainerView, var1);
   }

   public void setHideOnContentScrollEnabled(boolean var1) {
      if (var1 && !this.mOverlayLayout.isInOverlayMode()) {
         throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
      } else {
         this.mHideOnContentScroll = var1;
         this.mOverlayLayout.setHideOnContentScrollEnabled(var1);
      }
   }

   public void setHomeAsUpIndicator(Drawable var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeButtonEnabled(boolean var1) {
      this.mDecorToolbar.setHomeButtonEnabled(var1);
   }

   public void setShowHideAnimationEnabled(boolean var1) {
      this.mShowHideAnimationEnabled = var1;
      if (!var1 && this.mCurrentShowAnim != null) {
         this.mCurrentShowAnim.cancel();
      }

   }

   public void setWindowTitle(CharSequence var1) {
      this.mDecorToolbar.setWindowTitle(var1);
   }

   public void showForSystem() {
      if (this.mHiddenBySystem) {
         this.mHiddenBySystem = false;
         this.updateVisibility(true);
      }

   }

   public ActionMode startActionMode(ActionMode.Callback var1) {
      if (this.mActionMode != null) {
         this.mActionMode.finish();
      }

      this.mOverlayLayout.setHideOnContentScrollEnabled(false);
      this.mContextView.killMode();
      WindowDecorActionBar.ActionModeImpl var2 = new WindowDecorActionBar.ActionModeImpl(this.mContextView.getContext(), var1);
      if (var2.dispatchOnCreate()) {
         this.mActionMode = var2;
         var2.invalidate();
         this.mContextView.initForMode(var2);
         this.animateToMode(true);
         this.mContextView.sendAccessibilityEvent(32);
         return var2;
      } else {
         return null;
      }
   }

   public class ActionModeImpl extends ActionMode implements MenuBuilder.Callback {
      private final Context mActionModeContext;
      private ActionMode.Callback mCallback;
      private WeakReference mCustomView;
      private final MenuBuilder mMenu;

      public ActionModeImpl(Context var2, ActionMode.Callback var3) {
         this.mActionModeContext = var2;
         this.mCallback = var3;
         this.mMenu = (new MenuBuilder(var2)).setDefaultShowAsAction(1);
         this.mMenu.setCallback(this);
      }

      public boolean dispatchOnCreate() {
         this.mMenu.stopDispatchingItemsChanged();

         boolean var1;
         try {
            var1 = this.mCallback.onCreateActionMode(this, this.mMenu);
         } finally {
            this.mMenu.startDispatchingItemsChanged();
         }

         return var1;
      }

      public void finish() {
         if (WindowDecorActionBar.this.mActionMode == this) {
            if (!WindowDecorActionBar.checkShowingFlags(WindowDecorActionBar.this.mHiddenByApp, WindowDecorActionBar.this.mHiddenBySystem, false)) {
               WindowDecorActionBar.this.mDeferredDestroyActionMode = this;
               WindowDecorActionBar.this.mDeferredModeDestroyCallback = this.mCallback;
            } else {
               this.mCallback.onDestroyActionMode(this);
            }

            this.mCallback = null;
            WindowDecorActionBar.this.animateToMode(false);
            WindowDecorActionBar.this.mContextView.closeMode();
            WindowDecorActionBar.this.mDecorToolbar.getViewGroup().sendAccessibilityEvent(32);
            WindowDecorActionBar.this.mOverlayLayout.setHideOnContentScrollEnabled(WindowDecorActionBar.this.mHideOnContentScroll);
            WindowDecorActionBar.this.mActionMode = null;
         }
      }

      public View getCustomView() {
         View var1;
         if (this.mCustomView != null) {
            var1 = (View)this.mCustomView.get();
         } else {
            var1 = null;
         }

         return var1;
      }

      public Menu getMenu() {
         return this.mMenu;
      }

      public MenuInflater getMenuInflater() {
         return new SupportMenuInflater(this.mActionModeContext);
      }

      public CharSequence getSubtitle() {
         return WindowDecorActionBar.this.mContextView.getSubtitle();
      }

      public CharSequence getTitle() {
         return WindowDecorActionBar.this.mContextView.getTitle();
      }

      public void invalidate() {
         if (WindowDecorActionBar.this.mActionMode == this) {
            this.mMenu.stopDispatchingItemsChanged();

            try {
               this.mCallback.onPrepareActionMode(this, this.mMenu);
            } finally {
               this.mMenu.startDispatchingItemsChanged();
            }

         }
      }

      public boolean isTitleOptional() {
         return WindowDecorActionBar.this.mContextView.isTitleOptional();
      }

      public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
         return this.mCallback != null ? this.mCallback.onActionItemClicked(this, var2) : false;
      }

      public void onMenuModeChange(MenuBuilder var1) {
         if (this.mCallback != null) {
            this.invalidate();
            WindowDecorActionBar.this.mContextView.showOverflowMenu();
         }
      }

      public void setCustomView(View var1) {
         WindowDecorActionBar.this.mContextView.setCustomView(var1);
         this.mCustomView = new WeakReference(var1);
      }

      public void setSubtitle(int var1) {
         this.setSubtitle(WindowDecorActionBar.this.mContext.getResources().getString(var1));
      }

      public void setSubtitle(CharSequence var1) {
         WindowDecorActionBar.this.mContextView.setSubtitle(var1);
      }

      public void setTitle(int var1) {
         this.setTitle(WindowDecorActionBar.this.mContext.getResources().getString(var1));
      }

      public void setTitle(CharSequence var1) {
         WindowDecorActionBar.this.mContextView.setTitle(var1);
      }

      public void setTitleOptionalHint(boolean var1) {
         super.setTitleOptionalHint(var1);
         WindowDecorActionBar.this.mContextView.setTitleOptional(var1);
      }
   }
}
