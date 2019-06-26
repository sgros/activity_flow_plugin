package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.Pools;

class ViewInfoStore {
   private static final boolean DEBUG = false;
   @VisibleForTesting
   final ArrayMap mLayoutHolderMap = new ArrayMap();
   @VisibleForTesting
   final LongSparseArray mOldChangedHolders = new LongSparseArray();

   private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder var1, int var2) {
      int var3 = this.mLayoutHolderMap.indexOfKey(var1);
      if (var3 < 0) {
         return null;
      } else {
         ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.valueAt(var3);
         if (var4 != null && (var4.flags & var2) != 0) {
            var4.flags &= ~var2;
            RecyclerView.ItemAnimator.ItemHolderInfo var5;
            if (var2 == 4) {
               var5 = var4.preInfo;
            } else {
               if (var2 != 8) {
                  throw new IllegalArgumentException("Must provide flag PRE or POST");
               }

               var5 = var4.postInfo;
            }

            if ((var4.flags & 12) == 0) {
               this.mLayoutHolderMap.removeAt(var3);
               ViewInfoStore.InfoRecord.recycle(var4);
            }

            return var5;
         } else {
            return null;
         }
      }
   }

   void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var4 = var3;
      if (var3 == null) {
         var4 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var4);
      }

      var4.flags |= 2;
      var4.preInfo = var2;
   }

   void addToDisappearedInLayout(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var2 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var3 = var2;
      if (var2 == null) {
         var3 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var3);
      }

      var3.flags |= 1;
   }

   void addToOldChangeHolders(long var1, RecyclerView.ViewHolder var3) {
      this.mOldChangedHolders.put(var1, var3);
   }

   void addToPostLayout(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var4 = var3;
      if (var3 == null) {
         var4 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var4);
      }

      var4.postInfo = var2;
      var4.flags |= 8;
   }

   void addToPreLayout(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2) {
      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      ViewInfoStore.InfoRecord var4 = var3;
      if (var3 == null) {
         var4 = ViewInfoStore.InfoRecord.obtain();
         this.mLayoutHolderMap.put(var1, var4);
      }

      var4.preInfo = var2;
      var4.flags |= 4;
   }

   void clear() {
      this.mLayoutHolderMap.clear();
      this.mOldChangedHolders.clear();
   }

   RecyclerView.ViewHolder getFromOldChangeHolders(long var1) {
      return (RecyclerView.ViewHolder)this.mOldChangedHolders.get(var1);
   }

   boolean isDisappearing(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      boolean var2 = true;
      if (var3 == null || (var3.flags & 1) == 0) {
         var2 = false;
      }

      return var2;
   }

   boolean isInPreLayout(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      boolean var2;
      if (var3 != null && (var3.flags & 4) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   void onDetach() {
      ViewInfoStore.InfoRecord.drainCache();
   }

   public void onViewDetached(RecyclerView.ViewHolder var1) {
      this.removeFromDisappearedInLayout(var1);
   }

   @Nullable
   RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder var1) {
      return this.popFromLayoutStep(var1, 8);
   }

   @Nullable
   RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder var1) {
      return this.popFromLayoutStep(var1, 4);
   }

   void process(ViewInfoStore.ProcessCallback var1) {
      for(int var2 = this.mLayoutHolderMap.size() - 1; var2 >= 0; --var2) {
         RecyclerView.ViewHolder var3 = (RecyclerView.ViewHolder)this.mLayoutHolderMap.keyAt(var2);
         ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.removeAt(var2);
         if ((var4.flags & 3) == 3) {
            var1.unused(var3);
         } else if ((var4.flags & 1) != 0) {
            if (var4.preInfo == null) {
               var1.unused(var3);
            } else {
               var1.processDisappeared(var3, var4.preInfo, var4.postInfo);
            }
         } else if ((var4.flags & 14) == 14) {
            var1.processAppeared(var3, var4.preInfo, var4.postInfo);
         } else if ((var4.flags & 12) == 12) {
            var1.processPersistent(var3, var4.preInfo, var4.postInfo);
         } else if ((var4.flags & 4) != 0) {
            var1.processDisappeared(var3, var4.preInfo, (RecyclerView.ItemAnimator.ItemHolderInfo)null);
         } else if ((var4.flags & 8) != 0) {
            var1.processAppeared(var3, var4.preInfo, var4.postInfo);
         } else {
            int var5 = var4.flags;
         }

         ViewInfoStore.InfoRecord.recycle(var4);
      }

   }

   void removeFromDisappearedInLayout(RecyclerView.ViewHolder var1) {
      ViewInfoStore.InfoRecord var2 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.get(var1);
      if (var2 != null) {
         var2.flags &= -2;
      }
   }

   void removeViewHolder(RecyclerView.ViewHolder var1) {
      for(int var2 = this.mOldChangedHolders.size() - 1; var2 >= 0; --var2) {
         if (var1 == this.mOldChangedHolders.valueAt(var2)) {
            this.mOldChangedHolders.removeAt(var2);
            break;
         }
      }

      ViewInfoStore.InfoRecord var3 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.remove(var1);
      if (var3 != null) {
         ViewInfoStore.InfoRecord.recycle(var3);
      }

   }

   static class InfoRecord {
      static final int FLAG_APPEAR = 2;
      static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
      static final int FLAG_APPEAR_PRE_AND_POST = 14;
      static final int FLAG_DISAPPEARED = 1;
      static final int FLAG_POST = 8;
      static final int FLAG_PRE = 4;
      static final int FLAG_PRE_AND_POST = 12;
      static Pools.Pool sPool = new Pools.SimplePool(20);
      int flags;
      @Nullable
      RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
      @Nullable
      RecyclerView.ItemAnimator.ItemHolderInfo preInfo;

      private InfoRecord() {
      }

      static void drainCache() {
         while(sPool.acquire() != null) {
         }

      }

      static ViewInfoStore.InfoRecord obtain() {
         ViewInfoStore.InfoRecord var0 = (ViewInfoStore.InfoRecord)sPool.acquire();
         ViewInfoStore.InfoRecord var1 = var0;
         if (var0 == null) {
            var1 = new ViewInfoStore.InfoRecord();
         }

         return var1;
      }

      static void recycle(ViewInfoStore.InfoRecord var0) {
         var0.flags = 0;
         var0.preInfo = null;
         var0.postInfo = null;
         sPool.release(var0);
      }
   }

   interface ProcessCallback {
      void processAppeared(RecyclerView.ViewHolder var1, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void processDisappeared(RecyclerView.ViewHolder var1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var2, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void processPersistent(RecyclerView.ViewHolder var1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var2, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void unused(RecyclerView.ViewHolder var1);
   }
}
