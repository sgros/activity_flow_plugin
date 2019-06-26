// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Bitmap$Config;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;

public class RadialProgress
{
    private static DecelerateInterpolator decelerateInterpolator;
    private boolean alphaForMiniPrevious;
    private boolean alphaForPrevious;
    private float animatedAlphaValue;
    private float animatedProgressValue;
    private float animationProgressStart;
    private Drawable checkBackgroundDrawable;
    private CheckDrawable checkDrawable;
    private RectF cicleRect;
    private Drawable currentDrawable;
    private Drawable currentMiniDrawable;
    private boolean currentMiniWithRound;
    private float currentProgress;
    private long currentProgressTime;
    private boolean currentWithRound;
    private int diff;
    private boolean drawMiniProgress;
    private boolean hideCurrentDrawable;
    private long lastUpdateTime;
    private Bitmap miniDrawBitmap;
    private Canvas miniDrawCanvas;
    private Paint miniProgressBackgroundPaint;
    private Paint miniProgressPaint;
    private float overrideAlpha;
    private View parent;
    private boolean previousCheckDrawable;
    private Drawable previousDrawable;
    private Drawable previousMiniDrawable;
    private boolean previousMiniWithRound;
    private boolean previousWithRound;
    private int progressColor;
    private Paint progressPaint;
    private RectF progressRect;
    private float radOffset;
    
    public RadialProgress(final View parent) {
        this.lastUpdateTime = 0L;
        this.radOffset = 0.0f;
        this.currentProgress = 0.0f;
        this.animationProgressStart = 0.0f;
        this.currentProgressTime = 0L;
        this.animatedProgressValue = 0.0f;
        this.progressRect = new RectF();
        this.cicleRect = new RectF();
        this.animatedAlphaValue = 1.0f;
        this.progressColor = -1;
        this.diff = AndroidUtilities.dp(4.0f);
        this.alphaForPrevious = true;
        this.alphaForMiniPrevious = true;
        this.overrideAlpha = 1.0f;
        if (RadialProgress.decelerateInterpolator == null) {
            RadialProgress.decelerateInterpolator = new DecelerateInterpolator();
        }
        (this.progressPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.progressPaint.setStrokeCap(Paint$Cap.ROUND);
        this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
        (this.miniProgressPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.miniProgressPaint.setStrokeCap(Paint$Cap.ROUND);
        this.miniProgressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.miniProgressBackgroundPaint = new Paint(1);
        this.parent = parent;
    }
    
    private void invalidateParent() {
        final int dp = AndroidUtilities.dp(2.0f);
        final View parent = this.parent;
        final RectF progressRect = this.progressRect;
        final int n = (int)progressRect.left;
        final int n2 = (int)progressRect.top;
        final int n3 = (int)progressRect.right;
        final int n4 = dp * 2;
        parent.invalidate(n - dp, n2 - dp, n3 + n4, (int)progressRect.bottom + n4);
    }
    
    private void updateAnimation(final boolean b) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final Drawable checkBackgroundDrawable = this.checkBackgroundDrawable;
        if (checkBackgroundDrawable != null && (this.currentDrawable == checkBackgroundDrawable || this.previousDrawable == checkBackgroundDrawable) && this.checkDrawable.updateAnimation(n)) {
            this.invalidateParent();
        }
        final boolean b2 = true;
        final boolean b3 = true;
        if (b) {
            if (this.animatedProgressValue != 1.0f) {
                this.radOffset += 360L * n / 3000.0f;
                final float currentProgress = this.currentProgress;
                final float animationProgressStart = this.animationProgressStart;
                final float n2 = currentProgress - animationProgressStart;
                if (n2 > 0.0f) {
                    this.currentProgressTime += n;
                    final long currentProgressTime = this.currentProgressTime;
                    if (currentProgressTime >= 300L) {
                        this.animatedProgressValue = currentProgress;
                        this.animationProgressStart = currentProgress;
                        this.currentProgressTime = 0L;
                    }
                    else {
                        this.animatedProgressValue = animationProgressStart + n2 * RadialProgress.decelerateInterpolator.getInterpolation(currentProgressTime / 300.0f);
                    }
                }
                this.invalidateParent();
            }
            if (this.drawMiniProgress) {
                if (this.animatedProgressValue >= 1.0f && this.previousMiniDrawable != null) {
                    this.animatedAlphaValue -= n / 200.0f;
                    if (this.animatedAlphaValue <= 0.0f) {
                        this.animatedAlphaValue = 0.0f;
                        this.previousMiniDrawable = null;
                        this.drawMiniProgress = (this.currentMiniDrawable != null && b3);
                    }
                    this.invalidateParent();
                }
            }
            else if (this.animatedProgressValue >= 1.0f && this.previousDrawable != null) {
                this.animatedAlphaValue -= n / 200.0f;
                if (this.animatedAlphaValue <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                    this.previousDrawable = null;
                }
                this.invalidateParent();
            }
        }
        else if (this.drawMiniProgress) {
            if (this.previousMiniDrawable != null) {
                this.animatedAlphaValue -= n / 200.0f;
                if (this.animatedAlphaValue <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                    this.previousMiniDrawable = null;
                    this.drawMiniProgress = (this.currentMiniDrawable != null && b2);
                }
                this.invalidateParent();
            }
        }
        else if (this.previousDrawable != null) {
            this.animatedAlphaValue -= n / 200.0f;
            if (this.animatedAlphaValue <= 0.0f) {
                this.animatedAlphaValue = 0.0f;
                this.previousDrawable = null;
            }
            this.invalidateParent();
        }
    }
    
    public void draw(final Canvas canvas) {
        if (this.drawMiniProgress && this.currentDrawable != null) {
            if (this.miniDrawCanvas != null) {
                this.miniDrawBitmap.eraseColor(0);
            }
            this.currentDrawable.setAlpha((int)(this.overrideAlpha * 255.0f));
            if (this.miniDrawCanvas != null) {
                this.currentDrawable.setBounds(0, 0, (int)this.progressRect.width(), (int)this.progressRect.height());
                this.currentDrawable.draw(this.miniDrawCanvas);
            }
            else {
                final Drawable currentDrawable = this.currentDrawable;
                final RectF progressRect = this.progressRect;
                currentDrawable.setBounds((int)progressRect.left, (int)progressRect.top, (int)progressRect.right, (int)progressRect.bottom);
                this.currentDrawable.draw(canvas);
            }
            int n;
            float n4;
            float n5;
            int n6;
            if (Math.abs(this.progressRect.width() - AndroidUtilities.dp(44.0f)) < AndroidUtilities.density) {
                n = 20;
                final float centerX = this.progressRect.centerX();
                final float n2 = 16;
                final float n3 = (float)AndroidUtilities.dp(n2);
                n4 = this.progressRect.centerY() + AndroidUtilities.dp(n2);
                n5 = centerX + n3;
                n6 = 0;
            }
            else {
                n = 22;
                final float centerX2 = this.progressRect.centerX();
                final float n7 = (float)AndroidUtilities.dp(18.0f);
                n4 = this.progressRect.centerY() + AndroidUtilities.dp(18.0f);
                n5 = centerX2 + n7;
                n6 = 2;
            }
            final int n8 = n / 2;
            float n9;
            if (this.previousMiniDrawable != null && this.alphaForMiniPrevious) {
                n9 = this.overrideAlpha * this.animatedAlphaValue;
            }
            else {
                n9 = 1.0f;
            }
            final Canvas miniDrawCanvas = this.miniDrawCanvas;
            if (miniDrawCanvas != null) {
                final float n10 = (float)(n + 18 + n6);
                miniDrawCanvas.drawCircle((float)AndroidUtilities.dp(n10), (float)AndroidUtilities.dp(n10), AndroidUtilities.dp((float)(n8 + 1)) * n9, Theme.checkboxSquare_eraserPaint);
            }
            else {
                this.miniProgressBackgroundPaint.setColor(this.progressColor);
                if (this.previousMiniDrawable != null && this.currentMiniDrawable == null) {
                    this.miniProgressBackgroundPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                }
                else {
                    this.miniProgressBackgroundPaint.setAlpha(255);
                }
                canvas.drawCircle(n5, n4, (float)AndroidUtilities.dp(12.0f), this.miniProgressBackgroundPaint);
            }
            if (this.miniDrawCanvas != null) {
                final Bitmap miniDrawBitmap = this.miniDrawBitmap;
                final RectF progressRect2 = this.progressRect;
                canvas.drawBitmap(miniDrawBitmap, (float)(int)progressRect2.left, (float)(int)progressRect2.top, (Paint)null);
            }
            final Drawable previousMiniDrawable = this.previousMiniDrawable;
            if (previousMiniDrawable != null) {
                if (this.alphaForMiniPrevious) {
                    previousMiniDrawable.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                }
                else {
                    previousMiniDrawable.setAlpha((int)(this.overrideAlpha * 255.0f));
                }
                final Drawable previousMiniDrawable2 = this.previousMiniDrawable;
                final float n11 = (float)n8;
                previousMiniDrawable2.setBounds((int)(n5 - AndroidUtilities.dp(n11) * n9), (int)(n4 - AndroidUtilities.dp(n11) * n9), (int)(AndroidUtilities.dp(n11) * n9 + n5), (int)(AndroidUtilities.dp(n11) * n9 + n4));
                this.previousMiniDrawable.draw(canvas);
            }
            if (!this.hideCurrentDrawable) {
                final Drawable currentMiniDrawable = this.currentMiniDrawable;
                if (currentMiniDrawable != null) {
                    if (this.previousMiniDrawable != null) {
                        currentMiniDrawable.setAlpha((int)((1.0f - this.animatedAlphaValue) * 255.0f * this.overrideAlpha));
                    }
                    else {
                        currentMiniDrawable.setAlpha((int)(this.overrideAlpha * 255.0f));
                    }
                    final Drawable currentMiniDrawable2 = this.currentMiniDrawable;
                    final float n12 = (float)n8;
                    currentMiniDrawable2.setBounds((int)(n5 - AndroidUtilities.dp(n12)), (int)(n4 - AndroidUtilities.dp(n12)), (int)(AndroidUtilities.dp(n12) + n5), (int)(AndroidUtilities.dp(n12) + n4));
                    this.currentMiniDrawable.draw(canvas);
                }
            }
            if (!this.currentMiniWithRound && !this.previousMiniWithRound) {
                this.updateAnimation(false);
            }
            else {
                this.miniProgressPaint.setColor(this.progressColor);
                if (this.previousMiniWithRound) {
                    this.miniProgressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                }
                else {
                    this.miniProgressPaint.setAlpha((int)(this.overrideAlpha * 255.0f));
                }
                final RectF cicleRect = this.cicleRect;
                final float n13 = (float)(n8 - 2);
                cicleRect.set(n5 - AndroidUtilities.dp(n13) * n9, n4 - AndroidUtilities.dp(n13) * n9, n5 + AndroidUtilities.dp(n13) * n9, n4 + AndroidUtilities.dp(n13) * n9);
                canvas.drawArc(this.cicleRect, this.radOffset - 90.0f, Math.max(4.0f, this.animatedProgressValue * 360.0f), false, this.miniProgressPaint);
                this.updateAnimation(true);
            }
        }
        else {
            final Drawable previousDrawable = this.previousDrawable;
            if (previousDrawable != null) {
                if (this.alphaForPrevious) {
                    previousDrawable.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                }
                else {
                    previousDrawable.setAlpha((int)(this.overrideAlpha * 255.0f));
                }
                final Drawable previousDrawable2 = this.previousDrawable;
                final RectF progressRect3 = this.progressRect;
                previousDrawable2.setBounds((int)progressRect3.left, (int)progressRect3.top, (int)progressRect3.right, (int)progressRect3.bottom);
                this.previousDrawable.draw(canvas);
            }
            if (!this.hideCurrentDrawable) {
                final Drawable currentDrawable2 = this.currentDrawable;
                if (currentDrawable2 != null) {
                    if (this.previousDrawable != null) {
                        currentDrawable2.setAlpha((int)((1.0f - this.animatedAlphaValue) * 255.0f * this.overrideAlpha));
                    }
                    else {
                        currentDrawable2.setAlpha((int)(this.overrideAlpha * 255.0f));
                    }
                    final Drawable currentDrawable3 = this.currentDrawable;
                    final RectF progressRect4 = this.progressRect;
                    currentDrawable3.setBounds((int)progressRect4.left, (int)progressRect4.top, (int)progressRect4.right, (int)progressRect4.bottom);
                    this.currentDrawable.draw(canvas);
                }
            }
            if (!this.currentWithRound && !this.previousWithRound) {
                this.updateAnimation(false);
            }
            else {
                this.progressPaint.setColor(this.progressColor);
                if (this.previousWithRound) {
                    this.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f * this.overrideAlpha));
                }
                else {
                    this.progressPaint.setAlpha((int)(this.overrideAlpha * 255.0f));
                }
                final RectF cicleRect2 = this.cicleRect;
                final RectF progressRect5 = this.progressRect;
                final float left = progressRect5.left;
                final int diff = this.diff;
                cicleRect2.set(left + diff, progressRect5.top + diff, progressRect5.right - diff, progressRect5.bottom - diff);
                canvas.drawArc(this.cicleRect, this.radOffset - 90.0f, Math.max(4.0f, this.animatedProgressValue * 360.0f), false, this.progressPaint);
                this.updateAnimation(true);
            }
        }
    }
    
    public float getAlpha() {
        float animatedAlphaValue;
        if (this.previousDrawable == null && this.currentDrawable == null) {
            animatedAlphaValue = 0.0f;
        }
        else {
            animatedAlphaValue = this.animatedAlphaValue;
        }
        return animatedAlphaValue;
    }
    
    public RectF getProgressRect() {
        return this.progressRect;
    }
    
    public boolean isDrawCheckDrawable() {
        return this.currentDrawable == this.checkBackgroundDrawable;
    }
    
    public void setAlphaForMiniPrevious(final boolean alphaForMiniPrevious) {
        this.alphaForMiniPrevious = alphaForMiniPrevious;
    }
    
    public void setAlphaForPrevious(final boolean alphaForPrevious) {
        this.alphaForPrevious = alphaForPrevious;
    }
    
    public void setBackground(final Drawable currentDrawable, final boolean currentWithRound, final boolean b) {
        this.lastUpdateTime = System.currentTimeMillis();
        Label_0061: {
            if (b) {
                final Drawable currentDrawable2 = this.currentDrawable;
                if (currentDrawable2 != currentDrawable) {
                    this.previousDrawable = currentDrawable2;
                    this.previousWithRound = this.currentWithRound;
                    this.setProgress(this.animatedAlphaValue = 1.0f, b);
                    break Label_0061;
                }
            }
            this.previousDrawable = null;
            this.previousWithRound = false;
        }
        this.currentWithRound = currentWithRound;
        this.currentDrawable = currentDrawable;
        if (!b) {
            this.parent.invalidate();
        }
        else {
            this.invalidateParent();
        }
    }
    
    public void setCheckBackground(final boolean b, final boolean b2) {
        if (this.checkDrawable == null) {
            this.checkDrawable = new CheckDrawable();
            this.checkBackgroundDrawable = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), this.checkDrawable, 0);
        }
        Theme.setCombinedDrawableColor(this.checkBackgroundDrawable, Theme.getColor("chat_mediaLoaderPhoto"), false);
        Theme.setCombinedDrawableColor(this.checkBackgroundDrawable, Theme.getColor("chat_mediaLoaderPhotoIcon"), true);
        final Drawable currentDrawable = this.currentDrawable;
        final Drawable checkBackgroundDrawable = this.checkBackgroundDrawable;
        if (currentDrawable != checkBackgroundDrawable) {
            this.setBackground(checkBackgroundDrawable, b, b2);
            this.checkDrawable.resetProgress(b2);
        }
    }
    
    public void setDiff(final int diff) {
        this.diff = diff;
    }
    
    public void setHideCurrentDrawable(final boolean hideCurrentDrawable) {
        this.hideCurrentDrawable = hideCurrentDrawable;
    }
    
    public void setMiniBackground(final Drawable currentMiniDrawable, final boolean currentMiniWithRound, final boolean b) {
        this.lastUpdateTime = System.currentTimeMillis();
        final boolean b2 = false;
        Label_0064: {
            if (b) {
                final Drawable currentMiniDrawable2 = this.currentMiniDrawable;
                if (currentMiniDrawable2 != currentMiniDrawable) {
                    this.previousMiniDrawable = currentMiniDrawable2;
                    this.previousMiniWithRound = this.currentMiniWithRound;
                    this.setProgress(this.animatedAlphaValue = 1.0f, b);
                    break Label_0064;
                }
            }
            this.previousMiniDrawable = null;
            this.previousMiniWithRound = false;
        }
        this.currentMiniWithRound = currentMiniWithRound;
        this.currentMiniDrawable = currentMiniDrawable;
        boolean drawMiniProgress = false;
        Label_0093: {
            if (this.previousMiniDrawable == null) {
                drawMiniProgress = b2;
                if (this.currentMiniDrawable == null) {
                    break Label_0093;
                }
            }
            drawMiniProgress = true;
        }
        this.drawMiniProgress = drawMiniProgress;
        if (this.drawMiniProgress && this.miniDrawBitmap == null) {
            try {
                this.miniDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(48.0f), AndroidUtilities.dp(48.0f), Bitmap$Config.ARGB_8888);
                this.miniDrawCanvas = new Canvas(this.miniDrawBitmap);
            }
            catch (Throwable t) {}
        }
        if (!b) {
            this.parent.invalidate();
        }
        else {
            this.invalidateParent();
        }
    }
    
    public void setMiniProgressBackgroundColor(final int color) {
        this.miniProgressBackgroundPaint.setColor(color);
    }
    
    public void setOverrideAlpha(final float overrideAlpha) {
        this.overrideAlpha = overrideAlpha;
    }
    
    public void setProgress(final float n, final boolean b) {
        if (this.drawMiniProgress) {
            if (n != 1.0f && this.animatedAlphaValue != 0.0f && this.previousMiniDrawable != null) {
                this.animatedAlphaValue = 0.0f;
                this.previousMiniDrawable = null;
                this.drawMiniProgress = (this.currentMiniDrawable != null);
            }
        }
        else if (n != 1.0f && this.animatedAlphaValue != 0.0f && this.previousDrawable != null) {
            this.animatedAlphaValue = 0.0f;
            this.previousDrawable = null;
        }
        if (!b) {
            this.animatedProgressValue = n;
            this.animationProgressStart = n;
        }
        else {
            if (this.animatedProgressValue > n) {
                this.animatedProgressValue = n;
            }
            this.animationProgressStart = this.animatedProgressValue;
        }
        this.currentProgress = n;
        this.currentProgressTime = 0L;
        this.invalidateParent();
    }
    
    public void setProgressColor(final int progressColor) {
        this.progressColor = progressColor;
    }
    
    public void setProgressRect(final int n, final int n2, final int n3, final int n4) {
        this.progressRect.set((float)n, (float)n2, (float)n3, (float)n4);
    }
    
    public void setStrokeWidth(final int n) {
        this.progressPaint.setStrokeWidth((float)n);
    }
    
    public boolean swapBackground(final Drawable currentDrawable) {
        if (this.currentDrawable != currentDrawable) {
            this.currentDrawable = currentDrawable;
            return true;
        }
        return false;
    }
    
    public boolean swapMiniBackground(final Drawable currentMiniDrawable) {
        final Drawable currentMiniDrawable2 = this.currentMiniDrawable;
        boolean drawMiniProgress = false;
        if (currentMiniDrawable2 != currentMiniDrawable) {
            this.currentMiniDrawable = currentMiniDrawable;
            if (this.previousMiniDrawable != null || this.currentMiniDrawable != null) {
                drawMiniProgress = true;
            }
            this.drawMiniProgress = drawMiniProgress;
            return true;
        }
        return false;
    }
    
    private class CheckDrawable extends Drawable
    {
        private Paint paint;
        private float progress;
        
        public CheckDrawable() {
            (this.paint = new Paint(1)).setStyle(Paint$Style.STROKE);
            this.paint.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
            this.paint.setStrokeCap(Paint$Cap.ROUND);
            this.paint.setColor(-1);
        }
        
        public void draw(final Canvas canvas) {
            final int n = this.getBounds().centerX() - AndroidUtilities.dp(12.0f);
            final int n2 = this.getBounds().centerY() - AndroidUtilities.dp(6.0f);
            final float progress = this.progress;
            float interpolation = 1.0f;
            if (progress != 1.0f) {
                interpolation = RadialProgress.decelerateInterpolator.getInterpolation(this.progress);
            }
            canvas.drawLine((float)(AndroidUtilities.dp(7.0f) + n), (float)((int)AndroidUtilities.dpf2(13.0f) + n2), (float)((int)(AndroidUtilities.dp(7.0f) - AndroidUtilities.dp(6.0f) * interpolation) + n), (float)((int)(AndroidUtilities.dpf2(13.0f) - AndroidUtilities.dp(6.0f) * interpolation) + n2), this.paint);
            canvas.drawLine((float)((int)AndroidUtilities.dpf2(7.0f) + n), (float)((int)AndroidUtilities.dpf2(13.0f) + n2), (float)(n + (int)(AndroidUtilities.dpf2(7.0f) + AndroidUtilities.dp(13.0f) * interpolation)), (float)(n2 + (int)(AndroidUtilities.dpf2(13.0f) - AndroidUtilities.dp(13.0f) * interpolation)), this.paint);
        }
        
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(48.0f);
        }
        
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(48.0f);
        }
        
        public int getOpacity() {
            return -2;
        }
        
        public void resetProgress(final boolean b) {
            float progress;
            if (b) {
                progress = 0.0f;
            }
            else {
                progress = 1.0f;
            }
            this.progress = progress;
        }
        
        public void setAlpha(final int alpha) {
            this.paint.setAlpha(alpha);
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
            this.paint.setColorFilter(colorFilter);
        }
        
        public boolean updateAnimation(final long n) {
            final float progress = this.progress;
            if (progress < 1.0f) {
                this.progress = progress + n / 700.0f;
                if (this.progress > 1.0f) {
                    this.progress = 1.0f;
                }
                return true;
            }
            return false;
        }
    }
}
