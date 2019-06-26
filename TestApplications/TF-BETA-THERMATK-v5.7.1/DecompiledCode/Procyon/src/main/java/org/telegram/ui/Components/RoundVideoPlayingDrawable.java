// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Canvas;
import android.view.View;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class RoundVideoPlayingDrawable extends Drawable
{
    private long lastUpdateTime;
    private Paint paint;
    private View parentView;
    private float progress1;
    private int progress1Direction;
    private float progress2;
    private int progress2Direction;
    private float progress3;
    private int progress3Direction;
    private boolean started;
    
    public RoundVideoPlayingDrawable(final View parentView) {
        this.lastUpdateTime = 0L;
        this.started = false;
        this.paint = new Paint(1);
        this.progress1 = 0.47f;
        this.progress2 = 0.0f;
        this.progress3 = 0.32f;
        this.progress1Direction = 1;
        this.progress2Direction = 1;
        this.progress3Direction = 1;
        this.parentView = parentView;
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 50L;
        if (n > 50L) {
            n = n2;
        }
        final float progress1 = this.progress1;
        final float n3 = (float)n;
        this.progress1 = progress1 + n3 / 300.0f * this.progress1Direction;
        final float progress2 = this.progress1;
        if (progress2 > 1.0f) {
            this.progress1Direction = -1;
            this.progress1 = 1.0f;
        }
        else if (progress2 < 0.0f) {
            this.progress1Direction = 1;
            this.progress1 = 0.0f;
        }
        this.progress2 += n3 / 310.0f * this.progress2Direction;
        final float progress3 = this.progress2;
        if (progress3 > 1.0f) {
            this.progress2Direction = -1;
            this.progress2 = 1.0f;
        }
        else if (progress3 < 0.0f) {
            this.progress2Direction = 1;
            this.progress2 = 0.0f;
        }
        this.progress3 += n3 / 320.0f * this.progress3Direction;
        final float progress4 = this.progress3;
        if (progress4 > 1.0f) {
            this.progress3Direction = -1;
            this.progress3 = 1.0f;
        }
        else if (progress4 < 0.0f) {
            this.progress3Direction = 1;
            this.progress3 = 0.0f;
        }
        this.parentView.invalidate();
    }
    
    public void draw(final Canvas canvas) {
        this.paint.setColor(Theme.getColor("chat_mediaTimeText"));
        final int left = this.getBounds().left;
        final int top = this.getBounds().top;
        for (int i = 0; i < 3; ++i) {
            canvas.drawRect((float)(AndroidUtilities.dp(2.0f) + left), (float)(AndroidUtilities.dp(this.progress1 * 7.0f + 2.0f) + top), (float)(AndroidUtilities.dp(4.0f) + left), (float)(AndroidUtilities.dp(10.0f) + top), this.paint);
            canvas.drawRect((float)(AndroidUtilities.dp(5.0f) + left), (float)(AndroidUtilities.dp(this.progress2 * 7.0f + 2.0f) + top), (float)(AndroidUtilities.dp(7.0f) + left), (float)(AndroidUtilities.dp(10.0f) + top), this.paint);
            canvas.drawRect((float)(AndroidUtilities.dp(8.0f) + left), (float)(AndroidUtilities.dp(this.progress3 * 7.0f + 2.0f) + top), (float)(AndroidUtilities.dp(10.0f) + left), (float)(AndroidUtilities.dp(10.0f) + top), this.paint);
        }
        if (this.started) {
            this.update();
        }
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(12.0f);
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
    
    public void start() {
        if (this.started) {
            return;
        }
        this.lastUpdateTime = System.currentTimeMillis();
        this.started = true;
        this.parentView.invalidate();
    }
    
    public void stop() {
        if (!this.started) {
            return;
        }
        this.started = false;
    }
}
