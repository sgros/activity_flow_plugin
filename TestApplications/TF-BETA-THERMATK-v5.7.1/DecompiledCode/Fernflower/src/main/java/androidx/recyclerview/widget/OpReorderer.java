package androidx.recyclerview.widget;

import java.util.List;

class OpReorderer {
   final OpReorderer.Callback mCallback;

   OpReorderer(OpReorderer.Callback var1) {
      this.mCallback = var1;
   }

   private int getLastMoveOutOfOrder(List var1) {
      int var2 = var1.size() - 1;

      boolean var4;
      for(boolean var3 = false; var2 >= 0; var3 = var4) {
         if (((AdapterHelper.UpdateOp)var1.get(var2)).cmd == 8) {
            var4 = var3;
            if (var3) {
               return var2;
            }
         } else {
            var4 = true;
         }

         --var2;
      }

      return -1;
   }

   private void swapMoveAdd(List var1, int var2, AdapterHelper.UpdateOp var3, int var4, AdapterHelper.UpdateOp var5) {
      byte var6;
      if (var3.itemCount < var5.positionStart) {
         var6 = -1;
      } else {
         var6 = 0;
      }

      int var7 = var6;
      if (var3.positionStart < var5.positionStart) {
         var7 = var6 + 1;
      }

      int var8 = var5.positionStart;
      int var9 = var3.positionStart;
      if (var8 <= var9) {
         var3.positionStart = var9 + var5.itemCount;
      }

      var8 = var5.positionStart;
      var9 = var3.itemCount;
      if (var8 <= var9) {
         var3.itemCount = var9 + var5.itemCount;
      }

      var5.positionStart += var7;
      var1.set(var2, var5);
      var1.set(var4, var3);
   }

   private void swapMoveOp(List var1, int var2, int var3) {
      AdapterHelper.UpdateOp var4 = (AdapterHelper.UpdateOp)var1.get(var2);
      AdapterHelper.UpdateOp var5 = (AdapterHelper.UpdateOp)var1.get(var3);
      int var6 = var5.cmd;
      if (var6 != 1) {
         if (var6 != 2) {
            if (var6 == 4) {
               this.swapMoveUpdate(var1, var2, var4, var3, var5);
            }
         } else {
            this.swapMoveRemove(var1, var2, var4, var3, var5);
         }
      } else {
         this.swapMoveAdd(var1, var2, var4, var3, var5);
      }

   }

   void reorderOps(List var1) {
      while(true) {
         int var2 = this.getLastMoveOutOfOrder(var1);
         if (var2 == -1) {
            return;
         }

         this.swapMoveOp(var1, var2, var2 + 1);
      }
   }

   void swapMoveRemove(List var1, int var2, AdapterHelper.UpdateOp var3, int var4, AdapterHelper.UpdateOp var5) {
      int var6;
      int var7;
      boolean var8;
      boolean var12;
      label105: {
         var6 = var3.positionStart;
         var7 = var3.itemCount;
         var8 = false;
         if (var6 < var7) {
            if (var5.positionStart != var6 || var5.itemCount != var7 - var6) {
               var12 = false;
               break label105;
            }

            var12 = false;
         } else {
            if (var5.positionStart != var7 + 1 || var5.itemCount != var6 - var7) {
               var12 = true;
               break label105;
            }

            var12 = true;
         }

         var8 = true;
      }

      var7 = var3.itemCount;
      int var9 = var5.positionStart;
      int var10;
      if (var7 < var9) {
         var5.positionStart = var9 - 1;
      } else {
         var10 = var5.itemCount;
         if (var7 < var9 + var10) {
            var5.itemCount = var10 - 1;
            var3.cmd = 2;
            var3.itemCount = 1;
            if (var5.itemCount == 0) {
               var1.remove(var4);
               this.mCallback.recycleUpdateOp(var5);
            }

            return;
         }
      }

      var10 = var3.positionStart;
      var9 = var5.positionStart;
      AdapterHelper.UpdateOp var11 = null;
      if (var10 <= var9) {
         var5.positionStart = var9 + 1;
      } else {
         var7 = var5.itemCount;
         if (var10 < var9 + var7) {
            var11 = this.mCallback.obtainUpdateOp(2, var10 + 1, var9 + var7 - var10, (Object)null);
            var5.itemCount = var3.positionStart - var5.positionStart;
         }
      }

      if (var8) {
         var1.set(var2, var5);
         var1.remove(var4);
         this.mCallback.recycleUpdateOp(var3);
      } else {
         if (var12) {
            if (var11 != null) {
               var6 = var3.positionStart;
               if (var6 > var11.positionStart) {
                  var3.positionStart = var6 - var11.itemCount;
               }

               var6 = var3.itemCount;
               if (var6 > var11.positionStart) {
                  var3.itemCount = var6 - var11.itemCount;
               }
            }

            var6 = var3.positionStart;
            if (var6 > var5.positionStart) {
               var3.positionStart = var6 - var5.itemCount;
            }

            var6 = var3.itemCount;
            if (var6 > var5.positionStart) {
               var3.itemCount = var6 - var5.itemCount;
            }
         } else {
            if (var11 != null) {
               var6 = var3.positionStart;
               if (var6 >= var11.positionStart) {
                  var3.positionStart = var6 - var11.itemCount;
               }

               var6 = var3.itemCount;
               if (var6 >= var11.positionStart) {
                  var3.itemCount = var6 - var11.itemCount;
               }
            }

            var6 = var3.positionStart;
            if (var6 >= var5.positionStart) {
               var3.positionStart = var6 - var5.itemCount;
            }

            var6 = var3.itemCount;
            if (var6 >= var5.positionStart) {
               var3.itemCount = var6 - var5.itemCount;
            }
         }

         var1.set(var2, var5);
         if (var3.positionStart != var3.itemCount) {
            var1.set(var4, var3);
         } else {
            var1.remove(var4);
         }

         if (var11 != null) {
            var1.add(var2, var11);
         }

      }
   }

   void swapMoveUpdate(List var1, int var2, AdapterHelper.UpdateOp var3, int var4, AdapterHelper.UpdateOp var5) {
      int var6;
      int var7;
      AdapterHelper.UpdateOp var8;
      int var9;
      AdapterHelper.UpdateOp var10;
      label33: {
         var6 = var3.itemCount;
         var7 = var5.positionStart;
         var8 = null;
         if (var6 < var7) {
            var5.positionStart = var7 - 1;
         } else {
            var9 = var5.itemCount;
            if (var6 < var7 + var9) {
               var5.itemCount = var9 - 1;
               var10 = this.mCallback.obtainUpdateOp(4, var3.positionStart, 1, var5.payload);
               break label33;
            }
         }

         var10 = null;
      }

      var9 = var3.positionStart;
      var6 = var5.positionStart;
      if (var9 <= var6) {
         var5.positionStart = var6 + 1;
      } else {
         var7 = var5.itemCount;
         if (var9 < var6 + var7) {
            var7 = var6 + var7 - var9;
            var8 = this.mCallback.obtainUpdateOp(4, var9 + 1, var7, var5.payload);
            var5.itemCount -= var7;
         }
      }

      var1.set(var4, var3);
      if (var5.itemCount > 0) {
         var1.set(var2, var5);
      } else {
         var1.remove(var2);
         this.mCallback.recycleUpdateOp(var5);
      }

      if (var10 != null) {
         var1.add(var2, var10);
      }

      if (var8 != null) {
         var1.add(var2, var8);
      }

   }

   interface Callback {
      AdapterHelper.UpdateOp obtainUpdateOp(int var1, int var2, int var3, Object var4);

      void recycleUpdateOp(AdapterHelper.UpdateOp var1);
   }
}
