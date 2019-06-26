// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.SharedConfig;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.graphics.RectF;
import android.view.View;
import android.graphics.Path;
import android.graphics.Paint;

public class PacmanAnimation
{
    private boolean currentGhostWalk;
    private Paint edgePaint;
    private Runnable finishRunnable;
    private Path ghostPath;
    private float ghostProgress;
    private boolean ghostWalk;
    private long lastUpdateTime;
    private Paint paint;
    private View parentView;
    private float progress;
    private RectF rect;
    private float translationProgress;
    
    public PacmanAnimation(final View parentView) {
        this.paint = new Paint(1);
        this.edgePaint = new Paint(1);
        this.lastUpdateTime = 0L;
        this.rect = new RectF();
        this.edgePaint.setStyle(Paint$Style.STROKE);
        this.edgePaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.parentView = parentView;
    }
    
    private void drawGhost(final Canvas canvas, final int n) {
        if (this.ghostPath == null || this.ghostWalk != this.currentGhostWalk) {
            if (this.ghostPath == null) {
                this.ghostPath = new Path();
            }
            this.ghostPath.reset();
            this.currentGhostWalk = this.ghostWalk;
            if (this.currentGhostWalk) {
                this.ghostPath.moveTo(0.0f, (float)AndroidUtilities.dp(50.0f));
                this.ghostPath.lineTo(0.0f, (float)AndroidUtilities.dp(24.0f));
                this.rect.set(0.0f, 0.0f, (float)AndroidUtilities.dp(42.0f), (float)AndroidUtilities.dp(24.0f));
                this.ghostPath.arcTo(this.rect, 180.0f, 180.0f, false);
                this.ghostPath.lineTo((float)AndroidUtilities.dp(42.0f), (float)AndroidUtilities.dp(50.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(35.0f), (float)AndroidUtilities.dp(43.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(28.0f), (float)AndroidUtilities.dp(50.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(21.0f), (float)AndroidUtilities.dp(43.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(14.0f), (float)AndroidUtilities.dp(50.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(7.0f), (float)AndroidUtilities.dp(43.0f));
            }
            else {
                this.ghostPath.moveTo(0.0f, (float)AndroidUtilities.dp(43.0f));
                this.ghostPath.lineTo(0.0f, (float)AndroidUtilities.dp(24.0f));
                this.rect.set(0.0f, 0.0f, (float)AndroidUtilities.dp(42.0f), (float)AndroidUtilities.dp(24.0f));
                this.ghostPath.arcTo(this.rect, 180.0f, 180.0f, false);
                this.ghostPath.lineTo((float)AndroidUtilities.dp(42.0f), (float)AndroidUtilities.dp(43.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(35.0f), (float)AndroidUtilities.dp(50.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(28.0f), (float)AndroidUtilities.dp(43.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(21.0f), (float)AndroidUtilities.dp(50.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(14.0f), (float)AndroidUtilities.dp(43.0f));
                this.ghostPath.lineTo((float)AndroidUtilities.dp(7.0f), (float)AndroidUtilities.dp(50.0f));
            }
            this.ghostPath.close();
        }
        canvas.drawPath(this.ghostPath, this.edgePaint);
        if (n == 0) {
            this.paint.setColor(-90112);
        }
        else if (n == 1) {
            this.paint.setColor(-85326);
        }
        else {
            this.paint.setColor(-16720161);
        }
        canvas.drawPath(this.ghostPath, this.paint);
        this.paint.setColor(-1);
        this.rect.set((float)AndroidUtilities.dp(8.0f), (float)AndroidUtilities.dp(14.0f), (float)AndroidUtilities.dp(20.0f), (float)AndroidUtilities.dp(28.0f));
        canvas.drawOval(this.rect, this.paint);
        this.rect.set((float)AndroidUtilities.dp(24.0f), (float)AndroidUtilities.dp(14.0f), (float)AndroidUtilities.dp(36.0f), (float)AndroidUtilities.dp(28.0f));
        canvas.drawOval(this.rect, this.paint);
        this.paint.setColor(-16777216);
        this.rect.set((float)AndroidUtilities.dp(14.0f), (float)AndroidUtilities.dp(18.0f), (float)AndroidUtilities.dp(19.0f), (float)AndroidUtilities.dp(24.0f));
        canvas.drawOval(this.rect, this.paint);
        this.rect.set((float)AndroidUtilities.dp(30.0f), (float)AndroidUtilities.dp(18.0f), (float)AndroidUtilities.dp(35.0f), (float)AndroidUtilities.dp(24.0f));
        canvas.drawOval(this.rect, this.paint);
    }
    
    private void update() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final long n2 = 17L;
        if (n > 17L) {
            n = n2;
        }
        if (this.progress >= 1.0f) {
            this.progress = 0.0f;
        }
        final float progress = this.progress;
        final float n3 = (float)n;
        this.progress = progress + n3 / 400.0f;
        if (this.progress > 1.0f) {
            this.progress = 1.0f;
        }
        this.translationProgress += n3 / 2000.0f;
        if (this.translationProgress > 1.0f) {
            this.translationProgress = 1.0f;
        }
        this.ghostProgress += n3 / 200.0f;
        if (this.ghostProgress >= 1.0f) {
            this.ghostWalk ^= true;
            this.ghostProgress = 0.0f;
        }
        this.parentView.invalidate();
    }
    
    public void draw(final Canvas canvas, int i) {
        final int dp = AndroidUtilities.dp(110.0f);
        float n;
        if (SharedConfig.useThreeLinesLayout) {
            n = 78.0f;
        }
        else {
            n = 72.0f;
        }
        final int dp2 = AndroidUtilities.dp(n);
        final int n2 = AndroidUtilities.dp(62.0f) * 3 + dp;
        final float n3 = (this.parentView.getMeasuredWidth() + n2) * this.translationProgress - n2;
        final int n4 = dp / 2;
        final int n5 = i - n4;
        this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
        final int n6 = dp2 / 2;
        final float n7 = (float)(i - n6);
        final float n8 = n3 + n4;
        canvas.drawRect(0.0f, n7, n8, (float)(i + n6 + 1), this.paint);
        this.paint.setColor(-69120);
        final RectF rect = this.rect;
        final float n9 = (float)n5;
        final float n10 = n3 + dp;
        rect.set(n3, n9, n10, (float)(n5 + dp));
        final float progress = this.progress;
        float n11;
        if (progress < 0.5f) {
            n11 = (1.0f - progress / 0.5f) * 35.0f;
        }
        else {
            n11 = (progress - 0.5f) * 35.0f / 0.5f;
        }
        final int n12 = (int)n11;
        final RectF rect2 = this.rect;
        final float n13 = (float)n12;
        final float n14 = (float)(360 - n12 * 2);
        canvas.drawArc(rect2, n13, n14, true, this.edgePaint);
        canvas.drawArc(this.rect, n13, n14, true, this.paint);
        this.paint.setColor(-16777216);
        canvas.drawCircle(n8 - AndroidUtilities.dp(8.0f), (float)(n5 + dp / 4), (float)AndroidUtilities.dp(8.0f), this.paint);
        canvas.save();
        canvas.translate(n10 + AndroidUtilities.dp(20.0f), (float)(i - AndroidUtilities.dp(25.0f)));
        for (i = 0; i < 3; ++i) {
            this.drawGhost(canvas, i);
            canvas.translate((float)AndroidUtilities.dp(62.0f), 0.0f);
        }
        canvas.restore();
        if (this.translationProgress >= 1.0f) {
            this.finishRunnable.run();
        }
        this.update();
    }
    
    public void setFinishRunnable(final Runnable finishRunnable) {
        this.finishRunnable = finishRunnable;
    }
    
    public void start() {
        this.translationProgress = 0.0f;
        this.progress = 0.0f;
        this.lastUpdateTime = System.currentTimeMillis();
        this.parentView.invalidate();
    }
}
