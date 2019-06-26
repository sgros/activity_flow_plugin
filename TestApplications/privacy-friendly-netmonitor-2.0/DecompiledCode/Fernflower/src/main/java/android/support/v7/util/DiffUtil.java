package android.support.v7.util;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DiffUtil {
   private static final Comparator SNAKE_COMPARATOR = new Comparator() {
      public int compare(DiffUtil.Snake var1, DiffUtil.Snake var2) {
         int var3 = var1.x - var2.x;
         int var4 = var3;
         if (var3 == 0) {
            var4 = var1.y - var2.y;
         }

         return var4;
      }
   };

   private DiffUtil() {
   }

   public static DiffUtil.DiffResult calculateDiff(DiffUtil.Callback var0) {
      return calculateDiff(var0, true);
   }

   public static DiffUtil.DiffResult calculateDiff(DiffUtil.Callback var0, boolean var1) {
      int var2 = var0.getOldListSize();
      int var3 = var0.getNewListSize();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      var5.add(new DiffUtil.Range(0, var2, 0, var3));
      var2 = Math.abs(var2 - var3) + var2 + var3;
      var3 = var2 * 2;
      int[] var6 = new int[var3];
      int[] var7 = new int[var3];
      ArrayList var8 = new ArrayList();

      while(!var5.isEmpty()) {
         DiffUtil.Range var9 = (DiffUtil.Range)var5.remove(var5.size() - 1);
         DiffUtil.Snake var10 = diffPartial(var0, var9.oldListStart, var9.oldListEnd, var9.newListStart, var9.newListEnd, var6, var7, var2);
         if (var10 != null) {
            if (var10.size > 0) {
               var4.add(var10);
            }

            var10.x += var9.oldListStart;
            var10.y += var9.newListStart;
            DiffUtil.Range var11;
            if (var8.isEmpty()) {
               var11 = new DiffUtil.Range();
            } else {
               var11 = (DiffUtil.Range)var8.remove(var8.size() - 1);
            }

            var11.oldListStart = var9.oldListStart;
            var11.newListStart = var9.newListStart;
            if (var10.reverse) {
               var11.oldListEnd = var10.x;
               var11.newListEnd = var10.y;
            } else if (var10.removal) {
               var11.oldListEnd = var10.x - 1;
               var11.newListEnd = var10.y;
            } else {
               var11.oldListEnd = var10.x;
               var11.newListEnd = var10.y - 1;
            }

            var5.add(var11);
            if (var10.reverse) {
               if (var10.removal) {
                  var9.oldListStart = var10.x + var10.size + 1;
                  var9.newListStart = var10.y + var10.size;
               } else {
                  var9.oldListStart = var10.x + var10.size;
                  var9.newListStart = var10.y + var10.size + 1;
               }
            } else {
               var9.oldListStart = var10.x + var10.size;
               var9.newListStart = var10.y + var10.size;
            }

            var5.add(var9);
         } else {
            var8.add(var9);
         }
      }

      Collections.sort(var4, SNAKE_COMPARATOR);
      return new DiffUtil.DiffResult(var0, var4, var6, var7, var1);
   }

   private static DiffUtil.Snake diffPartial(DiffUtil.Callback var0, int var1, int var2, int var3, int var4, int[] var5, int[] var6, int var7) {
      int var8 = var2 - var1;
      var2 = var4 - var3;
      if (var8 >= 1 && var2 >= 1) {
         int var9 = var8 - var2;
         int var10 = (var8 + var2 + 1) / 2;
         int var11 = var7 - var10 - 1;
         var4 = var7 + var10 + 1;
         Arrays.fill(var5, var11, var4, 0);
         Arrays.fill(var6, var11 + var9, var4 + var9, var8);
         boolean var19;
         if (var9 % 2 != 0) {
            var19 = true;
         } else {
            var19 = false;
         }

         int var12 = 0;

         for(var4 = var8; var12 <= var10; ++var12) {
            int var13 = -var12;

            int var14;
            boolean var15;
            int var16;
            DiffUtil.Snake var18;
            for(var14 = var13; var14 <= var12; var14 += 2) {
               label132: {
                  if (var14 != var13) {
                     label131: {
                        if (var14 != var12) {
                           var8 = var7 + var14;
                           if (var5[var8 - 1] < var5[var8 + 1]) {
                              break label131;
                           }
                        }

                        var8 = var5[var7 + var14 - 1] + 1;
                        var15 = true;
                        break label132;
                     }
                  }

                  var8 = var5[var7 + var14 + 1];
                  var15 = false;
               }

               for(var16 = var8 - var14; var8 < var4 && var16 < var2 && var0.areItemsTheSame(var1 + var8, var3 + var16); ++var16) {
                  ++var8;
               }

               var16 = var7 + var14;
               var5[var16] = var8;
               if (var19 && var14 >= var9 - var12 + 1 && var14 <= var9 + var12 - 1 && var5[var16] >= var6[var16]) {
                  var18 = new DiffUtil.Snake();
                  var18.x = var6[var16];
                  var18.y = var18.x - var14;
                  var18.size = var5[var16] - var6[var16];
                  var18.removal = var15;
                  var18.reverse = false;
                  return var18;
               }
            }

            var15 = false;

            for(var14 = var13; var14 <= var12; var15 = false) {
               int var17;
               label103: {
                  label102: {
                     var17 = var14 + var9;
                     if (var17 != var12 + var9) {
                        if (var17 == var13 + var9) {
                           break label102;
                        }

                        var8 = var7 + var17;
                        if (var6[var8 - 1] >= var6[var8 + 1]) {
                           break label102;
                        }
                     }

                     var8 = var6[var7 + var17 - 1];
                     break label103;
                  }

                  var8 = var6[var7 + var17 + 1] - 1;
                  var15 = true;
               }

               for(var16 = var8 - var17; var8 > 0 && var16 > 0 && var0.areItemsTheSame(var1 + var8 - 1, var3 + var16 - 1); --var16) {
                  --var8;
               }

               var16 = var7 + var17;
               var6[var16] = var8;
               if (!var19 && var17 >= var13 && var17 <= var12 && var5[var16] >= var6[var16]) {
                  var18 = new DiffUtil.Snake();
                  var18.x = var6[var16];
                  var18.y = var18.x - var17;
                  var18.size = var5[var16] - var6[var16];
                  var18.removal = var15;
                  var18.reverse = true;
                  return var18;
               }

               var14 += 2;
            }
         }

         throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
      } else {
         return null;
      }
   }

   public abstract static class Callback {
      public abstract boolean areContentsTheSame(int var1, int var2);

      public abstract boolean areItemsTheSame(int var1, int var2);

      @Nullable
      public Object getChangePayload(int var1, int var2) {
         return null;
      }

      public abstract int getNewListSize();

      public abstract int getOldListSize();
   }

   public static class DiffResult {
      private static final int FLAG_CHANGED = 2;
      private static final int FLAG_IGNORE = 16;
      private static final int FLAG_MASK = 31;
      private static final int FLAG_MOVED_CHANGED = 4;
      private static final int FLAG_MOVED_NOT_CHANGED = 8;
      private static final int FLAG_NOT_CHANGED = 1;
      private static final int FLAG_OFFSET = 5;
      private final DiffUtil.Callback mCallback;
      private final boolean mDetectMoves;
      private final int[] mNewItemStatuses;
      private final int mNewListSize;
      private final int[] mOldItemStatuses;
      private final int mOldListSize;
      private final List mSnakes;

      DiffResult(DiffUtil.Callback var1, List var2, int[] var3, int[] var4, boolean var5) {
         this.mSnakes = var2;
         this.mOldItemStatuses = var3;
         this.mNewItemStatuses = var4;
         Arrays.fill(this.mOldItemStatuses, 0);
         Arrays.fill(this.mNewItemStatuses, 0);
         this.mCallback = var1;
         this.mOldListSize = var1.getOldListSize();
         this.mNewListSize = var1.getNewListSize();
         this.mDetectMoves = var5;
         this.addRootSnake();
         this.findMatchingItems();
      }

      private void addRootSnake() {
         DiffUtil.Snake var1;
         if (this.mSnakes.isEmpty()) {
            var1 = null;
         } else {
            var1 = (DiffUtil.Snake)this.mSnakes.get(0);
         }

         if (var1 == null || var1.x != 0 || var1.y != 0) {
            var1 = new DiffUtil.Snake();
            var1.x = 0;
            var1.y = 0;
            var1.removal = false;
            var1.size = 0;
            var1.reverse = false;
            this.mSnakes.add(0, var1);
         }

      }

      private void dispatchAdditions(List var1, ListUpdateCallback var2, int var3, int var4, int var5) {
         if (!this.mDetectMoves) {
            var2.onInserted(var3, var4);
         } else {
            --var4;

            for(; var4 >= 0; --var4) {
               int[] var6 = this.mNewItemStatuses;
               int var7 = var5 + var4;
               int var8 = var6[var7] & 31;
               if (var8 != 0) {
                  if (var8 != 4 && var8 != 8) {
                     if (var8 != 16) {
                        StringBuilder var11 = new StringBuilder();
                        var11.append("unknown flag for pos ");
                        var11.append(var7);
                        var11.append(" ");
                        var11.append(Long.toBinaryString((long)var8));
                        throw new IllegalStateException(var11.toString());
                     }

                     var1.add(new DiffUtil.PostponedUpdate(var7, var3, false));
                  } else {
                     int var9 = this.mNewItemStatuses[var7] >> 5;
                     var2.onMoved(removePostponedUpdate(var1, var9, true).currentPos, var3);
                     if (var8 == 4) {
                        var2.onChanged(var3, 1, this.mCallback.getChangePayload(var9, var7));
                     }
                  }
               } else {
                  var2.onInserted(var3, 1);

                  DiffUtil.PostponedUpdate var12;
                  for(Iterator var10 = var1.iterator(); var10.hasNext(); ++var12.currentPos) {
                     var12 = (DiffUtil.PostponedUpdate)var10.next();
                  }
               }
            }

         }
      }

      private void dispatchRemovals(List var1, ListUpdateCallback var2, int var3, int var4, int var5) {
         if (!this.mDetectMoves) {
            var2.onRemoved(var3, var4);
         } else {
            --var4;

            for(; var4 >= 0; --var4) {
               int[] var6 = this.mOldItemStatuses;
               int var7 = var5 + var4;
               int var8 = var6[var7] & 31;
               if (var8 != 0) {
                  if (var8 != 4 && var8 != 8) {
                     if (var8 != 16) {
                        StringBuilder var11 = new StringBuilder();
                        var11.append("unknown flag for pos ");
                        var11.append(var7);
                        var11.append(" ");
                        var11.append(Long.toBinaryString((long)var8));
                        throw new IllegalStateException(var11.toString());
                     }

                     var1.add(new DiffUtil.PostponedUpdate(var7, var3 + var4, true));
                  } else {
                     int var9 = this.mOldItemStatuses[var7] >> 5;
                     DiffUtil.PostponedUpdate var13 = removePostponedUpdate(var1, var9, false);
                     var2.onMoved(var3 + var4, var13.currentPos - 1);
                     if (var8 == 4) {
                        var2.onChanged(var13.currentPos - 1, 1, this.mCallback.getChangePayload(var7, var9));
                     }
                  }
               } else {
                  var2.onRemoved(var3 + var4, 1);

                  DiffUtil.PostponedUpdate var10;
                  for(Iterator var12 = var1.iterator(); var12.hasNext(); --var10.currentPos) {
                     var10 = (DiffUtil.PostponedUpdate)var12.next();
                  }
               }
            }

         }
      }

      private void findAddition(int var1, int var2, int var3) {
         if (this.mOldItemStatuses[var1 - 1] == 0) {
            this.findMatchingItem(var1, var2, var3, false);
         }
      }

      private boolean findMatchingItem(int var1, int var2, int var3, boolean var4) {
         int var5;
         int var6;
         if (var4) {
            var5 = var2 - 1;
            var2 = var1;
            var6 = var5;
         } else {
            int var7 = var1 - 1;
            var6 = var7;
            var5 = var2;
            var2 = var7;
         }

         while(var3 >= 0) {
            DiffUtil.Snake var8 = (DiffUtil.Snake)this.mSnakes.get(var3);
            int var9 = var8.x;
            int var10 = var8.size;
            int var11 = var8.y;
            int var12 = var8.size;
            byte var13 = 4;
            if (var4) {
               --var2;

               while(var2 >= var9 + var10) {
                  if (this.mCallback.areItemsTheSame(var2, var6)) {
                     if (this.mCallback.areContentsTheSame(var2, var6)) {
                        var13 = 8;
                     }

                     this.mNewItemStatuses[var6] = var2 << 5 | 16;
                     this.mOldItemStatuses[var2] = var6 << 5 | var13;
                     return true;
                  }

                  --var2;
               }
            } else {
               for(var2 = var5 - 1; var2 >= var11 + var12; --var2) {
                  if (this.mCallback.areItemsTheSame(var6, var2)) {
                     if (this.mCallback.areContentsTheSame(var6, var2)) {
                        var13 = 8;
                     }

                     int[] var14 = this.mOldItemStatuses;
                     --var1;
                     var14[var1] = var2 << 5 | 16;
                     this.mNewItemStatuses[var2] = var1 << 5 | var13;
                     return true;
                  }
               }
            }

            var2 = var8.x;
            var5 = var8.y;
            --var3;
         }

         return false;
      }

      private void findMatchingItems() {
         int var1 = this.mOldListSize;
         int var2 = this.mNewListSize;

         for(int var3 = this.mSnakes.size() - 1; var3 >= 0; --var3) {
            DiffUtil.Snake var4 = (DiffUtil.Snake)this.mSnakes.get(var3);
            int var5 = var4.x;
            int var6 = var4.size;
            int var7 = var4.y;
            int var8 = var4.size;
            int var9;
            if (this.mDetectMoves) {
               while(true) {
                  var9 = var2;
                  if (var1 <= var5 + var6) {
                     while(var9 > var7 + var8) {
                        this.findRemoval(var1, var9, var3);
                        --var9;
                     }
                     break;
                  }

                  this.findAddition(var1, var2, var3);
                  --var1;
               }
            }

            for(var2 = 0; var2 < var4.size; ++var2) {
               var9 = var4.x + var2;
               var8 = var4.y + var2;
               byte var10;
               if (this.mCallback.areContentsTheSame(var9, var8)) {
                  var10 = 1;
               } else {
                  var10 = 2;
               }

               this.mOldItemStatuses[var9] = var8 << 5 | var10;
               this.mNewItemStatuses[var8] = var9 << 5 | var10;
            }

            var1 = var4.x;
            var2 = var4.y;
         }

      }

      private void findRemoval(int var1, int var2, int var3) {
         if (this.mNewItemStatuses[var2 - 1] == 0) {
            this.findMatchingItem(var1, var2, var3, true);
         }
      }

      private static DiffUtil.PostponedUpdate removePostponedUpdate(List var0, int var1, boolean var2) {
         for(int var3 = var0.size() - 1; var3 >= 0; --var3) {
            DiffUtil.PostponedUpdate var4 = (DiffUtil.PostponedUpdate)var0.get(var3);
            if (var4.posInOwnerList == var1 && var4.removal == var2) {
               var0.remove(var3);

               while(var3 < var0.size()) {
                  DiffUtil.PostponedUpdate var5 = (DiffUtil.PostponedUpdate)var0.get(var3);
                  int var6 = var5.currentPos;
                  byte var7;
                  if (var2) {
                     var7 = 1;
                  } else {
                     var7 = -1;
                  }

                  var5.currentPos = var6 + var7;
                  ++var3;
               }

               return var4;
            }
         }

         return null;
      }

      public void dispatchUpdatesTo(ListUpdateCallback var1) {
         BatchingListUpdateCallback var10;
         if (var1 instanceof BatchingListUpdateCallback) {
            var10 = (BatchingListUpdateCallback)var1;
         } else {
            var10 = new BatchingListUpdateCallback(var1);
         }

         ArrayList var2 = new ArrayList();
         int var3 = this.mOldListSize;
         int var4 = this.mNewListSize;
         int var5 = this.mSnakes.size();
         --var5;

         while(var5 >= 0) {
            DiffUtil.Snake var6 = (DiffUtil.Snake)this.mSnakes.get(var5);
            int var7 = var6.size;
            int var8 = var6.x + var7;
            int var9 = var6.y + var7;
            if (var8 < var3) {
               this.dispatchRemovals(var2, var10, var8, var3 - var8, var8);
            }

            if (var9 < var4) {
               this.dispatchAdditions(var2, var10, var8, var4 - var9, var9);
            }

            for(var4 = var7 - 1; var4 >= 0; --var4) {
               if ((this.mOldItemStatuses[var6.x + var4] & 31) == 2) {
                  var10.onChanged(var6.x + var4, 1, this.mCallback.getChangePayload(var6.x + var4, var6.y + var4));
               }
            }

            var3 = var6.x;
            var4 = var6.y;
            --var5;
         }

         var10.dispatchLastEvent();
      }

      public void dispatchUpdatesTo(final RecyclerView.Adapter var1) {
         this.dispatchUpdatesTo(new ListUpdateCallback() {
            public void onChanged(int var1x, int var2, Object var3) {
               var1.notifyItemRangeChanged(var1x, var2, var3);
            }

            public void onInserted(int var1x, int var2) {
               var1.notifyItemRangeInserted(var1x, var2);
            }

            public void onMoved(int var1x, int var2) {
               var1.notifyItemMoved(var1x, var2);
            }

            public void onRemoved(int var1x, int var2) {
               var1.notifyItemRangeRemoved(var1x, var2);
            }
         });
      }

      @VisibleForTesting
      List getSnakes() {
         return this.mSnakes;
      }
   }

   private static class PostponedUpdate {
      int currentPos;
      int posInOwnerList;
      boolean removal;

      public PostponedUpdate(int var1, int var2, boolean var3) {
         this.posInOwnerList = var1;
         this.currentPos = var2;
         this.removal = var3;
      }
   }

   static class Range {
      int newListEnd;
      int newListStart;
      int oldListEnd;
      int oldListStart;

      public Range() {
      }

      public Range(int var1, int var2, int var3, int var4) {
         this.oldListStart = var1;
         this.oldListEnd = var2;
         this.newListStart = var3;
         this.newListEnd = var4;
      }
   }

   static class Snake {
      boolean removal;
      boolean reverse;
      int size;
      int x;
      int y;
   }
}
