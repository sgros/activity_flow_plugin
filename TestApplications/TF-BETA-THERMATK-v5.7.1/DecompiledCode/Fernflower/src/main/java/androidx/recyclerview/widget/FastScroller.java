package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import androidx.core.view.ViewCompat;

class FastScroller extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {
   private static final int[] EMPTY_STATE_SET = new int[0];
   private static final int[] PRESSED_STATE_SET = new int[]{16842919};
   int mAnimationState = 0;
   private int mDragState = 0;
   private final Runnable mHideRunnable = new Runnable() {
      public void run() {
         FastScroller.this.hide(500);
      }
   };
   float mHorizontalDragX;
   private final int[] mHorizontalRange = new int[2];
   int mHorizontalThumbCenterX;
   private final StateListDrawable mHorizontalThumbDrawable;
   private final int mHorizontalThumbHeight;
   int mHorizontalThumbWidth;
   private final Drawable mHorizontalTrackDrawable;
   private final int mHorizontalTrackHeight;
   private final int mMargin;
   private boolean mNeedHorizontalScrollbar = false;
   private boolean mNeedVerticalScrollbar = false;
   private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
      public void onScrolled(RecyclerView var1, int var2, int var3) {
         FastScroller.this.updateScrollPosition(var1.computeHorizontalScrollOffset(), var1.computeVerticalScrollOffset());
      }
   };
   private RecyclerView mRecyclerView;
   private int mRecyclerViewHeight = 0;
   private int mRecyclerViewWidth = 0;
   private final int mScrollbarMinimumRange;
   final ValueAnimator mShowHideAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
   private int mState = 0;
   float mVerticalDragY;
   private final int[] mVerticalRange = new int[2];
   int mVerticalThumbCenterY;
   final StateListDrawable mVerticalThumbDrawable;
   int mVerticalThumbHeight;
   private final int mVerticalThumbWidth;
   final Drawable mVerticalTrackDrawable;
   private final int mVerticalTrackWidth;

   FastScroller(RecyclerView var1, StateListDrawable var2, Drawable var3, StateListDrawable var4, Drawable var5, int var6, int var7, int var8) {
      this.mVerticalThumbDrawable = var2;
      this.mVerticalTrackDrawable = var3;
      this.mHorizontalThumbDrawable = var4;
      this.mHorizontalTrackDrawable = var5;
      this.mVerticalThumbWidth = Math.max(var6, var2.getIntrinsicWidth());
      this.mVerticalTrackWidth = Math.max(var6, var3.getIntrinsicWidth());
      this.mHorizontalThumbHeight = Math.max(var6, var4.getIntrinsicWidth());
      this.mHorizontalTrackHeight = Math.max(var6, var5.getIntrinsicWidth());
      this.mScrollbarMinimumRange = var7;
      this.mMargin = var8;
      this.mVerticalThumbDrawable.setAlpha(255);
      this.mVerticalTrackDrawable.setAlpha(255);
      this.mShowHideAnimator.addListener(new FastScroller.AnimatorListener());
      this.mShowHideAnimator.addUpdateListener(new FastScroller.AnimatorUpdater());
      this.attachToRecyclerView(var1);
   }

   private void cancelHide() {
      this.mRecyclerView.removeCallbacks(this.mHideRunnable);
   }

   private void destroyCallbacks() {
      this.mRecyclerView.removeItemDecoration(this);
      this.mRecyclerView.removeOnItemTouchListener(this);
      this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
      this.cancelHide();
   }

   private void drawHorizontalScrollbar(Canvas var1) {
      int var2 = this.mRecyclerViewHeight;
      int var3 = this.mHorizontalThumbHeight;
      var2 -= var3;
      int var4 = this.mHorizontalThumbCenterX;
      int var5 = this.mHorizontalThumbWidth;
      var4 -= var5 / 2;
      this.mHorizontalThumbDrawable.setBounds(0, 0, var5, var3);
      this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
      var1.translate(0.0F, (float)var2);
      this.mHorizontalTrackDrawable.draw(var1);
      var1.translate((float)var4, 0.0F);
      this.mHorizontalThumbDrawable.draw(var1);
      var1.translate((float)(-var4), (float)(-var2));
   }

   private void drawVerticalScrollbar(Canvas var1) {
      int var2 = this.mRecyclerViewWidth;
      int var3 = this.mVerticalThumbWidth;
      var2 -= var3;
      int var4 = this.mVerticalThumbCenterY;
      int var5 = this.mVerticalThumbHeight;
      var4 -= var5 / 2;
      this.mVerticalThumbDrawable.setBounds(0, 0, var3, var5);
      this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
      if (this.isLayoutRTL()) {
         this.mVerticalTrackDrawable.draw(var1);
         var1.translate((float)this.mVerticalThumbWidth, (float)var4);
         var1.scale(-1.0F, 1.0F);
         this.mVerticalThumbDrawable.draw(var1);
         var1.scale(1.0F, 1.0F);
         var1.translate((float)(-this.mVerticalThumbWidth), (float)(-var4));
      } else {
         var1.translate((float)var2, 0.0F);
         this.mVerticalTrackDrawable.draw(var1);
         var1.translate(0.0F, (float)var4);
         this.mVerticalThumbDrawable.draw(var1);
         var1.translate((float)(-var2), (float)(-var4));
      }

   }

   private int[] getHorizontalRange() {
      int[] var1 = this.mHorizontalRange;
      int var2 = this.mMargin;
      var1[0] = var2;
      var1[1] = this.mRecyclerViewWidth - var2;
      return var1;
   }

   private int[] getVerticalRange() {
      int[] var1 = this.mVerticalRange;
      int var2 = this.mMargin;
      var1[0] = var2;
      var1[1] = this.mRecyclerViewHeight - var2;
      return var1;
   }

   private void horizontalScrollTo(float var1) {
      int[] var2 = this.getHorizontalRange();
      var1 = Math.max((float)var2[0], Math.min((float)var2[1], var1));
      if (Math.abs((float)this.mHorizontalThumbCenterX - var1) >= 2.0F) {
         int var3 = this.scrollTo(this.mHorizontalDragX, var1, var2, this.mRecyclerView.computeHorizontalScrollRange(), this.mRecyclerView.computeHorizontalScrollOffset(), this.mRecyclerViewWidth);
         if (var3 != 0) {
            this.mRecyclerView.scrollBy(var3, 0);
         }

         this.mHorizontalDragX = var1;
      }
   }

   private boolean isLayoutRTL() {
      int var1 = ViewCompat.getLayoutDirection(this.mRecyclerView);
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   private void resetHideDelay(int var1) {
      this.cancelHide();
      this.mRecyclerView.postDelayed(this.mHideRunnable, (long)var1);
   }

   private int scrollTo(float var1, float var2, int[] var3, int var4, int var5, int var6) {
      int var7 = var3[1] - var3[0];
      if (var7 == 0) {
         return 0;
      } else {
         var1 = (var2 - var1) / (float)var7;
         var4 -= var6;
         var6 = (int)(var1 * (float)var4);
         var5 += var6;
         return var5 < var4 && var5 >= 0 ? var6 : 0;
      }
   }

   private void setupCallbacks() {
      this.mRecyclerView.addItemDecoration(this);
      this.mRecyclerView.addOnItemTouchListener(this);
      this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
   }

   private void verticalScrollTo(float var1) {
      int[] var2 = this.getVerticalRange();
      var1 = Math.max((float)var2[0], Math.min((float)var2[1], var1));
      if (Math.abs((float)this.mVerticalThumbCenterY - var1) >= 2.0F) {
         int var3 = this.scrollTo(this.mVerticalDragY, var1, var2, this.mRecyclerView.computeVerticalScrollRange(), this.mRecyclerView.computeVerticalScrollOffset(), this.mRecyclerViewHeight);
         if (var3 != 0) {
            this.mRecyclerView.scrollBy(0, var3);
         }

         this.mVerticalDragY = var1;
      }
   }

   public void attachToRecyclerView(RecyclerView var1) {
      RecyclerView var2 = this.mRecyclerView;
      if (var2 != var1) {
         if (var2 != null) {
            this.destroyCallbacks();
         }

         this.mRecyclerView = var1;
         if (this.mRecyclerView != null) {
            this.setupCallbacks();
         }

      }
   }

   void hide(int var1) {
      int var2 = this.mAnimationState;
      if (var2 != 1) {
         if (var2 != 2) {
            return;
         }
      } else {
         this.mShowHideAnimator.cancel();
      }

      this.mAnimationState = 3;
      ValueAnimator var3 = this.mShowHideAnimator;
      var3.setFloatValues(new float[]{(Float)var3.getAnimatedValue(), 0.0F});
      this.mShowHideAnimator.setDuration((long)var1);
      this.mShowHideAnimator.start();
   }

   boolean isPointInsideHorizontalThumb(float var1, float var2) {
      boolean var5;
      if (var2 >= (float)(this.mRecyclerViewHeight - this.mHorizontalThumbHeight)) {
         int var3 = this.mHorizontalThumbCenterX;
         int var4 = this.mHorizontalThumbWidth;
         if (var1 >= (float)(var3 - var4 / 2) && var1 <= (float)(var3 + var4 / 2)) {
            var5 = true;
            return var5;
         }
      }

      var5 = false;
      return var5;
   }

   boolean isPointInsideVerticalThumb(float var1, float var2) {
      boolean var5;
      label20: {
         if (this.isLayoutRTL()) {
            if (var1 > (float)(this.mVerticalThumbWidth / 2)) {
               break label20;
            }
         } else if (var1 < (float)(this.mRecyclerViewWidth - this.mVerticalThumbWidth)) {
            break label20;
         }

         int var3 = this.mVerticalThumbCenterY;
         int var4 = this.mVerticalThumbHeight;
         if (var2 >= (float)(var3 - var4 / 2) && var2 <= (float)(var3 + var4 / 2)) {
            var5 = true;
            return var5;
         }
      }

      var5 = false;
      return var5;
   }

   public void onDrawOver(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
      if (this.mRecyclerViewWidth == this.mRecyclerView.getWidth() && this.mRecyclerViewHeight == this.mRecyclerView.getHeight()) {
         if (this.mAnimationState != 0) {
            if (this.mNeedVerticalScrollbar) {
               this.drawVerticalScrollbar(var1);
            }

            if (this.mNeedHorizontalScrollbar) {
               this.drawHorizontalScrollbar(var1);
            }
         }

      } else {
         this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
         this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
         this.setState(0);
      }
   }

   public boolean onInterceptTouchEvent(RecyclerView var1, MotionEvent var2) {
      int var3 = this.mState;
      boolean var4 = false;
      boolean var7;
      if (var3 == 1) {
         boolean var5 = this.isPointInsideVerticalThumb(var2.getX(), var2.getY());
         boolean var6 = this.isPointInsideHorizontalThumb(var2.getX(), var2.getY());
         var7 = var4;
         if (var2.getAction() != 0) {
            return var7;
         }

         if (!var5) {
            var7 = var4;
            if (!var6) {
               return var7;
            }
         }

         if (var6) {
            this.mDragState = 1;
            this.mHorizontalDragX = (float)((int)var2.getX());
         } else if (var5) {
            this.mDragState = 2;
            this.mVerticalDragY = (float)((int)var2.getY());
         }

         this.setState(2);
      } else {
         var7 = var4;
         if (var3 != 2) {
            return var7;
         }
      }

      var7 = true;
      return var7;
   }

   public void onRequestDisallowInterceptTouchEvent(boolean var1) {
   }

   public void onTouchEvent(RecyclerView var1, MotionEvent var2) {
      if (this.mState != 0) {
         if (var2.getAction() == 0) {
            boolean var3 = this.isPointInsideVerticalThumb(var2.getX(), var2.getY());
            boolean var4 = this.isPointInsideHorizontalThumb(var2.getX(), var2.getY());
            if (var3 || var4) {
               if (var4) {
                  this.mDragState = 1;
                  this.mHorizontalDragX = (float)((int)var2.getX());
               } else if (var3) {
                  this.mDragState = 2;
                  this.mVerticalDragY = (float)((int)var2.getY());
               }

               this.setState(2);
            }
         } else if (var2.getAction() == 1 && this.mState == 2) {
            this.mVerticalDragY = 0.0F;
            this.mHorizontalDragX = 0.0F;
            this.setState(1);
            this.mDragState = 0;
         } else if (var2.getAction() == 2 && this.mState == 2) {
            this.show();
            if (this.mDragState == 1) {
               this.horizontalScrollTo(var2.getX());
            }

            if (this.mDragState == 2) {
               this.verticalScrollTo(var2.getY());
            }
         }

      }
   }

   void requestRedraw() {
      this.mRecyclerView.invalidate();
   }

   void setState(int var1) {
      if (var1 == 2 && this.mState != 2) {
         this.mVerticalThumbDrawable.setState(PRESSED_STATE_SET);
         this.cancelHide();
      }

      if (var1 == 0) {
         this.requestRedraw();
      } else {
         this.show();
      }

      if (this.mState == 2 && var1 != 2) {
         this.mVerticalThumbDrawable.setState(EMPTY_STATE_SET);
         this.resetHideDelay(1200);
      } else if (var1 == 1) {
         this.resetHideDelay(1500);
      }

      this.mState = var1;
   }

   public void show() {
      int var1 = this.mAnimationState;
      if (var1 != 0) {
         if (var1 != 3) {
            return;
         }

         this.mShowHideAnimator.cancel();
      }

      this.mAnimationState = 1;
      ValueAnimator var2 = this.mShowHideAnimator;
      var2.setFloatValues(new float[]{(Float)var2.getAnimatedValue(), 1.0F});
      this.mShowHideAnimator.setDuration(500L);
      this.mShowHideAnimator.setStartDelay(0L);
      this.mShowHideAnimator.start();
   }

   void updateScrollPosition(int var1, int var2) {
      int var3 = this.mRecyclerView.computeVerticalScrollRange();
      int var4 = this.mRecyclerViewHeight;
      boolean var5;
      if (var3 - var4 > 0 && var4 >= this.mScrollbarMinimumRange) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.mNeedVerticalScrollbar = var5;
      int var6 = this.mRecyclerView.computeHorizontalScrollRange();
      int var7 = this.mRecyclerViewWidth;
      if (var6 - var7 > 0 && var7 >= this.mScrollbarMinimumRange) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.mNeedHorizontalScrollbar = var5;
      if (!this.mNeedVerticalScrollbar && !this.mNeedHorizontalScrollbar) {
         if (this.mState != 0) {
            this.setState(0);
         }

      } else {
         float var8;
         float var9;
         if (this.mNeedVerticalScrollbar) {
            var8 = (float)var2;
            var9 = (float)var4;
            this.mVerticalThumbCenterY = (int)(var9 * (var8 + var9 / 2.0F) / (float)var3);
            this.mVerticalThumbHeight = Math.min(var4, var4 * var4 / var3);
         }

         if (this.mNeedHorizontalScrollbar) {
            var8 = (float)var1;
            var9 = (float)var7;
            this.mHorizontalThumbCenterX = (int)(var9 * (var8 + var9 / 2.0F) / (float)var6);
            this.mHorizontalThumbWidth = Math.min(var7, var7 * var7 / var6);
         }

         var1 = this.mState;
         if (var1 == 0 || var1 == 1) {
            this.setState(1);
         }

      }
   }

   private class AnimatorListener extends AnimatorListenerAdapter {
      private boolean mCanceled = false;

      AnimatorListener() {
      }

      public void onAnimationCancel(Animator var1) {
         this.mCanceled = true;
      }

      public void onAnimationEnd(Animator var1) {
         if (this.mCanceled) {
            this.mCanceled = false;
         } else {
            FastScroller var2;
            if ((Float)FastScroller.this.mShowHideAnimator.getAnimatedValue() == 0.0F) {
               var2 = FastScroller.this;
               var2.mAnimationState = 0;
               var2.setState(0);
            } else {
               var2 = FastScroller.this;
               var2.mAnimationState = 2;
               var2.requestRedraw();
            }

         }
      }
   }

   private class AnimatorUpdater implements AnimatorUpdateListener {
      AnimatorUpdater() {
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         int var2 = (int)((Float)var1.getAnimatedValue() * 255.0F);
         FastScroller.this.mVerticalThumbDrawable.setAlpha(var2);
         FastScroller.this.mVerticalTrackDrawable.setAlpha(var2);
         FastScroller.this.requestRedraw();
      }
   }
}
