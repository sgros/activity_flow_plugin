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

public class VideoSeekBarView extends View
{
    private SeekBarDelegate delegate;
    private Paint paint;
    private Paint paint2;
    private boolean pressed;
    private float progress;
    private int thumbDX;
    private int thumbHeight;
    private int thumbWidth;
    
    public VideoSeekBarView(final Context context) {
        super(context);
        this.paint = new Paint();
        this.paint2 = new Paint(1);
        this.thumbWidth = AndroidUtilities.dp(12.0f);
        this.thumbHeight = AndroidUtilities.dp(12.0f);
        this.thumbDX = 0;
        this.progress = 0.0f;
        this.pressed = false;
        this.paint.setColor(-10724260);
        this.paint2.setColor(-1);
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = (this.getMeasuredHeight() - this.thumbHeight) / 2;
        final int measuredWidth = this.getMeasuredWidth();
        final int thumbWidth = this.thumbWidth;
        final int n2 = (int)((measuredWidth - thumbWidth) * this.progress);
        canvas.drawRect((float)(thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), (float)(this.getMeasuredWidth() - this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.paint);
        final int thumbWidth2 = this.thumbWidth;
        canvas.drawCircle((float)(n2 + thumbWidth2 / 2), (float)(n + this.thumbHeight / 2), (float)(thumbWidth2 / 2), this.paint2);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        final float n = (float)(int)((this.getMeasuredWidth() - this.thumbWidth) * this.progress);
        final int action = motionEvent.getAction();
        float n2 = 0.0f;
        if (action == 0) {
            final int measuredHeight = this.getMeasuredHeight();
            final int thumbWidth = this.thumbWidth;
            final float n3 = (float)((measuredHeight - thumbWidth) / 2);
            if (n - n3 <= x && x <= thumbWidth + n + n3 && y >= 0.0f && y <= this.getMeasuredHeight()) {
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
                    if (n4 > this.getMeasuredWidth() - this.thumbWidth) {
                        n2 = (float)(this.getMeasuredWidth() - this.thumbWidth);
                    }
                    else {
                        n2 = n4;
                    }
                }
                this.progress = n2 / (this.getMeasuredWidth() - this.thumbWidth);
                this.invalidate();
                return true;
            }
        }
        else if (this.pressed) {
            if (motionEvent.getAction() == 1) {
                final SeekBarDelegate delegate = this.delegate;
                if (delegate != null) {
                    delegate.onSeekBarDrag(n / (this.getMeasuredWidth() - this.thumbWidth));
                }
            }
            this.pressed = false;
            this.invalidate();
            return true;
        }
        return false;
    }
    
    public void setDelegate(final SeekBarDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setProgress(final float n) {
        float progress;
        if (n < 0.0f) {
            progress = 0.0f;
        }
        else {
            progress = n;
            if (n > 1.0f) {
                progress = 1.0f;
            }
        }
        this.progress = progress;
        this.invalidate();
    }
    
    public interface SeekBarDelegate
    {
        void onSeekBarDrag(final float p0);
    }
}
