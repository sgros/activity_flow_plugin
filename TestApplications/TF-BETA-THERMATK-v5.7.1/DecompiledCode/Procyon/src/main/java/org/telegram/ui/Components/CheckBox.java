// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Bitmap$Config;
import androidx.annotation.Keep;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.view.accessibility.AccessibilityNodeInfo;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.content.Context;
import android.text.TextPaint;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class CheckBox extends View
{
    private static Paint backgroundPaint;
    private static Paint checkPaint;
    private static Paint eraser;
    private static Paint eraser2;
    private static Paint paint;
    private static final float progressBounceDiff = 0.2f;
    private boolean attachedToWindow;
    private Canvas bitmapCanvas;
    private ObjectAnimator checkAnimator;
    private Bitmap checkBitmap;
    private Canvas checkCanvas;
    private Drawable checkDrawable;
    private int checkOffset;
    private String checkedText;
    private int color;
    private boolean drawBackground;
    private Bitmap drawBitmap;
    private boolean hasBorder;
    private boolean isCheckAnimation;
    private boolean isChecked;
    private float progress;
    private int size;
    private TextPaint textPaint;
    
    public CheckBox(final Context context, final int n) {
        super(context);
        this.isCheckAnimation = true;
        this.size = 22;
        if (CheckBox.paint == null) {
            CheckBox.paint = new Paint(1);
            (CheckBox.eraser = new Paint(1)).setColor(0);
            CheckBox.eraser.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            (CheckBox.eraser2 = new Paint(1)).setColor(0);
            CheckBox.eraser2.setStyle(Paint$Style.STROKE);
            CheckBox.eraser2.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            (CheckBox.backgroundPaint = new Paint(1)).setColor(-1);
            CheckBox.backgroundPaint.setStyle(Paint$Style.STROKE);
        }
        CheckBox.eraser2.setStrokeWidth((float)AndroidUtilities.dp(28.0f));
        CheckBox.backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        (this.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(18.0f));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.checkDrawable = context.getResources().getDrawable(n).mutate();
    }
    
    private void animateToCheckedState(final boolean isCheckAnimation) {
        this.isCheckAnimation = isCheckAnimation;
        float n;
        if (isCheckAnimation) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.checkAnimator = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { n })).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(CheckBox.this.checkAnimator)) {
                    CheckBox.this.checkAnimator = null;
                }
                if (!CheckBox.this.isChecked) {
                    CheckBox.this.checkedText = null;
                }
            }
        });
        this.checkAnimator.setDuration(300L);
        this.checkAnimator.start();
    }
    
    private void cancelCheckAnimator() {
        final ObjectAnimator checkAnimator = this.checkAnimator;
        if (checkAnimator != null) {
            checkAnimator.cancel();
            this.checkAnimator = null;
        }
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    public boolean isChecked() {
        return this.isChecked;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.getVisibility() == 0 && this.drawBitmap != null) {
            if (this.checkBitmap != null) {
                if (this.drawBackground || this.progress != 0.0f) {
                    CheckBox.eraser2.setStrokeWidth((float)AndroidUtilities.dp((float)(this.size + 6)));
                    this.drawBitmap.eraseColor(0);
                    final float n = (float)(this.getMeasuredWidth() / 2);
                    final float progress = this.progress;
                    float n2;
                    if (progress >= 0.5f) {
                        n2 = 1.0f;
                    }
                    else {
                        n2 = progress / 0.5f;
                    }
                    final float progress2 = this.progress;
                    float n3;
                    if (progress2 < 0.5f) {
                        n3 = 0.0f;
                    }
                    else {
                        n3 = (progress2 - 0.5f) / 0.5f;
                    }
                    float progress3;
                    if (this.isCheckAnimation) {
                        progress3 = this.progress;
                    }
                    else {
                        progress3 = 1.0f - this.progress;
                    }
                    float n5 = 0.0f;
                    Label_0210: {
                        float n4;
                        if (progress3 < 0.2f) {
                            n4 = AndroidUtilities.dp(2.0f) * progress3 / 0.2f;
                        }
                        else {
                            n5 = n;
                            if (progress3 >= 0.4f) {
                                break Label_0210;
                            }
                            n4 = AndroidUtilities.dp(2.0f) - AndroidUtilities.dp(2.0f) * (progress3 - 0.2f) / 0.2f;
                        }
                        n5 = n - n4;
                    }
                    if (this.drawBackground) {
                        CheckBox.paint.setColor(1140850688);
                        canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), n5 - AndroidUtilities.dp(1.0f), CheckBox.paint);
                        canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), n5 - AndroidUtilities.dp(1.0f), CheckBox.backgroundPaint);
                    }
                    CheckBox.paint.setColor(this.color);
                    float n6 = n5;
                    if (this.hasBorder) {
                        n6 = n5 - AndroidUtilities.dp(2.0f);
                    }
                    this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), n6, CheckBox.paint);
                    this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), n6 * (1.0f - n2), CheckBox.eraser);
                    canvas.drawBitmap(this.drawBitmap, 0.0f, 0.0f, (Paint)null);
                    this.checkBitmap.eraseColor(0);
                    final String checkedText = this.checkedText;
                    if (checkedText != null) {
                        final int n7 = (int)Math.ceil(this.textPaint.measureText(checkedText));
                        final Canvas checkCanvas = this.checkCanvas;
                        final String checkedText2 = this.checkedText;
                        final float n8 = (float)((this.getMeasuredWidth() - n7) / 2);
                        float n9;
                        if (this.size == 40) {
                            n9 = 28.0f;
                        }
                        else {
                            n9 = 21.0f;
                        }
                        checkCanvas.drawText(checkedText2, n8, (float)AndroidUtilities.dp(n9), (Paint)this.textPaint);
                    }
                    else {
                        final int intrinsicWidth = this.checkDrawable.getIntrinsicWidth();
                        final int intrinsicHeight = this.checkDrawable.getIntrinsicHeight();
                        final int n10 = (this.getMeasuredWidth() - intrinsicWidth) / 2;
                        final int n11 = (this.getMeasuredHeight() - intrinsicHeight) / 2;
                        final Drawable checkDrawable = this.checkDrawable;
                        final int checkOffset = this.checkOffset;
                        checkDrawable.setBounds(n10, n11 + checkOffset, intrinsicWidth + n10, n11 + intrinsicHeight + checkOffset);
                        this.checkDrawable.draw(this.checkCanvas);
                    }
                    this.checkCanvas.drawCircle((float)(this.getMeasuredWidth() / 2 - AndroidUtilities.dp(2.5f)), (float)(this.getMeasuredHeight() / 2 + AndroidUtilities.dp(4.0f)), (this.getMeasuredWidth() + AndroidUtilities.dp(6.0f)) / 2 * (1.0f - n3), CheckBox.eraser2);
                    canvas.drawBitmap(this.checkBitmap, 0.0f, 0.0f, (Paint)null);
                }
            }
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.CheckBox");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.isChecked);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
    }
    
    public void setBackgroundColor(final int color) {
        this.color = color;
        this.invalidate();
    }
    
    public void setCheckColor(final int color) {
        this.checkDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(color, PorterDuff$Mode.MULTIPLY));
        this.textPaint.setColor(color);
        this.invalidate();
    }
    
    public void setCheckOffset(final int checkOffset) {
        this.checkOffset = checkOffset;
    }
    
    public void setChecked(final int n, final boolean isChecked, final boolean b) {
        if (n >= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(n + 1);
            this.checkedText = sb.toString();
            this.invalidate();
        }
        if (isChecked == this.isChecked) {
            return;
        }
        this.isChecked = isChecked;
        if (this.attachedToWindow && b) {
            this.animateToCheckedState(isChecked);
        }
        else {
            this.cancelCheckAnimator();
            float progress;
            if (isChecked) {
                progress = 1.0f;
            }
            else {
                progress = 0.0f;
            }
            this.setProgress(progress);
        }
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.setChecked(-1, b, b2);
    }
    
    public void setColor(final int color, final int color2) {
        this.color = color;
        this.checkDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(color2, PorterDuff$Mode.MULTIPLY));
        this.textPaint.setColor(color2);
        this.invalidate();
    }
    
    public void setDrawBackground(final boolean drawBackground) {
        this.drawBackground = drawBackground;
    }
    
    public void setHasBorder(final boolean hasBorder) {
        this.hasBorder = hasBorder;
    }
    
    public void setNum(final int n) {
        if (n >= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(n + 1);
            this.checkedText = sb.toString();
        }
        else if (this.checkAnimator == null) {
            this.checkedText = null;
        }
        this.invalidate();
    }
    
    @Keep
    public void setProgress(final float progress) {
        if (this.progress == progress) {
            return;
        }
        this.progress = progress;
        this.invalidate();
    }
    
    public void setSize(final int size) {
        this.size = size;
        if (size == 40) {
            this.textPaint.setTextSize((float)AndroidUtilities.dp(24.0f));
        }
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        if (visibility != 0 || this.drawBitmap != null) {
            return;
        }
        try {
            this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp((float)this.size), AndroidUtilities.dp((float)this.size), Bitmap$Config.ARGB_4444);
            this.bitmapCanvas = new Canvas(this.drawBitmap);
            this.checkBitmap = Bitmap.createBitmap(AndroidUtilities.dp((float)this.size), AndroidUtilities.dp((float)this.size), Bitmap$Config.ARGB_4444);
            this.checkCanvas = new Canvas(this.checkBitmap);
        }
        catch (Throwable t) {}
    }
}
