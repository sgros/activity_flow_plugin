package android.support.v7.widget;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

public abstract class SnapHelper extends RecyclerView.OnFlingListener {
   private Scroller mGravityScroller;
   RecyclerView mRecyclerView;
   private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
      boolean mScrolled = false;

      public void onScrollStateChanged(RecyclerView var1, int var2) {
         super.onScrollStateChanged(var1, var2);
         if (var2 == 0 && this.mScrolled) {
            this.mScrolled = false;
            SnapHelper.this.snapToTargetExistingView();
         }

      }

      public void onScrolled(RecyclerView var1, int var2, int var3) {
         if (var2 != 0 || var3 != 0) {
            this.mScrolled = true;
         }

      }
   };

   private void destroyCallbacks() {
      this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
      this.mRecyclerView.setOnFlingListener((RecyclerView.OnFlingListener)null);
   }

   private void setupCallbacks() throws IllegalStateException {
      if (this.mRecyclerView.getOnFlingListener() == null) {
         this.mRecyclerView.addOnScrollListener(this.mScrollListener);
         this.mRecyclerView.setOnFlingListener(this);
      } else {
         throw new IllegalStateException("An instance of OnFlingListener already set.");
      }
   }

   private boolean snapFromFling(RecyclerView.LayoutManager var1, int var2, int var3) {
      if (!(var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
         return false;
      } else {
         RecyclerView.SmoothScroller var4 = this.createScroller(var1);
         if (var4 == null) {
            return false;
         } else {
            var2 = this.findTargetSnapPosition(var1, var2, var3);
            if (var2 == -1) {
               return false;
            } else {
               var4.setTargetPosition(var2);
               var1.startSmoothScroll(var4);
               return true;
            }
         }
      }
   }

   public void attachToRecyclerView(RecyclerView var1) throws IllegalStateException {
      if (this.mRecyclerView != var1) {
         if (this.mRecyclerView != null) {
            this.destroyCallbacks();
         }

         this.mRecyclerView = var1;
         if (this.mRecyclerView != null) {
            this.setupCallbacks();
            this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
            this.snapToTargetExistingView();
         }

      }
   }

   public abstract int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager var1, View var2);

   protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager var1) {
      return this.createSnapScroller(var1);
   }

   @Deprecated
   protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager var1) {
      return !(var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
         protected float calculateSpeedPerPixel(DisplayMetrics var1) {
            return 100.0F / (float)var1.densityDpi;
         }

         protected void onTargetFound(View var1, RecyclerView.State var2, RecyclerView.SmoothScroller.Action var3) {
            if (SnapHelper.this.mRecyclerView != null) {
               int[] var7 = SnapHelper.this.calculateDistanceToFinalSnap(SnapHelper.this.mRecyclerView.getLayoutManager(), var1);
               int var4 = var7[0];
               int var5 = var7[1];
               int var6 = this.calculateTimeForDeceleration(Math.max(Math.abs(var4), Math.abs(var5)));
               if (var6 > 0) {
                  var3.update(var4, var5, var6, this.mDecelerateInterpolator);
               }

            }
         }
      };
   }

   public abstract View findSnapView(RecyclerView.LayoutManager var1);

   public abstract int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3);

   public boolean onFling(int var1, int var2) {
      RecyclerView.LayoutManager var3 = this.mRecyclerView.getLayoutManager();
      boolean var4 = false;
      if (var3 == null) {
         return false;
      } else if (this.mRecyclerView.getAdapter() == null) {
         return false;
      } else {
         int var5 = this.mRecyclerView.getMinFlingVelocity();
         boolean var6;
         if (Math.abs(var2) <= var5) {
            var6 = var4;
            if (Math.abs(var1) <= var5) {
               return var6;
            }
         }

         var6 = var4;
         if (this.snapFromFling(var3, var1, var2)) {
            var6 = true;
         }

         return var6;
      }
   }

   void snapToTargetExistingView() {
      if (this.mRecyclerView != null) {
         RecyclerView.LayoutManager var1 = this.mRecyclerView.getLayoutManager();
         if (var1 != null) {
            View var2 = this.findSnapView(var1);
            if (var2 != null) {
               int[] var3 = this.calculateDistanceToFinalSnap(var1, var2);
               if (var3[0] != 0 || var3[1] != 0) {
                  this.mRecyclerView.smoothScrollBy(var3[0], var3[1]);
               }

            }
         }
      }
   }
}
