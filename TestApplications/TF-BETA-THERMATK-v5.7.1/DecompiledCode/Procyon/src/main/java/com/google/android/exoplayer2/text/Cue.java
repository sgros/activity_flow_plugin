// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import android.text.Layout$Alignment;
import android.graphics.Bitmap;

public class Cue
{
    public final Bitmap bitmap;
    public final float bitmapHeight;
    public final float line;
    public final int lineAnchor;
    public final int lineType;
    public final float position;
    public final int positionAnchor;
    public final float size;
    public final CharSequence text;
    public final Layout$Alignment textAlignment;
    public final float textSize;
    public final int textSizeType;
    public final int windowColor;
    public final boolean windowColorSet;
    
    public Cue(final Bitmap bitmap, final float n, final int n2, final float n3, final int n4, final float n5, final float n6) {
        this(null, null, bitmap, n3, 0, n4, n, n2, Integer.MIN_VALUE, Float.MIN_VALUE, n5, n6, false, -16777216);
    }
    
    public Cue(final CharSequence charSequence) {
        this(charSequence, null, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }
    
    public Cue(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final float n, final int n2, final int n3, final float n4, final int n5, final float n6) {
        this(charSequence, layout$Alignment, n, n2, n3, n4, n5, n6, false, -16777216);
    }
    
    public Cue(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final float n, final int n2, final int n3, final float n4, final int n5, final float n6, final int n7, final float n8) {
        this(charSequence, layout$Alignment, null, n, n2, n3, n4, n5, n7, n8, n6, Float.MIN_VALUE, false, -16777216);
    }
    
    public Cue(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final float n, final int n2, final int n3, final float n4, final int n5, final float n6, final boolean b, final int n7) {
        this(charSequence, layout$Alignment, null, n, n2, n3, n4, n5, Integer.MIN_VALUE, Float.MIN_VALUE, n6, Float.MIN_VALUE, b, n7);
    }
    
    private Cue(final CharSequence text, final Layout$Alignment textAlignment, final Bitmap bitmap, final float line, final int lineType, final int lineAnchor, final float position, final int positionAnchor, final int textSizeType, final float textSize, final float size, final float bitmapHeight, final boolean windowColorSet, final int windowColor) {
        this.text = text;
        this.textAlignment = textAlignment;
        this.bitmap = bitmap;
        this.line = line;
        this.lineType = lineType;
        this.lineAnchor = lineAnchor;
        this.position = position;
        this.positionAnchor = positionAnchor;
        this.size = size;
        this.bitmapHeight = bitmapHeight;
        this.windowColorSet = windowColorSet;
        this.windowColor = windowColor;
        this.textSizeType = textSizeType;
        this.textSize = textSize;
    }
}
