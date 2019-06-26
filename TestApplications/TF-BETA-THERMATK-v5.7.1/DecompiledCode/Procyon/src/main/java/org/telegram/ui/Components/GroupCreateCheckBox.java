// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.Theme;
import androidx.annotation.Keep;
import android.graphics.Bitmap$Config;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.content.Context;
import android.graphics.Bitmap;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class GroupCreateCheckBox extends View
{
    private static Paint eraser;
    private static Paint eraser2;
    private static final float progressBounceDiff = 0.2f;
    private boolean attachedToWindow;
    private Paint backgroundInnerPaint;
    private String backgroundKey;
    private Paint backgroundPaint;
    private Canvas bitmapCanvas;
    private ObjectAnimator checkAnimator;
    private String checkKey;
    private Paint checkPaint;
    private float checkScale;
    private Bitmap drawBitmap;
    private String innerKey;
    private int innerRadDiff;
    private boolean isCheckAnimation;
    private boolean isChecked;
    private float progress;
    
    public GroupCreateCheckBox(final Context context) {
        super(context);
        this.isCheckAnimation = true;
        this.checkScale = 1.0f;
        this.backgroundKey = "checkboxCheck";
        this.checkKey = "checkboxCheck";
        this.innerKey = "checkbox";
        if (GroupCreateCheckBox.eraser == null) {
            (GroupCreateCheckBox.eraser = new Paint(1)).setColor(0);
            GroupCreateCheckBox.eraser.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            (GroupCreateCheckBox.eraser2 = new Paint(1)).setColor(0);
            GroupCreateCheckBox.eraser2.setStyle(Paint$Style.STROKE);
            GroupCreateCheckBox.eraser2.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
        }
        this.backgroundPaint = new Paint(1);
        this.backgroundInnerPaint = new Paint(1);
        (this.checkPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.innerRadDiff = AndroidUtilities.dp(2.0f);
        this.checkPaint.setStrokeWidth((float)AndroidUtilities.dp(1.5f));
        GroupCreateCheckBox.eraser2.setStrokeWidth((float)AndroidUtilities.dp(28.0f));
        this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f), Bitmap$Config.ARGB_4444);
        this.bitmapCanvas = new Canvas(this.drawBitmap);
        this.updateColors();
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
        (this.checkAnimator = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { n })).setDuration(300L);
        this.checkAnimator.start();
    }
    
    private void cancelCheckAnimator() {
        final ObjectAnimator checkAnimator = this.checkAnimator;
        if (checkAnimator != null) {
            checkAnimator.cancel();
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
        this.updateColors();
        this.attachedToWindow = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.getVisibility() != 0) {
            return;
        }
        if (this.progress != 0.0f) {
            final int n = this.getMeasuredWidth() / 2;
            final int n2 = this.getMeasuredHeight() / 2;
            GroupCreateCheckBox.eraser2.setStrokeWidth((float)AndroidUtilities.dp(30.0f));
            this.drawBitmap.eraseColor(0);
            final float progress = this.progress;
            float n3;
            if (progress >= 0.5f) {
                n3 = 1.0f;
            }
            else {
                n3 = progress / 0.5f;
            }
            final float progress2 = this.progress;
            float n4;
            if (progress2 < 0.5f) {
                n4 = 0.0f;
            }
            else {
                n4 = (progress2 - 0.5f) / 0.5f;
            }
            float progress3;
            if (this.isCheckAnimation) {
                progress3 = this.progress;
            }
            else {
                progress3 = 1.0f - this.progress;
            }
            float n5;
            if (progress3 < 0.2f) {
                n5 = AndroidUtilities.dp(2.0f) * progress3 / 0.2f;
            }
            else if (progress3 < 0.4f) {
                n5 = AndroidUtilities.dp(2.0f) - AndroidUtilities.dp(2.0f) * (progress3 - 0.2f) / 0.2f;
            }
            else {
                n5 = 0.0f;
            }
            if (n4 != 0.0f) {
                canvas.drawCircle((float)n, (float)n2, n - AndroidUtilities.dp(2.0f) + AndroidUtilities.dp(2.0f) * n4 - n5, this.backgroundPaint);
            }
            final float n6 = n - this.innerRadDiff - n5;
            final Canvas bitmapCanvas = this.bitmapCanvas;
            final float n7 = (float)n;
            final float n8 = (float)n2;
            bitmapCanvas.drawCircle(n7, n8, n6, this.backgroundInnerPaint);
            this.bitmapCanvas.drawCircle(n7, n8, n6 * (1.0f - n3), GroupCreateCheckBox.eraser);
            canvas.drawBitmap(this.drawBitmap, 0.0f, 0.0f, (Paint)null);
            final float n9 = AndroidUtilities.dp(10.0f) * n4 * this.checkScale;
            final float n10 = AndroidUtilities.dp(5.0f) * n4 * this.checkScale;
            final int n11 = n - AndroidUtilities.dp(1.0f);
            final int dp = AndroidUtilities.dp(4.0f);
            final float n12 = (float)Math.sqrt(n10 * n10 / 2.0f);
            final float n13 = (float)n11;
            final float n14 = (float)(n2 + dp);
            canvas.drawLine(n13, n14, n13 - n12, n14 - n12, this.checkPaint);
            final float n15 = (float)Math.sqrt(n9 * n9 / 2.0f);
            final float n16 = (float)(n11 - AndroidUtilities.dp(1.2f));
            canvas.drawLine(n16, n14, n16 + n15, n14 - n15, this.checkPaint);
        }
    }
    
    public void setCheckScale(final float checkScale) {
        this.checkScale = checkScale;
    }
    
    public void setChecked(final boolean isChecked, final boolean b) {
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
    
    public void setColorKeysOverrides(final String checkKey, final String innerKey, final String backgroundKey) {
        this.checkKey = checkKey;
        this.innerKey = innerKey;
        this.backgroundKey = backgroundKey;
        this.updateColors();
    }
    
    public void setInnerRadDiff(final int innerRadDiff) {
        this.innerRadDiff = innerRadDiff;
    }
    
    @Keep
    public void setProgress(final float progress) {
        if (this.progress == progress) {
            return;
        }
        this.progress = progress;
        this.invalidate();
    }
    
    public void updateColors() {
        this.backgroundInnerPaint.setColor(Theme.getColor(this.innerKey));
        this.backgroundPaint.setColor(Theme.getColor(this.backgroundKey));
        this.checkPaint.setColor(Theme.getColor(this.checkKey));
        this.invalidate();
    }
}
