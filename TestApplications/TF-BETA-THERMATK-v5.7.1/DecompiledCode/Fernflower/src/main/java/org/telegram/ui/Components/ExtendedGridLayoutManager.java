package org.telegram.ui.Components;

import android.content.Context;
import android.util.SparseIntArray;
import androidx.recyclerview.widget.GridLayoutManager;
import org.telegram.messenger.AndroidUtilities;

public class ExtendedGridLayoutManager extends GridLayoutManager {
   private int calculatedWidth;
   private int firstRowMax;
   private SparseIntArray itemSpans = new SparseIntArray();
   private SparseIntArray itemsToRow = new SparseIntArray();
   private int rowsCount;

   public ExtendedGridLayoutManager(Context var1, int var2) {
      super(var1, var2);
   }

   private void checkLayout() {
      if (this.itemSpans.size() != this.getFlowItemCount() || this.calculatedWidth != this.getWidth()) {
         this.calculatedWidth = this.getWidth();
         this.prepareLayout((float)this.getWidth());
      }

   }

   private void prepareLayout(float var1) {
      float var2 = var1;
      if (var1 == 0.0F) {
         var2 = 100.0F;
      }

      this.itemSpans.clear();
      this.itemsToRow.clear();
      this.rowsCount = 0;
      this.firstRowMax = 0;
      int var3 = AndroidUtilities.dp(100.0F);
      int var4 = this.getFlowItemCount();
      int var5 = this.getSpanCount();
      int var6 = var5;
      int var7 = 0;

      for(int var8 = 0; var7 < var4; ++var7) {
         Size var9 = this.sizeForItem(var7);
         int var10 = Math.min(var5, (int)Math.floor((double)((float)var5 * (var9.width / var9.height * (float)var3 / var2))));
         boolean var11;
         if (var6 < var10 || var10 > 33 && var6 < var10 - 15) {
            var11 = true;
         } else {
            var11 = false;
         }

         int var12;
         int var13;
         int var16;
         if (!var11) {
            var16 = var6;
            var12 = var8;
            var13 = var10;
            if (var6 < var10) {
               var13 = var6;
               var12 = var8;
               var16 = var6;
            }
         } else {
            if (var6 != 0) {
               var12 = var6 / var8;
               var13 = var7 - var8;
               var16 = var13;

               while(true) {
                  int var14 = var13 + var8;
                  if (var16 >= var14) {
                     this.itemsToRow.put(var7 - 1, this.rowsCount);
                     break;
                  }

                  SparseIntArray var15;
                  if (var16 == var14 - 1) {
                     var15 = this.itemSpans;
                     var15.put(var16, var15.get(var16) + var6);
                  } else {
                     var15 = this.itemSpans;
                     var15.put(var16, var15.get(var16) + var12);
                  }

                  var6 -= var12;
                  ++var16;
               }
            }

            ++this.rowsCount;
            var16 = var5;
            var12 = 0;
            var13 = var10;
         }

         if (this.rowsCount == 0) {
            this.firstRowMax = Math.max(this.firstRowMax, var7);
         }

         if (var7 == var4 - 1) {
            this.itemsToRow.put(var7, this.rowsCount);
         }

         var8 = var12 + 1;
         var6 = var16 - var13;
         this.itemSpans.put(var7, var13);
      }

      if (var4 != 0) {
         ++this.rowsCount;
      }

   }

   private Size sizeForItem(int var1) {
      Size var2 = this.getSizeForItem(var1);
      if (var2.width == 0.0F) {
         var2.width = 100.0F;
      }

      if (var2.height == 0.0F) {
         var2.height = 100.0F;
      }

      float var3 = var2.width / var2.height;
      if (var3 > 4.0F || var3 < 0.2F) {
         var3 = Math.max(var2.width, var2.height);
         var2.width = var3;
         var2.height = var3;
      }

      return var2;
   }

   protected int getFlowItemCount() {
      return this.getItemCount();
   }

   public int getRowsCount(int var1) {
      if (this.rowsCount == 0) {
         this.prepareLayout((float)var1);
      }

      return this.rowsCount;
   }

   protected Size getSizeForItem(int var1) {
      return new Size(100.0F, 100.0F);
   }

   public int getSpanSizeForItem(int var1) {
      this.checkLayout();
      return this.itemSpans.get(var1);
   }

   public boolean isFirstRow(int var1) {
      this.checkLayout();
      boolean var2;
      if (var1 <= this.firstRowMax) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isLastInRow(int var1) {
      this.checkLayout();
      boolean var2;
      if (this.itemsToRow.get(var1, Integer.MAX_VALUE) != Integer.MAX_VALUE) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean supportsPredictiveItemAnimations() {
      return false;
   }
}
