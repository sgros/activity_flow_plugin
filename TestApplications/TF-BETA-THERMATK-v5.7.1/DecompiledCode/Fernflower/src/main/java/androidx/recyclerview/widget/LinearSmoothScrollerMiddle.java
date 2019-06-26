package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class LinearSmoothScrollerMiddle extends RecyclerView.SmoothScroller {
   private final float MILLISECONDS_PER_PX;
   protected final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(1.5F);
   protected int mInterimTargetDx = 0;
   protected int mInterimTargetDy = 0;
   protected final LinearInterpolator mLinearInterpolator = new LinearInterpolator();
   protected PointF mTargetVector;

   public LinearSmoothScrollerMiddle(Context var1) {
      this.MILLISECONDS_PER_PX = 25.0F / (float)var1.getResources().getDisplayMetrics().densityDpi;
   }

   private int clampApplyScroll(int var1, int var2) {
      var2 = var1 - var2;
      return var1 * var2 <= 0 ? 0 : var2;
   }

   public int calculateDyToMakeVisible(View var1) {
      RecyclerView.LayoutManager var2 = this.getLayoutManager();
      if (var2 != null && var2.canScrollVertically()) {
         RecyclerView.LayoutParams var3 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         int var4 = var2.getDecoratedTop(var1) - var3.topMargin;
         int var5 = var2.getDecoratedBottom(var1) + var3.bottomMargin;
         int var6 = var2.getPaddingTop();
         var6 = var2.getHeight() - var2.getPaddingBottom() - var6;
         int var7 = var5 - var4;
         if (var7 > var6) {
            var6 = 0;
         } else {
            var6 = (var6 - var7) / 2;
         }

         var4 = var6 - var4;
         if (var4 > 0) {
            return var4;
         }

         var6 = var7 + var6 - var5;
         if (var6 < 0) {
            return var6;
         }
      }

      return 0;
   }

   protected int calculateTimeForDeceleration(int var1) {
      double var2 = (double)this.calculateTimeForScrolling(var1);
      Double.isNaN(var2);
      return (int)Math.ceil(var2 / 0.3356D);
   }

   protected int calculateTimeForScrolling(int var1) {
      return (int)Math.ceil((double)((float)Math.abs(var1) * this.MILLISECONDS_PER_PX));
   }

   public PointF computeScrollVectorForPosition(int var1) {
      RecyclerView.LayoutManager var2 = this.getLayoutManager();
      return var2 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider ? ((RecyclerView.SmoothScroller.ScrollVectorProvider)var2).computeScrollVectorForPosition(var1) : null;
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
      int var4 = this.calculateDyToMakeVisible(var1);
      int var5 = this.calculateTimeForDeceleration(var4);
      if (var5 > 0) {
         var3.update(0, -var4, Math.max(400, var5), this.mDecelerateInterpolator);
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
