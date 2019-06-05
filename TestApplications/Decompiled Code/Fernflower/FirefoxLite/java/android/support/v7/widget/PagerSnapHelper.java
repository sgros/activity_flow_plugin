package android.support.v7.widget;

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;

public class PagerSnapHelper extends SnapHelper {
   private OrientationHelper mHorizontalHelper;
   private OrientationHelper mVerticalHelper;

   private int distanceToCenter(RecyclerView.LayoutManager var1, View var2, OrientationHelper var3) {
      int var4 = var3.getDecoratedStart(var2);
      int var5 = var3.getDecoratedMeasurement(var2) / 2;
      int var6;
      if (var1.getClipToPadding()) {
         var6 = var3.getStartAfterPadding() + var3.getTotalSpace() / 2;
      } else {
         var6 = var3.getEnd() / 2;
      }

      return var4 + var5 - var6;
   }

   private View findCenterView(RecyclerView.LayoutManager var1, OrientationHelper var2) {
      int var3 = var1.getChildCount();
      View var4 = null;
      if (var3 == 0) {
         return null;
      } else {
         int var5;
         if (var1.getClipToPadding()) {
            var5 = var2.getStartAfterPadding() + var2.getTotalSpace() / 2;
         } else {
            var5 = var2.getEnd() / 2;
         }

         int var6 = Integer.MAX_VALUE;

         int var10;
         for(int var7 = 0; var7 < var3; var6 = var10) {
            View var8 = var1.getChildAt(var7);
            int var9 = Math.abs(var2.getDecoratedStart(var8) + var2.getDecoratedMeasurement(var8) / 2 - var5);
            var10 = var6;
            if (var9 < var6) {
               var4 = var8;
               var10 = var9;
            }

            ++var7;
         }

         return var4;
      }
   }

   private View findStartView(RecyclerView.LayoutManager var1, OrientationHelper var2) {
      int var3 = var1.getChildCount();
      View var4 = null;
      if (var3 == 0) {
         return null;
      } else {
         int var5 = Integer.MAX_VALUE;

         int var9;
         for(int var6 = 0; var6 < var3; var5 = var9) {
            View var7 = var1.getChildAt(var6);
            int var8 = var2.getDecoratedStart(var7);
            var9 = var5;
            if (var8 < var5) {
               var4 = var7;
               var9 = var8;
            }

            ++var6;
         }

         return var4;
      }
   }

   private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager var1) {
      if (this.mHorizontalHelper == null || this.mHorizontalHelper.mLayoutManager != var1) {
         this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(var1);
      }

      return this.mHorizontalHelper;
   }

   private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager var1) {
      if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != var1) {
         this.mVerticalHelper = OrientationHelper.createVerticalHelper(var1);
      }

      return this.mVerticalHelper;
   }

   public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager var1, View var2) {
      int[] var3 = new int[2];
      if (var1.canScrollHorizontally()) {
         var3[0] = this.distanceToCenter(var1, var2, this.getHorizontalHelper(var1));
      } else {
         var3[0] = 0;
      }

      if (var1.canScrollVertically()) {
         var3[1] = this.distanceToCenter(var1, var2, this.getVerticalHelper(var1));
      } else {
         var3[1] = 0;
      }

      return var3;
   }

   protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager var1) {
      return !(var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
         protected float calculateSpeedPerPixel(DisplayMetrics var1) {
            return 100.0F / (float)var1.densityDpi;
         }

         protected int calculateTimeForScrolling(int var1) {
            return Math.min(100, super.calculateTimeForScrolling(var1));
         }

         protected void onTargetFound(View var1, RecyclerView.State var2, RecyclerView.SmoothScroller.Action var3) {
            int[] var7 = PagerSnapHelper.this.calculateDistanceToFinalSnap(PagerSnapHelper.this.mRecyclerView.getLayoutManager(), var1);
            int var4 = var7[0];
            int var5 = var7[1];
            int var6 = this.calculateTimeForDeceleration(Math.max(Math.abs(var4), Math.abs(var5)));
            if (var6 > 0) {
               var3.update(var4, var5, var6, this.mDecelerateInterpolator);
            }

         }
      };
   }

   public View findSnapView(RecyclerView.LayoutManager var1) {
      if (var1.canScrollVertically()) {
         return this.findCenterView(var1, this.getVerticalHelper(var1));
      } else {
         return var1.canScrollHorizontally() ? this.findCenterView(var1, this.getHorizontalHelper(var1)) : null;
      }
   }

   public int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3) {
      int var4 = var1.getItemCount();
      if (var4 == 0) {
         return -1;
      } else {
         View var5 = null;
         if (var1.canScrollVertically()) {
            var5 = this.findStartView(var1, this.getVerticalHelper(var1));
         } else if (var1.canScrollHorizontally()) {
            var5 = this.findStartView(var1, this.getHorizontalHelper(var1));
         }

         if (var5 == null) {
            return -1;
         } else {
            int var6 = var1.getPosition(var5);
            if (var6 == -1) {
               return -1;
            } else {
               boolean var8;
               boolean var11;
               label51: {
                  label50: {
                     boolean var7 = var1.canScrollHorizontally();
                     var8 = false;
                     if (var7) {
                        if (var2 > 0) {
                           break label50;
                        }
                     } else if (var3 > 0) {
                        break label50;
                     }

                     var11 = false;
                     break label51;
                  }

                  var11 = true;
               }

               boolean var10 = var8;
               if (var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
                  PointF var9 = ((RecyclerView.SmoothScroller.ScrollVectorProvider)var1).computeScrollVectorForPosition(var4 - 1);
                  var10 = var8;
                  if (var9 != null) {
                     label41: {
                        if (var9.x >= 0.0F) {
                           var10 = var8;
                           if (var9.y >= 0.0F) {
                              break label41;
                           }
                        }

                        var10 = true;
                     }
                  }
               }

               if (var10) {
                  var2 = var6;
                  if (var11) {
                     var2 = var6 - 1;
                  }
               } else {
                  var2 = var6;
                  if (var11) {
                     var2 = var6 + 1;
                  }
               }

               return var2;
            }
         }
      }
   }
}
