package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends ViewGroup implements DrawerLayoutImpl {
   private static final boolean ALLOW_EDGE_LOCK = false;
   static final boolean CAN_HIDE_DESCENDANTS;
   private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
   private static final int DEFAULT_SCRIM_COLOR = -1728053248;
   private static final int DRAWER_ELEVATION = 10;
   static final DrawerLayout.DrawerLayoutCompatImpl IMPL;
   static final int[] LAYOUT_ATTRS;
   public static final int LOCK_MODE_LOCKED_CLOSED = 1;
   public static final int LOCK_MODE_LOCKED_OPEN = 2;
   public static final int LOCK_MODE_UNDEFINED = 3;
   public static final int LOCK_MODE_UNLOCKED = 0;
   private static final int MIN_DRAWER_MARGIN = 64;
   private static final int MIN_FLING_VELOCITY = 400;
   private static final int PEEK_DELAY = 160;
   private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
   public static final int STATE_DRAGGING = 1;
   public static final int STATE_IDLE = 0;
   public static final int STATE_SETTLING = 2;
   private static final String TAG = "DrawerLayout";
   private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;
   private final DrawerLayout.ChildAccessibilityDelegate mChildAccessibilityDelegate;
   private boolean mChildrenCanceledTouch;
   private boolean mDisallowInterceptRequested;
   private boolean mDrawStatusBarBackground;
   private float mDrawerElevation;
   private int mDrawerState;
   private boolean mFirstLayout;
   private boolean mInLayout;
   private float mInitialMotionX;
   private float mInitialMotionY;
   private Object mLastInsets;
   private final DrawerLayout.ViewDragCallback mLeftCallback;
   private final ViewDragHelper mLeftDragger;
   @Nullable
   private DrawerLayout.DrawerListener mListener;
   private List mListeners;
   private int mLockModeEnd;
   private int mLockModeLeft;
   private int mLockModeRight;
   private int mLockModeStart;
   private int mMinDrawerMargin;
   private final ArrayList mNonDrawerViews;
   private final DrawerLayout.ViewDragCallback mRightCallback;
   private final ViewDragHelper mRightDragger;
   private int mScrimColor;
   private float mScrimOpacity;
   private Paint mScrimPaint;
   private Drawable mShadowEnd;
   private Drawable mShadowLeft;
   private Drawable mShadowLeftResolved;
   private Drawable mShadowRight;
   private Drawable mShadowRightResolved;
   private Drawable mShadowStart;
   private Drawable mStatusBarBackground;
   private CharSequence mTitleLeft;
   private CharSequence mTitleRight;

   static {
      boolean var0 = true;
      LAYOUT_ATTRS = new int[]{16842931};
      boolean var1;
      if (VERSION.SDK_INT >= 19) {
         var1 = true;
      } else {
         var1 = false;
      }

      CAN_HIDE_DESCENDANTS = var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = var0;
      } else {
         var1 = false;
      }

      SET_DRAWER_SHADOW_FROM_ELEVATION = var1;
      if (VERSION.SDK_INT >= 21) {
         IMPL = new DrawerLayout.DrawerLayoutCompatImplApi21();
      } else {
         IMPL = new DrawerLayout.DrawerLayoutCompatImplBase();
      }

   }

   public DrawerLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public DrawerLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public DrawerLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mChildAccessibilityDelegate = new DrawerLayout.ChildAccessibilityDelegate();
      this.mScrimColor = -1728053248;
      this.mScrimPaint = new Paint();
      this.mFirstLayout = true;
      this.mLockModeLeft = 3;
      this.mLockModeRight = 3;
      this.mLockModeStart = 3;
      this.mLockModeEnd = 3;
      this.mShadowStart = null;
      this.mShadowEnd = null;
      this.mShadowLeft = null;
      this.mShadowRight = null;
      this.setDescendantFocusability(262144);
      float var4 = this.getResources().getDisplayMetrics().density;
      this.mMinDrawerMargin = (int)(64.0F * var4 + 0.5F);
      float var5 = 400.0F * var4;
      this.mLeftCallback = new DrawerLayout.ViewDragCallback(3);
      this.mRightCallback = new DrawerLayout.ViewDragCallback(5);
      this.mLeftDragger = ViewDragHelper.create(this, 1.0F, this.mLeftCallback);
      this.mLeftDragger.setEdgeTrackingEnabled(1);
      this.mLeftDragger.setMinVelocity(var5);
      this.mLeftCallback.setDragger(this.mLeftDragger);
      this.mRightDragger = ViewDragHelper.create(this, 1.0F, this.mRightCallback);
      this.mRightDragger.setEdgeTrackingEnabled(2);
      this.mRightDragger.setMinVelocity(var5);
      this.mRightCallback.setDragger(this.mRightDragger);
      this.setFocusableInTouchMode(true);
      ViewCompat.setImportantForAccessibility(this, 1);
      ViewCompat.setAccessibilityDelegate(this, new DrawerLayout.AccessibilityDelegate());
      ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
      if (ViewCompat.getFitsSystemWindows(this)) {
         IMPL.configureApplyInsets(this);
         this.mStatusBarBackground = IMPL.getDefaultStatusBarBackground(var1);
      }

      this.mDrawerElevation = 10.0F * var4;
      this.mNonDrawerViews = new ArrayList();
   }

   static String gravityToString(int var0) {
      String var1;
      if ((var0 & 3) == 3) {
         var1 = "LEFT";
      } else if ((var0 & 5) == 5) {
         var1 = "RIGHT";
      } else {
         var1 = Integer.toHexString(var0);
      }

      return var1;
   }

   private static boolean hasOpaqueBackground(View var0) {
      boolean var1 = false;
      Drawable var3 = var0.getBackground();
      boolean var2 = var1;
      if (var3 != null) {
         var2 = var1;
         if (var3.getOpacity() == -1) {
            var2 = true;
         }
      }

      return var2;
   }

   private boolean hasPeekingDrawer() {
      int var1 = this.getChildCount();
      int var2 = 0;

      boolean var3;
      while(true) {
         if (var2 >= var1) {
            var3 = false;
            break;
         }

         if (((DrawerLayout.LayoutParams)this.getChildAt(var2).getLayoutParams()).isPeeking) {
            var3 = true;
            break;
         }

         ++var2;
      }

      return var3;
   }

   private boolean hasVisibleDrawer() {
      boolean var1;
      if (this.findVisibleDrawer() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   static boolean includeChildForAccessibility(View var0) {
      boolean var1;
      if (ViewCompat.getImportantForAccessibility(var0) != 4 && ViewCompat.getImportantForAccessibility(var0) != 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean mirror(Drawable var1, int var2) {
      boolean var3;
      if (var1 != null && DrawableCompat.isAutoMirrored(var1)) {
         DrawableCompat.setLayoutDirection(var1, var2);
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private Drawable resolveLeftShadow() {
      int var1 = ViewCompat.getLayoutDirection(this);
      Drawable var2;
      if (var1 == 0) {
         if (this.mShadowStart != null) {
            this.mirror(this.mShadowStart, var1);
            var2 = this.mShadowStart;
            return var2;
         }
      } else if (this.mShadowEnd != null) {
         this.mirror(this.mShadowEnd, var1);
         var2 = this.mShadowEnd;
         return var2;
      }

      var2 = this.mShadowLeft;
      return var2;
   }

   private Drawable resolveRightShadow() {
      int var1 = ViewCompat.getLayoutDirection(this);
      Drawable var2;
      if (var1 == 0) {
         if (this.mShadowEnd != null) {
            this.mirror(this.mShadowEnd, var1);
            var2 = this.mShadowEnd;
            return var2;
         }
      } else if (this.mShadowStart != null) {
         this.mirror(this.mShadowStart, var1);
         var2 = this.mShadowStart;
         return var2;
      }

      var2 = this.mShadowRight;
      return var2;
   }

   private void resolveShadowDrawables() {
      if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
         this.mShadowLeftResolved = this.resolveLeftShadow();
         this.mShadowRightResolved = this.resolveRightShadow();
      }

   }

   private void updateChildrenImportantForAccessibility(View var1, boolean var2) {
      int var3 = this.getChildCount();

      for(int var4 = 0; var4 < var3; ++var4) {
         View var5 = this.getChildAt(var4);
         if ((var2 || this.isDrawerView(var5)) && (!var2 || var5 != var1)) {
            ViewCompat.setImportantForAccessibility(var5, 4);
         } else {
            ViewCompat.setImportantForAccessibility(var5, 1);
         }
      }

   }

   public void addDrawerListener(@NonNull DrawerLayout.DrawerListener var1) {
      if (var1 != null) {
         if (this.mListeners == null) {
            this.mListeners = new ArrayList();
         }

         this.mListeners.add(var1);
      }

   }

   public void addFocusables(ArrayList var1, int var2, int var3) {
      if (this.getDescendantFocusability() != 393216) {
         int var4 = this.getChildCount();
         boolean var5 = false;

         int var6;
         View var7;
         for(var6 = 0; var6 < var4; ++var6) {
            var7 = this.getChildAt(var6);
            if (this.isDrawerView(var7)) {
               if (this.isDrawerOpen(var7)) {
                  var5 = true;
                  var7.addFocusables(var1, var2, var3);
               }
            } else {
               this.mNonDrawerViews.add(var7);
            }
         }

         if (!var5) {
            int var8 = this.mNonDrawerViews.size();

            for(var6 = 0; var6 < var8; ++var6) {
               var7 = (View)this.mNonDrawerViews.get(var6);
               if (var7.getVisibility() == 0) {
                  var7.addFocusables(var1, var2, var3);
               }
            }
         }

         this.mNonDrawerViews.clear();
      }

   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      super.addView(var1, var2, var3);
      if (this.findOpenDrawer() == null && !this.isDrawerView(var1)) {
         ViewCompat.setImportantForAccessibility(var1, 1);
      } else {
         ViewCompat.setImportantForAccessibility(var1, 4);
      }

      if (!CAN_HIDE_DESCENDANTS) {
         ViewCompat.setAccessibilityDelegate(var1, this.mChildAccessibilityDelegate);
      }

   }

   void cancelChildViewTouch() {
      if (!this.mChildrenCanceledTouch) {
         long var1 = SystemClock.uptimeMillis();
         MotionEvent var3 = MotionEvent.obtain(var1, var1, 3, 0.0F, 0.0F, 0);
         int var4 = this.getChildCount();

         for(int var5 = 0; var5 < var4; ++var5) {
            this.getChildAt(var5).dispatchTouchEvent(var3);
         }

         var3.recycle();
         this.mChildrenCanceledTouch = true;
      }

   }

   boolean checkDrawerViewAbsoluteGravity(View var1, int var2) {
      boolean var3;
      if ((this.getDrawerViewAbsoluteGravity(var1) & var2) == var2) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 instanceof DrawerLayout.LayoutParams && super.checkLayoutParams(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void closeDrawer(int var1) {
      this.closeDrawer(var1, true);
   }

   public void closeDrawer(int var1, boolean var2) {
      View var3 = this.findDrawerWithGravity(var1);
      if (var3 == null) {
         throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(var1));
      } else {
         this.closeDrawer(var3, var2);
      }
   }

   public void closeDrawer(View var1) {
      this.closeDrawer(var1, true);
   }

   public void closeDrawer(View var1, boolean var2) {
      if (!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a sliding drawer");
      } else {
         DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
         if (this.mFirstLayout) {
            var3.onScreen = 0.0F;
            var3.openState = 0;
         } else if (var2) {
            var3.openState |= 4;
            if (this.checkDrawerViewAbsoluteGravity(var1, 3)) {
               this.mLeftDragger.smoothSlideViewTo(var1, -var1.getWidth(), var1.getTop());
            } else {
               this.mRightDragger.smoothSlideViewTo(var1, this.getWidth(), var1.getTop());
            }
         } else {
            this.moveDrawerToOffset(var1, 0.0F);
            this.updateDrawerState(var3.gravity, 0, var1);
            var1.setVisibility(4);
         }

         this.invalidate();
      }
   }

   public void closeDrawers() {
      this.closeDrawers(false);
   }

   void closeDrawers(boolean var1) {
      boolean var2 = false;
      int var3 = this.getChildCount();

      boolean var7;
      for(int var4 = 0; var4 < var3; var2 = var7) {
         View var5 = this.getChildAt(var4);
         DrawerLayout.LayoutParams var6 = (DrawerLayout.LayoutParams)var5.getLayoutParams();
         var7 = var2;
         if (this.isDrawerView(var5)) {
            if (var1 && !var6.isPeeking) {
               var7 = var2;
            } else {
               int var8 = var5.getWidth();
               if (this.checkDrawerViewAbsoluteGravity(var5, 3)) {
                  var2 |= this.mLeftDragger.smoothSlideViewTo(var5, -var8, var5.getTop());
               } else {
                  var2 |= this.mRightDragger.smoothSlideViewTo(var5, this.getWidth(), var5.getTop());
               }

               var6.isPeeking = false;
               var7 = var2;
            }
         }

         ++var4;
      }

      this.mLeftCallback.removeCallbacks();
      this.mRightCallback.removeCallbacks();
      if (var2) {
         this.invalidate();
      }

   }

   public void computeScroll() {
      int var1 = this.getChildCount();
      float var2 = 0.0F;

      for(int var3 = 0; var3 < var1; ++var3) {
         var2 = Math.max(var2, ((DrawerLayout.LayoutParams)this.getChildAt(var3).getLayoutParams()).onScreen);
      }

      this.mScrimOpacity = var2;
      if (this.mLeftDragger.continueSettling(true) | this.mRightDragger.continueSettling(true)) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   void dispatchOnDrawerClosed(View var1) {
      DrawerLayout.LayoutParams var2 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if ((var2.openState & 1) == 1) {
         var2.openState = 0;
         if (this.mListeners != null) {
            for(int var3 = this.mListeners.size() - 1; var3 >= 0; --var3) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var3)).onDrawerClosed(var1);
            }
         }

         this.updateChildrenImportantForAccessibility(var1, false);
         if (this.hasWindowFocus()) {
            var1 = this.getRootView();
            if (var1 != null) {
               var1.sendAccessibilityEvent(32);
            }
         }
      }

   }

   void dispatchOnDrawerOpened(View var1) {
      DrawerLayout.LayoutParams var2 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if ((var2.openState & 1) == 0) {
         var2.openState = 1;
         if (this.mListeners != null) {
            for(int var3 = this.mListeners.size() - 1; var3 >= 0; --var3) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var3)).onDrawerOpened(var1);
            }
         }

         this.updateChildrenImportantForAccessibility(var1, true);
         if (this.hasWindowFocus()) {
            this.sendAccessibilityEvent(32);
         }
      }

   }

   void dispatchOnDrawerSlide(View var1, float var2) {
      if (this.mListeners != null) {
         for(int var3 = this.mListeners.size() - 1; var3 >= 0; --var3) {
            ((DrawerLayout.DrawerListener)this.mListeners.get(var3)).onDrawerSlide(var1, var2);
         }
      }

   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      int var5 = this.getHeight();
      boolean var6 = this.isContentView(var2);
      int var7 = 0;
      byte var8 = 0;
      int var9 = this.getWidth();
      int var10 = var1.save();
      int var11 = var9;
      int var18;
      if (var6) {
         int var12 = this.getChildCount();
         var11 = 0;

         int var14;
         for(var7 = var8; var11 < var12; var9 = var14) {
            View var13 = this.getChildAt(var11);
            var18 = var7;
            var14 = var9;
            if (var13 != var2) {
               var18 = var7;
               var14 = var9;
               if (var13.getVisibility() == 0) {
                  var18 = var7;
                  var14 = var9;
                  if (hasOpaqueBackground(var13)) {
                     var18 = var7;
                     var14 = var9;
                     if (this.isDrawerView(var13)) {
                        if (var13.getHeight() < var5) {
                           var14 = var9;
                           var18 = var7;
                        } else {
                           int var15;
                           if (this.checkDrawerViewAbsoluteGravity(var13, 3)) {
                              var15 = var13.getRight();
                              var18 = var7;
                              var14 = var9;
                              if (var15 > var7) {
                                 var18 = var15;
                                 var14 = var9;
                              }
                           } else {
                              var15 = var13.getLeft();
                              var18 = var7;
                              var14 = var9;
                              if (var15 < var9) {
                                 var14 = var15;
                                 var18 = var7;
                              }
                           }
                        }
                     }
                  }
               }
            }

            ++var11;
            var7 = var18;
         }

         var1.clipRect(var7, 0, var9, this.getHeight());
         var11 = var9;
      }

      boolean var16 = super.drawChild(var1, var2, var3);
      var1.restoreToCount(var10);
      if (this.mScrimOpacity > 0.0F && var6) {
         var9 = (int)((float)((this.mScrimColor & -16777216) >>> 24) * this.mScrimOpacity);
         var18 = this.mScrimColor;
         this.mScrimPaint.setColor(var9 << 24 | var18 & 16777215);
         var1.drawRect((float)var7, 0.0F, (float)var11, (float)this.getHeight(), this.mScrimPaint);
      } else {
         float var17;
         if (this.mShadowLeftResolved != null && this.checkDrawerViewAbsoluteGravity(var2, 3)) {
            var11 = this.mShadowLeftResolved.getIntrinsicWidth();
            var9 = var2.getRight();
            var7 = this.mLeftDragger.getEdgeSize();
            var17 = Math.max(0.0F, Math.min((float)var9 / (float)var7, 1.0F));
            this.mShadowLeftResolved.setBounds(var9, var2.getTop(), var9 + var11, var2.getBottom());
            this.mShadowLeftResolved.setAlpha((int)(255.0F * var17));
            this.mShadowLeftResolved.draw(var1);
         } else if (this.mShadowRightResolved != null && this.checkDrawerViewAbsoluteGravity(var2, 5)) {
            var9 = this.mShadowRightResolved.getIntrinsicWidth();
            var7 = var2.getLeft();
            var11 = this.getWidth();
            var18 = this.mRightDragger.getEdgeSize();
            var17 = Math.max(0.0F, Math.min((float)(var11 - var7) / (float)var18, 1.0F));
            this.mShadowRightResolved.setBounds(var7 - var9, var2.getTop(), var7, var2.getBottom());
            this.mShadowRightResolved.setAlpha((int)(255.0F * var17));
            this.mShadowRightResolved.draw(var1);
         }
      }

      return var16;
   }

   View findDrawerWithGravity(int var1) {
      int var2 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      int var3 = this.getChildCount();
      var1 = 0;

      View var4;
      while(true) {
         if (var1 >= var3) {
            var4 = null;
            break;
         }

         var4 = this.getChildAt(var1);
         if ((this.getDrawerViewAbsoluteGravity(var4) & 7) == (var2 & 7)) {
            break;
         }

         ++var1;
      }

      return var4;
   }

   View findOpenDrawer() {
      int var1 = this.getChildCount();
      int var2 = 0;

      View var3;
      while(true) {
         if (var2 >= var1) {
            var3 = null;
            break;
         }

         var3 = this.getChildAt(var2);
         if ((((DrawerLayout.LayoutParams)var3.getLayoutParams()).openState & 1) == 1) {
            break;
         }

         ++var2;
      }

      return var3;
   }

   View findVisibleDrawer() {
      int var1 = this.getChildCount();
      int var2 = 0;

      View var3;
      while(true) {
         if (var2 >= var1) {
            var3 = null;
            break;
         }

         var3 = this.getChildAt(var2);
         if (this.isDrawerView(var3) && this.isDrawerVisible(var3)) {
            break;
         }

         ++var2;
      }

      return var3;
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new DrawerLayout.LayoutParams(-1, -1);
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new DrawerLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      DrawerLayout.LayoutParams var2;
      if (var1 instanceof DrawerLayout.LayoutParams) {
         var2 = new DrawerLayout.LayoutParams((DrawerLayout.LayoutParams)var1);
      } else if (var1 instanceof MarginLayoutParams) {
         var2 = new DrawerLayout.LayoutParams((MarginLayoutParams)var1);
      } else {
         var2 = new DrawerLayout.LayoutParams(var1);
      }

      return var2;
   }

   public float getDrawerElevation() {
      float var1;
      if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
         var1 = this.mDrawerElevation;
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public int getDrawerLockMode(int var1) {
      int var2 = ViewCompat.getLayoutDirection(this);
      switch(var1) {
      case 3:
         if (this.mLockModeLeft != 3) {
            var1 = this.mLockModeLeft;
            return var1;
         }

         if (var2 == 0) {
            var1 = this.mLockModeStart;
         } else {
            var1 = this.mLockModeEnd;
         }

         if (var1 != 3) {
            return var1;
         }
         break;
      case 5:
         if (this.mLockModeRight != 3) {
            var1 = this.mLockModeRight;
            return var1;
         }

         if (var2 == 0) {
            var1 = this.mLockModeEnd;
         } else {
            var1 = this.mLockModeStart;
         }

         if (var1 != 3) {
            return var1;
         }
         break;
      case 8388611:
         if (this.mLockModeStart != 3) {
            var1 = this.mLockModeStart;
            return var1;
         }

         if (var2 == 0) {
            var1 = this.mLockModeLeft;
         } else {
            var1 = this.mLockModeRight;
         }

         if (var1 != 3) {
            return var1;
         }
         break;
      case 8388613:
         if (this.mLockModeEnd != 3) {
            var1 = this.mLockModeEnd;
            return var1;
         }

         if (var2 == 0) {
            var1 = this.mLockModeRight;
         } else {
            var1 = this.mLockModeLeft;
         }

         if (var1 != 3) {
            return var1;
         }
      }

      var1 = 0;
      return var1;
   }

   public int getDrawerLockMode(View var1) {
      if (!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a drawer");
      } else {
         return this.getDrawerLockMode(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity);
      }
   }

   @Nullable
   public CharSequence getDrawerTitle(int var1) {
      var1 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      CharSequence var2;
      if (var1 == 3) {
         var2 = this.mTitleLeft;
      } else if (var1 == 5) {
         var2 = this.mTitleRight;
      } else {
         var2 = null;
      }

      return var2;
   }

   int getDrawerViewAbsoluteGravity(View var1) {
      return GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
   }

   float getDrawerViewOffset(View var1) {
      return ((DrawerLayout.LayoutParams)var1.getLayoutParams()).onScreen;
   }

   public Drawable getStatusBarBackgroundDrawable() {
      return this.mStatusBarBackground;
   }

   boolean isContentView(View var1) {
      boolean var2;
      if (((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isDrawerOpen(int var1) {
      View var2 = this.findDrawerWithGravity(var1);
      boolean var3;
      if (var2 != null) {
         var3 = this.isDrawerOpen(var2);
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isDrawerOpen(View var1) {
      boolean var2 = true;
      if (!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a drawer");
      } else {
         if ((((DrawerLayout.LayoutParams)var1.getLayoutParams()).openState & 1) != 1) {
            var2 = false;
         }

         return var2;
      }
   }

   boolean isDrawerView(View var1) {
      int var2 = GravityCompat.getAbsoluteGravity(((DrawerLayout.LayoutParams)var1.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(var1));
      boolean var3;
      if ((var2 & 3) != 0) {
         var3 = true;
      } else if ((var2 & 5) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isDrawerVisible(int var1) {
      View var2 = this.findDrawerWithGravity(var1);
      boolean var3;
      if (var2 != null) {
         var3 = this.isDrawerVisible(var2);
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isDrawerVisible(View var1) {
      if (!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a drawer");
      } else {
         boolean var2;
         if (((DrawerLayout.LayoutParams)var1.getLayoutParams()).onScreen > 0.0F) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   void moveDrawerToOffset(View var1, float var2) {
      float var3 = this.getDrawerViewOffset(var1);
      int var4 = var1.getWidth();
      int var5 = (int)((float)var4 * var3);
      var5 = (int)((float)var4 * var2) - var5;
      if (!this.checkDrawerViewAbsoluteGravity(var1, 3)) {
         var5 = -var5;
      }

      var1.offsetLeftAndRight(var5);
      this.setDrawerViewOffset(var1, var2);
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mFirstLayout = true;
   }

   public void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
         int var2 = IMPL.getTopInset(this.mLastInsets);
         if (var2 > 0) {
            this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), var2);
            this.mStatusBarBackground.draw(var1);
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2 = false;
      int var3 = MotionEventCompat.getActionMasked(var1);
      boolean var4 = this.mLeftDragger.shouldInterceptTouchEvent(var1);
      boolean var5 = this.mRightDragger.shouldInterceptTouchEvent(var1);
      boolean var6 = false;
      boolean var7 = false;
      boolean var11;
      switch(var3) {
      case 0:
         float var8 = var1.getX();
         float var9 = var1.getY();
         this.mInitialMotionX = var8;
         this.mInitialMotionY = var9;
         var11 = var6;
         if (this.mScrimOpacity > 0.0F) {
            View var10 = this.mLeftDragger.findTopChildUnder((int)var8, (int)var9);
            var11 = var6;
            if (var10 != null) {
               var11 = var6;
               if (this.isContentView(var10)) {
                  var11 = true;
               }
            }
         }

         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
         break;
      case 1:
      case 3:
         this.closeDrawers(true);
         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
         var11 = var7;
         break;
      case 2:
         var11 = var7;
         if (this.mLeftDragger.checkTouchSlop(3)) {
            this.mLeftCallback.removeCallbacks();
            this.mRightCallback.removeCallbacks();
            var11 = var7;
         }
         break;
      default:
         var11 = var7;
      }

      if (var4 | var5 || var11 || this.hasPeekingDrawer() || this.mChildrenCanceledTouch) {
         var2 = true;
      }

      return var2;
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3;
      if (var1 == 4 && this.hasVisibleDrawer()) {
         var2.startTracking();
         var3 = true;
      } else {
         var3 = super.onKeyDown(var1, var2);
      }

      return var3;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var3;
      if (var1 == 4) {
         View var4 = this.findVisibleDrawer();
         if (var4 != null && this.getDrawerLockMode(var4) == 0) {
            this.closeDrawers();
         }

         if (var4 != null) {
            var3 = true;
         } else {
            var3 = false;
         }
      } else {
         var3 = super.onKeyUp(var1, var2);
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.mInLayout = true;
      int var6 = var4 - var2;
      int var7 = this.getChildCount();

      for(var4 = 0; var4 < var7; ++var4) {
         View var8 = this.getChildAt(var4);
         if (var8.getVisibility() != 8) {
            DrawerLayout.LayoutParams var9 = (DrawerLayout.LayoutParams)var8.getLayoutParams();
            if (this.isContentView(var8)) {
               var8.layout(var9.leftMargin, var9.topMargin, var9.leftMargin + var8.getMeasuredWidth(), var9.topMargin + var8.getMeasuredHeight());
            } else {
               int var10 = var8.getMeasuredWidth();
               int var11 = var8.getMeasuredHeight();
               int var12;
               float var13;
               if (this.checkDrawerViewAbsoluteGravity(var8, 3)) {
                  var12 = -var10 + (int)((float)var10 * var9.onScreen);
                  var13 = (float)(var10 + var12) / (float)var10;
               } else {
                  var12 = var6 - (int)((float)var10 * var9.onScreen);
                  var13 = (float)(var6 - var12) / (float)var10;
               }

               boolean var14;
               if (var13 != var9.onScreen) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               switch(var9.gravity & 112) {
               case 16:
                  int var15 = var5 - var3;
                  int var16 = (var15 - var11) / 2;
                  if (var16 < var9.topMargin) {
                     var2 = var9.topMargin;
                  } else {
                     var2 = var16;
                     if (var16 + var11 > var15 - var9.bottomMargin) {
                        var2 = var15 - var9.bottomMargin - var11;
                     }
                  }

                  var8.layout(var12, var2, var12 + var10, var2 + var11);
                  break;
               case 80:
                  var2 = var5 - var3;
                  var8.layout(var12, var2 - var9.bottomMargin - var8.getMeasuredHeight(), var12 + var10, var2 - var9.bottomMargin);
                  break;
               default:
                  var8.layout(var12, var9.topMargin, var12 + var10, var9.topMargin + var11);
               }

               if (var14) {
                  this.setDrawerViewOffset(var8, var13);
               }

               byte var17;
               if (var9.onScreen > 0.0F) {
                  var17 = 0;
               } else {
                  var17 = 4;
               }

               if (var8.getVisibility() != var17) {
                  var8.setVisibility(var17);
               }
            }
         }
      }

      this.mInLayout = false;
      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      int var3;
      int var7;
      int var8;
      label92: {
         var3 = MeasureSpec.getMode(var1);
         int var4 = MeasureSpec.getMode(var2);
         int var5 = MeasureSpec.getSize(var1);
         int var6 = MeasureSpec.getSize(var2);
         if (var3 == 1073741824) {
            var7 = var6;
            var8 = var5;
            if (var4 == 1073741824) {
               break label92;
            }
         }

         if (!this.isInEditMode()) {
            throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
         }

         if (var3 != Integer.MIN_VALUE && var3 == 0) {
            var5 = 300;
         }

         if (var4 == Integer.MIN_VALUE) {
            var8 = var5;
            var7 = var6;
         } else {
            var7 = var6;
            var8 = var5;
            if (var4 == 0) {
               var7 = 300;
               var8 = var5;
            }
         }
      }

      this.setMeasuredDimension(var8, var7);
      boolean var15;
      if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this)) {
         var15 = true;
      } else {
         var15 = false;
      }

      int var9 = ViewCompat.getLayoutDirection(this);
      boolean var16 = false;
      boolean var17 = false;
      int var10 = this.getChildCount();

      for(var3 = 0; var3 < var10; ++var3) {
         View var11 = this.getChildAt(var3);
         if (var11.getVisibility() != 8) {
            DrawerLayout.LayoutParams var12 = (DrawerLayout.LayoutParams)var11.getLayoutParams();
            if (var15) {
               int var13 = GravityCompat.getAbsoluteGravity(var12.gravity, var9);
               if (ViewCompat.getFitsSystemWindows(var11)) {
                  IMPL.dispatchChildInsets(var11, this.mLastInsets, var13);
               } else {
                  IMPL.applyMarginInsets(var12, this.mLastInsets, var13);
               }
            }

            if (this.isContentView(var11)) {
               var11.measure(MeasureSpec.makeMeasureSpec(var8 - var12.leftMargin - var12.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec(var7 - var12.topMargin - var12.bottomMargin, 1073741824));
            } else {
               if (!this.isDrawerView(var11)) {
                  throw new IllegalStateException("Child " + var11 + " at index " + var3 + " does not have a valid layout_gravity - must be Gravity.LEFT, " + "Gravity.RIGHT or Gravity.NO_GRAVITY");
               }

               if (SET_DRAWER_SHADOW_FROM_ELEVATION && ViewCompat.getElevation(var11) != this.mDrawerElevation) {
                  ViewCompat.setElevation(var11, this.mDrawerElevation);
               }

               int var14 = this.getDrawerViewAbsoluteGravity(var11) & 7;
               boolean var18;
               if (var14 == 3) {
                  var18 = true;
               } else {
                  var18 = false;
               }

               if (var18 && var16 || !var18 && var17) {
                  throw new IllegalStateException("Child drawer has absolute gravity " + gravityToString(var14) + " but this " + "DrawerLayout" + " already has a " + "drawer view along that edge");
               }

               if (var18) {
                  var16 = true;
               } else {
                  var17 = true;
               }

               var11.measure(getChildMeasureSpec(var1, this.mMinDrawerMargin + var12.leftMargin + var12.rightMargin, var12.width), getChildMeasureSpec(var2, var12.topMargin + var12.bottomMargin, var12.height));
            }
         }
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof DrawerLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         DrawerLayout.SavedState var2 = (DrawerLayout.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         if (var2.openDrawerGravity != 0) {
            View var3 = this.findDrawerWithGravity(var2.openDrawerGravity);
            if (var3 != null) {
               this.openDrawer(var3);
            }
         }

         if (var2.lockModeLeft != 3) {
            this.setDrawerLockMode(var2.lockModeLeft, 3);
         }

         if (var2.lockModeRight != 3) {
            this.setDrawerLockMode(var2.lockModeRight, 5);
         }

         if (var2.lockModeStart != 3) {
            this.setDrawerLockMode(var2.lockModeStart, 8388611);
         }

         if (var2.lockModeEnd != 3) {
            this.setDrawerLockMode(var2.lockModeEnd, 8388613);
         }
      }

   }

   public void onRtlPropertiesChanged(int var1) {
      this.resolveShadowDrawables();
   }

   protected Parcelable onSaveInstanceState() {
      DrawerLayout.SavedState var1 = new DrawerLayout.SavedState(super.onSaveInstanceState());
      int var2 = this.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         DrawerLayout.LayoutParams var4 = (DrawerLayout.LayoutParams)this.getChildAt(var3).getLayoutParams();
         boolean var5;
         if (var4.openState == 1) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var6;
         if (var4.openState == 2) {
            var6 = true;
         } else {
            var6 = false;
         }

         if (var5 || var6) {
            var1.openDrawerGravity = var4.gravity;
            break;
         }
      }

      var1.lockModeLeft = this.mLockModeLeft;
      var1.lockModeRight = this.mLockModeRight;
      var1.lockModeStart = this.mLockModeStart;
      var1.lockModeEnd = this.mLockModeEnd;
      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      this.mLeftDragger.processTouchEvent(var1);
      this.mRightDragger.processTouchEvent(var1);
      float var2;
      float var3;
      switch(var1.getAction() & 255) {
      case 0:
         var2 = var1.getX();
         var3 = var1.getY();
         this.mInitialMotionX = var2;
         this.mInitialMotionY = var3;
         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
         break;
      case 1:
         var2 = var1.getX();
         var3 = var1.getY();
         boolean var4 = true;
         View var7 = this.mLeftDragger.findTopChildUnder((int)var2, (int)var3);
         boolean var5 = var4;
         if (var7 != null) {
            var5 = var4;
            if (this.isContentView(var7)) {
               var2 -= this.mInitialMotionX;
               var3 -= this.mInitialMotionY;
               int var6 = this.mLeftDragger.getTouchSlop();
               var5 = var4;
               if (var2 * var2 + var3 * var3 < (float)(var6 * var6)) {
                  var7 = this.findOpenDrawer();
                  var5 = var4;
                  if (var7 != null) {
                     if (this.getDrawerLockMode(var7) == 2) {
                        var5 = true;
                     } else {
                        var5 = false;
                     }
                  }
               }
            }
         }

         this.closeDrawers(var5);
         this.mDisallowInterceptRequested = false;
      case 2:
      default:
         break;
      case 3:
         this.closeDrawers(true);
         this.mDisallowInterceptRequested = false;
         this.mChildrenCanceledTouch = false;
      }

      return true;
   }

   public void openDrawer(int var1) {
      this.openDrawer(var1, true);
   }

   public void openDrawer(int var1, boolean var2) {
      View var3 = this.findDrawerWithGravity(var1);
      if (var3 == null) {
         throw new IllegalArgumentException("No drawer view found with gravity " + gravityToString(var1));
      } else {
         this.openDrawer(var3, var2);
      }
   }

   public void openDrawer(View var1) {
      this.openDrawer(var1, true);
   }

   public void openDrawer(View var1, boolean var2) {
      if (!this.isDrawerView(var1)) {
         throw new IllegalArgumentException("View " + var1 + " is not a sliding drawer");
      } else {
         DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
         if (this.mFirstLayout) {
            var3.onScreen = 1.0F;
            var3.openState = 1;
            this.updateChildrenImportantForAccessibility(var1, true);
         } else if (var2) {
            var3.openState |= 2;
            if (this.checkDrawerViewAbsoluteGravity(var1, 3)) {
               this.mLeftDragger.smoothSlideViewTo(var1, 0, var1.getTop());
            } else {
               this.mRightDragger.smoothSlideViewTo(var1, this.getWidth() - var1.getWidth(), var1.getTop());
            }
         } else {
            this.moveDrawerToOffset(var1, 1.0F);
            this.updateDrawerState(var3.gravity, 0, var1);
            var1.setVisibility(0);
         }

         this.invalidate();
      }
   }

   public void removeDrawerListener(@NonNull DrawerLayout.DrawerListener var1) {
      if (var1 != null && this.mListeners != null) {
         this.mListeners.remove(var1);
      }

   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      super.requestDisallowInterceptTouchEvent(var1);
      this.mDisallowInterceptRequested = var1;
      if (var1) {
         this.closeDrawers(true);
      }

   }

   public void requestLayout() {
      if (!this.mInLayout) {
         super.requestLayout();
      }

   }

   public void setChildInsets(Object var1, boolean var2) {
      this.mLastInsets = var1;
      this.mDrawStatusBarBackground = var2;
      if (!var2 && this.getBackground() == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.requestLayout();
   }

   public void setDrawerElevation(float var1) {
      this.mDrawerElevation = var1;

      for(int var2 = 0; var2 < this.getChildCount(); ++var2) {
         View var3 = this.getChildAt(var2);
         if (this.isDrawerView(var3)) {
            ViewCompat.setElevation(var3, this.mDrawerElevation);
         }
      }

   }

   @Deprecated
   public void setDrawerListener(DrawerLayout.DrawerListener var1) {
      if (this.mListener != null) {
         this.removeDrawerListener(this.mListener);
      }

      if (var1 != null) {
         this.addDrawerListener(var1);
      }

      this.mListener = var1;
   }

   public void setDrawerLockMode(int var1) {
      this.setDrawerLockMode(var1, 3);
      this.setDrawerLockMode(var1, 5);
   }

   public void setDrawerLockMode(int var1, int var2) {
      int var3 = GravityCompat.getAbsoluteGravity(var2, ViewCompat.getLayoutDirection(this));
      switch(var2) {
      case 3:
         this.mLockModeLeft = var1;
         break;
      case 5:
         this.mLockModeRight = var1;
         break;
      case 8388611:
         this.mLockModeStart = var1;
         break;
      case 8388613:
         this.mLockModeEnd = var1;
      }

      if (var1 != 0) {
         ViewDragHelper var4;
         if (var3 == 3) {
            var4 = this.mLeftDragger;
         } else {
            var4 = this.mRightDragger;
         }

         var4.cancel();
      }

      View var5;
      switch(var1) {
      case 1:
         var5 = this.findDrawerWithGravity(var3);
         if (var5 != null) {
            this.closeDrawer(var5);
         }
         break;
      case 2:
         var5 = this.findDrawerWithGravity(var3);
         if (var5 != null) {
            this.openDrawer(var5);
         }
      }

   }

   public void setDrawerLockMode(int var1, View var2) {
      if (!this.isDrawerView(var2)) {
         throw new IllegalArgumentException("View " + var2 + " is not a " + "drawer with appropriate layout_gravity");
      } else {
         this.setDrawerLockMode(var1, ((DrawerLayout.LayoutParams)var2.getLayoutParams()).gravity);
      }
   }

   public void setDrawerShadow(@DrawableRes int var1, int var2) {
      this.setDrawerShadow(ContextCompat.getDrawable(this.getContext(), var1), var2);
   }

   public void setDrawerShadow(Drawable var1, int var2) {
      if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
         if ((var2 & 8388611) == 8388611) {
            this.mShadowStart = var1;
         } else if ((var2 & 8388613) == 8388613) {
            this.mShadowEnd = var1;
         } else if ((var2 & 3) == 3) {
            this.mShadowLeft = var1;
         } else {
            if ((var2 & 5) != 5) {
               return;
            }

            this.mShadowRight = var1;
         }

         this.resolveShadowDrawables();
         this.invalidate();
      }

   }

   public void setDrawerTitle(int var1, CharSequence var2) {
      var1 = GravityCompat.getAbsoluteGravity(var1, ViewCompat.getLayoutDirection(this));
      if (var1 == 3) {
         this.mTitleLeft = var2;
      } else if (var1 == 5) {
         this.mTitleRight = var2;
      }

   }

   void setDrawerViewOffset(View var1, float var2) {
      DrawerLayout.LayoutParams var3 = (DrawerLayout.LayoutParams)var1.getLayoutParams();
      if (var2 != var3.onScreen) {
         var3.onScreen = var2;
         this.dispatchOnDrawerSlide(var1, var2);
      }

   }

   public void setScrimColor(@ColorInt int var1) {
      this.mScrimColor = var1;
      this.invalidate();
   }

   public void setStatusBarBackground(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = ContextCompat.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.mStatusBarBackground = var2;
      this.invalidate();
   }

   public void setStatusBarBackground(Drawable var1) {
      this.mStatusBarBackground = var1;
      this.invalidate();
   }

   public void setStatusBarBackgroundColor(@ColorInt int var1) {
      this.mStatusBarBackground = new ColorDrawable(var1);
      this.invalidate();
   }

   void updateDrawerState(int var1, int var2, View var3) {
      var1 = this.mLeftDragger.getViewDragState();
      int var4 = this.mRightDragger.getViewDragState();
      byte var6;
      if (var1 != 1 && var4 != 1) {
         if (var1 != 2 && var4 != 2) {
            var6 = 0;
         } else {
            var6 = 2;
         }
      } else {
         var6 = 1;
      }

      if (var3 != null && var2 == 0) {
         DrawerLayout.LayoutParams var5 = (DrawerLayout.LayoutParams)var3.getLayoutParams();
         if (var5.onScreen == 0.0F) {
            this.dispatchOnDrawerClosed(var3);
         } else if (var5.onScreen == 1.0F) {
            this.dispatchOnDrawerOpened(var3);
         }
      }

      if (var6 != this.mDrawerState) {
         this.mDrawerState = var6;
         if (this.mListeners != null) {
            for(var2 = this.mListeners.size() - 1; var2 >= 0; --var2) {
               ((DrawerLayout.DrawerListener)this.mListeners.get(var2)).onDrawerStateChanged(var6);
            }
         }
      }

   }

   class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final Rect mTmpRect = new Rect();

      private void addChildrenForAccessibility(AccessibilityNodeInfoCompat var1, ViewGroup var2) {
         int var3 = var2.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var5 = var2.getChildAt(var4);
            if (DrawerLayout.includeChildForAccessibility(var5)) {
               var1.addChild(var5);
            }
         }

      }

      private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat var1, AccessibilityNodeInfoCompat var2) {
         Rect var3 = this.mTmpRect;
         var2.getBoundsInParent(var3);
         var1.setBoundsInParent(var3);
         var2.getBoundsInScreen(var3);
         var1.setBoundsInScreen(var3);
         var1.setVisibleToUser(var2.isVisibleToUser());
         var1.setPackageName(var2.getPackageName());
         var1.setClassName(var2.getClassName());
         var1.setContentDescription(var2.getContentDescription());
         var1.setEnabled(var2.isEnabled());
         var1.setClickable(var2.isClickable());
         var1.setFocusable(var2.isFocusable());
         var1.setFocused(var2.isFocused());
         var1.setAccessibilityFocused(var2.isAccessibilityFocused());
         var1.setSelected(var2.isSelected());
         var1.setLongClickable(var2.isLongClickable());
         var1.addAction(var2.getActions());
      }

      public boolean dispatchPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         boolean var4;
         if (var2.getEventType() == 32) {
            List var5 = var2.getText();
            View var6 = DrawerLayout.this.findVisibleDrawer();
            if (var6 != null) {
               int var3 = DrawerLayout.this.getDrawerViewAbsoluteGravity(var6);
               CharSequence var7 = DrawerLayout.this.getDrawerTitle(var3);
               if (var7 != null) {
                  var5.add(var7);
               }
            }

            var4 = true;
         } else {
            var4 = super.dispatchPopulateAccessibilityEvent(var1, var2);
         }

         return var4;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(DrawerLayout.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
         } else {
            AccessibilityNodeInfoCompat var3 = AccessibilityNodeInfoCompat.obtain(var2);
            super.onInitializeAccessibilityNodeInfo(var1, var3);
            var2.setSource(var1);
            ViewParent var4 = ViewCompat.getParentForAccessibility(var1);
            if (var4 instanceof View) {
               var2.setParent((View)var4);
            }

            this.copyNodeInfoNoChildren(var2, var3);
            var3.recycle();
            this.addChildrenForAccessibility(var2, (ViewGroup)var1);
         }

         var2.setClassName(DrawerLayout.class.getName());
         var2.setFocusable(false);
         var2.setFocused(false);
         var2.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
         var2.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         boolean var4;
         if (!DrawerLayout.CAN_HIDE_DESCENDANTS && !DrawerLayout.includeChildForAccessibility(var2)) {
            var4 = false;
         } else {
            var4 = super.onRequestSendAccessibilityEvent(var1, var2, var3);
         }

         return var4;
      }
   }

   final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         if (!DrawerLayout.includeChildForAccessibility(var1)) {
            var2.setParent((View)null);
         }

      }
   }

   interface DrawerLayoutCompatImpl {
      void applyMarginInsets(MarginLayoutParams var1, Object var2, int var3);

      void configureApplyInsets(View var1);

      void dispatchChildInsets(View var1, Object var2, int var3);

      Drawable getDefaultStatusBarBackground(Context var1);

      int getTopInset(Object var1);
   }

   static class DrawerLayoutCompatImplApi21 implements DrawerLayout.DrawerLayoutCompatImpl {
      public void applyMarginInsets(MarginLayoutParams var1, Object var2, int var3) {
         DrawerLayoutCompatApi21.applyMarginInsets(var1, var2, var3);
      }

      public void configureApplyInsets(View var1) {
         DrawerLayoutCompatApi21.configureApplyInsets(var1);
      }

      public void dispatchChildInsets(View var1, Object var2, int var3) {
         DrawerLayoutCompatApi21.dispatchChildInsets(var1, var2, var3);
      }

      public Drawable getDefaultStatusBarBackground(Context var1) {
         return DrawerLayoutCompatApi21.getDefaultStatusBarBackground(var1);
      }

      public int getTopInset(Object var1) {
         return DrawerLayoutCompatApi21.getTopInset(var1);
      }
   }

   static class DrawerLayoutCompatImplBase implements DrawerLayout.DrawerLayoutCompatImpl {
      public void applyMarginInsets(MarginLayoutParams var1, Object var2, int var3) {
      }

      public void configureApplyInsets(View var1) {
      }

      public void dispatchChildInsets(View var1, Object var2, int var3) {
      }

      public Drawable getDefaultStatusBarBackground(Context var1) {
         return null;
      }

      public int getTopInset(Object var1) {
         return 0;
      }
   }

   public interface DrawerListener {
      void onDrawerClosed(View var1);

      void onDrawerOpened(View var1);

      void onDrawerSlide(View var1, float var2);

      void onDrawerStateChanged(int var1);
   }

   public static class LayoutParams extends MarginLayoutParams {
      private static final int FLAG_IS_CLOSING = 4;
      private static final int FLAG_IS_OPENED = 1;
      private static final int FLAG_IS_OPENING = 2;
      public int gravity;
      boolean isPeeking;
      float onScreen;
      int openState;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
         this.gravity = 0;
      }

      public LayoutParams(int var1, int var2, int var3) {
         this(var1, var2);
         this.gravity = var3;
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         this.gravity = 0;
         TypedArray var3 = var1.obtainStyledAttributes(var2, DrawerLayout.LAYOUT_ATTRS);
         this.gravity = var3.getInt(0, 0);
         var3.recycle();
      }

      public LayoutParams(DrawerLayout.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
         this.gravity = var1.gravity;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
         this.gravity = 0;
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
         this.gravity = 0;
      }
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
         public DrawerLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new DrawerLayout.SavedState(var1, var2);
         }

         public DrawerLayout.SavedState[] newArray(int var1) {
            return new DrawerLayout.SavedState[var1];
         }
      });
      int lockModeEnd;
      int lockModeLeft;
      int lockModeRight;
      int lockModeStart;
      int openDrawerGravity = 0;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.openDrawerGravity = var1.readInt();
         this.lockModeLeft = var1.readInt();
         this.lockModeRight = var1.readInt();
         this.lockModeStart = var1.readInt();
         this.lockModeEnd = var1.readInt();
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.openDrawerGravity);
         var1.writeInt(this.lockModeLeft);
         var1.writeInt(this.lockModeRight);
         var1.writeInt(this.lockModeStart);
         var1.writeInt(this.lockModeEnd);
      }
   }

   public abstract static class SimpleDrawerListener implements DrawerLayout.DrawerListener {
      public void onDrawerClosed(View var1) {
      }

      public void onDrawerOpened(View var1) {
      }

      public void onDrawerSlide(View var1, float var2) {
      }

      public void onDrawerStateChanged(int var1) {
      }
   }

   private class ViewDragCallback extends ViewDragHelper.Callback {
      private final int mAbsGravity;
      private ViewDragHelper mDragger;
      private final Runnable mPeekRunnable = new Runnable() {
         public void run() {
            ViewDragCallback.this.peekDrawer();
         }
      };

      ViewDragCallback(int var2) {
         this.mAbsGravity = var2;
      }

      private void closeOtherDrawer() {
         byte var1 = 3;
         if (this.mAbsGravity == 3) {
            var1 = 5;
         }

         View var2 = DrawerLayout.this.findDrawerWithGravity(var1);
         if (var2 != null) {
            DrawerLayout.this.closeDrawer(var2);
         }

      }

      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            var2 = Math.max(-var1.getWidth(), Math.min(var2, 0));
         } else {
            var3 = DrawerLayout.this.getWidth();
            var2 = Math.max(var3 - var1.getWidth(), Math.min(var2, var3));
         }

         return var2;
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return var1.getTop();
      }

      public int getViewHorizontalDragRange(View var1) {
         int var2;
         if (DrawerLayout.this.isDrawerView(var1)) {
            var2 = var1.getWidth();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public void onEdgeDragStarted(int var1, int var2) {
         View var3;
         if ((var1 & 1) == 1) {
            var3 = DrawerLayout.this.findDrawerWithGravity(3);
         } else {
            var3 = DrawerLayout.this.findDrawerWithGravity(5);
         }

         if (var3 != null && DrawerLayout.this.getDrawerLockMode(var3) == 0) {
            this.mDragger.captureChildView(var3, var2);
         }

      }

      public boolean onEdgeLock(int var1) {
         return false;
      }

      public void onEdgeTouched(int var1, int var2) {
         DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
      }

      public void onViewCaptured(View var1, int var2) {
         ((DrawerLayout.LayoutParams)var1.getLayoutParams()).isPeeking = false;
         this.closeOtherDrawer();
      }

      public void onViewDragStateChanged(int var1) {
         DrawerLayout.this.updateDrawerState(this.mAbsGravity, var1, this.mDragger.getCapturedView());
      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         var3 = var1.getWidth();
         float var6;
         if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            var6 = (float)(var3 + var2) / (float)var3;
         } else {
            var6 = (float)(DrawerLayout.this.getWidth() - var2) / (float)var3;
         }

         DrawerLayout.this.setDrawerViewOffset(var1, var6);
         byte var7;
         if (var6 == 0.0F) {
            var7 = 4;
         } else {
            var7 = 0;
         }

         var1.setVisibility(var7);
         DrawerLayout.this.invalidate();
      }

      public void onViewReleased(View var1, float var2, float var3) {
         var3 = DrawerLayout.this.getDrawerViewOffset(var1);
         int var4 = var1.getWidth();
         int var5;
         if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, 3)) {
            if (var2 <= 0.0F && (var2 != 0.0F || var3 <= 0.5F)) {
               var5 = -var4;
            } else {
               var5 = 0;
            }
         } else {
            var5 = DrawerLayout.this.getWidth();
            if (var2 < 0.0F || var2 == 0.0F && var3 > 0.5F) {
               var5 -= var4;
            }
         }

         this.mDragger.settleCapturedViewAt(var5, var1.getTop());
         DrawerLayout.this.invalidate();
      }

      void peekDrawer() {
         int var1 = 0;
         int var2 = this.mDragger.getEdgeSize();
         boolean var3;
         if (this.mAbsGravity == 3) {
            var3 = true;
         } else {
            var3 = false;
         }

         View var4;
         if (var3) {
            var4 = DrawerLayout.this.findDrawerWithGravity(3);
            if (var4 != null) {
               var1 = -var4.getWidth();
            }

            var1 += var2;
         } else {
            var4 = DrawerLayout.this.findDrawerWithGravity(5);
            var1 = DrawerLayout.this.getWidth() - var2;
         }

         if (var4 != null && (var3 && var4.getLeft() < var1 || !var3 && var4.getLeft() > var1) && DrawerLayout.this.getDrawerLockMode(var4) == 0) {
            DrawerLayout.LayoutParams var5 = (DrawerLayout.LayoutParams)var4.getLayoutParams();
            this.mDragger.smoothSlideViewTo(var4, var1, var4.getTop());
            var5.isPeeking = true;
            DrawerLayout.this.invalidate();
            this.closeOtherDrawer();
            DrawerLayout.this.cancelChildViewTouch();
         }

      }

      public void removeCallbacks() {
         DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
      }

      public void setDragger(ViewDragHelper var1) {
         this.mDragger = var1;
      }

      public boolean tryCaptureView(View var1, int var2) {
         boolean var3;
         if (DrawerLayout.this.isDrawerView(var1) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(var1, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(var1) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
