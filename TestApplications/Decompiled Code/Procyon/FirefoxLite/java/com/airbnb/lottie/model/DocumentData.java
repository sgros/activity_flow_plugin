// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

public class DocumentData
{
    public final double baselineShift;
    public final int color;
    public final String fontName;
    final int justification;
    final double lineHeight;
    public final double size;
    public final int strokeColor;
    public final boolean strokeOverFill;
    public final double strokeWidth;
    public final String text;
    public final int tracking;
    
    public DocumentData(final String text, final String fontName, final double size, final int justification, final int tracking, final double lineHeight, final double baselineShift, final int color, final int strokeColor, final double strokeWidth, final boolean strokeOverFill) {
        this.text = text;
        this.fontName = fontName;
        this.size = size;
        this.justification = justification;
        this.tracking = tracking;
        this.lineHeight = lineHeight;
        this.baselineShift = baselineShift;
        this.color = color;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.strokeOverFill = strokeOverFill;
    }
    
    @Override
    public int hashCode() {
        final int n = (int)((this.text.hashCode() * 31 + this.fontName.hashCode()) * 31 + this.size);
        final int justification = this.justification;
        final int tracking = this.tracking;
        final long doubleToLongBits = Double.doubleToLongBits(this.lineHeight);
        return (((n * 31 + justification) * 31 + tracking) * 31 + (int)(doubleToLongBits ^ doubleToLongBits >>> 32)) * 31 + this.color;
    }
}
