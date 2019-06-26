// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TextPaintMarkSpan extends MetricAffectingSpan
{
    private TextPaint textPaint;
    
    public TextPaintMarkSpan(final TextPaint textPaint) {
        this.textPaint = textPaint;
    }
    
    public TextPaint getTextPaint() {
        return this.textPaint;
    }
    
    public void updateDrawState(final TextPaint textPaint) {
        final TextPaint textPaint2 = this.textPaint;
        if (textPaint2 != null) {
            textPaint.setColor(textPaint2.getColor());
            textPaint.setTypeface(this.textPaint.getTypeface());
            textPaint.setFlags(this.textPaint.getFlags());
            textPaint.setTextSize(this.textPaint.getTextSize());
            final TextPaint textPaint3 = this.textPaint;
            textPaint.baselineShift = textPaint3.baselineShift;
            textPaint.bgColor = textPaint3.bgColor;
        }
    }
    
    public void updateMeasureState(final TextPaint textPaint) {
        final TextPaint textPaint2 = this.textPaint;
        if (textPaint2 != null) {
            textPaint.setColor(textPaint2.getColor());
            textPaint.setTypeface(this.textPaint.getTypeface());
            textPaint.setFlags(this.textPaint.getFlags());
            textPaint.setTextSize(this.textPaint.getTextSize());
            final TextPaint textPaint3 = this.textPaint;
            textPaint.baselineShift = textPaint3.baselineShift;
            textPaint.bgColor = textPaint3.bgColor;
        }
    }
}
