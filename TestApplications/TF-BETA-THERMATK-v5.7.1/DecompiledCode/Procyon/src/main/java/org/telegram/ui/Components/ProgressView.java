// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint;

public class ProgressView
{
    public float currentProgress;
    public int height;
    private Paint innerPaint;
    private Paint outerPaint;
    public float progressHeight;
    public int width;
    
    public ProgressView() {
        this.currentProgress = 0.0f;
        this.progressHeight = (float)AndroidUtilities.dp(2.0f);
        this.innerPaint = new Paint();
        this.outerPaint = new Paint();
    }
    
    public void draw(final Canvas canvas) {
        final int height = this.height;
        final float n = (float)(height / 2);
        final float progressHeight = this.progressHeight;
        canvas.drawRect(0.0f, n - progressHeight / 2.0f, (float)this.width, height / 2 + progressHeight / 2.0f, this.innerPaint);
        final int height2 = this.height;
        final float n2 = (float)(height2 / 2);
        final float progressHeight2 = this.progressHeight;
        canvas.drawRect(0.0f, n2 - progressHeight2 / 2.0f, this.width * this.currentProgress, height2 / 2 + progressHeight2 / 2.0f, this.outerPaint);
    }
    
    public void setProgress(float currentProgress) {
        this.currentProgress = currentProgress;
        currentProgress = this.currentProgress;
        if (currentProgress < 0.0f) {
            this.currentProgress = 0.0f;
        }
        else if (currentProgress > 1.0f) {
            this.currentProgress = 1.0f;
        }
    }
    
    public void setProgressColors(final int color, final int color2) {
        this.innerPaint.setColor(color);
        this.outerPaint.setColor(color2);
    }
}
