package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.core.model.Tag;
import org.mapsforge.map.rendertheme.RenderCallback;

public interface RenderInstruction {
    void destroy();

    void renderNode(RenderCallback renderCallback, List<Tag> list);

    void renderWay(RenderCallback renderCallback, List<Tag> list);

    void scaleStrokeWidth(float f);

    void scaleTextSize(float f);
}
