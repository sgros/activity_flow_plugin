// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;

public class PhotoEditorSeekBar extends View
{
    private PhotoEditorSeekBarDelegate delegate;
    private Paint innerPaint;
    private int maxValue;
    private int minValue;
    private Paint outerPaint;
    private boolean pressed;
    private float progress;
    private int thumbDX;
    private int thumbSize;
    
    public PhotoEditorSeekBar(final Context context) {
        super(context);
        this.innerPaint = new Paint();
        this.outerPaint = new Paint(1);
        this.thumbSize = AndroidUtilities.dp(16.0f);
        this.thumbDX = 0;
        this.progress = 0.0f;
        this.pressed = false;
        this.innerPaint.setColor(-11711155);
        this.outerPaint.setColor(-1);
    }
    
    public int getProgress() {
        final int minValue = this.minValue;
        return (int)(minValue + this.progress * (this.maxValue - minValue));
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = (this.getMeasuredHeight() - this.thumbSize) / 2;
        final int measuredWidth = this.getMeasuredWidth();
        final int thumbSize = this.thumbSize;
        final int n2 = (int)((measuredWidth - thumbSize) * this.progress);
        canvas.drawRect((float)(thumbSize / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), (float)(this.getMeasuredWidth() - this.thumbSize / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.innerPaint);
        if (this.minValue == 0) {
            canvas.drawRect((float)(this.thumbSize / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), (float)n2, (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.outerPaint);
        }
        else if (this.progress > 0.5f) {
            canvas.drawRect((float)(this.getMeasuredWidth() / 2 - AndroidUtilities.dp(1.0f)), (float)((this.getMeasuredHeight() - this.thumbSize) / 2), (float)(this.getMeasuredWidth() / 2), (float)((this.getMeasuredHeight() + this.thumbSize) / 2), this.outerPaint);
            canvas.drawRect((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), (float)n2, (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.outerPaint);
        }
        else {
            canvas.drawRect((float)(this.getMeasuredWidth() / 2), (float)((this.getMeasuredHeight() - this.thumbSize) / 2), (float)(this.getMeasuredWidth() / 2 + AndroidUtilities.dp(1.0f)), (float)((this.getMeasuredHeight() + this.thumbSize) / 2), this.outerPaint);
            canvas.drawRect((float)n2, (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), (float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.outerPaint);
        }
        final int thumbSize2 = this.thumbSize;
        canvas.drawCircle((float)(n2 + thumbSize2 / 2), (float)(n + thumbSize2 / 2), (float)(thumbSize2 / 2), this.outerPaint);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        final float n = (float)(int)((this.getMeasuredWidth() - this.thumbSize) * this.progress);
        final int action = motionEvent.getAction();
        float n2 = 0.0f;
        if (action == 0) {
            final int measuredHeight = this.getMeasuredHeight();
            final int thumbSize = this.thumbSize;
            final float n3 = (float)((measuredHeight - thumbSize) / 2);
            if (n - n3 <= x && x <= thumbSize + n + n3 && y >= 0.0f && y <= this.getMeasuredHeight()) {
                this.pressed = true;
                this.thumbDX = (int)(x - n);
                this.getParent().requestDisallowInterceptTouchEvent(true);
                this.invalidate();
                return true;
            }
        }
        else if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
            if (motionEvent.getAction() == 2 && this.pressed) {
                final float n4 = (float)(int)(x - this.thumbDX);
                if (n4 >= 0.0f) {
                    if (n4 > this.getMeasuredWidth() - this.thumbSize) {
                        n2 = (float)(this.getMeasuredWidth() - this.thumbSize);
                    }
                    else {
                        n2 = n4;
                    }
                }
                this.progress = n2 / (this.getMeasuredWidth() - this.thumbSize);
                final PhotoEditorSeekBarDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.onProgressChanged((int)this.getTag(), this.getProgress());
                }
                this.invalidate();
                return true;
            }
        }
        else if (this.pressed) {
            this.pressed = false;
            this.invalidate();
            return true;
        }
        return false;
    }
    
    public void setDelegate(final PhotoEditorSeekBarDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setMinMax(final int minValue, final int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    public void setProgress(final int n) {
        this.setProgress(n, true);
    }
    
    public void setProgress(int n, final boolean b) {
        final int minValue = this.minValue;
        if (n < minValue) {
            n = minValue;
        }
        else {
            final int maxValue = this.maxValue;
            if (n > maxValue) {
                n = maxValue;
            }
        }
        final int minValue2 = this.minValue;
        this.progress = (n - minValue2) / (float)(this.maxValue - minValue2);
        this.invalidate();
        if (b) {
            final PhotoEditorSeekBarDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onProgressChanged((int)this.getTag(), this.getProgress());
            }
        }
    }
    
    public interface PhotoEditorSeekBarDelegate
    {
        void onProgressChanged(final int p0, final int p1);
    }
}
