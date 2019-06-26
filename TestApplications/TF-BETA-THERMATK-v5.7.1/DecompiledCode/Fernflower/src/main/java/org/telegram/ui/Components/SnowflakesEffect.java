package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;

public class SnowflakesEffect {
   final float angleDiff = 1.0471976F;
   private ArrayList freeParticles = new ArrayList();
   private long lastAnimationTime;
   private Paint particlePaint = new Paint(1);
   private Paint particleThinPaint;
   private ArrayList particles = new ArrayList();

   public SnowflakesEffect() {
      this.particlePaint.setStrokeWidth((float)AndroidUtilities.dp(1.5F));
      this.particlePaint.setColor(Theme.getColor("actionBarDefaultTitle") & -1644826);
      this.particlePaint.setStrokeCap(Cap.ROUND);
      this.particlePaint.setStyle(Style.STROKE);
      this.particleThinPaint = new Paint(1);
      this.particleThinPaint.setStrokeWidth((float)AndroidUtilities.dp(0.5F));
      this.particleThinPaint.setColor(Theme.getColor("actionBarDefaultTitle") & -1644826);
      this.particleThinPaint.setStrokeCap(Cap.ROUND);
      this.particleThinPaint.setStyle(Style.STROKE);

      for(int var1 = 0; var1 < 20; ++var1) {
         this.freeParticles.add(new SnowflakesEffect.Particle());
      }

   }

   private void updateParticles(long var1) {
      int var3 = this.particles.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         SnowflakesEffect.Particle var5 = (SnowflakesEffect.Particle)this.particles.get(var4);
         float var6 = var5.currentTime;
         float var7 = var5.lifeTime;
         if (var6 >= var7) {
            if (this.freeParticles.size() < 40) {
               this.freeParticles.add(var5);
            }

            this.particles.remove(var4);
            --var4;
            --var3;
         } else {
            if (var6 < 200.0F) {
               var5.alpha = AndroidUtilities.accelerateInterpolator.getInterpolation(var6 / 200.0F);
            } else {
               var5.alpha = 1.0F - AndroidUtilities.decelerateInterpolator.getInterpolation((var6 - 200.0F) / (var7 - 200.0F));
            }

            float var8 = var5.x;
            float var9 = var5.vx;
            var7 = var5.velocity;
            var6 = (float)var1;
            var5.x = var8 + var9 * var7 * var6 / 500.0F;
            var5.y += var5.vy * var7 * var6 / 500.0F;
            var5.currentTime += var6;
         }
      }

   }

   public void onDraw(View var1, Canvas var2) {
      if (var1 != null && var2 != null) {
         int var3 = this.particles.size();

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            ((SnowflakesEffect.Particle)this.particles.get(var4)).draw(var2);
         }

         if (Utilities.random.nextFloat() > 0.7F && this.particles.size() < 100) {
            if (VERSION.SDK_INT >= 21) {
               var4 = AndroidUtilities.statusBarHeight;
            } else {
               var4 = 0;
            }

            float var5 = Utilities.random.nextFloat();
            float var6 = (float)var1.getMeasuredWidth();
            float var7 = (float)var4;
            float var8 = Utilities.random.nextFloat();
            float var9 = (float)(var1.getMeasuredHeight() - AndroidUtilities.dp(20.0F) - var4);
            double var10 = (double)(Utilities.random.nextInt(40) - 20 + 90);
            Double.isNaN(var10);
            var10 *= 0.017453292519943295D;
            float var12 = (float)Math.cos(var10);
            float var13 = (float)Math.sin(var10);
            SnowflakesEffect.Particle var16;
            if (!this.freeParticles.isEmpty()) {
               var16 = (SnowflakesEffect.Particle)this.freeParticles.get(0);
               this.freeParticles.remove(0);
            } else {
               var16 = new SnowflakesEffect.Particle();
            }

            var16.x = var5 * var6;
            var16.y = var7 + var8 * var9;
            var16.vx = var12;
            var16.vy = var13;
            var16.alpha = 0.0F;
            var16.currentTime = 0.0F;
            var16.scale = Utilities.random.nextFloat() * 1.2F;
            var16.type = Utilities.random.nextInt(2);
            var16.lifeTime = (float)(Utilities.random.nextInt(100) + 2000);
            var16.velocity = Utilities.random.nextFloat() * 4.0F + 20.0F;
            this.particles.add(var16);
         }

         long var14 = System.currentTimeMillis();
         this.updateParticles(Math.min(17L, var14 - this.lastAnimationTime));
         this.lastAnimationTime = var14;
         var1.invalidate();
      }

   }

   private class Particle {
      float alpha;
      float currentTime;
      float lifeTime;
      float scale;
      int type;
      float velocity;
      float vx;
      float vy;
      float x;
      float y;

      private Particle() {
      }

      // $FF: synthetic method
      Particle(Object var2) {
         this();
      }

      public void draw(Canvas var1) {
         if (this.type != 0) {
            SnowflakesEffect.this.particleThinPaint.setAlpha((int)(this.alpha * 255.0F));
            float var2 = -1.5707964F;
            float var3 = AndroidUtilities.dpf2(2.0F) * 2.0F * this.scale;
            float var4 = -AndroidUtilities.dpf2(0.57F) * 2.0F * this.scale;
            float var5 = AndroidUtilities.dpf2(1.55F);
            float var6 = this.scale;

            for(int var7 = 0; var7 < 6; ++var7) {
               double var8 = (double)var2;
               float var10 = (float)Math.cos(var8) * var3;
               float var11 = (float)Math.sin(var8) * var3;
               float var12 = var10 * 0.66F;
               float var13 = 0.66F * var11;
               float var14 = this.x;
               float var15 = this.y;
               var1.drawLine(var14, var15, var14 + var10, var15 + var11, SnowflakesEffect.this.particleThinPaint);
               Double.isNaN(var8);
               double var16 = (double)((float)(var8 - 1.5707963267948966D));
               double var18 = Math.cos(var16);
               var8 = (double)var4;
               Double.isNaN(var8);
               double var20 = Math.sin(var16);
               double var22 = (double)(var5 * 2.0F * var6);
               Double.isNaN(var22);
               var11 = (float)(var18 * var8 - var20 * var22);
               var20 = Math.sin(var16);
               Double.isNaN(var8);
               var18 = Math.cos(var16);
               Double.isNaN(var22);
               var15 = (float)(var20 * var8 + var18 * var22);
               var14 = this.x;
               var10 = this.y;
               var1.drawLine(var14 + var12, var10 + var13, var14 + var11, var10 + var15, SnowflakesEffect.this.particleThinPaint);
               var20 = -Math.cos(var16);
               Double.isNaN(var8);
               var18 = Math.sin(var16);
               Double.isNaN(var22);
               var14 = (float)(var20 * var8 - var18 * var22);
               var18 = -Math.sin(var16);
               Double.isNaN(var8);
               var16 = Math.cos(var16);
               Double.isNaN(var22);
               var10 = (float)(var18 * var8 + var16 * var22);
               var11 = this.x;
               var15 = this.y;
               var1.drawLine(var11 + var12, var15 + var13, var11 + var14, var15 + var10, SnowflakesEffect.this.particleThinPaint);
               ++var2;
            }
         } else {
            SnowflakesEffect.this.particlePaint.setAlpha((int)(this.alpha * 255.0F));
            var1.drawPoint(this.x, this.y, SnowflakesEffect.this.particlePaint);
         }

      }
   }
}
