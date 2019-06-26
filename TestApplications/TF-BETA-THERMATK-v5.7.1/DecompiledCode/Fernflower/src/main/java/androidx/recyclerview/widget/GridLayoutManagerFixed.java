package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.ArrayList;
import java.util.Arrays;

public class GridLayoutManagerFixed extends GridLayoutManager {
   private ArrayList additionalViews = new ArrayList(4);
   private boolean canScrollVertically = true;

   public GridLayoutManagerFixed(Context var1, int var2) {
      super(var1, var2);
   }

   public GridLayoutManagerFixed(Context var1, int var2, int var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   protected int[] calculateItemBorders(int[] var1, int var2, int var3) {
      int var4;
      int[] var5;
      label22: {
         var4 = 1;
         if (var1 != null && var1.length == var2 + 1) {
            var5 = var1;
            if (var1[var1.length - 1] == var3) {
               break label22;
            }
         }

         var5 = new int[var2 + 1];
      }

      for(var5[0] = 0; var4 <= var2; ++var4) {
         var5[var4] = (int)Math.ceil((double)((float)var4 / (float)var2 * (float)var3));
      }

      return var5;
   }

   public boolean canScrollVertically() {
      return this.canScrollVertically;
   }

   protected boolean hasSiblingChild(int var1) {
      throw null;
   }

   void layoutChunk(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.LayoutState var3, LinearLayoutManager.LayoutChunkResult var4) {
      int var5 = super.mOrientationHelper.getModeInOther();
      boolean var6;
      if (var3.mItemDirection == 1) {
         var6 = true;
      } else {
         var6 = false;
      }

      var4.mConsumed = 0;
      int var7 = var3.mCurrentPosition;
      int var8;
      int var9;
      View var10;
      int var11;
      if (var3.mLayoutDirection != -1 && this.hasSiblingChild(var7) && this.findViewByPosition(var3.mCurrentPosition + 1) == null) {
         if (this.hasSiblingChild(var3.mCurrentPosition + 1)) {
            var3.mCurrentPosition += 3;
         } else {
            var3.mCurrentPosition += 2;
         }

         var8 = var3.mCurrentPosition;

         for(var9 = var8; var9 > var7; --var9) {
            var10 = var3.next(var1);
            this.additionalViews.add(var10);
            if (var9 != var8) {
               this.calculateItemDecorationsForChild(var10, super.mDecorInsets);
               this.measureChild(var10, var5, false);
               var11 = super.mOrientationHelper.getDecoratedMeasurement(var10);
               var3.mOffset -= var11;
               var3.mAvailable += var11;
            }
         }

         var3.mCurrentPosition = var8;
      }

      boolean var27 = true;

      while(true) {
         RecyclerView.Recycler var12 = var1;
         if (!var27) {
            return;
         }

         var9 = super.mSpanCount;
         boolean var13 = this.additionalViews.isEmpty();
         var8 = var3.mCurrentPosition;
         boolean var26 = var13 ^ true;
         var7 = 0;

         int var14;
         int var15;
         while(var7 < super.mSpanCount && var3.hasMore(var2) && var9 > 0) {
            var14 = var3.mCurrentPosition;
            var11 = var9 - this.getSpanSize(var12, var2, var14);
            if (var11 < 0) {
               break;
            }

            if (!this.additionalViews.isEmpty()) {
               var10 = (View)this.additionalViews.get(0);
               this.additionalViews.remove(0);
               --var3.mCurrentPosition;
            } else {
               var10 = var3.next(var12);
            }

            if (var10 == null) {
               break;
            }

            super.mSet[var7] = var10;
            var15 = var7 + 1;
            var9 = var11;
            var7 = var15;
            if (var3.mLayoutDirection == -1) {
               var9 = var11;
               var7 = var15;
               if (var11 <= 0) {
                  var9 = var11;
                  var7 = var15;
                  if (this.hasSiblingChild(var14)) {
                     var26 = true;
                     var9 = var11;
                     var7 = var15;
                  }
               }
            }
         }

         if (var7 == 0) {
            var4.mFinished = true;
            return;
         }

         this.assignSpans(var12, var2, var7, var6);
         var15 = 0;
         float var16 = 0.0F;

         GridLayoutManager.LayoutParams var29;
         for(var9 = 0; var15 < var7; var9 = var11) {
            var10 = super.mSet[var15];
            if (var3.mScrapList == null) {
               if (var6) {
                  this.addView(var10);
               } else {
                  this.addView(var10, 0);
               }
            } else if (var6) {
               this.addDisappearingView(var10);
            } else {
               this.addDisappearingView(var10, 0);
            }

            this.calculateItemDecorationsForChild(var10, super.mDecorInsets);
            this.measureChild(var10, var5, false);
            var14 = super.mOrientationHelper.getDecoratedMeasurement(var10);
            var11 = var9;
            if (var14 > var9) {
               var11 = var14;
            }

            var29 = (GridLayoutManager.LayoutParams)var10.getLayoutParams();
            float var17 = (float)super.mOrientationHelper.getDecoratedMeasurementInOther(var10) * 1.0F / (float)var29.mSpanSize;
            float var18 = var16;
            if (var17 > var16) {
               var18 = var17;
            }

            ++var15;
            var16 = var18;
         }

         int var22;
         int var23;
         int var24;
         for(var11 = 0; var11 < var7; ++var11) {
            var10 = super.mSet[var11];
            if (super.mOrientationHelper.getDecoratedMeasurement(var10) != var9) {
               var29 = (GridLayoutManager.LayoutParams)var10.getLayoutParams();
               Rect var19 = var29.mDecorInsets;
               int var20 = var19.top;
               int var21 = var19.bottom;
               var22 = var29.topMargin;
               var14 = var29.bottomMargin;
               var23 = var19.left;
               var24 = var19.right;
               int var25 = var29.leftMargin;
               var15 = var29.rightMargin;
               this.measureChildWithDecorationsAndMargin(var10, RecyclerView.LayoutManager.getChildMeasureSpec(super.mCachedBorders[var29.mSpanSize], 1073741824, var23 + var24 + var25 + var15, var29.width, false), MeasureSpec.makeMeasureSpec(var9 - (var20 + var21 + var22 + var14), 1073741824), true);
            }
         }

         var13 = this.shouldLayoutChildFromOpositeSide(super.mSet[0]);
         if (var13 && var3.mLayoutDirection == -1 || !var13 && var3.mLayoutDirection == 1) {
            if (var3.mLayoutDirection == -1) {
               var11 = var3.mOffset - var4.mConsumed;
               var14 = var11;
               var15 = var11 - var9;
               var11 = 0;
            } else {
               var14 = var3.mOffset + var4.mConsumed;
               var11 = this.getWidth();
               var15 = var14;
               var14 += var9;
            }

            var24 = var7 - 1;
            var7 = var11;

            while(true) {
               if (var24 < 0) {
                  var7 = var9;
                  break;
               }

               var10 = super.mSet[var24];
               var29 = (GridLayoutManager.LayoutParams)var10.getLayoutParams();
               var23 = super.mOrientationHelper.getDecoratedMeasurementInOther(var10);
               var11 = var7;
               if (var3.mLayoutDirection == 1) {
                  var11 = var7 - var23;
               }

               var7 = var11 + var23;
               this.layoutDecoratedWithMargins(var10, var11, var15, var7, var14);
               if (var3.mLayoutDirection != -1) {
                  var7 = var11;
               }

               if (var29.isItemRemoved() || var29.isItemChanged()) {
                  var4.mIgnoreConsumed = true;
               }

               var4.mFocusable |= var10.hasFocusable();
               --var24;
            }
         } else {
            var11 = var9;
            if (var3.mLayoutDirection == -1) {
               var14 = var3.mOffset - var4.mConsumed;
               var9 = this.getWidth();
               var15 = var14;
               var14 -= var11;
            } else {
               var9 = var3.mOffset + var4.mConsumed;
               var14 = var9;
               var15 = var9 + var11;
               var9 = 0;
            }

            var23 = 0;
            var24 = var7;

            while(true) {
               var7 = var11;
               if (var23 >= var24) {
                  break;
               }

               View var30 = super.mSet[var23];
               GridLayoutManager.LayoutParams var28 = (GridLayoutManager.LayoutParams)var30.getLayoutParams();
               var22 = super.mOrientationHelper.getDecoratedMeasurementInOther(var30);
               var7 = var9;
               if (var3.mLayoutDirection == -1) {
                  var7 = var9 - var22;
               }

               var9 = var7 + var22;
               this.layoutDecoratedWithMargins(var30, var7, var14, var9, var15);
               if (var3.mLayoutDirection == -1) {
                  var9 = var7;
               }

               if (var28.isItemRemoved() || var28.isItemChanged()) {
                  var4.mIgnoreConsumed = true;
               }

               var4.mFocusable |= var30.hasFocusable();
               ++var23;
            }
         }

         var4.mConsumed += var7;
         Arrays.fill(super.mSet, (Object)null);
         var27 = var26;
      }
   }

   protected void measureChild(View var1, int var2, boolean var3) {
      GridLayoutManager.LayoutParams var4 = (GridLayoutManager.LayoutParams)var1.getLayoutParams();
      Rect var5 = var4.mDecorInsets;
      int var6 = var5.top;
      int var7 = var5.bottom;
      int var8 = var4.topMargin;
      int var9 = var4.bottomMargin;
      int var10 = var5.left;
      int var11 = var5.right;
      int var12 = var4.leftMargin;
      int var13 = var4.rightMargin;
      this.measureChildWithDecorationsAndMargin(var1, RecyclerView.LayoutManager.getChildMeasureSpec(super.mCachedBorders[var4.mSpanSize], var2, var10 + var11 + var12 + var13, var4.width, false), RecyclerView.LayoutManager.getChildMeasureSpec(super.mOrientationHelper.getTotalSpace(), this.getHeightMode(), var6 + var7 + var8 + var9, var4.height, true), var3);
   }

   protected void recycleViewsFromStart(RecyclerView.Recycler var1, int var2, int var3) {
      if (var2 >= 0) {
         int var4 = this.getChildCount();
         View var5;
         if (!super.mShouldReverseLayout) {
            for(var3 = 0; var3 < var4; ++var3) {
               var5 = this.getChildAt(var3);
               if (super.mOrientationHelper.getDecoratedEnd(var5) > var2 || super.mOrientationHelper.getTransformedEndWithDecoration(var5) > var2) {
                  this.recycleChildren(var1, 0, var3);
                  break;
               }
            }
         } else {
            --var4;

            for(var3 = var4; var3 >= 0; --var3) {
               var5 = this.getChildAt(var3);
               RecyclerView.LayoutParams var6 = (RecyclerView.LayoutParams)var5.getLayoutParams();
               if (var5.getBottom() + var6.bottomMargin > var2 || var5.getTop() + var5.getHeight() > var2) {
                  this.recycleChildren(var1, var4, var3);
                  return;
               }
            }
         }

      }
   }

   public void setCanScrollVertically(boolean var1) {
      this.canScrollVertically = var1;
   }

   public boolean shouldLayoutChildFromOpositeSide(View var1) {
      throw null;
   }
}
