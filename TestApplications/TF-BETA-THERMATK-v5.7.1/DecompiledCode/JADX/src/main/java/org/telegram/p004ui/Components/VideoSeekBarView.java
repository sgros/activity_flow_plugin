package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;

/* renamed from: org.telegram.ui.Components.VideoSeekBarView */
public class VideoSeekBarView extends View {
    private SeekBarDelegate delegate;
    private Paint paint = new Paint();
    private Paint paint2 = new Paint(1);
    private boolean pressed = false;
    private float progress = 0.0f;
    private int thumbDX = 0;
    private int thumbHeight = AndroidUtilities.m26dp(12.0f);
    private int thumbWidth = AndroidUtilities.m26dp(12.0f);

    /* renamed from: org.telegram.ui.Components.VideoSeekBarView$SeekBarDelegate */
    public interface SeekBarDelegate {
        void onSeekBarDrag(float f);
    }

    public VideoSeekBarView(Context context) {
        super(context);
        this.paint.setColor(-10724260);
        this.paint2.setColor(-1);
    }

    public void setDelegate(SeekBarDelegate seekBarDelegate) {
        this.delegate = seekBarDelegate;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float measuredWidth = (float) ((int) (((float) (getMeasuredWidth() - this.thumbWidth)) * this.progress));
        float f = 0.0f;
        float f2;
        if (motionEvent.getAction() == 0) {
            int measuredHeight = getMeasuredHeight();
            int i = this.thumbWidth;
            f2 = (float) ((measuredHeight - i) / 2);
            if (measuredWidth - f2 <= x && x <= (((float) i) + measuredWidth) + f2 && y >= 0.0f && y <= ((float) getMeasuredHeight())) {
                this.pressed = true;
                this.thumbDX = (int) (x - measuredWidth);
                getParent().requestDisallowInterceptTouchEvent(true);
                invalidate();
                return true;
            }
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (this.pressed) {
                if (motionEvent.getAction() == 1) {
                    SeekBarDelegate seekBarDelegate = this.delegate;
                    if (seekBarDelegate != null) {
                        seekBarDelegate.onSeekBarDrag(measuredWidth / ((float) (getMeasuredWidth() - this.thumbWidth)));
                    }
                }
                this.pressed = false;
                invalidate();
                return true;
            }
        } else if (motionEvent.getAction() == 2 && this.pressed) {
            f2 = (float) ((int) (x - ((float) this.thumbDX)));
            if (f2 >= 0.0f) {
                f = f2 > ((float) (getMeasuredWidth() - this.thumbWidth)) ? (float) (getMeasuredWidth() - this.thumbWidth) : f2;
            }
            this.progress = f / ((float) (getMeasuredWidth() - this.thumbWidth));
            invalidate();
            return true;
        }
        return false;
    }

    public void setProgress(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        this.progress = f;
        invalidate();
    }

    public float getProgress() {
        return this.progress;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int measuredHeight = (getMeasuredHeight() - this.thumbHeight) / 2;
        int measuredWidth = getMeasuredWidth();
        int i = this.thumbWidth;
        measuredWidth = (int) (((float) (measuredWidth - i)) * this.progress);
        canvas.drawRect((float) (i / 2), (float) ((getMeasuredHeight() / 2) - AndroidUtilities.m26dp(1.0f)), (float) (getMeasuredWidth() - (this.thumbWidth / 2)), (float) ((getMeasuredHeight() / 2) + AndroidUtilities.m26dp(1.0f)), this.paint);
        i = this.thumbWidth;
        canvas.drawCircle((float) (measuredWidth + (i / 2)), (float) (measuredHeight + (this.thumbHeight / 2)), (float) (i / 2), this.paint2);
    }
}