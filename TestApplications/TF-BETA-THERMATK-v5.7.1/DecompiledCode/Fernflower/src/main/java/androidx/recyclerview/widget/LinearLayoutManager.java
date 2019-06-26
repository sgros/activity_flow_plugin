package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class LinearLayoutManager extends RecyclerView.LayoutManager implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {
   static final boolean DEBUG = false;
   public static final int HORIZONTAL = 0;
   public static final int INVALID_OFFSET = Integer.MIN_VALUE;
   private static final float MAX_SCROLL_FACTOR = 0.33333334F;
   private static final String TAG = "LinearLayoutManager";
   public static final int VERTICAL = 1;
   final LinearLayoutManager.AnchorInfo mAnchorInfo;
   private int mInitialPrefetchItemCount;
   private boolean mLastStackFromEnd;
   private final LinearLayoutManager.LayoutChunkResult mLayoutChunkResult;
   private LinearLayoutManager.LayoutState mLayoutState;
   int mOrientation;
   OrientationHelper mOrientationHelper;
   LinearLayoutManager.SavedState mPendingSavedState;
   int mPendingScrollPosition;
   boolean mPendingScrollPositionBottom;
   int mPendingScrollPositionOffset;
   private boolean mRecycleChildrenOnDetach;
   private int[] mReusableIntPair;
   private boolean mReverseLayout;
   boolean mShouldReverseLayout;
   private boolean mSmoothScrollbarEnabled;
   private boolean mStackFromEnd;

   public LinearLayoutManager(Context var1) {
      this(var1, 1, false);
   }

   public LinearLayoutManager(Context var1, int var2, boolean var3) {
      this.mOrientation = 1;
      this.mReverseLayout = false;
      this.mShouldReverseLayout = false;
      this.mStackFromEnd = false;
      this.mSmoothScrollbarEnabled = true;
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionBottom = true;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.mPendingSavedState = null;
      this.mAnchorInfo = new LinearLayoutManager.AnchorInfo();
      this.mLayoutChunkResult = new LinearLayoutManager.LayoutChunkResult();
      this.mInitialPrefetchItemCount = 2;
      this.mReusableIntPair = new int[2];
      this.setOrientation(var2);
      this.setReverseLayout(var3);
   }

   private int computeScrollExtent(RecyclerView.State var1) {
      if (this.getChildCount() == 0) {
         return 0;
      } else {
         this.ensureLayoutState();
         return ScrollbarHelper.computeScrollExtent(var1, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
      }
   }

   private int computeScrollOffset(RecyclerView.State var1) {
      if (this.getChildCount() == 0) {
         return 0;
      } else {
         this.ensureLayoutState();
         return ScrollbarHelper.computeScrollOffset(var1, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
      }
   }

   private int computeScrollRange(RecyclerView.State var1) {
      if (this.getChildCount() == 0) {
         return 0;
      } else {
         this.ensureLayoutState();
         return ScrollbarHelper.computeScrollRange(var1, this.mOrientationHelper, this.findFirstVisibleChildClosestToStart(this.mSmoothScrollbarEnabled ^ true, true), this.findFirstVisibleChildClosestToEnd(this.mSmoothScrollbarEnabled ^ true, true), this, this.mSmoothScrollbarEnabled);
      }
   }

   private View findFirstPartiallyOrCompletelyInvisibleChild() {
      return this.findOnePartiallyOrCompletelyInvisibleChild(0, this.getChildCount());
   }

   private View findFirstReferenceChild(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.findReferenceChild(var1, var2, 0, this.getChildCount(), var2.getItemCount());
   }

   private View findLastPartiallyOrCompletelyInvisibleChild() {
      return this.findOnePartiallyOrCompletelyInvisibleChild(this.getChildCount() - 1, -1);
   }

   private View findLastReferenceChild(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.findReferenceChild(var1, var2, this.getChildCount() - 1, -1, var2.getItemCount());
   }

   private View findPartiallyOrCompletelyInvisibleChildClosestToEnd() {
      View var1;
      if (this.mShouldReverseLayout) {
         var1 = this.findFirstPartiallyOrCompletelyInvisibleChild();
      } else {
         var1 = this.findLastPartiallyOrCompletelyInvisibleChild();
      }

      return var1;
   }

   private View findPartiallyOrCompletelyInvisibleChildClosestToStart() {
      View var1;
      if (this.mShouldReverseLayout) {
         var1 = this.findLastPartiallyOrCompletelyInvisibleChild();
      } else {
         var1 = this.findFirstPartiallyOrCompletelyInvisibleChild();
      }

      return var1;
   }

   private View findReferenceChildClosestToEnd(RecyclerView.Recycler var1, RecyclerView.State var2) {
      View var3;
      if (this.mShouldReverseLayout) {
         var3 = this.findFirstReferenceChild(var1, var2);
      } else {
         var3 = this.findLastReferenceChild(var1, var2);
      }

      return var3;
   }

   private View findReferenceChildClosestToStart(RecyclerView.Recycler var1, RecyclerView.State var2) {
      View var3;
      if (this.mShouldReverseLayout) {
         var3 = this.findLastReferenceChild(var1, var2);
      } else {
         var3 = this.findFirstReferenceChild(var1, var2);
      }

      return var3;
   }

   private int fixLayoutEndGap(int var1, RecyclerView.Recycler var2, RecyclerView.State var3, boolean var4) {
      int var5 = this.mOrientationHelper.getEndAfterPadding() - var1;
      if (var5 > 0) {
         var5 = -this.scrollBy(-var5, var2, var3);
         if (var4) {
            var1 = this.mOrientationHelper.getEndAfterPadding() - (var1 + var5);
            if (var1 > 0) {
               this.mOrientationHelper.offsetChildren(var1);
               return var1 + var5;
            }
         }

         return var5;
      } else {
         return 0;
      }
   }

   private int fixLayoutStartGap(int var1, RecyclerView.Recycler var2, RecyclerView.State var3, boolean var4) {
      int var5 = var1 - this.mOrientationHelper.getStartAfterPadding();
      if (var5 > 0) {
         int var6 = -this.scrollBy(var5, var2, var3);
         var5 = var6;
         if (var4) {
            var1 = var1 + var6 - this.mOrientationHelper.getStartAfterPadding();
            var5 = var6;
            if (var1 > 0) {
               this.mOrientationHelper.offsetChildren(-var1);
               var5 = var6 - var1;
            }
         }

         return var5;
      } else {
         return 0;
      }
   }

   private View getChildClosestToEnd() {
      int var1;
      if (this.mShouldReverseLayout) {
         var1 = 0;
      } else {
         var1 = this.getChildCount() - 1;
      }

      return this.getChildAt(var1);
   }

   private View getChildClosestToStart() {
      int var1;
      if (this.mShouldReverseLayout) {
         var1 = this.getChildCount() - 1;
      } else {
         var1 = 0;
      }

      return this.getChildAt(var1);
   }

   private void layoutForPredictiveAnimations(RecyclerView.Recycler var1, RecyclerView.State var2, int var3, int var4) {
      if (var2.willRunPredictiveAnimations() && this.getChildCount() != 0 && !var2.isPreLayout() && this.supportsPredictiveItemAnimations()) {
         List var5 = var1.getScrapList();
         int var6 = var5.size();
         int var7 = this.getPosition(this.getChildAt(0));
         int var8 = 0;
         int var9 = 0;

         int var10;
         for(var10 = 0; var8 < var6; ++var8) {
            RecyclerView.ViewHolder var11 = (RecyclerView.ViewHolder)var5.get(var8);
            if (!var11.isRemoved()) {
               int var12 = var11.getLayoutPosition();
               byte var13 = 1;
               boolean var14;
               if (var12 < var7) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               if (var14 != this.mShouldReverseLayout) {
                  var13 = -1;
               }

               if (var13 == -1) {
                  var9 += this.mOrientationHelper.getDecoratedMeasurement(var11.itemView);
               } else {
                  var10 += this.mOrientationHelper.getDecoratedMeasurement(var11.itemView);
               }
            }
         }

         this.mLayoutState.mScrapList = var5;
         LinearLayoutManager.LayoutState var15;
         if (var9 > 0) {
            this.updateLayoutStateToFillStart(this.getPosition(this.getChildClosestToStart()), var3);
            var15 = this.mLayoutState;
            var15.mExtraFillSpace = var9;
            var15.mAvailable = 0;
            var15.assignPositionFromScrapList();
            this.fill(var1, this.mLayoutState, var2, false);
         }

         if (var10 > 0) {
            this.updateLayoutStateToFillEnd(this.getPosition(this.getChildClosestToEnd()), var4);
            var15 = this.mLayoutState;
            var15.mExtraFillSpace = var10;
            var15.mAvailable = 0;
            var15.assignPositionFromScrapList();
            this.fill(var1, this.mLayoutState, var2, false);
         }

         this.mLayoutState.mScrapList = null;
      }

   }

   private void logChildren() {
      Log.d("LinearLayoutManager", "internal representation of views on the screen");

      for(int var1 = 0; var1 < this.getChildCount(); ++var1) {
         View var2 = this.getChildAt(var1);
         StringBuilder var3 = new StringBuilder();
         var3.append("item ");
         var3.append(this.getPosition(var2));
         var3.append(", coord:");
         var3.append(this.mOrientationHelper.getDecoratedStart(var2));
         Log.d("LinearLayoutManager", var3.toString());
      }

      Log.d("LinearLayoutManager", "==============");
   }

   private void recycleByLayoutState(RecyclerView.Recycler var1, LinearLayoutManager.LayoutState var2) {
      if (var2.mRecycle && !var2.mInfinite) {
         int var3 = var2.mScrollingOffset;
         int var4 = var2.mNoRecycleSpace;
         if (var2.mLayoutDirection == -1) {
            this.recycleViewsFromEnd(var1, var3, var4);
         } else {
            this.recycleViewsFromStart(var1, var3, var4);
         }
      }

   }

   private void recycleViewsFromEnd(RecyclerView.Recycler var1, int var2, int var3) {
      int var4 = this.getChildCount();
      if (var2 >= 0) {
         int var5 = this.mOrientationHelper.getEnd() - var2 + var3;
         View var6;
         if (!this.mShouldReverseLayout) {
            var3 = var4 - 1;

            for(var2 = var3; var2 >= 0; --var2) {
               var6 = this.getChildAt(var2);
               if (this.mOrientationHelper.getDecoratedStart(var6) < var5 || this.mOrientationHelper.getTransformedStartWithDecoration(var6) < var5) {
                  this.recycleChildren(var1, var3, var2);
                  break;
               }
            }
         } else {
            for(var2 = 0; var2 < var4; ++var2) {
               var6 = this.getChildAt(var2);
               if (this.mOrientationHelper.getDecoratedStart(var6) < var5 || this.mOrientationHelper.getTransformedStartWithDecoration(var6) < var5) {
                  this.recycleChildren(var1, 0, var2);
                  return;
               }
            }
         }

      }
   }

   private void resolveShouldLayoutReverse() {
      if (this.mOrientation != 1 && this.isLayoutRTL()) {
         this.mShouldReverseLayout = this.mReverseLayout ^ true;
      } else {
         this.mShouldReverseLayout = this.mReverseLayout;
      }

   }

   private boolean updateAnchorFromChildren(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3) {
      int var4 = this.getChildCount();
      boolean var5 = false;
      if (var4 == 0) {
         return false;
      } else {
         View var6 = this.getFocusedChild();
         if (var6 != null && var3.isViewValidAsAnchor(var6, var2)) {
            var3.assignFromViewAndKeepVisibleRect(var6, this.getPosition(var6));
            return true;
         } else if (this.mLastStackFromEnd != this.mStackFromEnd) {
            return false;
         } else {
            View var7;
            if (var3.mLayoutFromEnd) {
               var7 = this.findReferenceChildClosestToEnd(var1, var2);
            } else {
               var7 = this.findReferenceChildClosestToStart(var1, var2);
            }

            if (var7 == null) {
               return false;
            } else {
               var3.assignFromView(var7, this.getPosition(var7));
               if (!var2.isPreLayout() && this.supportsPredictiveItemAnimations()) {
                  if (this.mOrientationHelper.getDecoratedStart(var7) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(var7) < this.mOrientationHelper.getStartAfterPadding()) {
                     var5 = true;
                  }

                  if (var5) {
                     int var8;
                     if (var3.mLayoutFromEnd) {
                        var8 = this.mOrientationHelper.getEndAfterPadding();
                     } else {
                        var8 = this.mOrientationHelper.getStartAfterPadding();
                     }

                     var3.mCoordinate = var8;
                  }
               }

               return true;
            }
         }
      }
   }

   private boolean updateAnchorFromPendingData(RecyclerView.State var1, LinearLayoutManager.AnchorInfo var2) {
      boolean var3 = var1.isPreLayout();
      boolean var4 = false;
      if (!var3) {
         int var5 = this.mPendingScrollPosition;
         if (var5 != -1) {
            if (var5 >= 0 && var5 < var1.getItemCount()) {
               var2.mPosition = this.mPendingScrollPosition;
               LinearLayoutManager.SavedState var6 = this.mPendingSavedState;
               if (var6 != null && var6.hasValidAnchor()) {
                  var2.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
                  if (var2.mLayoutFromEnd) {
                     var2.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
                  } else {
                     var2.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
                  }

                  return true;
               }

               if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
                  View var7 = this.findViewByPosition(this.mPendingScrollPosition);
                  if (var7 != null) {
                     if (this.mOrientationHelper.getDecoratedMeasurement(var7) > this.mOrientationHelper.getTotalSpace()) {
                        var2.assignCoordinateFromPadding();
                        return true;
                     }

                     if (this.mOrientationHelper.getDecoratedStart(var7) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                        var2.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                        var2.mLayoutFromEnd = false;
                        return true;
                     }

                     if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var7) < 0) {
                        var2.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                        var2.mLayoutFromEnd = true;
                        return true;
                     }

                     if (var2.mLayoutFromEnd) {
                        var5 = this.mOrientationHelper.getDecoratedEnd(var7) + this.mOrientationHelper.getTotalSpaceChange();
                     } else {
                        var5 = this.mOrientationHelper.getDecoratedStart(var7);
                     }

                     var2.mCoordinate = var5;
                  } else {
                     if (this.getChildCount() > 0) {
                        var5 = this.getPosition(this.getChildAt(0));
                        if (this.mPendingScrollPosition < var5) {
                           var3 = true;
                        } else {
                           var3 = false;
                        }

                        if (var3 == this.mPendingScrollPositionBottom) {
                           var4 = true;
                        }

                        var2.mLayoutFromEnd = var4;
                     }

                     var2.assignCoordinateFromPadding();
                  }

                  return true;
               }

               var3 = this.mPendingScrollPositionBottom;
               var2.mLayoutFromEnd = var3;
               if (var3) {
                  var2.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
               } else {
                  var2.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
               }

               return true;
            }

            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
         }
      }

      return false;
   }

   private void updateAnchorInfoForLayout(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3) {
      if (!this.updateAnchorFromPendingData(var2, var3)) {
         if (!this.updateAnchorFromChildren(var1, var2, var3)) {
            var3.assignCoordinateFromPadding();
            int var4;
            if (this.mStackFromEnd) {
               var4 = var2.getItemCount() - 1;
            } else {
               var4 = 0;
            }

            var3.mPosition = var4;
         }
      }
   }

   private void updateLayoutState(int var1, int var2, boolean var3, RecyclerView.State var4) {
      this.mLayoutState.mInfinite = this.resolveIsInfinite();
      this.mLayoutState.mLayoutDirection = var1;
      int[] var5 = this.mReusableIntPair;
      boolean var6 = false;
      var5[0] = 0;
      var5[1] = 0;
      this.calculateExtraLayoutSpace(var4, var5);
      int var7 = Math.max(0, this.mReusableIntPair[0]);
      int var8 = Math.max(0, this.mReusableIntPair[1]);
      if (var1 == 1) {
         var6 = true;
      }

      LinearLayoutManager.LayoutState var11 = this.mLayoutState;
      if (var6) {
         var1 = var8;
      } else {
         var1 = var7;
      }

      var11.mExtraFillSpace = var1;
      var11 = this.mLayoutState;
      if (!var6) {
         var7 = var8;
      }

      var11.mNoRecycleSpace = var7;
      byte var10 = -1;
      LinearLayoutManager.LayoutState var9;
      View var12;
      LinearLayoutManager.LayoutState var13;
      if (var6) {
         var11 = this.mLayoutState;
         var11.mExtraFillSpace += this.mOrientationHelper.getEndPadding();
         var12 = this.getChildClosestToEnd();
         var13 = this.mLayoutState;
         if (!this.mShouldReverseLayout) {
            var10 = 1;
         }

         var13.mItemDirection = var10;
         var9 = this.mLayoutState;
         var1 = this.getPosition(var12);
         var13 = this.mLayoutState;
         var9.mCurrentPosition = var1 + var13.mItemDirection;
         var13.mOffset = this.mOrientationHelper.getDecoratedEnd(var12);
         var1 = this.mOrientationHelper.getDecoratedEnd(var12) - this.mOrientationHelper.getEndAfterPadding();
      } else {
         var12 = this.getChildClosestToStart();
         var13 = this.mLayoutState;
         var13.mExtraFillSpace += this.mOrientationHelper.getStartAfterPadding();
         var13 = this.mLayoutState;
         if (this.mShouldReverseLayout) {
            var10 = 1;
         }

         var13.mItemDirection = var10;
         var9 = this.mLayoutState;
         var1 = this.getPosition(var12);
         var13 = this.mLayoutState;
         var9.mCurrentPosition = var1 + var13.mItemDirection;
         var13.mOffset = this.mOrientationHelper.getDecoratedStart(var12);
         var1 = -this.mOrientationHelper.getDecoratedStart(var12) + this.mOrientationHelper.getStartAfterPadding();
      }

      var11 = this.mLayoutState;
      var11.mAvailable = var2;
      if (var3) {
         var11.mAvailable -= var1;
      }

      this.mLayoutState.mScrollingOffset = var1;
   }

   private void updateLayoutStateToFillEnd(int var1, int var2) {
      this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - var2;
      LinearLayoutManager.LayoutState var3 = this.mLayoutState;
      byte var4;
      if (this.mShouldReverseLayout) {
         var4 = -1;
      } else {
         var4 = 1;
      }

      var3.mItemDirection = var4;
      var3 = this.mLayoutState;
      var3.mCurrentPosition = var1;
      var3.mLayoutDirection = 1;
      var3.mOffset = var2;
      var3.mScrollingOffset = Integer.MIN_VALUE;
   }

   private void updateLayoutStateToFillEnd(LinearLayoutManager.AnchorInfo var1) {
      this.updateLayoutStateToFillEnd(var1.mPosition, var1.mCoordinate);
   }

   private void updateLayoutStateToFillStart(int var1, int var2) {
      this.mLayoutState.mAvailable = var2 - this.mOrientationHelper.getStartAfterPadding();
      LinearLayoutManager.LayoutState var3 = this.mLayoutState;
      var3.mCurrentPosition = var1;
      byte var4;
      if (this.mShouldReverseLayout) {
         var4 = 1;
      } else {
         var4 = -1;
      }

      var3.mItemDirection = var4;
      var3 = this.mLayoutState;
      var3.mLayoutDirection = -1;
      var3.mOffset = var2;
      var3.mScrollingOffset = Integer.MIN_VALUE;
   }

   private void updateLayoutStateToFillStart(LinearLayoutManager.AnchorInfo var1) {
      this.updateLayoutStateToFillStart(var1.mPosition, var1.mCoordinate);
   }

   public void assertNotInLayoutOrScroll(String var1) {
      if (this.mPendingSavedState == null) {
         super.assertNotInLayoutOrScroll(var1);
      }

   }

   protected void calculateExtraLayoutSpace(RecyclerView.State var1, int[] var2) {
      int var3 = this.getExtraLayoutSpace(var1);
      int var4;
      if (this.mLayoutState.mLayoutDirection == -1) {
         var4 = 0;
      } else {
         var4 = var3;
         var3 = 0;
      }

      var2[0] = var3;
      var2[1] = var4;
   }

   public boolean canScrollHorizontally() {
      boolean var1;
      if (this.mOrientation == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean canScrollVertically() {
      int var1 = this.mOrientation;
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public void collectAdjacentPrefetchPositions(int var1, int var2, RecyclerView.State var3, RecyclerView.LayoutManager.LayoutPrefetchRegistry var4) {
      if (this.mOrientation != 0) {
         var1 = var2;
      }

      if (this.getChildCount() != 0 && var1 != 0) {
         this.ensureLayoutState();
         byte var5;
         if (var1 > 0) {
            var5 = 1;
         } else {
            var5 = -1;
         }

         this.updateLayoutState(var5, Math.abs(var1), true, var3);
         this.collectPrefetchPositionsForLayoutState(var3, this.mLayoutState, var4);
      }

   }

   public void collectInitialPrefetchPositions(int var1, RecyclerView.LayoutManager.LayoutPrefetchRegistry var2) {
      LinearLayoutManager.SavedState var3 = this.mPendingSavedState;
      byte var4 = -1;
      boolean var5;
      int var6;
      int var8;
      if (var3 != null && var3.hasValidAnchor()) {
         var3 = this.mPendingSavedState;
         var5 = var3.mAnchorLayoutFromEnd;
         var6 = var3.mAnchorPosition;
      } else {
         this.resolveShouldLayoutReverse();
         boolean var7 = this.mShouldReverseLayout;
         var8 = this.mPendingScrollPosition;
         var6 = var8;
         var5 = var7;
         if (var8 == -1) {
            if (var7) {
               var6 = var1 - 1;
               var5 = var7;
            } else {
               var6 = 0;
               var5 = var7;
            }
         }
      }

      if (!var5) {
         var4 = 1;
      }

      for(var8 = 0; var8 < this.mInitialPrefetchItemCount && var6 >= 0 && var6 < var1; ++var8) {
         var2.addPosition(var6, 0);
         var6 += var4;
      }

   }

   void collectPrefetchPositionsForLayoutState(RecyclerView.State var1, LinearLayoutManager.LayoutState var2, RecyclerView.LayoutManager.LayoutPrefetchRegistry var3) {
      int var4 = var2.mCurrentPosition;
      if (var4 >= 0 && var4 < var1.getItemCount()) {
         var3.addPosition(var4, Math.max(0, var2.mScrollingOffset));
      }

   }

   public int computeHorizontalScrollExtent(RecyclerView.State var1) {
      return this.computeScrollExtent(var1);
   }

   public int computeHorizontalScrollOffset(RecyclerView.State var1) {
      return this.computeScrollOffset(var1);
   }

   public int computeHorizontalScrollRange(RecyclerView.State var1) {
      return this.computeScrollRange(var1);
   }

   public PointF computeScrollVectorForPosition(int var1) {
      if (this.getChildCount() == 0) {
         return null;
      } else {
         boolean var2 = false;
         int var3 = this.getPosition(this.getChildAt(0));
         byte var4 = 1;
         if (var1 < var3) {
            var2 = true;
         }

         byte var5 = var4;
         if (var2 != this.mShouldReverseLayout) {
            var5 = -1;
         }

         return this.mOrientation == 0 ? new PointF((float)var5, 0.0F) : new PointF(0.0F, (float)var5);
      }
   }

   public int computeVerticalScrollExtent(RecyclerView.State var1) {
      return this.computeScrollExtent(var1);
   }

   public int computeVerticalScrollOffset(RecyclerView.State var1) {
      return this.computeScrollOffset(var1);
   }

   public int computeVerticalScrollRange(RecyclerView.State var1) {
      return this.computeScrollRange(var1);
   }

   int convertFocusDirectionToLayoutDirection(int var1) {
      int var2 = -1;
      byte var3 = 1;
      byte var4 = 1;
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 != 17) {
               if (var1 != 33) {
                  if (var1 != 66) {
                     if (var1 != 130) {
                        return Integer.MIN_VALUE;
                     } else {
                        if (this.mOrientation == 1) {
                           var1 = var4;
                        } else {
                           var1 = Integer.MIN_VALUE;
                        }

                        return var1;
                     }
                  } else {
                     if (this.mOrientation == 0) {
                        var1 = var3;
                     } else {
                        var1 = Integer.MIN_VALUE;
                     }

                     return var1;
                  }
               } else {
                  if (this.mOrientation != 1) {
                     var2 = Integer.MIN_VALUE;
                  }

                  return var2;
               }
            } else {
               if (this.mOrientation != 0) {
                  var2 = Integer.MIN_VALUE;
               }

               return var2;
            }
         } else if (this.mOrientation == 1) {
            return 1;
         } else {
            return this.isLayoutRTL() ? -1 : 1;
         }
      } else if (this.mOrientation == 1) {
         return -1;
      } else {
         return this.isLayoutRTL() ? 1 : -1;
      }
   }

   LinearLayoutManager.LayoutState createLayoutState() {
      return new LinearLayoutManager.LayoutState();
   }

   void ensureLayoutState() {
      if (this.mLayoutState == null) {
         this.mLayoutState = this.createLayoutState();
      }

   }

   int fill(RecyclerView.Recycler var1, LinearLayoutManager.LayoutState var2, RecyclerView.State var3, boolean var4) {
      int var5 = var2.mAvailable;
      int var6 = var2.mScrollingOffset;
      if (var6 != Integer.MIN_VALUE) {
         if (var5 < 0) {
            var2.mScrollingOffset = var6 + var5;
         }

         this.recycleByLayoutState(var1, var2);
      }

      int var7 = var2.mAvailable + var2.mExtraFillSpace;
      LinearLayoutManager.LayoutChunkResult var8 = this.mLayoutChunkResult;

      while((var2.mInfinite || var7 > 0) && var2.hasMore(var3)) {
         var8.resetInternal();
         this.layoutChunk(var1, var3, var2, var8);
         if (var8.mFinished) {
            break;
         }

         label45: {
            var2.mOffset += var8.mConsumed * var2.mLayoutDirection;
            if (var8.mIgnoreConsumed && var2.mScrapList == null) {
               var6 = var7;
               if (var3.isPreLayout()) {
                  break label45;
               }
            }

            int var9 = var2.mAvailable;
            var6 = var8.mConsumed;
            var2.mAvailable = var9 - var6;
            var6 = var7 - var6;
         }

         var7 = var2.mScrollingOffset;
         if (var7 != Integer.MIN_VALUE) {
            var2.mScrollingOffset = var7 + var8.mConsumed;
            var7 = var2.mAvailable;
            if (var7 < 0) {
               var2.mScrollingOffset += var7;
            }

            this.recycleByLayoutState(var1, var2);
         }

         var7 = var6;
         if (var4) {
            var7 = var6;
            if (var8.mFocusable) {
               break;
            }
         }
      }

      return var5 - var2.mAvailable;
   }

   public int findFirstCompletelyVisibleItemPosition() {
      View var1 = this.findOneVisibleChild(0, this.getChildCount(), true, false);
      int var2;
      if (var1 == null) {
         var2 = -1;
      } else {
         var2 = this.getPosition(var1);
      }

      return var2;
   }

   View findFirstVisibleChildClosestToEnd(boolean var1, boolean var2) {
      return this.mShouldReverseLayout ? this.findOneVisibleChild(0, this.getChildCount(), var1, var2) : this.findOneVisibleChild(this.getChildCount() - 1, -1, var1, var2);
   }

   View findFirstVisibleChildClosestToStart(boolean var1, boolean var2) {
      return this.mShouldReverseLayout ? this.findOneVisibleChild(this.getChildCount() - 1, -1, var1, var2) : this.findOneVisibleChild(0, this.getChildCount(), var1, var2);
   }

   public int findFirstVisibleItemPosition() {
      View var1 = this.findOneVisibleChild(0, this.getChildCount(), false, true);
      int var2;
      if (var1 == null) {
         var2 = -1;
      } else {
         var2 = this.getPosition(var1);
      }

      return var2;
   }

   public int findLastCompletelyVisibleItemPosition() {
      int var1 = this.getChildCount();
      int var2 = -1;
      View var3 = this.findOneVisibleChild(var1 - 1, -1, true, false);
      if (var3 != null) {
         var2 = this.getPosition(var3);
      }

      return var2;
   }

   public int findLastVisibleItemPosition() {
      int var1 = this.getChildCount();
      int var2 = -1;
      View var3 = this.findOneVisibleChild(var1 - 1, -1, false, true);
      if (var3 != null) {
         var2 = this.getPosition(var3);
      }

      return var2;
   }

   View findOnePartiallyOrCompletelyInvisibleChild(int var1, int var2) {
      this.ensureLayoutState();
      byte var3;
      if (var2 > var1) {
         var3 = 1;
      } else if (var2 < var1) {
         var3 = -1;
      } else {
         var3 = 0;
      }

      if (var3 == 0) {
         return this.getChildAt(var1);
      } else {
         short var4;
         short var6;
         if (this.mOrientationHelper.getDecoratedStart(this.getChildAt(var1)) < this.mOrientationHelper.getStartAfterPadding()) {
            var6 = 16644;
            var4 = 16388;
         } else {
            var6 = 4161;
            var4 = 4097;
         }

         View var5;
         if (this.mOrientation == 0) {
            var5 = super.mHorizontalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var6, var4);
         } else {
            var5 = super.mVerticalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var6, var4);
         }

         return var5;
      }
   }

   View findOneVisibleChild(int var1, int var2, boolean var3, boolean var4) {
      this.ensureLayoutState();
      short var5 = 320;
      short var6;
      if (var3) {
         var6 = 24579;
      } else {
         var6 = 320;
      }

      if (!var4) {
         var5 = 0;
      }

      View var7;
      if (this.mOrientation == 0) {
         var7 = super.mHorizontalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var6, var5);
      } else {
         var7 = super.mVerticalBoundCheck.findOneViewWithinBoundFlags(var1, var2, var6, var5);
      }

      return var7;
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

      View var14 = null;

      View var12;
      View var13;
      for(var13 = null; var3 != var4; var13 = var12) {
         View var9 = this.getChildAt(var3);
         int var10 = this.getPosition(var9);
         View var11 = var14;
         var12 = var13;
         if (var10 >= 0) {
            var11 = var14;
            var12 = var13;
            if (var10 < var5) {
               if (((RecyclerView.LayoutParams)var9.getLayoutParams()).isItemRemoved()) {
                  var11 = var14;
                  var12 = var13;
                  if (var13 == null) {
                     var12 = var9;
                     var11 = var14;
                  }
               } else {
                  if (this.mOrientationHelper.getDecoratedStart(var9) < var7 && this.mOrientationHelper.getDecoratedEnd(var9) >= var6) {
                     return var9;
                  }

                  var11 = var14;
                  var12 = var13;
                  if (var14 == null) {
                     var11 = var9;
                     var12 = var13;
                  }
               }
            }
         }

         var3 += var8;
         var14 = var11;
      }

      if (var14 == null) {
         var14 = var13;
      }

      return var14;
   }

   public View findViewByPosition(int var1) {
      int var2 = this.getChildCount();
      if (var2 == 0) {
         return null;
      } else {
         int var3 = var1 - this.getPosition(this.getChildAt(0));
         if (var3 >= 0 && var3 < var2) {
            View var4 = this.getChildAt(var3);
            if (this.getPosition(var4) == var1) {
               return var4;
            }
         }

         return super.findViewByPosition(var1);
      }
   }

   public RecyclerView.LayoutParams generateDefaultLayoutParams() {
      return new RecyclerView.LayoutParams(-2, -2);
   }

   @Deprecated
   protected int getExtraLayoutSpace(RecyclerView.State var1) {
      return var1.hasTargetScrollPosition() ? this.mOrientationHelper.getTotalSpace() : 0;
   }

   public int getInitialPrefetchItemCount() {
      return this.mInitialPrefetchItemCount;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public boolean getRecycleChildrenOnDetach() {
      return this.mRecycleChildrenOnDetach;
   }

   public boolean getReverseLayout() {
      return this.mReverseLayout;
   }

   public boolean getStackFromEnd() {
      return this.mStackFromEnd;
   }

   public boolean isAutoMeasureEnabled() {
      return true;
   }

   protected boolean isLayoutRTL() {
      int var1 = this.getLayoutDirection();
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public boolean isSmoothScrollbarEnabled() {
      return this.mSmoothScrollbarEnabled;
   }

   void layoutChunk(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.LayoutState var3, LinearLayoutManager.LayoutChunkResult var4) {
      View var14 = var3.next(var1);
      if (var14 == null) {
         var4.mFinished = true;
      } else {
         RecyclerView.LayoutParams var15 = (RecyclerView.LayoutParams)var14.getLayoutParams();
         boolean var5;
         boolean var6;
         if (var3.mScrapList == null) {
            var5 = this.mShouldReverseLayout;
            if (var3.mLayoutDirection == -1) {
               var6 = true;
            } else {
               var6 = false;
            }

            if (var5 == var6) {
               this.addView(var14);
            } else {
               this.addView(var14, 0);
            }
         } else {
            var5 = this.mShouldReverseLayout;
            if (var3.mLayoutDirection == -1) {
               var6 = true;
            } else {
               var6 = false;
            }

            if (var5 == var6) {
               this.addDisappearingView(var14);
            } else {
               this.addDisappearingView(var14, 0);
            }
         }

         this.measureChildWithMargins(var14, 0, 0);
         var4.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(var14);
         int var7;
         int var8;
         int var9;
         int var10;
         int var11;
         int var12;
         if (this.mOrientation == 1) {
            if (this.isLayoutRTL()) {
               var7 = this.getWidth() - this.getPaddingRight();
               var8 = var7 - this.mOrientationHelper.getDecoratedMeasurementInOther(var14);
            } else {
               var8 = this.getPaddingLeft();
               var7 = this.mOrientationHelper.getDecoratedMeasurementInOther(var14) + var8;
            }

            if (var3.mLayoutDirection == -1) {
               var9 = var3.mOffset;
               var10 = var4.mConsumed;
               var11 = var9;
               var12 = var7;
               var7 = var9 - var10;
            } else {
               var9 = var3.mOffset;
               var10 = var4.mConsumed;
               var11 = var9;
               var12 = var7;
               var9 += var10;
               var7 = var11;
               var11 = var9;
            }
         } else {
            var8 = this.getPaddingTop();
            var7 = this.mOrientationHelper.getDecoratedMeasurementInOther(var14) + var8;
            if (var3.mLayoutDirection == -1) {
               var10 = var3.mOffset;
               int var13 = var4.mConsumed;
               var12 = var10;
               var9 = var8;
               var11 = var7;
               var8 = var10 - var13;
               var7 = var9;
            } else {
               var9 = var3.mOffset;
               var12 = var4.mConsumed;
               var12 += var9;
               var11 = var7;
               var7 = var8;
               var8 = var9;
            }
         }

         this.layoutDecoratedWithMargins(var14, var8, var7, var12, var11);
         if (var15.isItemRemoved() || var15.isItemChanged()) {
            var4.mIgnoreConsumed = true;
         }

         var4.mFocusable = var14.hasFocusable();
      }
   }

   void onAnchorReady(RecyclerView.Recycler var1, RecyclerView.State var2, LinearLayoutManager.AnchorInfo var3, int var4) {
   }

   public void onDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
      super.onDetachedFromWindow(var1, var2);
      if (this.mRecycleChildrenOnDetach) {
         this.removeAndRecycleAllViews(var2);
         var2.clear();
      }

   }

   public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
      this.resolveShouldLayoutReverse();
      if (this.getChildCount() == 0) {
         return null;
      } else {
         var2 = this.convertFocusDirectionToLayoutDirection(var2);
         if (var2 == Integer.MIN_VALUE) {
            return null;
         } else {
            this.ensureLayoutState();
            this.updateLayoutState(var2, (int)((float)this.mOrientationHelper.getTotalSpace() * 0.33333334F), false, var4);
            LinearLayoutManager.LayoutState var5 = this.mLayoutState;
            var5.mScrollingOffset = Integer.MIN_VALUE;
            var5.mRecycle = false;
            this.fill(var3, var5, var4, true);
            if (var2 == -1) {
               var1 = this.findPartiallyOrCompletelyInvisibleChildClosestToStart();
            } else {
               var1 = this.findPartiallyOrCompletelyInvisibleChildClosestToEnd();
            }

            View var6;
            if (var2 == -1) {
               var6 = this.getChildClosestToStart();
            } else {
               var6 = this.getChildClosestToEnd();
            }

            if (var6.hasFocusable()) {
               return var1 == null ? null : var6;
            } else {
               return var1;
            }
         }
      }
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      if (this.getChildCount() > 0) {
         var1.setFromIndex(this.findFirstVisibleItemPosition());
         var1.setToIndex(this.findLastVisibleItemPosition());
      }

   }

   public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
      LinearLayoutManager.SavedState var3 = this.mPendingSavedState;
      byte var4 = -1;
      if ((var3 != null || this.mPendingScrollPosition != -1) && var2.getItemCount() == 0) {
         this.removeAndRecycleAllViews(var1);
      } else {
         var3 = this.mPendingSavedState;
         if (var3 != null && var3.hasValidAnchor()) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
         }

         this.ensureLayoutState();
         this.mLayoutState.mRecycle = false;
         this.resolveShouldLayoutReverse();
         View var10 = this.getFocusedChild();
         LinearLayoutManager.AnchorInfo var11;
         if (this.mAnchorInfo.mValid && this.mPendingScrollPosition == -1 && this.mPendingSavedState == null) {
            if (var10 != null && (this.mOrientationHelper.getDecoratedStart(var10) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(var10) <= this.mOrientationHelper.getStartAfterPadding())) {
               this.mAnchorInfo.assignFromViewAndKeepVisibleRect(var10, this.getPosition(var10));
            }
         } else {
            this.mAnchorInfo.reset();
            var11 = this.mAnchorInfo;
            var11.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
            this.updateAnchorInfoForLayout(var1, var2, var11);
            this.mAnchorInfo.mValid = true;
         }

         LinearLayoutManager.LayoutState var12 = this.mLayoutState;
         byte var5;
         if (var12.mLastScrollDelta >= 0) {
            var5 = 1;
         } else {
            var5 = -1;
         }

         var12.mLayoutDirection = var5;
         int[] var13 = this.mReusableIntPair;
         var13[0] = 0;
         var13[1] = 0;
         this.calculateExtraLayoutSpace(var2, var13);
         int var6 = Math.max(0, this.mReusableIntPair[0]) + this.mOrientationHelper.getStartAfterPadding();
         int var7 = Math.max(0, this.mReusableIntPair[1]) + this.mOrientationHelper.getEndPadding();
         int var15 = var6;
         int var8 = var7;
         int var9;
         if (var2.isPreLayout()) {
            var9 = this.mPendingScrollPosition;
            var15 = var6;
            var8 = var7;
            if (var9 != -1) {
               var15 = var6;
               var8 = var7;
               if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                  var10 = this.findViewByPosition(var9);
                  var15 = var6;
                  var8 = var7;
                  if (var10 != null) {
                     if (this.mPendingScrollPositionBottom) {
                        var8 = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var10);
                        var15 = this.mPendingScrollPositionOffset;
                     } else {
                        var15 = this.mOrientationHelper.getDecoratedStart(var10) - this.mOrientationHelper.getStartAfterPadding();
                        var8 = this.mPendingScrollPositionOffset;
                     }

                     var15 = var8 - var15;
                     if (var15 > 0) {
                        var15 += var6;
                        var8 = var7;
                     } else {
                        var8 = var7 - var15;
                        var15 = var6;
                     }
                  }
               }
            }
         }

         label85: {
            if (this.mAnchorInfo.mLayoutFromEnd) {
               if (!this.mShouldReverseLayout) {
                  break label85;
               }
            } else if (this.mShouldReverseLayout) {
               break label85;
            }

            var4 = 1;
         }

         this.onAnchorReady(var1, var2, this.mAnchorInfo, var4);
         this.detachAndScrapAttachedViews(var1);
         this.mLayoutState.mInfinite = this.resolveIsInfinite();
         this.mLayoutState.mIsPreLayout = var2.isPreLayout();
         this.mLayoutState.mNoRecycleSpace = 0;
         var11 = this.mAnchorInfo;
         int var14;
         if (var11.mLayoutFromEnd) {
            this.updateLayoutStateToFillStart(var11);
            var12 = this.mLayoutState;
            var12.mExtraFillSpace = var15;
            this.fill(var1, var12, var2, false);
            var12 = this.mLayoutState;
            var14 = var12.mOffset;
            var6 = var12.mCurrentPosition;
            var7 = var12.mAvailable;
            var15 = var8;
            if (var7 > 0) {
               var15 = var8 + var7;
            }

            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            var12 = this.mLayoutState;
            var12.mExtraFillSpace = var15;
            var12.mCurrentPosition += var12.mItemDirection;
            this.fill(var1, var12, var2, false);
            var12 = this.mLayoutState;
            var7 = var12.mOffset;
            var9 = var12.mAvailable;
            var8 = var14;
            var15 = var7;
            if (var9 > 0) {
               this.updateLayoutStateToFillStart(var6, var14);
               var12 = this.mLayoutState;
               var12.mExtraFillSpace = var9;
               this.fill(var1, var12, var2, false);
               var8 = this.mLayoutState.mOffset;
               var15 = var7;
            }
         } else {
            this.updateLayoutStateToFillEnd(var11);
            var12 = this.mLayoutState;
            var12.mExtraFillSpace = var8;
            this.fill(var1, var12, var2, false);
            var12 = this.mLayoutState;
            var14 = var12.mOffset;
            var6 = var12.mCurrentPosition;
            var7 = var12.mAvailable;
            var8 = var15;
            if (var7 > 0) {
               var8 = var15 + var7;
            }

            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            var12 = this.mLayoutState;
            var12.mExtraFillSpace = var8;
            var12.mCurrentPosition += var12.mItemDirection;
            this.fill(var1, var12, var2, false);
            var12 = this.mLayoutState;
            var7 = var12.mOffset;
            var9 = var12.mAvailable;
            var8 = var7;
            var15 = var14;
            if (var9 > 0) {
               this.updateLayoutStateToFillEnd(var6, var14);
               var12 = this.mLayoutState;
               var12.mExtraFillSpace = var9;
               this.fill(var1, var12, var2, false);
               var15 = this.mLayoutState.mOffset;
               var8 = var7;
            }
         }

         var7 = var8;
         var14 = var15;
         if (this.getChildCount() > 0) {
            if (this.mShouldReverseLayout ^ this.mStackFromEnd) {
               var7 = this.fixLayoutEndGap(var15, var1, var2, true);
               var14 = var8 + var7;
               var15 += var7;
               var8 = this.fixLayoutStartGap(var14, var1, var2, false);
            } else {
               var7 = this.fixLayoutStartGap(var8, var1, var2, true);
               var14 = var8 + var7;
               var15 += var7;
               var8 = this.fixLayoutEndGap(var15, var1, var2, false);
            }

            var7 = var14 + var8;
            var14 = var15 + var8;
         }

         this.layoutForPredictiveAnimations(var1, var2, var7, var14);
         if (!var2.isPreLayout()) {
            this.mOrientationHelper.onLayoutComplete();
         } else {
            this.mAnchorInfo.reset();
         }

         this.mLastStackFromEnd = this.mStackFromEnd;
      }
   }

   public void onLayoutCompleted(RecyclerView.State var1) {
      super.onLayoutCompleted(var1);
      this.mPendingSavedState = null;
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.mAnchorInfo.reset();
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof LinearLayoutManager.SavedState) {
         this.mPendingSavedState = (LinearLayoutManager.SavedState)var1;
         this.requestLayout();
      }

   }

   public Parcelable onSaveInstanceState() {
      LinearLayoutManager.SavedState var1 = this.mPendingSavedState;
      if (var1 != null) {
         return new LinearLayoutManager.SavedState(var1);
      } else {
         var1 = new LinearLayoutManager.SavedState();
         if (this.getChildCount() > 0) {
            this.ensureLayoutState();
            boolean var2 = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            var1.mAnchorLayoutFromEnd = var2;
            View var3;
            if (var2) {
               var3 = this.getChildClosestToEnd();
               var1.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var3);
               var1.mAnchorPosition = this.getPosition(var3);
            } else {
               var3 = this.getChildClosestToStart();
               var1.mAnchorPosition = this.getPosition(var3);
               var1.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(var3) - this.mOrientationHelper.getStartAfterPadding();
            }
         } else {
            var1.invalidateAnchor();
         }

         return var1;
      }
   }

   public void prepareForDrop(View var1, View var2, int var3, int var4) {
      this.assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
      this.ensureLayoutState();
      this.resolveShouldLayoutReverse();
      var3 = this.getPosition(var1);
      var4 = this.getPosition(var2);
      byte var5;
      if (var3 < var4) {
         var5 = 1;
      } else {
         var5 = -1;
      }

      if (this.mShouldReverseLayout) {
         if (var5 == 1) {
            this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(var2) + this.mOrientationHelper.getDecoratedMeasurement(var1)));
         } else {
            this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(var2));
         }
      } else if (var5 == -1) {
         this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getDecoratedStart(var2));
      } else {
         this.scrollToPositionWithOffset(var4, this.mOrientationHelper.getDecoratedEnd(var2) - this.mOrientationHelper.getDecoratedMeasurement(var1));
      }

   }

   protected void recycleChildren(RecyclerView.Recycler var1, int var2, int var3) {
      if (var2 != var3) {
         int var4 = var2;
         if (var3 > var2) {
            --var3;

            while(var3 >= var2) {
               this.removeAndRecycleViewAt(var3, var1);
               --var3;
            }
         } else {
            while(var4 > var3) {
               this.removeAndRecycleViewAt(var4, var1);
               --var4;
            }
         }

      }
   }

   protected void recycleViewsFromStart(RecyclerView.Recycler var1, int var2, int var3) {
      if (var2 >= 0) {
         int var4 = var2 - var3;
         var3 = this.getChildCount();
         View var5;
         if (!this.mShouldReverseLayout) {
            for(var2 = 0; var2 < var3; ++var2) {
               var5 = this.getChildAt(var2);
               if (this.mOrientationHelper.getDecoratedEnd(var5) > var4 || this.mOrientationHelper.getTransformedEndWithDecoration(var5) > var4) {
                  this.recycleChildren(var1, 0, var2);
                  break;
               }
            }
         } else {
            --var3;

            for(var2 = var3; var2 >= 0; --var2) {
               var5 = this.getChildAt(var2);
               if (this.mOrientationHelper.getDecoratedEnd(var5) > var4 || this.mOrientationHelper.getTransformedEndWithDecoration(var5) > var4) {
                  this.recycleChildren(var1, var3, var2);
                  return;
               }
            }
         }

      }
   }

   boolean resolveIsInfinite() {
      boolean var1;
      if (this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   int scrollBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      if (this.getChildCount() != 0 && var1 != 0) {
         this.ensureLayoutState();
         this.mLayoutState.mRecycle = true;
         byte var4;
         if (var1 > 0) {
            var4 = 1;
         } else {
            var4 = -1;
         }

         int var5 = Math.abs(var1);
         this.updateLayoutState(var4, var5, true, var3);
         LinearLayoutManager.LayoutState var6 = this.mLayoutState;
         int var7 = var6.mScrollingOffset + this.fill(var2, var6, var3, false);
         if (var7 < 0) {
            return 0;
         } else {
            if (var5 > var7) {
               var1 = var4 * var7;
            }

            this.mOrientationHelper.offsetChildren(-var1);
            this.mLayoutState.mLastScrollDelta = var1;
            return var1;
         }
      } else {
         return 0;
      }
   }

   public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.mOrientation == 1 ? 0 : this.scrollBy(var1, var2, var3);
   }

   public void scrollToPosition(int var1) {
      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      LinearLayoutManager.SavedState var2 = this.mPendingSavedState;
      if (var2 != null) {
         var2.invalidateAnchor();
      }

      this.requestLayout();
   }

   public void scrollToPositionWithOffset(int var1, int var2) {
      this.scrollToPositionWithOffset(var1, var2, this.mShouldReverseLayout);
   }

   public void scrollToPositionWithOffset(int var1, int var2, boolean var3) {
      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = var2;
      this.mPendingScrollPositionBottom = var3;
      LinearLayoutManager.SavedState var4 = this.mPendingSavedState;
      if (var4 != null) {
         var4.invalidateAnchor();
      }

      this.requestLayout();
   }

   public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.mOrientation == 0 ? 0 : this.scrollBy(var1, var2, var3);
   }

   public void setInitialPrefetchItemCount(int var1) {
      this.mInitialPrefetchItemCount = var1;
   }

   public void setOrientation(int var1) {
      if (var1 != 0 && var1 != 1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("invalid orientation:");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      } else {
         this.assertNotInLayoutOrScroll((String)null);
         if (var1 != this.mOrientation || this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, var1);
            this.mAnchorInfo.mOrientationHelper = this.mOrientationHelper;
            this.mOrientation = var1;
            this.requestLayout();
         }

      }
   }

   public void setRecycleChildrenOnDetach(boolean var1) {
      this.mRecycleChildrenOnDetach = var1;
   }

   public void setReverseLayout(boolean var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (var1 != this.mReverseLayout) {
         this.mReverseLayout = var1;
         this.requestLayout();
      }
   }

   public void setSmoothScrollbarEnabled(boolean var1) {
      this.mSmoothScrollbarEnabled = var1;
   }

   public void setStackFromEnd(boolean var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (this.mStackFromEnd != var1) {
         this.mStackFromEnd = var1;
         this.requestLayout();
      }
   }

   boolean shouldMeasureTwice() {
      boolean var1;
      if (this.getHeightMode() != 1073741824 && this.getWidthMode() != 1073741824 && this.hasFlexibleChildInBothOrientations()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
      LinearSmoothScroller var4 = new LinearSmoothScroller(var1.getContext());
      var4.setTargetPosition(var3);
      this.startSmoothScroll(var4);
   }

   public boolean supportsPredictiveItemAnimations() {
      boolean var1;
      if (this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   void validateChildOrder() {
      StringBuilder var1 = new StringBuilder();
      var1.append("validating child count ");
      var1.append(this.getChildCount());
      Log.d("LinearLayoutManager", var1.toString());
      if (this.getChildCount() >= 1) {
         boolean var2 = false;
         boolean var3 = false;
         int var4 = this.getPosition(this.getChildAt(0));
         int var5 = this.mOrientationHelper.getDecoratedStart(this.getChildAt(0));
         int var6;
         int var7;
         int var8;
         View var9;
         if (this.mShouldReverseLayout) {
            for(var6 = 1; var6 < this.getChildCount(); ++var6) {
               var9 = this.getChildAt(var6);
               var7 = this.getPosition(var9);
               var8 = this.mOrientationHelper.getDecoratedStart(var9);
               if (var7 < var4) {
                  this.logChildren();
                  var1 = new StringBuilder();
                  var1.append("detected invalid position. loc invalid? ");
                  if (var8 < var5) {
                     var3 = true;
                  }

                  var1.append(var3);
                  throw new RuntimeException(var1.toString());
               }

               if (var8 > var5) {
                  this.logChildren();
                  throw new RuntimeException("detected invalid location");
               }
            }
         } else {
            for(var6 = 1; var6 < this.getChildCount(); ++var6) {
               var9 = this.getChildAt(var6);
               var8 = this.getPosition(var9);
               var7 = this.mOrientationHelper.getDecoratedStart(var9);
               if (var8 < var4) {
                  this.logChildren();
                  var1 = new StringBuilder();
                  var1.append("detected invalid position. loc invalid? ");
                  var3 = var2;
                  if (var7 < var5) {
                     var3 = true;
                  }

                  var1.append(var3);
                  throw new RuntimeException(var1.toString());
               }

               if (var7 < var5) {
                  this.logChildren();
                  throw new RuntimeException("detected invalid location");
               }
            }
         }

      }
   }

   static class AnchorInfo {
      int mCoordinate;
      boolean mLayoutFromEnd;
      OrientationHelper mOrientationHelper;
      int mPosition;
      boolean mValid;

      AnchorInfo() {
         this.reset();
      }

      void assignCoordinateFromPadding() {
         int var1;
         if (this.mLayoutFromEnd) {
            var1 = this.mOrientationHelper.getEndAfterPadding();
         } else {
            var1 = this.mOrientationHelper.getStartAfterPadding();
         }

         this.mCoordinate = var1;
      }

      public void assignFromView(View var1, int var2) {
         if (this.mLayoutFromEnd) {
            this.mCoordinate = this.mOrientationHelper.getDecoratedEnd(var1) + this.mOrientationHelper.getTotalSpaceChange();
         } else {
            this.mCoordinate = this.mOrientationHelper.getDecoratedStart(var1);
         }

         this.mPosition = var2;
      }

      public void assignFromViewAndKeepVisibleRect(View var1, int var2) {
         int var3 = this.mOrientationHelper.getTotalSpaceChange();
         if (var3 >= 0) {
            this.assignFromView(var1, var2);
         } else {
            this.mPosition = var2;
            int var4;
            int var5;
            if (this.mLayoutFromEnd) {
               var2 = this.mOrientationHelper.getEndAfterPadding() - var3 - this.mOrientationHelper.getDecoratedEnd(var1);
               this.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - var2;
               if (var2 > 0) {
                  var3 = this.mOrientationHelper.getDecoratedMeasurement(var1);
                  var4 = this.mCoordinate;
                  var5 = this.mOrientationHelper.getStartAfterPadding();
                  var3 = var4 - var3 - (var5 + Math.min(this.mOrientationHelper.getDecoratedStart(var1) - var5, 0));
                  if (var3 < 0) {
                     this.mCoordinate += Math.min(var2, -var3);
                  }
               }
            } else {
               var4 = this.mOrientationHelper.getDecoratedStart(var1);
               var2 = var4 - this.mOrientationHelper.getStartAfterPadding();
               this.mCoordinate = var4;
               if (var2 > 0) {
                  int var6 = this.mOrientationHelper.getDecoratedMeasurement(var1);
                  var5 = this.mOrientationHelper.getEndAfterPadding();
                  int var7 = this.mOrientationHelper.getDecoratedEnd(var1);
                  var3 = this.mOrientationHelper.getEndAfterPadding() - Math.min(0, var5 - var3 - var7) - (var4 + var6);
                  if (var3 < 0) {
                     this.mCoordinate -= Math.min(var2, -var3);
                  }
               }
            }

         }
      }

      boolean isViewValidAsAnchor(View var1, RecyclerView.State var2) {
         RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var1.getLayoutParams();
         boolean var3;
         if (!var4.isItemRemoved() && var4.getViewLayoutPosition() >= 0 && var4.getViewLayoutPosition() < var2.getItemCount()) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      void reset() {
         this.mPosition = -1;
         this.mCoordinate = Integer.MIN_VALUE;
         this.mLayoutFromEnd = false;
         this.mValid = false;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("AnchorInfo{mPosition=");
         var1.append(this.mPosition);
         var1.append(", mCoordinate=");
         var1.append(this.mCoordinate);
         var1.append(", mLayoutFromEnd=");
         var1.append(this.mLayoutFromEnd);
         var1.append(", mValid=");
         var1.append(this.mValid);
         var1.append('}');
         return var1.toString();
      }
   }

   protected static class LayoutChunkResult {
      public int mConsumed;
      public boolean mFinished;
      public boolean mFocusable;
      public boolean mIgnoreConsumed;

      void resetInternal() {
         this.mConsumed = 0;
         this.mFinished = false;
         this.mIgnoreConsumed = false;
         this.mFocusable = false;
      }
   }

   static class LayoutState {
      int mAvailable;
      int mCurrentPosition;
      int mExtraFillSpace = 0;
      boolean mInfinite;
      boolean mIsPreLayout = false;
      int mItemDirection;
      int mLastScrollDelta;
      int mLayoutDirection;
      int mNoRecycleSpace = 0;
      int mOffset;
      boolean mRecycle = true;
      List mScrapList = null;
      int mScrollingOffset;

      private View nextViewFromScrapList() {
         int var1 = this.mScrapList.size();

         for(int var2 = 0; var2 < var1; ++var2) {
            View var3 = ((RecyclerView.ViewHolder)this.mScrapList.get(var2)).itemView;
            RecyclerView.LayoutParams var4 = (RecyclerView.LayoutParams)var3.getLayoutParams();
            if (!var4.isItemRemoved() && this.mCurrentPosition == var4.getViewLayoutPosition()) {
               this.assignPositionFromScrapList(var3);
               return var3;
            }
         }

         return null;
      }

      public void assignPositionFromScrapList() {
         this.assignPositionFromScrapList((View)null);
      }

      public void assignPositionFromScrapList(View var1) {
         var1 = this.nextViewInLimitedList(var1);
         if (var1 == null) {
            this.mCurrentPosition = -1;
         } else {
            this.mCurrentPosition = ((RecyclerView.LayoutParams)var1.getLayoutParams()).getViewLayoutPosition();
         }

      }

      boolean hasMore(RecyclerView.State var1) {
         int var2 = this.mCurrentPosition;
         boolean var3;
         if (var2 >= 0 && var2 < var1.getItemCount()) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      View next(RecyclerView.Recycler var1) {
         if (this.mScrapList != null) {
            return this.nextViewFromScrapList();
         } else {
            View var2 = var1.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return var2;
         }
      }

      public View nextViewInLimitedList(View var1) {
         int var2 = this.mScrapList.size();
         View var3 = null;
         int var4 = Integer.MAX_VALUE;
         int var5 = 0;

         View var6;
         while(true) {
            var6 = var3;
            if (var5 >= var2) {
               break;
            }

            var6 = ((RecyclerView.ViewHolder)this.mScrapList.get(var5)).itemView;
            RecyclerView.LayoutParams var7 = (RecyclerView.LayoutParams)var6.getLayoutParams();
            View var8 = var3;
            int var9 = var4;
            if (var6 != var1) {
               if (var7.isItemRemoved()) {
                  var8 = var3;
                  var9 = var4;
               } else {
                  int var10 = (var7.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
                  if (var10 < 0) {
                     var8 = var3;
                     var9 = var4;
                  } else {
                     var8 = var3;
                     var9 = var4;
                     if (var10 < var4) {
                        if (var10 == 0) {
                           break;
                        }

                        var9 = var10;
                        var8 = var6;
                     }
                  }
               }
            }

            ++var5;
            var3 = var8;
            var4 = var9;
         }

         return var6;
      }
   }

   @SuppressLint({"BanParcelableUsage"})
   public static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public LinearLayoutManager.SavedState createFromParcel(Parcel var1) {
            return new LinearLayoutManager.SavedState(var1);
         }

         public LinearLayoutManager.SavedState[] newArray(int var1) {
            return new LinearLayoutManager.SavedState[var1];
         }
      };
      boolean mAnchorLayoutFromEnd;
      int mAnchorOffset;
      int mAnchorPosition;

      public SavedState() {
      }

      SavedState(Parcel var1) {
         this.mAnchorPosition = var1.readInt();
         this.mAnchorOffset = var1.readInt();
         int var2 = var1.readInt();
         boolean var3 = true;
         if (var2 != 1) {
            var3 = false;
         }

         this.mAnchorLayoutFromEnd = var3;
      }

      public SavedState(LinearLayoutManager.SavedState var1) {
         this.mAnchorPosition = var1.mAnchorPosition;
         this.mAnchorOffset = var1.mAnchorOffset;
         this.mAnchorLayoutFromEnd = var1.mAnchorLayoutFromEnd;
      }

      public int describeContents() {
         return 0;
      }

      boolean hasValidAnchor() {
         boolean var1;
         if (this.mAnchorPosition >= 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      void invalidateAnchor() {
         this.mAnchorPosition = -1;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.mAnchorPosition);
         var1.writeInt(this.mAnchorOffset);
         var1.writeInt(this.mAnchorLayoutFromEnd);
      }
   }
}
