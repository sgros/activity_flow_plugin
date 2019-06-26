package android.support.v7.widget;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class LinearSnapHelper extends SnapHelper {
   private static final float INVALID_DISTANCE = 1.0F;
   @Nullable
   private OrientationHelper mHorizontalHelper;
   @Nullable
   private OrientationHelper mVerticalHelper;

   private float computeDistancePerChild(RecyclerView.LayoutManager var1, OrientationHelper var2) {
      int var3 = var1.getChildCount();
      if (var3 == 0) {
         return 1.0F;
      } else {
         int var4 = 0;
         View var5 = null;
         int var6 = Integer.MAX_VALUE;
         int var7 = Integer.MIN_VALUE;

         View var8;
         int var12;
         int var13;
         for(var8 = null; var4 < var3; var7 = var12) {
            View var9 = var1.getChildAt(var4);
            int var10 = var1.getPosition(var9);
            View var11;
            if (var10 == -1) {
               var11 = var5;
               var12 = var7;
            } else {
               var13 = var6;
               if (var10 < var6) {
                  var5 = var9;
                  var13 = var10;
               }

               var11 = var5;
               var6 = var13;
               var12 = var7;
               if (var10 > var7) {
                  var12 = var10;
                  var8 = var9;
                  var6 = var13;
                  var11 = var5;
               }
            }

            ++var4;
            var5 = var11;
         }

         if (var5 != null && var8 != null) {
            var13 = Math.min(var2.getDecoratedStart(var5), var2.getDecoratedStart(var8));
            var13 = Math.max(var2.getDecoratedEnd(var5), var2.getDecoratedEnd(var8)) - var13;
            if (var13 == 0) {
               return 1.0F;
            } else {
               return 1.0F * (float)var13 / (float)(var7 - var6 + 1);
            }
         } else {
            return 1.0F;
         }
      }
   }

   private int distanceToCenter(@NonNull RecyclerView.LayoutManager var1, @NonNull View var2, OrientationHelper var3) {
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

   private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager var1, OrientationHelper var2, int var3, int var4) {
      int[] var5 = this.calculateScrollDistance(var3, var4);
      float var6 = this.computeDistancePerChild(var1, var2);
      if (var6 <= 0.0F) {
         return 0;
      } else {
         if (Math.abs(var5[0]) > Math.abs(var5[1])) {
            var3 = var5[0];
         } else {
            var3 = var5[1];
         }

         return Math.round((float)var3 / var6);
      }
   }

   @Nullable
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

   @NonNull
   private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager var1) {
      if (this.mHorizontalHelper == null || this.mHorizontalHelper.mLayoutManager != var1) {
         this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(var1);
      }

      return this.mHorizontalHelper;
   }

   @NonNull
   private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager var1) {
      if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != var1) {
         this.mVerticalHelper = OrientationHelper.createVerticalHelper(var1);
      }

      return this.mVerticalHelper;
   }

   public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager var1, @NonNull View var2) {
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

   public View findSnapView(RecyclerView.LayoutManager var1) {
      if (var1.canScrollVertically()) {
         return this.findCenterView(var1, this.getVerticalHelper(var1));
      } else {
         return var1.canScrollHorizontally() ? this.findCenterView(var1, this.getHorizontalHelper(var1)) : null;
      }
   }

   public int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3) {
      if (!(var1 instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
         return -1;
      } else {
         int var4 = var1.getItemCount();
         if (var4 == 0) {
            return -1;
         } else {
            View var5 = this.findSnapView(var1);
            if (var5 == null) {
               return -1;
            } else {
               int var6 = var1.getPosition(var5);
               if (var6 == -1) {
                  return -1;
               } else {
                  RecyclerView.SmoothScroller.ScrollVectorProvider var9 = (RecyclerView.SmoothScroller.ScrollVectorProvider)var1;
                  int var7 = var4 - 1;
                  PointF var10 = var9.computeScrollVectorForPosition(var7);
                  if (var10 == null) {
                     return -1;
                  } else {
                     int var8;
                     if (var1.canScrollHorizontally()) {
                        var8 = this.estimateNextPositionDiffForFling(var1, this.getHorizontalHelper(var1), var2, 0);
                        var2 = var8;
                        if (var10.x < 0.0F) {
                           var2 = -var8;
                        }
                     } else {
                        var2 = 0;
                     }

                     if (var1.canScrollVertically()) {
                        var8 = this.estimateNextPositionDiffForFling(var1, this.getVerticalHelper(var1), 0, var3);
                        var3 = var8;
                        if (var10.y < 0.0F) {
                           var3 = -var8;
                        }
                     } else {
                        var3 = 0;
                     }

                     if (var1.canScrollVertically()) {
                        var2 = var3;
                     }

                     if (var2 == 0) {
                        return -1;
                     } else {
                        var3 = var6 + var2;
                        var2 = var3;
                        if (var3 < 0) {
                           var2 = 0;
                        }

                        var3 = var2;
                        if (var2 >= var4) {
                           var3 = var7;
                        }

                        return var3;
                     }
                  }
               }
            }
         }
      }
   }
}
