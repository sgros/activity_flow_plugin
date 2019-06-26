package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.SeekBarView */
public class SeekBarView extends FrameLayout {
    private float bufferedProgress;
    private SeekBarViewDelegate delegate;
    private Paint innerPaint1 = new Paint(1);
    private Paint outerPaint1;
    private boolean pressed;
    private float progressToSet;
    private boolean reportChanges;
    private int thumbDX;
    private int thumbHeight;
    private int thumbWidth;
    private int thumbX;

    /* renamed from: org.telegram.ui.Components.SeekBarView$SeekBarViewDelegate */
    public interface SeekBarViewDelegate {
        void onSeekBarDrag(float f);
    }

    public SeekBarView(Context context) {
        super(context);
        setWillNotDraw(false);
        this.innerPaint1.setColor(Theme.getColor(Theme.key_player_progressBackground));
        this.outerPaint1 = new Paint(1);
        this.outerPaint1.setColor(Theme.getColor(Theme.key_player_progress));
        this.thumbWidth = AndroidUtilities.m26dp(24.0f);
        this.thumbHeight = AndroidUtilities.m26dp(24.0f);
    }

    public void setColors(int i, int i2) {
        this.innerPaint1.setColor(i);
        this.outerPaint1.setColor(i2);
    }

    public void setInnerColor(int i) {
        this.innerPaint1.setColor(i);
    }

    public void setOuterColor(int i) {
        this.outerPaint1.setColor(i);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return onTouch(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return onTouch(motionEvent);
    }

    public void setReportChanges(boolean z) {
        this.reportChanges = z;
    }

    public void setDelegate(SeekBarViewDelegate seekBarViewDelegate) {
        this.delegate = seekBarViewDelegate;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean onTouch(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            getParent().requestDisallowInterceptTouchEvent(true);
            int measuredHeight = (getMeasuredHeight() - this.thumbWidth) / 2;
            if (motionEvent.getY() >= 0.0f && motionEvent.getY() <= ((float) getMeasuredHeight())) {
                if (((float) (this.thumbX - measuredHeight)) > motionEvent.getX() || motionEvent.getX() > ((float) ((this.thumbX + this.thumbWidth) + measuredHeight))) {
                    this.thumbX = ((int) motionEvent.getX()) - (this.thumbWidth / 2);
                    measuredHeight = this.thumbX;
                    if (measuredHeight < 0) {
                        this.thumbX = 0;
                    } else if (measuredHeight > getMeasuredWidth() - this.thumbWidth) {
                        this.thumbX = getMeasuredWidth() - this.thumbWidth;
                    }
                }
                this.thumbDX = (int) (motionEvent.getX() - ((float) this.thumbX));
                this.pressed = true;
                invalidate();
                return true;
            }
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (this.pressed) {
                if (motionEvent.getAction() == 1) {
                    this.delegate.onSeekBarDrag(((float) this.thumbX) / ((float) (getMeasuredWidth() - this.thumbWidth)));
                }
                this.pressed = false;
                invalidate();
                return true;
            }
        } else if (motionEvent.getAction() == 2 && this.pressed) {
            this.thumbX = (int) (motionEvent.getX() - ((float) this.thumbDX));
            int i = this.thumbX;
            if (i < 0) {
                this.thumbX = 0;
            } else if (i > getMeasuredWidth() - this.thumbWidth) {
                this.thumbX = getMeasuredWidth() - this.thumbWidth;
            }
            if (this.reportChanges) {
                this.delegate.onSeekBarDrag(((float) this.thumbX) / ((float) (getMeasuredWidth() - this.thumbWidth)));
            }
            invalidate();
            return true;
        }
        return false;
    }

    public void setProgress(float f) {
        if (getMeasuredWidth() == 0) {
            this.progressToSet = f;
            return;
        }
        this.progressToSet = -1.0f;
        int ceil = (int) Math.ceil((double) (((float) (getMeasuredWidth() - this.thumbWidth)) * f));
        if (this.thumbX != ceil) {
            this.thumbX = ceil;
            ceil = this.thumbX;
            if (ceil < 0) {
                this.thumbX = 0;
            } else if (ceil > getMeasuredWidth() - this.thumbWidth) {
                this.thumbX = getMeasuredWidth() - this.thumbWidth;
            }
            invalidate();
        }
    }

    public void setBufferedProgress(float f) {
        this.bufferedProgress = f;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.progressToSet >= 0.0f && getMeasuredWidth() > 0) {
            setProgress(this.progressToSet);
            this.progressToSet = -1.0f;
        }
    }

    public boolean isDragging() {
        return this.pressed;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int measuredHeight = (getMeasuredHeight() - this.thumbHeight) / 2;
        canvas.drawRect((float) (this.thumbWidth / 2), (float) ((getMeasuredHeight() / 2) - AndroidUtilities.m26dp(1.0f)), (float) (getMeasuredWidth() - (this.thumbWidth / 2)), (float) ((getMeasuredHeight() / 2) + AndroidUtilities.m26dp(1.0f)), this.innerPaint1);
        if (this.bufferedProgress > 0.0f) {
            canvas.drawRect((float) (this.thumbWidth / 2), (float) ((getMeasuredHeight() / 2) - AndroidUtilities.m26dp(1.0f)), ((float) (this.thumbWidth / 2)) + (this.bufferedProgress * ((float) (getMeasuredWidth() - this.thumbWidth))), (float) ((getMeasuredHeight() / 2) + AndroidUtilities.m26dp(1.0f)), this.innerPaint1);
        }
        canvas.drawRect((float) (this.thumbWidth / 2), (float) ((getMeasuredHeight() / 2) - AndroidUtilities.m26dp(1.0f)), (float) ((this.thumbWidth / 2) + this.thumbX), (float) ((getMeasuredHeight() / 2) + AndroidUtilities.m26dp(1.0f)), this.outerPaint1);
        canvas.drawCircle((float) (this.thumbX + (this.thumbWidth / 2)), (float) (measuredHeight + (this.thumbHeight / 2)), (float) AndroidUtilities.m26dp(this.pressed ? 8.0f : 6.0f), this.outerPaint1);
    }
}