package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.rendertheme.RenderCallback;

public class PathText implements RenderInstruction {
   private final Paint fill;
   private final float fontSize;
   private final Paint stroke;
   private final TextKey textKey;

   PathText(PathTextBuilder var1) {
      this.fill = var1.fill;
      this.fontSize = var1.fontSize;
      this.stroke = var1.stroke;
      this.textKey = var1.textKey;
   }

   public void destroy() {
   }

   public void renderNode(RenderCallback var1, List var2) {
   }

   public void renderWay(RenderCallback var1, List var2) {
      String var3 = this.textKey.getValue(var2);
      if (var3 != null) {
         var1.renderWayText(var3, this.fill, this.stroke);
      }

   }

   public void scaleStrokeWidth(float var1) {
   }

   public void scaleTextSize(float var1) {
      this.fill.setTextSize(this.fontSize * var1);
      this.stroke.setTextSize(this.fontSize * var1);
   }
}
