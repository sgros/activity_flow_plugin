package org.mozilla.focus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import org.mozilla.focus.R;

public class EqualDistributeGrid extends ViewGroup {
   private int gutter = 0;
   private int rowCapacity = Integer.MAX_VALUE;

   public EqualDistributeGrid(Context var1) {
      super(var1);
   }

   public EqualDistributeGrid(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.EqualDistributeGrid);
      this.rowCapacity = var3.getInt(0, Integer.MAX_VALUE);
      this.gutter = var3.getDimensionPixelSize(1, 0);
      var3.recycle();
   }

   private int calculateHeight(int var1, int var2, int var3, int var4, View var5) {
      int var6 = MeasureSpec.getMode(var2);
      int var7 = MeasureSpec.getSize(var2);
      if (var6 == 1073741824) {
         return var7;
      } else {
         byte var8 = 0;
         if (var4 == 0) {
            return 0;
         } else {
            var2 = var3 / var4;
            byte var9;
            if (var3 % var4 == 0) {
               var9 = 0;
            } else {
               var9 = 1;
            }

            var4 = var2 + var9;
            if (var5 != null && var3 != 0) {
               var2 = var5.getMeasuredHeight() * var4;
            } else {
               var2 = 0;
            }

            var1 = var8;
            if (var5 != null) {
               if (var3 == 0) {
                  var1 = var8;
               } else {
                  var1 = var2 + (var4 - 1) * this.gutter + this.getPaddingTop() + this.getPaddingBottom();
               }
            }

            var2 = var1;
            if (var6 == Integer.MIN_VALUE) {
               var2 = Math.min(var1, var7);
            }

            return var2;
         }
      }
   }

   private int calculateWidth(int var1, int var2, int var3, int var4, View var5) {
      int var6 = MeasureSpec.getMode(var1);
      int var7 = MeasureSpec.getSize(var1);
      if (var6 == 1073741824) {
         return var7;
      } else {
         if (var5 != null && var3 != 0) {
            var1 = var5.getMeasuredWidth() * var4;
         } else {
            var1 = 0;
         }

         var2 = var1;
         if (var6 == Integer.MIN_VALUE) {
            var2 = Math.min(var1, var7);
         }

         return var2;
      }
   }

   private View getFirstNotGoneChild() {
      int var1 = this.getChildCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         View var3 = this.getChildAt(var2);
         if (var3.getVisibility() != 8) {
            return var3;
         }
      }

      return null;
   }

   private int getNotGoneCount() {
      int var1 = this.getChildCount();
      int var2 = 0;

      int var3;
      int var4;
      for(var3 = 0; var2 < var1; var3 = var4) {
         var4 = var3;
         if (this.getChildAt(var2).getVisibility() != 8) {
            var4 = var3 + 1;
         }

         ++var2;
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var3 = this.getNotGoneCount();
      View var6 = this.getFirstNotGoneChild();
      if (var6 != null && var3 != 0) {
         var5 = var6.getMeasuredWidth();
         int var7 = var6.getMeasuredHeight();
         int var8 = this.getPaddingStart();
         int var9 = this.getPaddingEnd();
         int var10 = Math.min(this.rowCapacity, var3);
         var3 = 0;
         if (var10 == 1) {
            var2 = 0;
         } else {
            var2 = (var4 - var2 - var5 * var10 - var8 - var9) / (var10 - 1);
         }

         var9 = this.getChildCount();

         for(var4 = 0; var3 < var9; ++var3) {
            var6 = this.getChildAt(var3);
            if (var6.getVisibility() != 8) {
               int var11 = var4 % var10;
               int var12 = var4 / var10;
               var11 = var11 * var5 + var11 * var2 + var8;
               var12 = var12 * var7 + this.gutter * var12;
               var6.layout(var11, var12, var11 + var5, var12 + var7);
               ++var4;
            }
         }

      }
   }

   protected void onMeasure(int var1, int var2) {
      this.measureChildren(var1, var2);
      int var3 = this.getNotGoneCount();
      int var4 = Math.min(this.rowCapacity, var3);
      View var5 = this.getFirstNotGoneChild();
      this.setMeasuredDimension(this.calculateWidth(var1, var2, var3, var4, var5), this.calculateHeight(var1, var2, var3, var4, var5));
   }
}
