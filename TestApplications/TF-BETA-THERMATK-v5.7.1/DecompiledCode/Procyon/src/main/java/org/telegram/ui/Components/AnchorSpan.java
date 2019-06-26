// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class AnchorSpan extends MetricAffectingSpan
{
    private String name;
    
    public AnchorSpan(final String s) {
        this.name = s.toLowerCase();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void updateDrawState(final TextPaint textPaint) {
    }
    
    public void updateMeasureState(final TextPaint textPaint) {
    }
}
