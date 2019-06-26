// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.Collection;
import java.util.Arrays;
import android.text.Layout$Alignment;
import java.util.List;

public final class WebvttCssStyle
{
    private int backgroundColor;
    private int bold;
    private int fontColor;
    private String fontFamily;
    private float fontSize;
    private int fontSizeUnit;
    private boolean hasBackgroundColor;
    private boolean hasFontColor;
    private int italic;
    private int linethrough;
    private List<String> targetClasses;
    private String targetId;
    private String targetTag;
    private String targetVoice;
    private Layout$Alignment textAlign;
    private int underline;
    
    public WebvttCssStyle() {
        this.reset();
    }
    
    private static int updateScoreForMatch(final int n, final String s, final String anObject, final int n2) {
        if (!s.isEmpty()) {
            int n3 = -1;
            if (n != -1) {
                if (s.equals(anObject)) {
                    n3 = n + n2;
                }
                return n3;
            }
        }
        return n;
    }
    
    public int getBackgroundColor() {
        if (this.hasBackgroundColor) {
            return this.backgroundColor;
        }
        throw new IllegalStateException("Background color not defined.");
    }
    
    public int getFontColor() {
        if (this.hasFontColor) {
            return this.fontColor;
        }
        throw new IllegalStateException("Font color not defined");
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
    
    public int getSpecificityScore(final String s, final String s2, final String[] a, final String s3) {
        if (this.targetId.isEmpty() && this.targetTag.isEmpty() && this.targetClasses.isEmpty() && this.targetVoice.isEmpty()) {
            return s2.isEmpty() ? 1 : 0;
        }
        final int updateScoreForMatch = updateScoreForMatch(updateScoreForMatch(updateScoreForMatch(0, this.targetId, s, 1073741824), this.targetTag, s2, 2), this.targetVoice, s3, 4);
        if (updateScoreForMatch != -1 && Arrays.asList(a).containsAll(this.targetClasses)) {
            return updateScoreForMatch + this.targetClasses.size() * 4;
        }
        return 0;
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
    
    public void reset() {
        this.targetId = "";
        this.targetTag = "";
        this.targetClasses = Collections.emptyList();
        this.targetVoice = "";
        this.fontFamily = null;
        this.hasFontColor = false;
        this.hasBackgroundColor = false;
        this.linethrough = -1;
        this.underline = -1;
        this.bold = -1;
        this.italic = -1;
        this.fontSizeUnit = -1;
        this.textAlign = null;
    }
    
    public WebvttCssStyle setBackgroundColor(final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.hasBackgroundColor = true;
        return this;
    }
    
    public WebvttCssStyle setBold(final boolean bold) {
        this.bold = (bold ? 1 : 0);
        return this;
    }
    
    public WebvttCssStyle setFontColor(final int fontColor) {
        this.fontColor = fontColor;
        this.hasFontColor = true;
        return this;
    }
    
    public WebvttCssStyle setFontFamily(final String s) {
        this.fontFamily = Util.toLowerInvariant(s);
        return this;
    }
    
    public WebvttCssStyle setItalic(final boolean italic) {
        this.italic = (italic ? 1 : 0);
        return this;
    }
    
    public void setTargetClasses(final String[] a) {
        this.targetClasses = Arrays.asList(a);
    }
    
    public void setTargetId(final String targetId) {
        this.targetId = targetId;
    }
    
    public void setTargetTagName(final String targetTag) {
        this.targetTag = targetTag;
    }
    
    public void setTargetVoice(final String targetVoice) {
        this.targetVoice = targetVoice;
    }
    
    public WebvttCssStyle setUnderline(final boolean underline) {
        this.underline = (underline ? 1 : 0);
        return this;
    }
}
