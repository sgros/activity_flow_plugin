// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme;

import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Bitmap;
import java.io.InputStream;

public interface GraphicAdapter
{
    Bitmap decodeStream(final InputStream p0);
    
    int getColor(final Color p0);
    
    Paint getPaint();
    
    int parseColor(final String p0);
    
    public enum Color
    {
        BLACK, 
        CYAN, 
        TRANSPARENT, 
        WHITE;
    }
}
