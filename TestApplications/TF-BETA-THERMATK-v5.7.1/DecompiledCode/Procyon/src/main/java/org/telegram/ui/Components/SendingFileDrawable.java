// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;

public class SendingFileDrawable extends StatusDrawable
{
    private boolean isChat;
    private long lastUpdateTime;
    private float progress;
    private boolean started;
    
    public SendingFileDrawable() {
        this.isChat = false;
        this.lastUpdateTime = 0L;
        this.started = false;
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 50L;
        if (n > 50L) {
            n = n2;
        }
        this.progress += n / 500.0f;
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
        for (int i = 0; i < 3; ++i) {
            if (i == 0) {
                Theme.chat_statusRecordPaint.setAlpha((int)(this.progress * 255.0f));
            }
            else if (i == 2) {
                Theme.chat_statusRecordPaint.setAlpha((int)((1.0f - this.progress) * 255.0f));
            }
            else {
                Theme.chat_statusRecordPaint.setAlpha(255);
            }
            final float n = AndroidUtilities.dp(5.0f) * this.progress + AndroidUtilities.dp(5.0f) * i;
            float n2;
            if (this.isChat) {
                n2 = 3.0f;
            }
            else {
                n2 = 4.0f;
            }
            final float n3 = (float)AndroidUtilities.dp(n2);
            final float n4 = (float)AndroidUtilities.dp(4.0f);
            final boolean isChat = this.isChat;
            final float n5 = 7.0f;
            float n6;
            if (isChat) {
                n6 = 7.0f;
            }
            else {
                n6 = 8.0f;
            }
            canvas.drawLine(n, n3, n + n4, (float)AndroidUtilities.dp(n6), Theme.chat_statusRecordPaint);
            float n7;
            if (this.isChat) {
                n7 = 11.0f;
            }
            else {
                n7 = 12.0f;
            }
            final float n8 = (float)AndroidUtilities.dp(n7);
            final float n9 = (float)AndroidUtilities.dp(4.0f);
            float n10;
            if (this.isChat) {
                n10 = n5;
            }
            else {
                n10 = 8.0f;
            }
            canvas.drawLine(n, n8, n + n9, (float)AndroidUtilities.dp(n10), Theme.chat_statusRecordPaint);
        }
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
