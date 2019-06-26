package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.rendertheme.RenderCallback;

public class PathText implements RenderInstruction {
    private final Paint fill;
    private final float fontSize;
    private final Paint stroke;
    private final TextKey textKey;

    PathText(PathTextBuilder pathTextBuilder) {
        this.fill = pathTextBuilder.fill;
        this.fontSize = pathTextBuilder.fontSize;
        this.stroke = pathTextBuilder.stroke;
        this.textKey = pathTextBuilder.textKey;
    }

    public void destroy() {
    }

    public void renderNode(RenderCallback renderCallback, List<Tag> list) {
    }

    public void renderWay(RenderCallback renderCallback, List<Tag> tags) {
        String caption = this.textKey.getValue(tags);
        if (caption != null) {
            renderCallback.renderWayText(caption, this.fill, this.stroke);
        }
    }

    public void scaleStrokeWidth(float scaleFactor) {
    }

    public void scaleTextSize(float scaleFactor) {
        this.fill.setTextSize(this.fontSize * scaleFactor);
        this.stroke.setTextSize(this.fontSize * scaleFactor);
    }
}
