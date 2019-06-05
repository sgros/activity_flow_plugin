package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.rendertheme.RenderCallback;

public class Caption implements RenderInstruction {
    /* renamed from: dy */
    private final float f85dy;
    private final Paint fill;
    private final float fontSize;
    private final Paint stroke;
    private final TextKey textKey;

    Caption(CaptionBuilder captionBuilder) {
        this.f85dy = captionBuilder.f72dy;
        this.fill = captionBuilder.fill;
        this.fontSize = captionBuilder.fontSize;
        this.stroke = captionBuilder.stroke;
        this.textKey = captionBuilder.textKey;
    }

    public void destroy() {
    }

    public void renderNode(RenderCallback renderCallback, List<Tag> tags) {
        String caption = this.textKey.getValue(tags);
        if (caption != null) {
            renderCallback.renderPointOfInterestCaption(caption, this.f85dy, this.fill, this.stroke);
        }
    }

    public void renderWay(RenderCallback renderCallback, List<Tag> tags) {
        String caption = this.textKey.getValue(tags);
        if (caption != null) {
            renderCallback.renderAreaCaption(caption, this.f85dy, this.fill, this.stroke);
        }
    }

    public void scaleStrokeWidth(float scaleFactor) {
    }

    public void scaleTextSize(float scaleFactor) {
        this.fill.setTextSize(this.fontSize * scaleFactor);
        this.stroke.setTextSize(this.fontSize * scaleFactor);
    }
}
