package android.support.v4.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import java.util.Arrays;

public class ViewDragHelper {
   private static final int BASE_SETTLE_DURATION = 256;
   public static final int DIRECTION_ALL = 3;
   public static final int DIRECTION_HORIZONTAL = 1;
   public static final int DIRECTION_VERTICAL = 2;
   public static final int EDGE_ALL = 15;
   public static final int EDGE_BOTTOM = 8;
   public static final int EDGE_LEFT = 1;
   public static final int EDGE_RIGHT = 2;
   private static final int EDGE_SIZE = 20;
   public static final int EDGE_TOP = 4;
   public static final int INVALID_POINTER = -1;
   private static final int MAX_SETTLE_DURATION = 600;
   public static final int STATE_DRAGGING = 1;
   public static final int STATE_IDLE = 0;
   public static final int STATE_SETTLING = 2;
   private static final String TAG = "ViewDragHelper";
   private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float var1) {
         --var1;
         return var1 * var1 * var1 * var1 * var1 + 1.0F;
      }
   };
   private int mActivePointerId = -1;
   private final ViewDragHelper.Callback mCallback;
   private View mCapturedView;
   private int mDragState;
   private int[] mEdgeDragsInProgress;
   private int[] mEdgeDragsLocked;
   private int mEdgeSize;
   private int[] mInitialEdgesTouched;
   private float[] mInitialMotionX;
   private float[] mInitialMotionY;
   private float[] mLastMotionX;
   private float[] mLastMotionY;
   private float mMaxVelocity;
   private float mMinVelocity;
   private final ViewGroup mParentView;
   private int mPointersDown;
   private boolean mReleaseInProgress;
   private ScrollerCompat mScroller;
   private final Runnable mSetIdleRunnable = new Runnable() {
      public void run() {
         ViewDragHelper.this.setDragState(0);
      }
   };
   private int mTouchSlop;
   private int mTrackingEdges;
   private VelocityTracker mVelocityTracker;

   private ViewDragHelper(Context var1, ViewGroup var2, ViewDragHelper.Callback var3) {
      if (var2 == null) {
         throw new IllegalArgumentException("Parent view may not be null");
      } else if (var3 == null) {
         throw new IllegalArgumentException("Callback may not be null");
      } else {
         this.mParentView = var2;
         this.mCallback = var3;
         ViewConfiguration var4 = ViewConfiguration.get(var1);
         this.mEdgeSize = (int)(20.0F * var1.getResources().getDisplayMetrics().density + 0.5F);
         this.mTouchSlop = var4.getScaledTouchSlop();
         this.mMaxVelocity = (float)var4.getScaledMaximumFlingVelocity();
         this.mMinVelocity = (float)var4.getScaledMinimumFlingVelocity();
         this.mScroller = ScrollerCompat.create(var1, sInterpolator);
      }
   }

   private boolean checkNewEdgeDrag(float var1, float var2, int var3, int var4) {
      boolean var5 = false;
      var1 = Math.abs(var1);
      var2 = Math.abs(var2);
      boolean var6 = var5;
      if ((this.mInitialEdgesTouched[var3] & var4) == var4) {
         var6 = var5;
         if ((this.mTrackingEdges & var4) != 0) {
            var6 = var5;
            if ((this.mEdgeDragsLocked[var3] & var4) != var4) {
               var6 = var5;
               if ((this.mEdgeDragsInProgress[var3] & var4) != var4) {
                  if (var1 <= (float)this.mTouchSlop && var2 <= (float)this.mTouchSlop) {
                     var6 = var5;
                  } else if (var1 < 0.5F * var2 && this.mCallback.onEdgeLock(var4)) {
                     int[] var7 = this.mEdgeDragsLocked;
                     var7[var3] |= var4;
                     var6 = var5;
                  } else {
                     var6 = var5;
                     if ((this.mEdgeDragsInProgress[var3] & var4) == 0) {
                        var6 = var5;
                        if (var1 > (float)this.mTouchSlop) {
                           var6 = true;
                        }
                     }
                  }
               }
            }
         }
      }

      return var6;
   }

   private boolean checkTouchSlop(View var1, float var2, float var3) {
      boolean var4 = true;
      if (var1 == null) {
         var4 = false;
      } else {
         boolean var5;
         if (this.mCallback.getViewHorizontalDragRange(var1) > 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var6;
         if (this.mCallback.getViewVerticalDragRange(var1) > 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         if (var5 && var6) {
            if (var2 * var2 + var3 * var3 <= (float)(this.mTouchSlop * this.mTouchSlop)) {
               var4 = false;
            }
         } else if (var5) {
            if (Math.abs(var2) <= (float)this.mTouchSlop) {
               var4 = false;
            }
         } else if (var6) {
            if (Math.abs(var3) <= (float)this.mTouchSlop) {
               var4 = false;
            }
         } else {
            var4 = false;
         }
      }

      return var4;
   }

   private float clampMag(float var1, float var2, float var3) {
      float var4 = Math.abs(var1);
      if (var4 < var2) {
         var2 = 0.0F;
      } else if (var4 > var3) {
         var2 = var3;
         if (var1 <= 0.0F) {
            var2 = -var3;
         }
      } else {
         var2 = var1;
      }

      return var2;
   }

   private int clampMag(int var1, int var2, int var3) {
      int var4 = Math.abs(var1);
      if (var4 < var2) {
         var2 = 0;
      } else if (var4 > var3) {
         var2 = var3;
         if (var1 <= 0) {
            var2 = -var3;
         }
      } else {
         var2 = var1;
      }

      return var2;
   }

   private void clearMotionHistory() {
      if (this.mInitialMotionX != null) {
         Arrays.fill(this.mInitialMotionX, 0.0F);
         Arrays.fill(this.mInitialMotionY, 0.0F);
         Arrays.fill(this.mLastMotionX, 0.0F);
         Arrays.fill(this.mLastMotionY, 0.0F);
         Arrays.fill(this.mInitialEdgesTouched, 0);
         Arrays.fill(this.mEdgeDragsInProgress, 0);
         Arrays.fill(this.mEdgeDragsLocked, 0);
         this.mPointersDown = 0;
      }

   }

   private void clearMotionHistory(int var1) {
      if (this.mInitialMotionX != null && this.isPointerDown(var1)) {
         this.mInitialMotionX[var1] = 0.0F;
         this.mInitialMotionY[var1] = 0.0F;
         this.mLastMotionX[var1] = 0.0F;
         this.mLastMotionY[var1] = 0.0F;
         this.mInitialEdgesTouched[var1] = 0;
         this.mEdgeDragsInProgress[var1] = 0;
         this.mEdgeDragsLocked[var1] = 0;
         this.mPointersDown &= ~(1 << var1);
      }

   }

   private int computeAxisDuration(int var1, int var2, int var3) {
      if (var1 == 0) {
         var1 = 0;
      } else {
         int var4 = this.mParentView.getWidth();
         int var5 = var4 / 2;
         float var6 = Math.min(1.0F, (float)Math.abs(var1) / (float)var4);
         float var7 = (float)var5;
         float var8 = (float)var5;
         var6 = this.distanceInfluenceForSnapDuration(var6);
         var2 = Math.abs(var2);
         if (var2 > 0) {
            var1 = Math.round(1000.0F * Math.abs((var7 + var8 * var6) / (float)var2)) * 4;
         } else {
            var1 = (int)(((float)Math.abs(var1) / (float)var3 + 1.0F) * 256.0F);
         }

         var1 = Math.min(var1, 600);
      }

      return var1;
   }

   private int computeSettleDuration(View var1, int var2, int var3, int var4, int var5) {
      var4 = this.clampMag(var4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
      var5 = this.clampMag(var5, (int)this.mMinVelocity, (int)this.mMaxVelocity);
      int var6 = Math.abs(var2);
      int var7 = Math.abs(var3);
      int var8 = Math.abs(var4);
      int var9 = Math.abs(var5);
      int var10 = var8 + var9;
      int var11 = var6 + var7;
      float var12;
      if (var4 != 0) {
         var12 = (float)var8 / (float)var10;
      } else {
         var12 = (float)var6 / (float)var11;
      }

      float var13;
      if (var5 != 0) {
         var13 = (float)var9 / (float)var10;
      } else {
         var13 = (float)var7 / (float)var11;
      }

      var2 = this.computeAxisDuration(var2, var4, this.mCallback.getViewHorizontalDragRange(var1));
      var3 = this.computeAxisDuration(var3, var5, this.mCallback.getViewVerticalDragRange(var1));
      return (int)((float)var2 * var12 + (float)var3 * var13);
   }

   public static ViewDragHelper create(ViewGroup var0, float var1, ViewDragHelper.Callback var2) {
      ViewDragHelper var3 = create(var0, var2);
      var3.mTouchSlop = (int)((float)var3.mTouchSlop * (1.0F / var1));
      return var3;
   }

   public static ViewDragHelper create(ViewGroup var0, ViewDragHelper.Callback var1) {
      return new ViewDragHelper(var0.getContext(), var0, var1);
   }

   private void dispatchViewReleased(float var1, float var2) {
      this.mReleaseInProgress = true;
      this.mCallback.onViewReleased(this.mCapturedView, var1, var2);
      this.mReleaseInProgress = false;
      if (this.mDragState == 1) {
         this.setDragState(0);
      }

   }

   private float distanceInfluenceForSnapDuration(float var1) {
      return (float)Math.sin((double)((float)((double)(var1 - 0.5F) * 0.4712389167638204D)));
   }

   private void dragTo(int var1, int var2, int var3, int var4) {
      int var5 = var1;
      int var6 = var2;
      int var7 = this.mCapturedView.getLeft();
      int var8 = this.mCapturedView.getTop();
      if (var3 != 0) {
         var5 = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, var1, var3);
         ViewCompat.offsetLeftAndRight(this.mCapturedView, var5 - var7);
      }

      if (var4 != 0) {
         var6 = this.mCallback.clampViewPositionVertical(this.mCapturedView, var2, var4);
         ViewCompat.offsetTopAndBottom(this.mCapturedView, var6 - var8);
      }

      if (var3 != 0 || var4 != 0) {
         this.mCallback.onViewPositionChanged(this.mCapturedView, var5, var6, var5 - var7, var6 - var8);
      }

   }

   private void ensureMotionHistorySizeForId(int var1) {
      if (this.mInitialMotionX == null || this.mInitialMotionX.length <= var1) {
         float[] var2 = new float[var1 + 1];
         float[] var3 = new float[var1 + 1];
         float[] var4 = new float[var1 + 1];
         float[] var5 = new float[var1 + 1];
         int[] var6 = new int[var1 + 1];
         int[] var7 = new int[var1 + 1];
         int[] var8 = new int[var1 + 1];
         if (this.mInitialMotionX != null) {
            System.arraycopy(this.mInitialMotionX, 0, var2, 0, this.mInitialMotionX.length);
            System.arraycopy(this.mInitialMotionY, 0, var3, 0, this.mInitialMotionY.length);
            System.arraycopy(this.mLastMotionX, 0, var4, 0, this.mLastMotionX.length);
            System.arraycopy(this.mLastMotionY, 0, var5, 0, this.mLastMotionY.length);
            System.arraycopy(this.mInitialEdgesTouched, 0, var6, 0, this.mInitialEdgesTouched.length);
            System.arraycopy(this.mEdgeDragsInProgress, 0, var7, 0, this.mEdgeDragsInProgress.length);
            System.arraycopy(this.mEdgeDragsLocked, 0, var8, 0, this.mEdgeDragsLocked.length);
         }

         this.mInitialMotionX = var2;
         this.mInitialMotionY = var3;
         this.mLastMotionX = var4;
         this.mLastMotionY = var5;
         this.mInitialEdgesTouched = var6;
         this.mEdgeDragsInProgress = var7;
         this.mEdgeDragsLocked = var8;
      }

   }

   private boolean forceSettleCapturedViewAt(int var1, int var2, int var3, int var4) {
      boolean var5 = false;
      int var6 = this.mCapturedView.getLeft();
      int var7 = this.mCapturedView.getTop();
      var1 -= var6;
      var2 -= var7;
      if (var1 == 0 && var2 == 0) {
         this.mScroller.abortAnimation();
         this.setDragState(0);
      } else {
         var3 = this.computeSettleDuration(this.mCapturedView, var1, var2, var3, var4);
         this.mScroller.startScroll(var6, var7, var1, var2, var3);
         this.setDragState(2);
         var5 = true;
      }

      return var5;
   }

   private int getEdgesTouched(int var1, int var2) {
      int var3 = 0;
      if (var1 < this.mParentView.getLeft() + this.mEdgeSize) {
         var3 = 0 | 1;
      }

      int var4 = var3;
      if (var2 < this.mParentView.getTop() + this.mEdgeSize) {
         var4 = var3 | 4;
      }

      var3 = var4;
      if (var1 > this.mParentView.getRight() - this.mEdgeSize) {
         var3 = var4 | 2;
      }

      var1 = var3;
      if (var2 > this.mParentView.getBottom() - this.mEdgeSize) {
         var1 = var3 | 8;
      }

      return var1;
   }

   private boolean isValidPointerForActionMove(int var1) {
      boolean var2;
      if (!this.isPointerDown(var1)) {
         Log.e("ViewDragHelper", "Ignoring pointerId=" + var1 + " because ACTION_DOWN was not received " + "for this pointer before ACTION_MOVE. It likely happened because " + " ViewDragHelper did not receive all the events in the event stream.");
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private void releaseViewForPointerUp() {
      this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
      this.dispatchViewReleased(this.clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), this.clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
   }

   private void reportNewEdgeDrags(float var1, float var2, int var3) {
      int var4 = 0;
      if (this.checkNewEdgeDrag(var1, var2, var3, 1)) {
         var4 = 0 | 1;
      }

      int var5 = var4;
      if (this.checkNewEdgeDrag(var2, var1, var3, 4)) {
         var5 = var4 | 4;
      }

      var4 = var5;
      if (this.checkNewEdgeDrag(var1, var2, var3, 2)) {
         var4 = var5 | 2;
      }

      var5 = var4;
      if (this.checkNewEdgeDrag(var2, var1, var3, 8)) {
         var5 = var4 | 8;
      }

      if (var5 != 0) {
         int[] var6 = this.mEdgeDragsInProgress;
         var6[var3] |= var5;
         this.mCallback.onEdgeDragStarted(var5, var3);
      }

   }

   private void saveInitialMotion(float var1, float var2, int var3) {
      this.ensureMotionHistorySizeForId(var3);
      float[] var4 = this.mInitialMotionX;
      this.mLastMotionX[var3] = var1;
      var4[var3] = var1;
      var4 = this.mInitialMotionY;
      this.mLastMotionY[var3] = var2;
      var4[var3] = var2;
      this.mInitialEdgesTouched[var3] = this.getEdgesTouched((int)var1, (int)var2);
      this.mPointersDown |= 1 << var3;
   }

   private void saveLastMotion(MotionEvent var1) {
      int var2 = var1.getPointerCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var1.getPointerId(var3);
         if (this.isValidPointerForActionMove(var4)) {
            float var5 = var1.getX(var3);
            float var6 = var1.getY(var3);
            this.mLastMotionX[var4] = var5;
            this.mLastMotionY[var4] = var6;
         }
      }

   }

   public void abort() {
      this.cancel();
      if (this.mDragState == 2) {
         int var1 = this.mScroller.getCurrX();
         int var2 = this.mScroller.getCurrY();
         this.mScroller.abortAnimation();
         int var3 = this.mScroller.getCurrX();
         int var4 = this.mScroller.getCurrY();
         this.mCallback.onViewPositionChanged(this.mCapturedView, var3, var4, var3 - var1, var4 - var2);
      }

      this.setDragState(0);
   }

   protected boolean canScroll(View var1, boolean var2, int var3, int var4, int var5, int var6) {
      if (var1 instanceof ViewGroup) {
         ViewGroup var7 = (ViewGroup)var1;
         int var8 = var1.getScrollX();
         int var9 = var1.getScrollY();

         for(int var10 = var7.getChildCount() - 1; var10 >= 0; --var10) {
            View var11 = var7.getChildAt(var10);
            if (var5 + var8 >= var11.getLeft() && var5 + var8 < var11.getRight() && var6 + var9 >= var11.getTop() && var6 + var9 < var11.getBottom() && this.canScroll(var11, true, var3, var4, var5 + var8 - var11.getLeft(), var6 + var9 - var11.getTop())) {
               var2 = true;
               return var2;
            }
         }
      }

      if (var2 && (ViewCompat.canScrollHorizontally(var1, -var3) || ViewCompat.canScrollVertically(var1, -var4))) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void cancel() {
      this.mActivePointerId = -1;
      this.clearMotionHistory();
      if (this.mVelocityTracker != null) {
         this.mVelocityTracker.recycle();
         this.mVelocityTracker = null;
      }

   }

   public void captureChildView(View var1, int var2) {
      if (var1.getParent() != this.mParentView) {
         throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")");
      } else {
         this.mCapturedView = var1;
         this.mActivePointerId = var2;
         this.mCallback.onViewCaptured(var1, var2);
         this.setDragState(1);
      }
   }

   public boolean checkTouchSlop(int var1) {
      int var2 = this.mInitialMotionX.length;
      int var3 = 0;

      boolean var4;
      while(true) {
         if (var3 >= var2) {
            var4 = false;
            break;
         }

         if (this.checkTouchSlop(var1, var3)) {
            var4 = true;
            break;
         }

         ++var3;
      }

      return var4;
   }

   public boolean checkTouchSlop(int var1, int var2) {
      boolean var3 = true;
      if (!this.isPointerDown(var2)) {
         var3 = false;
      } else {
         boolean var4;
         if ((var1 & 1) == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         boolean var7;
         if ((var1 & 2) == 2) {
            var7 = true;
         } else {
            var7 = false;
         }

         float var5 = this.mLastMotionX[var2] - this.mInitialMotionX[var2];
         float var6 = this.mLastMotionY[var2] - this.mInitialMotionY[var2];
         if (var4 && var7) {
            if (var5 * var5 + var6 * var6 <= (float)(this.mTouchSlop * this.mTouchSlop)) {
               var3 = false;
            }
         } else if (var4) {
            if (Math.abs(var5) <= (float)this.mTouchSlop) {
               var3 = false;
            }
         } else if (var7) {
            if (Math.abs(var6) <= (float)this.mTouchSlop) {
               var3 = false;
            }
         } else {
            var3 = false;
         }
      }

      return var3;
   }

   public boolean continueSettling(boolean var1) {
      if (this.mDragState == 2) {
         boolean var2 = this.mScroller.computeScrollOffset();
         int var3 = this.mScroller.getCurrX();
         int var4 = this.mScroller.getCurrY();
         int var5 = var3 - this.mCapturedView.getLeft();
         int var6 = var4 - this.mCapturedView.getTop();
         if (var5 != 0) {
            ViewCompat.offsetLeftAndRight(this.mCapturedView, var5);
         }

         if (var6 != 0) {
            ViewCompat.offsetTopAndBottom(this.mCapturedView, var6);
         }

         if (var5 != 0 || var6 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, var3, var4, var5, var6);
         }

         boolean var7 = var2;
         if (var2) {
            var7 = var2;
            if (var3 == this.mScroller.getFinalX()) {
               var7 = var2;
               if (var4 == this.mScroller.getFinalY()) {
                  this.mScroller.abortAnimation();
                  var7 = false;
               }
            }
         }

         if (!var7) {
            if (var1) {
               this.mParentView.post(this.mSetIdleRunnable);
            } else {
               this.setDragState(0);
            }
         }
      }

      if (this.mDragState == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public View findTopChildUnder(int var1, int var2) {
      int var3 = this.mParentView.getChildCount() - 1;

      View var4;
      while(true) {
         if (var3 < 0) {
            var4 = null;
            break;
         }

         var4 = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(var3));
         if (var1 >= var4.getLeft() && var1 < var4.getRight() && var2 >= var4.getTop() && var2 < var4.getBottom()) {
            break;
         }

         --var3;
      }

      return var4;
   }

   public void flingCapturedView(int var1, int var2, int var3, int var4) {
      if (!this.mReleaseInProgress) {
         throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
      } else {
         this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), var1, var3, var2, var4);
         this.setDragState(2);
      }
   }

   public int getActivePointerId() {
      return this.mActivePointerId;
   }

   public View getCapturedView() {
      return this.mCapturedView;
   }

   public int getEdgeSize() {
      return this.mEdgeSize;
   }

   public float getMinVelocity() {
      return this.mMinVelocity;
   }

   public int getTouchSlop() {
      return this.mTouchSlop;
   }

   public int getViewDragState() {
      return this.mDragState;
   }

   public boolean isCapturedViewUnder(int var1, int var2) {
      return this.isViewUnder(this.mCapturedView, var1, var2);
   }

   public boolean isEdgeTouched(int var1) {
      int var2 = this.mInitialEdgesTouched.length;
      int var3 = 0;

      boolean var4;
      while(true) {
         if (var3 >= var2) {
            var4 = false;
            break;
         }

         if (this.isEdgeTouched(var1, var3)) {
            var4 = true;
            break;
         }

         ++var3;
      }

      return var4;
   }

   public boolean isEdgeTouched(int var1, int var2) {
      boolean var3;
      if (this.isPointerDown(var2) && (this.mInitialEdgesTouched[var2] & var1) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isPointerDown(int var1) {
      boolean var2 = true;
      if ((this.mPointersDown & 1 << var1) == 0) {
         var2 = false;
      }

      return var2;
   }

   public boolean isViewUnder(View var1, int var2, int var3) {
      boolean var4 = false;
      boolean var5;
      if (var1 == null) {
         var5 = var4;
      } else {
         var5 = var4;
         if (var2 >= var1.getLeft()) {
            var5 = var4;
            if (var2 < var1.getRight()) {
               var5 = var4;
               if (var3 >= var1.getTop()) {
                  var5 = var4;
                  if (var3 < var1.getBottom()) {
                     var5 = true;
                  }
               }
            }
         }
      }

      return var5;
   }

   public void processTouchEvent(MotionEvent var1) {
      int var2 = MotionEventCompat.getActionMasked(var1);
      int var3 = MotionEventCompat.getActionIndex(var1);
      if (var2 == 0) {
         this.cancel();
      }

      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

      this.mVelocityTracker.addMovement(var1);
      float var4;
      float var5;
      switch(var2) {
      case 0:
         var4 = var1.getX();
         var5 = var1.getY();
         var3 = var1.getPointerId(0);
         View var12 = this.findTopChildUnder((int)var4, (int)var5);
         this.saveInitialMotion(var4, var5, var3);
         this.tryCaptureViewForDrag(var12, var3);
         var2 = this.mInitialEdgesTouched[var3];
         if ((this.mTrackingEdges & var2) != 0) {
            this.mCallback.onEdgeTouched(this.mTrackingEdges & var2, var3);
         }
         break;
      case 1:
         if (this.mDragState == 1) {
            this.releaseViewForPointerUp();
         }

         this.cancel();
         break;
      case 2:
         if (this.mDragState == 1) {
            if (this.isValidPointerForActionMove(this.mActivePointerId)) {
               var3 = var1.findPointerIndex(this.mActivePointerId);
               var4 = var1.getX(var3);
               var5 = var1.getY(var3);
               var3 = (int)(var4 - this.mLastMotionX[this.mActivePointerId]);
               var2 = (int)(var5 - this.mLastMotionY[this.mActivePointerId]);
               this.dragTo(this.mCapturedView.getLeft() + var3, this.mCapturedView.getTop() + var2, var3, var2);
               this.saveLastMotion(var1);
            }
         } else {
            var2 = var1.getPointerCount();

            for(var3 = 0; var3 < var2; ++var3) {
               int var13 = var1.getPointerId(var3);
               if (this.isValidPointerForActionMove(var13)) {
                  var4 = var1.getX(var3);
                  var5 = var1.getY(var3);
                  float var7 = var4 - this.mInitialMotionX[var13];
                  float var8 = var5 - this.mInitialMotionY[var13];
                  this.reportNewEdgeDrags(var7, var8, var13);
                  if (this.mDragState == 1) {
                     break;
                  }

                  View var9 = this.findTopChildUnder((int)var4, (int)var5);
                  if (this.checkTouchSlop(var9, var7, var8) && this.tryCaptureViewForDrag(var9, var13)) {
                     break;
                  }
               }
            }

            this.saveLastMotion(var1);
         }
         break;
      case 3:
         if (this.mDragState == 1) {
            this.dispatchViewReleased(0.0F, 0.0F);
         }

         this.cancel();
      case 4:
      default:
         break;
      case 5:
         var2 = var1.getPointerId(var3);
         var5 = var1.getX(var3);
         var4 = var1.getY(var3);
         this.saveInitialMotion(var5, var4, var2);
         if (this.mDragState == 0) {
            this.tryCaptureViewForDrag(this.findTopChildUnder((int)var5, (int)var4), var2);
            var3 = this.mInitialEdgesTouched[var2];
            if ((this.mTrackingEdges & var3) != 0) {
               this.mCallback.onEdgeTouched(this.mTrackingEdges & var3, var2);
            }
         } else if (this.isCapturedViewUnder((int)var5, (int)var4)) {
            this.tryCaptureViewForDrag(this.mCapturedView, var2);
         }
         break;
      case 6:
         int var10 = var1.getPointerId(var3);
         if (this.mDragState == 1 && var10 == this.mActivePointerId) {
            byte var6 = -1;
            int var11 = var1.getPointerCount();
            var3 = 0;

            while(true) {
               var2 = var6;
               if (var3 >= var11) {
                  break;
               }

               var2 = var1.getPointerId(var3);
               if (var2 != this.mActivePointerId) {
                  var4 = var1.getX(var3);
                  var5 = var1.getY(var3);
                  if (this.findTopChildUnder((int)var4, (int)var5) == this.mCapturedView && this.tryCaptureViewForDrag(this.mCapturedView, var2)) {
                     var2 = this.mActivePointerId;
                     break;
                  }
               }

               ++var3;
            }

            if (var2 == -1) {
               this.releaseViewForPointerUp();
            }
         }

         this.clearMotionHistory(var10);
      }

   }

   void setDragState(int var1) {
      this.mParentView.removeCallbacks(this.mSetIdleRunnable);
      if (this.mDragState != var1) {
         this.mDragState = var1;
         this.mCallback.onViewDragStateChanged(var1);
         if (this.mDragState == 0) {
            this.mCapturedView = null;
         }
      }

   }

   public void setEdgeTrackingEnabled(int var1) {
      this.mTrackingEdges = var1;
   }

   public void setMinVelocity(float var1) {
      this.mMinVelocity = var1;
   }

   public boolean settleCapturedViewAt(int var1, int var2) {
      if (!this.mReleaseInProgress) {
         throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
      } else {
         return this.forceSettleCapturedViewAt(var1, var2, (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
      }
   }

   public boolean shouldInterceptTouchEvent(MotionEvent var1) {
      int var2 = MotionEventCompat.getActionMasked(var1);
      int var3 = MotionEventCompat.getActionIndex(var1);
      if (var2 == 0) {
         this.cancel();
      }

      if (this.mVelocityTracker == null) {
         this.mVelocityTracker = VelocityTracker.obtain();
      }

      this.mVelocityTracker.addMovement(var1);
      float var5;
      float var6;
      View var18;
      switch(var2) {
      case 0:
         var5 = var1.getX();
         var6 = var1.getY();
         var3 = var1.getPointerId(0);
         this.saveInitialMotion(var5, var6, var3);
         var18 = this.findTopChildUnder((int)var5, (int)var6);
         if (var18 == this.mCapturedView && this.mDragState == 2) {
            this.tryCaptureViewForDrag(var18, var3);
         }

         var2 = this.mInitialEdgesTouched[var3];
         if ((this.mTrackingEdges & var2) != 0) {
            this.mCallback.onEdgeTouched(this.mTrackingEdges & var2, var3);
         }
         break;
      case 1:
      case 3:
         this.cancel();
         break;
      case 2:
         if (this.mInitialMotionX != null && this.mInitialMotionY != null) {
            int var7 = var1.getPointerCount();

            for(var2 = 0; var2 < var7; ++var2) {
               int var8 = var1.getPointerId(var2);
               if (this.isValidPointerForActionMove(var8)) {
                  float var9 = var1.getX(var2);
                  var5 = var1.getY(var2);
                  float var10 = var9 - this.mInitialMotionX[var8];
                  var6 = var5 - this.mInitialMotionY[var8];
                  View var11 = this.findTopChildUnder((int)var9, (int)var5);
                  boolean var19;
                  if (var11 != null && this.checkTouchSlop(var11, var10, var6)) {
                     var19 = true;
                  } else {
                     var19 = false;
                  }

                  if (var19) {
                     int var12 = var11.getLeft();
                     int var13 = (int)var10;
                     int var14 = this.mCallback.clampViewPositionHorizontal(var11, var12 + var13, (int)var10);
                     var13 = var11.getTop();
                     int var15 = (int)var6;
                     int var16 = this.mCallback.clampViewPositionVertical(var11, var13 + var15, (int)var6);
                     int var17 = this.mCallback.getViewHorizontalDragRange(var11);
                     var15 = this.mCallback.getViewVerticalDragRange(var11);
                     if ((var17 == 0 || var17 > 0 && var14 == var12) && (var15 == 0 || var15 > 0 && var16 == var13)) {
                        break;
                     }
                  }

                  this.reportNewEdgeDrags(var10, var6, var8);
                  if (this.mDragState == 1 || var19 && this.tryCaptureViewForDrag(var11, var8)) {
                     break;
                  }
               }
            }

            this.saveLastMotion(var1);
         }
      case 4:
      default:
         break;
      case 5:
         var2 = var1.getPointerId(var3);
         var6 = var1.getX(var3);
         var5 = var1.getY(var3);
         this.saveInitialMotion(var6, var5, var2);
         if (this.mDragState == 0) {
            var3 = this.mInitialEdgesTouched[var2];
            if ((this.mTrackingEdges & var3) != 0) {
               this.mCallback.onEdgeTouched(this.mTrackingEdges & var3, var2);
            }
         } else if (this.mDragState == 2) {
            var18 = this.findTopChildUnder((int)var6, (int)var5);
            if (var18 == this.mCapturedView) {
               this.tryCaptureViewForDrag(var18, var2);
            }
         }
         break;
      case 6:
         this.clearMotionHistory(var1.getPointerId(var3));
      }

      boolean var4;
      if (this.mDragState == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public boolean smoothSlideViewTo(View var1, int var2, int var3) {
      this.mCapturedView = var1;
      this.mActivePointerId = -1;
      boolean var4 = this.forceSettleCapturedViewAt(var2, var3, 0, 0);
      if (!var4 && this.mDragState == 0 && this.mCapturedView != null) {
         this.mCapturedView = null;
      }

      return var4;
   }

   boolean tryCaptureViewForDrag(View var1, int var2) {
      boolean var3 = true;
      if (var1 != this.mCapturedView || this.mActivePointerId != var2) {
         if (var1 != null && this.mCallback.tryCaptureView(var1, var2)) {
            this.mActivePointerId = var2;
            this.captureChildView(var1, var2);
         } else {
            var3 = false;
         }
      }

      return var3;
   }

   public abstract static class Callback {
      public int clampViewPositionHorizontal(View var1, int var2, int var3) {
         return 0;
      }

      public int clampViewPositionVertical(View var1, int var2, int var3) {
         return 0;
      }

      public int getOrderedChildIndex(int var1) {
         return var1;
      }

      public int getViewHorizontalDragRange(View var1) {
         return 0;
      }

      public int getViewVerticalDragRange(View var1) {
         return 0;
      }

      public void onEdgeDragStarted(int var1, int var2) {
      }

      public boolean onEdgeLock(int var1) {
         return false;
      }

      public void onEdgeTouched(int var1, int var2) {
      }

      public void onViewCaptured(View var1, int var2) {
      }

      public void onViewDragStateChanged(int var1) {
      }

      public void onViewPositionChanged(View var1, int var2, int var3, int var4, int var5) {
      }

      public void onViewReleased(View var1, float var2, float var3) {
      }

      public abstract boolean tryCaptureView(View var1, int var2);
   }
}
