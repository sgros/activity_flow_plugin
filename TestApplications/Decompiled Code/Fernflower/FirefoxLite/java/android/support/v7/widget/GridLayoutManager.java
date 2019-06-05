package android.support.v7.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
   int[] mCachedBorders;
   final Rect mDecorInsets = new Rect();
   boolean mPendingSpanCountChange = false;
   final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
   final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
   View[] mSet;
   int mSpanCount = -1;
   GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.DefaultSpanSizeLookup();

   public GridLayoutManager(Context var1, int var2) {
      super(var1);
      this.setSpanCount(var2);
   }

   public GridLayoutManager(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.setSpanCount(getProperties(var1, var2, var3, var4).spanCount);
   }

   private void assignSpans(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4, boolean var5) {
      var4 = -1;
      int var6 = 0;
      byte var7;
      if (var5) {
         var4 = var3;
         var3 = 0;
         var7 = 1;
      } else {
         --var3;
         var7 = -1;
      }

      while(var3 != var4) {
         View var8 = this.mSet[var3];
         GridLayoutManager.LayoutParams var9 = (GridLayoutManager.LayoutParams)var8.getLayoutParams();
         var9.mSpanSize = this.getSpanSize(var1, var2, this.getPosition(var8));
         var9.mSpanIndex = var6;
         var6 += var9.mSpanSize;
         var3 += var7;
      }

   }

   private void cachePreLayoutSpanMapping() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         GridLayoutManager.LayoutParams var3 = (GridLayoutManager.LayoutParams)this.getChildAt(var2).getLayoutParams();
         int var4 = var3.getViewLayoutPosition();
         this.mPreLayoutSpanSizeCache.put(var4, var3.getSpanSize());
         this.mPreLayoutSpanIndexCache.put(var4, var3.getSpanIndex());
      }

   }

   private void calculateItemBorders(int var1) {
      this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, var1);
   }

   static int[] calculateItemBorders(int[] var0, int var1, int var2) {
      int var3;
      int[] var4;
      label29: {
         var3 = 1;
         if (var0 != null && var0.length == var1 + 1) {
            var4 = var0;
            if (var0[var0.length - 1] == var2) {
               break label29;
            }
         }

         var4 = new int[var1 + 1];
      }

      byte var5 = 0;
      var4[0] = 0;
      int var6 = var2 / var1;
      int var7 = var2 % var1;
      int var8 = 0;

      for(var2 = var5; var3 <= var1; ++var3) {
         var2 += var7;
         int var9;
         if (var2 > 0 && var1 - var2 < var7) {
            var9 = var6 + 1;
            var2 -= var1;
         } else {
            var9 = var6;
         }

         var8 += var9;
         var4[var3] = var8;
      }

      return var4;
   }

   private void clearPreLayoutSpanMappingCache() {
      this.mPreLayoutSpanSizeCache.clear();
      this.mPreLayoutSpanIndexCache.clear();
   }

   private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3, int var4) {
      boolean var5;
      if (var4 == 1) {
         var5 = true;
      } else {
         var5 = false;
      }

      var4 = this.getSpanIndex(var1, var2, var3.mPosition);
      if (var5) {
         while(var4 > 0 && var3.mPosition > 0) {
            --var3.mPosition;
            var4 = this.getSpanIndex(var1, var2, var3.mPosition);
         }
      } else {
         int var6 = var2.getItemCount();

         int var8;
         int var9;
         for(var9 = var3.mPosition; var9 < var6 - 1; var4 = var8) {
            int var7 = var9 + 1;
            var8 = this.getSpanIndex(var1, var2, var7);
            if (var8 <= var4) {
               break;
            }

            var9 = var7;
         }

         var3.mPosition = var9;
      }

   }

   private void ensureViewSet() {
      if (this.mSet == null || this.mSet.length != this.mSpanCount) {
         this.mSet = new View[this.mSpanCount];
      }

   }

   private int getSpanGroupIndex(RecyclerView.Recycler var1, RecyclerView.State var2, int var3) {
      if (!var2.isPreLayout()) {
         return this.mSpanSizeLookup.getSpanGroupIndex(var3, this.mSpanCount);
      } else {
         int var4 = var1.convertPreLayoutPositionToPostLayout(var3);
         if (var4 == -1) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Cannot find span size for pre layout position. ");
            var5.append(var3);
            Log.w("GridLayoutManager", var5.toString());
            return 0;
         } else {
            return this.mSpanSizeLookup.getSpanGroupIndex(var4, this.mSpanCount);
         }
      }
   }

   private int getSpanIndex(RecyclerView.Recycler var1, RecyclerView.State var2, int var3) {
      if (!var2.isPreLayout()) {
         return this.mSpanSizeLookup.getCachedSpanIndex(var3, this.mSpanCount);
      } else {
         int var4 = this.mPreLayoutSpanIndexCache.get(var3, -1);
         if (var4 != -1) {
            return var4;
         } else {
            var4 = var1.convertPreLayoutPositionToPostLayout(var3);
            if (var4 == -1) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
               var5.append(var3);
               Log.w("GridLayoutManager", var5.toString());
               return 0;
            } else {
               return this.mSpanSizeLookup.getCachedSpanIndex(var4, this.mSpanCount);
            }
         }
      }
   }

   private int getSpanSize(RecyclerView.Recycler var1, RecyclerView.State var2, int var3) {
      if (!var2.isPreLayout()) {
         return this.mSpanSizeLookup.getSpanSize(var3);
      } else {
         int var4 = this.mPreLayoutSpanSizeCache.get(var3, -1);
         if (var4 != -1) {
            return var4;
         } else {
            var4 = var1.convertPreLayoutPositionToPostLayout(var3);
            if (var4 == -1) {
               StringBuilder var5 = new StringBuilder();
               var5.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
               var5.append(var3);
               Log.w("GridLayoutManager", var5.toString());
               return 1;
            } else {
               return this.mSpanSizeLookup.getSpanSize(var4);
            }
         }
      }
   }

   private void guessMeasurement(float var1, int var2) {
      this.calculateItemBorders(Math.max(Math.round(var1 * (float)this.mSpanCount), var2));
   }

   private void measureChild(View var1, int var2, boolean var3) {
      GridLayoutManager.LayoutParams var4 = (GridLayoutManager.LayoutParams)var1.getLayoutParams();
      Rect var5 = var4.mDecorInsets;
      int var6 = var5.top + var5.bottom + var4.topMargin + var4.bottomMargin;
      int var7 = var5.left + var5.right + var4.leftMargin + var4.rightMargin;
      int var8 = this.getSpaceForSpanRange(var4.mSpanIndex, var4.mSpanSize);
      if (this.mOrientation == 1) {
         var7 = getChildMeasureSpec(var8, var2, var7, var4.width, false);
         var2 = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getHeightMode(), var6, var4.height, true);
      } else {
         var2 = getChildMeasureSpec(var8, var2, var6, var4.height, false);
         var7 = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), this.getWidthMode(), var7, var4.width, true);
      }

      this.measureChildWithDecorationsAndMargin(var1, var7, var2, var3);
   }

   private void measureChildWithDecorationsAndMargin(View var1, int var2, int var3, boolean var4) {
      RecyclerView.LayoutParams var5 = (RecyclerView.LayoutParams)var1.getLayoutParams();
      if (var4) {
         var4 = this.shouldReMeasureChild(var1, var2, var3, var5);
      } else {
         var4 = this.shouldMeasureChild(var1, var2, var3, var5);
      }

      if (var4) {
         var1.measure(var2, var3);
      }

   }

   private void updateMeasurements() {
      int var1;
      if (this.getOrientation() == 1) {
         var1 = this.getWidth() - this.getPaddingRight() - this.getPaddingLeft();
      } else {
         var1 = this.getHeight() - this.getPaddingBottom() - this.getPaddingTop();
      }

      this.calculateItemBorders(var1);
   }

   public boolean checkLayoutParams(RecyclerView.LayoutParams var1) {
      return var1 instanceof GridLayoutManager.LayoutParams;
   }

   void collectPrefetchPositionsForLayoutState(RecyclerView.State var1, LinearLayoutManager.LayoutState var2, RecyclerView.LayoutManager.LayoutPrefetchRegistry var3) {
      int var4 = this.mSpanCount;

      for(int var5 = 0; var5 < this.mSpanCount && var2.hasMore(var1) && var4 > 0; ++var5) {
         int var6 = var2.mCurrentPosition;
         var3.addPosition(var6, Math.max(0, var2.mScrollingOffset));
         var4 -= this.mSpanSizeLookup.getSpanSize(var6);
         var2.mCurrentPosition += var2.mItemDirection;
      }

   }

   View findReferenceChild(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4, int var5) {
      this.ensureLayoutState();
      int var6 = this.mOrientationHelper.getStartAfterPadding();
      int var7 = this.mOrientationHelper.getEndAfterPadding();
      byte var8;
      if (var4 > var3) {
         var8 = 1;
      } else {
         var8 = -1;
      }

      View var9 = null;

      View var10;
      View var14;
      for(var10 = null; var3 != var4; var10 = var14) {
         View var11 = this.getChildAt(var3);
         int var12 = this.getPosition(var11);
         View var13 = var9;
         var14 = var10;
         if (var12 >= 0) {
            var13 = var9;
            var14 = var10;
            if (var12 < var5) {
               if (this.getSpanIndex(var1, var2, var12) != 0) {
                  var13 = var9;
                  var14 = var10;
               } else if (((RecyclerView.LayoutParams)var11.getLayoutParams()).isItemRemoved()) {
                  var13 = var9;
                  var14 = var10;
                  if (var10 == null) {
                     var14 = var11;
                     var13 = var9;
                  }
               } else {
                  if (this.mOrientationHelper.getDecoratedStart(var11) < var7 && this.mOrientationHelper.getDecoratedEnd(var11) >= var6) {
                     return var11;
                  }

                  var13 = var9;
                  var14 = var10;
                  if (var9 == null) {
                     var13 = var11;
                     var14 = var10;
                  }
               }
            }
         }

         var3 += var8;
         var9 = var13;
      }

      if (var9 != null) {
         var10 = var9;
      }

      return var10;
   }

   public RecyclerView.LayoutParams generateDefaultLayoutParams() {
      return this.mOrientation == 0 ? new GridLayoutManager.LayoutParams(-2, -1) : new GridLayoutManager.LayoutParams(-1, -2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(Context var1, AttributeSet var2) {
      return new GridLayoutManager.LayoutParams(var1, var2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof MarginLayoutParams ? new GridLayoutManager.LayoutParams((MarginLayoutParams)var1) : new GridLayoutManager.LayoutParams(var1);
   }

   public int getColumnCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      if (this.mOrientation == 1) {
         return this.mSpanCount;
      } else {
         return var2.getItemCount() < 1 ? 0 : this.getSpanGroupIndex(var1, var2, var2.getItemCount() - 1) + 1;
      }
   }

   public int getRowCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      if (this.mOrientation == 0) {
         return this.mSpanCount;
      } else {
         return var2.getItemCount() < 1 ? 0 : this.getSpanGroupIndex(var1, var2, var2.getItemCount() - 1) + 1;
      }
   }

   int getSpaceForSpanRange(int var1, int var2) {
      return this.mOrientation == 1 && this.isLayoutRTL() ? this.mCachedBorders[this.mSpanCount - var1] - this.mCachedBorders[this.mSpanCount - var1 - var2] : this.mCachedBorders[var2 + var1] - this.mCachedBorders[var1];
   }

   public int getSpanCount() {
      return this.mSpanCount;
   }

   void layoutChunk(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.LayoutState var3, LinearLayoutManager.LayoutChunkResult var4) {
      int var5 = this.mOrientationHelper.getModeInOther();
      boolean var6;
      if (var5 != 1073741824) {
         var6 = true;
      } else {
         var6 = false;
      }

      int var7;
      if (this.getChildCount() > 0) {
         var7 = this.mCachedBorders[this.mSpanCount];
      } else {
         var7 = 0;
      }

      if (var6) {
         this.updateMeasurements();
      }

      boolean var8;
      if (var3.mItemDirection == 1) {
         var8 = true;
      } else {
         var8 = false;
      }

      int var9 = this.mSpanCount;
      if (!var8) {
         var9 = this.getSpanIndex(var1, var2, var3.mCurrentPosition) + this.getSpanSize(var1, var2, var3.mCurrentPosition);
      }

      int var10 = 0;

      int var11;
      int var12;
      int var13;
      View var14;
      for(var11 = 0; var11 < this.mSpanCount && var3.hasMore(var2) && var9 > 0; ++var11) {
         var12 = var3.mCurrentPosition;
         var13 = this.getSpanSize(var1, var2, var12);
         if (var13 > this.mSpanCount) {
            StringBuilder var18 = new StringBuilder();
            var18.append("Item at position ");
            var18.append(var12);
            var18.append(" requires ");
            var18.append(var13);
            var18.append(" spans but GridLayoutManager has only ");
            var18.append(this.mSpanCount);
            var18.append(" spans.");
            throw new IllegalArgumentException(var18.toString());
         }

         var9 -= var13;
         if (var9 < 0) {
            break;
         }

         var14 = var3.next(var1);
         if (var14 == null) {
            break;
         }

         var10 += var13;
         this.mSet[var11] = var14;
      }

      if (var11 == 0) {
         var4.mFinished = true;
      } else {
         float var15 = 0.0F;
         this.assignSpans(var1, var2, var11, var10, var8);
         var13 = 0;

         float var17;
         GridLayoutManager.LayoutParams var19;
         for(var9 = 0; var13 < var11; var15 = var17) {
            View var21 = this.mSet[var13];
            if (var3.mScrapList == null) {
               if (var8) {
                  this.addView(var21);
               } else {
                  this.addView(var21, 0);
               }
            } else if (var8) {
               this.addDisappearingView(var21);
            } else {
               this.addDisappearingView(var21, 0);
            }

            this.calculateItemDecorationsForChild(var21, this.mDecorInsets);
            this.measureChild(var21, var5, false);
            var12 = this.mOrientationHelper.getDecoratedMeasurement(var21);
            var10 = var9;
            if (var12 > var9) {
               var10 = var12;
            }

            var19 = (GridLayoutManager.LayoutParams)var21.getLayoutParams();
            float var16 = (float)this.mOrientationHelper.getDecoratedMeasurementInOther(var21) * 1.0F / (float)var19.mSpanSize;
            var17 = var15;
            if (var16 > var15) {
               var17 = var16;
            }

            ++var13;
            var9 = var10;
         }

         var10 = var9;
         View var20;
         int var24;
         if (var6) {
            this.guessMeasurement(var15, var7);
            var24 = 0;
            var9 = 0;

            while(true) {
               var10 = var9;
               if (var24 >= var11) {
                  break;
               }

               var20 = this.mSet[var24];
               this.measureChild(var20, 1073741824, true);
               var7 = this.mOrientationHelper.getDecoratedMeasurement(var20);
               var10 = var9;
               if (var7 > var9) {
                  var10 = var7;
               }

               ++var24;
               var9 = var10;
            }
         }

         for(var9 = 0; var9 < var11; ++var9) {
            var14 = this.mSet[var9];
            if (this.mOrientationHelper.getDecoratedMeasurement(var14) != var10) {
               var19 = (GridLayoutManager.LayoutParams)var14.getLayoutParams();
               Rect var22 = var19.mDecorInsets;
               var7 = var22.top + var22.bottom + var19.topMargin + var19.bottomMargin;
               var24 = var22.left + var22.right + var19.leftMargin + var19.rightMargin;
               var13 = this.getSpaceForSpanRange(var19.mSpanIndex, var19.mSpanSize);
               if (this.mOrientation == 1) {
                  var24 = getChildMeasureSpec(var13, 1073741824, var24, var19.width, false);
                  var7 = MeasureSpec.makeMeasureSpec(var10 - var7, 1073741824);
               } else {
                  var24 = MeasureSpec.makeMeasureSpec(var10 - var24, 1073741824);
                  var7 = getChildMeasureSpec(var13, 1073741824, var7, var19.height, false);
               }

               this.measureChildWithDecorationsAndMargin(var14, var24, var7, true);
            }
         }

         var13 = 0;
         var4.mConsumed = var10;
         if (this.mOrientation == 1) {
            if (var3.mLayoutDirection == -1) {
               var24 = var3.mOffset;
               var9 = var24;
               var24 -= var10;
               var10 = var9;
               var9 = var24;
            } else {
               var24 = var3.mOffset;
               var9 = var24;
               var10 += var24;
            }

            var24 = 0;
            var7 = 0;
         } else if (var3.mLayoutDirection == -1) {
            var24 = var3.mOffset;
            var9 = 0;
            byte var25 = 0;
            var7 = var24;
            var24 -= var10;
            var10 = var25;
         } else {
            var24 = var3.mOffset;
            var7 = var10 + var24;
            var9 = 0;
            var10 = 0;
         }

         while(var13 < var11) {
            GridLayoutManager.LayoutParams var23;
            label114: {
               var20 = this.mSet[var13];
               var23 = (GridLayoutManager.LayoutParams)var20.getLayoutParams();
               if (this.mOrientation == 1) {
                  if (this.isLayoutRTL()) {
                     var7 = this.getPaddingLeft() + this.mCachedBorders[this.mSpanCount - var23.mSpanIndex];
                     var12 = this.mOrientationHelper.getDecoratedMeasurementInOther(var20);
                     var24 = var7;
                     var7 -= var12;
                     break label114;
                  }

                  var24 = this.getPaddingLeft() + this.mCachedBorders[var23.mSpanIndex];
                  var7 = this.mOrientationHelper.getDecoratedMeasurementInOther(var20) + var24;
               } else {
                  var9 = this.getPaddingTop() + this.mCachedBorders[var23.mSpanIndex];
                  var10 = this.mOrientationHelper.getDecoratedMeasurementInOther(var20) + var9;
               }

               var12 = var24;
               var24 = var7;
               var7 = var12;
            }

            this.layoutDecoratedWithMargins(var20, var7, var9, var24, var10);
            if (var23.isItemRemoved() || var23.isItemChanged()) {
               var4.mIgnoreConsumed = true;
            }

            var4.mFocusable |= var20.hasFocusable();
            var12 = var13 + 1;
            var13 = var24;
            var24 = var7;
            var7 = var13;
            var13 = var12;
         }

         Arrays.fill(this.mSet, (Object)null);
      }
   }

   void onAnchorReady(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3, int var4) {
      super.onAnchorReady(var1, var2, var3, var4);
      this.updateMeasurements();
      if (var2.getItemCount() > 0 && !var2.isPreLayout()) {
         this.ensureAnchorIsInCorrectSpan(var1, var2, var3, var4);
      }

      this.ensureViewSet();
   }

   public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
      View var5 = this.findContainingItemView(var1);
      View var6 = null;
      if (var5 == null) {
         return null;
      } else {
         GridLayoutManager.LayoutParams var7 = (GridLayoutManager.LayoutParams)var5.getLayoutParams();
         int var8 = var7.mSpanIndex;
         int var9 = var7.mSpanIndex + var7.mSpanSize;
         if (super.onFocusSearchFailed(var1, var2, var3, var4) == null) {
            return null;
         } else {
            boolean var10;
            if (this.convertFocusDirectionToLayoutDirection(var2) == 1) {
               var10 = true;
            } else {
               var10 = false;
            }

            boolean var25;
            if (var10 != this.mShouldReverseLayout) {
               var25 = true;
            } else {
               var25 = false;
            }

            int var11;
            int var12;
            byte var13;
            if (var25) {
               var11 = this.getChildCount() - 1;
               var12 = -1;
               var13 = -1;
            } else {
               var12 = this.getChildCount();
               var11 = 0;
               var13 = 1;
            }

            boolean var14;
            if (this.mOrientation == 1 && this.isLayoutRTL()) {
               var14 = true;
            } else {
               var14 = false;
            }

            int var15 = this.getSpanGroupIndex(var3, var4, var11);
            var1 = null;
            byte var16 = -1;
            int var17 = 0;
            var2 = 0;
            int var18 = -1;
            int var19 = var12;

            for(var12 = var16; var11 != var19; var11 += var13) {
               int var27 = this.getSpanGroupIndex(var3, var4, var11);
               View var26 = this.getChildAt(var11);
               if (var26 == var5) {
                  break;
               }

               if (var26.hasFocusable() && var27 != var15) {
                  if (var6 != null) {
                     break;
                  }
               } else {
                  GridLayoutManager.LayoutParams var20 = (GridLayoutManager.LayoutParams)var26.getLayoutParams();
                  int var21 = var20.mSpanIndex;
                  int var22 = var20.mSpanIndex + var20.mSpanSize;
                  if (var26.hasFocusable() && var21 == var8 && var22 == var9) {
                     return var26;
                  }

                  boolean var28;
                  label140: {
                     if ((!var26.hasFocusable() || var6 != null) && (var26.hasFocusable() || var1 != null)) {
                        label137: {
                           var27 = Math.max(var21, var8);
                           int var23 = Math.min(var22, var9) - var27;
                           if (var26.hasFocusable()) {
                              if (var23 > var17) {
                                 break label137;
                              }

                              if (var23 == var17) {
                                 if (var21 > var12) {
                                    var28 = true;
                                 } else {
                                    var28 = false;
                                 }

                                 if (var14 == var28) {
                                    break label137;
                                 }
                              }
                           } else if (var6 == null) {
                              boolean var24 = false;
                              if (this.isViewPartiallyVisible(var26, false, true)) {
                                 if (var23 > var2) {
                                    break label137;
                                 }

                                 if (var23 == var2) {
                                    var28 = var24;
                                    if (var21 > var18) {
                                       var28 = true;
                                    }

                                    if (var14 == var28) {
                                       break label137;
                                    }
                                 }
                              }
                           }

                           var28 = false;
                           break label140;
                        }
                     }

                     var28 = true;
                  }

                  if (var28) {
                     if (var26.hasFocusable()) {
                        var12 = var20.mSpanIndex;
                        var17 = Math.min(var22, var9) - Math.max(var21, var8);
                        var6 = var26;
                     } else {
                        var18 = var20.mSpanIndex;
                        var2 = Math.min(var22, var9);
                        var27 = Math.max(var21, var8);
                        var1 = var26;
                        var2 -= var27;
                     }
                  }
               }
            }

            if (var6 != null) {
               var1 = var6;
            }

            return var1;
         }
      }
   }

   public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, AccessibilityNodeInfoCompat var4) {
      android.view.ViewGroup.LayoutParams var5 = var3.getLayoutParams();
      if (!(var5 instanceof GridLayoutManager.LayoutParams)) {
         super.onInitializeAccessibilityNodeInfoForItem(var3, var4);
      } else {
         GridLayoutManager.LayoutParams var10 = (GridLayoutManager.LayoutParams)var5;
         int var6 = this.getSpanGroupIndex(var1, var2, var10.getViewLayoutPosition());
         int var7;
         int var8;
         boolean var9;
         if (this.mOrientation == 0) {
            var7 = var10.getSpanIndex();
            var8 = var10.getSpanSize();
            if (this.mSpanCount > 1 && var10.getSpanSize() == this.mSpanCount) {
               var9 = true;
            } else {
               var9 = false;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var7, var8, var6, 1, var9, false));
         } else {
            var7 = var10.getSpanIndex();
            var8 = var10.getSpanSize();
            if (this.mSpanCount > 1 && var10.getSpanSize() == this.mSpanCount) {
               var9 = true;
            } else {
               var9 = false;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var6, 1, var7, var8, var9, false));
         }

      }
   }

   public void onItemsAdded(RecyclerView var1, int var2, int var3) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsChanged(RecyclerView var1) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsMoved(RecyclerView var1, int var2, int var3, int var4) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsRemoved(RecyclerView var1, int var2, int var3) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onItemsUpdated(RecyclerView var1, int var2, int var3, Object var4) {
      this.mSpanSizeLookup.invalidateSpanIndexCache();
   }

   public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
      if (var2.isPreLayout()) {
         this.cachePreLayoutSpanMapping();
      }

      super.onLayoutChildren(var1, var2);
      this.clearPreLayoutSpanMappingCache();
   }

   public void onLayoutCompleted(RecyclerView.State var1) {
      super.onLayoutCompleted(var1);
      this.mPendingSpanCountChange = false;
   }

   public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      this.updateMeasurements();
      this.ensureViewSet();
      return super.scrollHorizontallyBy(var1, var2, var3);
   }

   public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      this.updateMeasurements();
      this.ensureViewSet();
      return super.scrollVerticallyBy(var1, var2, var3);
   }

   public void setMeasuredDimension(Rect var1, int var2, int var3) {
      if (this.mCachedBorders == null) {
         super.setMeasuredDimension(var1, var2, var3);
      }

      int var4 = this.getPaddingLeft() + this.getPaddingRight();
      int var5 = this.getPaddingTop() + this.getPaddingBottom();
      if (this.mOrientation == 1) {
         var3 = chooseSize(var3, var1.height() + var5, this.getMinimumHeight());
         var2 = chooseSize(var2, this.mCachedBorders[this.mCachedBorders.length - 1] + var4, this.getMinimumWidth());
      } else {
         var2 = chooseSize(var2, var1.width() + var4, this.getMinimumWidth());
         var3 = chooseSize(var3, this.mCachedBorders[this.mCachedBorders.length - 1] + var5, this.getMinimumHeight());
      }

      this.setMeasuredDimension(var2, var3);
   }

   public void setSpanCount(int var1) {
      if (var1 != this.mSpanCount) {
         this.mPendingSpanCountChange = true;
         if (var1 >= 1) {
            this.mSpanCount = var1;
            this.mSpanSizeLookup.invalidateSpanIndexCache();
            this.requestLayout();
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Span count should be at least 1. Provided ");
            var2.append(var1);
            throw new IllegalArgumentException(var2.toString());
         }
      }
   }

   public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup var1) {
      this.mSpanSizeLookup = var1;
   }

   public void setStackFromEnd(boolean var1) {
      if (!var1) {
         super.setStackFromEnd(false);
      } else {
         throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
      }
   }

   public boolean supportsPredictiveItemAnimations() {
      boolean var1;
      if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static final class DefaultSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
      public int getSpanIndex(int var1, int var2) {
         return var1 % var2;
      }

      public int getSpanSize(int var1) {
         return 1;
      }
   }

   public static class LayoutParams extends RecyclerView.LayoutParams {
      int mSpanIndex = -1;
      int mSpanSize = 0;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public int getSpanIndex() {
         return this.mSpanIndex;
      }

      public int getSpanSize() {
         return this.mSpanSize;
      }
   }

   public abstract static class SpanSizeLookup {
      private boolean mCacheSpanIndices = false;
      final SparseIntArray mSpanIndexCache = new SparseIntArray();

      int findReferenceIndexFromCache(int var1) {
         int var2 = this.mSpanIndexCache.size() - 1;
         int var3 = 0;

         while(var3 <= var2) {
            int var4 = var3 + var2 >>> 1;
            if (this.mSpanIndexCache.keyAt(var4) < var1) {
               var3 = var4 + 1;
            } else {
               var2 = var4 - 1;
            }
         }

         var1 = var3 - 1;
         if (var1 >= 0 && var1 < this.mSpanIndexCache.size()) {
            return this.mSpanIndexCache.keyAt(var1);
         } else {
            return -1;
         }
      }

      int getCachedSpanIndex(int var1, int var2) {
         if (!this.mCacheSpanIndices) {
            return this.getSpanIndex(var1, var2);
         } else {
            int var3 = this.mSpanIndexCache.get(var1, -1);
            if (var3 != -1) {
               return var3;
            } else {
               var2 = this.getSpanIndex(var1, var2);
               this.mSpanIndexCache.put(var1, var2);
               return var2;
            }
         }
      }

      public int getSpanGroupIndex(int var1, int var2) {
         int var3 = this.getSpanSize(var1);
         int var4 = 0;
         int var5 = 0;

         int var6;
         int var9;
         for(var6 = 0; var4 < var1; var6 = var9) {
            int var7 = this.getSpanSize(var4);
            int var8 = var5 + var7;
            if (var8 == var2) {
               var9 = var6 + 1;
               var5 = 0;
            } else {
               var5 = var8;
               var9 = var6;
               if (var8 > var2) {
                  var9 = var6 + 1;
                  var5 = var7;
               }
            }

            ++var4;
         }

         var1 = var6;
         if (var5 + var3 > var2) {
            var1 = var6 + 1;
         }

         return var1;
      }

      public int getSpanIndex(int var1, int var2) {
         int var3 = this.getSpanSize(var1);
         if (var3 == var2) {
            return 0;
         } else {
            int var4;
            int var5;
            label33: {
               if (this.mCacheSpanIndices && this.mSpanIndexCache.size() > 0) {
                  var4 = this.findReferenceIndexFromCache(var1);
                  if (var4 >= 0) {
                     var5 = this.mSpanIndexCache.get(var4) + this.getSpanSize(var4);
                     ++var4;
                     break label33;
                  }
               }

               var4 = 0;
            }

            for(var5 = 0; var4 < var1; ++var4) {
               int var6 = this.getSpanSize(var4);
               int var7 = var5 + var6;
               if (var7 == var2) {
                  var5 = 0;
               } else {
                  var5 = var7;
                  if (var7 > var2) {
                     var5 = var6;
                  }
               }
            }

            if (var3 + var5 <= var2) {
               return var5;
            } else {
               return 0;
            }
         }
      }

      public abstract int getSpanSize(int var1);

      public void invalidateSpanIndexCache() {
         this.mSpanIndexCache.clear();
      }
   }
}
