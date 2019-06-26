// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.annotation.Keep;
import android.graphics.Color;
import org.telegram.messenger.FileLog;
import android.graphics.Bitmap$Config;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.View;

public class RadioButton extends View
{
    private static Paint checkedPaint;
    private static Paint eraser;
    private static Paint paint;
    private boolean attachedToWindow;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private ObjectAnimator checkAnimator;
    private int checkedColor;
    private int color;
    private boolean isChecked;
    private float progress;
    private int size;
    
    public RadioButton(final Context context) {
        super(context);
        this.size = AndroidUtilities.dp(16.0f);
        if (RadioButton.paint == null) {
            (RadioButton.paint = new Paint(1)).setStrokeWidth((float)AndroidUtilities.dp(2.0f));
            RadioButton.paint.setStyle(Paint$Style.STROKE);
            RadioButton.checkedPaint = new Paint(1);
            (RadioButton.eraser = new Paint(1)).setColor(0);
            RadioButton.eraser.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
        }
        try {
            this.bitmap = Bitmap.createBitmap(AndroidUtilities.dp((float)this.size), AndroidUtilities.dp((float)this.size), Bitmap$Config.ARGB_4444);
            this.bitmapCanvas = new Canvas(this.bitmap);
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
    }
    
    private void animateToCheckedState(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.checkAnimator = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { n })).setDuration(200L);
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
        this.attachedToWindow = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        final Bitmap bitmap = this.bitmap;
        if (bitmap == null || bitmap.getWidth() != this.getMeasuredWidth()) {
            final Bitmap bitmap2 = this.bitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.bitmap = null;
            }
            try {
                this.bitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Bitmap$Config.ARGB_8888);
                this.bitmapCanvas = new Canvas(this.bitmap);
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
        final float progress = this.progress;
        float n;
        if (progress <= 0.5f) {
            RadioButton.paint.setColor(this.color);
            RadioButton.checkedPaint.setColor(this.color);
            n = this.progress / 0.5f;
        }
        else {
            n = 2.0f - progress / 0.5f;
            final int red = Color.red(this.color);
            final float n2 = (float)(Color.red(this.checkedColor) - red);
            final float n3 = 1.0f - n;
            final int n4 = (int)(n2 * n3);
            final int green = Color.green(this.color);
            final int n5 = (int)((Color.green(this.checkedColor) - green) * n3);
            final int blue = Color.blue(this.color);
            final int rgb = Color.rgb(red + n4, green + n5, blue + (int)((Color.blue(this.checkedColor) - blue) * n3));
            RadioButton.paint.setColor(rgb);
            RadioButton.checkedPaint.setColor(rgb);
        }
        final Bitmap bitmap3 = this.bitmap;
        if (bitmap3 != null) {
            bitmap3.eraseColor(0);
            final float n6 = this.size / 2 - (n + 1.0f) * AndroidUtilities.density;
            this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), n6, RadioButton.paint);
            if (this.progress <= 0.5f) {
                this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), n6 - AndroidUtilities.dp(1.0f), RadioButton.checkedPaint);
                this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (n6 - AndroidUtilities.dp(1.0f)) * (1.0f - n), RadioButton.eraser);
            }
            else {
                this.bitmapCanvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), this.size / 4 + (n6 - AndroidUtilities.dp(1.0f) - this.size / 4) * n, RadioButton.checkedPaint);
            }
            canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, (Paint)null);
        }
    }
    
    public void setBackgroundColor(final int color) {
        this.color = color;
        this.invalidate();
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
    
    public void setCheckedColor(final int checkedColor) {
        this.checkedColor = checkedColor;
        this.invalidate();
    }
    
    public void setColor(final int color, final int checkedColor) {
        this.color = color;
        this.checkedColor = checkedColor;
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
        if (this.size == size) {
            return;
        }
        this.size = size;
    }
}
