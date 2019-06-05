package android.support.design.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.design.R;
import android.support.design.animation.AnimationUtils;
import android.support.design.internal.ThemeEnforcement;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(AppBarLayout.Behavior.class)
public class AppBarLayout extends LinearLayout {
   private int downPreScrollRange;
   private int downScrollRange;
   private boolean haveChildWithInterpolator;
   private WindowInsetsCompat lastInsets;
   private boolean liftOnScroll;
   private boolean liftable;
   private boolean liftableOverride;
   private boolean lifted;
   private List listeners;
   private int pendingAction;
   private int[] tmpStatesArray;
   private int totalScrollRange;

   public AppBarLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppBarLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.totalScrollRange = -1;
      this.downPreScrollRange = -1;
      this.downScrollRange = -1;
      this.pendingAction = 0;
      this.setOrientation(1);
      if (VERSION.SDK_INT >= 21) {
         ViewUtilsLollipop.setBoundsViewOutlineProvider(this);
         ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, var2, 0, R.style.Widget_Design_AppBarLayout);
      }

      TypedArray var3 = ThemeEnforcement.obtainStyledAttributes(var1, var2, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout);
      ViewCompat.setBackground(this, var3.getDrawable(R.styleable.AppBarLayout_android_background));
      if (var3.hasValue(R.styleable.AppBarLayout_expanded)) {
         this.setExpanded(var3.getBoolean(R.styleable.AppBarLayout_expanded, false), false, false);
      }

      if (VERSION.SDK_INT >= 21 && var3.hasValue(R.styleable.AppBarLayout_elevation)) {
         ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, (float)var3.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0));
      }

      if (VERSION.SDK_INT >= 26) {
         if (var3.hasValue(R.styleable.AppBarLayout_android_keyboardNavigationCluster)) {
            this.setKeyboardNavigationCluster(var3.getBoolean(R.styleable.AppBarLayout_android_keyboardNavigationCluster, false));
         }

         if (var3.hasValue(R.styleable.AppBarLayout_android_touchscreenBlocksFocus)) {
            this.setTouchscreenBlocksFocus(var3.getBoolean(R.styleable.AppBarLayout_android_touchscreenBlocksFocus, false));
         }
      }

      this.liftOnScroll = var3.getBoolean(R.styleable.AppBarLayout_liftOnScroll, false);
      var3.recycle();
      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            return AppBarLayout.this.onWindowInsetChanged(var2);
         }
      });
   }

   private boolean hasCollapsibleChild() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         if (((AppBarLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).isCollapsible()) {
            return true;
         }
      }

      return false;
   }

   private void invalidateScrollRanges() {
      this.totalScrollRange = -1;
      this.downPreScrollRange = -1;
      this.downScrollRange = -1;
   }

   private void setExpanded(boolean var1, boolean var2, boolean var3) {
      byte var4;
      if (var1) {
         var4 = 1;
      } else {
         var4 = 2;
      }

      byte var5 = 0;
      byte var6;
      if (var2) {
         var6 = 4;
      } else {
         var6 = 0;
      }

      if (var3) {
         var5 = 8;
      }

      this.pendingAction = var4 | var6 | var5;
      this.requestLayout();
   }

   private boolean setLiftableState(boolean var1) {
      if (this.liftable != var1) {
         this.liftable = var1;
         this.refreshDrawableState();
         return true;
      } else {
         return false;
      }
   }

   public void addOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener var1) {
      if (this.listeners == null) {
         this.listeners = new ArrayList();
      }

      if (var1 != null && !this.listeners.contains(var1)) {
         this.listeners.add(var1);
      }

   }

   public void addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener var1) {
      this.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener)var1);
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof AppBarLayout.LayoutParams;
   }

   void dispatchOffsetUpdates(int var1) {
      if (this.listeners != null) {
         int var2 = 0;

         for(int var3 = this.listeners.size(); var2 < var3; ++var2) {
            AppBarLayout.BaseOnOffsetChangedListener var4 = (AppBarLayout.BaseOnOffsetChangedListener)this.listeners.get(var2);
            if (var4 != null) {
               var4.onOffsetChanged(this, var1);
            }
         }
      }

   }

   protected AppBarLayout.LayoutParams generateDefaultLayoutParams() {
      return new AppBarLayout.LayoutParams(-1, -2);
   }

   public AppBarLayout.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new AppBarLayout.LayoutParams(this.getContext(), var1);
   }

   protected AppBarLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      if (VERSION.SDK_INT >= 19 && var1 instanceof android.widget.LinearLayout.LayoutParams) {
         return new AppBarLayout.LayoutParams((android.widget.LinearLayout.LayoutParams)var1);
      } else {
         return var1 instanceof MarginLayoutParams ? new AppBarLayout.LayoutParams((MarginLayoutParams)var1) : new AppBarLayout.LayoutParams(var1);
      }
   }

   int getDownNestedPreScrollRange() {
      if (this.downPreScrollRange != -1) {
         return this.downPreScrollRange;
      } else {
         int var1 = this.getChildCount() - 1;

         int var2;
         int var5;
         for(var2 = 0; var1 >= 0; var2 = var5) {
            View var3 = this.getChildAt(var1);
            AppBarLayout.LayoutParams var4 = (AppBarLayout.LayoutParams)var3.getLayoutParams();
            var5 = var3.getMeasuredHeight();
            int var6 = var4.scrollFlags;
            if ((var6 & 5) == 5) {
               var2 += var4.topMargin + var4.bottomMargin;
               if ((var6 & 8) != 0) {
                  var5 = var2 + ViewCompat.getMinimumHeight(var3);
               } else if ((var6 & 2) != 0) {
                  var5 = var2 + (var5 - ViewCompat.getMinimumHeight(var3));
               } else {
                  var5 = var2 + (var5 - this.getTopInset());
               }
            } else {
               var5 = var2;
               if (var2 > 0) {
                  break;
               }
            }

            --var1;
         }

         var5 = Math.max(0, var2);
         this.downPreScrollRange = var5;
         return var5;
      }
   }

   int getDownNestedScrollRange() {
      if (this.downScrollRange != -1) {
         return this.downScrollRange;
      } else {
         int var1 = this.getChildCount();
         int var2 = 0;
         int var3 = 0;

         int var4;
         while(true) {
            var4 = var3;
            if (var2 >= var1) {
               break;
            }

            View var5 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var6 = (AppBarLayout.LayoutParams)var5.getLayoutParams();
            int var7 = var5.getMeasuredHeight();
            int var8 = var6.topMargin;
            int var9 = var6.bottomMargin;
            int var10 = var6.scrollFlags;
            var4 = var3;
            if ((var10 & 1) == 0) {
               break;
            }

            var3 += var7 + var8 + var9;
            if ((var10 & 2) != 0) {
               var4 = var3 - (ViewCompat.getMinimumHeight(var5) + this.getTopInset());
               break;
            }

            ++var2;
         }

         var3 = Math.max(0, var4);
         this.downScrollRange = var3;
         return var3;
      }
   }

   public final int getMinimumHeightForVisibleOverlappingContent() {
      int var1 = this.getTopInset();
      int var2 = ViewCompat.getMinimumHeight(this);
      if (var2 != 0) {
         return var2 * 2 + var1;
      } else {
         var2 = this.getChildCount();
         if (var2 >= 1) {
            var2 = ViewCompat.getMinimumHeight(this.getChildAt(var2 - 1));
         } else {
            var2 = 0;
         }

         return var2 != 0 ? var2 * 2 + var1 : this.getHeight() / 3;
      }
   }

   int getPendingAction() {
      return this.pendingAction;
   }

   @Deprecated
   public float getTargetElevation() {
      return 0.0F;
   }

   final int getTopInset() {
      int var1;
      if (this.lastInsets != null) {
         var1 = this.lastInsets.getSystemWindowInsetTop();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public final int getTotalScrollRange() {
      if (this.totalScrollRange != -1) {
         return this.totalScrollRange;
      } else {
         int var1 = this.getChildCount();
         int var2 = 0;
         int var3 = 0;

         int var4;
         while(true) {
            var4 = var3;
            if (var2 >= var1) {
               break;
            }

            View var5 = this.getChildAt(var2);
            AppBarLayout.LayoutParams var6 = (AppBarLayout.LayoutParams)var5.getLayoutParams();
            int var7 = var5.getMeasuredHeight();
            int var8 = var6.scrollFlags;
            var4 = var3;
            if ((var8 & 1) == 0) {
               break;
            }

            var3 += var7 + var6.topMargin + var6.bottomMargin;
            if ((var8 & 2) != 0) {
               var4 = var3 - ViewCompat.getMinimumHeight(var5);
               break;
            }

            ++var2;
         }

         var3 = Math.max(0, var4 - this.getTopInset());
         this.totalScrollRange = var3;
         return var3;
      }
   }

   int getUpNestedPreScrollRange() {
      return this.getTotalScrollRange();
   }

   boolean hasChildWithInterpolator() {
      return this.haveChildWithInterpolator;
   }

   boolean hasScrollableChildren() {
      boolean var1;
      if (this.getTotalScrollRange() != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isLiftOnScroll() {
      return this.liftOnScroll;
   }

   protected int[] onCreateDrawableState(int var1) {
      if (this.tmpStatesArray == null) {
         this.tmpStatesArray = new int[4];
      }

      int[] var2 = this.tmpStatesArray;
      int[] var3 = super.onCreateDrawableState(var1 + var2.length);
      if (this.liftable) {
         var1 = R.attr.state_liftable;
      } else {
         var1 = -R.attr.state_liftable;
      }

      var2[0] = var1;
      if (this.liftable && this.lifted) {
         var1 = R.attr.state_lifted;
      } else {
         var1 = -R.attr.state_lifted;
      }

      var2[1] = var1;
      if (this.liftable) {
         var1 = R.attr.state_collapsible;
      } else {
         var1 = -R.attr.state_collapsible;
      }

      var2[2] = var1;
      if (this.liftable && this.lifted) {
         var1 = R.attr.state_collapsed;
      } else {
         var1 = -R.attr.state_collapsed;
      }

      var2[3] = var1;
      return mergeDrawableStates(var3, var2);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.invalidateScrollRanges();
      var1 = false;
      this.haveChildWithInterpolator = false;
      var3 = this.getChildCount();

      for(var2 = 0; var2 < var3; ++var2) {
         if (((AppBarLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).getScrollInterpolator() != null) {
            this.haveChildWithInterpolator = true;
            break;
         }
      }

      if (!this.liftableOverride) {
         if (this.liftOnScroll || this.hasCollapsibleChild()) {
            var1 = true;
         }

         this.setLiftableState(var1);
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      this.invalidateScrollRanges();
   }

   WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat var1) {
      WindowInsetsCompat var2;
      if (ViewCompat.getFitsSystemWindows(this)) {
         var2 = var1;
      } else {
         var2 = null;
      }

      if (!ObjectsCompat.equals(this.lastInsets, var2)) {
         this.lastInsets = var2;
         this.invalidateScrollRanges();
      }

      return var1;
   }

   public void removeOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener var1) {
      if (this.listeners != null && var1 != null) {
         this.listeners.remove(var1);
      }

   }

   public void removeOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener var1) {
      this.removeOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener)var1);
   }

   void resetPendingAction() {
      this.pendingAction = 0;
   }

   public void setExpanded(boolean var1) {
      this.setExpanded(var1, ViewCompat.isLaidOut(this));
   }

   public void setExpanded(boolean var1, boolean var2) {
      this.setExpanded(var1, var2, true);
   }

   public void setLiftOnScroll(boolean var1) {
      this.liftOnScroll = var1;
   }

   boolean setLiftedState(boolean var1) {
      if (this.lifted != var1) {
         this.lifted = var1;
         this.refreshDrawableState();
         return true;
      } else {
         return false;
      }
   }

   public void setOrientation(int var1) {
      if (var1 == 1) {
         super.setOrientation(var1);
      } else {
         throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
      }
   }

   @Deprecated
   public void setTargetElevation(float var1) {
      if (VERSION.SDK_INT >= 21) {
         ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, var1);
      }

   }

   protected static class BaseBehavior extends HeaderBehavior {
      private WeakReference lastNestedScrollingChildRef;
      private int lastStartedType;
      private ValueAnimator offsetAnimator;
      private int offsetDelta;
      private int offsetToChildIndexOnLayout = -1;
      private boolean offsetToChildIndexOnLayoutIsMinHeight;
      private float offsetToChildIndexOnLayoutPerc;
      private AppBarLayout.BaseBehavior.BaseDragCallback onDragCallback;

      public BaseBehavior() {
      }

      public BaseBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      private void animateOffsetTo(CoordinatorLayout var1, AppBarLayout var2, int var3, float var4) {
         int var5 = Math.abs(this.getTopBottomOffsetForScrollingSibling() - var3);
         var4 = Math.abs(var4);
         if (var4 > 0.0F) {
            var5 = Math.round((float)var5 / var4 * 1000.0F) * 3;
         } else {
            var5 = (int)(((float)var5 / (float)var2.getHeight() + 1.0F) * 150.0F);
         }

         this.animateOffsetWithDuration(var1, var2, var3, var5);
      }

      private void animateOffsetWithDuration(final CoordinatorLayout var1, final AppBarLayout var2, int var3, int var4) {
         int var5 = this.getTopBottomOffsetForScrollingSibling();
         if (var5 == var3) {
            if (this.offsetAnimator != null && this.offsetAnimator.isRunning()) {
               this.offsetAnimator.cancel();
            }

         } else {
            if (this.offsetAnimator == null) {
               this.offsetAnimator = new ValueAnimator();
               this.offsetAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
               this.offsetAnimator.addUpdateListener(new AnimatorUpdateListener() {
                  public void onAnimationUpdate(ValueAnimator var1x) {
                     BaseBehavior.this.setHeaderTopBottomOffset(var1, var2, (Integer)var1x.getAnimatedValue());
                  }
               });
            } else {
               this.offsetAnimator.cancel();
            }

            this.offsetAnimator.setDuration((long)Math.min(var4, 600));
            this.offsetAnimator.setIntValues(new int[]{var5, var3});
            this.offsetAnimator.start();
         }
      }

      private boolean canScrollChildren(CoordinatorLayout var1, AppBarLayout var2, View var3) {
         boolean var4;
         if (var2.hasScrollableChildren() && var1.getHeight() - var3.getHeight() <= var2.getHeight()) {
            var4 = true;
         } else {
            var4 = false;
         }

         return var4;
      }

      private static boolean checkFlag(int var0, int var1) {
         boolean var2;
         if ((var0 & var1) == var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      private View findFirstScrollingChild(CoordinatorLayout var1) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = var1.getChildAt(var3);
            if (var4 instanceof NestedScrollingChild) {
               return var4;
            }
         }

         return null;
      }

      private static View getAppBarChildOnOffset(AppBarLayout var0, int var1) {
         int var2 = Math.abs(var1);
         int var3 = var0.getChildCount();

         for(var1 = 0; var1 < var3; ++var1) {
            View var4 = var0.getChildAt(var1);
            if (var2 >= var4.getTop() && var2 <= var4.getBottom()) {
               return var4;
            }
         }

         return null;
      }

      private int getChildIndexOnOffset(AppBarLayout var1, int var2) {
         int var3 = var1.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var5 = var1.getChildAt(var4);
            int var6 = var5.getTop();
            int var7 = var5.getBottom();
            AppBarLayout.LayoutParams var10 = (AppBarLayout.LayoutParams)var5.getLayoutParams();
            int var8 = var6;
            int var9 = var7;
            if (checkFlag(var10.getScrollFlags(), 32)) {
               var8 = var6 - var10.topMargin;
               var9 = var7 + var10.bottomMargin;
            }

            var7 = -var2;
            if (var8 <= var7 && var9 >= var7) {
               return var4;
            }
         }

         return -1;
      }

      private int interpolateOffset(AppBarLayout var1, int var2) {
         int var3 = Math.abs(var2);
         int var4 = var1.getChildCount();
         byte var5 = 0;

         for(int var6 = 0; var6 < var4; ++var6) {
            View var7 = var1.getChildAt(var6);
            AppBarLayout.LayoutParams var8 = (AppBarLayout.LayoutParams)var7.getLayoutParams();
            Interpolator var9 = var8.getScrollInterpolator();
            if (var3 >= var7.getTop() && var3 <= var7.getBottom()) {
               if (var9 != null) {
                  var4 = var8.getScrollFlags();
                  var6 = var5;
                  int var11;
                  if ((var4 & 1) != 0) {
                     var11 = 0 + var7.getHeight() + var8.topMargin + var8.bottomMargin;
                     var6 = var11;
                     if ((var4 & 2) != 0) {
                        var6 = var11 - ViewCompat.getMinimumHeight(var7);
                     }
                  }

                  var11 = var6;
                  if (ViewCompat.getFitsSystemWindows(var7)) {
                     var11 = var6 - var1.getTopInset();
                  }

                  if (var11 > 0) {
                     var6 = var7.getTop();
                     float var10 = (float)var11;
                     var6 = Math.round(var10 * var9.getInterpolation((float)(var3 - var6) / var10));
                     return Integer.signum(var2) * (var7.getTop() + var6);
                  }
               }
               break;
            }
         }

         return var2;
      }

      private boolean shouldJumpElevationState(CoordinatorLayout var1, AppBarLayout var2) {
         List var7 = var1.getDependents(var2);
         int var3 = var7.size();
         boolean var4 = false;

         for(int var5 = 0; var5 < var3; ++var5) {
            CoordinatorLayout.Behavior var6 = ((CoordinatorLayout.LayoutParams)((View)var7.get(var5)).getLayoutParams()).getBehavior();
            if (var6 instanceof AppBarLayout.ScrollingViewBehavior) {
               if (((AppBarLayout.ScrollingViewBehavior)var6).getOverlayTop() != 0) {
                  var4 = true;
               }

               return var4;
            }
         }

         return false;
      }

      private void snapToChildIfNeeded(CoordinatorLayout var1, AppBarLayout var2) {
         int var3 = this.getTopBottomOffsetForScrollingSibling();
         int var4 = this.getChildIndexOnOffset(var2, var3);
         if (var4 >= 0) {
            View var5 = var2.getChildAt(var4);
            AppBarLayout.LayoutParams var6 = (AppBarLayout.LayoutParams)var5.getLayoutParams();
            int var7 = var6.getScrollFlags();
            if ((var7 & 17) == 17) {
               int var8 = -var5.getTop();
               int var9 = -var5.getBottom();
               int var10 = var9;
               if (var4 == var2.getChildCount() - 1) {
                  var10 = var9 + var2.getTopInset();
               }

               if (checkFlag(var7, 2)) {
                  var9 = var10 + ViewCompat.getMinimumHeight(var5);
                  var4 = var8;
               } else {
                  var4 = var8;
                  var9 = var10;
                  if (checkFlag(var7, 5)) {
                     var9 = ViewCompat.getMinimumHeight(var5) + var10;
                     if (var3 < var9) {
                        var4 = var9;
                        var9 = var10;
                     } else {
                        var4 = var8;
                     }
                  }
               }

               var8 = var4;
               var10 = var9;
               if (checkFlag(var7, 32)) {
                  var8 = var4 + var6.topMargin;
                  var10 = var9 - var6.bottomMargin;
               }

               var9 = var8;
               if (var3 < (var10 + var8) / 2) {
                  var9 = var10;
               }

               this.animateOffsetTo(var1, var2, android.support.v4.math.MathUtils.clamp(var9, -var2.getTotalScrollRange(), 0), 0.0F);
            }
         }

      }

      private void stopNestedScrollIfNeeded(int var1, AppBarLayout var2, View var3, int var4) {
         if (var4 == 1) {
            var4 = this.getTopBottomOffsetForScrollingSibling();
            if (var1 < 0 && var4 == 0 || var1 > 0 && var4 == -var2.getDownNestedScrollRange()) {
               ViewCompat.stopNestedScroll(var3, 1);
            }
         }

      }

      private void updateAppBarLayoutDrawableState(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, boolean var5) {
         View var6 = getAppBarChildOnOffset(var2, var3);
         if (var6 != null) {
            boolean var9;
            label48: {
               label55: {
                  int var7 = ((AppBarLayout.LayoutParams)var6.getLayoutParams()).getScrollFlags();
                  if ((var7 & 1) != 0) {
                     int var8 = ViewCompat.getMinimumHeight(var6);
                     if (var4 > 0 && (var7 & 12) != 0) {
                        if (-var3 >= var6.getBottom() - var8 - var2.getTopInset()) {
                           break label55;
                        }
                     } else if ((var7 & 2) != 0 && -var3 >= var6.getBottom() - var8 - var2.getTopInset()) {
                        break label55;
                     }
                  }

                  var9 = false;
                  break label48;
               }

               var9 = true;
            }

            boolean var10 = var9;
            if (var2.isLiftOnScroll()) {
               var6 = this.findFirstScrollingChild(var1);
               var10 = var9;
               if (var6 != null) {
                  if (var6.getScrollY() > 0) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }
               }
            }

            var10 = var2.setLiftedState(var10);
            if (VERSION.SDK_INT >= 11 && (var5 || var10 && this.shouldJumpElevationState(var1, var2))) {
               var2.jumpDrawablesToCurrentState();
            }
         }

      }

      boolean canDragView(AppBarLayout var1) {
         if (this.onDragCallback != null) {
            return this.onDragCallback.canDrag(var1);
         } else {
            WeakReference var3 = this.lastNestedScrollingChildRef;
            boolean var2 = true;
            if (var3 == null) {
               return true;
            } else {
               View var4 = (View)this.lastNestedScrollingChildRef.get();
               if (var4 == null || !var4.isShown() || var4.canScrollVertically(-1)) {
                  var2 = false;
               }

               return var2;
            }
         }
      }

      int getMaxDragOffset(AppBarLayout var1) {
         return -var1.getDownNestedScrollRange();
      }

      int getScrollRangeForDragFling(AppBarLayout var1) {
         return var1.getTotalScrollRange();
      }

      int getTopBottomOffsetForScrollingSibling() {
         return this.getTopAndBottomOffset() + this.offsetDelta;
      }

      void onFlingFinished(CoordinatorLayout var1, AppBarLayout var2) {
         this.snapToChildIfNeeded(var1, var2);
      }

      public boolean onLayoutChild(CoordinatorLayout var1, AppBarLayout var2, int var3) {
         boolean var4 = super.onLayoutChild(var1, var2, var3);
         int var5 = var2.getPendingAction();
         if (this.offsetToChildIndexOnLayout >= 0 && (var5 & 8) == 0) {
            View var6 = var2.getChildAt(this.offsetToChildIndexOnLayout);
            var3 = -var6.getBottom();
            if (this.offsetToChildIndexOnLayoutIsMinHeight) {
               var3 += ViewCompat.getMinimumHeight(var6) + var2.getTopInset();
            } else {
               var3 += Math.round((float)var6.getHeight() * this.offsetToChildIndexOnLayoutPerc);
            }

            this.setHeaderTopBottomOffset(var1, var2, var3);
         } else if (var5 != 0) {
            boolean var7;
            if ((var5 & 4) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            if ((var5 & 2) != 0) {
               var5 = -var2.getUpNestedPreScrollRange();
               if (var7) {
                  this.animateOffsetTo(var1, var2, var5, 0.0F);
               } else {
                  this.setHeaderTopBottomOffset(var1, var2, var5);
               }
            } else if ((var5 & 1) != 0) {
               if (var7) {
                  this.animateOffsetTo(var1, var2, 0, 0.0F);
               } else {
                  this.setHeaderTopBottomOffset(var1, var2, 0);
               }
            }
         }

         var2.resetPendingAction();
         this.offsetToChildIndexOnLayout = -1;
         this.setTopAndBottomOffset(android.support.v4.math.MathUtils.clamp(this.getTopAndBottomOffset(), -var2.getTotalScrollRange(), 0));
         this.updateAppBarLayoutDrawableState(var1, var2, this.getTopAndBottomOffset(), 0, true);
         var2.dispatchOffsetUpdates(this.getTopAndBottomOffset());
         return var4;
      }

      public boolean onMeasureChild(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, int var5, int var6) {
         if (((CoordinatorLayout.LayoutParams)var2.getLayoutParams()).height == -2) {
            var1.onMeasureChild(var2, var3, var4, MeasureSpec.makeMeasureSpec(0, 0), var6);
            return true;
         } else {
            return super.onMeasureChild(var1, var2, var3, var4, var5, var6);
         }
      }

      public void onNestedPreScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4, int var5, int[] var6, int var7) {
         if (var5 != 0) {
            int var8;
            if (var5 < 0) {
               var8 = -var2.getTotalScrollRange();
               int var9 = var2.getDownNestedPreScrollRange();
               var9 += var8;
               var8 = var8;
               var4 = var9;
            } else {
               var8 = -var2.getUpNestedPreScrollRange();
               var4 = 0;
            }

            if (var8 != var4) {
               var6[1] = this.scroll(var1, var2, var5, var8, var4);
               this.stopNestedScrollIfNeeded(var5, var2, var3, var7);
            }
         }

      }

      public void onNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4, int var5, int var6, int var7, int var8) {
         if (var7 < 0) {
            this.scroll(var1, var2, var7, -var2.getDownNestedScrollRange(), 0);
            this.stopNestedScrollIfNeeded(var7, var2, var3, var8);
         }

         if (var2.isLiftOnScroll()) {
            boolean var9;
            if (var3.getScrollY() > 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            var2.setLiftedState(var9);
         }

      }

      public void onRestoreInstanceState(CoordinatorLayout var1, AppBarLayout var2, Parcelable var3) {
         if (var3 instanceof AppBarLayout.BaseBehavior.SavedState) {
            AppBarLayout.BaseBehavior.SavedState var4 = (AppBarLayout.BaseBehavior.SavedState)var3;
            super.onRestoreInstanceState(var1, var2, var4.getSuperState());
            this.offsetToChildIndexOnLayout = var4.firstVisibleChildIndex;
            this.offsetToChildIndexOnLayoutPerc = var4.firstVisibleChildPercentageShown;
            this.offsetToChildIndexOnLayoutIsMinHeight = var4.firstVisibleChildAtMinimumHeight;
         } else {
            super.onRestoreInstanceState(var1, var2, var3);
            this.offsetToChildIndexOnLayout = -1;
         }

      }

      public Parcelable onSaveInstanceState(CoordinatorLayout var1, AppBarLayout var2) {
         Parcelable var3 = super.onSaveInstanceState(var1, var2);
         int var4 = this.getTopAndBottomOffset();
         int var5 = var2.getChildCount();
         boolean var6 = false;

         for(int var7 = 0; var7 < var5; ++var7) {
            View var9 = var2.getChildAt(var7);
            int var8 = var9.getBottom() + var4;
            if (var9.getTop() + var4 <= 0 && var8 >= 0) {
               AppBarLayout.BaseBehavior.SavedState var10 = new AppBarLayout.BaseBehavior.SavedState(var3);
               var10.firstVisibleChildIndex = var7;
               if (var8 == ViewCompat.getMinimumHeight(var9) + var2.getTopInset()) {
                  var6 = true;
               }

               var10.firstVisibleChildAtMinimumHeight = var6;
               var10.firstVisibleChildPercentageShown = (float)var8 / (float)var9.getHeight();
               return var10;
            }
         }

         return var3;
      }

      public boolean onStartNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, View var4, int var5, int var6) {
         boolean var7;
         if ((var5 & 2) == 0 || !var2.isLiftOnScroll() && !this.canScrollChildren(var1, var2, var3)) {
            var7 = false;
         } else {
            var7 = true;
         }

         if (var7 && this.offsetAnimator != null) {
            this.offsetAnimator.cancel();
         }

         this.lastNestedScrollingChildRef = null;
         this.lastStartedType = var6;
         return var7;
      }

      public void onStopNestedScroll(CoordinatorLayout var1, AppBarLayout var2, View var3, int var4) {
         if (this.lastStartedType == 0 || var4 == 1) {
            this.snapToChildIfNeeded(var1, var2);
         }

         this.lastNestedScrollingChildRef = new WeakReference(var3);
      }

      int setHeaderTopBottomOffset(CoordinatorLayout var1, AppBarLayout var2, int var3, int var4, int var5) {
         int var6 = this.getTopBottomOffsetForScrollingSibling();
         byte var7 = 0;
         if (var4 != 0 && var6 >= var4 && var6 <= var5) {
            var4 = android.support.v4.math.MathUtils.clamp(var3, var4, var5);
            var3 = var7;
            if (var6 != var4) {
               if (var2.hasChildWithInterpolator()) {
                  var3 = this.interpolateOffset(var2, var4);
               } else {
                  var3 = var4;
               }

               boolean var8 = this.setTopAndBottomOffset(var3);
               var5 = var6 - var4;
               this.offsetDelta = var4 - var3;
               if (!var8 && var2.hasChildWithInterpolator()) {
                  var1.dispatchDependentViewsChanged(var2);
               }

               var2.dispatchOffsetUpdates(this.getTopAndBottomOffset());
               byte var9;
               if (var4 < var6) {
                  var9 = -1;
               } else {
                  var9 = 1;
               }

               this.updateAppBarLayoutDrawableState(var1, var2, var4, var9, false);
               var3 = var5;
            }
         } else {
            this.offsetDelta = 0;
            var3 = var7;
         }

         return var3;
      }

      public abstract static class BaseDragCallback {
         public abstract boolean canDrag(AppBarLayout var1);
      }

      protected static class SavedState extends AbsSavedState {
         public static final Creator CREATOR = new ClassLoaderCreator() {
            public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel var1) {
               return new AppBarLayout.BaseBehavior.SavedState(var1, (ClassLoader)null);
            }

            public AppBarLayout.BaseBehavior.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
               return new AppBarLayout.BaseBehavior.SavedState(var1, var2);
            }

            public AppBarLayout.BaseBehavior.SavedState[] newArray(int var1) {
               return new AppBarLayout.BaseBehavior.SavedState[var1];
            }
         };
         boolean firstVisibleChildAtMinimumHeight;
         int firstVisibleChildIndex;
         float firstVisibleChildPercentageShown;

         public SavedState(Parcel var1, ClassLoader var2) {
            super(var1, var2);
            this.firstVisibleChildIndex = var1.readInt();
            this.firstVisibleChildPercentageShown = var1.readFloat();
            boolean var3;
            if (var1.readByte() != 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.firstVisibleChildAtMinimumHeight = var3;
         }

         public SavedState(Parcelable var1) {
            super(var1);
         }

         public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeInt(this.firstVisibleChildIndex);
            var1.writeFloat(this.firstVisibleChildPercentageShown);
            var1.writeByte((byte)this.firstVisibleChildAtMinimumHeight);
         }
      }
   }

   public interface BaseOnOffsetChangedListener {
      void onOffsetChanged(AppBarLayout var1, int var2);
   }

   public static class Behavior extends AppBarLayout.BaseBehavior {
      public Behavior() {
      }

      public Behavior(Context var1, AttributeSet var2) {
         super(var1, var2);
      }
   }

   public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
      int scrollFlags = 1;
      Interpolator scrollInterpolator;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.AppBarLayout_Layout);
         this.scrollFlags = var3.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
         if (var3.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
            this.scrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(var1, var3.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
         }

         var3.recycle();
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public LayoutParams(android.widget.LinearLayout.LayoutParams var1) {
         super(var1);
      }

      public int getScrollFlags() {
         return this.scrollFlags;
      }

      public Interpolator getScrollInterpolator() {
         return this.scrollInterpolator;
      }

      boolean isCollapsible() {
         int var1 = this.scrollFlags;
         boolean var2 = true;
         if ((var1 & 1) != 1 || (this.scrollFlags & 10) == 0) {
            var2 = false;
         }

         return var2;
      }
   }

   public interface OnOffsetChangedListener extends AppBarLayout.BaseOnOffsetChangedListener {
   }

   public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
      public ScrollingViewBehavior() {
      }

      public ScrollingViewBehavior(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.ScrollingViewBehavior_Layout);
         this.setOverlayTop(var3.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
         var3.recycle();
      }

      private static int getAppBarLayoutOffset(AppBarLayout var0) {
         CoordinatorLayout.Behavior var1 = ((CoordinatorLayout.LayoutParams)var0.getLayoutParams()).getBehavior();
         return var1 instanceof AppBarLayout.BaseBehavior ? ((AppBarLayout.BaseBehavior)var1).getTopBottomOffsetForScrollingSibling() : 0;
      }

      private void offsetChildAsNeeded(View var1, View var2) {
         CoordinatorLayout.Behavior var3 = ((CoordinatorLayout.LayoutParams)var2.getLayoutParams()).getBehavior();
         if (var3 instanceof AppBarLayout.BaseBehavior) {
            AppBarLayout.BaseBehavior var4 = (AppBarLayout.BaseBehavior)var3;
            ViewCompat.offsetTopAndBottom(var1, var2.getBottom() - var1.getTop() + var4.offsetDelta + this.getVerticalLayoutGap() - this.getOverlapPixelsForOffset(var2));
         }

      }

      private void updateLiftedStateIfNeeded(View var1, View var2) {
         if (var2 instanceof AppBarLayout) {
            AppBarLayout var4 = (AppBarLayout)var2;
            if (var4.isLiftOnScroll()) {
               boolean var3;
               if (var1.getScrollY() > 0) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var4.setLiftedState(var3);
            }
         }

      }

      AppBarLayout findFirstDependency(List var1) {
         int var2 = var1.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = (View)var1.get(var3);
            if (var4 instanceof AppBarLayout) {
               return (AppBarLayout)var4;
            }
         }

         return null;
      }

      float getOverlapRatioForOffset(View var1) {
         if (var1 instanceof AppBarLayout) {
            AppBarLayout var5 = (AppBarLayout)var1;
            int var2 = var5.getTotalScrollRange();
            int var3 = var5.getDownNestedPreScrollRange();
            int var4 = getAppBarLayoutOffset(var5);
            if (var3 != 0 && var2 + var4 <= var3) {
               return 0.0F;
            }

            var3 = var2 - var3;
            if (var3 != 0) {
               return (float)var4 / (float)var3 + 1.0F;
            }
         }

         return 0.0F;
      }

      int getScrollRange(View var1) {
         return var1 instanceof AppBarLayout ? ((AppBarLayout)var1).getTotalScrollRange() : super.getScrollRange(var1);
      }

      public boolean layoutDependsOn(CoordinatorLayout var1, View var2, View var3) {
         return var3 instanceof AppBarLayout;
      }

      public boolean onDependentViewChanged(CoordinatorLayout var1, View var2, View var3) {
         this.offsetChildAsNeeded(var2, var3);
         this.updateLiftedStateIfNeeded(var2, var3);
         return false;
      }

      public boolean onRequestChildRectangleOnScreen(CoordinatorLayout var1, View var2, Rect var3, boolean var4) {
         AppBarLayout var5 = this.findFirstDependency(var1.getDependencies(var2));
         if (var5 != null) {
            var3.offset(var2.getLeft(), var2.getTop());
            Rect var6 = this.tempRect1;
            var6.set(0, 0, var1.getWidth(), var1.getHeight());
            if (!var6.contains(var3)) {
               var5.setExpanded(false, var4 ^ true);
               return true;
            }
         }

         return false;
      }
   }
}
