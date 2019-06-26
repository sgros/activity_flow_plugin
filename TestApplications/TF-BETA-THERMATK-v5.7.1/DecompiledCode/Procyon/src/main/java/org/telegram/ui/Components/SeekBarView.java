// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.MotionEvent;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class SeekBarView extends FrameLayout
{
    private float bufferedProgress;
    private SeekBarViewDelegate delegate;
    private Paint innerPaint1;
    private Paint outerPaint1;
    private boolean pressed;
    private float progressToSet;
    private boolean reportChanges;
    private int thumbDX;
    private int thumbHeight;
    private int thumbWidth;
    private int thumbX;
    
    public SeekBarView(final Context context) {
        super(context);
        this.setWillNotDraw(false);
        (this.innerPaint1 = new Paint(1)).setColor(Theme.getColor("player_progressBackground"));
        (this.outerPaint1 = new Paint(1)).setColor(Theme.getColor("player_progress"));
        this.thumbWidth = AndroidUtilities.dp(24.0f);
        this.thumbHeight = AndroidUtilities.dp(24.0f);
    }
    
    public boolean isDragging() {
        return this.pressed;
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = (this.getMeasuredHeight() - this.thumbHeight) / 2;
        canvas.drawRect((float)(this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), (float)(this.getMeasuredWidth() - this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.innerPaint1);
        if (this.bufferedProgress > 0.0f) {
            canvas.drawRect((float)(this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), this.thumbWidth / 2 + this.bufferedProgress * (this.getMeasuredWidth() - this.thumbWidth), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.innerPaint1);
        }
        canvas.drawRect((float)(this.thumbWidth / 2), (float)(this.getMeasuredHeight() / 2 - AndroidUtilities.dp(1.0f)), (float)(this.thumbWidth / 2 + this.thumbX), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(1.0f)), this.outerPaint1);
        final float n2 = (float)(this.thumbX + this.thumbWidth / 2);
        final float n3 = (float)(n + this.thumbHeight / 2);
        float n4;
        if (this.pressed) {
            n4 = 8.0f;
        }
        else {
            n4 = 6.0f;
        }
        canvas.drawCircle(n2, n3, (float)AndroidUtilities.dp(n4), this.outerPaint1);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return this.onTouch(motionEvent);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        if (this.progressToSet >= 0.0f && this.getMeasuredWidth() > 0) {
            this.setProgress(this.progressToSet);
            this.progressToSet = -1.0f;
        }
    }
    
    boolean onTouch(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            final int n = (this.getMeasuredHeight() - this.thumbWidth) / 2;
            if (motionEvent.getY() >= 0.0f && motionEvent.getY() <= this.getMeasuredHeight()) {
                if (this.thumbX - n > motionEvent.getX() || motionEvent.getX() > this.thumbX + this.thumbWidth + n) {
                    this.thumbX = (int)motionEvent.getX() - this.thumbWidth / 2;
                    final int thumbX = this.thumbX;
                    if (thumbX < 0) {
                        this.thumbX = 0;
                    }
                    else if (thumbX > this.getMeasuredWidth() - this.thumbWidth) {
                        this.thumbX = this.getMeasuredWidth() - this.thumbWidth;
                    }
                }
                this.thumbDX = (int)(motionEvent.getX() - this.thumbX);
                this.pressed = true;
                this.invalidate();
                return true;
            }
        }
        else if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
            if (motionEvent.getAction() == 2 && this.pressed) {
                this.thumbX = (int)(motionEvent.getX() - this.thumbDX);
                final int thumbX2 = this.thumbX;
                if (thumbX2 < 0) {
                    this.thumbX = 0;
                }
                else if (thumbX2 > this.getMeasuredWidth() - this.thumbWidth) {
                    this.thumbX = this.getMeasuredWidth() - this.thumbWidth;
                }
                if (this.reportChanges) {
                    this.delegate.onSeekBarDrag(this.thumbX / (float)(this.getMeasuredWidth() - this.thumbWidth));
                }
                this.invalidate();
                return true;
            }
        }
        else if (this.pressed) {
            if (motionEvent.getAction() == 1) {
                this.delegate.onSeekBarDrag(this.thumbX / (float)(this.getMeasuredWidth() - this.thumbWidth));
            }
            this.pressed = false;
            this.invalidate();
            return true;
        }
        return false;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.onTouch(motionEvent);
    }
    
    public void setBufferedProgress(final float bufferedProgress) {
        this.bufferedProgress = bufferedProgress;
    }
    
    public void setColors(final int color, final int color2) {
        this.innerPaint1.setColor(color);
        this.outerPaint1.setColor(color2);
    }
    
    public void setDelegate(final SeekBarViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setInnerColor(final int color) {
        this.innerPaint1.setColor(color);
    }
    
    public void setOuterColor(final int color) {
        this.outerPaint1.setColor(color);
    }
    
    public void setProgress(final float progressToSet) {
        if (this.getMeasuredWidth() == 0) {
            this.progressToSet = progressToSet;
            return;
        }
        this.progressToSet = -1.0f;
        final int thumbX = (int)Math.ceil((this.getMeasuredWidth() - this.thumbWidth) * progressToSet);
        if (this.thumbX != thumbX) {
            this.thumbX = thumbX;
            final int thumbX2 = this.thumbX;
            if (thumbX2 < 0) {
                this.thumbX = 0;
            }
            else if (thumbX2 > this.getMeasuredWidth() - this.thumbWidth) {
                this.thumbX = this.getMeasuredWidth() - this.thumbWidth;
            }
            this.invalidate();
        }
    }
    
    public void setReportChanges(final boolean reportChanges) {
        this.reportChanges = reportChanges;
    }
    
    public interface SeekBarViewDelegate
    {
        void onSeekBarDrag(final float p0);
    }
}
