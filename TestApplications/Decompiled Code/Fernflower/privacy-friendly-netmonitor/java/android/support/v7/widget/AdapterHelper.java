package android.support.v7.widget;

import android.support.v4.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AdapterHelper implements OpReorderer.Callback {
   private static final boolean DEBUG = false;
   static final int POSITION_TYPE_INVISIBLE = 0;
   static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
   private static final String TAG = "AHT";
   final AdapterHelper.Callback mCallback;
   final boolean mDisableRecycler;
   private int mExistingUpdateTypes;
   Runnable mOnItemProcessedCallback;
   final OpReorderer mOpReorderer;
   final ArrayList mPendingUpdates;
   final ArrayList mPostponedList;
   private Pools.Pool mUpdateOpPool;

   AdapterHelper(AdapterHelper.Callback var1) {
      this(var1, false);
   }

   AdapterHelper(AdapterHelper.Callback var1, boolean var2) {
      this.mUpdateOpPool = new Pools.SimplePool(30);
      this.mPendingUpdates = new ArrayList();
      this.mPostponedList = new ArrayList();
      this.mExistingUpdateTypes = 0;
      this.mCallback = var1;
      this.mDisableRecycler = var2;
      this.mOpReorderer = new OpReorderer(this);
   }

   private void applyAdd(AdapterHelper.UpdateOp var1) {
      this.postponeAndUpdateViewHolders(var1);
   }

   private void applyMove(AdapterHelper.UpdateOp var1) {
      this.postponeAndUpdateViewHolders(var1);
   }

   private void applyRemove(AdapterHelper.UpdateOp var1) {
      int var2 = var1.positionStart;
      int var3 = var1.positionStart + var1.itemCount;
      int var4 = var1.positionStart;
      byte var5 = -1;

      int var6;
      int var11;
      for(var6 = 0; var4 < var3; var6 = var11) {
         boolean var8;
         if (this.mCallback.findViewHolder(var4) == null && !this.canFindInPreLayout(var4)) {
            boolean var10;
            if (var5 == 1) {
               this.postponeAndUpdateViewHolders(this.obtainUpdateOp(2, var2, var6, (Object)null));
               var10 = true;
            } else {
               var10 = false;
            }

            byte var7 = 0;
            var8 = var10;
            var5 = var7;
         } else {
            if (var5 == 0) {
               this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(2, var2, var6, (Object)null));
               var8 = true;
            } else {
               var8 = false;
            }

            var5 = 1;
         }

         if (var8) {
            var4 -= var6;
            var3 -= var6;
            var11 = 1;
         } else {
            var11 = var6 + 1;
         }

         ++var4;
      }

      AdapterHelper.UpdateOp var9 = var1;
      if (var6 != var1.itemCount) {
         this.recycleUpdateOp(var1);
         var9 = this.obtainUpdateOp(2, var2, var6, (Object)null);
      }

      if (var5 == 0) {
         this.dispatchAndUpdateViewHolders(var9);
      } else {
         this.postponeAndUpdateViewHolders(var9);
      }

   }

   private void applyUpdate(AdapterHelper.UpdateOp var1) {
      int var2 = var1.positionStart;
      int var3 = var1.positionStart;
      int var4 = var1.itemCount;
      int var5 = var1.positionStart;
      byte var6 = -1;

      int var7;
      byte var13;
      for(var7 = 0; var5 < var3 + var4; var6 = var13) {
         int var8;
         int var9;
         if (this.mCallback.findViewHolder(var5) == null && !this.canFindInPreLayout(var5)) {
            var8 = var7;
            var9 = var2;
            if (var6 == 1) {
               this.postponeAndUpdateViewHolders(this.obtainUpdateOp(4, var2, var7, var1.payload));
               var9 = var5;
               var8 = 0;
            }

            byte var11 = 0;
            var7 = var8;
            var13 = var11;
            var2 = var9;
         } else {
            var9 = var7;
            var8 = var2;
            if (var6 == 0) {
               this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(4, var2, var7, var1.payload));
               var8 = var5;
               var9 = 0;
            }

            byte var12 = 1;
            var2 = var8;
            var13 = var12;
            var7 = var9;
         }

         ++var7;
         ++var5;
      }

      AdapterHelper.UpdateOp var10 = var1;
      if (var7 != var1.itemCount) {
         Object var14 = var1.payload;
         this.recycleUpdateOp(var1);
         var10 = this.obtainUpdateOp(4, var2, var7, var14);
      }

      if (var6 == 0) {
         this.dispatchAndUpdateViewHolders(var10);
      } else {
         this.postponeAndUpdateViewHolders(var10);
      }

   }

   private boolean canFindInPreLayout(int var1) {
      int var2 = this.mPostponedList.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         AdapterHelper.UpdateOp var4 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var3);
         if (var4.cmd == 8) {
            if (this.findPositionOffset(var4.itemCount, var3 + 1) == var1) {
               return true;
            }
         } else if (var4.cmd == 1) {
            int var5 = var4.positionStart;
            int var6 = var4.itemCount;

            for(int var7 = var4.positionStart; var7 < var5 + var6; ++var7) {
               if (this.findPositionOffset(var7, var3 + 1) == var1) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void dispatchAndUpdateViewHolders(AdapterHelper.UpdateOp var1) {
      if (var1.cmd != 1 && var1.cmd != 8) {
         int var2 = this.updatePositionWithPostponed(var1.positionStart, var1.cmd);
         int var3 = var1.positionStart;
         int var4 = var1.cmd;
         byte var6;
         if (var4 != 2) {
            if (var4 != 4) {
               StringBuilder var5 = new StringBuilder();
               var5.append("op should be remove or update.");
               var5.append(var1);
               throw new IllegalArgumentException(var5.toString());
            }

            var6 = 1;
         } else {
            var6 = 0;
         }

         int var7 = 1;

         int var8;
         for(var8 = var7; var7 < var1.itemCount; var8 = var4) {
            int var9;
            boolean var10;
            label51: {
               label50: {
                  var9 = this.updatePositionWithPostponed(var1.positionStart + var6 * var7, var1.cmd);
                  var4 = var1.cmd;
                  if (var4 != 2) {
                     if (var4 != 4 || var9 != var2 + 1) {
                        break label50;
                     }
                  } else if (var9 != var2) {
                     break label50;
                  }

                  var10 = true;
                  break label51;
               }

               var10 = false;
            }

            if (var10) {
               var4 = var8 + 1;
            } else {
               AdapterHelper.UpdateOp var11 = this.obtainUpdateOp(var1.cmd, var2, var8, var1.payload);
               this.dispatchFirstPassAndUpdateViewHolders(var11, var3);
               this.recycleUpdateOp(var11);
               var4 = var3;
               if (var1.cmd == 4) {
                  var4 = var3 + var8;
               }

               byte var13 = 1;
               var2 = var9;
               var3 = var4;
               var4 = var13;
            }

            ++var7;
         }

         Object var12 = var1.payload;
         this.recycleUpdateOp(var1);
         if (var8 > 0) {
            var1 = this.obtainUpdateOp(var1.cmd, var2, var8, var12);
            this.dispatchFirstPassAndUpdateViewHolders(var1, var3);
            this.recycleUpdateOp(var1);
         }

      } else {
         throw new IllegalArgumentException("should not dispatch add or move for pre layout");
      }
   }

   private void postponeAndUpdateViewHolders(AdapterHelper.UpdateOp var1) {
      this.mPostponedList.add(var1);
      int var2 = var1.cmd;
      if (var2 != 4) {
         if (var2 != 8) {
            switch(var2) {
            case 1:
               this.mCallback.offsetPositionsForAdd(var1.positionStart, var1.itemCount);
               break;
            case 2:
               this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(var1.positionStart, var1.itemCount);
               break;
            default:
               StringBuilder var3 = new StringBuilder();
               var3.append("Unknown update op type for ");
               var3.append(var1);
               throw new IllegalArgumentException(var3.toString());
            }
         } else {
            this.mCallback.offsetPositionsForMove(var1.positionStart, var1.itemCount);
         }
      } else {
         this.mCallback.markViewHoldersUpdated(var1.positionStart, var1.itemCount, var1.payload);
      }

   }

   private int updatePositionWithPostponed(int var1, int var2) {
      int var3 = this.mPostponedList.size() - 1;

      int var4;
      AdapterHelper.UpdateOp var5;
      for(var4 = var1; var3 >= 0; var4 = var1) {
         var5 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var3);
         if (var5.cmd == 8) {
            int var6;
            if (var5.positionStart < var5.itemCount) {
               var6 = var5.positionStart;
               var1 = var5.itemCount;
            } else {
               var6 = var5.itemCount;
               var1 = var5.positionStart;
            }

            if (var4 >= var6 && var4 <= var1) {
               if (var6 == var5.positionStart) {
                  if (var2 == 1) {
                     ++var5.itemCount;
                  } else if (var2 == 2) {
                     --var5.itemCount;
                  }

                  var1 = var4 + 1;
               } else {
                  if (var2 == 1) {
                     ++var5.positionStart;
                  } else if (var2 == 2) {
                     --var5.positionStart;
                  }

                  var1 = var4 - 1;
               }
            } else {
               var1 = var4;
               if (var4 < var5.positionStart) {
                  if (var2 == 1) {
                     ++var5.positionStart;
                     ++var5.itemCount;
                     var1 = var4;
                  } else {
                     var1 = var4;
                     if (var2 == 2) {
                        --var5.positionStart;
                        --var5.itemCount;
                        var1 = var4;
                     }
                  }
               }
            }
         } else if (var5.positionStart <= var4) {
            if (var5.cmd == 1) {
               var1 = var4 - var5.itemCount;
            } else {
               var1 = var4;
               if (var5.cmd == 2) {
                  var1 = var4 + var5.itemCount;
               }
            }
         } else if (var2 == 1) {
            ++var5.positionStart;
            var1 = var4;
         } else {
            var1 = var4;
            if (var2 == 2) {
               --var5.positionStart;
               var1 = var4;
            }
         }

         --var3;
      }

      for(var1 = this.mPostponedList.size() - 1; var1 >= 0; --var1) {
         var5 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var1);
         if (var5.cmd == 8) {
            if (var5.itemCount == var5.positionStart || var5.itemCount < 0) {
               this.mPostponedList.remove(var1);
               this.recycleUpdateOp(var5);
            }
         } else if (var5.itemCount <= 0) {
            this.mPostponedList.remove(var1);
            this.recycleUpdateOp(var5);
         }
      }

      return var4;
   }

   AdapterHelper addUpdateOp(AdapterHelper.UpdateOp... var1) {
      Collections.addAll(this.mPendingUpdates, var1);
      return this;
   }

   public int applyPendingUpdatesToPosition(int var1) {
      int var2 = this.mPendingUpdates.size();
      int var3 = 0;

      int var4;
      for(var4 = var1; var3 < var2; var4 = var1) {
         AdapterHelper.UpdateOp var5 = (AdapterHelper.UpdateOp)this.mPendingUpdates.get(var3);
         var1 = var5.cmd;
         if (var1 != 8) {
            switch(var1) {
            case 1:
               var1 = var4;
               if (var5.positionStart <= var4) {
                  var1 = var4 + var5.itemCount;
               }
               break;
            case 2:
               var1 = var4;
               if (var5.positionStart <= var4) {
                  if (var5.positionStart + var5.itemCount > var4) {
                     return -1;
                  }

                  var1 = var4 - var5.itemCount;
               }
               break;
            default:
               var1 = var4;
            }
         } else if (var5.positionStart == var4) {
            var1 = var5.itemCount;
         } else {
            int var6 = var4;
            if (var5.positionStart < var4) {
               var6 = var4 - 1;
            }

            var1 = var6;
            if (var5.itemCount <= var6) {
               var1 = var6 + 1;
            }
         }

         ++var3;
      }

      return var4;
   }

   void consumePostponedUpdates() {
      int var1 = this.mPostponedList.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         this.mCallback.onDispatchSecondPass((AdapterHelper.UpdateOp)this.mPostponedList.get(var2));
      }

      this.recycleUpdateOpsAndClearList(this.mPostponedList);
      this.mExistingUpdateTypes = 0;
   }

   void consumeUpdatesInOnePass() {
      this.consumePostponedUpdates();
      int var1 = this.mPendingUpdates.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         AdapterHelper.UpdateOp var3 = (AdapterHelper.UpdateOp)this.mPendingUpdates.get(var2);
         int var4 = var3.cmd;
         if (var4 != 4) {
            if (var4 != 8) {
               switch(var4) {
               case 1:
                  this.mCallback.onDispatchSecondPass(var3);
                  this.mCallback.offsetPositionsForAdd(var3.positionStart, var3.itemCount);
                  break;
               case 2:
                  this.mCallback.onDispatchSecondPass(var3);
                  this.mCallback.offsetPositionsForRemovingInvisible(var3.positionStart, var3.itemCount);
               }
            } else {
               this.mCallback.onDispatchSecondPass(var3);
               this.mCallback.offsetPositionsForMove(var3.positionStart, var3.itemCount);
            }
         } else {
            this.mCallback.onDispatchSecondPass(var3);
            this.mCallback.markViewHoldersUpdated(var3.positionStart, var3.itemCount, var3.payload);
         }

         if (this.mOnItemProcessedCallback != null) {
            this.mOnItemProcessedCallback.run();
         }
      }

      this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
      this.mExistingUpdateTypes = 0;
   }

   void dispatchFirstPassAndUpdateViewHolders(AdapterHelper.UpdateOp var1, int var2) {
      this.mCallback.onDispatchFirstPass(var1);
      int var3 = var1.cmd;
      if (var3 != 2) {
         if (var3 != 4) {
            throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
         }

         this.mCallback.markViewHoldersUpdated(var2, var1.itemCount, var1.payload);
      } else {
         this.mCallback.offsetPositionsForRemovingInvisible(var2, var1.itemCount);
      }

   }

   int findPositionOffset(int var1) {
      return this.findPositionOffset(var1, 0);
   }

   int findPositionOffset(int var1, int var2) {
      int var3 = this.mPostponedList.size();
      int var4 = var2;

      for(var2 = var1; var4 < var3; var2 = var1) {
         AdapterHelper.UpdateOp var5 = (AdapterHelper.UpdateOp)this.mPostponedList.get(var4);
         if (var5.cmd == 8) {
            if (var5.positionStart == var2) {
               var1 = var5.itemCount;
            } else {
               int var6 = var2;
               if (var5.positionStart < var2) {
                  var6 = var2 - 1;
               }

               var1 = var6;
               if (var5.itemCount <= var6) {
                  var1 = var6 + 1;
               }
            }
         } else {
            var1 = var2;
            if (var5.positionStart <= var2) {
               if (var5.cmd == 2) {
                  if (var2 < var5.positionStart + var5.itemCount) {
                     return -1;
                  }

                  var1 = var2 - var5.itemCount;
               } else {
                  var1 = var2;
                  if (var5.cmd == 1) {
                     var1 = var2 + var5.itemCount;
                  }
               }
            }
         }

         ++var4;
      }

      return var2;
   }

   boolean hasAnyUpdateTypes(int var1) {
      boolean var2;
      if ((var1 & this.mExistingUpdateTypes) != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   boolean hasPendingUpdates() {
      boolean var1;
      if (this.mPendingUpdates.size() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   boolean hasUpdates() {
      boolean var1;
      if (!this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public AdapterHelper.UpdateOp obtainUpdateOp(int var1, int var2, int var3, Object var4) {
      AdapterHelper.UpdateOp var5 = (AdapterHelper.UpdateOp)this.mUpdateOpPool.acquire();
      AdapterHelper.UpdateOp var6;
      if (var5 == null) {
         var6 = new AdapterHelper.UpdateOp(var1, var2, var3, var4);
      } else {
         var5.cmd = var1;
         var5.positionStart = var2;
         var5.itemCount = var3;
         var5.payload = var4;
         var6 = var5;
      }

      return var6;
   }

   boolean onItemRangeChanged(int var1, int var2, Object var3) {
      boolean var4 = false;
      if (var2 < 1) {
         return false;
      } else {
         this.mPendingUpdates.add(this.obtainUpdateOp(4, var1, var2, var3));
         this.mExistingUpdateTypes |= 4;
         if (this.mPendingUpdates.size() == 1) {
            var4 = true;
         }

         return var4;
      }
   }

   boolean onItemRangeInserted(int var1, int var2) {
      boolean var3 = false;
      if (var2 < 1) {
         return false;
      } else {
         this.mPendingUpdates.add(this.obtainUpdateOp(1, var1, var2, (Object)null));
         this.mExistingUpdateTypes |= 1;
         if (this.mPendingUpdates.size() == 1) {
            var3 = true;
         }

         return var3;
      }
   }

   boolean onItemRangeMoved(int var1, int var2, int var3) {
      boolean var4 = false;
      if (var1 == var2) {
         return false;
      } else if (var3 != 1) {
         throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
      } else {
         this.mPendingUpdates.add(this.obtainUpdateOp(8, var1, var2, (Object)null));
         this.mExistingUpdateTypes |= 8;
         if (this.mPendingUpdates.size() == 1) {
            var4 = true;
         }

         return var4;
      }
   }

   boolean onItemRangeRemoved(int var1, int var2) {
      boolean var3 = false;
      if (var2 < 1) {
         return false;
      } else {
         this.mPendingUpdates.add(this.obtainUpdateOp(2, var1, var2, (Object)null));
         this.mExistingUpdateTypes |= 2;
         if (this.mPendingUpdates.size() == 1) {
            var3 = true;
         }

         return var3;
      }
   }

   void preProcess() {
      this.mOpReorderer.reorderOps(this.mPendingUpdates);
      int var1 = this.mPendingUpdates.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         AdapterHelper.UpdateOp var3 = (AdapterHelper.UpdateOp)this.mPendingUpdates.get(var2);
         int var4 = var3.cmd;
         if (var4 != 4) {
            if (var4 != 8) {
               switch(var4) {
               case 1:
                  this.applyAdd(var3);
                  break;
               case 2:
                  this.applyRemove(var3);
               }
            } else {
               this.applyMove(var3);
            }
         } else {
            this.applyUpdate(var3);
         }

         if (this.mOnItemProcessedCallback != null) {
            this.mOnItemProcessedCallback.run();
         }
      }

      this.mPendingUpdates.clear();
   }

   public void recycleUpdateOp(AdapterHelper.UpdateOp var1) {
      if (!this.mDisableRecycler) {
         var1.payload = null;
         this.mUpdateOpPool.release(var1);
      }

   }

   void recycleUpdateOpsAndClearList(List var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.recycleUpdateOp((AdapterHelper.UpdateOp)var1.get(var3));
      }

      var1.clear();
   }

   void reset() {
      this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
      this.recycleUpdateOpsAndClearList(this.mPostponedList);
      this.mExistingUpdateTypes = 0;
   }

   interface Callback {
      RecyclerView.ViewHolder findViewHolder(int var1);

      void markViewHoldersUpdated(int var1, int var2, Object var3);

      void offsetPositionsForAdd(int var1, int var2);

      void offsetPositionsForMove(int var1, int var2);

      void offsetPositionsForRemovingInvisible(int var1, int var2);

      void offsetPositionsForRemovingLaidOutOrNewView(int var1, int var2);

      void onDispatchFirstPass(AdapterHelper.UpdateOp var1);

      void onDispatchSecondPass(AdapterHelper.UpdateOp var1);
   }

   static class UpdateOp {
      static final int ADD = 1;
      static final int MOVE = 8;
      static final int POOL_SIZE = 30;
      static final int REMOVE = 2;
      static final int UPDATE = 4;
      int cmd;
      int itemCount;
      Object payload;
      int positionStart;

      UpdateOp(int var1, int var2, int var3, Object var4) {
         this.cmd = var1;
         this.positionStart = var2;
         this.itemCount = var3;
         this.payload = var4;
      }

      String cmdToString() {
         int var1 = this.cmd;
         if (var1 != 4) {
            if (var1 != 8) {
               switch(var1) {
               case 1:
                  return "add";
               case 2:
                  return "rm";
               default:
                  return "??";
               }
            } else {
               return "mv";
            }
         } else {
            return "up";
         }
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            AdapterHelper.UpdateOp var2 = (AdapterHelper.UpdateOp)var1;
            if (this.cmd != var2.cmd) {
               return false;
            } else if (this.cmd == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == var2.positionStart && this.positionStart == var2.itemCount) {
               return true;
            } else if (this.itemCount != var2.itemCount) {
               return false;
            } else if (this.positionStart != var2.positionStart) {
               return false;
            } else {
               if (this.payload != null) {
                  if (!this.payload.equals(var2.payload)) {
                     return false;
                  }
               } else if (var2.payload != null) {
                  return false;
               }

               return true;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return 31 * (this.cmd * 31 + this.positionStart) + this.itemCount;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append("[");
         var1.append(this.cmdToString());
         var1.append(",s:");
         var1.append(this.positionStart);
         var1.append("c:");
         var1.append(this.itemCount);
         var1.append(",p:");
         var1.append(this.payload);
         var1.append("]");
         return var1.toString();
      }
   }
}
