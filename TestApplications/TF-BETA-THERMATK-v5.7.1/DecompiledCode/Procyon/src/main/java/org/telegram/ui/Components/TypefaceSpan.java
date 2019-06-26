// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextPaint;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Typeface;
import android.text.style.MetricAffectingSpan;

public class TypefaceSpan extends MetricAffectingSpan
{
    private int color;
    private int textSize;
    private Typeface typeface;
    
    public TypefaceSpan(final Typeface typeface) {
        this.typeface = typeface;
    }
    
    public TypefaceSpan(final Typeface typeface, final int textSize) {
        this.typeface = typeface;
        this.textSize = textSize;
    }
    
    public TypefaceSpan(final Typeface typeface, final int textSize, final int color) {
        this.typeface = typeface;
        if (textSize > 0) {
            this.textSize = textSize;
        }
        this.color = color;
    }
    
    public Typeface getTypeface() {
        return this.typeface;
    }
    
    public boolean isBold() {
        return this.typeface == AndroidUtilities.getTypeface("fonts/rmedium.ttf");
    }
    
    public boolean isItalic() {
        return this.typeface == AndroidUtilities.getTypeface("fonts/ritalic.ttf");
    }
    
    public boolean isMono() {
        return this.typeface == Typeface.MONOSPACE;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void updateDrawState(final TextPaint textPaint) {
        final Typeface typeface = this.typeface;
        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }
        final int textSize = this.textSize;
        if (textSize != 0) {
            textPaint.setTextSize((float)textSize);
        }
        final int color = this.color;
        if (color != 0) {
            textPaint.setColor(color);
        }
        textPaint.setFlags(textPaint.getFlags() | 0x80);
    }
    
    public void updateMeasureState(final TextPaint textPaint) {
        final Typeface typeface = this.typeface;
        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }
        final int textSize = this.textSize;
        if (textSize != 0) {
            textPaint.setTextSize((float)textSize);
        }
        textPaint.setFlags(textPaint.getFlags() | 0x80);
    }
}
