// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ttml;

import com.google.android.exoplayer2.util.Assertions;
import android.text.Layout$Alignment;

final class TtmlStyle
{
    private int backgroundColor;
    private int bold;
    private int fontColor;
    private String fontFamily;
    private float fontSize;
    private int fontSizeUnit;
    private boolean hasBackgroundColor;
    private boolean hasFontColor;
    private String id;
    private TtmlStyle inheritableStyle;
    private int italic;
    private int linethrough;
    private Layout$Alignment textAlign;
    private int underline;
    
    public TtmlStyle() {
        this.linethrough = -1;
        this.underline = -1;
        this.bold = -1;
        this.italic = -1;
        this.fontSizeUnit = -1;
    }
    
    private TtmlStyle inherit(final TtmlStyle ttmlStyle, final boolean b) {
        if (ttmlStyle != null) {
            if (!this.hasFontColor && ttmlStyle.hasFontColor) {
                this.setFontColor(ttmlStyle.fontColor);
            }
            if (this.bold == -1) {
                this.bold = ttmlStyle.bold;
            }
            if (this.italic == -1) {
                this.italic = ttmlStyle.italic;
            }
            if (this.fontFamily == null) {
                this.fontFamily = ttmlStyle.fontFamily;
            }
            if (this.linethrough == -1) {
                this.linethrough = ttmlStyle.linethrough;
            }
            if (this.underline == -1) {
                this.underline = ttmlStyle.underline;
            }
            if (this.textAlign == null) {
                this.textAlign = ttmlStyle.textAlign;
            }
            if (this.fontSizeUnit == -1) {
                this.fontSizeUnit = ttmlStyle.fontSizeUnit;
                this.fontSize = ttmlStyle.fontSize;
            }
            if (b && !this.hasBackgroundColor && ttmlStyle.hasBackgroundColor) {
                this.setBackgroundColor(ttmlStyle.backgroundColor);
            }
        }
        return this;
    }
    
    public TtmlStyle chain(final TtmlStyle ttmlStyle) {
        this.inherit(ttmlStyle, true);
        return this;
    }
    
    public int getBackgroundColor() {
        if (this.hasBackgroundColor) {
            return this.backgroundColor;
        }
        throw new IllegalStateException("Background color has not been defined.");
    }
    
    public int getFontColor() {
        if (this.hasFontColor) {
            return this.fontColor;
        }
        throw new IllegalStateException("Font color has not been defined.");
    }
    
    public String getFontFamily() {
        return this.fontFamily;
    }
    
    public float getFontSize() {
        return this.fontSize;
    }
    
    public int getFontSizeUnit() {
        return this.fontSizeUnit;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getStyle() {
        if (this.bold == -1 && this.italic == -1) {
            return -1;
        }
        final int bold = this.bold;
        int n = false ? 1 : 0;
        final boolean b = bold == 1;
        if (this.italic == 1) {
            n = 2;
        }
        return (b ? 1 : 0) | n;
    }
    
    public Layout$Alignment getTextAlign() {
        return this.textAlign;
    }
    
    public boolean hasBackgroundColor() {
        return this.hasBackgroundColor;
    }
    
    public boolean hasFontColor() {
        return this.hasFontColor;
    }
    
    public boolean isLinethrough() {
        final int linethrough = this.linethrough;
        boolean b = true;
        if (linethrough != 1) {
            b = false;
        }
        return b;
    }
    
    public boolean isUnderline() {
        final int underline = this.underline;
        boolean b = true;
        if (underline != 1) {
            b = false;
        }
        return b;
    }
    
    public TtmlStyle setBackgroundColor(final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.hasBackgroundColor = true;
        return this;
    }
    
    public TtmlStyle setBold(final boolean bold) {
        Assertions.checkState(this.inheritableStyle == null);
        this.bold = (bold ? 1 : 0);
        return this;
    }
    
    public TtmlStyle setFontColor(final int fontColor) {
        Assertions.checkState(this.inheritableStyle == null);
        this.fontColor = fontColor;
        this.hasFontColor = true;
        return this;
    }
    
    public TtmlStyle setFontFamily(final String fontFamily) {
        Assertions.checkState(this.inheritableStyle == null);
        this.fontFamily = fontFamily;
        return this;
    }
    
    public TtmlStyle setFontSize(final float fontSize) {
        this.fontSize = fontSize;
        return this;
    }
    
    public TtmlStyle setFontSizeUnit(final int fontSizeUnit) {
        this.fontSizeUnit = fontSizeUnit;
        return this;
    }
    
    public TtmlStyle setId(final String id) {
        this.id = id;
        return this;
    }
    
    public TtmlStyle setItalic(final boolean italic) {
        Assertions.checkState(this.inheritableStyle == null);
        this.italic = (italic ? 1 : 0);
        return this;
    }
    
    public TtmlStyle setLinethrough(final boolean linethrough) {
        Assertions.checkState(this.inheritableStyle == null);
        this.linethrough = (linethrough ? 1 : 0);
        return this;
    }
    
    public TtmlStyle setTextAlign(final Layout$Alignment textAlign) {
        this.textAlign = textAlign;
        return this;
    }
    
    public TtmlStyle setUnderline(final boolean underline) {
        Assertions.checkState(this.inheritableStyle == null);
        this.underline = (underline ? 1 : 0);
        return this;
    }
}
