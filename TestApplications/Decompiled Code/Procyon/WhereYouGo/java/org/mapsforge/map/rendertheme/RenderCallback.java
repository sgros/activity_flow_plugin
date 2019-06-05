// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme;

import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

public interface RenderCallback
{
    void renderArea(final Paint p0, final Paint p1, final int p2);
    
    void renderAreaCaption(final String p0, final float p1, final Paint p2, final Paint p3);
    
    void renderAreaSymbol(final Bitmap p0);
    
    void renderPointOfInterestCaption(final String p0, final float p1, final Paint p2, final Paint p3);
    
    void renderPointOfInterestCircle(final float p0, final Paint p1, final Paint p2, final int p3);
    
    void renderPointOfInterestSymbol(final Bitmap p0);
    
    void renderWay(final Paint p0, final int p1);
    
    void renderWaySymbol(final Bitmap p0, final boolean p1, final boolean p2);
    
    void renderWayText(final String p0, final Paint p1, final Paint p2);
}
