package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCreateSectionCell;

public class GroupCreateDividerItemDecoration extends RecyclerView.ItemDecoration {
   private boolean searching;
   private boolean single;
   private int skipRows;

   public void getItemOffsets(android.graphics.Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
      super.getItemOffsets(var1, var2, var3, var4);
      var1.top = 1;
   }

   public void onDraw(Canvas var1, RecyclerView var2, RecyclerView.State var3) {
      int var4 = var2.getWidth();
      int var5 = var2.getChildCount() - (this.single ^ 1);

      for(int var6 = 0; var6 < var5; ++var6) {
         View var7 = var2.getChildAt(var6);
         View var11;
         if (var6 < var5 - 1) {
            var11 = var2.getChildAt(var6 + 1);
         } else {
            var11 = null;
         }

         if (var2.getChildAdapterPosition(var7) >= this.skipRows && !(var7 instanceof GroupCreateSectionCell) && !(var11 instanceof GroupCreateSectionCell)) {
            int var8 = var7.getBottom();
            float var9;
            if (LocaleController.isRTL) {
               var9 = 0.0F;
            } else {
               var9 = (float)AndroidUtilities.dp(72.0F);
            }

            float var10 = (float)var8;
            if (LocaleController.isRTL) {
               var8 = AndroidUtilities.dp(72.0F);
            } else {
               var8 = 0;
            }

            var1.drawLine(var9, var10, (float)(var4 - var8), var10, Theme.dividerPaint);
         }
      }

   }

   public void setSearching(boolean var1) {
      this.searching = var1;
   }

   public void setSingle(boolean var1) {
      this.single = var1;
   }

   public void setSkipRows(int var1) {
      this.skipRows = var1;
   }
}
