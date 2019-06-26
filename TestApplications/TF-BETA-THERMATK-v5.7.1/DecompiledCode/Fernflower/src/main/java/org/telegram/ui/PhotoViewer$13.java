package org.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;

class PhotoViewer$13 extends FrameLayout {
   // $FF: synthetic field
   final PhotoViewer this$0;

   PhotoViewer$13(PhotoViewer var1, Context var2) {
      super(var2);
      this.this$0 = var1;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var5 = this.getChildCount();
      int var6 = var4 - var2;
      View var7 = null;

      for(var2 = 0; var2 < var5; ++var2) {
         View var8 = this.getChildAt(var2);
         if ((Integer)var8.getTag() == -1) {
            var8.layout(var6 - this.getPaddingRight() - var8.getMeasuredWidth(), this.getPaddingTop(), var6 - this.getPaddingRight() + var8.getMeasuredWidth(), this.getPaddingTop() + var8.getMeasuredHeight());
            var7 = var8;
         } else if ((Integer)var8.getTag() == -2) {
            var4 = var6 - this.getPaddingRight() - var8.getMeasuredWidth();
            var3 = var4;
            if (var7 != null) {
               var3 = var4 - (var7.getMeasuredWidth() + AndroidUtilities.dp(8.0F));
            }

            var8.layout(var3, this.getPaddingTop(), var8.getMeasuredWidth() + var3, this.getPaddingTop() + var8.getMeasuredHeight());
         } else {
            var8.layout(this.getPaddingLeft(), this.getPaddingTop(), this.getPaddingLeft() + var8.getMeasuredWidth(), this.getPaddingTop() + var8.getMeasuredHeight());
         }
      }

   }
}
