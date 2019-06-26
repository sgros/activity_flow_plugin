// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.graphics.Paint$Cap;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class ProxyDrawable extends Drawable
{
    private RectF cicleRect;
    private boolean connected;
    private float connectedAnimationProgress;
    private int currentColorType;
    private Drawable emptyDrawable;
    private Drawable fullDrawable;
    private boolean isEnabled;
    private long lastUpdateTime;
    private Paint outerPaint;
    private int radOffset;
    
    public ProxyDrawable(final Context context) {
        this.outerPaint = new Paint(1);
        this.cicleRect = new RectF();
        this.radOffset = 0;
        this.emptyDrawable = context.getResources().getDrawable(2131165795);
        this.fullDrawable = context.getResources().getDrawable(2131165796);
        this.outerPaint.setStyle(Paint$Style.STROKE);
        this.outerPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.outerPaint.setStrokeCap(Paint$Cap.ROUND);
        this.lastUpdateTime = SystemClock.elapsedRealtime();
    }
    
    public void draw(final Canvas canvas) {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        final long n = elapsedRealtime - this.lastUpdateTime;
        this.lastUpdateTime = elapsedRealtime;
        if (!this.isEnabled) {
            this.emptyDrawable.setBounds(this.getBounds());
            this.emptyDrawable.draw(canvas);
        }
        else if (!this.connected || this.connectedAnimationProgress != 1.0f) {
            this.emptyDrawable.setBounds(this.getBounds());
            this.emptyDrawable.draw(canvas);
            this.outerPaint.setColor(Theme.getColor("contextProgressOuter2"));
            this.outerPaint.setAlpha((int)((1.0f - this.connectedAnimationProgress) * 255.0f));
            this.radOffset += (int)(360L * n / 1000.0f);
            final int width = this.getBounds().width();
            final int height = this.getBounds().height();
            final int n2 = width / 2 - AndroidUtilities.dp(3.0f);
            final int n3 = height / 2 - AndroidUtilities.dp(3.0f);
            this.cicleRect.set((float)n2, (float)n3, (float)(n2 + AndroidUtilities.dp(6.0f)), (float)(n3 + AndroidUtilities.dp(6.0f)));
            canvas.drawArc(this.cicleRect, (float)(this.radOffset - 90), 90.0f, false, this.outerPaint);
            this.invalidateSelf();
        }
        if (this.isEnabled && (this.connected || this.connectedAnimationProgress != 0.0f)) {
            this.fullDrawable.setAlpha((int)(this.connectedAnimationProgress * 255.0f));
            this.fullDrawable.setBounds(this.getBounds());
            this.fullDrawable.draw(canvas);
        }
        if (this.connected) {
            final float connectedAnimationProgress = this.connectedAnimationProgress;
            if (connectedAnimationProgress != 1.0f) {
                this.connectedAnimationProgress = connectedAnimationProgress + n / 300.0f;
                if (this.connectedAnimationProgress > 1.0f) {
                    this.connectedAnimationProgress = 1.0f;
                }
                this.invalidateSelf();
                return;
            }
        }
        if (!this.connected) {
            final float connectedAnimationProgress2 = this.connectedAnimationProgress;
            if (connectedAnimationProgress2 != 0.0f) {
                this.connectedAnimationProgress = connectedAnimationProgress2 - n / 300.0f;
                if (this.connectedAnimationProgress < 0.0f) {
                    this.connectedAnimationProgress = 0.0f;
                }
                this.invalidateSelf();
            }
        }
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(24.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(24.0f);
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.emptyDrawable.setColorFilter(colorFilter);
        this.fullDrawable.setColorFilter(colorFilter);
    }
    
    public void setConnected(final boolean isEnabled, final boolean connected, final boolean b) {
        this.isEnabled = isEnabled;
        this.connected = connected;
        this.lastUpdateTime = SystemClock.elapsedRealtime();
        if (!b) {
            float connectedAnimationProgress;
            if (this.connected) {
                connectedAnimationProgress = 1.0f;
            }
            else {
                connectedAnimationProgress = 0.0f;
            }
            this.connectedAnimationProgress = connectedAnimationProgress;
        }
        this.invalidateSelf();
    }
}
