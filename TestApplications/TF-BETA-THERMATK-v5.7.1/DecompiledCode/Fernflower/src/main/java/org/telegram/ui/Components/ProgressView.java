package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.telegram.messenger.AndroidUtilities;

public class ProgressView {
   public float currentProgress = 0.0F;
   public int height;
   private Paint innerPaint = new Paint();
   private Paint outerPaint = new Paint();
   public float progressHeight = (float)AndroidUtilities.dp(2.0F);
   public int width;

   public void draw(Canvas var1) {
      int var2 = this.height;
      float var3 = (float)(var2 / 2);
      float var4 = this.progressHeight;
      var1.drawRect(0.0F, var3 - var4 / 2.0F, (float)this.width, (float)(var2 / 2) + var4 / 2.0F, this.innerPaint);
      var2 = this.height;
      var4 = (float)(var2 / 2);
      var3 = this.progressHeight;
      var1.drawRect(0.0F, var4 - var3 / 2.0F, (float)this.width * this.currentProgress, (float)(var2 / 2) + var3 / 2.0F, this.outerPaint);
   }

   public void setProgress(float var1) {
      this.currentProgress = var1;
      var1 = this.currentProgress;
      if (var1 < 0.0F) {
         this.currentProgress = 0.0F;
      } else if (var1 > 1.0F) {
         this.currentProgress = 1.0F;
      }

   }

   public void setProgressColors(int var1, int var2) {
      this.innerPaint.setColor(var1);
      this.outerPaint.setColor(var2);
   }
}
