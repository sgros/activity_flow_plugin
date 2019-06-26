// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.ttml;

final class TtmlRegion
{
    public final String id;
    public final float line;
    public final int lineAnchor;
    public final int lineType;
    public final float position;
    public final float textSize;
    public final int textSizeType;
    public final float width;
    
    public TtmlRegion(final String s) {
        this(s, Float.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }
    
    public TtmlRegion(final String id, final float position, final float line, final int lineType, final int lineAnchor, final float width, final int textSizeType, final float textSize) {
        this.id = id;
        this.position = position;
        this.line = line;
        this.lineType = lineType;
        this.lineAnchor = lineAnchor;
        this.width = width;
        this.textSizeType = textSizeType;
        this.textSize = textSize;
    }
}
