package org.telegram.ui.Components;

import android.content.Context;
import android.text.Layout;
import android.widget.TextView;

public class CorrectlyMeasuringTextView extends TextView {
   public CorrectlyMeasuringTextView(Context var1) {
      super(var1);
   }

   public void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);

      boolean var10001;
      Layout var3;
      try {
         var3 = this.getLayout();
         if (var3.getLineCount() <= 1) {
            return;
         }
      } catch (Exception var7) {
         var10001 = false;
         return;
      }

      var1 = 0;

      try {
         var2 = var3.getLineCount() - 1;
      } catch (Exception var6) {
         var10001 = false;
         return;
      }

      for(; var2 >= 0; --var2) {
         try {
            var1 = Math.max(var1, Math.round(var3.getPaint().measureText(this.getText(), var3.getLineStart(var2), var3.getLineEnd(var2))));
         } catch (Exception var5) {
            var10001 = false;
            return;
         }
      }

      try {
         super.onMeasure(Math.min(var1 + this.getPaddingLeft() + this.getPaddingRight(), this.getMeasuredWidth()) | 1073741824, 1073741824 | this.getMeasuredHeight());
      } catch (Exception var4) {
         var10001 = false;
      }

   }
}
