// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.os.Build$VERSION;
import org.telegram.messenger.Utilities;
import android.graphics.Canvas;
import android.view.View;
import android.graphics.Paint$Style;
import android.graphics.Paint$Cap;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint;
import java.util.ArrayList;

public class SnowflakesEffect
{
    final float angleDiff;
    private ArrayList<Particle> freeParticles;
    private long lastAnimationTime;
    private Paint particlePaint;
    private Paint particleThinPaint;
    private ArrayList<Particle> particles;
    
    public SnowflakesEffect() {
        this.angleDiff = 1.0471976f;
        this.particles = new ArrayList<Particle>();
        this.freeParticles = new ArrayList<Particle>();
        (this.particlePaint = new Paint(1)).setStrokeWidth((float)AndroidUtilities.dp(1.5f));
        this.particlePaint.setColor(Theme.getColor("actionBarDefaultTitle") & 0xFFE6E6E6);
        this.particlePaint.setStrokeCap(Paint$Cap.ROUND);
        this.particlePaint.setStyle(Paint$Style.STROKE);
        (this.particleThinPaint = new Paint(1)).setStrokeWidth((float)AndroidUtilities.dp(0.5f));
        this.particleThinPaint.setColor(Theme.getColor("actionBarDefaultTitle") & 0xFFE6E6E6);
        this.particleThinPaint.setStrokeCap(Paint$Cap.ROUND);
        this.particleThinPaint.setStyle(Paint$Style.STROKE);
        for (int i = 0; i < 20; ++i) {
            this.freeParticles.add(new Particle());
        }
    }
    
    private void updateParticles(final long n) {
        for (int size = this.particles.size(), i = 0; i < size; ++i) {
            final Particle e = this.particles.get(i);
            final float currentTime = e.currentTime;
            final float lifeTime = e.lifeTime;
            if (currentTime >= lifeTime) {
                if (this.freeParticles.size() < 40) {
                    this.freeParticles.add(e);
                }
                this.particles.remove(i);
                --i;
                --size;
            }
            else {
                if (currentTime < 200.0f) {
                    e.alpha = AndroidUtilities.accelerateInterpolator.getInterpolation(currentTime / 200.0f);
                }
                else {
                    e.alpha = 1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation((currentTime - 200.0f) / (lifeTime - 200.0f));
                }
                final float x = e.x;
                final float vx = e.vx;
                final float velocity = e.velocity;
                final float n2 = (float)n;
                e.x = x + vx * velocity * n2 / 500.0f;
                e.y += e.vy * velocity * n2 / 500.0f;
                e.currentTime += n2;
            }
        }
    }
    
    public void onDraw(final View view, final Canvas canvas) {
        if (view != null) {
            if (canvas != null) {
                for (int size = this.particles.size(), i = 0; i < size; ++i) {
                    this.particles.get(i).draw(canvas);
                }
                if (Utilities.random.nextFloat() > 0.7f && this.particles.size() < 100) {
                    int statusBarHeight;
                    if (Build$VERSION.SDK_INT >= 21) {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        statusBarHeight = 0;
                    }
                    final float nextFloat = Utilities.random.nextFloat();
                    final float n = (float)view.getMeasuredWidth();
                    final float n2 = (float)statusBarHeight;
                    final float nextFloat2 = Utilities.random.nextFloat();
                    final float n3 = (float)(view.getMeasuredHeight() - AndroidUtilities.dp(20.0f) - statusBarHeight);
                    final double v = Utilities.random.nextInt(40) - 20 + 90;
                    Double.isNaN(v);
                    final double n4 = v * 0.017453292519943295;
                    final float vx = (float)Math.cos(n4);
                    final float vy = (float)Math.sin(n4);
                    Particle e;
                    if (!this.freeParticles.isEmpty()) {
                        e = this.freeParticles.get(0);
                        this.freeParticles.remove(0);
                    }
                    else {
                        e = new Particle();
                    }
                    e.x = nextFloat * n;
                    e.y = n2 + nextFloat2 * n3;
                    e.vx = vx;
                    e.vy = vy;
                    e.alpha = 0.0f;
                    e.currentTime = 0.0f;
                    e.scale = Utilities.random.nextFloat() * 1.2f;
                    e.type = Utilities.random.nextInt(2);
                    e.lifeTime = (float)(Utilities.random.nextInt(100) + 2000);
                    e.velocity = Utilities.random.nextFloat() * 4.0f + 20.0f;
                    this.particles.add(e);
                }
                final long currentTimeMillis = System.currentTimeMillis();
                this.updateParticles(Math.min(17L, currentTimeMillis - this.lastAnimationTime));
                this.lastAnimationTime = currentTimeMillis;
                view.invalidate();
            }
        }
    }
    
    private class Particle
    {
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
        
        public void draw(final Canvas canvas) {
            if (this.type != 0) {
                SnowflakesEffect.this.particleThinPaint.setAlpha((int)(this.alpha * 255.0f));
                float n = -1.5707964f;
                final float n2 = AndroidUtilities.dpf2(2.0f) * 2.0f * this.scale;
                final float n3 = -AndroidUtilities.dpf2(0.57f) * 2.0f * this.scale;
                final float dpf2 = AndroidUtilities.dpf2(1.55f);
                final float scale = this.scale;
                for (int i = 0; i < 6; ++i) {
                    final double v = n;
                    final float n4 = (float)Math.cos(v) * n2;
                    final float n5 = (float)Math.sin(v) * n2;
                    final float n6 = n4 * 0.66f;
                    final float n7 = 0.66f * n5;
                    final float x = this.x;
                    final float y = this.y;
                    canvas.drawLine(x, y, x + n4, y + n5, SnowflakesEffect.this.particleThinPaint);
                    Double.isNaN(v);
                    final double n8 = (float)(v - 1.5707963267948966);
                    final double cos = Math.cos(n8);
                    final double n9 = n3;
                    Double.isNaN(n9);
                    final double sin = Math.sin(n8);
                    final double n10 = dpf2 * 2.0f * scale;
                    Double.isNaN(n10);
                    final float n11 = (float)(cos * n9 - sin * n10);
                    final double sin2 = Math.sin(n8);
                    Double.isNaN(n9);
                    final double cos2 = Math.cos(n8);
                    Double.isNaN(n10);
                    final float n12 = (float)(sin2 * n9 + cos2 * n10);
                    final float x2 = this.x;
                    final float y2 = this.y;
                    canvas.drawLine(x2 + n6, y2 + n7, x2 + n11, y2 + n12, SnowflakesEffect.this.particleThinPaint);
                    final double n13 = -Math.cos(n8);
                    Double.isNaN(n9);
                    final double sin3 = Math.sin(n8);
                    Double.isNaN(n10);
                    final float n14 = (float)(n13 * n9 - sin3 * n10);
                    final double n15 = -Math.sin(n8);
                    Double.isNaN(n9);
                    final double cos3 = Math.cos(n8);
                    Double.isNaN(n10);
                    final float n16 = (float)(n15 * n9 + cos3 * n10);
                    final float x3 = this.x;
                    final float y3 = this.y;
                    canvas.drawLine(x3 + n6, y3 + n7, x3 + n14, y3 + n16, SnowflakesEffect.this.particleThinPaint);
                    n += 1.0471976f;
                }
            }
            else {
                SnowflakesEffect.this.particlePaint.setAlpha((int)(this.alpha * 255.0f));
                canvas.drawPoint(this.x, this.y, SnowflakesEffect.this.particlePaint);
            }
        }
    }
}
