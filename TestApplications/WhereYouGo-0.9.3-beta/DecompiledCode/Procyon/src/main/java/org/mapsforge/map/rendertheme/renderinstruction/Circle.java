// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.map.rendertheme.renderinstruction;

import org.mapsforge.core.model.Tag;
import java.util.List;
import org.mapsforge.map.rendertheme.RenderCallback;
import org.mapsforge.map.graphics.Paint;

public class Circle implements RenderInstruction
{
    private final Paint fill;
    private final int level;
    private final float radius;
    private float renderRadius;
    private final boolean scaleRadius;
    private final Paint stroke;
    private final float strokeWidth;
    
    Circle(final CircleBuilder circleBuilder) {
        this.fill = circleBuilder.fill;
        this.level = circleBuilder.level;
        this.radius = circleBuilder.radius;
        this.scaleRadius = circleBuilder.scaleRadius;
        this.stroke = circleBuilder.stroke;
        this.strokeWidth = circleBuilder.strokeWidth;
        if (!this.scaleRadius) {
            this.renderRadius = this.radius;
            if (this.stroke != null) {
                this.stroke.setStrokeWidth(this.strokeWidth);
            }
        }
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public void renderNode(final RenderCallback renderCallback, final List<Tag> list) {
        renderCallback.renderPointOfInterestCircle(this.renderRadius, this.fill, this.stroke, this.level);
    }
    
    @Override
    public void renderWay(final RenderCallback renderCallback, final List<Tag> list) {
    }
    
    @Override
    public void scaleStrokeWidth(final float n) {
        if (this.scaleRadius) {
            this.renderRadius = this.radius * n;
            if (this.stroke != null) {
                this.stroke.setStrokeWidth(this.strokeWidth * n);
            }
        }
    }
    
    @Override
    public void scaleTextSize(final float n) {
    }
}
