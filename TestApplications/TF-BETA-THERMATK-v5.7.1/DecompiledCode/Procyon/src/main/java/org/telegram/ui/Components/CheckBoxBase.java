// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.annotation.Keep;
import android.graphics.Color;
import org.telegram.ui.ActionBar.Theme;
import android.animation.TimeInterpolator;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap$Config;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Join;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.RectF;
import android.graphics.Path;
import android.view.View;
import android.graphics.Bitmap;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Paint;

public class CheckBoxBase
{
    private static Paint backgroundPaint;
    private static Paint checkPaint;
    private static Paint eraser;
    private static Paint paint;
    private boolean attachedToWindow;
    private String background2ColorKey;
    private float backgroundAlpha;
    private String backgroundColorKey;
    private Canvas bitmapCanvas;
    private Rect bounds;
    private ObjectAnimator checkAnimator;
    private String checkColorKey;
    private int drawBackgroundAsArc;
    private Bitmap drawBitmap;
    private boolean drawUnchecked;
    private boolean enabled;
    private boolean isChecked;
    private View parentView;
    private Path path;
    private float progress;
    private ProgressDelegate progressDelegate;
    private RectF rect;
    private float size;
    private boolean useDefaultCheck;
    
    public CheckBoxBase(final View parentView) {
        this.bounds = new Rect();
        this.rect = new RectF();
        this.path = new Path();
        this.enabled = true;
        this.backgroundAlpha = 1.0f;
        this.checkColorKey = "checkboxCheck";
        this.backgroundColorKey = "chat_serviceBackground";
        this.background2ColorKey = "chat_serviceBackground";
        this.drawUnchecked = true;
        this.size = 21.0f;
        this.parentView = parentView;
        if (CheckBoxBase.paint == null) {
            CheckBoxBase.paint = new Paint(1);
            (CheckBoxBase.eraser = new Paint(1)).setColor(0);
            CheckBoxBase.eraser.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            (CheckBoxBase.backgroundPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
            (CheckBoxBase.checkPaint = new Paint(1)).setStrokeCap(Paint$Cap.ROUND);
            CheckBoxBase.checkPaint.setStyle(Paint$Style.STROKE);
            CheckBoxBase.checkPaint.setStrokeJoin(Paint$Join.ROUND);
        }
        CheckBoxBase.checkPaint.setStrokeWidth((float)AndroidUtilities.dp(1.9f));
        CheckBoxBase.backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.2f));
        this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(this.size), AndroidUtilities.dp(this.size), Bitmap$Config.ARGB_4444);
        this.bitmapCanvas = new Canvas(this.drawBitmap);
    }
    
    private void animateToCheckedState(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.checkAnimator = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { n })).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (animator.equals(CheckBoxBase.this.checkAnimator)) {
                    CheckBoxBase.this.checkAnimator = null;
                }
            }
        });
        this.checkAnimator.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
        this.checkAnimator.setDuration(200L);
        this.checkAnimator.start();
    }
    
    private void cancelCheckAnimator() {
        final ObjectAnimator checkAnimator = this.checkAnimator;
        if (checkAnimator != null) {
            checkAnimator.cancel();
            this.checkAnimator = null;
        }
    }
    
    public void draw(final Canvas canvas) {
        final Bitmap drawBitmap = this.drawBitmap;
        if (drawBitmap == null) {
            return;
        }
        drawBitmap.eraseColor(0);
        final float n = (float)AndroidUtilities.dp(this.size / 2.0f);
        float n2;
        if (this.drawBackgroundAsArc != 0) {
            n2 = n - AndroidUtilities.dp(0.2f);
        }
        else {
            n2 = n;
        }
        final float progress = this.progress;
        final float n3 = 1.0f;
        float n4;
        if (progress >= 0.5f) {
            n4 = 1.0f;
        }
        else {
            n4 = progress / 0.5f;
        }
        final int centerX = this.bounds.centerX();
        final int centerY = this.bounds.centerY();
        if (this.backgroundColorKey != null) {
            if (this.drawUnchecked) {
                CheckBoxBase.paint.setColor((Theme.getServiceMessageColor() & 0xFFFFFF) | 0x28000000);
                CheckBoxBase.backgroundPaint.setColor(Theme.getColor(this.checkColorKey));
            }
            else {
                final Paint backgroundPaint = CheckBoxBase.backgroundPaint;
                String s = this.background2ColorKey;
                if (s == null) {
                    s = this.checkColorKey;
                }
                backgroundPaint.setColor(AndroidUtilities.getOffsetColor(16777215, Theme.getColor(s), this.progress, this.backgroundAlpha));
            }
        }
        else if (this.drawUnchecked) {
            CheckBoxBase.paint.setColor(Color.argb((int)(this.backgroundAlpha * 25.0f), 0, 0, 0));
            CheckBoxBase.backgroundPaint.setColor(AndroidUtilities.getOffsetColor(-1, Theme.getColor(this.checkColorKey), this.progress, this.backgroundAlpha));
        }
        else {
            final Paint backgroundPaint2 = CheckBoxBase.backgroundPaint;
            String s2 = this.background2ColorKey;
            if (s2 == null) {
                s2 = this.checkColorKey;
            }
            backgroundPaint2.setColor(AndroidUtilities.getOffsetColor(16777215, Theme.getColor(s2), this.progress, this.backgroundAlpha));
        }
        if (this.drawUnchecked) {
            canvas.drawCircle((float)centerX, (float)centerY, n, CheckBoxBase.paint);
        }
        CheckBoxBase.paint.setColor(Theme.getColor(this.checkColorKey));
        if (this.drawBackgroundAsArc == 0) {
            canvas.drawCircle((float)centerX, (float)centerY, n, CheckBoxBase.backgroundPaint);
        }
        else {
            final RectF rect = this.rect;
            final float n5 = (float)centerX;
            final float n6 = (float)centerY;
            rect.set(n5 - n2, n6 - n2, n5 + n2, n6 + n2);
            int n7;
            float n8;
            float n9;
            if (this.drawBackgroundAsArc == 1) {
                n7 = -90;
                n8 = -270.0f;
                n9 = this.progress;
            }
            else {
                n7 = 90;
                n8 = 270.0f;
                n9 = this.progress;
            }
            canvas.drawArc(this.rect, (float)n7, (float)(int)(n9 * n8), false, CheckBoxBase.backgroundPaint);
        }
        if (n4 > 0.0f) {
            final float progress2 = this.progress;
            float n10;
            if (progress2 < 0.5f) {
                n10 = 0.0f;
            }
            else {
                n10 = (progress2 - 0.5f) / 0.5f;
            }
            Label_0551: {
                if (!this.drawUnchecked) {
                    final String backgroundColorKey = this.backgroundColorKey;
                    if (backgroundColorKey != null) {
                        CheckBoxBase.paint.setColor(Theme.getColor(backgroundColorKey));
                        break Label_0551;
                    }
                }
                final Paint paint = CheckBoxBase.paint;
                String s3;
                if (this.enabled) {
                    s3 = "checkbox";
                }
                else {
                    s3 = "checkboxDisabled";
                }
                paint.setColor(Theme.getColor(s3));
            }
            Label_0591: {
                if (!this.useDefaultCheck) {
                    final String checkColorKey = this.checkColorKey;
                    if (checkColorKey != null) {
                        CheckBoxBase.checkPaint.setColor(Theme.getColor(checkColorKey));
                        break Label_0591;
                    }
                }
                CheckBoxBase.checkPaint.setColor(Theme.getColor("checkboxCheck"));
            }
            final float n11 = n - AndroidUtilities.dp(0.5f);
            this.bitmapCanvas.drawCircle((float)(this.drawBitmap.getWidth() / 2), (float)(this.drawBitmap.getHeight() / 2), n11, CheckBoxBase.paint);
            this.bitmapCanvas.drawCircle((float)(this.drawBitmap.getWidth() / 2), (float)(this.drawBitmap.getWidth() / 2), n11 * (1.0f - n4), CheckBoxBase.eraser);
            final Bitmap drawBitmap2 = this.drawBitmap;
            canvas.drawBitmap(drawBitmap2, (float)(centerX - drawBitmap2.getWidth() / 2), (float)(centerY - this.drawBitmap.getHeight() / 2), (Paint)null);
            if (n10 != 0.0f) {
                this.path.reset();
                float n12 = n3;
                if (this.drawBackgroundAsArc == 5) {
                    n12 = 0.8f;
                }
                final float n13 = AndroidUtilities.dp(9.0f * n12) * n10;
                final float n14 = AndroidUtilities.dp(n12 * 4.0f) * n10;
                final int dp = AndroidUtilities.dp(1.5f);
                final int dp2 = AndroidUtilities.dp(4.0f);
                final float n15 = (float)Math.sqrt(n14 * n14 / 2.0f);
                final Path path = this.path;
                final float n16 = (float)(centerX - dp);
                final float n17 = (float)(centerY + dp2);
                path.moveTo(n16 - n15, n17 - n15);
                this.path.lineTo(n16, n17);
                final float n18 = (float)Math.sqrt(n13 * n13 / 2.0f);
                this.path.lineTo(n16 + n18, n17 - n18);
                canvas.drawPath(this.path, CheckBoxBase.checkPaint);
            }
        }
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    public boolean isChecked() {
        return this.isChecked;
    }
    
    public void onAttachedToWindow() {
        this.attachedToWindow = true;
    }
    
    public void onDetachedFromWindow() {
        this.attachedToWindow = false;
    }
    
    public void setBackgroundAlpha(final float backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
    }
    
    public void setBounds(final int left, final int top, final int n, final int n2) {
        final Rect bounds = this.bounds;
        bounds.left = left;
        bounds.top = top;
        bounds.right = left + n;
        bounds.bottom = top + n2;
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
    
    public void setColor(final String backgroundColorKey, final String background2ColorKey, final String checkColorKey) {
        this.backgroundColorKey = backgroundColorKey;
        this.background2ColorKey = background2ColorKey;
        this.checkColorKey = checkColorKey;
    }
    
    public void setDrawBackgroundAsArc(final int drawBackgroundAsArc) {
        this.drawBackgroundAsArc = drawBackgroundAsArc;
        if (drawBackgroundAsArc != 4 && drawBackgroundAsArc != 5) {
            if (drawBackgroundAsArc == 3) {
                CheckBoxBase.backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.2f));
            }
            else if (drawBackgroundAsArc != 0) {
                CheckBoxBase.backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.5f));
            }
        }
        else {
            CheckBoxBase.backgroundPaint.setStrokeWidth((float)AndroidUtilities.dp(1.9f));
            if (drawBackgroundAsArc == 5) {
                CheckBoxBase.checkPaint.setStrokeWidth((float)AndroidUtilities.dp(1.5f));
            }
        }
    }
    
    public void setDrawUnchecked(final boolean drawUnchecked) {
        this.drawUnchecked = drawUnchecked;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    @Keep
    public void setProgress(final float n) {
        if (this.progress == n) {
            return;
        }
        this.progress = n;
        if (this.parentView.getParent() != null) {
            ((View)this.parentView.getParent()).invalidate();
        }
        this.parentView.invalidate();
        final ProgressDelegate progressDelegate = this.progressDelegate;
        if (progressDelegate != null) {
            progressDelegate.setProgress(n);
        }
    }
    
    public void setProgressDelegate(final ProgressDelegate progressDelegate) {
        this.progressDelegate = progressDelegate;
    }
    
    public void setSize(final int n) {
        this.size = (float)n;
    }
    
    public void setUseDefaultCheck(final boolean useDefaultCheck) {
        this.useDefaultCheck = useDefaultCheck;
    }
    
    public interface ProgressDelegate
    {
        void setProgress(final float p0);
    }
}
