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
import android.view.animation.DecelerateInterpolator;

public class TypingDotsDrawable extends StatusDrawable
{
    private int currentAccount;
    private DecelerateInterpolator decelerateInterpolator;
    private float[] elapsedTimes;
    private boolean isChat;
    private long lastUpdateTime;
    private float[] scales;
    private float[] startTimes;
    private boolean started;
    
    public TypingDotsDrawable() {
        this.currentAccount = UserConfig.selectedAccount;
        this.isChat = false;
        this.scales = new float[3];
        this.startTimes = new float[] { 0.0f, 150.0f, 300.0f };
        this.elapsedTimes = new float[] { 0.0f, 0.0f, 0.0f };
        this.lastUpdateTime = 0L;
        this.started = false;
        this.decelerateInterpolator = new DecelerateInterpolator();
    }
    
    private void checkUpdate() {
        if (this.started) {
            if (!NotificationCenter.getInstance(this.currentAccount).isAnimationInProgress()) {
                this.update();
            }
            else {
                AndroidUtilities.runOnUIThread(new _$$Lambda$TypingDotsDrawable$6mZKSEfaAngfDGlsoqdZ2efS_EU(this), 100L);
            }
        }
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 50L;
        if (n > 50L) {
            n = n2;
        }
        for (int i = 0; i < 3; ++i) {
            final float[] elapsedTimes = this.elapsedTimes;
            elapsedTimes[i] += n;
            final float n3 = elapsedTimes[i];
            final float[] startTimes = this.startTimes;
            final float n4 = n3 - startTimes[i];
            if (n4 > 0.0f) {
                if (n4 <= 320.0f) {
                    this.scales[i] = this.decelerateInterpolator.getInterpolation(n4 / 320.0f) + 1.33f;
                }
                else if (n4 <= 640.0f) {
                    this.scales[i] = 1.0f - this.decelerateInterpolator.getInterpolation((n4 - 320.0f) / 320.0f) + 1.33f;
                }
                else if (n4 >= 800.0f) {
                    startTimes[i] = (elapsedTimes[i] = 0.0f);
                    this.scales[i] = 1.33f;
                }
                else {
                    this.scales[i] = 1.33f;
                }
            }
            else {
                this.scales[i] = 1.33f;
            }
        }
        this.invalidateSelf();
    }
    
    public void draw(final Canvas canvas) {
        int n;
        int n2;
        if (this.isChat) {
            n = AndroidUtilities.dp(8.5f);
            n2 = this.getBounds().top;
        }
        else {
            n = AndroidUtilities.dp(9.3f);
            n2 = this.getBounds().top;
        }
        Theme.chat_statusPaint.setAlpha(255);
        final float n3 = (float)AndroidUtilities.dp(3.0f);
        final float n4 = (float)(n + n2);
        canvas.drawCircle(n3, n4, this.scales[0] * AndroidUtilities.density, Theme.chat_statusPaint);
        canvas.drawCircle((float)AndroidUtilities.dp(9.0f), n4, this.scales[1] * AndroidUtilities.density, Theme.chat_statusPaint);
        canvas.drawCircle((float)AndroidUtilities.dp(15.0f), n4, this.scales[2] * AndroidUtilities.density, Theme.chat_statusPaint);
        this.checkUpdate();
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(18.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(18.0f);
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
        for (int i = 0; i < 3; ++i) {
            this.elapsedTimes[i] = 0.0f;
            this.scales[i] = 1.33f;
        }
        final float[] startTimes = this.startTimes;
        startTimes[0] = 0.0f;
        startTimes[1] = 150.0f;
        startTimes[2] = 300.0f;
        this.started = false;
    }
}
