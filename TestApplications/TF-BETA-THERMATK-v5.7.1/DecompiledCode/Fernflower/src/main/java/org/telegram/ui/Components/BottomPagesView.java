package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.viewpager.widget.ViewPager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class BottomPagesView extends View {
   private float animatedProgress;
   private String colorKey;
   private int currentPage;
   private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
   private int pagesCount;
   private Paint paint = new Paint(1);
   private float progress;
   private RectF rect = new RectF();
   private int scrollPosition;
   private String selectedColorKey;
   private ViewPager viewPager;

   public BottomPagesView(Context var1, ViewPager var2, int var3) {
      super(var1);
      this.viewPager = var2;
      this.pagesCount = var3;
   }

   protected void onDraw(Canvas var1) {
      AndroidUtilities.dp(5.0F);
      String var2 = this.colorKey;
      if (var2 != null) {
         this.paint.setColor(Theme.getColor(var2) & 16777215 | -1275068416);
      } else {
         this.paint.setColor(-4473925);
      }

      this.currentPage = this.viewPager.getCurrentItem();

      int var3;
      for(var3 = 0; var3 < this.pagesCount; ++var3) {
         if (var3 != this.currentPage) {
            int var4 = AndroidUtilities.dp(11.0F) * var3;
            this.rect.set((float)var4, 0.0F, (float)(var4 + AndroidUtilities.dp(5.0F)), (float)AndroidUtilities.dp(5.0F));
            var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.5F), (float)AndroidUtilities.dp(2.5F), this.paint);
         }
      }

      var2 = this.selectedColorKey;
      if (var2 != null) {
         this.paint.setColor(Theme.getColor(var2));
      } else {
         this.paint.setColor(-13851168);
      }

      var3 = this.currentPage * AndroidUtilities.dp(11.0F);
      if (this.progress != 0.0F) {
         if (this.scrollPosition >= this.currentPage) {
            this.rect.set((float)var3, 0.0F, (float)(var3 + AndroidUtilities.dp(5.0F)) + (float)AndroidUtilities.dp(11.0F) * this.progress, (float)AndroidUtilities.dp(5.0F));
         } else {
            this.rect.set((float)var3 - (float)AndroidUtilities.dp(11.0F) * (1.0F - this.progress), 0.0F, (float)(var3 + AndroidUtilities.dp(5.0F)), (float)AndroidUtilities.dp(5.0F));
         }
      } else {
         this.rect.set((float)var3, 0.0F, (float)(var3 + AndroidUtilities.dp(5.0F)), (float)AndroidUtilities.dp(5.0F));
      }

      var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.5F), (float)AndroidUtilities.dp(2.5F), this.paint);
   }

   public void setColor(String var1, String var2) {
      this.colorKey = var1;
      this.selectedColorKey = var2;
   }

   public void setCurrentPage(int var1) {
      this.currentPage = var1;
      this.invalidate();
   }

   public void setPageOffset(int var1, float var2) {
      this.progress = var2;
      this.scrollPosition = var1;
      this.invalidate();
   }
}
