package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.rendertheme.RenderCallback;

public class Area implements RenderInstruction {
   private final Paint fill;
   private final int level;
   private final Paint stroke;
   private final float strokeWidth;

   Area(AreaBuilder var1) {
      this.fill = var1.fill;
      this.level = var1.level;
      this.stroke = var1.stroke;
      this.strokeWidth = var1.strokeWidth;
   }

   public void destroy() {
      this.fill.destroy();
      this.stroke.destroy();
   }

   public void renderNode(RenderCallback var1, List var2) {
   }

   public void renderWay(RenderCallback var1, List var2) {
      var1.renderArea(this.fill, this.stroke, this.level);
   }

   public void scaleStrokeWidth(float var1) {
      if (this.stroke != null) {
         this.stroke.setStrokeWidth(this.strokeWidth * var1);
      }

   }

   public void scaleTextSize(float var1) {
   }
}
