package androidx.viewpager.widget;

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
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
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
import android.widget.EdgeEffect;
import android.widget.Scroller;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
   private EdgeEffect mLeftEdge;
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
   private EdgeEffect mRightEdge;
   private int mScrollState = 0;
   private Scroller mScroller;
   private boolean mScrollingCacheEnabled;
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
      int var9;
      float var10;
      if (var3 != null) {
         var5 = var3.position;
         var7 = var1.position;
         if (var5 < var7) {
            var8 = var3.offset + var3.widthFactor + var6;
            ++var5;

            for(var7 = 0; var5 <= var1.position && var7 < this.mItems.size(); var5 = var9 + 1) {
               var3 = (ViewPager.ItemInfo)this.mItems.get(var7);

               while(true) {
                  var9 = var5;
                  var10 = var8;
                  if (var5 <= var3.position) {
                     break;
                  }

                  var9 = var5;
                  var10 = var8;
                  if (var7 >= this.mItems.size() - 1) {
                     break;
                  }

                  ++var7;
                  var3 = (ViewPager.ItemInfo)this.mItems.get(var7);
               }

               while(var9 < var3.position) {
                  var10 += this.mAdapter.getPageWidth(var9) + var6;
                  ++var9;
               }

               var3.offset = var10;
               var8 = var10 + var3.widthFactor + var6;
            }
         } else if (var5 > var7) {
            var7 = this.mItems.size() - 1;
            var8 = var3.offset;
            --var5;

            while(var5 >= var1.position && var7 >= 0) {
               var3 = (ViewPager.ItemInfo)this.mItems.get(var7);

               while(true) {
                  var9 = var5;
                  var10 = var8;
                  if (var5 >= var3.position) {
                     break;
                  }

                  var9 = var5;
                  var10 = var8;
                  if (var7 <= 0) {
                     break;
                  }

                  --var7;
                  var3 = (ViewPager.ItemInfo)this.mItems.get(var7);
               }

               while(var9 > var3.position) {
                  var10 -= this.mAdapter.getPageWidth(var9) + var6;
                  --var9;
               }

               var8 = var10 - (var3.widthFactor + var6);
               var3.offset = var8;
               var5 = var9 - 1;
            }
         }
      }

      var9 = this.mItems.size();
      var8 = var1.offset;
      var7 = var1.position;
      var5 = var7 - 1;
      if (var7 == 0) {
         var10 = var8;
      } else {
         var10 = -3.4028235E38F;
      }

      this.mFirstOffset = var10;
      var7 = var1.position;
      --var4;
      if (var7 == var4) {
         var10 = var1.offset + var1.widthFactor - 1.0F;
      } else {
         var10 = Float.MAX_VALUE;
      }

      this.mLastOffset = var10;

      for(var7 = var2 - 1; var7 >= 0; --var5) {
         var3 = (ViewPager.ItemInfo)this.mItems.get(var7);

         while(true) {
            int var11 = var3.position;
            if (var5 <= var11) {
               var8 -= var3.widthFactor + var6;
               var3.offset = var8;
               if (var11 == 0) {
                  this.mFirstOffset = var8;
               }

               --var7;
               break;
            }

            var8 -= this.mAdapter.getPageWidth(var5) + var6;
            --var5;
         }
      }

      var8 = var1.offset + var1.widthFactor + var6;
      var7 = var1.position + 1;
      var5 = var2 + 1;

      for(var2 = var7; var5 < var9; ++var2) {
         var1 = (ViewPager.ItemInfo)this.mItems.get(var5);

         while(true) {
            var7 = var1.position;
            if (var2 >= var7) {
               if (var7 == var4) {
                  this.mLastOffset = var1.widthFactor + var8 - 1.0F;
               }

               var1.offset = var8;
               var8 += var1.widthFactor + var6;
               ++var5;
               break;
            }

            var8 += this.mAdapter.getPageWidth(var2) + var6;
            ++var2;
         }
      }

      this.mNeedCalculatePageOffsets = false;
   }

   private void completeScroll(boolean var1) {
      boolean var2;
      if (this.mScrollState == 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         this.setScrollingCacheEnabled(false);
         if (this.mScroller.isFinished() ^ true) {
            this.mScroller.abortAnimation();
            int var3 = this.getScrollX();
            int var4 = this.getScrollY();
            int var5 = this.mScroller.getCurrX();
            int var6 = this.mScroller.getCurrY();
            if (var3 != var5 || var4 != var6) {
               this.scrollTo(var5, var6);
               if (var5 != var3) {
                  this.pageScrolled(var5);
               }
            }
         }
      }

      this.mPopulatePending = false;
      byte var9 = 0;
      boolean var10 = var2;

      for(int var8 = var9; var8 < this.mItems.size(); ++var8) {
         ViewPager.ItemInfo var7 = (ViewPager.ItemInfo)this.mItems.get(var8);
         if (var7.scrolling) {
            var7.scrolling = false;
            var10 = true;
         }
      }

      if (var10) {
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
         float var5;
         if (var1 >= this.mCurItem) {
            var5 = 0.4F;
         } else {
            var5 = 0.6F;
         }

         var1 += (int)(var2 + var5);
      }

      var3 = var1;
      if (this.mItems.size() > 0) {
         ViewPager.ItemInfo var6 = (ViewPager.ItemInfo)this.mItems.get(0);
         ArrayList var7 = this.mItems;
         ViewPager.ItemInfo var8 = (ViewPager.ItemInfo)var7.get(var7.size() - 1);
         var3 = Math.max(var6.position, Math.min(var1, var8.position));
      }

      return var3;
   }

   private void dispatchOnPageScrolled(int var1, float var2, int var3) {
      ViewPager.OnPageChangeListener var4 = this.mOnPageChangeListener;
      if (var4 != null) {
         var4.onPageScrolled(var1, var2, var3);
      }

      List var7 = this.mOnPageChangeListeners;
      if (var7 != null) {
         int var5 = 0;

         for(int var6 = var7.size(); var5 < var6; ++var5) {
            var4 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var5);
            if (var4 != null) {
               var4.onPageScrolled(var1, var2, var3);
            }
         }
      }

      var4 = this.mInternalPageChangeListener;
      if (var4 != null) {
         var4.onPageScrolled(var1, var2, var3);
      }

   }

   private void dispatchOnPageSelected(int var1) {
      ViewPager.OnPageChangeListener var2 = this.mOnPageChangeListener;
      if (var2 != null) {
         var2.onPageSelected(var1);
      }

      List var5 = this.mOnPageChangeListeners;
      if (var5 != null) {
         int var3 = 0;

         for(int var4 = var5.size(); var3 < var4; ++var3) {
            var2 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var3);
            if (var2 != null) {
               var2.onPageSelected(var1);
            }
         }
      }

      var2 = this.mInternalPageChangeListener;
      if (var2 != null) {
         var2.onPageSelected(var1);
      }

   }

   private void dispatchOnScrollStateChanged(int var1) {
      ViewPager.OnPageChangeListener var2 = this.mOnPageChangeListener;
      if (var2 != null) {
         var2.onPageScrollStateChanged(var1);
      }

      List var5 = this.mOnPageChangeListeners;
      if (var5 != null) {
         int var3 = 0;

         for(int var4 = var5.size(); var3 < var4; ++var3) {
            var2 = (ViewPager.OnPageChangeListener)this.mOnPageChangeListeners.get(var3);
            if (var2 != null) {
               var2.onPageScrollStateChanged(var1);
            }
         }
      }

      var2 = this.mInternalPageChangeListener;
      if (var2 != null) {
         var2.onPageScrollStateChanged(var1);
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

         this.getChildAt(var3).setLayerType(var4, (Paint)null);
      }

   }

   private void endDrag() {
      this.mIsBeingDragged = false;
      this.mIsUnableToDrag = false;
      VelocityTracker var1 = this.mVelocityTracker;
      if (var1 != null) {
         var1.recycle();
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
         return var3;
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

         return var3;
      }
   }

   private int getClientWidth() {
      return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
   }

   private ViewPager.ItemInfo infoForCurrentScrollPosition() {
      int var1 = this.getClientWidth();
      float var2;
      if (var1 > 0) {
         var2 = (float)this.getScrollX() / (float)var1;
      } else {
         var2 = 0.0F;
      }

      float var3;
      if (var1 > 0) {
         var3 = (float)this.mPageMargin / (float)var1;
      } else {
         var3 = 0.0F;
      }

      ViewPager.ItemInfo var4 = null;
      var1 = 0;
      boolean var5 = true;
      int var6 = -1;
      float var7 = 0.0F;

      ViewPager.ItemInfo var11;
      for(float var8 = 0.0F; var1 < this.mItems.size(); var4 = var11) {
         ViewPager.ItemInfo var9 = (ViewPager.ItemInfo)this.mItems.get(var1);
         int var10 = var1;
         var11 = var9;
         if (!var5) {
            int var12 = var9.position;
            ++var6;
            var10 = var1;
            var11 = var9;
            if (var12 != var6) {
               var11 = this.mTempItem;
               var11.offset = var7 + var8 + var3;
               var11.position = var6;
               var11.widthFactor = this.mAdapter.getPageWidth(var11.position);
               var10 = var1 - 1;
            }
         }

         var7 = var11.offset;
         var8 = var11.widthFactor;
         if (!var5 && var2 < var7) {
            return var4;
         }

         if (var2 < var8 + var7 + var3 || var10 == this.mItems.size() - 1) {
            return var11;
         }

         var6 = var11.position;
         var8 = var11.widthFactor;
         var1 = var10 + 1;
         var5 = false;
      }

      return var4;
   }

   private static boolean isDecorView(View var0) {
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
      int var2 = var1.getActionIndex();
      if (var1.getPointerId(var2) == this.mActivePointerId) {
         byte var4;
         if (var2 == 0) {
            var4 = 1;
         } else {
            var4 = 0;
         }

         this.mLastMotionX = var1.getX(var4);
         this.mActivePointerId = var1.getPointerId(var4);
         VelocityTracker var3 = this.mVelocityTracker;
         if (var3 != null) {
            var3.clear();
         }
      }

   }

   private boolean pageScrolled(int var1) {
      if (this.mItems.size() == 0) {
         if (this.mFirstLayout) {
            return false;
         } else {
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0F, 0);
            if (this.mCalledSuper) {
               return false;
            } else {
               throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
         }
      } else {
         ViewPager.ItemInfo var2 = this.infoForCurrentScrollPosition();
         int var3 = this.getClientWidth();
         int var4 = this.mPageMargin;
         float var5 = (float)var4;
         float var6 = (float)var3;
         var5 /= var6;
         int var7 = var2.position;
         var6 = ((float)var1 / var6 - var2.offset) / (var2.widthFactor + var5);
         var1 = (int)((float)(var3 + var4) * var6);
         this.mCalledSuper = false;
         this.onPageScrolled(var7, var6, var1);
         if (this.mCalledSuper) {
            return true;
         } else {
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
         }
      }
   }

   private boolean performDrag(float var1) {
      float var2 = this.mLastMotionX;
      this.mLastMotionX = var1;
      float var3 = (float)this.getScrollX() + (var2 - var1);
      float var4 = (float)this.getClientWidth();
      var1 = this.mFirstOffset * var4;
      var2 = this.mLastOffset * var4;
      ArrayList var5 = this.mItems;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      ViewPager.ItemInfo var12 = (ViewPager.ItemInfo)var5.get(0);
      ArrayList var9 = this.mItems;
      ViewPager.ItemInfo var13 = (ViewPager.ItemInfo)var9.get(var9.size() - 1);
      boolean var10;
      if (var12.position != 0) {
         var1 = var12.offset * var4;
         var10 = false;
      } else {
         var10 = true;
      }

      boolean var11;
      if (var13.position != this.mAdapter.getCount() - 1) {
         var2 = var13.offset * var4;
         var11 = false;
      } else {
         var11 = true;
      }

      if (var3 < var1) {
         if (var10) {
            this.mLeftEdge.onPull(Math.abs(var1 - var3) / var4);
            var8 = true;
         }
      } else {
         var8 = var7;
         var1 = var3;
         if (var3 > var2) {
            var8 = var6;
            if (var11) {
               this.mRightEdge.onPull(Math.abs(var3 - var2) / var4);
               var8 = true;
            }

            var1 = var2;
         }
      }

      var2 = this.mLastMotionX;
      int var14 = (int)var1;
      this.mLastMotionX = var2 + (var1 - (float)var14);
      this.scrollTo(var14, this.getScrollY());
      this.pageScrolled(var14);
      return var8;
   }

   private void recomputeScrollPosition(int var1, int var2, int var3, int var4) {
      if (var2 > 0 && !this.mItems.isEmpty()) {
         if (!this.mScroller.isFinished()) {
            this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
         } else {
            int var5 = this.getPaddingLeft();
            int var6 = this.getPaddingRight();
            int var7 = this.getPaddingLeft();
            int var8 = this.getPaddingRight();
            this.scrollTo((int)((float)this.getScrollX() / (float)(var2 - var7 - var8 + var4) * (float)(var1 - var5 - var6 + var3)), this.getScrollY());
         }
      } else {
         ViewPager.ItemInfo var9 = this.infoForPosition(this.mCurItem);
         float var10;
         if (var9 != null) {
            var10 = Math.min(var9.offset, this.mLastOffset);
         } else {
            var10 = 0.0F;
         }

         var1 = (int)(var10 * (float)(var1 - this.getPaddingLeft() - this.getPaddingRight()));
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
      this.mLeftEdge.onRelease();
      this.mRightEdge.onRelease();
      boolean var1;
      if (!this.mLeftEdge.isFinished() && !this.mRightEdge.isFinished()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void scrollToItem(int var1, boolean var2, int var3, boolean var4) {
      ViewPager.ItemInfo var5 = this.infoForPosition(var1);
      int var6;
      if (var5 != null) {
         var6 = (int)((float)this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(var5.offset, this.mLastOffset)));
      } else {
         var6 = 0;
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
         ArrayList var1 = this.mDrawingOrderedChildren;
         if (var1 == null) {
            this.mDrawingOrderedChildren = new ArrayList();
         } else {
            var1.clear();
         }

         int var2 = this.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.getChildAt(var3);
            this.mDrawingOrderedChildren.add(var4);
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

      if (var5 != 262144 || var4 == var1.size()) {
         if (!this.isFocusable()) {
            return;
         }

         if ((var3 & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
            return;
         }

         if (var1 != null) {
            var1.add(this);
         }
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

   public void addOnAdapterChangeListener(ViewPager.OnAdapterChangeListener var1) {
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
      boolean var3 = false;
      View var4 = null;
      View var5;
      if (var2 == this) {
         var5 = var4;
      } else {
         label84: {
            if (var2 != null) {
               ViewParent var9 = var2.getParent();

               boolean var6;
               while(true) {
                  if (!(var9 instanceof ViewGroup)) {
                     var6 = false;
                     break;
                  }

                  if (var9 == this) {
                     var6 = true;
                     break;
                  }

                  var9 = var9.getParent();
               }

               if (!var6) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append(var2.getClass().getSimpleName());

                  for(var9 = var2.getParent(); var9 instanceof ViewGroup; var9 = var9.getParent()) {
                     var7.append(" => ");
                     var7.append(var9.getClass().getSimpleName());
                  }

                  StringBuilder var11 = new StringBuilder();
                  var11.append("arrowScroll tried to find focus based on non-child current focused view ");
                  var11.append(var7.toString());
                  Log.e("ViewPager", var11.toString());
                  var5 = var4;
                  break label84;
               }
            }

            var5 = var2;
         }
      }

      var4 = FocusFinder.getInstance().findNextFocus(this, var5, var1);
      if (var4 != null && var4 != var5) {
         int var8;
         int var10;
         if (var1 == 17) {
            var8 = this.getChildRectInPagerCoordinates(this.mTempRect, var4).left;
            var10 = this.getChildRectInPagerCoordinates(this.mTempRect, var5).left;
            if (var5 != null && var8 >= var10) {
               var3 = this.pageLeft();
            } else {
               var3 = var4.requestFocus();
            }
         } else if (var1 == 66) {
            var8 = this.getChildRectInPagerCoordinates(this.mTempRect, var4).left;
            var10 = this.getChildRectInPagerCoordinates(this.mTempRect, var5).left;
            if (var5 != null && var8 <= var10) {
               var3 = this.pageRight();
            } else {
               var3 = var4.requestFocus();
            }
         }
      } else if (var1 != 17 && var1 != 1) {
         if (var1 == 66 || var1 == 2) {
            var3 = this.pageRight();
         }
      } else {
         var3 = this.pageLeft();
      }

      if (var3) {
         this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(var1));
      }

      return var3;
   }

   public boolean beginFakeDrag() {
      if (this.mIsBeingDragged) {
         return false;
      } else {
         this.mFakeDragging = true;
         this.setScrollState(1);
         this.mLastMotionX = 0.0F;
         this.mInitialMotionX = 0.0F;
         VelocityTracker var1 = this.mVelocityTracker;
         if (var1 == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         } else {
            var1.clear();
         }

         long var2 = SystemClock.uptimeMillis();
         MotionEvent var4 = MotionEvent.obtain(var2, var2, 0, 0.0F, 0.0F, 0);
         this.mVelocityTracker.addMovement(var4);
         var4.recycle();
         this.mFakeDragBeginTime = var2;
         return true;
      }
   }

   protected boolean canScroll(View var1, boolean var2, int var3, int var4, int var5) {
      boolean var6 = var1 instanceof ViewGroup;
      boolean var7 = true;
      if (var6) {
         ViewGroup var8 = (ViewGroup)var1;
         int var9 = var1.getScrollX();
         int var10 = var1.getScrollY();

         for(int var11 = var8.getChildCount() - 1; var11 >= 0; --var11) {
            View var12 = var8.getChildAt(var11);
            int var13 = var4 + var9;
            if (var13 >= var12.getLeft() && var13 < var12.getRight()) {
               int var14 = var5 + var10;
               if (var14 >= var12.getTop() && var14 < var12.getBottom() && this.canScroll(var12, true, var3, var13 - var12.getLeft(), var14 - var12.getTop())) {
                  return true;
               }
            }
         }
      }

      if (var2 && var1.canScrollHorizontally(-var3)) {
         var2 = var7;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean canScrollHorizontally(int var1) {
      PagerAdapter var2 = this.mAdapter;
      boolean var3 = false;
      boolean var4 = false;
      if (var2 == null) {
         return false;
      } else {
         int var5 = this.getClientWidth();
         int var6 = this.getScrollX();
         if (var1 < 0) {
            if (var6 > (int)((float)var5 * this.mFirstOffset)) {
               var4 = true;
            }

            return var4;
         } else {
            var4 = var3;
            if (var1 > 0) {
               var4 = var3;
               if (var6 < (int)((float)var5 * this.mLastOffset)) {
                  var4 = true;
               }
            }

            return var4;
         }
      }
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
      List var1 = this.mOnPageChangeListeners;
      if (var1 != null) {
         var1.clear();
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
      boolean var4 = var2;
      int var12 = var3;
      int var5 = 0;

      int var10;
      boolean var13;
      for(var13 = false; var5 < this.mItems.size(); var12 = var10) {
         ViewPager.ItemInfo var6 = (ViewPager.ItemInfo)this.mItems.get(var5);
         int var7 = this.mAdapter.getItemPosition(var6.object);
         int var8;
         boolean var9;
         if (var7 == -1) {
            var8 = var5;
            var9 = var13;
            var10 = var12;
         } else {
            label74: {
               if (var7 == -2) {
                  this.mItems.remove(var5);
                  var8 = var5 - 1;
                  var9 = var13;
                  if (!var13) {
                     this.mAdapter.startUpdate((ViewGroup)this);
                     var9 = true;
                  }

                  this.mAdapter.destroyItem((ViewGroup)this, var6.position, var6.object);
                  int var14 = this.mCurItem;
                  var5 = var8;
                  var13 = var9;
                  if (var14 == var6.position) {
                     var12 = Math.max(0, Math.min(var14, var1 - 1));
                     var13 = var9;
                     var5 = var8;
                  }
               } else {
                  int var11 = var6.position;
                  var8 = var5;
                  var9 = var13;
                  var10 = var12;
                  if (var11 == var7) {
                     break label74;
                  }

                  if (var11 == this.mCurItem) {
                     var12 = var7;
                  }

                  var6.position = var7;
               }

               var4 = true;
               var8 = var5;
               var9 = var13;
               var10 = var12;
            }
         }

         var5 = var8 + 1;
         var13 = var9;
      }

      if (var13) {
         this.mAdapter.finishUpdate((ViewGroup)this);
      }

      Collections.sort(this.mItems, COMPARATOR);
      if (var4) {
         var5 = this.getChildCount();

         for(var3 = 0; var3 < var5; ++var3) {
            ViewPager.LayoutParams var15 = (ViewPager.LayoutParams)this.getChildAt(var3).getLayoutParams();
            if (!var15.isDecor) {
               var15.widthFactor = 0.0F;
            }
         }

         this.setCurrentItemInternal(var12, false, true);
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
      if (var1.getEventType() == 4096) {
         return super.dispatchPopulateAccessibilityEvent(var1);
      } else {
         int var2 = this.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.getChildAt(var3);
            if (var4.getVisibility() == 0) {
               ViewPager.ItemInfo var5 = this.infoForChild(var4);
               if (var5 != null && var5.position == this.mCurItem && var4.dispatchPopulateAccessibilityEvent(var1)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   float distanceInfluenceForSnapDuration(float var1) {
      return (float)Math.sin((double)((var1 - 0.5F) * 0.47123894F));
   }

   public void draw(Canvas var1) {
      boolean var3;
      label36: {
         super.draw(var1);
         int var2 = this.getOverScrollMode();
         var3 = false;
         boolean var4 = false;
         if (var2 != 0) {
            label35: {
               if (var2 == 1) {
                  PagerAdapter var5 = this.mAdapter;
                  if (var5 != null && var5.getCount() > 1) {
                     break label35;
                  }
               }

               this.mLeftEdge.finish();
               this.mRightEdge.finish();
               break label36;
            }
         }

         int var9;
         if (!this.mLeftEdge.isFinished()) {
            var9 = var1.save();
            var2 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            int var10 = this.getWidth();
            var1.rotate(270.0F);
            var1.translate((float)(-var2 + this.getPaddingTop()), this.mFirstOffset * (float)var10);
            this.mLeftEdge.setSize(var2, var10);
            var4 = false | this.mLeftEdge.draw(var1);
            var1.restoreToCount(var9);
         }

         var3 = var4;
         if (!this.mRightEdge.isFinished()) {
            var2 = var1.save();
            int var6 = this.getWidth();
            int var7 = this.getHeight();
            int var8 = this.getPaddingTop();
            var9 = this.getPaddingBottom();
            var1.rotate(90.0F);
            var1.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0F) * (float)var6);
            this.mRightEdge.setSize(var7 - var8 - var9, var6);
            var3 = var4 | this.mRightEdge.draw(var1);
            var1.restoreToCount(var2);
         }
      }

      if (var3) {
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
      if (this.mFakeDragging) {
         if (this.mAdapter != null) {
            VelocityTracker var1 = this.mVelocityTracker;
            var1.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            int var2 = (int)var1.getXVelocity(this.mActivePointerId);
            this.mPopulatePending = true;
            int var3 = this.getClientWidth();
            int var4 = this.getScrollX();
            ViewPager.ItemInfo var5 = this.infoForCurrentScrollPosition();
            this.setCurrentItemInternal(this.determineTargetPage(var5.position, ((float)var4 / (float)var3 - var5.offset) / var5.widthFactor, var2, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, var2);
         }

         this.endDrag();
         this.mFakeDragging = false;
      } else {
         throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
      }
   }

   public boolean executeKeyEvent(KeyEvent var1) {
      boolean var3;
      if (var1.getAction() == 0) {
         int var2 = var1.getKeyCode();
         if (var2 == 21) {
            if (var1.hasModifiers(2)) {
               var3 = this.pageLeft();
            } else {
               var3 = this.arrowScroll(17);
            }

            return var3;
         }

         if (var2 == 22) {
            if (var1.hasModifiers(2)) {
               var3 = this.pageRight();
            } else {
               var3 = this.arrowScroll(66);
            }

            return var3;
         }

         if (var2 == 61) {
            if (var1.hasNoModifiers()) {
               var3 = this.arrowScroll(2);
               return var3;
            }

            if (var1.hasModifiers(1)) {
               var3 = this.arrowScroll(1);
               return var3;
            }
         }
      }

      var3 = false;
      return var3;
   }

   public void fakeDragBy(float var1) {
      if (this.mFakeDragging) {
         if (this.mAdapter != null) {
            this.mLastMotionX += var1;
            float var2 = (float)this.getScrollX() - var1;
            float var3 = (float)this.getClientWidth();
            var1 = this.mFirstOffset * var3;
            float var4 = this.mLastOffset * var3;
            ViewPager.ItemInfo var5 = (ViewPager.ItemInfo)this.mItems.get(0);
            ArrayList var6 = this.mItems;
            ViewPager.ItemInfo var11 = (ViewPager.ItemInfo)var6.get(var6.size() - 1);
            if (var5.position != 0) {
               var1 = var5.offset * var3;
            }

            if (var11.position != this.mAdapter.getCount() - 1) {
               var4 = var11.offset * var3;
            }

            if (var2 >= var1) {
               var1 = var2;
               if (var2 > var4) {
                  var1 = var4;
               }
            }

            var4 = this.mLastMotionX;
            int var7 = (int)var1;
            this.mLastMotionX = var4 + (var1 - (float)var7);
            this.scrollTo(var7, this.getScrollY());
            this.pageScrolled(var7);
            long var8 = SystemClock.uptimeMillis();
            MotionEvent var10 = MotionEvent.obtain(this.mFakeDragBeginTime, var8, 2, this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(var10);
            var10.recycle();
         }
      } else {
         throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
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
      int var3 = var2;
      if (this.mDrawingOrder == 2) {
         var3 = var1 - 1 - var2;
      }

      return ((ViewPager.LayoutParams)((View)this.mDrawingOrderedChildren.get(var3)).getLayoutParams()).childIndex;
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
         if (var2 != this) {
            if (var2 != null && var2 instanceof View) {
               var1 = (View)var2;
               continue;
            }

            return null;
         }

         return this.infoForChild(var1);
      }
   }

   ViewPager.ItemInfo infoForChild(View var1) {
      for(int var2 = 0; var2 < this.mItems.size(); ++var2) {
         ViewPager.ItemInfo var3 = (ViewPager.ItemInfo)this.mItems.get(var2);
         if (this.mAdapter.isViewFromObject(var1, var3.object)) {
            return var3;
         }
      }

      return null;
   }

   ViewPager.ItemInfo infoForPosition(int var1) {
      for(int var2 = 0; var2 < this.mItems.size(); ++var2) {
         ViewPager.ItemInfo var3 = (ViewPager.ItemInfo)this.mItems.get(var2);
         if (var3.position == var1) {
            return var3;
         }
      }

      return null;
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
      this.mLeftEdge = new EdgeEffect(var1);
      this.mRightEdge = new EdgeEffect(var1);
      this.mFlingDistance = (int)(25.0F * var3);
      this.mCloseEnough = (int)(2.0F * var3);
      this.mDefaultGutterSize = (int)(var3 * 16.0F);
      ViewCompat.setAccessibilityDelegate(this, new ViewPager.MyAccessibilityDelegate());
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

      ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
         private final Rect mTempRect = new Rect();

         public WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2) {
            WindowInsetsCompat var6 = ViewCompat.onApplyWindowInsets(var1, var2);
            if (var6.isConsumed()) {
               return var6;
            } else {
               Rect var7 = this.mTempRect;
               var7.left = var6.getSystemWindowInsetLeft();
               var7.top = var6.getSystemWindowInsetTop();
               var7.right = var6.getSystemWindowInsetRight();
               var7.bottom = var6.getSystemWindowInsetBottom();
               int var3 = 0;

               for(int var4 = ViewPager.this.getChildCount(); var3 < var4; ++var3) {
                  WindowInsetsCompat var5 = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(var3), var6);
                  var7.left = Math.min(var5.getSystemWindowInsetLeft(), var7.left);
                  var7.top = Math.min(var5.getSystemWindowInsetTop(), var7.top);
                  var7.right = Math.min(var5.getSystemWindowInsetRight(), var7.right);
                  var7.bottom = Math.min(var5.getSystemWindowInsetBottom(), var7.bottom);
               }

               return var6.replaceSystemWindowInsets(var7.left, var7.top, var7.right, var7.bottom);
            }
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
      Scroller var1 = this.mScroller;
      if (var1 != null && !var1.isFinished()) {
         this.mScroller.abortAnimation();
      }

      super.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
         int var2 = this.getScrollX();
         int var3 = this.getWidth();
         float var4 = (float)this.mPageMargin;
         float var5 = (float)var3;
         float var6 = var4 / var5;
         ArrayList var7 = this.mItems;
         int var8 = 0;
         ViewPager.ItemInfo var14 = (ViewPager.ItemInfo)var7.get(0);
         var4 = var14.offset;
         int var9 = this.mItems.size();
         int var10 = var14.position;

         for(int var11 = ((ViewPager.ItemInfo)this.mItems.get(var9 - 1)).position; var10 < var11; ++var10) {
            while(var10 > var14.position && var8 < var9) {
               var7 = this.mItems;
               ++var8;
               var14 = (ViewPager.ItemInfo)var7.get(var8);
            }

            float var12;
            float var13;
            if (var10 == var14.position) {
               var12 = var14.offset;
               var4 = var14.widthFactor;
               var13 = (var12 + var4) * var5;
               var4 = var12 + var4 + var6;
            } else {
               var12 = this.mAdapter.getPageWidth(var10);
               var13 = (var4 + var12) * var5;
               var4 += var12 + var6;
            }

            if ((float)this.mPageMargin + var13 > (float)var2) {
               this.mMarginDrawable.setBounds(Math.round(var13), this.mTopPageBounds, Math.round((float)this.mPageMargin + var13), this.mBottomPageBounds);
               this.mMarginDrawable.draw(var1);
            }

            if (var13 > (float)(var2 + var3)) {
               break;
            }
         }
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var2 = var1.getAction() & 255;
      if (var2 != 3 && var2 != 1) {
         if (var2 != 0) {
            if (this.mIsBeingDragged) {
               return true;
            }

            if (this.mIsUnableToDrag) {
               return false;
            }
         }

         float var7;
         if (var2 != 0) {
            if (var2 != 2) {
               if (var2 == 6) {
                  this.onSecondaryPointerUp(var1);
               }
            } else {
               var2 = this.mActivePointerId;
               if (var2 != -1) {
                  var2 = var1.findPointerIndex(var2);
                  float var3 = var1.getX(var2);
                  float var4 = var3 - this.mLastMotionX;
                  float var5 = Math.abs(var4);
                  float var6 = var1.getY(var2);
                  var7 = Math.abs(var6 - this.mInitialMotionY);
                  if (var4 != 0.0F && !this.isGutterDrag(this.mLastMotionX, var4) && this.canScroll(this, false, (int)var4, (int)var3, (int)var6)) {
                     this.mLastMotionX = var3;
                     this.mLastMotionY = var6;
                     this.mIsUnableToDrag = true;
                     return false;
                  }

                  if (var5 > (float)this.mTouchSlop && var5 * 0.5F > var7) {
                     this.mIsBeingDragged = true;
                     this.requestParentDisallowInterceptTouchEvent(true);
                     this.setScrollState(1);
                     if (var4 > 0.0F) {
                        var7 = this.mInitialMotionX + (float)this.mTouchSlop;
                     } else {
                        var7 = this.mInitialMotionX - (float)this.mTouchSlop;
                     }

                     this.mLastMotionX = var7;
                     this.mLastMotionY = var6;
                     this.setScrollingCacheEnabled(true);
                  } else if (var7 > (float)this.mTouchSlop) {
                     this.mIsUnableToDrag = true;
                  }

                  if (this.mIsBeingDragged && this.performDrag(var3)) {
                     ViewCompat.postInvalidateOnAnimation(this);
                  }
               }
            }
         } else {
            var7 = var1.getX();
            this.mInitialMotionX = var7;
            this.mLastMotionX = var7;
            var7 = var1.getY();
            this.mInitialMotionY = var7;
            this.mLastMotionY = var7;
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
         }

         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         this.mVelocityTracker.addMovement(var1);
         return this.mIsBeingDragged;
      } else {
         this.resetTouch();
         return false;
      }
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

      int var14;
      for(int var12 = 0; var12 < var6; var11 = var4) {
         View var13 = this.getChildAt(var12);
         var14 = var3;
         int var15 = var9;
         int var16 = var2;
         int var17 = var5;
         var4 = var11;
         if (var13.getVisibility() != 8) {
            ViewPager.LayoutParams var18 = (ViewPager.LayoutParams)var13.getLayoutParams();
            var14 = var3;
            var15 = var9;
            var16 = var2;
            var17 = var5;
            var4 = var11;
            if (var18.isDecor) {
               var14 = var18.gravity;
               var4 = var14 & 7;
               var17 = var14 & 112;
               if (var4 != 1) {
                  if (var4 != 3) {
                     if (var4 != 5) {
                        var4 = var3;
                        var14 = var3;
                     } else {
                        var4 = var7 - var9 - var13.getMeasuredWidth();
                        var9 += var13.getMeasuredWidth();
                        var14 = var3;
                     }
                  } else {
                     var14 = var13.getMeasuredWidth();
                     var4 = var3;
                     var14 += var3;
                  }
               } else {
                  var4 = Math.max((var7 - var13.getMeasuredWidth()) / 2, var3);
                  var14 = var3;
               }

               if (var17 != 16) {
                  if (var17 != 48) {
                     if (var17 != 80) {
                        var3 = var2;
                     } else {
                        var3 = var8 - var5 - var13.getMeasuredHeight();
                        var5 += var13.getMeasuredHeight();
                     }
                  } else {
                     var17 = var13.getMeasuredHeight();
                     var3 = var2;
                     var2 += var17;
                  }
               } else {
                  var3 = Math.max((var8 - var13.getMeasuredHeight()) / 2, var2);
               }

               var4 += var10;
               var13.layout(var4, var3, var13.getMeasuredWidth() + var4, var3 + var13.getMeasuredHeight());
               var4 = var11 + 1;
               var17 = var5;
               var16 = var2;
               var15 = var9;
            }
         }

         ++var12;
         var3 = var14;
         var9 = var15;
         var2 = var16;
         var5 = var17;
      }

      for(var4 = 0; var4 < var6; ++var4) {
         View var22 = this.getChildAt(var4);
         if (var22.getVisibility() != 8) {
            ViewPager.LayoutParams var21 = (ViewPager.LayoutParams)var22.getLayoutParams();
            if (!var21.isDecor) {
               ViewPager.ItemInfo var19 = this.infoForChild(var22);
               if (var19 != null) {
                  float var20 = (float)(var7 - var3 - var9);
                  var14 = (int)(var19.offset * var20) + var3;
                  if (var21.needsMeasure) {
                     var21.needsMeasure = false;
                     var22.measure(MeasureSpec.makeMeasureSpec((int)(var20 * var21.widthFactor), 1073741824), MeasureSpec.makeMeasureSpec(var8 - var2 - var5, 1073741824));
                  }

                  var22.layout(var14, var2, var22.getMeasuredWidth() + var14, var22.getMeasuredHeight() + var2);
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
      byte var3 = 0;
      this.setMeasuredDimension(ViewGroup.getDefaultSize(0, var1), ViewGroup.getDefaultSize(0, var2));
      var1 = this.getMeasuredWidth();
      this.mGutterSize = Math.min(var1 / 10, this.mDefaultGutterSize);
      int var4 = this.getPaddingLeft();
      int var5 = this.getPaddingRight();
      int var6 = this.getMeasuredHeight();
      var2 = this.getPaddingTop();
      int var7 = this.getPaddingBottom();
      int var8 = this.getChildCount();
      var2 = var6 - var2 - var7;
      var1 = var1 - var4 - var5;
      var6 = 0;

      while(true) {
         boolean var9 = true;
         int var10 = 1073741824;
         if (var6 >= var8) {
            this.mChildWidthMeasureSpec = MeasureSpec.makeMeasureSpec(var1, 1073741824);
            this.mChildHeightMeasureSpec = MeasureSpec.makeMeasureSpec(var2, 1073741824);
            this.mInLayout = true;
            this.populate();
            this.mInLayout = false;
            var4 = this.getChildCount();

            for(var2 = var3; var2 < var4; ++var2) {
               View var18 = this.getChildAt(var2);
               if (var18.getVisibility() != 8) {
                  ViewPager.LayoutParams var17 = (ViewPager.LayoutParams)var18.getLayoutParams();
                  if (var17 == null || !var17.isDecor) {
                     var18.measure(MeasureSpec.makeMeasureSpec((int)((float)var1 * var17.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                  }
               }
            }

            return;
         }

         View var11 = this.getChildAt(var6);
         var4 = var2;
         var5 = var1;
         if (var11.getVisibility() != 8) {
            ViewPager.LayoutParams var12 = (ViewPager.LayoutParams)var11.getLayoutParams();
            var4 = var2;
            var5 = var1;
            if (var12 != null) {
               var4 = var2;
               var5 = var1;
               if (var12.isDecor) {
                  var5 = var12.gravity;
                  var4 = var5 & 7;
                  var5 &= 112;
                  boolean var13;
                  if (var5 != 48 && var5 != 80) {
                     var13 = false;
                  } else {
                     var13 = true;
                  }

                  boolean var15 = var9;
                  if (var4 != 3) {
                     if (var4 == 5) {
                        var15 = var9;
                     } else {
                        var15 = false;
                     }
                  }

                  int var16;
                  label80: {
                     var4 = Integer.MIN_VALUE;
                     if (var13) {
                        var5 = 1073741824;
                     } else {
                        var5 = var4;
                        if (var15) {
                           var16 = 1073741824;
                           var5 = var4;
                           var4 = var16;
                           break label80;
                        }
                     }

                     var4 = Integer.MIN_VALUE;
                  }

                  var16 = var12.width;
                  int var14;
                  if (var16 != -2) {
                     if (var16 != -1) {
                        var5 = var16;
                     } else {
                        var5 = var1;
                     }

                     var14 = 1073741824;
                     var16 = var5;
                  } else {
                     var16 = var1;
                     var14 = var5;
                  }

                  var5 = var12.height;
                  if (var5 != -2) {
                     if (var5 != -1) {
                        var4 = var5;
                     } else {
                        var4 = var2;
                     }
                  } else {
                     var10 = var4;
                     var4 = var2;
                  }

                  var11.measure(MeasureSpec.makeMeasureSpec(var16, var14), MeasureSpec.makeMeasureSpec(var4, var10));
                  if (var13) {
                     var4 = var2 - var11.getMeasuredHeight();
                     var5 = var1;
                  } else {
                     var4 = var2;
                     var5 = var1;
                     if (var15) {
                        var5 = var1 - var11.getMeasuredWidth();
                        var4 = var2;
                     }
                  }
               }
            }
         }

         ++var6;
         var2 = var4;
         var1 = var5;
      }
   }

   protected void onPageScrolled(int var1, float var2, int var3) {
      int var4 = this.mDecorChildCount;
      byte var5 = 0;
      if (var4 > 0) {
         int var6 = this.getScrollX();
         var4 = this.getPaddingLeft();
         int var7 = this.getPaddingRight();
         int var8 = this.getWidth();
         int var9 = this.getChildCount();

         for(int var10 = 0; var10 < var9; ++var10) {
            View var11 = this.getChildAt(var10);
            ViewPager.LayoutParams var12 = (ViewPager.LayoutParams)var11.getLayoutParams();
            if (var12.isDecor) {
               int var13 = var12.gravity & 7;
               if (var13 != 1) {
                  if (var13 != 3) {
                     if (var13 != 5) {
                        var13 = var4;
                        var4 = var4;
                     } else {
                        var13 = var8 - var7 - var11.getMeasuredWidth();
                        var7 += var11.getMeasuredWidth();
                     }
                  } else {
                     int var14 = var11.getWidth() + var4;
                     var13 = var4;
                     var4 = var14;
                  }
               } else {
                  var13 = Math.max((var8 - var11.getMeasuredWidth()) / 2, var4);
               }

               var13 = var13 + var6 - var11.getLeft();
               if (var13 != 0) {
                  var11.offsetLeftAndRight(var13);
               }
            }
         }
      }

      this.dispatchOnPageScrolled(var1, var2, var3);
      if (this.mPageTransformer != null) {
         var3 = this.getScrollX();
         var4 = this.getChildCount();

         for(var1 = var5; var1 < var4; ++var1) {
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
      int var4 = -1;
      byte var5;
      if ((var1 & 2) != 0) {
         var4 = var3;
         var3 = 0;
         var5 = 1;
      } else {
         --var3;
         var5 = -1;
      }

      for(; var3 != var4; var3 += var5) {
         View var6 = this.getChildAt(var3);
         if (var6.getVisibility() == 0) {
            ViewPager.ItemInfo var7 = this.infoForChild(var6);
            if (var7 != null && var7.position == this.mCurItem && var6.requestFocus(var1, var2)) {
               return true;
            }
         }
      }

      return false;
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof ViewPager.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         ViewPager.SavedState var3 = (ViewPager.SavedState)var1;
         super.onRestoreInstanceState(var3.getSuperState());
         PagerAdapter var2 = this.mAdapter;
         if (var2 != null) {
            var2.restoreState(var3.adapterState, var3.loader);
            this.setCurrentItemInternal(var3.position, false, true);
         } else {
            this.mRestoredCurItem = var3.position;
            this.mRestoredAdapterState = var3.adapterState;
            this.mRestoredClassLoader = var3.loader;
         }

      }
   }

   public Parcelable onSaveInstanceState() {
      ViewPager.SavedState var1 = new ViewPager.SavedState(super.onSaveInstanceState());
      var1.position = this.mCurItem;
      PagerAdapter var2 = this.mAdapter;
      if (var2 != null) {
         var1.adapterState = var2.saveState();
      }

      return var1;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3) {
         var2 = this.mPageMargin;
         this.recomputeScrollPosition(var1, var3, var2, var2);
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.mFakeDragging) {
         return true;
      } else {
         int var2 = var1.getAction();
         boolean var3 = false;
         if (var2 == 0 && var1.getEdgeFlags() != 0) {
            return false;
         } else {
            PagerAdapter var4 = this.mAdapter;
            if (var4 != null && var4.getCount() != 0) {
               if (this.mVelocityTracker == null) {
                  this.mVelocityTracker = VelocityTracker.obtain();
               }

               this.mVelocityTracker.addMovement(var1);
               var2 = var1.getAction() & 255;
               float var5;
               if (var2 != 0) {
                  float var7;
                  if (var2 != 1) {
                     if (var2 != 2) {
                        if (var2 != 3) {
                           if (var2 != 5) {
                              if (var2 == 6) {
                                 this.onSecondaryPointerUp(var1);
                                 this.mLastMotionX = var1.getX(var1.findPointerIndex(this.mActivePointerId));
                              }
                           } else {
                              var2 = var1.getActionIndex();
                              this.mLastMotionX = var1.getX(var2);
                              this.mActivePointerId = var1.getPointerId(var2);
                           }
                        } else if (this.mIsBeingDragged) {
                           this.scrollToItem(this.mCurItem, true, 0, false);
                           var3 = this.resetTouch();
                        }
                     } else {
                        label64: {
                           if (!this.mIsBeingDragged) {
                              var2 = var1.findPointerIndex(this.mActivePointerId);
                              if (var2 == -1) {
                                 var3 = this.resetTouch();
                                 break label64;
                              }

                              var5 = var1.getX(var2);
                              float var6 = Math.abs(var5 - this.mLastMotionX);
                              var7 = var1.getY(var2);
                              float var8 = Math.abs(var7 - this.mLastMotionY);
                              if (var6 > (float)this.mTouchSlop && var6 > var8) {
                                 this.mIsBeingDragged = true;
                                 this.requestParentDisallowInterceptTouchEvent(true);
                                 var8 = this.mInitialMotionX;
                                 if (var5 - var8 > 0.0F) {
                                    var5 = var8 + (float)this.mTouchSlop;
                                 } else {
                                    var5 = var8 - (float)this.mTouchSlop;
                                 }

                                 this.mLastMotionX = var5;
                                 this.mLastMotionY = var7;
                                 this.setScrollState(1);
                                 this.setScrollingCacheEnabled(true);
                                 ViewParent var11 = this.getParent();
                                 if (var11 != null) {
                                    var11.requestDisallowInterceptTouchEvent(true);
                                 }
                              }
                           }

                           if (this.mIsBeingDragged) {
                              var3 = false | this.performDrag(var1.getX(var1.findPointerIndex(this.mActivePointerId)));
                           }
                        }
                     }
                  } else if (this.mIsBeingDragged) {
                     VelocityTracker var12 = this.mVelocityTracker;
                     var12.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                     int var9 = (int)var12.getXVelocity(this.mActivePointerId);
                     this.mPopulatePending = true;
                     int var10 = this.getClientWidth();
                     var2 = this.getScrollX();
                     ViewPager.ItemInfo var13 = this.infoForCurrentScrollPosition();
                     var7 = (float)this.mPageMargin;
                     var5 = (float)var10;
                     var7 /= var5;
                     this.setCurrentItemInternal(this.determineTargetPage(var13.position, ((float)var2 / var5 - var13.offset) / (var13.widthFactor + var7), var9, (int)(var1.getX(var1.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, var9);
                     var3 = this.resetTouch();
                  }
               } else {
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
               }

               if (var3) {
                  ViewCompat.postInvalidateOnAnimation(this);
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   boolean pageLeft() {
      int var1 = this.mCurItem;
      if (var1 > 0) {
         this.setCurrentItem(var1 - 1, true);
         return true;
      } else {
         return false;
      }
   }

   boolean pageRight() {
      PagerAdapter var1 = this.mAdapter;
      if (var1 != null && this.mCurItem < var1.getCount() - 1) {
         this.setCurrentItem(this.mCurItem + 1, true);
         return true;
      } else {
         return false;
      }
   }

   void populate() {
      this.populate(this.mCurItem);
   }

   void populate(int var1) {
      int var2 = this.mCurItem;
      ViewPager.ItemInfo var3;
      if (var2 != var1) {
         var3 = this.infoForPosition(var2);
         this.mCurItem = var1;
      } else {
         var3 = null;
      }

      if (this.mAdapter == null) {
         this.sortChildDrawingOrder();
      } else if (this.mPopulatePending) {
         this.sortChildDrawingOrder();
      } else if (this.getWindowToken() != null) {
         this.mAdapter.startUpdate((ViewGroup)this);
         var1 = this.mOffscreenPageLimit;
         int var4 = Math.max(0, this.mCurItem - var1);
         int var5 = this.mAdapter.getCount();
         int var6 = Math.min(var5 - 1, this.mCurItem + var1);
         if (var5 != this.mExpectedAdapterCount) {
            String var22;
            try {
               var22 = this.getResources().getResourceName(this.getId());
            } catch (NotFoundException var17) {
               var22 = Integer.toHexString(this.getId());
            }

            StringBuilder var19 = new StringBuilder();
            var19.append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
            var19.append(this.mExpectedAdapterCount);
            var19.append(", found: ");
            var19.append(var5);
            var19.append(" Pager id: ");
            var19.append(var22);
            var19.append(" Pager class: ");
            var19.append(this.getClass());
            var19.append(" Problematic adapter: ");
            var19.append(this.mAdapter.getClass());
            throw new IllegalStateException(var19.toString());
         } else {
            var1 = 0;

            ViewPager.ItemInfo var7;
            int var8;
            while(true) {
               if (var1 < this.mItems.size()) {
                  var7 = (ViewPager.ItemInfo)this.mItems.get(var1);
                  var8 = var7.position;
                  var2 = this.mCurItem;
                  if (var8 < var2) {
                     ++var1;
                     continue;
                  }

                  if (var8 == var2) {
                     break;
                  }
               }

               var7 = null;
               break;
            }

            ViewPager.ItemInfo var9 = var7;
            if (var7 == null) {
               var9 = var7;
               if (var5 > 0) {
                  var9 = this.addNewItem(this.mCurItem, var1);
               }
            }

            if (var9 != null) {
               var2 = var1 - 1;
               if (var2 >= 0) {
                  var7 = (ViewPager.ItemInfo)this.mItems.get(var2);
               } else {
                  var7 = null;
               }

               int var10 = this.getClientWidth();
               float var11;
               if (var10 <= 0) {
                  var11 = 0.0F;
               } else {
                  var11 = 2.0F - var9.widthFactor + (float)this.getPaddingLeft() / (float)var10;
               }

               int var12 = this.mCurItem - 1;
               float var13 = 0.0F;

               ViewPager.ItemInfo var14;
               float var15;
               int var16;
               for(var14 = var7; var12 >= 0; var2 = var8) {
                  label202: {
                     label241: {
                        if (var13 >= var11 && var12 < var4) {
                           if (var14 == null) {
                              break;
                           }

                           var15 = var13;
                           var16 = var1;
                           var7 = var14;
                           var8 = var2;
                           if (var12 != var14.position) {
                              break label202;
                           }

                           var15 = var13;
                           var16 = var1;
                           var7 = var14;
                           var8 = var2;
                           if (var14.scrolling) {
                              break label202;
                           }

                           this.mItems.remove(var2);
                           this.mAdapter.destroyItem((ViewGroup)this, var12, var14.object);
                           --var2;
                           --var1;
                           var15 = var13;
                           var8 = var1;
                           var16 = var2;
                           if (var2 >= 0) {
                              var7 = (ViewPager.ItemInfo)this.mItems.get(var2);
                              var15 = var13;
                              break label241;
                           }
                        } else if (var14 != null && var12 == var14.position) {
                           var13 += var14.widthFactor;
                           --var2;
                           var15 = var13;
                           var8 = var1;
                           var16 = var2;
                           if (var2 >= 0) {
                              var7 = (ViewPager.ItemInfo)this.mItems.get(var2);
                              var15 = var13;
                              break label241;
                           }
                        } else {
                           var13 += this.addNewItem(var12, var2 + 1).widthFactor;
                           ++var1;
                           var15 = var13;
                           var8 = var1;
                           var16 = var2;
                           if (var2 >= 0) {
                              var7 = (ViewPager.ItemInfo)this.mItems.get(var2);
                              var15 = var13;
                              break label241;
                           }
                        }

                        var7 = null;
                        var2 = var16;
                        var1 = var8;
                     }

                     var8 = var2;
                     var16 = var1;
                  }

                  --var12;
                  var13 = var15;
                  var1 = var16;
                  var14 = var7;
               }

               var13 = var9.widthFactor;
               var8 = var1 + 1;
               if (var13 < 2.0F) {
                  if (var8 < this.mItems.size()) {
                     var7 = (ViewPager.ItemInfo)this.mItems.get(var8);
                  } else {
                     var7 = null;
                  }

                  if (var10 <= 0) {
                     var11 = 0.0F;
                  } else {
                     var11 = (float)this.getPaddingRight() / (float)var10 + 2.0F;
                  }

                  var2 = this.mCurItem;
                  var14 = var7;

                  while(true) {
                     var16 = var2 + 1;
                     if (var16 >= var5) {
                        break;
                     }

                     label242: {
                        if (var13 >= var11 && var16 > var6) {
                           if (var14 == null) {
                              break;
                           }

                           var15 = var13;
                           var2 = var8;
                           var7 = var14;
                           if (var16 != var14.position) {
                              break label242;
                           }

                           var15 = var13;
                           var2 = var8;
                           var7 = var14;
                           if (var14.scrolling) {
                              break label242;
                           }

                           this.mItems.remove(var8);
                           this.mAdapter.destroyItem((ViewGroup)this, var16, var14.object);
                           var15 = var13;
                           var2 = var8;
                           if (var8 < this.mItems.size()) {
                              var7 = (ViewPager.ItemInfo)this.mItems.get(var8);
                              var15 = var13;
                              var2 = var8;
                              break label242;
                           }
                        } else if (var14 != null && var16 == var14.position) {
                           var13 += var14.widthFactor;
                           ++var8;
                           var15 = var13;
                           var2 = var8;
                           if (var8 < this.mItems.size()) {
                              var7 = (ViewPager.ItemInfo)this.mItems.get(var8);
                              var15 = var13;
                              var2 = var8;
                              break label242;
                           }
                        } else {
                           var7 = this.addNewItem(var16, var8);
                           ++var8;
                           var13 += var7.widthFactor;
                           var15 = var13;
                           var2 = var8;
                           if (var8 < this.mItems.size()) {
                              var7 = (ViewPager.ItemInfo)this.mItems.get(var8);
                              var2 = var8;
                              var15 = var13;
                              break label242;
                           }
                        }

                        var7 = null;
                     }

                     var13 = var15;
                     var8 = var2;
                     var14 = var7;
                     var2 = var16;
                  }
               }

               this.calculatePageOffsets(var9, var1, var3);
               this.mAdapter.setPrimaryItem((ViewGroup)this, this.mCurItem, var9.object);
            }

            this.mAdapter.finishUpdate((ViewGroup)this);
            var2 = this.getChildCount();

            View var18;
            for(var1 = 0; var1 < var2; ++var1) {
               var18 = this.getChildAt(var1);
               ViewPager.LayoutParams var20 = (ViewPager.LayoutParams)var18.getLayoutParams();
               var20.childIndex = var1;
               if (!var20.isDecor && var20.widthFactor == 0.0F) {
                  var3 = this.infoForChild(var18);
                  if (var3 != null) {
                     var20.widthFactor = var3.widthFactor;
                     var20.position = var3.position;
                  }
               }
            }

            this.sortChildDrawingOrder();
            if (this.hasFocus()) {
               View var21 = this.findFocus();
               if (var21 != null) {
                  var7 = this.infoForAnyChild(var21);
               } else {
                  var7 = null;
               }

               if (var7 == null || var7.position != this.mCurItem) {
                  for(var1 = 0; var1 < this.getChildCount(); ++var1) {
                     var18 = this.getChildAt(var1);
                     var7 = this.infoForChild(var18);
                     if (var7 != null && var7.position == this.mCurItem && var18.requestFocus(2)) {
                        break;
                     }
                  }
               }
            }

         }
      }
   }

   public void removeOnAdapterChangeListener(ViewPager.OnAdapterChangeListener var1) {
      List var2 = this.mAdapterChangeListeners;
      if (var2 != null) {
         var2.remove(var1);
      }

   }

   public void removeOnPageChangeListener(ViewPager.OnPageChangeListener var1) {
      List var2 = this.mOnPageChangeListeners;
      if (var2 != null) {
         var2.remove(var1);
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
      PagerAdapter var2 = this.mAdapter;
      byte var3 = 0;
      int var4;
      if (var2 != null) {
         var2.setViewPagerObserver((DataSetObserver)null);
         this.mAdapter.startUpdate((ViewGroup)this);

         for(var4 = 0; var4 < this.mItems.size(); ++var4) {
            ViewPager.ItemInfo var8 = (ViewPager.ItemInfo)this.mItems.get(var4);
            this.mAdapter.destroyItem((ViewGroup)this, var8.position, var8.object);
         }

         this.mAdapter.finishUpdate((ViewGroup)this);
         this.mItems.clear();
         this.removeNonDecorViews();
         this.mCurItem = 0;
         this.scrollTo(0, 0);
      }

      PagerAdapter var5 = this.mAdapter;
      this.mAdapter = var1;
      this.mExpectedAdapterCount = 0;
      if (this.mAdapter != null) {
         if (this.mObserver == null) {
            this.mObserver = new ViewPager.PagerObserver();
         }

         this.mAdapter.setViewPagerObserver(this.mObserver);
         this.mPopulatePending = false;
         boolean var6 = this.mFirstLayout;
         this.mFirstLayout = true;
         this.mExpectedAdapterCount = this.mAdapter.getCount();
         if (this.mRestoredCurItem >= 0) {
            this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
            this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
            this.mRestoredCurItem = -1;
            this.mRestoredAdapterState = null;
            this.mRestoredClassLoader = null;
         } else if (!var6) {
            this.populate();
         } else {
            this.requestLayout();
         }
      }

      List var9 = this.mAdapterChangeListeners;
      if (var9 != null && !var9.isEmpty()) {
         int var7 = this.mAdapterChangeListeners.size();

         for(var4 = var3; var4 < var7; ++var4) {
            ((ViewPager.OnAdapterChangeListener)this.mAdapterChangeListeners.get(var4)).onAdapterChanged(this, var5, var1);
         }
      }

   }

   public void setCurrentItem(int var1) {
      this.mPopulatePending = false;
      this.setCurrentItemInternal(var1, this.mFirstLayout ^ true, false);
   }

   public void setCurrentItem(int var1, boolean var2) {
      this.mPopulatePending = false;
      this.setCurrentItemInternal(var1, var2, false);
   }

   void setCurrentItemInternal(int var1, boolean var2, boolean var3) {
      this.setCurrentItemInternal(var1, var2, var3, 0);
   }

   void setCurrentItemInternal(int var1, boolean var2, boolean var3, int var4) {
      PagerAdapter var5 = this.mAdapter;
      if (var5 != null && var5.getCount() > 0) {
         if (!var3 && this.mCurItem == var1 && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
         } else {
            var3 = true;
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
            int var7 = this.mCurItem;
            if (var6 > var7 + var1 || var6 < var7 - var1) {
               for(var1 = 0; var1 < this.mItems.size(); ++var1) {
                  ((ViewPager.ItemInfo)this.mItems.get(var1)).scrolling = true;
               }
            }

            if (this.mCurItem == var6) {
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
         StringBuilder var3 = new StringBuilder();
         var3.append("Requested offscreen page limit ");
         var3.append(var1);
         var3.append(" too small; defaulting to ");
         var3.append(1);
         Log.w("ViewPager", var3.toString());
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

   public void setPageMarginDrawable(int var1) {
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
      this.setChildrenDrawingOrderEnabled(var5);
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
         Scroller var4 = this.mScroller;
         boolean var5;
         if (var4 != null && !var4.isFinished()) {
            var5 = true;
         } else {
            var5 = false;
         }

         int var12;
         if (var5) {
            if (this.mIsScrollStarted) {
               var12 = this.mScroller.getCurrX();
            } else {
               var12 = this.mScroller.getStartX();
            }

            this.mScroller.abortAnimation();
            this.setScrollingCacheEnabled(false);
         } else {
            var12 = this.getScrollX();
         }

         int var6 = this.getScrollY();
         int var7 = var1 - var12;
         var2 -= var6;
         if (var7 == 0 && var2 == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
         } else {
            this.setScrollingCacheEnabled(true);
            this.setScrollState(2);
            var1 = this.getClientWidth();
            int var8 = var1 / 2;
            float var9 = (float)Math.abs(var7);
            float var10 = (float)var1;
            float var11 = Math.min(1.0F, var9 * 1.0F / var10);
            var9 = (float)var8;
            var11 = this.distanceInfluenceForSnapDuration(var11);
            var1 = Math.abs(var3);
            if (var1 > 0) {
               var1 = Math.round(Math.abs((var9 + var11 * var9) / (float)var1) * 1000.0F) * 4;
            } else {
               var9 = this.mAdapter.getPageWidth(this.mCurItem);
               var1 = (int)(((float)Math.abs(var7) / (var10 * var9 + (float)this.mPageMargin) + 1.0F) * 100.0F);
            }

            var1 = Math.min(var1, 600);
            this.mIsScrollStarted = false;
            this.mScroller.startScroll(var12, var6, var7, var2, var1);
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
         PagerAdapter var1 = ViewPager.this.mAdapter;
         boolean var2 = true;
         if (var1 == null || var1.getCount() <= 1) {
            var2 = false;
         }

         return var2;
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(ViewPager.class.getName());
         var2.setScrollable(this.canScroll());
         if (var2.getEventType() == 4096) {
            PagerAdapter var3 = ViewPager.this.mAdapter;
            if (var3 != null) {
               var2.setItemCount(var3.getCount());
               var2.setFromIndex(ViewPager.this.mCurItem);
               var2.setToIndex(ViewPager.this.mCurItem);
            }
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
         if (super.performAccessibilityAction(var1, var2, var3)) {
            return true;
         } else {
            ViewPager var4;
            if (var2 != 4096) {
               if (var2 != 8192) {
                  return false;
               } else if (ViewPager.this.canScrollHorizontally(-1)) {
                  var4 = ViewPager.this;
                  var4.setCurrentItem(var4.mCurItem - 1);
                  return true;
               } else {
                  return false;
               }
            } else if (ViewPager.this.canScrollHorizontally(1)) {
               var4 = ViewPager.this;
               var4.setCurrentItem(var4.mCurItem + 1);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   public interface OnAdapterChangeListener {
      void onAdapterChanged(ViewPager var1, PagerAdapter var2, PagerAdapter var3);
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
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public ViewPager.SavedState createFromParcel(Parcel var1) {
            return new ViewPager.SavedState(var1, (ClassLoader)null);
         }

         public ViewPager.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new ViewPager.SavedState(var1, var2);
         }

         public ViewPager.SavedState[] newArray(int var1) {
            return new ViewPager.SavedState[var1];
         }
      };
      Parcelable adapterState;
      ClassLoader loader;
      int position;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         ClassLoader var3 = var2;
         if (var2 == null) {
            var3 = ViewPager.SavedState.class.getClassLoader();
         }

         this.position = var1.readInt();
         this.adapterState = var1.readParcelable(var3);
         this.loader = var3;
      }

      public SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("FragmentPager.SavedState{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" position=");
         var1.append(this.position);
         var1.append("}");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.position);
         var1.writeParcelable(this.adapterState, var2);
      }
   }

   static class ViewPositionComparator implements Comparator {
      public int compare(View var1, View var2) {
         ViewPager.LayoutParams var5 = (ViewPager.LayoutParams)var1.getLayoutParams();
         ViewPager.LayoutParams var6 = (ViewPager.LayoutParams)var2.getLayoutParams();
         boolean var3 = var5.isDecor;
         if (var3 != var6.isDecor) {
            byte var4;
            if (var3) {
               var4 = 1;
            } else {
               var4 = -1;
            }

            return var4;
         } else {
            return var5.position - var6.position;
         }
      }
   }
}
