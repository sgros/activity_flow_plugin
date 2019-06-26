// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.graphics;

public interface Paint
{
    void destroy();
    
    int getColor();
    
    int getTextHeight(final String p0);
    
    int getTextWidth(final String p0);
    
    void setBitmapShader(final Bitmap p0);
    
    void setColor(final int p0);
    
    void setDashPathEffect(final float[] p0);
    
    void setStrokeCap(final Cap p0);
    
    void setStrokeWidth(final float p0);
    
    void setStyle(final Style p0);
    
    void setTextAlign(final Align p0);
    
    void setTextSize(final float p0);
    
    void setTypeface(final FontFamily p0, final FontStyle p1);
}
