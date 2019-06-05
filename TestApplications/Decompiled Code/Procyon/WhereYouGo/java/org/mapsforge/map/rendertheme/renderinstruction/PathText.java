// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.renderinstruction;

import org.mapsforge.core.model.Tag;
import java.util.List;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.graphics.Paint;

public class PathText implements RenderInstruction
{
    private final Paint fill;
    private final float fontSize;
    private final Paint stroke;
    private final TextKey textKey;
    
    PathText(final PathTextBuilder pathTextBuilder) {
        this.fill = pathTextBuilder.fill;
        this.fontSize = pathTextBuilder.fontSize;
        this.stroke = pathTextBuilder.stroke;
        this.textKey = pathTextBuilder.textKey;
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public void renderNode(final RenderCallback renderCallback, final List<Tag> list) {
    }
    
    @Override
    public void renderWay(final RenderCallback renderCallback, final List<Tag> list) {
        final String value = this.textKey.getValue(list);
        if (value != null) {
            renderCallback.renderWayText(value, this.fill, this.stroke);
        }
    }
    
    @Override
    public void scaleStrokeWidth(final float n) {
    }
    
    @Override
    public void scaleTextSize(final float n) {
        this.fill.setTextSize(this.fontSize * n);
        this.stroke.setTextSize(this.fontSize * n);
    }
}
