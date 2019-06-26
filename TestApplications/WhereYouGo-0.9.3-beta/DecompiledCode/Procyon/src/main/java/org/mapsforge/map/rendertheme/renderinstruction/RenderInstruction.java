// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.renderinstruction;

import org.mapsforge.core.model.Tag;
import java.util.List;
import org.mapsforge.map.rendertheme.RenderCallback;

public interface RenderInstruction
{
    void destroy();
    
    void renderNode(final RenderCallback p0, final List<Tag> p1);
    
    void renderWay(final RenderCallback p0, final List<Tag> p1);
    
    void scaleStrokeWidth(final float p0);
    
    void scaleTextSize(final float p0);
}
