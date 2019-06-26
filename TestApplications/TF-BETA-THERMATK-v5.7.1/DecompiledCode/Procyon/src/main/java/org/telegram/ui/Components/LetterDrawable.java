// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.FileLog;
import android.text.Layout$Alignment;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Canvas;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.text.StaticLayout;
import android.graphics.RectF;
import android.graphics.Paint;
import android.text.TextPaint;
import android.graphics.drawable.Drawable;

public class LetterDrawable extends Drawable
{
    private static TextPaint namePaint;
    public static Paint paint;
    private RectF rect;
    private StringBuilder stringBuilder;
    private float textHeight;
    private StaticLayout textLayout;
    private float textLeft;
    private float textWidth;
    
    static {
        LetterDrawable.paint = new Paint();
    }
    
    public LetterDrawable() {
        this.rect = new RectF();
        this.stringBuilder = new StringBuilder(5);
        if (LetterDrawable.namePaint == null) {
            LetterDrawable.namePaint = new TextPaint(1);
        }
        LetterDrawable.namePaint.setTextSize((float)AndroidUtilities.dp(28.0f));
        LetterDrawable.paint.setColor(Theme.getColor("sharedMedia_linkPlaceholder"));
        LetterDrawable.namePaint.setColor(Theme.getColor("sharedMedia_linkPlaceholderText"));
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        if (bounds == null) {
            return;
        }
        this.rect.set((float)bounds.left, (float)bounds.top, (float)bounds.right, (float)bounds.bottom);
        canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(4.0f), LetterDrawable.paint);
        canvas.save();
        if (this.textLayout != null) {
            final int width = bounds.width();
            final float n = (float)bounds.left;
            final float n2 = (float)width;
            canvas.translate(n + (n2 - this.textWidth) / 2.0f - this.textLeft, bounds.top + (n2 - this.textHeight) / 2.0f);
            this.textLayout.draw(canvas);
        }
        canvas.restore();
    }
    
    public int getIntrinsicHeight() {
        return 0;
    }
    
    public int getIntrinsicWidth() {
        return 0;
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setBackgroundColor(final int color) {
        LetterDrawable.paint.setColor(color);
    }
    
    public void setColor(final int color) {
        LetterDrawable.namePaint.setColor(color);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    public void setTitle(String upperCase) {
        this.stringBuilder.setLength(0);
        if (upperCase != null && upperCase.length() > 0) {
            this.stringBuilder.append(upperCase.substring(0, 1));
        }
        if (this.stringBuilder.length() > 0) {
            upperCase = this.stringBuilder.toString().toUpperCase();
            try {
                this.textLayout = new StaticLayout((CharSequence)upperCase, LetterDrawable.namePaint, AndroidUtilities.dp(100.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                if (this.textLayout.getLineCount() > 0) {
                    this.textLeft = this.textLayout.getLineLeft(0);
                    this.textWidth = this.textLayout.getLineWidth(0);
                    this.textHeight = (float)this.textLayout.getLineBottom(0);
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        else {
            this.textLayout = null;
        }
    }
}
