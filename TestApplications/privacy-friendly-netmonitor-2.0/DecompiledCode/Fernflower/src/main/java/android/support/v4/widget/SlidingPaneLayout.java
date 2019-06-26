package android.support.v4.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup {
   private static final int DEFAULT_FADE_COLOR = -858993460;
   private static final int DEFAULT_OVERHANG_SIZE = 32;
   static final SlidingPaneLayout.SlidingPanelLayoutImpl IMPL;
   private static final int MIN_FLING_VELOCITY = 400;
   private static final String TAG = "SlidingPaneLayout";
   private boolean mCanSlide;
   private int mCoveredFadeColor;
   final ViewDragHelper mDragHelper;
   private boolean mFirstLayout;
   private float mInitialMotionX;
   private float mInitialMotionY;
   boolean mIsUnableToDrag;
   private final int mOverhangSize;
   private SlidingPaneLayout.PanelSlideListener mPanelSlideListener;
   private int mParallaxBy;
   private float mParallaxOffset;
   final ArrayList mPostedRunnables;
   boolean mPreservedOpenState;
   private Drawable mShadowDrawableLeft;
   private Drawable mShadowDrawableRight;
   float mSlideOffset;
   int mSlideRange;
   View mSlideableView;
   private int mSliderFadeColor;
   private final Rect mTmpRect;

   static {
      if (VERSION.SDK_INT >= 17) {
         IMPL = new SlidingPaneLayout.SlidingPanelLayoutImplJBMR1();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new SlidingPaneLayout.SlidingPanelLayoutImplJB();
      } else {
         IMPL = new SlidingPaneLayout.SlidingPanelLayoutImplBase();
      }

   }

   public SlidingPaneLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SlidingPaneLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public SlidingPaneLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSliderFadeColor = -858993460;
      this.mFirstLayout = true;
      this.mTmpRect = new Rect();
      this.mPostedRunnables = new ArrayList();
      float var4 = var1.getResources().getDisplayMetrics().density;
      this.mOverhangSize = (int)(32.0F * var4 + 0.5F);
      this.setWillNotDraw(false);
      ViewCompat.setAccessibilityDelegate(this, new SlidingPaneLayout.AccessibilityDelegate());
      ViewCompat.setImportantForAccessibility(this, 1);
      this.mDragHelper = ViewDragHelper.create(this, 0.5F, new SlidingPaneLayout.DragHelperCallback());
      this.mDragHelper.setMinVelocity(400.0F * var4);
   }

   private boolean closePane(View var1, int var2) {
      if (!this.mFirstLayout && !this.smoothSlideTo(0.0F, var2)) {
         return false;
      } else {
         this.mPreservedOpenState = false;
         return true;
      }
   }

   private void dimChildView(View var1, float var2, int var3) {
      SlidingPaneLayout.LayoutParams var4 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
      if (var2 > 0.0F && var3 != 0) {
         int var5 = (int)((float)((-16777216 & var3) >>> 24) * var2);
         if (var4.dimPaint == null) {
            var4.dimPaint = new Paint();
         }

         var4.dimPaint.setColorFilter(new PorterDuffColorFilter(var5 << 24 | var3 & 16777215, Mode.SRC_OVER));
         if (var1.getLayerType() != 2) {
            var1.setLayerType(2, var4.dimPaint);
         }

         this.invalidateChildRegion(var1);
      } else if (var1.getLayerType() != 0) {
         if (var4.dimPaint != null) {
            var4.dimPaint.setColorFilter((ColorFilter)null);
         }

         SlidingPaneLayout.DisableLayerRunnable var6 = new SlidingPaneLayout.DisableLayerRunnable(var1);
         this.mPostedRunnables.add(var6);
         ViewCompat.postOnAnimation(this, var6);
      }

   }

   private boolean openPane(View var1, int var2) {
      if (!this.mFirstLayout && !this.smoothSlideTo(1.0F, var2)) {
         return false;
      } else {
         this.mPreservedOpenState = true;
         return true;
      }
   }

   private void parallaxOtherViews(float var1) {
      boolean var2;
      int var5;
      boolean var12;
      label40: {
         var2 = this.isLayoutRtlSupport();
         SlidingPaneLayout.LayoutParams var3 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         boolean var4 = var3.dimWhenOffset;
         var5 = 0;
         if (var4) {
            int var6;
            if (var2) {
               var6 = var3.rightMargin;
            } else {
               var6 = var3.leftMargin;
            }

            if (var6 <= 0) {
               var12 = true;
               break label40;
            }
         }

         var12 = false;
      }

      for(int var7 = this.getChildCount(); var5 < var7; ++var5) {
         View var11 = this.getChildAt(var5);
         if (var11 != this.mSlideableView) {
            int var8 = (int)((1.0F - this.mParallaxOffset) * (float)this.mParallaxBy);
            this.mParallaxOffset = var1;
            int var9 = var8 - (int)((1.0F - var1) * (float)this.mParallaxBy);
            var8 = var9;
            if (var2) {
               var8 = -var9;
            }

            var11.offsetLeftAndRight(var8);
            if (var12) {
               float var10;
               if (var2) {
                  var10 = this.mParallaxOffset - 1.0F;
               } else {
                  var10 = 1.0F - this.mParallaxOffset;
               }

               this.dimChildView(var11, var10, this.mCoveredFadeColor);
            }
         }
      }

   }

   private static boolean viewIsOpaque(View var0) {
      boolean var1 = var0.isOpaque();
      boolean var2 = true;
      if (var1) {
         return true;
      } else if (VERSION.SDK_INT >= 18) {
         return false;
      } else {
         Drawable var3 = var0.getBackground();
         if (var3 != null) {
            if (var3.getOpacity() != -1) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
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

      if (var2) {
         if (!this.isLayoutRtlSupport()) {
            var3 = -var3;
         }

         if (var1.canScrollHorizontally(var3)) {
            var2 = var7;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   @Deprecated
   public boolean canSlide() {
      return this.mCanSlide;
   }

   protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      boolean var2;
      if (var1 instanceof SlidingPaneLayout.LayoutParams && super.checkLayoutParams(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean closePane() {
      return this.closePane(this.mSlideableView, 0);
   }

   public void computeScroll() {
      if (this.mDragHelper.continueSettling(true)) {
         if (!this.mCanSlide) {
            this.mDragHelper.abort();
            return;
         }

         ViewCompat.postInvalidateOnAnimation(this);
      }

   }

   void dispatchOnPanelClosed(View var1) {
      if (this.mPanelSlideListener != null) {
         this.mPanelSlideListener.onPanelClosed(var1);
      }

      this.sendAccessibilityEvent(32);
   }

   void dispatchOnPanelOpened(View var1) {
      if (this.mPanelSlideListener != null) {
         this.mPanelSlideListener.onPanelOpened(var1);
      }

      this.sendAccessibilityEvent(32);
   }

   void dispatchOnPanelSlide(View var1) {
      if (this.mPanelSlideListener != null) {
         this.mPanelSlideListener.onPanelSlide(var1, this.mSlideOffset);
      }

   }

   public void draw(Canvas var1) {
      super.draw(var1);
      Drawable var2;
      if (this.isLayoutRtlSupport()) {
         var2 = this.mShadowDrawableRight;
      } else {
         var2 = this.mShadowDrawableLeft;
      }

      View var3;
      if (this.getChildCount() > 1) {
         var3 = this.getChildAt(1);
      } else {
         var3 = null;
      }

      if (var3 != null && var2 != null) {
         int var4 = var3.getTop();
         int var5 = var3.getBottom();
         int var6 = var2.getIntrinsicWidth();
         int var7;
         int var8;
         if (this.isLayoutRtlSupport()) {
            var7 = var3.getRight();
            var8 = var6 + var7;
         } else {
            var7 = var3.getLeft();
            var8 = var7;
            var7 -= var6;
         }

         var2.setBounds(var7, var4, var8, var5);
         var2.draw(var1);
      }
   }

   protected boolean drawChild(Canvas var1, View var2, long var3) {
      SlidingPaneLayout.LayoutParams var5 = (SlidingPaneLayout.LayoutParams)var2.getLayoutParams();
      int var6 = var1.save(2);
      if (this.mCanSlide && !var5.slideable && this.mSlideableView != null) {
         var1.getClipBounds(this.mTmpRect);
         if (this.isLayoutRtlSupport()) {
            this.mTmpRect.left = Math.max(this.mTmpRect.left, this.mSlideableView.getRight());
         } else {
            this.mTmpRect.right = Math.min(this.mTmpRect.right, this.mSlideableView.getLeft());
         }

         var1.clipRect(this.mTmpRect);
      }

      boolean var7 = super.drawChild(var1, var2, var3);
      var1.restoreToCount(var6);
      return var7;
   }

   protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
      return new SlidingPaneLayout.LayoutParams();
   }

   public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet var1) {
      return new SlidingPaneLayout.LayoutParams(this.getContext(), var1);
   }

   protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      SlidingPaneLayout.LayoutParams var2;
      if (var1 instanceof MarginLayoutParams) {
         var2 = new SlidingPaneLayout.LayoutParams((MarginLayoutParams)var1);
      } else {
         var2 = new SlidingPaneLayout.LayoutParams(var1);
      }

      return var2;
   }

   @ColorInt
   public int getCoveredFadeColor() {
      return this.mCoveredFadeColor;
   }

   public int getParallaxDistance() {
      return this.mParallaxBy;
   }

   @ColorInt
   public int getSliderFadeColor() {
      return this.mSliderFadeColor;
   }

   void invalidateChildRegion(View var1) {
      IMPL.invalidateChildRegion(this, var1);
   }

   boolean isDimmed(View var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         SlidingPaneLayout.LayoutParams var4 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
         boolean var3 = var2;
         if (this.mCanSlide) {
            var3 = var2;
            if (var4.dimWhenOffset) {
               var3 = var2;
               if (this.mSlideOffset > 0.0F) {
                  var3 = true;
               }
            }
         }

         return var3;
      }
   }

   boolean isLayoutRtlSupport() {
      int var1 = ViewCompat.getLayoutDirection(this);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public boolean isOpen() {
      boolean var1;
      if (this.mCanSlide && this.mSlideOffset != 1.0F) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isSlideable() {
      return this.mCanSlide;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.mFirstLayout = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.mFirstLayout = true;
      int var1 = this.mPostedRunnables.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         ((SlidingPaneLayout.DisableLayerRunnable)this.mPostedRunnables.get(var2)).run();
      }

      this.mPostedRunnables.clear();
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      boolean var3 = this.mCanSlide;
      boolean var4 = true;
      if (!var3 && var2 == 0 && this.getChildCount() > 1) {
         View var5 = this.getChildAt(1);
         if (var5 != null) {
            this.mPreservedOpenState = this.mDragHelper.isViewUnder(var5, (int)var1.getX(), (int)var1.getY()) ^ true;
         }
      }

      if (!this.mCanSlide || this.mIsUnableToDrag && var2 != 0) {
         this.mDragHelper.cancel();
         return super.onInterceptTouchEvent(var1);
      } else if (var2 != 3 && var2 != 1) {
         boolean var8;
         label47: {
            float var6;
            float var7;
            if (var2 != 0) {
               if (var2 == 2) {
                  var6 = var1.getX();
                  var7 = var1.getY();
                  var6 = Math.abs(var6 - this.mInitialMotionX);
                  var7 = Math.abs(var7 - this.mInitialMotionY);
                  if (var6 > (float)this.mDragHelper.getTouchSlop() && var7 > var6) {
                     this.mDragHelper.cancel();
                     this.mIsUnableToDrag = true;
                     return false;
                  }
               }
            } else {
               this.mIsUnableToDrag = false;
               var7 = var1.getX();
               var6 = var1.getY();
               this.mInitialMotionX = var7;
               this.mInitialMotionY = var6;
               if (this.mDragHelper.isViewUnder(this.mSlideableView, (int)var7, (int)var6) && this.isDimmed(this.mSlideableView)) {
                  var8 = true;
                  break label47;
               }
            }

            var8 = false;
         }

         var3 = var4;
         if (!this.mDragHelper.shouldInterceptTouchEvent(var1)) {
            if (var8) {
               var3 = var4;
            } else {
               var3 = false;
            }
         }

         return var3;
      } else {
         this.mDragHelper.cancel();
         return false;
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      boolean var6 = this.isLayoutRtlSupport();
      if (var6) {
         this.mDragHelper.setEdgeTrackingEnabled(2);
      } else {
         this.mDragHelper.setEdgeTrackingEnabled(1);
      }

      int var7 = var4 - var2;
      if (var6) {
         var2 = this.getPaddingRight();
      } else {
         var2 = this.getPaddingLeft();
      }

      if (var6) {
         var4 = this.getPaddingLeft();
      } else {
         var4 = this.getPaddingRight();
      }

      int var8 = this.getPaddingTop();
      int var9 = this.getChildCount();
      if (this.mFirstLayout) {
         float var10;
         if (this.mCanSlide && this.mPreservedOpenState) {
            var10 = 1.0F;
         } else {
            var10 = 0.0F;
         }

         this.mSlideOffset = var10;
      }

      var3 = var2;

      for(var5 = 0; var5 < var9; ++var5) {
         View var11 = this.getChildAt(var5);
         if (var11.getVisibility() != 8) {
            int var13;
            int var14;
            int var16;
            label85: {
               SlidingPaneLayout.LayoutParams var12 = (SlidingPaneLayout.LayoutParams)var11.getLayoutParams();
               var13 = var11.getMeasuredWidth();
               if (var12.slideable) {
                  var14 = var12.leftMargin;
                  int var15 = var12.rightMargin;
                  var16 = var7 - var4;
                  var15 = Math.min(var2, var16 - this.mOverhangSize) - var3 - (var14 + var15);
                  this.mSlideRange = var15;
                  if (var6) {
                     var14 = var12.rightMargin;
                  } else {
                     var14 = var12.leftMargin;
                  }

                  if (var3 + var14 + var15 + var13 / 2 > var16) {
                     var1 = true;
                  } else {
                     var1 = false;
                  }

                  var12.dimWhenOffset = var1;
                  var16 = (int)((float)var15 * this.mSlideOffset);
                  var3 += var14 + var16;
                  this.mSlideOffset = (float)var16 / (float)this.mSlideRange;
               } else {
                  if (this.mCanSlide && this.mParallaxBy != 0) {
                     var14 = (int)((1.0F - this.mSlideOffset) * (float)this.mParallaxBy);
                     var3 = var2;
                     break label85;
                  }

                  var3 = var2;
               }

               var14 = 0;
            }

            if (var6) {
               var16 = var7 - var3 + var14;
               var14 = var16 - var13;
            } else {
               var14 = var3 - var14;
               var16 = var14 + var13;
            }

            var11.layout(var14, var8, var16, var11.getMeasuredHeight() + var8);
            var2 += var11.getWidth();
         }
      }

      if (this.mFirstLayout) {
         if (this.mCanSlide) {
            if (this.mParallaxBy != 0) {
               this.parallaxOtherViews(this.mSlideOffset);
            }

            if (((SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset) {
               this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
            }
         } else {
            for(var2 = 0; var2 < var9; ++var2) {
               this.dimChildView(this.getChildAt(var2), 0.0F, this.mSliderFadeColor);
            }
         }

         this.updateObscuredViewsVisibility(this.mSlideableView);
      }

      this.mFirstLayout = false;
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getMode(var1);
      int var4 = MeasureSpec.getSize(var1);
      int var5 = MeasureSpec.getMode(var2);
      var2 = MeasureSpec.getSize(var2);
      int var6;
      int var7;
      if (var3 != 1073741824) {
         if (!this.isInEditMode()) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
         }

         if (var3 == Integer.MIN_VALUE) {
            var6 = var4;
            var7 = var5;
            var1 = var2;
         } else {
            var6 = var4;
            var7 = var5;
            var1 = var2;
            if (var3 == 0) {
               var6 = 300;
               var7 = var5;
               var1 = var2;
            }
         }
      } else {
         var6 = var4;
         var7 = var5;
         var1 = var2;
         if (var5 == 0) {
            if (!this.isInEditMode()) {
               throw new IllegalStateException("Height must not be UNSPECIFIED");
            }

            var6 = var4;
            var7 = var5;
            var1 = var2;
            if (var5 == 0) {
               var1 = 300;
               var7 = Integer.MIN_VALUE;
               var6 = var4;
            }
         }
      }

      if (var7 != Integer.MIN_VALUE) {
         if (var7 != 1073741824) {
            var1 = 0;
         } else {
            var1 = var1 - this.getPaddingTop() - this.getPaddingBottom();
         }

         var5 = var1;
      } else {
         var5 = var1 - this.getPaddingTop() - this.getPaddingBottom();
         var1 = 0;
      }

      int var8 = var6 - this.getPaddingLeft() - this.getPaddingRight();
      int var9 = this.getChildCount();
      if (var9 > 2) {
         Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
      }

      this.mSlideableView = null;
      int var10 = 0;
      var3 = var8;
      float var11 = 0.0F;
      int var12 = var10;

      View var13;
      SlidingPaneLayout.LayoutParams var14;
      int var16;
      for(var2 = var1; var12 < var9; var2 = var1) {
         var13 = this.getChildAt(var12);
         var14 = (SlidingPaneLayout.LayoutParams)var13.getLayoutParams();
         float var15;
         if (var13.getVisibility() == 8) {
            var14.dimWhenOffset = false;
            var15 = var11;
            var1 = var2;
         } else {
            label190: {
               var15 = var11;
               if (var14.weight > 0.0F) {
                  var11 += var14.weight;
                  var15 = var11;
                  if (var14.width == 0) {
                     var15 = var11;
                     var1 = var2;
                     break label190;
                  }
               }

               var1 = var14.leftMargin + var14.rightMargin;
               if (var14.width == -2) {
                  var1 = MeasureSpec.makeMeasureSpec(var8 - var1, Integer.MIN_VALUE);
               } else if (var14.width == -1) {
                  var1 = MeasureSpec.makeMeasureSpec(var8 - var1, 1073741824);
               } else {
                  var1 = MeasureSpec.makeMeasureSpec(var14.width, 1073741824);
               }

               if (var14.height == -2) {
                  var4 = MeasureSpec.makeMeasureSpec(var5, Integer.MIN_VALUE);
               } else if (var14.height == -1) {
                  var4 = MeasureSpec.makeMeasureSpec(var5, 1073741824);
               } else {
                  var4 = MeasureSpec.makeMeasureSpec(var14.height, 1073741824);
               }

               var13.measure(var1, var4);
               var4 = var13.getMeasuredWidth();
               var16 = var13.getMeasuredHeight();
               var1 = var2;
               if (var7 == Integer.MIN_VALUE) {
                  var1 = var2;
                  if (var16 > var2) {
                     var1 = Math.min(var16, var5);
                  }
               }

               var3 -= var4;
               byte var17;
               if (var3 < 0) {
                  var17 = 1;
               } else {
                  var17 = 0;
               }

               var14.slideable = (boolean)var17;
               if (var14.slideable) {
                  this.mSlideableView = var13;
               }

               var10 |= var17;
            }
         }

         ++var12;
         var11 = var15;
      }

      if (var10 != 0 || var11 > 0.0F) {
         var7 = var8 - this.mOverhangSize;

         for(var4 = 0; var4 < var9; ++var4) {
            var13 = this.getChildAt(var4);
            if (var13.getVisibility() != 8) {
               var14 = (SlidingPaneLayout.LayoutParams)var13.getLayoutParams();
               if (var13.getVisibility() != 8) {
                  boolean var19;
                  if (var14.width == 0 && var14.weight > 0.0F) {
                     var19 = true;
                  } else {
                     var19 = false;
                  }

                  if (var19) {
                     var12 = 0;
                  } else {
                     var12 = var13.getMeasuredWidth();
                  }

                  if (var10 != 0 && var13 != this.mSlideableView) {
                     if (var14.width < 0 && (var12 > var7 || var14.weight > 0.0F)) {
                        if (var19) {
                           if (var14.height == -2) {
                              var1 = MeasureSpec.makeMeasureSpec(var5, Integer.MIN_VALUE);
                           } else if (var14.height == -1) {
                              var1 = MeasureSpec.makeMeasureSpec(var5, 1073741824);
                           } else {
                              var1 = MeasureSpec.makeMeasureSpec(var14.height, 1073741824);
                           }
                        } else {
                           var1 = MeasureSpec.makeMeasureSpec(var13.getMeasuredHeight(), 1073741824);
                        }

                        var13.measure(MeasureSpec.makeMeasureSpec(var7, 1073741824), var1);
                     }
                  } else if (var14.weight > 0.0F) {
                     if (var14.width == 0) {
                        if (var14.height == -2) {
                           var1 = MeasureSpec.makeMeasureSpec(var5, Integer.MIN_VALUE);
                        } else if (var14.height == -1) {
                           var1 = MeasureSpec.makeMeasureSpec(var5, 1073741824);
                        } else {
                           var1 = MeasureSpec.makeMeasureSpec(var14.height, 1073741824);
                        }
                     } else {
                        var1 = MeasureSpec.makeMeasureSpec(var13.getMeasuredHeight(), 1073741824);
                     }

                     if (var10 != 0) {
                        int var18 = var8 - (var14.leftMargin + var14.rightMargin);
                        var16 = MeasureSpec.makeMeasureSpec(var18, 1073741824);
                        if (var12 != var18) {
                           var13.measure(var16, var1);
                        }
                     } else {
                        var16 = Math.max(0, var3);
                        var13.measure(MeasureSpec.makeMeasureSpec(var12 + (int)(var14.weight * (float)var16 / var11), 1073741824), var1);
                     }
                  }
               }
            }
         }
      }

      this.setMeasuredDimension(var6, var2 + this.getPaddingTop() + this.getPaddingBottom());
      this.mCanSlide = (boolean)var10;
      if (this.mDragHelper.getViewDragState() != 0 && var10 == 0) {
         this.mDragHelper.abort();
      }

   }

   void onPanelDragged(int var1) {
      if (this.mSlideableView == null) {
         this.mSlideOffset = 0.0F;
      } else {
         boolean var2 = this.isLayoutRtlSupport();
         SlidingPaneLayout.LayoutParams var3 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         int var4 = this.mSlideableView.getWidth();
         int var5 = var1;
         if (var2) {
            var5 = this.getWidth() - var1 - var4;
         }

         if (var2) {
            var1 = this.getPaddingRight();
         } else {
            var1 = this.getPaddingLeft();
         }

         if (var2) {
            var4 = var3.rightMargin;
         } else {
            var4 = var3.leftMargin;
         }

         this.mSlideOffset = (float)(var5 - (var1 + var4)) / (float)this.mSlideRange;
         if (this.mParallaxBy != 0) {
            this.parallaxOtherViews(this.mSlideOffset);
         }

         if (var3.dimWhenOffset) {
            this.dimChildView(this.mSlideableView, this.mSlideOffset, this.mSliderFadeColor);
         }

         this.dispatchOnPanelSlide(this.mSlideableView);
      }
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof SlidingPaneLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         SlidingPaneLayout.SavedState var2 = (SlidingPaneLayout.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         if (var2.isOpen) {
            this.openPane();
         } else {
            this.closePane();
         }

         this.mPreservedOpenState = var2.isOpen;
      }
   }

   protected Parcelable onSaveInstanceState() {
      SlidingPaneLayout.SavedState var1 = new SlidingPaneLayout.SavedState(super.onSaveInstanceState());
      boolean var2;
      if (this.isSlideable()) {
         var2 = this.isOpen();
      } else {
         var2 = this.mPreservedOpenState;
      }

      var1.isOpen = var2;
      return var1;
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 != var3) {
         this.mFirstLayout = true;
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (!this.mCanSlide) {
         return super.onTouchEvent(var1);
      } else {
         this.mDragHelper.processTouchEvent(var1);
         float var3;
         float var5;
         switch(var1.getActionMasked()) {
         case 0:
            var3 = var1.getX();
            var5 = var1.getY();
            this.mInitialMotionX = var3;
            this.mInitialMotionY = var5;
            break;
         case 1:
            if (this.isDimmed(this.mSlideableView)) {
               float var2 = var1.getX();
               var3 = var1.getY();
               float var4 = var2 - this.mInitialMotionX;
               var5 = var3 - this.mInitialMotionY;
               int var6 = this.mDragHelper.getTouchSlop();
               if (var4 * var4 + var5 * var5 < (float)(var6 * var6) && this.mDragHelper.isViewUnder(this.mSlideableView, (int)var2, (int)var3)) {
                  this.closePane(this.mSlideableView, 0);
               }
            }
         }

         return true;
      }
   }

   public boolean openPane() {
      return this.openPane(this.mSlideableView, 0);
   }

   public void requestChildFocus(View var1, View var2) {
      super.requestChildFocus(var1, var2);
      if (!this.isInTouchMode() && !this.mCanSlide) {
         boolean var3;
         if (var1 == this.mSlideableView) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mPreservedOpenState = var3;
      }

   }

   void setAllChildrenVisible() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3.getVisibility() == 4) {
            var3.setVisibility(0);
         }
      }

   }

   public void setCoveredFadeColor(@ColorInt int var1) {
      this.mCoveredFadeColor = var1;
   }

   public void setPanelSlideListener(SlidingPaneLayout.PanelSlideListener var1) {
      this.mPanelSlideListener = var1;
   }

   public void setParallaxDistance(int var1) {
      this.mParallaxBy = var1;
      this.requestLayout();
   }

   @Deprecated
   public void setShadowDrawable(Drawable var1) {
      this.setShadowDrawableLeft(var1);
   }

   public void setShadowDrawableLeft(Drawable var1) {
      this.mShadowDrawableLeft = var1;
   }

   public void setShadowDrawableRight(Drawable var1) {
      this.mShadowDrawableRight = var1;
   }

   @Deprecated
   public void setShadowResource(@DrawableRes int var1) {
      this.setShadowDrawable(this.getResources().getDrawable(var1));
   }

   public void setShadowResourceLeft(int var1) {
      this.setShadowDrawableLeft(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setShadowResourceRight(int var1) {
      this.setShadowDrawableRight(ContextCompat.getDrawable(this.getContext(), var1));
   }

   public void setSliderFadeColor(@ColorInt int var1) {
      this.mSliderFadeColor = var1;
   }

   @Deprecated
   public void smoothSlideClosed() {
      this.closePane();
   }

   @Deprecated
   public void smoothSlideOpen() {
      this.openPane();
   }

   boolean smoothSlideTo(float var1, int var2) {
      if (!this.mCanSlide) {
         return false;
      } else {
         boolean var3 = this.isLayoutRtlSupport();
         SlidingPaneLayout.LayoutParams var4 = (SlidingPaneLayout.LayoutParams)this.mSlideableView.getLayoutParams();
         if (var3) {
            var2 = this.getPaddingRight();
            int var5 = var4.rightMargin;
            int var6 = this.mSlideableView.getWidth();
            var2 = (int)((float)this.getWidth() - ((float)(var2 + var5) + var1 * (float)this.mSlideRange + (float)var6));
         } else {
            var2 = (int)((float)(this.getPaddingLeft() + var4.leftMargin) + var1 * (float)this.mSlideRange);
         }

         if (this.mDragHelper.smoothSlideViewTo(this.mSlideableView, var2, this.mSlideableView.getTop())) {
            this.setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
         } else {
            return false;
         }
      }
   }

   void updateObscuredViewsVisibility(View var1) {
      boolean var2 = this.isLayoutRtlSupport();
      int var3;
      if (var2) {
         var3 = this.getWidth() - this.getPaddingRight();
      } else {
         var3 = this.getPaddingLeft();
      }

      int var4;
      if (var2) {
         var4 = this.getPaddingLeft();
      } else {
         var4 = this.getWidth() - this.getPaddingRight();
      }

      int var5 = this.getPaddingTop();
      int var6 = this.getHeight();
      int var7 = this.getPaddingBottom();
      int var8;
      int var9;
      int var10;
      int var11;
      if (var1 != null && viewIsOpaque(var1)) {
         var8 = var1.getLeft();
         var9 = var1.getRight();
         var10 = var1.getTop();
         var11 = var1.getBottom();
      } else {
         var8 = 0;
         var9 = 0;
         var10 = 0;
         var11 = 0;
      }

      int var12 = this.getChildCount();

      for(int var13 = 0; var13 < var12; ++var13) {
         View var14 = this.getChildAt(var13);
         if (var14 == var1) {
            break;
         }

         if (var14.getVisibility() != 8) {
            int var15;
            if (var2) {
               var15 = var4;
            } else {
               var15 = var3;
            }

            int var16 = Math.max(var15, var14.getLeft());
            int var17 = Math.max(var5, var14.getTop());
            if (var2) {
               var15 = var3;
            } else {
               var15 = var4;
            }

            int var18 = Math.min(var15, var14.getRight());
            var15 = Math.min(var6 - var7, var14.getBottom());
            byte var19;
            if (var16 >= var8 && var17 >= var10 && var18 <= var9 && var15 <= var11) {
               var19 = 4;
            } else {
               var19 = 0;
            }

            var14.setVisibility(var19);
         }
      }

   }

   class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final Rect mTmpRect = new Rect();

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
         var1.setMovementGranularities(var2.getMovementGranularities());
      }

      public boolean filter(View var1) {
         return SlidingPaneLayout.this.isDimmed(var1);
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(SlidingPaneLayout.class.getName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         AccessibilityNodeInfoCompat var3 = AccessibilityNodeInfoCompat.obtain(var2);
         super.onInitializeAccessibilityNodeInfo(var1, var3);
         this.copyNodeInfoNoChildren(var2, var3);
         var3.recycle();
         var2.setClassName(SlidingPaneLayout.class.getName());
         var2.setSource(var1);
         ViewParent var6 = ViewCompat.getParentForAccessibility(var1);
         if (var6 instanceof View) {
            var2.setParent((View)var6);
         }

         int var4 = SlidingPaneLayout.this.getChildCount();

         for(int var5 = 0; var5 < var4; ++var5) {
            var1 = SlidingPaneLayout.this.getChildAt(var5);
            if (!this.filter(var1) && var1.getVisibility() == 0) {
               ViewCompat.setImportantForAccessibility(var1, 1);
               var2.addChild(var1);
            }
         }

      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup var1, View var2, AccessibilityEvent var3) {
         return !this.filter(var2) ? super.onRequestSendAccessibilityEvent(var1, var2, var3) : false;
      }
   }

   private class DisableLayerRunnable implements Runnable {
      final View mChildView;

      DisableLayerRunnable(View var2) {
         this.mChildView = var2;
      }

      public void run() {
         if (this.mChildView.getParent() == SlidingPaneLayout.this) {
            this.mChildView.setLayerType(0, (Paint)null);
            SlidingPaneLayout.this.invalidateChildRegion(this.mChildView);
         }

         SlidingPaneLayout.this.mPostedRunnables.remove(this);
      }
   }

   private class DragHelperCallback extends ViewDragHelper.Callback {
      DragHelperCallback() {
      }

      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         SlidingPaneLayout.LayoutParams var5 = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
         int var4;
         if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
            var4 = SlidingPaneLayout.this.getWidth() - (SlidingPaneLayout.this.getPaddingRight() + var5.rightMargin + SlidingPaneLayout.this.mSlideableView.getWidth());
            var3 = SlidingPaneLayout.this.mSlideRange;
            var2 = Math.max(Math.min(var2, var4), var4 - var3);
         } else {
            var3 = SlidingPaneLayout.this.getPaddingLeft() + var5.leftMargin;
            var4 = SlidingPaneLayout.this.mSlideRange;
            var2 = Math.min(Math.max(var2, var3), var4 + var3);
         }

         return var2;
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return var1.getTop();
      }

      public int getViewHorizontalDragRange(View var1) {
         return SlidingPaneLayout.this.mSlideRange;
      }

      public void onEdgeDragStarted(int var1, int var2) {
         SlidingPaneLayout.this.mDragHelper.captureChildView(SlidingPaneLayout.this.mSlideableView, var2);
      }

      public void onViewCaptured(View var1, int var2) {
         SlidingPaneLayout.this.setAllChildrenVisible();
      }

      public void onViewDragStateChanged(int var1) {
         if (SlidingPaneLayout.this.mDragHelper.getViewDragState() == 0) {
            if (SlidingPaneLayout.this.mSlideOffset == 0.0F) {
               SlidingPaneLayout.this.updateObscuredViewsVisibility(SlidingPaneLayout.this.mSlideableView);
               SlidingPaneLayout.this.dispatchOnPanelClosed(SlidingPaneLayout.this.mSlideableView);
               SlidingPaneLayout.this.mPreservedOpenState = false;
            } else {
               SlidingPaneLayout.this.dispatchOnPanelOpened(SlidingPaneLayout.this.mSlideableView);
               SlidingPaneLayout.this.mPreservedOpenState = true;
            }
         }

      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         SlidingPaneLayout.this.onPanelDragged(var2);
         SlidingPaneLayout.this.invalidate();
      }

      public void onViewReleased(View var1, float var2, float var3) {
         SlidingPaneLayout.LayoutParams var4 = (SlidingPaneLayout.LayoutParams)var1.getLayoutParams();
         int var5;
         int var6;
         if (SlidingPaneLayout.this.isLayoutRtlSupport()) {
            label26: {
               var5 = SlidingPaneLayout.this.getPaddingRight() + var4.rightMargin;
               if (var2 >= 0.0F) {
                  var6 = var5;
                  if (var2 != 0.0F) {
                     break label26;
                  }

                  var6 = var5;
                  if (SlidingPaneLayout.this.mSlideOffset <= 0.5F) {
                     break label26;
                  }
               }

               var6 = var5 + SlidingPaneLayout.this.mSlideRange;
            }

            var5 = SlidingPaneLayout.this.mSlideableView.getWidth();
            var6 = SlidingPaneLayout.this.getWidth() - var6 - var5;
         } else {
            label20: {
               var6 = SlidingPaneLayout.this.getPaddingLeft();
               var5 = var4.leftMargin + var6;
               if (var2 <= 0.0F) {
                  var6 = var5;
                  if (var2 != 0.0F) {
                     break label20;
                  }

                  var6 = var5;
                  if (SlidingPaneLayout.this.mSlideOffset <= 0.5F) {
                     break label20;
                  }
               }

               var6 = var5 + SlidingPaneLayout.this.mSlideRange;
            }
         }

         SlidingPaneLayout.this.mDragHelper.settleCapturedViewAt(var6, var1.getTop());
         SlidingPaneLayout.this.invalidate();
      }

      public boolean tryCaptureView(View var1, int var2) {
         return SlidingPaneLayout.this.mIsUnableToDrag ? false : ((SlidingPaneLayout.LayoutParams)var1.getLayoutParams()).slideable;
      }
   }

   public static class LayoutParams extends MarginLayoutParams {
      private static final int[] ATTRS = new int[]{16843137};
      Paint dimPaint;
      boolean dimWhenOffset;
      boolean slideable;
      public float weight = 0.0F;

      public LayoutParams() {
         super(-1, -1);
      }

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, ATTRS);
         this.weight = var3.getFloat(0, 0.0F);
         var3.recycle();
      }

      public LayoutParams(SlidingPaneLayout.LayoutParams var1) {
         super(var1);
         this.weight = var1.weight;
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }
   }

   public interface PanelSlideListener {
      void onPanelClosed(View var1);

      void onPanelOpened(View var1);

      void onPanelSlide(View var1, float var2);
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public SlidingPaneLayout.SavedState createFromParcel(Parcel var1) {
            return new SlidingPaneLayout.SavedState(var1, (ClassLoader)null);
         }

         public SlidingPaneLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new SlidingPaneLayout.SavedState(var1, (ClassLoader)null);
         }

         public SlidingPaneLayout.SavedState[] newArray(int var1) {
            return new SlidingPaneLayout.SavedState[var1];
         }
      };
      boolean isOpen;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         boolean var3;
         if (var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.isOpen = var3;
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.isOpen);
      }
   }

   public static class SimplePanelSlideListener implements SlidingPaneLayout.PanelSlideListener {
      public void onPanelClosed(View var1) {
      }

      public void onPanelOpened(View var1) {
      }

      public void onPanelSlide(View var1, float var2) {
      }
   }

   interface SlidingPanelLayoutImpl {
      void invalidateChildRegion(SlidingPaneLayout var1, View var2);
   }

   static class SlidingPanelLayoutImplBase implements SlidingPaneLayout.SlidingPanelLayoutImpl {
      public void invalidateChildRegion(SlidingPaneLayout var1, View var2) {
         ViewCompat.postInvalidateOnAnimation(var1, var2.getLeft(), var2.getTop(), var2.getRight(), var2.getBottom());
      }
   }

   @RequiresApi(16)
   static class SlidingPanelLayoutImplJB extends SlidingPaneLayout.SlidingPanelLayoutImplBase {
      private Method mGetDisplayList;
      private Field mRecreateDisplayList;

      SlidingPanelLayoutImplJB() {
         try {
            this.mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[])null);
         } catch (NoSuchMethodException var3) {
            Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", var3);
         }

         try {
            this.mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
            this.mRecreateDisplayList.setAccessible(true);
         } catch (NoSuchFieldException var2) {
            Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", var2);
         }

      }

      public void invalidateChildRegion(SlidingPaneLayout var1, View var2) {
         if (this.mGetDisplayList != null && this.mRecreateDisplayList != null) {
            try {
               this.mRecreateDisplayList.setBoolean(var2, true);
               this.mGetDisplayList.invoke(var2, (Object[])null);
            } catch (Exception var4) {
               Log.e("SlidingPaneLayout", "Error refreshing display list state", var4);
            }

            super.invalidateChildRegion(var1, var2);
         } else {
            var2.invalidate();
         }
      }
   }

   @RequiresApi(17)
   static class SlidingPanelLayoutImplJBMR1 extends SlidingPaneLayout.SlidingPanelLayoutImplBase {
      public void invalidateChildRegion(SlidingPaneLayout var1, View var2) {
         ViewCompat.setLayerPaint(var2, ((SlidingPaneLayout.LayoutParams)var2.getLayoutParams()).dimPaint);
      }
   }
}
