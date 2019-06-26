package androidx.recyclerview.widget;

import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pools$Pool;
import androidx.core.util.Pools$SimplePool;

class ViewInfoStore {
   final ArrayMap mLayoutHolderMap = new ArrayMap();
   final LongSparseArray mOldChangedHolders = new LongSparseArray();

   private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder var1, int var2) {
      int var3 = this.mLayoutHolderMap.indexOfKey(var1);
      if (var3 < 0) {
         return null;
      } else {
         ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.valueAt(var3);
         if (var4 != null) {
            int var5 = var4.flags;
            if ((var5 & var2) != 0) {
               var4.flags = ~var2 & var5;
               RecyclerView.ItemAnimator.ItemHolderInfo var6;
               if (var2 == 4) {
                  var6 = var4.preInfo;
               } else {
                  if (var2 != 8) {
                     throw new IllegalArgumentException("Must provide flag PRE or POST");
                  }

                  var6 = var4.postInfo;
               }

               if ((var4.flags & 12) == 0) {
                  this.mLayoutHolderMap.removeAt(var3);
                  ViewInfoStore.InfoRecord.recycle(var4);
               }

               return var6;
            }
         }

         return null;
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

   RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder var1) {
      return this.popFromLayoutStep(var1, 8);
   }

   RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder var1) {
      return this.popFromLayoutStep(var1, 4);
   }

   void process(ViewInfoStore.ProcessCallback var1) {
      for(int var2 = this.mLayoutHolderMap.size() - 1; var2 >= 0; --var2) {
         RecyclerView.ViewHolder var3 = (RecyclerView.ViewHolder)this.mLayoutHolderMap.keyAt(var2);
         ViewInfoStore.InfoRecord var4 = (ViewInfoStore.InfoRecord)this.mLayoutHolderMap.removeAt(var2);
         int var5 = var4.flags;
         if ((var5 & 3) == 3) {
            var1.unused(var3);
         } else if ((var5 & 1) != 0) {
            RecyclerView.ItemAnimator.ItemHolderInfo var6 = var4.preInfo;
            if (var6 == null) {
               var1.unused(var3);
            } else {
               var1.processDisappeared(var3, var6, var4.postInfo);
            }
         } else if ((var5 & 14) == 14) {
            var1.processAppeared(var3, var4.preInfo, var4.postInfo);
         } else if ((var5 & 12) == 12) {
            var1.processPersistent(var3, var4.preInfo, var4.postInfo);
         } else if ((var5 & 4) != 0) {
            var1.processDisappeared(var3, var4.preInfo, (RecyclerView.ItemAnimator.ItemHolderInfo)null);
         } else if ((var5 & 8) != 0) {
            var1.processAppeared(var3, var4.preInfo, var4.postInfo);
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
      static Pools$Pool sPool = new Pools$SimplePool(20);
      int flags;
      RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
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
      void processAppeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void processDisappeared(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void processPersistent(RecyclerView.ViewHolder var1, RecyclerView.ItemAnimator.ItemHolderInfo var2, RecyclerView.ItemAnimator.ItemHolderInfo var3);

      void unused(RecyclerView.ViewHolder var1);
   }
}
