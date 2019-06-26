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

public class FireworksEffect
{
    final float angleDiff;
    private ArrayList<Particle> freeParticles;
    private long lastAnimationTime;
    private Paint particlePaint;
    private ArrayList<Particle> particles;
    
    public FireworksEffect() {
        this.angleDiff = 1.0471976f;
        this.particles = new ArrayList<Particle>();
        this.freeParticles = new ArrayList<Particle>();
        (this.particlePaint = new Paint(1)).setStrokeWidth((float)AndroidUtilities.dp(1.5f));
        this.particlePaint.setColor(Theme.getColor("actionBarDefaultTitle") & 0xFFE6E6E6);
        this.particlePaint.setStrokeCap(Paint$Cap.ROUND);
        this.particlePaint.setStyle(Paint$Style.STROKE);
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
                e.alpha = 1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation(currentTime / lifeTime);
                final float x = e.x;
                final float vx = e.vx;
                final float velocity = e.velocity;
                final float n2 = (float)n;
                e.x = x + vx * velocity * n2 / 500.0f;
                final float y = e.y;
                final float vy = e.vy;
                e.y = y + velocity * vy * n2 / 500.0f;
                e.vy = vy + n2 / 100.0f;
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
                if (Utilities.random.nextBoolean() && this.particles.size() + 8 < 150) {
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
                    final int nextInt = Utilities.random.nextInt(4);
                    int color;
                    if (nextInt != 0) {
                        if (nextInt != 1) {
                            if (nextInt != 2) {
                                if (nextInt != 3) {
                                    color = -5752;
                                }
                                else {
                                    color = -15088582;
                                }
                            }
                            else {
                                color = -207021;
                            }
                        }
                        else {
                            color = -843755;
                        }
                    }
                    else {
                        color = -13357350;
                    }
                    for (int j = 0; j < 8; ++j) {
                        final double v = Utilities.random.nextInt(270) - 225;
                        Double.isNaN(v);
                        final double n4 = v * 0.017453292519943295;
                        final float n5 = (float)Math.cos(n4);
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
                        e.vx = n5 * 1.5f;
                        e.vy = vy;
                        e.color = color;
                        e.alpha = 1.0f;
                        e.currentTime = 0.0f;
                        e.scale = Math.max(1.0f, Utilities.random.nextFloat() * 1.5f);
                        e.type = 0;
                        e.lifeTime = (float)(Utilities.random.nextInt(1000) + 1000);
                        e.velocity = Utilities.random.nextFloat() * 4.0f + 20.0f;
                        this.particles.add(e);
                    }
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
        
        public void draw(final Canvas canvas) {
            if (this.type == 0) {
                FireworksEffect.this.particlePaint.setColor(this.color);
                FireworksEffect.this.particlePaint.setStrokeWidth(AndroidUtilities.dp(1.5f) * this.scale);
                FireworksEffect.this.particlePaint.setAlpha((int)(this.alpha * 255.0f));
                canvas.drawPoint(this.x, this.y, FireworksEffect.this.particlePaint);
            }
        }
    }
}
