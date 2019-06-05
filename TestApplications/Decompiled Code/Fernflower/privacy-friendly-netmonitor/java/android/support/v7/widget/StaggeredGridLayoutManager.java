package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class StaggeredGridLayoutManager extends RecyclerView.LayoutManager implements RecyclerView.SmoothScroller.ScrollVectorProvider {
   static final boolean DEBUG = false;
   @Deprecated
   public static final int GAP_HANDLING_LAZY = 1;
   public static final int GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS = 2;
   public static final int GAP_HANDLING_NONE = 0;
   public static final int HORIZONTAL = 0;
   static final int INVALID_OFFSET = Integer.MIN_VALUE;
   private static final float MAX_SCROLL_FACTOR = 0.33333334F;
   private static final String TAG = "StaggeredGridLManager";
   public static final int VERTICAL = 1;
   private final StaggeredGridLayoutManager.AnchorInfo mAnchorInfo = new StaggeredGridLayoutManager.AnchorInfo();
   private final Runnable mCheckForGapsRunnable;
   private int mFullSizeSpec;
   private int mGapStrategy = 2;
   private boolean mLaidOutInvalidFullSpan = false;
   private boolean mLastLayoutFromEnd;
   private boolean mLastLayoutRTL;
   @NonNull
   private final LayoutState mLayoutState;
   StaggeredGridLayoutManager.LazySpanLookup mLazySpanLookup = new StaggeredGridLayoutManager.LazySpanLookup();
   private int mOrientation;
   private StaggeredGridLayoutManager.SavedState mPendingSavedState;
   int mPendingScrollPosition = -1;
   int mPendingScrollPositionOffset = Integer.MIN_VALUE;
   private int[] mPrefetchDistances;
   @NonNull
   OrientationHelper mPrimaryOrientation;
   private BitSet mRemainingSpans;
   boolean mReverseLayout = false;
   @NonNull
   OrientationHelper mSecondaryOrientation;
   boolean mShouldReverseLayout = false;
   private int mSizePerSpan;
   private boolean mSmoothScrollbarEnabled;
   private int mSpanCount = -1;
   StaggeredGridLayoutManager.Span[] mSpans;
   private final Rect mTmpRect = new Rect();

   public StaggeredGridLayoutManager(int var1, int var2) {
      boolean var3 = true;
      this.mSmoothScrollbarEnabled = true;
      this.mCheckForGapsRunnable = new Runnable() {
         public void run() {
            StaggeredGridLayoutManager.this.checkForGaps();
         }
      };
      this.mOrientation = var2;
      this.setSpanCount(var1);
      if (this.mGapStrategy == 0) {
         var3 = false;
      }

      this.setAutoMeasureEnabled(var3);
      this.mLayoutState = new LayoutState();
      this.createOrientationHelpers();
   }

   public StaggeredGridLayoutManager(Context var1, AttributeSet var2, int var3, int var4) {
      boolean var5 = true;
      this.mSmoothScrollbarEnabled = true;
      this.mCheckForGapsRunnable = new Runnable() {
         public void run() {
            StaggeredGridLayoutManager.this.checkForGaps();
         }
      };
      RecyclerView.LayoutManager.Properties var6 = getProperties(var1, var2, var3, var4);
      this.setOrientation(var6.orientation);
      this.setSpanCount(var6.spanCount);
      this.setReverseLayout(var6.reverseLayout);
      if (this.mGapStrategy == 0) {
         var5 = false;
      }

      this.setAutoMeasureEnabled(var5);
      this.mLayoutState = new LayoutState();
      this.createOrientationHelpers();
   }

   private void appendViewToAllSpans(View var1) {
      for(int var2 = this.mSpanCount - 1; var2 >= 0; --var2) {
         this.mSpans[var2].appendToSpan(var1);
      }

   }

   private void applyPendingSavedState(StaggeredGridLayoutManager.AnchorInfo var1) {
      if (this.mPendingSavedState.mSpanOffsetsSize > 0) {
         if (this.mPendingSavedState.mSpanOffsetsSize == this.mSpanCount) {
            for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
               this.mSpans[var2].clear();
               int var3 = this.mPendingSavedState.mSpanOffsets[var2];
               int var4 = var3;
               if (var3 != Integer.MIN_VALUE) {
                  if (this.mPendingSavedState.mAnchorLayoutFromEnd) {
                     var4 = var3 + this.mPrimaryOrientation.getEndAfterPadding();
                  } else {
                     var4 = var3 + this.mPrimaryOrientation.getStartAfterPadding();
                  }
               }

               this.mSpans[var2].setLine(var4);
            }
         } else {
            this.mPendingSavedState.invalidateSpanInfo();
            this.mPendingSavedState.mAnchorPosition = this.mPendingSavedState.mVisibleAnchorPosition;
         }
      }

      this.mLastLayoutRTL = this.mPendingSavedState.mLastLayoutRTL;
      this.setReverseLayout(this.mPendingSavedState.mReverseLayout);
      this.resolveShouldLayoutReverse();
      if (this.mPendingSavedState.mAnchorPosition != -1) {
         this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
         var1.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
      } else {
         var1.mLayoutFromEnd = this.mShouldReverseLayout;
      }

      if (this.mPendingSavedState.mSpanLookupSize > 1) {
         this.mLazySpanLookup.mData = this.mPendingSavedState.mSpanLookup;
         this.mLazySpanLookup.mFullSpanItems = this.mPendingSavedState.mFullSpanItems;
      }

   }

   private void attachViewToSpans(View var1, StaggeredGridLayoutManager.LayoutParams var2, LayoutState var3) {
      if (var3.mLayoutDirection == 1) {
         if (var2.mFullSpan) {
            this.appendViewToAllSpans(var1);
         } else {
            var2.mSpan.appendToSpan(var1);
         }
      } else if (var2.mFullSpan) {
         this.prependViewToAllSpans(var1);
      } else {
         var2.mSpan.prependToSpan(var1);
      }

   }

   private int calculateScrollDirectionForPosition(int var1) {
      int var2 = this.getChildCount();
      byte var3 = -1;
      if (var2 == 0) {
         if (this.mShouldReverseLayout) {
            var3 = 1;
         }

         return var3;
      } else {
         boolean var4;
         if (var1 < this.getFirstChildPosition()) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (var4 == this.mShouldReverseLayout) {
            var3 = 1;
         }

         return var3;
      }
   }

   private boolean checkSpanForGap(StaggeredGridLayoutManager.Span var1) {
      if (this.mShouldReverseLayout) {
         if (var1.getEndLine() < this.mPrimaryOrientation.getEndAfterPadding()) {
            return var1.getLayoutParams((View)var1.mViews.get(var1.mViews.size() - 1)).mFullSpan ^ true;
         }
      } else if (var1.getStartLine() > this.mPrimaryOrientation.getStartAfterPadding()) {
         return var1.getLayoutParams((View)var1.mViews.get(0)).mFullSpan ^ true;
      }

      return false;
   }

   private int computeScrollExtent(RecyclerView.State var1) {
      return this.getChildCount() == 0 ? 0 : ScrollbarHelper.computeScrollExtent(var1, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
   }

   private int computeScrollOffset(RecyclerView.State var1) {
      return this.getChildCount() == 0 ? 0 : ScrollbarHelper.computeScrollOffset(var1, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
   }

   private int computeScrollRange(RecyclerView.State var1) {
      return this.getChildCount() == 0 ? 0 : ScrollbarHelper.computeScrollRange(var1, this.mPrimaryOrientation, this.findFirstVisibleItemClosestToStart(this.mSmoothScrollbarEnabled ^ true), this.findFirstVisibleItemClosestToEnd(this.mSmoothScrollbarEnabled ^ true), this, this.mSmoothScrollbarEnabled);
   }

   private int convertFocusDirectionToLayoutDirection(int var1) {
      int var2 = -1;
      int var3 = Integer.MIN_VALUE;
      if (var1 != 17) {
         if (var1 != 33) {
            if (var1 != 66) {
               if (var1 != 130) {
                  switch(var1) {
                  case 1:
                     if (this.mOrientation == 1) {
                        return -1;
                     } else {
                        if (this.isLayoutRTL()) {
                           return 1;
                        }

                        return -1;
                     }
                  case 2:
                     if (this.mOrientation == 1) {
                        return 1;
                     } else {
                        if (this.isLayoutRTL()) {
                           return -1;
                        }

                        return 1;
                     }
                  default:
                     return Integer.MIN_VALUE;
                  }
               } else {
                  if (this.mOrientation == 1) {
                     var3 = 1;
                  }

                  return var3;
               }
            } else {
               if (this.mOrientation == 0) {
                  var3 = 1;
               }

               return var3;
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
   }

   private StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFullSpanItemFromEnd(int var1) {
      StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var2 = new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem();
      var2.mGapPerSpan = new int[this.mSpanCount];

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         var2.mGapPerSpan[var3] = var1 - this.mSpans[var3].getEndLine(var1);
      }

      return var2;
   }

   private StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFullSpanItemFromStart(int var1) {
      StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var2 = new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem();
      var2.mGapPerSpan = new int[this.mSpanCount];

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         var2.mGapPerSpan[var3] = this.mSpans[var3].getStartLine(var1) - var1;
      }

      return var2;
   }

   private void createOrientationHelpers() {
      this.mPrimaryOrientation = OrientationHelper.createOrientationHelper(this, this.mOrientation);
      this.mSecondaryOrientation = OrientationHelper.createOrientationHelper(this, 1 - this.mOrientation);
   }

   private int fill(RecyclerView.Recycler var1, LayoutState var2, RecyclerView.State var3) {
      BitSet var4 = this.mRemainingSpans;
      int var5 = this.mSpanCount;
      int var6 = 0;
      var4.set(0, var5, true);
      if (this.mLayoutState.mInfinite) {
         if (var2.mLayoutDirection == 1) {
            var5 = Integer.MAX_VALUE;
         } else {
            var5 = Integer.MIN_VALUE;
         }
      } else if (var2.mLayoutDirection == 1) {
         var5 = var2.mEndLine + var2.mAvailable;
      } else {
         var5 = var2.mStartLine - var2.mAvailable;
      }

      this.updateAllRemainingSpans(var2.mLayoutDirection, var5);
      int var7;
      if (this.mShouldReverseLayout) {
         var7 = this.mPrimaryOrientation.getEndAfterPadding();
      } else {
         var7 = this.mPrimaryOrientation.getStartAfterPadding();
      }

      boolean var8;
      for(var8 = false; var2.hasMore(var3) && (this.mLayoutState.mInfinite || !this.mRemainingSpans.isEmpty()); var8 = true) {
         View var9 = var2.next(var1);
         StaggeredGridLayoutManager.LayoutParams var10 = (StaggeredGridLayoutManager.LayoutParams)var9.getLayoutParams();
         int var11 = var10.getViewLayoutPosition();
         int var17 = this.mLazySpanLookup.getSpan(var11);
         int var12;
         if (var17 == -1) {
            var12 = 1;
         } else {
            var12 = var6;
         }

         StaggeredGridLayoutManager.Span var16;
         if (var12 != 0) {
            if (var10.mFullSpan) {
               var16 = this.mSpans[var6];
            } else {
               var16 = this.getNextSpan(var2);
            }

            this.mLazySpanLookup.setSpan(var11, var16);
         } else {
            var16 = this.mSpans[var17];
         }

         var10.mSpan = var16;
         if (var2.mLayoutDirection == 1) {
            this.addView(var9);
         } else {
            this.addView(var9, var6);
         }

         this.measureChildWithDecorationsAndMargin(var9, var10, (boolean)var6);
         StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var13;
         int var14;
         if (var2.mLayoutDirection == 1) {
            if (var10.mFullSpan) {
               var6 = this.getMaxEnd(var7);
            } else {
               var6 = var16.getEndLine(var7);
            }

            var17 = this.mPrimaryOrientation.getDecoratedMeasurement(var9);
            if (var12 != 0 && var10.mFullSpan) {
               var13 = this.createFullSpanItemFromEnd(var6);
               var13.mGapDir = -1;
               var13.mPosition = var11;
               this.mLazySpanLookup.addFullSpanItem(var13);
            }

            var14 = var17 + var6;
            var17 = var6;
         } else {
            if (var10.mFullSpan) {
               var6 = this.getMinStart(var7);
            } else {
               var6 = var16.getStartLine(var7);
            }

            var17 = var6 - this.mPrimaryOrientation.getDecoratedMeasurement(var9);
            if (var12 != 0 && var10.mFullSpan) {
               var13 = this.createFullSpanItemFromStart(var6);
               var13.mGapDir = 1;
               var13.mPosition = var11;
               this.mLazySpanLookup.addFullSpanItem(var13);
            }

            var14 = var6;
         }

         if (var10.mFullSpan && var2.mItemDirection == -1) {
            if (var12 != 0) {
               this.mLaidOutInvalidFullSpan = true;
            } else {
               boolean var15;
               if (var2.mLayoutDirection == 1) {
                  var15 = this.areAllEndsEqual();
               } else {
                  var15 = this.areAllStartsEqual();
               }

               if (var15 ^ true) {
                  var13 = this.mLazySpanLookup.getFullSpanItem(var11);
                  if (var13 != null) {
                     var13.mHasUnwantedGapAfter = true;
                  }

                  this.mLaidOutInvalidFullSpan = true;
               }
            }
         }

         this.attachViewToSpans(var9, var10, var2);
         if (this.isLayoutRTL() && this.mOrientation == 1) {
            if (var10.mFullSpan) {
               var6 = this.mSecondaryOrientation.getEndAfterPadding();
            } else {
               var6 = this.mSecondaryOrientation.getEndAfterPadding() - (this.mSpanCount - 1 - var16.mIndex) * this.mSizePerSpan;
            }

            var11 = this.mSecondaryOrientation.getDecoratedMeasurement(var9);
            var11 = var6 - var11;
            var6 = var6;
         } else {
            if (var10.mFullSpan) {
               var6 = this.mSecondaryOrientation.getStartAfterPadding();
            } else {
               var6 = var16.mIndex * this.mSizePerSpan + this.mSecondaryOrientation.getStartAfterPadding();
            }

            var11 = this.mSecondaryOrientation.getDecoratedMeasurement(var9);
            var12 = var6;
            var6 += var11;
            var11 = var12;
         }

         if (this.mOrientation == 1) {
            this.layoutDecoratedWithMargins(var9, var11, var17, var6, var14);
         } else {
            this.layoutDecoratedWithMargins(var9, var17, var11, var14, var6);
         }

         if (var10.mFullSpan) {
            this.updateAllRemainingSpans(this.mLayoutState.mLayoutDirection, var5);
         } else {
            this.updateRemainingSpans(var16, this.mLayoutState.mLayoutDirection, var5);
         }

         this.recycle(var1, this.mLayoutState);
         if (this.mLayoutState.mStopInFocusable && var9.hasFocusable()) {
            if (var10.mFullSpan) {
               this.mRemainingSpans.clear();
            } else {
               this.mRemainingSpans.set(var16.mIndex, false);
            }
         }

         var6 = 0;
      }

      if (!var8) {
         this.recycle(var1, this.mLayoutState);
      }

      if (this.mLayoutState.mLayoutDirection == -1) {
         var5 = this.getMinStart(this.mPrimaryOrientation.getStartAfterPadding());
         var5 = this.mPrimaryOrientation.getStartAfterPadding() - var5;
      } else {
         var5 = this.getMaxEnd(this.mPrimaryOrientation.getEndAfterPadding()) - this.mPrimaryOrientation.getEndAfterPadding();
      }

      if (var5 > 0) {
         var6 = Math.min(var2.mAvailable, var5);
      }

      return var6;
   }

   private int findFirstReferenceChildPosition(int var1) {
      int var2 = this.getChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = this.getPosition(this.getChildAt(var3));
         if (var4 >= 0 && var4 < var1) {
            return var4;
         }
      }

      return 0;
   }

   private int findLastReferenceChildPosition(int var1) {
      for(int var2 = this.getChildCount() - 1; var2 >= 0; --var2) {
         int var3 = this.getPosition(this.getChildAt(var2));
         if (var3 >= 0 && var3 < var1) {
            return var3;
         }
      }

      return 0;
   }

   private void fixEndGap(RecyclerView.Recycler var1, RecyclerView.State var2, boolean var3) {
      int var4 = this.getMaxEnd(Integer.MIN_VALUE);
      if (var4 != Integer.MIN_VALUE) {
         var4 = this.mPrimaryOrientation.getEndAfterPadding() - var4;
         if (var4 > 0) {
            var4 -= -this.scrollBy(-var4, var1, var2);
            if (var3 && var4 > 0) {
               this.mPrimaryOrientation.offsetChildren(var4);
            }

         }
      }
   }

   private void fixStartGap(RecyclerView.Recycler var1, RecyclerView.State var2, boolean var3) {
      int var4 = this.getMinStart(Integer.MAX_VALUE);
      if (var4 != Integer.MAX_VALUE) {
         var4 -= this.mPrimaryOrientation.getStartAfterPadding();
         if (var4 > 0) {
            var4 -= this.scrollBy(var4, var1, var2);
            if (var3 && var4 > 0) {
               this.mPrimaryOrientation.offsetChildren(-var4);
            }

         }
      }
   }

   private int getMaxEnd(int var1) {
      int var2 = this.mSpans[0].getEndLine(var1);

      int var5;
      for(int var3 = 1; var3 < this.mSpanCount; var2 = var5) {
         int var4 = this.mSpans[var3].getEndLine(var1);
         var5 = var2;
         if (var4 > var2) {
            var5 = var4;
         }

         ++var3;
      }

      return var2;
   }

   private int getMaxStart(int var1) {
      int var2 = this.mSpans[0].getStartLine(var1);

      int var5;
      for(int var3 = 1; var3 < this.mSpanCount; var2 = var5) {
         int var4 = this.mSpans[var3].getStartLine(var1);
         var5 = var2;
         if (var4 > var2) {
            var5 = var4;
         }

         ++var3;
      }

      return var2;
   }

   private int getMinEnd(int var1) {
      int var2 = this.mSpans[0].getEndLine(var1);

      int var5;
      for(int var3 = 1; var3 < this.mSpanCount; var2 = var5) {
         int var4 = this.mSpans[var3].getEndLine(var1);
         var5 = var2;
         if (var4 < var2) {
            var5 = var4;
         }

         ++var3;
      }

      return var2;
   }

   private int getMinStart(int var1) {
      int var2 = this.mSpans[0].getStartLine(var1);

      int var5;
      for(int var3 = 1; var3 < this.mSpanCount; var2 = var5) {
         int var4 = this.mSpans[var3].getStartLine(var1);
         var5 = var2;
         if (var4 < var2) {
            var5 = var4;
         }

         ++var3;
      }

      return var2;
   }

   private StaggeredGridLayoutManager.Span getNextSpan(LayoutState var1) {
      boolean var2 = this.preferLastSpan(var1.mLayoutDirection);
      int var3 = -1;
      int var4;
      byte var5;
      if (var2) {
         var4 = this.mSpanCount - 1;
         var5 = -1;
      } else {
         var4 = 0;
         var3 = this.mSpanCount;
         var5 = 1;
      }

      int var6 = var1.mLayoutDirection;
      StaggeredGridLayoutManager.Span var7 = null;
      StaggeredGridLayoutManager.Span var11 = null;
      int var8;
      int var9;
      int var10;
      if (var6 == 1) {
         var8 = Integer.MAX_VALUE;
         var9 = this.mPrimaryOrientation.getStartAfterPadding();

         for(var6 = var4; var6 != var3; var8 = var4) {
            var7 = this.mSpans[var6];
            var10 = var7.getEndLine(var9);
            var4 = var8;
            if (var10 < var8) {
               var11 = var7;
               var4 = var10;
            }

            var6 += var5;
         }

         return var11;
      } else {
         var6 = Integer.MIN_VALUE;
         var9 = this.mPrimaryOrientation.getEndAfterPadding();

         for(var11 = var7; var4 != var3; var6 = var8) {
            var7 = this.mSpans[var4];
            var10 = var7.getStartLine(var9);
            var8 = var6;
            if (var10 > var6) {
               var11 = var7;
               var8 = var10;
            }

            var4 += var5;
         }

         return var11;
      }
   }

   private void handleUpdate(int var1, int var2, int var3) {
      int var4;
      if (this.mShouldReverseLayout) {
         var4 = this.getLastChildPosition();
      } else {
         var4 = this.getFirstChildPosition();
      }

      int var5;
      int var6;
      label43: {
         if (var3 == 8) {
            if (var1 >= var2) {
               var6 = var1 + 1;
               var5 = var2;
               break label43;
            }

            var5 = var2 + 1;
         } else {
            var5 = var1 + var2;
         }

         var6 = var5;
         var5 = var1;
      }

      this.mLazySpanLookup.invalidateAfter(var5);
      if (var3 != 8) {
         switch(var3) {
         case 1:
            this.mLazySpanLookup.offsetForAddition(var1, var2);
            break;
         case 2:
            this.mLazySpanLookup.offsetForRemoval(var1, var2);
         }
      } else {
         this.mLazySpanLookup.offsetForRemoval(var1, 1);
         this.mLazySpanLookup.offsetForAddition(var2, 1);
      }

      if (var6 > var4) {
         if (this.mShouldReverseLayout) {
            var1 = this.getFirstChildPosition();
         } else {
            var1 = this.getLastChildPosition();
         }

         if (var5 <= var1) {
            this.requestLayout();
         }

      }
   }

   private void measureChildWithDecorationsAndMargin(View var1, int var2, int var3, boolean var4) {
      this.calculateItemDecorationsForChild(var1, this.mTmpRect);
      StaggeredGridLayoutManager.LayoutParams var5 = (StaggeredGridLayoutManager.LayoutParams)var1.getLayoutParams();
      var2 = this.updateSpecWithExtra(var2, var5.leftMargin + this.mTmpRect.left, var5.rightMargin + this.mTmpRect.right);
      var3 = this.updateSpecWithExtra(var3, var5.topMargin + this.mTmpRect.top, var5.bottomMargin + this.mTmpRect.bottom);
      if (var4) {
         var4 = this.shouldReMeasureChild(var1, var2, var3, var5);
      } else {
         var4 = this.shouldMeasureChild(var1, var2, var3, var5);
      }

      if (var4) {
         var1.measure(var2, var3);
      }

   }

   private void measureChildWithDecorationsAndMargin(View var1, StaggeredGridLayoutManager.LayoutParams var2, boolean var3) {
      if (var2.mFullSpan) {
         if (this.mOrientation == 1) {
            this.measureChildWithDecorationsAndMargin(var1, this.mFullSizeSpec, getChildMeasureSpec(this.getHeight(), this.getHeightMode(), 0, var2.height, true), var3);
         } else {
            this.measureChildWithDecorationsAndMargin(var1, getChildMeasureSpec(this.getWidth(), this.getWidthMode(), 0, var2.width, true), this.mFullSizeSpec, var3);
         }
      } else if (this.mOrientation == 1) {
         this.measureChildWithDecorationsAndMargin(var1, getChildMeasureSpec(this.mSizePerSpan, this.getWidthMode(), 0, var2.width, false), getChildMeasureSpec(this.getHeight(), this.getHeightMode(), 0, var2.height, true), var3);
      } else {
         this.measureChildWithDecorationsAndMargin(var1, getChildMeasureSpec(this.getWidth(), this.getWidthMode(), 0, var2.width, true), getChildMeasureSpec(this.mSizePerSpan, this.getHeightMode(), 0, var2.height, false), var3);
      }

   }

   private void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2, boolean var3) {
      StaggeredGridLayoutManager.AnchorInfo var4 = this.mAnchorInfo;
      if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && var2.getItemCount() == 0) {
         this.removeAndRecycleAllViews(var1);
         var4.reset();
      } else {
         boolean var5 = var4.mValid;
         boolean var6 = true;
         boolean var7;
         if (var5 && this.mPendingScrollPosition == -1 && this.mPendingSavedState == null) {
            var7 = false;
         } else {
            var7 = true;
         }

         if (var7) {
            var4.reset();
            if (this.mPendingSavedState != null) {
               this.applyPendingSavedState(var4);
            } else {
               this.resolveShouldLayoutReverse();
               var4.mLayoutFromEnd = this.mShouldReverseLayout;
            }

            this.updateAnchorInfoForLayout(var2, var4);
            var4.mValid = true;
         }

         if (this.mPendingSavedState == null && this.mPendingScrollPosition == -1 && (var4.mLayoutFromEnd != this.mLastLayoutFromEnd || this.isLayoutRTL() != this.mLastLayoutRTL)) {
            this.mLazySpanLookup.clear();
            var4.mInvalidateOffsets = true;
         }

         if (this.getChildCount() > 0 && (this.mPendingSavedState == null || this.mPendingSavedState.mSpanOffsetsSize < 1)) {
            int var9;
            if (var4.mInvalidateOffsets) {
               for(var9 = 0; var9 < this.mSpanCount; ++var9) {
                  this.mSpans[var9].clear();
                  if (var4.mOffset != Integer.MIN_VALUE) {
                     this.mSpans[var9].setLine(var4.mOffset);
                  }
               }
            } else if (!var7 && this.mAnchorInfo.mSpanReferenceLines != null) {
               for(var9 = 0; var9 < this.mSpanCount; ++var9) {
                  StaggeredGridLayoutManager.Span var8 = this.mSpans[var9];
                  var8.clear();
                  var8.setLine(this.mAnchorInfo.mSpanReferenceLines[var9]);
               }
            } else {
               for(var9 = 0; var9 < this.mSpanCount; ++var9) {
                  this.mSpans[var9].cacheReferenceLineAndClear(this.mShouldReverseLayout, var4.mOffset);
               }

               this.mAnchorInfo.saveSpanReferenceLines(this.mSpans);
            }
         }

         this.detachAndScrapAttachedViews(var1);
         this.mLayoutState.mRecycle = false;
         this.mLaidOutInvalidFullSpan = false;
         this.updateMeasureSpecs(this.mSecondaryOrientation.getTotalSpace());
         this.updateLayoutState(var4.mPosition, var2);
         if (var4.mLayoutFromEnd) {
            this.setLayoutStateDirection(-1);
            this.fill(var1, this.mLayoutState, var2);
            this.setLayoutStateDirection(1);
            this.mLayoutState.mCurrentPosition = var4.mPosition + this.mLayoutState.mItemDirection;
            this.fill(var1, this.mLayoutState, var2);
         } else {
            this.setLayoutStateDirection(1);
            this.fill(var1, this.mLayoutState, var2);
            this.setLayoutStateDirection(-1);
            this.mLayoutState.mCurrentPosition = var4.mPosition + this.mLayoutState.mItemDirection;
            this.fill(var1, this.mLayoutState, var2);
         }

         this.repositionToWrapContentIfNecessary();
         if (this.getChildCount() > 0) {
            if (this.mShouldReverseLayout) {
               this.fixEndGap(var1, var2, true);
               this.fixStartGap(var1, var2, false);
            } else {
               this.fixStartGap(var1, var2, true);
               this.fixEndGap(var1, var2, false);
            }
         }

         label96: {
            if (var3 && !var2.isPreLayout()) {
               if (this.mGapStrategy == 0 || this.getChildCount() <= 0 || !this.mLaidOutInvalidFullSpan && this.hasGapsToFix() == null) {
                  var7 = false;
               } else {
                  var7 = true;
               }

               if (var7) {
                  this.removeCallbacks(this.mCheckForGapsRunnable);
                  if (this.checkForGaps()) {
                     var7 = var6;
                     break label96;
                  }
               }
            }

            var7 = false;
         }

         if (var2.isPreLayout()) {
            this.mAnchorInfo.reset();
         }

         this.mLastLayoutFromEnd = var4.mLayoutFromEnd;
         this.mLastLayoutRTL = this.isLayoutRTL();
         if (var7) {
            this.mAnchorInfo.reset();
            this.onLayoutChildren(var1, var2, false);
         }

      }
   }

   private boolean preferLastSpan(int var1) {
      int var2 = this.mOrientation;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5;
      if (var2 == 0) {
         if (var1 == -1) {
            var5 = true;
         } else {
            var5 = false;
         }

         var3 = var4;
         if (var5 != this.mShouldReverseLayout) {
            var3 = true;
         }

         return var3;
      } else {
         if (var1 == -1) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var5 == this.mShouldReverseLayout) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var5 == this.isLayoutRTL()) {
            var3 = true;
         }

         return var3;
      }
   }

   private void prependViewToAllSpans(View var1) {
      for(int var2 = this.mSpanCount - 1; var2 >= 0; --var2) {
         this.mSpans[var2].prependToSpan(var1);
      }

   }

   private void recycle(RecyclerView.Recycler var1, LayoutState var2) {
      if (var2.mRecycle && !var2.mInfinite) {
         if (var2.mAvailable == 0) {
            if (var2.mLayoutDirection == -1) {
               this.recycleFromEnd(var1, var2.mEndLine);
            } else {
               this.recycleFromStart(var1, var2.mStartLine);
            }
         } else {
            int var3;
            if (var2.mLayoutDirection == -1) {
               var3 = var2.mStartLine - this.getMaxStart(var2.mStartLine);
               if (var3 < 0) {
                  var3 = var2.mEndLine;
               } else {
                  var3 = var2.mEndLine - Math.min(var3, var2.mAvailable);
               }

               this.recycleFromEnd(var1, var3);
            } else {
               var3 = this.getMinEnd(var2.mEndLine) - var2.mEndLine;
               if (var3 < 0) {
                  var3 = var2.mStartLine;
               } else {
                  int var4 = var2.mStartLine;
                  var3 = Math.min(var3, var2.mAvailable) + var4;
               }

               this.recycleFromStart(var1, var3);
            }
         }

      }
   }

   private void recycleFromEnd(RecyclerView.Recycler var1, int var2) {
      for(int var3 = this.getChildCount() - 1; var3 >= 0; --var3) {
         View var4 = this.getChildAt(var3);
         if (this.mPrimaryOrientation.getDecoratedStart(var4) < var2 || this.mPrimaryOrientation.getTransformedStartWithDecoration(var4) < var2) {
            return;
         }

         StaggeredGridLayoutManager.LayoutParams var5 = (StaggeredGridLayoutManager.LayoutParams)var4.getLayoutParams();
         if (var5.mFullSpan) {
            byte var6 = 0;
            int var7 = 0;

            while(true) {
               int var8 = var6;
               if (var7 >= this.mSpanCount) {
                  while(var8 < this.mSpanCount) {
                     this.mSpans[var8].popEnd();
                     ++var8;
                  }
                  break;
               }

               if (this.mSpans[var7].mViews.size() == 1) {
                  return;
               }

               ++var7;
            }
         } else {
            if (var5.mSpan.mViews.size() == 1) {
               return;
            }

            var5.mSpan.popEnd();
         }

         this.removeAndRecycleView(var4, var1);
      }

   }

   private void recycleFromStart(RecyclerView.Recycler var1, int var2) {
      View var4;
      for(; this.getChildCount() > 0; this.removeAndRecycleView(var4, var1)) {
         byte var3 = 0;
         var4 = this.getChildAt(0);
         if (this.mPrimaryOrientation.getDecoratedEnd(var4) > var2 || this.mPrimaryOrientation.getTransformedEndWithDecoration(var4) > var2) {
            return;
         }

         StaggeredGridLayoutManager.LayoutParams var5 = (StaggeredGridLayoutManager.LayoutParams)var4.getLayoutParams();
         if (var5.mFullSpan) {
            int var6 = 0;

            while(true) {
               int var7 = var3;
               if (var6 >= this.mSpanCount) {
                  while(var7 < this.mSpanCount) {
                     this.mSpans[var7].popStart();
                     ++var7;
                  }
                  break;
               }

               if (this.mSpans[var6].mViews.size() == 1) {
                  return;
               }

               ++var6;
            }
         } else {
            if (var5.mSpan.mViews.size() == 1) {
               return;
            }

            var5.mSpan.popStart();
         }
      }

   }

   private void repositionToWrapContentIfNecessary() {
      if (this.mSecondaryOrientation.getMode() != 1073741824) {
         int var1 = this.getChildCount();
         byte var2 = 0;
         float var3 = 0.0F;

         int var4;
         View var5;
         for(var4 = 0; var4 < var1; ++var4) {
            var5 = this.getChildAt(var4);
            float var6 = (float)this.mSecondaryOrientation.getDecoratedMeasurement(var5);
            if (var6 >= var3) {
               float var7 = var6;
               if (((StaggeredGridLayoutManager.LayoutParams)var5.getLayoutParams()).isFullSpan()) {
                  var7 = 1.0F * var6 / (float)this.mSpanCount;
               }

               var3 = Math.max(var3, var7);
            }
         }

         int var8 = this.mSizePerSpan;
         int var9 = Math.round(var3 * (float)this.mSpanCount);
         var4 = var9;
         if (this.mSecondaryOrientation.getMode() == Integer.MIN_VALUE) {
            var4 = Math.min(var9, this.mSecondaryOrientation.getTotalSpace());
         }

         this.updateMeasureSpecs(var4);
         var4 = var2;
         if (this.mSizePerSpan != var8) {
            for(; var4 < var1; ++var4) {
               var5 = this.getChildAt(var4);
               StaggeredGridLayoutManager.LayoutParams var10 = (StaggeredGridLayoutManager.LayoutParams)var5.getLayoutParams();
               if (!var10.mFullSpan) {
                  if (this.isLayoutRTL() && this.mOrientation == 1) {
                     var5.offsetLeftAndRight(-(this.mSpanCount - 1 - var10.mSpan.mIndex) * this.mSizePerSpan - -(this.mSpanCount - 1 - var10.mSpan.mIndex) * var8);
                  } else {
                     var9 = var10.mSpan.mIndex * this.mSizePerSpan;
                     int var11 = var10.mSpan.mIndex * var8;
                     if (this.mOrientation == 1) {
                        var5.offsetLeftAndRight(var9 - var11);
                     } else {
                        var5.offsetTopAndBottom(var9 - var11);
                     }
                  }
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

   private void setLayoutStateDirection(int var1) {
      this.mLayoutState.mLayoutDirection = var1;
      LayoutState var2 = this.mLayoutState;
      boolean var3 = this.mShouldReverseLayout;
      byte var4 = 1;
      boolean var5;
      if (var1 == -1) {
         var5 = true;
      } else {
         var5 = false;
      }

      byte var6;
      if (var3 == var5) {
         var6 = var4;
      } else {
         var6 = -1;
      }

      var2.mItemDirection = var6;
   }

   private void updateAllRemainingSpans(int var1, int var2) {
      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         if (!this.mSpans[var3].mViews.isEmpty()) {
            this.updateRemainingSpans(this.mSpans[var3], var1, var2);
         }
      }

   }

   private boolean updateAnchorFromChildren(RecyclerView.State var1, StaggeredGridLayoutManager.AnchorInfo var2) {
      int var3;
      if (this.mLastLayoutFromEnd) {
         var3 = this.findLastReferenceChildPosition(var1.getItemCount());
      } else {
         var3 = this.findFirstReferenceChildPosition(var1.getItemCount());
      }

      var2.mPosition = var3;
      var2.mOffset = Integer.MIN_VALUE;
      return true;
   }

   private void updateLayoutState(int var1, RecyclerView.State var2) {
      boolean var4;
      int var5;
      boolean var7;
      label31: {
         LayoutState var3 = this.mLayoutState;
         var4 = false;
         var3.mAvailable = 0;
         this.mLayoutState.mCurrentPosition = var1;
         if (this.isSmoothScrolling()) {
            var5 = var2.getTargetScrollPosition();
            if (var5 != -1) {
               boolean var6 = this.mShouldReverseLayout;
               if (var5 < var1) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               if (var6 == var7) {
                  var5 = this.mPrimaryOrientation.getTotalSpace();
                  var1 = 0;
               } else {
                  var1 = this.mPrimaryOrientation.getTotalSpace();
                  var5 = 0;
               }
               break label31;
            }
         }

         var1 = 0;
         var5 = var1;
      }

      if (this.getClipToPadding()) {
         this.mLayoutState.mStartLine = this.mPrimaryOrientation.getStartAfterPadding() - var1;
         this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEndAfterPadding() + var5;
      } else {
         this.mLayoutState.mEndLine = this.mPrimaryOrientation.getEnd() + var5;
         this.mLayoutState.mStartLine = -var1;
      }

      this.mLayoutState.mStopInFocusable = false;
      this.mLayoutState.mRecycle = true;
      LayoutState var8 = this.mLayoutState;
      var7 = var4;
      if (this.mPrimaryOrientation.getMode() == 0) {
         var7 = var4;
         if (this.mPrimaryOrientation.getEnd() == 0) {
            var7 = true;
         }
      }

      var8.mInfinite = var7;
   }

   private void updateRemainingSpans(StaggeredGridLayoutManager.Span var1, int var2, int var3) {
      int var4 = var1.getDeletedSize();
      if (var2 == -1) {
         if (var1.getStartLine() + var4 <= var3) {
            this.mRemainingSpans.set(var1.mIndex, false);
         }
      } else if (var1.getEndLine() - var4 >= var3) {
         this.mRemainingSpans.set(var1.mIndex, false);
      }

   }

   private int updateSpecWithExtra(int var1, int var2, int var3) {
      if (var2 == 0 && var3 == 0) {
         return var1;
      } else {
         int var4 = MeasureSpec.getMode(var1);
         return var4 != Integer.MIN_VALUE && var4 != 1073741824 ? var1 : MeasureSpec.makeMeasureSpec(Math.max(0, MeasureSpec.getSize(var1) - var2 - var3), var4);
      }
   }

   boolean areAllEndsEqual() {
      int var1 = this.mSpans[0].getEndLine(Integer.MIN_VALUE);

      for(int var2 = 1; var2 < this.mSpanCount; ++var2) {
         if (this.mSpans[var2].getEndLine(Integer.MIN_VALUE) != var1) {
            return false;
         }
      }

      return true;
   }

   boolean areAllStartsEqual() {
      int var1 = this.mSpans[0].getStartLine(Integer.MIN_VALUE);

      for(int var2 = 1; var2 < this.mSpanCount; ++var2) {
         if (this.mSpans[var2].getStartLine(Integer.MIN_VALUE) != var1) {
            return false;
         }
      }

      return true;
   }

   public void assertNotInLayoutOrScroll(String var1) {
      if (this.mPendingSavedState == null) {
         super.assertNotInLayoutOrScroll(var1);
      }

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

   boolean checkForGaps() {
      if (this.getChildCount() != 0 && this.mGapStrategy != 0 && this.isAttachedToWindow()) {
         int var1;
         int var2;
         if (this.mShouldReverseLayout) {
            var1 = this.getLastChildPosition();
            var2 = this.getFirstChildPosition();
         } else {
            var1 = this.getFirstChildPosition();
            var2 = this.getLastChildPosition();
         }

         if (var1 == 0 && this.hasGapsToFix() != null) {
            this.mLazySpanLookup.clear();
            this.requestSimpleAnimationsInNextLayout();
            this.requestLayout();
            return true;
         } else if (!this.mLaidOutInvalidFullSpan) {
            return false;
         } else {
            byte var3;
            if (this.mShouldReverseLayout) {
               var3 = -1;
            } else {
               var3 = 1;
            }

            StaggeredGridLayoutManager.LazySpanLookup var4 = this.mLazySpanLookup;
            ++var2;
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var6 = var4.getFirstFullSpanItemInRange(var1, var2, var3, true);
            if (var6 == null) {
               this.mLaidOutInvalidFullSpan = false;
               this.mLazySpanLookup.forceInvalidateAfter(var2);
               return false;
            } else {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var5 = this.mLazySpanLookup.getFirstFullSpanItemInRange(var1, var6.mPosition, var3 * -1, true);
               if (var5 == null) {
                  this.mLazySpanLookup.forceInvalidateAfter(var6.mPosition);
               } else {
                  this.mLazySpanLookup.forceInvalidateAfter(var5.mPosition + 1);
               }

               this.requestSimpleAnimationsInNextLayout();
               this.requestLayout();
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public boolean checkLayoutParams(RecyclerView.LayoutParams var1) {
      return var1 instanceof StaggeredGridLayoutManager.LayoutParams;
   }

   public void collectAdjacentPrefetchPositions(int var1, int var2, RecyclerView.State var3, RecyclerView.LayoutManager.LayoutPrefetchRegistry var4) {
      if (this.mOrientation != 0) {
         var1 = var2;
      }

      if (this.getChildCount() != 0 && var1 != 0) {
         this.prepareLayoutStateForDelta(var1, var3);
         if (this.mPrefetchDistances == null || this.mPrefetchDistances.length < this.mSpanCount) {
            this.mPrefetchDistances = new int[this.mSpanCount];
         }

         byte var5 = 0;
         var2 = 0;

         int var7;
         for(var1 = var2; var2 < this.mSpanCount; var1 = var7) {
            int var6;
            if (this.mLayoutState.mItemDirection == -1) {
               var6 = this.mLayoutState.mStartLine - this.mSpans[var2].getStartLine(this.mLayoutState.mStartLine);
            } else {
               var6 = this.mSpans[var2].getEndLine(this.mLayoutState.mEndLine) - this.mLayoutState.mEndLine;
            }

            var7 = var1;
            if (var6 >= 0) {
               this.mPrefetchDistances[var1] = var6;
               var7 = var1 + 1;
            }

            ++var2;
         }

         Arrays.sort(this.mPrefetchDistances, 0, var1);

         for(var2 = var5; var2 < var1 && this.mLayoutState.hasMore(var3); ++var2) {
            var4.addPosition(this.mLayoutState.mCurrentPosition, this.mPrefetchDistances[var2]);
            LayoutState var8 = this.mLayoutState;
            var8.mCurrentPosition += this.mLayoutState.mItemDirection;
         }

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
      var1 = this.calculateScrollDirectionForPosition(var1);
      PointF var2 = new PointF();
      if (var1 == 0) {
         return null;
      } else {
         if (this.mOrientation == 0) {
            var2.x = (float)var1;
            var2.y = 0.0F;
         } else {
            var2.x = 0.0F;
            var2.y = (float)var1;
         }

         return var2;
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

   public int[] findFirstCompletelyVisibleItemPositions(int[] var1) {
      int[] var2;
      if (var1 == null) {
         var2 = new int[this.mSpanCount];
      } else {
         var2 = var1;
         if (var1.length < this.mSpanCount) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            var4.append(this.mSpanCount);
            var4.append(", array size:");
            var4.append(var1.length);
            throw new IllegalArgumentException(var4.toString());
         }
      }

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         var2[var3] = this.mSpans[var3].findFirstCompletelyVisibleItemPosition();
      }

      return var2;
   }

   View findFirstVisibleItemClosestToEnd(boolean var1) {
      int var2 = this.mPrimaryOrientation.getStartAfterPadding();
      int var3 = this.mPrimaryOrientation.getEndAfterPadding();
      int var4 = this.getChildCount() - 1;

      View var5;
      View var9;
      for(var5 = null; var4 >= 0; var5 = var9) {
         View var6 = this.getChildAt(var4);
         int var7 = this.mPrimaryOrientation.getDecoratedStart(var6);
         int var8 = this.mPrimaryOrientation.getDecoratedEnd(var6);
         var9 = var5;
         if (var8 > var2) {
            if (var7 >= var3) {
               var9 = var5;
            } else {
               if (var8 <= var3 || !var1) {
                  return var6;
               }

               var9 = var5;
               if (var5 == null) {
                  var9 = var6;
               }
            }
         }

         --var4;
      }

      return var5;
   }

   View findFirstVisibleItemClosestToStart(boolean var1) {
      int var2 = this.mPrimaryOrientation.getStartAfterPadding();
      int var3 = this.mPrimaryOrientation.getEndAfterPadding();
      int var4 = this.getChildCount();
      View var5 = null;

      View var9;
      for(int var6 = 0; var6 < var4; var5 = var9) {
         View var7 = this.getChildAt(var6);
         int var8 = this.mPrimaryOrientation.getDecoratedStart(var7);
         var9 = var5;
         if (this.mPrimaryOrientation.getDecoratedEnd(var7) > var2) {
            if (var8 >= var3) {
               var9 = var5;
            } else {
               if (var8 >= var2 || !var1) {
                  return var7;
               }

               var9 = var5;
               if (var5 == null) {
                  var9 = var7;
               }
            }
         }

         ++var6;
      }

      return var5;
   }

   int findFirstVisibleItemPositionInt() {
      View var1;
      if (this.mShouldReverseLayout) {
         var1 = this.findFirstVisibleItemClosestToEnd(true);
      } else {
         var1 = this.findFirstVisibleItemClosestToStart(true);
      }

      int var2;
      if (var1 == null) {
         var2 = -1;
      } else {
         var2 = this.getPosition(var1);
      }

      return var2;
   }

   public int[] findFirstVisibleItemPositions(int[] var1) {
      int[] var2;
      if (var1 == null) {
         var2 = new int[this.mSpanCount];
      } else {
         var2 = var1;
         if (var1.length < this.mSpanCount) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            var4.append(this.mSpanCount);
            var4.append(", array size:");
            var4.append(var1.length);
            throw new IllegalArgumentException(var4.toString());
         }
      }

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         var2[var3] = this.mSpans[var3].findFirstVisibleItemPosition();
      }

      return var2;
   }

   public int[] findLastCompletelyVisibleItemPositions(int[] var1) {
      int[] var2;
      if (var1 == null) {
         var2 = new int[this.mSpanCount];
      } else {
         var2 = var1;
         if (var1.length < this.mSpanCount) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            var4.append(this.mSpanCount);
            var4.append(", array size:");
            var4.append(var1.length);
            throw new IllegalArgumentException(var4.toString());
         }
      }

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         var2[var3] = this.mSpans[var3].findLastCompletelyVisibleItemPosition();
      }

      return var2;
   }

   public int[] findLastVisibleItemPositions(int[] var1) {
      int[] var2;
      if (var1 == null) {
         var2 = new int[this.mSpanCount];
      } else {
         var2 = var1;
         if (var1.length < this.mSpanCount) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Provided int[]'s size must be more than or equal to span count. Expected:");
            var4.append(this.mSpanCount);
            var4.append(", array size:");
            var4.append(var1.length);
            throw new IllegalArgumentException(var4.toString());
         }
      }

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         var2[var3] = this.mSpans[var3].findLastVisibleItemPosition();
      }

      return var2;
   }

   public RecyclerView.LayoutParams generateDefaultLayoutParams() {
      return this.mOrientation == 0 ? new StaggeredGridLayoutManager.LayoutParams(-2, -1) : new StaggeredGridLayoutManager.LayoutParams(-1, -2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(Context var1, AttributeSet var2) {
      return new StaggeredGridLayoutManager.LayoutParams(var1, var2);
   }

   public RecyclerView.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      return var1 instanceof MarginLayoutParams ? new StaggeredGridLayoutManager.LayoutParams((MarginLayoutParams)var1) : new StaggeredGridLayoutManager.LayoutParams(var1);
   }

   public int getColumnCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mOrientation == 1 ? this.mSpanCount : super.getColumnCountForAccessibility(var1, var2);
   }

   int getFirstChildPosition() {
      int var1 = this.getChildCount();
      int var2 = 0;
      if (var1 != 0) {
         var2 = this.getPosition(this.getChildAt(0));
      }

      return var2;
   }

   public int getGapStrategy() {
      return this.mGapStrategy;
   }

   int getLastChildPosition() {
      int var1 = this.getChildCount();
      if (var1 == 0) {
         var1 = 0;
      } else {
         var1 = this.getPosition(this.getChildAt(var1 - 1));
      }

      return var1;
   }

   public int getOrientation() {
      return this.mOrientation;
   }

   public boolean getReverseLayout() {
      return this.mReverseLayout;
   }

   public int getRowCountForAccessibility(RecyclerView.Recycler var1, RecyclerView.State var2) {
      return this.mOrientation == 0 ? this.mSpanCount : super.getRowCountForAccessibility(var1, var2);
   }

   public int getSpanCount() {
      return this.mSpanCount;
   }

   View hasGapsToFix() {
      int var1 = this.getChildCount() - 1;
      BitSet var2 = new BitSet(this.mSpanCount);
      var2.set(0, this.mSpanCount, true);
      int var3 = this.mOrientation;
      byte var4 = -1;
      byte var12;
      if (var3 == 1 && this.isLayoutRTL()) {
         var12 = 1;
      } else {
         var12 = -1;
      }

      int var5;
      if (this.mShouldReverseLayout) {
         var5 = -1;
      } else {
         var5 = var1 + 1;
         var1 = 0;
      }

      int var6 = var1;
      if (var1 < var5) {
         var4 = 1;
         var6 = var1;
      }

      for(; var6 != var5; var6 += var4) {
         View var7 = this.getChildAt(var6);
         StaggeredGridLayoutManager.LayoutParams var8 = (StaggeredGridLayoutManager.LayoutParams)var7.getLayoutParams();
         if (var2.get(var8.mSpan.mIndex)) {
            if (this.checkSpanForGap(var8.mSpan)) {
               return var7;
            }

            var2.clear(var8.mSpan.mIndex);
         }

         if (!var8.mFullSpan) {
            var1 = var6 + var4;
            if (var1 != var5) {
               View var9;
               boolean var11;
               label71: {
                  label70: {
                     var9 = this.getChildAt(var1);
                     int var10;
                     if (this.mShouldReverseLayout) {
                        var10 = this.mPrimaryOrientation.getDecoratedEnd(var7);
                        var1 = this.mPrimaryOrientation.getDecoratedEnd(var9);
                        if (var10 < var1) {
                           return var7;
                        }

                        if (var10 == var1) {
                           break label70;
                        }
                     } else {
                        var10 = this.mPrimaryOrientation.getDecoratedStart(var7);
                        var1 = this.mPrimaryOrientation.getDecoratedStart(var9);
                        if (var10 > var1) {
                           return var7;
                        }

                        if (var10 == var1) {
                           break label70;
                        }
                     }

                     var11 = false;
                     break label71;
                  }

                  var11 = true;
               }

               if (var11) {
                  StaggeredGridLayoutManager.LayoutParams var13 = (StaggeredGridLayoutManager.LayoutParams)var9.getLayoutParams();
                  if (var8.mSpan.mIndex - var13.mSpan.mIndex < 0) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  boolean var14;
                  if (var12 < 0) {
                     var14 = true;
                  } else {
                     var14 = false;
                  }

                  if (var11 != var14) {
                     return var7;
                  }
               }
            }
         }
      }

      return null;
   }

   public void invalidateSpanAssignments() {
      this.mLazySpanLookup.clear();
      this.requestLayout();
   }

   boolean isLayoutRTL() {
      int var1 = this.getLayoutDirection();
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   public void offsetChildrenHorizontal(int var1) {
      super.offsetChildrenHorizontal(var1);

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         this.mSpans[var2].onOffset(var1);
      }

   }

   public void offsetChildrenVertical(int var1) {
      super.offsetChildrenVertical(var1);

      for(int var2 = 0; var2 < this.mSpanCount; ++var2) {
         this.mSpans[var2].onOffset(var1);
      }

   }

   public void onDetachedFromWindow(RecyclerView var1, RecyclerView.Recycler var2) {
      this.removeCallbacks(this.mCheckForGapsRunnable);

      for(int var3 = 0; var3 < this.mSpanCount; ++var3) {
         this.mSpans[var3].clear();
      }

      var1.requestLayout();
   }

   @Nullable
   public View onFocusSearchFailed(View var1, int var2, RecyclerView.Recycler var3, RecyclerView.State var4) {
      if (this.getChildCount() == 0) {
         return null;
      } else {
         var1 = this.findContainingItemView(var1);
         if (var1 == null) {
            return null;
         } else {
            this.resolveShouldLayoutReverse();
            int var5 = this.convertFocusDirectionToLayoutDirection(var2);
            if (var5 == Integer.MIN_VALUE) {
               return null;
            } else {
               StaggeredGridLayoutManager.LayoutParams var6 = (StaggeredGridLayoutManager.LayoutParams)var1.getLayoutParams();
               boolean var7 = var6.mFullSpan;
               StaggeredGridLayoutManager.Span var14 = var6.mSpan;
               if (var5 == 1) {
                  var2 = this.getLastChildPosition();
               } else {
                  var2 = this.getFirstChildPosition();
               }

               this.updateLayoutState(var2, var4);
               this.setLayoutStateDirection(var5);
               this.mLayoutState.mCurrentPosition = this.mLayoutState.mItemDirection + var2;
               this.mLayoutState.mAvailable = (int)(0.33333334F * (float)this.mPrimaryOrientation.getTotalSpace());
               this.mLayoutState.mStopInFocusable = true;
               LayoutState var8 = this.mLayoutState;
               byte var9 = 0;
               var8.mRecycle = false;
               this.fill(var3, this.mLayoutState, var4);
               this.mLastLayoutFromEnd = this.mShouldReverseLayout;
               View var12;
               if (!var7) {
                  var12 = var14.getFocusableViewAfter(var2, var5);
                  if (var12 != null && var12 != var1) {
                     return var12;
                  }
               }

               int var10;
               if (this.preferLastSpan(var5)) {
                  for(var10 = this.mSpanCount - 1; var10 >= 0; --var10) {
                     var12 = this.mSpans[var10].getFocusableViewAfter(var2, var5);
                     if (var12 != null && var12 != var1) {
                        return var12;
                     }
                  }
               } else {
                  for(var10 = 0; var10 < this.mSpanCount; ++var10) {
                     var12 = this.mSpans[var10].getFocusableViewAfter(var2, var5);
                     if (var12 != null && var12 != var1) {
                        return var12;
                     }
                  }
               }

               boolean var11 = this.mReverseLayout;
               boolean var13;
               if (var5 == -1) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               if ((var11 ^ true) == var13) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               if (!var7) {
                  if (var13) {
                     var10 = var14.findFirstPartiallyVisibleItemPosition();
                  } else {
                     var10 = var14.findLastPartiallyVisibleItemPosition();
                  }

                  var12 = this.findViewByPosition(var10);
                  if (var12 != null && var12 != var1) {
                     return var12;
                  }
               }

               var10 = var9;
               int var15;
               if (this.preferLastSpan(var5)) {
                  for(var10 = this.mSpanCount - 1; var10 >= 0; --var10) {
                     if (var10 != var14.mIndex) {
                        if (var13) {
                           var15 = this.mSpans[var10].findFirstPartiallyVisibleItemPosition();
                        } else {
                           var15 = this.mSpans[var10].findLastPartiallyVisibleItemPosition();
                        }

                        var12 = this.findViewByPosition(var15);
                        if (var12 != null && var12 != var1) {
                           return var12;
                        }
                     }
                  }
               } else {
                  while(var10 < this.mSpanCount) {
                     if (var13) {
                        var15 = this.mSpans[var10].findFirstPartiallyVisibleItemPosition();
                     } else {
                        var15 = this.mSpans[var10].findLastPartiallyVisibleItemPosition();
                     }

                     var12 = this.findViewByPosition(var15);
                     if (var12 != null && var12 != var1) {
                        return var12;
                     }

                     ++var10;
                  }
               }

               return null;
            }
         }
      }
   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      if (this.getChildCount() > 0) {
         View var2 = this.findFirstVisibleItemClosestToStart(false);
         View var3 = this.findFirstVisibleItemClosestToEnd(false);
         if (var2 == null || var3 == null) {
            return;
         }

         int var4 = this.getPosition(var2);
         int var5 = this.getPosition(var3);
         if (var4 < var5) {
            var1.setFromIndex(var4);
            var1.setToIndex(var5);
         } else {
            var1.setFromIndex(var5);
            var1.setToIndex(var4);
         }
      }

   }

   public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler var1, RecyclerView.State var2, View var3, AccessibilityNodeInfoCompat var4) {
      android.view.ViewGroup.LayoutParams var8 = var3.getLayoutParams();
      if (!(var8 instanceof StaggeredGridLayoutManager.LayoutParams)) {
         super.onInitializeAccessibilityNodeInfoForItem(var3, var4);
      } else {
         StaggeredGridLayoutManager.LayoutParams var9 = (StaggeredGridLayoutManager.LayoutParams)var8;
         int var5 = this.mOrientation;
         byte var6 = 1;
         int var7 = 1;
         if (var5 == 0) {
            int var10 = var9.getSpanIndex();
            if (var9.mFullSpan) {
               var7 = this.mSpanCount;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(var10, var7, -1, -1, var9.mFullSpan, false));
         } else {
            var5 = var9.getSpanIndex();
            var7 = var6;
            if (var9.mFullSpan) {
               var7 = this.mSpanCount;
            }

            var4.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(-1, -1, var5, var7, var9.mFullSpan, false));
         }

      }
   }

   public void onItemsAdded(RecyclerView var1, int var2, int var3) {
      this.handleUpdate(var2, var3, 1);
   }

   public void onItemsChanged(RecyclerView var1) {
      this.mLazySpanLookup.clear();
      this.requestLayout();
   }

   public void onItemsMoved(RecyclerView var1, int var2, int var3, int var4) {
      this.handleUpdate(var2, var3, 8);
   }

   public void onItemsRemoved(RecyclerView var1, int var2, int var3) {
      this.handleUpdate(var2, var3, 2);
   }

   public void onItemsUpdated(RecyclerView var1, int var2, int var3, Object var4) {
      this.handleUpdate(var2, var3, 4);
   }

   public void onLayoutChildren(RecyclerView.Recycler var1, RecyclerView.State var2) {
      this.onLayoutChildren(var1, var2, true);
   }

   public void onLayoutCompleted(RecyclerView.State var1) {
      super.onLayoutCompleted(var1);
      this.mPendingScrollPosition = -1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.mPendingSavedState = null;
      this.mAnchorInfo.reset();
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (var1 instanceof StaggeredGridLayoutManager.SavedState) {
         this.mPendingSavedState = (StaggeredGridLayoutManager.SavedState)var1;
         this.requestLayout();
      }

   }

   public Parcelable onSaveInstanceState() {
      if (this.mPendingSavedState != null) {
         return new StaggeredGridLayoutManager.SavedState(this.mPendingSavedState);
      } else {
         StaggeredGridLayoutManager.SavedState var1 = new StaggeredGridLayoutManager.SavedState();
         var1.mReverseLayout = this.mReverseLayout;
         var1.mAnchorLayoutFromEnd = this.mLastLayoutFromEnd;
         var1.mLastLayoutRTL = this.mLastLayoutRTL;
         StaggeredGridLayoutManager.LazySpanLookup var2 = this.mLazySpanLookup;
         int var3 = 0;
         if (var2 != null && this.mLazySpanLookup.mData != null) {
            var1.mSpanLookup = this.mLazySpanLookup.mData;
            var1.mSpanLookupSize = var1.mSpanLookup.length;
            var1.mFullSpanItems = this.mLazySpanLookup.mFullSpanItems;
         } else {
            var1.mSpanLookupSize = 0;
         }

         if (this.getChildCount() > 0) {
            int var4;
            if (this.mLastLayoutFromEnd) {
               var4 = this.getLastChildPosition();
            } else {
               var4 = this.getFirstChildPosition();
            }

            var1.mAnchorPosition = var4;
            var1.mVisibleAnchorPosition = this.findFirstVisibleItemPositionInt();
            var1.mSpanOffsetsSize = this.mSpanCount;

            for(var1.mSpanOffsets = new int[this.mSpanCount]; var3 < this.mSpanCount; ++var3) {
               int var5;
               if (this.mLastLayoutFromEnd) {
                  var5 = this.mSpans[var3].getEndLine(Integer.MIN_VALUE);
                  var4 = var5;
                  if (var5 != Integer.MIN_VALUE) {
                     var4 = var5 - this.mPrimaryOrientation.getEndAfterPadding();
                  }
               } else {
                  var5 = this.mSpans[var3].getStartLine(Integer.MIN_VALUE);
                  var4 = var5;
                  if (var5 != Integer.MIN_VALUE) {
                     var4 = var5 - this.mPrimaryOrientation.getStartAfterPadding();
                  }
               }

               var1.mSpanOffsets[var3] = var4;
            }
         } else {
            var1.mAnchorPosition = -1;
            var1.mVisibleAnchorPosition = -1;
            var1.mSpanOffsetsSize = 0;
         }

         return var1;
      }
   }

   public void onScrollStateChanged(int var1) {
      if (var1 == 0) {
         this.checkForGaps();
      }

   }

   void prepareLayoutStateForDelta(int var1, RecyclerView.State var2) {
      int var3;
      byte var4;
      if (var1 > 0) {
         var3 = this.getLastChildPosition();
         var4 = 1;
      } else {
         var3 = this.getFirstChildPosition();
         var4 = -1;
      }

      this.mLayoutState.mRecycle = true;
      this.updateLayoutState(var3, var2);
      this.setLayoutStateDirection(var4);
      this.mLayoutState.mCurrentPosition = var3 + this.mLayoutState.mItemDirection;
      this.mLayoutState.mAvailable = Math.abs(var1);
   }

   int scrollBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      if (this.getChildCount() != 0 && var1 != 0) {
         this.prepareLayoutStateForDelta(var1, var3);
         int var4 = this.fill(var2, this.mLayoutState, var3);
         if (this.mLayoutState.mAvailable >= var4) {
            if (var1 < 0) {
               var1 = -var4;
            } else {
               var1 = var4;
            }
         }

         this.mPrimaryOrientation.offsetChildren(-var1);
         this.mLastLayoutFromEnd = this.mShouldReverseLayout;
         this.mLayoutState.mAvailable = 0;
         this.recycle(var2, this.mLayoutState);
         return var1;
      } else {
         return 0;
      }
   }

   public int scrollHorizontallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.scrollBy(var1, var2, var3);
   }

   public void scrollToPosition(int var1) {
      if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != var1) {
         this.mPendingSavedState.invalidateAnchorPositionInfo();
      }

      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
      this.requestLayout();
   }

   public void scrollToPositionWithOffset(int var1, int var2) {
      if (this.mPendingSavedState != null) {
         this.mPendingSavedState.invalidateAnchorPositionInfo();
      }

      this.mPendingScrollPosition = var1;
      this.mPendingScrollPositionOffset = var2;
      this.requestLayout();
   }

   public int scrollVerticallyBy(int var1, RecyclerView.Recycler var2, RecyclerView.State var3) {
      return this.scrollBy(var1, var2, var3);
   }

   public void setGapStrategy(int var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (var1 != this.mGapStrategy) {
         if (var1 != 0 && var1 != 2) {
            throw new IllegalArgumentException("invalid gap strategy. Must be GAP_HANDLING_NONE or GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS");
         } else {
            this.mGapStrategy = var1;
            boolean var2;
            if (this.mGapStrategy != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            this.setAutoMeasureEnabled(var2);
            this.requestLayout();
         }
      }
   }

   public void setMeasuredDimension(Rect var1, int var2, int var3) {
      int var4 = this.getPaddingLeft() + this.getPaddingRight();
      int var5 = this.getPaddingTop() + this.getPaddingBottom();
      if (this.mOrientation == 1) {
         var3 = chooseSize(var3, var1.height() + var5, this.getMinimumHeight());
         var2 = chooseSize(var2, this.mSizePerSpan * this.mSpanCount + var4, this.getMinimumWidth());
      } else {
         var2 = chooseSize(var2, var1.width() + var4, this.getMinimumWidth());
         var3 = chooseSize(var3, this.mSizePerSpan * this.mSpanCount + var5, this.getMinimumHeight());
      }

      this.setMeasuredDimension(var2, var3);
   }

   public void setOrientation(int var1) {
      if (var1 != 0 && var1 != 1) {
         throw new IllegalArgumentException("invalid orientation.");
      } else {
         this.assertNotInLayoutOrScroll((String)null);
         if (var1 != this.mOrientation) {
            this.mOrientation = var1;
            OrientationHelper var2 = this.mPrimaryOrientation;
            this.mPrimaryOrientation = this.mSecondaryOrientation;
            this.mSecondaryOrientation = var2;
            this.requestLayout();
         }
      }
   }

   public void setReverseLayout(boolean var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (this.mPendingSavedState != null && this.mPendingSavedState.mReverseLayout != var1) {
         this.mPendingSavedState.mReverseLayout = var1;
      }

      this.mReverseLayout = var1;
      this.requestLayout();
   }

   public void setSpanCount(int var1) {
      this.assertNotInLayoutOrScroll((String)null);
      if (var1 != this.mSpanCount) {
         this.invalidateSpanAssignments();
         this.mSpanCount = var1;
         this.mRemainingSpans = new BitSet(this.mSpanCount);
         this.mSpans = new StaggeredGridLayoutManager.Span[this.mSpanCount];

         for(var1 = 0; var1 < this.mSpanCount; ++var1) {
            this.mSpans[var1] = new StaggeredGridLayoutManager.Span(var1);
         }

         this.requestLayout();
      }

   }

   public void smoothScrollToPosition(RecyclerView var1, RecyclerView.State var2, int var3) {
      LinearSmoothScroller var4 = new LinearSmoothScroller(var1.getContext());
      var4.setTargetPosition(var3);
      this.startSmoothScroll(var4);
   }

   public boolean supportsPredictiveItemAnimations() {
      boolean var1;
      if (this.mPendingSavedState == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean updateAnchorFromPendingData(RecyclerView.State var1, StaggeredGridLayoutManager.AnchorInfo var2) {
      boolean var3 = var1.isPreLayout();
      boolean var4 = false;
      if (!var3 && this.mPendingScrollPosition != -1) {
         if (this.mPendingScrollPosition >= 0 && this.mPendingScrollPosition < var1.getItemCount()) {
            if (this.mPendingSavedState != null && this.mPendingSavedState.mAnchorPosition != -1 && this.mPendingSavedState.mSpanOffsetsSize >= 1) {
               var2.mOffset = Integer.MIN_VALUE;
               var2.mPosition = this.mPendingScrollPosition;
            } else {
               View var6 = this.findViewByPosition(this.mPendingScrollPosition);
               if (var6 != null) {
                  int var5;
                  if (this.mShouldReverseLayout) {
                     var5 = this.getLastChildPosition();
                  } else {
                     var5 = this.getFirstChildPosition();
                  }

                  var2.mPosition = var5;
                  if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                     if (var2.mLayoutFromEnd) {
                        var2.mOffset = this.mPrimaryOrientation.getEndAfterPadding() - this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedEnd(var6);
                     } else {
                        var2.mOffset = this.mPrimaryOrientation.getStartAfterPadding() + this.mPendingScrollPositionOffset - this.mPrimaryOrientation.getDecoratedStart(var6);
                     }

                     return true;
                  }

                  if (this.mPrimaryOrientation.getDecoratedMeasurement(var6) > this.mPrimaryOrientation.getTotalSpace()) {
                     if (var2.mLayoutFromEnd) {
                        var5 = this.mPrimaryOrientation.getEndAfterPadding();
                     } else {
                        var5 = this.mPrimaryOrientation.getStartAfterPadding();
                     }

                     var2.mOffset = var5;
                     return true;
                  }

                  var5 = this.mPrimaryOrientation.getDecoratedStart(var6) - this.mPrimaryOrientation.getStartAfterPadding();
                  if (var5 < 0) {
                     var2.mOffset = -var5;
                     return true;
                  }

                  var5 = this.mPrimaryOrientation.getEndAfterPadding() - this.mPrimaryOrientation.getDecoratedEnd(var6);
                  if (var5 < 0) {
                     var2.mOffset = var5;
                     return true;
                  }

                  var2.mOffset = Integer.MIN_VALUE;
               } else {
                  var2.mPosition = this.mPendingScrollPosition;
                  if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
                     if (this.calculateScrollDirectionForPosition(var2.mPosition) == 1) {
                        var4 = true;
                     }

                     var2.mLayoutFromEnd = var4;
                     var2.assignCoordinateFromPadding();
                  } else {
                     var2.assignCoordinateFromPadding(this.mPendingScrollPositionOffset);
                  }

                  var2.mInvalidateOffsets = true;
               }
            }

            return true;
         } else {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
         }
      } else {
         return false;
      }
   }

   void updateAnchorInfoForLayout(RecyclerView.State var1, StaggeredGridLayoutManager.AnchorInfo var2) {
      if (!this.updateAnchorFromPendingData(var1, var2)) {
         if (!this.updateAnchorFromChildren(var1, var2)) {
            var2.assignCoordinateFromPadding();
            var2.mPosition = 0;
         }
      }
   }

   void updateMeasureSpecs(int var1) {
      this.mSizePerSpan = var1 / this.mSpanCount;
      this.mFullSizeSpec = MeasureSpec.makeMeasureSpec(var1, this.mSecondaryOrientation.getMode());
   }

   class AnchorInfo {
      boolean mInvalidateOffsets;
      boolean mLayoutFromEnd;
      int mOffset;
      int mPosition;
      int[] mSpanReferenceLines;
      boolean mValid;

      AnchorInfo() {
         this.reset();
      }

      void assignCoordinateFromPadding() {
         int var1;
         if (this.mLayoutFromEnd) {
            var1 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
         } else {
            var1 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
         }

         this.mOffset = var1;
      }

      void assignCoordinateFromPadding(int var1) {
         if (this.mLayoutFromEnd) {
            this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding() - var1;
         } else {
            this.mOffset = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding() + var1;
         }

      }

      void reset() {
         this.mPosition = -1;
         this.mOffset = Integer.MIN_VALUE;
         this.mLayoutFromEnd = false;
         this.mInvalidateOffsets = false;
         this.mValid = false;
         if (this.mSpanReferenceLines != null) {
            Arrays.fill(this.mSpanReferenceLines, -1);
         }

      }

      void saveSpanReferenceLines(StaggeredGridLayoutManager.Span[] var1) {
         int var2 = var1.length;
         if (this.mSpanReferenceLines == null || this.mSpanReferenceLines.length < var2) {
            this.mSpanReferenceLines = new int[StaggeredGridLayoutManager.this.mSpans.length];
         }

         for(int var3 = 0; var3 < var2; ++var3) {
            this.mSpanReferenceLines[var3] = var1[var3].getStartLine(Integer.MIN_VALUE);
         }

      }
   }

   public static class LayoutParams extends RecyclerView.LayoutParams {
      public static final int INVALID_SPAN_ID = -1;
      boolean mFullSpan;
      StaggeredGridLayoutManager.Span mSpan;

      public LayoutParams(int var1, int var2) {
         super(var1, var2);
      }

      public LayoutParams(Context var1, AttributeSet var2) {
         super(var1, var2);
      }

      public LayoutParams(RecyclerView.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(android.view.ViewGroup.LayoutParams var1) {
         super(var1);
      }

      public LayoutParams(MarginLayoutParams var1) {
         super(var1);
      }

      public final int getSpanIndex() {
         return this.mSpan == null ? -1 : this.mSpan.mIndex;
      }

      public boolean isFullSpan() {
         return this.mFullSpan;
      }

      public void setFullSpan(boolean var1) {
         this.mFullSpan = var1;
      }
   }

   static class LazySpanLookup {
      private static final int MIN_SIZE = 10;
      int[] mData;
      List mFullSpanItems;

      private int invalidateFullSpansAfter(int var1) {
         if (this.mFullSpanItems == null) {
            return -1;
         } else {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var2 = this.getFullSpanItem(var1);
            if (var2 != null) {
               this.mFullSpanItems.remove(var2);
            }

            int var3 = this.mFullSpanItems.size();
            int var4 = 0;

            while(true) {
               if (var4 >= var3) {
                  var4 = -1;
                  break;
               }

               if (((StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var4)).mPosition >= var1) {
                  break;
               }

               ++var4;
            }

            if (var4 != -1) {
               var2 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var4);
               this.mFullSpanItems.remove(var4);
               return var2.mPosition;
            } else {
               return -1;
            }
         }
      }

      private void offsetFullSpansForAddition(int var1, int var2) {
         if (this.mFullSpanItems != null) {
            for(int var3 = this.mFullSpanItems.size() - 1; var3 >= 0; --var3) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var4 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var3);
               if (var4.mPosition >= var1) {
                  var4.mPosition += var2;
               }
            }

         }
      }

      private void offsetFullSpansForRemoval(int var1, int var2) {
         if (this.mFullSpanItems != null) {
            for(int var3 = this.mFullSpanItems.size() - 1; var3 >= 0; --var3) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var4 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var3);
               if (var4.mPosition >= var1) {
                  if (var4.mPosition < var1 + var2) {
                     this.mFullSpanItems.remove(var3);
                  } else {
                     var4.mPosition -= var2;
                  }
               }
            }

         }
      }

      public void addFullSpanItem(StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var1) {
         if (this.mFullSpanItems == null) {
            this.mFullSpanItems = new ArrayList();
         }

         int var2 = this.mFullSpanItems.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var4 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var3);
            if (var4.mPosition == var1.mPosition) {
               this.mFullSpanItems.remove(var3);
            }

            if (var4.mPosition >= var1.mPosition) {
               this.mFullSpanItems.add(var3, var1);
               return;
            }
         }

         this.mFullSpanItems.add(var1);
      }

      void clear() {
         if (this.mData != null) {
            Arrays.fill(this.mData, -1);
         }

         this.mFullSpanItems = null;
      }

      void ensureSize(int var1) {
         if (this.mData == null) {
            this.mData = new int[Math.max(var1, 10) + 1];
            Arrays.fill(this.mData, -1);
         } else if (var1 >= this.mData.length) {
            int[] var2 = this.mData;
            this.mData = new int[this.sizeForPosition(var1)];
            System.arraycopy(var2, 0, this.mData, 0, var2.length);
            Arrays.fill(this.mData, var2.length, this.mData.length, -1);
         }

      }

      int forceInvalidateAfter(int var1) {
         if (this.mFullSpanItems != null) {
            for(int var2 = this.mFullSpanItems.size() - 1; var2 >= 0; --var2) {
               if (((StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var2)).mPosition >= var1) {
                  this.mFullSpanItems.remove(var2);
               }
            }
         }

         return this.invalidateAfter(var1);
      }

      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem getFirstFullSpanItemInRange(int var1, int var2, int var3, boolean var4) {
         if (this.mFullSpanItems == null) {
            return null;
         } else {
            int var5 = this.mFullSpanItems.size();

            for(int var6 = 0; var6 < var5; ++var6) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var7 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var6);
               if (var7.mPosition >= var2) {
                  return null;
               }

               if (var7.mPosition >= var1 && (var3 == 0 || var7.mGapDir == var3 || var4 && var7.mHasUnwantedGapAfter)) {
                  return var7;
               }
            }

            return null;
         }
      }

      public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem getFullSpanItem(int var1) {
         if (this.mFullSpanItems == null) {
            return null;
         } else {
            for(int var2 = this.mFullSpanItems.size() - 1; var2 >= 0; --var2) {
               StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var3 = (StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem)this.mFullSpanItems.get(var2);
               if (var3.mPosition == var1) {
                  return var3;
               }
            }

            return null;
         }
      }

      int getSpan(int var1) {
         return this.mData != null && var1 < this.mData.length ? this.mData[var1] : -1;
      }

      int invalidateAfter(int var1) {
         if (this.mData == null) {
            return -1;
         } else if (var1 >= this.mData.length) {
            return -1;
         } else {
            int var2 = this.invalidateFullSpansAfter(var1);
            if (var2 == -1) {
               Arrays.fill(this.mData, var1, this.mData.length, -1);
               return this.mData.length;
            } else {
               int[] var3 = this.mData;
               ++var2;
               Arrays.fill(var3, var1, var2, -1);
               return var2;
            }
         }
      }

      void offsetForAddition(int var1, int var2) {
         if (this.mData != null && var1 < this.mData.length) {
            int var3 = var1 + var2;
            this.ensureSize(var3);
            System.arraycopy(this.mData, var1, this.mData, var3, this.mData.length - var1 - var2);
            Arrays.fill(this.mData, var1, var3, -1);
            this.offsetFullSpansForAddition(var1, var2);
         }
      }

      void offsetForRemoval(int var1, int var2) {
         if (this.mData != null && var1 < this.mData.length) {
            int var3 = var1 + var2;
            this.ensureSize(var3);
            System.arraycopy(this.mData, var3, this.mData, var1, this.mData.length - var1 - var2);
            Arrays.fill(this.mData, this.mData.length - var2, this.mData.length, -1);
            this.offsetFullSpansForRemoval(var1, var2);
         }
      }

      void setSpan(int var1, StaggeredGridLayoutManager.Span var2) {
         this.ensureSize(var1);
         this.mData[var1] = var2.mIndex;
      }

      int sizeForPosition(int var1) {
         int var2;
         for(var2 = this.mData.length; var2 <= var1; var2 *= 2) {
         }

         return var2;
      }

      static class FullSpanItem implements Parcelable {
         public static final Creator CREATOR = new Creator() {
            public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem createFromParcel(Parcel var1) {
               return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem(var1);
            }

            public StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[] newArray(int var1) {
               return new StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem[var1];
            }
         };
         int mGapDir;
         int[] mGapPerSpan;
         boolean mHasUnwantedGapAfter;
         int mPosition;

         FullSpanItem() {
         }

         FullSpanItem(Parcel var1) {
            this.mPosition = var1.readInt();
            this.mGapDir = var1.readInt();
            int var2 = var1.readInt();
            boolean var3 = true;
            if (var2 != 1) {
               var3 = false;
            }

            this.mHasUnwantedGapAfter = var3;
            var2 = var1.readInt();
            if (var2 > 0) {
               this.mGapPerSpan = new int[var2];
               var1.readIntArray(this.mGapPerSpan);
            }

         }

         public int describeContents() {
            return 0;
         }

         int getGapForSpan(int var1) {
            if (this.mGapPerSpan == null) {
               var1 = 0;
            } else {
               var1 = this.mGapPerSpan[var1];
            }

            return var1;
         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append("FullSpanItem{mPosition=");
            var1.append(this.mPosition);
            var1.append(", mGapDir=");
            var1.append(this.mGapDir);
            var1.append(", mHasUnwantedGapAfter=");
            var1.append(this.mHasUnwantedGapAfter);
            var1.append(", mGapPerSpan=");
            var1.append(Arrays.toString(this.mGapPerSpan));
            var1.append('}');
            return var1.toString();
         }

         public void writeToParcel(Parcel var1, int var2) {
            var1.writeInt(this.mPosition);
            var1.writeInt(this.mGapDir);
            var1.writeInt(this.mHasUnwantedGapAfter);
            if (this.mGapPerSpan != null && this.mGapPerSpan.length > 0) {
               var1.writeInt(this.mGapPerSpan.length);
               var1.writeIntArray(this.mGapPerSpan);
            } else {
               var1.writeInt(0);
            }

         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static class SavedState implements Parcelable {
      public static final Creator CREATOR = new Creator() {
         public StaggeredGridLayoutManager.SavedState createFromParcel(Parcel var1) {
            return new StaggeredGridLayoutManager.SavedState(var1);
         }

         public StaggeredGridLayoutManager.SavedState[] newArray(int var1) {
            return new StaggeredGridLayoutManager.SavedState[var1];
         }
      };
      boolean mAnchorLayoutFromEnd;
      int mAnchorPosition;
      List mFullSpanItems;
      boolean mLastLayoutRTL;
      boolean mReverseLayout;
      int[] mSpanLookup;
      int mSpanLookupSize;
      int[] mSpanOffsets;
      int mSpanOffsetsSize;
      int mVisibleAnchorPosition;

      public SavedState() {
      }

      SavedState(Parcel var1) {
         this.mAnchorPosition = var1.readInt();
         this.mVisibleAnchorPosition = var1.readInt();
         this.mSpanOffsetsSize = var1.readInt();
         if (this.mSpanOffsetsSize > 0) {
            this.mSpanOffsets = new int[this.mSpanOffsetsSize];
            var1.readIntArray(this.mSpanOffsets);
         }

         this.mSpanLookupSize = var1.readInt();
         if (this.mSpanLookupSize > 0) {
            this.mSpanLookup = new int[this.mSpanLookupSize];
            var1.readIntArray(this.mSpanLookup);
         }

         int var2 = var1.readInt();
         boolean var3 = false;
         boolean var4;
         if (var2 == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.mReverseLayout = var4;
         if (var1.readInt() == 1) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.mAnchorLayoutFromEnd = var4;
         var4 = var3;
         if (var1.readInt() == 1) {
            var4 = true;
         }

         this.mLastLayoutRTL = var4;
         this.mFullSpanItems = var1.readArrayList(StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem.class.getClassLoader());
      }

      public SavedState(StaggeredGridLayoutManager.SavedState var1) {
         this.mSpanOffsetsSize = var1.mSpanOffsetsSize;
         this.mAnchorPosition = var1.mAnchorPosition;
         this.mVisibleAnchorPosition = var1.mVisibleAnchorPosition;
         this.mSpanOffsets = var1.mSpanOffsets;
         this.mSpanLookupSize = var1.mSpanLookupSize;
         this.mSpanLookup = var1.mSpanLookup;
         this.mReverseLayout = var1.mReverseLayout;
         this.mAnchorLayoutFromEnd = var1.mAnchorLayoutFromEnd;
         this.mLastLayoutRTL = var1.mLastLayoutRTL;
         this.mFullSpanItems = var1.mFullSpanItems;
      }

      public int describeContents() {
         return 0;
      }

      void invalidateAnchorPositionInfo() {
         this.mSpanOffsets = null;
         this.mSpanOffsetsSize = 0;
         this.mAnchorPosition = -1;
         this.mVisibleAnchorPosition = -1;
      }

      void invalidateSpanInfo() {
         this.mSpanOffsets = null;
         this.mSpanOffsetsSize = 0;
         this.mSpanLookupSize = 0;
         this.mSpanLookup = null;
         this.mFullSpanItems = null;
      }

      public void writeToParcel(Parcel var1, int var2) {
         var1.writeInt(this.mAnchorPosition);
         var1.writeInt(this.mVisibleAnchorPosition);
         var1.writeInt(this.mSpanOffsetsSize);
         if (this.mSpanOffsetsSize > 0) {
            var1.writeIntArray(this.mSpanOffsets);
         }

         var1.writeInt(this.mSpanLookupSize);
         if (this.mSpanLookupSize > 0) {
            var1.writeIntArray(this.mSpanLookup);
         }

         var1.writeInt(this.mReverseLayout);
         var1.writeInt(this.mAnchorLayoutFromEnd);
         var1.writeInt(this.mLastLayoutRTL);
         var1.writeList(this.mFullSpanItems);
      }
   }

   class Span {
      static final int INVALID_LINE = Integer.MIN_VALUE;
      int mCachedEnd = Integer.MIN_VALUE;
      int mCachedStart = Integer.MIN_VALUE;
      int mDeletedSize = 0;
      final int mIndex;
      ArrayList mViews = new ArrayList();

      Span(int var2) {
         this.mIndex = var2;
      }

      void appendToSpan(View var1) {
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         var2.mSpan = this;
         this.mViews.add(var1);
         this.mCachedEnd = Integer.MIN_VALUE;
         if (this.mViews.size() == 1) {
            this.mCachedStart = Integer.MIN_VALUE;
         }

         if (var2.isItemRemoved() || var2.isItemChanged()) {
            this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var1);
         }

      }

      void cacheReferenceLineAndClear(boolean var1, int var2) {
         int var3;
         if (var1) {
            var3 = this.getEndLine(Integer.MIN_VALUE);
         } else {
            var3 = this.getStartLine(Integer.MIN_VALUE);
         }

         this.clear();
         if (var3 != Integer.MIN_VALUE) {
            if ((!var1 || var3 >= StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding()) && (var1 || var3 <= StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding())) {
               int var4 = var3;
               if (var2 != Integer.MIN_VALUE) {
                  var4 = var3 + var2;
               }

               this.mCachedEnd = var4;
               this.mCachedStart = var4;
            }
         }
      }

      void calculateCachedEnd() {
         View var1 = (View)this.mViews.get(this.mViews.size() - 1);
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         this.mCachedEnd = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(var1);
         if (var2.mFullSpan) {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var3 = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(var2.getViewLayoutPosition());
            if (var3 != null && var3.mGapDir == 1) {
               this.mCachedEnd += var3.getGapForSpan(this.mIndex);
            }
         }

      }

      void calculateCachedStart() {
         View var1 = (View)this.mViews.get(0);
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         this.mCachedStart = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(var1);
         if (var2.mFullSpan) {
            StaggeredGridLayoutManager.LazySpanLookup.FullSpanItem var3 = StaggeredGridLayoutManager.this.mLazySpanLookup.getFullSpanItem(var2.getViewLayoutPosition());
            if (var3 != null && var3.mGapDir == -1) {
               this.mCachedStart -= var3.getGapForSpan(this.mIndex);
            }
         }

      }

      void clear() {
         this.mViews.clear();
         this.invalidateCache();
         this.mDeletedSize = 0;
      }

      public int findFirstCompletelyVisibleItemPosition() {
         int var1;
         if (StaggeredGridLayoutManager.this.mReverseLayout) {
            var1 = this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
         } else {
            var1 = this.findOneVisibleChild(0, this.mViews.size(), true);
         }

         return var1;
      }

      public int findFirstPartiallyVisibleItemPosition() {
         int var1;
         if (StaggeredGridLayoutManager.this.mReverseLayout) {
            var1 = this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
         } else {
            var1 = this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
         }

         return var1;
      }

      public int findFirstVisibleItemPosition() {
         int var1;
         if (StaggeredGridLayoutManager.this.mReverseLayout) {
            var1 = this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
         } else {
            var1 = this.findOneVisibleChild(0, this.mViews.size(), false);
         }

         return var1;
      }

      public int findLastCompletelyVisibleItemPosition() {
         int var1;
         if (StaggeredGridLayoutManager.this.mReverseLayout) {
            var1 = this.findOneVisibleChild(0, this.mViews.size(), true);
         } else {
            var1 = this.findOneVisibleChild(this.mViews.size() - 1, -1, true);
         }

         return var1;
      }

      public int findLastPartiallyVisibleItemPosition() {
         int var1;
         if (StaggeredGridLayoutManager.this.mReverseLayout) {
            var1 = this.findOnePartiallyVisibleChild(0, this.mViews.size(), true);
         } else {
            var1 = this.findOnePartiallyVisibleChild(this.mViews.size() - 1, -1, true);
         }

         return var1;
      }

      public int findLastVisibleItemPosition() {
         int var1;
         if (StaggeredGridLayoutManager.this.mReverseLayout) {
            var1 = this.findOneVisibleChild(0, this.mViews.size(), false);
         } else {
            var1 = this.findOneVisibleChild(this.mViews.size() - 1, -1, false);
         }

         return var1;
      }

      int findOnePartiallyOrCompletelyVisibleChild(int var1, int var2, boolean var3, boolean var4, boolean var5) {
         int var6 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getStartAfterPadding();
         int var7 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getEndAfterPadding();
         byte var8;
         if (var2 > var1) {
            var8 = 1;
         } else {
            var8 = -1;
         }

         for(; var1 != var2; var1 += var8) {
            View var9;
            int var10;
            int var11;
            boolean var12;
            boolean var13;
            label45: {
               label44: {
                  var9 = (View)this.mViews.get(var1);
                  var10 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedStart(var9);
                  var11 = StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedEnd(var9);
                  var12 = false;
                  if (var5) {
                     if (var10 <= var7) {
                        break label44;
                     }
                  } else if (var10 < var7) {
                     break label44;
                  }

                  var13 = false;
                  break label45;
               }

               var13 = true;
            }

            label51: {
               if (var5) {
                  if (var11 < var6) {
                     break label51;
                  }
               } else if (var11 <= var6) {
                  break label51;
               }

               var12 = true;
            }

            if (var13 && var12) {
               if (var3 && var4) {
                  if (var10 >= var6 && var11 <= var7) {
                     return StaggeredGridLayoutManager.this.getPosition(var9);
                  }
               } else {
                  if (var4) {
                     return StaggeredGridLayoutManager.this.getPosition(var9);
                  }

                  if (var10 < var6 || var11 > var7) {
                     return StaggeredGridLayoutManager.this.getPosition(var9);
                  }
               }
            }
         }

         return -1;
      }

      int findOnePartiallyVisibleChild(int var1, int var2, boolean var3) {
         return this.findOnePartiallyOrCompletelyVisibleChild(var1, var2, false, false, var3);
      }

      int findOneVisibleChild(int var1, int var2, boolean var3) {
         return this.findOnePartiallyOrCompletelyVisibleChild(var1, var2, var3, true, false);
      }

      public int getDeletedSize() {
         return this.mDeletedSize;
      }

      int getEndLine() {
         if (this.mCachedEnd != Integer.MIN_VALUE) {
            return this.mCachedEnd;
         } else {
            this.calculateCachedEnd();
            return this.mCachedEnd;
         }
      }

      int getEndLine(int var1) {
         if (this.mCachedEnd != Integer.MIN_VALUE) {
            return this.mCachedEnd;
         } else if (this.mViews.size() == 0) {
            return var1;
         } else {
            this.calculateCachedEnd();
            return this.mCachedEnd;
         }
      }

      public View getFocusableViewAfter(int var1, int var2) {
         View var3 = null;
         View var4 = null;
         View var6;
         if (var2 == -1) {
            int var5 = this.mViews.size();
            var2 = 0;

            while(true) {
               var3 = var4;
               if (var2 >= var5) {
                  break;
               }

               var6 = (View)this.mViews.get(var2);
               if (StaggeredGridLayoutManager.this.mReverseLayout) {
                  var3 = var4;
                  if (StaggeredGridLayoutManager.this.getPosition(var6) <= var1) {
                     break;
                  }
               }

               if (!StaggeredGridLayoutManager.this.mReverseLayout && StaggeredGridLayoutManager.this.getPosition(var6) >= var1) {
                  var3 = var4;
                  break;
               }

               var3 = var4;
               if (!var6.hasFocusable()) {
                  break;
               }

               ++var2;
               var4 = var6;
            }
         } else {
            var2 = this.mViews.size() - 1;
            var4 = var3;

            while(true) {
               var3 = var4;
               if (var2 < 0) {
                  break;
               }

               var6 = (View)this.mViews.get(var2);
               if (StaggeredGridLayoutManager.this.mReverseLayout) {
                  var3 = var4;
                  if (StaggeredGridLayoutManager.this.getPosition(var6) >= var1) {
                     break;
                  }
               }

               if (!StaggeredGridLayoutManager.this.mReverseLayout && StaggeredGridLayoutManager.this.getPosition(var6) <= var1) {
                  var3 = var4;
                  break;
               }

               var3 = var4;
               if (!var6.hasFocusable()) {
                  break;
               }

               --var2;
               var4 = var6;
            }
         }

         return var3;
      }

      StaggeredGridLayoutManager.LayoutParams getLayoutParams(View var1) {
         return (StaggeredGridLayoutManager.LayoutParams)var1.getLayoutParams();
      }

      int getStartLine() {
         if (this.mCachedStart != Integer.MIN_VALUE) {
            return this.mCachedStart;
         } else {
            this.calculateCachedStart();
            return this.mCachedStart;
         }
      }

      int getStartLine(int var1) {
         if (this.mCachedStart != Integer.MIN_VALUE) {
            return this.mCachedStart;
         } else if (this.mViews.size() == 0) {
            return var1;
         } else {
            this.calculateCachedStart();
            return this.mCachedStart;
         }
      }

      void invalidateCache() {
         this.mCachedStart = Integer.MIN_VALUE;
         this.mCachedEnd = Integer.MIN_VALUE;
      }

      void onOffset(int var1) {
         if (this.mCachedStart != Integer.MIN_VALUE) {
            this.mCachedStart += var1;
         }

         if (this.mCachedEnd != Integer.MIN_VALUE) {
            this.mCachedEnd += var1;
         }

      }

      void popEnd() {
         int var1 = this.mViews.size();
         View var2 = (View)this.mViews.remove(var1 - 1);
         StaggeredGridLayoutManager.LayoutParams var3 = this.getLayoutParams(var2);
         var3.mSpan = null;
         if (var3.isItemRemoved() || var3.isItemChanged()) {
            this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var2);
         }

         if (var1 == 1) {
            this.mCachedStart = Integer.MIN_VALUE;
         }

         this.mCachedEnd = Integer.MIN_VALUE;
      }

      void popStart() {
         View var1 = (View)this.mViews.remove(0);
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         var2.mSpan = null;
         if (this.mViews.size() == 0) {
            this.mCachedEnd = Integer.MIN_VALUE;
         }

         if (var2.isItemRemoved() || var2.isItemChanged()) {
            this.mDeletedSize -= StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var1);
         }

         this.mCachedStart = Integer.MIN_VALUE;
      }

      void prependToSpan(View var1) {
         StaggeredGridLayoutManager.LayoutParams var2 = this.getLayoutParams(var1);
         var2.mSpan = this;
         this.mViews.add(0, var1);
         this.mCachedStart = Integer.MIN_VALUE;
         if (this.mViews.size() == 1) {
            this.mCachedEnd = Integer.MIN_VALUE;
         }

         if (var2.isItemRemoved() || var2.isItemChanged()) {
            this.mDeletedSize += StaggeredGridLayoutManager.this.mPrimaryOrientation.getDecoratedMeasurement(var1);
         }

      }

      void setLine(int var1) {
         this.mCachedStart = var1;
         this.mCachedEnd = var1;
      }
   }
}
