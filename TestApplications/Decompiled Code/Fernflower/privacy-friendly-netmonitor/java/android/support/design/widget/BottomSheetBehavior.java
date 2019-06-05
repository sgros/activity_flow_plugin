package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.v4.math.MathUtils;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewGroup.LayoutParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class BottomSheetBehavior extends CoordinatorLayout.Behavior {
   private static final float HIDE_FRICTION = 0.1F;
   private static final float HIDE_THRESHOLD = 0.5F;
   public static final int PEEK_HEIGHT_AUTO = -1;
   public static final int STATE_COLLAPSED = 4;
   public static final int STATE_DRAGGING = 1;
   public static final int STATE_EXPANDED = 3;
   public static final int STATE_HIDDEN = 5;
   public static final int STATE_SETTLING = 2;
   int mActivePointerId;
   private BottomSheetBehavior.BottomSheetCallback mCallback;
   private final ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {
      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         return var1.getLeft();
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         int var4 = BottomSheetBehavior.this.mMinOffset;
         if (BottomSheetBehavior.this.mHideable) {
            var3 = BottomSheetBehavior.this.mParentHeight;
         } else {
            var3 = BottomSheetBehavior.this.mMaxOffset;
         }

         return MathUtils.clamp(var2, var4, var3);
      }

      public int getViewVerticalDragRange(View var1) {
         return BottomSheetBehavior.this.mHideable ? BottomSheetBehavior.this.mParentHeight - BottomSheetBehavior.this.mMinOffset : BottomSheetBehavior.this.mMaxOffset - BottomSheetBehavior.this.mMinOffset;
      }

      public void onViewDragStateChanged(int var1) {
         if (var1 == 1) {
            BottomSheetBehavior.this.setStateInternal(1);
         }

      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
         BottomSheetBehavior.this.dispatchOnSlide(var3);
      }

      public void onViewReleased(View var1, float var2, float var3) {
         byte var4;
         int var5;
         label30: {
            var4 = 4;
            if (var3 < 0.0F) {
               var5 = BottomSheetBehavior.this.mMinOffset;
            } else {
               if (BottomSheetBehavior.this.mHideable && BottomSheetBehavior.this.shouldHide(var1, var3)) {
                  var5 = BottomSheetBehavior.this.mParentHeight;
                  var4 = 5;
                  break label30;
               }

               if (var3 != 0.0F) {
                  var5 = BottomSheetBehavior.this.mMaxOffset;
                  break label30;
               }

               var5 = var1.getTop();
               if (Math.abs(var5 - BottomSheetBehavior.this.mMinOffset) >= Math.abs(var5 - BottomSheetBehavior.this.mMaxOffset)) {
                  var5 = BottomSheetBehavior.this.mMaxOffset;
                  break label30;
               }

               var5 = BottomSheetBehavior.this.mMinOffset;
            }

            var4 = 3;
         }

         if (BottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(var1.getLeft(), var5)) {
            BottomSheetBehavior.this.setStateInternal(2);
            ViewCompat.postOnAnimation(var1, BottomSheetBehavior.this.new SettleRunnable(var1, var4));
         } else {
            BottomSheetBehavior.this.setStateInternal(var4);
         }

      }

      public boolean tryCaptureView(View var1, int var2) {
         int var3 = BottomSheetBehavior.this.mState;
         boolean var4 = true;
         if (var3 == 1) {
            return false;
         } else if (BottomSheetBehavior.this.mTouchingScrollingChild) {
            return false;
         } else {
            if (BottomSheetBehavior.this.mState == 3 && BottomSheetBehavior.this.mActivePointerId == var2) {
               View var5 = (View)BottomSheetBehavior.this.mNestedScrollingChildRef.get();
               if (var5 != null && var5.canScrollVertically(-1)) {
                  return false;
               }
            }

            if (BottomSheetBehavior.this.mViewRef == null || BottomSheetBehavior.this.mViewRef.get() != var1) {
               var4 = false;
            }

            return var4;
         }
      }
   };
   boolean mHideable;
   private boolean mIgnoreEvents;
   private int mInitialY;
   private int mLastNestedScrollDy;
   int mMaxOffset;
   private float mMaximumVelocity;
   int mMinOffset;
   private boolean mNestedScrolled;
   WeakReference mNestedScrollingChildRef;
   int mParentHeight;
   private int mPeekHeight;
   private boolean mPeekHeightAuto;
   private int mPeekHeightMin;
   private boolean mSkipCollapsed;
   int mState = 4;
   boolean mTouchingScrollingChild;
   private VelocityTracker mVelocityTracker;
   ViewDragHelper mViewDragHelper;
   WeakReference mViewRef;

   public BottomSheetBehavior() {
   }

   public BottomSheetBehavior(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.BottomSheetBehavior_Layout);
      TypedValue var4 = var3.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
      if (var4 != null && var4.data == -1) {
         this.setPeekHeight(var4.data);
      } else {
         this.setPeekHeight(var3.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
      }

      this.setHideable(var3.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
      this.setSkipCollapsed(var3.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
      var3.recycle();
      this.mMaximumVelocity = (float)ViewConfiguration.get(var1).getScaledMaximumFlingVelocity();
   }

   public static BottomSheetBehavior from(View var0) {
      LayoutParams var1 = var0.getLayoutParams();
      if (!(var1 instanceof CoordinatorLayout.LayoutParams)) {
         throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
      } else {
         CoordinatorLayout.Behavior var2 = ((CoordinatorLayout.LayoutParams)var1).getBehavior();
         if (!(var2 instanceof BottomSheetBehavior)) {
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
         } else {
            return (BottomSheetBehavior)var2;
         }
      }
   }

   private float getYVelocity() {
      this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
      return this.mVelocityTracker.getYVelocity(this.mActivePointerId);
   }

   private void reset() {
      this.mActivePointerId = -1;
      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

   }

   void dispatchOnSlide(int var1) {
      View var2 = (View)this.mViewRef.get();
      if (var2 != null && this.mCallback != null) {
         if (var1 > this.mMaxOffset) {
            this.mCallback.onSlide(var2, (float)(this.mMaxOffset - var1) / (float)(this.mParentHeight - this.mMaxOffset));
         } else {
            this.mCallback.onSlide(var2, (float)(this.mMaxOffset - var1) / (float)(this.mMaxOffset - this.mMinOffset));
         }
      }

   }

   @VisibleForTesting
   View findScrollingChild(View var1) {
      if (ViewCompat.isNestedScrollingEnabled(var1)) {
         return var1;
      } else {
         if (var1 instanceof ViewGroup) {
            ViewGroup var2 = (ViewGroup)var1;
            int var3 = 0;

            for(int var4 = var2.getChildCount(); var3 < var4; ++var3) {
               var1 = this.findScrollingChild(var2.getChildAt(var3));
               if (var1 != null) {
                  return var1;
               }
            }
         }

         return null;
      }
   }

   public final int getPeekHeight() {
      int var1;
      if (this.mPeekHeightAuto) {
         var1 = -1;
      } else {
         var1 = this.mPeekHeight;
      }

      return var1;
   }

   @VisibleForTesting
   int getPeekHeightMin() {
      return this.mPeekHeightMin;
   }

   public boolean getSkipCollapsed() {
      return this.mSkipCollapsed;
   }

   public final int getState() {
      return this.mState;
   }

   public boolean isHideable() {
      return this.mHideable;
   }

   public boolean onInterceptTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      boolean var4 = var2.isShown();
      boolean var5 = false;
      if (!var4) {
         this.mIgnoreEvents = true;
         return false;
      } else {
         int var6 = var3.getActionMasked();
         if (var6 == 0) {
            this.reset();
         }

         if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
         }

         label67: {
            this.mVelocityTracker.addMovement(var3);
            if (var6 != 3) {
               switch(var6) {
               case 0:
                  int var7 = (int)var3.getX();
                  this.mInitialY = (int)var3.getY();
                  View var8;
                  if (this.mNestedScrollingChildRef != null) {
                     var8 = (View)this.mNestedScrollingChildRef.get();
                  } else {
                     var8 = null;
                  }

                  if (var8 != null && var1.isPointInChildBounds(var8, var7, this.mInitialY)) {
                     this.mActivePointerId = var3.getPointerId(var3.getActionIndex());
                     this.mTouchingScrollingChild = true;
                  }

                  if (this.mActivePointerId == -1 && !var1.isPointInChildBounds(var2, var7, this.mInitialY)) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  this.mIgnoreEvents = var4;
                  break label67;
               case 1:
                  break;
               default:
                  break label67;
               }
            }

            this.mTouchingScrollingChild = false;
            this.mActivePointerId = -1;
            if (this.mIgnoreEvents) {
               this.mIgnoreEvents = false;
               return false;
            }
         }

         if (!this.mIgnoreEvents && this.mViewDragHelper.shouldInterceptTouchEvent(var3)) {
            return true;
         } else {
            var2 = (View)this.mNestedScrollingChildRef.get();
            var4 = var5;
            if (var6 == 2) {
               var4 = var5;
               if (var2 != null) {
                  var4 = var5;
                  if (!this.mIgnoreEvents) {
                     var4 = var5;
                     if (this.mState != 1) {
                        var4 = var5;
                        if (!var1.isPointInChildBounds(var2, (int)var3.getX(), (int)var3.getY())) {
                           var4 = var5;
                           if (Math.abs((float)this.mInitialY - var3.getY()) > (float)this.mViewDragHelper.getTouchSlop()) {
                              var4 = true;
                           }
                        }
                     }
                  }
               }
            }

            return var4;
         }
      }
   }

   public boolean onLayoutChild(CoordinatorLayout var1, View var2, int var3) {
      if (ViewCompat.getFitsSystemWindows(var1) && !ViewCompat.getFitsSystemWindows(var2)) {
         ViewCompat.setFitsSystemWindows(var2, true);
      }

      int var4 = var2.getTop();
      var1.onLayoutChild(var2, var3);
      this.mParentHeight = var1.getHeight();
      if (this.mPeekHeightAuto) {
         if (this.mPeekHeightMin == 0) {
            this.mPeekHeightMin = var1.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
         }

         var3 = Math.max(this.mPeekHeightMin, this.mParentHeight - var1.getWidth() * 9 / 16);
      } else {
         var3 = this.mPeekHeight;
      }

      this.mMinOffset = Math.max(0, this.mParentHeight - var2.getHeight());
      this.mMaxOffset = Math.max(this.mParentHeight - var3, this.mMinOffset);
      if (this.mState == 3) {
         ViewCompat.offsetTopAndBottom(var2, this.mMinOffset);
      } else if (this.mHideable && this.mState == 5) {
         ViewCompat.offsetTopAndBottom(var2, this.mParentHeight);
      } else if (this.mState == 4) {
         ViewCompat.offsetTopAndBottom(var2, this.mMaxOffset);
      } else if (this.mState == 1 || this.mState == 2) {
         ViewCompat.offsetTopAndBottom(var2, var4 - var2.getTop());
      }

      if (this.mViewDragHelper == null) {
         this.mViewDragHelper = ViewDragHelper.create(var1, this.mDragCallback);
      }

      this.mViewRef = new WeakReference(var2);
      this.mNestedScrollingChildRef = new WeakReference(this.findScrollingChild(var2));
      return true;
   }

   public boolean onNestedPreFling(CoordinatorLayout var1, View var2, View var3, float var4, float var5) {
      boolean var6;
      if (var3 != this.mNestedScrollingChildRef.get() || this.mState == 3 && !super.onNestedPreFling(var1, var2, var3, var4, var5)) {
         var6 = false;
      } else {
         var6 = true;
      }

      return var6;
   }

   public void onNestedPreScroll(CoordinatorLayout var1, View var2, View var3, int var4, int var5, int[] var6) {
      if (var3 == (View)this.mNestedScrollingChildRef.get()) {
         var4 = var2.getTop();
         int var7 = var4 - var5;
         if (var5 > 0) {
            if (var7 < this.mMinOffset) {
               var6[1] = var4 - this.mMinOffset;
               ViewCompat.offsetTopAndBottom(var2, -var6[1]);
               this.setStateInternal(3);
            } else {
               var6[1] = var5;
               ViewCompat.offsetTopAndBottom(var2, -var5);
               this.setStateInternal(1);
            }
         } else if (var5 < 0 && !var3.canScrollVertically(-1)) {
            if (var7 > this.mMaxOffset && !this.mHideable) {
               var6[1] = var4 - this.mMaxOffset;
               ViewCompat.offsetTopAndBottom(var2, -var6[1]);
               this.setStateInternal(4);
            } else {
               var6[1] = var5;
               ViewCompat.offsetTopAndBottom(var2, -var5);
               this.setStateInternal(1);
            }
         }

         this.dispatchOnSlide(var2.getTop());
         this.mLastNestedScrollDy = var5;
         this.mNestedScrolled = true;
      }
   }

   public void onRestoreInstanceState(CoordinatorLayout var1, View var2, Parcelable var3) {
      BottomSheetBehavior.SavedState var4 = (BottomSheetBehavior.SavedState)var3;
      super.onRestoreInstanceState(var1, var2, var4.getSuperState());
      if (var4.state != 1 && var4.state != 2) {
         this.mState = var4.state;
      } else {
         this.mState = 4;
      }

   }

   public Parcelable onSaveInstanceState(CoordinatorLayout var1, View var2) {
      return new BottomSheetBehavior.SavedState(super.onSaveInstanceState(var1, var2), this.mState);
   }

   public boolean onStartNestedScroll(CoordinatorLayout var1, View var2, View var3, View var4, int var5) {
      boolean var6 = false;
      this.mLastNestedScrollDy = 0;
      this.mNestedScrolled = false;
      if ((var5 & 2) != 0) {
         var6 = true;
      }

      return var6;
   }

   public void onStopNestedScroll(CoordinatorLayout var1, View var2, View var3) {
      int var4 = var2.getTop();
      int var5 = this.mMinOffset;
      byte var6 = 3;
      if (var4 == var5) {
         this.setStateInternal(3);
      } else if (this.mNestedScrollingChildRef != null && var3 == this.mNestedScrollingChildRef.get() && this.mNestedScrolled) {
         if (this.mLastNestedScrollDy > 0) {
            var4 = this.mMinOffset;
         } else if (this.mHideable && this.shouldHide(var2, this.getYVelocity())) {
            var4 = this.mParentHeight;
            var6 = 5;
         } else {
            label49: {
               if (this.mLastNestedScrollDy == 0) {
                  var4 = var2.getTop();
                  if (Math.abs(var4 - this.mMinOffset) < Math.abs(var4 - this.mMaxOffset)) {
                     var4 = this.mMinOffset;
                     break label49;
                  }

                  var4 = this.mMaxOffset;
               } else {
                  var4 = this.mMaxOffset;
               }

               var6 = 4;
            }
         }

         if (this.mViewDragHelper.smoothSlideViewTo(var2, var2.getLeft(), var4)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation(var2, new BottomSheetBehavior.SettleRunnable(var2, var6));
         } else {
            this.setStateInternal(var6);
         }

         this.mNestedScrolled = false;
      }
   }

   public boolean onTouchEvent(CoordinatorLayout var1, View var2, MotionEvent var3) {
      if (!var2.isShown()) {
         return false;
      } else {
         int var4 = var3.getActionMasked();
         if (this.mState == 1 && var4 == 0) {
            return true;
         } else {
            this.mViewDragHelper.processTouchEvent(var3);
            if (var4 == 0) {
               this.reset();
            }

            if (this.mVelocityTracker == null) {
               this.mVelocityTracker = VelocityTracker.obtain();
            }

            this.mVelocityTracker.addMovement(var3);
            if (var4 == 2 && !this.mIgnoreEvents && Math.abs((float)this.mInitialY - var3.getY()) > (float)this.mViewDragHelper.getTouchSlop()) {
               this.mViewDragHelper.captureChildView(var2, var3.getPointerId(var3.getActionIndex()));
            }

            return this.mIgnoreEvents ^ true;
         }
      }
   }

   public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback var1) {
      this.mCallback = var1;
   }

   public void setHideable(boolean var1) {
      this.mHideable = var1;
   }

   public final void setPeekHeight(int var1) {
      boolean var4;
      label36: {
         boolean var2 = true;
         if (var1 == -1) {
            if (!this.mPeekHeightAuto) {
               this.mPeekHeightAuto = true;
               var4 = var2;
               break label36;
            }
         } else if (this.mPeekHeightAuto || this.mPeekHeight != var1) {
            this.mPeekHeightAuto = false;
            this.mPeekHeight = Math.max(0, var1);
            this.mMaxOffset = this.mParentHeight - var1;
            var4 = var2;
            break label36;
         }

         var4 = false;
      }

      if (var4 && this.mState == 4 && this.mViewRef != null) {
         View var3 = (View)this.mViewRef.get();
         if (var3 != null) {
            var3.requestLayout();
         }
      }

   }

   public void setSkipCollapsed(boolean var1) {
      this.mSkipCollapsed = var1;
   }

   public final void setState(final int var1) {
      if (var1 != this.mState) {
         if (this.mViewRef == null) {
            if (var1 == 4 || var1 == 3 || this.mHideable && var1 == 5) {
               this.mState = var1;
            }

         } else {
            final View var2 = (View)this.mViewRef.get();
            if (var2 != null) {
               ViewParent var3 = var2.getParent();
               if (var3 != null && var3.isLayoutRequested() && ViewCompat.isAttachedToWindow(var2)) {
                  var2.post(new Runnable() {
                     public void run() {
                        BottomSheetBehavior.this.startSettlingAnimation(var2, var1);
                     }
                  });
               } else {
                  this.startSettlingAnimation(var2, var1);
               }

            }
         }
      }
   }

   void setStateInternal(int var1) {
      if (this.mState != var1) {
         this.mState = var1;
         View var2 = (View)this.mViewRef.get();
         if (var2 != null && this.mCallback != null) {
            this.mCallback.onStateChanged(var2, var1);
         }

      }
   }

   boolean shouldHide(View var1, float var2) {
      boolean var3 = this.mSkipCollapsed;
      boolean var4 = true;
      if (var3) {
         return true;
      } else if (var1.getTop() < this.mMaxOffset) {
         return false;
      } else {
         if (Math.abs((float)var1.getTop() + var2 * 0.1F - (float)this.mMaxOffset) / (float)this.mPeekHeight <= 0.5F) {
            var4 = false;
         }

         return var4;
      }
   }

   void startSettlingAnimation(View var1, int var2) {
      int var3;
      if (var2 == 4) {
         var3 = this.mMaxOffset;
      } else if (var2 == 3) {
         var3 = this.mMinOffset;
      } else {
         if (!this.mHideable || var2 != 5) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Illegal state argument: ");
            var4.append(var2);
            throw new IllegalArgumentException(var4.toString());
         }

         var3 = this.mParentHeight;
      }

      if (this.mViewDragHelper.smoothSlideViewTo(var1, var1.getLeft(), var3)) {
         this.setStateInternal(2);
         ViewCompat.postOnAnimation(var1, new BottomSheetBehavior.SettleRunnable(var1, var2));
      } else {
         this.setStateInternal(var2);
      }

   }

   public abstract static class BottomSheetCallback {
      public abstract void onSlide(@NonNull View var1, float var2);

      public abstract void onStateChanged(@NonNull View var1, int var2);
   }

   protected static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public BottomSheetBehavior.SavedState createFromParcel(Parcel var1) {
            return new BottomSheetBehavior.SavedState(var1, (ClassLoader)null);
         }

         public BottomSheetBehavior.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new BottomSheetBehavior.SavedState(var1, var2);
         }

         public BottomSheetBehavior.SavedState[] newArray(int var1) {
            return new BottomSheetBehavior.SavedState[var1];
         }
      };
      final int state;

      public SavedState(Parcel var1) {
         this(var1, (ClassLoader)null);
      }

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.state = var1.readInt();
      }

      public SavedState(Parcelable var1, int var2) {
         super(var1);
         this.state = var2;
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeInt(this.state);
      }
   }

   private class SettleRunnable implements Runnable {
      private final int mTargetState;
      private final View mView;

      SettleRunnable(View var2, int var3) {
         this.mView = var2;
         this.mTargetState = var3;
      }

      public void run() {
         if (BottomSheetBehavior.this.mViewDragHelper != null && BottomSheetBehavior.this.mViewDragHelper.continueSettling(true)) {
            ViewCompat.postOnAnimation(this.mView, this);
         } else {
            BottomSheetBehavior.this.setStateInternal(this.mTargetState);
         }

      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface State {
   }
}
