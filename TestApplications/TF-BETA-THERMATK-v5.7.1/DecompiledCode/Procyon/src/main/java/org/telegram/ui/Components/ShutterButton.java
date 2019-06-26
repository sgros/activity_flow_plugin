// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.MotionEvent;
import org.telegram.messenger.LocaleController;
import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.view.View;

public class ShutterButton extends View
{
    private static final int LONG_PRESS_TIME = 800;
    private ShutterButtonDelegate delegate;
    private DecelerateInterpolator interpolator;
    private long lastUpdateTime;
    private Runnable longPressed;
    private boolean pressed;
    private boolean processRelease;
    private Paint redPaint;
    private float redProgress;
    private Drawable shadowDrawable;
    private State state;
    private long totalTime;
    private Paint whitePaint;
    
    public ShutterButton(final Context context) {
        super(context);
        this.interpolator = new DecelerateInterpolator();
        this.longPressed = new Runnable() {
            @Override
            public void run() {
                if (ShutterButton.this.delegate != null && !ShutterButton.this.delegate.shutterLongPressed()) {
                    ShutterButton.this.processRelease = false;
                }
            }
        };
        this.shadowDrawable = this.getResources().getDrawable(2131165334);
        (this.whitePaint = new Paint(1)).setStyle(Paint$Style.FILL);
        this.whitePaint.setColor(-1);
        (this.redPaint = new Paint(1)).setStyle(Paint$Style.FILL);
        this.redPaint.setColor(-3324089);
        this.state = State.DEFAULT;
    }
    
    private void setHighlighted(final boolean b) {
        final AnimatorSet set = new AnimatorSet();
        if (b) {
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "scaleX", new float[] { 1.06f }), (Animator)ObjectAnimator.ofFloat((Object)this, "scaleY", new float[] { 1.06f }) });
        }
        else {
            set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, "scaleY", new float[] { 1.0f }) });
            set.setStartDelay(40L);
        }
        set.setDuration(120L);
        set.setInterpolator((TimeInterpolator)this.interpolator);
        set.start();
    }
    
    public ShutterButtonDelegate getDelegate() {
        return this.delegate;
    }
    
    public State getState() {
        return this.state;
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = this.getMeasuredWidth() / 2;
        final int n2 = this.getMeasuredHeight() / 2;
        this.shadowDrawable.setBounds(n - AndroidUtilities.dp(36.0f), n2 - AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f) + n, AndroidUtilities.dp(36.0f) + n2);
        this.shadowDrawable.draw(canvas);
        if (!this.pressed && this.getScaleX() == 1.0f) {
            if (this.redProgress != 0.0f) {
                this.redProgress = 0.0f;
            }
        }
        else {
            final float n3 = (this.getScaleX() - 1.0f) / 0.06f;
            this.whitePaint.setAlpha((int)(255.0f * n3));
            final float n4 = (float)n;
            final float n5 = (float)n2;
            canvas.drawCircle(n4, n5, (float)AndroidUtilities.dp(26.0f), this.whitePaint);
            if (this.state == State.RECORDING) {
                if (this.redProgress != 1.0f) {
                    long abs;
                    if ((abs = Math.abs(System.currentTimeMillis() - this.lastUpdateTime)) > 17L) {
                        abs = 17L;
                    }
                    this.totalTime += abs;
                    if (this.totalTime > 120L) {
                        this.totalTime = 120L;
                    }
                    this.redProgress = this.interpolator.getInterpolation(this.totalTime / 120.0f);
                    this.invalidate();
                }
                canvas.drawCircle(n4, n5, AndroidUtilities.dp(26.0f) * n3 * this.redProgress, this.redPaint);
            }
            else if (this.redProgress != 0.0f) {
                canvas.drawCircle(n4, n5, AndroidUtilities.dp(26.0f) * n3, this.redPaint);
            }
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Button");
        accessibilityNodeInfo.setClickable(true);
        accessibilityNodeInfo.setLongClickable(true);
        if (Build$VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo$AccessibilityAction(AccessibilityNodeInfo$AccessibilityAction.ACTION_CLICK.getId(), (CharSequence)LocaleController.getString("AccActionTakePicture", 2131558411)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo$AccessibilityAction(AccessibilityNodeInfo$AccessibilityAction.ACTION_LONG_CLICK.getId(), (CharSequence)LocaleController.getString("AccActionRecordVideo", 2131558410)));
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(AndroidUtilities.dp(84.0f), AndroidUtilities.dp(84.0f));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final float x = motionEvent.getX();
        final float x2 = motionEvent.getX();
        final int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    if (action == 3) {
                        this.setHighlighted(false);
                        this.pressed = false;
                    }
                }
                else if (x < 0.0f || x2 < 0.0f || x > this.getMeasuredWidth() || x2 > this.getMeasuredHeight()) {
                    AndroidUtilities.cancelRunOnUIThread(this.longPressed);
                    if (this.state == State.RECORDING) {
                        this.setHighlighted(false);
                        this.delegate.shutterCancel();
                        this.setState(State.DEFAULT, true);
                    }
                }
            }
            else {
                this.setHighlighted(false);
                AndroidUtilities.cancelRunOnUIThread(this.longPressed);
                if (this.processRelease && x >= 0.0f && x2 >= 0.0f && x <= this.getMeasuredWidth() && x2 <= this.getMeasuredHeight()) {
                    this.delegate.shutterReleased();
                }
            }
        }
        else {
            AndroidUtilities.runOnUIThread(this.longPressed, 800L);
            this.pressed = true;
            this.setHighlighted(this.processRelease = true);
        }
        return true;
    }
    
    public void setDelegate(final ShutterButtonDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setScaleX(final float scaleX) {
        super.setScaleX(scaleX);
        this.invalidate();
    }
    
    public void setState(final State state, final boolean b) {
        if (this.state != state) {
            this.state = state;
            if (b) {
                this.lastUpdateTime = System.currentTimeMillis();
                this.totalTime = 0L;
                if (this.state != State.RECORDING) {
                    this.redProgress = 0.0f;
                }
            }
            else if (this.state == State.RECORDING) {
                this.redProgress = 1.0f;
            }
            else {
                this.redProgress = 0.0f;
            }
            this.invalidate();
        }
    }
    
    public interface ShutterButtonDelegate
    {
        void shutterCancel();
        
        boolean shutterLongPressed();
        
        void shutterReleased();
    }
    
    public enum State
    {
        DEFAULT, 
        RECORDING;
    }
}
