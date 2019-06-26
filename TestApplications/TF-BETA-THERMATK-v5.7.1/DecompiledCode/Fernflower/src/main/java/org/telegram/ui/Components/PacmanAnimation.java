package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;

public class PacmanAnimation {
   private boolean currentGhostWalk;
   private Paint edgePaint = new Paint(1);
   private Runnable finishRunnable;
   private Path ghostPath;
   private float ghostProgress;
   private boolean ghostWalk;
   private long lastUpdateTime = 0L;
   private Paint paint = new Paint(1);
   private View parentView;
   private float progress;
   private RectF rect = new RectF();
   private float translationProgress;

   public PacmanAnimation(View var1) {
      this.edgePaint.setStyle(Style.STROKE);
      this.edgePaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.parentView = var1;
   }

   private void drawGhost(Canvas var1, int var2) {
      if (this.ghostPath == null || this.ghostWalk != this.currentGhostWalk) {
         if (this.ghostPath == null) {
            this.ghostPath = new Path();
         }

         this.ghostPath.reset();
         this.currentGhostWalk = this.ghostWalk;
         if (this.currentGhostWalk) {
            this.ghostPath.moveTo(0.0F, (float)AndroidUtilities.dp(50.0F));
            this.ghostPath.lineTo(0.0F, (float)AndroidUtilities.dp(24.0F));
            this.rect.set(0.0F, 0.0F, (float)AndroidUtilities.dp(42.0F), (float)AndroidUtilities.dp(24.0F));
            this.ghostPath.arcTo(this.rect, 180.0F, 180.0F, false);
            this.ghostPath.lineTo((float)AndroidUtilities.dp(42.0F), (float)AndroidUtilities.dp(50.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(35.0F), (float)AndroidUtilities.dp(43.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(28.0F), (float)AndroidUtilities.dp(50.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(21.0F), (float)AndroidUtilities.dp(43.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(14.0F), (float)AndroidUtilities.dp(50.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(7.0F), (float)AndroidUtilities.dp(43.0F));
         } else {
            this.ghostPath.moveTo(0.0F, (float)AndroidUtilities.dp(43.0F));
            this.ghostPath.lineTo(0.0F, (float)AndroidUtilities.dp(24.0F));
            this.rect.set(0.0F, 0.0F, (float)AndroidUtilities.dp(42.0F), (float)AndroidUtilities.dp(24.0F));
            this.ghostPath.arcTo(this.rect, 180.0F, 180.0F, false);
            this.ghostPath.lineTo((float)AndroidUtilities.dp(42.0F), (float)AndroidUtilities.dp(43.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(35.0F), (float)AndroidUtilities.dp(50.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(28.0F), (float)AndroidUtilities.dp(43.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(21.0F), (float)AndroidUtilities.dp(50.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(14.0F), (float)AndroidUtilities.dp(43.0F));
            this.ghostPath.lineTo((float)AndroidUtilities.dp(7.0F), (float)AndroidUtilities.dp(50.0F));
         }

         this.ghostPath.close();
      }

      var1.drawPath(this.ghostPath, this.edgePaint);
      if (var2 == 0) {
         this.paint.setColor(-90112);
      } else if (var2 == 1) {
         this.paint.setColor(-85326);
      } else {
         this.paint.setColor(-16720161);
      }

      var1.drawPath(this.ghostPath, this.paint);
      this.paint.setColor(-1);
      this.rect.set((float)AndroidUtilities.dp(8.0F), (float)AndroidUtilities.dp(14.0F), (float)AndroidUtilities.dp(20.0F), (float)AndroidUtilities.dp(28.0F));
      var1.drawOval(this.rect, this.paint);
      this.rect.set((float)AndroidUtilities.dp(24.0F), (float)AndroidUtilities.dp(14.0F), (float)AndroidUtilities.dp(36.0F), (float)AndroidUtilities.dp(28.0F));
      var1.drawOval(this.rect, this.paint);
      this.paint.setColor(-16777216);
      this.rect.set((float)AndroidUtilities.dp(14.0F), (float)AndroidUtilities.dp(18.0F), (float)AndroidUtilities.dp(19.0F), (float)AndroidUtilities.dp(24.0F));
      var1.drawOval(this.rect, this.paint);
      this.rect.set((float)AndroidUtilities.dp(30.0F), (float)AndroidUtilities.dp(18.0F), (float)AndroidUtilities.dp(35.0F), (float)AndroidUtilities.dp(24.0F));
      var1.drawOval(this.rect, this.paint);
   }

   private void update() {
      long var1 = System.currentTimeMillis();
      long var3 = var1 - this.lastUpdateTime;
      this.lastUpdateTime = var1;
      var1 = 17L;
      if (var3 > 17L) {
         var3 = var1;
      }

      if (this.progress >= 1.0F) {
         this.progress = 0.0F;
      }

      float var5 = this.progress;
      float var6 = (float)var3;
      this.progress = var5 + var6 / 400.0F;
      if (this.progress > 1.0F) {
         this.progress = 1.0F;
      }

      this.translationProgress += var6 / 2000.0F;
      if (this.translationProgress > 1.0F) {
         this.translationProgress = 1.0F;
      }

      this.ghostProgress += var6 / 200.0F;
      if (this.ghostProgress >= 1.0F) {
         this.ghostWalk ^= true;
         this.ghostProgress = 0.0F;
      }

      this.parentView.invalidate();
   }

   public void draw(Canvas var1, int var2) {
      int var3 = AndroidUtilities.dp(110.0F);
      float var4;
      if (SharedConfig.useThreeLinesLayout) {
         var4 = 78.0F;
      } else {
         var4 = 72.0F;
      }

      int var5 = AndroidUtilities.dp(var4);
      int var6 = AndroidUtilities.dp(62.0F) * 3 + var3;
      var4 = (float)(this.parentView.getMeasuredWidth() + var6) * this.translationProgress - (float)var6;
      int var7 = var3 / 2;
      var6 = var2 - var7;
      this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
      var5 /= 2;
      float var8 = (float)(var2 - var5);
      float var9 = var4 + (float)var7;
      var1.drawRect(0.0F, var8, var9, (float)(var2 + var5 + 1), this.paint);
      this.paint.setColor(-69120);
      RectF var10 = this.rect;
      float var11 = (float)var6;
      var8 = var4 + (float)var3;
      var10.set(var4, var11, var8, (float)(var6 + var3));
      var4 = this.progress;
      if (var4 < 0.5F) {
         var4 = (1.0F - var4 / 0.5F) * 35.0F;
      } else {
         var4 = (var4 - 0.5F) * 35.0F / 0.5F;
      }

      var5 = (int)var4;
      var10 = this.rect;
      var4 = (float)var5;
      var11 = (float)(360 - var5 * 2);
      var1.drawArc(var10, var4, var11, true, this.edgePaint);
      var1.drawArc(this.rect, var4, var11, true, this.paint);
      this.paint.setColor(-16777216);
      var1.drawCircle(var9 - (float)AndroidUtilities.dp(8.0F), (float)(var6 + var3 / 4), (float)AndroidUtilities.dp(8.0F), this.paint);
      var1.save();
      var1.translate(var8 + (float)AndroidUtilities.dp(20.0F), (float)(var2 - AndroidUtilities.dp(25.0F)));

      for(var2 = 0; var2 < 3; ++var2) {
         this.drawGhost(var1, var2);
         var1.translate((float)AndroidUtilities.dp(62.0F), 0.0F);
      }

      var1.restore();
      if (this.translationProgress >= 1.0F) {
         this.finishRunnable.run();
      }

      this.update();
   }

   public void setFinishRunnable(Runnable var1) {
      this.finishRunnable = var1;
   }

   public void start() {
      this.translationProgress = 0.0F;
      this.progress = 0.0F;
      this.lastUpdateTime = System.currentTimeMillis();
      this.parentView.invalidate();
   }
}
