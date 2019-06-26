package androidx.core.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.View.AccessibilityDelegate;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.core.R$id;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewCompat {
   private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
   private static boolean sAccessibilityDelegateCheckFailed = false;
   private static Field sAccessibilityDelegateField;
   private static ViewCompat.AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager;
   private static Field sMinHeightField;
   private static boolean sMinHeightFieldFetched;
   private static Field sMinWidthField;
   private static boolean sMinWidthFieldFetched;
   private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
   private static WeakHashMap sViewPropertyAnimatorMap = null;

   static {
      ACCESSIBILITY_ACTIONS_RESOURCE_IDS = new int[]{R$id.accessibility_custom_action_0, R$id.accessibility_custom_action_1, R$id.accessibility_custom_action_2, R$id.accessibility_custom_action_3, R$id.accessibility_custom_action_4, R$id.accessibility_custom_action_5, R$id.accessibility_custom_action_6, R$id.accessibility_custom_action_7, R$id.accessibility_custom_action_8, R$id.accessibility_custom_action_9, R$id.accessibility_custom_action_10, R$id.accessibility_custom_action_11, R$id.accessibility_custom_action_12, R$id.accessibility_custom_action_13, R$id.accessibility_custom_action_14, R$id.accessibility_custom_action_15, R$id.accessibility_custom_action_16, R$id.accessibility_custom_action_17, R$id.accessibility_custom_action_18, R$id.accessibility_custom_action_19, R$id.accessibility_custom_action_20, R$id.accessibility_custom_action_21, R$id.accessibility_custom_action_22, R$id.accessibility_custom_action_23, R$id.accessibility_custom_action_24, R$id.accessibility_custom_action_25, R$id.accessibility_custom_action_26, R$id.accessibility_custom_action_27, R$id.accessibility_custom_action_28, R$id.accessibility_custom_action_29, R$id.accessibility_custom_action_30, R$id.accessibility_custom_action_31};
      sAccessibilityPaneVisibilityManager = new ViewCompat.AccessibilityPaneVisibilityManager();
   }

   @SuppressLint({"BanTargetApiAnnotation"})
   @TargetApi(28)
   private static ViewCompat.AccessibilityViewProperty accessibilityHeadingProperty() {
      return new ViewCompat.AccessibilityViewProperty(R$id.tag_accessibility_heading, Boolean.class, 28) {
         Boolean frameworkGet(View var1) {
            return var1.isAccessibilityHeading();
         }
      };
   }

   public static WindowInsetsCompat dispatchApplyWindowInsets(View var0, WindowInsetsCompat var1) {
      if (VERSION.SDK_INT >= 21) {
         WindowInsets var4 = (WindowInsets)WindowInsetsCompat.unwrap(var1);
         WindowInsets var2 = var0.dispatchApplyWindowInsets(var4);
         WindowInsets var3 = var4;
         if (!var2.equals(var4)) {
            var3 = new WindowInsets(var2);
         }

         return WindowInsetsCompat.wrap(var3);
      } else {
         return var1;
      }
   }

   public static AccessibilityDelegateCompat getAccessibilityDelegate(View var0) {
      AccessibilityDelegate var1 = getAccessibilityDelegateInternal(var0);
      if (var1 == null) {
         return null;
      } else {
         return var1 instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter ? ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter)var1).mCompat : new AccessibilityDelegateCompat(var1);
      }
   }

   private static AccessibilityDelegate getAccessibilityDelegateInternal(View var0) {
      if (sAccessibilityDelegateCheckFailed) {
         return null;
      } else {
         if (sAccessibilityDelegateField == null) {
            try {
               sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
               sAccessibilityDelegateField.setAccessible(true);
            } catch (Throwable var2) {
               sAccessibilityDelegateCheckFailed = true;
               return null;
            }
         }

         try {
            Object var3 = sAccessibilityDelegateField.get(var0);
            if (var3 instanceof AccessibilityDelegate) {
               AccessibilityDelegate var4 = (AccessibilityDelegate)var3;
               return var4;
            } else {
               return null;
            }
         } catch (Throwable var1) {
            sAccessibilityDelegateCheckFailed = true;
            return null;
         }
      }
   }

   public static int getAccessibilityLiveRegion(View var0) {
      return VERSION.SDK_INT >= 19 ? var0.getAccessibilityLiveRegion() : 0;
   }

   public static CharSequence getAccessibilityPaneTitle(View var0) {
      return (CharSequence)paneTitleProperty().get(var0);
   }

   public static ColorStateList getBackgroundTintList(View var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getBackgroundTintList();
      } else {
         ColorStateList var1;
         if (var0 instanceof TintableBackgroundView) {
            var1 = ((TintableBackgroundView)var0).getSupportBackgroundTintList();
         } else {
            var1 = null;
         }

         return var1;
      }
   }

   public static Mode getBackgroundTintMode(View var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getBackgroundTintMode();
      } else {
         Mode var1;
         if (var0 instanceof TintableBackgroundView) {
            var1 = ((TintableBackgroundView)var0).getSupportBackgroundTintMode();
         } else {
            var1 = null;
         }

         return var1;
      }
   }

   public static Display getDisplay(View var0) {
      if (VERSION.SDK_INT >= 17) {
         return var0.getDisplay();
      } else {
         return isAttachedToWindow(var0) ? ((WindowManager)var0.getContext().getSystemService("window")).getDefaultDisplay() : null;
      }
   }

   public static float getElevation(View var0) {
      return VERSION.SDK_INT >= 21 ? var0.getElevation() : 0.0F;
   }

   public static int getImportantForAccessibility(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.getImportantForAccessibility() : 0;
   }

   @SuppressLint({"InlinedApi"})
   public static int getImportantForAutofill(View var0) {
      return VERSION.SDK_INT >= 26 ? var0.getImportantForAutofill() : 0;
   }

   public static int getLayoutDirection(View var0) {
      return VERSION.SDK_INT >= 17 ? var0.getLayoutDirection() : 0;
   }

   public static int getMinimumHeight(View var0) {
      if (VERSION.SDK_INT >= 16) {
         return var0.getMinimumHeight();
      } else {
         if (!sMinHeightFieldFetched) {
            try {
               sMinHeightField = View.class.getDeclaredField("mMinHeight");
               sMinHeightField.setAccessible(true);
            } catch (NoSuchFieldException var3) {
            }

            sMinHeightFieldFetched = true;
         }

         Field var1 = sMinHeightField;
         if (var1 != null) {
            try {
               int var2 = (Integer)var1.get(var0);
               return var2;
            } catch (Exception var4) {
            }
         }

         return 0;
      }
   }

   public static int getMinimumWidth(View var0) {
      if (VERSION.SDK_INT >= 16) {
         return var0.getMinimumWidth();
      } else {
         if (!sMinWidthFieldFetched) {
            try {
               sMinWidthField = View.class.getDeclaredField("mMinWidth");
               sMinWidthField.setAccessible(true);
            } catch (NoSuchFieldException var3) {
            }

            sMinWidthFieldFetched = true;
         }

         Field var1 = sMinWidthField;
         if (var1 != null) {
            try {
               int var2 = (Integer)var1.get(var0);
               return var2;
            } catch (Exception var4) {
            }
         }

         return 0;
      }
   }

   public static int getPaddingEnd(View var0) {
      return VERSION.SDK_INT >= 17 ? var0.getPaddingEnd() : var0.getPaddingRight();
   }

   public static int getPaddingStart(View var0) {
      return VERSION.SDK_INT >= 17 ? var0.getPaddingStart() : var0.getPaddingLeft();
   }

   public static int getWindowSystemUiVisibility(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.getWindowSystemUiVisibility() : 0;
   }

   public static boolean hasTransientState(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.hasTransientState() : false;
   }

   public static boolean isAccessibilityHeading(View var0) {
      Boolean var2 = (Boolean)accessibilityHeadingProperty().get(var0);
      boolean var1;
      if (var2 == null) {
         var1 = false;
      } else {
         var1 = var2;
      }

      return var1;
   }

   public static boolean isAttachedToWindow(View var0) {
      if (VERSION.SDK_INT >= 19) {
         return var0.isAttachedToWindow();
      } else {
         boolean var1;
         if (var0.getWindowToken() != null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isScreenReaderFocusable(View var0) {
      Boolean var2 = (Boolean)screenReaderFocusableProperty().get(var0);
      boolean var1;
      if (var2 == null) {
         var1 = false;
      } else {
         var1 = var2;
      }

      return var1;
   }

   @SuppressLint({"BanTargetApiAnnotation"})
   @TargetApi(19)
   static void notifyViewAccessibilityStateChangedIfNeeded(View var0, int var1) {
      if (((AccessibilityManager)var0.getContext().getSystemService("accessibility")).isEnabled()) {
         boolean var2;
         if (getAccessibilityPaneTitle(var0) != null) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (getAccessibilityLiveRegion(var0) == 0 && (!var2 || var0.getVisibility() != 0)) {
            if (var0.getParent() != null) {
               try {
                  var0.getParent().notifySubtreeAccessibilityStateChanged(var0, var0, var1);
               } catch (AbstractMethodError var5) {
                  StringBuilder var4 = new StringBuilder();
                  var4.append(var0.getParent().getClass().getSimpleName());
                  var4.append(" does not fully implement ViewParent");
                  Log.e("ViewCompat", var4.toString(), var5);
               }
            }
         } else {
            AccessibilityEvent var3 = AccessibilityEvent.obtain();
            short var6;
            if (var2) {
               var6 = 32;
            } else {
               var6 = 2048;
            }

            var3.setEventType(var6);
            var3.setContentChangeTypes(var1);
            var0.sendAccessibilityEventUnchecked(var3);
         }

      }
   }

   public static WindowInsetsCompat onApplyWindowInsets(View var0, WindowInsetsCompat var1) {
      if (VERSION.SDK_INT >= 21) {
         WindowInsets var4 = (WindowInsets)WindowInsetsCompat.unwrap(var1);
         WindowInsets var2 = var0.onApplyWindowInsets(var4);
         WindowInsets var3 = var4;
         if (!var2.equals(var4)) {
            var3 = new WindowInsets(var2);
         }

         return WindowInsetsCompat.wrap(var3);
      } else {
         return var1;
      }
   }

   public static void onInitializeAccessibilityNodeInfo(View var0, AccessibilityNodeInfoCompat var1) {
      var0.onInitializeAccessibilityNodeInfo(var1.unwrap());
   }

   @SuppressLint({"BanTargetApiAnnotation"})
   @TargetApi(28)
   private static ViewCompat.AccessibilityViewProperty paneTitleProperty() {
      return new ViewCompat.AccessibilityViewProperty(R$id.tag_accessibility_pane_title, CharSequence.class, 8, 28) {
         CharSequence frameworkGet(View var1) {
            return var1.getAccessibilityPaneTitle();
         }
      };
   }

   public static boolean performAccessibilityAction(View var0, int var1, Bundle var2) {
      return VERSION.SDK_INT >= 16 ? var0.performAccessibilityAction(var1, var2) : false;
   }

   public static void postInvalidateOnAnimation(View var0) {
      if (VERSION.SDK_INT >= 16) {
         var0.postInvalidateOnAnimation();
      } else {
         var0.postInvalidate();
      }

   }

   public static void postOnAnimation(View var0, Runnable var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.postOnAnimation(var1);
      } else {
         var0.postDelayed(var1, ValueAnimator.getFrameDelay());
      }

   }

   public static void postOnAnimationDelayed(View var0, Runnable var1, long var2) {
      if (VERSION.SDK_INT >= 16) {
         var0.postOnAnimationDelayed(var1, var2);
      } else {
         var0.postDelayed(var1, ValueAnimator.getFrameDelay() + var2);
      }

   }

   public static void requestApplyInsets(View var0) {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 20) {
         var0.requestApplyInsets();
      } else if (var1 >= 16) {
         var0.requestFitSystemWindows();
      }

   }

   @SuppressLint({"BanTargetApiAnnotation"})
   @TargetApi(28)
   private static ViewCompat.AccessibilityViewProperty screenReaderFocusableProperty() {
      return new ViewCompat.AccessibilityViewProperty(R$id.tag_screen_reader_focusable, Boolean.class, 28) {
         Boolean frameworkGet(View var1) {
            return var1.isScreenReaderFocusable();
         }
      };
   }

   public static void setAccessibilityDelegate(View var0, AccessibilityDelegateCompat var1) {
      AccessibilityDelegateCompat var2 = var1;
      if (var1 == null) {
         var2 = var1;
         if (getAccessibilityDelegateInternal(var0) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter) {
            var2 = new AccessibilityDelegateCompat();
         }
      }

      AccessibilityDelegate var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = var2.getBridge();
      }

      var0.setAccessibilityDelegate(var3);
   }

   public static void setBackground(View var0, Drawable var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.setBackground(var1);
      } else {
         var0.setBackgroundDrawable(var1);
      }

   }

   public static void setBackgroundTintList(View var0, ColorStateList var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setBackgroundTintList(var1);
         if (VERSION.SDK_INT == 21) {
            Drawable var3 = var0.getBackground();
            boolean var2;
            if (var0.getBackgroundTintList() == null && var0.getBackgroundTintMode() == null) {
               var2 = false;
            } else {
               var2 = true;
            }

            if (var3 != null && var2) {
               if (var3.isStateful()) {
                  var3.setState(var0.getDrawableState());
               }

               var0.setBackground(var3);
            }
         }
      } else if (var0 instanceof TintableBackgroundView) {
         ((TintableBackgroundView)var0).setSupportBackgroundTintList(var1);
      }

   }

   public static void setBackgroundTintMode(View var0, Mode var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setBackgroundTintMode(var1);
         if (VERSION.SDK_INT == 21) {
            Drawable var3 = var0.getBackground();
            boolean var2;
            if (var0.getBackgroundTintList() == null && var0.getBackgroundTintMode() == null) {
               var2 = false;
            } else {
               var2 = true;
            }

            if (var3 != null && var2) {
               if (var3.isStateful()) {
                  var3.setState(var0.getDrawableState());
               }

               var0.setBackground(var3);
            }
         }
      } else if (var0 instanceof TintableBackgroundView) {
         ((TintableBackgroundView)var0).setSupportBackgroundTintMode(var1);
      }

   }

   public static void setElevation(View var0, float var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setElevation(var1);
      }

   }

   public static void setImportantForAccessibility(View var0, int var1) {
      int var2 = VERSION.SDK_INT;
      if (var2 >= 19) {
         var0.setImportantForAccessibility(var1);
      } else if (var2 >= 16) {
         var2 = var1;
         if (var1 == 4) {
            var2 = 2;
         }

         var0.setImportantForAccessibility(var2);
      }

   }

   public static void setImportantForAutofill(View var0, int var1) {
      if (VERSION.SDK_INT >= 26) {
         var0.setImportantForAutofill(var1);
      }

   }

   public static void setOnApplyWindowInsetsListener(View var0, final OnApplyWindowInsetsListener var1) {
      if (VERSION.SDK_INT >= 21) {
         if (var1 == null) {
            var0.setOnApplyWindowInsetsListener((android.view.View.OnApplyWindowInsetsListener)null);
            return;
         }

         var0.setOnApplyWindowInsetsListener(new android.view.View.OnApplyWindowInsetsListener() {
            public WindowInsets onApplyWindowInsets(View var1x, WindowInsets var2) {
               WindowInsetsCompat var3 = WindowInsetsCompat.wrap(var2);
               return (WindowInsets)WindowInsetsCompat.unwrap(var1.onApplyWindowInsets(var1x, var3));
            }
         });
      }

   }

   public static void stopNestedScroll(View var0) {
      if (VERSION.SDK_INT >= 21) {
         var0.stopNestedScroll();
      } else if (var0 instanceof NestedScrollingChild) {
         ((NestedScrollingChild)var0).stopNestedScroll();
      }

   }

   @SuppressLint({"BanTargetApiAnnotation"})
   @TargetApi(19)
   static class AccessibilityPaneVisibilityManager implements OnGlobalLayoutListener, OnAttachStateChangeListener {
      private WeakHashMap mPanesToVisible = new WeakHashMap();

      private void checkPaneVisibility(View var1, boolean var2) {
         boolean var3;
         if (var1.getVisibility() == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (var2 != var3) {
            if (var3) {
               ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(var1, 16);
            }

            this.mPanesToVisible.put(var1, var3);
         }

      }

      private void registerForLayoutCallback(View var1) {
         var1.getViewTreeObserver().addOnGlobalLayoutListener(this);
      }

      public void onGlobalLayout() {
         Iterator var1 = this.mPanesToVisible.entrySet().iterator();

         while(var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            this.checkPaneVisibility((View)var2.getKey(), (Boolean)var2.getValue());
         }

      }

      public void onViewAttachedToWindow(View var1) {
         this.registerForLayoutCallback(var1);
      }

      public void onViewDetachedFromWindow(View var1) {
      }
   }

   abstract static class AccessibilityViewProperty {
      private final int mContentChangeType;
      private final int mFrameworkMinimumSdk;
      private final int mTagKey;
      private final Class mType;

      AccessibilityViewProperty(int var1, Class var2, int var3) {
         this(var1, var2, 0, var3);
      }

      AccessibilityViewProperty(int var1, Class var2, int var3, int var4) {
         this.mTagKey = var1;
         this.mType = var2;
         this.mContentChangeType = var3;
         this.mFrameworkMinimumSdk = var4;
      }

      private boolean extrasAvailable() {
         boolean var1;
         if (VERSION.SDK_INT >= 19) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      private boolean frameworkAvailable() {
         boolean var1;
         if (VERSION.SDK_INT >= this.mFrameworkMinimumSdk) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      abstract Object frameworkGet(View var1);

      Object get(View var1) {
         if (this.frameworkAvailable()) {
            return this.frameworkGet(var1);
         } else {
            if (this.extrasAvailable()) {
               Object var2 = var1.getTag(this.mTagKey);
               if (this.mType.isInstance(var2)) {
                  return var2;
               }
            }

            return null;
         }
      }
   }
}
