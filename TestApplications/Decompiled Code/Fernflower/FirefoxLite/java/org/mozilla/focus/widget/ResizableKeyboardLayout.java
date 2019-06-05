package org.mozilla.focus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.view.ViewGroup.MarginLayoutParams;
import org.mozilla.focus.R;

public class ResizableKeyboardLayout extends CoordinatorLayout {
   private final int idOfViewToHide;
   private int marginBottom;
   private View viewToHide;

   public ResizableKeyboardLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public ResizableKeyboardLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public ResizableKeyboardLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      TypedArray var6 = this.getContext().getTheme().obtainStyledAttributes(var2, R.styleable.ResizableKeyboardLayout, 0, 0);

      try {
         this.idOfViewToHide = var6.getResourceId(0, -1);
      } finally {
         var6.recycle();
      }

      this.setOnApplyWindowInsetsListener(new _$$Lambda$ResizableKeyboardLayout$H8zzoWULdCUNHnihVFXycNJ6TkA(this));
   }

   // $FF: synthetic method
   public static WindowInsets lambda$new$0(ResizableKeyboardLayout var0, View var1, WindowInsets var2) {
      int var3 = var2.getSystemWindowInsetBottom();
      if (var3 != 0) {
         if (var0.getPaddingBottom() != var3) {
            var0.setPadding(0, 0, 0, var3);
            if (var0.getLayoutParams() instanceof MarginLayoutParams) {
               ((MarginLayoutParams)var0.getLayoutParams()).bottomMargin = 0;
            }

            if (var0.viewToHide != null) {
               var0.viewToHide.setVisibility(8);
            }
         }
      } else if (var0.getPaddingBottom() != 0) {
         var0.setPadding(0, 0, 0, 0);
         ((MarginLayoutParams)var0.getLayoutParams()).bottomMargin = var0.marginBottom;
         if (var0.viewToHide != null) {
            var0.viewToHide.setVisibility(0);
         }
      }

      return var2;
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.idOfViewToHide != -1) {
         this.viewToHide = this.findViewById(this.idOfViewToHide);
      }

   }

   public void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.viewToHide = null;
   }

   public void setLayoutParams(android.view.ViewGroup.LayoutParams var1) {
      super.setLayoutParams(var1);
      if (var1 instanceof MarginLayoutParams) {
         this.marginBottom = ((MarginLayoutParams)var1).bottomMargin;
      }

   }
}
