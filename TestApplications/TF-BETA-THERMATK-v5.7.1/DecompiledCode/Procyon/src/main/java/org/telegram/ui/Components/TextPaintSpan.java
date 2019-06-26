// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TextPaintSpan extends MetricAffectingSpan
{
    private TextPaint textPaint;
    
    public TextPaintSpan(final TextPaint textPaint) {
        this.textPaint = textPaint;
    }
    
    public void updateDrawState(final TextPaint textPaint) {
        textPaint.setColor(this.textPaint.getColor());
        textPaint.setTypeface(this.textPaint.getTypeface());
        textPaint.setFlags(this.textPaint.getFlags());
        textPaint.setTextSize(this.textPaint.getTextSize());
        final TextPaint textPaint2 = this.textPaint;
        textPaint.baselineShift = textPaint2.baselineShift;
        textPaint.bgColor = textPaint2.bgColor;
    }
    
    public void updateMeasureState(final TextPaint textPaint) {
        textPaint.setColor(this.textPaint.getColor());
        textPaint.setTypeface(this.textPaint.getTypeface());
        textPaint.setFlags(this.textPaint.getFlags());
        textPaint.setTextSize(this.textPaint.getTextSize());
        final TextPaint textPaint2 = this.textPaint;
        textPaint.baselineShift = textPaint2.baselineShift;
        textPaint.bgColor = textPaint2.bgColor;
    }
}
