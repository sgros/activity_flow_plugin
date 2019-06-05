// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model;

import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.List;

public class FontCharacter
{
    private final char character;
    private final String fontFamily;
    private final List<ShapeGroup> shapes;
    private final double size;
    private final String style;
    private final double width;
    
    public FontCharacter(final List<ShapeGroup> shapes, final char c, final double size, final double width, final String style, final String fontFamily) {
        this.shapes = shapes;
        this.character = c;
        this.size = size;
        this.width = width;
        this.style = style;
        this.fontFamily = fontFamily;
    }
    
    public static int hashFor(final char c, final String s, final String s2) {
        return (('\0' + c) * 31 + s.hashCode()) * 31 + s2.hashCode();
    }
    
    public List<ShapeGroup> getShapes() {
        return this.shapes;
    }
    
    public double getWidth() {
        return this.width;
    }
    
    @Override
    public int hashCode() {
        return hashFor(this.character, this.fontFamily, this.style);
    }
}
