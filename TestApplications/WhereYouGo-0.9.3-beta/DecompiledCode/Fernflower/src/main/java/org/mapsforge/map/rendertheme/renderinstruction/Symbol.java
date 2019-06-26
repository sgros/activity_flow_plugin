package org.mapsforge.map.rendertheme.renderinstruction;

import java.util.List;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.RenderCallback;

public class Symbol implements RenderInstruction {
   private final Bitmap bitmap;

   Symbol(SymbolBuilder var1) {
      this.bitmap = var1.bitmap;
   }

   public void destroy() {
      this.bitmap.destroy();
   }

   public void renderNode(RenderCallback var1, List var2) {
      var1.renderPointOfInterestSymbol(this.bitmap);
   }

   public void renderWay(RenderCallback var1, List var2) {
      var1.renderAreaSymbol(this.bitmap);
   }

   public void scaleStrokeWidth(float var1) {
   }

   public void scaleTextSize(float var1) {
   }
}
