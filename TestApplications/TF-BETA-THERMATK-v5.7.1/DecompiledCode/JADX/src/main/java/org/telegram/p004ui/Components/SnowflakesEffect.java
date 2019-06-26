package org.telegram.p004ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.SnowflakesEffect */
public class SnowflakesEffect {
    final float angleDiff = 1.0471976f;
    private ArrayList<Particle> freeParticles = new ArrayList();
    private long lastAnimationTime;
    private Paint particlePaint = new Paint(1);
    private Paint particleThinPaint;
    private ArrayList<Particle> particles = new ArrayList();

    /* renamed from: org.telegram.ui.Components.SnowflakesEffect$Particle */
    private class Particle {
        float alpha;
        float currentTime;
        float lifeTime;
        float scale;
        int type;
        float velocity;
        /* renamed from: vx */
        float f604vx;
        /* renamed from: vy */
        float f605vy;
        /* renamed from: x */
        float f606x;
        /* renamed from: y */
        float f607y;

        private Particle() {
        }

        public void draw(Canvas canvas) {
            if (this.type != 0) {
                SnowflakesEffect.this.particleThinPaint.setAlpha((int) (this.alpha * 255.0f));
                float f = -1.5707964f;
                float dpf2 = (AndroidUtilities.dpf2(2.0f) * 2.0f) * this.scale;
                float f2 = ((-AndroidUtilities.dpf2(0.57f)) * 2.0f) * this.scale;
                float dpf22 = (AndroidUtilities.dpf2(1.55f) * 2.0f) * this.scale;
                int i = 0;
                while (i < 6) {
                    double d = (double) f;
                    float cos = ((float) Math.cos(d)) * dpf2;
                    float sin = ((float) Math.sin(d)) * dpf2;
                    float f3 = cos * 0.66f;
                    float f4 = 0.66f * sin;
                    float f5 = this.f606x;
                    float f6 = this.f607y;
                    canvas.drawLine(f5, f6, f5 + cos, f6 + sin, SnowflakesEffect.this.particleThinPaint);
                    Double.isNaN(d);
                    d = (double) ((float) (d - 1.5707963267948966d));
                    double cos2 = Math.cos(d);
                    double d2 = (double) f2;
                    Double.isNaN(d2);
                    cos2 *= d2;
                    double sin2 = Math.sin(d);
                    float f7 = dpf2;
                    float f8 = f2;
                    double d3 = (double) dpf22;
                    Double.isNaN(d3);
                    cos = (float) (cos2 - (sin2 * d3));
                    sin2 = Math.sin(d);
                    Double.isNaN(d2);
                    sin2 *= d2;
                    double cos3 = Math.cos(d);
                    Double.isNaN(d3);
                    sin = (float) (sin2 + (cos3 * d3));
                    f6 = this.f606x;
                    float f9 = f6 + f3;
                    float f10 = this.f607y;
                    Canvas canvas2 = canvas;
                    canvas2.drawLine(f9, f10 + f4, f6 + cos, f10 + sin, SnowflakesEffect.this.particleThinPaint);
                    cos2 = -Math.cos(d);
                    Double.isNaN(d2);
                    cos2 *= d2;
                    sin2 = Math.sin(d);
                    Double.isNaN(d3);
                    cos = (float) (cos2 - (sin2 * d3));
                    sin2 = -Math.sin(d);
                    Double.isNaN(d2);
                    sin2 *= d2;
                    d = Math.cos(d);
                    Double.isNaN(d3);
                    dpf2 = (float) (sin2 + (d * d3));
                    f2 = this.f606x;
                    f9 = f2 + f3;
                    float f11 = this.f607y;
                    canvas2.drawLine(f9, f11 + f4, f2 + cos, f11 + dpf2, SnowflakesEffect.this.particleThinPaint);
                    f += 1.0471976f;
                    i++;
                    dpf2 = f7;
                    f2 = f8;
                }
                return;
            }
            SnowflakesEffect.this.particlePaint.setAlpha((int) (this.alpha * 255.0f));
            canvas.drawPoint(this.f606x, this.f607y, SnowflakesEffect.this.particlePaint);
        }
    }

    public SnowflakesEffect() {
        this.particlePaint.setStrokeWidth((float) AndroidUtilities.m26dp(1.5f));
        Paint paint = this.particlePaint;
        String str = Theme.key_actionBarDefaultTitle;
        paint.setColor(Theme.getColor(str) & -1644826);
        this.particlePaint.setStrokeCap(Cap.ROUND);
        this.particlePaint.setStyle(Style.STROKE);
        this.particleThinPaint = new Paint(1);
        this.particleThinPaint.setStrokeWidth((float) AndroidUtilities.m26dp(0.5f));
        this.particleThinPaint.setColor(Theme.getColor(str) & -1644826);
        this.particleThinPaint.setStrokeCap(Cap.ROUND);
        this.particleThinPaint.setStyle(Style.STROKE);
        for (int i = 0; i < 20; i++) {
            this.freeParticles.add(new Particle());
        }
    }

    private void updateParticles(long j) {
        int size = this.particles.size();
        int i = 0;
        while (i < size) {
            Particle particle = (Particle) this.particles.get(i);
            float f = particle.currentTime;
            float f2 = particle.lifeTime;
            if (f >= f2) {
                if (this.freeParticles.size() < 40) {
                    this.freeParticles.add(particle);
                }
                this.particles.remove(i);
                i--;
                size--;
            } else {
                if (f < 200.0f) {
                    particle.alpha = AndroidUtilities.accelerateInterpolator.getInterpolation(f / 200.0f);
                } else {
                    particle.alpha = 1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation((f - 200.0f) / (f2 - 200.0f));
                }
                f = particle.f606x;
                f2 = particle.f604vx;
                float f3 = particle.velocity;
                float f4 = (float) j;
                particle.f606x = f + (((f2 * f3) * f4) / 500.0f);
                particle.f607y += ((particle.f605vy * f3) * f4) / 500.0f;
                particle.currentTime += f4;
            }
            i++;
        }
    }

    public void onDraw(View view, Canvas canvas) {
        if (view != null && canvas != null) {
            int size = this.particles.size();
            for (int i = 0; i < size; i++) {
                ((Particle) this.particles.get(i)).draw(canvas);
            }
            if (Utilities.random.nextFloat() > 0.7f && this.particles.size() < 100) {
                Particle particle;
                int i2 = VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
                float nextFloat = Utilities.random.nextFloat() * ((float) view.getMeasuredWidth());
                float nextFloat2 = ((float) i2) + (Utilities.random.nextFloat() * ((float) ((view.getMeasuredHeight() - AndroidUtilities.m26dp(20.0f)) - i2)));
                double nextInt = (double) ((Utilities.random.nextInt(40) - 20) + 90);
                Double.isNaN(nextInt);
                nextInt *= 0.017453292519943295d;
                float cos = (float) Math.cos(nextInt);
                float sin = (float) Math.sin(nextInt);
                if (this.freeParticles.isEmpty()) {
                    particle = new Particle();
                } else {
                    particle = (Particle) this.freeParticles.get(0);
                    this.freeParticles.remove(0);
                }
                particle.f606x = nextFloat;
                particle.f607y = nextFloat2;
                particle.f604vx = cos;
                particle.f605vy = sin;
                particle.alpha = 0.0f;
                particle.currentTime = 0.0f;
                particle.scale = Utilities.random.nextFloat() * 1.2f;
                particle.type = Utilities.random.nextInt(2);
                particle.lifeTime = (float) (Utilities.random.nextInt(100) + 2000);
                particle.velocity = (Utilities.random.nextFloat() * 4.0f) + 20.0f;
                this.particles.add(particle);
            }
            long currentTimeMillis = System.currentTimeMillis();
            updateParticles(Math.min(17, currentTimeMillis - this.lastAnimationTime));
            this.lastAnimationTime = currentTimeMillis;
            view.invalidate();
        }
    }
}