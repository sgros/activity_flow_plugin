package android.support.v4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewPager extends ViewGroup {
   private static final int CLOSE_ENOUGH = 2;
   private static final Comparator COMPARATOR = new Comparator() {
      public int compare(ViewPager.ItemInfo var1, ViewPager.ItemInfo var2) {
         return var1.position - var2.position;
      }
   };
   private static final boolean DEBUG = false;
   private static final int DEFAULT_GUTTER_SIZE = 16;
   private static final int DEFAULT_OFFSCREEN_PAGES = 1;
   private static final int DRAW_ORDER_DEFAULT = 0;
   private static final int DRAW_ORDER_FORWARD = 1;
   private static final int DRAW_ORDER_REVERSE = 2;
   private static final int INVALID_POINTER = -1;
   static final int[] LAYOUT_ATTRS = new int[]{16842931};
   private static final int MAX_SETTLE_DURATION = 600;
   private static final int MIN_DISTANCE_FOR_FLING = 25;
   private static final int MIN_FLING_VELOCITY = 400;
   public static final int SCROLL_STATE_DRAGGING = 1;
   public static final int SCROLL_STATE_IDLE = 0;
   public static final int SCROLL_STATE_SETTLING = 2;
   private static final String TAG = "ViewPager";
   private static final boolean USE_CACHE = false;
   private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float var1) {
         --var1;
         return var1 * var1 * var1 * var1 * var1 + 1.0F;
      }
   };
   private static final ViewPager.ViewPositionComparator sPositionComparator = new ViewPager.ViewPositionComparator();
   private int mActivePointerId = -1;
   PagerAdapter mAdapter;
   private List mAdapterChangeListeners;
   private int mBottomPageBounds;
   private boolean mCalledSuper;
   private int mChildHeightMeasureSpec;
   private int mChildWidthMeasureSpec;
   private int mCloseEnough;
   int mCurItem;
   private int mDecorChildCount;
   private int mDefaultGutterSize;
   private int mDrawingOrder;
   private ArrayList mDrawingOrderedChildren;
   private final Runnable mEndScrollRunnable = new Runnable() {
      public void run() {
         ViewPager.this.setScrollState(0);
         ViewPager.this.populate();
      }
   };
   private int mExpectedAdapterCount;
   private long mFakeDragBeginTime;
   private boolean mFakeDragging;
   private boolean mFirstLayout = true;
   private float mFirstOffset = -3.4028235E38F;
   private int mFlingDistance;
   private int mGutterSize;
   private boolean mInLayout;
   private float mInitialMotionX;
   private float mInitialMotionY;
   private ViewPager.OnPageChangeListener mInternalPageChangeListener;
   private boolean mIsBeingDragged;
   private boolean mIsScrollStarted;
   private boolean mIsUnableToDrag;
   private final ArrayList mItems = new ArrayList();
   private float mLastMotionX;
   private float mLastMotionY;
   private float mLastOffset = Float.MAX_VALUE;
   private EdgeEffectCompat mLeftEdge;
   private Drawable mMarginDrawable;
   private int mMaximumVelocity;
   private int mMinimumVelocity;
   private boolean mNeedCalculatePageOffsets = false;
   private ViewPager.PagerObserver mObserver;
   private int mOffscreenPageLimit = 1;
   private ViewPager.OnPageChangeListener mOnPageChangeListener;
   private List mOnPageChangeListeners;
   private int mPageMargin;
   private ViewPager.PageTransformer mPageTransformer;
   private int mPageTransformerLayerType;
   private boolean mPopulatePending;
   private Parcelable mRestoredAdapterState = null;
   private ClassLoader mRestoredClassLoader = null;
   private int mRestoredCurItem = -1;
   private EdgeEffectCompat mRightEdge;
   private int mScrollState = 0;
   private Scroller mScroller;
   private boolean mScrollingCacheEnabled;
   private Method mSetChildrenDrawingOrderEnabled;
   private final ViewPager.ItemInfo mTempItem = new ViewPager.ItemInfo();
   private final Rect mTempRect = new Rect();
   private int mTopPageBounds;
   private int mTouchSlop;
   private VelocityTracker mVelocityTracker;

   public ViewPager(Context var1) {
      super(var1);
      this.initViewPager();
   }

   public ViewPager(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.initViewPager();
   }

   private void calculatePageOffsets(ViewPager.ItemInfo var1, int var2, ViewPager.ItemInfo var3) {
      int var4 = this.mAdapter.getCount();
      int var5 = this.getClientWidth();
      float var6;
      if (var5 > 0) {
         var6 = (float)this.mPageMargin / (float)var5;
      } else {
         var6 = 0.0F;
      }

      int var7;
      float var8;
      float var9;
      int var10;
      if (var3 != null) {
         var5 = var3.position;
         if (var5 < var1.position) {
            var7 = 0;
            var8 = var3.offset + var3.widthFactor + var6;
            ++var5;

            while(var5 <= var1.position && var7 < this.mItems.size()) {
               var3 = (ViewPager.ItemInfo)this.mItems.get(var7);

               while(true) {
                  var9 = var8;
                  var10 = var5;
                  if (var5 <= var3.position) {
                     break;
                  }

                  var9 = var8;
                  var10 = var5;
                  if (var7 >= this.mItems.size() - 1) {
                     break;
                  }

                  ++var7;
                  var3 = (ViewPager.ItemInfo)this.mItems.get(var7);
               }

               while(var10 < var3.position) {
                  var9 += this.mAdapter.getPageWidth(var10) + var6;
                  ++var10;
               }

               var3.offset = var9;
               var8 = var9 + var3.widthFactor + var6;
               var5 = var10 + 1;
            }
         } else if (var5 > var1.position) {
            var7 = this.mItems.size() - 1;
            var8 = var3.offset;
            --var5;

            while(var5 >= var1.position && var7 >= 0) {
               var3 = (ViewPager.ItemInfo)this.mItems.get(var7);

               while(true) {
                  var9 = var8;
                  var10 = var5;
                  if (var5 >= var3.position) {
                     break;
                  }

                  var9 = var8;
                  var10 = var5;
                  if (var7 <= 0) {
                     break;
                  }

                  --var7;
                  var3 = (ViewPager.ItemInfo)this.mItems.get(var7);
               }

               while(var10 > var3.position) {
                  var9 -= this.mAdapter.getPageWidth(var10) + var6;
                  --var10;
               }

               var8 = var9 - (var3.widthFactor + var6);
               var3.offset = var8;
               var5 = var10 - 1;
            }
         }
      }

      var10 = this.mItems.size();
      var9 = var1.offset;
      var5 = var1.position - 1;
      if (var1.position == 0) {
         var8 = var1.offset;
      } else {
         var8 = -3.4028235E38F;
      }

      this.mFirstOffset = var8;
      if (var1.position == var4 - 1) {
         var8 = var1.offset + var1.widthFactor - 1.0F;
      } else {
         var8 = Float.MAX_VALUE;
      }

      this.mLastOffset = var8;
      var7 = var2 - 1;

      for(var8 = var9; var7 >= 0; --var5) {
         for(var3 = (ViewPager.ItemInfo)this.mItems.get(var7); var5 > var3.position; --var5) {
            var8 -= this.mAdapter.getPageWidth(var5) + var6;
         }

         var8 -= var3.widthFactor + var6;
         var3.offset = var8;
         if (var3.position == 0) {
            this.mFirstOffset = var8;
         }

         --var7;
      }

      var8 = var1.offset + var1.widthFactor + var6;
      var5 = var1.position + 1;
      var7 = var2 + 1;
      var2 = var5;

      for(var5 = var7; var5 < var10; ++var2) {
         for(var1 = (ViewPager.ItemInfo)this.mItems.get(var5); var2 < var1.position; ++var2) {
            var8 += this.mAdapter.getPageWidth(var2) + var6;
         }

         if (var1.position == var4 - 1) {
            this.mLastOffset = var1.widthFactor + var8 - 1.0F;
         }

         var1.offset = var8;
         var8 += var1.widthFactor + var6;
         ++var5;
      }

      this.mNeedCalculatePageOffsets = false;
   }

   private void completeScroll(boolean var1) {
      boolean var2 = true;
      boolean var3;
      if (this.mScrollState == 2) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         this.setScrollingCacheEnabled(false);
         if (this.mScroller.isFinished()) {
            var2 = false;
         }

         if (var2) {
            this.mScroller.abortAnimation();
            int var4 = this.getScrollX();
            int var5 = this.getScrollY();
            int var8 = this.mScroller.getCurrX();
            int var6 = this.mScroller.getCurrY();
            if (var4 != var8 || var5 != var6) {
               this.scrollTo(var8, var6);
               if (var8 != var4) {
                  this.pageScrolled(var8);
               }
            }
         }
      }

      this.mPopulatePending = false;
      byte var10 = 0;
      var2 = var3;

      for(int var9 = var10; var9 < this.mItems.size(); ++var9) {
         ViewPager.ItemInfo var7 = (ViewPager.ItemInfo)this.mItems.get(var9);
         if (var7.scrolling) {
            var2 = true;
            var7.scrolling = false;
         }
      }

      if (var2) {
         if (var1) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
         } else {
            this.mEndScrollRunnable.run();
         }
      }

   }

   private int determineTargetPage(int var1, float var2, int var3, int var4) {
      if (Math.abs(var4) > this.mFlingDistance && Math.abs(var3) > this.mMinimumVelocity) {
         if (var3 <= 0) {
            ++var1;
         }
      } else {
         float var7;
         if (var1 >= this.mCurItem) {
            var7 = 0.4F;
         } else {
            var7 = 0.6F;
         }

         var1 += (int)(var2 + var7);
      }

      var3 = var1;
      if (this.mItems.size() > 0) {
         ViewPager.ItemInfo var5 = (ViewPager.ItemInfo)this.mItems.get(0);
         ViewPager.ItemInfo var6 = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
         var3 = Math.max(var5.position, Math.min(var1, var6.position));
      }

      return var3;
   }

   private void dispatchOnPageScrolled(int var1, float var2, int var3) {
      if (this.mOnPageChangeListener != null) {
         this.mOnPageChangeListener.onPageScrolled(var1, var2, var3);
      }

      if (this.mOnPageChangeListeners != null) {
         int var4 = 0;

         for(int var5 = this.mOnPageChangeListeners.size(); var4 < var5; ++var4) {
            ViewPager.OnPageChangeListener var6 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var4);
            if (var6 != null) {
               var6.onPageScrolled(var1, var2, var3);
            }
         }
      }

      if (this.mInternalPageChangeListener != null) {
         this.mInternalPageChangeListener.onPageScrolled(var1, var2, var3);
      }

   }

   private void dispatchOnPageSelected(int var1) {
      if (this.mOnPageChangeListener != null) {
         this.mOnPageChangeListener.onPageSelected(var1);
      }

      if (this.mOnPageChangeListeners != null) {
         int var2 = 0;

         for(int var3 = this.mOnPageChangeListeners.size(); var2 < var3; ++var2) {
            ViewPager.OnPageChangeListener var4 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var2);
            if (var4 != null) {
               var4.onPageSelected(var1);
            }
         }
      }

      if (this.mInternalPageChangeListener != null) {
         this.mInternalPageChangeListener.onPageSelected(var1);
      }

   }

   private void dispatchOnScrollStateChanged(int var1) {
      if (this.mOnPageChangeListener != null) {
         this.mOnPageChangeListener.onPageScrollStateChanged(var1);
      }

      if (this.mOnPageChangeListeners != null) {
         int var2 = 0;

         for(int var3 = this.mOnPageChangeListeners.size(); var2 < var3; ++var2) {
            ViewPager.OnPageChangeListener var4 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var2);
            if (var4 != null) {
               var4.onPageScrollStateChanged(var1);
            }
         }
      }

      if (this.mInternalPageChangeListener != null) {
         this.mInternalPageChangeListener.onPageScrollStateChanged(var1);
      }

   }

   private void enableLayers(boolean var1) {
      int var2 = this.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4;
         if (var1) {
            var4 = this.mPageTransformerLayerType;
         } else {
            var4 = 0;
         }

         ViewCompat.setLayerType(this.getChildAt(var3), var4, (Paint)null);
      }

   }

   private void endDrag() {
      this.mIsBeingDragged = false;
      this.mIsUnableToDrag = false;
      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

   }

   private Rect getChildRectInPagerCoordinates(Rect var1, View var2) {
      Rect var3 = var1;
      if (var1 == null) {
         var3 = new Rect();
      }

      if (var2 == null) {
         var3.set(0, 0, 0, 0);
      } else {
         var3.left = var2.getLeft();
         var3.right = var2.getRight();
         var3.top = var2.getTop();
         var3.bottom = var2.getBottom();

         ViewGroup var5;
         for(ViewParent var4 = var2.getParent(); var4 instanceof ViewGroup && var4 != this; var4 = var5.getParent()) {
            var5 = (ViewGroup)var4;
            var3.left += var5.getLeft();
            var3.right += var5.getRight();
            var3.top += var5.getTop();
            var3.bottom += var5.getBottom();
         }
      }

      return var3;
   }

   private int getClientWidth() {
      return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
   }

   private ViewPager.ItemInfo infoForCurrentScrollPosition() {
      float var1 = 0.0F;
      int var2 = this.getClientWidth();
      float var3;
      if (var2 > 0) {
         var3 = (float)this.getScrollX() / (float)var2;
      } else {
         var3 = 0.0F;
      }

      if (var2 > 0) {
         var1 = (float)this.mPageMargin / (float)var2;
      }

      int var4 = -1;
      float var5 = 0.0F;
      float var6 = 0.0F;
      boolean var7 = true;
      ViewPager.ItemInfo var8 = null;
      var2 = 0;

      ViewPager.ItemInfo var9;
      while(true) {
         var9 = var8;
         if (var2 >= this.mItems.size()) {
            break;
         }

         var9 = (ViewPager.ItemInfo)this.mItems.get(var2);
         int var10 = var2;
         ViewPager.ItemInfo var11 = var9;
         if (!var7) {
            var10 = var2;
            var11 = var9;
            if (var9.position != var4 + 1) {
               var11 = this.mTempItem;
               var11.offset = var5 + var6 + var1;
               var11.position = var4 + 1;
               var11.widthFactor = this.mAdapter.getPageWidth(var11.position);
               var10 = var2 - 1;
            }
         }

         var5 = var11.offset;
         var6 = var11.widthFactor;
         if (!var7) {
            var9 = var8;
            if (var3 < var5) {
               break;
            }
         }

         if (var3 < var6 + var5 + var1 || var10 == this.mItems.size() - 1) {
            var9 = var11;
            break;
         }

         var7 = false;
         var4 = var11.position;
         var6 = var11.widthFactor;
         var2 = var10 + 1;
         var8 = var11;
      }

      return var9;
   }

   private static boolean isDecorView(@NonNull View var0) {
      boolean var1;
      if (var0.getClass().getAnnotation(ViewPager.DecorView.class) != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean isGutterDrag(float var1, float var2) {
      boolean var3;
      if ((var1 >= (float)this.mGutterSize || var2 <= 0.0F) && (var1 <= (float)(this.getWidth() - this.mGutterSize) || var2 >= 0.0F)) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   private void onSecondaryPointerUp(MotionEvent var1) {
      int var2 = MotionEventCompat.getActionIndex(var1);
      if (var1.getPointerId(var2) == this.mActivePointerId) {
         byte var3;
         if (var2 == 0) {
            var3 = 1;
         } else {
            var3 = 0;
         }

         this.mLastMotionX = var1.getX(var3);
         this.mActivePointerId = var1.getPointerId(var3);
         if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
         }
      }

   }

   private boolean pageScrolled(int var1) {
      boolean var2 = false;
      if (this.mItems.size() == 0) {
         if (!this.mFirstLayout) {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0F, 0);
            if (!this.mCalledSuper) {
               throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
         }
      } else {
         ViewPager.ItemInfo var3 = this.infoForCurrentScrollPosition();
         int var4 = this.getClientWidth();
         int var5 = this.mPageMargin;
         float var6 = (float)this.mPageMargin / (float)var4;
         int var7 = var3.position;
         var6 = ((float)var1 / (float)var4 - var3.offset) / (var3.widthFactor + var6);
         var1 = (int)((float)(var4 + var5) * var6);
         this.mCalledSuper = false;
         this.onPageScrolled(var7, var6, var1);
         if (!this.mCalledSuper) {
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
         }

         var2 = true;
      }

      return var2;
   }

   private boolean performDrag(float var1) {
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      float var5 = this.mLastMotionX;
      this.mLastMotionX = var1;
      float var6 = (float)this.getScrollX() + (var5 - var1);
      int var7 = this.getClientWidth();
      var1 = (float)var7 * this.mFirstOffset;
      var5 = (float)var7 * this.mLastOffset;
      boolean var8 = true;
      boolean var9 = true;
      ViewPager.ItemInfo var10 = (ViewPager.ItemInfo)this.mItems.get(0);
      ViewPager.ItemInfo var11 = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
      if (var10.position != 0) {
         var8 = false;
         var1 = var10.offset * (float)var7;
      }

      if (var11.position != this.mAdapter.getCount() - 1) {
         var9 = false;
         var5 = var11.offset * (float)var7;
      }

      if (var6 < var1) {
         if (var8) {
            var4 = this.mLeftEdge.onPull(Math.abs(var1 - var6) / (float)var7);
         }
      } else {
         var4 = var2;
         var1 = var6;
         if (var6 > var5) {
            var4 = var3;
            if (var9) {
               var4 = this.mRightEdge.onPull(Math.abs(var6 - var5) / (float)var7);
            }

            var1 = var5;
         }
      }

      this.mLastMotionX += var1 - (float)((int)var1);
      this.scrollTo((int)var1, this.getScrollY());
      this.pageScrolled((int)var1);
      return var4;
   }

   private void recomputeScrollPosition(int var1, int var2, int var3, int var4) {
      float var9;
      if (var2 > 0 && !this.mItems.isEmpty()) {
         if (!this.mScroller.isFinished()) {
            this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
         } else {
            int var5 = this.getPaddingLeft();
            int var6 = this.getPaddingRight();
            int var7 = this.getPaddingLeft();
            int var8 = this.getPaddingRight();
            var9 = (float)this.getScrollX() / (float)(var2 - var7 - var8 + var4);
            this.scrollTo((int)((float)(var1 - var5 - var6 + var3) * var9), this.getScrollY());
         }
      } else {
         ViewPager.ItemInfo var10 = this.infoForPosition(this.mCurItem);
         if (var10 != null) {
            var9 = Math.min(var10.offset, this.mLastOffset);
         } else {
            var9 = 0.0F;
         }

         var1 = (int)((float)(var1 - this.getPaddingLeft() - this.getPaddingRight()) * var9);
         if (var1 != this.getScrollX()) {
            this.completeScroll(false);
            this.scrollTo(var1, this.getScrollY());
         }
      }

   }

   private void removeNonDecorViews() {
      int var2;
      for(int var1 = 0; var1 < this.getChildCount(); var1 = var2 + 1) {
         var2 = var1;
         if (!((ViewPager.LayoutParams)this.getChildAt(var1).getLayoutParams()).isDecor) {
            this.removeViewAt(var1);
            var2 = var1 - 1;
         }
      }

   }

   private void requestParentDisallowInterceptTouchEvent(boolean var1) {
      ViewParent var2 = this.getParent();
      if (var2 != null) {
         var2.requestDisallowInterceptTouchEvent(var1);
      }

   }

   private boolean resetTouch() {
      this.mActivePointerId = -1;
      this.endDrag();
      return this.mLeftEdge.onRelease() | this.mRightEdge.onRelease();
   }

   private void scrollToItem(int var1, boolean var2, int var3, boolean var4) {
      ViewPager.ItemInfo var5 = this.infoForPosition(var1);
      int var6 = 0;
      if (var5 != null) {
         var6 = (int)((float)this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(var5.offset, this.mLastOffset)));
      }

      if (var2) {
         this.smoothScrollTo(var6, 0, var3);
         if (var4) {
            this.dispatchOnPageSelected(var1);
         }
      } else {
         if (var4) {
            this.dispatchOnPageSelected(var1);
         }

         this.completeScroll(false);
         this.scrollTo(var6, 0);
         this.pageScrolled(var6);
      }

   }

   private void setScrollingCacheEnabled(boolean var1) {
      if (this.mScrollingCacheEnabled != var1) {
         this.mScrollingCacheEnabled = var1;
      }

   }

   private void sortChildDrawingOrder() {
      if (this.mDrawingOrder != 0) {
         if (this.mDrawingOrderedChildren == null) {
            this.mDrawingOrderedChildren = new ArrayList();
         } else {
            this.mDrawingOrderedChildren.clear();
         }

         int var1 = this.getChildCount();

         for(int var2 = 0; var2 < var1; ++var2) {
            View var3 = this.getChildAt(var2);
            this.mDrawingOrderedChildren.add(var3);
         }

         Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
      }

   }

   public void addFocusables(ArrayList var1, int var2, int var3) {
      int var4 = var1.size();
      int var5 = this.getDescendantFocusability();
      if (var5 != 393216) {
         for(int var6 = 0; var6 < this.getChildCount(); ++var6) {
            View var7 = this.getChildAt(var6);
            if (var7.getVisibility() == 0) {
               ViewPager.ItemInfo var8 = this.infoForChild(var7);
               if (var8 != null && var8.position == this.mCurItem) {
                  var7.addFocusables(var1, var2, var3);
               }
            }
         }
      }

      if ((var5 != 262144 || var4 == var1.size()) && this.isFocusable() && ((var3 & 1) != 1 || !this.isInTouchMode() || this.isFocusableInTouchMode()) && var1 != null) {
         var1.add(this);
      }

   }

   ViewPager.ItemInfo addNewItem(int var1, int var2) {
      ViewPager.ItemInfo var3 = new ViewPager.ItemInfo();
      var3.position = var1;
      var3.object = this.mAdapter.instantiateItem((ViewGroup)this, var1);
      var3.widthFactor = this.mAdapter.getPageWidth(var1);
      if (var2 >= 0 && var2 < this.mItems.size()) {
         this.mItems.add(var2, var3);
      } else {
         this.mItems.add(var3);
      }

      return var3;
   }

   public void addOnAdapterChangeListener(@NonNull ViewPager.OnAdapterChangeListener var1) {
      if (this.mAdapterChangeListeners == null) {
         this.mAdapterChangeListeners = new ArrayList();
      }

      this.mAdapterChangeListeners.add(var1);
   }

   public void addOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      if (this.mOnPageChangeListeners == null) {
         this.mOnPageChangeListeners = new ArrayList();
      }

      this.mOnPageChangeListeners.add(var1);
   }

   public void addTouchables(ArrayList var1) {
      for(int var2 = 0; var2 < this.getChildCount(); ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3.getVisibility() == 0) {
            ViewPager.ItemInfo var4 = this.infoForChild(var3);
            if (var4 != null && var4.position == this.mCurItem) {
               var3.addTouchables(var1);
            }
         }
      }

   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      android.view.ViewGroup.LayoutParams var4 = var3;
      if (!this.checkLayoutParams(var3)) {
         var4 = this.generateLayoutParams(var3);
      }

      ViewPager.LayoutParams var5 = (ViewPager.LayoutParams)var4;
      var5.isDecor |= isDecorView(var1);
      if (this.mInLayout) {
         if (var5 != null && var5.isDecor) {
            throw new IllegalStateException("Cannot add pager decor view during layout");
         }

         var5.needsMeasure = true;
         this.addViewInLayout(var1, var2, var4);
      } else {
         super.addView(var1, var2, var4);
      }

   }

   public boolean arrowScroll(int var1) {
      View var2 = this.findFocus();
      View var3;
      if (var2 == this) {
         var3 = null;
      } else {
         var3 = var2;
         if (var2 != null) {
            boolean var6 = false;
            ViewParent var8 = var2.getParent();

            boolean var5;
            while(true) {
               var5 = var6;
               if (!(var8 instanceof ViewGroup)) {
                  break;
               }

               if (var8 == this) {
                  var5 = true;
                  break;
               }

               var8 = var8.getParent();
            }

            var3 = var2;
            if (!var5) {
               StringBuilder var7 = new StringBuilder();
               var7.append(var2.getClass().getSimpleName());

               for(var8 = var2.getParent(); var8 instanceof ViewGroup; var8 = var8.getParent()) {
                  var7.append(" => ").append(var8.getClass().getSimpleName());
               }

               Log.e("ViewPager", "arrowScroll tried to find focus based on non-child current focused view " + var7.toString());
               var3 = null;
            }
         }
      }

      boolean var4 = false;
      var2 = FocusFinder.getInstance().findNextFocus(this, var3, var1);
      if (var2 != null && var2 != var3) {
         int var9;
         int var10;
         if (var1 == 17) {
            var9 = this.getChildRectInPagerCoordinates(this.mTempRect, var2).left;
            var10 = this.getChildRectInPagerCoordinates(this.mTempRect, var3).left;
            if (var3 != null && var9 >= var10) {
               var4 = this.pageLeft();
            } else {
               var4 = var2.requestFocus();
            }
         } else if (var1 == 66) {
            var9 = this.getChildRectInPagerCoordinates(this.mTempRect, var2).left;
            var10 = this.getChildRectInPagerCoordinates(this.mTempRect, var3).left;
            if (var3 != null && var9 <= var10) {
               var4 = this.pageRight();
            } else {
               var4 = var2.requestFocus();
            }
         }
      } else if (var1 != 17 && var1 != 1) {
         if (var1 == 66 || var1 == 2) {
            var4 = this.pageRight();
         }
      } else {
         var4 = this.pageLeft();
      }

      if (var4) {
         this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(var1));
      }

      return var4;
   }

   public boolean beginFakeDrag() {
      boolean var1 = false;
      if (!this.mIsBeingDragged) {
         this.mFakeDragging = true;
         this.setScrollState(1);
         this.mLastMotionX = 0.0F;
         this.mInitialMotionX = 0.0F;
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         } else {
            this.mVelocityTracker.clear();
         }

         long var2 = SystemClock.uptimeMillis();
         MotionEvent var4 = MotionEvent.obtain(var2, var2, 0, 0.0F, 0.0F, 0);
         this.mVelocityTracker.addMovement(var4);
         var4.recycle();
         this.mFakeDragBeginTime = var2;
         var1 = true;
      }

      return var1;
   }

   protected boolean canScroll(View var1, boolean var2, int var3, int var4, int var5) {
      if (var1 instanceof ViewGroup) {
         ViewGroup var6 = (ViewGroup)var1;
         int var7 = var1.getScrollX();
         int var8 = var1.getScrollY();

         for(int var9 = var6.getChildCount() - 1; var9 >= 0; --var9) {
            View var10 = var6.getChildAt(var9);
            if (var4 + var7 >= var10.getLeft() && var4 + var7 < var10.getRight() && var5 + var8 >= var10.getTop() && var5 + var8 < var10.getBottom() && this.canScroll(var10, true, var3, var4 + var7 - var10.getLeft(), var5 + var8 - var10.getTop())) {
               var2 = true;
               return var2;
            }
         }
      }

      if (var2 && ViewCompat.canScrollHorizontally(var1, -var3)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean canScrollHorizontally(int var1) {
      boolean var2 = true;
      boolean var3 = true;
      boolean var4 = false;
      if (this.mAdapter == null) {
         var3 = var4;
      } else {
         int var5 = this.getClientWidth();
         int var6 = this.getScrollX();
         if (var1 < 0) {
            if (var6 <= (int)((float)var5 * this.mFirstOffset)) {
               var3 = false;
            }
         } else {
            var3 = var4;
            if (var1 > 0) {
               if (var6 < (int)((float)var5 * this.mLastOffset)) {
                  var3 = var2;
               } else {
                  var3 = false;
               }
            }
         }
      }

      return var3;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 instanceof ViewPager.LayoutParams && super.checkLayoutParams(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void clearOnPageChangeListeners() {
      if (this.mOnPageChangeListeners != null) {
         this.mOnPageChangeListeners.clear();
      }

   }

   public void computeScroll() {
      this.mIsScrollStarted = true;
      if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
         int var1 = this.getScrollX();
         int var2 = this.getScrollY();
         int var3 = this.mScroller.getCurrX();
         int var4 = this.mScroller.getCurrY();
         if (var1 != var3 || var2 != var4) {
            this.scrollTo(var3, var4);
            if (!this.pageScrolled(var3)) {
               this.mScroller.abortAnimation();
               this.scrollTo(0, var4);
            }
         }

         ViewCompat.postInvalidateOnAnimation(this);
      } else {
         this.completeScroll(true);
      }

   }

   void dataSetChanged() {
      int var1 = this.mAdapter.getCount();
      this.mExpectedAdapterCount = var1;
      boolean var2;
      if (this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var3 = this.mCurItem;
      boolean var4 = false;

      int var8;
      for(int var5 = 0; var5 < this.mItems.size(); var3 = var8) {
         ViewPager.ItemInfo var6 = (ViewPager.ItemInfo)this.mItems.get(var5);
         int var7 = this.mAdapter.getItemPosition(var6.object);
         boolean var9;
         int var10;
         if (var7 == -1) {
            var8 = var3;
            var9 = var4;
            var10 = var5;
         } else if (var7 == -2) {
            this.mItems.remove(var5);
            var7 = var5 - 1;
            boolean var13 = var4;
            if (!var4) {
               this.mAdapter.startUpdate((ViewGroup)this);
               var13 = true;
            }

            this.mAdapter.destroyItem((ViewGroup)this, var6.position, var6.object);
            var2 = true;
            var10 = var7;
            var9 = var13;
            var8 = var3;
            if (this.mCurItem == var6.position) {
               var8 = Math.max(0, Math.min(this.mCurItem, var1 - 1));
               var2 = true;
               var10 = var7;
               var9 = var13;
            }
         } else {
            var10 = var5;
            var9 = var4;
            var8 = var3;
            if (var6.position != var7) {
               if (var6.position == this.mCurItem) {
                  var3 = var7;
               }

               var6.position = var7;
               var2 = true;
               var10 = var5;
               var9 = var4;
               var8 = var3;
            }
         }

         var5 = var10 + 1;
         var4 = var9;
      }

      if (var4) {
         this.mAdapter.finishUpdate((ViewGroup)this);
      }

      Collections.sort(this.mItems, COMPARATOR);
      if (var2) {
         int var12 = this.getChildCount();

         for(int var11 = 0; var11 < var12; ++var11) {
            ViewPager.LayoutParams var14 = (ViewPager.LayoutParams)this.getChildAt(var11).getLayoutParams();
            if (!var14.isDecor) {
               var14.widthFactor = 0.0F;
            }
         }

         this.setCurrentItemInternal(var3, false, true);
         this.requestLayout();
      }

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

   public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
      boolean var2;
      if (var1.getEventType() == 4096) {
         var2 = super.dispatchPopulateAccessibilityEvent(var1);
      } else {
         int var3 = this.getChildCount();
         int var4 = 0;

         while(true) {
            if (var4 >= var3) {
               var2 = false;
               break;
            }

            View var5 = this.getChildAt(var4);
            if (var5.getVisibility() == 0) {
               ViewPager.ItemInfo var6 = this.infoForChild(var5);
               if (var6 != null && var6.position == this.mCurItem && var5.dispatchPopulateAccessibilityEvent(var1)) {
                  var2 = true;
                  break;
               }
            }

            ++var4;
         }
      }

      return var2;
   }

   float distanceInfluenceForSnapDuration(float var1) {
      return (float)Math.sin((double)((float)((double)(var1 - 0.5F) * 0.4712389167638204D)));
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      boolean var2 = false;
      boolean var3 = false;
      int var4 = this.getOverScrollMode();
      if (var4 != 0 && (var4 != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1)) {
         this.mLeftEdge.finish();
         this.mRightEdge.finish();
      } else {
         int var8;
         if (!this.mLeftEdge.isFinished()) {
            var8 = var1.save();
            var4 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            int var9 = this.getWidth();
            var1.rotate(270.0F);
            var1.translate((float)(-var4 + this.getPaddingTop()), this.mFirstOffset * (float)var9);
            this.mLeftEdge.setSize(var4, var9);
            var3 = false | this.mLeftEdge.draw(var1);
            var1.restoreToCount(var8);
         }

         var2 = var3;
         if (!this.mRightEdge.isFinished()) {
            var4 = var1.save();
            int var5 = this.getWidth();
            int var6 = this.getHeight();
            int var7 = this.getPaddingTop();
            var8 = this.getPaddingBottom();
            var1.rotate(90.0F);
            var1.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0F) * (float)var5);
            this.mRightEdge.setSize(var6 - var7 - var8, var5);
            var2 = var3 | this.mRightEdge.draw(var1);
            var1.restoreToCount(var4);
         }
      }

      if (var2) {
         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      Drawable var1 = this.mMarginDrawable;
      if (var1 != null && var1.isStateful()) {
         var1.setState(this.getDrawableState());
      }

   }

   public void endFakeDrag() {
      if (!this.mFakeDragging) {
         throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
      } else {
         if (this.mAdapter != null) {
            VelocityTracker var1 = this.mVelocityTracker;
            var1.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            int var2 = (int)VelocityTrackerCompat.getXVelocity(var1, this.mActivePointerId);
            this.mPopulatePending = true;
            int var3 = this.getClientWidth();
            int var4 = this.getScrollX();
            ViewPager.ItemInfo var5 = this.infoForCurrentScrollPosition();
            this.setCurrentItemInternal(this.determineTargetPage(var5.position, ((float)var4 / (float)var3 - var5.offset) / var5.widthFactor, var2, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, var2);
         }

         this.endDrag();
         this.mFakeDragging = false;
      }
   }

   public boolean executeKeyEvent(KeyEvent var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1.getAction() == 0) {
         switch(var1.getKeyCode()) {
         case 21:
            var3 = this.arrowScroll(17);
            break;
         case 22:
            var3 = this.arrowScroll(66);
            break;
         case 61:
            var3 = var2;
            if (VERSION.SDK_INT >= 11) {
               if (KeyEventCompat.hasNoModifiers(var1)) {
                  var3 = this.arrowScroll(2);
               } else {
                  var3 = var2;
                  if (KeyEventCompat.hasModifiers(var1, 1)) {
                     var3 = this.arrowScroll(1);
                  }
               }
            }
            break;
         default:
            var3 = var2;
         }
      }

      return var3;
   }

   public void fakeDragBy(float var1) {
      if (!this.mFakeDragging) {
         throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
      } else {
         if (this.mAdapter != null) {
            this.mLastMotionX += var1;
            float var2 = (float)this.getScrollX() - var1;
            int var3 = this.getClientWidth();
            var1 = (float)var3 * this.mFirstOffset;
            float var4 = (float)var3 * this.mLastOffset;
            ViewPager.ItemInfo var5 = (ViewPager.ItemInfo)this.mItems.get(0);
            ViewPager.ItemInfo var6 = (ViewPager.ItemInfo)this.mItems.get(this.mItems.size() - 1);
            if (var5.position != 0) {
               var1 = var5.offset * (float)var3;
            }

            if (var6.position != this.mAdapter.getCount() - 1) {
               var4 = var6.offset * (float)var3;
            }

            if (var2 >= var1) {
               var1 = var2;
               if (var2 > var4) {
                  var1 = var4;
               }
            }

            this.mLastMotionX += var1 - (float)((int)var1);
            this.scrollTo((int)var1, this.getScrollY());
            this.pageScrolled((int)var1);
            long var7 = SystemClock.uptimeMillis();
            MotionEvent var9 = MotionEvent.obtain(this.mFakeDragBeginTime, var7, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(var9);
            var9.recycle();
         }

      }
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new ViewPager.LayoutParams();
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new ViewPager.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return this.generateDefaultLayoutParams();
   }

   public PagerAdapter getAdapter() {
      return this.mAdapter;
   }

   protected int getChildDrawingOrder(int var1, int var2) {
      if (this.mDrawingOrder == 2) {
         var2 = var1 - 1 - var2;
      }

      return ((ViewPager.LayoutParams)((View)this.mDrawingOrderedChildren.get(var2)).getLayoutParams()).childIndex;
   }

   public int getCurrentItem() {
      return this.mCurItem;
   }

   public int getOffscreenPageLimit() {
      return this.mOffscreenPageLimit;
   }

   public int getPageMargin() {
      return this.mPageMargin;
   }

   ViewPager.ItemInfo infoForAnyChild(View var1) {
      while(true) {
         ViewParent var2 = var1.getParent();
         ViewPager.ItemInfo var3;
         if (var2 != this) {
            if (var2 != null && var2 instanceof View) {
               var1 = (View)var2;
               continue;
            }

            var3 = null;
         } else {
            var3 = this.infoForChild(var1);
         }

         return var3;
      }
   }

   ViewPager.ItemInfo infoForChild(View var1) {
      int var2 = 0;

      ViewPager.ItemInfo var4;
      while(true) {
         if (var2 >= this.mItems.size()) {
            var4 = null;
            break;
         }

         ViewPager.ItemInfo var3 = (ViewPager.ItemInfo)this.mItems.get(var2);
         if (this.mAdapter.isViewFromObject(var1, var3.object)) {
            var4 = var3;
            break;
         }

         ++var2;
      }

      return var4;
   }

   ViewPager.ItemInfo infoForPosition(int var1) {
      int var2 = 0;

      ViewPager.ItemInfo var3;
      while(true) {
         if (var2 >= this.mItems.size()) {
            var3 = null;
            break;
         }

         var3 = (ViewPager.ItemInfo)this.mItems.get(var2);
         if (var3.position == var1) {
            break;
         }

         ++var2;
      }

      return var3;
   }

   void initViewPager() {
      this.setWillNotDraw(false);
      this.setDescendantFocusability(262144);
      this.setFocusable(true);
      Context var1 = this.getContext();
      this.mScroller = new Scroller(var1, sInterpolator);
      ViewConfiguration var2 = ViewConfiguration.get(var1);
      float var3 = var1.getResources().getDisplayMetrics().density;
      this.mTouchSlop = var2.getScaledPagingTouchSlop();
      this.mMinimumVelocity = (int)(400.0F * var3);
      this.mMaximumVelocity = var2.getScaledMaximumFlingVelocity();
      this.mLeftEdge = new EdgeEffectCompat(var1);
      this.mRightEdge = new EdgeEffectCompat(var1);
      this.mFlingDistance = (int)(25.0F * var3);
      this.mCloseEnough = (int)(2.0F * var3);
      this.mDefaultGutterSize = (int)(16.0F * var3);
      ViewCompat.setAccessibilityDelegate(this, new ViewPager.MyAccessibilityDelegate());
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         private final Rect mTempRect = new Rect();

         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            WindowInsetsCompat var6 = ViewCompat.onApplyWindowInsets(var1, var2);
            if (!var6.isConsumed()) {
               Rect var3 = this.mTempRect;
               var3.left = var6.getSystemWindowInsetLeft();
               var3.top = var6.getSystemWindowInsetTop();
               var3.right = var6.getSystemWindowInsetRight();
               var3.bottom = var6.getSystemWindowInsetBottom();
               int var4 = 0;

               for(int var5 = ViewPager.this.getChildCount(); var4 < var5; ++var4) {
                  var2 = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(var4), var6);
                  var3.left = Math.min(var2.getSystemWindowInsetLeft(), var3.left);
                  var3.top = Math.min(var2.getSystemWindowInsetTop(), var3.top);
                  var3.right = Math.min(var2.getSystemWindowInsetRight(), var3.right);
                  var3.bottom = Math.min(var2.getSystemWindowInsetBottom(), var3.bottom);
               }

               var6 = var6.replaceSystemWindowInsets(var3.left, var3.top, var3.right, var3.bottom);
            }

            return var6;
         }
      });
   }

   public boolean isFakeDragging() {
      return this.mFakeDragging;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      this.removeCallbacks(this.mEndScrollRunnable);
      if (this.mScroller != null && !this.mScroller.isFinished()) {
         this.mScroller.abortAnimation();
      }

      super.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
         int var2 = this.getScrollX();
         int var3 = this.getWidth();
         float var4 = (float)this.mPageMargin / (float)var3;
         int var5 = 0;
         ViewPager.ItemInfo var6 = (ViewPager.ItemInfo)this.mItems.get(0);
         float var7 = var6.offset;
         int var8 = this.mItems.size();
         int var9 = var6.position;

         for(int var10 = ((ViewPager.ItemInfo)this.mItems.get(var8 - 1)).position; var9 < var10; ++var9) {
            while(var9 > var6.position && var5 < var8) {
               ArrayList var13 = this.mItems;
               ++var5;
               var6 = (ViewPager.ItemInfo)var13.get(var5);
            }

            float var11;
            if (var9 == var6.position) {
               var11 = (var6.offset + var6.widthFactor) * (float)var3;
               var7 = var6.offset + var6.widthFactor + var4;
            } else {
               float var12 = this.mAdapter.getPageWidth(var9);
               var11 = (var7 + var12) * (float)var3;
               var7 += var12 + var4;
            }

            if ((float)this.mPageMargin + var11 > (float)var2) {
               this.mMarginDrawable.setBounds(Math.round(var11), this.mTopPageBounds, Math.round((float)this.mPageMargin + var11), this.mBottomPageBounds);
               this.mMarginDrawable.draw(var1);
            }

            if (var11 > (float)(var2 + var3)) {
               break;
            }
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var2 = var1.getAction() & 255;
      boolean var3;
      if (var2 != 3 && var2 != 1) {
         if (var2 != 0) {
            if (this.mIsBeingDragged) {
               var3 = true;
               return var3;
            }

            if (this.mIsUnableToDrag) {
               var3 = false;
               return var3;
            }
         }

         float var5;
         switch(var2) {
         case 0:
            var5 = var1.getX();
            this.mInitialMotionX = var5;
            this.mLastMotionX = var5;
            var5 = var1.getY();
            this.mInitialMotionY = var5;
            this.mLastMotionY = var5;
            this.mActivePointerId = var1.getPointerId(0);
            this.mIsUnableToDrag = false;
            this.mIsScrollStarted = true;
            this.mScroller.computeScrollOffset();
            if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
               this.mScroller.abortAnimation();
               this.mPopulatePending = false;
               this.populate();
               this.mIsBeingDragged = true;
               this.requestParentDisallowInterceptTouchEvent(true);
               this.setScrollState(1);
            } else {
               this.completeScroll(false);
               this.mIsBeingDragged = false;
            }
            break;
         case 2:
            var2 = this.mActivePointerId;
            if (var2 != -1) {
               var2 = var1.findPointerIndex(var2);
               float var4 = var1.getX(var2);
               var5 = var4 - this.mLastMotionX;
               float var6 = Math.abs(var5);
               float var7 = var1.getY(var2);
               float var8 = Math.abs(var7 - this.mInitialMotionY);
               if (var5 != 0.0F && !this.isGutterDrag(this.mLastMotionX, var5) && this.canScroll(this, false, (int)var5, (int)var4, (int)var7)) {
                  this.mLastMotionX = var4;
                  this.mLastMotionY = var7;
                  this.mIsUnableToDrag = true;
                  var3 = false;
                  return var3;
               }

               if (var6 > (float)this.mTouchSlop && 0.5F * var6 > var8) {
                  this.mIsBeingDragged = true;
                  this.requestParentDisallowInterceptTouchEvent(true);
                  this.setScrollState(1);
                  if (var5 > 0.0F) {
                     var5 = this.mInitialMotionX + (float)this.mTouchSlop;
                  } else {
                     var5 = this.mInitialMotionX - (float)this.mTouchSlop;
                  }

                  this.mLastMotionX = var5;
                  this.mLastMotionY = var7;
                  this.setScrollingCacheEnabled(true);
               } else if (var8 > (float)this.mTouchSlop) {
                  this.mIsUnableToDrag = true;
               }

               if (this.mIsBeingDragged && this.performDrag(var4)) {
                  ViewCompat.postInvalidateOnAnimation(this);
               }
            }
            break;
         case 6:
            this.onSecondaryPointerUp(var1);
         }

         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         var3 = this.mIsBeingDragged;
      } else {
         this.resetTouch();
         var3 = false;
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = this.getChildCount();
      int var7 = var4 - var2;
      int var8 = var5 - var3;
      var3 = this.getPaddingLeft();
      var2 = this.getPaddingTop();
      int var9 = this.getPaddingRight();
      var5 = this.getPaddingBottom();
      int var10 = this.getScrollX();
      int var11 = 0;

      View var13;
      int var16;
      for(int var12 = 0; var12 < var6; var2 = var4) {
         var13 = this.getChildAt(var12);
         int var14 = var11;
         int var15 = var5;
         var16 = var3;
         int var17 = var9;
         var4 = var2;
         if (var13.getVisibility() != 8) {
            ViewPager.LayoutParams var18 = (ViewPager.LayoutParams)var13.getLayoutParams();
            var14 = var11;
            var15 = var5;
            var16 = var3;
            var17 = var9;
            var4 = var2;
            if (var18.isDecor) {
               var4 = var18.gravity;
               var17 = var18.gravity;
               switch(var4 & 7) {
               case 1:
                  var4 = Math.max((var7 - var13.getMeasuredWidth()) / 2, var3);
                  var16 = var3;
                  break;
               case 2:
               case 4:
               default:
                  var4 = var3;
                  var16 = var3;
                  break;
               case 3:
                  var4 = var3;
                  var16 = var3 + var13.getMeasuredWidth();
                  break;
               case 5:
                  var4 = var7 - var9 - var13.getMeasuredWidth();
                  var9 += var13.getMeasuredWidth();
                  var16 = var3;
               }

               switch(var17 & 112) {
               case 16:
                  var3 = Math.max((var8 - var13.getMeasuredHeight()) / 2, var2);
                  break;
               case 48:
                  var3 = var2;
                  var2 += var13.getMeasuredHeight();
                  break;
               case 80:
                  var3 = var8 - var5 - var13.getMeasuredHeight();
                  var5 += var13.getMeasuredHeight();
                  break;
               default:
                  var3 = var2;
               }

               var4 += var10;
               var13.layout(var4, var3, var13.getMeasuredWidth() + var4, var13.getMeasuredHeight() + var3);
               var14 = var11 + 1;
               var4 = var2;
               var17 = var9;
               var15 = var5;
            }
         }

         ++var12;
         var11 = var14;
         var5 = var15;
         var3 = var16;
         var9 = var17;
      }

      var9 = var7 - var3 - var9;

      for(var4 = 0; var4 < var6; ++var4) {
         var13 = this.getChildAt(var4);
         if (var13.getVisibility() != 8) {
            ViewPager.LayoutParams var19 = (ViewPager.LayoutParams)var13.getLayoutParams();
            if (!var19.isDecor) {
               ViewPager.ItemInfo var20 = this.infoForChild(var13);
               if (var20 != null) {
                  var16 = var3 + (int)((float)var9 * var20.offset);
                  if (var19.needsMeasure) {
                     var19.needsMeasure = false;
                     var13.measure(MeasureSpec.makeMeasureSpec((int)((float)var9 * var19.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec(var8 - var2 - var5, 1073741824));
                  }

                  var13.layout(var16, var2, var13.getMeasuredWidth() + var16, var13.getMeasuredHeight() + var2);
               }
            }
         }
      }

      this.mTopPageBounds = var2;
      this.mBottomPageBounds = var8 - var5;
      this.mDecorChildCount = var11;
      if (this.mFirstLayout) {
         this.scrollToItem(this.mCurItem, false, 0, false);
      }

      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      this.setMeasuredDimension(getDefaultSize(0, var1), getDefaultSize(0, var2));
      var1 = this.getMeasuredWidth();
      this.mGutterSize = Math.min(var1 / 10, this.mDefaultGutterSize);
      var1 = var1 - this.getPaddingLeft() - this.getPaddingRight();
      var2 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
      int var3 = this.getChildCount();

      View var5;
      int var6;
      int var7;
      ViewPager.LayoutParams var8;
      for(int var4 = 0; var4 < var3; var1 = var7) {
         var5 = this.getChildAt(var4);
         var6 = var2;
         var7 = var1;
         if (var5.getVisibility() != 8) {
            var8 = (ViewPager.LayoutParams)var5.getLayoutParams();
            var6 = var2;
            var7 = var1;
            if (var8 != null) {
               var6 = var2;
               var7 = var1;
               if (var8.isDecor) {
                  var7 = var8.gravity & 7;
                  int var9 = var8.gravity & 112;
                  int var10 = Integer.MIN_VALUE;
                  var6 = Integer.MIN_VALUE;
                  boolean var15;
                  if (var9 != 48 && var9 != 80) {
                     var15 = false;
                  } else {
                     var15 = true;
                  }

                  boolean var11;
                  if (var7 != 3 && var7 != 5) {
                     var11 = false;
                  } else {
                     var11 = true;
                  }

                  if (var15) {
                     var7 = 1073741824;
                  } else {
                     var7 = var10;
                     if (var11) {
                        var6 = 1073741824;
                        var7 = var10;
                     }
                  }

                  int var13 = var1;
                  int var14;
                  if (var8.width != -2) {
                     var14 = 1073741824;
                     var7 = var14;
                     var13 = var1;
                     if (var8.width != -1) {
                        var13 = var8.width;
                        var7 = var14;
                     }
                  }

                  int var12 = var6;
                  var6 = var2;
                  if (var8.height != -2) {
                     var14 = 1073741824;
                     var12 = var14;
                     var6 = var2;
                     if (var8.height != -1) {
                        var6 = var8.height;
                        var12 = var14;
                     }
                  }

                  var5.measure(MeasureSpec.makeMeasureSpec(var13, var7), MeasureSpec.makeMeasureSpec(var6, var12));
                  if (var15) {
                     var6 = var2 - var5.getMeasuredHeight();
                     var7 = var1;
                  } else {
                     var6 = var2;
                     var7 = var1;
                     if (var11) {
                        var7 = var1 - var5.getMeasuredWidth();
                        var6 = var2;
                     }
                  }
               }
            }
         }

         ++var4;
         var2 = var6;
      }

      this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(var1, 1073741824);
      this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(var2, 1073741824);
      this.mInLayout = true;
      this.populate();
      this.mInLayout = false;
      var6 = this.getChildCount();

      for(var2 = 0; var2 < var6; ++var2) {
         var5 = this.getChildAt(var2);
         if (var5.getVisibility() != 8) {
            var8 = (ViewPager.LayoutParams)var5.getLayoutParams();
            if (var8 == null || !var8.isDecor) {
               var5.measure(MeasureSpec.makeMeasureSpec((int)((float)var1 * var8.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
            }
         }
      }

   }

   @CallSuper
   protected void onPageScrolled(int var1, float var2, int var3) {
      int var5;
      if (this.mDecorChildCount > 0) {
         int var4 = this.getScrollX();
         var5 = this.getPaddingLeft();
         int var6 = this.getPaddingRight();
         int var7 = this.getWidth();
         int var8 = this.getChildCount();

         int var12;
         for(int var9 = 0; var9 < var8; var6 = var12) {
            View var10 = this.getChildAt(var9);
            ViewPager.LayoutParams var11 = (ViewPager.LayoutParams)var10.getLayoutParams();
            int var13;
            if (!var11.isDecor) {
               var12 = var6;
               var13 = var5;
            } else {
               switch(var11.gravity & 7) {
               case 1:
                  var12 = Math.max((var7 - var10.getMeasuredWidth()) / 2, var5);
                  break;
               case 2:
               case 4:
               default:
                  var12 = var5;
                  break;
               case 3:
                  var12 = var5;
                  var5 += var10.getWidth();
                  break;
               case 5:
                  var12 = var7 - var6 - var10.getMeasuredWidth();
                  var6 += var10.getMeasuredWidth();
               }

               int var14 = var12 + var4 - var10.getLeft();
               var13 = var5;
               var12 = var6;
               if (var14 != 0) {
                  var10.offsetLeftAndRight(var14);
                  var13 = var5;
                  var12 = var6;
               }
            }

            ++var9;
            var5 = var13;
         }
      }

      this.dispatchOnPageScrolled(var1, var2, var3);
      if (this.mPageTransformer != null) {
         var3 = this.getScrollX();
         var5 = this.getChildCount();

         for(var1 = 0; var1 < var5; ++var1) {
            View var15 = this.getChildAt(var1);
            if (!((ViewPager.LayoutParams)var15.getLayoutParams()).isDecor) {
               var2 = (float)(var15.getLeft() - var3) / (float)this.getClientWidth();
               this.mPageTransformer.transformPage(var15, var2);
            }
         }
      }

      this.mCalledSuper = true;
   }

   protected boolean onRequestFocusInDescendants(int var1, Rect var2) {
      int var3 = this.getChildCount();
      int var4;
      byte var5;
      if ((var1 & 2) != 0) {
         var4 = 0;
         var5 = 1;
      } else {
         var4 = var3 - 1;
         var5 = -1;
         var3 = -1;
      }

      boolean var8;
      while(true) {
         if (var4 == var3) {
            var8 = false;
            break;
         }

         View var6 = this.getChildAt(var4);
         if (var6.getVisibility() == 0) {
            ViewPager.ItemInfo var7 = this.infoForChild(var6);
            if (var7 != null && var7.position == this.mCurItem && var6.requestFocus(var1, var2)) {
               var8 = true;
               break;
            }
         }

         var4 += var5;
      }

      return var8;
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof ViewPager.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         ViewPager.SavedState var2 = (ViewPager.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         if (this.mAdapter != null) {
            this.mAdapter.restoreState(var2.adapterState, var2.loader);
            this.setCurrentItemInternal(var2.position, false, true);
         } else {
            this.mRestoredCurItem = var2.position;
            this.mRestoredAdapterState = var2.adapterState;
            this.mRestoredClassLoader = var2.loader;
         }
      }

   }

   public Parcelable onSaveInstanceState() {
      ViewPager.SavedState var1 = new ViewPager.SavedState(super.onSaveInstanceState());
      var1.position = this.mCurItem;
      if (this.mAdapter != null) {
         var1.adapterState = this.mAdapter.saveState();
      }

      return var1;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3) {
         this.recomputeScrollPosition(var1, var3, this.mPageMargin, this.mPageMargin);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2;
      if (this.mFakeDragging) {
         var2 = true;
      } else if (var1.getAction() == 0 && var1.getEdgeFlags() != 0) {
         var2 = false;
      } else if (this.mAdapter != null && this.mAdapter.getCount() != 0) {
         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         int var3 = var1.getAction();
         boolean var4 = false;
         var2 = var4;
         float var5;
         switch(var3 & 255) {
         case 0:
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            this.populate();
            var5 = var1.getX();
            this.mInitialMotionX = var5;
            this.mLastMotionX = var5;
            var5 = var1.getY();
            this.mInitialMotionY = var5;
            this.mLastMotionY = var5;
            this.mActivePointerId = var1.getPointerId(0);
            var2 = var4;
            break;
         case 1:
            var2 = var4;
            if (this.mIsBeingDragged) {
               VelocityTracker var12 = this.mVelocityTracker;
               var12.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
               var3 = (int)VelocityTrackerCompat.getXVelocity(var12, this.mActivePointerId);
               this.mPopulatePending = true;
               int var10 = this.getClientWidth();
               int var11 = this.getScrollX();
               ViewPager.ItemInfo var13 = this.infoForCurrentScrollPosition();
               var5 = (float)this.mPageMargin / (float)var10;
               this.setCurrentItemInternal(this.determineTargetPage(var13.position, ((float)var11 / (float)var10 - var13.offset) / (var13.widthFactor + var5), var3, (int)(var1.getX(var1.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, var3);
               var2 = this.resetTouch();
            }
            break;
         case 2:
            if (!this.mIsBeingDragged) {
               var3 = var1.findPointerIndex(this.mActivePointerId);
               if (var3 == -1) {
                  var2 = this.resetTouch();
                  break;
               }

               var5 = var1.getX(var3);
               float var6 = Math.abs(var5 - this.mLastMotionX);
               float var7 = var1.getY(var3);
               float var8 = Math.abs(var7 - this.mLastMotionY);
               if (var6 > (float)this.mTouchSlop && var6 > var8) {
                  this.mIsBeingDragged = true;
                  this.requestParentDisallowInterceptTouchEvent(true);
                  if (var5 - this.mInitialMotionX > 0.0F) {
                     var5 = this.mInitialMotionX + (float)this.mTouchSlop;
                  } else {
                     var5 = this.mInitialMotionX - (float)this.mTouchSlop;
                  }

                  this.mLastMotionX = var5;
                  this.mLastMotionY = var7;
                  this.setScrollState(1);
                  this.setScrollingCacheEnabled(true);
                  ViewParent var9 = this.getParent();
                  if (var9 != null) {
                     var9.requestDisallowInterceptTouchEvent(true);
                  }
               }
            }

            var2 = var4;
            if (this.mIsBeingDragged) {
               var2 = false | this.performDrag(var1.getX(var1.findPointerIndex(this.mActivePointerId)));
            }
            break;
         case 3:
            var2 = var4;
            if (this.mIsBeingDragged) {
               this.scrollToItem(this.mCurItem, true, 0, false);
               var2 = this.resetTouch();
            }
         case 4:
            break;
         case 5:
            var3 = MotionEventCompat.getActionIndex(var1);
            this.mLastMotionX = var1.getX(var3);
            this.mActivePointerId = var1.getPointerId(var3);
            var2 = var4;
            break;
         case 6:
            this.onSecondaryPointerUp(var1);
            this.mLastMotionX = var1.getX(var1.findPointerIndex(this.mActivePointerId));
            var2 = var4;
            break;
         default:
            var2 = var4;
         }

         if (var2) {
            ViewCompat.postInvalidateOnAnimation(this);
         }

         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   boolean pageLeft() {
      boolean var1 = true;
      if (this.mCurItem > 0) {
         this.setCurrentItem(this.mCurItem - 1, true);
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean pageRight() {
      boolean var1 = true;
      if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
         this.setCurrentItem(this.mCurItem + 1, true);
      } else {
         var1 = false;
      }

      return var1;
   }

   void populate() {
      this.populate(this.mCurItem);
   }

   void populate(int var1) {
      ViewPager.ItemInfo var2 = null;
      if (this.mCurItem != var1) {
         var2 = this.infoForPosition(this.mCurItem);
         this.mCurItem = var1;
      }

      if (this.mAdapter == null) {
         this.sortChildDrawingOrder();
      } else if (this.mPopulatePending) {
         this.sortChildDrawingOrder();
      } else if (this.getWindowToken() != null) {
         this.mAdapter.startUpdate((ViewGroup)this);
         var1 = this.mOffscreenPageLimit;
         int var3 = Math.max(0, this.mCurItem - var1);
         int var4 = this.mAdapter.getCount();
         int var5 = Math.min(var4 - 1, this.mCurItem + var1);
         if (var4 != this.mExpectedAdapterCount) {
            String var23;
            try {
               var23 = this.getResources().getResourceName(this.getId());
            } catch (NotFoundException var17) {
               var23 = Integer.toHexString(this.getId());
            }

            throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + var4 + " Pager id: " + var23 + " Pager class: " + this.getClass() + " Problematic adapter: " + this.mAdapter.getClass());
         }

         ViewPager.ItemInfo var7 = null;
         var1 = 0;

         ViewPager.ItemInfo var6;
         ViewPager.ItemInfo var8;
         while(true) {
            var6 = var7;
            if (var1 >= this.mItems.size()) {
               break;
            }

            var8 = (ViewPager.ItemInfo)this.mItems.get(var1);
            if (var8.position >= this.mCurItem) {
               var6 = var7;
               if (var8.position == this.mCurItem) {
                  var6 = var8;
               }
               break;
            }

            ++var1;
         }

         var7 = var6;
         if (var6 == null) {
            var7 = var6;
            if (var4 > 0) {
               var7 = this.addNewItem(this.mCurItem, var1);
            }
         }

         int var15;
         if (var7 != null) {
            float var9 = 0.0F;
            int var10 = var1 - 1;
            if (var10 >= 0) {
               var6 = (ViewPager.ItemInfo)this.mItems.get(var10);
            } else {
               var6 = null;
            }

            int var11 = this.getClientWidth();
            float var12;
            if (var11 <= 0) {
               var12 = 0.0F;
            } else {
               var12 = 2.0F - var7.widthFactor + (float)this.getPaddingLeft() / (float)var11;
            }

            int var13 = this.mCurItem - 1;
            var8 = var6;

            int var14;
            float var16;
            for(var14 = var1; var13 >= 0; var10 = var15) {
               if (var9 >= var12 && var13 < var3) {
                  if (var8 == null) {
                     break;
                  }

                  var1 = var14;
                  var16 = var9;
                  var6 = var8;
                  var15 = var10;
                  if (var13 == var8.position) {
                     var1 = var14;
                     var16 = var9;
                     var6 = var8;
                     var15 = var10;
                     if (!var8.scrolling) {
                        this.mItems.remove(var10);
                        this.mAdapter.destroyItem((ViewGroup)this, var13, var8.object);
                        var15 = var10 - 1;
                        var1 = var14 - 1;
                        if (var15 >= 0) {
                           var6 = (ViewPager.ItemInfo)this.mItems.get(var15);
                           var16 = var9;
                        } else {
                           var6 = null;
                           var16 = var9;
                        }
                     }
                  }
               } else if (var8 != null && var13 == var8.position) {
                  var16 = var9 + var8.widthFactor;
                  var15 = var10 - 1;
                  if (var15 >= 0) {
                     var6 = (ViewPager.ItemInfo)this.mItems.get(var15);
                  } else {
                     var6 = null;
                  }

                  var1 = var14;
               } else {
                  var16 = var9 + this.addNewItem(var13, var10 + 1).widthFactor;
                  var1 = var14 + 1;
                  if (var10 >= 0) {
                     var6 = (ViewPager.ItemInfo)this.mItems.get(var10);
                  } else {
                     var6 = null;
                  }

                  var15 = var10;
               }

               --var13;
               var14 = var1;
               var9 = var16;
               var8 = var6;
            }

            var9 = var7.widthFactor;
            var13 = var14 + 1;
            if (var9 < 2.0F) {
               if (var13 < this.mItems.size()) {
                  var6 = (ViewPager.ItemInfo)this.mItems.get(var13);
               } else {
                  var6 = null;
               }

               if (var11 <= 0) {
                  var12 = 0.0F;
               } else {
                  var12 = (float)this.getPaddingRight() / (float)var11 + 2.0F;
               }

               var15 = this.mCurItem + 1;

               for(var8 = var6; var15 < var4; var13 = var1) {
                  if (var9 >= var12 && var15 > var5) {
                     if (var8 == null) {
                        break;
                     }

                     var16 = var9;
                     var6 = var8;
                     var1 = var13;
                     if (var15 == var8.position) {
                        var16 = var9;
                        var6 = var8;
                        var1 = var13;
                        if (!var8.scrolling) {
                           this.mItems.remove(var13);
                           this.mAdapter.destroyItem((ViewGroup)this, var15, var8.object);
                           if (var13 < this.mItems.size()) {
                              var6 = (ViewPager.ItemInfo)this.mItems.get(var13);
                              var1 = var13;
                              var16 = var9;
                           } else {
                              var6 = null;
                              var16 = var9;
                              var1 = var13;
                           }
                        }
                     }
                  } else if (var8 != null && var15 == var8.position) {
                     var16 = var9 + var8.widthFactor;
                     var1 = var13 + 1;
                     if (var1 < this.mItems.size()) {
                        var6 = (ViewPager.ItemInfo)this.mItems.get(var1);
                     } else {
                        var6 = null;
                     }
                  } else {
                     var6 = this.addNewItem(var15, var13);
                     var1 = var13 + 1;
                     var16 = var9 + var6.widthFactor;
                     if (var1 < this.mItems.size()) {
                        var6 = (ViewPager.ItemInfo)this.mItems.get(var1);
                     } else {
                        var6 = null;
                     }
                  }

                  ++var15;
                  var9 = var16;
                  var8 = var6;
               }
            }

            this.calculatePageOffsets(var7, var14, var2);
         }

         PagerAdapter var18 = this.mAdapter;
         var1 = this.mCurItem;
         Object var20;
         if (var7 != null) {
            var20 = var7.object;
         } else {
            var20 = null;
         }

         var18.setPrimaryItem((ViewGroup)this, var1, var20);
         this.mAdapter.finishUpdate((ViewGroup)this);
         var15 = this.getChildCount();

         for(var1 = 0; var1 < var15; ++var1) {
            View var19 = this.getChildAt(var1);
            ViewPager.LayoutParams var21 = (ViewPager.LayoutParams)var19.getLayoutParams();
            var21.childIndex = var1;
            if (!var21.isDecor && var21.widthFactor == 0.0F) {
               var2 = this.infoForChild(var19);
               if (var2 != null) {
                  var21.widthFactor = var2.widthFactor;
                  var21.position = var2.position;
               }
            }
         }

         this.sortChildDrawingOrder();
         if (this.hasFocus()) {
            View var22 = this.findFocus();
            if (var22 != null) {
               var6 = this.infoForAnyChild(var22);
            } else {
               var6 = null;
            }

            if (var6 == null || var6.position != this.mCurItem) {
               for(var1 = 0; var1 < this.getChildCount(); ++var1) {
                  var22 = this.getChildAt(var1);
                  var2 = this.infoForChild(var22);
                  if (var2 != null && var2.position == this.mCurItem && var22.requestFocus(2)) {
                     break;
                  }
               }
            }
         }
      }

   }

   public void removeOnAdapterChangeListener(@NonNull ViewPager.OnAdapterChangeListener var1) {
      if (this.mAdapterChangeListeners != null) {
         this.mAdapterChangeListeners.remove(var1);
      }

   }

   public void removeOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      if (this.mOnPageChangeListeners != null) {
         this.mOnPageChangeListeners.remove(var1);
      }

   }

   public void removeView(View var1) {
      if (this.mInLayout) {
         this.removeViewInLayout(var1);
      } else {
         super.removeView(var1);
      }

   }

   public void setAdapter(PagerAdapter var1) {
      int var2;
      if (this.mAdapter != null) {
         this.mAdapter.setViewPagerObserver((DataSetObserver)null);
         this.mAdapter.startUpdate((ViewGroup)this);

         for(var2 = 0; var2 < this.mItems.size(); ++var2) {
            ViewPager.ItemInfo var3 = (ViewPager.ItemInfo)this.mItems.get(var2);
            this.mAdapter.destroyItem((ViewGroup)this, var3.position, var3.object);
         }

         this.mAdapter.finishUpdate((ViewGroup)this);
         this.mItems.clear();
         this.removeNonDecorViews();
         this.mCurItem = 0;
         this.scrollTo(0, 0);
      }

      PagerAdapter var6 = this.mAdapter;
      this.mAdapter = var1;
      this.mExpectedAdapterCount = 0;
      if (this.mAdapter != null) {
         if (this.mObserver == null) {
            this.mObserver = new ViewPager.PagerObserver();
         }

         this.mAdapter.setViewPagerObserver(this.mObserver);
         this.mPopulatePending = false;
         boolean var4 = this.mFirstLayout;
         this.mFirstLayout = true;
         this.mExpectedAdapterCount = this.mAdapter.getCount();
         if (this.mRestoredCurItem >= 0) {
            this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
            this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
            this.mRestoredCurItem = -1;
            this.mRestoredAdapterState = null;
            this.mRestoredClassLoader = null;
         } else if (!var4) {
            this.populate();
         } else {
            this.requestLayout();
         }
      }

      if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
         var2 = 0;

         for(int var5 = this.mAdapterChangeListeners.size(); var2 < var5; ++var2) {
            ((ViewPager.OnAdapterChangeListener)this.mAdapterChangeListeners.get(var2)).onAdapterChanged(this, var6, var1);
         }
      }

   }

   void setChildrenDrawingOrderEnabledCompat(boolean var1) {
      if (VERSION.SDK_INT >= 7) {
         if (this.mSetChildrenDrawingOrderEnabled == null) {
            try {
               this.mSetChildrenDrawingOrderEnabled = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
            } catch (NoSuchMethodException var4) {
               Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", var4);
            }
         }

         try {
            this.mSetChildrenDrawingOrderEnabled.invoke(this, var1);
         } catch (Exception var3) {
            Log.e("ViewPager", "Error changing children drawing order", var3);
         }
      }

   }

   public void setCurrentItem(int var1) {
      this.mPopulatePending = false;
      boolean var2;
      if (!this.mFirstLayout) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setCurrentItemInternal(var1, var2, false);
   }

   public void setCurrentItem(int var1, boolean var2) {
      this.mPopulatePending = false;
      this.setCurrentItemInternal(var1, var2, false);
   }

   void setCurrentItemInternal(int var1, boolean var2, boolean var3) {
      this.setCurrentItemInternal(var1, var2, var3, 0);
   }

   void setCurrentItemInternal(int var1, boolean var2, boolean var3, int var4) {
      boolean var5 = true;
      if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
         if (!var3 && this.mCurItem == var1 && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
         } else {
            int var6;
            if (var1 < 0) {
               var6 = 0;
            } else {
               var6 = var1;
               if (var1 >= this.mAdapter.getCount()) {
                  var6 = this.mAdapter.getCount() - 1;
               }
            }

            var1 = this.mOffscreenPageLimit;
            if (var6 > this.mCurItem + var1 || var6 < this.mCurItem - var1) {
               for(var1 = 0; var1 < this.mItems.size(); ++var1) {
                  ((ViewPager.ItemInfo)this.mItems.get(var1)).scrolling = true;
               }
            }

            if (this.mCurItem != var6) {
               var3 = var5;
            } else {
               var3 = false;
            }

            if (this.mFirstLayout) {
               this.mCurItem = var6;
               if (var3) {
                  this.dispatchOnPageSelected(var6);
               }

               this.requestLayout();
            } else {
               this.populate(var6);
               this.scrollToItem(var6, var2, var4, var3);
            }
         }
      } else {
         this.setScrollingCacheEnabled(false);
      }

   }

   ViewPager.OnPageChangeListener setInternalPageChangeListener(ViewPager.OnPageChangeListener var1) {
      ViewPager.OnPageChangeListener var2 = this.mInternalPageChangeListener;
      this.mInternalPageChangeListener = var1;
      return var2;
   }

   public void setOffscreenPageLimit(int var1) {
      int var2 = var1;
      if (var1 < 1) {
         Log.w("ViewPager", "Requested offscreen page limit " + var1 + " too small; defaulting to " + 1);
         var2 = 1;
      }

      if (var2 != this.mOffscreenPageLimit) {
         this.mOffscreenPageLimit = var2;
         this.populate();
      }

   }

   @Deprecated
   public void setOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      this.mOnPageChangeListener = var1;
   }

   public void setPageMargin(int var1) {
      int var2 = this.mPageMargin;
      this.mPageMargin = var1;
      int var3 = this.getWidth();
      this.recomputeScrollPosition(var3, var3, var1, var2);
      this.requestLayout();
   }

   public void setPageMarginDrawable(@DrawableRes int var1) {
      this.setPageMarginDrawable(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setPageMarginDrawable(Drawable var1) {
      this.mMarginDrawable = var1;
      if (var1 != null) {
         this.refreshDrawableState();
      }

      boolean var2;
      if (var1 == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setWillNotDraw(var2);
      this.invalidate();
   }

   public void setPageTransformer(boolean var1, ViewPager.PageTransformer var2) {
      this.setPageTransformer(var1, var2, 2);
   }

   public void setPageTransformer(boolean var1, ViewPager.PageTransformer var2, int var3) {
      byte var4 = 1;
      if (VERSION.SDK_INT >= 11) {
         boolean var5;
         if (var2 != null) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var6;
         if (this.mPageTransformer != null) {
            var6 = true;
         } else {
            var6 = false;
         }

         boolean var7;
         if (var5 != var6) {
            var7 = true;
         } else {
            var7 = false;
         }

         this.mPageTransformer = var2;
         this.setChildrenDrawingOrderEnabledCompat(var5);
         if (var5) {
            if (var1) {
               var4 = 2;
            }

            this.mDrawingOrder = var4;
            this.mPageTransformerLayerType = var3;
         } else {
            this.mDrawingOrder = 0;
         }

         if (var7) {
            this.populate();
         }
      }

   }

   void setScrollState(int var1) {
      if (this.mScrollState != var1) {
         this.mScrollState = var1;
         if (this.mPageTransformer != null) {
            boolean var2;
            if (var1 != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.enableLayers(var2);
         }

         this.dispatchOnScrollStateChanged(var1);
      }

   }

   void smoothScrollTo(int var1, int var2) {
      this.smoothScrollTo(var1, var2, 0);
   }

   void smoothScrollTo(int var1, int var2, int var3) {
      if (this.getChildCount() == 0) {
         this.setScrollingCacheEnabled(false);
      } else {
         boolean var4;
         if (this.mScroller != null && !this.mScroller.isFinished()) {
            var4 = true;
         } else {
            var4 = false;
         }

         int var11;
         if (var4) {
            if (this.mIsScrollStarted) {
               var11 = this.mScroller.getCurrX();
            } else {
               var11 = this.mScroller.getStartX();
            }

            this.mScroller.abortAnimation();
            this.setScrollingCacheEnabled(false);
         } else {
            var11 = this.getScrollX();
         }

         int var5 = this.getScrollY();
         int var6 = var1 - var11;
         var2 -= var5;
         if (var6 == 0 && var2 == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
         } else {
            this.setScrollingCacheEnabled(true);
            this.setScrollState(2);
            var1 = this.getClientWidth();
            int var7 = var1 / 2;
            float var8 = Math.min(1.0F, 1.0F * (float)Math.abs(var6) / (float)var1);
            float var9 = (float)var7;
            float var10 = (float)var7;
            var8 = this.distanceInfluenceForSnapDuration(var8);
            var3 = Math.abs(var3);
            if (var3 > 0) {
               var1 = Math.round(1000.0F * Math.abs((var9 + var10 * var8) / (float)var3)) * 4;
            } else {
               var10 = (float)var1;
               var9 = this.mAdapter.getPageWidth(this.mCurItem);
               var1 = (int)((1.0F + (float)Math.abs(var6) / ((float)this.mPageMargin + var10 * var9)) * 100.0F);
            }

            var1 = Math.min(var1, 600);
            this.mIsScrollStarted = false;
            this.mScroller.startScroll(var11, var5, var6, var2, var1);
            ViewCompat.postInvalidateOnAnimation(this);
         }
      }

   }

   protected boolean verifyDrawable(Drawable var1) {
      boolean var2;
      if (!super.verifyDrawable(var1) && var1 != this.mMarginDrawable) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   @Inherited
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.TYPE})
   public @interface DecorView {
   }

   static class ItemInfo {
      Object object;
      float offset;
      int position;
      boolean scrolling;
      float widthFactor;
   }

   public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
      int childIndex;
      public int gravity;
      public boolean isDecor;
      boolean needsMeasure;
      int position;
      float widthFactor = 0.0F;

      public LayoutParams() {
         super(-1, -1);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, ViewPager.LAYOUT_ATTRS);
         this.gravity = var3.getInteger(0, 48);
         var3.recycle();
      }
   }

   class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
      private boolean canScroll() {
         boolean var1 = true;
         if (ViewPager.this.mAdapter == null || ViewPager.this.mAdapter.getCount() <= 1) {
            var1 = false;
         }

         return var1;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(ViewPager.class.getName());
         AccessibilityRecordCompat var3 = AccessibilityEventCompat.asRecord(var2);
         var3.setScrollable(this.canScroll());
         if (var2.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
            var3.setItemCount(ViewPager.this.mAdapter.getCount());
            var3.setFromIndex(ViewPager.this.mCurItem);
            var3.setToIndex(ViewPager.this.mCurItem);
         }

      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         var2.setClassName(ViewPager.class.getName());
         var2.setScrollable(this.canScroll());
         if (ViewPager.this.canScrollHorizontally(1)) {
            var2.addAction(4096);
         }

         if (ViewPager.this.canScrollHorizontally(-1)) {
            var2.addAction(8192);
         }

      }

      public boolean performAccessibilityAction(View var1, int var2, Bundle var3) {
         boolean var4 = true;
         if (!super.performAccessibilityAction(var1, var2, var3)) {
            switch(var2) {
            case 4096:
               if (ViewPager.this.canScrollHorizontally(1)) {
                  ViewPager.this.setCurrentItem(ViewPager.this.mCurItem + 1);
               } else {
                  var4 = false;
               }
               break;
            case 8192:
               if (ViewPager.this.canScrollHorizontally(-1)) {
                  ViewPager.this.setCurrentItem(ViewPager.this.mCurItem - 1);
               } else {
                  var4 = false;
               }
               break;
            default:
               var4 = false;
            }
         }

         return var4;
      }
   }

   public interface OnAdapterChangeListener {
      void onAdapterChanged(@NonNull ViewPager var1, @Nullable PagerAdapter var2, @Nullable PagerAdapter var3);
   }

   public interface OnPageChangeListener {
      void onPageScrollStateChanged(int var1);

      void onPageScrolled(int var1, float var2, int var3);

      void onPageSelected(int var1);
   }

   public interface PageTransformer {
      void transformPage(View var1, float var2);
   }

   private class PagerObserver extends DataSetObserver {
      PagerObserver() {
      }

      public void onChanged() {
         ViewPager.this.dataSetChanged();
      }

      public void onInvalidated() {
         ViewPager.this.dataSetChanged();
      }
   }

   public static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks() {
         public ViewPager.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new ViewPager.SavedState(var1, var2);
         }

         public ViewPager.SavedState[] newArray(int var1) {
            return new ViewPager.SavedState[var1];
         }
      });
      Parcelable adapterState;
      ClassLoader loader;
      int position;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         ClassLoader var3 = var2;
         if (var2 == null) {
            var3 = this.getClass().getClassLoader();
         }

         this.position = var1.readInt();
         this.adapterState = var1.readParcelable(var3);
         this.loader = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.position);
         var1.writeParcelable(this.adapterState, var2);
      }
   }

   public static class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {
      public void onPageScrollStateChanged(int var1) {
      }

      public void onPageScrolled(int var1, float var2, int var3) {
      }

      public void onPageSelected(int var1) {
      }
   }

   static class ViewPositionComparator implements Comparator {
      public int compare(View var1, View var2) {
         ViewPager.LayoutParams var4 = (ViewPager.LayoutParams)var1.getLayoutParams();
         ViewPager.LayoutParams var5 = (ViewPager.LayoutParams)var2.getLayoutParams();
         int var3;
         if (var4.isDecor != var5.isDecor) {
            if (var4.isDecor) {
               var3 = 1;
            } else {
               var3 = -1;
            }
         } else {
            var3 = var4.position - var5.position;
         }

         return var3;
      }
   }
}
