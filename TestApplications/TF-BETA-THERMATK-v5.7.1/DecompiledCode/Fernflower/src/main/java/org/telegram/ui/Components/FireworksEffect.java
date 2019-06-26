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

public class FireworksEffect {
   final float angleDiff = 1.0471976F;
   private ArrayList freeParticles = new ArrayList();
   private long lastAnimationTime;
   private Paint particlePaint = new Paint(1);
   private ArrayList particles = new ArrayList();

   public FireworksEffect() {
      this.particlePaint.setStrokeWidth((float)AndroidUtilities.dp(1.5F));
      this.particlePaint.setColor(Theme.getColor("actionBarDefaultTitle") & -1644826);
      this.particlePaint.setStrokeCap(Cap.ROUND);
      this.particlePaint.setStyle(Style.STROKE);

      for(int var1 = 0; var1 < 20; ++var1) {
         this.freeParticles.add(new FireworksEffect.Particle());
      }

   }

   private void updateParticles(long var1) {
      int var3 = this.particles.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         FireworksEffect.Particle var5 = (FireworksEffect.Particle)this.particles.get(var4);
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
            var5.alpha = 1.0F - AndroidUtilities.decelerateInterpolator.getInterpolation(var6 / var7);
            float var8 = var5.x;
            float var9 = var5.vx;
            var7 = var5.velocity;
            var6 = (float)var1;
            var5.x = var8 + var9 * var7 * var6 / 500.0F;
            var8 = var5.y;
            var9 = var5.vy;
            var5.y = var8 + var7 * var9 * var6 / 500.0F;
            var5.vy = var9 + var6 / 100.0F;
            var5.currentTime += var6;
         }
      }

   }

   public void onDraw(View var1, Canvas var2) {
      if (var1 != null && var2 != null) {
         int var3 = this.particles.size();

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            ((FireworksEffect.Particle)this.particles.get(var4)).draw(var2);
         }

         if (Utilities.random.nextBoolean() && this.particles.size() + 8 < 150) {
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
            var4 = Utilities.random.nextInt(4);
            if (var4 != 0) {
               if (var4 != 1) {
                  if (var4 != 2) {
                     if (var4 != 3) {
                        var4 = -5752;
                     } else {
                        var4 = -15088582;
                     }
                  } else {
                     var4 = -207021;
                  }
               } else {
                  var4 = -843755;
               }
            } else {
               var4 = -13357350;
            }

            for(var3 = 0; var3 < 8; ++var3) {
               double var10 = (double)(Utilities.random.nextInt(270) - 225);
               Double.isNaN(var10);
               var10 *= 0.017453292519943295D;
               float var12 = (float)Math.cos(var10);
               float var13 = (float)Math.sin(var10);
               FireworksEffect.Particle var16;
               if (!this.freeParticles.isEmpty()) {
                  var16 = (FireworksEffect.Particle)this.freeParticles.get(0);
                  this.freeParticles.remove(0);
               } else {
                  var16 = new FireworksEffect.Particle();
               }

               var16.x = var5 * var6;
               var16.y = var7 + var8 * var9;
               var16.vx = var12 * 1.5F;
               var16.vy = var13;
               var16.color = var4;
               var16.alpha = 1.0F;
               var16.currentTime = 0.0F;
               var16.scale = Math.max(1.0F, Utilities.random.nextFloat() * 1.5F);
               var16.type = 0;
               var16.lifeTime = (float)(Utilities.random.nextInt(1000) + 1000);
               var16.velocity = Utilities.random.nextFloat() * 4.0F + 20.0F;
               this.particles.add(var16);
            }
         }

         long var14 = System.currentTimeMillis();
         this.updateParticles(Math.min(17L, var14 - this.lastAnimationTime));
         this.lastAnimationTime = var14;
         var1.invalidate();
      }

   }

   private class Particle {
      float alpha;
      int color;
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
         if (this.type == 0) {
            FireworksEffect.this.particlePaint.setColor(this.color);
            FireworksEffect.this.particlePaint.setStrokeWidth((float)AndroidUtilities.dp(1.5F) * this.scale);
            FireworksEffect.this.particlePaint.setAlpha((int)(this.alpha * 255.0F));
            var1.drawPoint(this.x, this.y, FireworksEffect.this.particlePaint);
         }

      }
   }
}
