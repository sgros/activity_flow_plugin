// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint.Views;

import android.graphics.Bitmap$Config;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint$Style;
import android.content.Context;
import android.text.TextPaint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.widget.EditText;

public class EditTextOutline extends EditText
{
    private Bitmap mCache;
    private final Canvas mCanvas;
    private final TextPaint mPaint;
    private int mStrokeColor;
    private float mStrokeWidth;
    private boolean mUpdateCachedBitmap;
    
    public EditTextOutline(final Context context) {
        super(context);
        this.mCanvas = new Canvas();
        this.mPaint = new TextPaint();
        this.mStrokeColor = 0;
        this.mUpdateCachedBitmap = true;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint$Style.FILL_AND_STROKE);
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mCache != null && this.mStrokeColor != 0) {
            if (this.mUpdateCachedBitmap) {
                final int measuredWidth = this.getMeasuredWidth();
                final int paddingLeft = this.getPaddingLeft();
                final int paddingRight = this.getPaddingRight();
                final int measuredHeight = this.getMeasuredHeight();
                final String string = this.getText().toString();
                this.mCanvas.setBitmap(this.mCache);
                this.mCanvas.drawColor(0, PorterDuff$Mode.CLEAR);
                float mStrokeWidth = this.mStrokeWidth;
                if (mStrokeWidth <= 0.0f) {
                    mStrokeWidth = (float)Math.ceil(this.getTextSize() / 11.5f);
                }
                this.mPaint.setStrokeWidth(mStrokeWidth);
                this.mPaint.setColor(this.mStrokeColor);
                this.mPaint.setTextSize(this.getTextSize());
                this.mPaint.setTypeface(this.getTypeface());
                this.mPaint.setStyle(Paint$Style.FILL_AND_STROKE);
                final StaticLayout staticLayout = new StaticLayout((CharSequence)string, this.mPaint, measuredWidth - paddingLeft - paddingRight, Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
                this.mCanvas.save();
                this.mCanvas.translate((float)this.getPaddingLeft(), (measuredHeight - this.getPaddingTop() - this.getPaddingBottom() - staticLayout.getHeight()) / 2.0f + this.getPaddingTop());
                staticLayout.draw(this.mCanvas);
                this.mCanvas.restore();
                this.mUpdateCachedBitmap = false;
            }
            canvas.drawBitmap(this.mCache, 0.0f, 0.0f, (Paint)this.mPaint);
        }
        super.onDraw(canvas);
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n > 0 && n2 > 0) {
            this.mUpdateCachedBitmap = true;
            this.mCache = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
        }
        else {
            this.mCache = null;
        }
    }
    
    protected void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        super.onTextChanged(charSequence, n, n2, n3);
        this.mUpdateCachedBitmap = true;
    }
    
    public void setStrokeColor(final int mStrokeColor) {
        this.mStrokeColor = mStrokeColor;
        this.mUpdateCachedBitmap = true;
        this.invalidate();
    }
    
    public void setStrokeWidth(final float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        this.mUpdateCachedBitmap = true;
        this.invalidate();
    }
}
