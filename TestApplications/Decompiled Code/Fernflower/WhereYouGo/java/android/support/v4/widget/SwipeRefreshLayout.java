package android.support.v4.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;

public class SwipeRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {
   private static final int ALPHA_ANIMATION_DURATION = 300;
   private static final int ANIMATE_TO_START_DURATION = 200;
   private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
   private static final int CIRCLE_BG_LIGHT = -328966;
   @VisibleForTesting
   static final int CIRCLE_DIAMETER = 40;
   @VisibleForTesting
   static final int CIRCLE_DIAMETER_LARGE = 56;
   private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0F;
   public static final int DEFAULT = 1;
   private static final int DEFAULT_CIRCLE_TARGET = 64;
   private static final float DRAG_RATE = 0.5F;
   private static final int INVALID_POINTER = -1;
   public static final int LARGE = 0;
   private static final int[] LAYOUT_ATTRS = new int[]{16842766};
   private static final String LOG_TAG = SwipeRefreshLayout.class.getSimpleName();
   private static final int MAX_ALPHA = 255;
   private static final float MAX_PROGRESS_ANGLE = 0.8F;
   private static final int SCALE_DOWN_DURATION = 150;
   private static final int STARTING_PROGRESS_ALPHA = 76;
   private int mActivePointerId;
   private Animation mAlphaMaxAnimation;
   private Animation mAlphaStartAnimation;
   private final Animation mAnimateToCorrectPosition;
   private final Animation mAnimateToStartPosition;
   private SwipeRefreshLayout.OnChildScrollUpCallback mChildScrollUpCallback;
   private int mCircleDiameter;
   CircleImageView mCircleView;
   private int mCircleViewIndex;
   int mCurrentTargetOffsetTop;
   private final DecelerateInterpolator mDecelerateInterpolator;
   protected int mFrom;
   private float mInitialDownY;
   private float mInitialMotionY;
   private boolean mIsBeingDragged;
   SwipeRefreshLayout.OnRefreshListener mListener;
   private int mMediumAnimationDuration;
   private boolean mNestedScrollInProgress;
   private final NestedScrollingChildHelper mNestedScrollingChildHelper;
   private final NestedScrollingParentHelper mNestedScrollingParentHelper;
   boolean mNotify;
   protected int mOriginalOffsetTop;
   private final int[] mParentOffsetInWindow;
   private final int[] mParentScrollConsumed;
   MaterialProgressDrawable mProgress;
   private AnimationListener mRefreshListener;
   boolean mRefreshing;
   private boolean mReturningToStart;
   boolean mScale;
   private Animation mScaleAnimation;
   private Animation mScaleDownAnimation;
   private Animation mScaleDownToStartAnimation;
   int mSpinnerOffsetEnd;
   float mStartingScale;
   private View mTarget;
   private float mTotalDragDistance;
   private float mTotalUnconsumed;
   private int mTouchSlop;
   boolean mUsingCustomStart;

   public SwipeRefreshLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SwipeRefreshLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.mRefreshing = false;
      this.mTotalDragDistance = -1.0F;
      this.mParentScrollConsumed = new int[2];
      this.mParentOffsetInWindow = new int[2];
      this.mActivePointerId = -1;
      this.mCircleViewIndex = -1;
      this.mRefreshListener = new AnimationListener() {
         @SuppressLint({"NewApi"})
         public void onAnimationEnd(Animation var1) {
            if (SwipeRefreshLayout.this.mRefreshing) {
               SwipeRefreshLayout.this.mProgress.setAlpha(255);
               SwipeRefreshLayout.this.mProgress.start();
               if (SwipeRefreshLayout.this.mNotify && SwipeRefreshLayout.this.mListener != null) {
                  SwipeRefreshLayout.this.mListener.onRefresh();
               }

               SwipeRefreshLayout.this.mCurrentTargetOffsetTop = SwipeRefreshLayout.this.mCircleView.getTop();
            } else {
               SwipeRefreshLayout.this.reset();
            }

         }

         public void onAnimationRepeat(Animation var1) {
         }

         public void onAnimationStart(Animation var1) {
         }
      };
      this.mAnimateToCorrectPosition = new Animation() {
         public void applyTransformation(float var1, Transformation var2) {
            int var3;
            if (!SwipeRefreshLayout.this.mUsingCustomStart) {
               var3 = SwipeRefreshLayout.this.mSpinnerOffsetEnd - Math.abs(SwipeRefreshLayout.this.mOriginalOffsetTop);
            } else {
               var3 = SwipeRefreshLayout.this.mSpinnerOffsetEnd;
            }

            int var4 = SwipeRefreshLayout.this.mFrom;
            int var5 = (int)((float)(var3 - SwipeRefreshLayout.this.mFrom) * var1);
            var3 = SwipeRefreshLayout.this.mCircleView.getTop();
            SwipeRefreshLayout.this.setTargetOffsetTopAndBottom(var4 + var5 - var3, false);
            SwipeRefreshLayout.this.mProgress.setArrowScale(1.0F - var1);
         }
      };
      this.mAnimateToStartPosition = new Animation() {
         public void applyTransformation(float var1, Transformation var2) {
            SwipeRefreshLayout.this.moveToStart(var1);
         }
      };
      this.mTouchSlop = ViewConfiguration.get(var1).getScaledTouchSlop();
      this.mMediumAnimationDuration = this.getResources().getInteger(17694721);
      this.setWillNotDraw(false);
      this.mDecelerateInterpolator = new DecelerateInterpolator(2.0F);
      DisplayMetrics var3 = this.getResources().getDisplayMetrics();
      this.mCircleDiameter = (int)(40.0F * var3.density);
      this.createProgressView();
      ViewCompat.setChildrenDrawingOrderEnabled(this, true);
      this.mSpinnerOffsetEnd = (int)(64.0F * var3.density);
      this.mTotalDragDistance = (float)this.mSpinnerOffsetEnd;
      this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
      this.mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
      this.setNestedScrollingEnabled(true);
      int var4 = -this.mCircleDiameter;
      this.mCurrentTargetOffsetTop = var4;
      this.mOriginalOffsetTop = var4;
      this.moveToStart(1.0F);
      TypedArray var5 = var1.obtainStyledAttributes(var2, LAYOUT_ATTRS);
      this.setEnabled(var5.getBoolean(0, true));
      var5.recycle();
   }

   private void animateOffsetToCorrectPosition(int var1, AnimationListener var2) {
      this.mFrom = var1;
      this.mAnimateToCorrectPosition.reset();
      this.mAnimateToCorrectPosition.setDuration(200L);
      this.mAnimateToCorrectPosition.setInterpolator(this.mDecelerateInterpolator);
      if (var2 != null) {
         this.mCircleView.setAnimationListener(var2);
      }

      this.mCircleView.clearAnimation();
      this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
   }

   private void animateOffsetToStartPosition(int var1, AnimationListener var2) {
      if (this.mScale) {
         this.startScaleDownReturnToStartAnimation(var1, var2);
      } else {
         this.mFrom = var1;
         this.mAnimateToStartPosition.reset();
         this.mAnimateToStartPosition.setDuration(200L);
         this.mAnimateToStartPosition.setInterpolator(this.mDecelerateInterpolator);
         if (var2 != null) {
            this.mCircleView.setAnimationListener(var2);
         }

         this.mCircleView.clearAnimation();
         this.mCircleView.startAnimation(this.mAnimateToStartPosition);
      }

   }

   private void createProgressView() {
      this.mCircleView = new CircleImageView(this.getContext(), -328966);
      this.mProgress = new MaterialProgressDrawable(this.getContext(), this);
      this.mProgress.setBackgroundColor(-328966);
      this.mCircleView.setImageDrawable(this.mProgress);
      this.mCircleView.setVisibility(8);
      this.addView(this.mCircleView);
   }

   private void ensureTarget() {
      if (this.mTarget == null) {
         for(int var1 = 0; var1 < this.getChildCount(); ++var1) {
            View var2 = this.getChildAt(var1);
            if (!var2.equals(this.mCircleView)) {
               this.mTarget = var2;
               break;
            }
         }
      }

   }

   private void finishSpinner(float var1) {
      if (var1 > this.mTotalDragDistance) {
         this.setRefreshing(true, true);
      } else {
         this.mRefreshing = false;
         this.mProgress.setStartEndTrim(0.0F, 0.0F);
         AnimationListener var2 = null;
         if (!this.mScale) {
            var2 = new AnimationListener() {
               public void onAnimationEnd(Animation var1) {
                  if (!SwipeRefreshLayout.this.mScale) {
                     SwipeRefreshLayout.this.startScaleDownAnimation((AnimationListener)null);
                  }

               }

               public void onAnimationRepeat(Animation var1) {
               }

               public void onAnimationStart(Animation var1) {
               }
            };
         }

         this.animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, var2);
         this.mProgress.showArrow(false);
      }

   }

   private boolean isAlphaUsedForScale() {
      boolean var1;
      if (VERSION.SDK_INT < 11) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean isAnimationRunning(Animation var1) {
      boolean var2;
      if (var1 != null && var1.hasStarted() && !var1.hasEnded()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   @SuppressLint({"NewApi"})
   private void moveSpinner(float var1) {
      this.mProgress.showArrow(true);
      float var2 = Math.min(1.0F, Math.abs(var1 / this.mTotalDragDistance));
      float var3 = (float)Math.max((double)var2 - 0.4D, 0.0D) * 5.0F / 3.0F;
      float var4 = Math.abs(var1);
      float var5 = this.mTotalDragDistance;
      float var6;
      if (this.mUsingCustomStart) {
         var6 = (float)(this.mSpinnerOffsetEnd - this.mOriginalOffsetTop);
      } else {
         var6 = (float)this.mSpinnerOffsetEnd;
      }

      var4 = Math.max(0.0F, Math.min(var4 - var5, 2.0F * var6) / var6);
      var4 = (float)((double)(var4 / 4.0F) - Math.pow((double)(var4 / 4.0F), 2.0D)) * 2.0F;
      int var7 = this.mOriginalOffsetTop;
      int var8 = (int)(var6 * var2 + var6 * var4 * 2.0F);
      if (this.mCircleView.getVisibility() != 0) {
         this.mCircleView.setVisibility(0);
      }

      if (!this.mScale) {
         ViewCompat.setScaleX(this.mCircleView, 1.0F);
         ViewCompat.setScaleY(this.mCircleView, 1.0F);
      }

      if (this.mScale) {
         this.setAnimationProgress(Math.min(1.0F, var1 / this.mTotalDragDistance));
      }

      if (var1 < this.mTotalDragDistance) {
         if (this.mProgress.getAlpha() > 76 && !this.isAnimationRunning(this.mAlphaStartAnimation)) {
            this.startProgressAlphaStartAnimation();
         }
      } else if (this.mProgress.getAlpha() < 255 && !this.isAnimationRunning(this.mAlphaMaxAnimation)) {
         this.startProgressAlphaMaxAnimation();
      }

      this.mProgress.setStartEndTrim(0.0F, Math.min(0.8F, var3 * 0.8F));
      this.mProgress.setArrowScale(Math.min(1.0F, var3));
      this.mProgress.setProgressRotation((-0.25F + 0.4F * var3 + 2.0F * var4) * 0.5F);
      this.setTargetOffsetTopAndBottom(var7 + var8 - this.mCurrentTargetOffsetTop, true);
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

         this.mActivePointerId = var1.getPointerId(var3);
      }

   }

   @SuppressLint({"NewApi"})
   private void setColorViewAlpha(int var1) {
      this.mCircleView.getBackground().setAlpha(var1);
      this.mProgress.setAlpha(var1);
   }

   private void setRefreshing(boolean var1, boolean var2) {
      if (this.mRefreshing != var1) {
         this.mNotify = var2;
         this.ensureTarget();
         this.mRefreshing = var1;
         if (this.mRefreshing) {
            this.animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
         } else {
            this.startScaleDownAnimation(this.mRefreshListener);
         }
      }

   }

   @SuppressLint({"NewApi"})
   private Animation startAlphaAnimation(final int var1, final int var2) {
      Animation var3;
      if (this.mScale && this.isAlphaUsedForScale()) {
         var3 = null;
      } else {
         var3 = new Animation() {
            public void applyTransformation(float var1x, Transformation var2x) {
               SwipeRefreshLayout.this.mProgress.setAlpha((int)((float)var1 + (float)(var2 - var1) * var1x));
            }
         };
         var3.setDuration(300L);
         this.mCircleView.setAnimationListener((AnimationListener)null);
         this.mCircleView.clearAnimation();
         this.mCircleView.startAnimation(var3);
      }

      return var3;
   }

   @SuppressLint({"NewApi"})
   private void startDragging(float var1) {
      if (var1 - this.mInitialDownY > (float)this.mTouchSlop && !this.mIsBeingDragged) {
         this.mInitialMotionY = this.mInitialDownY + (float)this.mTouchSlop;
         this.mIsBeingDragged = true;
         this.mProgress.setAlpha(76);
      }

   }

   @SuppressLint({"NewApi"})
   private void startProgressAlphaMaxAnimation() {
      this.mAlphaMaxAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 255);
   }

   @SuppressLint({"NewApi"})
   private void startProgressAlphaStartAnimation() {
      this.mAlphaStartAnimation = this.startAlphaAnimation(this.mProgress.getAlpha(), 76);
   }

   @SuppressLint({"NewApi"})
   private void startScaleDownReturnToStartAnimation(int var1, AnimationListener var2) {
      this.mFrom = var1;
      if (this.isAlphaUsedForScale()) {
         this.mStartingScale = (float)this.mProgress.getAlpha();
      } else {
         this.mStartingScale = ViewCompat.getScaleX(this.mCircleView);
      }

      this.mScaleDownToStartAnimation = new Animation() {
         public void applyTransformation(float var1, Transformation var2) {
            float var3 = SwipeRefreshLayout.this.mStartingScale;
            float var4 = -SwipeRefreshLayout.this.mStartingScale;
            SwipeRefreshLayout.this.setAnimationProgress(var3 + var4 * var1);
            SwipeRefreshLayout.this.moveToStart(var1);
         }
      };
      this.mScaleDownToStartAnimation.setDuration(150L);
      if (var2 != null) {
         this.mCircleView.setAnimationListener(var2);
      }

      this.mCircleView.clearAnimation();
      this.mCircleView.startAnimation(this.mScaleDownToStartAnimation);
   }

   @SuppressLint({"NewApi"})
   private void startScaleUpAnimation(AnimationListener var1) {
      this.mCircleView.setVisibility(0);
      if (VERSION.SDK_INT >= 11) {
         this.mProgress.setAlpha(255);
      }

      this.mScaleAnimation = new Animation() {
         public void applyTransformation(float var1, Transformation var2) {
            SwipeRefreshLayout.this.setAnimationProgress(var1);
         }
      };
      this.mScaleAnimation.setDuration((long)this.mMediumAnimationDuration);
      if (var1 != null) {
         this.mCircleView.setAnimationListener(var1);
      }

      this.mCircleView.clearAnimation();
      this.mCircleView.startAnimation(this.mScaleAnimation);
   }

   public boolean canChildScrollUp() {
      boolean var1 = true;
      boolean var2 = false;
      if (this.mChildScrollUpCallback != null) {
         var2 = this.mChildScrollUpCallback.canChildScrollUp(this, this.mTarget);
      } else if (VERSION.SDK_INT < 14) {
         if (this.mTarget instanceof AbsListView) {
            AbsListView var3 = (AbsListView)this.mTarget;
            if (var3.getChildCount() > 0) {
               var2 = var1;
               if (var3.getFirstVisiblePosition() > 0) {
                  return var2;
               }

               var2 = var1;
               if (var3.getChildAt(0).getTop() < var3.getPaddingTop()) {
                  return var2;
               }
            }

            var2 = false;
         } else if (ViewCompat.canScrollVertically(this.mTarget, -1) || this.mTarget.getScrollY() > 0) {
            var2 = true;
         }
      } else {
         var2 = ViewCompat.canScrollVertically(this.mTarget, -1);
      }

      return var2;
   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      return this.mNestedScrollingChildHelper.dispatchNestedFling(var1, var2, var3);
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      return this.mNestedScrollingChildHelper.dispatchNestedPreFling(var1, var2);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(var1, var2, var3, var4);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      return this.mNestedScrollingChildHelper.dispatchNestedScroll(var1, var2, var3, var4, var5);
   }

   protected int getChildDrawingOrder(int var1, int var2) {
      if (this.mCircleViewIndex < 0) {
         var1 = var2;
      } else if (var2 == var1 - 1) {
         var1 = this.mCircleViewIndex;
      } else {
         var1 = var2;
         if (var2 >= this.mCircleViewIndex) {
            var1 = var2 + 1;
         }
      }

      return var1;
   }

   public int getNestedScrollAxes() {
      return this.mNestedScrollingParentHelper.getNestedScrollAxes();
   }

   public int getProgressCircleDiameter() {
      return this.mCircleDiameter;
   }

   public int getProgressViewEndOffset() {
      return this.mSpinnerOffsetEnd;
   }

   public int getProgressViewStartOffset() {
      return this.mOriginalOffsetTop;
   }

   public boolean hasNestedScrollingParent() {
      return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
   }

   public boolean isNestedScrollingEnabled() {
      return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
   }

   public boolean isRefreshing() {
      return this.mRefreshing;
   }

   void moveToStart(float var1) {
      this.setTargetOffsetTopAndBottom(this.mFrom + (int)((float)(this.mOriginalOffsetTop - this.mFrom) * var1) - this.mCircleView.getTop(), false);
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.reset();
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2 = false;
      this.ensureTarget();
      int var3 = MotionEventCompat.getActionMasked(var1);
      if (this.mReturningToStart && var3 == 0) {
         this.mReturningToStart = false;
      }

      boolean var4 = var2;
      if (this.isEnabled()) {
         var4 = var2;
         if (!this.mReturningToStart) {
            var4 = var2;
            if (!this.canChildScrollUp()) {
               var4 = var2;
               if (!this.mRefreshing) {
                  if (this.mNestedScrollInProgress) {
                     var4 = var2;
                  } else {
                     switch(var3) {
                     case 0:
                        this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCircleView.getTop(), true);
                        this.mActivePointerId = var1.getPointerId(0);
                        this.mIsBeingDragged = false;
                        var3 = var1.findPointerIndex(this.mActivePointerId);
                        var4 = var2;
                        if (var3 < 0) {
                           return var4;
                        }

                        this.mInitialDownY = var1.getY(var3);
                        break;
                     case 1:
                     case 3:
                        this.mIsBeingDragged = false;
                        this.mActivePointerId = -1;
                        break;
                     case 2:
                        if (this.mActivePointerId == -1) {
                           Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                           var4 = var2;
                           return var4;
                        }

                        var3 = var1.findPointerIndex(this.mActivePointerId);
                        var4 = var2;
                        if (var3 < 0) {
                           return var4;
                        }

                        this.startDragging(var1.getY(var3));
                     case 4:
                     case 5:
                     default:
                        break;
                     case 6:
                        this.onSecondaryPointerUp(var1);
                     }

                     var4 = this.mIsBeingDragged;
                  }
               }
            }
         }
      }

      return var4;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var2 = this.getMeasuredWidth();
      var4 = this.getMeasuredHeight();
      if (this.getChildCount() != 0) {
         if (this.mTarget == null) {
            this.ensureTarget();
         }

         if (this.mTarget != null) {
            View var6 = this.mTarget;
            var3 = this.getPaddingLeft();
            var5 = this.getPaddingTop();
            var6.layout(var3, var5, var3 + (var2 - this.getPaddingLeft() - this.getPaddingRight()), var5 + (var4 - this.getPaddingTop() - this.getPaddingBottom()));
            var3 = this.mCircleView.getMeasuredWidth();
            var4 = this.mCircleView.getMeasuredHeight();
            this.mCircleView.layout(var2 / 2 - var3 / 2, this.mCurrentTargetOffsetTop, var2 / 2 + var3 / 2, this.mCurrentTargetOffsetTop + var4);
         }
      }

   }

   public void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      if (this.mTarget == null) {
         this.ensureTarget();
      }

      if (this.mTarget != null) {
         this.mTarget.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), 1073741824));
         this.mCircleView.measure(MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824), MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824));
         this.mCircleViewIndex = -1;

         for(var1 = 0; var1 < this.getChildCount(); ++var1) {
            if (this.getChildAt(var1) == this.mCircleView) {
               this.mCircleViewIndex = var1;
               break;
            }
         }
      }

   }

   public boolean onNestedFling(View var1, float var2, float var3, boolean var4) {
      return this.dispatchNestedFling(var2, var3, var4);
   }

   public boolean onNestedPreFling(View var1, float var2, float var3) {
      return this.dispatchNestedPreFling(var2, var3);
   }

   public void onNestedPreScroll(View var1, int var2, int var3, int[] var4) {
      if (var3 > 0 && this.mTotalUnconsumed > 0.0F) {
         if ((float)var3 > this.mTotalUnconsumed) {
            var4[1] = var3 - (int)this.mTotalUnconsumed;
            this.mTotalUnconsumed = 0.0F;
         } else {
            this.mTotalUnconsumed -= (float)var3;
            var4[1] = var3;
         }

         this.moveSpinner(this.mTotalUnconsumed);
      }

      if (this.mUsingCustomStart && var3 > 0 && this.mTotalUnconsumed == 0.0F && Math.abs(var3 - var4[1]) > 0) {
         this.mCircleView.setVisibility(8);
      }

      int[] var5 = this.mParentScrollConsumed;
      if (this.dispatchNestedPreScroll(var2 - var4[0], var3 - var4[1], var5, (int[])null)) {
         var4[0] += var5[0];
         var4[1] += var5[1];
      }

   }

   public void onNestedScroll(View var1, int var2, int var3, int var4, int var5) {
      this.dispatchNestedScroll(var2, var3, var4, var5, this.mParentOffsetInWindow);
      var2 = var5 + this.mParentOffsetInWindow[1];
      if (var2 < 0 && !this.canChildScrollUp()) {
         this.mTotalUnconsumed += (float)Math.abs(var2);
         this.moveSpinner(this.mTotalUnconsumed);
      }

   }

   public void onNestedScrollAccepted(View var1, View var2, int var3) {
      this.mNestedScrollingParentHelper.onNestedScrollAccepted(var1, var2, var3);
      this.startNestedScroll(var3 & 2);
      this.mTotalUnconsumed = 0.0F;
      this.mNestedScrollInProgress = true;
   }

   public boolean onStartNestedScroll(View var1, View var2, int var3) {
      boolean var4;
      if (this.isEnabled() && !this.mReturningToStart && !this.mRefreshing && (var3 & 2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public void onStopNestedScroll(View var1) {
      this.mNestedScrollingParentHelper.onStopNestedScroll(var1);
      this.mNestedScrollInProgress = false;
      if (this.mTotalUnconsumed > 0.0F) {
         this.finishSpinner(this.mTotalUnconsumed);
         this.mTotalUnconsumed = 0.0F;
      }

      this.stopNestedScroll();
   }

   public boolean onTouchEvent(MotionEvent var1) {
      boolean var2 = false;
      int var3 = MotionEventCompat.getActionMasked(var1);
      if (this.mReturningToStart && var3 == 0) {
         this.mReturningToStart = false;
      }

      boolean var4 = var2;
      if (this.isEnabled()) {
         var4 = var2;
         if (!this.mReturningToStart) {
            var4 = var2;
            if (!this.canChildScrollUp()) {
               var4 = var2;
               if (!this.mRefreshing) {
                  if (this.mNestedScrollInProgress) {
                     var4 = var2;
                  } else {
                     var4 = var2;
                     float var5;
                     switch(var3) {
                     case 0:
                        this.mActivePointerId = var1.getPointerId(0);
                        this.mIsBeingDragged = false;
                        break;
                     case 1:
                        var3 = var1.findPointerIndex(this.mActivePointerId);
                        if (var3 < 0) {
                           Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                           var4 = var2;
                        } else {
                           if (this.mIsBeingDragged) {
                              var5 = var1.getY(var3);
                              float var6 = this.mInitialMotionY;
                              this.mIsBeingDragged = false;
                              this.finishSpinner((var5 - var6) * 0.5F);
                           }

                           this.mActivePointerId = -1;
                           var4 = var2;
                        }

                        return var4;
                     case 2:
                        var3 = var1.findPointerIndex(this.mActivePointerId);
                        if (var3 < 0) {
                           Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                           var4 = var2;
                           return var4;
                        } else {
                           var5 = var1.getY(var3);
                           this.startDragging(var5);
                           if (this.mIsBeingDragged) {
                              var5 = (var5 - this.mInitialMotionY) * 0.5F;
                              var4 = var2;
                              if (var5 <= 0.0F) {
                                 return var4;
                              }

                              this.moveSpinner(var5);
                           }
                           break;
                        }
                     case 3:
                        return var4;
                     case 4:
                     default:
                        break;
                     case 5:
                        var3 = MotionEventCompat.getActionIndex(var1);
                        if (var3 < 0) {
                           Log.e(LOG_TAG, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                           var4 = var2;
                           return var4;
                        }

                        this.mActivePointerId = var1.getPointerId(var3);
                        break;
                     case 6:
                        this.onSecondaryPointerUp(var1);
                     }

                     var4 = true;
                  }
               }
            }
         }
      }

      return var4;
   }

   public void requestDisallowInterceptTouchEvent(boolean var1) {
      if ((VERSION.SDK_INT >= 21 || !(this.mTarget instanceof AbsListView)) && (this.mTarget == null || ViewCompat.isNestedScrollingEnabled(this.mTarget))) {
         super.requestDisallowInterceptTouchEvent(var1);
      }

   }

   void reset() {
      this.mCircleView.clearAnimation();
      this.mProgress.stop();
      this.mCircleView.setVisibility(8);
      this.setColorViewAlpha(255);
      if (this.mScale) {
         this.setAnimationProgress(0.0F);
      } else {
         this.setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCurrentTargetOffsetTop, true);
      }

      this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
   }

   void setAnimationProgress(float var1) {
      if (this.isAlphaUsedForScale()) {
         this.setColorViewAlpha((int)(255.0F * var1));
      } else {
         ViewCompat.setScaleX(this.mCircleView, var1);
         ViewCompat.setScaleY(this.mCircleView, var1);
      }

   }

   @Deprecated
   public void setColorScheme(@ColorInt int... var1) {
      this.setColorSchemeResources(var1);
   }

   public void setColorSchemeColors(@ColorInt int... var1) {
      this.ensureTarget();
      this.mProgress.setColorSchemeColors(var1);
   }

   public void setColorSchemeResources(@ColorRes int... var1) {
      Context var2 = this.getContext();
      int[] var3 = new int[var1.length];

      for(int var4 = 0; var4 < var1.length; ++var4) {
         var3[var4] = ContextCompat.getColor(var2, var1[var4]);
      }

      this.setColorSchemeColors(var3);
   }

   public void setDistanceToTriggerSync(int var1) {
      this.mTotalDragDistance = (float)var1;
   }

   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      if (!var1) {
         this.reset();
      }

   }

   public void setNestedScrollingEnabled(boolean var1) {
      this.mNestedScrollingChildHelper.setNestedScrollingEnabled(var1);
   }

   public void setOnChildScrollUpCallback(@Nullable SwipeRefreshLayout.OnChildScrollUpCallback var1) {
      this.mChildScrollUpCallback = var1;
   }

   public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener var1) {
      this.mListener = var1;
   }

   @Deprecated
   public void setProgressBackgroundColor(int var1) {
      this.setProgressBackgroundColorSchemeResource(var1);
   }

   public void setProgressBackgroundColorSchemeColor(@ColorInt int var1) {
      this.mCircleView.setBackgroundColor(var1);
      this.mProgress.setBackgroundColor(var1);
   }

   public void setProgressBackgroundColorSchemeResource(@ColorRes int var1) {
      this.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this.getContext(), var1));
   }

   public void setProgressViewEndTarget(boolean var1, int var2) {
      this.mSpinnerOffsetEnd = var2;
      this.mScale = var1;
      this.mCircleView.invalidate();
   }

   public void setProgressViewOffset(boolean var1, int var2, int var3) {
      this.mScale = var1;
      this.mOriginalOffsetTop = var2;
      this.mSpinnerOffsetEnd = var3;
      this.mUsingCustomStart = true;
      this.reset();
      this.mRefreshing = false;
   }

   public void setRefreshing(boolean var1) {
      if (var1 && this.mRefreshing != var1) {
         this.mRefreshing = var1;
         int var2;
         if (!this.mUsingCustomStart) {
            var2 = this.mSpinnerOffsetEnd + this.mOriginalOffsetTop;
         } else {
            var2 = this.mSpinnerOffsetEnd;
         }

         this.setTargetOffsetTopAndBottom(var2 - this.mCurrentTargetOffsetTop, true);
         this.mNotify = false;
         this.startScaleUpAnimation(this.mRefreshListener);
      } else {
         this.setRefreshing(var1, false);
      }

   }

   public void setSize(int var1) {
      if (var1 == 0 || var1 == 1) {
         DisplayMetrics var2 = this.getResources().getDisplayMetrics();
         if (var1 == 0) {
            this.mCircleDiameter = (int)(56.0F * var2.density);
         } else {
            this.mCircleDiameter = (int)(40.0F * var2.density);
         }

         this.mCircleView.setImageDrawable((Drawable)null);
         this.mProgress.updateSizes(var1);
         this.mCircleView.setImageDrawable(this.mProgress);
      }

   }

   void setTargetOffsetTopAndBottom(int var1, boolean var2) {
      this.mCircleView.bringToFront();
      ViewCompat.offsetTopAndBottom(this.mCircleView, var1);
      this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
      if (var2 && VERSION.SDK_INT < 11) {
         this.invalidate();
      }

   }

   public boolean startNestedScroll(int var1) {
      return this.mNestedScrollingChildHelper.startNestedScroll(var1);
   }

   void startScaleDownAnimation(AnimationListener var1) {
      this.mScaleDownAnimation = new Animation() {
         public void applyTransformation(float var1, Transformation var2) {
            SwipeRefreshLayout.this.setAnimationProgress(1.0F - var1);
         }
      };
      this.mScaleDownAnimation.setDuration(150L);
      this.mCircleView.setAnimationListener(var1);
      this.mCircleView.clearAnimation();
      this.mCircleView.startAnimation(this.mScaleDownAnimation);
   }

   public void stopNestedScroll() {
      this.mNestedScrollingChildHelper.stopNestedScroll();
   }

   public interface OnChildScrollUpCallback {
      boolean canChildScrollUp(SwipeRefreshLayout var1, @Nullable View var2);
   }

   public interface OnRefreshListener {
      void onRefresh();
   }
}
