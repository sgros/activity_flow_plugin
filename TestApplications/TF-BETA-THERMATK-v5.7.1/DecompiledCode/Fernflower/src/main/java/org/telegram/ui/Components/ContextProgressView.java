package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class ContextProgressView extends View {
   private RectF cicleRect = new RectF();
   private int currentColorType;
   private String innerKey;
   private Paint innerPaint = new Paint(1);
   private long lastUpdateTime;
   private String outerKey;
   private Paint outerPaint = new Paint(1);
   private int radOffset = 0;

   public ContextProgressView(Context var1, int var2) {
      super(var1);
      this.innerPaint.setStyle(Style.STROKE);
      this.innerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.outerPaint.setStyle(Style.STROKE);
      this.outerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.outerPaint.setStrokeCap(Cap.ROUND);
      if (var2 == 0) {
         this.innerKey = "contextProgressInner1";
         this.outerKey = "contextProgressOuter1";
      } else if (var2 == 1) {
         this.innerKey = "contextProgressInner2";
         this.outerKey = "contextProgressOuter2";
      } else if (var2 == 2) {
         this.innerKey = "contextProgressInner3";
         this.outerKey = "contextProgressOuter3";
      } else if (var2 == 3) {
         this.innerKey = "contextProgressInner4";
         this.outerKey = "contextProgressOuter4";
      }

      this.updateColors();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.lastUpdateTime = System.currentTimeMillis();
      this.invalidate();
   }

   protected void onDraw(Canvas var1) {
      if (this.getVisibility() == 0) {
         long var2 = System.currentTimeMillis();
         long var4 = this.lastUpdateTime;
         this.lastUpdateTime = var2;
         this.radOffset = (int)((float)this.radOffset + (float)((var2 - var4) * 360L) / 1000.0F);
         int var6 = this.getMeasuredWidth() / 2 - AndroidUtilities.dp(9.0F);
         int var7 = this.getMeasuredHeight() / 2 - AndroidUtilities.dp(9.0F);
         this.cicleRect.set((float)var6, (float)var7, (float)(var6 + AndroidUtilities.dp(18.0F)), (float)(var7 + AndroidUtilities.dp(18.0F)));
         var1.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(9.0F), this.innerPaint);
         var1.drawArc(this.cicleRect, (float)(this.radOffset - 90), 90.0F, false, this.outerPaint);
         this.invalidate();
      }
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      this.lastUpdateTime = System.currentTimeMillis();
      this.invalidate();
   }

   public void updateColors() {
      this.innerPaint.setColor(Theme.getColor(this.innerKey));
      this.outerPaint.setColor(Theme.getColor(this.outerKey));
      this.invalidate();
   }
}
