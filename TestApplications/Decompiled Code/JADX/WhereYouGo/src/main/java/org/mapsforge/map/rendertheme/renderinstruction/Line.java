package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.rendertheme.RenderCallback;

public class Line implements RenderInstruction {
    private final int level;
    private final Paint stroke;
    private final float strokeWidth;

    Line(LineBuilder lineBuilder) {
        this.level = lineBuilder.level;
        this.stroke = lineBuilder.stroke;
        this.strokeWidth = lineBuilder.strokeWidth;
    }

    public void destroy() {
        this.stroke.destroy();
    }

    public void renderNode(RenderCallback renderCallback, List<Tag> list) {
    }

    public void renderWay(RenderCallback renderCallback, List<Tag> list) {
        renderCallback.renderWay(this.stroke, this.level);
    }

    public void scaleStrokeWidth(float scaleFactor) {
        this.stroke.setStrokeWidth(this.strokeWidth * scaleFactor);
    }

    public void scaleTextSize(float scaleFactor) {
    }
}
