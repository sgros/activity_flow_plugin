package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.map.rendertheme.RenderCallback;

public interface RenderInstruction {
   void destroy();

   void renderNode(RenderCallback var1, List var2);

   void renderWay(RenderCallback var1, List var2);

   void scaleStrokeWidth(float var1);

   void scaleTextSize(float var1);
}
