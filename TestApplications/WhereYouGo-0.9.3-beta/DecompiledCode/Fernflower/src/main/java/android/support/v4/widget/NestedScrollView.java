package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.RestrictTo;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import java.util.ArrayList;

public class NestedScrollView extends FrameLayout implements NestedScrollingParent, NestedScrollingChild, ScrollingView {
   private static final NestedScrollView.AccessibilityDelegate ACCESSIBILITY_DELEGATE = new NestedScrollView.AccessibilityDelegate();
   static final int ANIMATED_SCROLL_GAP = 250;
   private static final int INVALID_POINTER = -1;
   static final float MAX_SCROLL_FACTOR = 0.5F;
   private static final int[] SCROLLVIEW_STYLEABLE = new int[]{16843130};
   private static final String TAG = "NestedScrollView";
   private int mActivePointerId;
   private final NestedScrollingChildHelper mChildHelper;
   private View mChildToScrollTo;
   private EdgeEffectCompat mEdgeGlowBottom;
   private EdgeEffectCompat mEdgeGlowTop;
   private boolean mFillViewport;
   private boolean mIsBeingDragged;
   private boolean mIsLaidOut;
   private boolean mIsLayoutDirty;
   private int mLastMotionY;
   private long mLastScroll;
   private int mMaximumVelocity;
   private int mMinimumVelocity;
   private int mNestedYOffset;
   private NestedScrollView.OnScrollChangeListener mOnScrollChangeListener;
   private final NestedScrollingParentHelper mParentHelper;
   private NestedScrollView.SavedState mSavedState;
   private final int[] mScrollConsumed;
   private final int[] mScrollOffset;
   private ScrollerCompat mScroller;
   private boolean mSmoothScrollingEnabled;
   private final Rect mTempRect;
   private int mTouchSlop;
   private VelocityTracker mVelocityTracker;
   private float mVerticalScrollFactor;

   public NestedScrollView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public NestedScrollView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public NestedScrollView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mTempRect = new Rect();
      this.mIsLayoutDirty = true;
      this.mIsLaidOut = false;
      this.mChildToScrollTo = null;
      this.mIsBeingDragged = false;
      this.mSmoothScrollingEnabled = true;
      this.mActivePointerId = -1;
      this.mScrollOffset = new int[2];
      this.mScrollConsumed = new int[2];
      this.initScrollView();
      TypedArray var4 = var1.obtainStyledAttributes(var2, SCROLLVIEW_STYLEABLE, var3, 0);
      this.setFillViewport(var4.getBoolean(0, false));
      var4.recycle();
      this.mParentHelper = new NestedScrollingParentHelper(this);
      this.mChildHelper = new NestedScrollingChildHelper(this);
      this.setNestedScrollingEnabled(true);
      ViewCompat.setAccessibilityDelegate(this, ACCESSIBILITY_DELEGATE);
   }

   private boolean canScroll() {
      boolean var1 = false;
      View var2 = this.getChildAt(0);
      boolean var3 = var1;
      if (var2 != null) {
         int var4 = var2.getHeight();
         var3 = var1;
         if (this.getHeight() < this.getPaddingTop() + var4 + this.getPaddingBottom()) {
            var3 = true;
         }
      }

      return var3;
   }

   private static int clamp(int var0, int var1, int var2) {
      int var3;
      if (var1 < var2 && var0 >= 0) {
         var3 = var0;
         if (var1 + var0 > var2) {
            var3 = var2 - var1;
         }
      } else {
         var3 = 0;
      }

      return var3;
   }

   private void doScrollY(int var1) {
      if (var1 != 0) {
         if (this.mSmoothScrollingEnabled) {
            this.smoothScrollBy(0, var1);
         } else {
            this.scrollBy(0, var1);
         }
      }

   }

   private void endDrag() {
      this.mIsBeingDragged = false;
      this.recycleVelocityTracker();
      this.stopNestedScroll();
      if (this.mEdgeGlowTop != null) {
         this.mEdgeGlowTop.onRelease();
         this.mEdgeGlowBottom.onRelease();
      }

   }

   private void ensureGlows() {
      if (this.getOverScrollMode() != 2) {
         if (this.mEdgeGlowTop == null) {
            Context var1 = this.getContext();
            this.mEdgeGlowTop = new EdgeEffectCompat(var1);
            this.mEdgeGlowBottom = new EdgeEffectCompat(var1);
         }
      } else {
         this.mEdgeGlowTop = null;
         this.mEdgeGlowBottom = null;
      }

   }

   private View findFocusableViewInBounds(boolean var1, int var2, int var3) {
      ArrayList var4 = this.getFocusables(2);
      View var5 = null;
      boolean var6 = false;
      int var7 = var4.size();

      boolean var13;
      for(int var8 = 0; var8 < var7; var6 = var13) {
         View var9 = (View)var4.get(var8);
         int var10 = var9.getTop();
         int var11 = var9.getBottom();
         View var12 = var5;
         var13 = var6;
         if (var2 < var11) {
            var12 = var5;
            var13 = var6;
            if (var10 < var3) {
               boolean var14;
               if (var2 < var10 && var11 < var3) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               if (var5 == null) {
                  var12 = var9;
                  var13 = var14;
               } else {
                  boolean var15;
                  if ((!var1 || var10 >= var5.getTop()) && (var1 || var11 <= var5.getBottom())) {
                     var15 = false;
                  } else {
                     var15 = true;
                  }

                  if (var6) {
                     var12 = var5;
                     var13 = var6;
                     if (var14) {
                        var12 = var5;
                        var13 = var6;
                        if (var15) {
                           var12 = var9;
                           var13 = var6;
                        }
                     }
                  } else if (var14) {
                     var12 = var9;
                     var13 = true;
                  } else {
                     var12 = var5;
                     var13 = var6;
                     if (var15) {
                        var12 = var9;
                        var13 = var6;
                     }
                  }
               }
            }
         }

         ++var8;
         var5 = var12;
      }

      return var5;
   }

   private void flingWithNestedDispatch(int var1) {
      int var2 = this.getScrollY();
      boolean var3;
      if (var2 <= 0 && var1 <= 0 || var2 >= this.getScrollRange() && var1 >= 0) {
         var3 = false;
      } else {
         var3 = true;
      }

      if (!this.dispatchNestedPreFling(0.0F, (float)var1)) {
         this.dispatchNestedFling(0.0F, (float)var1, var3);
         if (var3) {
            this.fling(var1);
         }
      }

   }

   private float getVerticalScrollFactorCompat() {
      if (this.mVerticalScrollFactor == 0.0F) {
         TypedValue var1 = new TypedValue();
         Context var2 = this.getContext();
         if (!var2.getTheme().resolveAttribute(16842829, var1, true)) {
            throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
         }

         this.mVerticalScrollFactor = var1.getDimension(var2.getResources().getDisplayMetrics());
      }

      return this.mVerticalScrollFactor;
   }

   private boolean inChild(int var1, int var2) {
      boolean var3 = false;
      boolean var4 = var3;
      if (this.getChildCount() > 0) {
         int var5 = this.getScrollY();
         View var6 = this.getChildAt(0);
         var4 = var3;
         if (var2 >= var6.getTop() - var5) {
            var4 = var3;
            if (var2 < var6.getBottom() - var5) {
               var4 = var3;
               if (var1 >= var6.getLeft()) {
                  var4 = var3;
                  if (var1 < var6.getRight()) {
                     var4 = true;
                  }
               }
            }
         }
      }

      return var4;
   }

   private void initOrResetVelocityTracker() {
      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      } else {
         this.mVelocityTracker.clear();
      }

   }

   private void initScrollView() {
      this.mScroller = ScrollerCompat.create(this.getContext(), (Interpolator)null);
      this.setFocusable(true);
      this.setDescendantFocusability(262144);
      this.setWillNotDraw(false);
      ViewConfiguration var1 = ViewConfiguration.get(this.getContext());
      this.mTouchSlop = var1.getScaledTouchSlop();
      this.mMinimumVelocity = var1.getScaledMinimumFlingVelocity();
      this.mMaximumVelocity = var1.getScaledMaximumFlingVelocity();
   }

   private void initVelocityTrackerIfNotExists() {
      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

   }

   private boolean isOffScreen(View var1) {
      boolean var2 = false;
      if (!this.isWithinDeltaOfScreen(var1, 0, this.getHeight())) {
         var2 = true;
      }

      return var2;
   }

   private static boolean isViewDescendantOf(View var0, View var1) {
      boolean var2 = true;
      if (var0 != var1) {
         ViewParent var3 = var0.getParent();
         if (!(var3 instanceof ViewGroup) || !isViewDescendantOf((View)var3, var1)) {
            var2 = false;
         }
      }

      return var2;
   }

   private boolean isWithinDeltaOfScreen(View var1, int var2, int var3) {
      var1.getDrawingRect(this.mTempRect);
      this.offsetDescendantRectToMyCoords(var1, this.mTempRect);
      boolean var4;
      if (this.mTempRect.bottom + var2 >= this.getScrollY() && this.mTempRect.top - var2 <= this.getScrollY() + var3) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   private void onSecondaryPointerUp(MotionEvent var1) {
      int var2 = (var1.getAction() & '\uff00') >> 8;
      if (var1.getPointerId(var2) == this.mActivePointerId) {
         byte var3;
         if (var2 == 0) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         this.mLastMotionY = (int)var1.getY(var3);
         this.mActivePointerId = var1.getPointerId(var3);
         if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
         }
      }

   }

   private void recycleVelocityTracker() {
      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

   }

   private boolean scrollAndFocus(int var1, int var2, int var3) {
      boolean var4 = true;
      int var5 = this.getHeight();
      int var6 = this.getScrollY();
      var5 += var6;
      boolean var7;
      if (var1 == 33) {
         var7 = true;
      } else {
         var7 = false;
      }

      View var8 = this.findFocusableViewInBounds(var7, var2, var3);
      Object var9 = var8;
      if (var8 == null) {
         var9 = this;
      }

      if (var2 >= var6 && var3 <= var5) {
         var7 = false;
      } else {
         if (var7) {
            var2 -= var6;
         } else {
            var2 = var3 - var5;
         }

         this.doScrollY(var2);
         var7 = var4;
      }

      if (var9 != this.findFocus()) {
         ((View)var9).requestFocus(var1);
      }

      return var7;
   }

   private void scrollToChild(View var1) {
      var1.getDrawingRect(this.mTempRect);
      this.offsetDescendantRectToMyCoords(var1, this.mTempRect);
      int var2 = this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
      if (var2 != 0) {
         this.scrollBy(0, var2);
      }

   }

   private boolean scrollToChildRect(Rect var1, boolean var2) {
      int var3 = this.computeScrollDeltaToGetChildRectOnScreen(var1);
      boolean var4;
      if (var3 != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (var4) {
         if (var2) {
            this.scrollBy(0, var3);
         } else {
            this.smoothScrollBy(0, var3);
         }
      }

      return var4;
   }

   public void addView(View var1) {
      if (this.getChildCount() > 0) {
         throw new IllegalStateException("ScrollView can host only one direct child");
      } else {
         super.addView(var1);
      }
   }

   public void addView(View var1, int var2) {
      if (this.getChildCount() > 0) {
         throw new IllegalStateException("ScrollView can host only one direct child");
      } else {
         super.addView(var1, var2);
      }
   }

   public void addView(View var1, int var2, LayoutParams var3) {
      if (this.getChildCount() > 0) {
         throw new IllegalStateException("ScrollView can host only one direct child");
      } else {
         super.addView(var1, var2, var3);
      }
   }

   public void addView(View var1, LayoutParams var2) {
      if (this.getChildCount() > 0) {
         throw new IllegalStateException("ScrollView can host only one direct child");
      } else {
         super.addView(var1, var2);
      }
   }

   public boolean arrowScroll(int var1) {
      boolean var2 = false;
      View var3 = this.findFocus();
      View var4 = var3;
      if (var3 == this) {
         var4 = null;
      }

      var3 = FocusFinder.getInstance().findNextFocus(this, var4, var1);
      int var5 = this.getMaxScrollAmount();
      if (var3 != null && this.isWithinDeltaOfScreen(var3, var5, this.getHeight())) {
         var3.getDrawingRect(this.mTempRect);
         this.offsetDescendantRectToMyCoords(var3, this.mTempRect);
         this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
         var3.requestFocus(var1);
      } else {
         int var7;
         if (var1 == 33 && this.getScrollY() < var5) {
            var7 = this.getScrollY();
         } else {
            var7 = var5;
            if (var1 == 130) {
               var7 = var5;
               if (this.getChildCount() > 0) {
                  int var8 = this.getChildAt(0).getBottom();
                  int var9 = this.getScrollY() + this.getHeight() - this.getPaddingBottom();
                  var7 = var5;
                  if (var8 - var9 < var5) {
                     var7 = var8 - var9;
                  }
               }
            }
         }

         if (var7 == 0) {
            return var2;
         }

         if (var1 != 130) {
            var7 = -var7;
         }

         this.doScrollY(var7);
      }

      if (var4 != null && var4.isFocused() && this.isOffScreen(var4)) {
         var1 = this.getDescendantFocusability();
         this.setDescendantFocusability(131072);
         this.requestFocus();
         this.setDescendantFocusability(var1);
      }

      var2 = true;
      return var2;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeHorizontalScrollExtent() {
      return super.computeHorizontalScrollExtent();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeHorizontalScrollOffset() {
      return super.computeHorizontalScrollOffset();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeHorizontalScrollRange() {
      return super.computeHorizontalScrollRange();
   }

   public void computeScroll() {
      boolean var1 = true;
      if (this.mScroller.computeScrollOffset()) {
         int var2 = this.getScrollX();
         int var3 = this.getScrollY();
         int var4 = this.mScroller.getCurrX();
         int var5 = this.mScroller.getCurrY();
         if (var2 != var4 || var3 != var5) {
            int var6 = this.getScrollRange();
            int var7 = this.getOverScrollMode();
            boolean var8 = var1;
            if (var7 != 0) {
               if (var7 == 1 && var6 > 0) {
                  var8 = var1;
               } else {
                  var8 = false;
               }
            }

            this.overScrollByCompat(var4 - var2, var5 - var3, var2, var3, 0, var6, 0, 0, false);
            if (var8) {
               this.ensureGlows();
               if (var5 <= 0 && var3 > 0) {
                  this.mEdgeGlowTop.onAbsorb((int)this.mScroller.getCurrVelocity());
               } else if (var5 >= var6 && var3 < var6) {
                  this.mEdgeGlowBottom.onAbsorb((int)this.mScroller.getCurrVelocity());
               }
            }
         }
      }

   }

   protected int computeScrollDeltaToGetChildRectOnScreen(Rect var1) {
      int var2;
      if (this.getChildCount() == 0) {
         var2 = 0;
      } else {
         int var3 = this.getHeight();
         int var4 = this.getScrollY();
         var2 = var4 + var3;
         int var5 = this.getVerticalFadingEdgeLength();
         int var6 = var4;
         if (var1.top > 0) {
            var6 = var4 + var5;
         }

         var4 = var2;
         if (var1.bottom < this.getChildAt(0).getHeight()) {
            var4 = var2 - var5;
         }

         byte var7 = 0;
         if (var1.bottom > var4 && var1.top > var6) {
            if (var1.height() > var3) {
               var2 = 0 + (var1.top - var6);
            } else {
               var2 = 0 + (var1.bottom - var4);
            }

            var2 = Math.min(var2, this.getChildAt(0).getBottom() - var4);
         } else {
            var2 = var7;
            if (var1.top < var6) {
               var2 = var7;
               if (var1.bottom < var4) {
                  if (var1.height() > var3) {
                     var2 = 0 - (var4 - var1.bottom);
                  } else {
                     var2 = 0 - (var6 - var1.top);
                  }

                  var2 = Math.max(var2, -this.getScrollY());
               }
            }
         }
      }

      return var2;
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeVerticalScrollExtent() {
      return super.computeVerticalScrollExtent();
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeVerticalScrollOffset() {
      return Math.max(0, super.computeVerticalScrollOffset());
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public int computeVerticalScrollRange() {
      int var1 = this.getChildCount();
      int var2 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
      if (var1 != 0) {
         var1 = this.getChildAt(0).getBottom();
         int var3 = this.getScrollY();
         int var4 = Math.max(0, var1 - var2);
         if (var3 < 0) {
            var2 = var1 - var3;
         } else {
            var2 = var1;
            if (var3 > var4) {
               var2 = var1 + (var3 - var4);
            }
         }
      }

      return var2;
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      boolean var2;
      if (!super.dispatchKeyEvent(var1) && !this.executeKeyEvent(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      return this.mChildHelper.dispatchNestedFling(var1, var2, var3);
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      return this.mChildHelper.dispatchNestedPreFling(var1, var2);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      return this.mChildHelper.dispatchNestedPreScroll(var1, var2, var3, var4);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      return this.mChildHelper.dispatchNestedScroll(var1, var2, var3, var4, var5);
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      if (this.mEdgeGlowTop != null) {
         int var2 = this.getScrollY();
         int var3;
         int var4;
         int var5;
         if (!this.mEdgeGlowTop.isFinished()) {
            var3 = var1.save();
            var4 = this.getWidth();
            var5 = this.getPaddingLeft();
            int var6 = this.getPaddingRight();
            var1.translate((float)this.getPaddingLeft(), (float)Math.min(0, var2));
            this.mEdgeGlowTop.setSize(var4 - var5 - var6, this.getHeight());
            if (this.mEdgeGlowTop.draw(var1)) {
               ViewCompat.postInvalidateOnAnimation(this);
            }

            var1.restoreToCount(var3);
         }

         if (!this.mEdgeGlowBottom.isFinished()) {
            var3 = var1.save();
            var5 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            var4 = this.getHeight();
            var1.translate((float)(-var5 + this.getPaddingLeft()), (float)(Math.max(this.getScrollRange(), var2) + var4));
            var1.rotate(180.0F, (float)var5, 0.0F);
            this.mEdgeGlowBottom.setSize(var5, var4);
            if (this.mEdgeGlowBottom.draw(var1)) {
               ViewCompat.postInvalidateOnAnimation(this);
            }

            var1.restoreToCount(var3);
         }
      }

   }

   public boolean executeKeyEvent(KeyEvent var1) {
      boolean var2 = false;
      this.mTempRect.setEmpty();
      boolean var3;
      if (!this.canScroll()) {
         var3 = var2;
         if (this.isFocused()) {
            var3 = var2;
            if (var1.getKeyCode() != 4) {
               View var4 = this.findFocus();
               View var6 = var4;
               if (var4 == this) {
                  var6 = null;
               }

               var6 = FocusFinder.getInstance().findNextFocus(this, var6, 130);
               var3 = var2;
               if (var6 != null) {
                  var3 = var2;
                  if (var6 != this) {
                     var3 = var2;
                     if (var6.requestFocus(130)) {
                        var3 = true;
                     }
                  }
               }
            }
         }
      } else {
         var2 = false;
         var3 = var2;
         if (var1.getAction() == 0) {
            switch(var1.getKeyCode()) {
            case 19:
               if (!var1.isAltPressed()) {
                  var3 = this.arrowScroll(33);
               } else {
                  var3 = this.fullScroll(33);
               }
               break;
            case 20:
               if (!var1.isAltPressed()) {
                  var3 = this.arrowScroll(130);
               } else {
                  var3 = this.fullScroll(130);
               }
               break;
            case 62:
               short var5;
               if (var1.isShiftPressed()) {
                  var5 = 33;
               } else {
                  var5 = 130;
               }

               this.pageScroll(var5);
               var3 = var2;
               break;
            default:
               var3 = var2;
            }
         }
      }

      return var3;
   }

   public void fling(int var1) {
      if (this.getChildCount() > 0) {
         int var2 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
         int var3 = this.getChildAt(0).getHeight();
         this.mScroller.fling(this.getScrollX(), this.getScrollY(), 0, var1, 0, 0, 0, Math.max(0, var3 - var2), 0, var2 / 2);
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   public boolean fullScroll(int var1) {
      boolean var2;
      if (var1 == 130) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var3 = this.getHeight();
      this.mTempRect.top = 0;
      this.mTempRect.bottom = var3;
      if (var2) {
         int var5 = this.getChildCount();
         if (var5 > 0) {
            View var4 = this.getChildAt(var5 - 1);
            this.mTempRect.bottom = var4.getBottom() + this.getPaddingBottom();
            this.mTempRect.top = this.mTempRect.bottom - var3;
         }
      }

      return this.scrollAndFocus(var1, this.mTempRect.top, this.mTempRect.bottom);
   }

   protected float getBottomFadingEdgeStrength() {
      float var1;
      if (this.getChildCount() == 0) {
         var1 = 0.0F;
      } else {
         int var2 = this.getVerticalFadingEdgeLength();
         int var3 = this.getHeight();
         int var4 = this.getPaddingBottom();
         var4 = this.getChildAt(0).getBottom() - this.getScrollY() - (var3 - var4);
         if (var4 < var2) {
            var1 = (float)var4 / (float)var2;
         } else {
            var1 = 1.0F;
         }
      }

      return var1;
   }

   public int getMaxScrollAmount() {
      return (int)(0.5F * (float)this.getHeight());
   }

   public int getNestedScrollAxes() {
      return this.mParentHelper.getNestedScrollAxes();
   }

   int getScrollRange() {
      int var1 = 0;
      if (this.getChildCount() > 0) {
         var1 = Math.max(0, this.getChildAt(0).getHeight() - (this.getHeight() - this.getPaddingBottom() - this.getPaddingTop()));
      }

      return var1;
   }

   protected float getTopFadingEdgeStrength() {
      float var1;
      if (this.getChildCount() == 0) {
         var1 = 0.0F;
      } else {
         int var2 = this.getVerticalFadingEdgeLength();
         int var3 = this.getScrollY();
         if (var3 < var2) {
            var1 = (float)var3 / (float)var2;
         } else {
            var1 = 1.0F;
         }
      }

      return var1;
   }

   public boolean hasNestedScrollingParent() {
      return this.mChildHelper.hasNestedScrollingParent();
   }

   public boolean isFillViewport() {
      return this.mFillViewport;
   }

   public boolean isNestedScrollingEnabled() {
      return this.mChildHelper.isNestedScrollingEnabled();
   }

   public boolean isSmoothScrollingEnabled() {
      return this.mSmoothScrollingEnabled;
   }

   protected void measureChild(View var1, int var2, int var3) {
      LayoutParams var4 = var1.getLayoutParams();
      var1.measure(getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight(), var4.width), MeasureSpec.makeMeasureSpec(0, 0));
   }

   protected void measureChildWithMargins(View var1, int var2, int var3, int var4, int var5) {
      MarginLayoutParams var6 = (MarginLayoutParams)var1.getLayoutParams();
      var1.measure(getChildMeasureSpec(var2, this.getPaddingLeft() + this.getPaddingRight() + var6.leftMargin + var6.rightMargin + var3, var6.width), MeasureSpec.makeMeasureSpec(var6.topMargin + var6.bottomMargin, 0));
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mIsLaidOut = false;
   }

   public boolean onGenericMotionEvent(MotionEvent var1) {
      boolean var2;
      if ((var1.getSource() & 2) != 0) {
         switch(var1.getAction()) {
         case 8:
            if (!this.mIsBeingDragged) {
               float var3 = MotionEventCompat.getAxisValue(var1, 9);
               if (var3 != 0.0F) {
                  int var4 = (int)(this.getVerticalScrollFactorCompat() * var3);
                  int var5 = this.getScrollRange();
                  int var6 = this.getScrollY();
                  int var7 = var6 - var4;
                  if (var7 < 0) {
                     var4 = 0;
                  } else {
                     var4 = var7;
                     if (var7 > var5) {
                        var4 = var5;
                     }
                  }

                  if (var4 != var6) {
                     super.scrollTo(this.getScrollX(), var4);
                     var2 = true;
                     return var2;
                  }
               }
            }
         }
      }

      var2 = false;
      return var2;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2 = true;
      boolean var3 = false;
      int var4 = var1.getAction();
      if (var4 != 2 || !this.mIsBeingDragged) {
         switch(var4 & 255) {
         case 0:
            var4 = (int)var1.getY();
            if (!this.inChild((int)var1.getX(), var4)) {
               this.mIsBeingDragged = false;
               this.recycleVelocityTracker();
            } else {
               this.mLastMotionY = var4;
               this.mActivePointerId = var1.getPointerId(0);
               this.initOrResetVelocityTracker();
               this.mVelocityTracker.addMovement(var1);
               this.mScroller.computeScrollOffset();
               var2 = var3;
               if (!this.mScroller.isFinished()) {
                  var2 = true;
               }

               this.mIsBeingDragged = var2;
               this.startNestedScroll(2);
            }
            break;
         case 1:
         case 3:
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            this.recycleVelocityTracker();
            if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
               ViewCompat.postInvalidateOnAnimation(this);
            }

            this.stopNestedScroll();
            break;
         case 2:
            int var5 = this.mActivePointerId;
            if (var5 != -1) {
               var4 = var1.findPointerIndex(var5);
               if (var4 == -1) {
                  Log.e("NestedScrollView", "Invalid pointerId=" + var5 + " in onInterceptTouchEvent");
               } else {
                  var4 = (int)var1.getY(var4);
                  if (Math.abs(var4 - this.mLastMotionY) > this.mTouchSlop && (this.getNestedScrollAxes() & 2) == 0) {
                     this.mIsBeingDragged = true;
                     this.mLastMotionY = var4;
                     this.initVelocityTrackerIfNotExists();
                     this.mVelocityTracker.addMovement(var1);
                     this.mNestedYOffset = 0;
                     ViewParent var6 = this.getParent();
                     if (var6 != null) {
                        var6.requestDisallowInterceptTouchEvent(true);
                     }
                  }
               }
            }
         case 4:
         case 5:
         default:
            break;
         case 6:
            this.onSecondaryPointerUp(var1);
         }

         var2 = this.mIsBeingDragged;
      }

      return var2;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.mIsLayoutDirty = false;
      if (this.mChildToScrollTo != null && isViewDescendantOf(this.mChildToScrollTo, this)) {
         this.scrollToChild(this.mChildToScrollTo);
      }

      this.mChildToScrollTo = null;
      if (!this.mIsLaidOut) {
         if (this.mSavedState != null) {
            this.scrollTo(this.getScrollX(), this.mSavedState.scrollPosition);
            this.mSavedState = null;
         }

         if (this.getChildCount() > 0) {
            var2 = this.getChildAt(0).getMeasuredHeight();
         } else {
            var2 = 0;
         }

         var2 = Math.max(0, var2 - (var5 - var3 - this.getPaddingBottom() - this.getPaddingTop()));
         if (this.getScrollY() > var2) {
            this.scrollTo(this.getScrollX(), var2);
         } else if (this.getScrollY() < 0) {
            this.scrollTo(this.getScrollX(), 0);
         }
      }

      this.scrollTo(this.getScrollX(), this.getScrollY());
      this.mIsLaidOut = true;
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.mFillViewport && MeasureSpec.getMode(var2) != 0 && this.getChildCount() > 0) {
         View var3 = this.getChildAt(0);
         var2 = this.getMeasuredHeight();
         if (var3.getMeasuredHeight() < var2) {
            android.widget.FrameLayout.LayoutParams var4 = (android.widget.FrameLayout.LayoutParams)var3.getLayoutParams();
            var3.measure(getChildMeasureSpec(var1, this.getPaddingLeft() + this.getPaddingRight(), var4.width), MeasureSpec.makeMeasureSpec(var2 - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
         }
      }

   }

   public boolean onNestedFling(View var1, float var2, float var3, boolean var4) {
      if (!var4) {
         this.flingWithNestedDispatch((int)var3);
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public boolean onNestedPreFling(View var1, float var2, float var3) {
      return this.dispatchNestedPreFling(var2, var3);
   }

   public void onNestedPreScroll(View var1, int var2, int var3, int[] var4) {
      this.dispatchNestedPreScroll(var2, var3, var4, (int[])null);
   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5) {
      var2 = this.getScrollY();
      this.scrollBy(0, var5);
      var2 = this.getScrollY() - var2;
      this.dispatchNestedScroll(0, var2, 0, var5 - var2, (int[])null);
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3) {
      this.mParentHelper.onNestedScrollAccepted(var1, var2, var3);
      this.startNestedScroll(2);
   }

   protected void onOverScrolled(int var1, int var2, boolean var3, boolean var4) {
      super.scrollTo(var1, var2);
   }

   protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
      boolean var3 = false;
      int var4;
      if (var1 == 2) {
         var4 = 130;
      } else {
         var4 = var1;
         if (var1 == 1) {
            var4 = 33;
         }
      }

      View var5;
      if (var2 == null) {
         var5 = FocusFinder.getInstance().findNextFocus(this, (View)null, var4);
      } else {
         var5 = FocusFinder.getInstance().findNextFocusFromRect(this, var2, var4);
      }

      if (var5 != null && !this.isOffScreen(var5)) {
         var3 = var5.requestFocus(var4, var2);
      }

      return var3;
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof NestedScrollView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         NestedScrollView.SavedState var2 = (NestedScrollView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.mSavedState = var2;
         this.requestLayout();
      }

   }

   protected Parcelable onSaveInstanceState() {
      NestedScrollView.SavedState var1 = new NestedScrollView.SavedState(super.onSaveInstanceState());
      var1.scrollPosition = this.getScrollY();
      return var1;
   }

   protected void onScrollChanged(int var1, int var2, int var3, int var4) {
      super.onScrollChanged(var1, var2, var3, var4);
      if (this.mOnScrollChangeListener != null) {
         this.mOnScrollChangeListener.onScrollChange(this, var1, var2, var3, var4);
      }

   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      View var5 = this.findFocus();
      if (var5 != null && this != var5 && this.isWithinDeltaOfScreen(var5, 0, var4)) {
         var5.getDrawingRect(this.mTempRect);
         this.offsetDescendantRectToMyCoords(var5, this.mTempRect);
         this.doScrollY(this.computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
      }

   }

   public boolean onStartNestedScroll(View var1, View var2, int var3) {
      boolean var4;
      if ((var3 & 2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public void onStopNestedScroll(View var1) {
      this.mParentHelper.onStopNestedScroll(var1);
      this.stopNestedScroll();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.initVelocityTrackerIfNotExists();
      MotionEvent var2 = MotionEvent.obtain(var1);
      int var3 = MotionEventCompat.getActionMasked(var1);
      if (var3 == 0) {
         this.mNestedYOffset = 0;
      }

      var2.offsetLocation(0.0F, (float)this.mNestedYOffset);
      boolean var4;
      ViewParent var5;
      switch(var3) {
      case 0:
         if (this.getChildCount() == 0) {
            var4 = false;
            return var4;
         }

         if (!this.mScroller.isFinished()) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.mIsBeingDragged = var4;
         if (var4) {
            var5 = this.getParent();
            if (var5 != null) {
               var5.requestDisallowInterceptTouchEvent(true);
            }
         }

         if (!this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
         }

         this.mLastMotionY = (int)var1.getY();
         this.mActivePointerId = var1.getPointerId(0);
         this.startNestedScroll(2);
         break;
      case 1:
         if (this.mIsBeingDragged) {
            VelocityTracker var11 = this.mVelocityTracker;
            var11.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            var3 = (int)VelocityTrackerCompat.getYVelocity(var11, this.mActivePointerId);
            if (Math.abs(var3) > this.mMinimumVelocity) {
               this.flingWithNestedDispatch(-var3);
            } else if (this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
               ViewCompat.postInvalidateOnAnimation(this);
            }
         }

         this.mActivePointerId = -1;
         this.endDrag();
         break;
      case 2:
         int var6 = var1.findPointerIndex(this.mActivePointerId);
         if (var6 == -1) {
            Log.e("NestedScrollView", "Invalid pointerId=" + this.mActivePointerId + " in onTouchEvent");
         } else {
            int var7 = (int)var1.getY(var6);
            var3 = this.mLastMotionY - var7;
            int var8 = var3;
            if (this.dispatchNestedPreScroll(0, var3, this.mScrollConsumed, this.mScrollOffset)) {
               var8 = var3 - this.mScrollConsumed[1];
               var2.offsetLocation(0.0F, (float)this.mScrollOffset[1]);
               this.mNestedYOffset += this.mScrollOffset[1];
            }

            var3 = var8;
            if (!this.mIsBeingDragged) {
               var3 = var8;
               if (Math.abs(var8) > this.mTouchSlop) {
                  var5 = this.getParent();
                  if (var5 != null) {
                     var5.requestDisallowInterceptTouchEvent(true);
                  }

                  this.mIsBeingDragged = true;
                  if (var8 > 0) {
                     var3 = var8 - this.mTouchSlop;
                  } else {
                     var3 = var8 + this.mTouchSlop;
                  }
               }
            }

            if (this.mIsBeingDragged) {
               this.mLastMotionY = var7 - this.mScrollOffset[1];
               int var9 = this.getScrollY();
               var7 = this.getScrollRange();
               var8 = this.getOverScrollMode();
               boolean var12;
               if (var8 != 0 && (var8 != 1 || var7 <= 0)) {
                  var12 = false;
               } else {
                  var12 = true;
               }

               if (this.overScrollByCompat(0, var3, 0, this.getScrollY(), 0, var7, 0, 0, true) && !this.hasNestedScrollingParent()) {
                  this.mVelocityTracker.clear();
               }

               int var10 = this.getScrollY() - var9;
               if (this.dispatchNestedScroll(0, var10, 0, var3 - var10, this.mScrollOffset)) {
                  this.mLastMotionY -= this.mScrollOffset[1];
                  var2.offsetLocation(0.0F, (float)this.mScrollOffset[1]);
                  this.mNestedYOffset += this.mScrollOffset[1];
               } else if (var12) {
                  this.ensureGlows();
                  var8 = var9 + var3;
                  if (var8 < 0) {
                     this.mEdgeGlowTop.onPull((float)var3 / (float)this.getHeight(), var1.getX(var6) / (float)this.getWidth());
                     if (!this.mEdgeGlowBottom.isFinished()) {
                        this.mEdgeGlowBottom.onRelease();
                     }
                  } else if (var8 > var7) {
                     this.mEdgeGlowBottom.onPull((float)var3 / (float)this.getHeight(), 1.0F - var1.getX(var6) / (float)this.getWidth());
                     if (!this.mEdgeGlowTop.isFinished()) {
                        this.mEdgeGlowTop.onRelease();
                     }
                  }

                  if (this.mEdgeGlowTop != null && (!this.mEdgeGlowTop.isFinished() || !this.mEdgeGlowBottom.isFinished())) {
                     ViewCompat.postInvalidateOnAnimation(this);
                  }
               }
            }
         }
         break;
      case 3:
         if (this.mIsBeingDragged && this.getChildCount() > 0 && this.mScroller.springBack(this.getScrollX(), this.getScrollY(), 0, 0, 0, this.getScrollRange())) {
            ViewCompat.postInvalidateOnAnimation(this);
         }

         this.mActivePointerId = -1;
         this.endDrag();
      case 4:
      default:
         break;
      case 5:
         var3 = MotionEventCompat.getActionIndex(var1);
         this.mLastMotionY = (int)var1.getY(var3);
         this.mActivePointerId = var1.getPointerId(var3);
         break;
      case 6:
         this.onSecondaryPointerUp(var1);
         this.mLastMotionY = (int)var1.getY(var1.findPointerIndex(this.mActivePointerId));
      }

      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.addMovement(var2);
      }

      var2.recycle();
      var4 = true;
      return var4;
   }

   boolean overScrollByCompat(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
      int var10 = this.getOverScrollMode();
      boolean var11;
      if (this.computeHorizontalScrollRange() > this.computeHorizontalScrollExtent()) {
         var11 = true;
      } else {
         var11 = false;
      }

      boolean var12;
      if (this.computeVerticalScrollRange() > this.computeVerticalScrollExtent()) {
         var12 = true;
      } else {
         var12 = false;
      }

      if (var10 != 0 && (var10 != 1 || !var11)) {
         var11 = false;
      } else {
         var11 = true;
      }

      if (var10 != 0 && (var10 != 1 || !var12)) {
         var12 = false;
      } else {
         var12 = true;
      }

      var3 += var1;
      if (!var11) {
         var7 = 0;
      }

      var4 += var2;
      if (!var12) {
         var8 = 0;
      }

      var2 = -var7;
      var1 = var7 + var5;
      var5 = -var8;
      var6 += var8;
      var9 = false;
      if (var3 > var1) {
         var9 = true;
      } else {
         var1 = var3;
         if (var3 < var2) {
            var1 = var2;
            var9 = true;
         }
      }

      boolean var13 = false;
      if (var4 > var6) {
         var2 = var6;
         var13 = true;
      } else {
         var2 = var4;
         if (var4 < var5) {
            var2 = var5;
            var13 = true;
         }
      }

      if (var13) {
         this.mScroller.springBack(var1, var2, 0, 0, 0, this.getScrollRange());
      }

      this.onOverScrolled(var1, var2, var9, var13);
      if (!var9 && !var13) {
         var9 = false;
      } else {
         var9 = true;
      }

      return var9;
   }

   public boolean pageScroll(int var1) {
      boolean var2;
      if (var1 == 130) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var3 = this.getHeight();
      if (var2) {
         this.mTempRect.top = this.getScrollY() + var3;
         int var5 = this.getChildCount();
         if (var5 > 0) {
            View var4 = this.getChildAt(var5 - 1);
            if (this.mTempRect.top + var3 > var4.getBottom()) {
               this.mTempRect.top = var4.getBottom() - var3;
            }
         }
      } else {
         this.mTempRect.top = this.getScrollY() - var3;
         if (this.mTempRect.top < 0) {
            this.mTempRect.top = 0;
         }
      }

      this.mTempRect.bottom = this.mTempRect.top + var3;
      return this.scrollAndFocus(var1, this.mTempRect.top, this.mTempRect.bottom);
   }

   public void requestChildFocus(View var1, View var2) {
      if (!this.mIsLayoutDirty) {
         this.scrollToChild(var2);
      } else {
         this.mChildToScrollTo = var2;
      }

      super.requestChildFocus(var1, var2);
   }

   public boolean requestChildRectangleOnScreen(View var1, Rect var2, boolean var3) {
      var2.offset(var1.getLeft() - var1.getScrollX(), var1.getTop() - var1.getScrollY());
      return this.scrollToChildRect(var2, var3);
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      if (var1) {
         this.recycleVelocityTracker();
      }

      super.requestDisallowInterceptTouchEvent(var1);
   }

   public void requestLayout() {
      this.mIsLayoutDirty = true;
      super.requestLayout();
   }

   public void scrollTo(int var1, int var2) {
      if (this.getChildCount() > 0) {
         View var3 = this.getChildAt(0);
         var1 = clamp(var1, this.getWidth() - this.getPaddingRight() - this.getPaddingLeft(), var3.getWidth());
         var2 = clamp(var2, this.getHeight() - this.getPaddingBottom() - this.getPaddingTop(), var3.getHeight());
         if (var1 != this.getScrollX() || var2 != this.getScrollY()) {
            super.scrollTo(var1, var2);
         }
      }

   }

   public void setFillViewport(boolean var1) {
      if (var1 != this.mFillViewport) {
         this.mFillViewport = var1;
         this.requestLayout();
      }

   }

   public void setNestedScrollingEnabled(boolean var1) {
      this.mChildHelper.setNestedScrollingEnabled(var1);
   }

   public void setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener var1) {
      this.mOnScrollChangeListener = var1;
   }

   public void setSmoothScrollingEnabled(boolean var1) {
      this.mSmoothScrollingEnabled = var1;
   }

   public boolean shouldDelayChildPressedState() {
      return true;
   }

   public final void smoothScrollBy(int var1, int var2) {
      if (this.getChildCount() != 0) {
         if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250L) {
            var1 = this.getHeight();
            int var3 = this.getPaddingBottom();
            int var4 = this.getPaddingTop();
            var3 = Math.max(0, this.getChildAt(0).getHeight() - (var1 - var3 - var4));
            var1 = this.getScrollY();
            var2 = Math.max(0, Math.min(var1 + var2, var3));
            this.mScroller.startScroll(this.getScrollX(), var1, 0, var2 - var1);
            ViewCompat.postInvalidateOnAnimation(this);
         } else {
            if (!this.mScroller.isFinished()) {
               this.mScroller.abortAnimation();
            }

            this.scrollBy(var1, var2);
         }

         this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
      }

   }

   public final void smoothScrollTo(int var1, int var2) {
      this.smoothScrollBy(var1 - this.getScrollX(), var2 - this.getScrollY());
   }

   public boolean startNestedScroll(int var1) {
      return this.mChildHelper.startNestedScroll(var1);
   }

   public void stopNestedScroll() {
      this.mChildHelper.stopNestedScroll();
   }

   static class AccessibilityDelegate extends AccessibilityDelegateCompat {
      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         NestedScrollView var4 = (NestedScrollView)var1;
         var2.setClassName(ScrollView.class.getName());
         AccessibilityRecordCompat var5 = AccessibilityEventCompat.asRecord(var2);
         boolean var3;
         if (var4.getScrollRange() > 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var5.setScrollable(var3);
         var5.setScrollX(var4.getScrollX());
         var5.setScrollY(var4.getScrollY());
         var5.setMaxScrollX(var4.getScrollX());
         var5.setMaxScrollY(var4.getScrollRange());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         NestedScrollView var4 = (NestedScrollView)var1;
         var2.setClassName(ScrollView.class.getName());
         if (var4.isEnabled()) {
            int var3 = var4.getScrollRange();
            if (var3 > 0) {
               var2.setScrollable(true);
               if (var4.getScrollY() > 0) {
                  var2.addAction(8192);
               }

               if (var4.getScrollY() < var3) {
                  var2.addAction(4096);
               }
            }
         }

      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         boolean var4 = true;
         if (!super.performAccessibilityAction(var1, var2, var3)) {
            NestedScrollView var7 = (NestedScrollView)var1;
            if (!var7.isEnabled()) {
               var4 = false;
            } else {
               int var5;
               int var6;
               switch(var2) {
               case 4096:
                  var5 = var7.getHeight();
                  var2 = var7.getPaddingBottom();
                  var6 = var7.getPaddingTop();
                  var2 = Math.min(var7.getScrollY() + (var5 - var2 - var6), var7.getScrollRange());
                  if (var2 != var7.getScrollY()) {
                     var7.smoothScrollTo(0, var2);
                  } else {
                     var4 = false;
                  }
                  break;
               case 8192:
                  var2 = var7.getHeight();
                  var6 = var7.getPaddingBottom();
                  var5 = var7.getPaddingTop();
                  var2 = Math.max(var7.getScrollY() - (var2 - var6 - var5), 0);
                  if (var2 != var7.getScrollY()) {
                     var7.smoothScrollTo(0, var2);
                  } else {
                     var4 = false;
                  }
                  break;
               default:
                  var4 = false;
               }
            }
         }

         return var4;
      }
   }

   public interface OnScrollChangeListener {
      void onScrollChange(NestedScrollView var1, int var2, int var3, int var4, int var5);
   }

   static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public NestedScrollView.SavedState createFromParcel(Parcel var1) {
            return new NestedScrollView.SavedState(var1);
         }

         public NestedScrollView.SavedState[] newArray(int var1) {
            return new NestedScrollView.SavedState[var1];
         }
      };
      public int scrollPosition;

      SavedState(Parcel var1) {
         super(var1);
         this.scrollPosition = var1.readInt();
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         return "HorizontalScrollView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " scrollPosition=" + this.scrollPosition + "}";
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.scrollPosition);
      }
   }
}
