package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.RenderCallback;

public class LineSymbol implements RenderInstruction {
    private final boolean alignCenter;
    private final Bitmap bitmap;
    private final boolean repeat;

    LineSymbol(LineSymbolBuilder lineSymbolBuilder) {
        this.alignCenter = lineSymbolBuilder.alignCenter;
        this.bitmap = lineSymbolBuilder.bitmap;
        this.repeat = lineSymbolBuilder.repeat;
    }

    public void destroy() {
        this.bitmap.destroy();
    }

    public void renderNode(RenderCallback renderCallback, List<Tag> list) {
    }

    public void renderWay(RenderCallback renderCallback, List<Tag> list) {
        renderCallback.renderWaySymbol(this.bitmap, this.alignCenter, this.repeat);
    }

    public void scaleStrokeWidth(float scaleFactor) {
    }

    public void scaleTextSize(float scaleFactor) {
    }
}
