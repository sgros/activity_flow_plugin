package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class LinearSmoothScroller extends RecyclerView.SmoothScroller {
   private final float MILLISECONDS_PER_PX;
   protected final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
   protected int mInterimTargetDx = 0;
   protected int mInterimTargetDy = 0;
   protected final LinearInterpolator mLinearInterpolator = new LinearInterpolator();
   protected PointF mTargetVector;

   public LinearSmoothScroller(Context var1) {
      this.MILLISECONDS_PER_PX = this.calculateSpeedPerPixel(var1.getResources().getDisplayMetrics());
   }

   private int clampApplyScroll(int var1, int var2) {
      var2 = var1 - var2;
      return var1 * var2 <= 0 ? 0 : var2;
   }

   public int calculateDtToFit(int var1, int var2, int var3, int var4, int var5) {
      switch(var5) {
      case -1:
         return var3 - var1;
      case 0:
         var1 = var3 - var1;
         if (var1 > 0) {
            return var1;
         } else {
            var1 = var4 - var2;
            if (var1 < 0) {
               return var1;
            }

            return 0;
         }
      case 1:
         return var4 - var2;
      default:
         throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
      }
   }

   public int calculateDxToMakeVisible(View var1, int var2) {
      RecyclerView.LayoutManager var3 = this.getLayoutManager();
      if (var3 != null && var3.canScrollHorizontally()) {
         RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         return this.calculateDtToFit(var3.getDecoratedLeft(var1) - var4.leftMargin, var3.getDecoratedRight(var1) + var4.rightMargin, var3.getPaddingLeft(), var3.getWidth() - var3.getPaddingRight(), var2);
      } else {
         return 0;
      }
   }

   public int calculateDyToMakeVisible(View var1, int var2) {
      RecyclerView.LayoutManager var3 = this.getLayoutManager();
      if (var3 != null && var3.canScrollVertically()) {
         RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         return this.calculateDtToFit(var3.getDecoratedTop(var1) - var4.topMargin, var3.getDecoratedBottom(var1) + var4.bottomMargin, var3.getPaddingTop(), var3.getHeight() - var3.getPaddingBottom(), var2);
      } else {
         return 0;
      }
   }

   protected float calculateSpeedPerPixel(DisplayMetrics var1) {
      return 25.0F / (float)var1.densityDpi;
   }

   protected int calculateTimeForDeceleration(int var1) {
      return (int)Math.ceil((double)this.calculateTimeForScrolling(var1) / 0.3356D);
   }

   protected int calculateTimeForScrolling(int var1) {
      return (int)Math.ceil((double)((float)Math.abs(var1) * this.MILLISECONDS_PER_PX));
   }

   protected int getHorizontalSnapPreference() {
      byte var1;
      if (this.mTargetVector != null && this.mTargetVector.x != 0.0F) {
         if (this.mTargetVector.x > 0.0F) {
            var1 = 1;
         } else {
            var1 = -1;
         }
      } else {
         var1 = 0;
      }

      return var1;
   }

   protected int getVerticalSnapPreference() {
      byte var1;
      if (this.mTargetVector != null && this.mTargetVector.y != 0.0F) {
         if (this.mTargetVector.y > 0.0F) {
            var1 = 1;
         } else {
            var1 = -1;
         }
      } else {
         var1 = 0;
      }

      return var1;
   }

   protected void onSeekTargetStep(int var1, int var2, RecyclerView.State var3, RecyclerView.SmoothScroller.Action var4) {
      if (this.getChildCount() == 0) {
         this.stop();
      } else {
         this.mInterimTargetDx = this.clampApplyScroll(this.mInterimTargetDx, var1);
         this.mInterimTargetDy = this.clampApplyScroll(this.mInterimTargetDy, var2);
         if (this.mInterimTargetDx == 0 && this.mInterimTargetDy == 0) {
            this.updateActionForInterimTarget(var4);
         }

      }
   }

   protected void onStart() {
   }

   protected void onStop() {
      this.mInterimTargetDy = 0;
      this.mInterimTargetDx = 0;
      this.mTargetVector = null;
   }

   protected void onTargetFound(View var1, RecyclerView.State var2, RecyclerView.SmoothScroller.Action var3) {
      int var4 = this.calculateDxToMakeVisible(var1, this.getHorizontalSnapPreference());
      int var5 = this.calculateDyToMakeVisible(var1, this.getVerticalSnapPreference());
      int var6 = this.calculateTimeForDeceleration((int)Math.sqrt((double)(var4 * var4 + var5 * var5)));
      if (var6 > 0) {
         var3.update(-var4, -var5, var6, this.mDecelerateInterpolator);
      }

   }

   protected void updateActionForInterimTarget(RecyclerView.SmoothScroller.Action var1) {
      PointF var2 = this.computeScrollVectorForPosition(this.getTargetPosition());
      if (var2 == null || var2.x == 0.0F && var2.y == 0.0F) {
         var1.jumpTo(this.getTargetPosition());
         this.stop();
      } else {
         this.normalize(var2);
         this.mTargetVector = var2;
         this.mInterimTargetDx = (int)(var2.x * 10000.0F);
         this.mInterimTargetDy = (int)(var2.y * 10000.0F);
         int var3 = this.calculateTimeForScrolling(10000);
         var1.update((int)((float)this.mInterimTargetDx * 1.2F), (int)((float)this.mInterimTargetDy * 1.2F), (int)((float)var3 * 1.2F), this.mLinearInterpolator);
      }
   }
}