// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import org.telegram.messenger.LocaleController;
import android.graphics.Paint$Style;
import org.telegram.messenger.AndroidUtilities;
import android.text.TextPaint;
import android.graphics.RectF;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class ScamDrawable extends Drawable
{
    private Paint paint;
    private RectF rect;
    private String text;
    private TextPaint textPaint;
    private int textWidth;
    
    public ScamDrawable(final int n) {
        this.rect = new RectF();
        this.paint = new Paint(1);
        (this.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp((float)n));
        this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.paint.setStyle(Paint$Style.STROKE);
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(1.0f));
        this.text = LocaleController.getString("ScamMessage", 2131560635);
        this.textWidth = (int)Math.ceil(this.textPaint.measureText(this.text));
    }
    
    public void checkText() {
        final String string = LocaleController.getString("ScamMessage", 2131560635);
        if (!string.equals(this.text)) {
            this.text = string;
            this.textWidth = (int)Math.ceil(this.textPaint.measureText(this.text));
        }
    }
    
    public void draw(final Canvas canvas) {
        this.rect.set(this.getBounds());
        canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), this.paint);
        canvas.drawText(this.text, this.rect.left + AndroidUtilities.dp(5.0f), this.rect.top + AndroidUtilities.dp(12.0f), (Paint)this.textPaint);
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(16.0f);
    }
    
    public int getIntrinsicWidth() {
        return this.textWidth + AndroidUtilities.dp(10.0f);
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColor(final int n) {
        this.textPaint.setColor(n);
        this.paint.setColor(n);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}
