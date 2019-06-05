package android.support.v4.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.compat.R;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.View.AccessibilityDelegate;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewCompat {
   private static boolean sAccessibilityDelegateCheckFailed = false;
   private static Field sAccessibilityDelegateField;
   private static Field sMinHeightField;
   private static boolean sMinHeightFieldFetched;
   private static Field sMinWidthField;
   private static boolean sMinWidthFieldFetched;
   private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
   private static ThreadLocal sThreadLocalRect;
   private static WeakHashMap sTransitionNameMap;
   private static WeakHashMap sViewPropertyAnimatorMap = null;

   public static ViewPropertyAnimatorCompat animate(View var0) {
      if (sViewPropertyAnimatorMap == null) {
         sViewPropertyAnimatorMap = new WeakHashMap();
      }

      ViewPropertyAnimatorCompat var1 = (ViewPropertyAnimatorCompat)sViewPropertyAnimatorMap.get(var0);
      ViewPropertyAnimatorCompat var2 = var1;
      if (var1 == null) {
         var2 = new ViewPropertyAnimatorCompat(var0);
         sViewPropertyAnimatorMap.put(var0, var2);
      }

      return var2;
   }

   private static void compatOffsetLeftAndRight(View var0, int var1) {
      var0.offsetLeftAndRight(var1);
      if (var0.getVisibility() == 0) {
         tickleInvalidationFlag(var0);
         ViewParent var2 = var0.getParent();
         if (var2 instanceof View) {
            tickleInvalidationFlag((View)var2);
         }
      }

   }

   private static void compatOffsetTopAndBottom(View var0, int var1) {
      var0.offsetTopAndBottom(var1);
      if (var0.getVisibility() == 0) {
         tickleInvalidationFlag(var0);
         ViewParent var2 = var0.getParent();
         if (var2 instanceof View) {
            tickleInvalidationFlag((View)var2);
         }
      }

   }

   public static WindowInsetsCompat dispatchApplyWindowInsets(View var0, WindowInsetsCompat var1) {
      if (VERSION.SDK_INT >= 21) {
         WindowInsets var4 = (WindowInsets)WindowInsetsCompat.unwrap(var1);
         WindowInsets var2 = var0.dispatchApplyWindowInsets(var4);
         WindowInsets var3 = var4;
         if (var2 != var4) {
            var3 = new WindowInsets(var2);
         }

         return WindowInsetsCompat.wrap(var3);
      } else {
         return var1;
      }
   }

   static boolean dispatchUnhandledKeyEventBeforeCallback(View var0, KeyEvent var1) {
      return VERSION.SDK_INT >= 28 ? false : ViewCompat.UnhandledKeyEventManager.at(var0).dispatch(var0, var1);
   }

   static boolean dispatchUnhandledKeyEventBeforeHierarchy(View var0, KeyEvent var1) {
      return VERSION.SDK_INT >= 28 ? false : ViewCompat.UnhandledKeyEventManager.at(var0).preDispatch(var1);
   }

   public static int getAccessibilityLiveRegion(View var0) {
      return VERSION.SDK_INT >= 19 ? var0.getAccessibilityLiveRegion() : 0;
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

   public static Rect getClipBounds(View var0) {
      return VERSION.SDK_INT >= 18 ? var0.getClipBounds() : null;
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

   private static Rect getEmptyTempRect() {
      if (sThreadLocalRect == null) {
         sThreadLocalRect = new ThreadLocal();
      }

      Rect var0 = (Rect)sThreadLocalRect.get();
      Rect var1 = var0;
      if (var0 == null) {
         var1 = new Rect();
         sThreadLocalRect.set(var1);
      }

      var1.setEmpty();
      return var1;
   }

   public static boolean getFitsSystemWindows(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.getFitsSystemWindows() : false;
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

         if (sMinHeightField != null) {
            try {
               int var1 = (Integer)sMinHeightField.get(var0);
               return var1;
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

         if (sMinWidthField != null) {
            try {
               int var1 = (Integer)sMinWidthField.get(var0);
               return var1;
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

   public static ViewParent getParentForAccessibility(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.getParentForAccessibility() : var0.getParent();
   }

   public static String getTransitionName(View var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getTransitionName();
      } else {
         return sTransitionNameMap == null ? null : (String)sTransitionNameMap.get(var0);
      }
   }

   public static int getWindowSystemUiVisibility(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.getWindowSystemUiVisibility() : 0;
   }

   public static float getZ(View var0) {
      return VERSION.SDK_INT >= 21 ? var0.getZ() : 0.0F;
   }

   public static boolean hasAccessibilityDelegate(View var0) {
      boolean var1 = sAccessibilityDelegateCheckFailed;
      boolean var2 = false;
      if (var1) {
         return false;
      } else {
         if (sAccessibilityDelegateField == null) {
            try {
               sAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
               sAccessibilityDelegateField.setAccessible(true);
            } catch (Throwable var4) {
               sAccessibilityDelegateCheckFailed = true;
               return false;
            }
         }

         Object var5;
         try {
            var5 = sAccessibilityDelegateField.get(var0);
         } catch (Throwable var3) {
            sAccessibilityDelegateCheckFailed = true;
            return false;
         }

         if (var5 != null) {
            var2 = true;
         }

         return var2;
      }
   }

   public static boolean hasOnClickListeners(View var0) {
      return VERSION.SDK_INT >= 15 ? var0.hasOnClickListeners() : false;
   }

   public static boolean hasOverlappingRendering(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.hasOverlappingRendering() : true;
   }

   public static boolean hasTransientState(View var0) {
      return VERSION.SDK_INT >= 16 ? var0.hasTransientState() : false;
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

   public static boolean isLaidOut(View var0) {
      if (VERSION.SDK_INT >= 19) {
         return var0.isLaidOut();
      } else {
         boolean var1;
         if (var0.getWidth() > 0 && var0.getHeight() > 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }

   public static boolean isNestedScrollingEnabled(View var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.isNestedScrollingEnabled();
      } else {
         return var0 instanceof NestedScrollingChild ? ((NestedScrollingChild)var0).isNestedScrollingEnabled() : false;
      }
   }

   public static boolean isPaddingRelative(View var0) {
      return VERSION.SDK_INT >= 17 ? var0.isPaddingRelative() : false;
   }

   public static void offsetLeftAndRight(View var0, int var1) {
      if (VERSION.SDK_INT >= 23) {
         var0.offsetLeftAndRight(var1);
      } else if (VERSION.SDK_INT >= 21) {
         Rect var2 = getEmptyTempRect();
         boolean var3 = false;
         ViewParent var4 = var0.getParent();
         if (var4 instanceof View) {
            View var5 = (View)var4;
            var2.set(var5.getLeft(), var5.getTop(), var5.getRight(), var5.getBottom());
            var3 = var2.intersects(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom()) ^ true;
         }

         compatOffsetLeftAndRight(var0, var1);
         if (var3 && var2.intersect(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom())) {
            ((View)var4).invalidate(var2);
         }
      } else {
         compatOffsetLeftAndRight(var0, var1);
      }

   }

   public static void offsetTopAndBottom(View var0, int var1) {
      if (VERSION.SDK_INT >= 23) {
         var0.offsetTopAndBottom(var1);
      } else if (VERSION.SDK_INT >= 21) {
         Rect var2 = getEmptyTempRect();
         boolean var3 = false;
         ViewParent var4 = var0.getParent();
         if (var4 instanceof View) {
            View var5 = (View)var4;
            var2.set(var5.getLeft(), var5.getTop(), var5.getRight(), var5.getBottom());
            var3 = var2.intersects(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom()) ^ true;
         }

         compatOffsetTopAndBottom(var0, var1);
         if (var3 && var2.intersect(var0.getLeft(), var0.getTop(), var0.getRight(), var0.getBottom())) {
            ((View)var4).invalidate(var2);
         }
      } else {
         compatOffsetTopAndBottom(var0, var1);
      }

   }

   public static WindowInsetsCompat onApplyWindowInsets(View var0, WindowInsetsCompat var1) {
      if (VERSION.SDK_INT >= 21) {
         WindowInsets var4 = (WindowInsets)WindowInsetsCompat.unwrap(var1);
         WindowInsets var2 = var0.onApplyWindowInsets(var4);
         WindowInsets var3 = var4;
         if (var2 != var4) {
            var3 = new WindowInsets(var2);
         }

         return WindowInsetsCompat.wrap(var3);
      } else {
         return var1;
      }
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
      if (VERSION.SDK_INT >= 20) {
         var0.requestApplyInsets();
      } else if (VERSION.SDK_INT >= 16) {
         var0.requestFitSystemWindows();
      }

   }

   public static void setAccessibilityDelegate(View var0, AccessibilityDelegateCompat var1) {
      AccessibilityDelegate var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = var1.getBridge();
      }

      var0.setAccessibilityDelegate(var2);
   }

   public static void setAccessibilityLiveRegion(View var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.setAccessibilityLiveRegion(var1);
      }

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

   public static void setClipBounds(View var0, Rect var1) {
      if (VERSION.SDK_INT >= 18) {
         var0.setClipBounds(var1);
      }

   }

   public static void setElevation(View var0, float var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setElevation(var1);
      }

   }

   @Deprecated
   public static void setFitsSystemWindows(View var0, boolean var1) {
      var0.setFitsSystemWindows(var1);
   }

   public static void setHasTransientState(View var0, boolean var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.setHasTransientState(var1);
      }

   }

   public static void setImportantForAccessibility(View var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.setImportantForAccessibility(var1);
      } else if (VERSION.SDK_INT >= 16) {
         int var2 = var1;
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

   public static void setLayoutDirection(View var0, int var1) {
      if (VERSION.SDK_INT >= 17) {
         var0.setLayoutDirection(var1);
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

   public static void setPaddingRelative(View var0, int var1, int var2, int var3, int var4) {
      if (VERSION.SDK_INT >= 17) {
         var0.setPaddingRelative(var1, var2, var3, var4);
      } else {
         var0.setPadding(var1, var2, var3, var4);
      }

   }

   public static void setPointerIcon(View var0, PointerIconCompat var1) {
      if (VERSION.SDK_INT >= 24) {
         Object var2;
         if (var1 != null) {
            var2 = var1.getPointerIcon();
         } else {
            var2 = null;
         }

         var0.setPointerIcon((PointerIcon)var2);
      }

   }

   public static void setScrollIndicators(View var0, int var1, int var2) {
      if (VERSION.SDK_INT >= 23) {
         var0.setScrollIndicators(var1, var2);
      }

   }

   public static void setTransitionName(View var0, String var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setTransitionName(var1);
      } else {
         if (sTransitionNameMap == null) {
            sTransitionNameMap = new WeakHashMap();
         }

         sTransitionNameMap.put(var0, var1);
      }

   }

   public static void stopNestedScroll(View var0) {
      if (VERSION.SDK_INT >= 21) {
         var0.stopNestedScroll();
      } else if (var0 instanceof NestedScrollingChild) {
         ((NestedScrollingChild)var0).stopNestedScroll();
      }

   }

   public static void stopNestedScroll(View var0, int var1) {
      if (var0 instanceof NestedScrollingChild2) {
         ((NestedScrollingChild2)var0).stopNestedScroll(var1);
      } else if (var1 == 0) {
         stopNestedScroll(var0);
      }

   }

   private static void tickleInvalidationFlag(View var0) {
      float var1 = var0.getTranslationY();
      var0.setTranslationY(1.0F + var1);
      var0.setTranslationY(var1);
   }

   public interface OnUnhandledKeyEventListenerCompat {
      boolean onUnhandledKeyEvent(View var1, KeyEvent var2);
   }

   static class UnhandledKeyEventManager {
      private static final ArrayList sViewsWithListeners = new ArrayList();
      private SparseArray mCapturedKeys = null;
      private WeakReference mLastDispatchedPreViewKeyEvent = null;
      private WeakHashMap mViewsContainingListeners = null;

      static ViewCompat.UnhandledKeyEventManager at(View var0) {
         ViewCompat.UnhandledKeyEventManager var1 = (ViewCompat.UnhandledKeyEventManager)var0.getTag(R.id.tag_unhandled_key_event_manager);
         ViewCompat.UnhandledKeyEventManager var2 = var1;
         if (var1 == null) {
            var2 = new ViewCompat.UnhandledKeyEventManager();
            var0.setTag(R.id.tag_unhandled_key_event_manager, var2);
         }

         return var2;
      }

      private View dispatchInOrder(View var1, KeyEvent var2) {
         if (this.mViewsContainingListeners != null && this.mViewsContainingListeners.containsKey(var1)) {
            if (var1 instanceof ViewGroup) {
               ViewGroup var3 = (ViewGroup)var1;

               for(int var4 = var3.getChildCount() - 1; var4 >= 0; --var4) {
                  View var5 = this.dispatchInOrder(var3.getChildAt(var4), var2);
                  if (var5 != null) {
                     return var5;
                  }
               }
            }

            return this.onUnhandledKeyEvent(var1, var2) ? var1 : null;
         } else {
            return null;
         }
      }

      private SparseArray getCapturedKeys() {
         if (this.mCapturedKeys == null) {
            this.mCapturedKeys = new SparseArray();
         }

         return this.mCapturedKeys;
      }

      private boolean onUnhandledKeyEvent(View var1, KeyEvent var2) {
         ArrayList var3 = (ArrayList)var1.getTag(R.id.tag_unhandled_key_listeners);
         if (var3 != null) {
            for(int var4 = var3.size() - 1; var4 >= 0; --var4) {
               if (((ViewCompat.OnUnhandledKeyEventListenerCompat)var3.get(var4)).onUnhandledKeyEvent(var1, var2)) {
                  return true;
               }
            }
         }

         return false;
      }

      private void recalcViewsWithUnhandled() {
         if (this.mViewsContainingListeners != null) {
            this.mViewsContainingListeners.clear();
         }

         if (!sViewsWithListeners.isEmpty()) {
            ArrayList var1 = sViewsWithListeners;
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label694: {
               try {
                  if (this.mViewsContainingListeners == null) {
                     WeakHashMap var2 = new WeakHashMap();
                     this.mViewsContainingListeners = var2;
                  }
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label694;
               }

               int var3;
               try {
                  var3 = sViewsWithListeners.size() - 1;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label694;
               }

               for(; var3 >= 0; --var3) {
                  View var76;
                  try {
                     var76 = (View)((WeakReference)sViewsWithListeners.get(var3)).get();
                  } catch (Throwable var72) {
                     var10000 = var72;
                     var10001 = false;
                     break label694;
                  }

                  if (var76 == null) {
                     try {
                        sViewsWithListeners.remove(var3);
                     } catch (Throwable var71) {
                        var10000 = var71;
                        var10001 = false;
                        break label694;
                     }
                  } else {
                     ViewParent var77;
                     try {
                        this.mViewsContainingListeners.put(var76, Boolean.TRUE);
                        var77 = var76.getParent();
                     } catch (Throwable var70) {
                        var10000 = var70;
                        var10001 = false;
                        break label694;
                     }

                     while(true) {
                        try {
                           if (!(var77 instanceof View)) {
                              break;
                           }

                           this.mViewsContainingListeners.put((View)var77, Boolean.TRUE);
                           var77 = var77.getParent();
                        } catch (Throwable var74) {
                           var10000 = var74;
                           var10001 = false;
                           break label694;
                        }
                     }
                  }
               }

               label663:
               try {
                  return;
               } catch (Throwable var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label663;
               }
            }

            while(true) {
               Throwable var78 = var10000;

               try {
                  throw var78;
               } catch (Throwable var68) {
                  var10000 = var68;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      boolean dispatch(View var1, KeyEvent var2) {
         if (var2.getAction() == 0) {
            this.recalcViewsWithUnhandled();
         }

         var1 = this.dispatchInOrder(var1, var2);
         if (var2.getAction() == 0) {
            int var3 = var2.getKeyCode();
            if (var1 != null && !KeyEvent.isModifierKey(var3)) {
               this.getCapturedKeys().put(var3, new WeakReference(var1));
            }
         }

         boolean var4;
         if (var1 != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      }

      boolean preDispatch(KeyEvent var1) {
         if (this.mLastDispatchedPreViewKeyEvent != null && this.mLastDispatchedPreViewKeyEvent.get() == var1) {
            return false;
         } else {
            this.mLastDispatchedPreViewKeyEvent = new WeakReference(var1);
            WeakReference var2 = null;
            SparseArray var3 = this.getCapturedKeys();
            WeakReference var4 = var2;
            if (var1.getAction() == 1) {
               int var5 = var3.indexOfKey(var1.getKeyCode());
               var4 = var2;
               if (var5 >= 0) {
                  var4 = (WeakReference)var3.valueAt(var5);
                  var3.removeAt(var5);
               }
            }

            var2 = var4;
            if (var4 == null) {
               var2 = (WeakReference)var3.get(var1.getKeyCode());
            }

            if (var2 != null) {
               View var6 = (View)var2.get();
               if (var6 != null && ViewCompat.isAttachedToWindow(var6)) {
                  this.onUnhandledKeyEvent(var6, var1);
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }
}
