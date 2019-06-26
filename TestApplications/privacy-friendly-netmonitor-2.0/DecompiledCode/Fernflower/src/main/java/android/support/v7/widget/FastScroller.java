package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@VisibleForTesting
class FastScroller extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {
   private static final int ANIMATION_STATE_FADING_IN = 1;
   private static final int ANIMATION_STATE_FADING_OUT = 3;
   private static final int ANIMATION_STATE_IN = 2;
   private static final int ANIMATION_STATE_OUT = 0;
   private static final int DRAG_NONE = 0;
   private static final int DRAG_X = 1;
   private static final int DRAG_Y = 2;
   private static final int[] EMPTY_STATE_SET = new int[0];
   private static final int HIDE_DELAY_AFTER_DRAGGING_MS = 1200;
   private static final int HIDE_DELAY_AFTER_VISIBLE_MS = 1500;
   private static final int HIDE_DURATION_MS = 500;
   private static final int[] PRESSED_STATE_SET = new int[]{16842919};
   private static final int SCROLLBAR_FULL_OPAQUE = 255;
   private static final int SHOW_DURATION_MS = 500;
   private static final int STATE_DRAGGING = 2;
   private static final int STATE_HIDDEN = 0;
   private static final int STATE_VISIBLE = 1;
   private int mAnimationState = 0;
   private int mDragState = 0;
   private final Runnable mHideRunnable = new Runnable() {
      public void run() {
         FastScroller.this.hide(500);
      }
   };
   @VisibleForTesting
   float mHorizontalDragX;
   private final int[] mHorizontalRange = new int[2];
   @VisibleForTesting
   int mHorizontalThumbCenterX;
   private final StateListDrawable mHorizontalThumbDrawable;
   private final int mHorizontalThumbHeight;
   @VisibleForTesting
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
   private final ValueAnimator mShowHideAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
   private int mState = 0;
   @VisibleForTesting
   float mVerticalDragY;
   private final int[] mVerticalRange = new int[2];
   @VisibleForTesting
   int mVerticalThumbCenterY;
   private final StateListDrawable mVerticalThumbDrawable;
   @VisibleForTesting
   int mVerticalThumbHeight;
   private final int mVerticalThumbWidth;
   private final Drawable mVerticalTrackDrawable;
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
      int var2 = this.mRecyclerViewHeight - this.mHorizontalThumbHeight;
      int var3 = this.mHorizontalThumbCenterX - this.mHorizontalThumbWidth / 2;
      this.mHorizontalThumbDrawable.setBounds(0, 0, this.mHorizontalThumbWidth, this.mHorizontalThumbHeight);
      this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
      var1.translate(0.0F, (float)var2);
      this.mHorizontalTrackDrawable.draw(var1);
      var1.translate((float)var3, 0.0F);
      this.mHorizontalThumbDrawable.draw(var1);
      var1.translate((float)(-var3), (float)(-var2));
   }

   private void drawVerticalScrollbar(Canvas var1) {
      int var2 = this.mRecyclerViewWidth - this.mVerticalThumbWidth;
      int var3 = this.mVerticalThumbCenterY - this.mVerticalThumbHeight / 2;
      this.mVerticalThumbDrawable.setBounds(0, 0, this.mVerticalThumbWidth, this.mVerticalThumbHeight);
      this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
      if (this.isLayoutRTL()) {
         this.mVerticalTrackDrawable.draw(var1);
         var1.translate((float)this.mVerticalThumbWidth, (float)var3);
         var1.scale(-1.0F, 1.0F);
         this.mVerticalThumbDrawable.draw(var1);
         var1.scale(1.0F, 1.0F);
         var1.translate((float)(-this.mVerticalThumbWidth), (float)(-var3));
      } else {
         var1.translate((float)var2, 0.0F);
         this.mVerticalTrackDrawable.draw(var1);
         var1.translate(0.0F, (float)var3);
         this.mVerticalThumbDrawable.draw(var1);
         var1.translate((float)(-var2), (float)(-var3));
      }

   }

   private int[] getHorizontalRange() {
      this.mHorizontalRange[0] = this.mMargin;
      this.mHorizontalRange[1] = this.mRecyclerViewWidth - this.mMargin;
      return this.mHorizontalRange;
   }

   private int[] getVerticalRange() {
      this.mVerticalRange[0] = this.mMargin;
      this.mVerticalRange[1] = this.mRecyclerViewHeight - this.mMargin;
      return this.mVerticalRange;
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

   private void requestRedraw() {
      this.mRecyclerView.invalidate();
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
         var6 = var4 - var6;
         var4 = (int)(var1 * (float)var6);
         var5 += var4;
         return var5 < var6 && var5 >= 0 ? var4 : 0;
      }
   }

   private void setState(int var1) {
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

   public void attachToRecyclerView(@Nullable RecyclerView var1) {
      if (this.mRecyclerView != var1) {
         if (this.mRecyclerView != null) {
            this.destroyCallbacks();
         }

         this.mRecyclerView = var1;
         if (this.mRecyclerView != null) {
            this.setupCallbacks();
         }

      }
   }

   @VisibleForTesting
   Drawable getHorizontalThumbDrawable() {
      return this.mHorizontalThumbDrawable;
   }

   @VisibleForTesting
   Drawable getHorizontalTrackDrawable() {
      return this.mHorizontalTrackDrawable;
   }

   @VisibleForTesting
   Drawable getVerticalThumbDrawable() {
      return this.mVerticalThumbDrawable;
   }

   @VisibleForTesting
   Drawable getVerticalTrackDrawable() {
      return this.mVerticalTrackDrawable;
   }

   public void hide() {
      this.hide(0);
   }

   @VisibleForTesting
   void hide(int var1) {
      switch(this.mAnimationState) {
      case 1:
         this.mShowHideAnimator.cancel();
      case 2:
         this.mAnimationState = 3;
         this.mShowHideAnimator.setFloatValues(new float[]{(Float)this.mShowHideAnimator.getAnimatedValue(), 0.0F});
         this.mShowHideAnimator.setDuration((long)var1);
         this.mShowHideAnimator.start();
      default:
      }
   }

   public boolean isDragging() {
      boolean var1;
      if (this.mState == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @VisibleForTesting
   boolean isHidden() {
      boolean var1;
      if (this.mState == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   @VisibleForTesting
   boolean isPointInsideHorizontalThumb(float var1, float var2) {
      boolean var3;
      if (var2 >= (float)(this.mRecyclerViewHeight - this.mHorizontalThumbHeight) && var1 >= (float)(this.mHorizontalThumbCenterX - this.mHorizontalThumbWidth / 2) && var1 <= (float)(this.mHorizontalThumbCenterX + this.mHorizontalThumbWidth / 2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   @VisibleForTesting
   boolean isPointInsideVerticalThumb(float var1, float var2) {
      boolean var3;
      label20: {
         if (this.isLayoutRTL()) {
            if (var1 > (float)(this.mVerticalThumbWidth / 2)) {
               break label20;
            }
         } else if (var1 < (float)(this.mRecyclerViewWidth - this.mVerticalThumbWidth)) {
            break label20;
         }

         if (var2 >= (float)(this.mVerticalThumbCenterY - this.mVerticalThumbHeight / 2) && var2 <= (float)(this.mVerticalThumbCenterY + this.mVerticalThumbHeight / 2)) {
            var3 = true;
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   @VisibleForTesting
   boolean isVisible() {
      int var1 = this.mState;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
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
         if (this.mState != 2) {
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

   public void show() {
      int var1 = this.mAnimationState;
      if (var1 != 0) {
         if (var1 != 3) {
            return;
         }

         this.mShowHideAnimator.cancel();
      }

      this.mAnimationState = 1;
      this.mShowHideAnimator.setFloatValues(new float[]{(Float)this.mShowHideAnimator.getAnimatedValue(), 1.0F});
      this.mShowHideAnimator.setDuration(500L);
      this.mShowHideAnimator.setStartDelay(0L);
      this.mShowHideAnimator.start();
   }

   void updateScrollPosition(int var1, int var2) {
      int var3 = this.mRecyclerView.computeVerticalScrollRange();
      int var4 = this.mRecyclerViewHeight;
      boolean var5;
      if (var3 - var4 > 0 && this.mRecyclerViewHeight >= this.mScrollbarMinimumRange) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.mNeedVerticalScrollbar = var5;
      int var6 = this.mRecyclerView.computeHorizontalScrollRange();
      int var7 = this.mRecyclerViewWidth;
      if (var6 - var7 > 0 && this.mRecyclerViewWidth >= this.mScrollbarMinimumRange) {
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

         if (this.mState == 0 || this.mState == 1) {
            this.setState(1);
         }

      }
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface AnimationState {
   }

   private class AnimatorListener extends AnimatorListenerAdapter {
      private boolean mCanceled;

      private AnimatorListener() {
         this.mCanceled = false;
      }

      // $FF: synthetic method
      AnimatorListener(Object var2) {
         this();
      }

      public void onAnimationCancel(Animator var1) {
         this.mCanceled = true;
      }

      public void onAnimationEnd(Animator var1) {
         if (this.mCanceled) {
            this.mCanceled = false;
         } else {
            if ((Float)FastScroller.this.mShowHideAnimator.getAnimatedValue() == 0.0F) {
               FastScroller.this.mAnimationState = 0;
               FastScroller.this.setState(0);
            } else {
               FastScroller.this.mAnimationState = 2;
               FastScroller.this.requestRedraw();
            }

         }
      }
   }

   private class AnimatorUpdater implements AnimatorUpdateListener {
      private AnimatorUpdater() {
      }

      // $FF: synthetic method
      AnimatorUpdater(Object var2) {
         this();
      }

      public void onAnimationUpdate(ValueAnimator var1) {
         int var2 = (int)(255.0F * (Float)var1.getAnimatedValue());
         FastScroller.this.mVerticalThumbDrawable.setAlpha(var2);
         FastScroller.this.mVerticalTrackDrawable.setAlpha(var2);
         FastScroller.this.requestRedraw();
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface DragState {
   }

   @Retention(RetentionPolicy.SOURCE)
   private @interface State {
   }
}
