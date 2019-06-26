// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import android.graphics.RectF;
import android.graphics.Paint;

public class PlayingGameDrawable extends StatusDrawable
{
    private int currentAccount;
    private boolean isChat;
    private long lastUpdateTime;
    private Paint paint;
    private float progress;
    private RectF rect;
    private boolean started;
    
    public PlayingGameDrawable() {
        this.isChat = false;
        this.paint = new Paint(1);
        this.currentAccount = UserConfig.selectedAccount;
        this.lastUpdateTime = 0L;
        this.started = false;
        this.rect = new RectF();
    }
    
    private void checkUpdate() {
        if (this.started) {
            if (!NotificationCenter.getInstance(this.currentAccount).isAnimationInProgress()) {
                this.update();
            }
            else {
                AndroidUtilities.runOnUIThread(new _$$Lambda$PlayingGameDrawable$65ulwJDGFoIbDH0sYp9eXDBVUBc(this), 100L);
            }
        }
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 16L;
        if (n > 16L) {
            n = n2;
        }
        if (this.progress >= 1.0f) {
            this.progress = 0.0f;
        }
        this.progress += n / 300.0f;
        if (this.progress > 1.0f) {
            this.progress = 1.0f;
        }
        this.invalidateSelf();
    }
    
    public void draw(final Canvas canvas) {
        final int dp = AndroidUtilities.dp(10.0f);
        int n = this.getBounds().top + (this.getIntrinsicHeight() - dp) / 2;
        if (!this.isChat) {
            n += AndroidUtilities.dp(1.0f);
        }
        this.paint.setColor(Theme.getColor("chat_status"));
        this.rect.set(0.0f, (float)n, (float)dp, (float)(n + dp));
        final float progress = this.progress;
        float n2;
        if (progress < 0.5f) {
            n2 = (1.0f - progress / 0.5f) * 35.0f;
        }
        else {
            n2 = (progress - 0.5f) * 35.0f / 0.5f;
        }
        final int n3 = (int)n2;
        for (int i = 0; i < 3; ++i) {
            final float n4 = (float)(AndroidUtilities.dp(5.0f) * i + AndroidUtilities.dp(9.2f));
            final float n5 = (float)AndroidUtilities.dp(5.0f);
            final float progress2 = this.progress;
            if (i == 2) {
                this.paint.setAlpha(Math.min(255, (int)(progress2 * 255.0f / 0.5f)));
            }
            else if (i == 0) {
                if (progress2 > 0.5f) {
                    this.paint.setAlpha((int)((1.0f - (progress2 - 0.5f) / 0.5f) * 255.0f));
                }
                else {
                    this.paint.setAlpha(255);
                }
            }
            else {
                this.paint.setAlpha(255);
            }
            canvas.drawCircle(n4 - n5 * progress2, (float)(dp / 2 + n), (float)AndroidUtilities.dp(1.2f), this.paint);
        }
        this.paint.setAlpha(255);
        canvas.drawArc(this.rect, (float)n3, (float)(360 - n3 * 2), true, this.paint);
        this.paint.setColor(Theme.getColor("actionBarDefault"));
        canvas.drawCircle((float)AndroidUtilities.dp(4.0f), (float)(n + dp / 2 - AndroidUtilities.dp(2.0f)), (float)AndroidUtilities.dp(1.0f), this.paint);
        this.checkUpdate();
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(18.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(20.0f);
    }
    
    public int getOpacity() {
        return -2;
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
        this.progress = 0.0f;
        this.started = false;
    }
}
