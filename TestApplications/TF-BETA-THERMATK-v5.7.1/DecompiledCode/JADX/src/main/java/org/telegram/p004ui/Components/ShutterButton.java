package org.telegram.p004ui.Components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;

/* renamed from: org.telegram.ui.Components.ShutterButton */
public class ShutterButton extends View {
    private static final int LONG_PRESS_TIME = 800;
    private ShutterButtonDelegate delegate;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private long lastUpdateTime;
    private Runnable longPressed = new C29431();
    private boolean pressed;
    private boolean processRelease;
    private Paint redPaint;
    private float redProgress;
    private Drawable shadowDrawable = getResources().getDrawable(C1067R.C1065drawable.camera_btn);
    private State state;
    private long totalTime;
    private Paint whitePaint = new Paint(1);

    /* renamed from: org.telegram.ui.Components.ShutterButton$1 */
    class C29431 implements Runnable {
        C29431() {
        }

        public void run() {
            if (ShutterButton.this.delegate != null && !ShutterButton.this.delegate.shutterLongPressed()) {
                ShutterButton.this.processRelease = false;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.ShutterButton$ShutterButtonDelegate */
    public interface ShutterButtonDelegate {
        void shutterCancel();

        boolean shutterLongPressed();

        void shutterReleased();
    }

    /* renamed from: org.telegram.ui.Components.ShutterButton$State */
    public enum State {
        DEFAULT,
        RECORDING
    }

    public ShutterButton(Context context) {
        super(context);
        this.whitePaint.setStyle(Style.FILL);
        this.whitePaint.setColor(-1);
        this.redPaint = new Paint(1);
        this.redPaint.setStyle(Style.FILL);
        this.redPaint.setColor(-3324089);
        this.state = State.DEFAULT;
    }

    public void setDelegate(ShutterButtonDelegate shutterButtonDelegate) {
        this.delegate = shutterButtonDelegate;
    }

    public ShutterButtonDelegate getDelegate() {
        return this.delegate;
    }

    private void setHighlighted(boolean z) {
        AnimatorSet animatorSet = new AnimatorSet();
        String str = "scaleY";
        String str2 = "scaleX";
        Animator[] animatorArr;
        if (z) {
            animatorArr = new Animator[2];
            animatorArr[0] = ObjectAnimator.ofFloat(this, str2, new float[]{1.06f});
            animatorArr[1] = ObjectAnimator.ofFloat(this, str, new float[]{1.06f});
            animatorSet.playTogether(animatorArr);
        } else {
            animatorArr = new Animator[2];
            animatorArr[0] = ObjectAnimator.ofFloat(this, str2, new float[]{1.0f});
            animatorArr[1] = ObjectAnimator.ofFloat(this, str, new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
            animatorSet.setStartDelay(40);
        }
        animatorSet.setDuration(120);
        animatorSet.setInterpolator(this.interpolator);
        animatorSet.start();
    }

    public void setScaleX(float f) {
        super.setScaleX(f);
        invalidate();
    }

    public State getState() {
        return this.state;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth() / 2;
        int measuredHeight = getMeasuredHeight() / 2;
        this.shadowDrawable.setBounds(measuredWidth - AndroidUtilities.m26dp(36.0f), measuredHeight - AndroidUtilities.m26dp(36.0f), AndroidUtilities.m26dp(36.0f) + measuredWidth, AndroidUtilities.m26dp(36.0f) + measuredHeight);
        this.shadowDrawable.draw(canvas);
        if (this.pressed || getScaleX() != 1.0f) {
            float scaleX = (getScaleX() - 1.0f) / 0.06f;
            this.whitePaint.setAlpha((int) (255.0f * scaleX));
            float f = (float) measuredWidth;
            float f2 = (float) measuredHeight;
            canvas.drawCircle(f, f2, (float) AndroidUtilities.m26dp(26.0f), this.whitePaint);
            if (this.state == State.RECORDING) {
                if (this.redProgress != 1.0f) {
                    long abs = Math.abs(System.currentTimeMillis() - this.lastUpdateTime);
                    if (abs > 17) {
                        abs = 17;
                    }
                    this.totalTime += abs;
                    if (this.totalTime > 120) {
                        this.totalTime = 120;
                    }
                    this.redProgress = this.interpolator.getInterpolation(((float) this.totalTime) / 120.0f);
                    invalidate();
                }
                canvas.drawCircle(f, f2, (((float) AndroidUtilities.m26dp(26.0f)) * scaleX) * this.redProgress, this.redPaint);
            } else if (this.redProgress != 0.0f) {
                canvas.drawCircle(f, f2, ((float) AndroidUtilities.m26dp(26.0f)) * scaleX, this.redPaint);
            }
        } else if (this.redProgress != 0.0f) {
            this.redProgress = 0.0f;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(AndroidUtilities.m26dp(84.0f), AndroidUtilities.m26dp(84.0f));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float x2 = motionEvent.getX();
        int action = motionEvent.getAction();
        if (action == 0) {
            AndroidUtilities.runOnUIThread(this.longPressed, 800);
            this.pressed = true;
            this.processRelease = true;
            setHighlighted(true);
        } else if (action == 1) {
            setHighlighted(false);
            AndroidUtilities.cancelRunOnUIThread(this.longPressed);
            if (this.processRelease && x >= 0.0f && x2 >= 0.0f && x <= ((float) getMeasuredWidth()) && x2 <= ((float) getMeasuredHeight())) {
                this.delegate.shutterReleased();
            }
        } else if (action != 2) {
            if (action == 3) {
                setHighlighted(false);
                this.pressed = false;
            }
        } else if (x < 0.0f || x2 < 0.0f || x > ((float) getMeasuredWidth()) || x2 > ((float) getMeasuredHeight())) {
            AndroidUtilities.cancelRunOnUIThread(this.longPressed);
            if (this.state == State.RECORDING) {
                setHighlighted(false);
                this.delegate.shutterCancel();
                setState(State.DEFAULT, true);
            }
        }
        return true;
    }

    public void setState(State state, boolean z) {
        if (this.state != state) {
            this.state = state;
            if (z) {
                this.lastUpdateTime = System.currentTimeMillis();
                this.totalTime = 0;
                if (this.state != State.RECORDING) {
                    this.redProgress = 0.0f;
                }
            } else if (this.state == State.RECORDING) {
                this.redProgress = 1.0f;
            } else {
                this.redProgress = 0.0f;
            }
            invalidate();
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.Button");
        accessibilityNodeInfo.setClickable(true);
        accessibilityNodeInfo.setLongClickable(true);
        if (VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo.addAction(new AccessibilityAction(AccessibilityAction.ACTION_CLICK.getId(), LocaleController.getString("AccActionTakePicture", C1067R.string.AccActionTakePicture)));
            accessibilityNodeInfo.addAction(new AccessibilityAction(AccessibilityAction.ACTION_LONG_CLICK.getId(), LocaleController.getString("AccActionRecordVideo", C1067R.string.AccActionRecordVideo)));
        }
    }
}