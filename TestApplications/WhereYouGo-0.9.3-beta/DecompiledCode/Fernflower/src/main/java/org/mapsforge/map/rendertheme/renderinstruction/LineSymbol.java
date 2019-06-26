package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.RenderCallback;

public class LineSymbol implements RenderInstruction {
   private final boolean alignCenter;
   private final Bitmap bitmap;
   private final boolean repeat;

   LineSymbol(LineSymbolBuilder var1) {
      this.alignCenter = var1.alignCenter;
      this.bitmap = var1.bitmap;
      this.repeat = var1.repeat;
   }

   public void destroy() {
      this.bitmap.destroy();
   }

   public void renderNode(RenderCallback var1, List var2) {
   }

   public void renderWay(RenderCallback var1, List var2) {
      var1.renderWaySymbol(this.bitmap, this.alignCenter, this.repeat);
   }

   public void scaleStrokeWidth(float var1) {
   }

   public void scaleTextSize(float var1) {
   }
}
