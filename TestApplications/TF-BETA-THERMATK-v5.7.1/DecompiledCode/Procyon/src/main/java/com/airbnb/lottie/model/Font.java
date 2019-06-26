// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

public class Font
{
    private final float ascent;
    private final String family;
    private final String name;
    private final String style;
    
    public Font(final String family, final String name, final String style, final float ascent) {
        this.family = family;
        this.name = name;
        this.style = style;
        this.ascent = ascent;
    }
    
    public String getFamily() {
        return this.family;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getStyle() {
        return this.style;
    }
}
