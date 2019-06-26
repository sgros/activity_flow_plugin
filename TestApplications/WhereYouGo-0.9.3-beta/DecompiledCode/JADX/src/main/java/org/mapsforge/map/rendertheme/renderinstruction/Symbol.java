package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.RenderCallback;

public class Symbol implements RenderInstruction {
    private final Bitmap bitmap;

    Symbol(SymbolBuilder symbolBuilder) {
        this.bitmap = symbolBuilder.bitmap;
    }

    public void destroy() {
        this.bitmap.destroy();
    }

    public void renderNode(RenderCallback renderCallback, List<Tag> list) {
        renderCallback.renderPointOfInterestSymbol(this.bitmap);
    }

    public void renderWay(RenderCallback renderCallback, List<Tag> list) {
        renderCallback.renderAreaSymbol(this.bitmap);
    }

    public void scaleStrokeWidth(float scaleFactor) {
    }

    public void scaleTextSize(float scaleFactor) {
    }
}
