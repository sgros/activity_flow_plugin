package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import androidx.appcompat.R$id;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public class AlertDialogLayout extends LinearLayoutCompat {
   public AlertDialogLayout(Context var1) {
      super(var1);
   }

   public AlertDialogLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   private void forceUniformWidth(int var1, int var2) {
      int var3 = MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);

      for(int var4 = 0; var4 < var1; ++var4) {
         View var5 = this.getChildAt(var4);
         if (var5.getVisibility() != 8) {
            LinearLayoutCompat.LayoutParams var6 = (LinearLayoutCompat.LayoutParams)var5.getLayoutParams();
            if (var6.width == -1) {
               int var7 = var6.height;
               var6.height = var5.getMeasuredHeight();
               this.measureChildWithMargins(var5, var3, 0, var2, 0);
               var6.height = var7;
            }
         }
      }

   }

   private static int resolveMinimumHeight(View var0) {
      int var1 = ViewCompat.getMinimumHeight(var0);
      if (var1 > 0) {
         return var1;
      } else {
         if (var0 instanceof ViewGroup) {
            ViewGroup var2 = (ViewGroup)var0;
            if (var2.getChildCount() == 1) {
               return resolveMinimumHeight(var2.getChildAt(0));
            }
         }

         return 0;
      }
   }

   private void setChildFrame(View var1, int var2, int var3, int var4, int var5) {
      var1.layout(var2, var3, var4 + var2, var5 + var3);
   }

   private boolean tryOnMeasure(int var1, int var2) {
      int var3 = this.getChildCount();
      View var4 = null;
      View var6 = var4;
      int var7 = 0;

      View var5;
      View var8;
      int var9;
      for(var8 = var4; var7 < var3; ++var7) {
         var5 = this.getChildAt(var7);
         if (var5.getVisibility() != 8) {
            var9 = var5.getId();
            if (var9 == R$id.topPanel) {
               var4 = var5;
            } else if (var9 == R$id.buttonPanel) {
               var8 = var5;
            } else {
               if (var9 != R$id.contentPanel && var9 != R$id.customPanel) {
                  return false;
               }

               if (var6 != null) {
                  return false;
               }

               var6 = var5;
            }
         }
      }

      int var10 = MeasureSpec.getMode(var2);
      int var11 = MeasureSpec.getSize(var2);
      int var12 = MeasureSpec.getMode(var1);
      int var13 = this.getPaddingTop() + this.getPaddingBottom();
      if (var4 != null) {
         var4.measure(var1, 0);
         var13 += var4.getMeasuredHeight();
         var9 = View.combineMeasuredStates(0, var4.getMeasuredState());
      } else {
         var9 = 0;
      }

      int var14;
      if (var8 != null) {
         var8.measure(var1, 0);
         var7 = resolveMinimumHeight(var8);
         var14 = var8.getMeasuredHeight() - var7;
         var13 += var7;
         var9 = View.combineMeasuredStates(var9, var8.getMeasuredState());
      } else {
         var7 = 0;
         var14 = 0;
      }

      int var15;
      int var16;
      if (var6 != null) {
         if (var10 == 0) {
            var15 = 0;
         } else {
            var15 = MeasureSpec.makeMeasureSpec(Math.max(0, var11 - var13), var10);
         }

         var6.measure(var1, var15);
         var16 = var6.getMeasuredHeight();
         var13 += var16;
         var9 = View.combineMeasuredStates(var9, var6.getMeasuredState());
      } else {
         var16 = 0;
      }

      int var17 = var11 - var13;
      var11 = var9;
      int var18 = var17;
      var15 = var13;
      if (var8 != null) {
         var14 = Math.min(var17, var14);
         var11 = var17;
         var15 = var7;
         if (var14 > 0) {
            var11 = var17 - var14;
            var15 = var7 + var14;
         }

         var8.measure(var1, MeasureSpec.makeMeasureSpec(var15, 1073741824));
         var15 = var13 - var7 + var8.getMeasuredHeight();
         var7 = View.combineMeasuredStates(var9, var8.getMeasuredState());
         var18 = var11;
         var11 = var7;
      }

      var9 = var11;
      var7 = var15;
      if (var6 != null) {
         var9 = var11;
         var7 = var15;
         if (var18 > 0) {
            var6.measure(var1, MeasureSpec.makeMeasureSpec(var16 + var18, var10));
            var7 = var15 - var16 + var6.getMeasuredHeight();
            var9 = View.combineMeasuredStates(var11, var6.getMeasuredState());
         }
      }

      var11 = 0;

      for(var13 = 0; var11 < var3; var13 = var15) {
         var5 = this.getChildAt(var11);
         var15 = var13;
         if (var5.getVisibility() != 8) {
            var15 = Math.max(var13, var5.getMeasuredWidth());
         }

         ++var11;
      }

      this.setMeasuredDimension(View.resolveSizeAndState(var13 + this.getPaddingLeft() + this.getPaddingRight(), var1, var9), View.resolveSizeAndState(var7, var2, 0));
      if (var12 != 1073741824) {
         this.forceUniformWidth(var3, var2);
      }

      return true;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = this.getPaddingLeft();
      int var7 = var4 - var2;
      int var8 = this.getPaddingRight();
      int var9 = this.getPaddingRight();
      var2 = this.getMeasuredHeight();
      int var10 = this.getChildCount();
      int var11 = this.getGravity();
      var4 = var11 & 112;
      if (var4 != 16) {
         if (var4 != 80) {
            var2 = this.getPaddingTop();
         } else {
            var2 = this.getPaddingTop() + var5 - var3 - var2;
         }
      } else {
         var2 = this.getPaddingTop() + (var5 - var3 - var2) / 2;
      }

      Drawable var12 = this.getDividerDrawable();
      if (var12 == null) {
         var3 = 0;
      } else {
         var3 = var12.getIntrinsicHeight();
      }

      for(var4 = 0; var4 < var10; var2 = var5) {
         View var13 = this.getChildAt(var4);
         var5 = var2;
         if (var13 != null) {
            var5 = var2;
            if (var13.getVisibility() != 8) {
               int var14 = var13.getMeasuredWidth();
               int var15 = var13.getMeasuredHeight();
               LinearLayoutCompat.LayoutParams var17 = (LinearLayoutCompat.LayoutParams)var13.getLayoutParams();
               int var16 = var17.gravity;
               var5 = var16;
               if (var16 < 0) {
                  var5 = var11 & 8388615;
               }

               label48: {
                  var5 = GravityCompat.getAbsoluteGravity(var5, ViewCompat.getLayoutDirection(this)) & 7;
                  if (var5 != 1) {
                     if (var5 != 5) {
                        var5 = var17.leftMargin + var6;
                        break label48;
                     }

                     var5 = var7 - var8 - var14;
                     var16 = var17.rightMargin;
                  } else {
                     var5 = (var7 - var6 - var9 - var14) / 2 + var6 + var17.leftMargin;
                     var16 = var17.rightMargin;
                  }

                  var5 -= var16;
               }

               var16 = var2;
               if (this.hasDividerBeforeChildAt(var4)) {
                  var16 = var2 + var3;
               }

               var2 = var16 + var17.topMargin;
               this.setChildFrame(var13, var5, var2, var14, var15);
               var5 = var2 + var15 + var17.bottomMargin;
            }
         }

         ++var4;
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (!this.tryOnMeasure(var1, var2)) {
         super.onMeasure(var1, var2);
      }

   }
}
