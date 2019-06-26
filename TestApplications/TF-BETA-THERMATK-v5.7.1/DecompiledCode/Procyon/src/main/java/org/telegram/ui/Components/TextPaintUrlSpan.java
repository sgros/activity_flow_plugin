// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TextPaintUrlSpan extends MetricAffectingSpan
{
    private int color;
    private String currentUrl;
    private TextPaint textPaint;
    private int textSize;
    
    public TextPaintUrlSpan(final TextPaint textPaint, final String currentUrl) {
        this.textPaint = textPaint;
        this.currentUrl = currentUrl;
    }
    
    public TextPaint getTextPaint() {
        return this.textPaint;
    }
    
    public String getUrl() {
        return this.currentUrl;
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
