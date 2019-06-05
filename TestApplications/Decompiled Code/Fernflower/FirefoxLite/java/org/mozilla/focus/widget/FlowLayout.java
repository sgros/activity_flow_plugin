package org.mozilla.focus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import org.mozilla.focus.R;

public class FlowLayout extends ViewGroup {
   private int mSpacing;

   public FlowLayout(Context var1) {
      super(var1);
   }

   public FlowLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.FlowLayout);
      this.mSpacing = var3.getDimensionPixelSize(2, (int)var1.getResources().getDimension(2131165350));
      var3.recycle();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = this.getChildCount();
      int var7 = this.getMeasuredHeight();
      int var8 = 0;
      var3 = 0;

      for(int var9 = 0; var8 < var6; var7 = var5) {
         View var10 = this.getChildAt(var8);
         if (var10.getVisibility() == 8) {
            var5 = var7;
         } else {
            int var11 = var10.getMeasuredWidth();
            int var12 = var10.getMeasuredHeight();
            var5 = var7;
            int var13 = var3;
            if (var3 + var11 > var4 - var2) {
               var5 = var7 - (var9 + this.mSpacing);
               var13 = 0;
            }

            var10.layout(var13, var5 - var12, var13 + var11, var5);
            var3 = var13 + var11 + this.mSpacing;
            var9 = var12;
         }

         ++var8;
      }

   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      int var4 = this.getChildCount();
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      boolean var8 = true;

      boolean var12;
      for(int var9 = 0; var5 < var4; var8 = var12) {
         View var10 = this.getChildAt(var5);
         int var11;
         if (var10.getVisibility() == 8) {
            var11 = var7;
            var12 = var8;
         } else {
            int var13;
            int var15;
            label36: {
               this.measureChild(var10, var1, var2);
               var13 = var10.getMeasuredWidth();
               int var14 = var10.getMeasuredHeight();
               if (!var8) {
                  var11 = var7;
                  var12 = var8;
                  var15 = var9;
                  if (var9 + var13 <= var3) {
                     break label36;
                  }
               }

               var9 = var7 + var14;
               var11 = var9;
               if (!var8) {
                  var11 = var9 + this.mSpacing;
               }

               var12 = false;
               var15 = 0;
            }

            var9 = var15 + var13;
            int var16 = var6;
            if (var9 > var6) {
               var16 = var9;
            }

            var9 += this.mSpacing;
            var6 = var16;
         }

         ++var5;
         var7 = var11;
      }

      this.setMeasuredDimension(var6, var7);
   }
}
