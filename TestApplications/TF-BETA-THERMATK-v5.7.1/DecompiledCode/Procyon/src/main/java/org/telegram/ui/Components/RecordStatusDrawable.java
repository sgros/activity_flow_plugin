// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.RectF;

public class RecordStatusDrawable extends StatusDrawable
{
    private boolean isChat;
    private long lastUpdateTime;
    private float progress;
    private RectF rect;
    private boolean started;
    
    public RecordStatusDrawable() {
        this.isChat = false;
        this.lastUpdateTime = 0L;
        this.started = false;
        this.rect = new RectF();
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 50L;
        if (n > 50L) {
            n = n2;
        }
        this.progress += n / 800.0f;
        while (true) {
            final float progress = this.progress;
            if (progress <= 1.0f) {
                break;
            }
            this.progress = progress - 1.0f;
        }
        this.invalidateSelf();
    }
    
    public void draw(final Canvas canvas) {
        canvas.save();
        final int n = this.getIntrinsicHeight() / 2;
        float n2;
        if (this.isChat) {
            n2 = 1.0f;
        }
        else {
            n2 = 2.0f;
        }
        canvas.translate(0.0f, (float)(n + AndroidUtilities.dp(n2)));
        for (int i = 0; i < 4; ++i) {
            if (i == 0) {
                Theme.chat_statusRecordPaint.setAlpha((int)(this.progress * 255.0f));
            }
            else if (i == 3) {
                Theme.chat_statusRecordPaint.setAlpha((int)((1.0f - this.progress) * 255.0f));
            }
            else {
                Theme.chat_statusRecordPaint.setAlpha(255);
            }
            final float n3 = AndroidUtilities.dp(4.0f) * i + AndroidUtilities.dp(4.0f) * this.progress;
            final RectF rect = this.rect;
            final float n4 = -n3;
            rect.set(n4, n4, n3, n3);
            canvas.drawArc(this.rect, -15.0f, 30.0f, false, Theme.chat_statusRecordPaint);
        }
        canvas.restore();
        if (this.started) {
            this.update();
        }
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(14.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(18.0f);
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
