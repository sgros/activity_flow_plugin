// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;

public class RoundStatusDrawable extends StatusDrawable
{
    private boolean isChat;
    private long lastUpdateTime;
    private float progress;
    private int progressDirection;
    private boolean started;
    
    public RoundStatusDrawable() {
        this.isChat = false;
        this.lastUpdateTime = 0L;
        this.started = false;
        this.progressDirection = 1;
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 50L;
        if (n > 50L) {
            n = n2;
        }
        final float progress = this.progress;
        final int progressDirection = this.progressDirection;
        this.progress = progress + progressDirection * n / 400.0f;
        if (progressDirection > 0 && this.progress >= 1.0f) {
            this.progressDirection = -1;
            this.progress = 1.0f;
        }
        else if (this.progressDirection < 0 && this.progress <= 0.0f) {
            this.progressDirection = 1;
            this.progress = 0.0f;
        }
        this.invalidateSelf();
    }
    
    public void draw(final Canvas canvas) {
        Theme.chat_statusPaint.setAlpha((int)(this.progress * 200.0f) + 55);
        final float n = (float)AndroidUtilities.dp(6.0f);
        float n2;
        if (this.isChat) {
            n2 = 8.0f;
        }
        else {
            n2 = 9.0f;
        }
        canvas.drawCircle(n, (float)AndroidUtilities.dp(n2), (float)AndroidUtilities.dp(4.0f), Theme.chat_statusPaint);
        if (this.started) {
            this.update();
        }
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(10.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(12.0f);
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    @Override
    public void setIsChat(final boolean isChat) {
        this.isChat = isChat;
    }
    
    @Override
    public void start() {
        this.lastUpdateTime = System.currentTimeMillis();
        this.started = true;
        this.invalidateSelf();
    }
    
    @Override
    public void stop() {
        this.started = false;
    }
}
