package android.support.v7.widget;

import android.support.annotation.Nullable;
import android.support.v4.os.TraceCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

final class GapWorker implements Runnable {
   static final ThreadLocal sGapWorker = new ThreadLocal();
   static Comparator sTaskComparator = new Comparator() {
      public int compare(GapWorker.Task var1, GapWorker.Task var2) {
         RecyclerView var3 = var1.view;
         byte var4 = 1;
         byte var5 = 1;
         boolean var6;
         if (var3 == null) {
            var6 = true;
         } else {
            var6 = false;
         }

         boolean var7;
         if (var2.view == null) {
            var7 = true;
         } else {
            var7 = false;
         }

         byte var9;
         if (var6 != var7) {
            if (var1.view == null) {
               var9 = var5;
            } else {
               var9 = -1;
            }

            return var9;
         } else if (var1.immediate != var2.immediate) {
            var9 = var4;
            if (var1.immediate) {
               var9 = -1;
            }

            return var9;
         } else {
            int var8 = var2.viewVelocity - var1.viewVelocity;
            if (var8 != 0) {
               return var8;
            } else {
               var8 = var1.distanceToItem - var2.distanceToItem;
               return var8 != 0 ? var8 : 0;
            }
         }
      }
   };
   long mFrameIntervalNs;
   long mPostTimeNs;
   ArrayList mRecyclerViews = new ArrayList();
   private ArrayList mTasks = new ArrayList();

   private void buildTaskList() {
      int var1 = this.mRecyclerViews.size();
      int var2 = 0;

      int var3;
      int var5;
      for(var3 = var2; var2 < var1; var3 = var5) {
         RecyclerView var4 = (RecyclerView)this.mRecyclerViews.get(var2);
         var5 = var3;
         if (var4.getWindowVisibility() == 0) {
            var4.mPrefetchRegistry.collectPrefetchPositionsFromView(var4, false);
            var5 = var3 + var4.mPrefetchRegistry.mCount;
         }

         ++var2;
      }

      this.mTasks.ensureCapacity(var3);
      var5 = 0;

      for(var2 = var5; var5 < var1; ++var5) {
         RecyclerView var6 = (RecyclerView)this.mRecyclerViews.get(var5);
         if (var6.getWindowVisibility() == 0) {
            GapWorker.LayoutPrefetchRegistryImpl var7 = var6.mPrefetchRegistry;
            int var8 = Math.abs(var7.mPrefetchDx) + Math.abs(var7.mPrefetchDy);

            for(var3 = 0; var3 < var7.mCount * 2; var3 += 2) {
               GapWorker.Task var11;
               if (var2 >= this.mTasks.size()) {
                  var11 = new GapWorker.Task();
                  this.mTasks.add(var11);
               } else {
                  var11 = (GapWorker.Task)this.mTasks.get(var2);
               }

               int var9 = var7.mPrefetchArray[var3 + 1];
               boolean var10;
               if (var9 <= var8) {
                  var10 = true;
               } else {
                  var10 = false;
               }

               var11.immediate = var10;
               var11.viewVelocity = var8;
               var11.distanceToItem = var9;
               var11.view = var6;
               var11.position = var7.mPrefetchArray[var3];
               ++var2;
            }
         }
      }

      Collections.sort(this.mTasks, sTaskComparator);
   }

   private void flushTaskWithDeadline(GapWorker.Task var1, long var2) {
      long var4;
      if (var1.immediate) {
         var4 = Long.MAX_VALUE;
      } else {
         var4 = var2;
      }

      RecyclerView.ViewHolder var6 = this.prefetchPositionWithDeadline(var1.view, var1.position, var4);
      if (var6 != null && var6.mNestedRecyclerView != null && var6.isBound() && !var6.isInvalid()) {
         this.prefetchInnerRecyclerViewWithDeadline((RecyclerView)var6.mNestedRecyclerView.get(), var2);
      }

   }

   private void flushTasksWithDeadline(long var1) {
      for(int var3 = 0; var3 < this.mTasks.size(); ++var3) {
         GapWorker.Task var4 = (GapWorker.Task)this.mTasks.get(var3);
         if (var4.view == null) {
            break;
         }

         this.flushTaskWithDeadline(var4, var1);
         var4.clear();
      }

   }

   static boolean isPrefetchPositionAttached(RecyclerView var0, int var1) {
      int var2 = var0.mChildHelper.getUnfilteredChildCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         RecyclerView.ViewHolder var4 = RecyclerView.getChildViewHolderInt(var0.mChildHelper.getUnfilteredChildAt(var3));
         if (var4.mPosition == var1 && !var4.isInvalid()) {
            return true;
         }
      }

      return false;
   }

   private void prefetchInnerRecyclerViewWithDeadline(@Nullable RecyclerView var1, long var2) {
      if (var1 != null) {
         if (var1.mDataSetHasChangedAfterLayout && var1.mChildHelper.getUnfilteredChildCount() != 0) {
            var1.removeAndRecycleViews();
         }

         GapWorker.LayoutPrefetchRegistryImpl var4 = var1.mPrefetchRegistry;
         var4.collectPrefetchPositionsFromView(var1, true);
         if (var4.mCount != 0) {
            label126: {
               Throwable var10000;
               label135: {
                  boolean var10001;
                  try {
                     TraceCompat.beginSection("RV Nested Prefetch");
                     var1.mState.prepareForNestedPrefetch(var1.mAdapter);
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label135;
                  }

                  int var5 = 0;

                  while(true) {
                     try {
                        if (var5 >= var4.mCount * 2) {
                           break label126;
                        }

                        this.prefetchPositionWithDeadline(var1, var4.mPrefetchArray[var5], var2);
                     } catch (Throwable var10) {
                        var10000 = var10;
                        var10001 = false;
                        break;
                     }

                     var5 += 2;
                  }
               }

               Throwable var12 = var10000;
               TraceCompat.endSection();
               throw var12;
            }

            TraceCompat.endSection();
         }

      }
   }

   private RecyclerView.ViewHolder prefetchPositionWithDeadline(RecyclerView var1, int var2, long var3) {
      if (isPrefetchPositionAttached(var1, var2)) {
         return null;
      } else {
         RecyclerView.Recycler var5 = var1.mRecycler;

         RecyclerView.ViewHolder var6;
         label155: {
            Throwable var10000;
            label161: {
               boolean var10001;
               try {
                  var1.onEnterLayoutOrScroll();
                  var6 = var5.tryGetViewHolderForPositionByDeadline(var2, false, var3);
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label161;
               }

               if (var6 == null) {
                  break label155;
               }

               try {
                  if (var6.isBound() && !var6.isInvalid()) {
                     var5.recycleView(var6.itemView);
                     break label155;
                  }
               } catch (Throwable var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label161;
               }

               label147:
               try {
                  var5.addViewHolderToRecycledViewPool(var6, false);
                  break label155;
               } catch (Throwable var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label147;
               }
            }

            Throwable var19 = var10000;
            var1.onExitLayoutOrScroll(false);
            throw var19;
         }

         var1.onExitLayoutOrScroll(false);
         return var6;
      }
   }

   public void add(RecyclerView var1) {
      this.mRecyclerViews.add(var1);
   }

   void postFromTraversal(RecyclerView var1, int var2, int var3) {
      if (var1.isAttachedToWindow() && this.mPostTimeNs == 0L) {
         this.mPostTimeNs = var1.getNanoTime();
         var1.post(this);
      }

      var1.mPrefetchRegistry.setPrefetchVector(var2, var3);
   }

   void prefetch(long var1) {
      this.buildTaskList();
      this.flushTasksWithDeadline(var1);
   }

   public void remove(RecyclerView var1) {
      this.mRecyclerViews.remove(var1);
   }

   public void run() {
      Throwable var10000;
      label335: {
         boolean var1;
         boolean var10001;
         try {
            TraceCompat.beginSection("RV Prefetch");
            var1 = this.mRecyclerViews.isEmpty();
         } catch (Throwable var38) {
            var10000 = var38;
            var10001 = false;
            break label335;
         }

         if (var1) {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
            return;
         }

         int var2;
         try {
            var2 = this.mRecyclerViews.size();
         } catch (Throwable var37) {
            var10000 = var37;
            var10001 = false;
            break label335;
         }

         int var3 = 0;

         long var4;
         long var7;
         for(var4 = 0L; var3 < var2; var4 = var7) {
            RecyclerView var6;
            try {
               var6 = (RecyclerView)this.mRecyclerViews.get(var3);
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label335;
            }

            var7 = var4;

            try {
               if (var6.getWindowVisibility() == 0) {
                  var7 = Math.max(var6.getDrawingTime(), var4);
               }
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label335;
            }

            ++var3;
         }

         if (var4 == 0L) {
            this.mPostTimeNs = 0L;
            TraceCompat.endSection();
            return;
         }

         try {
            this.prefetch(TimeUnit.MILLISECONDS.toNanos(var4) + this.mFrameIntervalNs);
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label335;
         }

         this.mPostTimeNs = 0L;
         TraceCompat.endSection();
         return;
      }

      Throwable var39 = var10000;
      this.mPostTimeNs = 0L;
      TraceCompat.endSection();
      throw var39;
   }

   static class LayoutPrefetchRegistryImpl implements RecyclerView.LayoutManager.LayoutPrefetchRegistry {
      int mCount;
      int[] mPrefetchArray;
      int mPrefetchDx;
      int mPrefetchDy;

      public void addPosition(int var1, int var2) {
         if (var1 < 0) {
            throw new IllegalArgumentException("Layout positions must be non-negative");
         } else if (var2 < 0) {
            throw new IllegalArgumentException("Pixel distance must be non-negative");
         } else {
            int var3 = this.mCount * 2;
            if (this.mPrefetchArray == null) {
               this.mPrefetchArray = new int[4];
               Arrays.fill(this.mPrefetchArray, -1);
            } else if (var3 >= this.mPrefetchArray.length) {
               int[] var4 = this.mPrefetchArray;
               this.mPrefetchArray = new int[var3 * 2];
               System.arraycopy(var4, 0, this.mPrefetchArray, 0, var4.length);
            }

            this.mPrefetchArray[var3] = var1;
            this.mPrefetchArray[var3 + 1] = var2;
            ++this.mCount;
         }
      }

      void clearPrefetchPositions() {
         if (this.mPrefetchArray != null) {
            Arrays.fill(this.mPrefetchArray, -1);
         }

         this.mCount = 0;
      }

      void collectPrefetchPositionsFromView(RecyclerView var1, boolean var2) {
         this.mCount = 0;
         if (this.mPrefetchArray != null) {
            Arrays.fill(this.mPrefetchArray, -1);
         }

         RecyclerView.LayoutManager var3 = var1.mLayout;
         if (var1.mAdapter != null && var3 != null && var3.isItemPrefetchEnabled()) {
            if (var2) {
               if (!var1.mAdapterHelper.hasPendingUpdates()) {
                  var3.collectInitialPrefetchPositions(var1.mAdapter.getItemCount(), this);
               }
            } else if (!var1.hasPendingAdapterUpdates()) {
               var3.collectAdjacentPrefetchPositions(this.mPrefetchDx, this.mPrefetchDy, var1.mState, this);
            }

            if (this.mCount > var3.mPrefetchMaxCountObserved) {
               var3.mPrefetchMaxCountObserved = this.mCount;
               var3.mPrefetchMaxObservedInInitialPrefetch = var2;
               var1.mRecycler.updateViewCacheSize();
            }
         }

      }

      boolean lastPrefetchIncludedPosition(int var1) {
         if (this.mPrefetchArray != null) {
            int var2 = this.mCount;

            for(int var3 = 0; var3 < var2 * 2; var3 += 2) {
               if (this.mPrefetchArray[var3] == var1) {
                  return true;
               }
            }
         }

         return false;
      }

      void setPrefetchVector(int var1, int var2) {
         this.mPrefetchDx = var1;
         this.mPrefetchDy = var2;
      }
   }

   static class Task {
      public int distanceToItem;
      public boolean immediate;
      public int position;
      public RecyclerView view;
      public int viewVelocity;

      public void clear() {
         this.immediate = false;
         this.viewVelocity = 0;
         this.distanceToItem = 0;
         this.view = null;
         this.position = 0;
      }
   }
}
