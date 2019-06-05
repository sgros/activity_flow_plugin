package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.app.NavUtils;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.FitWindowsViewGroup;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.VectorEnabledTintResources;
import android.support.v7.widget.ViewStubCompat;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.LayoutInflater.Factory2;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window.Callback;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

class AppCompatDelegateImpl extends AppCompatDelegate implements MenuBuilder.Callback, Factory2 {
   private static final boolean IS_PRE_LOLLIPOP;
   private static boolean sInstalledExceptionHandler;
   private static final int[] sWindowBackgroundStyleable;
   ActionBar mActionBar;
   private AppCompatDelegateImpl.ActionMenuPresenterCallback mActionMenuPresenterCallback;
   ActionMode mActionMode;
   PopupWindow mActionModePopup;
   ActionBarContextView mActionModeView;
   final AppCompatCallback mAppCompatCallback;
   private AppCompatViewInflater mAppCompatViewInflater;
   final Callback mAppCompatWindowCallback;
   private boolean mApplyDayNightCalled;
   private AppCompatDelegateImpl.AutoNightModeManager mAutoNightModeManager;
   private boolean mClosingActionMenu;
   final Context mContext;
   private DecorContentParent mDecorContentParent;
   private boolean mEnableDefaultActionBarUp;
   ViewPropertyAnimatorCompat mFadeAnim = null;
   private boolean mFeatureIndeterminateProgress;
   private boolean mFeatureProgress;
   private boolean mHandleNativeActionModes = true;
   boolean mHasActionBar;
   int mInvalidatePanelMenuFeatures;
   boolean mInvalidatePanelMenuPosted;
   private final Runnable mInvalidatePanelMenuRunnable = new Runnable() {
      public void run() {
         if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 1) != 0) {
            AppCompatDelegateImpl.this.doInvalidatePanelMenu(0);
         }

         if ((AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures & 4096) != 0) {
            AppCompatDelegateImpl.this.doInvalidatePanelMenu(108);
         }

         AppCompatDelegateImpl.this.mInvalidatePanelMenuPosted = false;
         AppCompatDelegateImpl.this.mInvalidatePanelMenuFeatures = 0;
      }
   };
   boolean mIsDestroyed;
   boolean mIsFloating;
   private int mLocalNightMode = -100;
   private boolean mLongPressBackDown;
   MenuInflater mMenuInflater;
   final Callback mOriginalWindowCallback;
   boolean mOverlayActionBar;
   boolean mOverlayActionMode;
   private AppCompatDelegateImpl.PanelMenuPresenterCallback mPanelMenuPresenterCallback;
   private AppCompatDelegateImpl.PanelFeatureState[] mPanels;
   private AppCompatDelegateImpl.PanelFeatureState mPreparedPanel;
   Runnable mShowActionModePopup;
   private View mStatusGuard;
   private ViewGroup mSubDecor;
   private boolean mSubDecorInstalled;
   private Rect mTempRect1;
   private Rect mTempRect2;
   private CharSequence mTitle;
   private TextView mTitleView;
   final Window mWindow;
   boolean mWindowNoTitle;

   static {
      boolean var0;
      if (VERSION.SDK_INT < 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      IS_PRE_LOLLIPOP = var0;
      sWindowBackgroundStyleable = new int[]{16842836};
      if (IS_PRE_LOLLIPOP && !sInstalledExceptionHandler) {
         Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()) {
            // $FF: synthetic field
            final UncaughtExceptionHandler val$defHandler;

            {
               this.val$defHandler = var1;
            }

            private boolean shouldWrapException(Throwable var1) {
               boolean var2 = var1 instanceof NotFoundException;
               boolean var3 = false;
               if (!var2) {
                  return false;
               } else {
                  String var4 = var1.getMessage();
                  var2 = var3;
                  if (var4 != null) {
                     if (!var4.contains("drawable")) {
                        var2 = var3;
                        if (!var4.contains("Drawable")) {
                           return var2;
                        }
                     }

                     var2 = true;
                  }

                  return var2;
               }
            }

            public void uncaughtException(Thread var1, Throwable var2) {
               if (this.shouldWrapException(var2)) {
                  StringBuilder var3 = new StringBuilder();
                  var3.append(var2.getMessage());
                  var3.append(". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                  NotFoundException var4 = new NotFoundException(var3.toString());
                  var4.initCause(var2.getCause());
                  var4.setStackTrace(var2.getStackTrace());
                  this.val$defHandler.uncaughtException(var1, var4);
               } else {
                  this.val$defHandler.uncaughtException(var1, var2);
               }

            }
         });
         sInstalledExceptionHandler = true;
      }

   }

   AppCompatDelegateImpl(Context var1, Window var2, AppCompatCallback var3) {
      this.mContext = var1;
      this.mWindow = var2;
      this.mAppCompatCallback = var3;
      this.mOriginalWindowCallback = this.mWindow.getCallback();
      if (!(this.mOriginalWindowCallback instanceof AppCompatDelegateImpl.AppCompatWindowCallback)) {
         this.mAppCompatWindowCallback = new AppCompatDelegateImpl.AppCompatWindowCallback(this.mOriginalWindowCallback);
         this.mWindow.setCallback(this.mAppCompatWindowCallback);
         TintTypedArray var4 = TintTypedArray.obtainStyledAttributes(var1, (AttributeSet)null, sWindowBackgroundStyleable);
         Drawable var5 = var4.getDrawableIfKnown(0);
         if (var5 != null) {
            this.mWindow.setBackgroundDrawable(var5);
         }

         var4.recycle();
      } else {
         throw new IllegalStateException("AppCompat has already installed itself into the Window");
      }
   }

   private void applyFixedSizeWindow() {
      ContentFrameLayout var1 = (ContentFrameLayout)this.mSubDecor.findViewById(16908290);
      View var2 = this.mWindow.getDecorView();
      var1.setDecorPadding(var2.getPaddingLeft(), var2.getPaddingTop(), var2.getPaddingRight(), var2.getPaddingBottom());
      TypedArray var3 = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
      var3.getValue(R.styleable.AppCompatTheme_windowMinWidthMajor, var1.getMinWidthMajor());
      var3.getValue(R.styleable.AppCompatTheme_windowMinWidthMinor, var1.getMinWidthMinor());
      if (var3.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMajor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedWidthMajor, var1.getFixedWidthMajor());
      }

      if (var3.hasValue(R.styleable.AppCompatTheme_windowFixedWidthMinor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedWidthMinor, var1.getFixedWidthMinor());
      }

      if (var3.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMajor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedHeightMajor, var1.getFixedHeightMajor());
      }

      if (var3.hasValue(R.styleable.AppCompatTheme_windowFixedHeightMinor)) {
         var3.getValue(R.styleable.AppCompatTheme_windowFixedHeightMinor, var1.getFixedHeightMinor());
      }

      var3.recycle();
      var1.requestLayout();
   }

   private ViewGroup createSubDecor() {
      TypedArray var1 = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme);
      if (var1.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
         if (var1.getBoolean(R.styleable.AppCompatTheme_windowNoTitle, false)) {
            this.requestWindowFeature(1);
         } else if (var1.getBoolean(R.styleable.AppCompatTheme_windowActionBar, false)) {
            this.requestWindowFeature(108);
         }

         if (var1.getBoolean(R.styleable.AppCompatTheme_windowActionBarOverlay, false)) {
            this.requestWindowFeature(109);
         }

         if (var1.getBoolean(R.styleable.AppCompatTheme_windowActionModeOverlay, false)) {
            this.requestWindowFeature(10);
         }

         this.mIsFloating = var1.getBoolean(R.styleable.AppCompatTheme_android_windowIsFloating, false);
         var1.recycle();
         this.mWindow.getDecorView();
         LayoutInflater var5 = LayoutInflater.from(this.mContext);
         ViewGroup var2;
         ViewGroup var6;
         if (!this.mWindowNoTitle) {
            if (this.mIsFloating) {
               var6 = (ViewGroup)var5.inflate(R.layout.abc_dialog_title_material, (ViewGroup)null);
               this.mOverlayActionBar = false;
               this.mHasActionBar = false;
            } else if (this.mHasActionBar) {
               TypedValue var7 = new TypedValue();
               this.mContext.getTheme().resolveAttribute(R.attr.actionBarTheme, var7, true);
               Object var8;
               if (var7.resourceId != 0) {
                  var8 = new ContextThemeWrapper(this.mContext, var7.resourceId);
               } else {
                  var8 = this.mContext;
               }

               var2 = (ViewGroup)LayoutInflater.from((Context)var8).inflate(R.layout.abc_screen_toolbar, (ViewGroup)null);
               this.mDecorContentParent = (DecorContentParent)var2.findViewById(R.id.decor_content_parent);
               this.mDecorContentParent.setWindowCallback(this.getWindowCallback());
               if (this.mOverlayActionBar) {
                  this.mDecorContentParent.initFeature(109);
               }

               if (this.mFeatureProgress) {
                  this.mDecorContentParent.initFeature(2);
               }

               var6 = var2;
               if (this.mFeatureIndeterminateProgress) {
                  this.mDecorContentParent.initFeature(5);
                  var6 = var2;
               }
            } else {
               var6 = null;
            }
         } else {
            if (this.mOverlayActionMode) {
               var6 = (ViewGroup)var5.inflate(R.layout.abc_screen_simple_overlay_action_mode, (ViewGroup)null);
            } else {
               var6 = (ViewGroup)var5.inflate(R.layout.abc_screen_simple, (ViewGroup)null);
            }

            if (VERSION.SDK_INT >= 21) {
               ViewCompat.setOnApplyWindowInsetsListener(var6, new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     int var3 = var2.getSystemWindowInsetTop();
                     int var4 = AppCompatDelegateImpl.this.updateStatusGuard(var3);
                     WindowInsetsCompat var5 = var2;
                     if (var3 != var4) {
                        var5 = var2.replaceSystemWindowInsets(var2.getSystemWindowInsetLeft(), var4, var2.getSystemWindowInsetRight(), var2.getSystemWindowInsetBottom());
                     }

                     return ViewCompat.onApplyWindowInsets(var1, var5);
                  }
               });
            } else {
               ((FitWindowsViewGroup)var6).setOnFitSystemWindowsListener(new FitWindowsViewGroup.OnFitSystemWindowsListener() {
                  public void onFitSystemWindows(Rect var1) {
                     var1.top = AppCompatDelegateImpl.this.updateStatusGuard(var1.top);
                  }
               });
            }
         }

         if (var6 != null) {
            if (this.mDecorContentParent == null) {
               this.mTitleView = (TextView)var6.findViewById(R.id.title);
            }

            ViewUtils.makeOptionalFitsSystemWindows(var6);
            ContentFrameLayout var3 = (ContentFrameLayout)var6.findViewById(R.id.action_bar_activity_content);
            var2 = (ViewGroup)this.mWindow.findViewById(16908290);
            if (var2 != null) {
               while(var2.getChildCount() > 0) {
                  View var4 = var2.getChildAt(0);
                  var2.removeViewAt(0);
                  var3.addView(var4);
               }

               var2.setId(-1);
               var3.setId(16908290);
               if (var2 instanceof FrameLayout) {
                  ((FrameLayout)var2).setForeground((Drawable)null);
               }
            }

            this.mWindow.setContentView(var6);
            var3.setAttachListener(new ContentFrameLayout.OnAttachListener() {
               public void onAttachedFromWindow() {
               }

               public void onDetachedFromWindow() {
                  AppCompatDelegateImpl.this.dismissPopups();
               }
            });
            return var6;
         } else {
            StringBuilder var9 = new StringBuilder();
            var9.append("AppCompat does not support the current theme features: { windowActionBar: ");
            var9.append(this.mHasActionBar);
            var9.append(", windowActionBarOverlay: ");
            var9.append(this.mOverlayActionBar);
            var9.append(", android:windowIsFloating: ");
            var9.append(this.mIsFloating);
            var9.append(", windowActionModeOverlay: ");
            var9.append(this.mOverlayActionMode);
            var9.append(", windowNoTitle: ");
            var9.append(this.mWindowNoTitle);
            var9.append(" }");
            throw new IllegalArgumentException(var9.toString());
         }
      } else {
         var1.recycle();
         throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
      }
   }

   private void ensureAutoNightModeManager() {
      if (this.mAutoNightModeManager == null) {
         this.mAutoNightModeManager = new AppCompatDelegateImpl.AutoNightModeManager(TwilightManager.getInstance(this.mContext));
      }

   }

   private void ensureSubDecor() {
      if (!this.mSubDecorInstalled) {
         this.mSubDecor = this.createSubDecor();
         CharSequence var1 = this.getTitle();
         if (!TextUtils.isEmpty(var1)) {
            if (this.mDecorContentParent != null) {
               this.mDecorContentParent.setWindowTitle(var1);
            } else if (this.peekSupportActionBar() != null) {
               this.peekSupportActionBar().setWindowTitle(var1);
            } else if (this.mTitleView != null) {
               this.mTitleView.setText(var1);
            }
         }

         this.applyFixedSizeWindow();
         this.onSubDecorInstalled(this.mSubDecor);
         this.mSubDecorInstalled = true;
         AppCompatDelegateImpl.PanelFeatureState var2 = this.getPanelState(0, false);
         if (!this.mIsDestroyed && (var2 == null || var2.menu == null)) {
            this.invalidatePanelMenu(108);
         }
      }

   }

   private int getNightMode() {
      int var1;
      if (this.mLocalNightMode != -100) {
         var1 = this.mLocalNightMode;
      } else {
         var1 = getDefaultNightMode();
      }

      return var1;
   }

   private void initWindowDecorActionBar() {
      this.ensureSubDecor();
      if (this.mHasActionBar && this.mActionBar == null) {
         if (this.mOriginalWindowCallback instanceof Activity) {
            this.mActionBar = new WindowDecorActionBar((Activity)this.mOriginalWindowCallback, this.mOverlayActionBar);
         } else if (this.mOriginalWindowCallback instanceof Dialog) {
            this.mActionBar = new WindowDecorActionBar((Dialog)this.mOriginalWindowCallback);
         }

         if (this.mActionBar != null) {
            this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
         }

      }
   }

   private boolean initializePanelContent(AppCompatDelegateImpl.PanelFeatureState var1) {
      View var2 = var1.createdPanelView;
      boolean var3 = true;
      if (var2 != null) {
         var1.shownPanelView = var1.createdPanelView;
         return true;
      } else if (var1.menu == null) {
         return false;
      } else {
         if (this.mPanelMenuPresenterCallback == null) {
            this.mPanelMenuPresenterCallback = new AppCompatDelegateImpl.PanelMenuPresenterCallback();
         }

         var1.shownPanelView = (View)var1.getListMenuView(this.mPanelMenuPresenterCallback);
         if (var1.shownPanelView == null) {
            var3 = false;
         }

         return var3;
      }
   }

   private boolean initializePanelDecor(AppCompatDelegateImpl.PanelFeatureState var1) {
      var1.setStyle(this.getActionBarThemedContext());
      var1.decorView = new AppCompatDelegateImpl.ListMenuDecorView(var1.listPresenterContext);
      var1.gravity = 81;
      return true;
   }

   private boolean initializePanelMenu(AppCompatDelegateImpl.PanelFeatureState var1) {
      Object var3;
      label28: {
         Context var2 = this.mContext;
         if (var1.featureId != 0) {
            var3 = var2;
            if (var1.featureId != 108) {
               break label28;
            }
         }

         var3 = var2;
         if (this.mDecorContentParent != null) {
            TypedValue var4 = new TypedValue();
            Theme var5 = var2.getTheme();
            var5.resolveAttribute(R.attr.actionBarTheme, var4, true);
            Theme var7 = null;
            if (var4.resourceId != 0) {
               var7 = var2.getResources().newTheme();
               var7.setTo(var5);
               var7.applyStyle(var4.resourceId, true);
               var7.resolveAttribute(R.attr.actionBarWidgetTheme, var4, true);
            } else {
               var5.resolveAttribute(R.attr.actionBarWidgetTheme, var4, true);
            }

            Theme var6 = var7;
            if (var4.resourceId != 0) {
               var6 = var7;
               if (var7 == null) {
                  var6 = var2.getResources().newTheme();
                  var6.setTo(var5);
               }

               var6.applyStyle(var4.resourceId, true);
            }

            var3 = var2;
            if (var6 != null) {
               var3 = new ContextThemeWrapper(var2, 0);
               ((Context)var3).getTheme().setTo(var6);
            }
         }
      }

      MenuBuilder var8 = new MenuBuilder((Context)var3);
      var8.setCallback(this);
      var1.setMenu(var8);
      return true;
   }

   private void invalidatePanelMenu(int var1) {
      this.mInvalidatePanelMenuFeatures |= 1 << var1;
      if (!this.mInvalidatePanelMenuPosted) {
         ViewCompat.postOnAnimation(this.mWindow.getDecorView(), this.mInvalidatePanelMenuRunnable);
         this.mInvalidatePanelMenuPosted = true;
      }

   }

   private boolean onKeyDownPanel(int var1, KeyEvent var2) {
      if (var2.getRepeatCount() == 0) {
         AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(var1, true);
         if (!var3.isOpen) {
            return this.preparePanel(var3, var2);
         }
      }

      return false;
   }

   private boolean onKeyUpPanel(int var1, KeyEvent var2) {
      if (this.mActionMode != null) {
         return false;
      } else {
         boolean var4;
         label60: {
            AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(var1, true);
            if (var1 == 0 && this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && !ViewConfiguration.get(this.mContext).hasPermanentMenuKey()) {
               if (this.mDecorContentParent.isOverflowMenuShowing()) {
                  var4 = this.mDecorContentParent.hideOverflowMenu();
                  break label60;
               }

               if (!this.mIsDestroyed && this.preparePanel(var3, var2)) {
                  var4 = this.mDecorContentParent.showOverflowMenu();
                  break label60;
               }
            } else {
               if (var3.isOpen || var3.isHandled) {
                  var4 = var3.isOpen;
                  this.closePanel(var3, true);
                  break label60;
               }

               if (var3.isPrepared) {
                  if (var3.refreshMenuContent) {
                     var3.isPrepared = false;
                     var4 = this.preparePanel(var3, var2);
                  } else {
                     var4 = true;
                  }

                  if (var4) {
                     this.openPanel(var3, var2);
                     var4 = true;
                     break label60;
                  }
               }
            }

            var4 = false;
         }

         if (var4) {
            AudioManager var5 = (AudioManager)this.mContext.getSystemService("audio");
            if (var5 != null) {
               var5.playSoundEffect(0);
            } else {
               Log.w("AppCompatDelegate", "Couldn't get audio manager");
            }
         }

         return var4;
      }
   }

   private void openPanel(AppCompatDelegateImpl.PanelFeatureState var1, KeyEvent var2) {
      if (!var1.isOpen && !this.mIsDestroyed) {
         if (var1.featureId == 0) {
            boolean var3;
            if ((this.mContext.getResources().getConfiguration().screenLayout & 15) == 4) {
               var3 = true;
            } else {
               var3 = false;
            }

            if (var3) {
               return;
            }
         }

         Callback var4 = this.getWindowCallback();
         if (var4 != null && !var4.onMenuOpened(var1.featureId, var1.menu)) {
            this.closePanel(var1, true);
         } else {
            WindowManager var5 = (WindowManager)this.mContext.getSystemService("window");
            if (var5 != null) {
               if (this.preparePanel(var1, var2)) {
                  byte var9;
                  label99: {
                     LayoutParams var6;
                     if (var1.decorView != null && !var1.refreshDecorView) {
                        if (var1.createdPanelView != null) {
                           var6 = var1.createdPanelView.getLayoutParams();
                           if (var6 != null && var6.width == -1) {
                              var9 = -1;
                              break label99;
                           }
                        }
                     } else {
                        if (var1.decorView == null) {
                           if (!this.initializePanelDecor(var1) || var1.decorView == null) {
                              return;
                           }
                        } else if (var1.refreshDecorView && var1.decorView.getChildCount() > 0) {
                           var1.decorView.removeAllViews();
                        }

                        if (!this.initializePanelContent(var1) || !var1.hasPanelItems()) {
                           return;
                        }

                        LayoutParams var10 = var1.shownPanelView.getLayoutParams();
                        var6 = var10;
                        if (var10 == null) {
                           var6 = new LayoutParams(-2, -2);
                        }

                        int var8 = var1.background;
                        var1.decorView.setBackgroundResource(var8);
                        ViewParent var11 = var1.shownPanelView.getParent();
                        if (var11 != null && var11 instanceof ViewGroup) {
                           ((ViewGroup)var11).removeView(var1.shownPanelView);
                        }

                        var1.decorView.addView(var1.shownPanelView, var6);
                        if (!var1.shownPanelView.hasFocus()) {
                           var1.shownPanelView.requestFocus();
                        }
                     }

                     var9 = -2;
                  }

                  var1.isHandled = false;
                  android.view.WindowManager.LayoutParams var7 = new android.view.WindowManager.LayoutParams(var9, -2, var1.x, var1.y, 1002, 8519680, -3);
                  var7.gravity = var1.gravity;
                  var7.windowAnimations = var1.windowAnimations;
                  var5.addView(var1.decorView, var7);
                  var1.isOpen = true;
               }
            }
         }
      }
   }

   private boolean performPanelShortcut(AppCompatDelegateImpl.PanelFeatureState var1, int var2, KeyEvent var3, int var4) {
      boolean var5 = var3.isSystem();
      boolean var6 = false;
      if (var5) {
         return false;
      } else {
         label24: {
            if (!var1.isPrepared) {
               var5 = var6;
               if (!this.preparePanel(var1, var3)) {
                  break label24;
               }
            }

            var5 = var6;
            if (var1.menu != null) {
               var5 = var1.menu.performShortcut(var2, var3, var4);
            }
         }

         if (var5 && (var4 & 1) == 0 && this.mDecorContentParent == null) {
            this.closePanel(var1, true);
         }

         return var5;
      }
   }

   private boolean preparePanel(AppCompatDelegateImpl.PanelFeatureState var1, KeyEvent var2) {
      if (this.mIsDestroyed) {
         return false;
      } else if (var1.isPrepared) {
         return true;
      } else {
         if (this.mPreparedPanel != null && this.mPreparedPanel != var1) {
            this.closePanel(this.mPreparedPanel, false);
         }

         Callback var3 = this.getWindowCallback();
         if (var3 != null) {
            var1.createdPanelView = var3.onCreatePanelView(var1.featureId);
         }

         boolean var4;
         if (var1.featureId != 0 && var1.featureId != 108) {
            var4 = false;
         } else {
            var4 = true;
         }

         if (var4 && this.mDecorContentParent != null) {
            this.mDecorContentParent.setMenuPrepared();
         }

         if (var1.createdPanelView == null && (!var4 || !(this.peekSupportActionBar() instanceof ToolbarActionBar))) {
            if (var1.menu == null || var1.refreshMenuContent) {
               if (var1.menu == null && (!this.initializePanelMenu(var1) || var1.menu == null)) {
                  return false;
               }

               if (var4 && this.mDecorContentParent != null) {
                  if (this.mActionMenuPresenterCallback == null) {
                     this.mActionMenuPresenterCallback = new AppCompatDelegateImpl.ActionMenuPresenterCallback();
                  }

                  this.mDecorContentParent.setMenu(var1.menu, this.mActionMenuPresenterCallback);
               }

               var1.menu.stopDispatchingItemsChanged();
               if (!var3.onCreatePanelMenu(var1.featureId, var1.menu)) {
                  var1.setMenu((MenuBuilder)null);
                  if (var4 && this.mDecorContentParent != null) {
                     this.mDecorContentParent.setMenu((Menu)null, this.mActionMenuPresenterCallback);
                  }

                  return false;
               }

               var1.refreshMenuContent = false;
            }

            var1.menu.stopDispatchingItemsChanged();
            if (var1.frozenActionViewState != null) {
               var1.menu.restoreActionViewStates(var1.frozenActionViewState);
               var1.frozenActionViewState = null;
            }

            if (!var3.onPreparePanel(0, var1.createdPanelView, var1.menu)) {
               if (var4 && this.mDecorContentParent != null) {
                  this.mDecorContentParent.setMenu((Menu)null, this.mActionMenuPresenterCallback);
               }

               var1.menu.startDispatchingItemsChanged();
               return false;
            }

            int var6;
            if (var2 != null) {
               var6 = var2.getDeviceId();
            } else {
               var6 = -1;
            }

            boolean var5;
            if (KeyCharacterMap.load(var6).getKeyboardType() != 1) {
               var5 = true;
            } else {
               var5 = false;
            }

            var1.qwertyMode = var5;
            var1.menu.setQwertyMode(var1.qwertyMode);
            var1.menu.startDispatchingItemsChanged();
         }

         var1.isPrepared = true;
         var1.isHandled = false;
         this.mPreparedPanel = var1;
         return true;
      }
   }

   private void reopenMenu(MenuBuilder var1, boolean var2) {
      if (this.mDecorContentParent != null && this.mDecorContentParent.canShowOverflowMenu() && (!ViewConfiguration.get(this.mContext).hasPermanentMenuKey() || this.mDecorContentParent.isOverflowMenuShowPending())) {
         Callback var5 = this.getWindowCallback();
         if (this.mDecorContentParent.isOverflowMenuShowing() && var2) {
            this.mDecorContentParent.hideOverflowMenu();
            if (!this.mIsDestroyed) {
               var5.onPanelClosed(108, this.getPanelState(0, true).menu);
            }
         } else if (var5 != null && !this.mIsDestroyed) {
            if (this.mInvalidatePanelMenuPosted && (this.mInvalidatePanelMenuFeatures & 1) != 0) {
               this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
               this.mInvalidatePanelMenuRunnable.run();
            }

            AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(0, true);
            if (var3.menu != null && !var3.refreshMenuContent && var5.onPreparePanel(0, var3.createdPanelView, var3.menu)) {
               var5.onMenuOpened(108, var3.menu);
               this.mDecorContentParent.showOverflowMenu();
            }
         }

      } else {
         AppCompatDelegateImpl.PanelFeatureState var4 = this.getPanelState(0, true);
         var4.refreshDecorView = true;
         this.closePanel(var4, false);
         this.openPanel(var4, (KeyEvent)null);
      }
   }

   private int sanitizeWindowFeatureId(int var1) {
      if (var1 == 8) {
         Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
         return 108;
      } else if (var1 == 9) {
         Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
         return 109;
      } else {
         return var1;
      }
   }

   private boolean shouldInheritContext(ViewParent var1) {
      if (var1 == null) {
         return false;
      } else {
         for(View var2 = this.mWindow.getDecorView(); var1 != null; var1 = var1.getParent()) {
            if (var1 == var2 || !(var1 instanceof View) || ViewCompat.isAttachedToWindow((View)var1)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean shouldRecreateOnNightModeChange() {
      boolean var1 = this.mApplyDayNightCalled;
      boolean var2 = false;
      if (var1 && this.mContext instanceof Activity) {
         PackageManager var3 = this.mContext.getPackageManager();

         int var5;
         try {
            ComponentName var4 = new ComponentName(this.mContext, this.mContext.getClass());
            var5 = var3.getActivityInfo(var4, 0).configChanges;
         } catch (NameNotFoundException var6) {
            Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", var6);
            return true;
         }

         if ((var5 & 512) == 0) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   private void throwFeatureRequestIfSubDecorInstalled() {
      if (this.mSubDecorInstalled) {
         throw new AndroidRuntimeException("Window feature must be requested before adding content");
      }
   }

   private boolean updateForNightMode(int var1) {
      Resources var2 = this.mContext.getResources();
      Configuration var3 = var2.getConfiguration();
      int var4 = var3.uiMode;
      byte var6;
      if (var1 == 2) {
         var6 = 32;
      } else {
         var6 = 16;
      }

      if ((var4 & 48) != var6) {
         if (this.shouldRecreateOnNightModeChange()) {
            ((Activity)this.mContext).recreate();
         } else {
            var3 = new Configuration(var3);
            DisplayMetrics var5 = var2.getDisplayMetrics();
            var3.uiMode = var6 | var3.uiMode & -49;
            var2.updateConfiguration(var3, var5);
            if (VERSION.SDK_INT < 26) {
               ResourcesFlusher.flush(var2);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void addContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ((ViewGroup)this.mSubDecor.findViewById(16908290)).addView(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public boolean applyDayNight() {
      int var1 = this.getNightMode();
      int var2 = this.mapNightMode(var1);
      boolean var3;
      if (var2 != -1) {
         var3 = this.updateForNightMode(var2);
      } else {
         var3 = false;
      }

      if (var1 == 0) {
         this.ensureAutoNightModeManager();
         this.mAutoNightModeManager.setup();
      }

      this.mApplyDayNightCalled = true;
      return var3;
   }

   void callOnPanelClosed(int var1, AppCompatDelegateImpl.PanelFeatureState var2, Menu var3) {
      AppCompatDelegateImpl.PanelFeatureState var4 = var2;
      Object var5 = var3;
      if (var3 == null) {
         AppCompatDelegateImpl.PanelFeatureState var6 = var2;
         if (var2 == null) {
            var6 = var2;
            if (var1 >= 0) {
               var6 = var2;
               if (var1 < this.mPanels.length) {
                  var6 = this.mPanels[var1];
               }
            }
         }

         var4 = var6;
         var5 = var3;
         if (var6 != null) {
            var5 = var6.menu;
            var4 = var6;
         }
      }

      if (var4 == null || var4.isOpen) {
         if (!this.mIsDestroyed) {
            this.mOriginalWindowCallback.onPanelClosed(var1, (Menu)var5);
         }

      }
   }

   void checkCloseActionMenu(MenuBuilder var1) {
      if (!this.mClosingActionMenu) {
         this.mClosingActionMenu = true;
         this.mDecorContentParent.dismissPopups();
         Callback var2 = this.getWindowCallback();
         if (var2 != null && !this.mIsDestroyed) {
            var2.onPanelClosed(108, var1);
         }

         this.mClosingActionMenu = false;
      }
   }

   void closePanel(int var1) {
      this.closePanel(this.getPanelState(var1, true), true);
   }

   void closePanel(AppCompatDelegateImpl.PanelFeatureState var1, boolean var2) {
      if (var2 && var1.featureId == 0 && this.mDecorContentParent != null && this.mDecorContentParent.isOverflowMenuShowing()) {
         this.checkCloseActionMenu(var1.menu);
      } else {
         WindowManager var3 = (WindowManager)this.mContext.getSystemService("window");
         if (var3 != null && var1.isOpen && var1.decorView != null) {
            var3.removeView(var1.decorView);
            if (var2) {
               this.callOnPanelClosed(var1.featureId, var1, (Menu)null);
            }
         }

         var1.isPrepared = false;
         var1.isHandled = false;
         var1.isOpen = false;
         var1.shownPanelView = null;
         var1.refreshDecorView = true;
         if (this.mPreparedPanel == var1) {
            this.mPreparedPanel = null;
         }

      }
   }

   public View createView(View var1, String var2, Context var3, AttributeSet var4) {
      AppCompatViewInflater var5 = this.mAppCompatViewInflater;
      boolean var6 = false;
      if (var5 == null) {
         String var7 = this.mContext.obtainStyledAttributes(R.styleable.AppCompatTheme).getString(R.styleable.AppCompatTheme_viewInflaterClass);
         if (var7 != null && !AppCompatViewInflater.class.getName().equals(var7)) {
            try {
               this.mAppCompatViewInflater = (AppCompatViewInflater)Class.forName(var7).getDeclaredConstructor().newInstance();
            } catch (Throwable var9) {
               StringBuilder var10 = new StringBuilder();
               var10.append("Failed to instantiate custom view inflater ");
               var10.append(var7);
               var10.append(". Falling back to default.");
               Log.i("AppCompatDelegate", var10.toString(), var9);
               this.mAppCompatViewInflater = new AppCompatViewInflater();
            }
         } else {
            this.mAppCompatViewInflater = new AppCompatViewInflater();
         }
      }

      if (IS_PRE_LOLLIPOP) {
         if (var4 instanceof XmlPullParser) {
            if (((XmlPullParser)var4).getDepth() > 1) {
               var6 = true;
            }
         } else {
            var6 = this.shouldInheritContext((ViewParent)var1);
         }
      } else {
         var6 = false;
      }

      return this.mAppCompatViewInflater.createView(var1, var2, var3, var4, var6, IS_PRE_LOLLIPOP, true, VectorEnabledTintResources.shouldBeUsed());
   }

   void dismissPopups() {
      if (this.mDecorContentParent != null) {
         this.mDecorContentParent.dismissPopups();
      }

      if (this.mActionModePopup != null) {
         this.mWindow.getDecorView().removeCallbacks(this.mShowActionModePopup);
         if (this.mActionModePopup.isShowing()) {
            try {
               this.mActionModePopup.dismiss();
            } catch (IllegalArgumentException var2) {
            }
         }

         this.mActionModePopup = null;
      }

      this.endOnGoingFadeAnimation();
      AppCompatDelegateImpl.PanelFeatureState var1 = this.getPanelState(0, false);
      if (var1 != null && var1.menu != null) {
         var1.menu.close();
      }

   }

   boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var2 = this.mOriginalWindowCallback instanceof KeyEventDispatcher.Component;
      boolean var3 = true;
      if (var2 || this.mOriginalWindowCallback instanceof AppCompatDialog) {
         View var4 = this.mWindow.getDecorView();
         if (var4 != null && KeyEventDispatcher.dispatchBeforeHierarchy(var4, var1)) {
            return true;
         }
      }

      if (var1.getKeyCode() == 82 && this.mOriginalWindowCallback.dispatchKeyEvent(var1)) {
         return true;
      } else {
         int var5 = var1.getKeyCode();
         if (var1.getAction() != 0) {
            var3 = false;
         }

         if (var3) {
            var2 = this.onKeyDown(var5, var1);
         } else {
            var2 = this.onKeyUp(var5, var1);
         }

         return var2;
      }
   }

   void doInvalidatePanelMenu(int var1) {
      AppCompatDelegateImpl.PanelFeatureState var2 = this.getPanelState(var1, true);
      if (var2.menu != null) {
         Bundle var3 = new Bundle();
         var2.menu.saveActionViewStates(var3);
         if (var3.size() > 0) {
            var2.frozenActionViewState = var3;
         }

         var2.menu.stopDispatchingItemsChanged();
         var2.menu.clear();
      }

      var2.refreshMenuContent = true;
      var2.refreshDecorView = true;
      if ((var1 == 108 || var1 == 0) && this.mDecorContentParent != null) {
         AppCompatDelegateImpl.PanelFeatureState var4 = this.getPanelState(0, false);
         if (var4 != null) {
            var4.isPrepared = false;
            this.preparePanel(var4, (KeyEvent)null);
         }
      }

   }

   void endOnGoingFadeAnimation() {
      if (this.mFadeAnim != null) {
         this.mFadeAnim.cancel();
      }

   }

   AppCompatDelegateImpl.PanelFeatureState findMenuPanel(Menu var1) {
      AppCompatDelegateImpl.PanelFeatureState[] var2 = this.mPanels;
      int var3 = 0;
      int var4;
      if (var2 != null) {
         var4 = var2.length;
      } else {
         var4 = 0;
      }

      while(var3 < var4) {
         AppCompatDelegateImpl.PanelFeatureState var5 = var2[var3];
         if (var5 != null && var5.menu == var1) {
            return var5;
         }

         ++var3;
      }

      return null;
   }

   public View findViewById(int var1) {
      this.ensureSubDecor();
      return this.mWindow.findViewById(var1);
   }

   final Context getActionBarThemedContext() {
      ActionBar var1 = this.getSupportActionBar();
      Context var3;
      if (var1 != null) {
         var3 = var1.getThemedContext();
      } else {
         var3 = null;
      }

      Context var2 = var3;
      if (var3 == null) {
         var2 = this.mContext;
      }

      return var2;
   }

   public MenuInflater getMenuInflater() {
      if (this.mMenuInflater == null) {
         this.initWindowDecorActionBar();
         Context var1;
         if (this.mActionBar != null) {
            var1 = this.mActionBar.getThemedContext();
         } else {
            var1 = this.mContext;
         }

         this.mMenuInflater = new SupportMenuInflater(var1);
      }

      return this.mMenuInflater;
   }

   protected AppCompatDelegateImpl.PanelFeatureState getPanelState(int var1, boolean var2) {
      AppCompatDelegateImpl.PanelFeatureState[] var4;
      label22: {
         AppCompatDelegateImpl.PanelFeatureState[] var3 = this.mPanels;
         if (var3 != null) {
            var4 = var3;
            if (var3.length > var1) {
               break label22;
            }
         }

         var4 = new AppCompatDelegateImpl.PanelFeatureState[var1 + 1];
         if (var3 != null) {
            System.arraycopy(var3, 0, var4, 0, var3.length);
         }

         this.mPanels = var4;
      }

      AppCompatDelegateImpl.PanelFeatureState var5 = var4[var1];
      AppCompatDelegateImpl.PanelFeatureState var6 = var5;
      if (var5 == null) {
         var6 = new AppCompatDelegateImpl.PanelFeatureState(var1);
         var4[var1] = var6;
      }

      return var6;
   }

   public ActionBar getSupportActionBar() {
      this.initWindowDecorActionBar();
      return this.mActionBar;
   }

   final CharSequence getTitle() {
      return this.mOriginalWindowCallback instanceof Activity ? ((Activity)this.mOriginalWindowCallback).getTitle() : this.mTitle;
   }

   final Callback getWindowCallback() {
      return this.mWindow.getCallback();
   }

   public void installViewFactory() {
      LayoutInflater var1 = LayoutInflater.from(this.mContext);
      if (var1.getFactory() == null) {
         LayoutInflaterCompat.setFactory2(var1, this);
      } else if (!(var1.getFactory2() instanceof AppCompatDelegateImpl)) {
         Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
      }

   }

   public void invalidateOptionsMenu() {
      ActionBar var1 = this.getSupportActionBar();
      if (var1 == null || !var1.invalidateOptionsMenu()) {
         this.invalidatePanelMenu(0);
      }
   }

   public boolean isHandleNativeActionModesEnabled() {
      return this.mHandleNativeActionModes;
   }

   int mapNightMode(int var1) {
      if (var1 != -100) {
         if (var1 != 0) {
            return var1;
         } else if (VERSION.SDK_INT >= 23 && ((UiModeManager)this.mContext.getSystemService(UiModeManager.class)).getNightMode() == 0) {
            return -1;
         } else {
            this.ensureAutoNightModeManager();
            return this.mAutoNightModeManager.getApplyableNightMode();
         }
      } else {
         return -1;
      }
   }

   boolean onBackPressed() {
      if (this.mActionMode != null) {
         this.mActionMode.finish();
         return true;
      } else {
         ActionBar var1 = this.getSupportActionBar();
         return var1 != null && var1.collapseActionView();
      }
   }

   public void onConfigurationChanged(Configuration var1) {
      if (this.mHasActionBar && this.mSubDecorInstalled) {
         ActionBar var2 = this.getSupportActionBar();
         if (var2 != null) {
            var2.onConfigurationChanged(var1);
         }
      }

      AppCompatDrawableManager.get().onConfigurationChanged(this.mContext);
      this.applyDayNight();
   }

   public void onCreate(Bundle var1) {
      if (this.mOriginalWindowCallback instanceof Activity) {
         String var2 = null;

         label26: {
            String var3;
            try {
               var3 = NavUtils.getParentActivityName((Activity)this.mOriginalWindowCallback);
            } catch (IllegalArgumentException var4) {
               break label26;
            }

            var2 = var3;
         }

         if (var2 != null) {
            ActionBar var5 = this.peekSupportActionBar();
            if (var5 == null) {
               this.mEnableDefaultActionBarUp = true;
            } else {
               var5.setDefaultDisplayHomeAsUpEnabled(true);
            }
         }
      }

      if (var1 != null && this.mLocalNightMode == -100) {
         this.mLocalNightMode = var1.getInt("appcompat:local_night_mode", -100);
      }

   }

   public final View onCreateView(View var1, String var2, Context var3, AttributeSet var4) {
      return this.createView(var1, var2, var3, var4);
   }

   public View onCreateView(String var1, Context var2, AttributeSet var3) {
      return this.onCreateView((View)null, var1, var2, var3);
   }

   public void onDestroy() {
      if (this.mInvalidatePanelMenuPosted) {
         this.mWindow.getDecorView().removeCallbacks(this.mInvalidatePanelMenuRunnable);
      }

      this.mIsDestroyed = true;
      if (this.mActionBar != null) {
         this.mActionBar.onDestroy();
      }

      if (this.mAutoNightModeManager != null) {
         this.mAutoNightModeManager.cleanup();
      }

   }

   boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = true;
      if (var1 != 4) {
         if (var1 == 82) {
            this.onKeyDownPanel(0, var2);
            return true;
         }
      } else {
         if ((var2.getFlags() & 128) == 0) {
            var3 = false;
         }

         this.mLongPressBackDown = var3;
      }

      return false;
   }

   boolean onKeyShortcut(int var1, KeyEvent var2) {
      ActionBar var3 = this.getSupportActionBar();
      if (var3 != null && var3.onKeyShortcut(var1, var2)) {
         return true;
      } else if (this.mPreparedPanel != null && this.performPanelShortcut(this.mPreparedPanel, var2.getKeyCode(), var2, 1)) {
         if (this.mPreparedPanel != null) {
            this.mPreparedPanel.isHandled = true;
         }

         return true;
      } else {
         if (this.mPreparedPanel == null) {
            AppCompatDelegateImpl.PanelFeatureState var5 = this.getPanelState(0, true);
            this.preparePanel(var5, var2);
            boolean var4 = this.performPanelShortcut(var5, var2.getKeyCode(), var2, 1);
            var5.isPrepared = false;
            if (var4) {
               return true;
            }
         }

         return false;
      }
   }

   boolean onKeyUp(int var1, KeyEvent var2) {
      if (var1 != 4) {
         if (var1 == 82) {
            this.onKeyUpPanel(0, var2);
            return true;
         }
      } else {
         boolean var3 = this.mLongPressBackDown;
         this.mLongPressBackDown = false;
         AppCompatDelegateImpl.PanelFeatureState var4 = this.getPanelState(0, false);
         if (var4 != null && var4.isOpen) {
            if (!var3) {
               this.closePanel(var4, true);
            }

            return true;
         }

         if (this.onBackPressed()) {
            return true;
         }
      }

      return false;
   }

   public boolean onMenuItemSelected(MenuBuilder var1, MenuItem var2) {
      Callback var3 = this.getWindowCallback();
      if (var3 != null && !this.mIsDestroyed) {
         AppCompatDelegateImpl.PanelFeatureState var4 = this.findMenuPanel(var1.getRootMenu());
         if (var4 != null) {
            return var3.onMenuItemSelected(var4.featureId, var2);
         }
      }

      return false;
   }

   public void onMenuModeChange(MenuBuilder var1) {
      this.reopenMenu(var1, true);
   }

   void onMenuOpened(int var1) {
      if (var1 == 108) {
         ActionBar var2 = this.getSupportActionBar();
         if (var2 != null) {
            var2.dispatchMenuVisibilityChanged(true);
         }
      }

   }

   void onPanelClosed(int var1) {
      if (var1 == 108) {
         ActionBar var2 = this.getSupportActionBar();
         if (var2 != null) {
            var2.dispatchMenuVisibilityChanged(false);
         }
      } else if (var1 == 0) {
         AppCompatDelegateImpl.PanelFeatureState var3 = this.getPanelState(var1, true);
         if (var3.isOpen) {
            this.closePanel(var3, false);
         }
      }

   }

   public void onPostCreate(Bundle var1) {
      this.ensureSubDecor();
   }

   public void onPostResume() {
      ActionBar var1 = this.getSupportActionBar();
      if (var1 != null) {
         var1.setShowHideAnimationEnabled(true);
      }

   }

   public void onSaveInstanceState(Bundle var1) {
      if (this.mLocalNightMode != -100) {
         var1.putInt("appcompat:local_night_mode", this.mLocalNightMode);
      }

   }

   public void onStart() {
      this.applyDayNight();
   }

   public void onStop() {
      ActionBar var1 = this.getSupportActionBar();
      if (var1 != null) {
         var1.setShowHideAnimationEnabled(false);
      }

      if (this.mAutoNightModeManager != null) {
         this.mAutoNightModeManager.cleanup();
      }

   }

   void onSubDecorInstalled(ViewGroup var1) {
   }

   final ActionBar peekSupportActionBar() {
      return this.mActionBar;
   }

   public boolean requestWindowFeature(int var1) {
      var1 = this.sanitizeWindowFeatureId(var1);
      if (this.mWindowNoTitle && var1 == 108) {
         return false;
      } else {
         if (this.mHasActionBar && var1 == 1) {
            this.mHasActionBar = false;
         }

         switch(var1) {
         case 1:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mWindowNoTitle = true;
            return true;
         case 2:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureProgress = true;
            return true;
         case 5:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mFeatureIndeterminateProgress = true;
            return true;
         case 10:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionMode = true;
            return true;
         case 108:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mHasActionBar = true;
            return true;
         case 109:
            this.throwFeatureRequestIfSubDecorInstalled();
            this.mOverlayActionBar = true;
            return true;
         default:
            return this.mWindow.requestFeature(var1);
         }
      }
   }

   public void setContentView(int var1) {
      this.ensureSubDecor();
      ViewGroup var2 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var2.removeAllViews();
      LayoutInflater.from(this.mContext).inflate(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setContentView(View var1) {
      this.ensureSubDecor();
      ViewGroup var2 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var2.removeAllViews();
      var2.addView(var1);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setContentView(View var1, LayoutParams var2) {
      this.ensureSubDecor();
      ViewGroup var3 = (ViewGroup)this.mSubDecor.findViewById(16908290);
      var3.removeAllViews();
      var3.addView(var1, var2);
      this.mOriginalWindowCallback.onContentChanged();
   }

   public void setSupportActionBar(Toolbar var1) {
      if (this.mOriginalWindowCallback instanceof Activity) {
         ActionBar var2 = this.getSupportActionBar();
         if (!(var2 instanceof WindowDecorActionBar)) {
            this.mMenuInflater = null;
            if (var2 != null) {
               var2.onDestroy();
            }

            if (var1 != null) {
               ToolbarActionBar var3 = new ToolbarActionBar(var1, ((Activity)this.mOriginalWindowCallback).getTitle(), this.mAppCompatWindowCallback);
               this.mActionBar = var3;
               this.mWindow.setCallback(var3.getWrappedWindowCallback());
            } else {
               this.mActionBar = null;
               this.mWindow.setCallback(this.mAppCompatWindowCallback);
            }

            this.invalidateOptionsMenu();
         } else {
            throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
         }
      }
   }

   public final void setTitle(CharSequence var1) {
      this.mTitle = var1;
      if (this.mDecorContentParent != null) {
         this.mDecorContentParent.setWindowTitle(var1);
      } else if (this.peekSupportActionBar() != null) {
         this.peekSupportActionBar().setWindowTitle(var1);
      } else if (this.mTitleView != null) {
         this.mTitleView.setText(var1);
      }

   }

   final boolean shouldAnimateActionModeView() {
      boolean var1;
      if (this.mSubDecorInstalled && this.mSubDecor != null && ViewCompat.isLaidOut(this.mSubDecor)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public ActionMode startSupportActionMode(ActionMode.Callback var1) {
      if (var1 != null) {
         if (this.mActionMode != null) {
            this.mActionMode.finish();
         }

         AppCompatDelegateImpl.ActionModeCallbackWrapperV9 var3 = new AppCompatDelegateImpl.ActionModeCallbackWrapperV9(var1);
         ActionBar var2 = this.getSupportActionBar();
         if (var2 != null) {
            this.mActionMode = var2.startActionMode(var3);
            if (this.mActionMode != null && this.mAppCompatCallback != null) {
               this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
            }
         }

         if (this.mActionMode == null) {
            this.mActionMode = this.startSupportActionModeFromWindow(var3);
         }

         return this.mActionMode;
      } else {
         throw new IllegalArgumentException("ActionMode callback can not be null.");
      }
   }

   ActionMode startSupportActionModeFromWindow(ActionMode.Callback var1) {
      this.endOnGoingFadeAnimation();
      if (this.mActionMode != null) {
         this.mActionMode.finish();
      }

      Object var2 = var1;
      if (!(var1 instanceof AppCompatDelegateImpl.ActionModeCallbackWrapperV9)) {
         var2 = new AppCompatDelegateImpl.ActionModeCallbackWrapperV9(var1);
      }

      ActionMode var8;
      label67: {
         if (this.mAppCompatCallback != null && !this.mIsDestroyed) {
            try {
               var8 = this.mAppCompatCallback.onWindowStartingSupportActionMode((ActionMode.Callback)var2);
               break label67;
            } catch (AbstractMethodError var7) {
            }
         }

         var8 = null;
      }

      if (var8 != null) {
         this.mActionMode = var8;
      } else {
         ActionBarContextView var9 = this.mActionModeView;
         boolean var3 = true;
         if (var9 == null) {
            if (this.mIsFloating) {
               TypedValue var4 = new TypedValue();
               Theme var10 = this.mContext.getTheme();
               var10.resolveAttribute(R.attr.actionBarTheme, var4, true);
               Object var11;
               if (var4.resourceId != 0) {
                  Theme var5 = this.mContext.getResources().newTheme();
                  var5.setTo(var10);
                  var5.applyStyle(var4.resourceId, true);
                  var11 = new ContextThemeWrapper(this.mContext, 0);
                  ((Context)var11).getTheme().setTo(var5);
               } else {
                  var11 = this.mContext;
               }

               this.mActionModeView = new ActionBarContextView((Context)var11);
               this.mActionModePopup = new PopupWindow((Context)var11, (AttributeSet)null, R.attr.actionModePopupWindowStyle);
               PopupWindowCompat.setWindowLayoutType(this.mActionModePopup, 2);
               this.mActionModePopup.setContentView(this.mActionModeView);
               this.mActionModePopup.setWidth(-1);
               ((Context)var11).getTheme().resolveAttribute(R.attr.actionBarSize, var4, true);
               int var6 = TypedValue.complexToDimensionPixelSize(var4.data, ((Context)var11).getResources().getDisplayMetrics());
               this.mActionModeView.setContentHeight(var6);
               this.mActionModePopup.setHeight(-2);
               this.mShowActionModePopup = new Runnable() {
                  public void run() {
                     AppCompatDelegateImpl.this.mActionModePopup.showAtLocation(AppCompatDelegateImpl.this.mActionModeView, 55, 0, 0);
                     AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
                     if (AppCompatDelegateImpl.this.shouldAnimateActionModeView()) {
                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(0.0F);
                        AppCompatDelegateImpl.this.mFadeAnim = ViewCompat.animate(AppCompatDelegateImpl.this.mActionModeView).alpha(1.0F);
                        AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                           public void onAnimationEnd(View var1) {
                              AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                              AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                              AppCompatDelegateImpl.this.mFadeAnim = null;
                           }

                           public void onAnimationStart(View var1) {
                              AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                           }
                        });
                     } else {
                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                     }

                  }
               };
            } else {
               ViewStubCompat var12 = (ViewStubCompat)this.mSubDecor.findViewById(R.id.action_mode_bar_stub);
               if (var12 != null) {
                  var12.setLayoutInflater(LayoutInflater.from(this.getActionBarThemedContext()));
                  this.mActionModeView = (ActionBarContextView)var12.inflate();
               }
            }
         }

         if (this.mActionModeView != null) {
            this.endOnGoingFadeAnimation();
            this.mActionModeView.killMode();
            Context var13 = this.mActionModeView.getContext();
            ActionBarContextView var14 = this.mActionModeView;
            if (this.mActionModePopup != null) {
               var3 = false;
            }

            StandaloneActionMode var15 = new StandaloneActionMode(var13, var14, (ActionMode.Callback)var2, var3);
            if (((ActionMode.Callback)var2).onCreateActionMode(var15, var15.getMenu())) {
               var15.invalidate();
               this.mActionModeView.initForMode(var15);
               this.mActionMode = var15;
               if (this.shouldAnimateActionModeView()) {
                  this.mActionModeView.setAlpha(0.0F);
                  this.mFadeAnim = ViewCompat.animate(this.mActionModeView).alpha(1.0F);
                  this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                     public void onAnimationEnd(View var1) {
                        AppCompatDelegateImpl.this.mActionModeView.setAlpha(1.0F);
                        AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                        AppCompatDelegateImpl.this.mFadeAnim = null;
                     }

                     public void onAnimationStart(View var1) {
                        AppCompatDelegateImpl.this.mActionModeView.setVisibility(0);
                        AppCompatDelegateImpl.this.mActionModeView.sendAccessibilityEvent(32);
                        if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                           ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                        }

                     }
                  });
               } else {
                  this.mActionModeView.setAlpha(1.0F);
                  this.mActionModeView.setVisibility(0);
                  this.mActionModeView.sendAccessibilityEvent(32);
                  if (this.mActionModeView.getParent() instanceof View) {
                     ViewCompat.requestApplyInsets((View)this.mActionModeView.getParent());
                  }
               }

               if (this.mActionModePopup != null) {
                  this.mWindow.getDecorView().post(this.mShowActionModePopup);
               }
            } else {
               this.mActionMode = null;
            }
         }
      }

      if (this.mActionMode != null && this.mAppCompatCallback != null) {
         this.mAppCompatCallback.onSupportActionModeStarted(this.mActionMode);
      }

      return this.mActionMode;
   }

   int updateStatusGuard(int var1) {
      ActionBarContextView var2 = this.mActionModeView;
      byte var3 = 0;
      boolean var9;
      if (var2 != null && this.mActionModeView.getLayoutParams() instanceof MarginLayoutParams) {
         MarginLayoutParams var12 = (MarginLayoutParams)this.mActionModeView.getLayoutParams();
         boolean var4 = this.mActionModeView.isShown();
         boolean var5 = true;
         boolean var10;
         int var11;
         boolean var15;
         if (var4) {
            if (this.mTempRect1 == null) {
               this.mTempRect1 = new Rect();
               this.mTempRect2 = new Rect();
            }

            Rect var6 = this.mTempRect1;
            Rect var7 = this.mTempRect2;
            var6.set(0, var1, 0, 0);
            ViewUtils.computeFitSystemWindows(this.mSubDecor, var6, var7);
            int var8;
            if (var7.top == 0) {
               var8 = var1;
            } else {
               var8 = 0;
            }

            if (var12.topMargin != var8) {
               var12.topMargin = var1;
               if (this.mStatusGuard == null) {
                  this.mStatusGuard = new View(this.mContext);
                  this.mStatusGuard.setBackgroundColor(this.mContext.getResources().getColor(R.color.abc_input_method_navigation_guard));
                  this.mSubDecor.addView(this.mStatusGuard, -1, new LayoutParams(-1, var1));
               } else {
                  LayoutParams var14 = this.mStatusGuard.getLayoutParams();
                  if (var14.height != var1) {
                     var14.height = var1;
                     this.mStatusGuard.setLayoutParams(var14);
                  }
               }

               var9 = true;
            } else {
               var9 = false;
            }

            if (this.mStatusGuard == null) {
               var5 = false;
            }

            var10 = var9;
            var15 = var5;
            var11 = var1;
            if (!this.mOverlayActionMode) {
               var10 = var9;
               var15 = var5;
               var11 = var1;
               if (var5) {
                  var11 = 0;
                  var10 = var9;
                  var15 = var5;
               }
            }
         } else {
            if (var12.topMargin != 0) {
               var12.topMargin = 0;
               var9 = true;
            } else {
               var9 = false;
            }

            var15 = false;
            var11 = var1;
            var10 = var9;
         }

         var9 = var15;
         var1 = var11;
         if (var10) {
            this.mActionModeView.setLayoutParams(var12);
            var9 = var15;
            var1 = var11;
         }
      } else {
         var9 = false;
      }

      if (this.mStatusGuard != null) {
         View var13 = this.mStatusGuard;
         byte var16;
         if (var9) {
            var16 = var3;
         } else {
            var16 = 8;
         }

         var13.setVisibility(var16);
      }

      return var1;
   }

   private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
      ActionMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         AppCompatDelegateImpl.this.checkCloseActionMenu(var1);
      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         Callback var2 = AppCompatDelegateImpl.this.getWindowCallback();
         if (var2 != null) {
            var2.onMenuOpened(108, var1);
         }

         return true;
      }
   }

   class ActionModeCallbackWrapperV9 implements ActionMode.Callback {
      private ActionMode.Callback mWrapped;

      public ActionModeCallbackWrapperV9(ActionMode.Callback var2) {
         this.mWrapped = var2;
      }

      public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
         return this.mWrapped.onActionItemClicked(var1, var2);
      }

      public boolean onCreateActionMode(ActionMode var1, Menu var2) {
         return this.mWrapped.onCreateActionMode(var1, var2);
      }

      public void onDestroyActionMode(ActionMode var1) {
         this.mWrapped.onDestroyActionMode(var1);
         if (AppCompatDelegateImpl.this.mActionModePopup != null) {
            AppCompatDelegateImpl.this.mWindow.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.mShowActionModePopup);
         }

         if (AppCompatDelegateImpl.this.mActionModeView != null) {
            AppCompatDelegateImpl.this.endOnGoingFadeAnimation();
            AppCompatDelegateImpl.this.mFadeAnim = ViewCompat.animate(AppCompatDelegateImpl.this.mActionModeView).alpha(0.0F);
            AppCompatDelegateImpl.this.mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
               public void onAnimationEnd(View var1) {
                  AppCompatDelegateImpl.this.mActionModeView.setVisibility(8);
                  if (AppCompatDelegateImpl.this.mActionModePopup != null) {
                     AppCompatDelegateImpl.this.mActionModePopup.dismiss();
                  } else if (AppCompatDelegateImpl.this.mActionModeView.getParent() instanceof View) {
                     ViewCompat.requestApplyInsets((View)AppCompatDelegateImpl.this.mActionModeView.getParent());
                  }

                  AppCompatDelegateImpl.this.mActionModeView.removeAllViews();
                  AppCompatDelegateImpl.this.mFadeAnim.setListener((ViewPropertyAnimatorListener)null);
                  AppCompatDelegateImpl.this.mFadeAnim = null;
               }
            });
         }

         if (AppCompatDelegateImpl.this.mAppCompatCallback != null) {
            AppCompatDelegateImpl.this.mAppCompatCallback.onSupportActionModeFinished(AppCompatDelegateImpl.this.mActionMode);
         }

         AppCompatDelegateImpl.this.mActionMode = null;
      }

      public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
         return this.mWrapped.onPrepareActionMode(var1, var2);
      }
   }

   class AppCompatWindowCallback extends WindowCallbackWrapper {
      AppCompatWindowCallback(Callback var2) {
         super(var2);
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         boolean var2;
         if (!AppCompatDelegateImpl.this.dispatchKeyEvent(var1) && !super.dispatchKeyEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public boolean dispatchKeyShortcutEvent(KeyEvent var1) {
         boolean var2;
         if (!super.dispatchKeyShortcutEvent(var1) && !AppCompatDelegateImpl.this.onKeyShortcut(var1.getKeyCode(), var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void onContentChanged() {
      }

      public boolean onCreatePanelMenu(int var1, Menu var2) {
         return var1 == 0 && !(var2 instanceof MenuBuilder) ? false : super.onCreatePanelMenu(var1, var2);
      }

      public boolean onMenuOpened(int var1, Menu var2) {
         super.onMenuOpened(var1, var2);
         AppCompatDelegateImpl.this.onMenuOpened(var1);
         return true;
      }

      public void onPanelClosed(int var1, Menu var2) {
         super.onPanelClosed(var1, var2);
         AppCompatDelegateImpl.this.onPanelClosed(var1);
      }

      public boolean onPreparePanel(int var1, View var2, Menu var3) {
         MenuBuilder var4;
         if (var3 instanceof MenuBuilder) {
            var4 = (MenuBuilder)var3;
         } else {
            var4 = null;
         }

         if (var1 == 0 && var4 == null) {
            return false;
         } else {
            if (var4 != null) {
               var4.setOverrideVisibleItems(true);
            }

            boolean var5 = super.onPreparePanel(var1, var2, var3);
            if (var4 != null) {
               var4.setOverrideVisibleItems(false);
            }

            return var5;
         }
      }

      public void onProvideKeyboardShortcuts(List var1, Menu var2, int var3) {
         AppCompatDelegateImpl.PanelFeatureState var4 = AppCompatDelegateImpl.this.getPanelState(0, true);
         if (var4 != null && var4.menu != null) {
            super.onProvideKeyboardShortcuts(var1, var4.menu, var3);
         } else {
            super.onProvideKeyboardShortcuts(var1, var2, var3);
         }

      }

      public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1) {
         if (VERSION.SDK_INT >= 23) {
            return null;
         } else {
            return AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() ? this.startAsSupportActionMode(var1) : super.onWindowStartingActionMode(var1);
         }
      }

      public android.view.ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback var1, int var2) {
         return AppCompatDelegateImpl.this.isHandleNativeActionModesEnabled() && var2 == 0 ? this.startAsSupportActionMode(var1) : super.onWindowStartingActionMode(var1, var2);
      }

      final android.view.ActionMode startAsSupportActionMode(android.view.ActionMode.Callback var1) {
         SupportActionModeWrapper.CallbackWrapper var3 = new SupportActionModeWrapper.CallbackWrapper(AppCompatDelegateImpl.this.mContext, var1);
         ActionMode var2 = AppCompatDelegateImpl.this.startSupportActionMode(var3);
         return var2 != null ? var3.getActionModeWrapper(var2) : null;
      }
   }

   final class AutoNightModeManager {
      private BroadcastReceiver mAutoTimeChangeReceiver;
      private IntentFilter mAutoTimeChangeReceiverFilter;
      private boolean mIsNight;
      private TwilightManager mTwilightManager;

      AutoNightModeManager(TwilightManager var2) {
         this.mTwilightManager = var2;
         this.mIsNight = var2.isNight();
      }

      void cleanup() {
         if (this.mAutoTimeChangeReceiver != null) {
            AppCompatDelegateImpl.this.mContext.unregisterReceiver(this.mAutoTimeChangeReceiver);
            this.mAutoTimeChangeReceiver = null;
         }

      }

      void dispatchTimeChanged() {
         boolean var1 = this.mTwilightManager.isNight();
         if (var1 != this.mIsNight) {
            this.mIsNight = var1;
            AppCompatDelegateImpl.this.applyDayNight();
         }

      }

      int getApplyableNightMode() {
         this.mIsNight = this.mTwilightManager.isNight();
         byte var1;
         if (this.mIsNight) {
            var1 = 2;
         } else {
            var1 = 1;
         }

         return var1;
      }

      void setup() {
         this.cleanup();
         if (this.mAutoTimeChangeReceiver == null) {
            this.mAutoTimeChangeReceiver = new BroadcastReceiver() {
               public void onReceive(Context var1, Intent var2) {
                  AutoNightModeManager.this.dispatchTimeChanged();
               }
            };
         }

         if (this.mAutoTimeChangeReceiverFilter == null) {
            this.mAutoTimeChangeReceiverFilter = new IntentFilter();
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_SET");
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            this.mAutoTimeChangeReceiverFilter.addAction("android.intent.action.TIME_TICK");
         }

         AppCompatDelegateImpl.this.mContext.registerReceiver(this.mAutoTimeChangeReceiver, this.mAutoTimeChangeReceiverFilter);
      }
   }

   private class ListMenuDecorView extends ContentFrameLayout {
      public ListMenuDecorView(Context var2) {
         super(var2);
      }

      private boolean isOutOfBounds(int var1, int var2) {
         boolean var3;
         if (var1 >= -5 && var2 >= -5 && var1 <= this.getWidth() + 5 && var2 <= this.getHeight() + 5) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public boolean dispatchKeyEvent(KeyEvent var1) {
         boolean var2;
         if (!AppCompatDelegateImpl.this.dispatchKeyEvent(var1) && !super.dispatchKeyEvent(var1)) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         if (var1.getAction() == 0 && this.isOutOfBounds((int)var1.getX(), (int)var1.getY())) {
            AppCompatDelegateImpl.this.closePanel(0);
            return true;
         } else {
            return super.onInterceptTouchEvent(var1);
         }
      }

      public void setBackgroundResource(int var1) {
         this.setBackgroundDrawable(AppCompatResources.getDrawable(this.getContext(), var1));
      }
   }

   protected static final class PanelFeatureState {
      int background;
      View createdPanelView;
      ViewGroup decorView;
      int featureId;
      Bundle frozenActionViewState;
      int gravity;
      boolean isHandled;
      boolean isOpen;
      boolean isPrepared;
      ListMenuPresenter listMenuPresenter;
      Context listPresenterContext;
      MenuBuilder menu;
      public boolean qwertyMode;
      boolean refreshDecorView;
      boolean refreshMenuContent;
      View shownPanelView;
      int windowAnimations;
      int x;
      int y;

      PanelFeatureState(int var1) {
         this.featureId = var1;
         this.refreshDecorView = false;
      }

      MenuView getListMenuView(MenuPresenter.Callback var1) {
         if (this.menu == null) {
            return null;
         } else {
            if (this.listMenuPresenter == null) {
               this.listMenuPresenter = new ListMenuPresenter(this.listPresenterContext, R.layout.abc_list_menu_item_layout);
               this.listMenuPresenter.setCallback(var1);
               this.menu.addMenuPresenter(this.listMenuPresenter);
            }

            return this.listMenuPresenter.getMenuView(this.decorView);
         }
      }

      public boolean hasPanelItems() {
         View var1 = this.shownPanelView;
         boolean var2 = false;
         if (var1 == null) {
            return false;
         } else if (this.createdPanelView != null) {
            return true;
         } else {
            if (this.listMenuPresenter.getAdapter().getCount() > 0) {
               var2 = true;
            }

            return var2;
         }
      }

      void setMenu(MenuBuilder var1) {
         if (var1 != this.menu) {
            if (this.menu != null) {
               this.menu.removeMenuPresenter(this.listMenuPresenter);
            }

            this.menu = var1;
            if (var1 != null && this.listMenuPresenter != null) {
               var1.addMenuPresenter(this.listMenuPresenter);
            }

         }
      }

      void setStyle(Context var1) {
         TypedValue var2 = new TypedValue();
         Theme var3 = var1.getResources().newTheme();
         var3.setTo(var1.getTheme());
         var3.resolveAttribute(R.attr.actionBarPopupTheme, var2, true);
         if (var2.resourceId != 0) {
            var3.applyStyle(var2.resourceId, true);
         }

         var3.resolveAttribute(R.attr.panelMenuListTheme, var2, true);
         if (var2.resourceId != 0) {
            var3.applyStyle(var2.resourceId, true);
         } else {
            var3.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
         }

         ContextThemeWrapper var4 = new ContextThemeWrapper(var1, 0);
         var4.getTheme().setTo(var3);
         this.listPresenterContext = var4;
         TypedArray var5 = var4.obtainStyledAttributes(R.styleable.AppCompatTheme);
         this.background = var5.getResourceId(R.styleable.AppCompatTheme_panelBackground, 0);
         this.windowAnimations = var5.getResourceId(R.styleable.AppCompatTheme_android_windowAnimationStyle, 0);
         var5.recycle();
      }
   }

   private final class PanelMenuPresenterCallback implements MenuPresenter.Callback {
      PanelMenuPresenterCallback() {
      }

      public void onCloseMenu(MenuBuilder var1, boolean var2) {
         MenuBuilder var3 = var1.getRootMenu();
         boolean var4;
         if (var3 != var1) {
            var4 = true;
         } else {
            var4 = false;
         }

         AppCompatDelegateImpl var5 = AppCompatDelegateImpl.this;
         if (var4) {
            var1 = var3;
         }

         AppCompatDelegateImpl.PanelFeatureState var6 = var5.findMenuPanel(var1);
         if (var6 != null) {
            if (var4) {
               AppCompatDelegateImpl.this.callOnPanelClosed(var6.featureId, var6, var3);
               AppCompatDelegateImpl.this.closePanel(var6, true);
            } else {
               AppCompatDelegateImpl.this.closePanel(var6, var2);
            }
         }

      }

      public boolean onOpenSubMenu(MenuBuilder var1) {
         if (var1 == null && AppCompatDelegateImpl.this.mHasActionBar) {
            Callback var2 = AppCompatDelegateImpl.this.getWindowCallback();
            if (var2 != null && !AppCompatDelegateImpl.this.mIsDestroyed) {
               var2.onMenuOpened(108, var1);
            }
         }

         return true;
      }
   }
}
