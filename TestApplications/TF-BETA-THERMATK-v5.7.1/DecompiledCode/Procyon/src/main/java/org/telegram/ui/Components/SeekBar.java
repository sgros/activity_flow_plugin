// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Paint;

public class SeekBar
{
    private static Paint paint;
    private static int thumbWidth;
    private int backgroundColor;
    private int backgroundSelectedColor;
    private float bufferedProgress;
    private int cacheColor;
    private int circleColor;
    private SeekBarDelegate delegate;
    private int height;
    private int lineHeight;
    private boolean pressed;
    private int progressColor;
    private RectF rect;
    private boolean selected;
    private int thumbDX;
    private int thumbX;
    private int width;
    
    public SeekBar(final Context context) {
        this.thumbX = 0;
        this.thumbDX = 0;
        this.pressed = false;
        this.rect = new RectF();
        this.lineHeight = AndroidUtilities.dp(2.0f);
        if (SeekBar.paint == null) {
            SeekBar.paint = new Paint(1);
            SeekBar.thumbWidth = AndroidUtilities.dp(24.0f);
        }
    }
    
    public void draw(final Canvas canvas) {
        final RectF rect = this.rect;
        final int thumbWidth = SeekBar.thumbWidth;
        final float n = (float)(thumbWidth / 2);
        final int height = this.height;
        final int n2 = height / 2;
        final int lineHeight = this.lineHeight;
        rect.set(n, (float)(n2 - lineHeight / 2), (float)(this.width - thumbWidth / 2), (float)(height / 2 + lineHeight / 2));
        final Paint paint = SeekBar.paint;
        int color;
        if (this.selected) {
            color = this.backgroundSelectedColor;
        }
        else {
            color = this.backgroundColor;
        }
        paint.setColor(color);
        final RectF rect2 = this.rect;
        final int thumbWidth2 = SeekBar.thumbWidth;
        canvas.drawRoundRect(rect2, (float)(thumbWidth2 / 2), (float)(thumbWidth2 / 2), SeekBar.paint);
        if (this.bufferedProgress > 0.0f) {
            final Paint paint2 = SeekBar.paint;
            int color2;
            if (this.selected) {
                color2 = this.backgroundSelectedColor;
            }
            else {
                color2 = this.cacheColor;
            }
            paint2.setColor(color2);
            final RectF rect3 = this.rect;
            final int thumbWidth3 = SeekBar.thumbWidth;
            final float n3 = (float)(thumbWidth3 / 2);
            final int height2 = this.height;
            final int n4 = height2 / 2;
            final int lineHeight2 = this.lineHeight;
            rect3.set(n3, (float)(n4 - lineHeight2 / 2), thumbWidth3 / 2 + this.bufferedProgress * (this.width - thumbWidth3), (float)(height2 / 2 + lineHeight2 / 2));
            final RectF rect4 = this.rect;
            final int thumbWidth4 = SeekBar.thumbWidth;
            canvas.drawRoundRect(rect4, (float)(thumbWidth4 / 2), (float)(thumbWidth4 / 2), SeekBar.paint);
        }
        final RectF rect5 = this.rect;
        final int thumbWidth5 = SeekBar.thumbWidth;
        final float n5 = (float)(thumbWidth5 / 2);
        final int height3 = this.height;
        final int n6 = height3 / 2;
        final int lineHeight3 = this.lineHeight;
        rect5.set(n5, (float)(n6 - lineHeight3 / 2), (float)(thumbWidth5 / 2 + this.thumbX), (float)(height3 / 2 + lineHeight3 / 2));
        SeekBar.paint.setColor(this.progressColor);
        final RectF rect6 = this.rect;
        final int thumbWidth6 = SeekBar.thumbWidth;
        canvas.drawRoundRect(rect6, (float)(thumbWidth6 / 2), (float)(thumbWidth6 / 2), SeekBar.paint);
        SeekBar.paint.setColor(this.circleColor);
        final float n7 = (float)(this.thumbX + SeekBar.thumbWidth / 2);
        final float n8 = (float)(this.height / 2);
        float n9;
        if (this.pressed) {
            n9 = 8.0f;
        }
        else {
            n9 = 6.0f;
        }
        canvas.drawCircle(n7, n8, (float)AndroidUtilities.dp(n9), SeekBar.paint);
    }
    
    public float getProgress() {
        return this.thumbX / (float)(this.width - SeekBar.thumbWidth);
    }
    
    public boolean isDragging() {
        return this.pressed;
    }
    
    public boolean onTouch(int n, final float n2, final float n3) {
        if (n == 0) {
            n = this.height;
            final int thumbWidth = SeekBar.thumbWidth;
            final int n4 = (n - thumbWidth) / 2;
            final int thumbX = this.thumbX;
            if (thumbX - n4 <= n2 && n2 <= thumbWidth + thumbX + n4 && n3 >= 0.0f && n3 <= n) {
                this.pressed = true;
                this.thumbDX = (int)(n2 - thumbX);
                return true;
            }
        }
        else if (n != 1 && n != 3) {
            if (n == 2 && this.pressed) {
                this.thumbX = (int)(n2 - this.thumbDX);
                final int thumbX2 = this.thumbX;
                if (thumbX2 < 0) {
                    this.thumbX = 0;
                }
                else {
                    final int width = this.width;
                    n = SeekBar.thumbWidth;
                    if (thumbX2 > width - n) {
                        this.thumbX = width - n;
                    }
                }
                return true;
            }
        }
        else if (this.pressed) {
            if (n == 1) {
                final SeekBarDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.onSeekBarDrag(this.thumbX / (float)(this.width - SeekBar.thumbWidth));
                }
            }
            this.pressed = false;
            return true;
        }
        return false;
    }
    
    public void setBufferedProgress(final float bufferedProgress) {
        this.bufferedProgress = bufferedProgress;
    }
    
    public void setColors(final int backgroundColor, final int cacheColor, final int progressColor, final int circleColor, final int backgroundSelectedColor) {
        this.backgroundColor = backgroundColor;
        this.cacheColor = cacheColor;
        this.circleColor = circleColor;
        this.progressColor = progressColor;
        this.backgroundSelectedColor = backgroundSelectedColor;
    }
    
    public void setDelegate(final SeekBarDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setLineHeight(final int lineHeight) {
        this.lineHeight = lineHeight;
    }
    
    public void setProgress(final float n) {
        this.thumbX = (int)Math.ceil((this.width - SeekBar.thumbWidth) * n);
        final int thumbX = this.thumbX;
        if (thumbX < 0) {
            this.thumbX = 0;
        }
        else {
            final int width = this.width;
            final int thumbWidth = SeekBar.thumbWidth;
            if (thumbX > width - thumbWidth) {
                this.thumbX = width - thumbWidth;
            }
        }
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
    
    public void setSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public interface SeekBarDelegate
    {
        void onSeekBarDrag(final float p0);
    }
}
