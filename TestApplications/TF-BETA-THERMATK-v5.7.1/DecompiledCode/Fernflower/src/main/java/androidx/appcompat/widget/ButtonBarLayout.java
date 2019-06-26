package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.R$id;
import androidx.appcompat.R$styleable;
import androidx.core.view.ViewCompat;

public class ButtonBarLayout extends LinearLayout {
   private boolean mAllowStacking;
   private int mLastWidthSize = -1;
   private int mMinimumHeight = 0;

   public ButtonBarLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.ButtonBarLayout);
      this.mAllowStacking = var3.getBoolean(R$styleable.ButtonBarLayout_allowStacking, true);
      var3.recycle();
   }

   private int getNextVisibleChildIndex(int var1) {
      for(int var2 = this.getChildCount(); var1 < var2; ++var1) {
         if (this.getChildAt(var1).getVisibility() == 0) {
            return var1;
         }
      }

      return -1;
   }

   private boolean isStacked() {
      int var1 = this.getOrientation();
      boolean var2 = true;
      if (var1 != 1) {
         var2 = false;
      }

      return var2;
   }

   private void setStacked(boolean var1) {
      this.setOrientation(var1);
      byte var2;
      if (var1 != 0) {
         var2 = 5;
      } else {
         var2 = 80;
      }

      this.setGravity(var2);
      View var3 = this.findViewById(R$id.spacer);
      if (var3 != null) {
         if (var1 != 0) {
            var1 = 8;
         } else {
            var1 = 4;
         }

         var3.setVisibility(var1);
      }

      for(int var4 = this.getChildCount() - 2; var4 >= 0; --var4) {
         this.bringChildToFront(this.getChildAt(var4));
      }

   }

   public int getMinimumHeight() {
      return Math.max(this.mMinimumHeight, super.getMinimumHeight());
   }

   protected void onMeasure(int var1, int var2) {
      int var3 = MeasureSpec.getSize(var1);
      boolean var4 = this.mAllowStacking;
      byte var5 = 0;
      if (var4) {
         if (var3 > this.mLastWidthSize && this.isStacked()) {
            this.setStacked(false);
         }

         this.mLastWidthSize = var3;
      }

      int var6;
      boolean var10;
      if (!this.isStacked() && MeasureSpec.getMode(var1) == 1073741824) {
         var6 = MeasureSpec.makeMeasureSpec(var3, Integer.MIN_VALUE);
         var10 = true;
      } else {
         var6 = var1;
         var10 = false;
      }

      super.onMeasure(var6, var2);
      boolean var11 = var10;
      if (this.mAllowStacking) {
         var11 = var10;
         if (!this.isStacked()) {
            boolean var7;
            if ((this.getMeasuredWidthAndState() & -16777216) == 16777216) {
               var7 = true;
            } else {
               var7 = false;
            }

            var11 = var10;
            if (var7) {
               this.setStacked(true);
               var11 = true;
            }
         }
      }

      if (var11) {
         super.onMeasure(var1, var2);
      }

      var3 = this.getNextVisibleChildIndex(0);
      var1 = var5;
      if (var3 >= 0) {
         View var8 = this.getChildAt(var3);
         LayoutParams var9 = (LayoutParams)var8.getLayoutParams();
         var2 = this.getPaddingTop() + var8.getMeasuredHeight() + var9.topMargin + var9.bottomMargin + 0;
         if (this.isStacked()) {
            var3 = this.getNextVisibleChildIndex(var3 + 1);
            var1 = var2;
            if (var3 >= 0) {
               var1 = var2 + this.getChildAt(var3).getPaddingTop() + (int)(this.getResources().getDisplayMetrics().density * 16.0F);
            }
         } else {
            var1 = var2 + this.getPaddingBottom();
         }
      }

      if (ViewCompat.getMinimumHeight(this) != var1) {
         this.setMinimumHeight(var1);
      }

   }

   public void setAllowStacking(boolean var1) {
      if (this.mAllowStacking != var1) {
         this.mAllowStacking = var1;
         if (!this.mAllowStacking && this.getOrientation() == 1) {
            this.setStacked(false);
         }

         this.requestLayout();
      }

   }
}
