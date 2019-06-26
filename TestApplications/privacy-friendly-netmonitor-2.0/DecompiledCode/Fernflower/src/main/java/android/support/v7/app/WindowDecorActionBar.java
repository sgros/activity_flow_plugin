package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.ActionMode;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.SubMenuBuilder;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.SpinnerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class WindowDecorActionBar extends ActionBar implements ActionBarOverlayLayout.ActionBarVisibilityCallback {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   private static final long FADE_IN_DURATION_MS = 200L;
   private static final long FADE_OUT_DURATION_MS = 100L;
   private static final int INVALID_POSITION = -1;
   private static final String TAG = "WindowDecorActionBar";
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
   private WindowDecorActionBar.TabImpl mSelectedTab;
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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public WindowDecorActionBar(View var1) {
      this.init(var1);
   }

   static boolean checkShowingFlags(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return true;
      } else {
         return !var0 && !var1;
      }
   }

   private void cleanupTabs() {
      if (this.mSelectedTab != null) {
         this.selectTab((ActionBar.Tab)null);
      }

      this.mTabs.clear();
      if (this.mTabScrollView != null) {
         this.mTabScrollView.removeAllTabs();
      }

      this.mSavedTabPosition = -1;
   }

   private void configureTab(ActionBar.Tab var1, int var2) {
      WindowDecorActionBar.TabImpl var4 = (WindowDecorActionBar.TabImpl)var1;
      if (var4.getCallback() == null) {
         throw new IllegalStateException("Action Bar Tab must have a Callback");
      } else {
         var4.setPosition(var2);
         this.mTabs.add(var2, var4);
         int var3 = this.mTabs.size();

         while(true) {
            ++var2;
            if (var2 >= var3) {
               return;
            }

            ((WindowDecorActionBar.TabImpl)this.mTabs.get(var2)).setPosition(var2);
         }
      }
   }

   private void ensureTabsExist() {
      if (this.mTabScrollView == null) {
         ScrollingTabContainerView var1 = new ScrollingTabContainerView(this.mContext);
         if (this.mHasEmbeddedTabs) {
            var1.setVisibility(0);
            this.mDecorToolbar.setEmbeddedTabView(var1);
         } else {
            if (this.getNavigationMode() == 2) {
               var1.setVisibility(0);
               if (this.mOverlayLayout != null) {
                  ViewCompat.requestApplyInsets(this.mOverlayLayout);
               }
            } else {
               var1.setVisibility(8);
            }

            this.mContainerView.setTabContainer(var1);
         }

         this.mTabScrollView = var1;
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
         var2.append(var1);
         String var3;
         if (var2.toString() != null) {
            var3 = var1.getClass().getSimpleName();
         } else {
            var3 = "null";
         }

         throw new IllegalStateException(var3);
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

   public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1) {
      this.mMenuVisibilityListeners.add(var1);
   }

   public void addTab(ActionBar.Tab var1) {
      this.addTab(var1, this.mTabs.isEmpty());
   }

   public void addTab(ActionBar.Tab var1, int var2) {
      this.addTab(var1, var2, this.mTabs.isEmpty());
   }

   public void addTab(ActionBar.Tab var1, int var2, boolean var3) {
      this.ensureTabsExist();
      this.mTabScrollView.addTab(var1, var2, var3);
      this.configureTab(var1, var2);
      if (var3) {
         this.selectTab(var1);
      }

   }

   public void addTab(ActionBar.Tab var1, boolean var2) {
      this.ensureTabsExist();
      this.mTabScrollView.addTab(var1, var2);
      this.configureTab(var1, this.mTabs.size());
      if (var2) {
         this.selectTab(var1);
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

   public View getCustomView() {
      return this.mDecorToolbar.getCustomView();
   }

   public int getDisplayOptions() {
      return this.mDecorToolbar.getDisplayOptions();
   }

   public float getElevation() {
      return ViewCompat.getElevation(this.mContainerView);
   }

   public int getHeight() {
      return this.mContainerView.getHeight();
   }

   public int getHideOffset() {
      return this.mOverlayLayout.getActionBarHideOffset();
   }

   public int getNavigationItemCount() {
      switch(this.mDecorToolbar.getNavigationMode()) {
      case 1:
         return this.mDecorToolbar.getDropdownItemCount();
      case 2:
         return this.mTabs.size();
      default:
         return 0;
      }
   }

   public int getNavigationMode() {
      return this.mDecorToolbar.getNavigationMode();
   }

   public int getSelectedNavigationIndex() {
      int var1 = this.mDecorToolbar.getNavigationMode();
      int var2 = -1;
      switch(var1) {
      case 1:
         return this.mDecorToolbar.getDropdownSelectedPosition();
      case 2:
         if (this.mSelectedTab != null) {
            var2 = this.mSelectedTab.getPosition();
         }

         return var2;
      default:
         return -1;
      }
   }

   public ActionBar.Tab getSelectedTab() {
      return this.mSelectedTab;
   }

   public CharSequence getSubtitle() {
      return this.mDecorToolbar.getSubtitle();
   }

   public ActionBar.Tab getTabAt(int var1) {
      return (ActionBar.Tab)this.mTabs.get(var1);
   }

   public int getTabCount() {
      return this.mTabs.size();
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

   public CharSequence getTitle() {
      return this.mDecorToolbar.getTitle();
   }

   public boolean hasIcon() {
      return this.mDecorToolbar.hasIcon();
   }

   public boolean hasLogo() {
      return this.mDecorToolbar.hasLogo();
   }

   public void hide() {
      if (!this.mHiddenByApp) {
         this.mHiddenByApp = true;
         this.updateVisibility(false);
      }

   }

   public void hideForSystem() {
      if (!this.mHiddenBySystem) {
         this.mHiddenBySystem = true;
         this.updateVisibility(true);
      }

   }

   public boolean isHideOnContentScrollEnabled() {
      return this.mOverlayLayout.isHideOnContentScrollEnabled();
   }

   public boolean isShowing() {
      int var1 = this.getHeight();
      boolean var2;
      if (!this.mNowShowing || var1 != 0 && this.getHideOffset() >= var1) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean isTitleTruncated() {
      boolean var1;
      if (this.mDecorToolbar != null && this.mDecorToolbar.isTitleTruncated()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public ActionBar.Tab newTab() {
      return new WindowDecorActionBar.TabImpl();
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

   public void removeAllTabs() {
      this.cleanupTabs();
   }

   public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener var1) {
      this.mMenuVisibilityListeners.remove(var1);
   }

   public void removeTab(ActionBar.Tab var1) {
      this.removeTabAt(var1.getPosition());
   }

   public void removeTabAt(int var1) {
      if (this.mTabScrollView != null) {
         int var2;
         if (this.mSelectedTab != null) {
            var2 = this.mSelectedTab.getPosition();
         } else {
            var2 = this.mSavedTabPosition;
         }

         this.mTabScrollView.removeTabAt(var1);
         WindowDecorActionBar.TabImpl var3 = (WindowDecorActionBar.TabImpl)this.mTabs.remove(var1);
         if (var3 != null) {
            var3.setPosition(-1);
         }

         int var4 = this.mTabs.size();

         for(int var5 = var1; var5 < var4; ++var5) {
            ((WindowDecorActionBar.TabImpl)this.mTabs.get(var5)).setPosition(var5);
         }

         if (var2 == var1) {
            if (this.mTabs.isEmpty()) {
               var3 = null;
            } else {
               var3 = (WindowDecorActionBar.TabImpl)this.mTabs.get(Math.max(0, var1 - 1));
            }

            this.selectTab(var3);
         }

      }
   }

   public boolean requestFocus() {
      ViewGroup var1 = this.mDecorToolbar.getViewGroup();
      if (var1 != null && !var1.hasFocus()) {
         var1.requestFocus();
         return true;
      } else {
         return false;
      }
   }

   public void selectTab(ActionBar.Tab var1) {
      int var2 = this.getNavigationMode();
      int var3 = -1;
      if (var2 != 2) {
         if (var1 != null) {
            var3 = var1.getPosition();
         }

         this.mSavedTabPosition = var3;
      } else {
         FragmentTransaction var4;
         if (this.mActivity instanceof FragmentActivity && !this.mDecorToolbar.getViewGroup().isInEditMode()) {
            var4 = ((FragmentActivity)this.mActivity).getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
         } else {
            var4 = null;
         }

         if (this.mSelectedTab == var1) {
            if (this.mSelectedTab != null) {
               this.mSelectedTab.getCallback().onTabReselected(this.mSelectedTab, var4);
               this.mTabScrollView.animateToTab(var1.getPosition());
            }
         } else {
            ScrollingTabContainerView var5 = this.mTabScrollView;
            if (var1 != null) {
               var3 = var1.getPosition();
            }

            var5.setTabSelected(var3);
            if (this.mSelectedTab != null) {
               this.mSelectedTab.getCallback().onTabUnselected(this.mSelectedTab, var4);
            }

            this.mSelectedTab = (WindowDecorActionBar.TabImpl)var1;
            if (this.mSelectedTab != null) {
               this.mSelectedTab.getCallback().onTabSelected(this.mSelectedTab, var4);
            }
         }

         if (var4 != null && !var4.isEmpty()) {
            var4.commit();
         }

      }
   }

   public void setBackgroundDrawable(Drawable var1) {
      this.mContainerView.setPrimaryBackground(var1);
   }

   public void setCustomView(int var1) {
      this.setCustomView(LayoutInflater.from(this.getThemedContext()).inflate(var1, this.mDecorToolbar.getViewGroup(), false));
   }

   public void setCustomView(View var1) {
      this.mDecorToolbar.setCustomView(var1);
   }

   public void setCustomView(View var1, ActionBar.LayoutParams var2) {
      var1.setLayoutParams(var2);
      this.mDecorToolbar.setCustomView(var1);
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

   public void setDisplayOptions(int var1) {
      if ((var1 & 4) != 0) {
         this.mDisplayHomeAsUpSet = true;
      }

      this.mDecorToolbar.setDisplayOptions(var1);
   }

   public void setDisplayOptions(int var1, int var2) {
      int var3 = this.mDecorToolbar.getDisplayOptions();
      if ((var2 & 4) != 0) {
         this.mDisplayHomeAsUpSet = true;
      }

      this.mDecorToolbar.setDisplayOptions(var1 & var2 | ~var2 & var3);
   }

   public void setDisplayShowCustomEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 16;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 16);
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

   public void setDisplayShowTitleEnabled(boolean var1) {
      byte var2;
      if (var1) {
         var2 = 8;
      } else {
         var2 = 0;
      }

      this.setDisplayOptions(var2, 8);
   }

   public void setDisplayUseLogoEnabled(boolean var1) {
      this.setDisplayOptions(var1, 1);
   }

   public void setElevation(float var1) {
      ViewCompat.setElevation(this.mContainerView, var1);
   }

   public void setHideOffset(int var1) {
      if (var1 != 0 && !this.mOverlayLayout.isInOverlayMode()) {
         throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to set a non-zero hide offset");
      } else {
         this.mOverlayLayout.setActionBarHideOffset(var1);
      }
   }

   public void setHideOnContentScrollEnabled(boolean var1) {
      if (var1 && !this.mOverlayLayout.isInOverlayMode()) {
         throw new IllegalStateException("Action bar must be in overlay mode (Window.FEATURE_OVERLAY_ACTION_BAR) to enable hide on content scroll");
      } else {
         this.mHideOnContentScroll = var1;
         this.mOverlayLayout.setHideOnContentScrollEnabled(var1);
      }
   }

   public void setHomeActionContentDescription(int var1) {
      this.mDecorToolbar.setNavigationContentDescription(var1);
   }

   public void setHomeActionContentDescription(CharSequence var1) {
      this.mDecorToolbar.setNavigationContentDescription(var1);
   }

   public void setHomeAsUpIndicator(int var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeAsUpIndicator(Drawable var1) {
      this.mDecorToolbar.setNavigationIcon(var1);
   }

   public void setHomeButtonEnabled(boolean var1) {
      this.mDecorToolbar.setHomeButtonEnabled(var1);
   }

   public void setIcon(int var1) {
      this.mDecorToolbar.setIcon(var1);
   }

   public void setIcon(Drawable var1) {
      this.mDecorToolbar.setIcon(var1);
   }

   public void setListNavigationCallbacks(SpinnerAdapter var1, ActionBar.OnNavigationListener var2) {
      this.mDecorToolbar.setDropdownParams(var1, new NavItemSelectedListener(var2));
   }

   public void setLogo(int var1) {
      this.mDecorToolbar.setLogo(var1);
   }

   public void setLogo(Drawable var1) {
      this.mDecorToolbar.setLogo(var1);
   }

   public void setNavigationMode(int var1) {
      int var2 = this.mDecorToolbar.getNavigationMode();
      if (var2 == 2) {
         this.mSavedTabPosition = this.getSelectedNavigationIndex();
         this.selectTab((ActionBar.Tab)null);
         this.mTabScrollView.setVisibility(8);
      }

      if (var2 != var1 && !this.mHasEmbeddedTabs && this.mOverlayLayout != null) {
         ViewCompat.requestApplyInsets(this.mOverlayLayout);
      }

      this.mDecorToolbar.setNavigationMode(var1);
      boolean var3 = false;
      if (var1 == 2) {
         this.ensureTabsExist();
         this.mTabScrollView.setVisibility(0);
         if (this.mSavedTabPosition != -1) {
            this.setSelectedNavigationItem(this.mSavedTabPosition);
            this.mSavedTabPosition = -1;
         }
      }

      DecorToolbar var4 = this.mDecorToolbar;
      boolean var5;
      if (var1 == 2 && !this.mHasEmbeddedTabs) {
         var5 = true;
      } else {
         var5 = false;
      }

      var4.setCollapsible(var5);
      ActionBarOverlayLayout var6 = this.mOverlayLayout;
      var5 = var3;
      if (var1 == 2) {
         var5 = var3;
         if (!this.mHasEmbeddedTabs) {
            var5 = true;
         }
      }

      var6.setHasNonEmbeddedTabs(var5);
   }

   public void setSelectedNavigationItem(int var1) {
      switch(this.mDecorToolbar.getNavigationMode()) {
      case 1:
         this.mDecorToolbar.setDropdownSelectedPosition(var1);
         break;
      case 2:
         this.selectTab((ActionBar.Tab)this.mTabs.get(var1));
         break;
      default:
         throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
      }

   }

   public void setShowHideAnimationEnabled(boolean var1) {
      this.mShowHideAnimationEnabled = var1;
      if (!var1 && this.mCurrentShowAnim != null) {
         this.mCurrentShowAnim.cancel();
      }

   }

   public void setSplitBackgroundDrawable(Drawable var1) {
   }

   public void setStackedBackgroundDrawable(Drawable var1) {
      this.mContainerView.setStackedBackground(var1);
   }

   public void setSubtitle(int var1) {
      this.setSubtitle(this.mContext.getString(var1));
   }

   public void setSubtitle(CharSequence var1) {
      this.mDecorToolbar.setSubtitle(var1);
   }

   public void setTitle(int var1) {
      this.setTitle(this.mContext.getString(var1));
   }

   public void setTitle(CharSequence var1) {
      this.mDecorToolbar.setTitle(var1);
   }

   public void setWindowTitle(CharSequence var1) {
      this.mDecorToolbar.setWindowTitle(var1);
   }

   public void show() {
      if (this.mHiddenByApp) {
         this.mHiddenByApp = false;
         this.updateVisibility(false);
      }

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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
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

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
      }

      public void onCloseSubMenu(SubMenuBuilder var1) {
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

      public boolean onSubMenuSelected(SubMenuBuilder var1) {
         if (this.mCallback == null) {
            return false;
         } else if (!var1.hasVisibleItems()) {
            return true;
         } else {
            (new MenuPopupHelper(WindowDecorActionBar.this.getThemedContext(), var1)).show();
            return true;
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

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public class TabImpl extends ActionBar.Tab {
      private ActionBar.TabListener mCallback;
      private CharSequence mContentDesc;
      private View mCustomView;
      private Drawable mIcon;
      private int mPosition = -1;
      private Object mTag;
      private CharSequence mText;

      public ActionBar.TabListener getCallback() {
         return this.mCallback;
      }

      public CharSequence getContentDescription() {
         return this.mContentDesc;
      }

      public View getCustomView() {
         return this.mCustomView;
      }

      public Drawable getIcon() {
         return this.mIcon;
      }

      public int getPosition() {
         return this.mPosition;
      }

      public Object getTag() {
         return this.mTag;
      }

      public CharSequence getText() {
         return this.mText;
      }

      public void select() {
         WindowDecorActionBar.this.selectTab(this);
      }

      public ActionBar.Tab setContentDescription(int var1) {
         return this.setContentDescription(WindowDecorActionBar.this.mContext.getResources().getText(var1));
      }

      public ActionBar.Tab setContentDescription(CharSequence var1) {
         this.mContentDesc = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }

      public ActionBar.Tab setCustomView(int var1) {
         return this.setCustomView(LayoutInflater.from(WindowDecorActionBar.this.getThemedContext()).inflate(var1, (ViewGroup)null));
      }

      public ActionBar.Tab setCustomView(View var1) {
         this.mCustomView = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }

      public ActionBar.Tab setIcon(int var1) {
         return this.setIcon(AppCompatResources.getDrawable(WindowDecorActionBar.this.mContext, var1));
      }

      public ActionBar.Tab setIcon(Drawable var1) {
         this.mIcon = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }

      public void setPosition(int var1) {
         this.mPosition = var1;
      }

      public ActionBar.Tab setTabListener(ActionBar.TabListener var1) {
         this.mCallback = var1;
         return this;
      }

      public ActionBar.Tab setTag(Object var1) {
         this.mTag = var1;
         return this;
      }

      public ActionBar.Tab setText(int var1) {
         return this.setText(WindowDecorActionBar.this.mContext.getResources().getText(var1));
      }

      public ActionBar.Tab setText(CharSequence var1) {
         this.mText = var1;
         if (this.mPosition >= 0) {
            WindowDecorActionBar.this.mTabScrollView.updateTab(this.mPosition);
         }

         return this;
      }
   }
}
