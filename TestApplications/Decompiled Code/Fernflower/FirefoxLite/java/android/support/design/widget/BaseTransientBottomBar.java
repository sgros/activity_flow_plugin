package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Handler.Callback;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.snackbar.ContentViewCallback;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar {
   private static final int[] SNACKBAR_STYLE_ATTR;
   private static final boolean USE_OFFSET_API;
   static final Handler handler;
   private final AccessibilityManager accessibilityManager;
   private BaseTransientBottomBar.Behavior behavior;
   private List callbacks;
   private final ContentViewCallback contentViewCallback;
   private final Context context;
   private int duration;
   final SnackbarManager.Callback managerCallback = new SnackbarManager.Callback() {
      public void dismiss(int var1) {
         BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, var1, 0, BaseTransientBottomBar.this));
      }

      public void show() {
         BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, BaseTransientBottomBar.this));
      }
   };
   private final ViewGroup targetParent;
   protected final BaseTransientBottomBar.SnackbarBaseLayout view;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 19) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_OFFSET_API = var0;
      SNACKBAR_STYLE_ATTR = new int[]{R.attr.snackbarStyle};
      handler = new Handler(Looper.getMainLooper(), new Callback() {
         public boolean handleMessage(Message var1) {
            switch(var1.what) {
            case 0:
               ((BaseTransientBottomBar)var1.obj).showView();
               return true;
            case 1:
               ((BaseTransientBottomBar)var1.obj).hideView(var1.arg1);
               return true;
            default:
               return false;
            }
         }
      });
   }

   protected BaseTransientBottomBar(ViewGroup var1, View var2, ContentViewCallback var3) {
      if (var1 != null) {
         if (var2 != null) {
            if (var3 != null) {
               this.targetParent = var1;
               this.contentViewCallback = var3;
               this.context = var1.getContext();
               ThemeEnforcement.checkAppCompatTheme(this.context);
               this.view = (BaseTransientBottomBar.SnackbarBaseLayout)LayoutInflater.from(this.context).inflate(this.getSnackbarBaseLayoutResId(), this.targetParent, false);
               this.view.addView(var2);
               ViewCompat.setAccessibilityLiveRegion(this.view, 1);
               ViewCompat.setImportantForAccessibility(this.view, 1);
               ViewCompat.setFitsSystemWindows(this.view, true);
               ViewCompat.setOnApplyWindowInsetsListener(this.view, new OnApplyWindowInsetsListener() {
                  public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
                     var1.setPadding(var1.getPaddingLeft(), var1.getPaddingTop(), var1.getPaddingRight(), var2.getSystemWindowInsetBottom());
                     return var2;
                  }
               });
               ViewCompat.setAccessibilityDelegate(this.view, new AccessibilityDelegateCompat() {
                  public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
                     super.onInitializeAccessibilityNodeInfo(var1, var2);
                     var2.addAction(1048576);
                     var2.setDismissable(true);
                  }

                  public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
                     if (var2 == 1048576) {
                        BaseTransientBottomBar.this.dismiss();
                        return true;
                     } else {
                        return super.performAccessibilityAction(var1, var2, var3);
                     }
                  }
               });
               this.accessibilityManager = (AccessibilityManager)this.context.getSystemService("accessibility");
            } else {
               throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
            }
         } else {
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
         }
      } else {
         throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
      }
   }

   private void animateViewOut(final int var1) {
      ValueAnimator var2 = new ValueAnimator();
      var2.setIntValues(new int[]{0, this.getTranslationYBottom()});
      var2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      var2.setDuration(250L);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            BaseTransientBottomBar.this.onViewHidden(var1);
         }

         public void onAnimationStart(Animator var1x) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, 180);
         }
      });
      var2.addUpdateListener(new AnimatorUpdateListener() {
         private int previousAnimatedIntValue = 0;

         public void onAnimationUpdate(ValueAnimator var1) {
            int var2 = (Integer)var1.getAnimatedValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
               ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, var2 - this.previousAnimatedIntValue);
            } else {
               BaseTransientBottomBar.this.view.setTranslationY((float)var2);
            }

            this.previousAnimatedIntValue = var2;
         }
      });
      var2.start();
   }

   private int getTranslationYBottom() {
      int var1 = this.view.getHeight();
      LayoutParams var2 = this.view.getLayoutParams();
      int var3 = var1;
      if (var2 instanceof MarginLayoutParams) {
         var3 = var1 + ((MarginLayoutParams)var2).bottomMargin;
      }

      return var3;
   }

   public BaseTransientBottomBar addCallback(BaseTransientBottomBar.BaseCallback var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.callbacks == null) {
            this.callbacks = new ArrayList();
         }

         this.callbacks.add(var1);
         return this;
      }
   }

   void animateViewIn() {
      final int var1 = this.getTranslationYBottom();
      if (USE_OFFSET_API) {
         ViewCompat.offsetTopAndBottom(this.view, var1);
      } else {
         this.view.setTranslationY((float)var1);
      }

      ValueAnimator var2 = new ValueAnimator();
      var2.setIntValues(new int[]{var1, 0});
      var2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      var2.setDuration(250L);
      var2.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            BaseTransientBottomBar.this.onViewShown();
         }

         public void onAnimationStart(Animator var1) {
            BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, 180);
         }
      });
      var2.addUpdateListener(new AnimatorUpdateListener() {
         private int previousAnimatedIntValue = var1;

         public void onAnimationUpdate(ValueAnimator var1x) {
            int var2 = (Integer)var1x.getAnimatedValue();
            if (BaseTransientBottomBar.USE_OFFSET_API) {
               ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, var2 - this.previousAnimatedIntValue);
            } else {
               BaseTransientBottomBar.this.view.setTranslationY((float)var2);
            }

            this.previousAnimatedIntValue = var2;
         }
      });
      var2.start();
   }

   public void dismiss() {
      this.dispatchDismiss(3);
   }

   protected void dispatchDismiss(int var1) {
      SnackbarManager.getInstance().dismiss(this.managerCallback, var1);
   }

   public Context getContext() {
      return this.context;
   }

   public int getDuration() {
      return this.duration;
   }

   protected SwipeDismissBehavior getNewBehavior() {
      return new BaseTransientBottomBar.Behavior();
   }

   protected int getSnackbarBaseLayoutResId() {
      int var1;
      if (this.hasSnackbarStyleAttr()) {
         var1 = R.layout.mtrl_layout_snackbar;
      } else {
         var1 = R.layout.design_layout_snackbar;
      }

      return var1;
   }

   protected boolean hasSnackbarStyleAttr() {
      TypedArray var1 = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
      boolean var2 = false;
      int var3 = var1.getResourceId(0, -1);
      var1.recycle();
      if (var3 != -1) {
         var2 = true;
      }

      return var2;
   }

   final void hideView(int var1) {
      if (this.shouldAnimate() && this.view.getVisibility() == 0) {
         this.animateViewOut(var1);
      } else {
         this.onViewHidden(var1);
      }

   }

   public boolean isShownOrQueued() {
      return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
   }

   void onViewHidden(int var1) {
      SnackbarManager.getInstance().onDismissed(this.managerCallback);
      if (this.callbacks != null) {
         for(int var2 = this.callbacks.size() - 1; var2 >= 0; --var2) {
            ((BaseTransientBottomBar.BaseCallback)this.callbacks.get(var2)).onDismissed(this, var1);
         }
      }

      ViewParent var3 = this.view.getParent();
      if (var3 instanceof ViewGroup) {
         ((ViewGroup)var3).removeView(this.view);
      }

   }

   void onViewShown() {
      SnackbarManager.getInstance().onShown(this.managerCallback);
      if (this.callbacks != null) {
         for(int var1 = this.callbacks.size() - 1; var1 >= 0; --var1) {
            ((BaseTransientBottomBar.BaseCallback)this.callbacks.get(var1)).onShown(this);
         }
      }

   }

   public BaseTransientBottomBar setDuration(int var1) {
      this.duration = var1;
      return this;
   }

   boolean shouldAnimate() {
      AccessibilityManager var1 = this.accessibilityManager;
      boolean var2 = true;
      List var3 = var1.getEnabledAccessibilityServiceList(1);
      if (var3 == null || !var3.isEmpty()) {
         var2 = false;
      }

      return var2;
   }

   public void show() {
      SnackbarManager.getInstance().show(this.getDuration(), this.managerCallback);
   }

   final void showView() {
      if (this.view.getParent() == null) {
         LayoutParams var1 = this.view.getLayoutParams();
         if (var1 instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams var2 = (CoordinatorLayout.LayoutParams)var1;
            Object var3;
            if (this.behavior == null) {
               var3 = this.getNewBehavior();
            } else {
               var3 = this.behavior;
            }

            if (var3 instanceof BaseTransientBottomBar.Behavior) {
               ((BaseTransientBottomBar.Behavior)var3).setBaseTransientBottomBar(this);
            }

            ((SwipeDismissBehavior)var3).setListener(new SwipeDismissBehavior.OnDismissListener() {
               public void onDismiss(View var1) {
                  var1.setVisibility(8);
                  BaseTransientBottomBar.this.dispatchDismiss(0);
               }

               public void onDragStateChanged(int var1) {
                  switch(var1) {
                  case 0:
                     SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
                     break;
                  case 1:
                  case 2:
                     SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback);
                  }

               }
            });
            var2.setBehavior((CoordinatorLayout.Behavior)var3);
            var2.insetEdge = 80;
         }

         this.targetParent.addView(this.view);
      }

      this.view.setOnAttachStateChangeListener(new BaseTransientBottomBar.OnAttachStateChangeListener() {
         public void onViewAttachedToWindow(View var1) {
         }

         public void onViewDetachedFromWindow(View var1) {
            if (BaseTransientBottomBar.this.isShownOrQueued()) {
               BaseTransientBottomBar.handler.post(new Runnable() {
                  public void run() {
                     BaseTransientBottomBar.this.onViewHidden(3);
                  }
               });
            }

         }
      });
      if (ViewCompat.isLaidOut(this.view)) {
         if (this.shouldAnimate()) {
            this.animateViewIn();
         } else {
            this.onViewShown();
         }
      } else {
         this.view.setOnLayoutChangeListener(new BaseTransientBottomBar.OnLayoutChangeListener() {
            public void onLayoutChange(View var1, int var2, int var3, int var4, int var5) {
               BaseTransientBottomBar.this.view.setOnLayoutChangeListener((BaseTransientBottomBar.OnLayoutChangeListener)null);
               if (BaseTransientBottomBar.this.shouldAnimate()) {
                  BaseTransientBottomBar.this.animateViewIn();
               } else {
                  BaseTransientBottomBar.this.onViewShown();
               }

            }
         });
      }

   }

   public abstract static class BaseCallback {
      public void onDismissed(Object var1, int var2) {
      }

      public void onShown(Object var1) {
      }
   }

   public static class Behavior extends SwipeDismissBehavior {
      private final BaseTransientBottomBar.BehaviorDelegate delegate = new BaseTransientBottomBar.BehaviorDelegate(this);

      private void setBaseTransientBottomBar(BaseTransientBottomBar var1) {
         this.delegate.setBaseTransientBottomBar(var1);
      }

      public boolean canSwipeDismissView(View var1) {
         return this.delegate.canSwipeDismissView(var1);
      }

      public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         this.delegate.onInterceptTouchEvent(var1, var2, var3);
         return super.onInterceptTouchEvent(var1, var2, var3);
      }
   }

   public static class BehaviorDelegate {
      private SnackbarManager.Callback managerCallback;

      public BehaviorDelegate(SwipeDismissBehavior var1) {
         var1.setStartAlphaSwipeDistance(0.1F);
         var1.setEndAlphaSwipeDistance(0.6F);
         var1.setSwipeDirection(0);
      }

      public boolean canSwipeDismissView(View var1) {
         return var1 instanceof BaseTransientBottomBar.SnackbarBaseLayout;
      }

      public void onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
         int var4 = var3.getActionMasked();
         if (var4 != 3) {
            switch(var4) {
            case 0:
               if (var1.isPointInChildBounds(var2, (int)var3.getX(), (int)var3.getY())) {
                  SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
               }

               return;
            case 1:
               break;
            default:
               return;
            }
         }

         SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
      }

      public void setBaseTransientBottomBar(BaseTransientBottomBar var1) {
         this.managerCallback = var1.managerCallback;
      }
   }

   protected interface OnAttachStateChangeListener {
      void onViewAttachedToWindow(View var1);

      void onViewDetachedFromWindow(View var1);
   }

   protected interface OnLayoutChangeListener {
      void onLayoutChange(View var1, int var2, int var3, int var4, int var5);
   }

   protected static class SnackbarBaseLayout extends FrameLayout {
      private final AccessibilityManager accessibilityManager;
      private BaseTransientBottomBar.OnAttachStateChangeListener onAttachStateChangeListener;
      private BaseTransientBottomBar.OnLayoutChangeListener onLayoutChangeListener;
      private final AccessibilityManagerCompat.TouchExplorationStateChangeListener touchExplorationStateChangeListener;

      protected SnackbarBaseLayout(Context var1) {
         this(var1, (AttributeSet)null);
      }

      protected SnackbarBaseLayout(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.SnackbarLayout);
         if (var3.hasValue(R.styleable.SnackbarLayout_elevation)) {
            ViewCompat.setElevation(this, (float)var3.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
         }

         var3.recycle();
         this.accessibilityManager = (AccessibilityManager)var1.getSystemService("accessibility");
         this.touchExplorationStateChangeListener = new AccessibilityManagerCompat.TouchExplorationStateChangeListener() {
            public void onTouchExplorationStateChanged(boolean var1) {
               SnackbarBaseLayout.this.setClickableOrFocusableBasedOnAccessibility(var1);
            }
         };
         AccessibilityManagerCompat.addTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
         this.setClickableOrFocusableBasedOnAccessibility(this.accessibilityManager.isTouchExplorationEnabled());
      }

      private void setClickableOrFocusableBasedOnAccessibility(boolean var1) {
         this.setClickable(var1 ^ true);
         this.setFocusable(var1);
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         if (this.onAttachStateChangeListener != null) {
            this.onAttachStateChangeListener.onViewAttachedToWindow(this);
         }

         ViewCompat.requestApplyInsets(this);
      }

      protected void onDetachedFromWindow() {
         super.onDetachedFromWindow();
         if (this.onAttachStateChangeListener != null) {
            this.onAttachStateChangeListener.onViewDetachedFromWindow(this);
         }

         AccessibilityManagerCompat.removeTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         if (this.onLayoutChangeListener != null) {
            this.onLayoutChangeListener.onLayoutChange(this, var2, var3, var4, var5);
         }

      }

      void setOnAttachStateChangeListener(BaseTransientBottomBar.OnAttachStateChangeListener var1) {
         this.onAttachStateChangeListener = var1;
      }

      void setOnLayoutChangeListener(BaseTransientBottomBar.OnLayoutChangeListener var1) {
         this.onLayoutChangeListener = var1;
      }
   }
}
