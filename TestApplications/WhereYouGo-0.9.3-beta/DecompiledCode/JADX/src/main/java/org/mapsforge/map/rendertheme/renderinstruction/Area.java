package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.rendertheme.RenderCallback;

public class Area implements RenderInstruction {
    private final Paint fill;
    private final int level;
    private final Paint stroke;
    private final float strokeWidth;

    Area(AreaBuilder areaBuilder) {
        this.fill = areaBuilder.fill;
        this.level = areaBuilder.level;
        this.stroke = areaBuilder.stroke;
        this.strokeWidth = areaBuilder.strokeWidth;
    }

    public void destroy() {
        this.fill.destroy();
        this.stroke.destroy();
    }

    public void renderNode(RenderCallback renderCallback, List<Tag> list) {
    }

    public void renderWay(RenderCallback renderCallback, List<Tag> list) {
        renderCallback.renderArea(this.fill, this.stroke, this.level);
    }

    public void scaleStrokeWidth(float scaleFactor) {
        if (this.stroke != null) {
            this.stroke.setStrokeWidth(this.strokeWidth * scaleFactor);
        }
    }

    public void scaleTextSize(float scaleFactor) {
    }
}
